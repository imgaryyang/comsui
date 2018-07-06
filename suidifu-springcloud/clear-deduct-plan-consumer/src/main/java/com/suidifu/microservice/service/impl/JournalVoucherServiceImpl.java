package com.suidifu.microservice.service.impl;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import com.demo2do.core.service.impl.GenericServiceImpl;
import com.suidifu.microservice.entity.JournalVoucher;
import com.suidifu.microservice.service.JournalVoucherService;
import com.suidifu.owlman.microservice.enumation.JournalVoucherStatus;
import com.suidifu.owlman.microservice.enumation.JournalVoucherType;
import com.zufangbao.sun.entity.account.AccountSide;

@Service("journalVoucherService")
public class JournalVoucherServiceImpl extends GenericServiceImpl<JournalVoucher> implements JournalVoucherService {

	
	private static final Log logger = LogFactory.getLog(JournalVoucherServiceImpl.class);
	
	@Override
	public JournalVoucher getJournalVoucherBySourceDocumentUuidAndType(
			String sourceDocumentUuid, String sourceDocumentDetailUuid,String billingPlanUuid) {
		
		if(StringUtils.isEmpty(sourceDocumentUuid) || StringUtils.isEmpty(sourceDocumentDetailUuid) || StringUtils.isEmpty(billingPlanUuid) ){
			return null;
		}
		String query_hql = "FROM JournalVoucher WHERE sourceDocumentUuid = :sourceDocumentDetailUuid AND sourceDocumentIdentity = :sourceDocumentUuid AND journalVoucherType = :journalVoucherType And billingPlanUuid = :billingPlanUuid And status = :issued";
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("sourceDocumentUuid", sourceDocumentUuid);
		params.put("sourceDocumentDetailUuid", sourceDocumentDetailUuid);
		params.put("billingPlanUuid", billingPlanUuid);
		params.put("journalVoucherType", JournalVoucherType.THIRD_PARTY_DEDUCT_VOUCHER);
		params.put("issued", JournalVoucherStatus.VOUCHER_ISSUED);
		List<JournalVoucher> journalVoucherList = genericDaoSupport.searchForList(query_hql,params);
		if(CollectionUtils.isEmpty(journalVoucherList)){
			return null;
		}
		return journalVoucherList.get(0);
		
	}

	/*@Override
	public List<JournalVoucher> getJournalVoucherListByTypeAndBillingPlanUuid(
			String billingPlanUuid, JournalVoucherType journalVoucherType) {
		
		if(StringUtils.isEmpty(billingPlanUuid)){
			return Collections.emptyList();
		}
		
		String query_hql = "FROM JournalVoucher WHERE billingPlanUuid =:billingPlanUuid AND status =:issued AND journalVoucherType =:journalVoucherType";
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("billingPlanUuid", billingPlanUuid);
		params.put("issued", JournalVoucherStatus.VOUCHER_ISSUED);
		params.put("journalVoucherType", journalVoucherType);
		return genericDaoSupport.searchForList(query_hql,params);
	}*/
	
	@Override
	public List<JournalVoucher> getJournalVoucherListByBacthUuid(String bacthUuid) {

		if(StringUtils.isEmpty(bacthUuid)){
			return Collections.emptyList();
		}
		String hql = " FROM JournalVoucher where batchUuid =:batchUuid and status != :status";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("batchUuid", bacthUuid);
		params.put("status", JournalVoucherStatus.VOUCHER_LAPSE);
		return this.genericDaoSupport.searchForList(hql, params);
	}
	
	@Override
    public List<JournalVoucher> getJournalVoucherListByTypeAndBillingPlanUuid(
            String billingPlanUuid, JournalVoucherType journalVoucherType, AccountSide accountSide) {
        
        if(StringUtils.isEmpty(billingPlanUuid)){
            return Collections.emptyList();
        }
        
        String query_hql = "FROM JournalVoucher WHERE billingPlanUuid =:billingPlanUuid AND status =:issued  AND accountSide =:accountSide AND journalVoucherType =:journalVoucherType";
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("billingPlanUuid", billingPlanUuid);
        params.put("accountSide", accountSide);
        params.put("issued", JournalVoucherStatus.VOUCHER_ISSUED);
        params.put("journalVoucherType", journalVoucherType);
        return genericDaoSupport.searchForList(query_hql,params);
    }
	
}