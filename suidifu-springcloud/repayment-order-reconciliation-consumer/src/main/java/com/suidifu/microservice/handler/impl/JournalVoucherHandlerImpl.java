package com.suidifu.microservice.handler.impl;

import com.suidifu.microservice.handler.JournalVoucherHandler;
import com.suidifu.microservice.model.ReconciliationRepaymentContext;
import com.zufangbao.sun.entity.account.DepositeAccountInfo;
import com.zufangbao.sun.entity.contract.Contract;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.ledgerbook.LedgerBook;
import com.zufangbao.sun.ledgerbook.LedgerBookCarryOverContextWithAsset;
import com.zufangbao.sun.ledgerbook.LedgerBookHandler;
import com.zufangbao.sun.ledgerbookv2.handler.LedgerBookV2Handler;
import com.zufangbao.sun.service.ContractService;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.service.LedgerBookService;
import com.zufangbao.sun.utils.StringUtils;
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
    private LedgerBookService ledgerBookService;
    @Resource
    private FinancialContractService financialContractService;
    @Resource
    private ContractService contractService;

    @Override
    public void recoverAssetByReconciliationContextRepaymentOrder(ReconciliationRepaymentContext context) {
        AssetSet assetSet = context.getAssetSet();
        AssetRecoverType assetRecoverType = context.getRecoverType();
        BigDecimal bookingAmount = context.getBookingAmount();
        String journalVoucherUuid = context.getIssuedJournalVoucher().getUuid();
        String businessVoucherUuid = context.getIssuedJournalVoucher().getBusinessVoucherUuid();
        String sourceDocumentDetailUuid = "";
        String sourceDocumentUuid = context.getRepaymentOrder().getOrderUuid();
        Map<String, BigDecimal> bookingAmountDetailTable = context.getBookingDetailAmount();
        DepositeAccountInfo depositeAccountInfo = context.getRemittanceAccountForLedgerBook();
        String cashFlowIdentity = context.getCashFlowIdentity();
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
                    true,
                    context.getActualRecycleTime(),
                    cashFlowIdentity);
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
            log.info("#begin to recover_receivable_asset," +
                    "asset_uuid[{}]. [Seven]", assetSet.getAssetUuid());
            if (ledgerBookV2Handler.checkLegderBookVersion(book)) {
                log.info("#begin to recover_receivable_asset," +
                                "asset_uuid[{}]. [AccountTemplate] book ",
                        assetSet.getAssetUuid(),
                        book);
                ledgerBookV2Handler.recover_receivable_loan_asset(parameterObject);
                log.info("#end to recover_receivable_asset, " +
                        "asset_uuid[{}]. [AccountTemplate]", assetSet.getAssetUuid());
            }

            if (ledgerBookV2Handler.checkLedgerBookVersionV1(book)) {
                ledgerBookHandler.recover_receivable_loan_asset(parameterObject);
                log.info("#begin to recover_receivable_asset, " +
                        "asset_uuid[{}].", assetSet.getAssetUuid());
            }
        } else if (assetRecoverType == AssetRecoverType.REPURCHASE_ASSET) {
            log.info("#begin to repurchase_order_write_off, " +
                            "sourceDocument[{}].[Eight]",
                    context.getRepurchaseDoc().getRepurchaseDocUuid());
            if (ledgerBookV2Handler.checkLegderBookVersion(book)) {
                log.info("#begin to repurchase_order_write_off, " +
                                "sourceDocument[{}].[Eight] [AccountTemplate] book{}",
                        context.getRepurchaseDoc().getRepurchaseDocUuid(),
                        book);
                ledgerBookV2Handler.repurchase_order_write_off(
                        context.getContract().getId(),
                        sourceDocumentDetailUuid,
                        journalVoucherUuid,
                        businessVoucherUuid,
                        depositeAccountInfo,
                        bookingAmountDetailTable,
                        bookingAmount,
                        context.getActualRecycleTime(),
                        cashFlowIdentity);
                log.info("#end to repurchase_order_write_off, " +
                                "sourceDocument[{}].[Eight] [AccountTemplate] book{}",
                        context.getRepurchaseDoc().getRepurchaseDocUuid(),
                        book);
            }
            if (ledgerBookV2Handler.checkLedgerBookVersionV1(book)) {
                ledgerBookHandler.repurchase_order_write_off(contractUuid,
                        sourceDocumentDetailUuid,
                        journalVoucherUuid,
                        businessVoucherUuid,
                        depositeAccountInfo,
                        bookingAmountDetailTable,
                        bookingAmount,
                        context.getActualRecycleTime(),
                        cashFlowIdentity);
                log.info("#end to repurchase_order_write_off, sourceDocument[{}]",
                        context.getRepurchaseDoc().getRepurchaseDocUuid());
            }
        } else if (assetRecoverType.isReceivableInAdvance()) {
            log.info("#begin to receivable_in_advance_order_write_off, " +
                            "contractUuid[{}],currentPeriod[{}].identificationMode]" +
                            ".repaymentOrderItemUuid[{}].[ Nine]",
                    context.getContract().getUuid(),
                    context.getCurrentPeriod(),
                    context.getRepaymentOrderItem().getOrderDetailUuid());
            if (ledgerBookV2Handler.checkLegderBookVersion(getLedgerBook(context.getLedgerBookNo()))) {
                log.info("#begin to receivable_in_advance_order_write_off," +
                        "[AccountTemplate] [book]{}", book);
                ledgerBookV2Handler.reveivable_in_advance_order_write_off(contractUuid,
                        context.getCurrentPeriod(),
                        journalVoucherUuid,
                        depositeAccountInfo,
                        bookingAmount,
                        bookingAmountDetailTable,
                        true);
                log.info("#begin to receivable_in_advance_order_write_off," +
                        "[AccountTemplate] [book]{}", book);
            }
            if (ledgerBookV2Handler.checkLedgerBookVersionV1(getLedgerBook(context.getLedgerBookNo()))) {
                ledgerBookHandler.reveivable_in_advance_order_write_off(contractUuid,
                        context.getCurrentPeriod(),
                        journalVoucherUuid,
                        depositeAccountInfo,
                        bookingAmount,
                        bookingAmountDetailTable,
                        true);
                log.info("#end to receivable_in_advance_order_write_off,[Nine]");
            }
        }
    }

    private LedgerBook getLedgerBook(String ledgerBookNo) {
        if (StringUtils.isEmpty(ledgerBookNo)) {
            return null;
        }
        LedgerBook book = ledgerBookService.getBookByBookNo(ledgerBookNo);
        if (null == book) {
            return null;
        }
        return book;
    }
}