package com.suidifu.jpmorgan.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "payment_gateway_config")
public class PaymentGatewayConfig {

	@Id
	@GeneratedValue
	private Long id;

	private String paymentGatewayUuid;

	private String paymentChannelUuid;

	@Enumerated(EnumType.ORDINAL)
	private GatewayType gatewayType;

	private GatewayOnlineStatus gatewayOnlineStatus;// online,offline

	private GatewayWorkingStatus gatewayWorkingStatus;// working,suspending,stop

	private Date effectiveDate; // 生效日期

	private Date expirationDate;

	private String configKey;

	private String configValue;


//	private String channelAccountName;//TODO
//
//	private String channelAccountNo;
//	
//	@Column(columnDefinition = "text")
//	private String channelAccountAppendix;
//
//	@Column(columnDefinition = "text")
//	private String channelBankInfo;
	

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

	public Integer getGatewayType() {
		return gatewayType.ordinal();
	}

	public void setGatewayType(Integer gatewayType) {
		this.gatewayType = GatewayType.fromOrdinal(gatewayType);
	}

	public Integer getGatewayOnlineStatus() {
		return gatewayOnlineStatus.ordinal();
	}

	public void setGatewayOnlineStatus(Integer gatewayOnlineStatus) {
		this.gatewayOnlineStatus = GatewayOnlineStatus.fromOrdinal(gatewayOnlineStatus);
	}

	public Integer getGatewayWorkingStatus() {
		return gatewayWorkingStatus.ordinal();
	}

	public void setGatewayWorkingStatus(
			Integer gatewayWorkingStatus) {
		this.gatewayWorkingStatus = GatewayWorkingStatus.fromOrdinal(gatewayWorkingStatus);
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

}
