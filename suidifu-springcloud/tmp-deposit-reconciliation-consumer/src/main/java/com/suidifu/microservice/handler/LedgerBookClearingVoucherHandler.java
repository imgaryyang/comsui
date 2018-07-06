package com.suidifu.microservice.handler;


import com.zufangbao.sun.entity.account.DepositeAccountInfo;
import com.zufangbao.sun.entity.directbank.business.CashFlow;
import com.zufangbao.sun.ledgerbook.LedgerTradeParty;

/**
 * @author louguanyang
 */
public interface LedgerBookClearingVoucherHandler {

  void clearingVoucherWriteOff(String ledgerBookNo, String deductApplicationUuid, LedgerTradeParty ledgerTradeParty,
      String journalVoucherUuid, String businessVoucherUuid, String sourceDocumentDetailUuid,
      DepositeAccountInfo depositeAccountInfo, CashFlow cashFlow);
}
