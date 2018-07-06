package com.suidifu.jpmorgan.entity;

import java.math.BigDecimal;
import java.util.Date;

public class TradeScheduleModel {
	
	private String tradeUuid;

	private AccountSide accountSide;

	private String sourceAccountName;

	private String sourceAccountNo;

	private String sourceAccountAppendix;

	private String sourceBankInfo;

	private String destinationAccountName;

	private String destinationAccountNo;

	private String destinationAccountAppendix;

	private String destinationBankInfo;

	private String currencyCode;

	private String postscript;

	private String outlierTransactionUuid;

	private String sourceMessageUuid;

	private String sourceMessageIp;

	private Date sourceMessageTime;

	private BusinessStatus businessStatus;// inqueue-processing-succ-failed-abandon

	private Date businessSuccessTime;

	private String batchUuid;

	private String executionPrecond;

	private String accessVersion;

	private String fstSlotUuid;

	private String fstWorkerUuid;

	private GatewayType fstGatewayType;

	private String fstPaymentChannelUuid;

	private String fstGatewayRouterInfo;

	private Date fstTransactionBeginTime;

	private Date fstTransactionEndTime;

	private Date fstEffectiveAbsoluteTime;

	private BigDecimal fstTransactionAmount;

	private CommunicationStatus fstCommunicationStatus;// inqueue-processing-succ-failed-abandon

	private Date fstCommunicationStartTime;

	private Date fstCommunicationEndTime;

	private Date fstCommunicationLastSentTime;

	private BusinessStatus fstBusinessStatus;// inqueue-processing-succ-failed-abandon
	
	private String fstChannelSequenceNo;

	private String fstBusinessResultMsg;

	private String sndSlotUuid;

	private String sndWorkerUuid;

	private GatewayType sndGatewayType;

	private String sndPaymentChannelUuid;

	private String sndGatewayRouterInfo;

	private Date sndTransactionBeginTime;

	private Date sndTransactionEndTime;

	private Date sndEffectiveAbsoluteTime;

	private BigDecimal sndTransactionAmount;

	private CommunicationStatus sndCommunicationStatus;// inqueue-processing-succ-failed-abandon

	private Date sndCommunicationStartTime;

	private Date sndCommunicationEndTime;

	private Date sndCommunicationLastSentTime;

	private BusinessStatus sndBusinessStatus;// inqueue-processing-succ-failed-OppositeProcessing-abandon
	
	private String sndChannelSequenceNo;

	private String sndBusinessResultMsg;

	private String trdSlotUuid;

	private String trdWorkerUuid;

	private GatewayType trdGatewayType;

	private String trdPaymentChannelUuid;

	private String trdGatewayRouterInfo;

	private Date trdTransactionBeginTime;

	private Date trdTransactionEndTime;

	private Date trdEffectiveAbsoluteTime;

	private BigDecimal trdTransactionAmount;

	private CommunicationStatus trdCommunicationStatus;// inqueue-processing-succ-failed-abandon

	private Date trdCommunicationStartTime;

	private Date trdCommunicationEndTime;

	private Date trdCommunicationLastSentTime;

	private BusinessStatus trdBusinessStatus;// inqueue-processing-succ-failed-abandon
	
	private String trdChannelSequenceNo;

	private String trdBusinessResultMsg;

	private String fthSlotUuid;

	private String fthWorkerUuid;

	private GatewayType fthGatewayType;

	private String fthPaymentChannelUuid;

	private String fthGatewayRouterInfo;

	private Date fthTransactionBeginTime;

	private Date fthTransactionEndTime;

	private Date fthEffectiveAbsoluteTime;

	private BigDecimal fthTransactionAmount;

	private CommunicationStatus fthCommunicationStatus;// inqueue-processing-succ-failed-abandon

	private Date fthCommunicationStartTime;

	private Date fthCommunicationEndTime;

	private Date fthCommunicationLastSentTime;

	private BusinessStatus fthBusinessStatus;// inqueue-processing-succ-failed-abandon
	
	private String fthChannelSequenceNo;

	private String fthBusinessResultMsg;

	private String fvthSlotUuid;

	private String fvthWorkerUuid;

	private GatewayType fvthGatewayType;

	private String fvthPaymentChannelUuid;

	private String fvthGatewayRouterInfo;

	private Date fvthTransactionBeginTime;

	private Date fvthTransactionEndTime;

	private Date fvthEffectiveAbsoluteTime;

	private BigDecimal fvthTransactionAmount;

	private CommunicationStatus fvthCommunicationStatus;// inqueue-processing-succ-failed-abandon

	private Date fvthCommunicationStartTime;

	private Date fvthCommunicationEndTime;

	private Date fvthCommunicationLastSentTime;

	private BusinessStatus fvthBusinessStatus;// inqueue-processing-succ-failed-abandon
	
	private String fvthChannelSequenceNo;

	private String fvthBusinessResultMsg;
	
	private String notifyUrl;

	private String batchDeductApplicationUuid;

	private String financialContractUuid;

	public String getTradeUuid() {
		return tradeUuid;
	}

	public void setTradeUuid(String tradeUuid) {
		this.tradeUuid = tradeUuid;
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

	public BusinessStatus getBusinessStatus() {
		return businessStatus;
	}

	public void setBusinessStatus(BusinessStatus businessStatus) {
		this.businessStatus = businessStatus;
	}

	public Date getBusinessSuccessTime() {
		return businessSuccessTime;
	}

	public void setBusinessSuccessTime(Date businessSuccessTime) {
		this.businessSuccessTime = businessSuccessTime;
	}

	public String getBatchUuid() {
		return batchUuid;
	}

	public void setBatchUuid(String batchUuid) {
		this.batchUuid = batchUuid;
	}

	public String getExecutionPrecond() {
		return executionPrecond;
	}

	public void setExecutionPrecond(String executionPrecond) {
		this.executionPrecond = executionPrecond;
	}

	public String getAccessVersion() {
		return accessVersion;
	}

	public void setAccessVersion(String accessVersion) {
		this.accessVersion = accessVersion;
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

	public GatewayType getFstGatewayType() {
		return fstGatewayType;
	}

	public void setFstGatewayType(GatewayType fstGatewayType) {
		this.fstGatewayType = fstGatewayType;
	}

	public String getFstPaymentChannelUuid() {
		return fstPaymentChannelUuid;
	}

	public void setFstPaymentChannelUuid(String fstPaymentChannelUuid) {
		this.fstPaymentChannelUuid = fstPaymentChannelUuid;
	}

	public String getFstGatewayRouterInfo() {
		return fstGatewayRouterInfo;
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

	public Date getFstEffectiveAbsoluteTime() {
		return fstEffectiveAbsoluteTime;
	}

	public void setFstEffectiveAbsoluteTime(Date fstEffectiveAbsoluteTime) {
		this.fstEffectiveAbsoluteTime = fstEffectiveAbsoluteTime;
	}

	public BigDecimal getFstTransactionAmount() {
		return fstTransactionAmount;
	}

	public void setFstTransactionAmount(BigDecimal fstTransactionAmount) {
		this.fstTransactionAmount = fstTransactionAmount;
	}

	public CommunicationStatus getFstCommunicationStatus() {
		return fstCommunicationStatus;
	}

	public void setFstCommunicationStatus(CommunicationStatus fstCommunicationStatus) {
		this.fstCommunicationStatus = fstCommunicationStatus;
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

	public void setFstCommunicationLastSentTime(Date fstCommunicationLastSentTime) {
		this.fstCommunicationLastSentTime = fstCommunicationLastSentTime;
	}

	public BusinessStatus getFstBusinessStatus() {
		return fstBusinessStatus;
	}

	public void setFstBusinessStatus(BusinessStatus fstBusinessStatus) {
		this.fstBusinessStatus = fstBusinessStatus;
	}

	public String getFstChannelSequenceNo() {
		return fstChannelSequenceNo;
	}

	public void setFstChannelSequenceNo(String fstChannelSequenceNo) {
		this.fstChannelSequenceNo = fstChannelSequenceNo;
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

	public GatewayType getSndGatewayType() {
		return sndGatewayType;
	}

	public void setSndGatewayType(GatewayType sndGatewayType) {
		this.sndGatewayType = sndGatewayType;
	}

	public String getSndPaymentChannelUuid() {
		return sndPaymentChannelUuid;
	}

	public void setSndPaymentChannelUuid(String sndPaymentChannelUuid) {
		this.sndPaymentChannelUuid = sndPaymentChannelUuid;
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

	public Date getSndEffectiveAbsoluteTime() {
		return sndEffectiveAbsoluteTime;
	}

	public void setSndEffectiveAbsoluteTime(Date sndEffectiveAbsoluteTime) {
		this.sndEffectiveAbsoluteTime = sndEffectiveAbsoluteTime;
	}

	public BigDecimal getSndTransactionAmount() {
		return sndTransactionAmount;
	}

	public void setSndTransactionAmount(BigDecimal sndTransactionAmount) {
		this.sndTransactionAmount = sndTransactionAmount;
	}

	public CommunicationStatus getSndCommunicationStatus() {
		return sndCommunicationStatus;
	}

	public void setSndCommunicationStatus(CommunicationStatus sndCommunicationStatus) {
		this.sndCommunicationStatus = sndCommunicationStatus;
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

	public void setSndCommunicationLastSentTime(Date sndCommunicationLastSentTime) {
		this.sndCommunicationLastSentTime = sndCommunicationLastSentTime;
	}

	public BusinessStatus getSndBusinessStatus() {
		return sndBusinessStatus;
	}

	public void setSndBusinessStatus(BusinessStatus sndBusinessStatus) {
		this.sndBusinessStatus = sndBusinessStatus;
	}

	public String getSndChannelSequenceNo() {
		return sndChannelSequenceNo;
	}

	public void setSndChannelSequenceNo(String sndChannelSequenceNo) {
		this.sndChannelSequenceNo = sndChannelSequenceNo;
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

	public GatewayType getTrdGatewayType() {
		return trdGatewayType;
	}

	public void setTrdGatewayType(GatewayType trdGatewayType) {
		this.trdGatewayType = trdGatewayType;
	}

	public String getTrdPaymentChannelUuid() {
		return trdPaymentChannelUuid;
	}

	public void setTrdPaymentChannelUuid(String trdPaymentChannelUuid) {
		this.trdPaymentChannelUuid = trdPaymentChannelUuid;
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

	public Date getTrdEffectiveAbsoluteTime() {
		return trdEffectiveAbsoluteTime;
	}

	public void setTrdEffectiveAbsoluteTime(Date trdEffectiveAbsoluteTime) {
		this.trdEffectiveAbsoluteTime = trdEffectiveAbsoluteTime;
	}

	public BigDecimal getTrdTransactionAmount() {
		return trdTransactionAmount;
	}

	public void setTrdTransactionAmount(BigDecimal trdTransactionAmount) {
		this.trdTransactionAmount = trdTransactionAmount;
	}

	public CommunicationStatus getTrdCommunicationStatus() {
		return trdCommunicationStatus;
	}

	public void setTrdCommunicationStatus(CommunicationStatus trdCommunicationStatus) {
		this.trdCommunicationStatus = trdCommunicationStatus;
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

	public void setTrdCommunicationLastSentTime(Date trdCommunicationLastSentTime) {
		this.trdCommunicationLastSentTime = trdCommunicationLastSentTime;
	}

	public BusinessStatus getTrdBusinessStatus() {
		return trdBusinessStatus;
	}

	public void setTrdBusinessStatus(BusinessStatus trdBusinessStatus) {
		this.trdBusinessStatus = trdBusinessStatus;
	}

	public String getTrdChannelSequenceNo() {
		return trdChannelSequenceNo;
	}

	public void setTrdChannelSequenceNo(String trdChannelSequenceNo) {
		this.trdChannelSequenceNo = trdChannelSequenceNo;
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

	public GatewayType getFthGatewayType() {
		return fthGatewayType;
	}

	public void setFthGatewayType(GatewayType fthGatewayType) {
		this.fthGatewayType = fthGatewayType;
	}

	public String getFthPaymentChannelUuid() {
		return fthPaymentChannelUuid;
	}

	public void setFthPaymentChannelUuid(String fthPaymentChannelUuid) {
		this.fthPaymentChannelUuid = fthPaymentChannelUuid;
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

	public Date getFthEffectiveAbsoluteTime() {
		return fthEffectiveAbsoluteTime;
	}

	public void setFthEffectiveAbsoluteTime(Date fthEffectiveAbsoluteTime) {
		this.fthEffectiveAbsoluteTime = fthEffectiveAbsoluteTime;
	}

	public BigDecimal getFthTransactionAmount() {
		return fthTransactionAmount;
	}

	public void setFthTransactionAmount(BigDecimal fthTransactionAmount) {
		this.fthTransactionAmount = fthTransactionAmount;
	}

	public CommunicationStatus getFthCommunicationStatus() {
		return fthCommunicationStatus;
	}

	public void setFthCommunicationStatus(CommunicationStatus fthCommunicationStatus) {
		this.fthCommunicationStatus = fthCommunicationStatus;
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

	public void setFthCommunicationLastSentTime(Date fthCommunicationLastSentTime) {
		this.fthCommunicationLastSentTime = fthCommunicationLastSentTime;
	}

	public BusinessStatus getFthBusinessStatus() {
		return fthBusinessStatus;
	}

	public void setFthBusinessStatus(BusinessStatus fthBusinessStatus) {
		this.fthBusinessStatus = fthBusinessStatus;
	}

	public String getFthChannelSequenceNo() {
		return fthChannelSequenceNo;
	}

	public void setFthChannelSequenceNo(String fthChannelSequenceNo) {
		this.fthChannelSequenceNo = fthChannelSequenceNo;
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

	public GatewayType getFvthGatewayType() {
		return fvthGatewayType;
	}

	public void setFvthGatewayType(GatewayType fvthGatewayType) {
		this.fvthGatewayType = fvthGatewayType;
	}

	public String getFvthPaymentChannelUuid() {
		return fvthPaymentChannelUuid;
	}

	public void setFvthPaymentChannelUuid(String fvthPaymentChannelUuid) {
		this.fvthPaymentChannelUuid = fvthPaymentChannelUuid;
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

	public Date getFvthEffectiveAbsoluteTime() {
		return fvthEffectiveAbsoluteTime;
	}

	public void setFvthEffectiveAbsoluteTime(Date fvthEffectiveAbsoluteTime) {
		this.fvthEffectiveAbsoluteTime = fvthEffectiveAbsoluteTime;
	}

	public BigDecimal getFvthTransactionAmount() {
		return fvthTransactionAmount;
	}

	public void setFvthTransactionAmount(BigDecimal fvthTransactionAmount) {
		this.fvthTransactionAmount = fvthTransactionAmount;
	}

	public CommunicationStatus getFvthCommunicationStatus() {
		return fvthCommunicationStatus;
	}

	public void setFvthCommunicationStatus(
			CommunicationStatus fvthCommunicationStatus) {
		this.fvthCommunicationStatus = fvthCommunicationStatus;
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

	public void setFvthCommunicationLastSentTime(Date fvthCommunicationLastSentTime) {
		this.fvthCommunicationLastSentTime = fvthCommunicationLastSentTime;
	}

	public BusinessStatus getFvthBusinessStatus() {
		return fvthBusinessStatus;
	}

	public void setFvthBusinessStatus(BusinessStatus fvthBusinessStatus) {
		this.fvthBusinessStatus = fvthBusinessStatus;
	}

	public String getFvthChannelSequenceNo() {
		return fvthChannelSequenceNo;
	}

	public void setFvthChannelSequenceNo(String fvthChannelSequenceNo) {
		this.fvthChannelSequenceNo = fvthChannelSequenceNo;
	}

	public String getFvthBusinessResultMsg() {
		return fvthBusinessResultMsg;
	}

	public void setFvthBusinessResultMsg(String fvthBusinessResultMsg) {
		this.fvthBusinessResultMsg = fvthBusinessResultMsg;
	}

	public String getNotifyUrl() {
		return notifyUrl;
	}

	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}

	public String getBatchDeductApplicationUuid() {
		return batchDeductApplicationUuid;
	}

	public void setBatchDeductApplicationUuid(String batchDeductApplicationUuid) {
		this.batchDeductApplicationUuid = batchDeductApplicationUuid;
	}

	public String getFinancialContractUuid() {
		return financialContractUuid;
	}

	public void setFinancialContractUuid(String financialContractUuid) {
		this.financialContractUuid = financialContractUuid;
	}
}
