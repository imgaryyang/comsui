package com.suidifu.microservice.service.impl;

import com.demo2do.core.service.impl.GenericServiceImpl;
import com.suidifu.microservice.entity.SourceDocument;
import com.suidifu.microservice.service.SourceDocumentService;
import com.zufangbao.sun.yunxin.entity.SourceDocumentStatus;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

/**
 * @author louguanyang
 */
@Service("sourceDocumentService")
public class SourceDocumentServiceImpl extends GenericServiceImpl<SourceDocument> implements SourceDocumentService {

    @Override
    public SourceDocument getUnWriteOffSourceDocumentByDeductInformation(String deductApplicationUuid) {
        String queryString = "from  SourceDocument where outlierDocumentUuid =:deductApplicationUuid and  sourceDocumentStatus =:sourceDocumentStatus ";

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("deductApplicationUuid", deductApplicationUuid);
        params.put("sourceDocumentStatus", SourceDocumentStatus.CREATE);
        List<SourceDocument> sourceDocumentList = this.genericDaoSupport.searchForList(queryString, params);
        if (CollectionUtils.isEmpty(sourceDocumentList)) {
            return null;
        }
        return sourceDocumentList.get(0);
    }
}




