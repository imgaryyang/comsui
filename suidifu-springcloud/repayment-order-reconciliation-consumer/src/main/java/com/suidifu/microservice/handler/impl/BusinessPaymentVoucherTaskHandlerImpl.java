package com.suidifu.microservice.handler.impl;

import static com.zufangbao.sun.ledgerbook.ChartOfAccount.SND_FROZEN_CAPITAL_VOUCHER;
import static com.zufangbao.sun.ledgerbook.ChartOfAccount.SND_LIABILITIES_INDEPENDENT_INTER_ACCOUNT_REMITTANCE;

import com.suidifu.microservice.entity.SourceDocument;
import com.suidifu.microservice.handler.BusinessPaymentVoucherTaskHandler;
import com.suidifu.microservice.handler.LedgerBookVirtualAccountHandler;
import com.suidifu.microservice.handler.TemporaryDepositDocHandler;
import com.suidifu.microservice.handler.VirtualAccountHandler;
import com.suidifu.microservice.model.ReconciliationRepaymentContext;
import com.suidifu.microservice.service.SourceDocumentService;
import com.zufangbao.sun.entity.account.DepositeAccountInfo;
import com.zufangbao.sun.entity.account.VirtualAccount;
import com.zufangbao.sun.entity.customer.Customer;
import com.zufangbao.sun.entity.customer.CustomerType;
import com.zufangbao.sun.entity.directbank.business.CashFlow;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.entity.refund.TemporaryDepositDoc;
import com.zufangbao.sun.ledgerbook.AccountSide;
import com.zufangbao.sun.ledgerbook.AssetCategory;
import com.zufangbao.sun.ledgerbook.AssetConvertor;
import com.zufangbao.sun.ledgerbook.BankAccountCache;
import com.zufangbao.sun.ledgerbook.ChartOfAccount;
import com.zufangbao.sun.ledgerbook.LedgerBook;
import com.zufangbao.sun.ledgerbook.LedgerBookHandler;
import com.zufangbao.sun.ledgerbook.LedgerItemService;
import com.zufangbao.sun.ledgerbook.LedgerTradeParty;
import com.zufangbao.sun.ledgerbookv2.handler.LedgerBookV2Handler;
import com.zufangbao.sun.service.CashFlowService;
import com.zufangbao.sun.service.CustomerService;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.service.LedgerBookService;
import com.zufangbao.sun.service.VirtualAccountService;
import com.zufangbao.sun.yunxin.service.refund.TemporaryDepositDocService;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;
import javax.annotation.Resource;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

/**
 * @author louguanyang
 */
@Log4j2
@Component("businessPaymentVoucherTaskHandler")
public class BusinessPaymentVoucherTaskHandlerImpl implements BusinessPaymentVoucherTaskHandler {
    @Resource
    private CustomerService customerService;
    @Resource
    private FinancialContractService financialContractService;
    @Resource
    private LedgerBookVirtualAccountHandler ledgerBookVirtualAccountHandler;
    @Resource
    private LedgerBookHandler ledgerBookHandler;
    @Resource
    private SourceDocumentService sourceDocumentService;
    @Resource
    private VirtualAccountHandler virtualAccountHandler;
    @Resource
    private VirtualAccountService virtualAccountService;
    @Resource
    private LedgerItemService ledgerItemService;
    @Resource
    private TemporaryDepositDocService temporaryDepositDocService;
    @Resource
    private CashFlowService cashFlowService;
    @Resource
    private TemporaryDepositDocHandler temporaryDepositDocHandler;
    @Resource
    private LedgerBookV2Handler ledgerBookV2Handler;
    @Resource
    private BankAccountCache bankAccountCache;
    @Resource
    private LedgerBookService ledgerBookService;

    @Override
    public VirtualAccount fetchVirtualAccountAndBusinessPaymentVoucherTransfer(String sourceDocumentUuid,
                                                                               BigDecimal writeOffAmount,
                                                                               LedgerBook ledgerBook,
                                                                               String financialContractUuid,
                                                                               boolean isRepaymentOrder) {
        FinancialContract financialContract = financialContractService.getFinancialContractBy(financialContractUuid);
        if (financialContract == null) {
            log.info("financialContract is null");
            return null;
        }

        Customer companyCustomer = customerService.getCustomer(financialContract.getApp(), CustomerType.COMPANY);
        if (companyCustomer == null) {
            log.info("companyCustomer is null");
            return null;
        }
        boolean existBookedSourceDocument = ledgerItemService
                .existBookedSourceDocumentOfIndependtAccountRemittance(ledgerBook.getLedgerBookNo(),
                        sourceDocumentUuid, writeOffAmount);
        if (!existBookedSourceDocument) {
            log.info("not exist BookedSourceDocument");
            return bookAndRefreshVirtualAccount(sourceDocumentUuid,
                    writeOffAmount, financialContract, ledgerBook,
                    companyCustomer, isRepaymentOrder);
        }
        return virtualAccountService.getVirtualAccountByCustomerUuid(companyCustomer.getCustomerUuid());
    }

    private VirtualAccount bookAndRefreshVirtualAccount(String sourceDocumentUuid,
                                                        BigDecimal writeOffAmount,
                                                        FinancialContract financialContract,
                                                        LedgerBook ledgerBook,
                                                        Customer companyCustomer,
                                                        boolean isRepaymentOrder) {
        long start = System.currentTimeMillis();
        VirtualAccount virtualAccount = virtualAccountService.getVirtualAccountByCustomerUuid(companyCustomer.getCustomerUuid());
        log.info("getVirtualAccountByCustomerUuid used: {}ms", System.currentTimeMillis() - start);
        String oldVirtualAccountVersion = virtualAccount.getVersion();

        start = System.currentTimeMillis();
        BigDecimal balance = ledgerBookVirtualAccountHandler.getBalanceOfCustomer(ledgerBook.getLedgerBookNo(), companyCustomer.getCustomerUuid());
        log.info("getBalanceOfCustomer used: {}ms", System.currentTimeMillis() - start);
        if (balance.compareTo(writeOffAmount) < 0) {
            return null;
        }
        String financialCompanyUuid = financialContract.getCompany().getUuid();
        AssetCategory assetCategory = AssetConvertor.convertEmptyAssetCategory();
        LedgerTradeParty frozenCapitalTradeParty = new LedgerTradeParty(companyCustomer.getCustomerUuid(),
                financialCompanyUuid);
        LedgerTradeParty independentAccountTradePary = new LedgerTradeParty(companyCustomer.getCustomerUuid(),
                financialCompanyUuid);
        log.info("#begin to BusinessPaymentVoucherTaskHandlerImpl" +
                "#book_compensatory_remittance_virtual_accountV2  [V4]");
        if (ledgerBookV2Handler.checkLegderBookVersion(ledgerBook)) {
            log.info("#begin to BusinessPaymentVoucherTaskHandlerImpl" +
                    "#book_compensatory_remittance_virtual_accountV2  [V4] " +
                    "[AccountTemplate]");
            ledgerBookV2Handler.book_compensatory_remittance_virtual_accountV2(ledgerBook,
                    frozenCapitalTradeParty, independentAccountTradePary, "", "",
                    sourceDocumentUuid, writeOffAmount,
                    assetCategory,
                    SND_FROZEN_CAPITAL_VOUCHER,
                    SND_LIABILITIES_INDEPENDENT_INTER_ACCOUNT_REMITTANCE);

            log.info("#end to " +
                    "BusinessPaymentVoucherTaskHandlerImpl" +
                    "#book_compensatory_remittance_virtual_accountV2  [V4] " +
                    "[AccountTemplate]");
        }

        if (ledgerBookV2Handler.checkLedgerBookVersionV1(ledgerBook)) {
            ledgerBookHandler.book_compensatory_remittance_virtual_accountV2(ledgerBook,
                    frozenCapitalTradeParty, independentAccountTradePary, "", "",
                    sourceDocumentUuid, writeOffAmount, assetCategory,
                    SND_FROZEN_CAPITAL_VOUCHER,
                    SND_LIABILITIES_INDEPENDENT_INTER_ACCOUNT_REMITTANCE);
            log.info("#end to BusinessPaymentVoucherTaskHandlerImpl" +
                    "#book_compensatory_remittance_virtual_accountV2  [V4]");
        }
        if (!isRepaymentOrder) {
            SourceDocument sourceDocument = sourceDocumentService.getSourceDocumentBy(sourceDocumentUuid);

            //再次计算账本余额，不能比滞留金额小
            BigDecimal customerBalance = ledgerBookVirtualAccountHandler
                    .getBalanceOfCustomer(ledgerBook.getLedgerBookNo(), companyCustomer.getCustomerUuid());
            if (sourceDocument.getBookingAmount().compareTo(writeOffAmount) > 0
                    && customerBalance.compareTo(sourceDocument.
                    getBookingAmount().subtract(writeOffAmount)) >= 0) {
                //多余资金生成滞留单
                temporaryDepositDocHandler.
                        businessPayCreateTemporaryDepositDoc(sourceDocumentUuid,
                                writeOffAmount, virtualAccount, ledgerBook);
            }
        }
        return virtualAccountHandler.refreshVirtualAccountBalance(
                financialContract.getLedgerBookNo(),
                companyCustomer.getCustomerUuid(),
                financialContract.getFinancialContractUuid(),
                oldVirtualAccountVersion);
    }

    @Override
    public boolean unfreezeCapitalAmountOfVoucher(String sourceDocumentUuid,
                                                  String financialContractNo,
                                                  LedgerBook book,
                                                  String tmpDepositDocUuid,
                                                  String toCreditAccount) {
        FinancialContract financialContract = financialContractService.getUniqueFinancialContractBy(financialContractNo);
        if (financialContract == null) {
            log.info("financialContract is null");
            return false;
        }

        Customer companyCustomer = customerService.getCustomer(financialContract.getApp(), CustomerType.COMPANY);
        if (companyCustomer == null) {
            log.info("companyCustomer is null ");
            return false;
        }

        BigDecimal balanceOfFrozenCapital = ledgerBookVirtualAccountHandler.
                getBalanceOfFrozenCapital(book.getLedgerBookNo(), companyCustomer.getCustomerUuid(),
                        sourceDocumentUuid, tmpDepositDocUuid);
        if (BigDecimal.ZERO.compareTo(balanceOfFrozenCapital) == 0) {//无冻结资金
            return true;
        }
        TemporaryDepositDoc temporaryDepositDoc = temporaryDepositDocService.
                getTemporaryDepositDocBy(tmpDepositDocUuid);
        String cashFlowIdentity = null;
        if (temporaryDepositDoc != null) {
            CashFlow cashFlow = cashFlowService.getCashFlowByCashFlowUuid(temporaryDepositDoc.getCashFlowUuid());
            cashFlowIdentity = (cashFlow == null ? null : cashFlow.getCashFlowIdentity());
        }
        AssetCategory assetCategory = AssetConvertor.
                convertTemporayDepositDocAssetCategory(tmpDepositDocUuid, cashFlowIdentity);
        LedgerTradeParty creditTradePary = new LedgerTradeParty(companyCustomer.getCustomerUuid(),
                financialContract.getCompany().getUuid());
        String batchUuid = UUID.randomUUID().toString();
        Date bookInDate = new Date();
        ledgerBookHandler.
                book_single_asset_with_batch_uuid(book, creditTradePary, assetCategory, balanceOfFrozenCapital,
                        ChartOfAccount.SND_UNFROZEN_CAPITAL_VOUCHER, AccountSide.DEBIT, "", "",
                        sourceDocumentUuid, batchUuid, bookInDate);
        ledgerBookHandler.
                book_single_asset_with_batch_uuid(book, creditTradePary, assetCategory, balanceOfFrozenCapital,
                        toCreditAccount, AccountSide.CREDIT, "", "", sourceDocumentUuid, batchUuid, bookInDate);
        //refresh
        VirtualAccount virtualAccount = virtualAccountService.
                getVirtualAccountByCustomerUuid(companyCustomer.getCustomerUuid());
        String oldVirtualAccountVersion = virtualAccount.getVersion();

        virtualAccountHandler.
                refreshVirtualAccountBalance(financialContract.getLedgerBookNo(), companyCustomer.getCustomerUuid(),
                        financialContract.getFinancialContractUuid(), oldVirtualAccountVersion);

        return true;
    }

    @Override
    public void recoverEachFrozenCapitalAmount(ReconciliationRepaymentContext context) {
        String ledgerBookNo = context.getLedgerBookNo();
        FinancialContract financialContract = context.getFinancialContract();
        String companyCustomerUuid = context.getCompanyCustomer().getCustomerUuid();
        String jvUuid = context.getIssuedJournalVoucher().getJournalVoucherUuid();
        String sdUuid = context.getRepaymentOrder().getOrderUuid();
        BigDecimal bookingAmount = context.getBookingAmount();
        String tmpDepositDocUuid = context.getTmpDepositDocUuidFromTmpDepositRecover();
        String sndSecondNo = context.getSecondNoFromTmpDepositRecover();

        //冻结资金 debit
        LedgerBook book = ledgerBookService.getBookByBookNo(ledgerBookNo);
        String financialCompanyUuid = financialContract.getCompany().getUuid();
        AssetCategory assetCategory = AssetConvertor.convertAssetCategory(tmpDepositDocUuid, sndSecondNo);
        LedgerTradeParty frozenCapitalTradeParty = new LedgerTradeParty(companyCustomerUuid,
                financialContract.getCompany().getUuid());
        String batchUuid = UUID.randomUUID().toString();
        Date bookInDate = new Date();
        ledgerBookHandler.book_single_asset_with_batch_uuid(book,
                frozenCapitalTradeParty, assetCategory, bookingAmount,
                ChartOfAccount.SND_FROZEN_CAPITAL_VOUCHER,
                com.zufangbao.sun.ledgerbook.AccountSide.DEBIT,
                jvUuid, "", sdUuid, batchUuid, bookInDate);

        //银行存款 credit
        DepositeAccountInfo accountInfo = bankAccountCache.extractFirstBankAccountFrom(financialContract);
        LedgerTradeParty debitTradeParty = new LedgerTradeParty(financialCompanyUuid, "");
        ledgerBookHandler.book_single_asset_with_batch_uuid(book,
                debitTradeParty, assetCategory, bookingAmount,
                accountInfo.getDeposite_account_name(),
                com.zufangbao.sun.ledgerbook.AccountSide.CREDIT,
                jvUuid, "", sdUuid, batchUuid, bookInDate);
    }
}