
package com.suidifu.microservice.service;

import java.util.List;
import com.demo2do.core.service.GenericService;
import com.suidifu.microservice.entity.JournalVoucher;
import com.suidifu.owlman.microservice.enumation.JournalVoucherType;
import com.zufangbao.sun.entity.account.AccountSide;

public interface JournalVoucherService extends GenericService<JournalVoucher>{

	
	JournalVoucher getJournalVoucherBySourceDocumentUuidAndType(String sourceDocumentUuid, String sourceDocumentDetailUuid, String billingPlanUuid);
	
	/*List<JournalVoucher> getJournalVoucherListByTypeAndBillingPlanUuid(String billingPlanUuid, JournalVoucherType journalVoucherType);*/
	
	List<JournalVoucher> getJournalVoucherListByBacthUuid(String bacthUuid);
	
	List<JournalVoucher> getJournalVoucherListByTypeAndBillingPlanUuid(
        String billingPlanUuid, JournalVoucherType journalVoucherType, AccountSide accountSide);
	
}

