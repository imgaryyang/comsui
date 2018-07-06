package com.suidifu.microservice.entity;

import com.suidifu.owlman.microservice.enumation.SourceDocumentDetailStatus;
import com.zufangbao.sun.api.model.deduct.DeductApplicationRepaymentDetail;
import com.zufangbao.sun.entity.repayment.order.PaymentOrder;
import com.zufangbao.sun.entity.repayment.order.RepaymentBusinessType;
import com.zufangbao.sun.entity.repayment.order.RepaymentOrderItem;
import com.zufangbao.sun.entity.voucher.TemplatesForPay;
import com.zufangbao.sun.yunxin.entity.api.VoucherPayer;
import com.zufangbao.sun.yunxin.entity.api.VoucherSource;
import com.zufangbao.sun.yunxin.entity.api.VoucherType;
import com.zufangbao.sun.yunxin.entity.api.deduct.DeductPlan;
import com.zufangbao.sun.yunxin.entity.model.voucher.VoucherCreateAssetInfoModel;
import com.zufangbao.sun.yunxin.entity.model.voucher.VoucherTmpDepositCreateRepaymentModel;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

public class SourceDocumentDetailImporter {
	

	public static SourceDocumentDetail createSourceDocumentDetailV2(String sourceDocumentUuid, String contractUniqueId,String deductApplicationDetailUuid,String deductApplicationUuid,String capitalAccountNo, DeductPlan deductPlan, String repaymentOrderCode, BigDecimal amount){
		SourceDocumentDetail sourceDocumentDetail = new SourceDocumentDetail(sourceDocumentUuid, contractUniqueId, repaymentOrderCode, amount, SourceDocumentDetailStatus.UNSUCCESS,
				VoucherSource.THIRD_PARTY_DEDUCTION_VOUCHER.getKey(), deductApplicationUuid, VoucherType.THIRD_PARTY_DEDUCTION_VOUCHER.getKey(), deductApplicationDetailUuid, VoucherPayer.LOANER,capitalAccountNo, deductPlan.getCpBankCardNo(), deductPlan.getCpBankAccountHolder(), deductPlan.getCpBankName(), deductPlan.getFinancialContractUuid());
		return sourceDocumentDetail;
	}
	
	@Deprecated
	public static SourceDocumentDetail createSourceDocumentDetail(String sourceDocumentUuid, DeductApplicationRepaymentDetail deductApplicationRepaymentDetail,String capitalAccountNo, DeductPlan deductPlan){
		SourceDocumentDetail sourceDocumentDetail = new SourceDocumentDetail(sourceDocumentUuid, deductApplicationRepaymentDetail.getContractUniqueId(), deductApplicationRepaymentDetail.getRepaymentPlanCode(), deductApplicationRepaymentDetail.getTotalAmount(), SourceDocumentDetailStatus.UNSUCCESS,
				VoucherSource.THIRD_PARTY_DEDUCTION_VOUCHER.getKey(), deductApplicationRepaymentDetail.getDeductApplicationUuid(), VoucherType.THIRD_PARTY_DEDUCTION_VOUCHER.getKey(), deductApplicationRepaymentDetail.getDeductApplicationDetailUuid(), VoucherPayer.LOANER,capitalAccountNo, deductPlan.getCpBankCardNo(), deductPlan.getCpBankAccountHolder(), deductPlan.getCpBankName(), deductPlan.getFinancialContractUuid());
		return sourceDocumentDetail;
	}

	public static SourceDocumentDetail createActivePaymentVoucherDetail(String sourceDocumentUuid, String contractUniqueId, String repaymentPlanNo,
			BigDecimal amount, String firstNo, String secondType, VoucherPayer payer, String receivableAccountNo, String paymentAccountNo,
			String paymentName, String paymentBank, String financialContractUuid) {
		SourceDocumentDetailStatus status = SourceDocumentDetailStatus.UNSUCCESS;
		String firstType = VoucherSource.ACTIVE_PAYMENT_VOUCHER.getKey();
		return new SourceDocumentDetail(sourceDocumentUuid, contractUniqueId, repaymentPlanNo, amount, status, firstType, firstNo, secondType, null, payer, receivableAccountNo, paymentAccountNo, paymentName, paymentBank, financialContractUuid);
	}
	
	public static SourceDocumentDetail createActivePaymentVoucherDetailWithAssetInfoModel(String sourceDocumentUuid, String contractUniqueId, String repaymentPlanNo,
			BigDecimal amount, String firstNo, String secondType, VoucherPayer payer, String receivableAccountNo, String paymentAccountNo,
			String paymentName, String paymentBank, String financialContractUuid, VoucherCreateAssetInfoModel assetInfoModel) {
		SourceDocumentDetailStatus status = SourceDocumentDetailStatus.UNSUCCESS;
		String firstType = VoucherSource.ACTIVE_PAYMENT_VOUCHER.getKey();
		return new SourceDocumentDetail(sourceDocumentUuid, contractUniqueId, repaymentPlanNo, amount, status, firstType, firstNo, secondType, null, payer, receivableAccountNo, paymentAccountNo, paymentName, paymentBank, financialContractUuid, assetInfoModel);
	}
	
	public static SourceDocumentDetail createVoucherDetailWithRepaymentInfoModel(String sourceDocumentUuid, String contractUniqueId, String repaymentPlanNo,
																				 BigDecimal amount, String firstNo, String secondType, VoucherPayer payer, String receivableAccountNo, String paymentAccountNo,
																				 String paymentName, String paymentBank, String financialContractUuid, String voucherUuid, VoucherTmpDepositCreateRepaymentModel repaymentModel, RepaymentBusinessType repaymentBusinessType) {
		SourceDocumentDetailStatus status = SourceDocumentDetailStatus.UNSUCCESS;
		String firstType = VoucherSource.ACTIVE_PAYMENT_VOUCHER.getKey();
		SourceDocumentDetail sdd = new SourceDocumentDetail(sourceDocumentUuid, contractUniqueId, repaymentPlanNo, amount, status, firstType, firstNo, secondType, null, payer, receivableAccountNo, paymentAccountNo, paymentName, paymentBank, financialContractUuid,voucherUuid);
		sdd.setDetailAmount(repaymentModel.converToDetailAmount(repaymentBusinessType));
		return sdd;
	}


	public static SourceDocumentDetail createBusinessPaymentVoucherDetailsWithPayModel(String sourceDocumentUuid,String contractUniqueId,
		String repaymentPlanNo,BigDecimal amount,String firstNo,String secondType, String secondNo,
		String receivableAccountNo,String financialContractUuid,
		String paymentAccountNo,String paymentName, String paymentBank,String voucherUuid,
		TemplatesForPay templatesForPay, Date recycleDate){

		SourceDocumentDetail sdd = new SourceDocumentDetail(sourceDocumentUuid, contractUniqueId, repaymentPlanNo,
			amount, SourceDocumentDetailStatus.UNSUCCESS, VoucherSource.BUSINESS_PAYMENT_VOUCHER.getKey(),firstNo,secondType,
			secondNo, VoucherPayer.BUSINESS, receivableAccountNo, paymentAccountNo,
			paymentName,paymentBank,financialContractUuid,voucherUuid);

		sdd.setPrincipal(templatesForPay.getPrincipalValue());
		sdd.setInterest(templatesForPay.getInterestValue());
		sdd.setServiceCharge(templatesForPay.getLoanServiceFeeValue());
		sdd.setMaintenanceCharge(templatesForPay.getTechServiceFeeValue());
		sdd.setOtherCharge(templatesForPay.getOtherFeeValue());
		sdd.setPenaltyFee(templatesForPay.getOverduePenaltyValue());
		sdd.setLatePenalty(templatesForPay.getOverdueDamagesValue());
		sdd.setLateFee(templatesForPay.getOverdueServiceFeeValue());
		sdd.setLateOtherCost(templatesForPay.getOverdueOtherFeeValue());
		sdd.setActualPaymentTime(recycleDate);

		return sdd;
	}

	public static SourceDocumentDetail createBusinessPaymentVoucherDetails(String sourceDocumentUuid,String contractUniqueId,
		String repaymentPlanNo,BigDecimal amount,String firstNo,String secondType, String secondNo,
		String receivableAccountNo,String financialContractUuid,
		String paymentAccountNo,String paymentName, String paymentBank,String voucherUuid){

		return new SourceDocumentDetail(sourceDocumentUuid, contractUniqueId, repaymentPlanNo,
			amount, SourceDocumentDetailStatus.UNSUCCESS, VoucherSource.BUSINESS_PAYMENT_VOUCHER.getKey(),firstNo,secondType,
			secondNo, VoucherPayer.BUSINESS, receivableAccountNo, paymentAccountNo,
			paymentName,paymentBank,financialContractUuid,voucherUuid);
	}

	public static SourceDocumentDetail createSourceDocumentDetail(String sourceDocumentUuid, RepaymentOrderItem repaymentOrderItem, PaymentOrder paymentOrder, Map<String,BigDecimal> detailAmount, String repaymentPlanNo){
		SourceDocumentDetail sourceDocumentDetail = new SourceDocumentDetail(sourceDocumentUuid, repaymentOrderItem.getContractUniqueId(), repaymentPlanNo, repaymentOrderItem.getAmount(), SourceDocumentDetailStatus.UNSUCCESS,
				VoucherSource.THIRD_PARTY_DEDUCTION_VOUCHER.getKey(), repaymentOrderItem.getOrderUuid(), VoucherType.THIRD_PARTY_DEDUCTION_VOUCHER.getKey(), repaymentOrderItem.getOrderDetailUuid(), VoucherPayer.LOANER,paymentOrder.getHostAccountNo(), paymentOrder.getCounterAccountNo(), paymentOrder.getCounterAccountName(), paymentOrder.getCounterAccountBankName(), repaymentOrderItem.getFinancialContractUuid());
		sourceDocumentDetail.setActualPaymentTime(repaymentOrderItem.getRepaymentPlanTime());
		sourceDocumentDetail.setDetailAmount(detailAmount);
		return sourceDocumentDetail;
	}
}
