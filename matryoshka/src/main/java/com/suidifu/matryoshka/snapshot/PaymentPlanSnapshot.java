package com.suidifu.matryoshka.snapshot;

import com.alibaba.fastjson.annotation.JSONField;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.utils.Md5Util;
import com.zufangbao.sun.yunxin.entity.AssetClearStatus;
import com.zufangbao.sun.yunxin.entity.AssetSet;
import com.zufangbao.sun.yunxin.entity.ExecutingStatus;
import com.zufangbao.sun.yunxin.entity.OnAccountStatus;

import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 资产快照 Created by louguanyang on 2017/4/20.
 */
public class PaymentPlanSnapshot {
	/**
	 * UUID
	 */
	private String assetUuid;
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

	private Date assetRecycleDate;

	private int currentPeriod;

	private String financialContractUuid;

	private String customerUuid;

	private String contractUuid;

	private AssetClearStatus assetStatus = AssetClearStatus.UNCLEAR;

	private ExecutingStatus executingStatus = ExecutingStatus.UNEXECUTED;

	private OnAccountStatus onAccountStatus = OnAccountStatus.ON_ACCOUNT;

	private boolean canBeRollbacked = false;

	private Date createTime;

	private Date lastModifiedTime;

	private PaymentPlanExtraChargeSnapshot assetSetExtraChargeSnapshot;

	public PaymentPlanSnapshot() {
		super();
	}

	public PaymentPlanSnapshot(String assetUuid, String singleLoanContractNo, BigDecimal assetInitialValue,
			BigDecimal assetPrincipalValue, BigDecimal assetInterestValue, Date assetRecycleDate, int currentPeriod,
			String financialContractUuid, String customerUuid, String contractUuid, AssetClearStatus assetStatus,
			ExecutingStatus executingStatus) {
		this.assetUuid = assetUuid;
		this.singleLoanContractNo = singleLoanContractNo;
		this.assetInitialValue = assetInitialValue;
		this.assetPrincipalValue = assetPrincipalValue;
		this.assetInterestValue = assetInterestValue;
		this.assetRecycleDate = assetRecycleDate;
		this.currentPeriod = currentPeriod;
		this.financialContractUuid = financialContractUuid;
		this.customerUuid = customerUuid;
		this.contractUuid = contractUuid;
		this.assetStatus = assetStatus == null ? AssetClearStatus.UNCLEAR : assetStatus;
		this.executingStatus = executingStatus == null ? ExecutingStatus.UNEXECUTED : executingStatus;
	}

	public PaymentPlanSnapshot(AssetSet assetSet) {
		this.assetUuid = assetSet.getAssetUuid();
		this.singleLoanContractNo = assetSet.getSingleLoanContractNo();
		this.assetInitialValue = assetSet.getAssetInitialValue();
		this.assetPrincipalValue = assetSet.getAssetPrincipalValue();
		this.assetInterestValue = assetSet.getAssetInterestValue();
		this.assetRecycleDate = assetSet.getAssetRecycleDate();
		this.currentPeriod = assetSet.getCurrentPeriod();
		this.financialContractUuid = assetSet.getFinancialContractUuid();
		this.customerUuid = assetSet.getCustomerUuid();
		this.contractUuid = assetSet.getContractUuid();
		this.assetStatus = assetSet.getAssetStatus() == null ? AssetClearStatus.UNCLEAR : assetSet.getAssetStatus();
		this.executingStatus = assetSet.getExecutingStatus() == null ? ExecutingStatus.UNEXECUTED
				: assetSet.getExecutingStatus();
		this.canBeRollbacked = assetSet.isCanBeRollbacked();
		this.createTime = assetSet.getCreateTime();
		this.lastModifiedTime = assetSet.getLastModifiedTime();
		this.onAccountStatus = assetSet.getOnAccountStatus() == null ? OnAccountStatus.ON_ACCOUNT
				: assetSet.getOnAccountStatus();
	}

	public String getAssetUuid() {
		return assetUuid;
	}

	public void setAssetUuid(String assetUuid) {
		this.assetUuid = assetUuid;
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

	public Date getAssetRecycleDate() {
		return assetRecycleDate;
	}

	public void setAssetRecycleDate(Date assetRecycleDate) {
		this.assetRecycleDate = assetRecycleDate;
	}

	public int getCurrentPeriod() {
		return currentPeriod;
	}

	public void setCurrentPeriod(int currentPeriod) {
		this.currentPeriod = currentPeriod;
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

	public AssetClearStatus getAssetStatus() {
		return assetStatus;
	}

	public void setAssetStatus(AssetClearStatus assetStatus) {
		this.assetStatus = assetStatus;
	}

	public OnAccountStatus getOnAccountStatus() {
		return onAccountStatus;
	}

	public void setOnAccountStatus(OnAccountStatus onAccountStatus) {
		this.onAccountStatus = onAccountStatus;
	}

	public ExecutingStatus getExecutingStatus() {
		return executingStatus;
	}

	public void setExecutingStatus(ExecutingStatus executingStatus) {
		this.executingStatus = executingStatus;
	}

	public boolean isCanBeRollbacked() {
		return canBeRollbacked;
	}

	public void setCanBeRollbacked(boolean canBeRollbacked) {
		this.canBeRollbacked = canBeRollbacked;
	}

	public PaymentPlanExtraChargeSnapshot getAssetSetExtraChargeSnapshot() {
		return assetSetExtraChargeSnapshot;
	}

	public void setAssetSetExtraChargeSnapshot(PaymentPlanExtraChargeSnapshot assetSetExtraChargeSnapshot) {
		this.assetSetExtraChargeSnapshot = assetSetExtraChargeSnapshot;
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

	/**
	 * MD5(assetInterestValue=XXX;assetPrincipalValue=XXX)
	 */
	@JSONField(serialize = false)
	public String asset_interest_principal_fingerPrint() {
		return Md5Util.asset_interest_principal_fingerPrint(assetInterestValue, assetPrincipalValue);
	}

	/**
	 * MD5(assetInterestValue=XXX;assetPrincipalValue=XXX;assetRecycleDate=XXX)
	 */
	@JSONField(serialize = false)
	public String assetFingerPrint() {
		return Md5Util.assetFingerPrint(assetInterestValue, assetPrincipalValue, DateUtils.format(assetRecycleDate));
	}

	@JSONField(serialize = false)
	public String assetFingerPrintWithoutPrincipal() {
		return Md5Util.assetFingerPrint(assetInterestValue, BigDecimal.ZERO, DateUtils.format(assetRecycleDate));

	}

	@JSONField(serialize = false)
	public boolean checkAssetFingerPrint(String assetFingerPrint) {
		return StringUtils.equals(assetFingerPrint(), assetFingerPrint);
	}

	@JSONField(serialize = false)
	public boolean check_asset_interest_principal_fingerPrint(String asset_interest_principal_fingerPrint) {
		return StringUtils.equals(asset_interest_principal_fingerPrint(), asset_interest_principal_fingerPrint);
	}

	@JSONField(serialize = false)
	public boolean checkAssetFingerPrintWithoutPrincipal(String assetFingerPrintWithoutPrincipal) {
		return StringUtils.equals(assetFingerPrint(), assetFingerPrintWithoutPrincipal);
	}

	@JSONField(serialize = false)
	public boolean is_clear_repayment_plan() {
		return this.assetStatus == AssetClearStatus.CLEAR || this.getExecutingStatus() == ExecutingStatus.SUCCESSFUL;
	}

	@JSONField(serialize = false)
	public boolean is_overdue_repayment_plan() {
		return this.assetStatus == AssetClearStatus.UNCLEAR && assetRecycleDate.before(DateUtils.getToday());
	}

	@JSONField(serialize = false)
	public boolean is_today_repayment_plan() {
		return this.assetStatus == AssetClearStatus.UNCLEAR
				&& DateUtils.isSameDay(assetRecycleDate, DateUtils.getToday());
	}

	@JSONField(serialize = false)
	public boolean is_repayment_plan_has_actual_amount() {
		BigDecimal actualAmount = null == this.assetSetExtraChargeSnapshot ? BigDecimal.ZERO
				: this.assetSetExtraChargeSnapshot.getActualAmount();
		return actualAmount.compareTo(BigDecimal.ZERO) > 0;
	}

	/**
	 * 还款成功/逾期/还款计划可回滚/当前实收>0 凡此四种情况之一均不允许变更
	 * 
	 * @return
	 */
	@JSONField(serialize = false)
	public boolean is_immutable() {
		return is_clear_repayment_plan() || is_overdue_repayment_plan() || is_repayment_plan_has_actual_amount()
				|| isCanBeRollbacked();
	}

	public BigDecimal getRepayCharge() {
		return this.assetSetExtraChargeSnapshot.getRepayCharge();
	}
}
