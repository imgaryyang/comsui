package com.suidifu.jpmorgan.entity;

import java.math.BigDecimal;

public class TradeInfo {

	private String uuid;

	private Integer accountSide;

	private String sourceAccountName;

	private String sourceAccountNo;

	private String sourceAccountAppendix;

	private String sourceBankInfo;

	private String destinationAccountName;

	private String destinationAccountNo;

	private String destinationAccountAppendix;

	private String destinationBankInfo;

	private BigDecimal transactionAmount;

	private String currencyCode;

	private String postscript;

	private String outlierTransactionUuid;

	private Integer gatewayType;

	private String gatewayRouterInfo;

	public TradeInfo() {
		super();
	}

	public TradeInfo(String uuid, Integer accountSide,
			String sourceAccountName, String sourceAccountNo,
			String sourceAccountAppendix, String sourceBankInfo,
			String destinationAccountName, String destinationAccountNo,
			String destinationAccountAppendix, String destinationBankInfo,
			BigDecimal transactionAmount, String currencyCode,
			String postscript, String outierTransactionUuid,
			Integer gatewayType, String gatewayRouterInfo) {
		super();
		this.uuid = uuid;
		this.accountSide = accountSide;
		this.sourceAccountName = sourceAccountName;
		this.sourceAccountNo = sourceAccountNo;
		this.sourceAccountAppendix = sourceAccountAppendix;
		this.sourceBankInfo = sourceBankInfo;
		this.destinationAccountName = destinationAccountName;
		this.destinationAccountNo = destinationAccountNo;
		this.destinationAccountAppendix = destinationAccountAppendix;
		this.destinationBankInfo = destinationBankInfo;
		this.transactionAmount = transactionAmount;
		this.currencyCode = currencyCode;
		this.postscript = postscript;
		this.outlierTransactionUuid = outierTransactionUuid;
		this.gatewayType = gatewayType;
		this.gatewayRouterInfo = gatewayRouterInfo;
	}

	public TradeInfo(Integer accountSide, String sourceAccountName,
			String sourceAccountNo, String sourceAccountAppendix,
			String sourceBankInfo, String destinationAccountName,
			String destinationAccountNo, String destinationAccountAppendix,
			String destinationBankInfo, String currencyCode, String postscript,
			String outLierTransactionUuid) {
		super();
		this.accountSide = accountSide;
		this.sourceAccountName = sourceAccountName;
		this.sourceAccountNo = sourceAccountNo;
		this.sourceAccountAppendix = sourceAccountAppendix;
		this.sourceBankInfo = sourceBankInfo;
		this.destinationAccountName = destinationAccountName;
		this.destinationAccountNo = destinationAccountNo;
		this.destinationAccountAppendix = destinationAccountAppendix;
		this.destinationBankInfo = destinationBankInfo;
		this.currencyCode = currencyCode;
		this.postscript = postscript;
		this.outlierTransactionUuid = outLierTransactionUuid;
	}


	public Integer getAccountSide() {
		return accountSide;
	}

	public void setAccountSide(Integer accountSide) {
		this.accountSide = accountSide;
	}

	public String getSourceAccountName() {
		return sourceAccountName;
	}

	public void setSourceAccountName(String sourceAccountName) {
		this.sourceAccountName = sourceAccountName;
	}

	public String getSourceAccountNo() {
		return sourceAccountNo;
	}

	public void setSourceAccountNo(String sourceAccountNo) {
		this.sourceAccountNo = sourceAccountNo;
	}

	public String getSourceAccountAppendix() {
		return sourceAccountAppendix;
	}

	public void setSourceAccountAppendix(String sourceAccountAppendix) {
		this.sourceAccountAppendix = sourceAccountAppendix;
	}

	public String getSourceBankInfo() {
		return sourceBankInfo;
	}

	public void setSourceBankInfo(String sourceBankInfo) {
		this.sourceBankInfo = sourceBankInfo;
	}

	public String getDestinationAccountName() {
		return destinationAccountName;
	}

	public void setDestinationAccountName(String destinationAccountName) {
		this.destinationAccountName = destinationAccountName;
	}

	public String getDestinationAccountNo() {
		return destinationAccountNo;
	}

	public void setDestinationAccountNo(String destinationAccountNo) {
		this.destinationAccountNo = destinationAccountNo;
	}

	public String getDestinationAccountAppendix() {
		return destinationAccountAppendix;
	}

	public void setDestinationAccountAppendix(String destinationAccountAppendix) {
		this.destinationAccountAppendix = destinationAccountAppendix;
	}

	public String getDestinationBankInfo() {
		return destinationBankInfo;
	}

	public void setDestinationBankInfo(String destinationBankInfo) {
		this.destinationBankInfo = destinationBankInfo;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public String getPostscript() {
		return postscript;
	}

	public void setPostscript(String postscript) {
		this.postscript = postscript;
	}

	public String getOutlierTransactionUuid() {
		return outlierTransactionUuid;
	}

	public void setOutlierTransactionUuid(String outlierTransactionUuid) {
		this.outlierTransactionUuid = outlierTransactionUuid;
	}

	public BigDecimal getTransactionAmount() {
		return transactionAmount;
	}

	public void setTransactionAmount(BigDecimal transactionAmount) {
		this.transactionAmount = transactionAmount;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public Integer getGatewayType() {
		return gatewayType;
	}

	public void setGatewayType(Integer gatewayType) {
		this.gatewayType = gatewayType;
	}

	public String getGatewayRouterInfo() {
		return gatewayRouterInfo;
	}

	public void setGatewayRouterInfo(String gatewayRouterInfo) {
		this.gatewayRouterInfo = gatewayRouterInfo;
	}

}
