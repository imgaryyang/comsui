package com.suidifu.bridgewater.model.v2;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class NotifyModel {
	private String RequestId;
	private String RequestNo;
	private String UniqueId;
	private String DeductId;
	private String amount;
	private int executionStatus;
	private String executionRemark;
	
	private Date lastModifiedTime;
	
	private String financialContractUuid;
	private Map<String, Object> PaidNoticInfos;
	public String getRequestId() {
		return RequestId;
	}
	public NotifyModel(){};
	public void setRequestId(String requestId) {
		RequestId = requestId;
	}
	
	
	public Map<String, Object> getPaidNoticInfos() {
		return PaidNoticInfos;
	}
	public void setPaidNoticInfos(Map<String, Object> paidNoticInfos) {
		PaidNoticInfos = paidNoticInfos;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getRequestNo() {
		return RequestNo;
	}
	public void setRequestNo(String requestNo) {
		RequestNo = requestNo;
	}
	public String getUniqueId() {
		return UniqueId;
	}
	public void setUniqueId(String uniqueId) {
		UniqueId = uniqueId;
	}
	
	public String getExecutionRemark() {
		return executionRemark;
	}
	public void setExecutionRemark(String executionRemark) {
		this.executionRemark = executionRemark;
	}
	public String getDeductId() {
		return DeductId;
	}
	public void setDeductId(String deductId) {
		DeductId = deductId;
	}
	public int getExecutionStatus() {
		return executionStatus;
	}
	public void setExecutionStatus(int executionStatus) {
		this.executionStatus = executionStatus;
	}
	public String getFinancialContractUuid() {
		return financialContractUuid;
	}
	public void setFinancialContractUuid(String financialContractUuid) {
		this.financialContractUuid = financialContractUuid;
	}
	public Date getLastModifiedTime() {
		return lastModifiedTime;
	}
	public void setLastModifiedTime(Date lastModifiedTime) {
		this.lastModifiedTime = lastModifiedTime;
	}
	
	
}