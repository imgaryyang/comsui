package com.zufangbao.earth.yunxin.api.model;

import com.demo2do.core.utils.DateUtils;
import com.zufangbao.sun.entity.order.Order;
import com.zufangbao.sun.yunxin.entity.GuaranteeStatus;

import java.math.BigDecimal;

public class GuaranteeOrderShowModel {
	//担保补足号
	private String guaranteeOrderNo;
	//担保金额
	private BigDecimal guaranteeOrderAmount;
	//担保截止日
	private String guaranteeDueDate;
	//合作商户名称
	private String appName;
	//发生时间
	private String createTime;
	//担保状态
	private Integer guaranteeOrderStatus;
	
	public GuaranteeOrderShowModel(Order guaranteeOrder, GuaranteeStatus guaranteeStatus){
		this.guaranteeOrderNo = guaranteeOrder.getOrderNo();
		this.guaranteeOrderAmount = guaranteeOrder.getTotalRent();
		if(guaranteeOrder.getDueDate() != null){
			this.guaranteeDueDate = DateUtils.format(guaranteeOrder.getDueDate());
		}
		this.appName = guaranteeOrder.getFinancialContract().getApp().getName();
		if(guaranteeOrder.getCreateTime() != null){
			this.createTime = DateUtils.format(guaranteeOrder.getCreateTime(), "yyyy-MM-dd HH:mm:ss");
		}
		this.guaranteeOrderStatus = guaranteeStatus == null ? null: guaranteeStatus.ordinal();
	}

	public GuaranteeOrderShowModel() {
		super();
	}

	public String getGuaranteeOrderNo() {
		return guaranteeOrderNo;
	}

	public void setGuaranteeOrderNo(String guaranteeOrderNo) {
		this.guaranteeOrderNo = guaranteeOrderNo;
	}

	public BigDecimal getGuaranteeOrderAmount() {
		return guaranteeOrderAmount;
	}

	public void setGuaranteeOrderAmount(BigDecimal guaranteeOrderAmount) {
		this.guaranteeOrderAmount = guaranteeOrderAmount;
	}

	public String getGuaranteeDueDate() {
		return guaranteeDueDate;
	}

	public void setGuaranteeDueDate(String guaranteeDueDate) {
		this.guaranteeDueDate = guaranteeDueDate;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public Integer getGuaranteeOrderStatus() {
		return guaranteeOrderStatus;
	}

	public void setGuaranteeOrderStatus(Integer guaranteeOrderStatus) {
		this.guaranteeOrderStatus = guaranteeOrderStatus;
	}
	
	
}
