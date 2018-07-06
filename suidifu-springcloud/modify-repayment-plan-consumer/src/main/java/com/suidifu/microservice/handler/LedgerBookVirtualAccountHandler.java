package com.suidifu.microservice.handler;

import java.math.BigDecimal;

/**
 * @author louguanyang at 2018/3/14 12:51
 * @mail louguanyang@hzsuidifu.com
 */
public interface LedgerBookVirtualAccountHandler {

    public BigDecimal get_balance_of_customer(String ledgerBookNo, String customerUuid);
}
