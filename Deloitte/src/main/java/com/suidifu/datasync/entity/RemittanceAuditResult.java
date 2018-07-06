package com.suidifu.datasync.entity;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.springframework.util.StringUtils;

@Entity
public class RemittanceAuditResult {
	@Id
	private Long id;
	private String redisKey;// 同步到mysql用的唯一键
	protected String financialContractUuid;
	protected String paymentChannelUuid;
	protected String systemBillOccurDate;
	protected String systemBillIdentity;// execid或refundid
	protected Integer systemBillType;// 0代付单1撤销单

	protected String cashFlowIdentity;
	protected String hostAccountNo;
	protected String cashFlowTransactionTime;

	protected BigDecimal systemBillPlanAmount;
	protected BigDecimal cashFlowTransactionAmount;
	/** 流水借贷标志：0:贷；1:借 -1:无对应流水 */
	protected Integer cashFlowAccountSide;

	protected Integer resultCode = -1;// 1本端多账2平账3对端多账
	// protected String resultMsg;// 对账结果
	protected String resultTime;

	private String lastModifiedTime;// 系统修改时间

	protected String tradeId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRedisKey() {
		return redisKey;
	}

	public void setRedisKey(String redisKey) {
		this.redisKey = redisKey;
	}

	public String getFinancialContractUuid() {
		return financialContractUuid;
	}

	public void setFinancialContractUuid(String financialContractUuid) {
		this.financialContractUuid = financialContractUuid;
	}

	public String getPaymentChannelUuid() {
		return paymentChannelUuid;
	}

	public void setPaymentChannelUuid(String paymentChannelUuid) {
		this.paymentChannelUuid = paymentChannelUuid;
	}

	public String getSystemBillOccurDate() {
		return systemBillOccurDate;
	}

	public void setSystemBillOccurDate(String systemBillOccurDate) {
		this.systemBillOccurDate = systemBillOccurDate;
	}

	public String getSystemBillIdentity() {
		return systemBillIdentity;
	}

	public void setSystemBillIdentity(String systemBillIdentity) {
		this.systemBillIdentity = systemBillIdentity;
	}

	public Integer getSystemBillType() {
		return systemBillType;
	}

	public void setSystemBillType(Integer systemBillType) {
		this.systemBillType = systemBillType;
	}

	public String getCashFlowIdentity() {
		return cashFlowIdentity;
	}

	public void setCashFlowIdentity(String cashFlowIdentity) {
		this.cashFlowIdentity = cashFlowIdentity;
	}

	public String getHostAccountNo() {
		return hostAccountNo;
	}

	public void setHostAccountNo(String hostAccountNo) {
		this.hostAccountNo = hostAccountNo;
	}

	public String getCashFlowTransactionTime() {
		return cashFlowTransactionTime;
	}

	public void setCashFlowTransactionTime(String cashFlowTransactionTime) {
		this.cashFlowTransactionTime = cashFlowTransactionTime;
	}

	public Integer getResultCode() {
		return resultCode;
	}

	public void setResultCode(Integer resultCode) {
		this.resultCode = resultCode;
	}

	// public String getResultMsg() {
	// switch (this.resultCode) {
	// case 1:
	// return "本端多账";
	// case 2:
	// return "平账";
	// case 3:
	// return "对端多账";
	// }
	// return resultMsg;
	// }
	//
	// public void setResultMsg(String resultMsg) {
	// this.resultMsg = resultMsg;
	// }

	public String getResultTime() {
		if (!StringUtils.isEmpty(this.systemBillOccurDate))
			return this.systemBillOccurDate;
		if (!StringUtils.isEmpty(this.cashFlowTransactionTime))
			return this.cashFlowTransactionTime;
		return this.resultTime;
	}

	public void setResultTime(String resultTime) {
		this.resultTime = resultTime;
	}

	public String getLastModifiedTime() {
		return lastModifiedTime;
	}

	public void setLastModifiedTime(String lastModifiedTime) {
		this.lastModifiedTime = lastModifiedTime;
	}

	public String getTradeId() {
		return tradeId;
	}

	public void setTradeId(String tradeId) {
		this.tradeId = tradeId;
	}

	public BigDecimal getSystemBillPlanAmount() {
		return systemBillPlanAmount;
	}

	public void setSystemBillPlanAmount(BigDecimal systemBillPlanAmount) {
		this.systemBillPlanAmount = systemBillPlanAmount;
	}

	public BigDecimal getCashFlowTransactionAmount() {
		return cashFlowTransactionAmount;
	}

	public void setCashFlowTransactionAmount(BigDecimal cashFlowTransactionAmount) {
		this.cashFlowTransactionAmount = cashFlowTransactionAmount;
	}

	public Integer getCashFlowAccountSide() {
		return cashFlowAccountSide;
	}

	public void setCashFlowAccountSide(Integer cashFlowAccountSide) {
		this.cashFlowAccountSide = cashFlowAccountSide;
	}

	@Override
	public String toString() {
		return "RemittanceAuditResult [id=" + id + ", redisKey=" + redisKey + ", financialContractUuid=" + financialContractUuid + ", paymentChannelUuid="
				+ paymentChannelUuid + ", systemBillOccurDate=" + systemBillOccurDate + ", systemBillIdentity=" + systemBillIdentity + ", systemBillType="
				+ systemBillType + ", cashFlowIdentity=" + cashFlowIdentity + ", hostAccountNo=" + hostAccountNo + ", cashFlowTransactionTime="
				+ cashFlowTransactionTime + ", systemBillPlanAmount=" + systemBillPlanAmount + ", cashFlowTransactionAmount=" + cashFlowTransactionAmount
				+ ", cashFlowAccountSide=" + cashFlowAccountSide + ", resultCode=" + resultCode + ", resultTime=" + resultTime + ", lastModifiedTime="
				+ lastModifiedTime + ", tradeId=" + tradeId + "]";
	}

}