package com.suidifu.bridgewater.fast;

import com.suidifu.giotto.handler.DataTemperature;
import com.suidifu.giotto.handler.FastCacheObjectWithTemperature;
import com.suidifu.giotto.keyenum.FastKey;
import com.suidifu.giotto.util.SqlAndParamTuple;
import com.zufangbao.sun.yunxin.entity.remittance.ExecutionStatus;
import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.util.*;

public class FastRemittanceApplication extends FastCacheObjectWithTemperature {

	private Long id;

	/**
	 * 放款申请uuid
	 */
	private String remittanceApplicationUuid;

	/**
	 * 订单号
	 */
	private String remittanceId;

	/**
	 * 放款请求编号
	 */
	private String requestNo;

	/**
	 * 信托合同uuid
	 */
	private String financialContractUuid;

	/**
	 * 信托合同Id
	 */
	private Long financialContractId;

	/**
	 * 信托产品代码
	 */
	private String financialProductCode;

	/**
	 * 贷款合同唯一编号
	 */
	private String contractUniqueId;

	/**
	 * 贷款合同编号
	 */
	private String contractNo;

	/**
	 * 计划放款总金额
	 */
	private BigDecimal plannedTotalAmount;

	/**
	 * 实际放款总金额
	 */
	private BigDecimal actualTotalAmount;

	/**
	 * 审核人
	 */
	private String auditorName;

	/**
	 * 审核日期
	 */
	private Date auditTime;

	/**
	 * 回调地址
	 */
	private String notifyUrl;

	/**
	 * 计划通知次数
	 */
	private int planNotifyNumber;

	/**
	 * 实际通知次数
	 */
	private int actualNotifyNumber;

	/**
	 * 放款策略 0:多目标放款策略, 1:先放后扣放款策略
	 */
	private Integer remittanceStrategy;

	/**
	 * 备注
	 */
	private String remark;

	/**
	 * 执行状态, 0:已创建、1:处理中、2:成功、3:失败、4:异常、5:撤销
	 */
	private Integer executionStatus;

	/**
	 * 交易接收方, 0:本端，1:对端
	 */
	private Integer transactionRecipient;

	/**
	 * 执行备注
	 */
	private String executionRemark;

	/**
	 * 受理日期
	 */
	private Date createTime;

	/**
	 * 创建人
	 */
	private String creatorName;

	/**
	 * ip地址
	 */
	private String ip;

	/**
	 * 对端接收交易时间
	 */
	private Date oppositeReceiveDate;

	/**
	 * 最后更新时间
	 */
	private Date lastModifiedTime;

	/**
	 * RemittanceDetail总个数
	 */
	private int totalCount = 0;

	/**
	 * RemittanceDetail实际完成个数
	 */
	private int actualCount = 0;

	/**
	 * 版本锁
	 */
	private String versionLock;

	/**
	 * 转账类型 放款时该值为空
	 * RemittanceCommandModel.TransactionType
	 */
	private String stringField1;
	
	@Override
	public String obtainAddCacheKey() {
		if (StringUtils.isBlank(getRemittanceApplicationUuid())) {
			throw new RuntimeException("all keys value is null.");
		}
		String result = FastRemittanceApplicationEnum.PREFIX_KEY;
		if (StringUtils.isBlank(getRemittanceApplicationUuid())) {
			result = result.concat(":");
		} else {
			result = result.concat(this.getRemittanceApplicationUuid()).concat(":");
		}
		if (StringUtils.isBlank(getRequestNo())) {
			result = result.concat(":");
		} else {
			result = result.concat(this.getRequestNo()).concat(":");
		}
		if (StringUtils.isBlank(getRemittanceId())) {
			result = result.concat(":");
		} else {
			result = result.concat(this.getRemittanceId()).concat(":");
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
		return FastRemittanceApplicationEnum.REMITTANCE_APPLICATION_UUID;
	}

	@Override
	public String getColumnValue() {
		return getRemittanceApplicationUuid();
	}

	@Override
	public List<String> obtainAddCacheKeyList() {
		return new ArrayList<String>() {
			private static final long serialVersionUID = -3122598292970024279L;

			{
				if (StringUtils.isNotBlank(getRemittanceApplicationUuid())) {
					add(FastRemittanceApplicationEnum.PREFIX_KEY.concat(getRemittanceApplicationUuid()));
				}
				if (StringUtils.isNotBlank(getRequestNo())) {
					add(FastRemittanceApplicationEnum.PREFIX_KEY.concat(getRequestNo()));
				}
				if (StringUtils.isNotBlank(getRemittanceId())) {
					add(FastRemittanceApplicationEnum.PREFIX_KEY.concat(getRemittanceId()));
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

	public String getRemittanceApplicationUuid() {
		return remittanceApplicationUuid;
	}

	public void setRemittanceApplicationUuid(String remittanceApplicationUuid) {
		this.remittanceApplicationUuid = remittanceApplicationUuid;
	}

	public String getRemittanceId() {
		return remittanceId;
	}

	public void setRemittanceId(String remittanceId) {
		this.remittanceId = remittanceId;
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

	public String getContractUniqueId() {
		return contractUniqueId;
	}

	public void setContractUniqueId(String contractUniqueId) {
		this.contractUniqueId = contractUniqueId;
	}

	public String getContractNo() {
		return contractNo;
	}

	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
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

	public String getAuditorName() {
		return auditorName;
	}

	public void setAuditorName(String auditorName) {
		this.auditorName = auditorName;
	}

	public Date getAuditTime() {
		return auditTime;
	}

	public void setAuditTime(Date auditTime) {
		this.auditTime = auditTime;
	}

	public String getNotifyUrl() {
		return notifyUrl;
	}

	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
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

	public Integer getRemittanceStrategy() {
		return remittanceStrategy;
	}

	public void setRemittanceStrategy(Integer remittanceStrategy) {
		this.remittanceStrategy = remittanceStrategy;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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

	public String getStringField1() {
		return stringField1;
	}

	public void setStringField1(String stringField1) {
		this.stringField1 = stringField1;
	}

	@Override
	public DataTemperature temperature() {
		if ((executionStatus == ExecutionStatus.SUCCESS.ordinal()) && (this.actualNotifyNumber >= this.planNotifyNumber)) {
			return DataTemperature.FROZEN;
		} else {
			return DataTemperature.HOT;
		}
	}

	@Override
	public SqlAndParamTuple updateSqlAndParamTupleWithVersionLock(String oldVersion) {
		String update_sql = "UPDATE t_remittance_application " + "SET actual_total_amount =:actualTotalAmount, "
				+ " execution_status =:executionStatus, " + " plan_notify_number =:planNotifyNumber, "
				+ " opposite_receive_date =:oppositeReceiveDate, " + " last_modified_time =:lastModifiedTime, "
				+ " version_lock =:newVersion," + " actual_count =:actualCount"
				+ " WHERE remittance_application_uuid=:remittanceApplicationUuid AND version_lock =:oldVersion";
		Map<String, Object> params = new HashMap<>();
		params.put("actualTotalAmount", actualTotalAmount);
		params.put("executionStatus", executionStatus);
		params.put("planNotifyNumber", planNotifyNumber);
		params.put("lastModifiedTime", lastModifiedTime);
		params.put("oppositeReceiveDate", oppositeReceiveDate);
		params.put("remittanceApplicationUuid", remittanceApplicationUuid);
		params.put("newVersion", versionLock);
		params.put("oldVersion", oldVersion);
		params.put("actualCount", actualCount);
		params.put("remittanceApplicationUuid", this.remittanceApplicationUuid);

		return new SqlAndParamTuple(update_sql, params);
	}

	@Override
	public SqlAndParamTuple checkAfterUpdateSqlAndParamTuple() {
		String validate_sql = "select remittance_application_uuid from t_remittance_application WHERE remittance_application_uuid=:remittanceApplicationUuid AND version_lock =:newVersion";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("newVersion", this.versionLock);
		params.put("remittanceApplicationUuid", this.remittanceApplicationUuid);
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
