package com.suidifu.microservice.handler;

import java.math.BigDecimal;

/**
 * @author louguanyang
 */
public interface LedgerBookVirtualAccountHandler {

  public BigDecimal getBalanceOfCustomer(String ledgerBookNo, String customerUuid);

  public BigDecimal getBalanceOfFrozenCapital(String ledgerBookNo, String customerUuid, String sourceDocumentUuid,
      String tmpDepositDocUuid);

  public BigDecimal getBalanceOfTmpDepositDoc(String ledgerBookNo, String tmpDepositDocUuid);
}
