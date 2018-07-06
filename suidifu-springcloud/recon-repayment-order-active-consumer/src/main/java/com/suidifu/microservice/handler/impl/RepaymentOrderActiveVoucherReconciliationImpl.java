package com.suidifu.microservice.handler.impl;

import com.suidifu.microservice.model.ReconciliationRepayment;
import com.suidifu.microservice.model.ReconciliationRepaymentOrderParameterNameSpace;
import com.suidifu.owlman.microservice.handler.RepaymentOrderActiveVoucherReconciliation;
import com.zufangbao.sun.entity.repayment.order.RepaymentOrderItem;
import com.zufangbao.sun.yunxin.entity.model.RepaymentOrderReconciliationParameters;
import com.zufangbao.sun.yunxin.service.repayment.RepaymentOrderItemService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * Created by MieLongJun on 18-2-28.
 * @link com.suidifu.berkshire.mq.rpc.proxy.handler.RepaymentOrderActiveVoucherReconciliationProxy
 */
@Component("repaymentOrderActiveVoucherReconciliation")
public class RepaymentOrderActiveVoucherReconciliationImpl implements RepaymentOrderActiveVoucherReconciliation {

    @Autowired
    private RepaymentOrderItemService repaymentOrderItemService;

    private static Log logger = LogFactory.getLog(RepaymentOrderActiveVoucherReconciliationImpl.class);

    @Override
    public boolean repaymentOrderRecoverDetails(
            List<RepaymentOrderReconciliationParameters> sourceDocumentReconciliationStepThreeParameterList) {
        if (CollectionUtils.isEmpty(sourceDocumentReconciliationStepThreeParameterList))
            return false;
        RepaymentOrderReconciliationParameters param = sourceDocumentReconciliationStepThreeParameterList.get(0);
        if (param == null)
            return false;

        return this.recoverRepaymentOrder(sourceDocumentReconciliationStepThreeParameterList);

    }

    private boolean recoverRepaymentOrder(
            List<RepaymentOrderReconciliationParameters> subrogationReconciliationStepThreeParameters) {
        boolean is_all_detail_recovered = true;

        int index = 0;

        String repaymentOrderItemUuid = "";

        String repaymentOrderUuid = "";

        long start_one_chunck = System.currentTimeMillis();

        for (RepaymentOrderReconciliationParameters subrogationReconciliationStepThreeParameter : subrogationReconciliationStepThreeParameters) {
            try {
                long start = System.currentTimeMillis();

                Map<String, Object> params = new HashMap<String, Object>();

                repaymentOrderItemUuid = subrogationReconciliationStepThreeParameter.getRepaymentOrderItemUuid();

                RepaymentOrderItem repaymentOrderItem = repaymentOrderItemService
                        .getRepaymentOrderItemByUuid(repaymentOrderItemUuid);

                if (repaymentOrderItem == null) {
                    continue;
                }

                String sencondType = repaymentOrderItem.getRepaymentWay().getKey();

                repaymentOrderUuid = subrogationReconciliationStepThreeParameter.getRepaymentOrderUuid();

                params.put(
                        ReconciliationRepaymentOrderParameterNameSpace.ReconciliationRepaymentParams.PARAMS_REPAYMENT_ORDER_ITEM_UUID,
                        repaymentOrderItemUuid);
                params.put(
                        ReconciliationRepaymentOrderParameterNameSpace.ReconciliationRepaymentParams.PARAMS_REPAYMENT_ORDER_UUID,
                        repaymentOrderUuid);
                params.put(
                        ReconciliationRepaymentOrderParameterNameSpace.ReconciliationRepaymentParams.PARAMS_UNIQUE_CASH_IDENTITY,
                        subrogationReconciliationStepThreeParameter.getUniqueCashIdentity());

                logger.debug("begin to recover single repaymentOrderItem with repaymentOrderItemUuid["
                        + repaymentOrderItemUuid + "],repaymentOrderItemUuid[" + repaymentOrderItemUuid + "]");

                ReconciliationRepayment reconciliation = ReconciliationRepayment.reconciliationFactory(sencondType);
                reconciliation.accountReconciliation(params);

                logger.debug("end to recover single repaymentOrderItem,id[" + repaymentOrderItemUuid + "],index["
                        + index + "], use times[" + (System.currentTimeMillis() - start) + "]ms, result:+");

                index++;

            } catch (Exception e) {
                logger.error("recover details, size:" + subrogationReconciliationStepThreeParameters.size()
                        + ",repaymentOrderItemUuid:" + repaymentOrderItemUuid + ",index:" + index
                        + ",repaymentOrderItemUuid["
                        + subrogationReconciliationStepThreeParameter.getRepaymentOrderUuid() + "],stack trace["
                        + ExceptionUtils.getStackTrace(e) + "]");
                e.printStackTrace();
                is_all_detail_recovered = false;
            }

        }
        double avg_usage = ((System.currentTimeMillis() - start_one_chunck))
                / (subrogationReconciliationStepThreeParameters.size() * 1.0);

        logger.info("#compnesatory_recover_details#avgrecover use times[" + avg_usage + "]ms" + "  size["
                + subrogationReconciliationStepThreeParameters.size() + "]");

        return is_all_detail_recovered;

    }

}
