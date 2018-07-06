package com.suidifu.jpmorgan.entity;

import com.suidifu.jpmorgan.handler.PaymentHandler;

public class PaymentWorkerContext {

	private PaymentOrderWorkerConfig workerConfig;
	private String queueTableName;
	private String logTableName;
	private PaymentHandler workingHanlder;
	private String gatewayName;

	public PaymentOrderWorkerConfig getWorkerConfig() {
		return workerConfig;
	}

	public void setWorkerConfig(PaymentOrderWorkerConfig workerConfig) {
		this.workerConfig = workerConfig;
	}

	public String getQueueTableName() {
		return queueTableName;
	}

	public void setQueueTableName(String queueTableName) {
		this.queueTableName = queueTableName;
	}

	public PaymentHandler getWorkingHanlder() {
		return workingHanlder;
	}

	public void setWorkingHanlder(PaymentHandler workingHanlder) {
		this.workingHanlder = workingHanlder;
	}

	public String getLogTableName() {
		return logTableName;
	}

	public void setLogTableName(String logTableName) {
		this.logTableName = logTableName;
	}

	public String getGatewayName() {
		return gatewayName;
	}

	public void setGatewayName(String gatewayName) {
		this.gatewayName = gatewayName;
	}

	public PaymentWorkerContext(PaymentOrderWorkerConfig workerConfig,
			String queueTableName, String logTableName,
			PaymentHandler workingHanlder) {
		super();
		this.workerConfig = workerConfig;
		this.queueTableName = queueTableName;
		this.logTableName = logTableName;
		this.workingHanlder = workingHanlder;
	}
	
	public PaymentWorkerContext(PaymentOrderWorkerConfig workerConfig,
			String queueTableName, String logTableName,
			PaymentHandler workingHanlder, String gatewayName) {
		super();
		this.workerConfig = workerConfig;
		this.queueTableName = queueTableName;
		this.logTableName = logTableName;
		this.workingHanlder = workingHanlder;
		this.gatewayName = gatewayName;
	}

	public PaymentWorkerContext() {
		super();
	}

}
