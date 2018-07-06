/**
 * 
 */
package com.suidifu.bridgewater.api.entity.deduct.batch.v2;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * @author wukai
 * 扣款申请错误表
 */
@Entity(name="t_deduct_application_check_log")
public class DeductPlanApplicationCheckLog {
	
	@Id
	@GeneratedValue
	private Long id;
	
	private String deductId;
	
	private String deductApplicationUuid;
	
	private String requestNo;
	
	private String financialContractUuid;
	private String financialProductCode;
	private String contractUniqueId;
	/**
	 * 
	 */
	@Column(columnDefinition="text")
	private String repaymentPlanCodeList;
	
	private String contractNo;
	private BigDecimal plannedDeductTotalAmount;
	private BigDecimal actualDeductTotalAmount;
	
	private String notifyUrl;
	
	private int transcationType;
	private int repaymentType;
	private int executionStatus;
	private String executionRemark;
	private Date createTime;
	private String creatorName;
	private String ip;
	private Date lastModifiedTime;
	private int recordStatus;
	private int isAvailable;
	private Date apiCalledTime;
	private int transactionRecipient;
	private String customerName;
	private String mobile;
	private String gateway;
	private int sourceType;
	private int thirdPartVoucherStatus;
	private Date completeTime;
	private Date transactionTime;
	private int actualNotifyNumber;
	private int planNotifyNumber;

	/**
	 * 对手方传入的批扣编号，不保证唯一性
	 */
	private String batchDeductId;
	/**
	 * 系统内部产生的编号，保证唯一性
	 */
	private String batchDeductApplicationUuid;
	
	private int noneBusinessCheck;
	private int businessCheck;
	
	private String batchFilePath;
	
	private int currentLineNumber;
	
	private String accountName;
	
	private String acccountNo;
	
	/**
	 * 失败原因
	 */
	@Column(columnDefinition="longtext")
	private String failReason;
	
	/**
	 * 失败类型
	 */
	@Enumerated(EnumType.ORDINAL)
	private ProcessDeductResultFailType processDeductResultFailType;

	public DeductPlanApplicationCheckLog() {
		super();
	}
	
	public DeductPlanApplicationCheckLog(String deductId,
			String deductApplicationUuid, String requestNo,
			String financialContractUuid, String financialProductCode,
			String contractUniqueId, String repaymentPlanCodeList,
			String contractNo, BigDecimal plannedDeductTotalAmount,
			BigDecimal actualDeductTotalAmount, String notifyUrl,
			int transcationType, int repaymentType, int executionStatus,
			String executionRemark, String creatorName, String ip,
			int recordStatus, int isAvailable, Date apiCalledTime,
			int transactionRecipient, String customerName, String mobile,
			String gateway, int sourceType, int thirdPartVoucherStatus,
			Date completeTime, Date transactionTime, int actualNotifyNumber,
			int planNotifyNumber, String batchDeductId, int noneBusinessCheck,
			int businessCheck, String failReason,
			ProcessDeductResultFailType processDeductResultFailType) {
		super();
		this.deductId = deductId;
		this.deductApplicationUuid = deductApplicationUuid;
		this.requestNo = requestNo;
		this.financialContractUuid = financialContractUuid;
		this.financialProductCode = financialProductCode;
		this.contractUniqueId = contractUniqueId;
		this.repaymentPlanCodeList = repaymentPlanCodeList;
		this.contractNo = contractNo;
		this.plannedDeductTotalAmount = plannedDeductTotalAmount;
		this.actualDeductTotalAmount = actualDeductTotalAmount;
		this.notifyUrl = notifyUrl;
		this.transcationType = transcationType;
		this.repaymentType = repaymentType;
		this.executionStatus = executionStatus;
		this.executionRemark = executionRemark;
		this.creatorName = creatorName;
		this.ip = ip;
		this.recordStatus = recordStatus;
		this.isAvailable = isAvailable;
		this.apiCalledTime = apiCalledTime;
		this.transactionRecipient = transactionRecipient;
		this.customerName = customerName;
		this.mobile = mobile;
		this.gateway = gateway;
		this.sourceType = sourceType;
		this.thirdPartVoucherStatus = thirdPartVoucherStatus;
		this.completeTime = completeTime;
		this.transactionTime = transactionTime;
		this.actualNotifyNumber = actualNotifyNumber;
		this.planNotifyNumber = planNotifyNumber;
		this.batchDeductId = batchDeductId;
		this.noneBusinessCheck = noneBusinessCheck;
		this.businessCheck = businessCheck;
		this.failReason = failReason;
		this.processDeductResultFailType = processDeductResultFailType;
		this.createTime = new Date();
		this.lastModifiedTime = new Date();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}



	public String getDeductId() {
		return deductId;
	}



	public void setDeductId(String deductId) {
		this.deductId = deductId;
	}



	public String getDeductApplicationUuid() {
		return deductApplicationUuid;
	}



	public void setDeductApplicationUuid(String deductApplicationUuid) {
		this.deductApplicationUuid = deductApplicationUuid;
	}



	public String getRequestNo() {
		return requestNo;
	}



	public void setRequestNo(String requestNo) {
		this.requestNo = requestNo;
	}



	public String getFinancialContractUuid() {
		return financialContractUuid;
	}



	public void setFinancialContractUuid(String financialContractUuid) {
		this.financialContractUuid = financialContractUuid;
	}



	public String getFinancialProductCode() {
		return financialProductCode;
	}



	public void setFinancialProductCode(String financialProductCode) {
		this.financialProductCode = financialProductCode;
	}



	public String getContractUniqueId() {
		return contractUniqueId;
	}



	public void setContractUniqueId(String contractUniqueId) {
		this.contractUniqueId = contractUniqueId;
	}



	public String getRepaymentPlanCodeList() {
		return repaymentPlanCodeList;
	}



	public void setRepaymentPlanCodeList(String repaymentPlanCodeList) {
		this.repaymentPlanCodeList = repaymentPlanCodeList;
	}



	public String getContractNo() {
		return contractNo;
	}



	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}



	public BigDecimal getPlannedDeductTotalAmount() {
		return plannedDeductTotalAmount;
	}



	public void setPlannedDeductTotalAmount(BigDecimal plannedDeductTotalAmount) {
		this.plannedDeductTotalAmount = plannedDeductTotalAmount;
	}



	public BigDecimal getActualDeductTotalAmount() {
		return actualDeductTotalAmount;
	}



	public void setActualDeductTotalAmount(BigDecimal actualDeductTotalAmount) {
		this.actualDeductTotalAmount = actualDeductTotalAmount;
	}



	public String getNotifyUrl() {
		return notifyUrl;
	}



	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}



	public int getTranscationType() {
		return transcationType;
	}



	public void setTranscationType(int transcationType) {
		this.transcationType = transcationType;
	}



	public int getRepaymentType() {
		return repaymentType;
	}



	public void setRepaymentType(int repaymentType) {
		this.repaymentType = repaymentType;
	}



	public int getExecutionStatus() {
		return executionStatus;
	}



	public void setExecutionStatus(int executionStatus) {
		this.executionStatus = executionStatus;
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



	public String getCreatorName() {
		return creatorName;
	}



	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}



	public String getIp() {
		return ip;
	}



	public void setIp(String ip) {
		this.ip = ip;
	}



	public Date getLastModifiedTime() {
		return lastModifiedTime;
	}



	public void setLastModifiedTime(Date lastModifiedTime) {
		this.lastModifiedTime = lastModifiedTime;
	}



	public int getRecordStatus() {
		return recordStatus;
	}



	public void setRecordStatus(int recordStatus) {
		this.recordStatus = recordStatus;
	}



	public int getIsAvailable() {
		return isAvailable;
	}



	public void setIsAvailable(int isAvailable) {
		this.isAvailable = isAvailable;
	}



	public Date getApiCalledTime() {
		return apiCalledTime;
	}



	public void setApiCalledTime(Date apiCalledTime) {
		this.apiCalledTime = apiCalledTime;
	}



	public int getTransactionRecipient() {
		return transactionRecipient;
	}



	public void setTransactionRecipient(int transactionRecipient) {
		this.transactionRecipient = transactionRecipient;
	}



	public String getCustomerName() {
		return customerName;
	}



	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}



	public String getMobile() {
		return mobile;
	}



	public void setMobile(String mobile) {
		this.mobile = mobile;
	}



	public String getGateway() {
		return gateway;
	}



	public void setGateway(String gateway) {
		this.gateway = gateway;
	}



	public int getSourceType() {
		return sourceType;
	}



	public void setSourceType(int sourceType) {
		this.sourceType = sourceType;
	}



	public int getThirdPartVoucherStatus() {
		return thirdPartVoucherStatus;
	}



	public void setThirdPartVoucherStatus(int thirdPartVoucherStatus) {
		this.thirdPartVoucherStatus = thirdPartVoucherStatus;
	}



	public Date getCompleteTime() {
		return completeTime;
	}



	public void setCompleteTime(Date completeTime) {
		this.completeTime = completeTime;
	}



	public Date getTransactionTime() {
		return transactionTime;
	}



	public void setTransactionTime(Date transactionTime) {
		this.transactionTime = transactionTime;
	}



	public int getActualNotifyNumber() {
		return actualNotifyNumber;
	}



	public void setActualNotifyNumber(int actualNotifyNumber) {
		this.actualNotifyNumber = actualNotifyNumber;
	}



	public int getPlanNotifyNumber() {
		return planNotifyNumber;
	}



	public void setPlanNotifyNumber(int planNotifyNumber) {
		this.planNotifyNumber = planNotifyNumber;
	}



	public String getBatchDeductId() {
		return batchDeductId;
	}



	public void setBatchDeductId(String batchDeductId) {
		this.batchDeductId = batchDeductId;
	}



	public int getNoneBusinessCheck() {
		return noneBusinessCheck;
	}



	public void setNoneBusinessCheck(int noneBusinessCheck) {
		this.noneBusinessCheck = noneBusinessCheck;
	}



	public int getBusinessCheck() {
		return businessCheck;
	}



	public void setBusinessCheck(int businessCheck) {
		this.businessCheck = businessCheck;
	}



	public String getFailReason() {
		return failReason;
	}
	public void setFailReason(String failReason) {
		this.failReason = failReason;
	}
	public ProcessDeductResultFailType getProcessDeductResultFailType() {
		return processDeductResultFailType;
	}
	public void setProcessDeductResultFailType(
			ProcessDeductResultFailType processDeductResultFailType) {
		this.processDeductResultFailType = processDeductResultFailType;
	}

	public String getBatchFilePath() {
		return batchFilePath;
	}

	public void setBatchFilePath(String batchFilePath) {
		this.batchFilePath = batchFilePath;
	}

	public int getCurrentLineNumber() {
		return currentLineNumber;
	}

	public void setCurrentLineNumber(int currentLineNumber) {
		this.currentLineNumber = currentLineNumber;
	}

	public String getBatchDeductApplicationUuid() {
		return batchDeductApplicationUuid;
	}

	public void setBatchDeductApplicationUuid(String batchDeductApplicationUuid) {
		this.batchDeductApplicationUuid = batchDeductApplicationUuid;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getAcccountNo() {
		return acccountNo;
	}

	public void setAcccountNo(String acccountNo) {
		this.acccountNo = acccountNo;
	}
}