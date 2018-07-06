package com.suidifu.microservice.service;

import com.demo2do.core.service.GenericService;
import com.suidifu.microservice.entity.SourceDocument;
import com.suidifu.microservice.entity.Voucher;

public interface VoucherService extends GenericService<Voucher> {
    Voucher getVoucherBy(SourceDocument sourceDocument);

    String get_unique_valid_voucher_uuid(String cashFlowUuid);
}