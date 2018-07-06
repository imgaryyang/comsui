package com.suidifu.microservice.handler.impl;

import com.demo2do.core.utils.StringUtils;
import com.suidifu.hathaway.mq.annotations.MqAsyncRpcMethod;
import com.suidifu.hathaway.mq.annotations.MqRpcBusinessUuid;
import com.suidifu.hathaway.mq.annotations.MqRpcPriority;
import com.suidifu.microservice.handler.CashFlowAutoIssueV2_0Handler;
import com.suidifu.microservice.handler.ThirdPartVoucherV2_0Handler;
import com.suidifu.microservice.handler.ThirdPartyVoucherRepaymentOrderWithReconciliationNoSession;
import com.zufangbao.sun.entity.repayment.order.OrderRecoverResult;
import com.zufangbao.sun.entity.repayment.order.PaymentOrder;
import com.zufangbao.sun.entity.repayment.order.RepaymentOrder;
import com.zufangbao.sun.entity.repayment.order.RepaymentOrderItem;
import com.zufangbao.sun.service.PaymentOrderService;
import com.zufangbao.sun.service.RepaymentOrderService;
import com.zufangbao.sun.yunxin.service.repayment.RepaymentOrderItemService;
import java.util.List;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by zhusy on 2017/7/28.
 */
@Component("thirdPartyVoucherRepaymentOrderWithReconciliationNoSession")
public class ThirdPartyVoucherRepaymentOrderWithReconciliationNoSessionImpl implements ThirdPartyVoucherRepaymentOrderWithReconciliationNoSession {
    private Log logger = LogFactory.getLog(getClass());
    @Autowired
    private CashFlowAutoIssueV2_0Handler cashFlowAutoIssueV20Handler;
    @Autowired
    private ThirdPartVoucherV2_0Handler thirdPartVoucherV20Handler;
    @Autowired
    private RepaymentOrderService repaymentOrderService;
    @Autowired
    private PaymentOrderService paymentOrderService;
    @Autowired
    private RepaymentOrderItemService repaymentOrderItemService;

    @Override
    @MqAsyncRpcMethod(beanName = "thirdPartyVoucherRepaymentOrderWithReconciliationNewProxy")
    public void generateThirdPartVoucherWithReconciliation(@MqRpcBusinessUuid String contractUuid, String repaymentOrderUuid, String paymentOrderUuid, @MqRpcPriority int priority) {
        thirdPartVoucherWithReconciliation(repaymentOrderUuid, paymentOrderUuid);
    }

    @Override
    public void thirdPartVoucherWithReconciliation(String repaymentOrderUuid, String paymentOrderUuid) {
        logger.info("begin to  generate journal_voucher and audit repaymentOrderUuid[" + repaymentOrderUuid + "],paymentOrderUuid[" + paymentOrderUuid + "]");
        RepaymentOrder repaymentOrder = repaymentOrderService.getRepaymentOrderByUuid(repaymentOrderUuid);
        if (null == repaymentOrder || repaymentOrder.getOrderRecoverResult() == OrderRecoverResult.ALL) { //核销结果：NONE:未核销,PART:部分核销,ALL:全部核销.
            return;
        }
        PaymentOrder paymentOrder = paymentOrderService.getActivePaymenrOrderByUuid(paymentOrderUuid);

        if (null == paymentOrder || paymentOrder.isPaySuc() == false) { //核销结果：NONE:未核销,PART:部分核销,ALL:全部核销.
            return;
        }
        if (isReceivableInAdvance(repaymentOrder)) {
            try {
                cashFlowAutoIssueV20Handler.recoverReceivableInAdvance(repaymentOrderUuid, paymentOrder.getUuid());
            } catch (Exception e) {
                logger.error("generate journal_voucher and audit(isReceivableInAdvance)occur error ,repaymentOrderUuid[" + repaymentOrderUuid + "],paymentOrderUuid[" + paymentOrderUuid + "],msg:" + ExceptionUtils.getFullStackTrace(e));
            }
        } else {
            nonReceivableInAdvance(repaymentOrderUuid, repaymentOrder, paymentOrder);
        }
        logger.info("end to  generate journal_voucher and audit repaymentOrderUuid[" + repaymentOrderUuid + "],paymentOrderUuid[" + paymentOrderUuid + "]");
    }

    private void nonReceivableInAdvance(String repaymentOrderUuid, RepaymentOrder repaymentOrder, PaymentOrder paymentOrder) {
        String sourceDocumentUuid = null;
        try {
            logger.info("begin to generate  third part journal voucher repaymentOrderUuid [" + repaymentOrderUuid
                    + "]");
            sourceDocumentUuid = thirdPartVoucherV20Handler
                    .createDeductRepaymentOrderSourceDocumentUuid(repaymentOrder, paymentOrder);
            logger.info("end to generate  third part journal voucher repaymentOrderUuid [" + repaymentOrderUuid
                    + "]");
        } catch (Exception e) {
            logger.error("occur error when loop gengerate third part voucher repaymentOrderUuid ["
                    + repaymentOrderUuid + "],with the full error stack:[" + ExceptionUtils.getFullStackTrace(e) + "].");
            e.printStackTrace();
        }
        if (StringUtils.isEmpty(sourceDocumentUuid)) {
            logger.error("recover_assets_online_deduct_by_interface_each_deduct_application,error sourceDocumentUuid is empty");
            return;
        }
        try {
            logger.info("begin to recover_assets_online_deduct_by_interface_each_deduct_application,sourceDocumentUuid["
                    + sourceDocumentUuid + "],repaymentOrderUuid[" + repaymentOrderUuid + "].");

            cashFlowAutoIssueV20Handler
                    .recoverAssetsRepaymentOrderDeduct(sourceDocumentUuid);
            logger.info("end to recover_assets_online_deduct_by_interface_each_deduct_application,sourceDocumentUuid["
                    + sourceDocumentUuid + "],repaymentOrderUuid[" + repaymentOrderUuid + "].");
        } catch (Exception e) {
            logger.error(
                    "occur error when recover_assets_online_deduct_by_interface_each_deduct_application,sourceDocumentUuid["
                            + sourceDocumentUuid + "],repaymentOrderUuid[" + repaymentOrderUuid + "],with the full error stack:[" + ExceptionUtils.getFullStackTrace(e) + "].");
            e.printStackTrace();
        }
    }

    private boolean isReceivableInAdvance(RepaymentOrder repaymentOrder) {
        List<RepaymentOrderItem> items = repaymentOrderItemService.getRepaymentOrderItems(repaymentOrder.getOrderUuid());
        RepaymentOrderItem item = items.get(0);
        return item.isReceivableInAdvance();
    }
}