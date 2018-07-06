package com.zufangbao.earth.yunxin.api.model;

import com.demo2do.core.utils.DateUtils;
import com.zufangbao.sun.entity.directbank.business.CashFlow;
import com.zufangbao.sun.entity.directbank.business.CashFlowChannelType;
import com.zufangbao.sun.yunxin.entity.api.VoucherSource;
import com.zufangbao.sun.yunxin.entity.api.VoucherType;
import com.zufangbao.sun.yunxin.entity.api.deduct.DeductPlan;
import com.zufangbao.sun.yunxin.entity.model.RepaymentChargesDetail;
import com.zufangbao.wellsfargo.silverpool.cashauditing.entity.voucher.JournalVoucher;
import com.zufangbao.wellsfargo.silverpool.cashauditing.entity.voucher.SourceDocumentDetail;
import com.zufangbao.wellsfargo.silverpool.cashauditing.entity.voucher.ThirdPartVoucherSourceMapSpec;
import com.zufangbao.wellsfargo.silverpool.cashauditing.entity.voucher.Voucher;
import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;

public class PaymentRecordShowModel {
	//凭证来源
	private Integer voucherSource;
	//凭证类型
	private Integer voucherType;
	//凭证编号
	private String voucherNo;
	//凭证状态
	private Integer status;
	//付款机构名称 
	private String paymentBank;
	//付款机构账户姓名
	private String paymentName;
	//付款机构账户号
	private String paymentAccountNo;
	//支付网关
	private Integer paymentGateway;
	//付款机构流水号
	private String bankSequenceNo;
	//流水入账时间
	private String transactionTime;
	//第三方流水号
	private String transactionSerialNo;
	//第三方流水入账时间
	private String completeTime;
	//还款本金
	private BigDecimal loanAssetPrincipal;
	//还款利息
	private BigDecimal loanAssetInterest;
	//贷款服务费
	private BigDecimal loanServiceFee;
	//技术维护费
	private BigDecimal loanTechFee;
	//其他费用
	private BigDecimal loanOtherFee;
	//逾期罚息
	private BigDecimal overdueFeePenalty;
	//逾期违约金
	private BigDecimal overdueFeeObligation;
	//逾期服务费
	private BigDecimal overdueFeeService;
	//逾期其他费用
	private BigDecimal overdueFeeOther;
	//还款总额
	private BigDecimal totalFee;

	public PaymentRecordShowModel() {
		super();
	}

	public PaymentRecordShowModel(RepaymentChargesDetail paidUpChargesDetail,JournalVoucher jv, SourceDocumentDetail sourceDocumentDetail,DeductPlan deductPlan) {
		if(jv != null && sourceDocumentDetail != null){
			this.voucherSource = getVoucherSource(sourceDocumentDetail.getFirstType());
			this.voucherType = getVoucherType(sourceDocumentDetail.getSecondType());
			this.voucherNo = jv.getJournalVoucherNo();
			this.status = jv.getStatus() == null ? null:jv.getStatus().ordinal();
			this.paymentBank = sourceDocumentDetail.getPaymentBank();
			this.paymentName = jv.getSourceDocumentCounterPartyName();
			this.paymentAccountNo = getMarkedAccountNo(jv.getSourceDocumentCounterPartyAccount());
			CashFlowChannelType cashFlowChannelType = jv.getCashFlowChannelType();
			if(cashFlowChannelType != null){
				this.paymentGateway  = ThirdPartVoucherSourceMapSpec.cashFlowStringMap.containsKey(cashFlowChannelType.ordinal())?cashFlowChannelType.ordinal():null;
			}

		}
		if(deductPlan!=null){
			this.transactionSerialNo = deductPlan.getTransactionSerialNo();
			if(deductPlan.getCompleteTime() != null){
				this.completeTime = DateUtils.format(deductPlan.getCompleteTime(), "yyyy-MM-dd HH:mm:ss");
			}
		}
		this.loanAssetPrincipal = paidUpChargesDetail.getLoanAssetPrincipal();
		this.loanAssetInterest = paidUpChargesDetail.getLoanAssetInterest();
		this.loanServiceFee = paidUpChargesDetail.getLoanServiceFee();
		this.loanTechFee = paidUpChargesDetail.getLoanTechFee();
		this.loanOtherFee = paidUpChargesDetail.getLoanOtherFee();
		this.overdueFeePenalty = paidUpChargesDetail.getOverdueFeePenalty();
		this.overdueFeeObligation = paidUpChargesDetail.getOverdueFeeObligation();
		this.overdueFeeService = paidUpChargesDetail.getOverdueFeeService();
		this.overdueFeeOther = paidUpChargesDetail.getOverdueFeeOther();
		this.totalFee = paidUpChargesDetail.getTotalFee();
	}
	
	public PaymentRecordShowModel(RepaymentChargesDetail paidUpChargesDetail,JournalVoucher jv, SourceDocumentDetail sourceDocumentDetail, CashFlow cashFlow, Voucher voucher) {
		if(jv != null && sourceDocumentDetail != null){
			this.voucherSource = getVoucherSource(sourceDocumentDetail.getFirstType());
			this.voucherType = getVoucherType(sourceDocumentDetail.getSecondType());
			this.status = jv.getStatus() == null ? null:jv.getStatus().ordinal();
			this.paymentBank = sourceDocumentDetail.getPaymentBank();
			this.paymentName = jv.getSourceDocumentCounterPartyName();
			this.paymentAccountNo = getMarkedAccountNo(jv.getSourceDocumentCounterPartyAccount());
			CashFlowChannelType cashFlowChannelType = jv.getCashFlowChannelType();
			if(cashFlowChannelType != null){
				this.paymentGateway  = ThirdPartVoucherSourceMapSpec.cashFlowStringMap.containsKey(cashFlowChannelType.ordinal())?cashFlowChannelType.ordinal():null;
			}

		}
		if(cashFlow!=null){
			this.bankSequenceNo = cashFlow.getBankSequenceNo();
			if(cashFlow.getTransactionTime() != null){
				this.transactionTime = DateUtils.format(cashFlow.getTransactionTime(), "yyyy-MM-dd HH:mm:ss");
			}
		}
		if(voucher!=null){
			this.voucherNo = voucher.getVoucherNo();
		}
		this.loanAssetPrincipal = paidUpChargesDetail.getLoanAssetPrincipal();
		this.loanAssetInterest = paidUpChargesDetail.getLoanAssetInterest();
		this.loanServiceFee = paidUpChargesDetail.getLoanServiceFee();
		this.loanTechFee = paidUpChargesDetail.getLoanTechFee();
		this.loanOtherFee = paidUpChargesDetail.getLoanOtherFee();
		this.overdueFeePenalty = paidUpChargesDetail.getOverdueFeePenalty();
		this.overdueFeeObligation = paidUpChargesDetail.getOverdueFeeObligation();
		this.overdueFeeService = paidUpChargesDetail.getOverdueFeeService();
		this.overdueFeeOther = paidUpChargesDetail.getOverdueFeeOther();
		this.totalFee = paidUpChargesDetail.getTotalFee();
	}

	public Integer getVoucherSource(String firstType) {
		try {
			return VoucherSource.fromKey(firstType).ordinal();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public Integer getVoucherType(String secondType) {
		try {
			return VoucherType.fromKey(secondType).ordinal();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public String getMarkedAccountNo(String accountNo){
		if(StringUtils.isEmpty(accountNo)){
			return "";
		}
		return StringUtils.left(accountNo, 5)+"***"+StringUtils.right(accountNo, 4);
	}

	public Integer getVoucherSource() {
		return voucherSource;
	}

	public Integer getVoucherType() {
		return voucherType;
	}

	public String getVoucherNo() {
		return voucherNo;
	}

	public Integer getStatus() {
		return status;
	}

	public String getPaymentBank() {
		return paymentBank;
	}

	public String getPaymentName() {
		return paymentName;
	}

	public String getPaymentAccountNo() {
		return paymentAccountNo;
	}

	public Integer getPaymentGateway() {
		return paymentGateway;
	}

	public String getBankSequenceNo() {
		return bankSequenceNo;
	}

	public String getTransactionTime() {
		return transactionTime;
	}

	public String getTransactionSerialNo() {
		return transactionSerialNo;
	}

	public String getCompleteTime() {
		return completeTime;
	}

	public BigDecimal getLoanAssetPrincipal() {
		return loanAssetPrincipal;
	}

	public BigDecimal getLoanAssetInterest() {
		return loanAssetInterest;
	}

	public BigDecimal getLoanServiceFee() {
		return loanServiceFee;
	}

	public BigDecimal getLoanTechFee() {
		return loanTechFee;
	}

	public BigDecimal getLoanOtherFee() {
		return loanOtherFee;
	}

	public BigDecimal getOverdueFeePenalty() {
		return overdueFeePenalty;
	}

	public BigDecimal getOverdueFeeObligation() {
		return overdueFeeObligation;
	}

	public BigDecimal getOverdueFeeService() {
		return overdueFeeService;
	}

	public BigDecimal getOverdueFeeOther() {
		return overdueFeeOther;
	}

	public BigDecimal getTotalFee() {
		return totalFee;
	}

	public void setVoucherSource(Integer voucherSource) {
		this.voucherSource = voucherSource;
	}

	public void setVoucherType(Integer voucherType) {
		this.voucherType = voucherType;
	}

	public void setVoucherNo(String voucherNo) {
		this.voucherNo = voucherNo;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public void setPaymentBank(String paymentBank) {
		this.paymentBank = paymentBank;
	}

	public void setPaymentName(String paymentName) {
		this.paymentName = paymentName;
	}

	public void setPaymentAccountNo(String paymentAccountNo) {
		this.paymentAccountNo = paymentAccountNo;
	}

	public void setPaymentGateway(Integer paymentGateway) {
		this.paymentGateway = paymentGateway;
	}

	public void setBankSequenceNo(String bankSequenceNo) {
		this.bankSequenceNo = bankSequenceNo;
	}

	public void setTransactionTime(String transactionTime) {
		this.transactionTime = transactionTime;
	}

	public void setTransactionSerialNo(String transactionSerialNo) {
		this.transactionSerialNo = transactionSerialNo;
	}

	public void setCompleteTime(String completeTime) {
		this.completeTime = completeTime;
	}

	public void setLoanAssetPrincipal(BigDecimal loanAssetPrincipal) {
		this.loanAssetPrincipal = loanAssetPrincipal;
	}

	public void setLoanAssetInterest(BigDecimal loanAssetInterest) {
		this.loanAssetInterest = loanAssetInterest;
	}

	public void setLoanServiceFee(BigDecimal loanServiceFee) {
		this.loanServiceFee = loanServiceFee;
	}

	public void setLoanTechFee(BigDecimal loanTechFee) {
		this.loanTechFee = loanTechFee;
	}

	public void setLoanOtherFee(BigDecimal loanOtherFee) {
		this.loanOtherFee = loanOtherFee;
	}

	public void setOverdueFeePenalty(BigDecimal overdueFeePenalty) {
		this.overdueFeePenalty = overdueFeePenalty;
	}

	public void setOverdueFeeObligation(BigDecimal overdueFeeObligation) {
		this.overdueFeeObligation = overdueFeeObligation;
	}

	public void setOverdueFeeService(BigDecimal overdueFeeService) {
		this.overdueFeeService = overdueFeeService;
	}

	public void setOverdueFeeOther(BigDecimal overdueFeeOther) {
		this.overdueFeeOther = overdueFeeOther;
	}

	public void setTotalFee(BigDecimal totalFee) {
		this.totalFee = totalFee;
	}
	
	
}
