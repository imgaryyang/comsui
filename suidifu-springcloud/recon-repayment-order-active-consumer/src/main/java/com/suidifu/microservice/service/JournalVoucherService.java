
package com.suidifu.microservice.service;

import com.demo2do.core.service.GenericService;
import com.suidifu.microservice.model.JournalVoucher;
import com.suidifu.owlman.microservice.enumation.JournalVoucherType;
import com.suidifu.owlman.microservice.model.JournalVoucherResolver;
import java.util.Date;
import java.util.List;

public interface JournalVoucherService extends GenericService<JournalVoucher> {

    List<JournalVoucher> getJournalVoucherListByTypeAndAssetSetUuid(String assetSetUuid);
    void transfer_and_fill_compensate_active_payment_info_after_auto_remittance_repayment_order(Long companyId,
                                                                                                String assetSetUuid, JournalVoucherType journalVoucherType, JournalVoucherResolver resolver, Date actualRecycleTime, String cashFlowIdentity);
    List<JournalVoucher> getInforceVirtualAccountTransferRepaymentPlanJV(Long companyId, String billingPlanUuid);

}

