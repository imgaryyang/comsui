package com.suidifu.bridgewater.api.model;

import java.util.List;

public class RemittanceQueryResult {
	
	private String uniqueId;
	
	private String contractNo;

	private Integer executionStatus;
	
	private String oriRequestNo;
	
	private String remittanceId;
	
	private List<RemittanceResultDetail> remittanceResultDetails;

	public String getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}

	public String getContractNo() {
		return contractNo;
	}

	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}

	public Integer getExecutionStatus() {
		return executionStatus;
	}

	public void setExecutionStatus(Integer executionStatus) {
		this.executionStatus = executionStatus;
	}

	public String getOriRequestNo() {
		return oriRequestNo;
	}

	public void setOriRequestNo(String oriRequestNo) {
		this.oriRequestNo = oriRequestNo;
	}

	public String getRemittanceId() {
		return remittanceId;
	}

	public void setRemittanceId(String remittanceId) {
		this.remittanceId = remittanceId;
	}

	public List<RemittanceResultDetail> getRemittanceResultDetails() {
		return remittanceResultDetails;
	}

	public void setRemittanceResultDetails(
			List<RemittanceResultDetail> remittanceResultDetails) {
		this.remittanceResultDetails = remittanceResultDetails;
	}
	
}
