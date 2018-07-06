package com.suidifu.jpmorgan.entity;

import java.math.BigDecimal;
import java.util.Date;

public class GatewaySlot {

	private String slotUuid;

	private String workerUuid;

	private GatewayType gatewayType;

	private String paymentChannelUuid;
	
	private String gatewayRouterInfo;

	private Date transactionBeginTime;

	private Date transactionEndTime;

	private Date effectiveAbsoluteTime;

	private BigDecimal transactionAmount;

	private CommunicationStatus communicationStatus;// inqueue-processing-succ-failed-abandon

	private Date communicationStartTime;

	private Date communicationEndTime;

	private Date communicationLastSentTime;

	private BusinessStatus businessStatus;// inqueue-processing-succ-failed-abandon
	
	private Date businessSuccessTime;
	
	private String channelSequenceNo;

	private String businessResultMsg;

	public GatewaySlot() {
		super();
	}

	public GatewaySlot(String slotUuid, GatewayType gatewayType,
			BigDecimal transactionAmount,
			CommunicationStatus communicationStatus,
			Date communicationStartTime, Date communicationEndTime,
			Date communicationLastSentTime, BusinessStatus businessStatus,
			Date businessSuccessTime, String businessResultMsg, 
			String channelSequenceNo) {
		super();
		this.slotUuid = slotUuid;
		this.gatewayType = gatewayType;
		this.transactionAmount = transactionAmount;
		this.communicationStatus = communicationStatus;
		this.communicationStartTime = communicationStartTime;
		this.communicationEndTime = communicationEndTime;
		this.communicationLastSentTime = communicationLastSentTime;
		this.businessStatus = businessStatus;
		this.businessSuccessTime = businessSuccessTime;
		this.businessResultMsg = businessResultMsg;
		this.channelSequenceNo = channelSequenceNo;
	}

	public GatewaySlot(String slotUuid, String workerUuid,
			GatewayType gatewayType, String paymentChannelUuid,
			String gatewayRouterInfo, Date transactionBeginTime,
			Date transactionEndTime, Date effectiveAbsoluteTime,
			BigDecimal transactionAmount,
			CommunicationStatus communicationStatus,
			Date communicationStartTime, Date communicationEndTime,
			Date communicationLastSentTime, BusinessStatus businessStatus,
			String businessResultMsg, String channelSequenceNo) {
		super();
		this.slotUuid = slotUuid;
		this.workerUuid = workerUuid;
		this.gatewayType = gatewayType;
		this.paymentChannelUuid = paymentChannelUuid;
		this.gatewayRouterInfo = gatewayRouterInfo;
		this.transactionBeginTime = transactionBeginTime;
		this.transactionEndTime = transactionEndTime;
		this.effectiveAbsoluteTime = effectiveAbsoluteTime;
		this.transactionAmount = transactionAmount;
		this.communicationStatus = communicationStatus;
		this.communicationStartTime = communicationStartTime;
		this.communicationEndTime = communicationEndTime;
		this.communicationLastSentTime = communicationLastSentTime;
		this.businessStatus = businessStatus;
		this.businessResultMsg = businessResultMsg;
		this.channelSequenceNo = channelSequenceNo;
	}

	public String getSlotUuid() {
		return slotUuid;
	}

	public void setSlotUuid(String slotUuid) {
		this.slotUuid = slotUuid;
	}

	public String getWorkerUuid() {
		return workerUuid;
	}

	public void setWorkerUuid(String workerUuid) {
		this.workerUuid = workerUuid;
	}

	public Integer getGatewayType() {
		return null == gatewayType ? null : gatewayType.ordinal();
	}

	public void setGatewayType(Integer gatewayType) {
		this.gatewayType = GatewayType.fromOrdinal(gatewayType);
	}

	public String getPaymentChannelUuid() {
		return paymentChannelUuid;
	}

	public void setPaymentChannelUuid(String paymentChannelUuid) {
		this.paymentChannelUuid = paymentChannelUuid;
	}

	public Date getTransactionBeginTime() {
		return transactionBeginTime;
	}

	public void setTransactionBeginTime(Date transactionBeginTime) {
		this.transactionBeginTime = transactionBeginTime;
	}

	public Date getTransactionEndTime() {
		return transactionEndTime;
	}

	public void setTransactionEndTime(Date transactionEndTime) {
		this.transactionEndTime = transactionEndTime;
	}

	public Date getEffectiveAbsoluteTime() {
		return effectiveAbsoluteTime;
	}

	public void setEffectiveAbsoluteTime(Date effectiveAbsoluteTime) {
		this.effectiveAbsoluteTime = effectiveAbsoluteTime;
	}

	public BigDecimal getTransactionAmount() {
		return transactionAmount;
	}

	public void setTransactionAmount(BigDecimal transactionAmount) {
		this.transactionAmount = transactionAmount;
	}

	public Integer getCommunicationStatus() {
		return communicationStatus.ordinal();
	}

	public void setCommunicationStatus(Integer communicationStatus) {
		this.communicationStatus = CommunicationStatus.fromOrdinal(communicationStatus);
	}

	public Date getCommunicationStartTime() {
		return communicationStartTime;
	}

	public void setCommunicationStartTime(Date communicationStartTime) {
		this.communicationStartTime = communicationStartTime;
	}

	public Date getCommunicationEndTime() {
		return communicationEndTime;
	}

	public void setCommunicationEndTime(Date communicationEndTime) {
		this.communicationEndTime = communicationEndTime;
	}

	public Date getCommunicationLastSentTime() {
		return communicationLastSentTime;
	}

	public void setCommunicationLastSentTime(Date communicationLastSentTime) {
		this.communicationLastSentTime = communicationLastSentTime;
	}

	public Integer getBusinessStatus() {
		return businessStatus.ordinal();
	}

	public void setBusinessStatus(Integer businessStatus) {
		this.businessStatus = BusinessStatus.fromOrdinal(businessStatus);
	}

	public String getBusinessResultMsg() {
		return businessResultMsg;
	}

	public void setBusinessResultMsg(String businessResultMsg) {
		this.businessResultMsg = businessResultMsg;
	}

	public String getGatewayRouterInfo() {
		return gatewayRouterInfo;
	}
	
	public void setGatewayRouterInfo(String gatewayRouterInfo) {
		this.gatewayRouterInfo = gatewayRouterInfo;
	}

	public String getChannelSequenceNo() {
		return channelSequenceNo;
	}

	public void setChannelSequenceNo(String channelSequenceNo) {
		this.channelSequenceNo = channelSequenceNo;
	}

	public Date getBusinessSuccessTime() {
		return businessSuccessTime;
	}

	public void setBusinessSuccessTime(Date businessSuccessTime) {
		this.businessSuccessTime = businessSuccessTime;
	}

	public boolean isBusinessProcessing() {
		return BusinessStatus.Processing.ordinal() == getBusinessStatus() || BusinessStatus.OppositeProcessing.ordinal() == getBusinessStatus();
	}

	public boolean canCallback() {
		return BusinessStatus.Success.equals(this.businessStatus) || BusinessStatus.Failed.equals(this.businessStatus) || BusinessStatus.Abandon.equals(this.businessStatus);
	}
}
