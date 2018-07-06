package com.suidifu.microservice.handler;

import com.suidifu.microservice.entity.SourceDocumentDetail;
import com.suidifu.owlman.microservice.enumation.CounterAccountType;
import com.suidifu.owlman.microservice.enumation.JournalVoucherType;
import com.suidifu.owlman.microservice.enumation.ReconciliationType;
import com.suidifu.owlman.microservice.exception.AlreadyProcessedException;
import com.suidifu.owlman.microservice.exception.MisMatchedConditionException;
import com.suidifu.owlman.microservice.exception.ReconciliationException;
import com.zufangbao.sun.entity.account.AccountSide;
import com.zufangbao.sun.entity.customer.Customer;
import com.zufangbao.sun.ledgerbook.stat.LedgerBookStatHandler;
import com.zufangbao.sun.yunxin.entity.AssetRecoverType;
import com.zufangbao.sun.yunxin.entity.AssetSet;
import com.zufangbao.sun.yunxin.entity.api.VoucherType;
import java.math.BigDecimal;
import java.util.Map;
import javax.annotation.Resource;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Log4j2
@Component("reconciliationForSubrogationDocumentHandler")
public class ReconciliationForSubrogationDocumentHandler extends ReconciliationForSourceDocument {

  @Resource
  private LedgerBookStatHandler ledgerBookStatHandler;

  @Override
  public ReconciliationContext preAccountReconciliation(
      Map<String, Object> inputParams) throws AlreadyProcessedException {
    long start = System.currentTimeMillis();

    ReconciliationContext context = super.preAccountReconciliation(inputParams);
    long end1 = System.currentTimeMillis();
    log.debug(
        "#abcdefgh#preAccountReconciliation#super preAccountReconciliation use [" + (end1 - start)
            + "]ms");

    super.extractReconciliationContextFromRepaymentPlanNo(context,
        context.getSourceDocumentDetail().getRepaymentPlanNo());

    long end2 = System.currentTimeMillis();
    log.debug(
        "#abcdefgh#preAccountReconciliation#super extractReconciliationContextFromRepaymentPlanNo use ["
            + (end2 - end1) + "]ms");

    if (context.getSourceDocumentDetail().getSecondType().equals(VoucherType.PAY.getKey())) {
      context.setJournalVoucherType(JournalVoucherType.TRANSFER_BILL_BY_VOUCHER);
      context.setReconciliationType(ReconciliationType.RECONCILIATION_SUBROGATION);
    } else if (context.getSourceDocumentDetail().getSecondType()
        .equals(VoucherType.ADVANCE.getKey())) {
      context.setJournalVoucherType(JournalVoucherType.TRANSFER_BILL_BY_VOUCHER_ADVANCE);
      context.setReconciliationType(ReconciliationType.RECONCILIATION_SUBROGATION);
    } else if (context.getSourceDocumentDetail().getSecondType()
        .equals(VoucherType.MERCHANT_REFUND.getKey())) {
      context.setJournalVoucherType(JournalVoucherType.TRANSFER_BILL_BY_VOUCHER_BALANCE);
      context.setReconciliationType(ReconciliationType.RECONCILIATION_SUBROGATION);
    }
    context.setRecoverType(AssetRecoverType.LOAN_ASSET);
    context.resovleJournalVoucher(
        AccountSide.DEBIT, CounterAccountType.VirtualAccount, context.getAssetSet().getAssetUuid());

    fillVoucherNo(context, context.getSourceDocument());

    if (context.getSourceDocumentDetail().getActualPaymentTime() != null && context
        .getFinancialContract().isRepaymentDayCheck()) {
      context.setActualRecycleTime(context.getSourceDocumentDetail().getActualPaymentTime());
    }

    long end3 = System.currentTimeMillis();
    log
        .debug("#abcdefgh#preAccountReconciliation#fillVoucherNo use [" + (end3 - end2) + "]ms");

    //部分核销 在父类中 按明细 （构建Map）

    return context;
  }

  @Override
  public void extractRelatedBalnaceFromLedgerBook(ReconciliationContext context) {
    String ledgerBookNo = context.getFinancialContract().getLedgerBookNo();
    Customer borrower_customer = context.getBorrowerCustomer();

    AssetSet assetSet = context.getAssetSet();
    long start = System.currentTimeMillis();
    BigDecimal receivalbeAmount = ledgerBookStatHandler
        .get_receivable_amount(ledgerBookNo, assetSet.getAssetUuid(),
            borrower_customer.getCustomerUuid());
    long end = System.currentTimeMillis();
    log
        .debug("#abcdefgh#extract_related_balnace_from_ledger_book use [" + (end - start) + "]ms");

    context.setReceivalbeAmount(receivalbeAmount);

  }

  @Override
  public void ledgerBookReconciliation(ReconciliationContext context) {
    long start = System.currentTimeMillis();
    super.recoverEachFrozenCapitalAmount(context);
    long end = System.currentTimeMillis();
    log.debug("#abcdefgh#recoverEachFrozenCapitalAmount use [" + (end - start) + "]ms");
    super.ledgerBookReconciliation(context);
  }

  @Override
  public void refreshVirtualAccount(ReconciliationContext context) {
    super.refreshVirtualAccount(context);
  }

  @Override
  public void refreshAsset(ReconciliationContext context) {
    super.refreshAsset(context);
  }

  @Override
  public void postAccountReconciliation(ReconciliationContext context) {
    super.postAccountReconciliation(context);
  }

  @Override
  public boolean accountReconciliation(Map<String, Object> params) {
    return super.accountReconciliation(params);
  }

  @Override
  public void issueJournalVoucher(ReconciliationContext context) throws AlreadyProcessedException {
    super.issueJournalVoucher(context);

  }

  @Override
  public void validateReconciliationContext(ReconciliationContext context)
      throws AlreadyProcessedException {
    super.validateReconciliationContext(context);
    AssetSet assetSet = context.getAssetSet();
    if (assetSet == null || assetSet.isClearAssetSet()) {
      throw new MisMatchedConditionException("assetSet is null or clear");
    }

    SourceDocumentDetail detail = context.getSourceDocumentDetail();
    if (detail != null && detail.getActualPaymentTime() != null && context.getFinancialContract()
        .isRepaymentDayCheck()) {

      boolean result = detail
          .checkTransactionTime(context.getSourceDocument().getOutlierTradeTime(),
              context.getFinancialContract().getRepaymentCheckDays(),
              context.getContract().getBeginDate());
      if (!result) {
        throw new ReconciliationException(" distanceDays greater than seven");
      }
    }
  }
}