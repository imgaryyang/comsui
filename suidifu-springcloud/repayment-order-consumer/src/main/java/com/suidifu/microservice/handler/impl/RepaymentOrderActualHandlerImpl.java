package com.suidifu.microservice.handler.impl;

import com.demo2do.core.utils.JsonUtils;
import com.suidifu.hathaway.mq.annotations.MqAsyncRpcMethod;
import com.suidifu.hathaway.mq.annotations.MqRpcBusinessUuid;
import com.suidifu.hathaway.mq.annotations.MqRpcPriority;
import com.suidifu.hathaway.mq.annotations.MqSyncRpcMethod;
import com.suidifu.microservice.handler.RepaymentOrderReconciliationNoSession;
import com.suidifu.owlman.microservice.exception.PaymentSystemException;
import com.suidifu.owlman.microservice.handler.PaymentOrderHandler;
import com.suidifu.owlman.microservice.handler.RepaymentOrderCancelHandler;
import com.suidifu.owlman.microservice.handler.RepaymentOrderHandlerProxy;
import com.suidifu.owlman.microservice.handler.RepaymentOrderPlacingHandler;
import com.zufangbao.gluon.exception.repayment.RepaymentOrderCheckException;
import com.zufangbao.sun.api.model.repayment.PaymentOrderRequestModel;
import com.zufangbao.sun.api.model.repayment.RepaymentOrderDetail;
import com.zufangbao.sun.entity.repayment.order.OrderAliveStatus;
import com.zufangbao.sun.entity.repayment.order.PayStatus;
import com.zufangbao.sun.entity.repayment.order.PaymentOrder;
import com.zufangbao.sun.entity.repayment.order.RepaymentOrder;
import com.zufangbao.sun.entity.repayment.order.RepaymentOrderPayResult;
import com.zufangbao.sun.entity.repayment.order.RepaymentWayGroupType;
import com.zufangbao.sun.service.PaymentOrderService;
import com.zufangbao.sun.service.RepaymentOrderService;
import com.zufangbao.sun.yunxin.entity.repayment.RepaymentOrderLapseParam;
import com.zufangbao.sun.yunxin.service.repayment.RepaymentOrderItemService;
import java.math.BigDecimal;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

//@Component("repaymentOrderHandlerProxy")
public class RepaymentOrderActualHandlerImpl implements RepaymentOrderHandlerProxy {
    @Autowired
    private RepaymentOrderService repaymentOrderService;

    @Autowired
    private RepaymentOrderPlacingHandler repaymentOrderPlacingHandler;

    @Autowired
    private RepaymentOrderReconciliationNoSession repaymentOrderReconciliationNoSession;

    @Autowired
    private RepaymentOrderItemService repaymentOrderItemService;

    @Autowired
    private RepaymentOrderCancelHandler repaymentOrderCancelHandler;

    @Autowired
    private PaymentOrderService paymentOrderService;

    @Autowired
    private PaymentOrderHandler paymentOrderHandler;

    private Log logger = LogFactory.getLog(getClass());

    @Override
    @MqAsyncRpcMethod(beanName = "repaymentOrderHandlerProxy", methodName = "repaymentOrderSingleForEasyPay")
    public void repaymentOrderSingleForEasyPay(@MqRpcBusinessUuid String contractUuid, String repaymentOrderUuid, @MqRpcPriority int priority) {
        logger.info("#execute repaymentOrderForEasyPay check and deduct start orderUuid:" + repaymentOrderUuid + ".");

        RepaymentOrder repaymentOrder = repaymentOrderService.getRepaymentOrderByUuid(repaymentOrderUuid);

        List<RepaymentOrderDetail> repaymentOrderDetailList = JsonUtils.parseArray(repaymentOrder.getRepaymentOrderDetailJson(), RepaymentOrderDetail.class);
        if (CollectionUtils.isEmpty(repaymentOrderDetailList)) return;


        long start = System.currentTimeMillis();
        try {
            repaymentOrderPlacingHandler.check_and_save_for_single_contract_repayment_order(repaymentOrder, repaymentOrderDetailList);

            //生成支付单
            if (repaymentOrder.getFirstRepaymentWayGroup() == RepaymentWayGroupType.ONLINE_DEDUCT_REPAYMENT_ORDER_EASY_PAY_TYPE) {
                PaymentOrder paymentOrder = paymentOrderHandler.generatePaymentOrderAndUpdayeRepaymentOrder(repaymentOrder.getFinancialContractUuid(), repaymentOrder.getOrderUuid());
                repaymentOrderReconciliationNoSession.repayment_order_for_split_mode_and_deduct(contractUuid, repaymentOrder.getOrderUuid(), paymentOrder.getUuid(), priority);
            }
        } catch (RepaymentOrderCheckException roce) {
            logger.info("#execute repaymentOrderForEasyPay check fail[RepaymentOrderCheckException],orderUuid:" + repaymentOrderUuid);

        } catch (Exception e) {
            logger.error("#execute repaymentOrderForEasyPay check and deduct occur error," + "orderUuid:" + repaymentOrderUuid + ",error stack full trace[" + ExceptionUtils.getFullStackTrace(e) + "]");
        }
        long end = System.currentTimeMillis();
        logger.info("#execute repaymentOrderForEasyPay check and deduct end orderUuid:" + repaymentOrderUuid + ", with the " + (start - end) + "[ms],detailSize[" + repaymentOrderDetailList.size() + "]");
    }

    @Override
    @MqSyncRpcMethod(beanName = "repaymentOrderHandlerProxy", methodName = "cancelRepaymentOrderForSingleContract")
    public void cancelRepaymentOrderForSingleContract(@MqRpcBusinessUuid String contractUuid, String repaymentOrderUuid, @MqRpcPriority int priority) {
        logger.info("cancelRepaymentOrderForSingleContract start, orderUuid[" + repaymentOrderUuid + "].");
        List<RepaymentOrderLapseParam> repaymentOrderLapseParamList = repaymentOrderItemService.get_repayment_order_lapse_params(repaymentOrderUuid);

        logger.info("#repaymentOrder#cancelRepaymentOrderForSingleContract details ids size:" + repaymentOrderLapseParamList.size() + ",repaymentOrderUuid:" + repaymentOrderUuid);

        repaymentOrderCancelHandler.deleteRepaymentOrderItem(repaymentOrderLapseParamList);

        logger.info("#repaymentOrder#update_repayment_order_lock_and_alive_status repaymentOrderUuid:" + repaymentOrderUuid);

        repaymentOrderService.update_repayment_order_lock_and_alive_status(OrderAliveStatus.PAYMENT_ORDER_CANCEL, repaymentOrderUuid, RepaymentOrder.EMPTY, RepaymentOrder.LAPSE);
        logger.info("cancelRepaymentOrderForSingleContract end, orderUuid[" + repaymentOrderUuid + "].");
    }


    @Override
    @MqAsyncRpcMethod(beanName = "repaymentOrderHandlerProxy", methodName = "onlineDeductRepaymentOrder")
    public void onlineDeductRepaymentOrder(@MqRpcBusinessUuid String contractUuid,
                                           String repaymentOrderUuid, PaymentOrderRequestModel commandModel, String paymentOrderUuid, @MqRpcPriority int priority) throws PaymentSystemException {

        long start = System.currentTimeMillis();
        logger.info("onlineDeductRepaymentOrder berkshire  start 1 -----------------------------------------------------use times[" + (start) + "]ms" + ", 当前时间  " + System.currentTimeMillis());


        PaymentOrder paymentOrder = paymentOrderService.getPaymenrOrderByUuid(paymentOrderUuid);
        RepaymentOrder repaymentOrder = repaymentOrderService.getRepaymentOrderByUuid(repaymentOrderUuid);

        BigDecimal successPayingAmount = paymentOrderService.getPaySuccessAndPayingPaymentOrdersAmount(repaymentOrder.getOrderUuid());
        logger.info("onlineDeductRepaymentOrder, orderUuid[" + repaymentOrderUuid + "]." + "paymentOrderUuid :" + paymentOrder.getUuid() + ",successPayingAmount :" + successPayingAmount);
        if (successPayingAmount.compareTo(repaymentOrder.getOrderAmount()) > 0) {
            paymentOrder.updateOrderPayResultStatus(RepaymentOrderPayResult.PAY_FAIL, PayStatus.PAY_FAIL);
            paymentOrderService.update(paymentOrder);

            throw new PaymentSystemException("该还款订单支付有误！");
        }

        long endTime = System.currentTimeMillis();
        logger.info("check successPayingAmount 2 -----------------------------------------------------use times[" + (endTime - start) + "]ms" + ", 当前时间  " + System.currentTimeMillis());


        try {

            logger.info("onlineDeduct_repayment_order_for_split_mode_and_deduct begin, orderUuid[" + repaymentOrderUuid + "]." + "paymentOrderUuid :" + paymentOrder.getUuid());

            repaymentOrderReconciliationNoSession.repayment_order_for_split_mode_and_deduct(contractUuid, repaymentOrderUuid, paymentOrder.getUuid(), priority);

            logger.info("onlineDeduct_repayment_order_for_split_mode_and_deduct end, orderUuid[" + repaymentOrderUuid + "]." + "paymentOrderUuid :" + paymentOrder.getUuid());

            logger.info("onlineDeductRepaymentOrder berkshire  start 2 -----------------------------------------------------use times[" + (System.currentTimeMillis() - start) + "]ms" + ", 当前时间  " + System.currentTimeMillis());

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
