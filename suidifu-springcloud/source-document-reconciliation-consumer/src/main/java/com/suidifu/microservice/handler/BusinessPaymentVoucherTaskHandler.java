package com.suidifu.microservice.handler;

import com.suidifu.owlman.microservice.model.SourceDocumentDetailReconciliationParameters;
import com.suidifu.owlman.microservice.model.SourceDocumentReconciliationParameters;
import com.zufangbao.sun.entity.account.VirtualAccount;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.ledgerbook.LedgerBook;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface BusinessPaymentVoucherTaskHandler {
    //task
    VirtualAccount fetchVirtualAccountAndBusinessPaymentVoucherTransfer(String sourceDocumentUuid, BigDecimal bookingAmount, LedgerBook ledgerBook, String financialContractUuid, boolean isRepaymentOrder);

    //task
    boolean unfreezeCapitalAmountOfVoucher(String sourceDocumentUuid, String financialContractNo, LedgerBook book, String tmpDepositDocUuid, String toCreditAccount);

    Map<String, String> getCriticalMarkerV(List<SourceDocumentDetailReconciliationParameters> detailReconciliationParameters);

    void isDetailValid(SourceDocumentReconciliationParameters parameter, Date cashFlowTransactionTime, Boolean isDetailFile);

    void recoverEachFrozenCapitalAmount(String ledgerBookNo, FinancialContract financialContract, String companyCustomerUuid, String jvUuid, String sdUuid, BigDecimal bookingAmount, String tmpDepositDocUuid, String sndSecondNo);
}