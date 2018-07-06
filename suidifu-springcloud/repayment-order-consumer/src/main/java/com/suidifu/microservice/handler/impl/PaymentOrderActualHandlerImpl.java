package com.suidifu.microservice.handler.impl;

import com.demo2do.core.entity.Result;
import com.demo2do.core.utils.JsonUtils;
import com.suidifu.hathaway.job.Priority;
import com.suidifu.hathaway.mq.annotations.MqAsyncRpcMethod;
import com.suidifu.hathaway.mq.annotations.MqRpcBusinessUuid;
import com.suidifu.hathaway.mq.annotations.MqRpcPriority;
import com.suidifu.hathaway.mq.annotations.MqSyncRpcMethod;
import com.suidifu.microservice.handler.PaymentOrderHandlerNoSession;
import com.suidifu.microservice.handler.RepaymentOrderHandler;
import com.suidifu.microservice.handler.ThirdPartyVoucherRepaymentOrderWithReconciliationNoSession;
import com.suidifu.owlman.microservice.handler.PaymentOrderHandler;
import com.suidifu.owlman.microservice.handler.PaymentOrderHandlerProxy;
import com.suidifu.owlman.microservice.model.DeductReturnModel;
import com.suidifu.owlman.microservice.model.PaymentOrderSubmitModel;
import com.zufangbao.gluon.exception.repayment.RepaymentOrderDeductCallBackException;
import com.zufangbao.sun.entity.repayment.order.OrderPayStatus;
import com.zufangbao.sun.entity.repayment.order.PayStatus;
import com.zufangbao.sun.entity.repayment.order.PaymentOrder;
import com.zufangbao.sun.entity.repayment.order.RepaymentOrder;
import com.zufangbao.sun.service.PaymentOrderService;
import com.zufangbao.sun.service.RepaymentOrderService;
import com.zufangbao.sun.service.ThirdPartVoucherCommandLogService;
import com.zufangbao.sun.yunxin.entity.model.repaymentOrder.PaymentContext;
import java.math.BigDecimal;
import java.util.Date;
import javax.annotation.Resource;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.stereotype.Component;

@Log4j2
@Component("paymentOrderHandlerProxy")
public class PaymentOrderActualHandlerImpl implements PaymentOrderHandlerProxy {
    @Resource
    private RepaymentOrderService repaymentOrderService;

    @Resource
    private PaymentOrderService paymentOrderService;

    @Resource
    private PaymentOrderHandler paymentOrderHandler;

    @Resource
    private ThirdPartVoucherCommandLogService thirdPartVoucherCommandLogService;

    @Resource
    private ThirdPartyVoucherRepaymentOrderWithReconciliationNoSession thirdPartyVoucherRepaymentOrderWithReconciliationNoSession;

    @Resource
    private RepaymentOrderHandler repaymentOrderHandler;
    @Resource
    private PaymentOrderHandlerNoSession paymentOrderHandlerNoSession;

    @Override
    @MqAsyncRpcMethod
    public void updatePaymentOrderFail(@MqRpcBusinessUuid String repaymentOrderUuid,
                                       String paymentOrderUuid, int paymentOrderStatus,
                                       String remark, @MqRpcPriority int priority) {
        try {
            RepaymentOrder repaymentOrder = repaymentOrderService.getRepaymentOrderByUuid(repaymentOrderUuid);

            PaymentOrder paymentOrder = paymentOrderService.getActivePaymenrOrderByUuid(paymentOrderUuid);

            paymentOrderHandler.updatePaymentOrderAndRpaymentOrderFail(paymentOrder, repaymentOrder, paymentOrderStatus, remark);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    @MqSyncRpcMethod
    public void deductNotifyHandle(@MqRpcBusinessUuid String contractUuid, String paymentOrderUuid, String orderUuid,
                                   DeductReturnModel deductReturnModel, @MqRpcPriority int priority)
            throws RepaymentOrderDeductCallBackException {
        try {
            log.info("internal deduct notify deductReturnModel[" + deductReturnModel + "]");

            //收集PaymentContext
            PaymentContext paymentContext = paymentOrderHandler.collectPaymentOrderContextBy(paymentOrderUuid, orderUuid, deductReturnModel);

            PaymentOrder paymentOrder = evaluatePaymentOrder(paymentContext);

            RepaymentOrder repaymentOrder = evaluateRepaymentOrder(paymentContext);

            if (repaymentOrder.getOrderPayStatus() == OrderPayStatus.PAY_FAIL && paymentContext.getExpectedPayStatus() == PayStatus.PAY_FAIL) {

                //作废item  支付中金额回滚   还款计划 状态变更
                log.info("repaymentOrder of deduct fail update items and repaymentPlan send msg[lapseOrderItemRollBackRepaymentPlan]  start ,orderUuid[" + repaymentOrder.getOrderUuid() + "]");
                repaymentOrderHandler.lapseOrderItemRollBackRepaymentPlan(repaymentOrder.getOrderUuid());
                log.info("repaymentOrder of deduct fail update items and repaymentPlan send msg[lapseOrderItemRollBackRepaymentPlan]  end ,orderUuid[" + repaymentOrder.getOrderUuid() + "]");
            } else if (paymentContext.getExpectedPayStatus() == PayStatus.PAY_SUCCESS) {
                //3. 生成第三方支付凭证
                thirdPartVoucherCommandLogService.createThirdPartyVoucherCommandLogAfterDeduct(paymentOrder, paymentContext.getPaymentInstitutionNameOfSucDeductPlan(), false);
                //4.核销消息
                log.info("internal deduct notify create thirdPartVoucherCommandLog end: deductApplicationUuid[" + paymentOrder.getOutlierDocumentUuid() + "], orderUuid[" + paymentOrder.getOrderUuid() + "], deductReturnModel[" + deductReturnModel + "]");
                thirdPartyVoucherRepaymentOrderWithReconciliationNoSession.generateThirdPartVoucherWithReconciliation(repaymentOrder.getFirstContractUuid(), repaymentOrder.getOrderUuid(), paymentOrder.getUuid(), Priority.High.getPriority());
                log.info("internal deduct notify end: deductReturnModel[" + deductReturnModel + "]");
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (e instanceof RepaymentOrderDeductCallBackException) {
                throw (RepaymentOrderDeductCallBackException) e;
            }
        }

    }

    private PaymentOrder evaluatePaymentOrder(PaymentContext paymentContext) {

        PaymentOrder paymentOrder = paymentContext.getPaymentOrder();

        paymentOrder.updatePaySus(paymentContext.getExpectedPayStatus(), paymentContext.getExpectedRepaymentOrderPayResult(), paymentContext.getTransactionTime(), paymentContext.getRemark(), paymentContext.getSucDeductPlan() == null ? "" : paymentContext.getSucDeductPlan().getTradeUuid(), paymentContext.getPaymentInstitutionNameOfSucDeductPlan());
        paymentOrderService.saveOrUpdate(paymentOrder);

        log.info("internal deduct notify update paymentOrder end: expectedPayStatus[" + paymentContext.getExpectedPayStatus() + "], expectedRepaymentOrderPayResult[" + paymentContext.getExpectedRepaymentOrderPayResult() + "], paymentOrderUuid[" + paymentOrder.getUuid() + "], orderUuid[" + paymentOrder.getOrderUuid() + "]");
        return paymentOrder;
    }

    private RepaymentOrder evaluateRepaymentOrder(PaymentContext paymentContext) {
        RepaymentOrder repaymentOrder = paymentContext.getRepaymentOrder();

        //累加已支付的支付单金额
        BigDecimal paidAmount = paymentOrderService.getPaySuccessPaymentOrdersAmount(paymentContext.getPaymentOrder().getOrderUuid());
        //更新还款订单 payStatus 和 paidAmount
        repaymentOrder.setPaidAmount(paidAmount);
        String remark = getPayRemark(paymentContext.getPaymentOrder());
        repaymentOrder.evaluateRepaymentOrderStatus(paidAmount);
        repaymentOrder.setRemark(remark);
        repaymentOrder.setOrderLastModifiedTime(new Date());
        repaymentOrderService.saveOrUpdate(repaymentOrder);

        return repaymentOrder;
    }

    private String getPayRemark(PaymentOrder paymentOrder) {
        return "[支付单号：" + paymentOrder.getUuid() + ",备注：" + paymentOrder.getRemark() + "]";
    }

    @Override
    @MqAsyncRpcMethod
    public void cancelPaymentOrder(@MqRpcBusinessUuid String businessId, String paymentOrderUuid, @MqRpcPriority int priority) {
        try {
            log.info("作废支付订单 start,paymentOrderUuid[" + paymentOrderUuid + "],businessId[" + businessId + "]");
            paymentOrderHandler.cancelPaymentOrder(paymentOrderUuid);
            log.info("作废支付订单 end,paymentOrderUuid[" + paymentOrderUuid + "],businessId[" + businessId + "]");
        } catch (Exception e) {
            log.error("作废支付订单,paymentOrderUuid[" + paymentOrderUuid + "],businessId[" + businessId + "], error:" + ExceptionUtils.getFullStackTrace(e));
        }
    }

    @Override
    @MqSyncRpcMethod
    public String generatePaymentOrder(@MqRpcBusinessUuid String repaymentOrderUuid, String modelString, @MqRpcPriority int priority) {
        Result result = new Result();
        PaymentOrderSubmitModel submitModel = JsonUtils.parse(modelString, PaymentOrderSubmitModel.class);
        try {
            log.info("支付订单 start,repaymentOrderUuid[" + repaymentOrderUuid + "]");
            result = paymentOrderHandlerNoSession.submit(submitModel);
            log.info("支付订单 end,repaymentOrderUuid[" + repaymentOrderUuid + "]");

        } catch (Exception e) {
            log.error("支付订单,repaymentOrderUuid[" + repaymentOrderUuid + "],error:" + ExceptionUtils.getFullStackTrace(e));
            result.message(e.getMessage());
        }
        return JsonUtils.toJsonString(result);
    }
}