package com.suidifu.microservice.handler;

import com.suidifu.microservice.model.ReconciliationContext;

public interface JournalVoucherHandler {
    void recoverAssetByReconciliationContext(ReconciliationContext context);
}