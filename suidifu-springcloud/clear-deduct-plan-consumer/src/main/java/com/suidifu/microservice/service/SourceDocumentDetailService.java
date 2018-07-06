package com.suidifu.microservice.service;

import java.util.List;

import com.demo2do.core.service.GenericService;
import com.suidifu.microservice.entity.SourceDocumentDetail;

public interface SourceDocumentDetailService extends GenericService<SourceDocumentDetail> {

	List<SourceDocumentDetail> getValidDeductSourceDocumentDetailsBySourceDocumentUuid(String sourceDocumentUuid);
	
}
