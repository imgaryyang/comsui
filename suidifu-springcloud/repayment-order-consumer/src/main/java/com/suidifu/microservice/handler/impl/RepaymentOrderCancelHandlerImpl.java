package com.suidifu.microservice.handler.impl;

import com.suidifu.giotto.exception.GiottoException;
import com.suidifu.giotto.handler.FastHandler;
import com.suidifu.giotto.keyenum.FastAssetSetKeyEnum;
import com.suidifu.giotto.keyenum.FastRepaymentOrderItemKeyEnum;
import com.suidifu.giotto.model.FastAssetSet;
import com.suidifu.giotto.model.FastRepaymentOrderItem;
import com.suidifu.owlman.microservice.handler.RepaymentOrderCancelHandler;
import com.zufangbao.gluon.logspec.GloableLogSpec;
import com.zufangbao.sun.entity.repayment.order.RepaymentBusinessType;
import com.zufangbao.sun.yunxin.entity.repayment.RepaymentOrderLapseParam;
import com.zufangbao.sun.yunxin.exception.AssetSetOrderPaymentStatusLockFailedException;
import com.zufangbao.sun.yunxin.handler.RepaymentOrderItemHandler;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.stereotype.Component;

/**
 * Created by zhusy on 2017/6/12.
 */
@Log4j2
@Component("dstJobRepaymentOrderCancel")
public class RepaymentOrderCancelHandlerImpl implements RepaymentOrderCancelHandler {
    @Resource
    private RepaymentOrderItemHandler repaymentOrderItemHandler;

    @Resource
    private FastHandler fastHandler;

    @Override
    public Map<String, String> criticalMarker(List<RepaymentOrderLapseParam> repaymentOrderLapseParamList) throws GiottoException {
        Map<String, String> criticalMarker = new HashMap<>();
        for (RepaymentOrderLapseParam repaymentOrderLapseParam : repaymentOrderLapseParamList) {
            FastRepaymentOrderItem fastRepaymentOrderItem = fastHandler.getByKey(
                    FastRepaymentOrderItemKeyEnum.ORDER_DETAIL_UUID,
                    repaymentOrderLapseParam.getRepaymentOrderItemUuid(),
                    FastRepaymentOrderItem.class, true);
            if (fastRepaymentOrderItem != null) {
                criticalMarker.put(fastRepaymentOrderItem.getOrderDetailUuid(),
                        fastRepaymentOrderItem.getContractUuid());
            }
        }

        return criticalMarker;
    }

    @Override
    public boolean deleteRepaymentOrderItem(List<RepaymentOrderLapseParam> repaymentOrderLapseParamList) {
        boolean result = true;
        log.info("repayment_order deleteRepaymentOrderItem start. repaymentOrderLapseParamList size[{}]",
                repaymentOrderLapseParamList.size());

        for (RepaymentOrderLapseParam repaymentOrderLapseParam : repaymentOrderLapseParamList) {
            try {
                boolean isCurrentValid = lapseRepaymentOrderItemsTryTimes(repaymentOrderLapseParam);
                result = result && isCurrentValid;
            } catch (Exception e) {
                log.error("repayment_order deleteRepaymentOrderItem error,repaymentOrderItemUuid[{}],msg:{}",
                        repaymentOrderLapseParam.getRepaymentOrderItemUuid(), ExceptionUtils.getFullStackTrace(e));
                result = false;
            }
        }

        log.info("repayment_order deleteRepaymentOrderItem end. repaymentOrderLapseParamList size[{}].result[{}]",
                repaymentOrderLapseParamList.size(), result);
        return result;
    }

    private boolean lapseRepaymentOrderItemsTryTimes(RepaymentOrderLapseParam repaymentOrderLapseParam) {
        boolean currentResult = false;

        int tryTimes = 3;
        while (tryTimes > 0) {
            try {
                log.info(GloableLogSpec.AuditLogHeaderSpec() +
                                GloableLogSpec.REPAYMENT_ORDER_FUNCTION_POINT.START_LAPSE +
                                "RepaymentOrderUuid:[{}].RepaymentOrderDetailUuid:[{}],try_times[{}]",
                        repaymentOrderLapseParam.getRepaymentOrderUuid(),
                        repaymentOrderLapseParam.getRepaymentOrderItemUuid(),
                        tryTimes);
                refreshCache(repaymentOrderLapseParam);
                currentResult = repaymentOrderItemHandler.lapse_repayment_order_item(true,
                        repaymentOrderLapseParam.getRepaymentOrderItemUuid(),
                        repaymentOrderLapseParam.getRepaymentBusinessTypeEnum());
                tryTimes = 0;

                log.info(GloableLogSpec.AuditLogHeaderSpec() +
                                GloableLogSpec.REPAYMENT_ORDER_FUNCTION_POINT.END_LAPSE +
                                "RepaymentOrderUuid:[{}].RepaymentOrderDetailUuid:[{}],try_times[{}]",
                        repaymentOrderLapseParam.getRepaymentOrderUuid(),
                        repaymentOrderLapseParam.getRepaymentOrderItemUuid(),
                        tryTimes);

            } catch (AssetSetOrderPaymentStatusLockFailedException | GiottoException assetLockException) {
                tryTimes--;
                log.error(GloableLogSpec.AuditLogHeaderSpec() +
                                GloableLogSpec.REPAYMENT_ORDER_FUNCTION_POINT.FAIL_LAPSE +
                                "RepaymentOrderUuid:[{}].RepaymentOrderDetailUuid:[{}]," +
                                "with full stack trace [{}],try_times[{}]",
                        repaymentOrderLapseParam.getRepaymentOrderUuid(),
                        repaymentOrderLapseParam.getRepaymentOrderItemUuid(),
                        ExceptionUtils.getFullStackTrace(assetLockException),
                        tryTimes);
            }
        }
        return currentResult;
    }

    private void refreshCache(RepaymentOrderLapseParam repaymentOrderLapseParam) throws GiottoException {
        if (repaymentOrderLapseParam.getRepaymentBusinessTypeEnum() == null
                || repaymentOrderLapseParam.getRepaymentBusinessTypeEnum() == RepaymentBusinessType.REPURCHASE) {
            return;
        }
        String repaymentOrderItemUuid = repaymentOrderLapseParam.getRepaymentOrderItemUuid();
        FastRepaymentOrderItem fastRepaymentOrderItem = fastHandler.getByKey(FastRepaymentOrderItemKeyEnum.ORDER_DETAIL_UUID, repaymentOrderItemUuid, FastRepaymentOrderItem.class, false);
        if (fastRepaymentOrderItem == null) {
            return;
        }
        // 强制刷新assetSet
        FastAssetSet fastAssetSet = fastHandler.getByKey(FastAssetSetKeyEnum.SINGLE_LOAN_CONTRACT_NO,
                fastRepaymentOrderItem.getRepaymentBusinessNo(),
                FastAssetSet.class, false);
        if (fastAssetSet == null) {
            return;
        }
        String assetSetUuid = fastAssetSet.getAssetUuid();
        fastHandler.getByKey(FastRepaymentOrderItemKeyEnum.REPAYMENT_BUSINESS_UUID, assetSetUuid,
                FastRepaymentOrderItem.class, false);
    }
}