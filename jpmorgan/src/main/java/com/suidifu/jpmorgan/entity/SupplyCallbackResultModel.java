package com.suidifu.jpmorgan.entity;

import com.suidifu.coffer.entity.ResultBaseModel;

public class SupplyCallbackResultModel extends ResultBaseModel {
	
	private String transactionUuid;

	private BusinessStatus businessStatus;
	
	private String businessResultMsg;

	public SupplyCallbackResultModel(String transactionUuid,
			BusinessStatus businessStatus, String businessResultMsg) {
		super();
		this.transactionUuid = transactionUuid;
		this.businessStatus = businessStatus;
		this.businessResultMsg = businessResultMsg;
	}

	public SupplyCallbackResultModel() {
		super();
	}

	public String getTransactionUuid() {
		return transactionUuid;
	}

	public void setTransactionUuid(String transactionUuid) {
		this.transactionUuid = transactionUuid;
	}

	public BusinessStatus getBusinessStatus() {
		return businessStatus;
	}

	public void setBusinessStatus(BusinessStatus businessStatus) {
		this.businessStatus = businessStatus;
	}

	public String getBusinessResultMsg() {
		return businessResultMsg;
	}

	public void setBusinessResultMsg(String businessResultMsg) {
		this.businessResultMsg = businessResultMsg;
	}

	
}
