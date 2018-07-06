package com.suidifu.microservice.handler.impl;

import com.suidifu.giotto.exception.GiottoException;
import com.suidifu.giotto.handler.FastHandler;
import com.suidifu.giotto.keyenum.FastAssetSetKeyEnum;
import com.suidifu.giotto.keyenum.FastRepaymentOrderItemKeyEnum;
import com.suidifu.giotto.model.FastAssetSet;
import com.suidifu.giotto.model.FastRepaymentOrderItem;
import com.suidifu.hathaway.mq.annotations.MqRpcBusinessUuid;
import com.suidifu.hathaway.mq.annotations.MqRpcPriority;
import com.suidifu.hathaway.mq.annotations.MqSyncRpcMethod;
import com.suidifu.microservice.handler.OrderItemEditHandler;
import com.suidifu.owlman.microservice.handler.RepaymentOrderItemHandlerProxy;
import com.zufangbao.gluon.exception.ApiException;
import com.zufangbao.sun.entity.repayment.order.IdentificationMode;
import com.zufangbao.sun.yunxin.entity.model.repaymentOrder.RepaymentOrderItemModel;
import com.zufangbao.sun.yunxin.log.LogFunctionType;
import com.zufangbao.sun.yunxin.log.LogOperateType;
import com.zufangbao.sun.yunxin.log.SystemOperateLog;
import com.zufangbao.sun.yunxin.service.SystemOperateLogService;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.UnexpectedRollbackException;

@Component("repaymentOrderItemHandlerProxy")
public class RepaymentOrderItemHandlerProxyImpl implements RepaymentOrderItemHandlerProxy {

    private static final Log logger = LogFactory.getLog(RepaymentOrderItemHandlerProxyImpl.class);
    @Autowired
    private FastHandler fastHandler;

    @Autowired
    private OrderItemEditHandler orderItemEditHandler;

    @Autowired
    private SystemOperateLogService systemOperateLogService;

    @MqSyncRpcMethod
    public void lapseAndCreateNewItem(@MqRpcBusinessUuid String contractUuid, String orderUuid, String item_uuid_to_be_lapsed, List<RepaymentOrderItemModel> repaymentOrderItemModels, @MqRpcPriority int priority, Long principalId, String ip) {
        String msg = "成功";
        try {
            orderItemEditHandler.lapseAndCreateNewItemHandler(contractUuid, orderUuid, item_uuid_to_be_lapsed, repaymentOrderItemModels, priority);

        } catch (UnexpectedRollbackException re) {
            logger.error("order order item occur error: contractUuid[" + contractUuid + "],orderUuid[" + orderUuid + "],item_uuid_to_be_lapsed[" + item_uuid_to_be_lapsed + "],msg[" + re.getMessage() + "], fullStackTrace:" + ExceptionUtils.getFullStackTrace(re));
            throw new ApiException("核销时捕获异常回滚数据");
        } catch (Exception e) {
            logger.error("order order item occur error: contractUuid[" + contractUuid + "],orderUuid[" + orderUuid + "],item_uuid_to_be_lapsed[" + item_uuid_to_be_lapsed + "],msg[" + e.getMessage() + "], fullStackTrace:" + ExceptionUtils.getFullStackTrace(e));
            msg = e.getMessage();
            try {
                buildAssetParamtersByItemUuid(item_uuid_to_be_lapsed, repaymentOrderItemModels);
            } catch (Exception be) {
                logger.error("order order item occur error: contractUuid[" + contractUuid + "],orderUuid[" + orderUuid + "],item_uuid_to_be_lapsed[" + item_uuid_to_be_lapsed + "],msg[" + e.getMessage() + "], fullStackTrace:" + ExceptionUtils.getFullStackTrace(be));
            }
        }
        generateSystemLog(orderUuid, item_uuid_to_be_lapsed, principalId, ip, msg);

    }

    private void generateSystemLog(String orderUuid, String item_uuid_to_be_lapsed, Long principalId, String ip,
                                   String msg) {
        StringBuilder recordContent = new StringBuilder("作废核销还款订单明细[").append(item_uuid_to_be_lapsed).append("],订单号[").append(orderUuid)
                .append("]")
                .append("],备注[" + msg + "]");
        SystemOperateLog log = SystemOperateLog.createLog(principalId, recordContent.toString(),
                ip, LogFunctionType.REDO_RECONCILIATION, LogOperateType.OPERATE,
                orderUuid, "");
        systemOperateLogService.save(log);
    }

    private void buildAssetParamtersByItemUuid(String item_uuid_to_be_lapsed, List<RepaymentOrderItemModel> repaymentOrderItemModels) throws GiottoException {

        List<String> repaymentBusinessNoList = new ArrayList<>();
        List<String> repayScheduleNoList = new ArrayList<>();
        FastRepaymentOrderItem fastRepaymentOrderItem = fastHandler.getByKey(FastRepaymentOrderItemKeyEnum.ORDER_DETAIL_UUID, item_uuid_to_be_lapsed, FastRepaymentOrderItem.class, false);
        if (fastRepaymentOrderItem.getIdentificationMode() == IdentificationMode.REPAY_SCHEDULE_NO.ordinal() || fastRepaymentOrderItem.getIdentificationMode() == IdentificationMode.REPAYMENT_PLAN_NO.ordinal()) {
            if (StringUtils.isNotEmpty(fastRepaymentOrderItem.getRepaymentBusinessNo())) {
                repaymentBusinessNoList.add(fastRepaymentOrderItem.getRepaymentBusinessNo());
            }
            if (StringUtils.isNotEmpty(fastRepaymentOrderItem.getRepayScheduleNo())) {
                repayScheduleNoList.add(fastRepaymentOrderItem.getRepayScheduleNo());
            }
        }

        for (RepaymentOrderItemModel repaymentOrderItemModel : repaymentOrderItemModels) {
            if (StringUtils.isEmpty(repaymentOrderItemModel.getRepaymentBusinessNo()) == false) {
                repaymentBusinessNoList.add(repaymentOrderItemModel.getRepaymentBusinessNo());
            }
            if (StringUtils.isEmpty(fastRepaymentOrderItem.getRepayScheduleNo()) == false) {
                repayScheduleNoList.add(repaymentOrderItemModel.getRepayScheduleNo());
            }
        }
        //刷新缓存
        List<FastAssetSet> fastAssetSets = new ArrayList<>();

        for (String repayScheduleNo : repayScheduleNoList) {
            FastAssetSet fastAssetSet = fastHandler.getByKey(FastAssetSetKeyEnum.REPAY_SCHEDULE_NO, repayScheduleNo, FastAssetSet.class, false);
            if (fastAssetSet != null) {
                fastAssetSets.add(fastAssetSet);
            }
        }
        for (String repaymentBusinessNo : repaymentBusinessNoList) {
            FastAssetSet fastAssetSet = fastHandler.getByKey(FastAssetSetKeyEnum.SINGLE_LOAN_CONTRACT_NO, repaymentBusinessNo, FastAssetSet.class, false);
            if (fastAssetSet != null) {
                fastAssetSets.add(fastAssetSet);
            }
        }

        for (FastAssetSet fastAssetSet : fastAssetSets) {
            fastHandler.getByKey(FastRepaymentOrderItemKeyEnum.REPAYMENT_BUSINESS_UUID, fastAssetSet.getAssetUuid(),
                    FastRepaymentOrderItem.class, false);
        }
    }
}