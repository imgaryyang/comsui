package com.suidifu.microservice.handler;

import com.suidifu.microservice.model.ReconciliationContext;
import com.suidifu.microservice.model.ReconciliationRepaymentContext;
import com.zufangbao.sun.ledgerbook.AlreadayCarryOverException;
import com.zufangbao.sun.ledgerbook.InsufficientBalanceException;
import com.zufangbao.sun.ledgerbook.InsufficientPenaltyException;
import com.zufangbao.sun.ledgerbook.InvalidCarryOverException;
import com.zufangbao.sun.ledgerbook.InvalidLedgerException;
import com.zufangbao.sun.ledgerbook.LackBusinessVoucherException;

public interface JournalVoucherHandler {
	void recover_asset_by_reconciliation_context(ReconciliationContext context)
			throws LackBusinessVoucherException, AlreadayCarryOverException,
			InvalidLedgerException, InsufficientPenaltyException,
			InsufficientBalanceException, InvalidCarryOverException;

	void recover_asset_by_reconciliation_context_repayment_order(
      ReconciliationRepaymentContext context) throws LackBusinessVoucherException, AlreadayCarryOverException, InvalidLedgerException, InsufficientPenaltyException, InsufficientBalanceException, InvalidCarryOverException ;
	
}
