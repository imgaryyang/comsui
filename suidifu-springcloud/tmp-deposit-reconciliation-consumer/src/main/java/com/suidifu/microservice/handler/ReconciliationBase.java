package com.suidifu.microservice.handler;

import com.suidifu.hathaway.job.Priority;
import com.suidifu.matryoshka.customize.ReconciliationDelayTaskProcess;
import com.suidifu.microservice.entity.JournalVoucher;
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
import com.zufangbao.sun.yunxin.entity.ExecutingStatus;
import com.zufangbao.sun.yunxin.entity.api.PrepaymentApplication;
import com.zufangbao.sun.yunxin.entity.model.RepaymentChargesDetail;
import com.zufangbao.sun.yunxin.handler.RepaymentPlanHandler;
import com.zufangbao.sun.yunxin.handler.UpdateAssetStatusLedgerBookHandler;
import com.zufangbao.sun.yunxin.service.RepaymentPlanService;
import com.zufangbao.sun.yunxin.service.SettlementOrderService;
import com.zufangbao.sun.yunxin.service.account.ClearingExecLogService;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import javax.annotation.Resource;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Qualifier;

@Log4j2
public abstract class ReconciliationBase implements Reconciliation {

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
  @Qualifier("reconciliationDelayTaskProcess")
  private ReconciliationDelayTaskProcess reconciliationDelayTaskProcess;
  @Resource
  private FinancialContractConfigurationService financialContractConfigurationService;
  @Resource
  private LedgerBookV2Handler ledgerBookV2Handler;
  @Resource
  private ClearingExecLogService clearingExecLogService;

  @Override
  public boolean accountReconciliation(Map<String, Object> params) {

    try {
      long start = System.currentTimeMillis();
      ReconciliationContext context = preAccountReconciliation(params);
      long end1 = System.currentTimeMillis();
      log.debug("#abcdefgh#preAccountReconciliation use [" + (end1 - start) + "]ms");

      validateReconciliationContext(context);
      long end2 = System.currentTimeMillis();
      log.debug("#abcdefgh#validateReconciliationContext use [" + (end2 - end1) + "]ms");

      relatedDocumentsProcessing(context);
      long end3 = System.currentTimeMillis();
      log.debug("#abcdefgh#relatedDocumentsProcessing use [" + (end3 - end2) + "]ms");

      issueJournalVoucher(context);
      long end4 = System.currentTimeMillis();
      log.debug("#abcdefgh#issueJournalVoucher use [" + (end4 - end3) + "]ms");

      ledgerBookReconciliation(context);
      long end5 = System.currentTimeMillis();
      log.debug("#abcdefgh#ledgerBookReconciliation use [" + (end5 - end4) + "]ms");

      refreshVirtualAccount(context);
      long end6 = System.currentTimeMillis();
      log.debug("#abcdefgh#refreshVirtualAccount use [" + (end6 - end5) + "]ms");

      refreshAsset(context);
      long end7 = System.currentTimeMillis();
      log.debug("#abcdefgh#refreshAsset use [" + (end7 - end6) + "]ms");

      postAccountReconciliation(context);
      long end8 = System.currentTimeMillis();
      log.debug("#abcdefgh#postAccountReconciliation use [" + (end8 - end7) + "]ms");

      return true;

    } catch (MisMatchedConditionException e) {

      log.info(
          "#accountReconciliation# MisMatchedConditionException occur exception,error message :"
              + e);

      return false;
    } catch (AlreadyProcessedException e) {

      log.info(
          "#accountReconciliation# AlreadyProcessedException occur exception,error message :" + e);

      return true;
    }

  }

  public abstract void relatedDocumentsProcessing(ReconciliationContext context);


  /**
   * 提前还款解冻
   *
   * @param context 上下文
   */
  void processIfPrepaymentPlan(ReconciliationContext context) {
    AssetRecoverType recoverType = context.getRecoverType();
    if (recoverType == null || !recoverType.isLoanAsset()) {
      return;
    }

    AssetSet assetSet = context.getAssetSet();
    if (assetSet == null || !assetSet.isPrepaymentPlan()) {
      return;
    }
    PrepaymentApplication prepaymentApplication = prepaymentApplicationService
        .getUniquePrepaymentApplicationByRepaymentPlanUuid(assetSet.getAssetUuid());
    if (prepaymentApplication == null) {
      return;
    }
    String contractUuid = assetSet.getContractUuid();
    Long prepaymentApplicationId = prepaymentApplication.getId();

    prepaymentHandler.processingOnePrepaymentPlan(contractUuid, Priority.High.getPriority(), prepaymentApplicationId);
  }


  public void ledgerBookReconciliation(ReconciliationContext context) {
    String configureContext = financialContractConfigurationService.getFinancialContractConfigContentContent(
        context.getFinancialContract().getUuid(),
        FinancialContractConfigurationCode.CROSS_PERIOD_CANCEL_AFTER_VERIFICATION.getCode());
    if (StringUtils.isBlank(configureContext)) {
      if (context.getRecoverType().isLoanAsset()) {
        boolean isDeblock = repaymentPlanHandler.repaymentPlanDeblocking(context.getAssetSet());
        if (!isDeblock) {
          throw new AssetSetLockException();
        }
      }
    }
    journalVoucherHandler.recoverAssetByReconciliationContext(context);
  }


  private ContractCategory extractContractCategory(ReconciliationContext context) {
    ContractCategory contractCategory = new ContractCategory();
    contractCategory.setRelatedBillContractInfoLv1(context.getFinancialContract().getUuid());
    contractCategory.setRelatedBillContractInfoLv2(context.getContract().getUuid());

    contractCategory.setRelatedBillContractNoLv1(context.getFinancialContract().getContractName());
    contractCategory.setRelatedBillContractNoLv2(context.getContract().getContractNo());

    if (context.getReconciliationType() == ReconciliationType.RECONCILIATION_REPURCHASE) {
      contractCategory
          .setRelatedBillContractNoLv4(context.getRepurchaseDoc().getRepurchaseDocUuid());
      contractCategory
          .setRelatedBillContractInfoLv3(context.getRepurchaseDoc().getRepurchaseDocUuid());
      contractCategory
          .setRelatedBillContractNoLv3(context.getRepurchaseDoc().getRepurchaseDocUuid());
    } else {
      contractCategory.setRelatedBillContractNoLv4(context.getOrder().getOrderNo());
      contractCategory.setRelatedBillContractInfoLv3(context.getAssetSet().getAssetUuid());
      contractCategory.setRelatedBillContractNoLv3(context.getAssetSet().getSingleLoanContractNo());
    }
    return contractCategory;
  }

  @Override
  public abstract ReconciliationContext preAccountReconciliation(Map<String, Object> inputParams)
      throws AlreadyProcessedException;

  @Override
  public void issueJournalVoucher(ReconciliationContext context) throws AlreadyProcessedException {
    JournalVoucher journalVoucher = context.getIssuedJournalVoucher();
    ContractCategory contractCategory = extractContractCategory(context);
    journalVoucher.fillBillContractInfo(contractCategory);
    journalVoucherService.saveOrUpdate(journalVoucher);
  }


  @Override
  public void refreshVirtualAccount(ReconciliationContext context) {
    FinancialContract financialContract = context.getFinancialContract();
    if (context.getRemittanceVirtualAccount() == null) {
      throw new ReconciliationException("remittanceVirtualAccount is null.");
    }
    BigDecimal currentBalance = null;
    VirtualAccount virtualAccount = context.getRemittanceVirtualAccount();
    if (context.getReconciliationType().isRecoveredByVirtualAccountSelf()) {
      VirtualAccount refreshedVirtualAccount = virtualAccountHandler
          .refreshVirtualAccountBalance(financialContract.getLedgerBookNo(),
              context.getRemittanceVirtualAccount().getOwnerUuid(),
              financialContract.getFinancialContractUuid(), context.getVirutalAccountVersionLock());
      currentBalance = refreshedVirtualAccount.getTotalBalance();
    }
    long start = System.currentTimeMillis();
    //新增账户流水 (瞬时余额为null) ，但不刷新 账户余额
    virtualAccountFlowService
        .addAccountFlow(context.getIssuedJournalVoucher().getJournalVoucherNo(), virtualAccount,
            context.getBookingAmount(),
            AccountSide.CREDIT,
            VirtualAccountTransactionType.INTER_ACCOUNT_REMITTANCE,
            context.getIssuedJournalVoucher().getJournalVoucherUuid(), currentBalance);
    long end = System.currentTimeMillis();
    log.debug("#abcdefgh#addAccountFlow use [" + (end - start) + "]ms");
  }


  void extractReconciliationContextFromAssetSet(ReconciliationContext context,
      AssetSet assetSet) {
    long start = System.currentTimeMillis();
    if (assetSet == null) {
      throw new ReconciliationException("assetSet is null");
    }
    Contract contract = assetSet.getContract();

    if (contract == null) {
      throw new ReconciliationException("contract is null");
    }
    long end = System.currentTimeMillis();
    log.debug(
        "#abcdefgh#preAccountReconciliation#extractReconciliationContextFromAssetSet#extract asset,contract use ["
            + (end - start) + "]ms");

    extractReconciliationContextFrom(context, assetSet, contract);

  }

  void extractReconciliationContextFromRepaymentPlanNo(ReconciliationContext context, String repaymentPlanNo) {
    AssetSet assetSet = repaymentPlanService.getRepaymentPlanByRepaymentPlanNo(repaymentPlanNo);
    extractReconciliationContextFromAssetSet(context, assetSet);
  }

  void extractReconciliationContextFromContractUuidWithoutAsset(ReconciliationContext context, Contract contract) {
    if (contract == null) {
      throw new ReconciliationException("contract is null");
    }
    extractReconciliationContextFrom(context, null, contract);
  }


  private void extractReconciliationContextFrom(ReconciliationContext context, AssetSet assetSet, Contract contract) {
    long start = System.currentTimeMillis();
    ContractAccount contractAccount = contractAccountService.getTheEfficientContractAccountBy(contract);

    long end1 = System.currentTimeMillis();
    log.debug(
        "#abcdefgh#preAccountReconciliation#extractReconciliationContextFromAssetSet#extract ContractAccount use ["
            + (end1 - start) + "]ms");

    String financialContractUuid = contract.getFinancialContractUuid();
    FinancialContract financialContract = financialContractService.getFinancialContractBy(financialContractUuid);

    Company company = financialContract.getCompany();

    Customer borrowerCustomer = contract.getCustomer();
    Customer companyCustomer = customerService.getCustomer(financialContract.getApp(), CustomerType.COMPANY);

    long end2 = System.currentTimeMillis();
    log.debug("#abcdefgh#preAccountReconciliation#extractReconciliationContextFromAssetSet#extract fc,customer use ["
        + (end2 - end1) + "]ms");

    if (assetSet != null) {
      DepositeAccountInfo borrowerDepositeAccountForLedegerBook = depositeAccountHandler
          .extractCustomerVirtualAccount(assetSet);
      context.setBorrowerDepositeAccountForLedegerBook(borrowerDepositeAccountForLedegerBook);
    }

    long end3 = System.currentTimeMillis();
    log.debug(
        "#abcdefgh#preAccountReconciliation#extractReconciliationContextFromAssetSet#extract DepositeAccountInfo use ["
            + (end3 - end2) + "]ms");

    context.setAssetSet(assetSet);
    context.setContract(contract);
    context.setFinancialContract(financialContract);
    context.setBorrowerCustomer(borrowerCustomer);
    context.setCompanyCustomer(companyCustomer);
    context.setLedgerBookNo(financialContract.getLedgerBookNo());

    context.setCompany(company);

    context.setContractAccount(contractAccount);
    VirtualAccount remittanceFinancialVirtualAccount = null;
    if (companyCustomer != null) {
      String customerUuid = companyCustomer.getCustomerUuid();
      remittanceFinancialVirtualAccount = virtualAccountService.getVirtualAccountByCustomerUuid(customerUuid);
    }
    VirtualAccount remittanceBorrowerVirtualAccount = null;
    if (borrowerCustomer != null) {
      String customerUuid = borrowerCustomer.getCustomerUuid();
      remittanceBorrowerVirtualAccount = virtualAccountService.getVirtualAccountByCustomerUuid(customerUuid);
    }

    DepositeAccountInfo counterPartyAccount = bankAccountCache.extractFirstBankAccountFrom(financialContract);

    context.setRemittanceFinancialVirtualAccount(remittanceFinancialVirtualAccount);
    context.setRemittanceBorrowerVirtuaAccount(remittanceBorrowerVirtualAccount);
    context.setFinancialContractAccountForLedgerBook(counterPartyAccount);
    context.setFinancialContractBankAccount(context.getFinancialContract().getCapitalAccount());

    long end4 = System.currentTimeMillis();
    log.debug(
        "#abcdefgh#preAccountReconciliation#extractReconciliationContextFromAssetSet#extract rest use ["
            + (end4 - end3) + "]ms");

    extractRelatedBalnaceFromLedgerBook(context);
  }


  public void extractRelatedBalnaceFromLedgerBook(ReconciliationContext context) {
    String ledgerBookNo = context.getLedgerBookNo();
    Contract contract = context.getContract();
    Customer borrowerCustomer = context.getBorrowerCustomer();
    Customer companyCustomer = context.getCompanyCustomer();
    AssetSet assetSet = context.getAssetSet();
    String borrowerCustomerUuid = borrowerCustomer == null ? "" : borrowerCustomer.getCustomerUuid();
    if (assetSet != null) {
      String assetUuid = assetSet.getAssetUuid();
      BigDecimal receivableAmount = ledgerBookStatHandler
          .get_receivable_amount(ledgerBookNo, assetUuid, borrowerCustomerUuid);
      BigDecimal guaranteeAmount = ledgerBookStatHandler.get_gurantee_amount(ledgerBookNo, assetUuid);
      BigDecimal unearnedAmount = ledgerBookStatHandler.get_unearned_amount(ledgerBookNo, assetUuid);

      context.setReceivalbeAmount(receivableAmount);
      context.setGuranteeAmount(guaranteeAmount);
      context.setUnearnedAmount(unearnedAmount);
    }
    if (context.getReconciliationType().isRecoveredByVirtualAccountSelf()) {
      BigDecimal balance = ledgerBookVirtualAccountHandler.getBalanceOfCustomer(ledgerBookNo, borrowerCustomerUuid);
      context.setBorrowerCustomerBalance(balance);
    } else {
      String companyCustomerUuid = companyCustomer.getCustomerUuid();
      BigDecimal balance = ledgerBookVirtualAccountHandler.getBalanceOfCustomer(ledgerBookNo, companyCustomerUuid);
      context.setFinancialAppBalance(balance);
    }

    BigDecimal repurchaseAmount = ledgerBookStatHandler.get_repurchase_amount(ledgerBookNo, contract.getUuid());
    context.setRepurchaseAmount(repurchaseAmount);
  }

  public abstract void validateReconciliationContext(ReconciliationContext context) throws AlreadyProcessedException;


  @Override
  public void refreshAsset(ReconciliationContext context) {
    RepaymentType repaymentType = RepaymentType.NORMAL;
    if (context.getDeductApplication() != null) {
      repaymentType = context.getDeductApplication().getRepaymentType();
    }
    String ledgerBookNo = context.getLedgerBookNo();
    String assetUuid = context.getAssetSet().getAssetUuid();
    Date actualRecycleTime = context.getActualRecycleTime();
    ExecutingStatus executingStatus = context.getAssetSet().getExecutingStatus();

    updateAssetStatusLedgerBookHandler
        .updateAssetsFromLedgerBook(ledgerBookNo, Collections.singletonList(assetUuid), actualRecycleTime,
            repaymentType, executingStatus);
  }

  @Override
  public void postAccountReconciliation(ReconciliationContext context) {
    AssetRecoverType recoverType = context.getRecoverType();
    AssetSet assetSet = context.getAssetSet();
    if (recoverType != null && recoverType.isLoanAssetOrGuranteeAsset()) {
      String assetUuid = assetSet.getAssetUuid();
      assetSet = repaymentPlanService.getUniqueRepaymentPlanByUuid(assetUuid);
      settlementOrderService.createSettlementOrder(assetSet);
    }

    Order order = context.getOrder();
    if (recoverType != null && recoverType.isLoanAsset() && order != null && StringUtils
        .isBlank(order.getChargesDetail())) {
      RepaymentChargesDetail chargesDetail = getChargesDetailByLoanAsset(context);
      order.setChargesDetail(chargesDetail.toJsonString());
      orderService.save(order);
    }
    createReconciliationDelayTask(recoverType, context);
    int journalVoucherType = context.getJournalVoucherType().ordinal();
    if (JournalVoucherType.JournalVoucherTypeUsedSpecialAccount().contains(journalVoucherType)) {
      String financialContractUuid = context.getFinancialContract().getFinancialContractUuid();
      if (financialContractService.isSpecialAccountConfigured(financialContractUuid)) {
        Map<String, BigDecimal> chargesDetailMapByLoanAsset = getChargesDetailMapByLoanAsset(context);
        String issuedJVUuid = context.getIssuedJournalVoucher().getUuid();
        BigDecimal bookingAmount = context.getBookingAmount();
        Long contractId = context.getContract().getId();

        clearingExecLogService
            .createClearingExecLog(assetSet, chargesDetailMapByLoanAsset, issuedJVUuid, journalVoucherType,
                bookingAmount, contractId, "");
      }
    }
  }

  private void createReconciliationDelayTask(AssetRecoverType recoverType, ReconciliationContext context) {
    try {
      log.info("create_reconciliation_delay_task start:recoverType["+recoverType+"],assetsetuuid["+(context.getAssetSet()==null?null:context.getAssetSet().getAssetUuid())+"],"
          + "repurchaseUuid["+(context.getRepurchaseDoc()==null?null:context.getRepurchaseDoc().getRepurchaseDocUuid())+"]");
      if(recoverType==null || !recoverType.isLoanAssetOrRepurchaseAsset()){
        log.info("create_reconciliation_delay_task recoverType is not loanRepurAssset");
        return;
      }
      RepaymentChargesDetail chargesDetail = null;
      if(recoverType.isLoanAsset()){
        chargesDetail = getChargesDetailByLoanAsset(context);
      }else if(recoverType.isRepurchaseAsset()){
        chargesDetail = getChargesDetailByRepurchaseDoc(context);
      }

      //正常核销
      String taskUuidForRecover = financialContractConfigurationService.getFinancialContractConfigContentContent(context.getFinancialContract().getUuid(),
          FinancialContractConfigurationCode.RECOVER_DELAY_TASK_UUID.getCode());
      if (StringUtils.isNotBlank(taskUuidForRecover)){
        log.info("taskUuid:["+taskUuidForRecover+"]");
        reconciliationDelayTaskProcess.processingDelayTask(context.getActualRecycleTime(), context.getContract(), chargesDetail, context.getAssetSet(), context.getRepurchaseDoc()==null?null:context.getRepurchaseDoc().getRepurchaseDocUuid(), recoverType, taskUuidForRecover);
      }

      //逾期核销
      String taskUuidForOverRecover = financialContractConfigurationService.getFinancialContractConfigContentContent(context.getFinancialContract().getUuid(),
          FinancialContractConfigurationCode.BQ_OVER_RECOVER_DELAY_TASK_UUID.getCode());
      if (StringUtils.isNotBlank(taskUuidForOverRecover)){
        log.info("taskUuid:["+taskUuidForOverRecover+"]");
        reconciliationDelayTaskProcess.processingDelayTask(context.getActualRecycleTime(), context.getContract(), chargesDetail, context.getAssetSet(), context.getRepurchaseDoc()==null?null:context.getRepurchaseDoc().getRepurchaseDocUuid(), recoverType, taskUuidForOverRecover);
      }

      log.info("create_reconciliation_delay_task end:recoverType["+recoverType+"],assetsetuuid["+(context.getAssetSet()==null?null:context.getAssetSet().getAssetUuid())+"],"
          + "repurchaseUuid["+(context.getRepurchaseDoc()==null?null:context.getRepurchaseDoc().getRepurchaseDocUuid())+"]");
    } catch(Exception e){
      log.error("create_reconciliation_delay_task error:recoverType["+recoverType+"],assetsetuuid["+(context.getAssetSet()==null?null:context.getAssetSet().getAssetUuid())+"],"
          + "repurchaseUuid["+(context.getRepurchaseDoc()==null?null:context.getRepurchaseDoc().getRepurchaseDocUuid())+"],msg["+ExceptionUtils.getFullStackTrace(e)+"].");
    }
  }

  void recoverEachFrozenCapitalAmount(ReconciliationContext context) {
    log.info("begin to recover_each_frozen_capital_amount");

    String ledgerBookNo = context.getLedgerBookNo();
    FinancialContract financialContract = context.getFinancialContract();
    LedgerBook book = ledgerBookService.getBookByBookNo(ledgerBookNo);
    String customerUuid = context.getCompanyCustomer().getCustomerUuid();

    String issuedJVUuid = context.getIssuedJournalVoucher().getJournalVoucherUuid();
    String sourceDocumentUuid = context.getSourceDocumentUuid();
    BigDecimal bookingAmount = context.getBookingAmount();

    if (ledgerBookV2Handler.checkLegderBookVersion(book)) {

      ledgerBookV2Handler.recover_Each_Frozen_Capital_Amount(ledgerBookNo, financialContract, customerUuid, issuedJVUuid, sourceDocumentUuid, bookingAmount);

      log.info("#end to recover_ReconciliationBase#Each_Frozen_Capital_Amount [AccountTemplate] [核销]");
    }

    if (ledgerBookV2Handler.checkLedgerbookVersionByledgerBookNoV1(ledgerBookNo)) {
      String tmpDepositDocUuidFromTmpDepositRecover = context.getTmpDepositDocUuidFromTmpDepositRecover();
      String secondNoFromTmpDepositRecover = context.getSecondNoFromTmpDepositRecover();

      businessPaymentVoucherHandler
          .recoverEachFrozenCapitalAmount(ledgerBookNo, financialContract, customerUuid, issuedJVUuid,
              sourceDocumentUuid, bookingAmount, tmpDepositDocUuidFromTmpDepositRecover, secondNoFromTmpDepositRecover);

      log.info("#end recover_ReconciliationBase#Each_Frozen_Capital_Amount [预收] ");
    }

  }

  private RepaymentChargesDetail getChargesDetailByLoanAsset(ReconciliationContext context) {
    Map<String, BigDecimal> detailAmountMap = getChargesDetailMapByLoanAsset(context);
    return new RepaymentChargesDetail(detailAmountMap);
  }

  private Map<String, BigDecimal> getChargesDetailMapByLoanAsset(ReconciliationContext context) {
    Map<String, BigDecimal> detailAmountMap = context.getBookingDetailAmount();
    if (MapUtils.isEmpty(detailAmountMap)) {
      String ledgerBookNo = context.getLedgerBookNo();
      String issuedJVUuid = context.getIssuedJournalVoucher().getJournalVoucherUuid();
      String assetUuid = context.getAssetSet().getAssetUuid();
      detailAmountMap = ledgerBookStatHandler
          .get_jv_asset_detail_amount_of_banksaving_and_independent_accounts(ledgerBookNo, issuedJVUuid, assetUuid);
    }
    return detailAmountMap;
  }

  private RepaymentChargesDetail getChargesDetailByRepurchaseDoc(ReconciliationContext context) {
    RepaymentChargesDetail repurchaseDetail = new RepaymentChargesDetail();
    RepurchaseDoc repurchaseDoc = context.getRepurchaseDoc();
    if (repurchaseDoc != null) {
      String ledgerBookNo = context.getLedgerBookNo();
      String issuedJVUuid = context.getIssuedJournalVoucher().getJournalVoucherUuid();
      String contractUuid = context.getContract().getUuid();
      Map<String, BigDecimal> detailAmount = ledgerBookStatHandler
          .get_jv_repurchase_detail_amount(ledgerBookNo, issuedJVUuid, contractUuid);
      repurchaseDetail.fillRepurchaseAmount(detailAmount);
    }
    return repurchaseDetail;
  }

}
