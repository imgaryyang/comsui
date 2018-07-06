package com.suidifu.microservice.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.suidifu.hathaway.job.Priority;
import com.suidifu.matryoshka.customize.ReconciliationDelayTaskProcess;
import com.suidifu.microservice.entity.JournalVoucher;
import com.suidifu.microservice.model.ReconciliationContext;
import com.suidifu.microservice.service.JournalVoucherService;
import com.suidifu.owlman.microservice.enumation.JournalVoucherType;
import com.suidifu.owlman.microservice.enumation.ReconciliationType;
import com.suidifu.owlman.microservice.exception.AlreadyProcessedException;
import com.suidifu.owlman.microservice.exception.AssetSetLockException;
import com.suidifu.owlman.microservice.exception.MisMatchedConditionException;
import com.suidifu.owlman.microservice.exception.ReconciliationException;
import com.suidifu.owlman.microservice.model.ContractCategory;
import com.zufangbao.sun.api.model.deduct.RepaymentType;
import com.zufangbao.sun.entity.account.AccountSide;
import com.zufangbao.sun.entity.account.DepositeAccountInfo;
import com.zufangbao.sun.entity.account.VirtualAccount;
import com.zufangbao.sun.entity.account.VirtualAccountTransactionType;
import com.zufangbao.sun.entity.company.Company;
import com.zufangbao.sun.entity.contract.Contract;
import com.zufangbao.sun.entity.customer.Customer;
import com.zufangbao.sun.entity.customer.CustomerType;
import com.zufangbao.sun.entity.directbank.business.ContractAccount;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.entity.financial.FinancialContractConfigurationCode;
import com.zufangbao.sun.entity.order.Order;
import com.zufangbao.sun.entity.repurchase.RepurchaseDoc;
import com.zufangbao.sun.ledgerbook.BankAccountCache;
import com.zufangbao.sun.ledgerbook.DepositeAccountHandler;
import com.zufangbao.sun.ledgerbook.LedgerBook;
import com.zufangbao.sun.ledgerbook.stat.LedgerBookStatHandler;
import com.zufangbao.sun.ledgerbookv2.handler.LedgerBookV2Handler;
import com.zufangbao.sun.service.ContractAccountService;
import com.zufangbao.sun.service.CustomerService;
import com.zufangbao.sun.service.FinancialContractConfigurationService;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.service.LedgerBookService;
import com.zufangbao.sun.service.OrderService;
import com.zufangbao.sun.service.PrepaymentApplicationService;
import com.zufangbao.sun.service.VirtualAccountFlowService;
import com.zufangbao.sun.service.VirtualAccountService;
import com.zufangbao.sun.utils.JsonUtils;
import com.zufangbao.sun.yunxin.entity.AssetRecoverType;
import com.zufangbao.sun.yunxin.entity.AssetSet;
import com.zufangbao.sun.yunxin.entity.api.PrepaymentApplication;
import com.zufangbao.sun.yunxin.entity.model.RepaymentChargesDetail;
import com.zufangbao.sun.yunxin.handler.RepaymentPlanHandler;
import com.zufangbao.sun.yunxin.handler.UpdateAssetStatusLedgerBookHandler;
import com.zufangbao.sun.yunxin.service.RepaymentPlanService;
import com.zufangbao.sun.yunxin.service.SettlementOrderService;
import com.zufangbao.sun.yunxin.service.account.ClearingExecLogService;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

@Slf4j
public abstract class ReconciliationBase implements Reconciliation {
    @Autowired
    private LedgerBookVirtualAccountHandler ledgerBookVirtualAccountHandler;
    @Autowired
    private VirtualAccountService virtualAccountService;
    @Autowired
    private JournalVoucherHandler journalVoucherHandler;
    @Autowired
    private VirtualAccountFlowService virtualAccountFlowService;
    @Autowired
    private UpdateAssetStatusLedgerBookHandler updateAssetStatusLedgerBookHandler;
    @Autowired
    private VirtualAccountHandler virtualAccountHandler;
    @Autowired
    private RepaymentPlanService repaymentPlanService;
    @Autowired
    private FinancialContractService financialContractService;
    @Autowired
    private LedgerBookStatHandler ledgerBookStatHandler;
    @Autowired
    private JournalVoucherService journalVoucherService;
    @Autowired
    private RepaymentPlanHandler repaymentPlanHandler;
    @Autowired
    private SettlementOrderService settlementOrderService;
    @Autowired
    private OrderService orderService;
    @Autowired
    @Qualifier("reconciliationDelayTaskProcess")
    private ReconciliationDelayTaskProcess reconciliationDelayTaskProcess;
    @Autowired
    private FinancialContractConfigurationService financialContractConfigurationService;
    @Autowired
    private ClearingExecLogService clearingExecLogService;
    @Autowired
    private PrepaymentHandler prepaymentHandler;
    @Autowired
    private PrepaymentApplicationService prepaymentApplicationService;
    @Autowired
    private ContractAccountService contractAccountService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private DepositeAccountHandler depositeAccountHandler;
    @Autowired
    private BankAccountCache bankAccountCache;
    @Autowired
    private LedgerBookV2Handler ledgerBookV2Handler;
    @Autowired
    private BusinessPaymentVoucherTaskHandler businessPaymentVoucherHandler;
    @Autowired
    private LedgerBookService ledgerBookService;

    protected void extractReconciliationContextFromRepaymentPlanNo(ReconciliationContext context,
                                                                   String repaymentPlanNo) {
        AssetSet assetSet = repaymentPlanService.getRepaymentPlanByRepaymentPlanNo(repaymentPlanNo);
        extractReconciliationContextFromAssetSet(context, assetSet);
    }

    public void extractReconciliationContextFromAssetSet(ReconciliationContext context, AssetSet assetSet) {
        long start = System.currentTimeMillis();
        if (assetSet == null) {
            throw new ReconciliationException("assetSet is null");
        }
        Contract contract = assetSet.getContract();

        if (contract == null) {
            throw new ReconciliationException("contract is null");
        }
        long end = System.currentTimeMillis();
        log.info("#abcdefgh#preAccountReconciliation#extractReconciliationContextFromAssetSet#extract asset,contract use [" + (end - start) + "]ms");

        extractReconciliationContextFrom(context, assetSet, contract);
    }

    private void extractReconciliationContextFrom(ReconciliationContext context, AssetSet assetSet, Contract contract) {
        long start = System.currentTimeMillis();
        ContractAccount contractAccount = contractAccountService.getTheEfficientContractAccountBy(contract);
        long end = System.currentTimeMillis();
        log.info("#preAccountReconciliation#extractReconciliationContextFromAssetSet#extract ContractAccount use [{}]ms",
                end - start);

        start = System.currentTimeMillis();
        FinancialContract financialContract = financialContractService.
                getFinancialContractBy(contract.getFinancialContractUuid());

        Company company = financialContract.getCompany();

        Customer borrowerCustomer = contract.getCustomer();
        Customer companyCustomer = customerService.getCustomer(financialContract.getApp(), CustomerType.COMPANY);

        end = System.currentTimeMillis();
        log.info("#preAccountReconciliation#extractReconciliationContextFromAssetSet#extract fc,customer use [{}]ms",
                end - start);

        start = System.currentTimeMillis();
        if (assetSet != null) {
            context.setBorrowerDepositAccountForLedgerBook(depositeAccountHandler.
                    extractCustomerVirtualAccount(assetSet));
        }

        end = System.currentTimeMillis();
        log.info("#preAccountReconciliation#extractReconciliationContextFromAssetSet#extract DepositeAccountInfo use [{}]ms",
                end - start);

        start = System.currentTimeMillis();
        context.setAssetSet(assetSet);
        context.setContract(contract);
        context.setFinancialContract(financialContract);
        context.setBorrowerCustomer(borrowerCustomer);
        context.setCompanyCustomer(companyCustomer);
        context.setLedgerBookNo(financialContract.getLedgerBookNo());
        context.setCompany(company);
        context.setContractAccount(contractAccount);

        VirtualAccount remittanceFinancialVirtualAccount = null;
        if (context.getCompanyCustomer() != null) {
            remittanceFinancialVirtualAccount = virtualAccountService.
                    getVirtualAccountByCustomerUuid(context.getCompanyCustomer().getCustomerUuid());
        }
        VirtualAccount remittanceBorrowerVirtualAccount = null;
        if (context.getBorrowerCustomer() != null) {
            remittanceBorrowerVirtualAccount = virtualAccountService.
                    getVirtualAccountByCustomerUuid(borrowerCustomer.getCustomerUuid());
        }

        DepositeAccountInfo counterPartyAccount = bankAccountCache.extractFirstBankAccountFrom(context.getFinancialContract());

        context.setRemittanceFinancialVirtualAccount(remittanceFinancialVirtualAccount);
        context.setRemittanceBorrowerVirtualAccount(remittanceBorrowerVirtualAccount);
        context.setFinancialContractAccountForLedgerBook(counterPartyAccount);
        context.setFinancialContractBankAccount(context.getFinancialContract().getCapitalAccount());
        end = System.currentTimeMillis();
        log.info("#abcdefgh#preAccountReconciliation#extractReconciliationContextFromAssetSet#extract rest use [{}]ms",
                end - start);

        extractRelatedBalanceFromLedgerBook(context);
    }

    public void extractRelatedBalanceFromLedgerBook(ReconciliationContext context) {
        String ledgerBookNo = context.getFinancialContract().getLedgerBookNo();
        Contract contract = context.getContract();
        Customer borrowerCustomer = context.getBorrowerCustomer();
        Customer companyCustomer = context.getCompanyCustomer();
        AssetSet assetSet = context.getAssetSet();
        if (assetSet != null) {
            BigDecimal receivableAmount = ledgerBookStatHandler.
                    get_receivable_amount(ledgerBookNo, assetSet.getAssetUuid(), borrowerCustomer.getCustomerUuid());
            BigDecimal guaranteeAmount = ledgerBookStatHandler.
                    get_gurantee_amount(ledgerBookNo, assetSet.getAssetUuid());
            BigDecimal unearnedAmount = ledgerBookStatHandler.
                    get_unearned_amount(ledgerBookNo, assetSet.getAssetUuid());
            context.setReceivableAmount(receivableAmount);
            context.setGuaranteeAmount(guaranteeAmount);
            context.setUnearnedAmount(unearnedAmount);
        }

        log.info("\ncontext is:{}\n", JsonUtils.toJSONString(context));
        if (context.getReconciliationType().isRecoveredByVirtualAccountSelf()) {
            context.setBorrowerCustomerBalance(ledgerBookVirtualAccountHandler.
                    getBalanceOfCustomer(ledgerBookNo, borrowerCustomer.getCustomerUuid()));
        } else {
            context.setFinancialAppBalance(ledgerBookVirtualAccountHandler.
                    getBalanceOfCustomer(ledgerBookNo, companyCustomer.getCustomerUuid()));
        }

        BigDecimal repurchaseAmount = ledgerBookStatHandler.get_repurchase_amount(ledgerBookNo, contract.getUuid());
        log.info("\nrepurchaseAmount is:{}\n", repurchaseAmount);
        context.setRepurchaseAmount(repurchaseAmount);
    }

    //提前还款解冻
    void processIfPrepaymentPlan(ReconciliationContext context) {
        AssetRecoverType recoverType = context.getRecoverType();
        if (recoverType == null || !recoverType.isLoanAsset()) {
            return;
        }

        AssetSet assetSet = context.getAssetSet();
        if (assetSet == null || !assetSet.isPrepaymentPlan()) {
            return;
        }
        PrepaymentApplication prepaymentApplication = prepaymentApplicationService.getUniquePrepaymentApplicationByRepaymentPlanUuid(assetSet.getAssetUuid());
        if (prepaymentApplication == null) {
            return;
        }
        prepaymentHandler.processingOnePrepaymentPlan(assetSet.getContractUuid(), Priority.High.getPriority(), prepaymentApplication.getId());
    }

    public boolean accountReconciliation(Map<String, Object> params) throws JsonProcessingException {
        try {
            long start = System.currentTimeMillis();
            ReconciliationContext context = preAccountReconciliation(params);
            long end = System.currentTimeMillis();
            log.info("#preAccountReconciliation use [{}]ms", end - start);

            start = System.currentTimeMillis();
            validateReconciliationContext(context);
            end = System.currentTimeMillis();
            log.info("#validateReconciliationContext use [{}]ms", end - start);

            start = System.currentTimeMillis();
            relatedDocumentsProcessing(context);
            end = System.currentTimeMillis();
            log.info("#relatedDocumentsProcessing use [{}]ms", end - start);

            start = System.currentTimeMillis();
            issueJournalVoucher(context);
            end = System.currentTimeMillis();
            log.info("#issueJournalVoucher use [{}]ms", end - start);

            start = System.currentTimeMillis();
            ledgerBookReconciliation(context);
            end = System.currentTimeMillis();
            log.info("#ledgerBookReconciliation use [{}]ms", end - start);

            start = System.currentTimeMillis();
            refreshVirtualAccount(context);
            end = System.currentTimeMillis();
            log.info("#refreshVirtualAccount use [{}]ms", end - start);

            start = System.currentTimeMillis();
            refreshAsset(context);
            end = System.currentTimeMillis();
            log.info("#refreshAsset use [{}]ms", end - start);

            start = System.currentTimeMillis();
            postAccountReconciliation(context);
            end = System.currentTimeMillis();
            log.info("#postAccountReconciliation use [{}]ms", end - start);
            return true;
        } catch (MisMatchedConditionException e) {
            log.info("#accountReconciliation# MisMatchedConditionException occur exception," +
                            "error message:{}",
                    org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace(e));
            return false;
        } catch (AlreadyProcessedException e) {
            log.info("#accountReconciliation# AlreadyProcessedException occur exception," +
                            "error message:{}",
                    org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace(e));
            return false;
        }
    }

    abstract void relatedDocumentsProcessing(ReconciliationContext context);

    private void ledgerBookReconciliation(ReconciliationContext context) {
        String configureContext = financialContractConfigurationService.getFinancialContractConfigContentContent(context.getFinancialContract().getUuid(),
                FinancialContractConfigurationCode.CROSS_PERIOD_CANCEL_AFTER_VERIFICATION.getCode());
        if (StringUtils.isBlank(configureContext) && context.getRecoverType().isLoanAsset()) {
            boolean isDeblock = repaymentPlanHandler.repaymentPlanDeblocking(context.getAssetSet());
            if (!isDeblock) {
                throw new AssetSetLockException();
            }
        }

        long start = System.currentTimeMillis();
        journalVoucherHandler.recoverAssetByReconciliationContext(context);
        log.info("recoverAssetByReconciliationContext used: {}",
                System.currentTimeMillis() - start);
    }

    private ContractCategory extractContractCategory(ReconciliationContext context) {
        ContractCategory contractCategory = new ContractCategory();
        contractCategory.setRelatedBillContractInfoLv1(context.getFinancialContract().getUuid());
        contractCategory.setRelatedBillContractInfoLv2(context.getContract().getUuid());

        contractCategory.setRelatedBillContractNoLv1(context.getFinancialContract().getContractName());
        contractCategory.setRelatedBillContractNoLv2(context.getContract().getContractNo());

        if (context.getReconciliationType() == ReconciliationType.RECONCILIATION_REPURCHASE) {
            contractCategory.setRelatedBillContractNoLv4(context.getRepurchaseDoc().getRepurchaseDocUuid());
            contractCategory.setRelatedBillContractInfoLv3(context.getRepurchaseDoc().getRepurchaseDocUuid());
            contractCategory.setRelatedBillContractNoLv3(context.getRepurchaseDoc().getRepurchaseDocUuid());
        } else {
            contractCategory.setRelatedBillContractNoLv4(context.getOrder().getOrderNo());
            contractCategory.setRelatedBillContractInfoLv3(context.getAssetSet().getAssetUuid());
            contractCategory.setRelatedBillContractNoLv3(context.getAssetSet().getSingleLoanContractNo());
        }
        return contractCategory;
    }

    public void issueJournalVoucher(ReconciliationContext context) throws AlreadyProcessedException {
        JournalVoucher journalVoucher = context.getIssuedJournalVoucher();
        ContractCategory contractCategory = extractContractCategory(context);
        journalVoucher.fillBillContractInfo(contractCategory);
        journalVoucherService.saveOrUpdate(journalVoucher);
    }

    public void refreshVirtualAccount(ReconciliationContext context) {
        FinancialContract financialContract = context.getFinancialContract();
        if (context.getRemittanceVirtualAccount() == null) {
            throw new ReconciliationException("remittanceVirtualAccount is null.");
        }
        BigDecimal currentBalance = null;
        VirtualAccount virtualAccount = context.getRemittanceVirtualAccount();
        if (context.getReconciliationType().isRecoveredByVirtualAccountSelf()) {
            VirtualAccount refreshedVirtualAccount = virtualAccountHandler.
                    refreshVirtualAccountBalance(financialContract.getLedgerBookNo(),
                            context.getRemittanceVirtualAccount().getOwnerUuid(),
                            financialContract.getFinancialContractUuid(),
                            context.getVirtualAccountVersionLock());
            currentBalance = refreshedVirtualAccount.getTotalBalance();
        }
        long start = System.currentTimeMillis();
        //新增账户流水 (瞬时余额为null) ，但不刷新 账户余额
        virtualAccountFlowService.addAccountFlow(context.getIssuedJournalVoucher().getJournalVoucherNo(),
                virtualAccount, context.getBookingAmount(), AccountSide.CREDIT,
                VirtualAccountTransactionType.INTER_ACCOUNT_REMITTANCE,
                context.getIssuedJournalVoucher().getJournalVoucherUuid(),
                currentBalance);
        long end = System.currentTimeMillis();
        log.info("#addAccountFlow use [{}]ms", end - start);
    }

    public void refreshAsset(ReconciliationContext context) {
        RepaymentType repaymentType = RepaymentType.NORMAL;
        if (context.getDeductApplication() != null) {
            repaymentType = context.getDeductApplication().getRepaymentType();
        }

        updateAssetStatusLedgerBookHandler.updateAssetsFromLedgerBook(context.getLedgerBookNo(),
                Collections.singletonList(context.getAssetSet().getAssetUuid()),
                context.getActualRecycleTime(), repaymentType,
                context.getAssetSet().getExecutingStatus());
    }

    @Override
    public void postAccountReconciliation(ReconciliationContext context) {
        AssetRecoverType recoverType = context.getRecoverType();

        if (recoverType != null && recoverType.isLoanAssetOrGuranteeAsset()) {
            AssetSet assetSet = repaymentPlanService.
                    getUniqueRepaymentPlanByUuid(context.getAssetSet().getAssetUuid());
            settlementOrderService.createSettlementOrder(assetSet);
        }

        Order order = context.getOrder();
        if (recoverType != null && recoverType.isLoanAsset()
                && order != null && StringUtils.isBlank(order.getChargesDetail())) {
            RepaymentChargesDetail chargesDetail = getChargesDetailByLoanAsset(context);
            order.setChargesDetail(chargesDetail.toJsonString());
            orderService.save(order);
        }
        createReconciliationDelayTask(recoverType, context);
        if (JournalVoucherType.JournalVoucherTypeUsedSpecialAccount().contains(context.getJournalVoucherType().ordinal())) {
            if (financialContractService.isSpecialAccountConfigured(context.getFinancialContract().getFinancialContractUuid())) {
                clearingExecLogService.createClearingExecLog(context.getAssetSet(),
                        getChargesDetailMapByLoanAsset(context),
                        context.getIssuedJournalVoucher().getUuid(),
                        context.getJournalVoucherType().ordinal(),
                        context.getBookingAmount(), context.getContract().getId(), "");
            }
        }
    }

    private void createReconciliationDelayTask(AssetRecoverType recoverType, ReconciliationContext context) {
        try {
            log.info("createReconciliationDelayTask start:" +
                            "recoverType[{}]," +
                            "assetSetUuid[{}]," +
                            "repurchaseUuid[{}]",
                    recoverType,
                    context.getAssetSet() == null ? null :
                            context.getAssetSet().getAssetUuid(),
                    context.getRepurchaseDoc() == null ? null :
                            context.getRepurchaseDoc().getRepurchaseDocUuid());

            if (recoverType == null || !recoverType.isLoanAssetOrRepurchaseAsset()) {
                log.info("createReconciliationDelayTask recoverType is not loanRepurchaseAssset");
                return;
            }
            RepaymentChargesDetail chargesDetail = null;
            if (recoverType.isLoanAsset()) {
                chargesDetail = getChargesDetailByLoanAsset(context);
            } else if (recoverType.isRepurchaseAsset()) {
                chargesDetail = getChargesDetailByRepurchaseDoc(context);
            }

            //正常核销
            String taskUuidForRecover = financialContractConfigurationService.getFinancialContractConfigContentContent(context.getFinancialContract().getUuid(),
                    FinancialContractConfigurationCode.RECOVER_DELAY_TASK_UUID.getCode());
            if (StringUtils.isNotBlank(taskUuidForRecover)) {
                log.info("taskUuid:[{}]", taskUuidForRecover);
                reconciliationDelayTaskProcess.processingDelayTask(context.getActualRecycleTime(), context.getContract(), chargesDetail, context.getAssetSet(), context.getRepurchaseDoc() == null ? null : context.getRepurchaseDoc().getRepurchaseDocUuid(), recoverType, taskUuidForRecover);
            }

            //逾期核销
            String taskUuidForOverRecover = financialContractConfigurationService.getFinancialContractConfigContentContent(context.getFinancialContract().getUuid(),
                    FinancialContractConfigurationCode.BQ_OVER_RECOVER_DELAY_TASK_UUID.getCode());
            if (StringUtils.isNotBlank(taskUuidForOverRecover)) {
                log.info("taskUuid:[{}]", taskUuidForOverRecover);
                reconciliationDelayTaskProcess.processingDelayTask(context.getActualRecycleTime(), context.getContract(), chargesDetail, context.getAssetSet(), context.getRepurchaseDoc() == null ? null : context.getRepurchaseDoc().getRepurchaseDocUuid(), recoverType, taskUuidForOverRecover);
            }

            log.info("createReconciliationDelayTask end:" +
                            "recoverType[{}]," +
                            "assetSetUuid[{}]," +
                            "repurchaseUuid[{}]",
                    recoverType,
                    context.getAssetSet() == null ? null : context.getAssetSet().getAssetUuid(),
                    context.getRepurchaseDoc() == null ? null : context.getRepurchaseDoc().getRepurchaseDocUuid());
        } catch (Exception e) {
            log.error("createReconciliationDelayTask error:" +
                            "recoverType[{}],assetSetUuid[{}]," +
                            "repurchaseUuid[{}],msg[{}]",
                    recoverType,
                    context.getAssetSet() == null ? null : context.getAssetSet().getAssetUuid(),
                    context.getRepurchaseDoc() == null ? null :
                            context.getRepurchaseDoc().getRepurchaseDocUuid(),
                    ExceptionUtils.getFullStackTrace(e));
        }
    }

    private RepaymentChargesDetail getChargesDetailByLoanAsset(ReconciliationContext context) {
        Map<String, BigDecimal> detailAmountMap = context.getBookingDetailAmount();
        if (MapUtils.isEmpty(detailAmountMap)) {
            detailAmountMap = ledgerBookStatHandler.get_jv_asset_detail_amount_of_banksaving_and_independent_accounts(
                    context.getLedgerBookNo(),
                    context.getIssuedJournalVoucher().getJournalVoucherUuid(),
                    context.getAssetSet().getAssetUuid());
        }
        return new RepaymentChargesDetail(detailAmountMap);
    }

    private Map<String, BigDecimal> getChargesDetailMapByLoanAsset(ReconciliationContext context) {
        Map<String, BigDecimal> detailAmountMap = context.getBookingDetailAmount();
        if (MapUtils.isEmpty(detailAmountMap)) {
            detailAmountMap = ledgerBookStatHandler.get_jv_asset_detail_amount_of_banksaving_and_independent_accounts(context.getLedgerBookNo(),
                    context.getIssuedJournalVoucher().getJournalVoucherUuid(),
                    context.getAssetSet().getAssetUuid());
        }
        return detailAmountMap;
    }

    private RepaymentChargesDetail getChargesDetailByRepurchaseDoc(ReconciliationContext context) {
        RepurchaseDoc repurchaseDoc = context.getRepurchaseDoc();
        Map<String, BigDecimal> detailAmount = ledgerBookStatHandler.get_jv_repurchase_detail_amount(context.getLedgerBookNo(), context.getIssuedJournalVoucher().getJournalVoucherUuid(), context.getContract().getUuid());
        RepaymentChargesDetail repurchaseDetail = new RepaymentChargesDetail();
        if (repurchaseDoc != null) {
            repurchaseDetail.fillRepurchaseAmount(detailAmount);
        }
        return repurchaseDetail;
    }

    public void recoverEachFrozenCapitalAmount(ReconciliationContext context) {
        log.info("begin to [ledgerBookNo]:" + context.getLedgerBookNo()
                + "[FinancialContractUUID]:" + context.getFinancialContract()
                .getUuid() + "[JournalVoucherUuid]:" + context
                .getIssuedJournalVoucher().getJournalVoucherUuid()
                + "[SourceDocumentUuid]:" + context.getSourceDocumentDetailUuid
                () + " [核销]  ");
        if (ledgerBookV2Handler.checkLegderBookVersion(getLedgerBook(context.getLedgerBookNo()))) {
            log.info("#begin to " +
                    "recover_ReconciliationBase#Each_Frozen_Capital_Amount " +
                    "[AccountTemplate] [核销]");
            ledgerBookV2Handler.recover_Each_Frozen_Capital_Amount(context.getLedgerBookNo(),
                    context.getFinancialContract(),
                    context.getCompanyCustomer().getCustomerUuid(),
                    context.getIssuedJournalVoucher().getJournalVoucherUuid(),
                    context.getSourceDocumentUuid(),
                    context.getBookingAmount());
            log.info("#end to " +
                    "recover_ReconciliationBase#Each_Frozen_Capital_Amount " +
                    "[AccountTemplate] [核销]");
        }
        if (ledgerBookV2Handler.checkLedgerbookVersionByledgerBookNoV1(context.getLedgerBookNo())) {
            businessPaymentVoucherHandler.recoverEachFrozenCapitalAmount(context.getLedgerBookNo(),
                    context.getFinancialContract(),
                    context.getCompanyCustomer().getCustomerUuid(),
                    context.getIssuedJournalVoucher().getJournalVoucherUuid(),
                    context.getSourceDocumentUuid(),
                    context.getBookingAmount(),
                    context.getTmpDepositDocUuidFromTmpDepositRecover(),
                    context.getSecondNoFromTmpDepositRecover());
            log.info("#end recover_ReconciliationBase#Each_Frozen_Capital_Amount [预收] ");
        }
    }

    private LedgerBook getLedgerBook(String ledgerBookNo) {
        if (com.zufangbao.sun.utils.StringUtils.isEmpty(ledgerBookNo)) {
            return null;
        }
        LedgerBook book = ledgerBookService.getBookByBookNo(ledgerBookNo);
        if (null == book) {
            return null;
        }
        return book;
    }

    public void ledger_book_reconciliation(ReconciliationContext context) {
        String configureContext = financialContractConfigurationService.getFinancialContractConfigContentContent(context.getFinancialContract().getUuid(),
                FinancialContractConfigurationCode.CROSS_PERIOD_CANCEL_AFTER_VERIFICATION.getCode());
        if (StringUtils.isBlank(configureContext)) {
            if (context.getRecoverType().isLoanAsset()) {
                boolean isDeblock = repaymentPlanHandler.repaymentPlanDeblocking(context.getAssetSet());
                if (!isDeblock) {
                    throw new AssetSetLockException();
                }
            }
        }
        long start = System.currentTimeMillis();
        journalVoucherHandler.recoverAssetByReconciliationContext(context);
        log.info("recover_asset_by_reconciliation_context used : " + (System.currentTimeMillis() - start));
    }

    public void extractReconciliationContextFromContractUuidWithoutAsset(ReconciliationContext context, Contract contract) {
        if (contract == null) {
            throw new ReconciliationException("contract is null");
        }
        extractReconciliationContextFrom(context, null, contract);
    }
}