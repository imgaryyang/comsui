package com.suidifu.bridgewater.model.v2;

import java.util.UUID;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.annotation.JSONField;

public class BatchDeductCommandRequestModel {

	private String batchDeductApplicationUuid = UUID.randomUUID().toString();
	
	/**
	 * 功能代码
	 */
	private String fn;
	
	/**
	 * 请求唯一标识
	 */
	private String requestNo;
	
	/**
	 * 批次号
	 */
	private String batchDeductId;
	
	/**
	 * 批次回调地址
	 */
	private String notifyUrl;
	
	/**
	 * 文件md5
	 */
	private String md5;
	
	private String checkFailedMsg;

	@JSONField(serialize = false)
	public String getBatchDeductApplicationUuid() {
		return batchDeductApplicationUuid;
	}

	public void setBatchDeductApplicationUuid(String batchDeductApplicationUuid) {
		this.batchDeductApplicationUuid = batchDeductApplicationUuid;
	}

	public String getFn() {
		return fn;
	}

	public void setFn(String fn) {
		this.fn = fn;
	}

	public String getRequestNo() {
		return requestNo;
	}

	public void setRequestNo(String requestNo) {
		this.requestNo = requestNo;
	}

	public String getBatchDeductId() {
		return batchDeductId;
	}

	public void setBatchDeductId(String batchDeductId) {
		this.batchDeductId = batchDeductId;
	}

	public String getNotifyUrl() {
		return notifyUrl;
	}

	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}

	public String getMd5() {
		return md5;
	}

	public void setMd5(String md5) {
		this.md5 = md5;
	}

	public String getCheckFailedMsg() {
		return checkFailedMsg;
	}

	public void setCheckFailedMsg(String checkFailedMsg) {
		this.checkFailedMsg = checkFailedMsg;
	}
	
	@JSONField(serialize = false)
	public boolean isValid() {
		if(StringUtils.isEmpty(requestNo)) {
			this.checkFailedMsg = "请求唯一编号［requestNo］，不能为空！";
			return false;
		}
		if(StringUtils.isEmpty(batchDeductId)) {
			this.checkFailedMsg = "扣款批次号［batchDeductId］，不能为空！";
			return false;
		}
		if(StringUtils.isEmpty(notifyUrl)) {
			this.checkFailedMsg = "批次回调地址［notifyUrl］，不能为空！";
			return false;
		}
		if(StringUtils.isEmpty(md5)) {
			this.checkFailedMsg = "文件md5［md5］，不能为空！";
			return false;
		}
		
		return true;
	}
}
