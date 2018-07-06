package com.suidifu.microservice.service;

import com.demo2do.core.service.GenericService;
import com.suidifu.microservice.entity.SourceDocument;

public interface SourceDocumentService extends GenericService<SourceDocument> {

	SourceDocument getSourceDocumentByCashFlow(String cashFlowUuid, String firstOutlierDocType);
	
	SourceDocument getSourceDocumentByOutlierDocumentUuid(String outlierDocumentUuid, String firstOutlierDocType);
	
}
