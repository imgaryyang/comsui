package com.suidifu.hathaway.configurations;

public class RedisConfig {

	private String host;
	private int port;
	private String password;
	private int timeout;
	private RedisPool pool;

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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public RedisPool getPool() {
		return pool;
	}

	public void setPool(RedisPool pool) {
		this.pool = pool;
	}

	@Override
	public String toString() {
		return "RedisConfig [host=" + host + ", port=" + port + ", password=" + password + ", timeout=" + timeout + ", pool=" + pool + "]";
	}

}
