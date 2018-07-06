package com.suidifu.hathaway.configurations;

public class RedisPool {
	private int maxIdle;
	private int maxWait;

	public int getMaxIdle() {
		return maxIdle;
	}

	public void setMaxIdle(int maxIdle) {
		this.maxIdle = maxIdle;
	}

	public int getMaxWait() {
		return maxWait;
	}

	public void setMaxWait(int maxWait) {
		this.maxWait = maxWait;
	}

	@Override
	public String toString() {
		return "RedisPool [maxIdle=" + maxIdle + ", maxWait=" + maxWait + "]";
	}

}
