package com.zufangbao.earth.yunxin.api.model;

import com.demo2do.core.utils.DateUtils;
import com.zufangbao.sun.entity.repurchase.RepurchaseDoc;

import java.math.BigDecimal;



public class RepurchaseDetail {
	/**
	 * 回购单号
	 */
	private String repurchaseDocUuid;
	/**
	 * 回购金额
	 */
	private BigDecimal amount;
	
	/**
	 * 回购起始日
	 */
	private String repoStartDate;
	/**
	 * 回购截止日
	 */
	private String repoEndDate;
	/**
	 * 合作商户名称
	 */
	private String appName;
	/**
	 * 回购天数
	 */
	private int repoDays;
	
	/**	
	 * 发生时间 置回购中状态的时间
	 */
	private String creatTime;
	
	/**
	 * 回购状态
	 */
	private int repurchaseStatus;
	
	public RepurchaseDetail(){}
	
	public RepurchaseDetail(RepurchaseDoc repurchaseDoc){
		 this.repurchaseDocUuid = repurchaseDoc.getRepurchaseDocUuid();
		 this.amount = repurchaseDoc.getAmount();
		 this.repoStartDate = DateUtils.format(repurchaseDoc.getRepoStartDate());
		 this.appName = repurchaseDoc.getAppName();
		 this.repoEndDate = DateUtils.format(repurchaseDoc.getRepoEndDate());
		 this.repoDays = repurchaseDoc.getRepoDays();
		 this.creatTime = DateUtils.format(repurchaseDoc.getCreatTime(),"yyyy-MM-dd hh:mm:ss");
		 this.repurchaseStatus = repurchaseDoc.getRepurchaseStatus().ordinal();
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

	public String getRepoStartDate() {
		return repoStartDate;
	}

	public void setRepoStartDate(String repoStartDate) {
		this.repoStartDate = repoStartDate;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getRepoEndDate() {
		return repoEndDate;
	}

	public void setRepoEndDate(String repoEndDate) {
		this.repoEndDate = repoEndDate;
	}

	public int getRepoDays() {
		return repoDays;
	}

	public void setRepoDays(int repoDays) {
		this.repoDays = repoDays;
	}

	public String getCreatTime() {
		return creatTime;
	}

	public void setCreatTime(String creatTime) {
		this.creatTime = creatTime;
	}

	public int getRepurchaseStatus() {
		return repurchaseStatus;
	}

	public void setRepurchaseStatus(int repurchaseStatus) {
		this.repurchaseStatus = repurchaseStatus;
	}
	
}
