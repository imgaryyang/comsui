package com.suidifu.bridgewater.fast;

import com.suidifu.giotto.handler.DataTemperature;
import com.suidifu.giotto.handler.FastCacheObjectWithTemperature;
import com.suidifu.giotto.keyenum.FastKey;
import com.suidifu.giotto.util.SqlAndParamTuple;
import com.zufangbao.sun.yunxin.entity.remittance.ExecutionStatus;
import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.util.*;

public class FastRemittanceApplicationDetail extends FastCacheObjectWithTemperature {

	private Long id;

	/**
	 * 放款申请明细uuid
	 */
	private String remittanceApplicationDetailUuid;
	/**
	 * 放款申请uuid
	 */
	private String remittanceApplicationUuid;

	/**
	 * 信托合同uuid
	 */
	private String financialContractUuid;

	/**
	 * 信托合同Id
	 */
	private Long financialContractId;

	/**
	 * 业务记录号
	 */
	private String businessRecordNo;

	/**
	 * 优先级
	 */
	private int priorityLevel;

	/**
	 * 恒生银行编码
	 */
	private String cpBankCode;
	/**
	 * 交易对手方银行卡号
	 */
	private String cpBankCardNo;
	/**
	 * 交易对手方银行卡持有人
	 */
	private String cpBankAccountHolder;

	/**
	 * 交易对手方证件类型
	 */
	private Integer cpIdType;

	/**
	 * 交易对手方证件号
	 */
	private String cpIdNumber;

	/**
	 * 交易对手方开户行所在省
	 */
	private String cpBankProvince;

	/**
	 * 交易对手方开户行所在市
	 */
	private String cpBankCity;

	/**
	 * 交易对手方开户行名称
	 */
	private String cpBankName;

	/**
	 * 计划支付时间
	 */
	private Date plannedPaymentDate;
	/**
	 * 实际支付完成时间
	 */
	private Date completePaymentDate;

	/**
	 * 计划交易总金额
	 */
	private BigDecimal plannedTotalAmount;
	/**
	 * 实际交易总金额
	 */
	private BigDecimal actualTotalAmount;

	/**
	 * 执行状态, 0:已创建、1:处理中、2:成功、3:失败、4:异常、5:撤销
	 */
	private Integer executionStatus;

	/**
	 * 执行备注
	 */
	private String executionRemark;

	private Date createTime;

	private String creatorName;

	private Date lastModifiedTime;

	/**
	 * RemittancePlan总个数
	 */
	private int totalCount = 0;

	/**
	 * RemittancePlan实际完成个数
	 */
	private int actualCount = 0;

	/**
	 * 版本锁
	 */
	private String versionLock;

	@Override
	public String obtainAddCacheKey() {
		if (StringUtils.isBlank(getRemittanceApplicationDetailUuid())) {
			throw new RuntimeException("all keys value is null.");
		}
		String result = FastRemittanceApplicationDetailEnum.PREFIX_KEY;
		if (StringUtils.isBlank(getRemittanceApplicationDetailUuid())) {
			result = result.concat(":");
		} else {
			result = result.concat(this.getRemittanceApplicationDetailUuid()).concat(":");
		}
		return result;
	}

	@Override
	public SqlAndParamTuple obtainInsertSqlAndParam() {
		throw new RuntimeException("prohibited use:obtainInsertSqlAndParam");
	}

	@Override
	public String obtainQueryCheckMD5Sql(String updateSql) {
		throw new RuntimeException("prohibited use:obtainQueryCheckMD5Sql");
	}

	@Override
	public FastKey getColumnName() {
		return FastRemittanceApplicationDetailEnum.REMITTANCE_APPLICATION_DETAIL_UUID;

	}

	@Override
	public String getColumnValue() {
		return getRemittanceApplicationDetailUuid();
	}

	@Override
	public List<String> obtainAddCacheKeyList() {
		return new ArrayList<String>() {
			private static final long serialVersionUID = 1100812038011985094L;

			{
				if (StringUtils.isNotBlank(getRemittanceApplicationDetailUuid())) {
					add(FastRemittanceApplicationDetailEnum.PREFIX_KEY.concat(getRemittanceApplicationDetailUuid()));
				}
			}
		};
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRemittanceApplicationDetailUuid() {
		return remittanceApplicationDetailUuid;
	}

	public void setRemittanceApplicationDetailUuid(String remittanceApplicationDetailUuid) {
		this.remittanceApplicationDetailUuid = remittanceApplicationDetailUuid;
	}

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

	public String getBusinessRecordNo() {
		return businessRecordNo;
	}

	public void setBusinessRecordNo(String businessRecordNo) {
		this.businessRecordNo = businessRecordNo;
	}

	public int getPriorityLevel() {
		return priorityLevel;
	}

	public void setPriorityLevel(int priorityLevel) {
		this.priorityLevel = priorityLevel;
	}

	public String getCpBankCode() {
		return cpBankCode;
	}

	public void setCpBankCode(String cpBankCode) {
		this.cpBankCode = cpBankCode;
	}

	public String getCpBankCardNo() {
		return cpBankCardNo;
	}

	public void setCpBankCardNo(String cpBankCardNo) {
		this.cpBankCardNo = cpBankCardNo;
	}

	public String getCpBankAccountHolder() {
		return cpBankAccountHolder;
	}

	public void setCpBankAccountHolder(String cpBankAccountHolder) {
		this.cpBankAccountHolder = cpBankAccountHolder;
	}

	public Integer getCpIdType() {
		return cpIdType;
	}

	public void setCpIdType(Integer cpIdType) {
		this.cpIdType = cpIdType;
	}

	public String getCpIdNumber() {
		return cpIdNumber;
	}

	public void setCpIdNumber(String cpIdNumber) {
		this.cpIdNumber = cpIdNumber;
	}

	public String getCpBankProvince() {
		return cpBankProvince;
	}

	public void setCpBankProvince(String cpBankProvince) {
		this.cpBankProvince = cpBankProvince;
	}

	public String getCpBankCity() {
		return cpBankCity;
	}

	public void setCpBankCity(String cpBankCity) {
		this.cpBankCity = cpBankCity;
	}

	public String getCpBankName() {
		return cpBankName;
	}

	public void setCpBankName(String cpBankName) {
		this.cpBankName = cpBankName;
	}

	public Date getPlannedPaymentDate() {
		return plannedPaymentDate;
	}

	public void setPlannedPaymentDate(Date plannedPaymentDate) {
		this.plannedPaymentDate = plannedPaymentDate;
	}

	public Date getCompletePaymentDate() {
		return completePaymentDate;
	}

	public void setCompletePaymentDate(Date completePaymentDate) {
		this.completePaymentDate = completePaymentDate;
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

	public Integer getExecutionStatus() {
		return executionStatus;
	}

	public void setExecutionStatus(Integer executionStatus) {
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

	@Override
	public DataTemperature temperature() {
		if (executionStatus == ExecutionStatus.SUCCESS.ordinal() || executionStatus == ExecutionStatus.FAIL.ordinal() || executionStatus == ExecutionStatus.ABANDON
				.ordinal()) {
			return DataTemperature.FROZEN;
		} else {
			return DataTemperature.HOT;
		}
	}

	@Override
	public SqlAndParamTuple updateSqlAndParamTupleWithVersionLock(String oldVersion) {
		String sql = " UPDATE t_remittance_application_detail "
				+ " SET actual_total_amount =:actualTotalAmount, " + " execution_status =:executionStatus, "
				+ " execution_remark =:executionRemark, " + " complete_payment_date =:completePaymentDate, "
				+ " last_modified_time =:lastModifiedTime, " + " version_lock =:newVersion, "
				+ " actual_count =:actualCount "
				+ " WHERE remittance_application_detail_uuid =:remittanceApplicationDetailUuid AND version_lock =:oldVersion";


		Map<String, Object> params = new HashMap<String, Object>();
		params.put("actualTotalAmount", actualTotalAmount);
		params.put("executionStatus", executionStatus);
		params.put("executionRemark", executionRemark);
		params.put("completePaymentDate", completePaymentDate);
		params.put("lastModifiedTime", lastModifiedTime);
		params.put("newVersion", versionLock);
		params.put("oldVersion", oldVersion);
		params.put("actualCount", actualCount);
		params.put("remittanceApplicationDetailUuid", remittanceApplicationDetailUuid);
		return new SqlAndParamTuple(sql, params);
	}

	@Override
	public SqlAndParamTuple checkAfterUpdateSqlAndParamTuple() {
		String validate_sql = "select remittance_application_detail_uuid from t_remittance_application_detail WHERE remittance_application_detail_uuid=:remittanceApplicationDetailUuid AND version_lock =:newVersion";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("newVersion", this.versionLock);
		params.put("remittanceApplicationDetailUuid", this.remittanceApplicationDetailUuid);
		return new SqlAndParamTuple(validate_sql, params);
	}

	/**
	 * 不实现
	 */
	@Override
	public SqlAndParamTuple updateSqlAndParamTuple() {
		// TODO Auto-generated method stub
		return null;
	}

}
