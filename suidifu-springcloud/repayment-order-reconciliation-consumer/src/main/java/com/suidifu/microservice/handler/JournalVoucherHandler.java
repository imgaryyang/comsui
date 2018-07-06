package com.suidifu.microservice.handler;

import com.suidifu.microservice.model.ReconciliationRepaymentContext;

public interface JournalVoucherHandler {
    void recoverAssetByReconciliationContextRepaymentOrder(ReconciliationRepaymentContext context);
}