package com.suidifu.microservice.handler.impl;

import static com.suidifu.owlman.microservice.enumation.SourceDocumentDetailCheckState.CHECK_FAILS;
import static com.suidifu.owlman.microservice.enumation.SourceDocumentDetailCheckState.CHECK_SUCCESS;
import static com.zufangbao.gluon.spec.global.GlobalCodeSpec.VoucherErrorCodeSpec.VOUCHER_ERROR_OF_CONTRACT_CODE;
import static com.zufangbao.gluon.spec.global.GlobalCodeSpec.VoucherErrorCodeSpec.VOUCHER_ERROR_OF_FINANCIAL_CONTRACT_CODE;
import static com.zufangbao.gluon.spec.global.GlobalCodeSpec.VoucherErrorCodeSpec.VOUCHER_ERROR_OF_GUARANTEE_AMOUNT_CODE;
import static com.zufangbao.gluon.spec.global.GlobalCodeSpec.VoucherErrorCodeSpec.VOUCHER_ERROR_OF_NO_DETAIL_AMOUNT_CODE;
import static com.zufangbao.gluon.spec.global.GlobalCodeSpec.VoucherErrorCodeSpec.VOUCHER_ERROR_OF_PAYER_CODE;
import static com.zufangbao.gluon.spec.global.GlobalCodeSpec.VoucherErrorCodeSpec.VOUCHER_ERROR_OF_RECEIVABLE_AMOUNT_CODE;
import static com.zufangbao.gluon.spec.global.GlobalCodeSpec.VoucherErrorCodeSpec.VOUCHER_ERROR_OF_REPAYMENT_PLAN_NO_CODE;
import static com.zufangbao.gluon.spec.global.GlobalCodeSpec.VoucherErrorCodeSpec.VOUCHER_ERROR_OF_REPAY_SCHEDULE_NO_CODE;
import static com.zufangbao.gluon.spec.global.GlobalCodeSpec.VoucherErrorCodeSpec.VOUCHER_ERROR_OF_REPURCHASE_AMOUNT_CODE;
import static com.zufangbao.gluon.spec.global.GlobalCodeSpec.VoucherErrorCodeSpec.VOUCHER_ERROR_OF_REPURCHASE_DOC_NOT_EXIST_CODE;
import static com.zufangbao.gluon.spec.global.GlobalCodeSpec.VoucherErrorCodeSpec.VOUCHER_ERROR_OF_VOUCHER_TYPE_CODE;
import static com.zufangbao.gluon.spec.global.GlobalCodeSpec.VoucherErrorCodeSpec.VOUCHER_EXIST_PAYMENTING_ASSETSET_CODE;
import static com.zufangbao.gluon.spec.global.GlobalCodeSpec.VoucherErrorCodeSpec.VOUCHER_TRANSACTION_TIME_CODE;
import static com.zufangbao.gluon.spec.global.GlobalErrorMsgSpec.VoucherErrorMsgSpec.VOUCHER_DETAIL_TRANSACTION_TIME_CHECK;
import static com.zufangbao.gluon.spec.global.GlobalErrorMsgSpec.VoucherErrorMsgSpec.VOUCHER_ERROR_OF_CONTRACT_MSG;
import static com.zufangbao.gluon.spec.global.GlobalErrorMsgSpec.VoucherErrorMsgSpec.VOUCHER_ERROR_OF_FINANCIAL_CONTRACT_MSG;
import static com.zufangbao.gluon.spec.global.GlobalErrorMsgSpec.VoucherErrorMsgSpec.VOUCHER_ERROR_OF_GUARANTEE_AMOUNT_MSG;
import static com.zufangbao.gluon.spec.global.GlobalErrorMsgSpec.VoucherErrorMsgSpec.VOUCHER_ERROR_OF_NO_DETAIL_AMOUNT_MSG;
import static com.zufangbao.gluon.spec.global.GlobalErrorMsgSpec.VoucherErrorMsgSpec.VOUCHER_ERROR_OF_PAYER_MSG;
import static com.zufangbao.gluon.spec.global.GlobalErrorMsgSpec.VoucherErrorMsgSpec.VOUCHER_ERROR_OF_RECEIVABLE_AMOUNT_MSG;
import static com.zufangbao.gluon.spec.global.GlobalErrorMsgSpec.VoucherErrorMsgSpec.VOUCHER_ERROR_OF_REPAYMENT_PLAN_NO_MSG;
import static com.zufangbao.gluon.spec.global.GlobalErrorMsgSpec.VoucherErrorMsgSpec.VOUCHER_ERROR_OF_REPAY_SCHEDULE_NO_MSG;
import static com.zufangbao.gluon.spec.global.GlobalErrorMsgSpec.VoucherErrorMsgSpec.VOUCHER_ERROR_OF_REPURCHASE_AMOUNT_MSG;
import static com.zufangbao.gluon.spec.global.GlobalErrorMsgSpec.VoucherErrorMsgSpec.VOUCHER_ERROR_OF_REPURCHASE_DOC_NOT_EXIST_MSG;
import static com.zufangbao.gluon.spec.global.GlobalErrorMsgSpec.VoucherErrorMsgSpec.VOUCHER_ERROR_OF_VOUCHER_TYPE_MSG;
import static com.zufangbao.gluon.spec.global.GlobalErrorMsgSpec.VoucherErrorMsgSpec.VOUCHER_EXIST_PAYMENTING_ASSETSET_MSG;
import static com.zufangbao.sun.entity.repayment.order.RepaymentOrder.EMPTY;
import static com.zufangbao.sun.yunxin.entity.model.api.ExtraChargeSpec.TOTAL_OVERDUE_FEE;

import com.demo2do.core.entity.Result;
import com.suidifu.microservice.entity.SourceDocumentDetail;
import com.suidifu.microservice.handler.BusinessPaymentVoucherTaskHandler;
import com.suidifu.microservice.handler.LedgerBookVirtualAccountHandler;
import com.suidifu.microservice.handler.TemporaryDepositDocHandler;
import com.suidifu.microservice.handler.VirtualAccountHandler;
import com.suidifu.microservice.service.SourceDocumentDetailService;
import com.zufangbao.gluon.exception.GlobalRuntimeException;
import com.zufangbao.sun.entity.account.DepositeAccountInfo;
import com.zufangbao.sun.entity.account.VirtualAccount;
import com.zufangbao.sun.entity.company.corp.App;
import com.zufangbao.sun.entity.contract.Contract;
import com.zufangbao.sun.entity.contract.ContractSourceDocumentDetailMapper;
import com.zufangbao.sun.entity.customer.Customer;
import com.zufangbao.sun.entity.customer.CustomerType;
import com.zufangbao.sun.entity.directbank.business.CashFlow;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.entity.refund.TemporaryDepositDoc;
import com.zufangbao.sun.entity.repurchase.RepurchaseDoc;
import com.zufangbao.sun.ledgerbook.AccountSide;
import com.zufangbao.sun.ledgerbook.AssetCategory;
import com.zufangbao.sun.ledgerbook.AssetConvertor;
import com.zufangbao.sun.ledgerbook.BankAccountCache;
import com.zufangbao.sun.ledgerbook.ChartOfAccount;
import com.zufangbao.sun.ledgerbook.LedgerBook;
import com.zufangbao.sun.ledgerbook.LedgerBookHandler;
import com.zufangbao.sun.ledgerbook.LedgerItemService;
import com.zufangbao.sun.ledgerbook.LedgerTradeParty;
import com.zufangbao.sun.ledgerbook.stat.LedgerBookStatHandler;
import com.zufangbao.sun.ledgerbookv2.handler.LedgerBookV2Handler;
import com.zufangbao.sun.service.CashFlowService;
import com.zufangbao.sun.service.ContractService;
import com.zufangbao.sun.service.CustomerService;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.service.LedgerBookService;
import com.zufangbao.sun.service.RepurchaseService;
import com.zufangbao.sun.service.VirtualAccountService;
import com.zufangbao.sun.utils.AmountUtils;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.utils.JsonUtils;
import com.zufangbao.sun.utils.uuid.UUIDUtil;
import com.zufangbao.sun.yunxin.entity.AssetSet;
import com.zufangbao.sun.yunxin.entity.OrderPaymentStatus;
import com.zufangbao.sun.yunxin.entity.api.VoucherPayer;
import com.zufangbao.sun.yunxin.entity.api.VoucherSource;
import com.zufangbao.sun.yunxin.entity.api.VoucherType;
import com.zufangbao.sun.yunxin.service.RepaymentPlanService;
import com.zufangbao.sun.yunxin.service.refund.TemporaryDepositDocService;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
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
  private ContractService contractService;
  @Resource
  private FinancialContractService financialContractService;
  @Resource
  private RepaymentPlanService repaymentPlanService;
  @Resource
  private SourceDocumentDetailService sourceDocumentDetailService;
  @Resource
  private LedgerBookStatHandler ledgerBookStatHandler;
  @Resource
  private LedgerBookVirtualAccountHandler ledgerBookVirtualAccountHandler;
  @Resource
  private LedgerBookHandler ledgerBookHandler;
  @Resource
  private LedgerBookService ledgerBookService;
  @Resource
  private BankAccountCache bankAccountCache;
  @Resource
  private VirtualAccountHandler virtualAccountHandler;
  @Resource
  private VirtualAccountService virtualAccountService;
  @Resource
  private LedgerItemService ledgerItemService;
  @Resource
  private RepurchaseService repurchaseService;
  @Resource
  private TemporaryDepositDocService temporaryDepositDocService;
  @Resource
  private CashFlowService cashFlowService;
  @Resource
  private TemporaryDepositDocHandler temporaryDepositDocHandler;
  @Resource
  private LedgerBookV2Handler ledgerBookV2Handler;

  @Override
  public boolean isDetailValid(String detailUuid, String financialContractNo, String ledgerBookNo,
      Date cashFlowTransactionTime) {
    SourceDocumentDetail detail = sourceDocumentDetailService.getSourceDocumentDetail(detailUuid);
    if (detail == null) {
      log.error("isDetailValid false, detail is null, detailUuid:{}.", detailUuid);
      return false;
    }
    if (!detail.isUncheck()) {
      return (detail.getCheckState() != CHECK_FAILS);
    }
    boolean isAllCheckPass = true;
    String errorMsg = "";
    try {
      String keyOfVoucherType = detail.getSecondType();
      String uniqueId = detail.getContractUniqueId();
      //校验贷款合同Contract
      Contract contract = checkContract(uniqueId);
      String financialContractUuid = contract.getFinancialContractUuid();
      FinancialContract financialContract = financialContractService.getFinancialContractBy(financialContractUuid);
      //校验信托计划FinancialContract
      checkFinancialContract(financialContract, financialContractNo);

      String keyOfVoucherSource = detail.getFirstType();
      if (VoucherSource.BUSINESS_PAYMENT_VOUCHER.getKey().equals(keyOfVoucherSource)) {
        checkTransactionTime(detail, cashFlowTransactionTime, financialContract, contract);
      }

      String contractUuid = contract.getUuid();
      String customerUuid = contract.getCustomerUuid();
      if (keyOfVoucherType.equals(VoucherType.PAY.getKey()) || keyOfVoucherType.equals(VoucherType.ADVANCE.getKey())
          || keyOfVoucherType.equals(VoucherType.GUARANTEE.getKey())
          || keyOfVoucherType.equals(VoucherType.MERCHANT_REFUND.getKey())) {
        String repaymentPlanNo = detail.getRepaymentPlanNo();
        String repayScheduleNo = detail.getRepayScheduleNo();
        //校验还款计划AssetSet
        AssetSet repaymentPlan = checkRepaymentPlan(contractUuid, repaymentPlanNo, repayScheduleNo);

        //商户还款计划编号比还款计划编号优先级高,只要存在正确的商户还款计划编号都接受
        assert repaymentPlan != null;
        String singleLoanContractNo = repaymentPlan.getSingleLoanContractNo();
        if (StringUtils.isNotEmpty(singleLoanContractNo) && !singleLoanContractNo.equals(repaymentPlanNo)) {
          //repaymentPlanNo与AssetSet中的singleLoanContractNo不一致时
          detail.setRepaymentPlanNo(singleLoanContractNo);
        }

        //校验明细中是否包含支付中的还款计划
        checkoutIfPaymentingAssetset(repaymentPlan);

        Date today = DateUtils.getToday();
        boolean includeUnearned = !(repaymentPlan.isClearAssetSet() || repaymentPlan.isAssetRecycleDate(today)
            || repaymentPlan.isOverdueDate(today));
        String assetSetUuid = repaymentPlan.getAssetUuid();
        //校验明细金额
        checkVoucherAmount(detail, ledgerBookNo, assetSetUuid, customerUuid, contractUuid, includeUnearned);
      } else if (keyOfVoucherType.equals(VoucherType.REPURCHASE.getKey())) {
        RepurchaseDoc repurchaseDoc = checkRepurchaseDoc(contract.getId());
        String repurchaseDocUuid = repurchaseDoc.getRepurchaseDocUuid();
        detail.setRepaymentPlanNo(repurchaseDocUuid);
        //校验明细金额
        checkVoucherAmount(detail, ledgerBookNo, EMPTY, customerUuid, contractUuid, true);
      }
      //校验付款人类型
      checkVoucherPayer(detail.getPayer());

    } catch (GlobalRuntimeException e) {
      isAllCheckPass = false;
      errorMsg = e.getMessage();
    } catch (Exception e) {
      isAllCheckPass = false;
      errorMsg = "未知错误";
    }

    if (isAllCheckPass) {
      detail.setCheckState(CHECK_SUCCESS);
      sourceDocumentDetailService.saveOrUpdate(detail);
      return true;
    }

    detail.setCheckState(CHECK_FAILS);
    detail.setComment(errorMsg);
    sourceDocumentDetailService.saveOrUpdate(detail);
    log.error("检查明细内容的真实性失败-----> SourceDocumentDetailId:{}, 失败原因:{}", detailUuid, errorMsg);
    return false;
  }

  private void checkoutIfPaymentingAssetset(AssetSet assetSet) {
    if (assetSet == null) {
      checkFailsThrowException(VOUCHER_ERROR_OF_REPAYMENT_PLAN_NO_CODE, VOUCHER_ERROR_OF_REPAYMENT_PLAN_NO_MSG);
      return;
    }
    if (assetSet.getOrderPaymentStatus() == OrderPaymentStatus.ORDERPAYMENTING) {
      checkFailsThrowException(VOUCHER_EXIST_PAYMENTING_ASSETSET_CODE, VOUCHER_EXIST_PAYMENTING_ASSETSET_MSG);
    }
  }

  private void checkTransactionTime(SourceDocumentDetail detail, Date cashFlowTransactionTime,
      FinancialContract financialContract, Contract contract) {
    if (financialContract.isRepaymentDayCheck() && detail.getActualPaymentTime() != null) {
      int days = financialContract.getRepaymentCheckDays();
      boolean result = detail.checkTransactionTime(cashFlowTransactionTime, days, contract.getBeginDate());
      if (!result) {
        checkFailsThrowException(VOUCHER_TRANSACTION_TIME_CODE, VOUCHER_DETAIL_TRANSACTION_TIME_CHECK + days);
      }
    }
  }

  private Contract checkContract(String contractUniqueId) {
    Contract contract = contractService.getContractByUniqueId(contractUniqueId);
    if (contract == null) {
      checkFailsThrowException(VOUCHER_ERROR_OF_CONTRACT_CODE, VOUCHER_ERROR_OF_CONTRACT_MSG);
    }
    return contract;
  }

  private void checkFailsThrowException(int code, String message) {
    Result result = new Result().initialize(code + "", message);
    String errorMsg = JsonUtils.toJSONString(Collections.singletonList(result));
    throw new GlobalRuntimeException(errorMsg);
  }

  private void checkFinancialContract(FinancialContract financialContract, String financialContractNo) {
    if (financialContract == null || !StringUtils.equals(financialContractNo, financialContract.getContractNo())) {
      checkFailsThrowException(VOUCHER_ERROR_OF_FINANCIAL_CONTRACT_CODE, VOUCHER_ERROR_OF_FINANCIAL_CONTRACT_MSG);
    }
  }

  private AssetSet checkRepaymentPlan(String contractUuid, String repaymentPlanNo, String repayScheduleNo) {
    AssetSet repaymentPlan = repaymentPlanService.getRepaymentPlanByConditions(repayScheduleNo, repaymentPlanNo);
    if (repaymentPlan == null) {
      if (StringUtils.isNotEmpty(repayScheduleNo)) {
        checkFailsThrowException(VOUCHER_ERROR_OF_REPAY_SCHEDULE_NO_CODE, VOUCHER_ERROR_OF_REPAY_SCHEDULE_NO_MSG);
      } else {
        checkFailsThrowException(VOUCHER_ERROR_OF_REPAYMENT_PLAN_NO_CODE, VOUCHER_ERROR_OF_REPAYMENT_PLAN_NO_MSG);
      }
      return null;
    }
    if (!StringUtils.equals(contractUuid, repaymentPlan.getContractUuid())) {
      checkFailsThrowException(VOUCHER_ERROR_OF_REPAYMENT_PLAN_NO_CODE, VOUCHER_ERROR_OF_REPAYMENT_PLAN_NO_MSG);
    }
    return repaymentPlan;
  }

  private RepurchaseDoc checkRepurchaseDoc(Long contractId) {
    RepurchaseDoc repurchaseDoc = repurchaseService.getRepurchaseDocBy(contractId);
    if (repurchaseDoc == null) {
      checkFailsThrowException(VOUCHER_ERROR_OF_REPURCHASE_DOC_NOT_EXIST_CODE,
          VOUCHER_ERROR_OF_REPURCHASE_DOC_NOT_EXIST_MSG);
    }
    return repurchaseDoc;
  }

  /**
   * 校验还款人
   */
  private void checkVoucherPayer(VoucherPayer voucherPayer) {
    if (voucherPayer == null) {
      checkFailsThrowException(VOUCHER_ERROR_OF_PAYER_CODE, VOUCHER_ERROR_OF_PAYER_MSG);
    }
  }

  /**
   * 检查明细金额
   */
  private void checkVoucherAmount(SourceDocumentDetail sourceDocumentDetail, String ledgerBookNo, String assetSetUuid,
      String customerUuid, String contractUuid, boolean isIncludeUnearned) {
    String keyOfVoucherType = sourceDocumentDetail.getSecondType();
    VoucherType voucherType = VoucherType.fromKey(keyOfVoucherType);
    if (voucherType == null) {
      checkFailsThrowException(VOUCHER_ERROR_OF_VOUCHER_TYPE_CODE, VOUCHER_ERROR_OF_VOUCHER_TYPE_MSG);
      return;
    }
    switch (voucherType) {
      case PAY:
      case ADVANCE:
      case MERCHANT_REFUND:
        checkVoucherAmountOfTypePay(sourceDocumentDetail, ledgerBookNo, assetSetUuid, customerUuid, isIncludeUnearned);
        break;
      case GUARANTEE:
        checkVoucherAmountOfGuarantee(sourceDocumentDetail, ledgerBookNo, assetSetUuid);
        break;
      case REPURCHASE:
        checkVoucherAmountOfTypeRepurchase(sourceDocumentDetail, ledgerBookNo, contractUuid);
        break;
      default:
        checkFailsThrowException(VOUCHER_ERROR_OF_VOUCHER_TYPE_CODE, VOUCHER_ERROR_OF_VOUCHER_TYPE_MSG);
    }
  }

  private void checkVoucherAmountOfTypeRepurchase(SourceDocumentDetail sourceDocumentDetail, String ledgerBookNo,
      String contractUuid) {
    BigDecimal repurchaseAmount = ledgerBookStatHandler.get_repurchase_amount(ledgerBookNo, contractUuid);
    BigDecimal amount = sourceDocumentDetail.getAmount();
    BigDecimal repurchaseDetailAmount = sourceDocumentDetail.repurchaseDetailAmount();
    boolean needReturn = checkRepurchaseThreeAmount(amount, repurchaseDetailAmount, repurchaseAmount);
    if (needReturn) {
      return;
    }
    Map<String, BigDecimal> repurchaseSnapshot = ledgerBookStatHandler.repurchase_snapshot(ledgerBookNo, contractUuid);
    HashMap<String, BigDecimal> amountMap = sourceDocumentDetail.getRepurchaseDocDetailAmountMap();
    Set<Entry<String, BigDecimal>> entrySet = amountMap.entrySet();
    Iterator<Entry<String, BigDecimal>> iterator = entrySet.iterator();
    List<Result> resultList = new ArrayList<>();
    BigDecimal amountInDB;
    String amountName;
    String errorMsg = "";
    Integer errorCode;
    Result result;
    while (iterator.hasNext()) {
      Entry<String, BigDecimal> entry = iterator.next();
      if (AmountUtils.equals(BigDecimal.ZERO, entry.getValue())) {
        continue;
      }
      amountInDB = repurchaseSnapshot.getOrDefault(entry.getKey(), BigDecimal.ZERO);
      if (entry.getValue().compareTo(amountInDB) > 0) {
        amountName = sourceDocumentDetail.getDetailAmountName(entry.getKey());
        errorCode = sourceDocumentDetail.getDetailErrorCode(entry.getKey());
        errorMsg = String.format("(%s) %s 应为 %s", amountName, entry.getValue(), amountInDB);
        log.error(String
            .format("检查回购明细金额失败, sourceDocumentDetail uuid %s, %s", sourceDocumentDetail.getUuid(),
                errorMsg));
        result = new Result().initialize(errorCode + "", errorMsg);
        resultList.add(result);
      }
    }
    if (CollectionUtils.isNotEmpty(resultList)) {
      throw new GlobalRuntimeException(JsonUtils.toJSONString(resultList));
    }
  }

  private boolean checkRepurchaseThreeAmount(BigDecimal amount, BigDecimal detailAmount, BigDecimal amountInDb) {
    if (AmountUtils.isEmpty(amount)) {
      // 检查回购明细内容的真实性失败，明细总金额为零
      checkFailsThrowException(VOUCHER_ERROR_OF_NO_DETAIL_AMOUNT_CODE, VOUCHER_ERROR_OF_NO_DETAIL_AMOUNT_MSG);
    }
    if (amount.compareTo(amountInDb) > 0) {
      // 检查回购明细金额失败，明细金额大于回购单金额
      checkFailsThrowException(VOUCHER_ERROR_OF_REPURCHASE_AMOUNT_CODE, VOUCHER_ERROR_OF_REPURCHASE_AMOUNT_MSG);
    }
    if (AmountUtils.isEmpty(detailAmount)) {
      if (AmountUtils.equals(amount, amountInDb)) {
        return true;
      } else {
        // 检查回购明细内容的真实性失败，明细未传
        checkFailsThrowException(VOUCHER_ERROR_OF_NO_DETAIL_AMOUNT_CODE, VOUCHER_ERROR_OF_NO_DETAIL_AMOUNT_MSG);
      }
    }
    if (AmountUtils.notEquals(amount, detailAmount)) {
      // 检查回购明细金额失败，明细总金额不等于其他金额总和
      checkFailsThrowException(VOUCHER_ERROR_OF_REPURCHASE_AMOUNT_CODE, VOUCHER_ERROR_OF_REPURCHASE_AMOUNT_MSG);
    }
    return false;
  }

  private void checkVoucherAmountOfGuarantee(SourceDocumentDetail sourceDocumentDetail, String ledgerBookNo,
      String assetSetUuid) {
    BigDecimal amount = sourceDocumentDetail.getAmount();
    BigDecimal guranteeAmount = ledgerBookStatHandler.get_gurantee_amount(ledgerBookNo, assetSetUuid);
    if (amount.compareTo(guranteeAmount) > 0) {
      // 检查担保补足明细金额失败，明细金额大于还款计划担保金额
      checkFailsThrowException(VOUCHER_ERROR_OF_GUARANTEE_AMOUNT_CODE, VOUCHER_ERROR_OF_GUARANTEE_AMOUNT_MSG);
    }
  }

  private void checkVoucherAmountOfTypePay(SourceDocumentDetail sourceDocumentDetail, String ledgerBookNo,
      String assetSetUuid, String customerUuid, boolean isIncludeUnearned) {
    BigDecimal amount = sourceDocumentDetail.getAmount();
    BigDecimal unrecoveredAmount = ledgerBookStatHandler
        .unrecovered_asset_snapshot(ledgerBookNo, assetSetUuid, customerUuid, isIncludeUnearned);
    if (amount.compareTo(unrecoveredAmount) > 0) {
      // 检查明细内容的真实性失败，金额大于未还金额
      checkFailsThrowException(VOUCHER_ERROR_OF_RECEIVABLE_AMOUNT_CODE, VOUCHER_ERROR_OF_RECEIVABLE_AMOUNT_MSG);
    }
    BigDecimal detailAmount = sourceDocumentDetail.detailAmount();
    if (AmountUtils.equals(BigDecimal.ZERO, detailAmount)) {
      if (AmountUtils.equals(amount, unrecoveredAmount)) {
        return;
      } else {
        // 检查明细内容的真实性失败，明细未传
        checkFailsThrowException(VOUCHER_ERROR_OF_NO_DETAIL_AMOUNT_CODE, VOUCHER_ERROR_OF_NO_DETAIL_AMOUNT_MSG);
      }
    }
    Map<String, BigDecimal> unrecoveredAssetSnapshot = ledgerBookStatHandler
        .unrecovered_asset_snapshot(ledgerBookNo, assetSetUuid, isIncludeUnearned);
    HashMap<String, BigDecimal> detailAmountMap = sourceDocumentDetail.getDetailAmountMap();
    Set<Entry<String, BigDecimal>> entrySet = detailAmountMap.entrySet();
    Iterator<Entry<String, BigDecimal>> iterator = entrySet.iterator();
    List<Result> resultList = new ArrayList<>();
    String amountName, errorMsg;
    Integer errorCode;
    Result result;
    while (iterator.hasNext()) {
      Entry<String, BigDecimal> entry = iterator.next();
      String key = entry.getKey();
      if (StringUtils.equals(key, TOTAL_OVERDUE_FEE)) {
        continue;
      }
      BigDecimal requestAmount = entry.getValue();
      if (AmountUtils.equals(BigDecimal.ZERO, requestAmount)) {
        continue;
      }
      BigDecimal amountInDB = unrecoveredAssetSnapshot.getOrDefault(key, BigDecimal.ZERO);
      if (requestAmount.compareTo(amountInDB) > 0) {
        amountName = sourceDocumentDetail.getDetailAmountName(key);
        errorCode = sourceDocumentDetail.getDetailErrorCode(key);
        errorMsg = String.format("(%s) %s 应为 %s", amountName, requestAmount, amountInDB);
        log.error(String
            .format("检查代偿明细金额失败, sourceDocumentDetail uuid %s, %s", sourceDocumentDetail.getUuid(),
                errorMsg));
        result = new Result().initialize(errorCode + "", errorMsg);
        resultList.add(result);
      }
    }
    if (CollectionUtils.isNotEmpty(resultList)) {
      throw new GlobalRuntimeException(JsonUtils.toJSONString(resultList));
    }

  }

  @Override
  public void recoverEachFrozenCapitalAmount(String ledgerBookNo,
      FinancialContract financialContract,
      String companyCustomerUuid, String jvUuid, String sdUuid, BigDecimal bookingAmount,
      String tmpDepositDocUuid,
      String sndSecondNo) {
    //冻结资金 debit
    LedgerBook book = ledgerBookService.getBookByBookNo(ledgerBookNo);
    String financialCompayUuid = financialContract.getCompany().getUuid();
    AssetCategory assetCategory = AssetConvertor
        .convertAssetCategory(tmpDepositDocUuid, sndSecondNo);
    LedgerTradeParty frozenCapitalTradeParty = new LedgerTradeParty(companyCustomerUuid,
        financialContract.getCompany().getUuid());
    String batchUuid = UUID.randomUUID().toString();
    Date bookInDate = new Date();
    ledgerBookHandler
        .book_single_asset_with_batch_uuid(book, frozenCapitalTradeParty, assetCategory,
            bookingAmount, ChartOfAccount.SND_FROZEN_CAPITAL_VOUCHER, AccountSide.DEBIT, jvUuid, "",
            sdUuid, batchUuid, bookInDate);

    //银行存款 credit
    DepositeAccountInfo accountInfo = bankAccountCache
        .extractFirstBankAccountFrom(financialContract);
    LedgerTradeParty debitTradePary = new LedgerTradeParty(financialCompayUuid, "");
    ledgerBookHandler
        .book_single_asset_with_batch_uuid(book, debitTradePary, assetCategory, bookingAmount,
            accountInfo.getDeposite_account_name(), com.zufangbao.sun.ledgerbook.AccountSide.CREDIT,
            jvUuid, "", sdUuid, batchUuid, bookInDate);
  }

  @Override
  public boolean unfreezeCapitalAmountOfVoucher(String sourceDocumentUuid, String financialContractNo,
      LedgerBook book, String tmpDepositDocUuid, String toCreditAccount) {
    FinancialContract financialContract = financialContractService.getUniqueFinancialContractBy(financialContractNo);
    if (financialContract == null) {
      log.error("unfreezeCapitalAmountOfVoucher fail, financialContract is null");
      return false;
    }
    App app = financialContract.getApp();

    Customer companyCustomer = customerService.getCustomer(app, CustomerType.COMPANY);
    if (companyCustomer == null) {
      log.error("unfreezeCapitalAmountOfVoucher fail, companyCustomer is null ");
      return false;
    }

    String customerUuid = companyCustomer.getCustomerUuid();
    String ledgerBookNo = book.getLedgerBookNo();
    BigDecimal balanceOfFrozenCapital = ledgerBookVirtualAccountHandler
        .getBalanceOfFrozenCapital(ledgerBookNo, customerUuid, sourceDocumentUuid, tmpDepositDocUuid);

    if (BigDecimal.ZERO.compareTo(balanceOfFrozenCapital) == 0) {
      //无冻结资金
      return true;
    }
    TemporaryDepositDoc temporaryDepositDoc = temporaryDepositDocService.getTemporaryDepositDocBy(tmpDepositDocUuid);
    String cashFlowIdentity = null;
    if (temporaryDepositDoc != null) {
      String cashFlowUuid = temporaryDepositDoc.getCashFlowUuid();
      CashFlow cashFlow = cashFlowService.getCashFlowByCashFlowUuid(cashFlowUuid);
      cashFlowIdentity = (cashFlow == null ? null : cashFlow.getCashFlowIdentity());
    }
    AssetCategory assetoryCategory = AssetConvertor.convertTemporayDepositDocAssetCategory(tmpDepositDocUuid, cashFlowIdentity);

    String companyUuid = financialContract.getCompany().getUuid();
    LedgerTradeParty creditTradeParty = new LedgerTradeParty(customerUuid, companyUuid);
    String batchUuid = UUIDUtil.random32UUID();
    Date bookInDate = new Date();


    if (ledgerBookV2Handler.checkLegderBookVersion(book)){
      log.info("begin to #unfreeze_capital_amount_of_voucher sourceDocumentUuid:["+sourceDocumentUuid+"] [script]");
      ledgerBookV2Handler.book_single_asset_with_batch_uuid(book, creditTradeParty, assetoryCategory, balanceOfFrozenCapital,
          toCreditAccount, "", "", sourceDocumentUuid);
      log.info("end #unfreeze_capital_amount_of_voucher sourceDocumentUuid:["+sourceDocumentUuid+"] [script] ");
    }

    if (ledgerBookV2Handler.checkLedgerBookVersionV1(book)){
      log.info("begin to #unfreeze_capital_amount_of_voucher sourceDocumentUuid:["+sourceDocumentUuid+"] [pro]");
      ledgerBookHandler
          .book_single_asset_with_batch_uuid(book, creditTradeParty, assetoryCategory, balanceOfFrozenCapital,
              ChartOfAccount.SND_UNFROZEN_CAPITAL_VOUCHER, AccountSide.DEBIT, "", "", sourceDocumentUuid, batchUuid,
              bookInDate);
      ledgerBookHandler
          .book_single_asset_with_batch_uuid(book, creditTradeParty, assetoryCategory, balanceOfFrozenCapital,
              toCreditAccount, AccountSide.CREDIT, "", "", sourceDocumentUuid, batchUuid, bookInDate);
      log.info("end #unfreeze_capital_amount_of_voucher sourceDocumentUuid:["+sourceDocumentUuid+"] [pro] ");
    }

    //refresh
    VirtualAccount virtualAccount = virtualAccountService.getVirtualAccountByCustomerUuid(customerUuid);
    String oldVersion = virtualAccount.getVersion();

    String financialContractUuid = financialContract.getFinancialContractUuid();
    virtualAccountHandler.refreshVirtualAccountBalance(ledgerBookNo, customerUuid, financialContractUuid, oldVersion);
    return true;
  }

  @Override
  public boolean freezeTmpDeposit(String sourceDocumentUuid, BigDecimal bookingAmount, LedgerBook ledgerBook,
      String financialContractUuid, String tmpDepositDocUuid, String secondNo) {
    FinancialContract financialContract = financialContractService.getFinancialContractBy(financialContractUuid);
    if (financialContract == null) {
      log.error("freezeTmpDeposit fail, financialContract is null");
      return false;
    }

    App app = financialContract.getApp();
    Customer companyCustomer = customerService.getCustomer(app, CustomerType.COMPANY);
    if (companyCustomer == null) {
      log.error("freezeTmpDeposit fail, companyCustomer is null ");
      return false;
    }
    String ledgerBookNo = ledgerBook.getLedgerBookNo();
    boolean existBookedSourceDocument = ledgerItemService
        .existBookedSourceDocumentOfTmpDeposit(ledgerBookNo, sourceDocumentUuid, bookingAmount, tmpDepositDocUuid,
            secondNo);
    if (existBookedSourceDocument) {
      return true;
    }
    String customerUuid = companyCustomer.getCustomerUuid();
    VirtualAccount virtualAccount = virtualAccountService.getVirtualAccountByCustomerUuid(customerUuid);
    String oldVirtualAccountVersion = virtualAccount.getVersion();

    BigDecimal balance = ledgerBookVirtualAccountHandler.getBalanceOfCustomer(ledgerBookNo, customerUuid);
    if (balance.compareTo(bookingAmount) < 0) {
      return false;
    }
    AssetCategory assetoryCategory = AssetConvertor.convertAssetCategory(tmpDepositDocUuid, secondNo);
    String financialCompanyUuid = financialContract.getCompany().getUuid();
    LedgerTradeParty frozenCapitalTradeParty = new LedgerTradeParty(customerUuid, financialCompanyUuid);
    LedgerTradeParty independentAccountTradePary = new LedgerTradeParty(customerUuid, financialCompanyUuid);
    //滞留单-》冻结资金
    boolean checkLedgderBookVersion = ledgerBookV2Handler.checkLegderBookVersion(ledgerBook);
    if (checkLedgderBookVersion) {
      ledgerBookV2Handler.book_compensatory_remittance_virtual_accountV2(ledgerBook, frozenCapitalTradeParty,
          independentAccountTradePary, "", "", sourceDocumentUuid, bookingAmount, assetoryCategory,
          ChartOfAccount.SND_FROZEN_CAPITAL_VOUCHER, ChartOfAccount.SND_LIABILITIES_OF_INDEPENDENT_ACCOUNTS_PENDING);
    }
    if (ledgerBookV2Handler.checkLedgerBookVersionV1(ledgerBook)) {
      ledgerBookHandler.book_compensatory_remittance_virtual_accountV2(ledgerBook, frozenCapitalTradeParty,
          independentAccountTradePary, "", "", sourceDocumentUuid, bookingAmount, assetoryCategory,
          ChartOfAccount.SND_FROZEN_CAPITAL_VOUCHER, ChartOfAccount.SND_LIABILITIES_OF_INDEPENDENT_ACCOUNTS_PENDING);
    }
    temporaryDepositDocHandler.refreshBookingAmountAndStatus(tmpDepositDocUuid, ledgerBookNo);
    virtualAccountHandler
        .refreshVirtualAccountBalance(ledgerBookNo, customerUuid, financialContractUuid, oldVirtualAccountVersion);
    return true;
  }

  @Override
  public boolean unfreezeAmountOfTmpDepositDocAndVirtualAccount(String sourceDocumentUuid, String financialContractNo,
      LedgerBook book, String tmpDepositDocUuid) {
    unfreezeCapitalAmountOfVoucher(sourceDocumentUuid, financialContractNo, book, tmpDepositDocUuid,
        ChartOfAccount.SND_LIABILITIES_OF_INDEPENDENT_ACCOUNTS_PENDING);
    temporaryDepositDocHandler.refreshBookingAmountAndStatus(tmpDepositDocUuid, book.getLedgerBookNo());
    return true;
  }

  @Override
  public Map<String, String> getCriticalMarker(List<String> sourceDocumentDetailList) {
    if (CollectionUtils.isEmpty(sourceDocumentDetailList)) {
      log.info("#sychronizedMarker# detailUuids size is 0");
      return Collections.emptyMap();
    }
    List<ContractSourceDocumentDetailMapper> contractSourceDocumentDetailMapperList = sourceDocumentDetailService
        .getContractUuidSourceDocumentDetailUuidMapper(sourceDocumentDetailList);

    return contractSourceDocumentDetailMapperList.stream().collect(Collectors
        .toMap(ContractSourceDocumentDetailMapper::getSourceDocumentDetailUuid,
            ContractSourceDocumentDetailMapper::getContractUuid));

  }

}
