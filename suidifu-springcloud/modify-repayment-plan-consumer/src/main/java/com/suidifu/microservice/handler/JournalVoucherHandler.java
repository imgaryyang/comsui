package com.suidifu.microservice.handler;

import com.suidifu.microservice.model.ReconciliationRepaymentContext;
import com.zufangbao.sun.ledgerbook.AlreadayCarryOverException;
import com.zufangbao.sun.ledgerbook.InsufficientBalanceException;
import com.zufangbao.sun.ledgerbook.InsufficientPenaltyException;
import com.zufangbao.sun.ledgerbook.InvalidCarryOverException;
import com.zufangbao.sun.ledgerbook.InvalidLedgerException;
import com.zufangbao.sun.ledgerbook.LackBusinessVoucherException;

/**
 * @author louguanyang at 2018/3/14 12:37
 * @mail louguanyang@hzsuidifu.com
 */
public interface JournalVoucherHandler {

    void recover_asset_by_reconciliation_context_repayment_order(ReconciliationRepaymentContext context)
        throws LackBusinessVoucherException, AlreadayCarryOverException, InvalidLedgerException, InsufficientPenaltyException, InsufficientBalanceException, InvalidCarryOverException;

}
