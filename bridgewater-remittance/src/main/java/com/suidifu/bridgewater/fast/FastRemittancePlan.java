package com.suidifu.bridgewater.fast;

import com.alibaba.fastjson.annotation.JSONField;
import com.suidifu.giotto.handler.DataTemperature;
import com.suidifu.giotto.handler.FastCacheObjectWithTemperature;
import com.suidifu.giotto.keyenum.FastKey;
import com.suidifu.giotto.util.SqlAndParamTuple;
import com.zufangbao.sun.yunxin.entity.remittance.AccountSide;
import com.zufangbao.sun.yunxin.entity.remittance.ExecutionStatus;
import com.zufangbao.sun.yunxin.entity.remittance.RemittancePlan;
import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.util.*;

public class FastRemittancePlan extends FastCacheObjectWithTemperature {

	/*@Id
	@GeneratedValue*/
	private Long id;

	/**
	 * 放款计划uuid
	 */
	private String remittancePlanUuid;

	/**
	 * 放款申请uuid
	 */
	private String remittanceApplicationUuid;

	/**
	 * 放款申请明细uuid
	 */
	private String remittanceApplicationDetailUuid;

	/**
	 * 信托合同Uuid
	 */
	private String financialContractUuid;

	/**
	 * @Id
	 * @GeneratedValue private Long id;
	 * <p>
	 * 信托合同Id@Id
	 * @GeneratedValue
	 */
	private Long financialContractId;

	/**
	 * 业务记录号
	 */
	private String businessRecordNo;

	/**
	 * 贷款合同唯一编号
	 */
	private String contractUniqueId;

	/**
	 * 贷款合同编号
	 */
	private String contractNo;

	/**
	 * 支付网关0:广州银联，1:超级网银，2:银企直连
	 */
	private Integer paymentGateway;

	/**
	 * 支付通道uuid
	 */
	private String paymentChannelUuid;

	/**
	 * 支付通道名称
	 */
	private String paymentChannelName;

	/**
	 * 支付网关账户名
	 */
	private String pgAccountName;

	/**
	 * 支付网关帐户号
	 */
	private String pgAccountNo;

	/**
	 * 支付网关清算帐户
	 */
	private String pgClearingAccount;

	/**
	 * 交易类型0:贷，1:借
	 */
	private Integer transactionType;

	/**
	 * 交易备注
	 */
	private String transactionRemark;

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
	 * 执行前置条件
	 */
	private String executionPrecond;

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
	 * 交易流水号
	 */
	private String transactionSerialNo;


	public FastRemittancePlan() {

	}

	public FastRemittancePlan(RemittancePlan remittancePlan) {

	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRemittancePlanUuid() {
		return remittancePlanUuid;
	}

	public void setRemittancePlanUuid(String remittancePlanUuid) {
		this.remittancePlanUuid = remittancePlanUuid;
	}

	public String getRemittanceApplicationUuid() {
		return remittanceApplicationUuid;
	}

	public void setRemittanceApplicationUuid(String remittanceApplicationUuid) {
		this.remittanceApplicationUuid = remittanceApplicationUuid;
	}

	public String getRemittanceApplicationDetailUuid() {
		return remittanceApplicationDetailUuid;
	}

	public void setRemittanceApplicationDetailUuid(String remittanceApplicationDetailUuid) {
		this.remittanceApplicationDetailUuid = remittanceApplicationDetailUuid;
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

	public String getPaymentChannelUuid() {
		return paymentChannelUuid;
	}

	public void setPaymentChannelUuid(String paymentChannelUuid) {
		this.paymentChannelUuid = paymentChannelUuid;
	}

	public String getPaymentChannelName() {
		return paymentChannelName;
	}

	public void setPaymentChannelName(String paymentChannelName) {
		this.paymentChannelName = paymentChannelName;
	}

	public String getPgAccountName() {
		return pgAccountName;
	}

	public void setPgAccountName(String pgAccountName) {
		this.pgAccountName = pgAccountName;
	}

	public String getPgAccountNo() {
		return pgAccountNo;
	}

	public void setPgAccountNo(String pgAccountNo) {
		this.pgAccountNo = pgAccountNo;
	}

	public String getPgClearingAccount() {
		return pgClearingAccount;
	}

	public void setPgClearingAccount(String pgClearingAccount) {
		this.pgClearingAccount = pgClearingAccount;
	}

	public String getTransactionRemark() {
		return transactionRemark;
	}

	public void setTransactionRemark(String transactionRemark) {
		this.transactionRemark = transactionRemark;
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

	public String getExecutionPrecond() {
		return executionPrecond;
	}

	public void setExecutionPrecond(String executionPrecond) {
		this.executionPrecond = executionPrecond;
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

	public Integer getPaymentGateway() {
		return paymentGateway;
	}


	public void setPaymentGateway(Integer paymentGateway) {
		this.paymentGateway = paymentGateway;
	}


	public Integer getTransactionType() {
		return transactionType;
	}


	public void setTransactionType(Integer transactionType) {
		this.transactionType = transactionType;
	}


	public Integer getCpIdType() {
		return cpIdType;
	}


	public void setCpIdType(Integer cpIdType) {
		this.cpIdType = cpIdType;
	}


	public Integer getExecutionStatus() {
		return executionStatus;
	}


	public void setExecutionStatus(Integer executionStatus) {
		this.executionStatus = executionStatus;
	}

	public String getTransactionSerialNo() {
		return transactionSerialNo;
	}

	public void setTransactionSerialNo(String transactionSerialNo) {
		this.transactionSerialNo = transactionSerialNo;
	}

	@JSONField(serialize = false)
	public Date getMaxCompletePaymentDate(Date date) {
		if (this.completePaymentDate != null && date != null) {
			if (this.completePaymentDate.before(date)) {
				return date;
			} else {
				return this.completePaymentDate;
			}
		} else {
			return date == null ? this.completePaymentDate : date;
		}
	}

	@JSONField(serialize = false)
	public boolean isCredit() {
		return this.transactionType == AccountSide.CREDIT.getOrdinal();
	}

	@JSONField(serialize = false)
	public boolean isCreditSuccess() {
		return this.transactionType == AccountSide.CREDIT.getOrdinal() && this.executionStatus == ExecutionStatus.SUCCESS.getOrdinal();
	}

	public String getUuid() {
		return this.remittancePlanUuid;
	}


	@Override
	public String obtainAddCacheKey() {
		if (StringUtils.isBlank(getRemittancePlanUuid())) {
			throw new RuntimeException("all keys value is null.");
		}
		String result = FastRemittancePlanEnum.PREFIX_KEY;
		if (StringUtils.isBlank(getRemittancePlanUuid())) {
			result = result.concat(":");
		} else {
			result = result.concat(this.getRemittancePlanUuid()).concat(":");
		}
		return result;
	}

	@Override
	public SqlAndParamTuple obtainInsertSqlAndParam() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String obtainQueryCheckMD5Sql(String updateSql) {
		return null;
	}

	@Override
	public FastKey getColumnName() {
		return FastRemittancePlanEnum.REMITTANCE_PLAN_UUID;
	}

	@Override
	public String getColumnValue() {
		return getRemittancePlanUuid();
	}

	@Override
	public List<String> obtainAddCacheKeyList() {
		return new ArrayList<String>() {
			private static final long serialVersionUID = 2505755355562435085L;

			{
				if (StringUtils.isNotBlank(getRemittancePlanUuid())) {
					add(FastRemittancePlanEnum.PREFIX_KEY.concat(getRemittancePlanUuid()));
				}
			}
		};
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

	/**
	 * 不实现
	 */
	@Override
	public SqlAndParamTuple updateSqlAndParamTupleWithVersionLock(String oldVersion) {
		return null;
	}

	/**
	 * 不实现
	 */
	@Override
	public SqlAndParamTuple checkAfterUpdateSqlAndParamTuple() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SqlAndParamTuple updateSqlAndParamTuple() {

		String sql = "UPDATE t_remittance_plan " + " SET pg_account_name =:pgAccountName, "
				+ " pg_account_no =:pgAccountNo, " + " complete_payment_date =:completePaymentDate, "
				+ " actual_total_amount =:actualTotalAmount, " + " execution_status =:executionStatus, "
				+ " execution_remark =:executionRemark, " + " transaction_serial_no =:transactionSerialNo, "
				+ " last_modified_time =:lastModifiedTime " + "WHERE remittance_plan_uuid =:remittancePlanUuid "
				+ "AND execution_status =:processingStatus";

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pgAccountName", this.pgAccountName);
		params.put("pgAccountNo", this.pgAccountNo);
		params.put("completePaymentDate", this.completePaymentDate);
		params.put("actualTotalAmount", this.actualTotalAmount);
		params.put("executionStatus", this.executionStatus);
		params.put("executionRemark", this.executionRemark);
		params.put("lastModifiedTime", new Date());
		params.put("remittancePlanUuid", this.remittancePlanUuid);
		params.put("processingStatus", ExecutionStatus.PROCESSING.ordinal());
		params.put("transactionSerialNo", this.transactionSerialNo);

		return new SqlAndParamTuple(sql, params);
	}

}

