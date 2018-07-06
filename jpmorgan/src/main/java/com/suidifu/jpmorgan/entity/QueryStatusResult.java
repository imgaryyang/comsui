package com.suidifu.jpmorgan.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class QueryStatusResult {

	private String sourceMessageUuid;

	private String transactionUuid;

	private String tradeUuid;

	private String channelAccountName;

	private String channelAccountNo;

	private String destinationAccountName;

	private String destinationAccountNo;

	private BigDecimal transactionAmount;

	private BusinessStatus businessStatus;

	private String paymentChannelUuid;
	
	private Date communicationLastSentTime;

	private String businessResultMsg;
	
	private String businessResultCode;

	private Date businessSuccessTime;

	private String channelSequenceNo;

	private String batchUuid;

	private List<QueryStatusDetail> queryStatusDetails;

	public QueryStatusResult() {
		super();
	}

	public QueryStatusResult(String sourceMessageUuid, String transactionUuid,
			String tradeUuid, String destinationAccountName,
			String destinationAccountNo, BusinessStatus businessStatus) {
		super();
		this.sourceMessageUuid = sourceMessageUuid;
		this.transactionUuid = transactionUuid;
		this.tradeUuid = tradeUuid;
		this.destinationAccountName = destinationAccountName;
		this.destinationAccountNo = destinationAccountNo;
		this.businessStatus = businessStatus;
	}

	public String getTransactionUuid() {
		return transactionUuid;
	}

	public void setTransactionUuid(String transactionUuid) {
		this.transactionUuid = transactionUuid;
	}

	public String getTradeUuid() {
		return tradeUuid;
	}

	public void setTradeUuid(String tradeUuid) {
		this.tradeUuid = tradeUuid;
	}

	public String getChannelAccountName() {
		return channelAccountName;
	}

	public void setChannelAccountName(String channelAccountName) {
		this.channelAccountName = channelAccountName;
	}

	public String getChannelAccountNo() {
		return channelAccountNo;
	}

	public void setChannelAccountNo(String channelAccountNo) {
		this.channelAccountNo = channelAccountNo;
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

	public BigDecimal getTransactionAmount() {
		return transactionAmount;
	}

	public void setTransactionAmount(BigDecimal transactionAmount) {
		this.transactionAmount = transactionAmount;
	}

	public BusinessStatus getBusinessStatus() {
		return businessStatus;
	}

	public void setBusinessStatus(BusinessStatus businessStatus) {
		this.businessStatus = businessStatus;
	}

	public String getPaymentChannelUuid() {
		return paymentChannelUuid;
	}

	public void setPaymentChannelUuid(String paymentChannelUuid) {
		this.paymentChannelUuid = paymentChannelUuid;
	}

	public String getBusinessResultMsg() {
		return businessResultMsg;
	}

	public void setBusinessResultMsg(String businessResultMsg) {
		this.businessResultMsg = businessResultMsg;
	}

	public String getBusinessResultCode() {
		return businessResultCode;
	}

	public void setBusinessResultCode(String businessResultCode) {
		this.businessResultCode = businessResultCode;
	}

	public Date getBusinessSuccessTime() {
		return businessSuccessTime;
	}

	public void setBusinessSuccessTime(Date businessSuccessTime) {
		this.businessSuccessTime = businessSuccessTime;
	}

	public String getChannelSequenceNo() {
		return channelSequenceNo;
	}

	public void setChannelSequenceNo(String channelSequenceNo) {
		this.channelSequenceNo = channelSequenceNo;
	}

	public String getSourceMessageUuid() {
		return sourceMessageUuid;
	}

	public void setSourceMessageUuid(String sourceMessageUuid) {
		this.sourceMessageUuid = sourceMessageUuid;
	}

	public Date getCommunicationLastSentTime() {
		return communicationLastSentTime;
	}

	public String getBatchUuid() {
		return batchUuid;
	}

	public void setBatchUuid(String batchUuid) {
		this.batchUuid = batchUuid;
	}

	public void setCommunicationLastSentTime(Date communicationLastSentTime) {
		this.communicationLastSentTime = communicationLastSentTime;
	}

	public List<QueryStatusDetail> getQueryStatusDetails() {
		return queryStatusDetails;
	}

	public void setQueryStatusDetails(List<QueryStatusDetail> queryStatusDetails) {
		this.queryStatusDetails = queryStatusDetails;
	}
}
