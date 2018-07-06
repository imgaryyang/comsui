package com.suidifu.microservice.handler;

import com.zufangbao.sun.entity.account.VirtualAccount;
import com.zufangbao.sun.ledgerbook.LedgerBook;

import java.math.BigDecimal;

public interface TemporaryDepositDocHandler {
    void businessPayCreateTemporaryDepositDoc(String sourceDocumentUuid, BigDecimal writeOffAmount, VirtualAccount virtualAccount, LedgerBook ledgerBook);
}