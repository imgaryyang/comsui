package com.suidifu.microservice.handler.impl;

import com.suidifu.microservice.handler.BusinessPaymentVoucherTaskHandler;
import com.zufangbao.sun.entity.account.DepositeAccountInfo;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.ledgerbook.*;
import com.zufangbao.sun.service.LedgerBookService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

/**
 * @author louguanyang
 */
@Component("businessPaymentVoucherTaskHandler")
public class BusinessPaymentVoucherTaskHandlerImpl implements BusinessPaymentVoucherTaskHandler {


    @Autowired
    private LedgerBookHandler ledgerBookHandler;
    @Autowired
    private LedgerBookService ledgerBookService;
    @Autowired
    BankAccountCache bankAccountCache;

    private static final Log logger = LogFactory.getLog(BusinessPaymentVoucherTaskHandlerImpl.class);

    @Override
    public void recover_each_frozen_capital_amount(String ledgerBookNo, FinancialContract financialContract,
                                                   String companyCustomerUuid, String jvUuid, String sdUuid, BigDecimal bookingAmount, String tmpDepositDocUuid,
                                                   String sndSecondNo) {
        //冻结资金 debit
        LedgerBook book = ledgerBookService.getBookByBookNo(ledgerBookNo);
        String financialCompayUuid = financialContract.getCompany().getUuid();
        AssetCategory assetCategory = AssetConvertor.convertAssetCategory(tmpDepositDocUuid, sndSecondNo);
        LedgerTradeParty frozenCapitalTradeParty = new LedgerTradeParty(companyCustomerUuid,
                financialContract.getCompany().getUuid());
        String batchUuid = UUID.randomUUID().toString();
        Date bookInDate = new Date();
        ledgerBookHandler.book_single_asset_with_batch_uuid(book, frozenCapitalTradeParty, assetCategory, bookingAmount,
                ChartOfAccount.SND_FROZEN_CAPITAL_VOUCHER, com.zufangbao.sun.ledgerbook.AccountSide.DEBIT, jvUuid, "", sdUuid,
                batchUuid, bookInDate);

        //银行存款 credit
        DepositeAccountInfo accountInfo = bankAccountCache.extractFirstBankAccountFrom(financialContract);
        LedgerTradeParty debitTradePary = new LedgerTradeParty(financialCompayUuid, "");
        ledgerBookHandler.book_single_asset_with_batch_uuid(book, debitTradePary, assetCategory, bookingAmount,
                accountInfo.getDeposite_account_name(), com.zufangbao.sun.ledgerbook.AccountSide.CREDIT, jvUuid, "", sdUuid,
                batchUuid, bookInDate);
    }

}
