package com.suidifu.bridgewater.api.model;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.annotation.JSONField;
import com.zufangbao.sun.api.model.deduct.DeductCommandRequestModel;
import com.zufangbao.sun.entity.directbank.business.ContractAccount;

public class RepaymentInfoModifyModel {
	public static final String MODIFY_REPAYMENT_INFORMATION  = "200003";
	
	private String fn;
	
	private String requestNo;
	
	private String uniqueId;
	
	private String contractNo;
	
	private String payerName;
	
	private String bankCode;
	
	private String bankAccount;
	
	private String bankName;
	
	private String bankProvince;
	
	private String bankCity;
	
	public RepaymentInfoModifyModel(DeductCommandRequestModel commandModel, ContractAccount vaildContractAccount) {
		this.fn = MODIFY_REPAYMENT_INFORMATION;
		this.requestNo = UUID.randomUUID().toString();
		this.uniqueId = commandModel.getUniqueId();
		this.contractNo = commandModel.getContractNo();
		if(vaildContractAccount != null){
			this.payerName = StringUtils.isNotBlank(commandModel.getPayerName()) ? commandModel.getPayerName() : vaildContractAccount.getPayerName();
			this.bankCode = StringUtils.isNotBlank(commandModel.getBankCode()) ? commandModel.getBankCode() : vaildContractAccount.getStandardBankCode();
			this.bankAccount = StringUtils.isNotBlank(commandModel.getPayAcNo()) ? commandModel.getPayAcNo() : vaildContractAccount.getPayAcNo();
			this.bankName = StringUtils.isNotBlank(commandModel.getBankName()) ? commandModel.getBankName() : vaildContractAccount.getBank();
			this.bankProvince = StringUtils.isNotBlank(commandModel.getProvinceCode()) ? commandModel.getProvinceCode() : vaildContractAccount.getProvinceCode();
			this.bankCity = StringUtils.isNotBlank(commandModel.getCityCode()) ? commandModel.getCityCode() : vaildContractAccount.getCityCode();
		}
	}
	

	@JSONField(serialize = false)
	public Map<String, String> getRepaymentInfosMap() {
		Map<String, String> repaymentInfosMap = new HashMap<String, String>();
		repaymentInfosMap.put("fn",this.fn);
		repaymentInfosMap.put("requestNo",this.requestNo);
		repaymentInfosMap.put("uniqueId",this.uniqueId);
		repaymentInfosMap.put("contractNo",this.contractNo);
		repaymentInfosMap.put("payerName",this.payerName);
		repaymentInfosMap.put("bankCode",this.bankCode);
		repaymentInfosMap.put("bankAccount",this.bankAccount);
		repaymentInfosMap.put("bankName",this.bankName);
		repaymentInfosMap.put("bankProvince",this.bankProvince);
		repaymentInfosMap.put("bankCity",this.bankCity);
		return repaymentInfosMap;
	}

	public RepaymentInfoModifyModel() {
		// TODO Auto-generated constructor stub
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

	public String getPayerName() {
		return payerName;
	}

	public void setPayerName(String payerName) {
		this.payerName = payerName;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getBankAccount() {
		return bankAccount;
	}

	public void setBankAccount(String bankAccount) {
		this.bankAccount = bankAccount;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
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


}
