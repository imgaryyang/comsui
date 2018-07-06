package com.suidifu.munichre;

import java.math.BigDecimal;

public class RepaymentPlanDetail {
	
	/** 利息 **/
	private BigDecimal assetInterest;
	/** uuid **/
	private String assetUuid;
	/** 总额 **/
	private BigDecimal assetAmount;
	
	public BigDecimal getAssetInterest() {
		return assetInterest;
	}
	public void setAssetInterest(BigDecimal assetInterest) {
		this.assetInterest = assetInterest;
	}
	public String getAssetUuid() {
		return assetUuid;
	}
	public void setAssetUuid(String assetUuid) {
		this.assetUuid = assetUuid;
	}
	public BigDecimal getAssetAmount() {
		return assetAmount;
	}
	public void setAssetAmount(BigDecimal assetAmount) {
		this.assetAmount = assetAmount;
	}
	
}
