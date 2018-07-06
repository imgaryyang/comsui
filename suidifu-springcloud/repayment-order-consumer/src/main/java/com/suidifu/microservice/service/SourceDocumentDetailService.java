package com.suidifu.microservice.service;

import com.demo2do.core.service.GenericService;
import com.suidifu.microservice.entity.SourceDocumentDetail;
import java.util.List;

public interface SourceDocumentDetailService extends GenericService<SourceDocumentDetail> {
    SourceDocumentDetail getSourceDocumentDetailBy(String sourceDocumentDetailUuid);

    List<String> getValidDeductSourceDocumentDetailUuidsBy(String sourceDocumentUuid);

    void lapseSourceDocumentDetailBy(String sourceDocumentUuid);

    void updateSourceDocumentUuid(String voucherUuid, String sourceDocumentUuid, String contractUniqueId);

    boolean exist(String sourceDocumentUuid, String firstType, String secondNo);
}