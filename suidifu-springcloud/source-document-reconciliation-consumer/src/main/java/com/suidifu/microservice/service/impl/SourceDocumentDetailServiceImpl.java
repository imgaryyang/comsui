package com.suidifu.microservice.service.impl;

import com.demo2do.core.persistence.support.Filter;
import com.demo2do.core.service.impl.GenericServiceImpl;
import com.suidifu.microservice.entity.SourceDocumentDetail;
import com.suidifu.microservice.service.SourceDocumentDetailService;
import com.suidifu.owlman.microservice.enumation.SourceDocumentDetailStatus;
import com.zufangbao.sun.utils.StringUtils;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

@Log4j2
@Service("sourceDocumentDetailService")
public class SourceDocumentDetailServiceImpl extends GenericServiceImpl<SourceDocumentDetail> implements SourceDocumentDetailService {

    @Override
    public SourceDocumentDetail getSourceDocumentDetail(String sourceDocumentDetailUuid) {
        if (StringUtils.isEmpty(sourceDocumentDetailUuid)) {
            return null;
        }
        Filter filter = new Filter();
        filter.addEquals("uuid", sourceDocumentDetailUuid);
        List<SourceDocumentDetail> detailList = this.list(SourceDocumentDetail.class, filter);
        if (CollectionUtils.isEmpty(detailList)) {
            return null;
        }
        return detailList.get(0);
    }

    @Override
    public List<SourceDocumentDetail> getValidDeductSourceDocumentDetailsBySourceDocumentUuid(String sourceDocumentUuid) {
        if (StringUtils.isEmpty(sourceDocumentUuid)) {
            return Collections.emptyList();
        }
        Map<String, Object> params = new HashMap<>();
        String hql = " FROM SourceDocumentDetail WHERE sourceDocumentUuid = :sourceDocumentUuid and status <> :status";
        params.put("sourceDocumentUuid", sourceDocumentUuid);
        params.put("status", SourceDocumentDetailStatus.INVALID);

        return genericDaoSupport.searchForList(hql, params);
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean exist(String sourceDocumentUuid, String firstType, String secondNo) {
        log.info("\nsourceDocumentUuid is:{},firstType is:{},secondNo is:{}\n",
                sourceDocumentUuid, firstType, secondNo);

        if (StringUtils.isEmpty(sourceDocumentUuid) ||
                StringUtils.isEmpty(secondNo) || StringUtils.isEmpty(firstType)) {
            return false;
        }

        String hql = "Select id FROM SourceDocumentDetail WHERE sourceDocumentUuid = :sourceDocumentUuid "
                + " AND firstType=:firstType AND secondNo = :secondNo AND status <> :status ";
        Map<String, Object> params = new HashMap<>();
        params.put("sourceDocumentUuid", sourceDocumentUuid);
        params.put("firstType", firstType);
        params.put("secondNo", secondNo);
        params.put("status", SourceDocumentDetailStatus.INVALID);
        List<Long> detailIds = genericDaoSupport.searchForList(hql, params, 0, 1);
        return !CollectionUtils.isEmpty(detailIds);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<String> getDetailUuidsBySourceDocumentUuid(String sourceDocumentUuid, String secondNo) {
        log.info("\nsourceDocumentUuid is:{},secondNo is:{}\n",
                sourceDocumentUuid, secondNo);

        if (StringUtils.isEmpty(sourceDocumentUuid) || StringUtils.isEmpty(secondNo)) {
            return Collections.emptyList();
        }
        Map<String, Object> params = new HashMap<>();
        String hql = "SELECT uuid FROM SourceDocumentDetail WHERE sourceDocumentUuid = :sourceDocumentUuid " +
                "AND secondNo = :secondNo AND status <> :status ORDER BY id";
        params.put("sourceDocumentUuid", sourceDocumentUuid);
        params.put("secondNo", secondNo);
        params.put("status", SourceDocumentDetailStatus.INVALID);

        return this.genericDaoSupport.searchForList(hql, params);
    }
}