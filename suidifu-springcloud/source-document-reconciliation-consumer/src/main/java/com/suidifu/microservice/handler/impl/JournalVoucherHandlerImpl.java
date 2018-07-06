package com.suidifu.microservice.handler.impl;

import com.suidifu.microservice.handler.JournalVoucherHandler;
import com.suidifu.microservice.model.ReconciliationContext;
import com.suidifu.microservice.handler.LedgerBookClearingVoucherHandler;
import com.suidifu.microservice.handler.LedgerBookClearingVoucherV2Handler;
import com.zufangbao.sun.entity.account.DepositeAccountInfo;
import com.zufangbao.sun.entity.contract.Contract;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.ledgerbook.LedgerBook;
import com.zufangbao.sun.ledgerbook.LedgerBookCarryOverContextWithAsset;
import com.zufangbao.sun.ledgerbook.LedgerBookHandler;
import com.zufangbao.sun.ledgerbook.LedgerTradeParty;
import com.zufangbao.sun.ledgerbookv2.handler.LedgerBookV2Handler;
import com.zufangbao.sun.service.ContractService;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.service.LedgerBookService;
import com.zufangbao.sun.yunxin.entity.AssetRecoverType;
import com.zufangbao.sun.yunxin.entity.AssetSet;
import java.math.BigDecimal;
import java.util.Map;
import javax.annotation.Resource;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Log4j2
@Component("journalVoucherHandler")
public class JournalVoucherHandlerImpl implements JournalVoucherHandler {
    @Resource
    private LedgerBookHandler ledgerBookHandler;
    @Resource
    private LedgerBookV2Handler ledgerBookV2Handler;
    @Resource
    private LedgerBookClearingVoucherHandler ledgerBookClearingVoucherHandler;
    @Resource
    private LedgerBookClearingVoucherV2Handler ledgerBookClearingVoucherV2Handler;

    @Resource
    private LedgerBookService ledgerBookService;
    @Resource
    private FinancialContractService financialContractService;
    @Resource
    private ContractService contractService;

    @Override
    public void recoverAssetByReconciliationContext(ReconciliationContext context) {
        AssetSet assetSet = context.getAssetSet();
        AssetRecoverType assetRecoverType = context.getRecoverType();
        BigDecimal bookingAmount = context.getBookingAmount();
        String journalVoucherUuid = context.getIssuedJournalVoucher().getUuid();
        String businessVoucherUuid = context.getIssuedJournalVoucher().getBusinessVoucherUuid();
        String sourceDocumentDetailUuid = context.getSourceDocumentDetailUuid();
        String sourceDocumentUuid = context.getSourceDocumentUuid();
        Map<String, BigDecimal> bookingAmountDetailTable = context.getBookingDetailAmount();
        DepositeAccountInfo depositeAccountInfo = context.getRemittanceAccountForLedgerBook();

        boolean ifRecoveryMoney = true;
        String contractUuid = context.getContract().getUuid();
        LedgerBookCarryOverContextWithAsset parameterObject = null;

        LedgerBook book = null;

        if (assetSet != null) {
            book = ledgerBookService.getBookByAsset(assetSet);
            if (book == null) return;
            parameterObject = new LedgerBookCarryOverContextWithAsset(book,
                    assetSet,
                    journalVoucherUuid,
                    businessVoucherUuid,
                    sourceDocumentUuid,
                    depositeAccountInfo,
                    bookingAmount,
                    ifRecoveryMoney,
                    context.getActualRecycleTime(),
                    context.getCashFlowIdentity());
        }
        if (null == assetSet) {
            book = ledgerBookService.getBookByBookNo(context.getLedgerBookNo());
            if (null == book) {
                Contract contract = contractService.getContract(contractUuid);
                if (contract == null) {
                    return;
                }
                FinancialContract financialContract = financialContractService.getFinancialContractBy(contract.getFinancialContractUuid());
                book = ledgerBookService.getBookByBookNo(financialContract.getLedgerBookNo());
            }
        }

        if (assetRecoverType == AssetRecoverType.LOAN_ASSET) {
            parameterObject.setFeeTypeAndAmount(bookingAmountDetailTable);
            parameterObject.setSourceDocumentUUid(sourceDocumentDetailUuid);
            log.info("#Begin recover_receivable_loan_asset To [One] " +
                    "AssetSetUUID:[" + assetSet.getAssetUuid() + "]");
            if (ledgerBookV2Handler.checkLegderBookVersion(book)) {
                log.info("#begin to recover_receivable_loan_asset by " +
                        "AccountTemplate " +
                        "assetSetUUID:[" + assetSet.getAssetUuid() + "] Book " +
                        ":" + book);
                ledgerBookV2Handler.recover_receivable_loan_asset(parameterObject);
                log.info("#end recover_receivable_loan_asset by " +
                        "AccountTemplate " +
                        "assetSetUUID:[" + assetSet.getAssetUuid() + "]");
            }
            if (ledgerBookV2Handler.checkLedgerBookVersionV1(book)) {
                ledgerBookHandler.recover_receivable_loan_asset(parameterObject);
                log.info("#end recover_receivable_loan_asset To [One] " +
                        "AssetSetUUID:[" + assetSet.getAssetUuid() + "]");
            }
        } else if (assetRecoverType == AssetRecoverType.GUARANTEE_ASSET) {
            log.info("#Begin recover_receivable_loan_asset To [Two] " +
                    "AssetSetUUID:[" + assetSet.getAssetUuid() + "]");
            if (ledgerBookV2Handler.checkLegderBookVersion(book)) {
                log.info("#begin to recover_receivable_guranttee by " +
                        "AccountTemplate " +
                        "AssetSetUUID:[" + assetSet.getAssetUuid() + "] " +
                        "book:" + book);
                ledgerBookV2Handler.recover_receivable_guranttee(parameterObject);
                log.info("#end to recover_receivable_guranttee by " +
                        "AccountTemplate " +
                        "AssetSetUUID:[" + assetSet.getAssetUuid() + "]");
            }
            log.info("#end recover_receivable_loan_asset To [Two] " +
                    "AssetSetUUID:[" + assetSet.getAssetUuid() + "]");
            if (ledgerBookV2Handler.checkLedgerBookVersionV1(book)) {
                ledgerBookHandler.recover_receivable_guranttee(parameterObject);
            }
        } else if (assetRecoverType == AssetRecoverType.REPURCHASE_ASSET) {
            log.info("#Begin repurchase_order_write_off To [third] ");
            if (ledgerBookV2Handler.checkLegderBookVersion(book)) {
                log.info("#begin to repurchase_order_write_off, " +
                        "sourceDocument[" + context.getRepurchaseDoc()
                        .getRepurchaseDocUuid() + "]. by AccountTemplate book " +
                        ":" + book);
                ledgerBookV2Handler.repurchase_order_write_off(context.getContract().getId(),
                        sourceDocumentDetailUuid, journalVoucherUuid, businessVoucherUuid,
                        depositeAccountInfo, bookingAmountDetailTable, bookingAmount,
                        context.getActualRecycleTime(), context.getCashFlowIdentity());
                log.info("#end to repurchase_order_write_off, " +
                        "sourceDocument[" + context.getRepurchaseDoc()
                        .getRepurchaseDocUuid() + "]. by AccountTemplate ");
            }
            if (ledgerBookV2Handler.checkLedgerBookVersionV1(book)) {
                ledgerBookHandler.repurchase_order_write_off(contractUuid, sourceDocumentDetailUuid,
                        journalVoucherUuid, businessVoucherUuid, depositeAccountInfo,
                        bookingAmountDetailTable, bookingAmount, context.getActualRecycleTime(),
                        context.getCashFlowIdentity());
                log.info("#Begin repurchase_order_write_off To [third] ");
            }
        } else if (assetRecoverType == AssetRecoverType.CLEARING_DEDUCT) {
            LedgerTradeParty ledgerTradeParty = context.getFinancialContractAccountForLedgerBook().getDeopsite_account_owner_party();
            log.info("#begin to clearingVoucherWriteOff [four] " +
                    "DeductApplicationUuid:[" + context.getDeductApplication()
                    .getDeductApplicationUuid() + "]");
            if (ledgerBookV2Handler.checkLedgerbookVersionByledgerBookNo(context.getLedgerBookNo())) {
                log.info("#begin to clearingVoucherWriteOff " +
                        "[AccountTemplate] DeductApplicationUuid:[" + context
                        .getDeductApplication().getBatchDeductApplicationUuid
                                () + "]");
                ledgerBookClearingVoucherV2Handler.clearingVoucherWriteOff(context.getLedgerBookNo(),
                        context.getDeductApplication().getDeductApplicationUuid(), ledgerTradeParty,
                        journalVoucherUuid, businessVoucherUuid, sourceDocumentUuid, depositeAccountInfo,
                        context.getCashFlow());
                log.info("#end to clearingVoucherWriteOff " +
                        "[AccountTemplate] ");
            }
            if (ledgerBookV2Handler.checkLedgerbookVersionByledgerBookNoV1(context.getLedgerBookNo())) {
                ledgerBookClearingVoucherHandler.clearingVoucherWriteOff(context.getLedgerBookNo(),
                        context.getDeductApplication().getDeductApplicationUuid(), ledgerTradeParty,
                        journalVoucherUuid, businessVoucherUuid, sourceDocumentUuid, depositeAccountInfo,
                        context.getCashFlow());
                log.info("#end to clearingVoucherWriteOff [four] ");
            }
        }
    }
}