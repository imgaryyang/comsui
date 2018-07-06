package com.suidifu.microservice.handler;

import java.math.BigDecimal;

public interface LedgerBookVirtualAccountHandler {
    BigDecimal getBalanceOfCustomer(String ledgerBookNo, String customerUuid);

    BigDecimal getBalanceOfFrozenCapital(String ledgerBookNo, String customerUuid,
                                         String sourceDocumentUuid, String tmpDepositDocUuid);
}