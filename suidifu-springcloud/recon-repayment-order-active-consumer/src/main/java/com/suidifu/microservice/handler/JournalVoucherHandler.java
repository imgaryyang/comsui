package com.suidifu.microservice.handler;

import com.suidifu.microservice.model.ReconciliationRepaymentContext;
import com.zufangbao.sun.ledgerbook.*;

public interface JournalVoucherHandler {

	void recover_asset_by_reconciliation_context_repayment_order(ReconciliationRepaymentContext context) throws LackBusinessVoucherException, AlreadayCarryOverException, InvalidLedgerException, InsufficientPenaltyException, InsufficientBalanceException, InvalidCarryOverException ;

}
