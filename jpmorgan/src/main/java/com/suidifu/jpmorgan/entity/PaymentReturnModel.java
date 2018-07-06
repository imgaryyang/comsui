package com.suidifu.jpmorgan.entity;

public class PaymentReturnModel {

	private String sourceMessageUuid;

	private String outlierTransactionUuid;

	private String tradeUuid;
	
	private String fstSlotUuid;
	
	private String sndSlotUuid;
	
	private String trdSlotUuid;
	
	private String fthSlotUuid;
	
	private String fvthSlotUuid;

	public PaymentReturnModel() {
		super();
	}

	public PaymentReturnModel(String sourceMessageUuid,
			String outlierTransactionUuid, String tradeUuid) {
		super();
		this.sourceMessageUuid = sourceMessageUuid;
		this.outlierTransactionUuid = outlierTransactionUuid;
		this.tradeUuid = tradeUuid;
	}

	public PaymentReturnModel(String sourceMessageUuid,
			String outlierTransactionUuid, String tradeUuid,
			String fstSlotUuid, String sndSlotUuid, String trdSlotUuid,
			String fthSlotUuid, String fvthSlotUuid) {
		super();
		this.sourceMessageUuid = sourceMessageUuid;
		this.outlierTransactionUuid = outlierTransactionUuid;
		this.tradeUuid = tradeUuid;
		this.fstSlotUuid = fstSlotUuid;
		this.sndSlotUuid = sndSlotUuid;
		this.trdSlotUuid = trdSlotUuid;
		this.fthSlotUuid = fthSlotUuid;
		this.fvthSlotUuid = fvthSlotUuid;
	}

	public String getSourceMessageUuid() {
		return sourceMessageUuid;
	}

	public void setSourceMessageUuid(String sourceMessageUuid) {
		this.sourceMessageUuid = sourceMessageUuid;
	}

	public String getOutlierTransactionUuid() {
		return outlierTransactionUuid;
	}

	public void setOutlierTransactionUuid(String outlierTransactionUuid) {
		this.outlierTransactionUuid = outlierTransactionUuid;
	}

	public String getTradeUuid() {
		return tradeUuid;
	}

	public void setTradeUuid(String tradeUuid) {
		this.tradeUuid = tradeUuid;
	}

	public String getFstSlotUuid() {
		return fstSlotUuid;
	}

	public void setFstSlotUuid(String fstSlotUuid) {
		this.fstSlotUuid = fstSlotUuid;
	}

	public String getSndSlotUuid() {
		return sndSlotUuid;
	}

	public void setSndSlotUuid(String sndSlotUuid) {
		this.sndSlotUuid = sndSlotUuid;
	}

	public String getTrdSlotUuid() {
		return trdSlotUuid;
	}

	public void setTrdSlotUuid(String trdSlotUuid) {
		this.trdSlotUuid = trdSlotUuid;
	}

	public String getFthSlotUuid() {
		return fthSlotUuid;
	}

	public void setFthSlotUuid(String fthSlotUuid) {
		this.fthSlotUuid = fthSlotUuid;
	}

	public String getFvthSlotUuid() {
		return fvthSlotUuid;
	}

	public void setFvthSlotUuid(String fvthSlotUuid) {
		this.fvthSlotUuid = fvthSlotUuid;
	}

}
