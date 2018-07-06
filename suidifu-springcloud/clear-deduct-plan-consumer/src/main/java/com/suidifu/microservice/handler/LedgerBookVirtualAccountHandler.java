package com.suidifu.microservice.handler;

import java.math.BigDecimal;

public interface LedgerBookVirtualAccountHandler {

	public BigDecimal get_balance_of_customer(String ledgerBookNo, String customerUuid);

	public BigDecimal get_balance_of_tmp_deposit_doc(String ledgerBookNo, String tmpDepositDocUuid);
}
