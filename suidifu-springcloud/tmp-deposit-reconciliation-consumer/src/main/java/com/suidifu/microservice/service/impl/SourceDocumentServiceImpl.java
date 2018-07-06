package com.suidifu.microservice.service.impl;

import com.demo2do.core.service.impl.GenericServiceImpl;
import com.suidifu.microservice.entity.SourceDocument;
import com.suidifu.microservice.service.SourceDocumentService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

@Service("sourceDocumentService")
public class SourceDocumentServiceImpl extends GenericServiceImpl<SourceDocument> implements SourceDocumentService {

  @Override
  public SourceDocument getSourceDocumentBy(String sourceDocumentUuid) {
    if (StringUtils.isEmpty(sourceDocumentUuid)) {
      return null;
    }

    String sql = "FROM SourceDocument WHERE sourceDocumentUuid =:sourceDocumentUuid";
    List<SourceDocument> sourceDocumentList = genericDaoSupport
        .searchForList(sql, "sourceDocumentUuid", sourceDocumentUuid);
    if (CollectionUtils.isEmpty(sourceDocumentList)) {
      return null;
    }
    return sourceDocumentList.get(0);
  }


  @Override
  public SourceDocument getSourceDocumentByOutlierDocumentUuid(
      String outlierDocumentUuid, String firstOutlierDocType) {

    if (StringUtils.isEmpty(outlierDocumentUuid)) {
      return null;
    }

    String queryString = "from SourceDocument where outlierDocumentUuid =:outlierDocumentUuid AND firstOutlierDocType =:firstOutlierDocType";

    Map<String, Object> params = new HashMap<>();
    params.put("outlierDocumentUuid", outlierDocumentUuid);
    params.put("firstOutlierDocType", firstOutlierDocType);

    List<SourceDocument> sourceDocuments = this.genericDaoSupport.searchForList(queryString, params);
    if (CollectionUtils.isEmpty(sourceDocuments)) {
      return null;
    }
    return sourceDocuments.get(0);
  }


}




