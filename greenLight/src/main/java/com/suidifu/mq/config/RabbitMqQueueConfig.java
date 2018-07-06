package com.suidifu.mq.config;

public class RabbitMqQueueConfig {
	private String requestQueueName;
	private String replyQueueName;
	private String exchange;
	private String routingKey;
	private String[] routingKeyList;
	private int concurrentConsumers;
	private long receiveTimeout;
	private long replyTimeout;
	public static final long DEFAULT_REPLY_TIMEOUT = 5_000;
	public static final long DEFAULT_RECEIVE_TIMEOUT = 1_000;

	public String getRequestQueueName() {
		return requestQueueName;
	}

	public void setRequestQueueName(String requestQueueName) {
		this.requestQueueName = requestQueueName;
	}

	public String getReplyQueueName() {
		return replyQueueName;
	}

	public void setReplyQueueName(String replyQueueName) {
		this.replyQueueName = replyQueueName;
	}

	public String getExchange() {
		return exchange;
	}

	public void setExchange(String exchange) {
		this.exchange = exchange;
	}

	public String getRoutingKey() {
		return routingKey;
	}

	public void setRoutingKey(String routingKey) {
		this.routingKey = routingKey;
	}

	public int getConcurrentConsumers() {
		return concurrentConsumers;
	}

	public void setConcurrentConsumers(int concurrentConsumers) {
		this.concurrentConsumers = concurrentConsumers;
	}

	public long getReceiveTimeout() {
		return receiveTimeout <= 0 ? DEFAULT_RECEIVE_TIMEOUT : receiveTimeout;
	}

	public void setReceiveTimeout(int receiveTimeout) {
		this.receiveTimeout = receiveTimeout * 1000;
	}

	public long getReplyTimeout() {
		return replyTimeout <= 0 ? DEFAULT_REPLY_TIMEOUT : replyTimeout;
	}

	public void setReplyTimeout(int replyTimeout) {
		this.replyTimeout = replyTimeout * 1000;
	}

	public String[] getRoutingKeyList() {
		return routingKeyList;
	}

	public void setRoutingKeyList(String[] routingKeyList) {
		this.routingKeyList = routingKeyList;
	}
}
