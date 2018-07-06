
package com.suidifu.microservice.service;

import com.demo2do.core.service.GenericService;
import com.suidifu.microservice.entity.JournalVoucher;
import java.util.List;

public interface JournalVoucherService extends GenericService<JournalVoucher>{
	void save(JournalVoucher journalVoucher);
	List<JournalVoucher> getJournalVoucherBySourceDocumentUuid(String sourceDocumentUuid);

	JournalVoucher getJournalVoucherBySourceDocumentUuidAndType(String sourceDocumentUuid, String sourceDocumentDetailUuid, String billingPlanUuid);

	JournalVoucher getInforceJournalVoucher(String SourceDocumentDetailUuid);

}

