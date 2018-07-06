package com.suidifu.microservice.service;

import com.demo2do.core.service.GenericService;
import com.suidifu.microservice.entity.SourceDocumentDetail;
import java.util.List;

public interface SourceDocumentDetailService extends GenericService<SourceDocumentDetail> {
    SourceDocumentDetail getSourceDocumentDetail(String sourceDocumentDetailUuid);

    List<SourceDocumentDetail> getValidDeductSourceDocumentDetailsBySourceDocumentUuid(String sourceDocumentUuid);

    boolean exist(String sourceDocumentUuid, String firstType, String secondNo);

    List<String> getDetailUuidsBySourceDocumentUuid(String sourceDocumentUuid, String secondNo);
}