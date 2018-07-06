package com.suidifu.matryoshka.delayTask;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.MappedSuperclass;

import com.suidifu.matryoshka.delayTask.enums.DelayTaskExecuteStatus;

/**
 * 后置处理任务 Created by louguanyang on 2017/5/2.
 */
@MappedSuperclass
// @Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class BaseDelayProcessingTask {

	private String uuid;

	/**
	 * 后置任务配置Uuid
	 */
	private String configUuid;

	/** 还款计划uuid **/
	private String repaymentPlanUuid;

	/** 回购单Uuid **/
	private String repurchaseDocUuid;

	private Date taskExecuteDate;

	/** 相关数据 json格式 **/
	private String workParams;

	/**
	 * 执行状态
	 * 
	 * @see DelayTaskExecuteStatus
	 */
	private Integer executeStatus = DelayTaskExecuteStatus.CREATE.getCode();

	private Date createTime;

	private Date lastModifyTime;

	private String financialContractUuid;

	private String contractUuid;

	private String customerUuid;

	/**
	 * 预留字段
	 **/
	private Date dateFieldOne;

	private Date dateFieldTwo;

	private Date dateFieldThree;

	private Long longFieldOne;

	private Long longFieldTwo;

	private Long longFieldThree;

	private String stringFieldOne;

	private String stringFieldTwo;

	private String stringFieldThree;

	private BigDecimal decimalFieldOne;

	private BigDecimal decimalFieldTwo;

	private BigDecimal decimalFieldThree;

	public BaseDelayProcessingTask() {

	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getConfigUuid() {
		return configUuid;
	}

	public void setConfigUuid(String configUuid) {
		this.configUuid = configUuid;
	}

	public String getRepaymentPlanUuid() {
		return repaymentPlanUuid;
	}

	public void setRepaymentPlanUuid(String repaymentPlanUuid) {
		this.repaymentPlanUuid = repaymentPlanUuid;
	}

	public String getRepurchaseDocUuid() {
		return repurchaseDocUuid;
	}

	public void setRepurchaseDocUuid(String repurchaseDocUuid) {
		this.repurchaseDocUuid = repurchaseDocUuid;
	}

	public Date getTaskExecuteDate() {
		return taskExecuteDate;
	}

	public void setTaskExecuteDate(Date taskExecuteDate) {
		this.taskExecuteDate = taskExecuteDate;
	}

	public String getWorkParams() {
		return workParams;
	}

	public void setWorkParams(String workParams) {
		this.workParams = workParams;
	}

	public Integer getExecuteStatus() {
		return executeStatus;
	}

	public void setExecuteStatus(Integer executeStatus) {
		this.executeStatus = executeStatus;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getLastModifyTime() {
		return lastModifyTime;
	}

	public void setLastModifyTime(Date lastModifyTime) {
		this.lastModifyTime = lastModifyTime;
	}

	public String getFinancialContractUuid() {
		return financialContractUuid;
	}

	public void setFinancialContractUuid(String financialContractUuid) {
		this.financialContractUuid = financialContractUuid;
	}

	public String getContractUuid() {
		return contractUuid;
	}

	public void setContractUuid(String contractUuid) {
		this.contractUuid = contractUuid;
	}

	public String getCustomerUuid() {
		return customerUuid;
	}

	public void setCustomerUuid(String customerUuid) {
		this.customerUuid = customerUuid;
	}

	public Date getDateFieldOne() {
		return dateFieldOne;
	}

	public void setDateFieldOne(Date dateFieldOne) {
		this.dateFieldOne = dateFieldOne;
	}

	public Date getDateFieldTwo() {
		return dateFieldTwo;
	}

	public void setDateFieldTwo(Date dateFieldTwo) {
		this.dateFieldTwo = dateFieldTwo;
	}

	public Date getDateFieldThree() {
		return dateFieldThree;
	}

	public void setDateFieldThree(Date dateFieldThree) {
		this.dateFieldThree = dateFieldThree;
	}

	public Long getLongFieldOne() {
		return longFieldOne;
	}

	public void setLongFieldOne(Long longFieldOne) {
		this.longFieldOne = longFieldOne;
	}

	public Long getLongFieldTwo() {
		return longFieldTwo;
	}

	public void setLongFieldTwo(Long longFieldTwo) {
		this.longFieldTwo = longFieldTwo;
	}

	public Long getLongFieldThree() {
		return longFieldThree;
	}

	public void setLongFieldThree(Long longFieldThree) {
		this.longFieldThree = longFieldThree;
	}

	public String getStringFieldOne() {
		return stringFieldOne;
	}

	public void setStringFieldOne(String stringFieldOne) {
		this.stringFieldOne = stringFieldOne;
	}

	public String getStringFieldTwo() {
		return stringFieldTwo;
	}

	public void setStringFieldTwo(String stringFieldTwo) {
		this.stringFieldTwo = stringFieldTwo;
	}

	public String getStringFieldThree() {
		return stringFieldThree;
	}

	public void setStringFieldThree(String stringFieldThree) {
		this.stringFieldThree = stringFieldThree;
	}

	public BigDecimal getDecimalFieldOne() {
		return decimalFieldOne;
	}

	public void setDecimalFieldOne(BigDecimal decimalFieldOne) {
		this.decimalFieldOne = decimalFieldOne;
	}

	public BigDecimal getDecimalFieldTwo() {
		return decimalFieldTwo;
	}

	public void setDecimalFieldTwo(BigDecimal decimalFieldTwo) {
		this.decimalFieldTwo = decimalFieldTwo;
	}

	public BigDecimal getDecimalFieldThree() {
		return decimalFieldThree;
	}

	public void setDecimalFieldThree(BigDecimal decimalFieldThree) {
		this.decimalFieldThree = decimalFieldThree;
	}
}
