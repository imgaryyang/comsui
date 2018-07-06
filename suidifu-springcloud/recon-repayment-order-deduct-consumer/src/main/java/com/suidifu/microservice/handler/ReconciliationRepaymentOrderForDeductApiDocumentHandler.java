package com.suidifu.microservice.handler;

import com.suidifu.giotto.exception.GiottoException;
import com.suidifu.microservice.entity.JournalVoucher;
import com.suidifu.microservice.entity.SourceDocument;
import com.suidifu.microservice.entity.SourceDocumentDetail;
import com.suidifu.microservice.model.ReconciliationRepaymentContext;
import com.suidifu.microservice.service.JournalVoucherService;
import com.suidifu.microservice.service.SourceDocumentDetailService;
import com.suidifu.microservice.service.SourceDocumentService;
import com.suidifu.microservice.service.ThirdPartyPaymentVoucherService;
import com.suidifu.microservice.service.ThirdPartyVoucherCommandLogService;
import com.suidifu.owlman.microservice.enumation.CounterAccountType;
import com.suidifu.owlman.microservice.enumation.JournalVoucherStatus;
import com.suidifu.owlman.microservice.enumation.ReconciliationType;
import com.suidifu.owlman.microservice.enumation.SourceDocumentDetailStatus;
import com.suidifu.owlman.microservice.exception.AlreadyProcessedException;
import com.suidifu.owlman.microservice.exception.ReconciliationException;
import com.suidifu.owlman.microservice.spec.ReconciliationRepaymentOrderParameterNameSpace;
import com.suidifu.owlman.microservice.spec.ReconciliationRepaymentOrderParameterNameSpace.ReconciliationRepaymentForDeductApi;
import com.zufangbao.sun.api.model.deduct.RecordStatus;
import com.zufangbao.sun.entity.account.AccountSide;
import com.zufangbao.sun.entity.account.DepositeAccountInfo;
import com.zufangbao.sun.entity.customer.Customer;
import com.zufangbao.sun.entity.log.SystemDeductLog;
import com.zufangbao.sun.entity.order.Order;
import com.zufangbao.sun.entity.repayment.order.RepaymentWayGroupType;
import com.zufangbao.sun.ledgerbook.BankAccountCache;
import com.zufangbao.sun.ledgerbook.LedgerBookHandler;
import com.zufangbao.sun.ledgerbook.LedgerItemService;
import com.zufangbao.sun.ledgerbook.stat.LedgerBookStatHandler;
import com.zufangbao.sun.service.LedgerBookService;
import com.zufangbao.sun.service.OrderService;
import com.zufangbao.sun.service.SystemDeductLogService;
import com.zufangbao.sun.utils.AmountUtils;
import com.zufangbao.sun.yunxin.api.deduct.service.DeductApplicationDetailService;
import com.zufangbao.sun.yunxin.api.deduct.service.DeductPlanService;
import com.zufangbao.sun.yunxin.entity.AssetRecoverType;
import com.zufangbao.sun.yunxin.entity.AssetSet;
import com.zufangbao.sun.yunxin.entity.OrderType;
import com.zufangbao.sun.yunxin.entity.api.deduct.DeductApplication;
import com.zufangbao.sun.yunxin.entity.api.deduct.SourceType;
import com.zufangbao.sun.yunxin.entity.model.thirdPartVoucher.VoucherLogIssueStatus;
import com.zufangbao.sun.yunxin.enums.voucher.JournalVoucherType;
import com.zufangbao.sun.yunxin.service.RepaymentPlanService;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("reconciliationRepaymentOrderForDeductApiDocumentHandler")
public class ReconciliationRepaymentOrderForDeductApiDocumentHandler extends ReconciliationRepaymentOrderForSourceDocument {
	@Autowired
	private RepaymentPlanService repaymentPlanService;

	@Autowired
	private LedgerBookVirtualAccountHandler ledgerBookVirtualAccountHandler;

	@Autowired
	private JournalVoucherService journalVoucherService;
	@Autowired
	private SourceDocumentDetailService sourceDocumentDetailService;
	@Autowired
	private SourceDocumentService sourceDocumentService;
	@Autowired
	private SystemDeductLogService systemDeductLogService;
	@Autowired
	private BankAccountCache bankAccountCache;
	@Autowired
	private ThirdPartyVoucherCommandLogService thirdPartyVoucherCommandLogService;
	@Autowired
	private OrderService orderService;
	@Autowired
	private DeductPlanService deductPlanService;

	@Autowired
	private LedgerBookHandler ledgerBookHandler;
	@Autowired
	private LedgerItemService ledgerItemService;
	@Autowired
	private LedgerBookService ledgerBookService;
	@Autowired
	private LedgerBookStatHandler ledgerBookStatHandler;
	@Autowired
	private DeductApplicationDetailService deductApplicationDetailService;
	@Autowired
	private ThirdPartyPaymentVoucherService thirdPartyPaymentVoucherService;

	@Override
	public ReconciliationRepaymentContext preAccountReconciliation(
		Map<String, Object> inputParams) throws AlreadyProcessedException {
		ReconciliationRepaymentContext context = super.preAccountReconciliation(inputParams);//repaymentOrderItem,repaymentOrder,booking_amount,contract,RepaymentOrderCharge,ActualRecycleTime,RecoverType
		DeductApplication deductApplication = (DeductApplication)inputParams.get(ReconciliationRepaymentForDeductApi.PARAMS_DEDUCT_APPLICATION);
		SourceDocument deductSourceDocument = (SourceDocument)inputParams.get(ReconciliationRepaymentOrderParameterNameSpace.ReconciliationRepaymentParams.PARAMS_REPAYMENT_SOURCE_DOCUMENT);
		String sourceDocumentDetailUuid = (String)inputParams.get(ReconciliationRepaymentOrderParameterNameSpace.ReconciliationRepaymentParams.PARAMS_REPAYMENT_SOURCE_DOCUMENT_DETAIL_UUID);

		SourceDocumentDetail sourceDocumentDetail = sourceDocumentDetailService.getSourceDocumentDetail(sourceDocumentDetailUuid);
		AssetSet assetSet = repaymentPlanService.getRepaymentPlanByRepaymentCode(sourceDocumentDetail.getRepaymentPlanNo());
		super.extractReconciliationContextFromAssetSet(context,assetSet);
		JournalVoucher journalVoucher = journalVoucherService.getInforceJournalVoucher(sourceDocumentDetailUuid);
		context.setSourceDocument(deductSourceDocument);
		context.setSourceDocumentDetail(sourceDocumentDetail);
		context.setIssuedJournalVoucher(journalVoucher);
		context.setRecoverType(AssetRecoverType.LOAN_ASSET);
		context.setJournalVoucherType(JournalVoucherType.THIRD_PARTY_DEDUCT_VOUCHER);
		context.resovleJournalVoucher(AccountSide.DEBIT, CounterAccountType.BankAccount,assetSet.getAssetUuid());
		context.setReconciliationType(ReconciliationType.RECONCILIATION_THIRDPARTY_DEDUCT);

		String merIdClearingNo = deductPlanService.getMerIdClearingNoKey(deductApplication
			.getDeductApplicationUuid());
		DepositeAccountInfo unionDepositAccount = bankAccountCache.extractThirdPartyPaymentChannelAccountFrom(context.getFinancialContract(), merIdClearingNo);
		context.setFinancialContractAccountForLedgerBook(unionDepositAccount);
		context.setBookingAmount(sourceDocumentDetail.getAmount());
		// 设定还款时间
		if(context.isRepaymentPlanTimeInvalid(context.getPlanRepaymentTimeLock())){
			Date paymentSusTime = deductPlanService.getMaxPaymentSucTime(deductApplication.getDeductApplicationUuid());
			context.setActualRecycleTime(paymentSusTime);
		}
		context.setDeductApplication(deductApplication);
		super.fillVoucherNo(context, deductSourceDocument);
		return context;

	}

	@Override
	public void extract_related_balnace_from_ledger_book(ReconciliationRepaymentContext context){
		String ledgerBookNo = context.getFinancialContract().getLedgerBookNo();
		Customer borrower_customer = context.getBorrower_customer();

		AssetSet assetSet = context.getAssetSet();
		if(assetSet!=null){
			BigDecimal receivalbeAmount = ledgerBookStatHandler.get_receivable_amount(ledgerBookNo, assetSet.getAssetUuid(), borrower_customer.getCustomerUuid());
			BigDecimal unearnedAmount=ledgerBookStatHandler.get_unearned_amount(ledgerBookNo, assetSet.getAssetUuid());
			context.setReceivalbeAmount(receivalbeAmount);
			context.setUnearnedAmount(unearnedAmount);
		}

	}

	@Override
	public  void refreshVirtualAccount(ReconciliationRepaymentContext context) {
		//no virtualAccount;
	}

	public void ledger_book_reconciliation(ReconciliationRepaymentContext context)
	{
		super.ledger_book_reconciliation(context);

	}

	public  boolean accountReconciliation(Map<String,Object> params)
	{
		return super.accountReconciliation(params);
	}

	@Override
	public void relatedDocumentsProcessing(ReconciliationRepaymentContext context){
		super.process_if_prepayment_plan(context);
		DeductApplication deductApplication = context.getDeductApplication();
		SystemDeductLog systemDeductLog = systemDeductLogService.getUniqueSystemDeductLog(deductApplication.getRequestNo());
		Order order = null;
		if(systemDeductLog != null) {
			order = orderService.getOrderById(systemDeductLog.getOrderId(), OrderType.NORMAL);
		}
		if(order==null){
			order = orderService.createAndSaveOrder(context.getBookingAmount(), "", context.getAssetSet(), context.getBorrower_customer(), context.getFinancialContract(), context.getActualRecycleTime());
		}

		context.setOrder(order);
//		if (is_unearned_asset(context.getUnearnedAmount(), context.getReceivalbeAmount())) {
//			LedgerBook book = ledgerBookService.getBookByBookNo(context.getLedgerBookNo());
//			ledgerBookHandler.amortize_loan_asset_to_receivable(book, context.getAssetSet());
//		}
	}

	@Override
	public void issueJournalVoucher(ReconciliationRepaymentContext context)

	{
		JournalVoucher journalVoucher = context.getIssuedJournalVoucher();
		journalVoucher.setTradeTime(context.getActualRecycleTime());
		journalVoucher.setStatus(JournalVoucherStatus.VOUCHER_ISSUED);
		journalVoucher.setIssuedTime(new Date());
		journalVoucher.setRelatedBillContractNoLv4(context.getOrder().getOrderNo());
		journalVoucher.setLastModifiedTime(new Date());
		journalVoucherService.update(journalVoucher);
	}

	@Override
	public  void refreshAsset(ReconciliationRepaymentContext context) throws GiottoException {
		super.refreshAsset(context);
	}

	@Override
	public void validateReconciliationContext(ReconciliationRepaymentContext context) throws AlreadyProcessedException {
		SourceDocumentDetail sourceDocumentDetail = context.getSourceDocumentDetail();
		if(sourceDocumentDetail.getStatus()!=SourceDocumentDetailStatus.UNSUCCESS){
			throw new AlreadyProcessedException();
		}
		JournalVoucher journalVoucher = context.getIssuedJournalVoucher();
		if(journalVoucher==null){
			throw new ReconciliationException("journalVoucher is null");
		}
		if(journalVoucher.getStatus()!=JournalVoucherStatus.VOUCHER_CREATED){
			throw new ReconciliationException("journalVoucherStatus expected[created],but was ["+journalVoucher.getStatus()+"]");
		}
		if(AmountUtils.notEquals(sourceDocumentDetail.getAmount(), journalVoucher.getBookingAmount())){
			throw new ReconciliationException("sourceDocumentDetailAmount["+sourceDocumentDetail.getAmount()+"] is not equal to journalVoucherBookingAmount["+journalVoucher.getBookingAmount()+"]");
		}
		DeductApplication dedcutApplication = context.getDeductApplication();
		if(dedcutApplication.getRecordStatus()==RecordStatus.WRITE_OFF){
			throw new ReconciliationException("dedcutApplication recordStatus is already wirte off.");
		}
	}



	@Override
	public  void postAccountReconciliation(ReconciliationRepaymentContext context) throws GiottoException {
		AssetSet updatedAssetSet = repaymentPlanService.getUniqueRepaymentPlanByUuid(context.getAssetSet().getAssetUuid());
		if(updatedAssetSet.isClearAssetSet()==false) {
			//if uncleared then open deduct lock
			updatedAssetSet.setActiveDeductApplicationUuid(AssetSet.EMPTY_UUID);
			repaymentPlanService.update(updatedAssetSet);
		}
		//商户上传凭证置为以核销
		DeductApplication deductApplication = context.getDeductApplication();

		if(context.getRepaymentOrder().getFirstRepaymentWayGroup() == RepaymentWayGroupType.BUSINESS_DEDUCT_REPAYMENT_ORDER_TYPE && deductApplication.getSourceType() == SourceType.REPAYMENTORDER){

			thirdPartyPaymentVoucherService.updateVoucherLogIssueStatus(VoucherLogIssueStatus.HAS_ISSUED, deductApplication.getDeductId());

		}else if(deductApplication.getSourceType() == SourceType.APPUPLOAD
			||deductApplication.getSourceType()==SourceType.REPAYMENTORDER){
			thirdPartyVoucherCommandLogService.updateTransactionCommandVoucherLogIssueStatus(VoucherLogIssueStatus.HAS_ISSUED, deductApplication.getDeductId());
		}

		super.postAccountReconciliation(context);

	}

}


