package com.suidifu.bridgewater.api.model;

import java.math.BigDecimal;

public class RemittanceResultDetail {

	private String detailNo;
	
	private String bankCardNo;
	
	private String bankAccountHolder;
	
	private String amount;
	
	private Integer status;
	
	private String bankSerialNo;
	
	private String actExcutedTime;

	public RemittanceResultDetail() {
		super();
	}

	public RemittanceResultDetail(String detailNo, String bankCardNo,
			String bankAccountHolder, BigDecimal amount, Integer status, String bankSerialNo,
			String actExcutedTime) {
		super();
		this.detailNo = detailNo;
		this.bankCardNo = bankCardNo;
		this.bankAccountHolder = bankAccountHolder;
		this.amount = null == amount ? null : amount.toString();
		this.status = status;
		this.bankSerialNo = bankSerialNo;
		this.actExcutedTime = actExcutedTime;
	}

	public String getDetailNo() {
		return detailNo;
	}

	public void setDetailNo(String detailNo) {
		this.detailNo = detailNo;
	}

	public String getBankCardNo() {
		return bankCardNo;
	}

	public void setBankCardNo(String bankCardNo) {
		this.bankCardNo = bankCardNo;
	}

	public String getBankAccountHolder() {
		return bankAccountHolder;
	}

	public void setBankAccountHolder(String bankAccountHolder) {
		this.bankAccountHolder = bankAccountHolder;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getBankSerialNo() {
		return bankSerialNo;
	}

	public void setBankSerialNo(String bankSerialNo) {
		this.bankSerialNo = bankSerialNo;
	}

	public String getActExcutedTime() {
		return actExcutedTime;
	}

	public void setActExcutedTime(String actExcutedTime) {
		this.actExcutedTime = actExcutedTime;
	}

}
