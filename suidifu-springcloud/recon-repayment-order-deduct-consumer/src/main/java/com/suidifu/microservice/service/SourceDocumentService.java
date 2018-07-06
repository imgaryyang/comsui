package com.suidifu.microservice.service;

import com.demo2do.core.service.GenericService;
import com.suidifu.microservice.entity.SourceDocument;

public interface SourceDocumentService extends GenericService<SourceDocument> {

	SourceDocument getSourceDocumentBy(String sourceDocumentUuid);
	
	String getUnWriteOffSourceDocumentUuidByDeductInformation(String deductApplicationUuid);
	SourceDocument getSourceDocumentByOutlierDocumentUuid(String outlierDocumentUuid,
      String firstOutlierDocType);
	
}
