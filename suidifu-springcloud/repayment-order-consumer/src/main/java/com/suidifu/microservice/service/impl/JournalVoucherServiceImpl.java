package com.suidifu.microservice.service.impl;

import com.demo2do.core.persistence.support.Filter;
import com.demo2do.core.service.impl.GenericServiceImpl;
import com.suidifu.microservice.entity.JournalVoucher;
import com.suidifu.microservice.service.JournalVoucherService;
import com.suidifu.owlman.microservice.enumation.JournalVoucherCompleteness;
import com.suidifu.owlman.microservice.enumation.JournalVoucherStatus;
import com.suidifu.owlman.microservice.enumation.JournalVoucherType;
import com.suidifu.owlman.microservice.model.JournalAccount;
import com.zufangbao.sun.entity.account.AccountSide;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

@Service("journalVoucherService")
public class JournalVoucherServiceImpl extends GenericServiceImpl<JournalVoucher> implements JournalVoucherService {
    @Override
    public JournalVoucher getNoLapseJournalVoucherBy(String sourceDocumentDetailUuid) {
        if (StringUtils.isEmpty(sourceDocumentDetailUuid)) {
            return null;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("detailUuid", sourceDocumentDetailUuid);
        params.put("lapse", JournalVoucherStatus.VOUCHER_LAPSE);
        List<JournalVoucher> jouralVoucherList = genericDaoSupport.searchForList("FROM JournalVoucher where sourceDocumentUuid=:detailUuid AND status!=:lapse",
                params);
        if (CollectionUtils.isEmpty(jouralVoucherList)) {
            return null;
        }
        return jouralVoucherList.get(0);
    }

    //特定条件下使用（线上扣款的作废）
    @Override
    public void lapseJournalVoucherBy(String sourceDocumentIdentity) {
        String hql = "update JournalVoucher set status = :statusLapse where sourceDocumentIdentity = :sourceDocumentIdentity and status= :statusCreate";
        Map<String, Object> params = new HashMap<>();
        params.put("statusLapse", JournalVoucherStatus.VOUCHER_LAPSE);
        params.put("sourceDocumentIdentity", sourceDocumentIdentity);
        params.put("statusCreate", JournalVoucherStatus.VOUCHER_CREATED);
        genericDaoSupport.executeHQL(hql, params);
    }

    @Override
    public List<JournalVoucher> getJournalVoucherBy(String orderUuid, String itemUuid) {
        if (StringUtils.isEmpty(orderUuid) || StringUtils.isEmpty(itemUuid)) {
            return null;
        }
        String hql = "From JournalVoucher where businessVoucherUuid = :businessVoucherUuid and status=:journalVoucherStatus"
                + " and notificationRecordUuid=:notificationRecordUuid";
        Map<String, Object> params = new HashMap<>();
        params.put("businessVoucherUuid", orderUuid);
        params.put("journalVoucherStatus", JournalVoucherStatus.VOUCHER_ISSUED);
        params.put("notificationRecordUuid", itemUuid);
        return genericDaoSupport.searchForList(hql, params);
    }

    @Override
    public BigDecimal getIssuedAmountByIssueJVAndJVType(String cashFlowUuid,
                                                        JournalAccount journalAccount,
                                                        JournalVoucherType journalVoucherType) {
        if (StringUtils.isEmpty(cashFlowUuid)) {
            return BigDecimal.ZERO;
        }
        List<JournalVoucher> jvList = getJournalVoucherBy(cashFlowUuid, journalAccount.getAccountSide(), null, null,
                JournalVoucherStatus.VOUCHER_ISSUED);
        if (CollectionUtils.isEmpty(jvList)) {
            return BigDecimal.ZERO;
        }
        return getTotalAmountFrom(jvList);
    }

    @Override
    public List<JournalVoucher> getJournalVoucherBy(String cashFlowUuid,
                                                    AccountSide accountSide,
                                                    JournalVoucherCompleteness completeness,
                                                    Long companyId,
                                                    JournalVoucherStatus status) {
        Filter filter = new Filter();
        if (!StringUtils.isEmpty(cashFlowUuid)) {
            filter.addEquals("cashFlowUuid", cashFlowUuid);
        }
        if (accountSide != null) {
            filter.addEquals("accountSide", accountSide);
        }
        if (completeness != null) {
            filter.addEquals("completeness", completeness);
        }
        if (companyId != null) {
            filter.addEquals("companyId", companyId);
        }
        if (status != null) {
            filter.addEquals("status", status);
        }
        return this.list(JournalVoucher.class, filter);
    }

    private BigDecimal getTotalAmountFrom(List<JournalVoucher> jvList) {
        BigDecimal totalAmount = BigDecimal.ZERO;
        if (CollectionUtils.isEmpty(jvList)) {
            return totalAmount;
        }
        for (JournalVoucher journalVoucher : jvList) {
            if (journalVoucher == null || journalVoucher.getBookingAmount() == null) {
                continue;
            }
            totalAmount = totalAmount.add(journalVoucher.getBookingAmount());
        }
        return totalAmount;
    }
}