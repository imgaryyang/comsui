package com.suidifu.mq.config;

public class RabbitMqConfig {

	private String host;
	private int port;
	private String userName;
	private String password;
	private String serviceName;
	private int start;
	private int end;
	private int consumers;
	private int receiveTimeout;
	private int replyTimeout;
	private String topicRouting;

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getEnd() {
		return end;
	}

	public void setEnd(int end) {
		this.end = end;
	}

	public int getConsumers() {
		return consumers;
	}

	public void setConsumers(int consumers) {
		this.consumers = consumers;
	}

	public int getReceiveTimeout() {
		return receiveTimeout;
	}

	public void setReceiveTimeout(int receiveTimeout) {
		this.receiveTimeout = receiveTimeout;
	}

	public int getReplyTimeout() {
		return replyTimeout;
	}

	public void setReplyTimeout(int replyTimeout) {
		this.replyTimeout = replyTimeout;
	}

	public String getTopicRouting() {
		return topicRouting;
	}

	public void setTopicRouting(String topicRouting) {
		this.topicRouting = topicRouting;
	}

	@Override
	public String toString() {
		return "RabbitMqConfig [host=" + host + ", port=" + port + ", userName=" + userName + ", password=" + password + ", serviceName=" + serviceName + ", start=" + start + ", end=" + end + ", consumers=" + consumers + ", receiveTimeout=" + receiveTimeout + ", replyTimeout=" + replyTimeout
				+ ", topicRouting=" + topicRouting + "]";
	}

}
