package com.suidifu.microservice.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.demo2do.core.service.impl.GenericServiceImpl;
import com.suidifu.microservice.entity.SourceDocument;
import com.suidifu.microservice.service.SourceDocumentService;
import com.zufangbao.sun.yunxin.entity.SourceDocumentStatus;

@Service("sourceDocumentService")
public class SourceDocumentServiceImpl extends
		GenericServiceImpl<SourceDocument> implements SourceDocumentService {

	private static Log logger = LogFactory.getLog(SourceDocumentServiceImpl.class);
	
	
	@Override
	public SourceDocument getSourceDocumentByCashFlow(String cashFlowUuid,
			String firstOutlierDocType) {
		if(StringUtils.isEmpty(cashFlowUuid) || StringUtils.isEmpty(firstOutlierDocType)){
			return null;
		}
		
		String  queryString = "from SourceDocument where outlierDocumentUuid =:cashFlowUuid AND firstOutlierDocType =:firstOutlierDocType AND sourceDocumentStatus !=:sourceDocumentStatus";
		
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("cashFlowUuid", cashFlowUuid);
		params.put("firstOutlierDocType", firstOutlierDocType);
		params.put("sourceDocumentStatus", SourceDocumentStatus.INVALID);
		
		List<SourceDocument> sourceDocuments = this.genericDaoSupport.searchForList(queryString,params);
		if(CollectionUtils.isEmpty(sourceDocuments)){
			return null;
		}
		return sourceDocuments.get(0);
	}

	@Override
	public SourceDocument getSourceDocumentByOutlierDocumentUuid(
			String outlierDocumentUuid,String firstOutlierDocType) {
		
		if(StringUtils.isEmpty(outlierDocumentUuid)){
			return null;
		}
		
		String  queryString = "from SourceDocument where outlierDocumentUuid =:outlierDocumentUuid AND firstOutlierDocType =:firstOutlierDocType";
		
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("outlierDocumentUuid", outlierDocumentUuid);
		params.put("firstOutlierDocType", firstOutlierDocType);
		
		List<SourceDocument> sourceDocuments = this.genericDaoSupport.searchForList(queryString,params);
		if(CollectionUtils.isEmpty(sourceDocuments)){
			return null;
		}
		return sourceDocuments.get(0);
	}
	
}





