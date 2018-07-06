package com.zufangbao.earth.yunxin.api.model;

import com.demo2do.core.utils.DateUtils;
import com.zufangbao.sun.entity.repurchase.RepurchaseDoc;

import java.math.BigDecimal;

public class RepurchaseShowModel {
	//回购单号
	private String repurchaseDocUuid;
	//回购金额
	private BigDecimal amount;
	//回购本金
	private BigDecimal outstandingPrincipal = BigDecimal.ZERO;
	//回购利息
	private BigDecimal outstandingInterest = BigDecimal.ZERO;
    //罚息
    private BigDecimal outstandingPenaltyInterest = BigDecimal.ZERO;
    //回购其他费用
    private BigDecimal outstandingOtherFee = BigDecimal.ZERO;
	//回购起始日
	private String repoStartDate;
	//回购截止日
	private String repoEndDate;
	//合作商户名称
	private String appName;
	//回购天数
	private Integer repoDays;
	//创建时间
	private String creatTime;
	//状态变更时间
	private String lastModifedTime;
	//回购期数
	private Integer repurchasePeriods;
	//回购状态
	private Integer repurchaseStatus;
	
	public RepurchaseShowModel(RepurchaseDoc repurchaseDoc){
		this.repurchaseDocUuid = repurchaseDoc.getRepurchaseDocUuid();
		this.amount = repurchaseDoc.getAmount();

		this.outstandingPrincipal = repurchaseDoc.getRepurchasePrincipal();
		this.outstandingInterest = repurchaseDoc.getRepurchaseInterest();
		this.outstandingPenaltyInterest = repurchaseDoc.getRepurchasePenalty();
		this.outstandingOtherFee = repurchaseDoc.getRepurchaseOtherCharges();

		if(repurchaseDoc.getRepoStartDate() != null){
			this.repoStartDate = DateUtils.format(repurchaseDoc.getRepoStartDate());
		}
		if(repurchaseDoc.getRepoEndDate() != null){
			this.repoEndDate = DateUtils.format(repurchaseDoc.getRepoEndDate());
		}
		this.appName = repurchaseDoc.getAppName();
		this.repoDays = repurchaseDoc.getRepoDays();
		if(repurchaseDoc.getCreatTime() != null){
			this.creatTime = DateUtils.format(repurchaseDoc.getCreatTime(), "yyyy-MM-dd HH:mm:ss");
		}
		if(repurchaseDoc.getLastModifedTime() != null){
			this.lastModifedTime = DateUtils.format(repurchaseDoc.getLastModifedTime(), "yyyy-MM-dd HH:mm:ss");
		}
		this.repurchasePeriods = repurchaseDoc.getRepurchasePeriods();
		this.repurchaseStatus = repurchaseDoc.getRepurchaseStatus() == null ? null: repurchaseDoc.getRepurchaseStatus().ordinal();
	}

	public RepurchaseShowModel() {
		super();
	}

	public String getRepurchaseDocUuid() {
		return repurchaseDocUuid;
	}

	public void setRepurchaseDocUuid(String repurchaseDocUuid) {
		this.repurchaseDocUuid = repurchaseDocUuid;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getOutstandingPrincipal() {
		return outstandingPrincipal;
	}

	public void setOutstandingPrincipal(BigDecimal outstandingPrincipal) {
		this.outstandingPrincipal = outstandingPrincipal;
	}

	public BigDecimal getOutstandingInterest() {
		return outstandingInterest;
	}

	public void setOutstandingInterest(BigDecimal outstandingInterest) {
		this.outstandingInterest = outstandingInterest;
	}

	public BigDecimal getOutstandingPenaltyInterest() {
		return outstandingPenaltyInterest;
	}

	public void setOutstandingPenaltyInterest(BigDecimal outstandingPenaltyInterest) {
		this.outstandingPenaltyInterest = outstandingPenaltyInterest;
	}

	public BigDecimal getOutstandingOtherFee() {
		return outstandingOtherFee;
	}

	public void setOutstandingOtherFee(BigDecimal outstandingOtherFee) {
		this.outstandingOtherFee = outstandingOtherFee;
	}

	public String getRepoStartDate() {
		return repoStartDate;
	}

	public void setRepoStartDate(String repoStartDate) {
		this.repoStartDate = repoStartDate;
	}

	public String getRepoEndDate() {
		return repoEndDate;
	}

	public void setRepoEndDate(String repoEndDate) {
		this.repoEndDate = repoEndDate;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public Integer getRepoDays() {
		return repoDays;
	}

	public void setRepoDays(Integer repoDays) {
		this.repoDays = repoDays;
	}

	public String getCreatTime() {
		return creatTime;
	}

	public void setCreatTime(String creatTime) {
		this.creatTime = creatTime;
	}

	public String getLastModifedTime() {
		return lastModifedTime;
	}

	public void setLastModifedTime(String lastModifedTime) {
		this.lastModifedTime = lastModifedTime;
	}

	public Integer getRepurchasePeriods() {
		return repurchasePeriods;
	}

	public void setRepurchasePeriods(Integer repurchasePeriods) {
		this.repurchasePeriods = repurchasePeriods;
	}

	public Integer getRepurchaseStatus() {
		return repurchaseStatus;
	}

	public void setRepurchaseStatus(Integer repurchaseStatus) {
		this.repurchaseStatus = repurchaseStatus;
	}
	
	
	
}
