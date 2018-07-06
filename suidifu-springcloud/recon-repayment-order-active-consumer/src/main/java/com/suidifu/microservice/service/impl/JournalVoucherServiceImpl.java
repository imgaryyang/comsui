package com.suidifu.microservice.service.impl;

import com.demo2do.core.service.impl.GenericServiceImpl;
import com.suidifu.microservice.model.JournalVoucher;
import com.suidifu.microservice.service.JournalVoucherService;
import com.suidifu.owlman.microservice.enumation.JournalVoucherStatus;
import com.suidifu.owlman.microservice.enumation.JournalVoucherType;
import com.suidifu.owlman.microservice.model.JournalVoucherResolver;
import com.zufangbao.sun.entity.account.AccountSide;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;


@Service("journalVoucherService")
public class JournalVoucherServiceImpl extends GenericServiceImpl<JournalVoucher> implements JournalVoucherService {

    @Override
    public List<JournalVoucher> getJournalVoucherListByTypeAndAssetSetUuid(String assetSetUuid) {
        if(StringUtils.isEmpty(assetSetUuid)){
            return Collections.emptyList();
        }

        String query_hql = "FROM JournalVoucher WHERE billingPlanUuid =:assetSetUuid AND status =:issued AND journalVoucherType !=:journalVoucherType";
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("assetSetUuid", assetSetUuid);
        params.put("issued", JournalVoucherStatus.VOUCHER_ISSUED);
        params.put("journalVoucherType", JournalVoucherType.VIRTUAL_ACCOUNT_TRANSFER_REPAYMENT);
        return genericDaoSupport.searchForList(query_hql,params);
    }


    @Override
    public void transfer_and_fill_compensate_active_payment_info_after_auto_remittance_repayment_order(
            Long companyId, String assetSetUuid,
            JournalVoucherType journalVoucherType,
            JournalVoucherResolver resolver, Date actualRecycleTime, String cashFlowIdentity) {

        List<JournalVoucher> journalVoucherList = getInforceVirtualAccountTransferRepaymentPlanJV(companyId, assetSetUuid);
        for (JournalVoucher journalVoucher : journalVoucherList) {
            journalVoucher.compensate_active_payment_info_into_existed_auto_remittance_repayment_order( journalVoucherType, resolver, actualRecycleTime, cashFlowIdentity);
            saveOrUpdate(journalVoucher);
        }

    }

    @Override
    public List<JournalVoucher> getInforceVirtualAccountTransferRepaymentPlanJV(Long companyId,
                                                                                String billingPlanUuid) {
        return getInforceJournalVoucherList(companyId, billingPlanUuid, AccountSide.DEBIT,
                JournalVoucherType.VIRTUAL_ACCOUNT_TRANSFER_REPAYMENT);
    }

    private List<JournalVoucher> getInforceJournalVoucherList(Long companyId, String billingPlanUuid,
                                                              AccountSide accountSide, JournalVoucherType journalVoucherType) {
        StringBuffer query = new StringBuffer(
                "From JournalVoucher where companyId=:companyId AND accountSide=:accountSide AND billingPlanUuid=:billingPlanUuid "
                        + " AND journalVoucherType=:journalVoucherType AND status!=:lapse ");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("companyId", companyId);
        params.put("accountSide", accountSide);
        params.put("billingPlanUuid", billingPlanUuid);
        params.put("journalVoucherType", journalVoucherType);
        params.put("lapse", JournalVoucherStatus.VOUCHER_LAPSE);
        return genericDaoSupport.searchForList(query.toString(),params);
    }

}