package com.zufangbao.earth.yunxin.api.model.query;

import com.alibaba.fastjson.annotation.JSONField;
import com.zufangbao.gluon.opensdk.DateUtils;
import com.zufangbao.sun.ledgerbook.ChartOfAccount;
import com.zufangbao.sun.yunxin.entity.AssetSet;
import com.zufangbao.sun.yunxin.entity.RepaymentExecutionStateMapUtil;

import java.math.BigDecimal;
import java.util.Map;

public class RepaymentPlanDetail {

	private String repaymentNumber;
	
	private String period;

	private String planRepaymentDate;

	private String planRepaymentPrincipal="0.00";

	private String planRepaymentInterest="0.00";

	private String loanFees = "0.00";

	private String technicalServicesFees = "0.00";

	private String otherFees = "0.00";

	private String repaymentExecutionState;
	
	private String penaltyFee = "0.00";
	
	private String totalOverdueFee = "0.00";

	private Integer currentPeriod;  //产品说不要期数，故不再返回。

	/**
	 * 商户还款计划编号
	 */
	private String repayScheduleNo;
	
	/**
	 *  商户还款计划编号的MD5
	 */
    private String outerRepaymentPlanNo;
	
	public RepaymentPlanDetail(AssetSet assetSet, Map<String, BigDecimal> amountMap, BigDecimal totalOverDueFee) {
		this.repaymentNumber = assetSet.getSingleLoanContractNo();
		this.period = assetSet.getCurrentPeriod()+"";
		this.planRepaymentDate = DateUtils.format(assetSet.getAssetRecycleDate(), "yyyy-MM-dd");
		
		this.planRepaymentPrincipal = assetSet.getAssetPrincipalValue().toString();
		this.planRepaymentInterest = assetSet.getAssetInterestValue().toString();
		this.repaymentExecutionState = RepaymentExecutionStateMapUtil.getRepaymentExecutionState(assetSet);
		this.penaltyFee = getAmountFromMap(amountMap,ChartOfAccount.SND_RECIEVABLE_LOAN_PENALTY).toString();
		this.totalOverdueFee = totalOverDueFee.toString();
		this.loanFees = getAmountFromMap(amountMap,ChartOfAccount.SND_UNEARNED_LOAN_ASSET_LOAN_SERVICE_FEE).toString();
		this.technicalServicesFees = getAmountFromMap(amountMap,ChartOfAccount.SND_UNEARNED_LOAN_ASSET_TECH_FEE).toString();
		this.otherFees = getAmountFromMap(amountMap,ChartOfAccount.SND_UNEARNED_LOAN_ASSET_OTHER_FEE).toString();
		this.currentPeriod = assetSet.getCurrentPeriod();
		this.repayScheduleNo = assetSet.getOuterRepaymentPlanNo(); 
		this.outerRepaymentPlanNo = assetSet.getRepayScheduleNo();
	}
	@JSONField(serialize = false)
	private BigDecimal getAmountFromMap(Map<String,BigDecimal> amountMap,String chartString ){
		
		if(amountMap.get(chartString) != null){
			return amountMap.get(chartString);
		}
		return BigDecimal.ZERO; 
	}
	
	public RepaymentPlanDetail(){
		
	}

	public String getRepaymentNumber() {
		return repaymentNumber;
	}

	public void setRepaymentNumber(String repaymentNumber) {
		this.repaymentNumber = repaymentNumber;
	}

	public String getPeriod() {
		return period;
	}
	public void setPeriod(String period) {
		this.period = period;
	}
	public String getPlanRepaymentDate() {
		return planRepaymentDate;
	}

	public void setPlanRepaymentDate(String planRepaymentDate) {
		this.planRepaymentDate = planRepaymentDate;
	}

	public String getPlanRepaymentPrincipal() {
		return planRepaymentPrincipal;
	}

	public void setPlanRepaymentPrincipal(String planRepaymentPrincipal) {
		this.planRepaymentPrincipal = planRepaymentPrincipal;
	}

	public String getPlanRepaymentInterest() {
		return planRepaymentInterest;
	}

	public void setPlanRepaymentInterest(String planRepaymentInterest) {
		this.planRepaymentInterest = planRepaymentInterest;
	}

	public String getLoanFees() {
		return loanFees;
	}

	public void setLoanFees(String loanFees) {
		this.loanFees = loanFees;
	}

	public String getTechnicalServicesFees() {
		return technicalServicesFees;
	}

	public void setTechnicalServicesFees(String technicalServicesFees) {
		this.technicalServicesFees = technicalServicesFees;
	}

	public String getOtherFees() {
		return otherFees;
	}

	public void setOtherFees(String otherFees) {
		this.otherFees = otherFees;
	}

	public String getRepaymentExecutionState() {
		return repaymentExecutionState;
	}

	public void setRepaymentExecutionState(String repaymentExecutionState) {
		this.repaymentExecutionState = repaymentExecutionState;
	}
	
	public String getPenaltyFee() {
		return penaltyFee;
	}
	
	public void setPenaltyFee(String penaltyFee) {
		this.penaltyFee = penaltyFee;
	}
	
	public String getTotalOverdueFee() {
		return totalOverdueFee;
	}
	
	public void setTotalOverdueFee(String totalOverdueFee) {
		this.totalOverdueFee = totalOverdueFee;
	}

	public Integer getCurrentPeriod() {
		return currentPeriod;
	}

	public void setCurrentPeriod(Integer currentPeriod) {
		this.currentPeriod = currentPeriod;
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
	
}
