package com.suidifu.owlman.microservice.handler;


import com.suidifu.owlman.microservice.exception.PaymentSystemException;
import com.zufangbao.sun.api.model.repayment.PaymentOrderRequestModel;


public interface RepaymentOrderHandlerProxy {

    void repaymentOrderSingleForEasyPay(String contractUuid, String repaymentOrdeUuid, int priority);

    void cancelRepaymentOrderForSingleContract(String contractUuid, String repaymentOrderUuid, int priority);

    void onlineDeductRepaymentOrder(String contractUuid, String repaymentOrderUuid, PaymentOrderRequestModel commandModel, String paymentOrderUuid, int priority) throws PaymentSystemException;
}