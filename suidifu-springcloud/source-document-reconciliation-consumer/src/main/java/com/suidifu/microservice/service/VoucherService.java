package com.suidifu.microservice.service;

import com.demo2do.core.service.GenericService;
import com.suidifu.microservice.entity.SourceDocument;
import com.suidifu.microservice.entity.Voucher;
import com.suidifu.owlman.microservice.enumation.SourceDocumentDetailCheckState;
import com.suidifu.owlman.microservice.enumation.SourceDocumentDetailStatus;

public interface VoucherService extends GenericService<Voucher> {
    Voucher getVoucherByUuid(String uuid);

    void updateCheckStateVoucherState(String voucherUuid,
                                      SourceDocumentDetailCheckState checkState,
                                      SourceDocumentDetailStatus voucherState);

    Voucher getVoucherBySourceDocument(SourceDocument sourceDocument);
}