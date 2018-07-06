package com.suidifu.bridgewater.api.entity.deduct.batch.v2;

import com.zufangbao.sun.utils.ApplicationMessageUtils;

/**
 * @author wukai
 * 处理扣款失败类型
 */
public enum ProcessDeductResultFailType {
	
	/**
	 * 校验失败
	 */
	VALIDATE_FAIL("enum.processdeductresultfailtype.validate_fail"),
	
	/**
	 * http请求失败
	 */
	HTTP_FAIL("enum.processdeductresultfailtype.http_fail"),
	
	;
	
	private String key;

	private ProcessDeductResultFailType(String key) {
		this.key = key;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}


	public  String getChineseMessage() {
		return ApplicationMessageUtils.getChineseMessage(this.getKey());
	}
	
}
