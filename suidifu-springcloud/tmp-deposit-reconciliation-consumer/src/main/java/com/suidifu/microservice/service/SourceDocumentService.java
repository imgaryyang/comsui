package com.suidifu.microservice.service;

import com.demo2do.core.service.GenericService;
import com.suidifu.microservice.entity.SourceDocument;

/**
 * @author louguanyang
 */
public interface SourceDocumentService extends GenericService<SourceDocument> {

  SourceDocument getSourceDocumentBy(String sourceDocumentUuid);

  SourceDocument getSourceDocumentByOutlierDocumentUuid(String outlierDocumentUuid, String firstOutlierDocType);

}
