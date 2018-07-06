package com.suidifu.microservice.handler.impl;

import com.suidifu.microservice.handler.JournalVoucherHandler;
import com.suidifu.microservice.handler.LedgerBookClearingVoucherHandler;
import com.suidifu.microservice.handler.LedgerBookClearingVoucherV2Handler;
import com.suidifu.microservice.model.ReconciliationContext;
import com.suidifu.microservice.model.ReconciliationRepaymentContext;
import com.zufangbao.sun.entity.account.DepositeAccountInfo;
import com.zufangbao.sun.entity.contract.Contract;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.ledgerbook.AlreadayCarryOverException;
import com.zufangbao.sun.ledgerbook.InsufficientBalanceException;
import com.zufangbao.sun.ledgerbook.InsufficientPenaltyException;
import com.zufangbao.sun.ledgerbook.InvalidCarryOverException;
import com.zufangbao.sun.ledgerbook.InvalidLedgerException;
import com.zufangbao.sun.ledgerbook.LackBusinessVoucherException;
import com.zufangbao.sun.ledgerbook.LedgerBook;
import com.zufangbao.sun.ledgerbook.LedgerBookCarryOverContextWithAsset;
import com.zufangbao.sun.ledgerbook.LedgerBookHandler;
import com.zufangbao.sun.ledgerbook.LedgerTradeParty;
import com.zufangbao.sun.ledgerbookv2.handler.LedgerBookV2Handler;
import com.zufangbao.sun.service.ContractService;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.service.LedgerBookService;
import com.zufangbao.sun.utils.StringUtils;
import com.zufangbao.sun.yunxin.entity.AssetRecoverType;
import com.zufangbao.sun.yunxin.entity.AssetSet;
import java.math.BigDecimal;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("journalVoucherHandler")
public class JournalVoucherHandlerImpl implements JournalVoucherHandler {

    @Autowired
    private LedgerBookHandler ledgerBookHandler;

    @Autowired
    private LedgerBookV2Handler ledgerBookV2Handler;

    @Autowired
    private LedgerBookService ledgerBookService;

    @Autowired
    private FinancialContractService financialContractService;

		@Autowired
		private LedgerBookClearingVoucherV2Handler ledgerBookClearingVoucherV2Handler;
		@Autowired
		private LedgerBookClearingVoucherHandler ledgerBookClearingVoucherHandler;


    @Autowired
    private ContractService contractService;


    private static final Log logger = LogFactory.getLog(JournalVoucherHandlerImpl.class);

	@Override
	public void recover_asset_by_reconciliation_context(ReconciliationContext context) throws LackBusinessVoucherException, AlreadayCarryOverException,
			InvalidLedgerException, InsufficientPenaltyException, InsufficientBalanceException, InvalidCarryOverException {

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
					ifRecoveryMoney, context.getActualRecycleTime(), context.getCashFlowIdentity());
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


		if(assetRecoverType==AssetRecoverType.LOAN_ASSET){
			parameterObject.setFeeTypeAndAmount(bookingAmountDetailTable);
			parameterObject.setSourceDocumentUUid(sourceDocumentDetailUuid);
			logger.info("#Begin recover_receivable_loan_asset To [One] " +
					"AssetSetUUID:["+assetSet.getAssetUuid()+"]");
			if (ledgerBookV2Handler.checkLegderBookVersion(book)){
				logger.info("#begin to recover_receivable_loan_asset by " +
						"AccountTemplate " +
						"assetSetUUID:["+assetSet.getAssetUuid()+"] Book " +
						":"+book);
				ledgerBookV2Handler.recover_receivable_loan_asset(parameterObject);
				logger.info("#end recover_receivable_loan_asset by " +
						"AccountTemplate " +
						"assetSetUUID:["+assetSet.getAssetUuid()+"]");
			}
			if (ledgerBookV2Handler.checkLedgerBookVersionV1(book)){
				ledgerBookHandler.recover_receivable_loan_asset(parameterObject);
				logger.info("#end recover_receivable_loan_asset To [One] " +
						"AssetSetUUID:["+assetSet.getAssetUuid()+"]");
			}
		} else if(assetRecoverType==AssetRecoverType.GUARANTEE_ASSET){
			logger.info("#Begin recover_receivable_loan_asset To [Two] " +
					"AssetSetUUID:["+assetSet.getAssetUuid()+"]");
			if (ledgerBookV2Handler.checkLegderBookVersion(book)){
				logger.info("#begin to recover_receivable_guranttee by " +
						"AccountTemplate " +
						"AssetSetUUID:["+assetSet.getAssetUuid()+"] " +
						"book:"+book);
				ledgerBookV2Handler.recover_receivable_guranttee(parameterObject);
				logger.info("#end to recover_receivable_guranttee by " +
						"AccountTemplate " +
						"AssetSetUUID:["+assetSet.getAssetUuid()+"]");
			}
			logger.info("#end recover_receivable_loan_asset To [Two] " +
					"AssetSetUUID:["+assetSet.getAssetUuid()+"]");
			if (ledgerBookV2Handler.checkLedgerBookVersionV1(book)){
				ledgerBookHandler.recover_receivable_guranttee(parameterObject);
			}
		}else if(assetRecoverType==AssetRecoverType.REPURCHASE_ASSET){
			logger.info("#Begin repurchase_order_write_off To [third] ");
			if (ledgerBookV2Handler.checkLegderBookVersion(book)){
				logger.info("#begin to repurchase_order_write_off, " +
						"sourceDocument["+context.getRepurchaseDoc()
						.getRepurchaseDocUuid()+"]. by AccountTemplate book " +
						":"+book);
				ledgerBookV2Handler.repurchase_order_write_off(context.getContract().getId(),
						sourceDocumentDetailUuid, journalVoucherUuid, businessVoucherUuid,
						depositeAccountInfo,bookingAmountDetailTable, bookingAmount,
						context.getActualRecycleTime(), context.getCashFlowIdentity());
				logger.info("#end to repurchase_order_write_off, " +
						"sourceDocument["+context.getRepurchaseDoc()
						.getRepurchaseDocUuid()+"]. by AccountTemplate ");
			}
			if (ledgerBookV2Handler.checkLedgerBookVersionV1(book)){
				ledgerBookHandler.repurchase_order_write_off(contractUuid, sourceDocumentDetailUuid,
						journalVoucherUuid, businessVoucherUuid,depositeAccountInfo,
						bookingAmountDetailTable, bookingAmount, context.getActualRecycleTime(),
						context.getCashFlowIdentity());
				logger.info("#Begin repurchase_order_write_off To [third] ");
			}
		}else if(assetRecoverType==AssetRecoverType.CLEARING_DEDUCT){
			LedgerTradeParty ledgerTradeParty=context.getFinancialContractAccountForLedgerBook().getDeopsite_account_owner_party();
			logger.info("#begin to clearing_voucher_write_off [four] " +
					"DeductApplicationUuid:["+context.getDeductApplication()
					.getDeductApplicationUuid()+"]");
			if (ledgerBookV2Handler.checkLedgerbookVersionByledgerBookNo(context.getLedgerBookNo())){
				logger.info("#begin to clearing_voucher_write_off " +
						"[AccountTemplate] DeductApplicationUuid:["+context
						.getDeductApplication().getBatchDeductApplicationUuid
								()+"]");
				ledgerBookClearingVoucherV2Handler.clearing_voucher_write_off(context.getLedgerBookNo(),
						context.getDeductApplication().getDeductApplicationUuid(), ledgerTradeParty,
						journalVoucherUuid, businessVoucherUuid, sourceDocumentUuid, depositeAccountInfo,
						context.getCashFlow());
				logger.info("#end to clearing_voucher_write_off " +
						"[AccountTemplate] ");
			}
			if (ledgerBookV2Handler.checkLedgerbookVersionByledgerBookNoV1(context.getLedgerBookNo())){
				ledgerBookClearingVoucherHandler.clearing_voucher_write_off(context.getLedgerBookNo(),
						context.getDeductApplication().getDeductApplicationUuid(), ledgerTradeParty,
						journalVoucherUuid, businessVoucherUuid, sourceDocumentUuid, depositeAccountInfo,
						context.getCashFlow());
				logger.info("#end to clearing_voucher_write_off [four] ");
			}
		}
	}

    @Override
    public void recover_asset_by_reconciliation_context_repayment_order(
            ReconciliationRepaymentContext context)
            throws LackBusinessVoucherException, AlreadayCarryOverException,
            InvalidLedgerException, InsufficientPenaltyException,
            InsufficientBalanceException, InvalidCarryOverException {

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
                    ifRecoveryMoney, context.getActualRecycleTime(), cashFlowIdentity);
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

		if(assetRecoverType==AssetRecoverType.LOAN_ASSET){
						parameterObject.setFeeTypeAndAmount(bookingAmountDetailTable);
						parameterObject.setSourceDocumentUUid(sourceDocumentDetailUuid);
			logger.info("#begin to recover_receivable_asset, " +
					"asset_uuid["+assetSet.getAssetUuid()+"]. [Seven]");
			if (ledgerBookV2Handler.checkLegderBookVersion(book)){
				logger.info("#begin to recover_receivable_asset, " +
						"asset_uuid["+assetSet.getAssetUuid()+"]. " +
						"[AccontTemplate] book "+book);
				ledgerBookV2Handler.recover_receivable_loan_asset(parameterObject);
				logger.info("#end to recover_receivable_asset, " +
						"asset_uuid["+assetSet.getAssetUuid()+"].[AccontTemplate]");
			}
			if (ledgerBookV2Handler.checkLedgerBookVersionV1(book)){
				ledgerBookHandler.recover_receivable_loan_asset(parameterObject);
				logger.info("#begin to recover_receivable_asset, " +
						"asset_uuid["+assetSet.getAssetUuid()+"].");
			}
		} else if(assetRecoverType==AssetRecoverType.REPURCHASE_ASSET){
			logger.info("#begin to repurchase_order_write_off, " +
					"sourceDocument["+context.getRepurchaseDoc()
					.getRepurchaseDocUuid()+"].[Eight]");
			if (ledgerBookV2Handler.checkLegderBookVersion(book)){
				logger.info("#begin to repurchase_order_write_off, " +
						"sourceDocument["+context.getRepurchaseDoc()
						.getRepurchaseDocUuid()+"].[Eight] [AccountTemplate] " +
						"book"+book);
				ledgerBookV2Handler.repurchase_order_write_off(context.getContract().getId(),sourceDocumentDetailUuid, journalVoucherUuid, businessVoucherUuid,depositeAccountInfo,bookingAmountDetailTable, bookingAmount, context.getActualRecycleTime(), cashFlowIdentity);
				logger.info("#end to repurchase_order_write_off, " +
						"sourceDocument["+context.getRepurchaseDoc()
						.getRepurchaseDocUuid()+"].[Eight] [AccountTemplate] " +
						"book"+book);
			}
			if (ledgerBookV2Handler.checkLedgerBookVersionV1(book)){
				ledgerBookHandler.repurchase_order_write_off(contractUuid, sourceDocumentDetailUuid, journalVoucherUuid, businessVoucherUuid,depositeAccountInfo,bookingAmountDetailTable, bookingAmount, context.getActualRecycleTime(), cashFlowIdentity);
				logger.info("#end to repurchase_order_write_off, " +
						"sourceDocument["+context.getRepurchaseDoc()
						.getRepurchaseDocUuid()+"]");
			}
		}
		else if(assetRecoverType.isReceivableInAdvance()){
			logger.info("#begin to reveivable_in_advance_order_write_off, " +
					"contractUuid["+context.getContract().getUuid()+"]," +
					"currentPeriod["+context.getCurrentPeriod()+"]" +
					".identificationMode"+"].repaymentOrderItemUuid["+context
					.getRepaymentOrderItem().getOrderDetailUuid()+"].[ Nine]");
			if (ledgerBookV2Handler.checkLegderBookVersion(getLedgerBook(context.getLedgerBookNo()))){
				logger.info("#begin to reveivable_in_advance_order_write_off," +
						"[AccountTemplate] [book]"+book);
				ledgerBookV2Handler.reveivable_in_advance_order_write_off(contractUuid, context.getCurrentPeriod(), journalVoucherUuid, depositeAccountInfo, bookingAmount,bookingAmountDetailTable, true);
				logger.info("#begin to reveivable_in_advance_order_write_off," +
						"[AccountTemplate] [book]"+book);
			}
			if (ledgerBookV2Handler.checkLedgerBookVersionV1(getLedgerBook(context.getLedgerBookNo()))){
				ledgerBookHandler.reveivable_in_advance_order_write_off(contractUuid, context.getCurrentPeriod(), journalVoucherUuid, depositeAccountInfo, bookingAmount,bookingAmountDetailTable, true);
				logger.info("#end to reveivable_in_advance_order_write_off,[Nine]");
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
