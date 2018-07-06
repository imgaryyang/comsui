package com.suidifu.bridgewater.model.v2;

import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;

public class DeductCommandModel {


	private String deductApplicationUuid = UUID.randomUUID().toString();
	
	/**
	 * 功能代码
	 */
	private String fn;
	
	/**
	 * 请求唯一标识
	 */
	private String requestNo;
	
	/**
	 * 扣款唯一编号
	 */
	private String deductId;
	
	/**
	 * 信托产品代码
	 */
	private String financialProductCode;
	
	/**
	 * 接口调用时间
	 */
	private String apiCalledTime;
	
	/**
	 * 贷款合同唯一编号
	 */
	private String uniqueId;
	
	/**
	 * 贷款合同编号
	 */
	private String contractNo;
	
	/**
	 * 交易类型
	 */
	private String transType;
	
	/**
	 * 扣款金额
	 */
	private String amount;
	
	/**
	 * 还款类型
	 */
	private int repaymentType;
	
	/**
	 * 手机号
	 */
	private String mobile;
	
	/**
	 * 对方账户名
	 */
	private String accountName;
	
	/**
	 * 对方账号
	 */
	private String accountNo;
	
	private String bankCode;
	
    private String idCardNo;

    private String bankProvince;

    private String bankCity;

    private String bankName;
	
	/**
	 * 回调地址
	 */
	private String notifyUrl;
	
	/**
	 *网关
	 */
	private String gateway;

	public DeductCommandModel() {
		super();
	}

	public DeductCommandModel(String fn, String requestNo, String deductId,
			String financialProductCode, String uniqueId, String contractNo,
			String transType, String amount, String accountName,
			String accountNo, String notifyUrl) {
		super();
		this.fn = fn;
		this.requestNo = requestNo;
		this.deductId = deductId;
		this.financialProductCode = financialProductCode;
		this.uniqueId = uniqueId;
		this.contractNo = contractNo;
		this.transType = transType;
		this.amount = amount;
		this.accountName = accountName;
		this.accountNo = accountNo;
		this.notifyUrl = notifyUrl;
	}

	public DeductCommandModel(Map<String, String> delayPostRequestParams) {
		super();
		this.fn = delayPostRequestParams.getOrDefault("fn", StringUtils.EMPTY);
		this.requestNo = delayPostRequestParams.getOrDefault("requestNo", StringUtils.EMPTY);
		this.deductId = delayPostRequestParams.getOrDefault("deductId", StringUtils.EMPTY);
		this.financialProductCode = delayPostRequestParams.getOrDefault("financialProductCode", StringUtils.EMPTY);
		this.uniqueId = delayPostRequestParams.getOrDefault("uniqueId", StringUtils.EMPTY);
		this.contractNo = delayPostRequestParams.getOrDefault("contractNo", StringUtils.EMPTY);
		this.transType = delayPostRequestParams.getOrDefault("transType", StringUtils.EMPTY);
		this.amount = delayPostRequestParams.getOrDefault("amount", StringUtils.EMPTY);
		this.accountName = delayPostRequestParams.getOrDefault("accountName", StringUtils.EMPTY);
		this.accountNo = delayPostRequestParams.getOrDefault("accountNo", StringUtils.EMPTY);
		this.bankCode = delayPostRequestParams.getOrDefault("bankCode", StringUtils.EMPTY);
		this.notifyUrl = delayPostRequestParams.getOrDefault("notifyUrl", StringUtils.EMPTY);
	}
	
	public String getDeductApplicationUuid() {
		return deductApplicationUuid;
	}

	public void setDeductApplicationUuid(String deductApplicationUuid) {
		this.deductApplicationUuid = deductApplicationUuid;
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

	public String getDeductId() {
		return deductId;
	}

	public void setDeductId(String deductId) {
		this.deductId = deductId;
	}

	public String getFinancialProductCode() {
		return financialProductCode;
	}

	public void setFinancialProductCode(String financialProductCode) {
		this.financialProductCode = financialProductCode;
	}

	public String getApiCalledTime() {
		return apiCalledTime;
	}

	public void setApiCalledTime(String apiCalledTime) {
		this.apiCalledTime = apiCalledTime;
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

	public String getTransType() {
		return transType;
	}

	public void setTransType(String transType) {
		this.transType = transType;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public int getRepaymentType() {
		return repaymentType;
	}

	public void setRepaymentType(int repaymentType) {
		this.repaymentType = repaymentType;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getIdCardNo() {
		return idCardNo;
	}

	public void setIdCardNo(String idCardNo) {
		this.idCardNo = idCardNo;
	}

	public String getBankProvince() {
		return bankProvince;
	}

	public void setBankProvince(String bankProvince) {
		this.bankProvince = bankProvince;
	}

	public String getBankCity() {
		return bankCity;
	}

	public void setBankCity(String bankCity) {
		this.bankCity = bankCity;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getNotifyUrl() {
		return notifyUrl;
	}

	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}

	public String getGateway() {
		return gateway;
	}

	public void setGateway(String gateway) {
		this.gateway = gateway;
	}
	
}
