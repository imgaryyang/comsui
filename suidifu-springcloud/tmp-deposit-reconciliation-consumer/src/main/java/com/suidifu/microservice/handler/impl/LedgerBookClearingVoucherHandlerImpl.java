package com.suidifu.microservice.handler.impl;

import com.suidifu.microservice.entity.JournalVoucher;
import com.suidifu.microservice.entity.SourceDocument;
import com.suidifu.microservice.entity.SourceDocumentDetail;
import com.suidifu.microservice.handler.LedgerBookClearingVoucherHandler;
import com.suidifu.microservice.service.JournalVoucherService;
import com.suidifu.microservice.service.SourceDocumentDetailService;
import com.suidifu.microservice.service.SourceDocumentService;
import com.suidifu.owlman.microservice.exception.DeductApplicationWriteOffException;
import com.zufangbao.sun.api.model.deduct.RecordStatus;
import com.zufangbao.sun.entity.account.DepositeAccountInfo;
import com.zufangbao.sun.entity.directbank.business.CashFlow;
import com.zufangbao.sun.ledgerbook.AssetCategory;
import com.zufangbao.sun.ledgerbook.AssetConvertor;
import com.zufangbao.sun.ledgerbook.ChartOfAccount;
import com.zufangbao.sun.ledgerbook.LedgerBook;
import com.zufangbao.sun.ledgerbook.LedgerItem;
import com.zufangbao.sun.ledgerbook.LedgerItemService;
import com.zufangbao.sun.ledgerbook.LedgerTradeParty;
import com.zufangbao.sun.ledgerbook.TAccountInfo;
import com.zufangbao.sun.service.LedgerBookService;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.yunxin.api.deduct.service.DeductApplicationService;
import com.zufangbao.sun.yunxin.entity.AssetSet;
import com.zufangbao.sun.yunxin.entity.api.deduct.DeductApplication;
import com.zufangbao.sun.yunxin.service.RepaymentPlanService;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component("ledgerBookClearingVoucherHandler")
public class LedgerBookClearingVoucherHandlerImpl implements LedgerBookClearingVoucherHandler {

  @Autowired
  private LedgerItemService ledgerItemService;

  @Autowired
  private SourceDocumentService sourceDocumentService;

  @Autowired
  private SourceDocumentDetailService sourceDocumentDetailService;

  @Autowired
  private RepaymentPlanService repaymentPlanService;

  @Autowired
  private JournalVoucherService journalVoucherService;

  @Autowired
  private LedgerBookService ledgerBookService;

  @Autowired
  private DeductApplicationService deductApplicationService;

  @Override
  public void clearingVoucherWriteOff(String ledgerBookNo,
      String deductApplicationUuid,
      LedgerTradeParty ledgerTradeParty, String journalVoucherUuid,
      String businessVoucherUuid, String sourceDocumentDetailUuid,
      DepositeAccountInfo depositeAccountInfo, CashFlow cashFlow) {
    // 根据扣款申请订单uuid查出扣款申请订单
    DeductApplication deductApplication = deductApplicationService
        .getDeductApplicationByDeductApplicationUuid(deductApplicationUuid);
    if (deductApplication.getRecordStatus() != RecordStatus.WRITE_OFF) {
      //异常
      throw new DeductApplicationWriteOffException();
    }
    //根据扣款申请订单UUid 查出原始凭证表对象
    SourceDocument sourceDocument = sourceDocumentService
        .getSourceDocumentByOutlierDocumentUuid(deductApplicationUuid, SourceDocument.FIRSTOUTLIER_DEDUCTAPPLICATION);

    List<SourceDocumentDetail> detailList = new ArrayList<SourceDocumentDetail>();

    if (sourceDocument != null) {
      //根据原始凭证表UUID查出相对应的所有原始凭证明细表
      detailList = sourceDocumentDetailService
          .getValidDeductSourceDocumentDetailsBySourceDocumentUuid(sourceDocument.getSourceDocumentUuid());
    }

    for (SourceDocumentDetail sourceDocumentDetail : detailList) {
      //根据当前明细表 还款计划编号查出 资产对象  single_loan_contract_no
      AssetSet asset = repaymentPlanService.getRepaymentPlanByRepaymentCode(sourceDocumentDetail.getRepaymentPlanNo());
      //用当前原始凭证表UUid,原始凭证明细表UUID,资产UUid查出财务凭证表
      JournalVoucher journalVoucher = journalVoucherService
          .getJournalVoucherBySourceDocumentUuidAndType(sourceDocument.getSourceDocumentUuid(),
              sourceDocumentDetail.getUuid(), asset.getAssetUuid());

      if (journalVoucher != null) {
        //备注   交易时间
        journalVoucher
            .setClearingTimeInAppendix(DateUtils.format(cashFlow.getTransactionTime(), DateUtils.LONG_DATE_FORMAT));

        journalVoucherService.saveOrUpdate(journalVoucher);

        AssetCategory assetCategory = new AssetCategory();

        if (asset != null) {

          assetCategory = AssetConvertor
              .convertAssetCategory(asset, cashFlow.getTransactionTime(), cashFlow.getCashFlowIdentity());
        }

        LedgerBook book = ledgerBookService.getBookByBookNo(ledgerBookNo);

        List<LedgerItem> ledgerItemList = ledgerItemService
            .get_jv_asset_accountName_related_ledgers(ledgerBookNo, asset.getAssetUuid(),
                journalVoucher.getJournalVoucherUuid(), Arrays.asList(
                    ChartOfAccount.FST_BANK_SAVING));

        if (!CollectionUtils.isEmpty(ledgerItemList)) {

          String sndAccountName = ledgerItemList.get(0).getSecondAccountName();
          //校验ledgerItemList
          for (LedgerItem ledgerItem : ledgerItemList) {

            if (ledgerItem.getSecondAccountName().equals(sndAccountName)) {

              sndAccountName = ledgerItem.getSecondAccountName();
            }


          }

          Map<String, List<LedgerItem>> accountNameMutilItemsMap = ledgerItemList.stream()
              .collect(Collectors.groupingBy(LedgerItem::lastAccountName, TreeMap::new, Collectors.toList()));

          Map<String, BigDecimal> accountAmountMap = new HashMap<String, BigDecimal>();

          for (String accountName : accountNameMutilItemsMap.keySet()) {

            BigDecimal amountInDb = accountNameMutilItemsMap.get(accountName).stream()
                .map(LedgerItem::getDebitSubCreditBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

            if (amountInDb.compareTo(BigDecimal.ZERO) > 0) {
              accountAmountMap.put(accountName, amountInDb);
            }

          }

          Map<String, List<LedgerItem>> ledgersMap = ledgerItemService
              .carry_over_by_account_balance_table2(book, assetCategory,
                  ledgerTradeParty, journalVoucherUuid, businessVoucherUuid,
                  sourceDocumentDetailUuid, new ArrayList<>(),
                  accountNameMutilItemsMap, accountAmountMap, CLEARING_VOUCHERACCOUNT_TO_ACCOUNT_MAPPING_TABLE_CLONE);

          List<LedgerItem> savingLedgers = new ArrayList<LedgerItem>();

          List<LedgerItem> partialLedger = ledgersMap.get("PARTIAL_LEDGER");
          List<LedgerItem> forwardLedger = ledgersMap.get("FORWARD_LEDGER");

          String partialSecondAccountName = ledgerItemList.get(0).getSecondAccountName();
          String partialSecondAccountCode = ledgerItemList.get(0).getSecondAccountUuid();

          for (LedgerItem ledgerItem : partialLedger) {

            ledgerItem.setSecondAccountName(partialSecondAccountName);
            ledgerItem.setSecondAccountUuid(partialSecondAccountCode);
          }

          //反向的
          savingLedgers.addAll(partialLedger);

          //结转的
          savingLedgers.addAll(bankSavingLedgersWashing(depositeAccountInfo, forwardLedger));

          ledgerItemService.saveItemListInsql(savingLedgers);

        }

      }

    }

  }

  private static final Map<String, String> CLEARING_VOUCHERACCOUNT_TO_ACCOUNT_MAPPING_TABLE_CLONE = new HashMap<String, String>() {
    private static final long serialVersionUID = -5389203297831108536L;

    {
      put(ChartOfAccount.TRD_BANK_SAVING_GENERAL_PRINCIPAL, ChartOfAccount.TRD_BANK_SAVING_GENERAL_PRINCIPAL);
      put(ChartOfAccount.TRD_BANK_SAVING_GENERAL_INTEREST, ChartOfAccount.TRD_BANK_SAVING_GENERAL_INTEREST);
      put(ChartOfAccount.TRD_BANK_SAVING_GENERAL_LOAN_ASSET_LOAN_SERVICE_FEE, ChartOfAccount.TRD_BANK_SAVING_GENERAL_LOAN_ASSET_LOAN_SERVICE_FEE);
      put(ChartOfAccount.TRD_BANK_SAVING_GENERAL_LOAN_ASSET_TECH_FEE, ChartOfAccount.TRD_BANK_SAVING_GENERAL_LOAN_ASSET_TECH_FEE);

      put(ChartOfAccount.TRD_BANK_SAVING_GENERAL_LOAN_ASSET_OTHER_FEE, ChartOfAccount.TRD_BANK_SAVING_GENERAL_LOAN_ASSET_OTHER_FEE);
      put(ChartOfAccount.TRD_BANK_SAVING_GENERAL_LOAN_ASSET_PENALTY, ChartOfAccount.TRD_BANK_SAVING_GENERAL_LOAN_ASSET_PENALTY);
      put(ChartOfAccount.TRD_BANK_SAVING_GENERAL_LOAN_ASSET_OVERDUE_FEE_OBLIGATION, ChartOfAccount.TRD_BANK_SAVING_GENERAL_LOAN_ASSET_OVERDUE_FEE_OBLIGATION);

      put(ChartOfAccount.TRD_BANK_SAVING_GENERAL_LOAN_ASSET_OVERDUE_FEE_SERVICE_FEE, ChartOfAccount.TRD_BANK_SAVING_GENERAL_LOAN_ASSET_OVERDUE_FEE_SERVICE_FEE);
      put(ChartOfAccount.TRD_BANK_SAVING_GENERAL_LOAN_ASSET_OVERDUE_FEE_OTHER_FEE, ChartOfAccount.TRD_BANK_SAVING_GENERAL_LOAN_ASSET_OVERDUE_FEE_OTHER_FEE);
    }
  };

  private List<LedgerItem> bankSavingLedgersWashing(DepositeAccountInfo depositeAccountInfo, List<LedgerItem> bankSavingLedgers) {
    List<LedgerItem> washingResult = new ArrayList();
    washingResult.addAll(bankSavingLedgers);
    TAccountInfo accountInfo = null;
    if (depositeAccountInfo != null) {
      String toBankAccoutName = depositeAccountInfo.getDeposite_account_name();
      accountInfo = ChartOfAccount.EntryBook().get(toBankAccoutName);
    }

    if (accountInfo == null) {
      return washingResult;
    }
    if (accountInfo.getFirstLevelAccount().getAccountName().equals(ChartOfAccount.FST_BANK_SAVING) == false) {
      return washingResult;
    }

    String sndaccountName = accountInfo.getSecondLevelAccount() == null ? ChartOfAccount.SND_BANK_SAVING_GENERAL
        : accountInfo.getSecondLevelAccount().getAccountName();
    String sndaccountCode = accountInfo.getSecondLevelAccount() == null ? ChartOfAccount.SND_BANK_SAVING_GENERAL_CODE
        : accountInfo.getSecondLevelAccount().getAccountCode();
    washingResult.forEach(ledger -> {
      if (ledger.getFirstAccountName().equals(ChartOfAccount.FST_BANK_SAVING)) {
        ledger.setSecondAccountName(sndaccountName);
        ledger.setSecondAccountUuid(sndaccountCode);
      }
    });
    return washingResult;

  }
}
