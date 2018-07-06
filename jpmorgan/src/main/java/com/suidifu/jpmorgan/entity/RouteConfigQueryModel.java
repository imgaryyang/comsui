package com.suidifu.jpmorgan.entity;

public class RouteConfigQueryModel {

	private String ip; // ip地址

	private String port; //

	private String url;//
	
	private String account;

	private GatewayOnlineStatus gatewayOnlineStatus;// online,offline

	private GatewayWorkingStatus gatewayWorkingStatus;// working,suspending,stop

	private GatewayType gatewayType;// ZJ,GY


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

	public GatewayOnlineStatus getGatewayOnlineStatus() {
		return gatewayOnlineStatus;
	}

	public void setGatewayOnlineStatus(GatewayOnlineStatus gatewayOnlineStatus) {
		this.gatewayOnlineStatus = gatewayOnlineStatus;
	}

	public GatewayWorkingStatus getGatewayWorkingStatus() {
		return gatewayWorkingStatus;
	}

	public void setGatewayWorkingStatus(
			GatewayWorkingStatus gatewayWorkingStatus) {
		this.gatewayWorkingStatus = gatewayWorkingStatus;
	}

	public GatewayType getGatewayType() {
		return gatewayType;
	}

	public void setGatewayType(GatewayType gatewayType) {
		this.gatewayType = gatewayType;
	}


	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}
	
	public RouteConfigQueryModel() {
		super();
	}

	public RouteConfigQueryModel(String account,
			GatewayOnlineStatus gatewayOnlineStatus,
			GatewayWorkingStatus gatewayWorkingStatus, GatewayType gatewayType) {
		super();
		this.account = account;
		this.gatewayOnlineStatus = gatewayOnlineStatus;
		this.gatewayWorkingStatus = gatewayWorkingStatus;
		this.gatewayType = gatewayType;
	}

}
