/**
 * 
 */
package com.suidifu.microservice.handler.impl;

import com.suidifu.microservice.entity.JournalVoucher;
import com.suidifu.microservice.handler.TotalReceivableBillsHandler;
import com.suidifu.microservice.service.JournalVoucherService;
import com.suidifu.owlman.microservice.enumation.JournalVoucherType;
import com.zufangbao.sun.entity.account.AccountSide;
import com.zufangbao.sun.yunxin.entity.audit.ClearingVoucher;
import com.zufangbao.sun.yunxin.entity.audit.ClearingVoucherStatus;
import com.zufangbao.sun.yunxin.entity.audit.TotalReceivableBills;
import com.zufangbao.sun.yunxin.service.ClearingVoucherService;
import com.zufangbao.sun.yunxin.service.audit.TotalReceivableBillsService;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author hjl
 *
 */
@Component("totalReceivableBillsHandler")
public class TotalReceivableBillsHandlerImpl implements TotalReceivableBillsHandler{
	@Autowired
	private TotalReceivableBillsService totalReceivableBillsService;
	@Autowired
	private JournalVoucherService journalvoucherSerivce;
	
	@Autowired
	private ClearingVoucherService clearingVoucherService;
	
	@Override
	public List<TotalReceivableBills> queryAllOfSameClearingVoucherByOne(TotalReceivableBills totalReceivableBills) {
      List<TotalReceivableBills> totalReceivableBillsList = new ArrayList<>();
      if(totalReceivableBills == null || totalReceivableBills.isVaild()==false){
          return totalReceivableBillsList;
      }
      List<JournalVoucher> JournalVouchers = journalvoucherSerivce.getJournalVoucherListByTypeAndBillingPlanUuid(totalReceivableBills.getUuid(), JournalVoucherType.TRANSFER_BILL_BY_CLEARING_CASH_FLOW, AccountSide.DEBIT);
      if(CollectionUtils.isNotEmpty(JournalVouchers) && JournalVouchers.size()>1){
          TotalReceivableBills bill= totalReceivableBillsService.getTotalReceivableBillsByUuid(JournalVouchers.get(0).getBillingPlanUuid());
          totalReceivableBillsList.add(bill);
      }
      if(CollectionUtils.isNotEmpty(JournalVouchers) && JournalVouchers.size()==1){
          List<JournalVoucher> sameAuditJobJournalVouchers  = journalvoucherSerivce.getJournalVoucherListByBacthUuid(JournalVouchers.get(0).getBatchUuid());
          for(JournalVoucher journalVoucher:sameAuditJobJournalVouchers){
              TotalReceivableBills bill= totalReceivableBillsService.getTotalReceivableBillsByUuid(journalVoucher.getBillingPlanUuid());
              totalReceivableBillsList.add(bill);
          }
      }
      return totalReceivableBillsList;
  }
  
  @Override
  public ClearingVoucher queryClearingVoucherByTotalReceivableBills(TotalReceivableBills totalReceivableBills,ClearingVoucherStatus clearingVoucherStatus) {
      if(totalReceivableBills == null || totalReceivableBills.isVaild()==false){
          return null;
      }
      List<JournalVoucher> journalVouchers = journalvoucherSerivce.getJournalVoucherListByTypeAndBillingPlanUuid(totalReceivableBills.getUuid(), JournalVoucherType.TRANSFER_BILL_BY_CLEARING_CASH_FLOW, AccountSide.DEBIT);
      if(CollectionUtils.isEmpty(journalVouchers)){
          return null;
      }
      String batchUuid = journalVouchers.get(0).getBatchUuid();
      ClearingVoucher clearingVoucher=clearingVoucherService.queryClearingVoucherByBatchUuid(batchUuid, clearingVoucherStatus);
      return clearingVoucher;
  }

}
