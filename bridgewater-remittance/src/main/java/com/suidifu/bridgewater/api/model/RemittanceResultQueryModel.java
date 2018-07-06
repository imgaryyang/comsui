package com.suidifu.bridgewater.api.model;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.annotation.JSONField;

public class RemittanceResultQueryModel {

	/**
	 * 功能代码
	 */
	private String fn;
	
	/**
	 * 请求编号
	 */
	private String requestNo;
	
	/**
	 * 原放款请求号
	 */
	private String oriRequestNo;
	
	/**
	 * 贷款合同唯一编号
	 */
	private String uniqueId;
	
	/**
	 * 贷款合同号
	 */
	private String contractNo;
	
	/**
	 * 校验失败信息
	 */
	private String checkFailedMsg;

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

	public String getOriRequestNo() {
		return oriRequestNo;
	}

	public void setOriRequestNo(String oriRequestNo) {
		this.oriRequestNo = oriRequestNo;
	}

	public String getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}
	
	public String getContractNo() {
		return contractNo;
	}

	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}

	@JSONField(serialize = false)
	public String getCheckFailedMsg() {
		return checkFailedMsg;
	}

	public void setCheckFailedMsg(String checkFailedMsg) {
		this.checkFailedMsg = checkFailedMsg;
	}

	@JSONField(serialize = false)
	public boolean isValid() {
		if(StringUtils.isEmpty(this.requestNo)) {
			this.checkFailedMsg = "请求唯一标识［requestNo］，不能为空！";
			return false;
		}
		
		if(StringUtils.isEmpty(this.oriRequestNo)) {
			this.checkFailedMsg = "原始放款请求号［oriRequestNo］，不能为空！";
			return false;
		}
		
		if(StringUtils.isEmpty(this.uniqueId)) {
			this.checkFailedMsg = "贷款合同唯一编号［uniqueId］，不能为空！";
			return false;
		}
		return true;
	}
	
}
