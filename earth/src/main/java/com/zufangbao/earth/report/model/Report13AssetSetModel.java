package com.zufangbao.earth.report.model;

import com.zufangbao.sun.utils.DateUtils;
import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.util.Date;

public class Report13AssetSetModel {

	private Long contractId;

	private Date assetRecycleDate;

	private Date actualRecycleDate;

	private Integer currentPeriod;

	private BigDecimal assetPrincipalValue;

	private BigDecimal assetInterestValue;

	private Integer assetStatus;

	private Integer onAccountStatus;

	public Long getContractId() {
		return contractId;
	}

	public void setContractId(Long contractId) {
		this.contractId = contractId;
	}

	public Date getAssetRecycleDate() {
		return assetRecycleDate;
	}
	
	public String getAssetRecycleDateStr() {
		return assetRecycleDate == null ? StringUtils.EMPTY : DateUtils.format(assetRecycleDate, "yyyy-MM-dd");
	}

	public void setAssetRecycleDate(Date assetRecycleDate) {
		this.assetRecycleDate = assetRecycleDate;
	}

	public Date getActualRecycleDate() {
		return actualRecycleDate;
	}
	
	public String getActualRecycleDateStr() {
		return actualRecycleDate == null ? StringUtils.EMPTY : DateUtils.format(actualRecycleDate, "yyyy-MM-dd");
	}

	public void setActualRecycleDate(Date actualRecycleDate) {
		this.actualRecycleDate = actualRecycleDate;
	}

	public Integer getCurrentPeriod() {
		return currentPeriod;
	}
	
	public String getCurrentPeriodStr() {
		return currentPeriod == null ? StringUtils.EMPTY : currentPeriod.toString() ;
	}

	public void setCurrentPeriod(Integer currentPeriod) {
		this.currentPeriod = currentPeriod;
	}

	public BigDecimal getAssetPrincipalValue() {
		return assetPrincipalValue;
	}
	
	public String getAssetPrincipalValueStr() {
		return assetPrincipalValue == null ? StringUtils.EMPTY : assetPrincipalValue.toString();
	}

	public void setAssetPrincipalValue(BigDecimal assetPrincipalValue) {
		this.assetPrincipalValue = assetPrincipalValue;
	}

	public BigDecimal getAssetInterestValue() {
		return assetInterestValue;
	}
	
	public String getAssetInterestValueStr() {
		return assetInterestValue == null ? StringUtils.EMPTY : assetInterestValue.toString();
	}

	public void setAssetInterestValue(BigDecimal assetInterestValue) {
		this.assetInterestValue = assetInterestValue;
	}

	public Integer getAssetStatus() {
		return assetStatus;
	}

	public void setAssetStatus(Integer assetStatus) {
		this.assetStatus = assetStatus;
	}

	public Integer getOnAccountStatus() {
		return onAccountStatus;
	}

	public void setOnAccountStatus(Integer onAccountStatus) {
		this.onAccountStatus = onAccountStatus;
	}

	public Report13AssetSetModel() {
		super();
	}

}
