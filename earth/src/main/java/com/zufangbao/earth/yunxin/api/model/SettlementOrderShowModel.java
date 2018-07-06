package com.zufangbao.earth.yunxin.api.model;

import com.demo2do.core.utils.DateUtils;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.yunxin.SettlementStatus;
import com.zufangbao.sun.yunxin.entity.SettlementOrder;

import java.math.BigDecimal;

public class SettlementOrderShowModel {
	//清算单号
	private String settlementOrderNo;
	//清算金额
	private BigDecimal settlementOrderAmount;
	//合作商户名称
	private String appName;
	//发生时间
	private String createTime;
	//清算状态
	private Integer settlementOrderStatus;
	
	public SettlementOrderShowModel(SettlementOrder settlementOrder, SettlementStatus settlementStatus, FinancialContract financialContract){
		this.settlementOrderNo = settlementOrder.getSettleOrderNo();
		this.settlementOrderAmount = settlementOrder.getSettlementAmount();
		this.appName = financialContract.getApp().getName();
		if(settlementOrder.getCreateTime() != null){
			this.createTime = DateUtils.format(settlementOrder.getCreateTime(), "yyyy-MM-dd HH:mm:ss");
		}
		this.settlementOrderStatus = settlementStatus == null? null : settlementStatus.ordinal();
	}
	
	public SettlementOrderShowModel(){
		super();
	}

	public String getSettlementOrderNo() {
		return settlementOrderNo;
	}

	public void setSettlementOrderNo(String settlementOrderNo) {
		this.settlementOrderNo = settlementOrderNo;
	}

	public BigDecimal getSettlementOrderAmount() {
		return settlementOrderAmount;
	}

	public void setSettlementOrderAmount(BigDecimal settlementOrderAmount) {
		this.settlementOrderAmount = settlementOrderAmount;
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

	public Integer getSettlementOrderStatus() {
		return settlementOrderStatus;
	}

	public void setSettlementOrderStatus(Integer settlementOrderStatus) {
		this.settlementOrderStatus = settlementOrderStatus;
	}
	
	
}
