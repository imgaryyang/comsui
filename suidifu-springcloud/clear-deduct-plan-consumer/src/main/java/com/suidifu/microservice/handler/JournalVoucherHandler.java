package com.suidifu.microservice.handler;

import com.suidifu.microservice.entity.ReconciliationContext;
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
	
}
