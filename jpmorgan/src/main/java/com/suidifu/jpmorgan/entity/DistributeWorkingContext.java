package com.suidifu.jpmorgan.entity;


public class DistributeWorkingContext {

	private String paymentOrderQueueTableName;

	private Integer queueIndex;

	private String tradeInfo;
	
	private String innerCallbackInfo;

	public String getPaymentOrderQueueTableName() {
		return paymentOrderQueueTableName;
	}

	public void setPaymentOrderQueueTableName(String paymentOrderQueueTableName) {
		this.paymentOrderQueueTableName = paymentOrderQueueTableName;
	}

	public Integer getQueueIndex() {
		return queueIndex;
	}

	public void setQueueIndex(Integer queueIndex) {
		this.queueIndex = queueIndex;
	}

	public String getTradeInfo() {
		return tradeInfo;
	}

	public void setTradeInfo(String tradeInfo) {
		this.tradeInfo = tradeInfo;
	}

	public void setInnerCallbackInfo(String innerCallbackInfo) {
		this.innerCallbackInfo = innerCallbackInfo;
	}

	public String getInnerCallbackInfo() {
		return innerCallbackInfo;
	}


}
