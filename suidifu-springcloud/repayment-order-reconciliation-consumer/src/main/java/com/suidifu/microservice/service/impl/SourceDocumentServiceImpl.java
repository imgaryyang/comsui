package com.suidifu.microservice.service.impl;

import com.demo2do.core.service.impl.GenericServiceImpl;
import com.suidifu.microservice.entity.SourceDocument;
import com.suidifu.microservice.service.SourceDocumentService;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

@Log4j2
@Service("sourceDocumentService")
public class SourceDocumentServiceImpl extends
        GenericServiceImpl<SourceDocument> implements SourceDocumentService {
    @Override
    public SourceDocument getSourceDocumentBy(String sourceDocumentUuid) {
        if (StringUtils.isEmpty(sourceDocumentUuid)) {
            return null;
        }

        List<SourceDocument> sourceDocumentList = genericDaoSupport.searchForList(
                "FROM SourceDocument WHERE sourceDocumentUuid =:sourceDocumentUuid",
                "sourceDocumentUuid", sourceDocumentUuid);
        if (CollectionUtils.isEmpty(sourceDocumentList)) {
            return null;
        }
        return sourceDocumentList.get(0);
    }
}