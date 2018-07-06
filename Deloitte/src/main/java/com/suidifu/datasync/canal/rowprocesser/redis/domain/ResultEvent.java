package com.suidifu.datasync.canal.rowprocesser.redis.domain;

import com.suidifu.datasync.canal.StaticsConfig.RemittanceType;

public class ResultEvent {
	private String rediskey;
	private int eventCode;
	private Result result;

	public ResultEvent() {

	}

	public ResultEvent(RemittanceType remittanceType, String resultkey, int eventCode, Result result) {
		this.eventCode = eventCode;
		this.result = result;
		this.rediskey = remittanceType.type() + ":" + resultkey;
		if (this.result != null)
			this.result.setRedisKey(this.rediskey);
	}

	public String getRediskey() {
		return rediskey;
	}

	public void setRediskey(String rediskey) {
		this.rediskey = rediskey;
	}

	public int getEventCode() {
		return eventCode;
	}

	public void setEventCode(int eventCode) {
		this.eventCode = eventCode;
	}

	public Result getResult() {
		return result;
	}

	public void setResult(Result result) {
		this.result = result;
	}

}
