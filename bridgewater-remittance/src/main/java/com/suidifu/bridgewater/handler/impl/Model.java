package com.suidifu.bridgewater.handler.impl;

import java.math.BigDecimal;
import java.util.Date;

import com.demo2do.core.utils.DateUtils;

public class Model {
	private String remittanceApplicationUuid;
	private String financialContractUuid;
	private Long financialContractId;
	private String financialProductCode;
	private BigDecimal plannedTotalAmount;
	private BigDecimal actualTotalAmount;
	private int planNotifyNumber;
	private int actualNotifyNumber;
	private Integer executionStatus;
	private Integer transactionRecipient;
	private String executionRemark;
	private Date createTime;
	private Date oppositeReceiveDate;
	private Date lastModifiedTime;
	private int totalCount;
	private int actualCount;
    private String versionLock;
    private Integer checkStatus;
    private Integer notifyStatus;
    
	public String getRemittanceApplicationUuid() {
		return remittanceApplicationUuid;
	}
	public void setRemittanceApplicationUuid(String remittanceApplicationUuid) {
		this.remittanceApplicationUuid = remittanceApplicationUuid;
	}
	public String getFinancialContractUuid() {
		return financialContractUuid;
	}
	public void setFinancialContractUuid(String financialContractUuid) {
		this.financialContractUuid = financialContractUuid;
	}
	public Long getFinancialContractId() {
		return financialContractId;
	}
	public void setFinancialContractId(Long financialContractId) {
		this.financialContractId = financialContractId;
	}
	public String getFinancialProductCode() {
		return financialProductCode;
	}
	public void setFinancialProductCode(String financialProductCode) {
		this.financialProductCode = financialProductCode;
	}
	public BigDecimal getPlannedTotalAmount() {
		return plannedTotalAmount;
	}
	public void setPlannedTotalAmount(BigDecimal plannedTotalAmount) {
		this.plannedTotalAmount = plannedTotalAmount;
	}
	public BigDecimal getActualTotalAmount() {
		return actualTotalAmount;
	}
	public void setActualTotalAmount(BigDecimal actualTotalAmount) {
		this.actualTotalAmount = actualTotalAmount;
	}
	public int getPlanNotifyNumber() {
		return planNotifyNumber;
	}
	public void setPlanNotifyNumber(int planNotifyNumber) {
		this.planNotifyNumber = planNotifyNumber;
	}
	public int getActualNotifyNumber() {
		return actualNotifyNumber;
	}
	public void setActualNotifyNumber(int actualNotifyNumber) {
		this.actualNotifyNumber = actualNotifyNumber;
	}
	public Integer getExecutionStatus() {
		return executionStatus;
	}
	public void setExecutionStatus(Integer executionStatus) {
		this.executionStatus = executionStatus;
	}
	public Integer getTransactionRecipient() {
		return transactionRecipient;
	}
	public void setTransactionRecipient(Integer transactionRecipient) {
		this.transactionRecipient = transactionRecipient;
	}
	public String getExecutionRemark() {
		return executionRemark;
	}
	public void setExecutionRemark(String executionRemark) {
		this.executionRemark = executionRemark;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getOppositeReceiveDate() {
		return oppositeReceiveDate;
	}
	public void setOppositeReceiveDate(Date oppositeReceiveDate) {
		this.oppositeReceiveDate = oppositeReceiveDate;
	}
	public Date getLastModifiedTime() {
		return lastModifiedTime;
	}
	public void setLastModifiedTime(Date lastModifiedTime) {
		this.lastModifiedTime = lastModifiedTime;
	}
	public int getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	public int getActualCount() {
		return actualCount;
	}
	public void setActualCount(int actualCount) {
		this.actualCount = actualCount;
	}
	public String getVersionLock() {
		return versionLock;
	}
	public void setVersionLock(String versionLock) {
		this.versionLock = versionLock;
	}
	public Integer getCheckStatus() {
		return checkStatus;
	}
	public void setCheckStatus(Integer checkStatus) {
		this.checkStatus = checkStatus;
	}
	public Integer getNotifyStatus() {
		return notifyStatus;
	}
	public void setNotifyStatus(Integer notifyStatus) {
		this.notifyStatus = notifyStatus;
	}
	
	public Model() {}
	
	public Model(String remittanceApplicationUuid, String financialContractUuid, Long financialContractId,
			String financialProductCode, BigDecimal plannedTotalAmount, BigDecimal actualTotalAmount,
			int planNotifyNumber, int actualNotifyNumber, Integer executionStatus, Integer transactionRecipient,
			String executionRemark, Date createTime, Date oppositeReceiveDate, Date lastModifiedTime, int totalCount,
			int actualCount, String versionLock, Integer checkStatus, Integer notifyStatus) {
		super();
		this.remittanceApplicationUuid = remittanceApplicationUuid;
		this.financialContractUuid = financialContractUuid;
		this.financialContractId = financialContractId;
		this.financialProductCode = financialProductCode;
		this.plannedTotalAmount = plannedTotalAmount;
		this.actualTotalAmount = actualTotalAmount;
		this.planNotifyNumber = planNotifyNumber;
		this.actualNotifyNumber = actualNotifyNumber;
		this.executionStatus = executionStatus;
		this.transactionRecipient = transactionRecipient;
		this.executionRemark = executionRemark;
		this.createTime = createTime;
		this.oppositeReceiveDate = oppositeReceiveDate;
		this.lastModifiedTime = lastModifiedTime;
		this.totalCount = totalCount;
		this.actualCount = actualCount;
		this.versionLock = versionLock;
		this.checkStatus = checkStatus;
		this.notifyStatus = notifyStatus;
	}
    public String toString() {
    	String log = "xxxnowtime:["+DateUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss")+"],remittanceApplicationUuid:["+remittanceApplicationUuid+"],financialContractUuid:["+
    			financialContractUuid+"],financialContractId:["+financialContractId+"],financialProductCode:["+financialProductCode+"],plannedTotalAmount:["+plannedTotalAmount+
    			"],actualTotalAmount:["+actualTotalAmount+"],planNotifyNumber:["+planNotifyNumber+"],actualNotifyNumber:["+actualNotifyNumber+"],executionStatus:["+
    			executionStatus+"],transactionRecipient:["+transactionRecipient+"],executionRemark:["+executionRemark+"],createTime:["+createTime+"],oppositeReceiveDate:["+oppositeReceiveDate+
    			"],lastModifiedTime:["+lastModifiedTime+"],totalCount:["+totalCount+"],actualCount:["+actualCount+"],versionLock:["+versionLock+"],checkStatus:["+checkStatus+
    			"],notifyStatus:["+notifyStatus+"].";
    	
		return log;
    	
    }
}
