package com.suidifu.microservice.service.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import com.demo2do.core.service.impl.GenericServiceImpl;
import com.suidifu.microservice.entity.SourceDocumentDetail;
import com.suidifu.microservice.service.SourceDocumentDetailService;
import com.suidifu.owlman.microservice.enumation.SourceDocumentDetailStatus;
import com.zufangbao.sun.utils.StringUtils;

@Service("sourceDocumentDetailService")
public class SourceDocumentDetailServiceImpl extends GenericServiceImpl<SourceDocumentDetail> implements SourceDocumentDetailService {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public List<SourceDocumentDetail> getValidDeductSourceDocumentDetailsBySourceDocumentUuid(String sourceDocumentUuid) {
        if (StringUtils.isEmpty(sourceDocumentUuid)) {
            return Collections.emptyList();
        }
        Map<String, Object> params = new HashMap<>();
        String hql = " FROM SourceDocumentDetail WHERE sourceDocumentUuid = :sourceDocumentUuid and status <> :status";
        params.put("sourceDocumentUuid", sourceDocumentUuid);
        params.put("status", SourceDocumentDetailStatus.INVALID);

        return this.genericDaoSupport.searchForList(hql, params);
    }

}
