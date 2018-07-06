package com.suidifu.microservice.service;

import com.demo2do.core.service.GenericService;
import com.suidifu.microservice.entity.JournalVoucher;

public interface JournalVoucherService extends GenericService<JournalVoucher> {
    JournalVoucher getJournalVoucherBySourceDocumentUuidAndType(String sourceDocumentUuid, String sourceDocumentDetailUuid, String billingPlanUuid);
}