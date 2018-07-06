package com.suidifu.bridgewater.api.entity.deduct.batch.v2;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "t_batch_deduct_application")
public class BatchDeductApplication {

	@Id
	@GeneratedValue
	private Long id;

	private String batchDeductApplicationUuid;

	private String batchDeductId;

	private String requestNo;

	private String financialContractUuid;

	private String financialProductCode;

	private String filePath;

	private String notifyUrl;

	@Enumerated(EnumType.ORDINAL)
	private BatchProcessStatus batchProcessStatus;// 落盘 处理中 处理完成

	@Enumerated(EnumType.ORDINAL)
	private NotifyStatus batchNotifyStatus;// 未回调 已回调 回调失败

	private String remark;

	private Date createTime;

	private String ip;

	private Date lastModifiedTime;
	
	//总文件行数
	private Integer totalCount;
	
	//实际总条数［可能拆单］
	private Integer actualCount;
	
	//执行总条数
	private Integer executedCount;
	
	//失败总次数[包括重试次数]
	private Integer failCount;
	
	//失败的信息
	private String failMsg;

	private String version  = UUID.randomUUID().toString();
	
	private String reservedField1;
	private BigDecimal reservedField2;
	private Date reservedField3;
	
	private String reservedField4;
	private BigDecimal reservedField5;
	private Date reservedField6;
	
	private String reservedField7;
	private BigDecimal reservedField8;
	private Date reservedField9;

	public BatchDeductApplication() {
		super();
	}

	public BatchDeductApplication(String batchDeductApplicationUuid,
			String batchDeductId, String requestNo, String filePath,
			String notifyUrl) {
		super();
		this.batchDeductApplicationUuid = batchDeductApplicationUuid;
		this.batchDeductId = batchDeductId;
		this.requestNo = requestNo;
		this.filePath = filePath;
		this.notifyUrl = notifyUrl;
		this.actualCount=0;
		this.executedCount=0;
		this.failCount=0;
		this.batchNotifyStatus=batchNotifyStatus.Waiting;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getBatchDeductApplicationUuid() {
		return batchDeductApplicationUuid;
	}

	public void setBatchDeductApplicationUuid(String batchDeductApplicationUuid) {
		this.batchDeductApplicationUuid = batchDeductApplicationUuid;
	}

	public String getBatchDeductId() {
		return batchDeductId;
	}

	public void setBatchDeductId(String batchDeductId) {
		this.batchDeductId = batchDeductId;
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

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getNotifyUrl() {
		return notifyUrl;
	}

	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}

	public BatchProcessStatus getBatchProcessStatus() {
		return batchProcessStatus;
	}

	public void setBatchProcessStatus(BatchProcessStatus batchProcessStatus) {
		this.batchProcessStatus = batchProcessStatus;
	}

	public NotifyStatus getBatchNotifyStatus() {
		return batchNotifyStatus;
	}

	public void setBatchNotifyStatus(NotifyStatus batchNotifyStatus) {
		this.batchNotifyStatus = batchNotifyStatus;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
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

	public String getReservedField1() {
		return reservedField1;
	}

	public void setReservedField1(String reservedField1) {
		this.reservedField1 = reservedField1;
	}

	public BigDecimal getReservedField2() {
		return reservedField2;
	}

	public void setReservedField2(BigDecimal reservedField2) {
		this.reservedField2 = reservedField2;
	}

	public Date getReservedField3() {
		return reservedField3;
	}

	public void setReservedField3(Date reservedField3) {
		this.reservedField3 = reservedField3;
	}

	public String getReservedField4() {
		return reservedField4;
	}

	public void setReservedField4(String reservedField4) {
		this.reservedField4 = reservedField4;
	}

	public BigDecimal getReservedField5() {
		return reservedField5;
	}

	public void setReservedField5(BigDecimal reservedField5) {
		this.reservedField5 = reservedField5;
	}

	public Date getReservedField6() {
		return reservedField6;
	}

	public void setReservedField6(Date reservedField6) {
		this.reservedField6 = reservedField6;
	}

	public String getReservedField7() {
		return reservedField7;
	}

	public void setReservedField7(String reservedField7) {
		this.reservedField7 = reservedField7;
	}

	public BigDecimal getReservedField8() {
		return reservedField8;
	}

	public void setReservedField8(BigDecimal reservedField8) {
		this.reservedField8 = reservedField8;
	}

	public Date getReservedField9() {
		return reservedField9;
	}

	public void setReservedField9(Date reservedField9) {
		this.reservedField9 = reservedField9;
	}

	public Integer getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}

	public Integer getFailCount() {
		return failCount;
	}

	public void setFailCount(Integer failCount) {
		this.failCount = failCount;
	}

	public String getFailMsg() {
		return failMsg;
	}

	public void setFailMsg(String failMsg) {
		this.failMsg = failMsg;
	}

	public Integer getActualCount() {
		return actualCount;
	}

	public void setActualCount(Integer actualCount) {
		this.actualCount = actualCount;
	}

	public Integer getExecutedCount() {
		return executedCount;
	}

	public void setExecutedCount(Integer executedCount) {
		this.executedCount = executedCount;
	}
}
