package com.suidifu.microservice.service.impl;

import com.demo2do.core.persistence.support.Filter;
import com.demo2do.core.service.impl.GenericServiceImpl;
import com.suidifu.microservice.entity.JournalVoucher;
import com.suidifu.microservice.service.JournalVoucherService;
import com.suidifu.owlman.microservice.enumation.JournalVoucherStatus;
import com.suidifu.owlman.microservice.enumation.JournalVoucherType;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.service.OrderService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("journalVoucherService")
public class JournalVoucherServiceImpl extends GenericServiceImpl<JournalVoucher> implements
		JournalVoucherService {

	
	private static final Log logger = LogFactory.getLog(JournalVoucherServiceImpl.class);
	
	@Autowired
	private OrderService orderService;
	@Autowired
	private FinancialContractService financialContractService;

	@Override
	public void save(JournalVoucher journalVoucher) {
		genericDaoSupport.save(journalVoucher);
	}

	@Override
	public List<JournalVoucher> getJournalVoucherBySourceDocumentUuid(String sourceDocumentUuid) {
		Filter filter = new Filter();
		filter.addEquals("sourceDocumentUuid", sourceDocumentUuid);
		return  this.list(JournalVoucher.class, filter);
	}


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

	@Override
	public JournalVoucher getInforceJournalVoucher(String SourceDocumentDetailUuid) {
		if(StringUtils.isEmpty(SourceDocumentDetailUuid)){
			return null;
		}
		StringBuffer stringBuffer = new StringBuffer("FROM JournalVoucher where sourceDocumentUuid=:detailUuid"
				+ " AND status!=:lapse");
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("detailUuid", SourceDocumentDetailUuid);
		params.put("lapse", JournalVoucherStatus.VOUCHER_LAPSE);
		List<JournalVoucher> jouralVoucherList = genericDaoSupport.searchForList(stringBuffer.toString(),params);
		if(CollectionUtils.isEmpty(jouralVoucherList)){
			return null;
		}
		return jouralVoucherList.get(0);
	}

}