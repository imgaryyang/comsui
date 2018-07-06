package com.suidifu.giotto.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.demo2do.core.utils.JsonUtils;
import com.suidifu.giotto.exception.GiottoException;
import com.suidifu.giotto.keyenum.FastAssetSetKeyEnum;
import com.suidifu.giotto.keyenum.FastKey;
import com.suidifu.giotto.util.SqlAndParamTuple;
import com.zufangbao.gluon.opensdk.Md5Util;
import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FastAssetSet extends FastCacheObject {

	public static final String EMPTY_UUID = "empty_deduct_uuid";

	private Long id;
	/**
	 * UUID
	 */
	private String assetUuid;
	/**
	 * 贷款合同
	 */
	private long contractId;
	/**
	 * 单笔还款编号
	 */
	private String singleLoanContractNo;
	/**
	 * 资产初始价值
	 */
	private BigDecimal assetInitialValue = BigDecimal.ZERO;

	/**
	 * 本期资产本金
	 */
	private BigDecimal assetPrincipalValue = BigDecimal.ZERO;

	/**
	 * 本期资产利息
	 */
	private BigDecimal assetInterestValue = BigDecimal.ZERO;

	/**
	 * 资产公允值
	 */
	private BigDecimal assetFairValue = BigDecimal.ZERO;
	/**
	 * 资金状态: 0:未结转,1:已结转
	 */
	private Integer assetStatus = 0;

	/**
	 * 结转类型（0:正常, 1:提前 2:逾期）
	 */
	private Integer repaymentPlanType = 0;

	/**
	 * 计划类型：0:正常 1:提前
	 */
	private Integer planType = 0;

	/**
	 * 作废原因
	 */
	private Integer writeOffReason = 0;

	/**
	 * 是否可回滚
	 */
	private Integer canBeRollbacked = 0;

	/**
	 * 资产迁徙状态: 未到期 正常 逾期
	 */
	private Integer timeInterval = 0;

	/**
	 * 挂账状态: 0:未挂账,1:已挂账,2:已核销 3:部分核销
	 */
	private Integer onAccountStatus = 0;

	/**
	 * 担保补足状态: 0:未发生,1:待补足,2:已补足,3:担保作废
	 */
	private Integer guaranteeStatus = 0;

	/**
	 * 担保结清状态: 0:未发生,1:申请清算,2:清算处理中,3:已清算
	 */
	private Integer settlementStatus = 0;

	/**
	 * 最后评估时间
	 */
	private Date lastValuationTime;
	/**
	 * 计划资产回收日期
	 */
	private Date assetRecycleDate;
	/**
	 * 资产实际回收时间 -- 信息流时间
	 */
	private Date actualRecycleDate;
	/**
	 * 人工确认到账日期
	 */
	private Date confirmRecycleDate;
	/**
	 * 退款金额
	 */
	private BigDecimal refundAmount = BigDecimal.ZERO;
	/**
	 * 当前期数
	 */
	private int currentPeriod;
	/**
	 * 创建时间
	 */
	private Date createTime;

	/**
	 * 最后修改时间
	 */
	private Date lastModifiedTime;
	/**
	 * 备注
	 */
	private String Comment;

	/**
	 * 逾期确认状态（0:正常，1:系统逾期待确认，2:已逾期）
	 */
	private Integer overdueStatus = 0;

	/**
	 * 逾期日期
	 */
	private Date overdueDate;

	/**
	 * 还款计划版本号
	 */
	private Integer versionNo;

	/**
	 * 当前扣款类型
	 */
	private Integer deductionStatus = 0;

	/**
	 * 还款计划执行状态
	 * 0:未执行
	 * 1:处理中
	 * 2:还款成功
	 * 3:回购中
	 * 4:已回购
	 * 5:回购违约
	 * 6:中止执行
	 */
	private Integer executingStatus = 0;

	/**
	 * 还款计划执行状态备份
	 */
	private Integer executingStatusBak = 0;

	/**
	 * 合同资金状态：0：合同部分完成 1：合同全部完成
	 */
	private Integer contractFundingStatus;

	/**
	 *  0 未同步 1已同步
	 */
	private Integer syncStatus = 0;

	/**
	 * 有效状态 0:开启,1:作废 2:冻结
	 */
	private Integer activeStatus = 0;

	private String activeDeductApplicationUuid = EMPTY_UUID;

	/**
	 * 信托合同uuid
	 */
	private String financialContractUuid;

    private String customerUuid;

    private String contractUuid;

	 /**
     * MD5(assetInterestValue=XXX;assetPrincipalValue=XXX;assetRecycleDate=XXX)
     */
    private String assetFingerPrint;
    private Date assetFingerPrintUpdateTime = new Date();
    /**
	 * MD5(ChartOfAccount.SND_UNEARNED_LOAN_ASSET_TECH_FEE=XXX;ChartOfAccount.SND_UNEARNED_LOAN_ASSET_LOAN_SERVICE_FEE=XXX;ChartOfAccount.SND_UNEARNED_LOAN_ASSET_OTHER_FEE=XXX;......)
	 */
    private String assetExtraFeeFingerPrint;
    private Date assetExtraFeeFingerPrintUpdateTime = new Date();

	private String uuidVersion;

	/**
     * 版本锁
     */
    private String versionLock;

    /**
     * 支付状态
     */
    private Integer orderPaymentStatus = 0;

    private String repayScheduleNo;

    private String outerRepaymentPlanNo;

	@Override
    public String obtainAddCacheKey() throws GiottoException {
		if (StringUtils.isBlank(getAssetUuid()) && StringUtils.isBlank(getSingleLoanContractNo())) {
			throw new GiottoException("all keys value is null.");
		}
		String result = FastAssetSetKeyEnum.PREFIX_KEY;
		if (StringUtils.isBlank(getAssetUuid())) {
			result = result.concat(":");
		} else {
			result = result.concat(this.getAssetUuid()).concat(":");
		}
		if (StringUtils.isBlank(getSingleLoanContractNo())) {
			result = result.concat(":");
		} else {
			result = result.concat(this.getSingleLoanContractNo()).concat(":");
		}
		if (StringUtils.isBlank(this.getRepayScheduleNo())) {
			result = result.concat(":");
		} else {
			result = result.concat(this.getRepayScheduleNo());
		}
		return result;
	}

	@Override
    public SqlAndParamTuple obtainInsertSqlAndParam() {
		String addSql = "insert into asset_set (guarantee_status,settlement_status," +
				"asset_fair_value, asset_principal_value, asset_interest_value,asset_initial_value," +
				"asset_recycle_date, confirm_recycle_date, refund_amount, asset_status," +
				"on_account_status, repayment_plan_type, last_valuation_time, asset_uuid," +
				"create_time,last_modified_time, comment,single_loan_contract_no, contract_id,actual_recycle_date," +
				"current_period,overdue_status, overdue_date,version_no,active_status," +
				"sync_status,active_deduct_application_uuid, financial_contract_uuid," +
				"asset_finger_print,asset_extra_fee_finger_print, asset_finger_print_update_time," +
				"asset_extra_fee_finger_print_update_time, plan_type,write_off_reason," +
				"can_be_rollbacked,time_interval, deduction_status,executing_status," +
				"executing_status_bak,customer_uuid, contract_uuid,contract_funding_status,version_lock,order_payment_status,repay_schedule_no,outer_repayment_plan_no) values (" +
				":guaranteeStatus,:settlementStatus,:assetFairValue,:assetPrincipalValue," +
				":assetInterestValue,:assetInitialValue, :assetRecycleDate, :confirmRecycleDate," +
				":refundAmount, :assetStatus, :onAccountStatus, :repaymentPlanType, :lastValuationTime," +
				":assetUuid, :createTime,:lastModifiedTime, :comment,:singleLoanContractNo," +
				":contractId,:actualRecycleDate, :currentPeriod,:overdueStatus," +
				":overdueDate,:versionNo,:activeStatus, :syncStatus,:activeDeductApplicationUuid," +
				":financialContractUuid, :assetFingerPrint,:assetExtraFeeFingerPrint," +
				":assetFingerPrintUpdateTime, :assetExtraFeeFingerPrintUpdateTime," +
				":planType,:writeOffReason, :canBeRollbacked,:timeInterval," +
				":deductionStatus,:executingStatus, :executingStatusBak,:customerUuid," +
				":contractUuid,:contractFundingStatus,:versionLock,:orderPaymentStatus,:repayScheduleNo,:outerRepaymentPlanNo)";
		String fastJson = JSON.toJSONStringWithDateFormat(this, "yyyy-MM-dd HH:mm:ss",SerializerFeature.WriteMapNullValue);
        return new SqlAndParamTuple(addSql, JsonUtils.parse(fastJson));
	}

	@Override
    public String obtainQueryCheckMD5Sql(String updateSql) {
		int startIndex = updateSql.indexOf("where");
		String selectSql = null;
		if (startIndex >= 0) {
			selectSql = "select count(1) from asset_set ".concat(updateSql.substring(startIndex, updateSql.length()));
		} else {
			selectSql = "select count(1) from asset_set where ";
		}
		return selectSql.concat(obtainCheckMD5Sql());
	}

	public String obtainUpdateCheckMD5Sql(String updateSql) {
		int whereIndex = updateSql.indexOf("where");
		if (whereIndex >= 0) {
			updateSql += " and ".concat(obtainCheckMD5Sql());
		} else {
			updateSql += " where ".concat(obtainCheckMD5Sql());
		}
		return updateSql;
	}

	private String obtainCheckMD5Sql() {
		String initialVal = this.getAssetInitialValue() == null ? "" : this.getAssetInitialValue().toString();
		String assetPriVal = this.getAssetPrincipalValue() == null ? "" : this.getAssetPrincipalValue().toString();
		String assetInterVal = this.getAssetInterestValue() == null ? "" : this.getAssetInterestValue().toString();
		String assetFairVal = this.getAssetFairValue() == null ? "" : this.getAssetFairValue().toString();
		String assetStatus = this.getAssetStatus() == null ? "" : this.getAssetStatus().toString();
		String assetAccoutStatus = this.getOnAccountStatus() == null ? "" : this.getOnAccountStatus().toString();
		String guarStatus = this.getGuaranteeStatus() == null ? "" : this.getGuaranteeStatus().toString();
		String setStatus = this.getSettlementStatus() == null ? "" : this.getSettlementStatus().toString();

		String md5Val = Md5Util.encode(initialVal.concat(assetPriVal).concat(assetInterVal)
				.concat(assetFairVal).concat(assetStatus)
				.concat(assetAccoutStatus).concat(guarStatus).concat(setStatus));
		String md5Sql = "MD5(concat(IFNULL(asset_initial_value,''), IFNULL(asset_principal_value,''), " +
				"IFNULL(asset_interest_value,''), IFNULL(asset_fair_value, ''), IFNULL(asset_status, ''), " +
				"IFNULL(on_account_status, ''), IFNULL(guarantee_status, ''), IFNULL(settlement_status,'')))";
		return " and '".concat(md5Val).concat("'=").concat(md5Sql);
	}

	@Override
	public List<String> obtainAddCacheKeyList() {
		return new ArrayList<String>() {{
			if (StringUtils.isNotBlank(getAssetUuid())) {
				add(FastAssetSetKeyEnum.PREFIX_KEY.concat(getAssetUuid()));
			}
			if (StringUtils.isNotBlank(getSingleLoanContractNo())) {
				add(FastAssetSetKeyEnum.PREFIX_KEY.concat(getSingleLoanContractNo()));
			}
			if (StringUtils.isNotBlank(getRepayScheduleNo())) {
				add(FastAssetSetKeyEnum.PREFIX_KEY.concat(getRepayScheduleNo()));
			}
		}};
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAssetUuid() {
		return assetUuid;
	}

	public void setAssetUuid(String assetUuid) {
		this.assetUuid = assetUuid;
	}

	public long getContractId() {
		return contractId;
	}

	public void setContractId(long contractId) {
		this.contractId = contractId;
	}

	public String getSingleLoanContractNo() {
		return singleLoanContractNo;
	}

	public void setSingleLoanContractNo(String singleLoanContractNo) {
		this.singleLoanContractNo = singleLoanContractNo;
	}

	public BigDecimal getAssetInitialValue() {
		return assetInitialValue;
	}

	public void setAssetInitialValue(BigDecimal assetInitialValue) {
		this.assetInitialValue = assetInitialValue;
	}

	public BigDecimal getAssetPrincipalValue() {
		return assetPrincipalValue;
	}

	public void setAssetPrincipalValue(BigDecimal assetPrincipalValue) {
		this.assetPrincipalValue = assetPrincipalValue;
	}

	public BigDecimal getAssetInterestValue() {
		return assetInterestValue;
	}

	public void setAssetInterestValue(BigDecimal assetInterestValue) {
		this.assetInterestValue = assetInterestValue;
	}

	public BigDecimal getAssetFairValue() {
		return assetFairValue;
	}

	public void setAssetFairValue(BigDecimal assetFairValue) {
		this.assetFairValue = assetFairValue;
	}

	public Integer getAssetStatus() {
		return assetStatus;
	}

	public void setAssetStatus(Integer assetStatus) {
		this.assetStatus = assetStatus;
	}

	public Integer getRepaymentPlanType() {
		return repaymentPlanType;
	}

	public void setRepaymentPlanType(Integer repaymentPlanType) {
		this.repaymentPlanType = repaymentPlanType;
	}

	public Integer getPlanType() {
		return planType;
	}

	public void setPlanType(Integer planType) {
		this.planType = planType;
	}

	public Integer getWriteOffReason() {
		return writeOffReason;
	}

	public void setWriteOffReason(Integer writeOffReason) {
		this.writeOffReason = writeOffReason;
	}

	public Integer getCanBeRollbacked() {
		return canBeRollbacked;
	}

	public void setCanBeRollbacked(Integer canBeRollbacked) {
		this.canBeRollbacked = canBeRollbacked;
	}

	public Integer getTimeInterval() {
		return timeInterval;
	}

	public void setTimeInterval(Integer timeInterval) {
		this.timeInterval = timeInterval;
	}

	public Integer getOnAccountStatus() {
		return onAccountStatus;
	}

	public void setOnAccountStatus(Integer onAccountStatus) {
		this.onAccountStatus = onAccountStatus;
	}

	public Integer getGuaranteeStatus() {
		return guaranteeStatus;
	}

	public void setGuaranteeStatus(Integer guaranteeStatus) {
		this.guaranteeStatus = guaranteeStatus;
	}

	public Integer getSettlementStatus() {
		return settlementStatus;
	}

	public void setSettlementStatus(Integer settlementStatus) {
		this.settlementStatus = settlementStatus;
	}

	public Date getLastValuationTime() {
		return lastValuationTime;
	}

	public void setLastValuationTime(Date lastValuationTime) {
		this.lastValuationTime = lastValuationTime;
	}

	public Date getAssetRecycleDate() {
		return assetRecycleDate;
	}

	public void setAssetRecycleDate(Date assetRecycleDate) {
		this.assetRecycleDate = assetRecycleDate;
	}

	public Date getActualRecycleDate() {
		return actualRecycleDate;
	}

	public void setActualRecycleDate(Date actualRecycleDate) {
		this.actualRecycleDate = actualRecycleDate;
	}

	public Date getConfirmRecycleDate() {
		return confirmRecycleDate;
	}

	public void setConfirmRecycleDate(Date confirmRecycleDate) {
		this.confirmRecycleDate = confirmRecycleDate;
	}

	public BigDecimal getRefundAmount() {
		return refundAmount;
	}

	public void setRefundAmount(BigDecimal refundAmount) {
		this.refundAmount = refundAmount;
	}

	public int getCurrentPeriod() {
		return currentPeriod;
	}

	public void setCurrentPeriod(int currentPeriod) {
		this.currentPeriod = currentPeriod;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getLastModifiedTime() {
		return lastModifiedTime;
	}

	public void setLastModifiedTime(Date lastModifiedTime) {
		this.lastModifiedTime = lastModifiedTime;
	}

	public String getComment() {
		return Comment;
	}

	public void setComment(String comment) {
		Comment = comment;
	}

	public Integer getOverdueStatus() {
		return overdueStatus;
	}

	public void setOverdueStatus(Integer overdueStatus) {
		this.overdueStatus = overdueStatus;
	}

	public Date getOverdueDate() {
		return overdueDate;
	}

	public void setOverdueDate(Date overdueDate) {
		this.overdueDate = overdueDate;
	}

	public Integer getVersionNo() {
		return versionNo;
	}

	public void setVersionNo(Integer versionNo) {
		this.versionNo = versionNo;
	}

	public Integer getDeductionStatus() {
		return deductionStatus;
	}

	public void setDeductionStatus(Integer deductionStatus) {
		this.deductionStatus = deductionStatus;
	}

	public Integer getExecutingStatus() {
		return executingStatus;
	}

	public void setExecutingStatus(Integer executingStatus) {
		this.executingStatus = executingStatus;
	}

	public Integer getExecutingStatusBak() {
		return executingStatusBak;
	}

	public void setExecutingStatusBak(Integer executingStatusBak) {
		this.executingStatusBak = executingStatusBak;
	}

	public Integer getContractFundingStatus() {
		return contractFundingStatus;
	}

	public void setContractFundingStatus(Integer contractFundingStatus) {
		this.contractFundingStatus = contractFundingStatus;
	}

	public Integer getSyncStatus() {
		return syncStatus;
	}

	public void setSyncStatus(Integer syncStatus) {
		this.syncStatus = syncStatus;
	}

	public Integer getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(Integer activeStatus) {
		this.activeStatus = activeStatus;
	}

	public String getActiveDeductApplicationUuid() {
		return activeDeductApplicationUuid;
	}

	public void setActiveDeductApplicationUuid(String activeDeductApplicationUuid) {
		this.activeDeductApplicationUuid = activeDeductApplicationUuid;
	}

	public String getFinancialContractUuid() {
		return financialContractUuid;
	}

	public void setFinancialContractUuid(String financialContractUuid) {
		this.financialContractUuid = financialContractUuid;
	}

	public String getCustomerUuid() {
		return customerUuid;
	}

	public void setCustomerUuid(String customerUuid) {
		this.customerUuid = customerUuid;
	}

	public String getContractUuid() {
		return contractUuid;
	}

	public void setContractUuid(String contractUuid) {
		this.contractUuid = contractUuid;
	}

	public String getAssetFingerPrint() {
		return assetFingerPrint;
	}

	public void setAssetFingerPrint(String assetFingerPrint) {
		this.assetFingerPrint = assetFingerPrint;
	}

	public Date getAssetFingerPrintUpdateTime() {
		return assetFingerPrintUpdateTime;
	}

	public void setAssetFingerPrintUpdateTime(Date assetFingerPrintUpdateTime) {
		this.assetFingerPrintUpdateTime = assetFingerPrintUpdateTime;
	}

	public String getAssetExtraFeeFingerPrint() {
		return assetExtraFeeFingerPrint;
	}

	public void setAssetExtraFeeFingerPrint(String assetExtraFeeFingerPrint) {
		this.assetExtraFeeFingerPrint = assetExtraFeeFingerPrint;
	}

	public Date getAssetExtraFeeFingerPrintUpdateTime() {
		return assetExtraFeeFingerPrintUpdateTime;
	}

	public void setAssetExtraFeeFingerPrintUpdateTime(Date assetExtraFeeFingerPrintUpdateTime) {
		this.assetExtraFeeFingerPrintUpdateTime = assetExtraFeeFingerPrintUpdateTime;
	}

	public String getUuidVersion() {
		return uuidVersion;
	}

	public void setUuidVersion(String uuidVersion) {
		this.uuidVersion = uuidVersion;
	}


	public String getVersionLock() {
		return versionLock;
	}

	public Integer getOrderPaymentStatus() {
		return orderPaymentStatus;
	}

	public void setVersionLock(String versionLock) {
		this.versionLock = versionLock;
	}

	public void setOrderPaymentStatus(Integer orderPaymentStatus) {
		this.orderPaymentStatus = orderPaymentStatus;
	}

	public String getRepayScheduleNo() {
		return repayScheduleNo;
	}

	public void setRepayScheduleNo(String repayScheduleNo) {
		this.repayScheduleNo = repayScheduleNo;
	}

	public String getOuterRepaymentPlanNo() {
		return outerRepaymentPlanNo;
	}

	public void setOuterRepaymentPlanNo(String outerRepaymentPlanNo) {
		this.outerRepaymentPlanNo = outerRepaymentPlanNo;
	}

	@Override
	public String getColumnValue() {
		return assetUuid;
	}

	@Override
	public FastKey getColumnName() {
		return FastAssetSetKeyEnum.ASSET_SET_UUID;
	}

	public FastAssetSet(Long id, String assetUuid, long contractId, String singleLoanContractNo,
			BigDecimal assetInitialValue, BigDecimal assetPrincipalValue, BigDecimal assetInterestValue,
			BigDecimal assetFairValue, Integer assetStatus, Integer repaymentPlanType, Integer planType,
			Integer writeOffReason, Integer canBeRollbacked, Integer timeInterval, Integer onAccountStatus,
			Integer guaranteeStatus, Integer settlementStatus, Date lastValuationTime, Date assetRecycleDate,
			Date actualRecycleDate, Date confirmRecycleDate, BigDecimal refundAmount, int currentPeriod,
			Date createTime, Date lastModifiedTime, String comment, Integer overdueStatus, Date overdueDate,
			Integer versionNo, Integer deductionStatus, Integer executingStatus, Integer executingStatusBak,
			Integer contractFundingStatus, Integer syncStatus, Integer activeStatus, String activeDeductApplicationUuid,
			String financialContractUuid, String customerUuid, String contractUuid, String assetFingerPrint,
			Date assetFingerPrintUpdateTime, String assetExtraFeeFingerPrint, Date assetExtraFeeFingerPrintUpdateTime,
			String uuidVersion) {
		super();
		this.id = id;
		this.assetUuid = assetUuid;
		this.contractId = contractId;
		this.singleLoanContractNo = singleLoanContractNo;
		this.assetInitialValue = assetInitialValue;
		this.assetPrincipalValue = assetPrincipalValue;
		this.assetInterestValue = assetInterestValue;
		this.assetFairValue = assetFairValue;
		this.assetStatus = assetStatus;
		this.repaymentPlanType = repaymentPlanType;
		this.planType = planType;
		this.writeOffReason = writeOffReason;
		this.canBeRollbacked = canBeRollbacked;
		this.timeInterval = timeInterval;
		this.onAccountStatus = onAccountStatus;
		this.guaranteeStatus = guaranteeStatus;
		this.settlementStatus = settlementStatus;
		this.lastValuationTime = lastValuationTime;
		this.assetRecycleDate = assetRecycleDate;
		this.actualRecycleDate = actualRecycleDate;
		this.confirmRecycleDate = confirmRecycleDate;
		this.refundAmount = refundAmount;
		this.currentPeriod = currentPeriod;
		this.createTime = createTime;
		this.lastModifiedTime = lastModifiedTime;
		Comment = comment;
		this.overdueStatus = overdueStatus;
		this.overdueDate = overdueDate;
		this.versionNo = versionNo;
		this.deductionStatus = deductionStatus;
		this.executingStatus = executingStatus;
		this.executingStatusBak = executingStatusBak;
		this.contractFundingStatus = contractFundingStatus;
		this.syncStatus = syncStatus;
		this.activeStatus = activeStatus;
		this.activeDeductApplicationUuid = activeDeductApplicationUuid;
		this.financialContractUuid = financialContractUuid;
		this.customerUuid = customerUuid;
		this.contractUuid = contractUuid;
		this.assetFingerPrint = assetFingerPrint;
		this.assetFingerPrintUpdateTime = assetFingerPrintUpdateTime;
		this.assetExtraFeeFingerPrint = assetExtraFeeFingerPrint;
		this.assetExtraFeeFingerPrintUpdateTime = assetExtraFeeFingerPrintUpdateTime;
		this.uuidVersion = uuidVersion;
	}

	public FastAssetSet() {
	}

}
