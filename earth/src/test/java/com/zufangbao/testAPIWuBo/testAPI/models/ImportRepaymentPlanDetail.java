package com.zufangbao.testAPIWuBo.testAPI.models;

import com.alibaba.fastjson.annotation.JSONField;
import com.zufangbao.sun.ledgerbook.ChartOfAccount;
import com.zufangbao.sun.utils.Md5Util;

import java.math.BigDecimal;

public class ImportRepaymentPlanDetail {

	private String repayScheduleNo;
	private String repaymentDate;
	
	private String repaymentPrincipal;
	
	private String repaymentInterest;
	
	private String techMaintenanceFee;
	
	private String loanServiceFee;
	
	private String otheFee;
	
	
	public BigDecimal getTheAdditionalFee(){
		return new BigDecimal(techMaintenanceFee).add(new BigDecimal(loanServiceFee)).add(new BigDecimal(otheFee));
		
	}
	
	public String getRepaymentDate() {
		return repaymentDate;
	}

	public void setRepaymentDate(String repaymentDate) {
		this.repaymentDate = repaymentDate;
	}

	public String getRepaymentPrincipal() {
		return repaymentPrincipal;
	}

	public void setRepaymentPrincipal(String repaymentPrincipal) {
		this.repaymentPrincipal = repaymentPrincipal;
	}

	public String getRepaymentInterest() {
		return repaymentInterest;
	}

	public void setRepaymentInterest(String repaymentInterest) {
		this.repaymentInterest = repaymentInterest;
	}

	public String getOtheFee() {
		return otheFee;
	}
	
	public BigDecimal getBDOtheFee() {
		return new BigDecimal(otheFee);
	}

	public void setOtheFee(String otheFee) {
		this.otheFee = otheFee;
	}
	
	public String getTechMaintenanceFee() {
		return techMaintenanceFee;
	}
	
	public BigDecimal getBDTechMaintenanceFee() {
		return new BigDecimal(techMaintenanceFee);
	}

	public void setTechMaintenanceFee(String techMaintenanceFee) {
		this.techMaintenanceFee = techMaintenanceFee;
	}

	public String getLoanServiceFee() {
		return loanServiceFee;
	}
	
	public BigDecimal getBDLoanServiceFee() {
		return new BigDecimal(loanServiceFee);
	}

	public void setLoanServiceFee(String loanServiceFee) {
		this.loanServiceFee = loanServiceFee;
	}

	public String getRepayScheduleNo() {
		return repayScheduleNo;
	}

	public void setRepayScheduleNo(String repayScheduleNo) {
		this.repayScheduleNo = repayScheduleNo;
	}

	/**
     * assetInterestValue=XXX;assetPrincipalValue=XXX;assetRecycleDate=XXX
     */
	@JSONField(serialize = false)
	private String assetFingerPrintOrigin() {
		try {
			StringBuffer buffer = new StringBuffer();
			buffer.append("assetInterestValue=");
			buffer.append(repaymentInterest);
			buffer.append(";assetPrincipalValue=");
			buffer.append(repaymentPrincipal);
			buffer.append(";assetRecycleDate=");
			buffer.append(repaymentDate);
			return buffer.toString();
		} catch (Exception e) {
			return "";
		}
	}
	
	 /**
     * MD5(assetInterestValue=XXX;assetPrincipalValue=XXX;assetRecycleDate=XXX)
     */
	@JSONField(serialize = false)
	public String assetFingerPrint() {
		try {
			return Md5Util.encode(assetFingerPrintOrigin());
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * ChartOfAccount.SND_UNEARNED_LOAN_ASSET_TECH_FEE=XXX;ChartOfAccount.SND_UNEARNED_LOAN_ASSET_LOAN_SERVICE_FEE=XXX;ChartOfAccount.SND_UNEARNED_LOAN_ASSET_OTHER_FEE=XXX;......
	 */
	@JSONField(serialize = false)
	private String assetExtraFeeFingerPrintOrigin() {
		try {
			;
			StringBuffer buffer = new StringBuffer();
			buffer.append(ChartOfAccount.SND_UNEARNED_LOAN_ASSET_TECH_FEE);
			buffer.append("=");
			buffer.append(techMaintenanceFee);
			buffer.append(";");
			buffer.append(ChartOfAccount.SND_UNEARNED_LOAN_ASSET_LOAN_SERVICE_FEE);
			buffer.append("=");
			buffer.append(loanServiceFee);
			buffer.append(";");
			buffer.append(ChartOfAccount.SND_UNEARNED_LOAN_ASSET_OTHER_FEE);
			buffer.append("=");
			buffer.append(otheFee);
			return buffer.toString();
		} catch (Exception e) {
			return "";
		}
	}
	
	/**
	 * MD5(ChartOfAccount.SND_UNEARNED_LOAN_ASSET_TECH_FEE=XXX;ChartOfAccount.SND_UNEARNED_LOAN_ASSET_LOAN_SERVICE_FEE=XXX;ChartOfAccount.SND_UNEARNED_LOAN_ASSET_OTHER_FEE=XXX;......)
	 */
	@JSONField(serialize = false)
	public String assetExtraFeeFingerPrint() {
		try {
			return Md5Util.encode(assetExtraFeeFingerPrintOrigin());
		} catch (Exception e) {
			return "";
		}
	}
	
}
