package com.suidifu.microservice.handler;


import com.zufangbao.sun.entity.account.DepositeAccountInfo;
import com.zufangbao.sun.entity.directbank.business.CashFlow;
import com.zufangbao.sun.ledgerbook.LedgerTradeParty;

public interface LedgerBookClearingVoucherV2Handler {
	
	void clearing_voucher_write_off(String ledgerBookNo, String deductApplicationUuid,
      LedgerTradeParty ledgerTradeParty, String journalVoucherUuid,
      String businessVoucherUuid, String sourceDocumentDetailUuid,
      DepositeAccountInfo depositeAccountInfo, CashFlow cashFlow);
}
