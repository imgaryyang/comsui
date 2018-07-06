package com.suidifu.microservice.handler;

import com.suidifu.owlman.microservice.enumation.CounterAccountType;
import com.suidifu.owlman.microservice.enumation.JournalVoucherType;
import com.suidifu.owlman.microservice.enumation.ReconciliationType;
import com.suidifu.owlman.microservice.exception.AlreadyProcessedException;
import com.suidifu.owlman.microservice.exception.ReconciliationException;
import com.zufangbao.sun.entity.account.AccountSide;
import com.zufangbao.sun.entity.contract.Contract;
import com.zufangbao.sun.entity.customer.Customer;
import com.zufangbao.sun.entity.repurchase.RepurchaseDoc;
import com.zufangbao.sun.entity.repurchase.RepurchaseStatus;
import com.zufangbao.sun.ledgerbook.LedgerBook;
import com.zufangbao.sun.ledgerbook.stat.LedgerBookStatHandler;
import com.zufangbao.sun.service.ContractService;
import com.zufangbao.sun.service.LedgerBookService;
import com.zufangbao.sun.service.RepurchaseService;
import com.zufangbao.sun.yunxin.ContractState;
import com.zufangbao.sun.yunxin.entity.AssetRecoverType;
import com.zufangbao.sun.yunxin.entity.AssetSet;
import com.zufangbao.sun.yunxin.entity.AssetSetHistory;
import com.zufangbao.sun.yunxin.entity.api.VoucherType;
import com.zufangbao.sun.yunxin.service.RepaymentPlanHistoryService;
import com.zufangbao.sun.yunxin.service.RepaymentPlanService;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("reconciliationForRepurchaseDocumentHandler")
public class ReconciliationForRepurchaseDocumentHandler extends ReconciliationForSourceDocument {

  @Autowired
  private RepurchaseService repurchaseService;
  @Autowired
  private RepaymentPlanService repaymentPlanService;
  @Autowired
  private ContractService contractService;
  @Autowired
  private LedgerBookService ledgerBookService;
  @Autowired
  private LedgerBookStatHandler ledgerBookStatHandler;
  @Autowired
  private RepaymentPlanHistoryService repaymentPlanHistoryService;

  @Override
  public ReconciliationContext preAccountReconciliation(
      Map<String, Object> inputParams) throws AlreadyProcessedException {

    ReconciliationContext context = super.preAccountReconciliation(inputParams);
    String repaymentPlanNo = context.getSourceDocumentDetail().getRepaymentPlanNo();
    RepurchaseDoc repurchaseDoc = repurchaseService.getRepurchaseDocByUuid(repaymentPlanNo);
    Contract contract = contractService.getContract(repurchaseDoc.getContractId());
    super.extractReconciliationContextFromContractUuidWithoutAsset(context, contract);

    context.setRepurchaseDoc(repurchaseDoc);
    context.setRecoverType(AssetRecoverType.REPURCHASE_ASSET);
    context.setBookingAmount(context.getSourceDocumentDetail().getAmount());
    //设置核销明细
    context.setBookingDetailAmount(context.getSourceDocumentDetail().getRepurchaseDocDetailAmountMap());
    context.setJournalVoucherType(JournalVoucherType.TRANSFER_BILL_BY_REPURCHASE);
    context.resovleJournalVoucher(AccountSide.DEBIT, CounterAccountType.VirtualAccount, context.getRepurchaseDoc().getUuid());
    if (context.getSourceDocumentDetail().getSecondType().equals(VoucherType.REPURCHASE.getKey())) {
      context.setReconciliationType(ReconciliationType.RECONCILIATION_REPURCHASE);
    }
    fillVoucherNo(context, context.getSourceDocument());
    return context;

  }

  @Override
  public void extractRelatedBalnaceFromLedgerBook(ReconciliationContext context) {
    String ledgerBookNo = context.getFinancialContract().getLedgerBookNo();
    Contract contract = context.getContract();
    Customer borrowerCustomer = context.getBorrowerCustomer();

    AssetSet assetSet = context.getAssetSet();
    if (assetSet != null) {
      BigDecimal receivalbeAmount = ledgerBookStatHandler.get_receivable_amount(ledgerBookNo, assetSet.getAssetUuid(),
              borrowerCustomer.getCustomerUuid());
      BigDecimal unearnedAmount = ledgerBookStatHandler.get_unearned_amount(ledgerBookNo, assetSet.getAssetUuid());
      context.setReceivalbeAmount(receivalbeAmount);
      context.setUnearnedAmount(unearnedAmount);
    }

    BigDecimal repurchaseAmount = ledgerBookStatHandler.get_repurchase_amount(ledgerBookNo, contract.getUuid());
    context.setRepurchaseAmount(repurchaseAmount);
  }


  @Override
  public void refreshVirtualAccount(ReconciliationContext context) {
    super.refreshVirtualAccount(context);
  }

  @Override
  public void refreshAsset(ReconciliationContext context) {
    //回购不刷新资产
  }

  @Override
  public void postAccountReconciliation(ReconciliationContext context) {

    if (BigDecimal.ZERO.compareTo(ledgerBookStatHandler
        .get_repurchase_amount(context.getLedgerBookNo(), context.getContract().getUuid())) == 0) {
      RepurchaseDoc repurchaseDoc = context.getRepurchaseDoc();
      Contract contract = context.getContract();
      repurchaseDoc.setRepurchaseStatus(RepurchaseStatus.REPURCHASED);
      repurchaseDoc.setLastModifedTime(new Date());
      repurchaseDoc.setVerificationTime(new Date());
      List<String> assetSetUuids = repurchaseDoc.getAssetSetUuidList();
      for (String assetSetUuid : assetSetUuids) {
        AssetSet assetSet = repaymentPlanService.getUniqueRepaymentPlanByUuid(assetSetUuid);
        assetSet.repurchase();
        AssetSetHistory history = new AssetSetHistory().init(assetSet);
        repaymentPlanService.delete(assetSet);
        repaymentPlanHistoryService.save(history);
      }

      contract.setState(ContractState.REPURCHASED);
      contractService.saveOrUpdate(contract);
      repurchaseService.saveOrUpdate(repurchaseDoc);
    }
    super.postAccountReconciliation(context);
  }


  @Override
  public void validateReconciliationContext(ReconciliationContext context)
      throws AlreadyProcessedException {

    super.validateReconciliationContext(context);
    LedgerBook ledgerBook = ledgerBookService.getBookByBookNo(context.getLedgerBookNo());
    if (ledgerBook == null) {
      throw new ReconciliationException("empty ledgerBook");
    }
    if (context.getRepurchaseDoc() == null) {
      throw new ReconciliationException("empty repurchaseDoc");
    }

  }

  @Override
  public void issueJournalVoucher(ReconciliationContext context) throws AlreadyProcessedException {

    super.issueJournalVoucher(context);
  }

  @Override
  public void relatedDocumentsProcessing(ReconciliationContext context) {

  }

  @Override
  public boolean accountReconciliation(Map<String, Object> params) {
    return super.accountReconciliation(params);
  }

  @Override
  public void ledgerBookReconciliation(ReconciliationContext context) {
    super.recoverEachFrozenCapitalAmount(context);
    super.ledgerBookReconciliation(context);

  }
}
