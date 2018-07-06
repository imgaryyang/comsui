package com.suidifu.microservice.handler;

public interface CashFlowAutoIssueV2_0Handler {
    void recoverAssetsRepaymentOrderDeduct(String sourceDocumentUuid);

    void recoverReceivableInAdvance(String orderUuid, String paymentOrderUuid);
}