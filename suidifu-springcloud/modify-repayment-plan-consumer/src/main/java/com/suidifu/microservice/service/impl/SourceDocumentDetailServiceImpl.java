package com.suidifu.microservice.service.impl;

import com.demo2do.core.persistence.support.Filter;
import com.demo2do.core.service.impl.GenericServiceImpl;
import com.suidifu.microservice.entity.SourceDocumentDetail;
import com.suidifu.microservice.service.SourceDocumentDetailService;
import com.suidifu.owlman.microservice.enumation.SourceDocumentDetailStatus;
import com.zufangbao.sun.entity.contract.ContractSourceDocumentDetailMapper;
import com.zufangbao.sun.utils.StringUtils;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

@Service("sourceDocumentDetailService")
public class SourceDocumentDetailServiceImpl extends
    GenericServiceImpl<SourceDocumentDetail> implements SourceDocumentDetailService {


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

    return this.genericDaoSupport.searchForList(hql, params);
  }

  @Override
  public List<ContractSourceDocumentDetailMapper> getContractUuidSourceDocumentDetailUuidMapper(
      List<String> sourceDocumentDetailUuidList) {

    String inListStr = org.apache.commons.lang.StringUtils.join(sourceDocumentDetailUuidList, "','");

    String sql = "SELECT sdd.uuid AS sourceDocumentDetailUuid,"
        + "(case when c.uuid is not null THEN c.uuid else sdd.financial_contract_uuid END) AS contractUuid "
        + " FROM source_document_detail sdd LEFT JOIN contract c ON sdd.contract_unique_id = c.unique_id  WHERE sdd.uuid IN ('"
        + inListStr + "') ";

    List<ContractSourceDocumentDetailMapper> result = this.genericDaoSupport
        .queryForList(sql, ContractSourceDocumentDetailMapper.class);

    return result;
  }

}
