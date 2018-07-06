package com.suidifu.matryoshka.snapshot;

import com.zufangbao.sun.entity.repurchase.RepurchaseDoc;
import com.zufangbao.sun.entity.repurchase.RepurchaseStatus;

import java.math.BigDecimal;
import java.util.Date;

public class RepurchaseDocSnapshot {

	// 回购单号
	private String repurchaseDocUuid;

	// 信托合同uuid
	private String financialContractUuid;

	// 回购金额
	private BigDecimal amount;

	// 回购起始期数
	private Integer fstPeriod;

	// 回购起始日
	private Date repoStartDate;

	// 回购截止日
	private Date repoEndDate;

	// 发生时间 置回购中状态的时间
	private Date createTime;

	// 核销日期
	private Date verificationTime;

	// 贷款合同Id
	private Long contractId;

	// 贷款合同编号
	private String contractNo;

	// 客户Uuid
	private String customerUuid;

	// 回购状态
	private RepurchaseStatus repurchaseStatus;

	// 回购本金金额
	private BigDecimal repurchasePrincipal;

	// 回购利息金额
	private BigDecimal repurchaseInterest;

	// 回购罚息金额
	private BigDecimal repurchasePenalty;

	// 回购其他费用金额
	private BigDecimal repurchaseOtherCharges;

	// 回购应还费用
	private BigDecimal repurchaseFee;

	public RepurchaseDocSnapshot() {

	}

	public RepurchaseDocSnapshot(RepurchaseDoc repurchaseDoc) {
		if (repurchaseDoc == null)
			return;
		this.repurchaseDocUuid = repurchaseDoc.getRepurchaseDocUuid();
		this.financialContractUuid = repurchaseDoc.getFinancialContractUuid();
		this.amount = repurchaseDoc.getAmount();
		this.repoStartDate = repurchaseDoc.getRepoStartDate();
		this.repoEndDate = repurchaseDoc.getRepoEndDate();
		this.createTime = repurchaseDoc.getCreatTime();
		this.verificationTime = repurchaseDoc.getVerificationTime();
		this.contractId = repurchaseDoc.getContractId();
		this.contractNo = repurchaseDoc.getContractNo();
		this.customerUuid = repurchaseDoc.getCustomerUuid();
		this.repurchaseStatus = repurchaseDoc.getRepurchaseStatus();
		this.repurchasePrincipal = repurchaseDoc.getRepurchasePrincipal();
		this.repurchaseInterest = repurchaseDoc.getRepurchaseInterest();
		this.repurchasePenalty = repurchaseDoc.getRepurchasePenalty();
		this.repurchaseOtherCharges = repurchaseDoc.getRepurchaseOtherCharges();
		this.repurchaseFee = repurchaseDoc.getRepurchasePenalty().add(repurchaseDoc.getRepurchaseOtherCharges());
	}

	public String getRepurchaseDocUuid() {
		return repurchaseDocUuid;
	}

	public void setRepurchaseDocUuid(String repurchaseDocUuid) {
		this.repurchaseDocUuid = repurchaseDocUuid;
	}

	public String getFinancialContractUuid() {
		return financialContractUuid;
	}

	public void setFinancialContractUuid(String financialContractUuid) {
		this.financialContractUuid = financialContractUuid;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Date getRepoStartDate() {
		return repoStartDate;
	}

	public void setRepoStartDate(Date repoStartDate) {
		this.repoStartDate = repoStartDate;
	}

	public Date getRepoEndDate() {
		return repoEndDate;
	}

	public void setRepoEndDate(Date repoEndDate) {
		this.repoEndDate = repoEndDate;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getVerificationTime() {
		return verificationTime;
	}

	public void setVerificationTime(Date verificationTime) {
		this.verificationTime = verificationTime;
	}

	public Long getContractId() {
		return contractId;
	}

	public void setContractId(Long contractId) {
		this.contractId = contractId;
	}

	public String getContractNo() {
		return contractNo;
	}

	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}

	public String getCustomerUuid() {
		return customerUuid;
	}

	public void setCustomerUuid(String customerUuid) {
		this.customerUuid = customerUuid;
	}

	public RepurchaseStatus getRepurchaseStatus() {
		return repurchaseStatus;
	}

	public Integer getFstPeriod() {
		return fstPeriod;
	}

	public void setFstPeriod(Integer fstPeriod) {
		this.fstPeriod = fstPeriod;
	}

	public void setRepurchaseStatus(RepurchaseStatus repurchaseStatus) {
		this.repurchaseStatus = repurchaseStatus;
	}

	public BigDecimal getRepurchasePrincipal() {
		return repurchasePrincipal;
	}

	public void setRepurchasePrincipal(BigDecimal repurchasePrincipal) {
		this.repurchasePrincipal = repurchasePrincipal;
	}

	public BigDecimal getRepurchaseInterest() {
		return repurchaseInterest;
	}

	public void setRepurchaseInterest(BigDecimal repurchaseInterest) {
		this.repurchaseInterest = repurchaseInterest;
	}

	public BigDecimal getRepurchasePenalty() {
		return repurchasePenalty;
	}

	public void setRepurchasePenalty(BigDecimal repurchasePenalty) {
		this.repurchasePenalty = repurchasePenalty;
	}

	public BigDecimal getRepurchaseOtherCharges() {
		return repurchaseOtherCharges;
	}

	public void setRepurchaseOtherCharges(BigDecimal repurchaseOtherCharges) {
		this.repurchaseOtherCharges = repurchaseOtherCharges;
	}
	
	public BigDecimal getRepurchaseFee() {
        return repurchaseFee == null ? BigDecimal.ZERO : this.repurchaseFee;
    }

	public void setRepurchaseFee(BigDecimal repurchaseFee) {
		this.repurchaseFee = repurchaseFee;
	}

	public void subRepurchaseFee(BigDecimal repurchaseFee) {
		this.repurchaseFee = getRepurchaseFee().subtract(repurchaseFee);
	}
}
