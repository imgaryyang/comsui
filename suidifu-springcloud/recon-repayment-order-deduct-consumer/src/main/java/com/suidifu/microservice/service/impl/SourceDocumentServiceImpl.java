package com.suidifu.microservice.service.impl;

import com.demo2do.core.service.impl.GenericServiceImpl;
import com.suidifu.microservice.entity.SourceDocument;
import com.suidifu.microservice.service.JournalVoucherService;
import com.suidifu.microservice.service.SourceDocumentDetailService;
import com.suidifu.microservice.service.SourceDocumentService;
import com.zufangbao.sun.yunxin.entity.SourceDocumentStatus;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("sourceDocumentService")
public class SourceDocumentServiceImpl extends
		GenericServiceImpl<SourceDocument> implements SourceDocumentService {


	@Autowired
	private SourceDocumentDetailService sourceDocumentDetailService;
	
	@Autowired
	private JournalVoucherService journalVoucherService;
	
	private static Log logger = LogFactory.getLog(SourceDocumentServiceImpl.class);
	
	@Override
	public SourceDocument getSourceDocumentBy(String sourceDocumentUuid) {
		if(StringUtils.isEmpty(sourceDocumentUuid)) {
			return null;
		}
		 
		List<SourceDocument> sourceDocumentList = genericDaoSupport.searchForList("FROM SourceDocument WHERE sourceDocumentUuid =:sourceDocumentUuid", "sourceDocumentUuid", sourceDocumentUuid);
		if(CollectionUtils.isEmpty(sourceDocumentList)) {
			return null;
		}
		return sourceDocumentList.get(0);
	}

	@Override
	public String getUnWriteOffSourceDocumentUuidByDeductInformation(String deductApplicationUuid) {

		String  queryString = "select sourceDocumentUuid from  SourceDocument where outlierDocumentUuid =:deductApplicationUuid and  sourceDocumentStatus =:sourceDocumentStatus ";

		Map<String,Object> params = new HashMap<String,Object>();
		params.put("deductApplicationUuid", deductApplicationUuid);
		params.put("sourceDocumentStatus",  SourceDocumentStatus.CREATE);
		List<String> sourceDocumentUuidList = this.genericDaoSupport.searchForList(queryString,params);
		if(CollectionUtils.isEmpty(sourceDocumentUuidList)){
			return null;
		}
		return sourceDocumentUuidList.get(0);

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





