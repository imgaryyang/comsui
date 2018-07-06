package com.suidifu.microservice.handler;

import com.suidifu.microservice.model.ReconciliationRepaymentContext;
import com.zufangbao.sun.entity.account.VirtualAccount;
import com.zufangbao.sun.ledgerbook.LedgerBook;
import java.math.BigDecimal;

public interface BusinessPaymentVoucherTaskHandler {
    //task
    VirtualAccount fetchVirtualAccountAndBusinessPaymentVoucherTransfer(String sourceDocumentUuid,
                                                                        BigDecimal bookingAmount,
                                                                        LedgerBook ledgerBook,
                                                                        String financialContractUuid,
                                                                        boolean isRepaymentOrder);

    //task
    boolean unfreezeCapitalAmountOfVoucher(String sourceDocumentUuid,
                                           String financialContractNo,
                                           LedgerBook book,
                                           String tmpDepositDocUuid,
                                           String toCreditAccount);

    void recoverEachFrozenCapitalAmount(ReconciliationRepaymentContext context);
}