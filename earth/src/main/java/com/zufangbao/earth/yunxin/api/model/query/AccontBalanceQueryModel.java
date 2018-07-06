package com.zufangbao.earth.yunxin.api.model.query;

import org.apache.commons.lang.StringUtils;

public class AccontBalanceQueryModel {
	
	/**
	 * 请求唯一编号
	 */
	private String requestNo;
	
	/**
	 *信托产品代码 
	 */
	private String productCode;
	
	/**
	 * 专户帐号
	 */
	private String accountNo;

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

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}
	
	public boolean hasError() {
		return (StringUtils.isEmpty(requestNo) || StringUtils.isEmpty(accountNo) || StringUtils.isEmpty(productCode));
	}
}
