package com.suidifu.jpmorgan.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSON;
import com.suidifu.jpmorgan.factory.PaymentHandlerFactory;
import com.suidifu.jpmorgan.hash.ConsistentHash;
import com.suidifu.jpmorgan.util.UUIDUtil;
import com.suidifu.jpmorgan.util.Uuid16;

@Entity
@Table(name = "trade_schedule")
public class TradeSchedule {

	@Id
	@GeneratedValue
	private Long id;

	private String tradeUuid;

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
	private String destinationAccountAppendix;

	@Column(columnDefinition = "text")
	private String destinationBankInfo;

	private String currencyCode;

	private String postscript;

	private String outlierTransactionUuid;

	private String sourceMessageUuid;

	private String sourceMessageIp;

	private Date sourceMessageTime;

	@Enumerated(EnumType.ORDINAL)
	private BusinessStatus businessStatus;// inqueue-processing-succ-failed-abandon

	private Date businessSuccessTime;

	private String batchUuid;

	@Column(columnDefinition = "text")
	private String executionPrecond;

	private String accessVersion;

	private String fstSlotUuid;

	private String fstWorkerUuid;

	@Enumerated(EnumType.ORDINAL)
	private GatewayType fstGatewayType;

	private String fstPaymentChannelUuid;

	@Column(columnDefinition = "text")
	private String fstGatewayRouterInfo;

	private Date fstTransactionBeginTime;

	private Date fstTransactionEndTime;

	private Date fstEffectiveAbsoluteTime;

	private BigDecimal fstTransactionAmount;

	@Enumerated(EnumType.ORDINAL)
	private CommunicationStatus fstCommunicationStatus;// inqueue-processing-succ-failed-abandon

	private Date fstCommunicationStartTime;

	private Date fstCommunicationEndTime;

	private Date fstCommunicationLastSentTime;

	@Enumerated(EnumType.ORDINAL)
	private BusinessStatus fstBusinessStatus;// inqueue-processing-succ-failed-abandon
	
	private String fstChannelSequenceNo;

	private String fstBusinessResultMsg;

	private String sndSlotUuid;

	private String sndWorkerUuid;

	@Enumerated(EnumType.ORDINAL)
	private GatewayType sndGatewayType;

	private String sndPaymentChannelUuid;

	@Column(columnDefinition = "text")
	private String sndGatewayRouterInfo;

	private Date sndTransactionBeginTime;

	private Date sndTransactionEndTime;

	private Date sndEffectiveAbsoluteTime;

	private BigDecimal sndTransactionAmount;

	@Enumerated(EnumType.ORDINAL)
	private CommunicationStatus sndCommunicationStatus;// inqueue-processing-succ-failed-abandon

	private Date sndCommunicationStartTime;

	private Date sndCommunicationEndTime;

	private Date sndCommunicationLastSentTime;

	@Enumerated(EnumType.ORDINAL)
	private BusinessStatus sndBusinessStatus;// inqueue-processing-succ-failed-OppositeProcessing-abandon
	
	private String sndChannelSequenceNo;

	private String sndBusinessResultMsg;

	private String trdSlotUuid;

	private String trdWorkerUuid;

	@Enumerated(EnumType.ORDINAL)
	private GatewayType trdGatewayType;

	private String trdPaymentChannelUuid;

	@Column(columnDefinition = "text")
	private String trdGatewayRouterInfo;

	private Date trdTransactionBeginTime;

	private Date trdTransactionEndTime;

	private Date trdEffectiveAbsoluteTime;

	private BigDecimal trdTransactionAmount;

	@Enumerated(EnumType.ORDINAL)
	private CommunicationStatus trdCommunicationStatus;// inqueue-processing-succ-failed-abandon

	private Date trdCommunicationStartTime;

	private Date trdCommunicationEndTime;

	private Date trdCommunicationLastSentTime;

	@Enumerated(EnumType.ORDINAL)
	private BusinessStatus trdBusinessStatus;// inqueue-processing-succ-failed-abandon
	
	private String trdChannelSequenceNo;

	private String trdBusinessResultMsg;

	private String fthSlotUuid;

	private String fthWorkerUuid;

	@Enumerated(EnumType.ORDINAL)
	private GatewayType fthGatewayType;

	private String fthPaymentChannelUuid;

	@Column(columnDefinition = "text")
	private String fthGatewayRouterInfo;

	private Date fthTransactionBeginTime;

	private Date fthTransactionEndTime;

	private Date fthEffectiveAbsoluteTime;

	private BigDecimal fthTransactionAmount;

	@Enumerated(EnumType.ORDINAL)
	private CommunicationStatus fthCommunicationStatus;// inqueue-processing-succ-failed-abandon

	private Date fthCommunicationStartTime;

	private Date fthCommunicationEndTime;

	private Date fthCommunicationLastSentTime;

	@Enumerated(EnumType.ORDINAL)
	private BusinessStatus fthBusinessStatus;// inqueue-processing-succ-failed-abandon
	
	private String fthChannelSequenceNo;

	private String fthBusinessResultMsg;

	private String fvthSlotUuid;

	private String fvthWorkerUuid;

	@Enumerated(EnumType.ORDINAL)
	private GatewayType fvthGatewayType;

	private String fvthPaymentChannelUuid;

	@Column(columnDefinition = "text")
	private String fvthGatewayRouterInfo;

	private Date fvthTransactionBeginTime;

	private Date fvthTransactionEndTime;

	private Date fvthEffectiveAbsoluteTime;

	private BigDecimal fvthTransactionAmount;

	@Enumerated(EnumType.ORDINAL)
	private CommunicationStatus fvthCommunicationStatus;// inqueue-processing-succ-failed-abandon

	private Date fvthCommunicationStartTime;

	private Date fvthCommunicationEndTime;

	private Date fvthCommunicationLastSentTime;

	@Enumerated(EnumType.ORDINAL)
	private BusinessStatus fvthBusinessStatus;// inqueue-processing-succ-failed-abandon
	
	private String fvthChannelSequenceNo;

	private String fvthBusinessResultMsg;

	private Date dateFieldOne;

	private Date dateFieldTwo;

	private Date dateFieldThree;

	private Long longFieldOne;// for consistant hash of distribute and writeback

	private Long longFieldTwo;

	private Long longFieldThree;

	private String stringFieldOne;// for callback url

	private String stringFieldTwo;// for batchDeductApplicationUuid

	private String stringFieldThree; // for financialContractUuid

	private BigDecimal decimalFieldOne;

	private BigDecimal decimalFieldTwo;

	private BigDecimal decimalFieldThree;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTradeUuid() {
		return tradeUuid;
	}

	public void setTradeUuid(String tradeUuid) {
		this.tradeUuid = tradeUuid;
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

	public Date getBusinessSuccessTime() {
		return businessSuccessTime;
	}

	public void setBusinessSuccessTime(Date businessSuccessTime) {
		this.businessSuccessTime = businessSuccessTime;
	}

	public String getFstSlotUuid() {
		return fstSlotUuid;
	}

	public void setFstSlotUuid(String fstSlotUuid) {
		this.fstSlotUuid = fstSlotUuid;
	}

	public String getFstWorkerUuid() {
		return fstWorkerUuid;
	}

	public void setFstWorkerUuid(String fstWorkerUuid) {
		this.fstWorkerUuid = fstWorkerUuid;
	}

	public Integer getFstGatewayType() {
		return null == fstGatewayType ? null : fstGatewayType.ordinal();
	}

	public void setFstGatewayType(Integer fstGatewayType) {
		this.fstGatewayType = GatewayType.fromOrdinal(fstGatewayType);
	}

	public String getFstGatewayRouterInfo() {
		return fstGatewayRouterInfo;
	}

	public void appendGatewayRouterInfo(int nthSlot, Map<String, Object> parms) {
		switch (nthSlot) {
		case 1:
			setFstGatewayRouterInfo(JSON.toJSONString(parms));
			break;

		case 2:
			setSndGatewayRouterInfo(JSON.toJSONString(parms));
			break;

		case 3:
			setTrdGatewayRouterInfo(JSON.toJSONString(parms));
			break;

		case 4:
			setFthGatewayRouterInfo(JSON.toJSONString(parms));
			break;

		case 5:
			setFvthGatewayRouterInfo(JSON.toJSONString(parms));
			break;

		default:
			break;
		}
	}

	public void setFstGatewayRouterInfo(String fstGatewayRouterInfo) {
		this.fstGatewayRouterInfo = fstGatewayRouterInfo;
	}

	public Date getFstTransactionBeginTime() {
		return fstTransactionBeginTime;
	}

	public void setFstTransactionBeginTime(Date fstTransactionBeginTime) {
		this.fstTransactionBeginTime = fstTransactionBeginTime;
	}

	public Date getFstTransactionEndTime() {
		return fstTransactionEndTime;
	}

	public void setFstTransactionEndTime(Date fstTransactionEndTime) {
		this.fstTransactionEndTime = fstTransactionEndTime;
	}

	public BigDecimal getFstTransactionAmount() {
		return fstTransactionAmount;
	}

	public void setFstTransactionAmount(BigDecimal fstTransactionAmount) {
		this.fstTransactionAmount = fstTransactionAmount;
	}

	public Integer getFstCommunicationStatus() {
		return fstCommunicationStatus.ordinal();
	}

	public void setFstCommunicationStatus(Integer fstCommunicationStatus) {
		this.fstCommunicationStatus = CommunicationStatus
				.fromOrdinal(fstCommunicationStatus);
	}

	public Date getFstCommunicationStartTime() {
		return fstCommunicationStartTime;
	}

	public void setFstCommunicationStartTime(Date fstCommunicationStartTime) {
		this.fstCommunicationStartTime = fstCommunicationStartTime;
	}

	public Date getFstCommunicationEndTime() {
		return fstCommunicationEndTime;
	}

	public void setFstCommunicationEndTime(Date fstCommunicationEndTime) {
		this.fstCommunicationEndTime = fstCommunicationEndTime;
	}

	public Date getFstCommunicationLastSentTime() {
		return fstCommunicationLastSentTime;
	}

	public void setFstCommunicationLastSentTime(
			Date fstCommunicationLastSentTime) {
		this.fstCommunicationLastSentTime = fstCommunicationLastSentTime;
	}

	public Integer getFstBusinessStatus() {
		return fstBusinessStatus.ordinal();
	}

	public void setFstBusinessStatus(Integer fstBusinessStatus) {
		this.fstBusinessStatus = BusinessStatus.fromOrdinal(fstBusinessStatus);
	}

	public String getFstBusinessResultMsg() {
		return fstBusinessResultMsg;
	}

	public void setFstBusinessResultMsg(String fstBusinessResultMsg) {
		this.fstBusinessResultMsg = fstBusinessResultMsg;
	}

	public String getSndSlotUuid() {
		return sndSlotUuid;
	}

	public void setSndSlotUuid(String sndSlotUuid) {
		this.sndSlotUuid = sndSlotUuid;
	}

	public String getSndWorkerUuid() {
		return sndWorkerUuid;
	}

	public void setSndWorkerUuid(String sndWorkerUuid) {
		this.sndWorkerUuid = sndWorkerUuid;
	}

	public Integer getSndGatewayType() {
		return null == sndGatewayType ? null : sndGatewayType.ordinal();
	}

	public void setSndGatewayType(Integer sndGatewayType) {
		this.sndGatewayType = GatewayType.fromOrdinal(sndGatewayType);
	}

	public String getSndGatewayRouterInfo() {
		return sndGatewayRouterInfo;
	}

	public void setSndGatewayRouterInfo(String sndGatewayRouterInfo) {
		this.sndGatewayRouterInfo = sndGatewayRouterInfo;
	}

	public Date getSndTransactionBeginTime() {
		return sndTransactionBeginTime;
	}

	public void setSndTransactionBeginTime(Date sndTransactionBeginTime) {
		this.sndTransactionBeginTime = sndTransactionBeginTime;
	}

	public Date getSndTransactionEndTime() {
		return sndTransactionEndTime;
	}

	public void setSndTransactionEndTime(Date sndTransactionEndTime) {
		this.sndTransactionEndTime = sndTransactionEndTime;
	}

	public BigDecimal getSndTransactionAmount() {
		return sndTransactionAmount;
	}

	public void setSndTransactionAmount(BigDecimal sndTransactionAmount) {
		this.sndTransactionAmount = sndTransactionAmount;
	}

	public Integer getSndCommunicationStatus() {
		return sndCommunicationStatus.ordinal();
	}

	public void setSndCommunicationStatus(Integer sndCommunicationStatus) {
		this.sndCommunicationStatus = CommunicationStatus
				.fromOrdinal(sndCommunicationStatus);
	}

	public Date getSndCommunicationStartTime() {
		return sndCommunicationStartTime;
	}

	public void setSndCommunicationStartTime(Date sndCommunicationStartTime) {
		this.sndCommunicationStartTime = sndCommunicationStartTime;
	}

	public Date getSndCommunicationEndTime() {
		return sndCommunicationEndTime;
	}

	public void setSndCommunicationEndTime(Date sndCommunicationEndTime) {
		this.sndCommunicationEndTime = sndCommunicationEndTime;
	}

	public Date getSndCommunicationLastSentTime() {
		return sndCommunicationLastSentTime;
	}

	public void setSndCommunicationLastSentTime(
			Date sndCommunicationLastSentTime) {
		this.sndCommunicationLastSentTime = sndCommunicationLastSentTime;
	}

	public Integer getSndBusinessStatus() {
		return sndBusinessStatus.ordinal();
	}

	public void setSndBusinessStatus(Integer sndBusinessStatus) {
		this.sndBusinessStatus = BusinessStatus.fromOrdinal(sndBusinessStatus);
	}

	public String getSndBusinessResultMsg() {
		return sndBusinessResultMsg;
	}

	public void setSndBusinessResultMsg(String sndBusinessResultMsg) {
		this.sndBusinessResultMsg = sndBusinessResultMsg;
	}

	public String getTrdSlotUuid() {
		return trdSlotUuid;
	}

	public void setTrdSlotUuid(String trdSlotUuid) {
		this.trdSlotUuid = trdSlotUuid;
	}

	public String getTrdWorkerUuid() {
		return trdWorkerUuid;
	}

	public void setTrdWorkerUuid(String trdWorkerUuid) {
		this.trdWorkerUuid = trdWorkerUuid;
	}

	public Integer getTrdGatewayType() {
		return null == trdGatewayType ? null : trdGatewayType.ordinal();
	}

	public void setTrdGatewayType(Integer trdGatewayType) {
		this.trdGatewayType = GatewayType.fromOrdinal(trdGatewayType);
	}

	public String getTrdGatewayRouterInfo() {
		return trdGatewayRouterInfo;
	}

	public void setTrdGatewayRouterInfo(String trdGatewayRouterInfo) {
		this.trdGatewayRouterInfo = trdGatewayRouterInfo;
	}

	public Date getTrdTransactionBeginTime() {
		return trdTransactionBeginTime;
	}

	public void setTrdTransactionBeginTime(Date trdTransactionBeginTime) {
		this.trdTransactionBeginTime = trdTransactionBeginTime;
	}

	public Date getTrdTransactionEndTime() {
		return trdTransactionEndTime;
	}

	public void setTrdTransactionEndTime(Date trdTransactionEndTime) {
		this.trdTransactionEndTime = trdTransactionEndTime;
	}

	public BigDecimal getTrdTransactionAmount() {
		return trdTransactionAmount;
	}

	public void setTrdTransactionAmount(BigDecimal trdTransactionAmount) {
		this.trdTransactionAmount = trdTransactionAmount;
	}

	public Integer getTrdCommunicationStatus() {
		return trdCommunicationStatus.ordinal();
	}

	public void setTrdCommunicationStatus(Integer trdCommunicationStatus) {
		this.trdCommunicationStatus = CommunicationStatus
				.fromOrdinal(trdCommunicationStatus);
	}

	public Date getTrdCommunicationStartTime() {
		return trdCommunicationStartTime;
	}

	public void setTrdCommunicationStartTime(Date trdCommunicationStartTime) {
		this.trdCommunicationStartTime = trdCommunicationStartTime;
	}

	public Date getTrdCommunicationEndTime() {
		return trdCommunicationEndTime;
	}

	public void setTrdCommunicationEndTime(Date trdCommunicationEndTime) {
		this.trdCommunicationEndTime = trdCommunicationEndTime;
	}

	public Date getTrdCommunicationLastSentTime() {
		return trdCommunicationLastSentTime;
	}

	public void setTrdCommunicationLastSentTime(
			Date trdCommunicationLastSentTime) {
		this.trdCommunicationLastSentTime = trdCommunicationLastSentTime;
	}

	public Integer getTrdBusinessStatus() {
		return trdBusinessStatus.ordinal();
	}

	public void setTrdBusinessStatus(Integer trdBusinessStatus) {
		this.trdBusinessStatus = BusinessStatus.fromOrdinal(trdBusinessStatus);
	}

	public String getTrdBusinessResultMsg() {
		return trdBusinessResultMsg;
	}

	public void setTrdBusinessResultMsg(String trdBusinessResultMsg) {
		this.trdBusinessResultMsg = trdBusinessResultMsg;
	}

	public String getFthSlotUuid() {
		return fthSlotUuid;
	}

	public void setFthSlotUuid(String fthSlotUuid) {
		this.fthSlotUuid = fthSlotUuid;
	}

	public String getFthWorkerUuid() {
		return fthWorkerUuid;
	}

	public void setFthWorkerUuid(String fthWorkerUuid) {
		this.fthWorkerUuid = fthWorkerUuid;
	}

	public Integer getFthGatewayType() {
		return null == fthGatewayType ? null : fthGatewayType.ordinal();
	}

	public void setFthGatewayType(Integer fthGatewayType) {
		this.fthGatewayType = GatewayType.fromOrdinal(fthGatewayType);
	}

	public String getFthGatewayRouterInfo() {
		return fthGatewayRouterInfo;
	}

	public void setFthGatewayRouterInfo(String fthGatewayRouterInfo) {
		this.fthGatewayRouterInfo = fthGatewayRouterInfo;
	}

	public Date getFthTransactionBeginTime() {
		return fthTransactionBeginTime;
	}

	public void setFthTransactionBeginTime(Date fthTransactionBeginTime) {
		this.fthTransactionBeginTime = fthTransactionBeginTime;
	}

	public Date getFthTransactionEndTime() {
		return fthTransactionEndTime;
	}

	public void setFthTransactionEndTime(Date fthTransactionEndTime) {
		this.fthTransactionEndTime = fthTransactionEndTime;
	}

	public BigDecimal getFthTransactionAmount() {
		return fthTransactionAmount;
	}

	public void setFthTransactionAmount(BigDecimal fthTransactionAmount) {
		this.fthTransactionAmount = fthTransactionAmount;
	}

	public Integer getFthCommunicationStatus() {
		return fthCommunicationStatus.ordinal();
	}

	public void setFthCommunicationStatus(Integer fthCommunicationStatus) {
		this.fthCommunicationStatus = CommunicationStatus
				.fromOrdinal(fthCommunicationStatus);
	}

	public Date getFthCommunicationStartTime() {
		return fthCommunicationStartTime;
	}

	public void setFthCommunicationStartTime(Date fthCommunicationStartTime) {
		this.fthCommunicationStartTime = fthCommunicationStartTime;
	}

	public Date getFthCommunicationEndTime() {
		return fthCommunicationEndTime;
	}

	public void setFthCommunicationEndTime(Date fthCommunicationEndTime) {
		this.fthCommunicationEndTime = fthCommunicationEndTime;
	}

	public Date getFthCommunicationLastSentTime() {
		return fthCommunicationLastSentTime;
	}

	public void setFthCommunicationLastSentTime(
			Date fthCommunicationLastSentTime) {
		this.fthCommunicationLastSentTime = fthCommunicationLastSentTime;
	}

	public Integer getFthBusinessStatus() {
		return fthBusinessStatus.ordinal();
	}

	public void setFthBusinessStatus(Integer fthBusinessStatus) {
		this.fthBusinessStatus = BusinessStatus.fromOrdinal(fthBusinessStatus);
	}

	public String getFthBusinessResultMsg() {
		return fthBusinessResultMsg;
	}

	public void setFthBusinessResultMsg(String fthBusinessResultMsg) {
		this.fthBusinessResultMsg = fthBusinessResultMsg;
	}

	public String getFvthSlotUuid() {
		return fvthSlotUuid;
	}

	public void setFvthSlotUuid(String fvthSlotUuid) {
		this.fvthSlotUuid = fvthSlotUuid;
	}

	public String getFvthWorkerUuid() {
		return fvthWorkerUuid;
	}

	public void setFvthWorkerUuid(String fvthWorkerUuid) {
		this.fvthWorkerUuid = fvthWorkerUuid;
	}

	public Integer getFvthGatewayType() {
		return null == fvthGatewayType ? null : fvthGatewayType.ordinal();
	}

	public void setFvthGatewayType(Integer fvthGatewayType) {
		this.fvthGatewayType = GatewayType.fromOrdinal(fvthGatewayType);
	}

	public String getFvthGatewayRouterInfo() {
		return fvthGatewayRouterInfo;
	}

	public void setFvthGatewayRouterInfo(String fvthGatewayRouterInfo) {
		this.fvthGatewayRouterInfo = fvthGatewayRouterInfo;
	}

	public Date getFvthTransactionBeginTime() {
		return fvthTransactionBeginTime;
	}

	public void setFvthTransactionBeginTime(Date fvthTransactionBeginTime) {
		this.fvthTransactionBeginTime = fvthTransactionBeginTime;
	}

	public Date getFvthTransactionEndTime() {
		return fvthTransactionEndTime;
	}

	public void setFvthTransactionEndTime(Date fvthTransactionEndTime) {
		this.fvthTransactionEndTime = fvthTransactionEndTime;
	}

	public BigDecimal getFvthTransactionAmount() {
		return fvthTransactionAmount;
	}

	public void setFvthTransactionAmount(BigDecimal fvthTransactionAmount) {
		this.fvthTransactionAmount = fvthTransactionAmount;
	}

	public Integer getFvthCommunicationStatus() {
		return fvthCommunicationStatus.ordinal();
	}

	public void setFvthCommunicationStatus(Integer fvthCommunicationStatus) {
		this.fvthCommunicationStatus = CommunicationStatus
				.fromOrdinal(fvthCommunicationStatus);
	}

	public Date getFvthCommunicationStartTime() {
		return fvthCommunicationStartTime;
	}

	public void setFvthCommunicationStartTime(Date fvthCommunicationStartTime) {
		this.fvthCommunicationStartTime = fvthCommunicationStartTime;
	}

	public Date getFvthCommunicationEndTime() {
		return fvthCommunicationEndTime;
	}

	public void setFvthCommunicationEndTime(Date fvthCommunicationEndTime) {
		this.fvthCommunicationEndTime = fvthCommunicationEndTime;
	}

	public Date getFvthCommunicationLastSentTime() {
		return fvthCommunicationLastSentTime;
	}

	public void setFvthCommunicationLastSentTime(
			Date fvthCommunicationLastSentTime) {
		this.fvthCommunicationLastSentTime = fvthCommunicationLastSentTime;
	}

	public Integer getFvthBusinessStatus() {
		return fvthBusinessStatus.ordinal();
	}

	public void setFvthBusinessStatus(Integer fvthBusinessStatus) {
		this.fvthBusinessStatus = BusinessStatus
				.fromOrdinal(fvthBusinessStatus);
	}

	public String getFvthBusinessResultMsg() {
		return fvthBusinessResultMsg;
	}

	public void setFvthBusinessResultMsg(String fvthBusinessResultMsg) {
		this.fvthBusinessResultMsg = fvthBusinessResultMsg;
	}

	public String getAccessVersion() {
		return accessVersion;
	}

	public void setAccessVersion(String accessVersion) {
		this.accessVersion = accessVersion;
	}

	public String getExecutionPrecond() {
		return executionPrecond;
	}

	public void setExecutionPrecond(String executionPrecond) {// TODO
		this.executionPrecond = executionPrecond;
	}

	public String getBatchUuid() {
		return batchUuid;
	}

	public void setBatchUuid(String batchUuid) {
		this.batchUuid = batchUuid;
	}

	public String getFstPaymentChannelUuid() {
		return fstPaymentChannelUuid;
	}

	public void setFstPaymentChannelUuid(String fstPaymentChannelUuid) {
		this.fstPaymentChannelUuid = fstPaymentChannelUuid;
	}

	public String getSndPaymentChannelUuid() {
		return sndPaymentChannelUuid;
	}

	public void setSndPaymentChannelUuid(String sndPaymentChannelUuid) {
		this.sndPaymentChannelUuid = sndPaymentChannelUuid;
	}

	public String getTrdPaymentChannelUuid() {
		return trdPaymentChannelUuid;
	}

	public void setTrdPaymentChannelUuid(String trdPaymentChannelUuid) {
		this.trdPaymentChannelUuid = trdPaymentChannelUuid;
	}

	public String getFthPaymentChannelUuid() {
		return fthPaymentChannelUuid;
	}

	public void setFthPaymentChannelUuid(String fthPaymentChannelUuid) {
		this.fthPaymentChannelUuid = fthPaymentChannelUuid;
	}

	public String getFvthPaymentChannelUuid() {
		return fvthPaymentChannelUuid;
	}

	public void setFvthPaymentChannelUuid(String fvthPaymentChannelUuid) {
		this.fvthPaymentChannelUuid = fvthPaymentChannelUuid;
	}

	public Date getFstEffectiveAbsoluteTime() {
		return fstEffectiveAbsoluteTime;
	}

	public void setFstEffectiveAbsoluteTime(Date fstEffectiveAbsoluteTime) {
		this.fstEffectiveAbsoluteTime = fstEffectiveAbsoluteTime;
	}

	public Date getSndEffectiveAbsoluteTime() {
		return sndEffectiveAbsoluteTime;
	}

	public void setSndEffectiveAbsoluteTime(Date sndEffectiveAbsoluteTime) {
		this.sndEffectiveAbsoluteTime = sndEffectiveAbsoluteTime;
	}

	public Date getTrdEffectiveAbsoluteTime() {
		return trdEffectiveAbsoluteTime;
	}

	public void setTrdEffectiveAbsoluteTime(Date trdEffectiveAbsoluteTime) {
		this.trdEffectiveAbsoluteTime = trdEffectiveAbsoluteTime;
	}

	public Date getFthEffectiveAbsoluteTime() {
		return fthEffectiveAbsoluteTime;
	}

	public void setFthEffectiveAbsoluteTime(Date fthEffectiveAbsoluteTime) {
		this.fthEffectiveAbsoluteTime = fthEffectiveAbsoluteTime;
	}

	public Date getFvthEffectiveAbsoluteTime() {
		return fvthEffectiveAbsoluteTime;
	}

	public void setFvthEffectiveAbsoluteTime(Date fvthEffectiveAbsoluteTime) {
		this.fvthEffectiveAbsoluteTime = fvthEffectiveAbsoluteTime;
	}

	public String getFstChannelSequenceNo() {
		return fstChannelSequenceNo;
	}

	public void setFstChannelSequenceNo(String fstChannelSequenceNo) {
		this.fstChannelSequenceNo = fstChannelSequenceNo;
	}

	public String getSndChannelSequenceNo() {
		return sndChannelSequenceNo;
	}

	public void setSndChannelSequenceNo(String sndChannelSequenceNo) {
		this.sndChannelSequenceNo = sndChannelSequenceNo;
	}

	public String getTrdChannelSequenceNo() {
		return trdChannelSequenceNo;
	}

	public void setTrdChannelSequenceNo(String trdChannelSequenceNo) {
		this.trdChannelSequenceNo = trdChannelSequenceNo;
	}

	public String getFthChannelSequenceNo() {
		return fthChannelSequenceNo;
	}

	public void setFthChannelSequenceNo(String fthChannelSequenceNo) {
		this.fthChannelSequenceNo = fthChannelSequenceNo;
	}

	public String getFvthChannelSequenceNo() {
		return fvthChannelSequenceNo;
	}

	public void setFvthChannelSequenceNo(String fvthChannelSequenceNo) {
		this.fvthChannelSequenceNo = fvthChannelSequenceNo;
	}

	public String getStringFieldOne() {
		return stringFieldOne;
	}

	public void setStringFieldOne(String stringFieldOne) {
		this.stringFieldOne = stringFieldOne;
	}

	public String getStringFieldTwo() {
		return stringFieldTwo;
	}

	public void setStringFieldTwo(String stringFieldTwo) {
		this.stringFieldTwo = stringFieldTwo;
	}

	public String getStringFieldThree() {
		return stringFieldThree;
	}

	public void setStringFieldThree(String stringFieldThree) {
		this.stringFieldThree = stringFieldThree;
	}

	public Long getLongFieldOne() {
		return longFieldOne;
	}

	public void setLongFieldOne(Long longFieldOne) {
		this.longFieldOne = longFieldOne;
	}

	public int nextReadySlot() {
		if (BusinessStatus.Inqueue.equals(this.fstBusinessStatus)) {
			return 1;
		}
		if (BusinessStatus.Inqueue.equals(this.sndBusinessStatus)) {
			return 2;
		}
		if (BusinessStatus.Inqueue.equals(this.trdBusinessStatus)) {
			return 3;
		}
		if (BusinessStatus.Inqueue.equals(this.fthBusinessStatus)) {
			return 4;
		}
		if (BusinessStatus.Inqueue.equals(this.fvthBusinessStatus)) {
			return 5;
		}
		return 0;
	}

	public int currentWorkSlot() {
		if (BusinessStatus.Processing.equals(this.fstBusinessStatus)
				|| BusinessStatus.OppositeProcessing
						.equals(this.fstBusinessStatus)) {
			return 1;
		}
		if (BusinessStatus.Processing.equals(this.sndBusinessStatus)
				|| BusinessStatus.OppositeProcessing
						.equals(this.sndBusinessStatus)) {
			return 2;
		}
		if (BusinessStatus.Processing.equals(this.trdBusinessStatus)
				|| BusinessStatus.OppositeProcessing
						.equals(this.trdBusinessStatus)) {
			return 3;
		}
		if (BusinessStatus.Processing.equals(this.fthBusinessStatus)
				|| BusinessStatus.OppositeProcessing
						.equals(this.fthBusinessStatus)) {
			return 4;
		}
		if (BusinessStatus.Processing.equals(this.fvthBusinessStatus)
				|| BusinessStatus.OppositeProcessing
						.equals(this.fvthBusinessStatus)) {
			return 5;
		}
		return 0;
	}

	public GatewaySlot extractSlotInfo(int nthSlot) {

		switch (nthSlot) {
		case 1:

			return new GatewaySlot(this.fstSlotUuid, this.fstWorkerUuid,
					this.fstGatewayType, this.fstPaymentChannelUuid,
					this.fstGatewayRouterInfo, this.fstTransactionBeginTime,
					this.fstTransactionEndTime, this.fstEffectiveAbsoluteTime,
					this.fstTransactionAmount, this.fstCommunicationStatus,
					this.fstCommunicationStartTime,
					this.fstCommunicationEndTime,
					this.fstCommunicationLastSentTime, this.fstBusinessStatus,
					this.fstBusinessResultMsg, this.fstChannelSequenceNo);

		case 2:

			return new GatewaySlot(this.sndSlotUuid, this.sndWorkerUuid,
					this.sndGatewayType, this.sndPaymentChannelUuid,
					this.sndGatewayRouterInfo, this.sndTransactionBeginTime,
					this.sndTransactionEndTime, this.sndEffectiveAbsoluteTime,
					this.sndTransactionAmount, this.sndCommunicationStatus,
					this.sndCommunicationStartTime,
					this.sndCommunicationEndTime,
					this.sndCommunicationLastSentTime, this.sndBusinessStatus,
					this.sndBusinessResultMsg, this.sndChannelSequenceNo);

		case 3:

			return new GatewaySlot(this.trdSlotUuid, this.trdWorkerUuid,
					this.trdGatewayType, this.trdPaymentChannelUuid,
					this.trdGatewayRouterInfo, this.trdTransactionBeginTime,
					this.trdTransactionEndTime, this.trdEffectiveAbsoluteTime,
					this.trdTransactionAmount, this.trdCommunicationStatus,
					this.trdCommunicationStartTime,
					this.trdCommunicationEndTime,
					this.trdCommunicationLastSentTime, this.trdBusinessStatus,
					this.trdBusinessResultMsg, this.trdChannelSequenceNo);

		case 4:

			return new GatewaySlot(this.fthSlotUuid, this.fthWorkerUuid,
					this.fthGatewayType, this.fthPaymentChannelUuid,
					this.fthGatewayRouterInfo, this.fthTransactionBeginTime,
					this.fthTransactionEndTime, this.fthEffectiveAbsoluteTime,
					this.fthTransactionAmount, this.fthCommunicationStatus,
					this.fthCommunicationStartTime,
					this.fthCommunicationEndTime,
					this.fthCommunicationLastSentTime, this.fthBusinessStatus,
					this.fthBusinessResultMsg, this.fthChannelSequenceNo);

		case 5:

			return new GatewaySlot(this.fvthSlotUuid, this.fvthWorkerUuid,
					this.fvthGatewayType, this.fvthPaymentChannelUuid,
					this.fvthGatewayRouterInfo, this.fvthTransactionBeginTime,
					this.fvthTransactionEndTime,
					this.fvthEffectiveAbsoluteTime, this.fvthTransactionAmount,
					this.fvthCommunicationStatus,
					this.fvthCommunicationStartTime,
					this.fvthCommunicationEndTime,
					this.fvthCommunicationLastSentTime,
					this.fvthBusinessStatus, this.fvthBusinessResultMsg,
					this.fvthChannelSequenceNo);

		default:
			return null;
		}
	}

	public TradeInfo extractTradeInfo(GatewaySlot gatewaySlot) {
		return new TradeInfo(gatewaySlot.getSlotUuid(), accountSide.ordinal(),
				sourceAccountName, sourceAccountNo, sourceAccountAppendix,
				sourceBankInfo, destinationAccountName, destinationAccountNo,
				destinationAccountAppendix, destinationBankInfo,
				gatewaySlot.getTransactionAmount(), currencyCode, postscript,
				tradeUuid, gatewaySlot.getGatewayType(),
				gatewaySlot.getGatewayRouterInfo());
	}

	public boolean hasNecessaryAttr() {// TODO 完善

		boolean checkFlag = null != this.accountSide
				// && null != this.sourceAccountNo
				&& StringUtils.isNotBlank(this.destinationAccountNo)
				&& StringUtils.isNotBlank(this.destinationAccountName)
				// && null != this.currencyCode
				&& StringUtils.isNotBlank(this.outlierTransactionUuid)
				// && null != this.fstGatewayType
				&& StringUtils.isNotBlank(this.fstPaymentChannelUuid)
				&& null != this.fstTransactionAmount;
		
		if(!checkFlag) {
			return false;
		}
		
		if(StringUtils.isNotBlank(this.sndPaymentChannelUuid) && null == this.sndTransactionAmount) {
			return false;
		}
		if(StringUtils.isNotBlank(this.trdPaymentChannelUuid) && null == this.trdTransactionAmount) {
			return false;
		}
		if(StringUtils.isNotBlank(this.fthPaymentChannelUuid) && null == this.fthTransactionAmount) {
			return false;
		}
		if(StringUtils.isNotBlank(this.fvthPaymentChannelUuid) && null == this.fvthTransactionAmount) {
			return false;
		}
		return true;
	}

	public void fillInitData(ConsistentHash<Integer> generalConsistentHashSource, String gatewayName, int nextCount) {
		this.tradeUuid = UUIDUtil.uuid();
		this.accessVersion = UUID.randomUUID().toString();
		this.businessStatus = BusinessStatus.Inqueue;
		this.sourceMessageTime = new Date();

		if(StringUtils.isBlank(this.fstSlotUuid)) {
			this.fstSlotUuid = generateSlotUuid(gatewayName, nextCount);
		}
		this.fstCommunicationStatus = CommunicationStatus.Inqueue;
		this.fstBusinessStatus = BusinessStatus.Inqueue;

		if(StringUtils.isBlank(this.sndSlotUuid)) {
			this.sndSlotUuid = UUIDUtil.snowFlakeIdString();
		}
		this.sndCommunicationStatus = CommunicationStatus.Inqueue;
		this.sndBusinessStatus = BusinessStatus.Inqueue;

		if(StringUtils.isBlank(this.trdSlotUuid)) {
			this.trdSlotUuid = UUIDUtil.snowFlakeIdString();
		}
		this.trdCommunicationStatus = CommunicationStatus.Inqueue;
		this.trdBusinessStatus = BusinessStatus.Inqueue;

		if(StringUtils.isBlank(this.fthSlotUuid)) {
			this.fthSlotUuid = UUIDUtil.snowFlakeIdString();
		}
		this.fthCommunicationStatus = CommunicationStatus.Inqueue;
		this.fthBusinessStatus = BusinessStatus.Inqueue;

		if(StringUtils.isBlank(this.fvthSlotUuid)) {
			this.fvthSlotUuid = UUIDUtil.snowFlakeIdString();
		}
		this.fvthCommunicationStatus = CommunicationStatus.Inqueue;
		this.fvthBusinessStatus = BusinessStatus.Inqueue;
		
		try {
			this.longFieldOne = (long)generalConsistentHashSource.getNode(this.tradeUuid);
		} catch (Exception e) {
			this.longFieldOne = 1l;
			e.printStackTrace();
		}

	}

	public boolean canBeTransfer() {
		return BusinessStatus.Success.equals(this.businessStatus)
				|| BusinessStatus.Failed.equals(this.businessStatus)
				|| BusinessStatus.Abandon.equals(this.businessStatus);
	}

	public TradeScheduleLog transferToTradeScheduleLog() {
		TradeScheduleLog tradeScheduleLog = new TradeScheduleLog();
		tradeScheduleLog.setTradeUuid(tradeUuid);
		tradeScheduleLog.setAccessVersion(accessVersion);
		tradeScheduleLog.setAccountSide(accountSide);
		tradeScheduleLog.setSourceAccountName(sourceAccountName);
		tradeScheduleLog.setSourceAccountNo(sourceAccountNo);
		tradeScheduleLog.setSourceAccountAppendix(sourceAccountAppendix);
		tradeScheduleLog.setSourceBankInfo(sourceBankInfo);
		tradeScheduleLog.setDestinationAccountNo(destinationAccountNo);
		tradeScheduleLog.setDestinationAccountName(destinationAccountName);
		tradeScheduleLog
				.setDestinationAccountAppendix(destinationAccountAppendix);
		tradeScheduleLog.setDestinationBankInfo(destinationBankInfo);
		tradeScheduleLog.setCurrencyCode(currencyCode);
		tradeScheduleLog.setPostscript(postscript);
		tradeScheduleLog.setOutlierTransactionUuid(outlierTransactionUuid);
		tradeScheduleLog.setSourceMessageIp(sourceMessageIp);
		tradeScheduleLog.setSourceMessageTime(sourceMessageTime);
		tradeScheduleLog.setSourceMessageUuid(sourceMessageUuid);
		tradeScheduleLog.setBusinessStatus(businessStatus);
		tradeScheduleLog.setBusinessSuccessTime(businessSuccessTime);
		tradeScheduleLog.setBatchUuid(batchUuid);
		tradeScheduleLog.setExecutionPrecond(executionPrecond);

		tradeScheduleLog.setFstSlotUuid(fstSlotUuid);
		tradeScheduleLog.setFstWorkerUuid(fstWorkerUuid);
		tradeScheduleLog.setFstGatewayType(fstGatewayType);
		tradeScheduleLog.setFstPaymentChannelUuid(fstPaymentChannelUuid);
		tradeScheduleLog.setFstGatewayRouterInfo(fstGatewayRouterInfo);
		tradeScheduleLog.setFstTransactionBeginTime(fstTransactionBeginTime);
		tradeScheduleLog.setFstTransactionEndTime(fstTransactionEndTime);
		tradeScheduleLog.setFstEffectiveAbsoluteTime(fstEffectiveAbsoluteTime);
		tradeScheduleLog.setFstTransactionAmount(fstTransactionAmount);
		tradeScheduleLog
				.setFstCommunicationStartTime(fstCommunicationStartTime);
		tradeScheduleLog.setFstCommunicationEndTime(fstCommunicationEndTime);
		tradeScheduleLog
				.setFstCommunicationLastSentTime(fstCommunicationLastSentTime);
		tradeScheduleLog.setFstCommunicationStatus(fstCommunicationStatus);
		tradeScheduleLog.setFstBusinessStatus(fstBusinessStatus);
		tradeScheduleLog.setFstChannelSequenceNo(fstChannelSequenceNo);
		tradeScheduleLog.setFstBusinessResultMsg(fstBusinessResultMsg);

		tradeScheduleLog.setSndSlotUuid(sndSlotUuid);
		tradeScheduleLog.setSndWorkerUuid(sndWorkerUuid);
		tradeScheduleLog.setSndGatewayType(sndGatewayType);
		tradeScheduleLog.setSndPaymentChannelUuid(sndPaymentChannelUuid);
		tradeScheduleLog.setSndGatewayRouterInfo(sndGatewayRouterInfo);
		tradeScheduleLog.setSndTransactionBeginTime(sndTransactionBeginTime);
		tradeScheduleLog.setSndTransactionEndTime(sndTransactionEndTime);
		tradeScheduleLog.setSndEffectiveAbsoluteTime(sndEffectiveAbsoluteTime);
		tradeScheduleLog.setSndTransactionAmount(sndTransactionAmount);
		tradeScheduleLog
				.setSndCommunicationStartTime(sndCommunicationStartTime);
		tradeScheduleLog.setSndCommunicationEndTime(sndCommunicationEndTime);
		tradeScheduleLog
				.setSndCommunicationLastSentTime(sndCommunicationLastSentTime);
		tradeScheduleLog.setSndCommunicationStatus(sndCommunicationStatus);
		tradeScheduleLog.setSndBusinessStatus(sndBusinessStatus);
		tradeScheduleLog.setSndChannelSequenceNo(sndChannelSequenceNo);
		tradeScheduleLog.setSndBusinessResultMsg(sndBusinessResultMsg);

		tradeScheduleLog.setTrdSlotUuid(trdSlotUuid);
		tradeScheduleLog.setTrdWorkerUuid(trdWorkerUuid);
		tradeScheduleLog.setTrdGatewayType(trdGatewayType);
		tradeScheduleLog.setTrdPaymentChannelUuid(trdPaymentChannelUuid);
		tradeScheduleLog.setTrdGatewayRouterInfo(trdGatewayRouterInfo);
		tradeScheduleLog.setTrdTransactionBeginTime(trdTransactionBeginTime);
		tradeScheduleLog.setTrdTransactionEndTime(trdTransactionEndTime);
		tradeScheduleLog.setTrdEffectiveAbsoluteTime(trdEffectiveAbsoluteTime);
		tradeScheduleLog.setTrdTransactionAmount(trdTransactionAmount);
		tradeScheduleLog
				.setTrdCommunicationStartTime(trdCommunicationStartTime);
		tradeScheduleLog.setTrdCommunicationEndTime(trdCommunicationEndTime);
		tradeScheduleLog
				.setTrdCommunicationLastSentTime(trdCommunicationLastSentTime);
		tradeScheduleLog.setTrdCommunicationStatus(trdCommunicationStatus);
		tradeScheduleLog.setTrdBusinessStatus(trdBusinessStatus);
		tradeScheduleLog.setTrdChannelSequenceNo(trdChannelSequenceNo);
		tradeScheduleLog.setTrdBusinessResultMsg(trdBusinessResultMsg);

		tradeScheduleLog.setFthSlotUuid(fthSlotUuid);
		tradeScheduleLog.setFthWorkerUuid(fthWorkerUuid);
		tradeScheduleLog.setFthGatewayType(fthGatewayType);
		tradeScheduleLog.setFthPaymentChannelUuid(fthPaymentChannelUuid);
		tradeScheduleLog.setFthGatewayRouterInfo(fthGatewayRouterInfo);
		tradeScheduleLog.setFthTransactionBeginTime(fthTransactionBeginTime);
		tradeScheduleLog.setFthTransactionEndTime(fthTransactionEndTime);
		tradeScheduleLog.setFthEffectiveAbsoluteTime(fthEffectiveAbsoluteTime);
		tradeScheduleLog.setFthTransactionAmount(fthTransactionAmount);
		tradeScheduleLog
				.setFthCommunicationStartTime(fthCommunicationStartTime);
		tradeScheduleLog.setFthCommunicationEndTime(fthCommunicationEndTime);
		tradeScheduleLog
				.setFthCommunicationLastSentTime(fthCommunicationLastSentTime);
		tradeScheduleLog.setFthCommunicationStatus(fthCommunicationStatus);
		tradeScheduleLog.setFthBusinessStatus(fthBusinessStatus);
		tradeScheduleLog.setFthChannelSequenceNo(fthChannelSequenceNo);
		tradeScheduleLog.setFthBusinessResultMsg(fthBusinessResultMsg);

		tradeScheduleLog.setFvthSlotUuid(fvthSlotUuid);
		tradeScheduleLog.setFvthWorkerUuid(fvthWorkerUuid);
		tradeScheduleLog.setFvthGatewayType(fvthGatewayType);
		tradeScheduleLog.setFvthPaymentChannelUuid(fvthPaymentChannelUuid);
		tradeScheduleLog.setFvthGatewayRouterInfo(fvthGatewayRouterInfo);
		tradeScheduleLog.setFvthTransactionBeginTime(fvthTransactionBeginTime);
		tradeScheduleLog.setFvthTransactionEndTime(fvthTransactionEndTime);
		tradeScheduleLog
				.setFvthEffectiveAbsoluteTime(fvthEffectiveAbsoluteTime);
		tradeScheduleLog.setFvthTransactionAmount(fvthTransactionAmount);
		tradeScheduleLog
				.setFvthCommunicationStartTime(fvthCommunicationStartTime);
		tradeScheduleLog.setFvthCommunicationEndTime(fvthCommunicationEndTime);
		tradeScheduleLog
				.setFvthCommunicationLastSentTime(fvthCommunicationLastSentTime);
		tradeScheduleLog.setFvthCommunicationStatus(fvthCommunicationStatus);
		tradeScheduleLog.setFvthBusinessStatus(fvthBusinessStatus);
		tradeScheduleLog.setFvthChannelSequenceNo(fvthChannelSequenceNo);
		tradeScheduleLog.setFvthBusinessResultMsg(fvthBusinessResultMsg);
		
		tradeScheduleLog.setStringFieldOne(stringFieldOne);
		tradeScheduleLog.setStringFieldTwo(stringFieldTwo);
		tradeScheduleLog.setStringFieldThree(stringFieldThree);
		tradeScheduleLog.setLongFieldOne(longFieldOne);

		return tradeScheduleLog;
	}

	public boolean canResubmit() {
		return BusinessStatus.Failed.equals(this.businessStatus)
				|| BusinessStatus.Abandon.equals(this.businessStatus);
	}

	public PaymentReturnModel transferToReturnModel() {//TODO 2016.11.21改，在只用fst_slot的情况下，默认直接将fst_slot_uuid返回
		return new PaymentReturnModel(this.sourceMessageUuid,
				this.outlierTransactionUuid, this.fstSlotUuid);
	}

	public int theLastProcessSlot() {
		if (!BusinessStatus.Inqueue.equals(this.fvthBusinessStatus) && !BusinessStatus.Abandon.equals(this.fvthBusinessStatus)) {
			return 5;
		}

		if (!BusinessStatus.Inqueue.equals(this.fthBusinessStatus) && !BusinessStatus.Abandon.equals(this.fthBusinessStatus)) {
			return 4;
		}

		if (!BusinessStatus.Inqueue.equals(this.trdBusinessStatus) && !BusinessStatus.Abandon.equals(this.trdBusinessStatus)) {
			return 3;
		}

		if (!BusinessStatus.Inqueue.equals(this.sndBusinessStatus) && !BusinessStatus.Abandon.equals(this.sndBusinessStatus)) {
			return 2;
		}

		return 1;
	}

	public TradeSchedule() {
		super();
	}

	public TradeSchedule(TradeScheduleModel tradeScheduleModel) {
		super();
		//this.tradeUuid = tradeScheduleModel.getTradeUuid();
		this.accountSide = tradeScheduleModel.getAccountSide();
		this.sourceAccountName = tradeScheduleModel.getSourceAccountName();
		this.sourceAccountNo = tradeScheduleModel.getSourceAccountNo();
		this.sourceAccountAppendix = tradeScheduleModel.getSourceAccountAppendix();
		this.sourceBankInfo = tradeScheduleModel.getSourceBankInfo();
		this.destinationAccountName = tradeScheduleModel.getDestinationAccountName();
		this.destinationAccountNo = tradeScheduleModel.getDestinationAccountNo();
		this.destinationAccountAppendix = tradeScheduleModel.getDestinationAccountAppendix();
		this.destinationBankInfo = tradeScheduleModel.getDestinationBankInfo();
		this.currencyCode = tradeScheduleModel.getCurrencyCode();
		this.postscript = tradeScheduleModel.getPostscript();
		this.outlierTransactionUuid = tradeScheduleModel.getOutlierTransactionUuid();
		this.sourceMessageUuid = tradeScheduleModel.getSourceMessageUuid();
		//this.sourceMessageIp = tradeScheduleModel.getSourceMessageIp();
		//this.sourceMessageTime = tradeScheduleModel.getSourceMessageTime();
		//this.businessStatus = tradeScheduleModel.getBusinessStatus();
		//this.businessSuccessTime = tradeScheduleModel.getBusinessSuccessTime();
		this.batchUuid = tradeScheduleModel.getBatchUuid();
		this.executionPrecond = tradeScheduleModel.getExecutionPrecond();
		//this.accessVersion = tradeScheduleModel.getAccessVersion();
		this.fstSlotUuid = tradeScheduleModel.getFstSlotUuid();
		//this.fstWorkerUuid = tradeScheduleModel.getFstWorkerUuid();
		this.fstGatewayType = tradeScheduleModel.getFstGatewayType();
		this.fstPaymentChannelUuid = tradeScheduleModel.getFstPaymentChannelUuid();
		this.fstGatewayRouterInfo = tradeScheduleModel.getFstGatewayRouterInfo();
		//this.fstTransactionBeginTime = tradeScheduleModel.getFstTransactionBeginTime();
		//this.fstTransactionEndTime = tradeScheduleModel.getFstTransactionEndTime();
		this.fstEffectiveAbsoluteTime = tradeScheduleModel.getFstEffectiveAbsoluteTime();
		this.fstTransactionAmount = tradeScheduleModel.getFstTransactionAmount();

		this.sndSlotUuid = tradeScheduleModel.getSndSlotUuid();
		this.sndGatewayType = tradeScheduleModel.getSndGatewayType();
		this.sndPaymentChannelUuid = tradeScheduleModel.getSndPaymentChannelUuid();
		this.sndGatewayRouterInfo = tradeScheduleModel.getSndGatewayRouterInfo();
		this.sndEffectiveAbsoluteTime = tradeScheduleModel.getSndEffectiveAbsoluteTime();
		this.sndTransactionAmount = tradeScheduleModel.getSndTransactionAmount();
		
		this.trdSlotUuid = tradeScheduleModel.getTrdSlotUuid();
		this.trdGatewayType = tradeScheduleModel.getTrdGatewayType();
		this.trdPaymentChannelUuid = tradeScheduleModel.getTrdPaymentChannelUuid();
		this.trdGatewayRouterInfo = tradeScheduleModel.getTrdGatewayRouterInfo();
		this.trdEffectiveAbsoluteTime = tradeScheduleModel.getTrdEffectiveAbsoluteTime();
		this.trdTransactionAmount = tradeScheduleModel.getTrdTransactionAmount();
		
		this.fthSlotUuid = tradeScheduleModel.getFthSlotUuid();
		this.fthGatewayType = tradeScheduleModel.getFthGatewayType();
		this.fthPaymentChannelUuid = tradeScheduleModel.getFthPaymentChannelUuid();
		this.fthGatewayRouterInfo = tradeScheduleModel.getFthGatewayRouterInfo();
		this.fthEffectiveAbsoluteTime = tradeScheduleModel.getFthEffectiveAbsoluteTime();
		this.fthTransactionAmount = tradeScheduleModel.getFthTransactionAmount();
		
		this.fvthSlotUuid = tradeScheduleModel.getFvthSlotUuid();
		this.fvthGatewayType = tradeScheduleModel.getFvthGatewayType();
		this.fvthPaymentChannelUuid = tradeScheduleModel.getFvthPaymentChannelUuid();
		this.fvthGatewayRouterInfo = tradeScheduleModel.getFvthGatewayRouterInfo();
		this.fvthEffectiveAbsoluteTime = tradeScheduleModel.getFvthEffectiveAbsoluteTime();
		this.fvthTransactionAmount = tradeScheduleModel.getFvthTransactionAmount();
		
		this.stringFieldOne = tradeScheduleModel.getNotifyUrl();
		this.stringFieldTwo = tradeScheduleModel.getBatchDeductApplicationUuid();
		this.stringFieldThree = tradeScheduleModel.getFinancialContractUuid();
	}

	private String generateSlotUuid(String gatewayName, int nextCount) {
		if(PaymentHandlerFactory.GATEWAY_TYPE_DIRECT_BANK_SPDB.equals(gatewayName)) {
			return Uuid16.create().toString();
		}
		//TODO 先写死，待改
		if(PaymentHandlerFactory.GATEWAY_TYPE_DIRECT_BANK_WFCCB.equals(gatewayName)) {
			return "1570218000009" + String.format("%06d", nextCount);
		}
		return UUIDUtil.snowFlakeIdString();
	}
}
