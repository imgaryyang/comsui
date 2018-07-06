package com.suidifu.jpmorgan.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "payment_order_log")
public class PaymentOrderLog {

	@Id
	@GeneratedValue
	private Long id;

	private String uuid;

	@Enumerated(EnumType.ORDINAL)
	private AccountSide accountSide;

	private String sourceAccountName;

	private String sourceAccountNo;

	@Column(columnDefinition = "text")
	private String sourceAccountAppendix;

	@Column(columnDefinition = "text")
	private String sourceBankInfo;

	private String destinationAccountName;

	private String destinationAccountNo;

	@Column(columnDefinition = "text")
	private String destinationBankInfo;

	@Column(columnDefinition = "text")
	private String destinationAccountAppendix;

	private BigDecimal transactionAmount;

	private String currencyCode;

	private String postscript;

	private String outlierTransactionUuid;

	@Column(columnDefinition = "text")
	private String gatewayRouterInfo;

	@Enumerated(EnumType.ORDINAL)
	private GatewayType gatewayType;

	private String sourceMessageUuid;

	private String sourceMessageIp;

	private Date sourceMessageTime;

	@Enumerated(EnumType.ORDINAL)
	private BusinessStatus businessStatus;// processing-succ-failed-abandon
	
	private String channelSequenceNo;
	
	private String businessResultMsg;
	
	private Date businessStatusLastUpdateTime;
	
	private Date businessSuccessTime;

	@Enumerated(EnumType.ORDINAL)
	private CommunicationStatus communicationStatus;// inqueue-processing-succ-failed

	private Date communicationStartTime;

	private Date communicationEndTime;

	private Date communicationLastSentTime;

	@Enumerated(EnumType.ORDINAL)
	private OccupyStatus fstOccupyStatus;

	@Enumerated(EnumType.ORDINAL)
	private OccupyCommunicationStatus fstCommunicationStatus;// ready－processing-sending-done

	private Date fstOccupiedTime;

	private Date fstOccupiedSentTime;

	private Date fstOccupiedFeedBackTime;

	private Date fstOccupiedForceStopTime;

	private String fstOccuppiedWorkerUuid;

	private String fstOccupiedWorkerIp;

	private String fstOccupiedMessageRecordUuid;// TODO

	private String fstOccupiedResultRecordUuid;

	@Enumerated(EnumType.ORDINAL)
	private OccupyStatus sndOccupyStatus;// free-occupied

	@Enumerated(EnumType.ORDINAL)
	private OccupyCommunicationStatus sndCommunicationStatus;// ready－processing-sending-done

	private Date sndOccupiedTime;

	private Date sndOccupiedSentTime;

	private Date sndOccupiedFeedBackTime;

	private Date sndOccupiedForceStopTime;

	private String sndOccuppiedWorkerUuid;

	private String sndOccupiedWorkerIp;

	private String sndOccupiedMessageRecordUuid;

	private String sndOccupiedResultRecordUuid;

	@Enumerated(EnumType.ORDINAL)
	private OccupyStatus trdOccupyStatus;// free-occupied

	@Enumerated(EnumType.ORDINAL)
	private OccupyCommunicationStatus trdCommunicationStatus;// ready－processing-sending-done

	private Date trdOccupiedTime;

	private Date trdOccupiedSentTime;

	private Date trdOccupiedFeedBackTime;

	private Date trdOccupiedForceStopTime;

	private String trdOccuppiedWorkerUuid;

	private String trdOccupiedWorkerIp;

	private String trdOccupiedMessageRecordUuid;

	private String trdOccupiedResultRecordUuid;

	private String accessVersion;

	private Date dateFieldOne;

	private Date dateFieldTwo;

	private Date dateFieldThree;

	private Long longFieldOne;

	private Long longFieldTwo;

	private Long longFieldThree;

	private String stringFieldOne;

	private String stringFieldTwo;

	private String stringFieldThree;

	private BigDecimal decimalFieldOne;

	private BigDecimal decimalFieldTwo;

	private BigDecimal decimalFieldThree;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public AccountSide getAccountSide() {
		return accountSide;
	}

	public void setAccountSide(AccountSide accountSide) {
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

	public String getDestinationBankInfo() {
		return destinationBankInfo;
	}

	public void setDestinationBankInfo(String destinationBankInfo) {
		this.destinationBankInfo = destinationBankInfo;
	}

	public String getDestinationAccountAppendix() {
		return destinationAccountAppendix;
	}

	public void setDestinationAccountAppendix(String destinationAccountAppendix) {
		this.destinationAccountAppendix = destinationAccountAppendix;
	}

	public BigDecimal getTransactionAmount() {
		return transactionAmount;
	}

	public void setTransactionAmount(BigDecimal transactionAmount) {
		this.transactionAmount = transactionAmount;
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

	public String getGatewayRouterInfo() {
		return gatewayRouterInfo;
	}

	public void setGatewayRouterInfo(String gatewayRouterInfo) {
		this.gatewayRouterInfo = gatewayRouterInfo;
	}

	public GatewayType getGatewayType() {
		return gatewayType;
	}

	public void setGatewayType(GatewayType gatewayType) {
		this.gatewayType = gatewayType;
	}

	public String getSourceMessageUuid() {
		return sourceMessageUuid;
	}

	public void setSourceMessageUuid(String sourceMessageUuid) {
		this.sourceMessageUuid = sourceMessageUuid;
	}

	public String getSourceMessageIp() {
		return sourceMessageIp;
	}

	public void setSourceMessageIp(String sourceMessageIp) {
		this.sourceMessageIp = sourceMessageIp;
	}

	public Date getSourceMessageTime() {
		return sourceMessageTime;
	}

	public void setSourceMessageTime(Date sourceMessageTime) {
		this.sourceMessageTime = sourceMessageTime;
	}

	public BusinessStatus getBusinessStatus() {
		return businessStatus;
	}

	public void setBusinessStatus(BusinessStatus businessStatus) {
		this.businessStatus = businessStatus;
	}

	public String getChannelSequenceNo() {
		return channelSequenceNo;
	}

	public void setChannelSequenceNo(String channelSequenceNo) {
		this.channelSequenceNo = channelSequenceNo;
	}

	public String getBusinessResultMsg() {
		return businessResultMsg;
	}

	public void setBusinessResultMsg(String businessResultMsg) {
		this.businessResultMsg = businessResultMsg;
	}

	public Date getBusinessStatusLastUpdateTime() {
		return businessStatusLastUpdateTime;
	}

	public void setBusinessStatusLastUpdateTime(Date businessStatusLastUpdateTime) {
		this.businessStatusLastUpdateTime = businessStatusLastUpdateTime;
	}

	public Date getBusinessSuccessTime() {
		return businessSuccessTime;
	}

	public void setBusinessSuccessTime(Date businessSuccessTime) {
		this.businessSuccessTime = businessSuccessTime;
	}

	public CommunicationStatus getCommunicationStatus() {
		return communicationStatus;
	}

	public void setCommunicationStatus(CommunicationStatus communicationStatus) {
		this.communicationStatus = communicationStatus;
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

	public OccupyStatus getFstOccupyStatus() {
		return fstOccupyStatus;
	}

	public void setFstOccupyStatus(OccupyStatus fstOccupyStatus) {
		this.fstOccupyStatus = fstOccupyStatus;
	}

	public OccupyCommunicationStatus getFstCommunicationStatus() {
		return fstCommunicationStatus;
	}

	public void setFstCommunicationStatus(
			OccupyCommunicationStatus fstCommunicationStatus) {
		this.fstCommunicationStatus = fstCommunicationStatus;
	}

	public Date getFstOccupiedTime() {
		return fstOccupiedTime;
	}

	public void setFstOccupiedTime(Date fstOccupiedTime) {
		this.fstOccupiedTime = fstOccupiedTime;
	}

	public Date getFstOccupiedSentTime() {
		return fstOccupiedSentTime;
	}

	public void setFstOccupiedSentTime(Date fstOccupiedSentTime) {
		this.fstOccupiedSentTime = fstOccupiedSentTime;
	}

	public Date getFstOccupiedFeedBackTime() {
		return fstOccupiedFeedBackTime;
	}

	public void setFstOccupiedFeedBackTime(Date fstOccupiedFeedBackTime) {
		this.fstOccupiedFeedBackTime = fstOccupiedFeedBackTime;
	}

	public Date getFstOccupiedForceStopTime() {
		return fstOccupiedForceStopTime;
	}

	public void setFstOccupiedForceStopTime(Date fstOccupiedForceStopTime) {
		this.fstOccupiedForceStopTime = fstOccupiedForceStopTime;
	}

	public String getFstOccuppiedWorkerUuid() {
		return fstOccuppiedWorkerUuid;
	}

	public void setFstOccuppiedWorkerUuid(String fstOccuppiedWorkerUuid) {
		this.fstOccuppiedWorkerUuid = fstOccuppiedWorkerUuid;
	}

	public String getFstOccupiedWorkerIp() {
		return fstOccupiedWorkerIp;
	}

	public void setFstOccupiedWorkerIp(String fstOccupiedWorkerIp) {
		this.fstOccupiedWorkerIp = fstOccupiedWorkerIp;
	}

	public String getFstOccupiedMessageRecordUuid() {
		return fstOccupiedMessageRecordUuid;
	}

	public void setFstOccupiedMessageRecordUuid(String fstOccupiedMessageRecordUuid) {
		this.fstOccupiedMessageRecordUuid = fstOccupiedMessageRecordUuid;
	}

	public String getFstOccupiedResultRecordUuid() {
		return fstOccupiedResultRecordUuid;
	}

	public void setFstOccupiedResultRecordUuid(String fstOccupiedResultRecordUuid) {
		this.fstOccupiedResultRecordUuid = fstOccupiedResultRecordUuid;
	}

	public OccupyStatus getSndOccupyStatus() {
		return sndOccupyStatus;
	}

	public void setSndOccupyStatus(OccupyStatus sndOccupyStatus) {
		this.sndOccupyStatus = sndOccupyStatus;
	}

	public OccupyCommunicationStatus getSndCommunicationStatus() {
		return sndCommunicationStatus;
	}

	public void setSndCommunicationStatus(
			OccupyCommunicationStatus sndCommunicationStatus) {
		this.sndCommunicationStatus = sndCommunicationStatus;
	}

	public Date getSndOccupiedTime() {
		return sndOccupiedTime;
	}

	public void setSndOccupiedTime(Date sndOccupiedTime) {
		this.sndOccupiedTime = sndOccupiedTime;
	}

	public Date getSndOccupiedSentTime() {
		return sndOccupiedSentTime;
	}

	public void setSndOccupiedSentTime(Date sndOccupiedSentTime) {
		this.sndOccupiedSentTime = sndOccupiedSentTime;
	}

	public Date getSndOccupiedFeedBackTime() {
		return sndOccupiedFeedBackTime;
	}

	public void setSndOccupiedFeedBackTime(Date sndOccupiedFeedBackTime) {
		this.sndOccupiedFeedBackTime = sndOccupiedFeedBackTime;
	}

	public Date getSndOccupiedForceStopTime() {
		return sndOccupiedForceStopTime;
	}

	public void setSndOccupiedForceStopTime(Date sndOccupiedForceStopTime) {
		this.sndOccupiedForceStopTime = sndOccupiedForceStopTime;
	}

	public String getSndOccuppiedWorkerUuid() {
		return sndOccuppiedWorkerUuid;
	}

	public void setSndOccuppiedWorkerUuid(String sndOccuppiedWorkerUuid) {
		this.sndOccuppiedWorkerUuid = sndOccuppiedWorkerUuid;
	}

	public String getSndOccupiedWorkerIp() {
		return sndOccupiedWorkerIp;
	}

	public void setSndOccupiedWorkerIp(String sndOccupiedWorkerIp) {
		this.sndOccupiedWorkerIp = sndOccupiedWorkerIp;
	}

	public String getSndOccupiedMessageRecordUuid() {
		return sndOccupiedMessageRecordUuid;
	}

	public void setSndOccupiedMessageRecordUuid(String sndOccupiedMessageRecordUuid) {
		this.sndOccupiedMessageRecordUuid = sndOccupiedMessageRecordUuid;
	}

	public String getSndOccupiedResultRecordUuid() {
		return sndOccupiedResultRecordUuid;
	}

	public void setSndOccupiedResultRecordUuid(String sndOccupiedResultRecordUuid) {
		this.sndOccupiedResultRecordUuid = sndOccupiedResultRecordUuid;
	}

	public OccupyStatus getTrdOccupyStatus() {
		return trdOccupyStatus;
	}

	public void setTrdOccupyStatus(OccupyStatus trdOccupyStatus) {
		this.trdOccupyStatus = trdOccupyStatus;
	}

	public OccupyCommunicationStatus getTrdCommunicationStatus() {
		return trdCommunicationStatus;
	}

	public void setTrdCommunicationStatus(
			OccupyCommunicationStatus trdCommunicationStatus) {
		this.trdCommunicationStatus = trdCommunicationStatus;
	}

	public Date getTrdOccupiedTime() {
		return trdOccupiedTime;
	}

	public void setTrdOccupiedTime(Date trdOccupiedTime) {
		this.trdOccupiedTime = trdOccupiedTime;
	}

	public Date getTrdOccupiedSentTime() {
		return trdOccupiedSentTime;
	}

	public void setTrdOccupiedSentTime(Date trdOccupiedSentTime) {
		this.trdOccupiedSentTime = trdOccupiedSentTime;
	}

	public Date getTrdOccupiedFeedBackTime() {
		return trdOccupiedFeedBackTime;
	}

	public void setTrdOccupiedFeedBackTime(Date trdOccupiedFeedBackTime) {
		this.trdOccupiedFeedBackTime = trdOccupiedFeedBackTime;
	}

	public Date getTrdOccupiedForceStopTime() {
		return trdOccupiedForceStopTime;
	}

	public void setTrdOccupiedForceStopTime(Date trdOccupiedForceStopTime) {
		this.trdOccupiedForceStopTime = trdOccupiedForceStopTime;
	}

	public String getTrdOccuppiedWorkerUuid() {
		return trdOccuppiedWorkerUuid;
	}

	public void setTrdOccuppiedWorkerUuid(String trdOccuppiedWorkerUuid) {
		this.trdOccuppiedWorkerUuid = trdOccuppiedWorkerUuid;
	}

	public String getTrdOccupiedWorkerIp() {
		return trdOccupiedWorkerIp;
	}

	public void setTrdOccupiedWorkerIp(String trdOccupiedWorkerIp) {
		this.trdOccupiedWorkerIp = trdOccupiedWorkerIp;
	}

	public String getTrdOccupiedMessageRecordUuid() {
		return trdOccupiedMessageRecordUuid;
	}

	public void setTrdOccupiedMessageRecordUuid(String trdOccupiedMessageRecordUuid) {
		this.trdOccupiedMessageRecordUuid = trdOccupiedMessageRecordUuid;
	}

	public String getTrdOccupiedResultRecordUuid() {
		return trdOccupiedResultRecordUuid;
	}

	public void setTrdOccupiedResultRecordUuid(String trdOccupiedResultRecordUuid) {
		this.trdOccupiedResultRecordUuid = trdOccupiedResultRecordUuid;
	}

	public String getAccessVersion() {
		return accessVersion;
	}

	public void setAccessVersion(String accessVersion) {
		this.accessVersion = accessVersion;
	}
	
}
