package com.suidifu.jpmorgan.entity;

import java.math.BigDecimal;
import java.util.Date;

public class SlotInfo {

	private String tradeUuid;

	private Integer nthSlot;

	private String slotUuid;

	private GatewayType gatewayType;

	private String paymentChannelUuid;

	private Date effectiveTime;

	private BigDecimal transactionAmount;

	public SlotInfo() {
		super();
	}

	public SlotInfo(String tradeUuid, int nthSlot, String slotUuid,
			GatewayType gatewayType, String paymentChannelUuid,
			Date effectiveTime, BigDecimal transactionAmount) {
		super();
		this.tradeUuid = tradeUuid;
		this.nthSlot = nthSlot;
		this.slotUuid = slotUuid;
		this.gatewayType = gatewayType;
		this.paymentChannelUuid = paymentChannelUuid;
		this.effectiveTime = effectiveTime;
		this.transactionAmount = transactionAmount;
	}

	public String getTradeUuid() {
		return tradeUuid;
	}

	public void setTradeUuid(String tradeUuid) {
		this.tradeUuid = tradeUuid;
	}

	public int getNthSlot() {
		return nthSlot;
	}

	public void setNthSlot(int nthSlot) {
		this.nthSlot = nthSlot;
	}

	public String getSlotUuid() {
		return slotUuid;
	}

	public void setSlotUuid(String slotUuid) {
		this.slotUuid = slotUuid;
	}

	public GatewayType getGatewayType() {
		return gatewayType;
	}

	public void setGatewayType(GatewayType gatewayType) {
		this.gatewayType = gatewayType;
	}

	public String getPaymentChannelUuid() {
		return paymentChannelUuid;
	}

	public void setPaymentChannelUuid(String paymentChannelUuid) {
		this.paymentChannelUuid = paymentChannelUuid;
	}

	public Date getEffectiveTime() {
		return effectiveTime;
	}

	public void setEffectiveTime(Date effectiveTime) {
		this.effectiveTime = effectiveTime;
	}

	public BigDecimal getTransactionAmount() {
		return transactionAmount;
	}

	public void setTransactionAmount(BigDecimal transactionAmount) {
		this.transactionAmount = transactionAmount;
	}

	public boolean hasNecessaryAttr() {
		return null != this.tradeUuid && null != this.nthSlot
				&& null != this.slotUuid && null != this.transactionAmount
				&& null != this.paymentChannelUuid && null != this.gatewayType
				&& null != this.effectiveTime;
	}
}
