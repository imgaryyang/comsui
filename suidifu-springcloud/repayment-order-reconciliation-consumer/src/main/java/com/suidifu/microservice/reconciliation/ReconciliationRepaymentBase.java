package com.suidifu.microservice.reconciliation;

import com.suidifu.giotto.exception.GiottoException;
import com.suidifu.giotto.handler.FastHandler;
import com.suidifu.giotto.keyenum.FastAssetSetKeyEnum;
import com.suidifu.giotto.model.FastAssetSet;
import com.suidifu.hathaway.job.Priority;
import com.suidifu.matryoshka.customize.ReconciliationDelayTaskProcess;
import com.suidifu.microservice.entity.JournalVoucher;
import com.suidifu.microservice.handler.BusinessPaymentVoucherTaskHandler;
import com.suidifu.microservice.handler.JournalVoucherHandler;
import com.suidifu.microservice.handler.LedgerBookVirtualAccountHandler;
import com.suidifu.microservice.handler.PrepaymentHandler;
import com.suidifu.microservice.handler.VirtualAccountHandler;
import com.suidifu.microservice.model.ReconciliationRepaymentContext;
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
import com.zufangbao.sun.yunxin.entity.AssetRecoverType;
import com.zufangbao.sun.yunxin.entity.AssetSet;
import com.zufangbao.sun.yunxin.entity.OrderPaymentStatus;
import com.zufangbao.sun.yunxin.entity.RepaymentPlanExtraData;
import com.zufangbao.sun.yunxin.entity.api.PrepaymentApplication;
import com.zufangbao.sun.yunxin.entity.model.RepaymentChargesDetail;
import com.zufangbao.sun.yunxin.handler.RepaymentPlanHandler;
import com.zufangbao.sun.yunxin.handler.UpdateAssetStatusLedgerBookHandler;
import com.zufangbao.sun.yunxin.service.RepaymentPlanExtraDataService;
import com.zufangbao.sun.yunxin.service.RepaymentPlanService;
import com.zufangbao.sun.yunxin.service.SettlementOrderService;
import com.zufangbao.sun.yunxin.service.account.ClearingExecLogService;
import com.zufangbao.sun.yunxin.service.repayment.RepaymentOrderItemService;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Map;
import javax.annotation.Resource;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;

@Log4j2
public abstract class ReconciliationRepaymentBase implements ReconciliationRepayment {
    @Resource
    private JournalVoucherHandler journalVoucherHandler;
    @Resource
    private VirtualAccountFlowService virtualAccountFlowService;
    @Resource
    private UpdateAssetStatusLedgerBookHandler updateAssetStatusLedgerBookHandler;
    @Resource
    private VirtualAccountHandler virtualAccountHandler;
    @Resource
    private RepaymentPlanService repaymentPlanService;
    @Resource
    private FinancialContractService financialContractService;
    @Resource
    private CustomerService customerService;
    @Resource
    private LedgerBookStatHandler ledgerBookStatHandler;
    @Resource
    private ContractAccountService contractAccountService;
    @Resource
    private JournalVoucherService journalVoucherService;
    @Resource
    private VirtualAccountService virtualAccountService;
    @Resource
    private LedgerBookService ledgerBookService;
    @Resource
    private BankAccountCache bankAccountCache;
    @Resource
    private DepositeAccountHandler depositeAccountHandler;
    @Resource
    private LedgerBookVirtualAccountHandler ledgerBookVirtualAccountHandler;
    @Resource
    private RepaymentPlanHandler repaymentPlanHandler;
    @Resource
    private SettlementOrderService settlementOrderService;
    @Resource
    private PrepaymentHandler prepaymentHandler;
    @Resource
    private PrepaymentApplicationService prepaymentApplicationService;
    @Resource
    private OrderService orderService;
    @Resource
    private BusinessPaymentVoucherTaskHandler businessPaymentVoucherHandler;
    @Resource
    private ReconciliationDelayTaskProcess reconciliationDelayTaskProcess;
    @Resource
    private FinancialContractConfigurationService financialContractConfigurationService;
    @Resource
    private FastHandler fastHandler;
    @Resource
    private RepaymentOrderItemService repaymentOrderItemService;
    @Resource
    private RepaymentPlanExtraDataService repaymentPlanExtraDataService;
    @Resource
    private LedgerBookV2Handler ledgerBookV2Handler;
    @Resource
    private ClearingExecLogService clearingExecLogService;

    public boolean accountReconciliation(Map<String, Object> params) {
        try {
            ReconciliationRepaymentContext context = preAccountReconciliation(params);
            validateReconciliationContext(context);
            relatedDocumentsProcessing(context);
            issueJournalVoucher(context);
            ledgerBookReconciliation(context);
            refreshVirtualAccount(context);
            refreshAsset(context);
            postAccountReconciliation(context);
            return true;
        } catch (MisMatchedConditionException | GiottoException e) {
            log.info("#accountReconciliation# MisMatchedConditionException occur exception,error message :{}", ExceptionUtils.getStackTrace(e));
            return false;
        } catch (AlreadyProcessedException e) {
            log.info("#accountReconciliation# AlreadyProcessedException occur exception,error message :{}", ExceptionUtils.getStackTrace(e));
            return true;
        }
    }

    public abstract void relatedDocumentsProcessing(ReconciliationRepaymentContext context);

    void processIfPrepaymentPlan(ReconciliationRepaymentContext context) {
        //提前还款解冻
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

    public void ledgerBookReconciliation(ReconciliationRepaymentContext context) {
        String configureContext = financialContractConfigurationService.getFinancialContractConfigContentContent(context.getFinancialContract().getUuid(),
                FinancialContractConfigurationCode.CROSS_PERIOD_CANCEL_AFTER_VERIFICATION.getCode());
        if (StringUtils.isBlank(configureContext) && context.getRecoverType().isLoanAsset()) {//进预收也不需要验证
            boolean isDblock = repaymentPlanHandler.repaymentPlanDeblocking(context.getAssetSet());

            if (!isDblock) {
                throw new AssetSetLockException();
            }
        }
        journalVoucherHandler.recoverAssetByReconciliationContextRepaymentOrder(context);
    }

    private ContractCategory extractContractCategory(ReconciliationRepaymentContext context) {
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
            contractCategory.setRelatedBillContractNoLv4(context.getOrder() == null ? "" : context.getOrder().getOrderNo());
            contractCategory.setRelatedBillContractInfoLv3(context.getAssetSet() == null ? "" : context.getAssetSet().getAssetUuid());
            contractCategory.setRelatedBillContractNoLv3(context.getAssetSet() == null ? "" : context.getAssetSet().getSingleLoanContractNo());
        }
        return contractCategory;
    }

    public void issueJournalVoucher(ReconciliationRepaymentContext context)
            throws AlreadyProcessedException {
        JournalVoucher journalVoucher = context.getIssuedJournalVoucher();
        ContractCategory contractCategory = extractContractCategory(context);
        journalVoucher.fillBillContractInfo(contractCategory);
        journalVoucherService.saveOrUpdate(journalVoucher);
    }

    public void refreshVirtualAccount(ReconciliationRepaymentContext context) {
        FinancialContract financialContract = context.getFinancialContract();
        if (context.getRemittanceVirtualAccount() == null) {
            throw new ReconciliationException("remittanceVirtualAccount is null.");
        }
        BigDecimal currentBalance = null;
        VirtualAccount virtualAccount = context.getRemittanceVirtualAccount();
        if (context.getReconciliationType().isRecoveredByVirtualAccountSelf()) {
            VirtualAccount refreshedVirtualAccount = virtualAccountHandler.refreshVirtualAccountBalance(financialContract.getLedgerBookNo(),
                    context.getRemittanceVirtualAccount().getOwnerUuid(),
                    financialContract.getFinancialContractUuid(),
                    context.getVirtualAccountVersionLock());
            currentBalance = refreshedVirtualAccount.getTotalBalance();
        }
        long start = System.currentTimeMillis();
        //新增账户流水 (瞬时余额为null) ，但不刷新 账户余额
        virtualAccountFlowService.addAccountFlow(context.getIssuedJournalVoucher().getJournalVoucherNo(), virtualAccount, context.getBookingAmount(), AccountSide.CREDIT, VirtualAccountTransactionType.INTER_ACCOUNT_REMITTANCE, context.getIssuedJournalVoucher().getJournalVoucherUuid(), currentBalance);
        long end = System.currentTimeMillis();
        log.debug("#abcdefgh#addAccountFlow use [{}]", end - start);
    }

    private void extractReconciliationContextFromAssetSet(ReconciliationRepaymentContext context, AssetSet assetSet) {
        long start = System.currentTimeMillis();
        if (assetSet == null) {
            throw new ReconciliationException("assetSet is null");
        }

        Contract contract = assetSet.getContract();
        if (contract == null) {
            throw new ReconciliationException("contract is null");
        }
        long end = System.currentTimeMillis();

        log.info("\n#preAccountReconciliation#extractReconciliationContextFromAssetSet#extract asset,contract use [{}]ms\n", end - start);
        extractReconciliationContextFrom(context, assetSet, contract);
    }

    void extractReconciliationContextFromContractUuidWithoutAsset(
            ReconciliationRepaymentContext context, Contract contract) {
        if (contract == null) {
            throw new ReconciliationException("contract is null");
        }
        extractReconciliationContextFrom(context, null, contract);
    }

    void extractReconciliationContextFromRepaymentPlanNo(ReconciliationRepaymentContext context, String repaymentPlanNo) {
        AssetSet assetSet = repaymentPlanService.getRepaymentPlanByRepaymentPlanNo(repaymentPlanNo);
        extractReconciliationContextFromAssetSet(context, assetSet);
    }

    private void extractReconciliationContextFrom(ReconciliationRepaymentContext context, AssetSet assetSet, Contract contract) {
        long start = System.currentTimeMillis();
        ContractAccount contractAccount = contractAccountService.getTheEfficientContractAccountBy(contract);
        long end = System.currentTimeMillis();
        log.info("\n#preAccountReconciliation#extractReconciliationContextFromAssetSet#extract ContractAccount use [{}]ms\n", end - start);

        start = System.currentTimeMillis();
        FinancialContract financialContract = financialContractService.getFinancialContractBy(contract.getFinancialContractUuid());
        Company company = financialContract.getCompany();
        Customer borrowerCustomer = contract.getCustomer();
        Customer companyCustomer = customerService.getCustomer(financialContract.getApp(), CustomerType.COMPANY);
        end = System.currentTimeMillis();
        log.info("\n#preAccountReconciliation#extractReconciliationContextFromAssetSet#extract fc,customer use [{}]ms\n", end - start);

        start = System.currentTimeMillis();
        if (assetSet != null) {
            DepositeAccountInfo borrowerDepositAccountForLedgerBook =
                    depositeAccountHandler.extractCustomerVirtualAccount(assetSet);
            context.setBorrowerDepositAccountForLedgerBook(borrowerDepositAccountForLedgerBook);
        }
        end = System.currentTimeMillis();
        log.info("\n#preAccountReconciliation#extractReconciliationContextFromAssetSet#extract DepositAccountInfo use [{}]ms\n", end - start);

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
            remittanceFinancialVirtualAccount = virtualAccountService.getVirtualAccountByCustomerUuid(context.getCompanyCustomer().getCustomerUuid());
        }

        VirtualAccount remittanceBorrowerVirtualAccount = null;
        if (context.getBorrowerCustomer() != null) {
            remittanceBorrowerVirtualAccount = virtualAccountService.getVirtualAccountByCustomerUuid(borrowerCustomer.getCustomerUuid());
        }
        DepositeAccountInfo counterPartyAccount = bankAccountCache.extractFirstBankAccountFrom(context.getFinancialContract());
        context.setRemittanceFinancialVirtualAccount(remittanceFinancialVirtualAccount);
        context.setRemittanceBorrowerVirtualAccount(remittanceBorrowerVirtualAccount);
        context.setFinancialContractAccountForLedgerBook(counterPartyAccount);
        context.setFinancialContractBankAccount(context.getFinancialContract().getCapitalAccount());
        end = System.currentTimeMillis();
        log.debug("\n#preAccountReconciliation#extractReconciliationContextFromAssetSet#extract rest use [\n", end - start);

        extractRelatedBalnaceFromLedgerBook(context);
    }

    public void extractRelatedBalnaceFromLedgerBook(ReconciliationRepaymentContext context) {
        String ledgerBookNo = context.getFinancialContract().getLedgerBookNo();
        Contract contract = context.getContract();
        Customer borrowerCustomer = context.getBorrowerCustomer();
        Customer companyCustomer = context.getCompanyCustomer();
        AssetSet assetSet = context.getAssetSet();

        if (assetSet != null) {
            BigDecimal receivalbeAmount = ledgerBookStatHandler.get_receivable_amount(ledgerBookNo, assetSet.getAssetUuid(), borrowerCustomer.getCustomerUuid());
            BigDecimal guranteeAmount = ledgerBookStatHandler.get_gurantee_amount(ledgerBookNo, assetSet.getAssetUuid());
            BigDecimal unearnedAmount = ledgerBookStatHandler.get_unearned_amount(ledgerBookNo, assetSet.getAssetUuid());
            context.setReceivableAmount(receivalbeAmount);
            context.setGuaranteeAmount(guranteeAmount);
            context.setUnearnedAmount(unearnedAmount);
        }
        if (context.getReconciliationType().isRecoveredByVirtualAccountSelf()) {
            BigDecimal borrowerCustomerBalance = ledgerBookVirtualAccountHandler.
                    getBalanceOfCustomer(ledgerBookNo,
                            borrowerCustomer.getCustomerUuid());
            context.setBorrowerCustomerBalance(borrowerCustomerBalance);
        } else {
            BigDecimal financialAppBalance = ledgerBookVirtualAccountHandler.
                    getBalanceOfCustomer(ledgerBookNo,
                            companyCustomer.getCustomerUuid());
            context.setFinancialAppBalance(financialAppBalance);
        }

        BigDecimal repurchaseAmount = ledgerBookStatHandler.get_repurchase_amount(ledgerBookNo, contract.getUuid());
        context.setRepurchaseAmount(repurchaseAmount);
    }

    public abstract void validateReconciliationContext(ReconciliationRepaymentContext context) throws AlreadyProcessedException;

    public void refreshAsset(ReconciliationRepaymentContext context) throws GiottoException {
        RepaymentType repaymentType = RepaymentType.NORMAL;
        if (context.getDeductApplication() != null) {
            repaymentType = context.getDeductApplication().getRepaymentType();
        }
        updateAssetStatusLedgerBookHandler.updateAssetsFromLedgerBook(context.getLedgerBookNo(),
                Collections.singletonList(context.getAssetSet().getAssetUuid()),
                context.getActualRecycleTime(),
                repaymentType,
                context.getAssetSet().getExecutingStatus());

        BigDecimal payingTotalAmount = repaymentOrderItemService.get_total_paying_amount(
                context.getAssetSet().getAssetUuid(),
                context.getRepaymentOrderItem().getOrderDetailUuid());
        AssetSet assetSet = context.getAssetSet();
        OrderPaymentStatus expectedPaymentStatus;
        if (payingTotalAmount.compareTo(BigDecimal.ZERO) == 0) {
            expectedPaymentStatus = OrderPaymentStatus.UNEXECUTINGORDER;
        } else {
            expectedPaymentStatus = OrderPaymentStatus.ORDERPAYMENTING;
        }
        if (expectedPaymentStatus != assetSet.getOrderPaymentStatus()) {//无处理订单
            assetSet.updateOrderPaymentStatus(expectedPaymentStatus);
            repaymentPlanService.update(assetSet);
        }

        //刷新缓存
        FastAssetSet fastAssetSet = fastHandler.getByKey(
                FastAssetSetKeyEnum.SINGLE_LOAN_CONTRACT_NO,
                context.getRepaymentOrderItem().getRepaymentBusinessNo(),
                FastAssetSet.class, false);

        if (fastAssetSet != null) {
            String assetSetUuid = fastAssetSet.getAssetUuid();

            RepaymentPlanExtraData extraData = repaymentPlanExtraDataService.get(assetSetUuid);
            extraData.setOrderPayingAmount(payingTotalAmount);
            repaymentPlanExtraDataService.saveOrUpdate(extraData);
        }
    }

    @Override
    public void postAccountReconciliation(ReconciliationRepaymentContext context) throws GiottoException {
        AssetRecoverType recoverType = context.getRecoverType();

        if (recoverType != null && recoverType.isLoanAssetOrGuranteeAsset()) {
            AssetSet assetSet = repaymentPlanService.getUniqueRepaymentPlanByUuid(
                    context.getAssetSet().getAssetUuid());
            settlementOrderService.createSettlementOrder(assetSet);
        }

        Order order = context.getOrder();
        if (recoverType != null && recoverType.isLoanAsset() && order != null && StringUtils.isBlank(order.getChargesDetail())) {
            RepaymentChargesDetail chargesDetail = getChargesDetailByLoanAsset(context);
            order.setChargesDetail(chargesDetail.toJsonString());
            orderService.save(order);
        }

        createReconciliationDelayTask(recoverType, context);
        if (JournalVoucherType.JournalVoucherTypeUsedSpecialAccount().contains(context.getJournalVoucherType().ordinal())) {
            if (financialContractService.isSpecialAccountConfigured(context.getFinancialContract().getFinancialContractUuid())) {
                clearingExecLogService.createClearingExecLog(
                        context.getAssetSet(),
                        getChargesDetailMapByLoanAsset(context),
                        context.getIssuedJournalVoucher().getUuid(),
                        context.getJournalVoucherType().ordinal(),
                        context.getBookingAmount(),
                        context.getContract().getId(), "");
            }
        }
    }

    private void createReconciliationDelayTask(AssetRecoverType recoverType, ReconciliationRepaymentContext context) {
        try {
            log.info("createReconciliationDelayTask start:recoverType[" + recoverType + "],assetsetuuid[" + (context.getAssetSet() == null ? null : context.getAssetSet().getAssetUuid()) + "],"
                    + "repurchaseUuid[" + (context.getRepurchaseDoc() == null ? null : context.getRepurchaseDoc().getRepurchaseDocUuid()) + "]");
            if (recoverType == null || !recoverType.isLoanAssetOrRepurchaseAsset()) {
                log.info("createReconciliationDelayTask recoverType is not loanRepurAssset");
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
                log.info("taskUuid:[" + taskUuidForRecover + "]");
                reconciliationDelayTaskProcess.processingDelayTask(context.getActualRecycleTime(), context.getContract(), chargesDetail, context.getAssetSet(), context.getRepurchaseDoc() == null ? null : context.getRepurchaseDoc().getRepurchaseDocUuid(), recoverType, taskUuidForRecover);
            }

            //逾期核销
            String taskUuidForOverRecover = financialContractConfigurationService.getFinancialContractConfigContentContent(context.getFinancialContract().getUuid(),
                    FinancialContractConfigurationCode.BQ_OVER_RECOVER_DELAY_TASK_UUID.getCode());
            if (StringUtils.isNotBlank(taskUuidForOverRecover)) {
                log.info("taskUuid:[" + taskUuidForOverRecover + "]");
                reconciliationDelayTaskProcess.processingDelayTask(context.getActualRecycleTime(), context.getContract(), chargesDetail, context.getAssetSet(), context.getRepurchaseDoc() == null ? null : context.getRepurchaseDoc().getRepurchaseDocUuid(), recoverType, taskUuidForOverRecover);
            }

            log.info("createReconciliationDelayTask end:recoverType[" + recoverType + "],assetsetuuid[" + (context.getAssetSet() == null ? null : context.getAssetSet().getAssetUuid()) + "],"
                    + "repurchaseUuid[" + (context.getRepurchaseDoc() == null ? null : context.getRepurchaseDoc().getRepurchaseDocUuid()) + "]");
        } catch (Exception e) {
            log.error("createReconciliationDelayTask error:recoverType[" + recoverType + "],assetsetuuid[" + (context.getAssetSet() == null ? null : context.getAssetSet().getAssetUuid()) + "],"
                    + "repurchaseUuid[" + (context.getRepurchaseDoc() == null ? null : context.getRepurchaseDoc().getRepurchaseDocUuid()) + "],msg[" + ExceptionUtils.getFullStackTrace(e) + "].");
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

    void recoverEachFrozenCapitalAmount(ReconciliationRepaymentContext context) {
        log.info("begin to [ledgerBookNo]:" + context.getLedgerBookNo() + "[FinancialContractUUID]:" + context.getFinancialContract().getUuid() + "[JournalVoucherUuid]:" + context.getIssuedJournalVoucher().getJournalVoucherUuid() + "[SourceDocumentUuid]:" + context.getSourceDocumentDetailUuid());
        if (ledgerBookV2Handler.checkLegderBookVersion(getLedgerBook(context.getLedgerBookNo()))) {
            log.info("#begin " +
                    "ReconciliationRepaymentBase" +
                    "#recoverEachFrozenCapitalAmount [核销] " +
                    "[AccountTemplate]");
            ledgerBookV2Handler.recover_Each_Frozen_Capital_Amount(context.getLedgerBookNo(), context.getFinancialContract(), context.getCompanyCustomer().getCustomerUuid(), context.getIssuedJournalVoucher().getJournalVoucherUuid(), context.getRepaymentOrder().getOrderUuid(), context.getBookingAmount());
            log.info("#end " +
                    "ReconciliationRepaymentBase" +
                    "#recoverEachFrozenCapitalAmount [核销] " +
                    "[AccountTemplate]");
        }
        if (ledgerBookV2Handler.checkLedgerbookVersionByledgerBookNoV1(context.getLedgerBookNo())) {
            businessPaymentVoucherHandler.recoverEachFrozenCapitalAmount(context);
            log.info("#end " +
                    "ReconciliationRepaymentBase" +
                    "#recoverEachFrozenCapitalAmount [核销]");
        }
    }

    private RepaymentChargesDetail getChargesDetailByLoanAsset(ReconciliationRepaymentContext context) {
        Map<String, BigDecimal> detailAmountMap = context.getBookingDetailAmount();
        if (MapUtils.isEmpty(detailAmountMap)) {
            detailAmountMap = ledgerBookStatHandler.get_jv_asset_detail_amount_of_banksaving_and_independent_accounts(context.getLedgerBookNo(), context.getIssuedJournalVoucher().getJournalVoucherUuid(), context.getAssetSet().getAssetUuid());
        }
        return new RepaymentChargesDetail(detailAmountMap);
    }

    private Map<String, BigDecimal> getChargesDetailMapByLoanAsset(ReconciliationRepaymentContext context) {
        Map<String, BigDecimal> detailAmountMap = context.getBookingDetailAmount();
        if (MapUtils.isEmpty(detailAmountMap)) {
            detailAmountMap = ledgerBookStatHandler.get_jv_asset_detail_amount_of_banksaving_and_independent_accounts(context.getLedgerBookNo(), context.getIssuedJournalVoucher().getJournalVoucherUuid(), context.getAssetSet().getAssetUuid());
        }
        return detailAmountMap;
    }

    private RepaymentChargesDetail getChargesDetailByRepurchaseDoc(ReconciliationRepaymentContext context) {
        RepurchaseDoc repurchaseDoc = context.getRepurchaseDoc();
        Map<String, BigDecimal> detailAmount = ledgerBookStatHandler.get_jv_repurchase_detail_amount(context.getLedgerBookNo(), context.getIssuedJournalVoucher().getJournalVoucherUuid(), context.getContract().getUuid());
        RepaymentChargesDetail repurchaseDetail = new RepaymentChargesDetail();
        if (repurchaseDoc != null) {
            repurchaseDetail.fillRepurchaseAmount(detailAmount);
        }
        return repurchaseDetail;
    }
}