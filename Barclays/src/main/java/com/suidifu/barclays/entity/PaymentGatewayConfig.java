package com.suidifu.barclays.entity;

import java.util.Date;

public class PaymentGatewayConfig {
	private Long id;

	private String paymentGatewayUuid;

	private String paymentChannelUuid;

	private int gatewayType;
	/*
	 * 0-->银企直联
	 * 1-->超级网银
	 * 2-->银联
	 */

	private int gatewayOnlineStatus;
	/* 0-->online
	 * 1-->offline
	 */

	private int gatewayWorkingStatus;
	/* 0-->working
	 * 1-->suspending
	 * 2-->stop
	 */

	private Date effectiveDate; // 生效日期

	private Date expirationDate;

	private String configKey;

	private String configValue;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPaymentGatewayUuid() {
		return paymentGatewayUuid;
	}

	public void setPaymentGatewayUuid(String paymentGatewayUuid) {
		this.paymentGatewayUuid = paymentGatewayUuid;
	}

	public String getPaymentChannelUuid() {
		return paymentChannelUuid;
	}

	public void setPaymentChannelUuid(String paymentChannelUuid) {
		this.paymentChannelUuid = paymentChannelUuid;
	}

	public Date getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	public String getConfigKey() {
		return configKey;
	}

	public void setConfigKey(String configKey) {
		this.configKey = configKey;
	}

	public String getConfigValue() {
		return configValue;
	}

	public void setConfigValue(String configValue) {
		this.configValue = configValue;
	}

	public int getGatewayType() {
		return gatewayType;
	}

	public void setGatewayType(int gatewayType) {
		this.gatewayType = gatewayType;
	}

	public int getGatewayOnlineStatus() {
		return gatewayOnlineStatus;
	}

	public void setGatewayOnlineStatus(int gatewayOnlineStatus) {
		this.gatewayOnlineStatus = gatewayOnlineStatus;
	}

	public int getGatewayWorkingStatus() {
		return gatewayWorkingStatus;
	}

	public void setGatewayWorkingStatus(int gatewayWorkingStatus) {
		this.gatewayWorkingStatus = gatewayWorkingStatus;
	}
}
