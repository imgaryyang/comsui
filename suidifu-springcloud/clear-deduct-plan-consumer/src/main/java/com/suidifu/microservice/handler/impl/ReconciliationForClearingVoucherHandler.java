package com.suidifu.microservice.handler.impl;

import com.suidifu.microservice.entity.JournalVoucher;
import com.suidifu.microservice.entity.ReconciliationContext;
import com.suidifu.microservice.entity.SourceDocument;
import com.suidifu.microservice.entity.SourceDocumentDetail;
import com.suidifu.microservice.handler.TotalReceivableBillsHandler;
import com.suidifu.microservice.service.JournalVoucherService;
import com.suidifu.microservice.service.SourceDocumentDetailService;
import com.suidifu.microservice.service.SourceDocumentService;
import com.suidifu.owlman.microservice.enumation.JournalVoucherCheckingLevel;
import com.suidifu.owlman.microservice.enumation.JournalVoucherStatus;
import com.suidifu.owlman.microservice.enumation.JournalVoucherType;
import com.suidifu.owlman.microservice.enumation.ReconciliationType;
import com.suidifu.owlman.microservice.exception.AlreadyProcessedException;
import com.suidifu.owlman.microservice.exception.DeductApplicationWriteOffException;
import com.suidifu.owlman.microservice.exception.ReconciliationException;
import com.suidifu.owlman.microservice.spec.ReconciliationParameterNameSpace.ReconciliationForClearingVoucher;
import com.zufangbao.sun.api.model.deduct.RecordStatus;
import com.zufangbao.sun.entity.account.DepositeAccountInfo;
import com.zufangbao.sun.entity.contract.Contract;
import com.zufangbao.sun.entity.directbank.business.CashFlow;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.ledgerbook.BankAccountCache;
import com.zufangbao.sun.ledgerbook.stat.LedgerBookStatHandler;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.yunxin.api.deduct.service.DeductApplicationDetailService;
import com.zufangbao.sun.yunxin.api.deduct.service.DeductApplicationService;
import com.zufangbao.sun.yunxin.api.deduct.service.DeductPlanService;
import com.zufangbao.sun.yunxin.entity.AssetRecoverType;
import com.zufangbao.sun.yunxin.entity.AssetSet;
import com.zufangbao.sun.yunxin.entity.api.deduct.DeductApplication;
import com.zufangbao.sun.yunxin.entity.api.deduct.DeductApplicationDetail;
import com.zufangbao.sun.yunxin.entity.api.deduct.DeductPlan;
import com.zufangbao.sun.yunxin.entity.audit.BeneficiaryAuditResult;
import com.zufangbao.sun.yunxin.entity.audit.ClearingStatus;
import com.zufangbao.sun.yunxin.entity.audit.ClearingVoucher;
import com.zufangbao.sun.yunxin.entity.audit.TotalReceivableBills;
import com.zufangbao.sun.yunxin.service.RepaymentPlanService;
import com.zufangbao.sun.yunxin.service.account.ClearingExecLogService;
import com.zufangbao.sun.yunxin.service.audit.BeneficiaryAuditResultService;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("reconciliationForClearingVoucherHandler")
public class ReconciliationForClearingVoucherHandler extends ReconciliationBase {

	@Autowired
	private FinancialContractService financialContractService;

	@Autowired
	private DeductApplicationService deductApplicationService;

	@Autowired
	private JournalVoucherService journalVoucherService;

	@Autowired
	private DeductApplicationDetailService deductApplicationDetailService;

	@Autowired
	private DeductPlanService deductPlanService;

	@Autowired
	private SourceDocumentDetailService  sourceDocumentDetailService;

	@Autowired
	private RepaymentPlanService repaymentPlanService;

	@Autowired
	private BankAccountCache bankAccountCache;

	@Autowired
	private ClearingExecLogService clearingExecLogService;

	@Autowired
	private LedgerBookStatHandler ledgerBookStatHandler;

	@Autowired
	private SourceDocumentService sourceDocumentService;
	
	 @Autowired
	  private BeneficiaryAuditResultService beneficiaryAuditResultService;

	@Autowired
	private TotalReceivableBillsHandler totalReceivableBillsHandler;

	private Log logger = LogFactory.getLog(getClass());

	@Override
	public boolean accountReconciliation(Map<String, Object> params) {
		return super.accountReconciliation(params);
	}

	@Override
	public ReconciliationContext preAccountReconciliation(Map<String, Object> inputParams)
			throws AlreadyProcessedException {
		ReconciliationContext context = new ReconciliationContext();
		DeductPlan deductPlan = (DeductPlan) inputParams.get(ReconciliationForClearingVoucher.PARAMS_DEDUCT_PLAN);
		DeductApplication deductApplication =  deductApplicationService.getDeductApplicationByDeductApplicationUuid(deductPlan.getDeductApplicationUuid());

		CashFlow cashFlow = (CashFlow) inputParams.get(ReconciliationForClearingVoucher.PARAMS_CASH_FLOW);
		SourceDocument sourceDocument = (SourceDocument) inputParams
				.get(ReconciliationForClearingVoucher.PARAMS_SOURCE_DOCUMENT);
		FinancialContract financialContract = financialContractService
				.getFinancialContractBy(deductPlan.getFinancialContractUuid());
		if (financialContract == null) {
			logger.info("financialContract is null ");
			throw new ReconciliationException("financialContract is null ");
		}

		context.setDeductPlan(deductPlan);
		context.setDeductApplication(deductApplication);
		context.setFinancialContract(financialContract);
		context.setLedgerBookNo(financialContract.getLedgerBookNo());
		context.setCashFlow(cashFlow);
		context.setSourceDocument(sourceDocument);
		context.setAppendix(inputParams);
		context.setBookingAmount(deductPlan.getActualTotalAmount());
		context.setJournalVoucherType(JournalVoucherType.TRANSFER_BILL_BY_CLEARING_DEDUCT_PLAN);
		context.setRecoverType(AssetRecoverType.CLEARING_DEDUCT);
		context.setReconciliationType(ReconciliationType.RECONCILIATION_Clearing_Voucher);
		context.setContract(new Contract());
		DepositeAccountInfo counterPartyAccount = bankAccountCache.extractFirstBankAccountFrom(context.getFinancialContract());
		context.setFinancialContractAccountForLedgerBook(counterPartyAccount);

		return context;
	}

	@Override
	public void validateReconciliationContext(ReconciliationContext context) throws AlreadyProcessedException {
		DeductPlan deductPlan = context.getDeductPlan();
		if(ClearingStatus.DONE.equals(deductPlan.getClearingStatus())){
			logger.info("deductPlan is already clearing");
			throw new ReconciliationException("deductPlan is already clearing");
		}
		List<DeductApplicationDetail> deductPlanDetailList = deductApplicationDetailService
				.getDeductPlanDetailListBy(deductPlan.getDeductApplicationUuid());
		if (CollectionUtils.isEmpty(deductPlanDetailList)) {
			logger.info("deductPlanDetailUuidList is empty ");
			throw new ReconciliationException("deductPlanDetailUuidList is empty ");
		}

		context.getAppendix().put(ReconciliationForClearingVoucher.PARAMS_DEDUCT_DETTAILS, deductPlanDetailList);
	}

	@Override
	public void relatedDocumentsProcessing(ReconciliationContext context) {

	}

	@Override
	public void issueJournalVoucher(ReconciliationContext context) throws AlreadyProcessedException {
		TotalReceivableBills totalReceivableBills = (TotalReceivableBills) context.getAppendix()
				.get(ReconciliationForClearingVoucher.PARAMS_TOTAL_RECEIVABLE_BILLS);
		JournalVoucher journalVoucher = new JournalVoucher();
		CashFlow cashFlow = context.getCashFlow();
		FinancialContract financialContract = context.getFinancialContract();
		SourceDocument sourceDocument = context.getSourceDocument();
		journalVoucher.createFromCashFlow(cashFlow, financialContract.getCompany());
		journalVoucher.copyFromSourceDocument(sourceDocument);
		journalVoucher.fill_voucher_and_booking_amount(context.getDeductPlan().getDeductPlanUuid(), "", "",
				context.getBookingAmount(), JournalVoucherStatus.VOUCHER_ISSUED,
				JournalVoucherCheckingLevel.AUTO_BOOKING, JournalVoucherType.TRANSFER_BILL_BY_CLEARING_DEDUCT_PLAN);
		journalVoucher.fillBillContractInfo(financialContract.getFinancialContractUuid(), "", "",
				financialContract.getContractName(), "", "", "");
		journalVoucherService.save(journalVoucher);
		logger.info("生成journalvoucher");
		context.setIssuedJournalVoucher(journalVoucher);
		// TODO 需要修改
	}

	@Override
	public void ledger_book_reconciliation(ReconciliationContext context) {
		super.ledger_book_reconciliation(context);
	}

	@Override
	public void refreshVirtualAccount(ReconciliationContext context) {
		// to do nothing
	}

	@Override
	public void refreshAsset(ReconciliationContext context) {
		// to do nothing
	}

	   @Override
	    public void postAccountReconciliation(ReconciliationContext context) {
	        // TODO update deductPlan
	        
	        DeductPlan deductPlan = (DeductPlan) context.getAppendix().get(ReconciliationForClearingVoucher.PARAMS_DEDUCT_PLAN);
	        CashFlow cash = context.getCashFlow();
	        deductPlan.setClearingCashFlowUuid(cash.getUuid());
	        deductPlan.setClearingStatus(ClearingStatus.DONE);
	        deductPlan.setClearingTime(cash.getTransactionTime());
	        deductPlanService.update(deductPlan);
	        BeneficiaryAuditResult beneficiaryAuditResult = beneficiaryAuditResultService.getBeneficiaryAuditResultBy(deductPlan.getTradeUuid());
	        if(beneficiaryAuditResult==null){
	            logger.info("deductPlanUuid:["+deductPlan.getDeductPlanUuid()+"] beneficiaryAuditResult is null");
	        }else{
	            beneficiaryAuditResult.setDeductApplicationUuid(deductPlan.getDeductApplicationUuid());
	            beneficiaryAuditResult.setClearingStatus(ClearingStatus.DONE);
	            beneficiaryAuditResult.setLastModifiedTime(new Date());
	            beneficiaryAuditResultService.save(beneficiaryAuditResult);
	        }
	        try {
	            createClearingExecLogForDeductPlan(context);
	        } catch (Exception e) {
	            logger .info("deductPlanUuid:["+deductPlan.getDeductPlanUuid()+"]创建clearingExecLog 失败");
	            e.printStackTrace();
	        }
	    }

	private void createClearingExecLogForDeductPlan(ReconciliationContext context){

		if(!financialContractService.isSpecialAccountConfigured(context.getFinancialContract().getFinancialContractUuid())) {
			logger.info("目前不是专户配置，不生成ClearingExecLog");
			return;
		}
		// 根据扣款申请订单uuid查出扣款申请订单
		DeductApplication deductApplication = deductApplicationService.getDeductApplicationByDeductApplicationUuid(context.getDeductPlan().getDeductApplicationUuid());
		if(deductApplication.getRecordStatus() != RecordStatus.WRITE_OFF){
			//异常
			throw new DeductApplicationWriteOffException();
		}
		//根据总应收单查出清算凭证
		TotalReceivableBills totalReceivableBills = (TotalReceivableBills) context.getAppendix().get(ReconciliationForClearingVoucher.PARAMS_TOTAL_RECEIVABLE_BILLS);

		ClearingVoucher clearingVoucher = totalReceivableBillsHandler.queryClearingVoucherByTotalReceivableBills(totalReceivableBills, null);
		String clearingVoucherUuid = clearingVoucher.getUuid();
		//根据扣款申请订单UUid 查出原始凭证表对象
		SourceDocument sourceDocument = sourceDocumentService.getSourceDocumentByOutlierDocumentUuid(context.getDeductPlan().getDeductApplicationUuid(), SourceDocument.FIRSTOUTLIER_DEDUCTAPPLICATION);

		List<SourceDocumentDetail> detailList = new ArrayList<SourceDocumentDetail>();

		if(sourceDocument != null ){
			//根据原始凭证表UUID查出相对应的所有原始凭证明细表
			detailList = sourceDocumentDetailService.getValidDeductSourceDocumentDetailsBySourceDocumentUuid(sourceDocument.getSourceDocumentUuid());
		}

		for (SourceDocumentDetail sourceDocumentDetail : detailList) {
			//根据当前明细表 还款计划编号查出 资产对象  single_loan_contract_no
			AssetSet asset = repaymentPlanService
					.getRepaymentPlanByRepaymentCode(sourceDocumentDetail.getRepaymentPlanNo());
			//用当前原始凭证表UUid,原始凭证明细表UUID,资产UUid查出财务凭证表
			JournalVoucher journalVoucher = journalVoucherService
					.getJournalVoucherBySourceDocumentUuidAndType(sourceDocument.getSourceDocumentUuid(),
							sourceDocumentDetail.getUuid(), asset.getAssetUuid());

			if (journalVoucher != null) {
				Map<String, BigDecimal> detailAmountMap = ledgerBookStatHandler
						.get_jv_asset_detail_amount_of_banksaving_and_independent_accounts(
								context.getLedgerBookNo(), journalVoucher.getJournalVoucherUuid(),
								asset.getAssetUuid());

				clearingExecLogService
						.createClearingExecLog(asset, detailAmountMap, journalVoucher.getUuid(),
								journalVoucher.getJournalVoucherType().ordinal(),
								journalVoucher.getBookingAmount(), asset.getContract().getId(),
								clearingVoucherUuid);
			}
		}
	}
}
