package com.suidifu.owlman.microservice.handler;

import com.demo2do.core.web.resolver.Page;
import com.suidifu.owlman.microservice.exception.PaymentOrderException;
import com.suidifu.owlman.microservice.exception.PaymentSystemException;
import com.suidifu.owlman.microservice.model.DeductReturnModel;
import com.zufangbao.gluon.exception.repayment.RepaymentOrderDeductCallBackException;
import com.zufangbao.sun.api.model.repayment.PaymentOrderRequestModel;
import com.zufangbao.sun.entity.directbank.business.CashFlow;
import com.zufangbao.sun.entity.payment.order.PaymentOrderCashFlowShowModel;
import com.zufangbao.sun.entity.payment.order.PaymentOrderRecordModel;
import com.zufangbao.sun.entity.repayment.order.PayWay;
import com.zufangbao.sun.entity.repayment.order.PaymentOrder;
import com.zufangbao.sun.entity.repayment.order.RepaymentOrder;
import com.zufangbao.sun.yunxin.entity.model.repaymentOrder.PaymentContext;
import java.math.BigDecimal;
import java.util.List;

public interface PaymentOrderHandler {
    String savePaymentOrderAndUpdateOrder(PayWay payWay, RepaymentOrder order, CashFlow cashFlow, String remark);

    PaymentOrder cancelPaymentOrder(String paymentOrderUuid) throws PaymentOrderException;

    PaymentOrder checkIfPaymentOrderCanBeCancelled(String paymentOrderUuid) throws PaymentOrderException;

    PaymentOrder generatePaymentOrderAndUpdayeRepaymentOrder(String financialContractUuid, String repaymentOrderUuid);

    PaymentOrder create_payment_order(PaymentOrderRequestModel paymentOrderRequestModel, RepaymentOrder repaymentOrder);

    List<PaymentOrderRecordModel> getPaymentOrderRecordModelsByPaymentOrder(String paymentOrderUuid, Page page);

    int getCountPaymentOrderRecordModel(String paymentOrderUuid);

    List<PaymentOrderCashFlowShowModel> getCashFlowShowModels(String paymentOrderUuid);

    void paymentOrderMatchCashFlow(RepaymentOrder order, PaymentOrder paymentOrder, String accountNo);

    void updatePaymentOrderAndRpaymentOrderFail(PaymentOrder paymentOrder, RepaymentOrder repaymentOrder, int paymentOrderStatus, String remark);

    void updatePaymentOrderAndOrderStatus(CashFlow cashFlow, PaymentOrder paymentOrder, RepaymentOrder repaymentOrder);

    boolean isMoreOrEmpty(String financialContractNo, String counterAccountNo, String counterAccountName, String orderUuid, BigDecimal amount);

    //4
    void offlineTransferPaymentOrderPay(String repaymentOrderUuid, PaymentOrderRequestModel commandModel, String paymentOrderUuid, int priority) throws PaymentSystemException;

    //5
    void updatePaymentOrderAndOrder(String repaymentOrderUuid, String paymentOrderUuid, String cashFlowUuid, int priority);

    //3
    void matchCashFlowByPaymentOrder(String repaymentOrderUuid, String paymentOrderUuid, String accountNo, int priority);

    PaymentContext collectPaymentOrderContextBy(String paymentOrderUuid, String orderUuid, DeductReturnModel deductReturnModel) throws RepaymentOrderDeductCallBackException;

    //1
    void businessDeductPaymentOrderPay(String repaymentOrderUuid, PaymentOrderRequestModel commandModel, String paymentOrderUuid, int priority) throws PaymentSystemException;

    //2
    void handlerPaymentOrderForBusinessDeduct(String repaymentOrderUuid, String tradeUuid, Integer retryTimes);

    void rtFlowQueryByNotifyServer(PaymentOrder paymentOrder);

    PaymentOrder savePaymentOrderByCommandModel(PaymentOrderRequestModel commandModel, RepaymentOrder order);

}