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
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

@Service("sourceDocumentDetailService")
public class SourceDocumentDetailServiceImpl extends GenericServiceImpl<SourceDocumentDetail> implements SourceDocumentDetailService {
    @Override
    public SourceDocumentDetail getSourceDocumentDetailBy(String sourceDocumentDetailUuid) {
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
    public List<String> getValidDeductSourceDocumentDetailUuidsBy(String sourceDocumentUuid) {
        if (StringUtils.isEmpty(sourceDocumentUuid)) {
            return Collections.emptyList();
        }

        Map<String, Object> params = new HashMap<>();
        String hql = "SELECT sdd.uuid FROM SourceDocumentDetail AS sdd,AssetSet AS ass WHERE sdd.repaymentPlanNo=ass.singleLoanContractNo AND sdd.sourceDocumentUuid=:sourceDocumentUuid AND sdd.status <> :status ORDER BY ass.currentPeriod ASC";
        params.put("sourceDocumentUuid", sourceDocumentUuid);
        params.put("status", SourceDocumentDetailStatus.INVALID);
        return genericDaoSupport.searchForList(hql, params);
    }

    //特定条件下使用（线上扣款的作废）
    @Override
    public void lapseSourceDocumentDetailBy(String sourceDocumentUuid) {
        String hql = "update SourceDocumentDetail set status = :statusLapse where sourceDocumentUuid = :sourceDocumentUuid and status= :statusUnSuccess";
        Map<String, Object> params = new HashMap<>();
        params.put("statusLapse", SourceDocumentDetailStatus.INVALID);
        params.put("sourceDocumentUuid", sourceDocumentUuid);
        params.put("statusUnSuccess", SourceDocumentDetailStatus.UNSUCCESS);
        genericDaoSupport.executeHQL(hql, params);
    }

    @Override
    public void updateSourceDocumentUuid(String voucherUuid, String sourceDocumentUuid, String contractUniqueId) {
        if (StringUtils.isEmpty(voucherUuid) || StringUtils.isEmpty(sourceDocumentUuid)) {
            return;
        }
        StringBuilder hql = new StringBuilder("UPDATE SourceDocumentDetail "
                + "SET sourceDocumentUuid = :sourceDocumentUuid "
                + "WHERE voucherUuid = :voucherUuid "
                + "AND (sourceDocumentUuid IS NULL "
                + "OR sourceDocumentUuid = '')");
        Map<String, Object> params = new HashMap<>();
        params.put("voucherUuid", voucherUuid);
        params.put("sourceDocumentUuid", sourceDocumentUuid);

        if (StringUtils.isNotEmpty(contractUniqueId)) {
            params.put("contractUniqueId", contractUniqueId);
            hql.append("AND contractUniqueId = :contractUniqueId");
        }
        genericDaoSupport.executeHQL(hql.toString(), params);
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean exist(String sourceDocumentUuid, String firstType, String secondNo) {
        if (StringUtils.isEmpty(sourceDocumentUuid) || StringUtils.isEmpty(secondNo) || StringUtils.isEmpty(firstType)) {
            return false;
        }
        String hql = "Select id FROM SourceDocumentDetail WHERE sourceDocumentUuid = :sourceDocumentUuid "
                + " AND firstType=:firstType AND secondNo = :secondNo AND status <> :status ";
        Map<String, Object> params = new HashMap<>();
        params.put("sourceDocumentUuid", sourceDocumentUuid);
        params.put("firstType", firstType);
        params.put("secondNo", secondNo);
        params.put("status", SourceDocumentDetailStatus.INVALID);
        List<Long> detailIds = this.genericDaoSupport.searchForList(hql, params, 0, 1);
        return !CollectionUtils.isEmpty(detailIds);
    }
}