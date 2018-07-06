package com.suidifu.microservice.handler.impl;

import com.suidifu.hathaway.job.Priority;
import com.suidifu.microservice.entity.JournalVoucher;
import com.suidifu.microservice.entity.SourceDocument;
import com.suidifu.microservice.handler.CashFlowAutoIssueHandler;
import com.suidifu.microservice.handler.CashFlowChargeProxy;
import com.suidifu.microservice.handler.LedgerBookVirtualAccountHandler;
import com.suidifu.microservice.handler.SourceDocumentHandler;
import com.suidifu.microservice.handler.VirtualAccountHandler;
import com.suidifu.microservice.model.SourceDocumentVirtualAccountResolver;
import com.suidifu.microservice.service.JournalVoucherService;
import com.suidifu.microservice.service.SourceDocumentDetailService;
import com.suidifu.microservice.service.SourceDocumentService;
import com.suidifu.microservice.service.VoucherService;
import com.suidifu.owlman.microservice.enumation.JournalVoucherCheckingLevel;
import com.suidifu.owlman.microservice.enumation.JournalVoucherStatus;
import com.suidifu.owlman.microservice.enumation.JournalVoucherType;
import com.suidifu.owlman.microservice.enumation.PaymentCondition;
import com.suidifu.owlman.microservice.exception.BankRechargeAmountException;
import com.suidifu.owlman.microservice.model.JournalAccount;
import com.suidifu.owlman.microservice.spec.CashFLowAutoIssueHandlerSpec;
import com.suidifu.owlman.microservice.spec.GeneralLeasingDocumentTypeDictionary.BusinessDocumentTypeUuid;
import com.zufangbao.sun.entity.account.AccountSide;
import com.zufangbao.sun.entity.account.AppAccount;
import com.zufangbao.sun.entity.account.AppAccountActiveStatus;
import com.zufangbao.sun.entity.account.AuditStatus;
import com.zufangbao.sun.entity.account.BankAccountAdapter;
import com.zufangbao.sun.entity.account.BankAccountConvertor;
import com.zufangbao.sun.entity.account.ConflictBankAccountException;
import com.zufangbao.sun.entity.account.DepositeAccountInfo;
import com.zufangbao.sun.entity.account.DepositeAccountType;
import com.zufangbao.sun.entity.account.VirtualAccount;
import com.zufangbao.sun.entity.account.VirtualAccountTransactionType;
import com.zufangbao.sun.entity.company.Company;
import com.zufangbao.sun.entity.company.corp.App;
import com.zufangbao.sun.entity.contract.Contract;
import com.zufangbao.sun.entity.customer.Customer;
import com.zufangbao.sun.entity.customer.CustomerType;
import com.zufangbao.sun.entity.directbank.business.CashFlow;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.entity.repayment.order.RepaymentOrder;
import com.zufangbao.sun.entity.repayment.order.RepaymentWayGroupType;
import com.zufangbao.sun.ledgerbook.AssetCategory;
import com.zufangbao.sun.ledgerbook.AssetConvertor;
import com.zufangbao.sun.ledgerbook.BankAccountCache;
import com.zufangbao.sun.ledgerbook.ChartOfAccount;
import com.zufangbao.sun.ledgerbook.LedgerBook;
import com.zufangbao.sun.ledgerbook.LedgerBookHandler;
import com.zufangbao.sun.ledgerbook.LedgerItemService;
import com.zufangbao.sun.ledgerbook.LedgerTradeParty;
import com.zufangbao.sun.ledgerbookv2.handler.LedgerBookV2Handler;
import com.zufangbao.sun.service.AppAccountService;
import com.zufangbao.sun.service.BankAccountService;
import com.zufangbao.sun.service.CashFlowService;
import com.zufangbao.sun.service.ContractAccountService;
import com.zufangbao.sun.service.ContractService;
import com.zufangbao.sun.service.CustomerService;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.service.LedgerBookService;
import com.zufangbao.sun.service.VirtualAccountFlowService;
import com.zufangbao.sun.service.VirtualAccountService;
import com.zufangbao.sun.yunxin.entity.BusinessTaskMessage;
import com.zufangbao.sun.yunxin.entity.BusinessTaskTargetType;
import com.zufangbao.sun.yunxin.entity.SourceDocumentStatus;
import com.zufangbao.sun.yunxin.service.BusinessTaskMessageService;
import java.math.BigDecimal;
import java.util.List;
import javax.annotation.Resource;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.stereotype.Component;

@Log4j2
@Component("cashFlowAutoIssueHandler")
public class CashFlowAutoIssueHandlerImpl implements CashFlowAutoIssueHandler {
    @Resource
    private BankAccountCache bankAccountCache;
    @Resource
    private ContractService contractService;
    @Resource
    private CashFlowChargeProxy cashFlowChargeProxy;
    @Resource
    private SourceDocumentHandler sourceDocumentHandler;
    @Resource
    private SourceDocumentService sourceDocumentService;
    @Resource
    private VirtualAccountHandler virtualAccountHandler;
    @Resource
    private LedgerItemService ledgerItemService;
    @Resource
    private VirtualAccountService virtualAccountService;
    @Resource
    private VirtualAccountFlowService virtualAccountFlowService;
    @Resource
    private BusinessTaskMessageService businessTaskMessageService;
    @Resource
    private VoucherService voucherService;
    @Resource
    private SourceDocumentDetailService sourceDocumentDetailService;
    @Resource
    private JournalVoucherService journalVoucherService;
    @Resource
    private LedgerBookVirtualAccountHandler ledgerBookVirtualAccountHandler;
    @Resource
    private FinancialContractService financialContractService;
    @Resource
    private LedgerBookService ledgerBookService;
    @Resource
    private LedgerBookV2Handler ledgerBookV2Handler;
    @Resource
    private LedgerBookHandler ledgerBookHandler;
    @Resource
    private AppAccountService appAccountService;
    @Resource
    private ContractAccountService contractAccountService;
    @Resource
    private CashFlowService cashFlowService;
    @Resource
    private CustomerService customerService;

    @Override
    public void charge_cash_into_virtual_account(CashFlow cashFlow, String fst_snd_contractUuid) {
        // 确定cash是否已被制证
        if ((cashFlow == null) || (!cashFlow.can_auto_charge_cashflow())) {
            return;
        }
        List<FinancialContract> financialContractList = financialContractService
                .getFinancialContractListBy(cashFlow.getHostAccountNo());
        if (financialContractList.size() != 1) {
            log.debug("find financialContractList size is 0 or >1.");
            return;
        }
        FinancialContract financialContract = financialContractList.get(0);

        AppAccount appAccount = appAccountService.getAppAccountByAccountNo(
                cashFlow.getCounterAccountNo(), AppAccountActiveStatus.VALID, financialContract.getApp());
        //商户充值
        if (is_merchant_payment_account(appAccount)) {
            log.debug("exists appAccount.");
            charge_cash_from_merchant_payment_account(cashFlow,
                    financialContract, appAccount, CashFLowAutoIssueHandlerSpec.AUTO_CHARGE_REMARK);
            return;
        }

        log.debug("appAccount not exist. ready to find activeSourceDocument");
        String contractUuid = "";
        if (existsContract(fst_snd_contractUuid)) {
            contractUuid = fst_snd_contractUuid;
        }
        //主动还款凭证充值
        SourceDocument activeSourceDocument = sourceDocumentService
                .getActiveVoucher(cashFlow.getCashFlowUuid(),
                        SourceDocumentStatus.CREATE, contractUuid, financialContract.getFinancialContractUuid());
        if (exist_active_payment_voucher(activeSourceDocument)) {
            charge_cash_from_active_payment_voucher(cashFlow,
                    financialContract, activeSourceDocument, CashFLowAutoIssueHandlerSpec.AUTO_CHARGE_REMARK);
            return;
        }
        log.debug("activeSourceDocument not exist. ready to find by contractAccount");
        //根据客户账号充值
        charge_cash_according_to_payment_account(cashFlow, financialContract, CashFLowAutoIssueHandlerSpec.AUTO_CHARGE_REMARK);
    }

    private boolean existsContract(String fst_snd_contractUuid) {
        Contract contract = contractService.getContract(fst_snd_contractUuid);
        return contract != null;
    }

    /**
     * 是否为商户付款账户
     *
     * @param appAccount
     * @param
     * @return
     */
    private boolean is_merchant_payment_account(AppAccount appAccount) {
        return appAccount != null;
    }

    private boolean exist_active_payment_voucher(
            SourceDocument activeSourceDocument) {
        return activeSourceDocument != null;
    }

    private void charge_cash_according_to_payment_account(CashFlow cashFlow,
                                                          FinancialContract financialContract, String remark) {
        // 根据合同查找
        log.info("activeSourceDocument not exist. ready to find contractAccount");
        List<Contract> contractList = contractAccountService
                .getEfficientContractsExactlyBy(cashFlow.getCounterAccountNo(),
                        financialContract.getFinancialContractUuid());
        if (contractList.size() != 1) {
            return;
        }
        Contract contract = contractList.get(0);
        Customer customer = contract.getCustomer();
        if (customer == null) {
            log.debug("No customer.");
            return;
        }

        //判断一个银行卡号是否  对应多个账户
        boolean isExist = contractAccountService.isExistSamePayAccountNo(cashFlow.getCounterAccountNo());

        AssetCategory assetCategory = AssetConvertor.convertDepositAssetCategory(contract, cashFlow.getCashFlowIdentity());

        if (isExist) {
            try {
                charge_cash_into_virtual_account(cashFlow, customer, cashFlow.getTransactionAmount(), financialContract,
                        assetCategory, remark, null, contract, true);
            } catch (ConflictBankAccountException e) {
                cashFlow.setAuditStatus(AuditStatus.CLOSE);
                cashFlowService.saveOrUpdate(cashFlow);
            }
        }
    }

    private void charge_cash_from_active_payment_voucher(CashFlow cashFlow,
                                                         FinancialContract financialContract,
                                                         SourceDocument activeSourceDocument, String remark) {
        Contract contract = contractService.getContract(activeSourceDocument
                .getRelatedContractUuid());

        if (contract == null) {
            log.debug("No contract.");
            return;
        }

        Customer customer = customerService.getCustomer(activeSourceDocument
                .getFirstPartyId());
        if (customer == null) {
            log.debug("No customer.");
            return;
        }
        AssetCategory assetCategory = AssetConvertor
                .convertDepositAssetCategory(contract, cashFlow.getCashFlowIdentity());

        try {
            charge_cash_into_virtual_account(cashFlow, customer, activeSourceDocument.getPlanBookingAmount(), financialContract,
                    assetCategory, remark, activeSourceDocument, contract, true);
        } catch (ConflictBankAccountException e) {
            cashFlow.setAuditStatus(AuditStatus.CLOSE);
            cashFlowService.saveOrUpdate(cashFlow);
        }

    }

    private void charge_cash_from_merchant_payment_account(CashFlow cashFlow,
                                                           FinancialContract financialContract, AppAccount appAccount, String remark) {
        App app = appAccount.getApp();
        if (app == null) {
            return;
        }
        Customer company_customer = customerService.getCustomer(app,
                CustomerType.COMPANY);
        if (company_customer == null) {
            return;
        }

        //判断一个银行卡号是否  对应多个账户
        boolean isExist = appAccountService.isExistSamePayAccountNo(appAccount.getAccountNo(), app);

        AssetCategory assetCategory = AssetConvertor
                .convertAppDepositAssetCategory(cashFlow.getCashFlowIdentity());

        if (isExist == true) {

            try {
                charge_cash_into_virtual_account(cashFlow, company_customer, cashFlow.getTransactionAmount(), financialContract, assetCategory, remark, null, new Contract(), true);
            } catch (ConflictBankAccountException e) {
                cashFlow.setAuditStatus(AuditStatus.CLOSE);
                cashFlowService.saveOrUpdate(cashFlow);
                e.printStackTrace();
            }

        }
    }

    @Override
    public PaymentCondition paymentOrderRecharge(CashFlow cashFlow, RepaymentOrder order, String contractUuid,
                                                 FinancialContract financialContract) {
        PaymentCondition condition = PaymentCondition.还款订单支付成功但是流水无法自动充值到虚户;
        RepaymentWayGroupType groupType = order.getFirstRepaymentWayGroup();
        String orderUuid = order.getOrderUuid();
        try {
            if (RepaymentWayGroupType.ALTER_OFFLINE_REPAYMENT_ORDER_TYPE.equals(groupType)) {
                cashFlowChargeProxy.charge_cash_into_virtual_account_for_rpc(cashFlow.getCashFlowUuid(), contractUuid, Priority.High.getPriority());
                condition = PaymentCondition.还款订单支付成功流水自动充值到虚户中;
            } else if (RepaymentWayGroupType.OWNER_OFFLINE_REPAYMENT_ORDER_TYPE.equals(groupType)
                    ) {
                Contract contract = contractService.getContract(contractUuid);
                Customer customer = contract.getCustomer();
                AssetCategory assetCategory = AssetConvertor.convertDepositAssetCategory(contract, cashFlow.getCashFlowIdentity());
                charge_cash_into_virtual_account(cashFlow, customer,
                        cashFlow.getTransactionAmount(), financialContract, assetCategory,
                        CashFLowAutoIssueHandlerSpec.AUTO_CHARGE_REMARK, null, contract, true);
                condition = PaymentCondition.还款订单支付成功流水自动充值到虚户中;

            } else {
                log.error("还款订单的付款方式组无效:[" + groupType + "].orderUuid:["
                        + orderUuid + "]");
                condition = PaymentCondition.还款订单支付成功但是流水无法自动充值到虚户;
            }
        } catch (Exception e) {
            log.info("支付订单自动充值失败,with full stack trace [" + ExceptionUtils.getFullStackTrace(e) + "].orderUuid:["
                    + orderUuid + "]");
            condition = PaymentCondition.还款订单支付成功但是流水无法自动充值到虚户;
        }
        return condition;
    }

    @Override
    public SourceDocumentVirtualAccountResolver charge_cash_into_virtual_account(CashFlow cashFlow,
                                                                                 Customer customer,
                                                                                 BigDecimal bookingAmount,
                                                                                 FinancialContract financialContract,
                                                                                 AssetCategory assetCategory,
                                                                                 String remark,
                                                                                 SourceDocument sourceDocument,
                                                                                 Contract contract,
                                                                                 boolean binding)
            throws BankRechargeAmountException, ConflictBankAccountException {
        if (cashFlow == null || customer == null || bookingAmount == null || bookingAmount.compareTo(BigDecimal.ZERO) <= 0
                || financialContract == null) {
            log.debug("cashFlow or customer or bookingAmount or financialContract is null");
            return null;
        }
        BigDecimal totalIssuedAmount = validate_booking_amount(cashFlow,
                bookingAmount);
        if (assetCategory == null) {
            assetCategory = ledgerItemService
                    .getDepositAssetCategoryByCustomer(customer, cashFlow.getCashFlowIdentity());
            log.debug("assetCategory is null get Deposit AssetCategory By Custome， assetCategory RelatedContractUuid is [" + assetCategory.getRelatedContractUuid() + "]");
        }
        Company financial_company = financialContract.getCompany();
        Long companyId = financial_company.getId();

        // create before
        VirtualAccount virtualAccount = virtualAccountService
                .create_if_not_exist_virtual_account(
                        customer.getCustomerUuid(),
                        financialContract.getFinancialContractUuid(),
                        assetCategory.getRelatedContractUuid());

        if (binding) {
            isExistSameBankCard(cashFlow, customer, virtualAccount, contract, financialContract.getFinancialContractUuid());
        }

        log.debug("create if not exist virtual_account,virtualAccountNo is [" + virtualAccount.getVirtualAccountNo() + "]");

        String oldVirtualAccountVersion = virtualAccount.getVersion();
        if (sourceDocument == null) {
            sourceDocument = sourceDocumentHandler.createDepositeReceipt(
                    cashFlow, companyId, bookingAmount, customer,
                    assetCategory.getRelatedContractUuid(),
                    financialContract.getFinancialContractUuid(),
                    virtualAccount.getVirtualAccountNo(), remark);
            log.debug("create if null sourceDocument,sourceDocumentNo is [" + sourceDocument.getSourceDocumentNo() + "]");
            String voucherUuid = voucherService.get_unique_valid_voucher_uuid(cashFlow.getCashFlowUuid());
            fill_voucher_uuid_in_sd_sdd(sourceDocument, voucherUuid, cashFlow.getStringFieldOne());
        } else {
            sourceDocument.setRemarkInAppendix(remark);
            sourceDocumentService.signSourceDocument(sourceDocument,
                    bookingAmount);

        }
        JournalVoucher journalVoucher = create_issued_bank_deposit_jv(cashFlow,
                bookingAmount, financialContract, assetCategory,
                sourceDocument, companyId);

        log.debug("create issued bank deposit journalVoucher,journalVoucherNo is [" + journalVoucher.getJournalVoucherNo() + "]");

        despost_independent_account_in_ledger_book(customer, bookingAmount,
                financialContract, assetCategory, sourceDocument,
                financial_company, journalVoucher, cashFlow.getCashFlowIdentity());

        VirtualAccount refreshedVirtualAccount = virtualAccountHandler
                .refreshVirtualAccountBalance(
                        financialContract.getLedgerBookNo(),
                        customer.getCustomerUuid(),
                        financialContract.getFinancialContractUuid(), oldVirtualAccountVersion);
        virtualAccountFlowService.addAccountFlow(
                sourceDocument.getSourceDocumentNo(), refreshedVirtualAccount,
                bookingAmount, AccountSide.DEBIT,
                VirtualAccountTransactionType.DEPOSIT, sourceDocument.getSourceDocumentUuid(), refreshedVirtualAccount.getTotalBalance());
        log.debug("refresh VirtualAccount And add AccountFlow， VirtualAccountNo is [" + refreshedVirtualAccount.getVirtualAccountNo() + "]");

        BusinessTaskMessage taskMsg = new BusinessTaskMessage(cashFlow.getUuid(), BusinessTaskTargetType.CASH_FLOW_RECHARGE.ordinal(), financialContract.getFinancialContractUuid(), assetCategory.getRelatedContractUuid(), "");
        businessTaskMessageService.save(taskMsg);

        create_business_msg_task_if_special_account_configure(cashFlow.getUuid(), bookingAmount, financialContract.getFinancialContractUuid(), assetCategory.getRelatedContractUuid());

        return new SourceDocumentVirtualAccountResolver(sourceDocument, virtualAccount);
    }

    private BigDecimal validate_booking_amount(CashFlow cashFlow,
                                               BigDecimal bookingAmount) {
        BigDecimal issuedAmount = journalVoucherService
                .getIssuedAmountByIssueJVAndJVType(cashFlow.getCashFlowUuid(),
                        new JournalAccount(AccountSide.DEBIT),
                        JournalVoucherType.BANK_CASHFLOW_DEPOSIT);
        BigDecimal totalIssuedAmount = issuedAmount.add(bookingAmount);
        if (totalIssuedAmount.compareTo(cashFlow.getTransactionAmount()) > 0) {
            throw new BankRechargeAmountException();
        }
        return totalIssuedAmount;
    }

    private void create_business_msg_task_if_special_account_configure(String cashFlowUuid, BigDecimal bookingAmount, String financialContractUuid, String sndContractUuid) {
        if (financialContractService.isSpecialAccountConfigured(financialContractUuid) == false) {
            return;
        }
        BusinessTaskMessage bank_pending_to_customer_account_msg = new BusinessTaskMessage(cashFlowUuid, BusinessTaskTargetType.CASH_FLOW_RECHARGE_BANK_PENDING_ACCOUNT.ordinal(), financialContractUuid, sndContractUuid, "");
        bank_pending_to_customer_account_msg.setAmountInAppendix(bookingAmount);
        businessTaskMessageService.save(bank_pending_to_customer_account_msg);
    }

    private void despost_independent_account_in_ledger_book(Customer customer,
                                                            BigDecimal bookingAmount, FinancialContract financialContract,
                                                            AssetCategory assetCategory, SourceDocument sourceDocument,
                                                            Company financial_company, JournalVoucher journalVoucher, String cashFlowIdentity) {
        LedgerBook book = ledgerBookService.getBookByBookNo(financialContract
                .getLedgerBookNo());
        LedgerTradeParty customerParty = new LedgerTradeParty(
                customer.getCustomerUuid(), financial_company.getUuid());

        DepositeAccountInfo bankAccountInfo = bankAccountCache
                .extractFirstBankAccountFrom(financialContract);
        DepositeAccountInfo custmerAccountInfo = new DepositeAccountInfo(
                ChartOfAccount.SND_LIABILITIES_INDEPENDENT_CUSTOMER_DEPOSIT,
                customerParty, DepositeAccountType.VIRTUAL_ACCOUNT);

        book_bank_pending_credit_payable_pending_debit(bookingAmount, financial_company.getUuid(), book, cashFlowIdentity, journalVoucher.getUuid(), sourceDocument.getUuid(), bankAccountInfo, financialContract.getFinancialContractUuid());

        if (ledgerBookV2Handler.checkLegderBookVersion(book)) {
            log.info("#befin to#CashFlowAutoIssueHandlerImpl" +
                    "#despost_independent_account_in_ledger_book [template] " +
                    "!!!");
            ledgerBookV2Handler.deposit_independent_account_assets(book, bankAccountInfo, custmerAccountInfo, assetCategory,
                    bookingAmount, journalVoucher.getJournalVoucherUuid(), "",
                    sourceDocument.getSourceDocumentUuid());
            log.info("#end !!!");
        }
        if (ledgerBookV2Handler.checkLedgerBookVersionV1(book)) {
            ledgerBookVirtualAccountHandler.deposit_independent_account_assets(
                    book, bankAccountInfo, custmerAccountInfo, assetCategory,
                    bookingAmount, journalVoucher.getJournalVoucherUuid(), "",
                    sourceDocument.getSourceDocumentUuid());
        }
    }

    private void book_bank_pending_credit_payable_pending_debit(BigDecimal transactionAmount, String companyUuid, LedgerBook book, String cashFlowIdentity, String jvUuid, String sdUuid, DepositeAccountInfo bankAccountInfo, String financialContractUuid) {
        if (false == financialContractService.isSpecialAccountConfigured(financialContractUuid)) {
            return;
        }
        AssetCategory assetCategory = AssetConvertor.convertAppDepositAssetCategory(cashFlowIdentity);
        LedgerTradeParty party = new LedgerTradeParty(companyUuid, "");
        if (ledgerBookV2Handler.checkLegderBookVersion(book)) {
            log.info("#begin to #CashFlowAutoIssueHandlerImpl#book_bank_pending_credit_payable_pending_debit#Third AssetSetUUID:[" + cashFlowIdentity + "] [accountTemplate] ");
            ledgerBookV2Handler.book_ledgers_with_washing(book, party, party,
                    jvUuid, "", sdUuid, transactionAmount,
                    assetCategory, ChartOfAccount.TRD_BANK_SAVING_GENERAL_PENDING, ChartOfAccount.SND_PAYABLE_ASSET_PENDING, bankAccountInfo);
            log.info("#end !!!");
        }
        if (ledgerBookV2Handler.checkLedgerBookVersionV1(book)) {
            ledgerBookHandler.book_ledgers_with_washing(book, party, party,
                    jvUuid, "", sdUuid, transactionAmount,
                    assetCategory, ChartOfAccount.TRD_BANK_SAVING_GENERAL_PENDING, ChartOfAccount.SND_PAYABLE_ASSET_PENDING, bankAccountInfo);
        }

    }

    private JournalVoucher create_issued_bank_deposit_jv(CashFlow cashFlow,
                                                         BigDecimal bookingAmount, FinancialContract financialContract,
                                                         AssetCategory assetCategory, SourceDocument sourceDocument,
                                                         Long companyId) {
        JournalVoucher journalVoucher = new JournalVoucher();
        journalVoucher.copyFromSourceDocument(sourceDocument);
        journalVoucher.copyFromCashFlow(cashFlow, financialContract.getCompany());
        journalVoucher
                .fill_voucher_and_booking_amount(
                        sourceDocument.getSourceDocumentUuid(),
                        BusinessDocumentTypeUuid.BUSINESS_DOC_BUSINESS_VOUCHER_TYPE_UUID,
                        "", bookingAmount, JournalVoucherStatus.VOUCHER_ISSUED,
                        JournalVoucherCheckingLevel.AUTO_BOOKING,
                        JournalVoucherType.BANK_CASHFLOW_DEPOSIT);
        journalVoucher.fillBillContractInfo(
                financialContract.getFinancialContractUuid(),
                assetCategory.getRelatedContractUuid(), "",
                financialContract.getContractName(),
                getContractNo(assetCategory.getRelatedContractUuid()), null,
                null);
        journalVoucherService.save(journalVoucher);
        return journalVoucher;
    }

    private void fill_voucher_uuid_in_sd_sdd(SourceDocument sourceDocument, String voucherUuid, String outlierglobalIdentity) {
        if (StringUtils.isEmpty(voucherUuid)) {
            return;
        }
        sourceDocument.setVoucherUuid(voucherUuid);
        if (!StringUtils.isEmpty(outlierglobalIdentity)) {
            sourceDocument.setOutlierSerialGlobalIdentity(outlierglobalIdentity);
        }
        Contract contract = contractService.getContract(sourceDocument.getRelatedContractUuid());
        String contractUniqueId = null;
        if (contract != null) {
            contractUniqueId = contract.getUniqueId();
        }
        sourceDocumentDetailService.updateSourceDocumentUuid(voucherUuid, sourceDocument.getUuid(), contractUniqueId);
        sourceDocumentService.saveOrUpdate(sourceDocument);
    }

    private String getContractNo(String contractUuid) {
        Contract contract = contractService.getContract(contractUuid);
        if (contract != null) {
            return contract.getContractNo();
        }
        return "";
    }

    @Override
    public void isExistSameBankCard(CashFlow cashFlow, Customer customer,
                                    VirtualAccount virtualAccount, Contract contract, String financialContractUuid) throws ConflictBankAccountException {
        if (virtualAccount == null && cashFlow == null) {
            return;
        }
        BankAccountAdapter bankAccountAdapter = BankAccountConvertor.createFrom(cashFlow);
        BankAccountService bankAccountService = BankAccountService.bankAccountServiceFactory(customer.getCustomerType());
        bankAccountService.mergAccountFrom(bankAccountAdapter, virtualAccount.getVirtualAccountUuid(), contract, financialContractUuid);
    }
}