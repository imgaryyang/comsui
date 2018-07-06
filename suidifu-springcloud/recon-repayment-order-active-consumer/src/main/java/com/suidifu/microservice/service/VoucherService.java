package com.suidifu.microservice.service;

import com.demo2do.core.service.GenericService;
import com.suidifu.microservice.model.SourceDocument;
import com.suidifu.microservice.model.Voucher;

public interface VoucherService extends GenericService<Voucher> {
    Voucher get_voucher_by_sourceDocument(SourceDocument sourceDocument);
}
