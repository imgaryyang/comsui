package com.suidifu.microservice.service.impl;

import com.demo2do.core.service.impl.GenericServiceImpl;
import com.suidifu.microservice.entity.JournalVoucher;
import com.suidifu.microservice.service.JournalVoucherService;
import com.suidifu.owlman.microservice.enumation.JournalVoucherStatus;
import com.suidifu.owlman.microservice.enumation.JournalVoucherType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

@Service("journalVoucherService")
public class JournalVoucherServiceImpl extends GenericServiceImpl<JournalVoucher> implements
    JournalVoucherService {

  @Override
  public JournalVoucher getJournalVoucherBySourceDocumentUuidAndType(
      String sourceDocumentUuid, String sourceDocumentDetailUuid, String billingPlanUuid) {

    if (StringUtils.isEmpty(sourceDocumentUuid) || StringUtils.isEmpty(sourceDocumentDetailUuid)
        || StringUtils.isEmpty(billingPlanUuid)) {
      return null;
    }
    String queryHql = "FROM JournalVoucher WHERE sourceDocumentUuid = :sourceDocumentDetailUuid AND sourceDocumentIdentity = :sourceDocumentUuid AND journalVoucherType = :journalVoucherType And billingPlanUuid = :billingPlanUuid And status = :issued";
    Map<String, Object> params = new HashMap<>();
    params.put("sourceDocumentUuid", sourceDocumentUuid);
    params.put("sourceDocumentDetailUuid", sourceDocumentDetailUuid);
    params.put("billingPlanUuid", billingPlanUuid);
    params.put("journalVoucherType", JournalVoucherType.THIRD_PARTY_DEDUCT_VOUCHER);
    params.put("issued", JournalVoucherStatus.VOUCHER_ISSUED);
    List<JournalVoucher> journalVoucherList = genericDaoSupport.searchForList(queryHql, params);
    if (CollectionUtils.isEmpty(journalVoucherList)) {
      return null;
    }
    return journalVoucherList.get(0);

  }

}