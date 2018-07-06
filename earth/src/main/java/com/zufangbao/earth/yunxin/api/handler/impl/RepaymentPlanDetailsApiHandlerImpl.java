package com.zufangbao.earth.yunxin.api.handler.impl;

import com.zufangbao.earth.yunxin.api.exception.ApiResponseCode;
import com.zufangbao.earth.yunxin.api.handler.RepaymentPlanDetailsApiHandler;
import com.zufangbao.earth.yunxin.api.model.PaymentRecordShowModel;
import com.zufangbao.earth.yunxin.api.model.query.RepaymentPlanDetailsQueryModel;
import com.zufangbao.earth.yunxin.api.model.query.RepaymentPlanDetailsResultModel;
import com.zufangbao.gluon.exception.ApiException;
import com.zufangbao.sun.api.model.deduct.DeductApplicationExecutionStatus;
import com.zufangbao.sun.entity.contract.Contract;
import com.zufangbao.sun.entity.directbank.business.CashFlow;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.entity.order.Order;
import com.zufangbao.sun.entity.repurchase.RepurchaseDoc;
import com.zufangbao.sun.ledgerbook.stat.LedgerBookStatHandler;
import com.zufangbao.sun.service.CashFlowService;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.service.OrderService;
import com.zufangbao.sun.service.RepurchaseService;
import com.zufangbao.sun.yunxin.api.deduct.service.DeductApplicationService;
import com.zufangbao.sun.yunxin.api.deduct.service.DeductPlanService;
import com.zufangbao.sun.yunxin.entity.AssetSet;
import com.zufangbao.sun.yunxin.entity.SettlementOrder;
import com.zufangbao.sun.yunxin.entity.api.deduct.DeductApplication;
import com.zufangbao.sun.yunxin.entity.api.deduct.DeductPlan;
import com.zufangbao.sun.yunxin.entity.model.RepaymentChargesDetail;
import com.zufangbao.sun.yunxin.handler.RepaymentPlanHandler;
import com.zufangbao.sun.yunxin.service.RepaymentPlanService;
import com.zufangbao.sun.yunxin.service.SettlementOrderService;
import com.zufangbao.wellsfargo.silverpool.cashauditing.entity.voucher.JournalVoucher;
import com.zufangbao.wellsfargo.silverpool.cashauditing.entity.voucher.SourceDocument;
import com.zufangbao.wellsfargo.silverpool.cashauditing.entity.voucher.SourceDocumentDetail;
import com.zufangbao.wellsfargo.silverpool.cashauditing.entity.voucher.Voucher;
import com.zufangbao.wellsfargo.silverpool.cashauditing.service.JournalVoucherService;
import com.zufangbao.wellsfargo.silverpool.cashauditing.service.SourceDocumentDetailService;
import com.zufangbao.wellsfargo.silverpool.cashauditing.service.SourceDocumentService;
import com.zufangbao.wellsfargo.silverpool.cashauditing.service.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component("repaymentPlanDetailsApiHandler")
public class RepaymentPlanDetailsApiHandlerImpl implements RepaymentPlanDetailsApiHandler{
	
	@Autowired
	private FinancialContractService financialContractService;
	@Autowired
	private RepaymentPlanService repaymentPlanService;
	@Autowired
	private OrderService orderService;
	@Autowired
	private VoucherService voucherService;
	@Autowired
	private SettlementOrderService settlementOrderService;
	@Autowired
	private RepurchaseService repurchaseService;
	@Autowired
	private JournalVoucherService journalVoucherService;
	@Autowired
	private RepaymentPlanHandler repaymentPlanHandler;
	@Autowired
	private LedgerBookStatHandler ledgerBookStatHandler;
	@Autowired
	private DeductPlanService deductPlanService;
	@Autowired
	private DeductApplicationService deductApplicationService;
	@Autowired
	private SourceDocumentService sourceDocumentService;
	@Autowired
	private SourceDocumentDetailService sourceDocumentDetailService;
	@Autowired
	private CashFlowService cashFlowService;
	
	@Override
	public RepaymentPlanDetailsResultModel queryRepaymentPlanDetails(RepaymentPlanDetailsQueryModel queryModel, Date queryTime) {
		FinancialContract financialContract = financialContractService.getUniqueFinancialContractBy(queryModel.getContractNo());
		if(financialContract == null) {
			throw new ApiException(ApiResponseCode.FINANCIAL_PRODUCT_CODE_ERROR);
		}
		String repayScheduleNo = repaymentPlanService.getRepayScheduleNoMD5(queryModel.getContractNo(), queryModel.getOuterRepaymentPlanNo(), com.zufangbao.sun
                .utils.StringUtils.EMPTY);
		AssetSet assetSet = repaymentPlanService.getRepaymentPlanByConditions(repayScheduleNo, queryModel.getSingleLoanContractNo());
		if(assetSet == null) {
			throw new ApiException(ApiResponseCode.SINGLE_LOAN_CONTRACT_NO_ERROR);
		}
		if(!financialContract.getFinancialContractUuid().equals(assetSet.getFinancialContractUuid())){
			throw new ApiException(ApiResponseCode.TRUST_CODE_NOT_AGREE_WITH_TRUST_CODE_OF_ASSET);
		}
		Contract contract = assetSet.getContract();
		if(contract == null) {
			throw new ApiException(ApiResponseCode.CONTRACT_NOT_EXIST);
		}
		RepaymentChargesDetail chargesDetail = repaymentPlanHandler.getPlanRepaymentChargesDetail(assetSet);
		RepaymentChargesDetail paidChargesDetail = repaymentPlanHandler.getPaidChargesDetail(assetSet);
		
		List<PaymentRecordShowModel> paymentRecords = getPaymentRecords(assetSet, financialContract);
		
		List<RepurchaseDoc> repurchaseDocs = repurchaseService.getRepurchaseDocListBy(contract.getId());
		List<Order> allOrderList = orderService.getOrderListByAssetSetUuid(assetSet.getAssetUuid());
		List<Order> guaranteeOrders = allOrderList.stream().filter(o -> o.isGuaranteeOrder()).collect(Collectors.toList());
		List<SettlementOrder> settlementOrders = settlementOrderService.getSettlementOrderListBy(assetSet);
		
		return new RepaymentPlanDetailsResultModel(queryTime, assetSet, contract, financialContract, chargesDetail, paidChargesDetail, paymentRecords, repurchaseDocs, guaranteeOrders, settlementOrders);
	}
	
	private List<PaymentRecordShowModel> getPaymentRecords(AssetSet assetSet, FinancialContract financialContract){
		if (assetSet == null) {
			return null;
		}
		List<JournalVoucher> journalVouchers = journalVoucherService.getPaymentRecordsJournalVoucher(assetSet.getAssetUuid());
		
		List<PaymentRecordShowModel> paymentRecords = new ArrayList<PaymentRecordShowModel>();
		for (JournalVoucher jv : journalVouchers) {
			RepaymentChargesDetail paidUpChargesDetail = new RepaymentChargesDetail(ledgerBookStatHandler.get_jv_asset_detail_amount_of_banksaving_and_independent_accounts(financialContract.getLedgerBookNo(), jv.getJournalVoucherUuid(), assetSet.getAssetUuid()));

			//TODO
			//还款订单时，为null
			SourceDocument sourceDocument = sourceDocumentService.getSourceDocumentBy(jv.getUsedSourceDocumentUuid());
			SourceDocumentDetail sourceDocumentDetail =  sourceDocumentDetailService.getSourceDocumentDetail(jv.getUsedSourceDocumentDetailUuid());
			PaymentRecordShowModel pr = new PaymentRecordShowModel();
			if(sourceDocument!=null && SourceDocument.FIRSTOUTLIER_DEDUCTAPPLICATION.equals(sourceDocument.getFirstOutlierDocType())){
				DeductApplication deductApplication = deductApplicationService.getDeductApplicationByDeductApplicationUuid(sourceDocument.getOutlierDocumentUuid());
				DeductPlan deductPlan = deductPlanService.getDeductPlanByDeductApplicationUuid(deductApplication.getDeductApplicationUuid(),DeductApplicationExecutionStatus.SUCCESS);
				pr = new PaymentRecordShowModel(paidUpChargesDetail, jv, sourceDocumentDetail, deductPlan);
			}else{
				CashFlow cashFlow = null;
				Voucher voucher = null;
				if(sourceDocument!=null){
					cashFlow = cashFlowService.getCashFlowByCashFlowUuid(sourceDocument.getOutlierDocumentUuid());
					voucher = voucherService.get_voucher_by_sourceDocument(sourceDocument);

				}
				pr = new PaymentRecordShowModel(paidUpChargesDetail, jv, sourceDocumentDetail, cashFlow, voucher);
			}
			paymentRecords.add(pr);
		}
		return paymentRecords;
	}
	
}
