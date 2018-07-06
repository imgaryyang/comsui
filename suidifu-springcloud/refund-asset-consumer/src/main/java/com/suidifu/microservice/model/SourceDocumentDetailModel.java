package com.suidifu.microservice.model;

import java.math.BigDecimal;
import java.util.Date;

public class SourceDocumentDetailModel {
	private long id;
	private String uuid;
	private String sourceDocumentUuid;
	private String contractUniqueId;
	private String repaymentPlanNo;
	private BigDecimal amount;
	private int status;
	private String firstType;
	private String firstNo;
	private String secondType;
	private String secondNo;
	private int payer;
	private String receivableAccountNo;
	private String paymentAccountNo;
	private String paymentName;
	private String paymentBank;
	private int checkState;
	private String comment;
	private String financialContractUuid;
	private BigDecimal principal = BigDecimal.ZERO;
	private BigDecimal interest = BigDecimal.ZERO;
	private BigDecimal serviceCharge = BigDecimal.ZERO;
	private BigDecimal maintenanceCharge = BigDecimal.ZERO;
	private BigDecimal otherCharge = BigDecimal.ZERO;
	private BigDecimal penaltyFee = BigDecimal.ZERO;
	private BigDecimal latePenalty = BigDecimal.ZERO;
	private BigDecimal lateFee = BigDecimal.ZERO;
	private BigDecimal lateOtherCost = BigDecimal.ZERO;
	private String voucherUuid;
	private Date actualPaymentTime;
	
	
	public SourceDocumentDetailModel() {
		// TODO Auto-generated constructor stub
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getSourceDocumentUuid() {
		return sourceDocumentUuid;
	}
	public void setSourceDocumentUuid(String sourceDocumentUuid) {
		this.sourceDocumentUuid = sourceDocumentUuid;
	}
	public String getContractUniqueId() {
		return contractUniqueId;
	}
	public void setContractUniqueId(String contractUniqueId) {
		this.contractUniqueId = contractUniqueId;
	}
	public String getRepaymentPlanNo() {
		return repaymentPlanNo;
	}
	public void setRepaymentPlanNo(String repaymentPlanNo) {
		this.repaymentPlanNo = repaymentPlanNo;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getFirstType() {
		return firstType;
	}
	public void setFirstType(String firstType) {
		this.firstType = firstType;
	}
	public String getFirstNo() {
		return firstNo;
	}
	public void setFirstNo(String firstNo) {
		this.firstNo = firstNo;
	}
	public String getSecondType() {
		return secondType;
	}
	public void setSecondType(String secondType) {
		this.secondType = secondType;
	}
	public String getSecondNo() {
		return secondNo;
	}
	public void setSecondNo(String secondNo) {
		this.secondNo = secondNo;
	}
	public int getPayer() {
		return payer;
	}
	public void setPayer(int payer) {
		this.payer = payer;
	}
	public String getReceivableAccountNo() {
		return receivableAccountNo;
	}
	public void setReceivableAccountNo(String receivableAccountNo) {
		this.receivableAccountNo = receivableAccountNo;
	}
	public String getPaymentAccountNo() {
		return paymentAccountNo;
	}
	public void setPaymentAccountNo(String paymentAccountNo) {
		this.paymentAccountNo = paymentAccountNo;
	}
	public String getPaymentName() {
		return paymentName;
	}
	public void setPaymentName(String paymentName) {
		this.paymentName = paymentName;
	}
	public String getPaymentBank() {
		return paymentBank;
	}
	public void setPaymentBank(String paymentBank) {
		this.paymentBank = paymentBank;
	}
	public int getCheckState() {
		return checkState;
	}
	public void setCheckState(int checkState) {
		this.checkState = checkState;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getFinancialContractUuid() {
		return financialContractUuid;
	}
	public void setFinancialContractUuid(String financialContractUuid) {
		this.financialContractUuid = financialContractUuid;
	}
	public BigDecimal getPrincipal() {
		return principal;
	}
	public void setPrincipal(BigDecimal principal) {
		this.principal = principal;
	}
	public BigDecimal getInterest() {
		return interest;
	}
	public void setInterest(BigDecimal interest) {
		this.interest = interest;
	}
	public BigDecimal getServiceCharge() {
		return serviceCharge;
	}
	public void setServiceCharge(BigDecimal serviceCharge) {
		this.serviceCharge = serviceCharge;
	}
	public BigDecimal getMaintenanceCharge() {
		return maintenanceCharge;
	}
	public void setMaintenanceCharge(BigDecimal maintenanceCharge) {
		this.maintenanceCharge = maintenanceCharge;
	}
	public BigDecimal getOtherCharge() {
		return otherCharge;
	}
	public void setOtherCharge(BigDecimal otherCharge) {
		this.otherCharge = otherCharge;
	}
	public BigDecimal getPenaltyFee() {
		return penaltyFee;
	}
	public void setPenaltyFee(BigDecimal penaltyFee) {
		this.penaltyFee = penaltyFee;
	}
	public BigDecimal getLatePenalty() {
		return latePenalty;
	}
	public void setLatePenalty(BigDecimal latePenalty) {
		this.latePenalty = latePenalty;
	}
	public BigDecimal getLateFee() {
		return lateFee;
	}
	public void setLateFee(BigDecimal lateFee) {
		this.lateFee = lateFee;
	}
	public BigDecimal getLateOtherCost() {
		return lateOtherCost;
	}
	public void setLateOtherCost(BigDecimal lateOtherCost) {
		this.lateOtherCost = lateOtherCost;
	}
	public String getVoucherUuid() {
		return voucherUuid;
	}
	public void setVoucherUuid(String voucherUuid) {
		this.voucherUuid = voucherUuid;
	}
	public Date getActualPaymentTime() {
		return actualPaymentTime;
	}
	public void setActualPaymentTime(Date actualPaymentTime) {
		this.actualPaymentTime = actualPaymentTime;
	}
	
}
