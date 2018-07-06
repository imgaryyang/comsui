package com.suidifu.microservice.handler;

import java.math.BigDecimal;

public interface LedgerBookVirtualAccountHandler {
	 BigDecimal get_balance_of_customer(String ledgerBookNo, String customerUuid);
}
