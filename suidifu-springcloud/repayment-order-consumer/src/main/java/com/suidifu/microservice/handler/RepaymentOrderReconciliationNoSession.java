package com.suidifu.microservice.handler;

public interface RepaymentOrderReconciliationNoSession {
    void repayment_order_for_split_mode_and_deduct(String contractUuid, String orderUuid, String paymentOrderUuid, int priority);
}