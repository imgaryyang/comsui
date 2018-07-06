package com.suidifu.jpmorgan.entity;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.alibaba.fastjson.JSON;
import com.demo2do.core.utils.StringUtils;

@Entity
@Table(name = "payment_order_worker_config")
public class PaymentOrderWorkerConfig {

	@Id
	@GeneratedValue
	private Long id;

	private String paymentOrderWorkerUuid;

	private String paymentGatewayUuid;

	private String ip;

	private String port;

	private String url;

	@Column(columnDefinition = "text")
	private String localWorkingConfig;// 私钥 证书等static信息

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPaymentOrderWorkerUuid() {
		return paymentOrderWorkerUuid;
	}

	public void setPaymentOrderWorkerUuid(String paymentOrderWorkerUuid) {
		this.paymentOrderWorkerUuid = paymentOrderWorkerUuid;
	}

	public String getPaymentGatewayUuid() {
		return paymentGatewayUuid;
	}

	public void setPaymentGatewayUuid(String paymentGatewayUuid) {
		this.paymentGatewayUuid = paymentGatewayUuid;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@SuppressWarnings("unchecked")
	public Map<String, String> getLocalWorkingConfig() {
		try {
			if (StringUtils.isEmpty(this.localWorkingConfig)) {
				new HashMap<String, String>();
			}
			return (Map<String, String>) JSON.parse(this.localWorkingConfig);
		} catch (Exception e) {
			e.printStackTrace();
			return new HashMap<String, String>();
		}
	}

	public void setLocalWorkingConfig(String localWorkingConfig) {
		this.localWorkingConfig = localWorkingConfig;
	}

}
