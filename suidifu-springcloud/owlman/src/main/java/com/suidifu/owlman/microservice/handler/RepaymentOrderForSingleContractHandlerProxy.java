package com.suidifu.owlman.microservice.handler;

import com.zufangbao.sun.api.model.repayment.PaymentOrderRequestModel;


public interface RepaymentOrderForSingleContractHandlerProxy {
    void repaymentOrderSingleForEasyPay(String contractUuid, String repaymentOrdeUuid, int priority);

    void cancelRepaymentOrderForSingleContract(String contractUuid, String repaymentOrderUuid, int priority);

    void onlineDeductPaymentOrderPay(String contractUuid, String repaymentOrderUuid, PaymentOrderRequestModel commandModel, String paymentOrderUuid, int priority);
}