package com.suidifu.microservice.handler.impl;

import com.suidifu.microservice.entity.JournalVoucher;
import com.suidifu.microservice.entity.SourceDocument;
import com.suidifu.microservice.entity.SourceDocumentDetail;
import com.suidifu.microservice.service.JournalVoucherService;
import com.suidifu.microservice.service.SourceDocumentDetailService;
import com.suidifu.microservice.service.SourceDocumentService;
import com.suidifu.owlman.microservice.exception.DeductApplicationWriteOffException;
import com.suidifu.microservice.handler.LedgerBookClearingVoucherV2Handler;
import com.zufangbao.sun.api.model.deduct.RecordStatus;
import com.zufangbao.sun.entity.account.DepositeAccountInfo;
import com.zufangbao.sun.entity.directbank.business.CashFlow;
import com.zufangbao.sun.ledgerbook.AssetCategory;
import com.zufangbao.sun.ledgerbook.AssetConvertor;
import com.zufangbao.sun.ledgerbook.ChartOfAccount;
import com.zufangbao.sun.ledgerbook.LedgerBook;
import com.zufangbao.sun.ledgerbook.LedgerItem;
import com.zufangbao.sun.ledgerbook.LedgerTradeParty;
import com.zufangbao.sun.ledgerbook.TAccountInfo;
import com.zufangbao.sun.ledgerbookv2.dictionary.LedgerConstant;
import com.zufangbao.sun.ledgerbookv2.dictionary.LedgerConstant.Event_Type;
import com.zufangbao.sun.ledgerbookv2.enums.EventType;
import com.zufangbao.sun.ledgerbookv2.handler.LedgerBookV2Handler;
import com.zufangbao.sun.ledgerbookv2.service.LedgerItemServiceV2;
import com.zufangbao.sun.service.LedgerBookService;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.yunxin.api.deduct.service.DeductApplicationService;
import com.zufangbao.sun.yunxin.entity.AssetSet;
import com.zufangbao.sun.yunxin.entity.api.deduct.DeductApplication;
import com.zufangbao.sun.yunxin.service.RepaymentPlanService;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Log4j2
@Component("ledgerBookClearingVoucherV2Handler")
public class LedgerBookClearingVoucherV2HandlerImpl implements LedgerBookClearingVoucherV2Handler {
    @Resource
    private LedgerBookV2Handler ledgerBookV2Handler;

    @Resource
    private LedgerItemServiceV2 ledgerItemServiceV2;
    @Resource
    private SourceDocumentService sourceDocumentService;
    @Resource
    private SourceDocumentDetailService sourceDocumentDetailService;
    @Resource
    private RepaymentPlanService repaymentPlanService;
    @Resource
    private JournalVoucherService journalVoucherService;
    @Resource
    private LedgerBookService ledgerBookService;
    @Resource
    private DeductApplicationService deductApplicationService;

    @Override
    public void clearingVoucherWriteOff(String ledgerBookNo,
                                        String deductApplicationUuid,
                                        LedgerTradeParty ledgerTradeParty, String journalVoucherUuid,
                                        String businessVoucherUuid, String sourceDocumentDetailUuid,
                                        DepositeAccountInfo depositeAccountInfo, CashFlow cashFlow) {

        DeductApplication deductApplication = deductApplicationService.getDeductApplicationByDeductApplicationUuid(deductApplicationUuid);
        if (deductApplication != null && deductApplication.getRecordStatus() != RecordStatus.WRITE_OFF) {
            //异常
            throw new DeductApplicationWriteOffException();
        }

        SourceDocument sourceDocument = sourceDocumentService.getSourceDocumentByOutlierDocumentUuid(deductApplicationUuid, SourceDocument.FIRSTOUTLIER_DEDUCTAPPLICATION);

        List<SourceDocumentDetail> detailList = new ArrayList<>();

        if (sourceDocument != null) {

            detailList = sourceDocumentDetailService.getValidDeductSourceDocumentDetailsBySourceDocumentUuid(sourceDocument.getSourceDocumentUuid());
        }

        for (SourceDocumentDetail sourceDocumentDetail : detailList) {

            AssetSet asset = repaymentPlanService.getRepaymentPlanByRepaymentCode(sourceDocumentDetail.getRepaymentPlanNo());

            JournalVoucher journalVoucher = journalVoucherService.getJournalVoucherBySourceDocumentUuidAndType(sourceDocument.getSourceDocumentUuid(), sourceDocumentDetail.getUuid(), asset.getAssetUuid());

            if (journalVoucher != null) {
                journalVoucher.setClearingTimeInAppendix(DateUtils.format(cashFlow.getTransactionTime(), DateUtils.LONG_DATE_FORMAT));
                journalVoucher.setCashFlowUuid(cashFlow.getCashFlowUuid());
                journalVoucherService.saveOrUpdate(journalVoucher);

                AssetCategory assetCategory = AssetConvertor.convertAssetCategory(asset, cashFlow.getTransactionTime(), cashFlow.getCashFlowIdentity());

                LedgerBook book = ledgerBookService.getBookByBookNo(ledgerBookNo);

                List<LedgerItem> ledgerItemList = ledgerItemServiceV2.get_jv_asset_accountName_related_ledgers(ledgerBookNo, asset.getAssetUuid(), journalVoucher.getJournalVoucherUuid(), Collections.singletonList(ChartOfAccount.FST_BANK_SAVING));

                if (!CollectionUtils.isEmpty(ledgerItemList)) {

                    Map<String, List<LedgerItem>> accountNameMutilItemsMap = ledgerItemList.stream()
                            .collect(Collectors.groupingBy(LedgerItem::lastAccountName, TreeMap::new, Collectors.toList()));

                    Map<String, BigDecimal> accountAmountMap = new HashMap<>();

                    for (String accountName : accountNameMutilItemsMap.keySet()) {

                        BigDecimal amountInDb = accountNameMutilItemsMap.get(accountName).stream().map(LedgerItem::getDebitSubCreditBalance)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);

                        if (amountInDb.compareTo(BigDecimal.ZERO) > 0) {
                            accountAmountMap.put(accountName, amountInDb);
                        }
                    }

                    TAccountInfo accountInfo = null;
                    if (depositeAccountInfo != null) {
                        String toBankAccoutName = depositeAccountInfo.getDeposite_account_name();
                        accountInfo = ChartOfAccount.EntryBook().get(toBankAccoutName);
                    }

                    Map<String, Object> dataFrame = new HashMap<>();

                    dataFrame.put(LedgerConstant.Event_Type.ASSET_CATEGORY,
                            assetCategory);

                    String fstParty = StringUtils.isEmpty(ledgerTradeParty
                            .getFstParty()) ? "" : ledgerTradeParty.getFstParty();
                    String sndParty = StringUtils.isEmpty(ledgerTradeParty
                            .getSndParty()) ? "" : ledgerTradeParty.getSndParty();
                    dataFrame.put(Event_Type.A_FSTPARTY, fstParty);
                    dataFrame.put(Event_Type.A_SNDPARTY, sndParty);
                    dataFrame.put(Event_Type.B_FSTPARTY, fstParty);
                    dataFrame.put(Event_Type.B_SNDPARTY, sndParty);
                    dataFrame.put(Event_Type.DEPOSITE_ACCOUNT_INFO, depositeAccountInfo);
                    dataFrame.put(Event_Type.JOURNAL_VOUCHER_UUID, journalVoucherUuid);
                    dataFrame.put(Event_Type.BUSINESS_VOUCHER_UUID, businessVoucherUuid);
                    dataFrame.put(Event_Type.SOURCE_DOCUMENT_UUID, sourceDocumentDetailUuid);
                    dataFrame.put(Event_Type.LEDGER_BOOK_ORGNIZATION_ID, book.getLedgerBookOrgnizationId());

                    //反向
                    dataFrame.put(LedgerConstant.Event_Type_Clearing_Voucher_Write_Off.SECOND_ACCOUNT_NAME, ledgerItemList.get(0).getSecondAccountName());
                    dataFrame.put(LedgerConstant.Event_Type_Clearing_Voucher_Write_Off.SECOND_ACCOUNT_CODE, ledgerItemList.get(0).getSecondAccountUuid());

                    //结转
                    dataFrame.put(LedgerConstant.Event_Type_Clearing_Voucher_Write_Off.FORWARD_SECOND_ACCOUNT_NAME, accountInfo.getSecondLevelAccount().getAccountName());
                    dataFrame.put(LedgerConstant.Event_Type_Clearing_Voucher_Write_Off.FORWARD_SECOND_ACCOUNT_CODE, accountInfo.getSecondLevelAccount().getAccountCode());

                    dataFrame.put(LedgerConstant.Event_Type_Clearing_Voucher_Write_Off.JOURNAL_VOUCHER_UUID_DB, journalVoucher.getJournalVoucherUuid());

                    try {
                        ledgerBookV2Handler.generateAndSaveLedgerItems(book.getLedgerBookNo(), EventType.CLEARING_VOUCHER_WRITE_OFF,
                                dataFrame);
                    } catch (Exception e) {
                        log.info(ExceptionUtils.getFullStackTrace(e));
                    }
                }
            }
        }
    }
}