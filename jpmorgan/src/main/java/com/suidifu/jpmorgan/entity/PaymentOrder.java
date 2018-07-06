package com.suidifu.jpmorgan.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.alibaba.fastjson.JSON;
import com.demo2do.core.utils.StringUtils;

/**
 * 
 * @author zjm
 *
 */
@Entity
@Table(name = "payment_order")
public class PaymentOrder {

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

	private Long longFieldOne;//20170423 for worker no

	private Long longFieldTwo;

	private Long longFieldThree;

	private String stringFieldOne;//20180101 for inner callback url

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

	public Integer getAccountSide() {
		return accountSide.ordinal();
	}

	public void setAccountSide(Integer accountSide) {

		this.accountSide = AccountSide.fromOrdinal(accountSide);
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
	
	@SuppressWarnings("unchecked")
	public Map<String, String> getGatewayRouterInfoConfig() {
		try {
			if (StringUtils.isEmpty(this.gatewayRouterInfo)) {
				return new HashMap<String, String>();
			}
			return (Map<String, String>) JSON.parse(this.gatewayRouterInfo);
		} catch (Exception e) {
			e.printStackTrace();
			return new HashMap<String, String>();
		}
	}

	public void setGatewayRouterInfo(String gatewayRouterInfo) {
		this.gatewayRouterInfo = gatewayRouterInfo;
	}

	public Integer getGatewayType() {
		return null == gatewayType ? null : gatewayType.ordinal();
	}

	public void setGatewayType(Integer gatewayType) {
		this.gatewayType = GatewayType.fromOrdinal(gatewayType);
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

	public Integer getBusinessStatus() {
		return businessStatus.ordinal();
	}

	public void setBusinessStatus(Integer businessStatus) {
		this.businessStatus = BusinessStatus.fromOrdinal(businessStatus);
	}

	public String getChannelSequenceNo() {
		return channelSequenceNo;
	}

	public void setChannelSequenceNo(String channelSequenceNo) {
		this.channelSequenceNo = channelSequenceNo;
	}

	public Date getBusinessStatusLastUpdateTime() {
		return businessStatusLastUpdateTime;
	}

	public void setBusinessStatusLastUpdateTime(
			Date businessStatusLastUpdateTime) {
		this.businessStatusLastUpdateTime = businessStatusLastUpdateTime;
	}

	public String getBusinessResultMsg() {
		return businessResultMsg;
	}

	public void setBusinessResultMsg(String businessResultMsg) {
		this.businessResultMsg = businessResultMsg;
	}

	public Date getBusinessSuccessTime() {
		return businessSuccessTime;
	}

	public void setBusinessSuccessTime(Date businessSuccessTime) {
		this.businessSuccessTime = businessSuccessTime;
	}

	public Integer getCommunicationStatus() {
		return communicationStatus.ordinal();
	}

	public void setCommunicationStatus(Integer communicationStatus) {
		this.communicationStatus = CommunicationStatus
				.fromOrdinal(communicationStatus);
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

	public Integer getFstOccupyStatus() {
		return fstOccupyStatus.ordinal();
	}

	public void setFstOccupyStatus(Integer fstOccupyStatus) {
		this.fstOccupyStatus = OccupyStatus.fromOrdinal(fstOccupyStatus);
	}

	public Integer getFstCommunicationStatus() {
		return fstCommunicationStatus.ordinal();
	}

	public void setFstCommunicationStatus(Integer fstCommunicationStatus) {
		this.fstCommunicationStatus = OccupyCommunicationStatus
				.fromOrdinal(fstCommunicationStatus);
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

	public void setFstOccupiedMessageRecordUuid(
			String fstOccupiedMessageRecordUuid) {
		this.fstOccupiedMessageRecordUuid = fstOccupiedMessageRecordUuid;
	}

	public String getFstOccupiedResultRecordUuid() {
		return fstOccupiedResultRecordUuid;
	}

	public void setFstOccupiedResultRecordUuid(
			String fstOccupiedResultRecordUuid) {
		this.fstOccupiedResultRecordUuid = fstOccupiedResultRecordUuid;
	}

	public Integer getSndOccupyStatus() {
		return sndOccupyStatus.ordinal();
	}

	public void setSndOccupyStatus(Integer sndOccupyStatus) {
		this.sndOccupyStatus = OccupyStatus.fromOrdinal(sndOccupyStatus);
	}

	public Integer getSndCommunicationStatus() {
		return sndCommunicationStatus.ordinal();
	}

	public void setSndCommunicationStatus(Integer sndCommunicationStatus) {
		this.sndCommunicationStatus = OccupyCommunicationStatus
				.fromOrdinal(sndCommunicationStatus);
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

	public void setSndOccupiedMessageRecordUuid(
			String sndOccupiedMessageRecordUuid) {
		this.sndOccupiedMessageRecordUuid = sndOccupiedMessageRecordUuid;
	}

	public String getSndOccupiedResultRecordUuid() {
		return sndOccupiedResultRecordUuid;
	}

	public void setSndOccupiedResultRecordUuid(
			String sndOccupiedResultRecordUuid) {
		this.sndOccupiedResultRecordUuid = sndOccupiedResultRecordUuid;
	}

	public Integer getTrdOccupyStatus() {
		return trdOccupyStatus.ordinal();
	}

	public void setTrdOccupyStatus(Integer trdOccupyStatus) {
		this.trdOccupyStatus = OccupyStatus.fromOrdinal(trdOccupyStatus);
	}

	public Integer getTrdCommunicationStatus() {
		return trdCommunicationStatus.ordinal();
	}

	public void setTrdCommunicationStatus(Integer trdCommunicationStatus) {
		this.trdCommunicationStatus = OccupyCommunicationStatus
				.fromOrdinal(trdCommunicationStatus);
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

	public void setTrdOccupiedMessageRecordUuid(
			String trdOccupiedMessageRecordUuid) {
		this.trdOccupiedMessageRecordUuid = trdOccupiedMessageRecordUuid;
	}

	public String getTrdOccupiedResultRecordUuid() {
		return trdOccupiedResultRecordUuid;
	}

	public void setTrdOccupiedResultRecordUuid(
			String trdOccupiedResultRecordUuid) {
		this.trdOccupiedResultRecordUuid = trdOccupiedResultRecordUuid;
	}

	public String getAccessVersion() {
		return accessVersion;
	}

	public void setAccessVersion(String accessVersion) {
		this.accessVersion = accessVersion;
	}

	public Long getLongFieldOne() {
		return longFieldOne;
	}

	public void setLongFieldOne(Long longFieldOne) {
		this.longFieldOne = longFieldOne;
	}

	public String getStringFieldOne() {
		return stringFieldOne;
	}

	public void setStringFieldOne(String stringFieldOne) {
		this.stringFieldOne = stringFieldOne;
	}

	public int nextReadySlot() {
		if (OccupyStatus.Free.equals(this.fstOccupyStatus)
				&& OccupyCommunicationStatus.Ready
						.equals(this.fstCommunicationStatus)) {
			return 1;
		}

		if (OccupyStatus.Occupied.equals(this.fstOccupyStatus)
				&& OccupyCommunicationStatus.Done
						.equals(this.fstCommunicationStatus)
				&& OccupyStatus.Free.equals(this.sndOccupyStatus)
				&& OccupyCommunicationStatus.Ready
						.equals(this.sndCommunicationStatus)) {
			return 2;
		}

		if (OccupyStatus.Occupied.equals(this.sndOccupyStatus)// TODO
				&& OccupyCommunicationStatus.Done
						.equals(this.sndCommunicationStatus)
				&& OccupyStatus.Free.equals(this.trdOccupyStatus)
				&& OccupyCommunicationStatus.Ready
						.equals(this.trdCommunicationStatus)) {
			return 3;
		}
		return 0;
	}

	public int currentOccupiedSlot() {
		if (OccupyStatus.Occupied.equals(this.fstOccupyStatus)
				&& OccupyStatus.Free.equals(this.sndOccupyStatus)
				&& OccupyStatus.Free.equals(this.trdOccupyStatus)) {
			return 1;
		}

		if (OccupyStatus.Occupied.equals(this.fstOccupyStatus)
				&& OccupyStatus.Occupied.equals(this.sndOccupyStatus)
				&& OccupyStatus.Free.equals(this.trdOccupyStatus)) {
			return 2;
		}

		if (OccupyStatus.Occupied.equals(this.fstOccupyStatus)
				&& OccupyStatus.Occupied.equals(this.sndOccupyStatus)
				&& OccupyStatus.Occupied.equals(this.trdOccupyStatus)) {
			return 3;
		}
		return 0;
	}

	public PaymentOrder() {
		super();
	}

	public PaymentOrder(String uuid, AccountSide accountSide,
			String sourceAccountName, String sourceAccountNo,
			String sourceBankInfo, String destinationAccountName,
			String destinationAccountNo, String destinationBankInfo,
			BigDecimal transactionAmount, String currencyCode,
			String postscript, String outlierTransactionUuid,
			GatewayType gatewayType, BusinessStatus businessStatus,
			CommunicationStatus communicationStatus,
			OccupyStatus fstOccupyStatus,
			OccupyCommunicationStatus fstCommunicationStatus,
			OccupyStatus sndOccupyStatus,
			OccupyCommunicationStatus sndCommunicationStatus,
			OccupyStatus trdOccupyStatus,
			OccupyCommunicationStatus trdCommunicationStatus,
			String accessVersion, String sourceAccountAppendix,
			String destinationAccountAppendix, String gatewayRouterInfo) {
		super();
		this.uuid = uuid;
		this.accountSide = accountSide;
		this.sourceAccountName = sourceAccountName;
		this.sourceAccountNo = sourceAccountNo;
		this.sourceBankInfo = sourceBankInfo;
		this.destinationAccountName = destinationAccountName;
		this.destinationAccountNo = destinationAccountNo;
		this.destinationBankInfo = destinationBankInfo;
		this.transactionAmount = transactionAmount;
		this.currencyCode = currencyCode;
		this.postscript = postscript;
		this.outlierTransactionUuid = outlierTransactionUuid;
		this.gatewayType = gatewayType;
		this.businessStatus = businessStatus;
		this.communicationStatus = communicationStatus;
		this.fstOccupyStatus = fstOccupyStatus;
		this.fstCommunicationStatus = fstCommunicationStatus;
		this.sndOccupyStatus = sndOccupyStatus;
		this.sndCommunicationStatus = sndCommunicationStatus;
		this.trdOccupyStatus = trdOccupyStatus;
		this.trdCommunicationStatus = trdCommunicationStatus;
		this.accessVersion = accessVersion;
		this.sourceAccountAppendix = sourceAccountAppendix;
		this.destinationAccountAppendix = destinationAccountAppendix;
		this.gatewayRouterInfo = gatewayRouterInfo;
	}

	public void init(DistributeWorkingContext workingContext) {
		
		this.setAccessVersion(UUID.randomUUID().toString());
		this.setSourceMessageTime(new Date());
		
		this.setBusinessStatus(BusinessStatus.Processing.ordinal());
		this.setCommunicationStatus(CommunicationStatus.Inqueue.ordinal());

		this.setFstOccupyStatus(OccupyStatus.Free.ordinal());
		this.setFstCommunicationStatus(OccupyCommunicationStatus.Ready
				.ordinal());

		this.setSndOccupyStatus(OccupyStatus.Free.ordinal());
		this.setSndCommunicationStatus(OccupyCommunicationStatus.Ready
				.ordinal());

		this.setTrdOccupyStatus(OccupyStatus.Free.ordinal());
		this.setTrdCommunicationStatus(OccupyCommunicationStatus.Ready
				.ordinal());
		
		this.setStringFieldOne(workingContext.getInnerCallbackInfo());
	}
	
	public GatewaySlot transferToGatewaySlot() {
		if(null == this.businessSuccessTime && BusinessStatus.Success.equals(this.businessStatus)) {
			this.businessSuccessTime = this.businessStatusLastUpdateTime;
		}
		return new GatewaySlot(uuid, gatewayType, transactionAmount, communicationStatus, communicationStartTime, communicationEndTime, communicationLastSentTime, businessStatus, businessSuccessTime, businessResultMsg, channelSequenceNo);
	}
	
/*	public PaymentOrderLog transferToPaymentOrderLog() {
		PaymentOrderLog paymentOrderLog = new PaymentOrderLog();
		paymentOrderLog.setUuid(uuid);
		paymentOrderLog.setAccountSide(accountSide);
		paymentOrderLog.setSourceAccountName(sourceAccountName);
		paymentOrderLog.setSourceAccountNo(sourceAccountNo);
		paymentOrderLog.setSourceAccountAppendix(sourceAccountAppendix);
		paymentOrderLog.setSourceBankInfo(sourceBankInfo);
		paymentOrderLog.setDestinationAccountName(destinationAccountName);
		paymentOrderLog.setDestinationAccountNo(destinationAccountNo);
		paymentOrderLog.setDestinationBankInfo(destinationBankInfo);
		paymentOrderLog.setDestinationAccountAppendix(destinationAccountAppendix);
		paymentOrderLog.setTransactionAmount(transactionAmount);
		paymentOrderLog.setCurrencyCode(currencyCode);
		paymentOrderLog.setPostscript(postscript);
		paymentOrderLog.setOutlierTransactionUuid(outlierTransactionUuid);
		paymentOrderLog.setGatewayRouterInfo(gatewayRouterInfo);
		paymentOrderLog.setGatewayType(gatewayType);
		paymentOrderLog.setSourceMessageUuid(sourceMessageUuid);
		paymentOrderLog.setSourceMessageIp(sourceMessageIp);
		paymentOrderLog.setSourceMessageTime(sourceMessageTime);
		paymentOrderLog.setBusinessStatus(businessStatus);
		paymentOrderLog.setChannelSequenceNo(channelSequenceNo);
		paymentOrderLog.setBusinessResultMsg(businessResultMsg);
		paymentOrderLog.setBusinessStatusLastUpdateTime(businessStatusLastUpdateTime);
		paymentOrderLog.setBusinessSuccessTime(businessSuccessTime);
		paymentOrderLog.setCommunicationStatus(communicationStatus);
		paymentOrderLog.setCommunicationStartTime(communicationStartTime);
		paymentOrderLog.setCommunicationEndTime(communicationEndTime);
		paymentOrderLog.setCommunicationLastSentTime(communicationLastSentTime);
		
		paymentOrderLog.setFstOccupyStatus(fstOccupyStatus);
		paymentOrderLog.setFstCommunicationStatus(fstCommunicationStatus);
		paymentOrderLog.setFstOccupiedTime(fstOccupiedTime);
		paymentOrderLog.setFstOccupiedSentTime(fstOccupiedSentTime);
		paymentOrderLog.setFstOccupiedFeedBackTime(fstOccupiedFeedBackTime);
		paymentOrderLog.setFstOccupiedForceStopTime(fstOccupiedForceStopTime);
		paymentOrderLog.setFstOccuppiedWorkerUuid(fstOccuppiedWorkerUuid);
		paymentOrderLog.setFstOccupiedWorkerIp(fstOccupiedWorkerIp);
		paymentOrderLog.setFstOccupiedMessageRecordUuid(fstOccupiedMessageRecordUuid);
		paymentOrderLog.setFstOccupiedResultRecordUuid(fstOccupiedResultRecordUuid);
		
		paymentOrderLog.setSndOccupyStatus(sndOccupyStatus);
		paymentOrderLog.setSndCommunicationStatus(sndCommunicationStatus);
		paymentOrderLog.setSndOccupiedTime(sndOccupiedTime);
		paymentOrderLog.setSndOccupiedSentTime(sndOccupiedSentTime);
		paymentOrderLog.setSndOccupiedFeedBackTime(sndOccupiedFeedBackTime);
		paymentOrderLog.setSndOccupiedForceStopTime(sndOccupiedForceStopTime);
		paymentOrderLog.setSndOccuppiedWorkerUuid(sndOccuppiedWorkerUuid);
		paymentOrderLog.setSndOccupiedWorkerIp(sndOccupiedWorkerIp);
		paymentOrderLog.setSndOccupiedMessageRecordUuid(sndOccupiedMessageRecordUuid);
		paymentOrderLog.setSndOccupiedResultRecordUuid(sndOccupiedResultRecordUuid);
		
		paymentOrderLog.setTrdOccupyStatus(trdOccupyStatus);
		paymentOrderLog.setTrdCommunicationStatus(trdCommunicationStatus);
		paymentOrderLog.setTrdOccupiedTime(trdOccupiedTime);
		paymentOrderLog.setTrdOccupiedSentTime(trdOccupiedSentTime);
		paymentOrderLog.setTrdOccupiedFeedBackTime(trdOccupiedFeedBackTime);
		paymentOrderLog.setTrdOccupiedForceStopTime(trdOccupiedForceStopTime);
		paymentOrderLog.setTrdOccuppiedWorkerUuid(trdOccuppiedWorkerUuid);
		paymentOrderLog.setTrdOccupiedWorkerIp(trdOccupiedWorkerIp);
		paymentOrderLog.setTrdOccupiedMessageRecordUuid(trdOccupiedMessageRecordUuid);
		paymentOrderLog.setTrdOccupiedResultRecordUuid(trdOccupiedResultRecordUuid);
	
		paymentOrderLog.setAccessVersion(accessVersion);
		return paymentOrderLog;
	}*/

}
