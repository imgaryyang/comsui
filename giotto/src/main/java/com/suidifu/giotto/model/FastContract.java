package com.suidifu.giotto.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.demo2do.core.utils.JsonUtils;
import com.suidifu.giotto.exception.GiottoException;
import com.suidifu.giotto.keyenum.FastContractKeyEnum;
import com.suidifu.giotto.keyenum.FastKey;
import com.suidifu.giotto.util.SqlAndParamTuple;
import com.zufangbao.gluon.opensdk.Md5Util;
import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 贷款合同
 * @author zhanghongbing
 *
 */
public class FastContract extends FastCacheObject {
	
	public static final int INITIAL_VERSION_NO = 1;
	
	private Long id;
	
	/**
	 * 信托合同uuid
	 */
	private String financialContractUuid;
	
	/**
	 * 贷款合同编号
	 */
	private String contractNo;
	/**
	 * 贷款人
	 */
	private long customerId;
	/**
	 * 抵押物
	 */
	private long houseId;
	/**
	 * 所属商户
	 */
	private long appId;
	/**
	 * 贷款开始日
	 */
	private Date beginDate;
	/**
	 * 贷款结束日
	 */
	private Date endDate;
	/**
	 * 月还款额(等额本息)
	 */
	private BigDecimal monthFee = BigDecimal.ZERO;
	/**
	 * 资产类型 0:二手车,1:种子贷
	 */
	private Integer assetType;
	/**
	 * 实际结束日
	 */
	private Date actualEndDate;
	/**
	 * 贷款总额
	 */
	private BigDecimal totalAmount = BigDecimal.ZERO;
	/**
	 * 罚息利率
	 */
	private BigDecimal penaltyInterest = BigDecimal.ZERO;
	/**
	 * 贷款利率
	 */
	private BigDecimal interestRate = BigDecimal.ZERO;
	/**
	 * 回款方式: 0:等额本息,1:等额本金,2,锯齿型
	 */
	private Integer repaymentWay;
	/**
	 * 贷款期数
	 */
	private Integer periods;
	/**
	 * 还款频率(月)
	 */
	private Integer paymentFrequency;
	/**
	 * 还款日
	 */
	private int paymentDayInMonth;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 利率周期 0:年化 1月化 2:日化
	 */
	private Integer interestRateCycle;

	/**
	 * 贷款合同唯一编号
	 */
	private String uniqueId;
	/**
	 * 有效的还款计划版本号
	 */
	private Integer activeVersionNo;
	
	/**
	 * 还款计划操作日志JsonArray
	 */
	private String repaymentPlanOperateLogs;
	
	private String uuid;
	
	private String customerUuid;
	
	private Integer state;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public String obtainAddCacheKey() throws GiottoException {
		if (StringUtils.isBlank(getContractNo()) && StringUtils.isBlank(getUniqueId())
				&& StringUtils.isBlank(getUuid())) {
			throw new GiottoException("all keys value is null.");
		}
		String result = FastContractKeyEnum.PREFIX_KEY;
		if (StringUtils.isBlank(getContractNo())) {
			result = result.concat(":");
		} else {
			result = result.concat(this.getContractNo()).concat(":");
		}
		if (StringUtils.isBlank(getUniqueId())) {
			result = result.concat(":");
		} else {
			result = result.concat(this.getUniqueId()).concat(":");
		}
		if (StringUtils.isBlank(getUuid())) {
			result = result.concat(":");
		} else {
			result = result.concat(this.getUuid());
		}
		return result;
	}

	@Override
	public List<String> obtainAddCacheKeyList() {
		return new ArrayList<String>() {{
			if (StringUtils.isNotBlank(getContractNo())) {
				add(FastContractKeyEnum.PREFIX_KEY.concat(getContractNo()));
			}
			if (StringUtils.isNotBlank(getUniqueId())) {
				add(FastContractKeyEnum.PREFIX_KEY.concat(getUniqueId()));
			}
			if (StringUtils.isNotBlank(getUuid())) {
				add(FastContractKeyEnum.PREFIX_KEY.concat(getUuid()));
			}
		}};
	}

	@Override
	public SqlAndParamTuple obtainInsertSqlAndParam() {
		String addSql = "insert into contract (uuid, unique_id, begin_date," +
				"contract_no, end_date, asset_type, month_fee, app_id, customer_id," +
				"house_id, actual_end_date, create_time, interest_rate," +
				"payment_day_in_month, payment_frequency, periods, repayment_way, total_amount," +
				"penalty_interest, active_version_no, repayment_plan_operate_logs, state," +
				"financial_contract_uuid, interest_rate_cycle, customer_uuid) values (" +
				":uuid, :uniqueId, :beginDate, :contractNo, :endDate, :assetType," +
				":monthFee, :appId, :customerId, :houseId, :actualEndDate," +
				":createTime, :interestRate, :paymentDayInMonth, :paymentFrequency," +
				":periods, :repaymentWay, :totalAmount, :penaltyInterest, :activeVersionNo," +
				":repaymentPlanOperateLogs, :state, :financialContractUuid, :interestRateCycle, :customerUuid)";
		String fastJson = JSON.toJSONStringWithDateFormat(this, "yyyy-MM-dd HH:mm:ss",SerializerFeature.WriteMapNullValue);
        return new SqlAndParamTuple(addSql, JsonUtils.parse(fastJson));
	}

	@Override
	public String obtainQueryCheckMD5Sql(String updateSql) {
		int startIndex = updateSql.indexOf("where");
		String selectSql = "select count(1) from contract ".concat(updateSql.substring(startIndex, updateSql.length()));
		String assetSteType = this.getAssetType() == null ? "" : this.getAssetType().toString();
		String tAmount = this.getTotalAmount() == null ? "" : this.getTotalAmount().toString();
		String penalty = this.getPenaltyInterest() == null ? "" : this.getPenaltyInterest().toString();
		String interes = this.getInterestRate() == null ? "" : this.getInterestRate().toString();
		String repayWay = this.getRepaymentWay() == null ? "" : this.getRepaymentWay().toString();
		String period = this.getPeriods() == null ? "" : this.getPeriods().toString();
		String paymentFre = this.getPaymentFrequency() == null ? "" : this.getPaymentFrequency().toString();
		String md5Val = Md5Util.encode(assetSteType.concat(tAmount).concat(penalty).concat(interes).
				concat(repayWay).concat(period).concat(paymentFre));
		String md5Sql = "MD5(concat(IFNULL(asset_type, ''),IFNULL(total_amount, '')," +
				"IFNULL(penalty_interest, ''),IFNULL(interest_rate, ''),IFNULL(repayment_way, '')," +
				"IFNULL(periods, ''),IFNULL(payment_frequency, '')))";
		return selectSql.concat(" and '").concat(md5Val).concat("'=").concat(md5Sql);
	}

	public String getFinancialContractUuid() {
		return financialContractUuid;
	}

	public void setFinancialContractUuid(String financialContractUuid) {
		this.financialContractUuid = financialContractUuid;
	}

	public String getContractNo() {
		return contractNo;
	}

	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}

	public long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}

	public long getHouseId() {
		return houseId;
	}

	public void setHouseId(long houseId) {
		this.houseId = houseId;
	}

	public long getAppId() {
		return appId;
	}

	public void setAppId(long appId) {
		this.appId = appId;
	}

	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public BigDecimal getMonthFee() {
		return monthFee;
	}

	public void setMonthFee(BigDecimal monthFee) {
		this.monthFee = monthFee;
	}

	public Integer getAssetType() {
		return assetType;
	}

	public void setAssetType(Integer assetType) {
		this.assetType = assetType;
	}

	public Date getActualEndDate() {
		return actualEndDate;
	}

	public void setActualEndDate(Date actualEndDate) {
		this.actualEndDate = actualEndDate;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public BigDecimal getPenaltyInterest() {
		return penaltyInterest;
	}

	public void setPenaltyInterest(BigDecimal penaltyInterest) {
		this.penaltyInterest = penaltyInterest;
	}

	public BigDecimal getInterestRate() {
		return interestRate;
	}

	public void setInterestRate(BigDecimal interestRate) {
		this.interestRate = interestRate;
	}

	public Integer getRepaymentWay() {
		return repaymentWay;
	}

	public void setRepaymentWay(Integer repaymentWay) {
		this.repaymentWay = repaymentWay;
	}

	public Integer getPeriods() {
		return periods;
	}

	public void setPeriods(Integer periods) {
		this.periods = periods;
	}

	public Integer getPaymentFrequency() {
		return paymentFrequency;
	}

	public void setPaymentFrequency(Integer paymentFrequency) {
		this.paymentFrequency = paymentFrequency;
	}

	public int getPaymentDayInMonth() {
		return paymentDayInMonth;
	}

	public void setPaymentDayInMonth(int paymentDayInMonth) {
		this.paymentDayInMonth = paymentDayInMonth;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getInterestRateCycle() {
		return interestRateCycle;
	}

	public void setInterestRateCycle(Integer interestRateCycle) {
		this.interestRateCycle = interestRateCycle;
	}

	public String getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}

	public Integer getActiveVersionNo() {
		return activeVersionNo;
	}

	public void setActiveVersionNo(Integer activeVersionNo) {
		this.activeVersionNo = activeVersionNo;
	}

	public String getRepaymentPlanOperateLogs() {
		return repaymentPlanOperateLogs;
	}

	public void setRepaymentPlanOperateLogs(String repaymentPlanOperateLogs) {
		this.repaymentPlanOperateLogs = repaymentPlanOperateLogs;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getCustomerUuid() {
		return customerUuid;
	}

	public void setCustomerUuid(String customerUuid) {
		this.customerUuid = customerUuid;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	

	@Override
	public String getColumnValue() {
		return uuid;
	}

	@Override
	public FastKey getColumnName() {
		return FastContractKeyEnum.UUID;
	}

	public FastContract() {}

	public FastContract(Long id, String financialContractUuid, String contractNo, long customerId, long houseId,
			long appId, Date beginDate, Date endDate, BigDecimal monthFee, Integer assetType, Date actualEndDate,
			BigDecimal totalAmount, BigDecimal penaltyInterest, BigDecimal interestRate, Integer repaymentWay,
			Integer periods, Integer paymentFrequency, int paymentDayInMonth, Date createTime,
			Integer interestRateCycle, String uniqueId, Integer activeVersionNo, String repaymentPlanOperateLogs,
			String uuid, String customerUuid, Integer state) {
		super();
		this.id = id;
		this.financialContractUuid = financialContractUuid;
		this.contractNo = contractNo;
		this.customerId = customerId;
		this.houseId = houseId;
		this.appId = appId;
		this.beginDate = beginDate;
		this.endDate = endDate;
		this.monthFee = monthFee;
		this.assetType = assetType;
		this.actualEndDate = actualEndDate;
		this.totalAmount = totalAmount;
		this.penaltyInterest = penaltyInterest;
		this.interestRate = interestRate;
		this.repaymentWay = repaymentWay;
		this.periods = periods;
		this.paymentFrequency = paymentFrequency;
		this.paymentDayInMonth = paymentDayInMonth;
		this.createTime = createTime;
		this.interestRateCycle = interestRateCycle;
		this.uniqueId = uniqueId;
		this.activeVersionNo = activeVersionNo;
		this.repaymentPlanOperateLogs = repaymentPlanOperateLogs;
		this.uuid = uuid;
		this.customerUuid = customerUuid;
		this.state = state;
	}
}
