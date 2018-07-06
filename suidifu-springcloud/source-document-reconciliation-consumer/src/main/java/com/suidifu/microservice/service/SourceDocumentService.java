package com.suidifu.microservice.service;

import com.demo2do.core.service.GenericService;
import com.suidifu.microservice.entity.SourceDocument;
import com.suidifu.owlman.microservice.enumation.SourceDocumentExcuteResult;
import com.suidifu.owlman.microservice.enumation.SourceDocumentExcuteStatus;
import java.util.List;

public interface SourceDocumentService extends GenericService<SourceDocument> {
    SourceDocument getSourceDocumentBy(String sourceDocumentUuid);

    SourceDocument getSourceDocumentByOutlierDocumentUuid(String outlierDocumentUuid, String firstOutlierDocType);

    List<SourceDocument> getDepositSourceDocumentListConnectedBy(SourceDocumentExcuteResult excuteResult,
                                                                 SourceDocumentExcuteStatus excuteStatus);
}