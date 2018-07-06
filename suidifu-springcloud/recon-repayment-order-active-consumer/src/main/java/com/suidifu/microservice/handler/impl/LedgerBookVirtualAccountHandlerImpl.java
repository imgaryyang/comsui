package com.suidifu.microservice.handler.impl;

import com.suidifu.microservice.handler.LedgerBookVirtualAccountHandler;
import com.zufangbao.sun.ledgerbook.ChartOfAccount;
import com.zufangbao.sun.ledgerbook.LedgerItemService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component("ledgerBookVirtualAccountHandler")
public class LedgerBookVirtualAccountHandlerImpl implements LedgerBookVirtualAccountHandler {
	
	@Autowired
	private LedgerItemService ledgerItemService;

	private static Log logger = LogFactory.getLog(LedgerBookVirtualAccountHandlerImpl.class);

	@Override
	public BigDecimal get_balance_of_customer(String ledgerBookNo, String customerUuid) {
		BigDecimal amount = ledgerItemService.getBalancedAmount(ledgerBookNo, ChartOfAccount.FST_LIABILITIES_OF_INDEPENDENT_ACCOUNTS, customerUuid, "", null, null, "", "", "");
		if(amount==null){
			return BigDecimal.ZERO;
		}
		return amount.negate();
	}
}
