package com.suidifu.bridgewater.api.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.demo2do.core.utils.DateUtils;
import org.apache.commons.lang.StringUtils;

import java.util.Date;

public class RemittanceAuditbillDownloadModel {

	/**
	 * 功能代码（400001）
	 */
	private String fn;

	/**
	 * 请求唯一标识
	 */
	private String requestNo;

	/**
	 * 信托产品代码
	 */
	private String productCode;

	/**
	 * 清算日期
	 */
	private String settleDate;

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

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getSettleDate() {
		return settleDate;
	}

	public void setSettleDate(String settleDate) {
		this.settleDate = settleDate;
	}

	public String getCheckFailedMsg() {
		return checkFailedMsg;
	}

	public void setCheckFailedMsg(String checkFailedMsg) {
		this.checkFailedMsg = checkFailedMsg;
	}

	@JSONField(serialize = false)
	public boolean isValid() {
		if (StringUtils.isEmpty(this.requestNo)) {
			this.checkFailedMsg = "请求唯一标识［requestNo］，不能为空！";
			return false;
		}
		if (StringUtils.isEmpty(this.productCode)) {
			this.checkFailedMsg = "信贷产品代码［productCode］，不能为空！";
			return false;
		}
		Date date = null;
		try {
			date = DateUtils.parseDate(settleDate, "yyyy-MM-dd");
			if (null == date) {
				this.checkFailedMsg = "清算日期［settleDate］，格式错误！";
				return false;
			}
		} catch (Exception e) {
			this.checkFailedMsg = "清算日期［settleDate］，格式错误！";
			return false;
		}

		if (!(DateUtils.compareTwoDatesOnDay(date, DateUtils.parseDate(DateUtils.format(new Date(), "yyyy-MM-dd"), "yyyy-MM-dd")) < 0)) {
			this.checkFailedMsg = "清算日期［settleDate］，必须小于当前日期！";
			return false;
		}
		return true;
	}
}
