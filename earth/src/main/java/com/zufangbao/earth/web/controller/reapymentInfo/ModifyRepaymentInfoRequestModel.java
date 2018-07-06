package com.zufangbao.earth.web.controller.reapymentInfo;

import org.apache.commons.lang.StringUtils;

/** 
* @author 作者 zhenghangbo
* @version 创建时间：Oct 2, 2016 3:53:25 PM 
* 类说明 
*/
public class ModifyRepaymentInfoRequestModel {
	
	private String payerName;
	
	private String bankAccount;
	
	private String bankCode;
	
	private String provinceCode;
	
	private String cityCode;
	
	private Long contractId;
	
	private String idCardNum;
	
	private String mobile;

	
	public String getPayerName() {
		return payerName;
	}

	public void setPayerName(String payerName) {
		this.payerName = payerName;
	}

	public String getBankAccount() {
		return bankAccount.trim();
	}
	
	public void setBankAccount(String bankAccount) {
		this.bankAccount = bankAccount.trim();
	}
	
	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getProvinceCode() {
		return provinceCode;
	}

	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	
	public boolean checkData(){
		if(StringUtils.isBlank(payerName) || StringUtils.isBlank(bankCode) || StringUtils.isBlank(provinceCode)
				|| StringUtils.isBlank(cityCode) || StringUtils.isBlank(bankAccount) ||  contractId ==null){
			return false;
		}
		return true;
	}

	public Long getContractId() {
		return contractId;
	}

	public void setContractId(Long contractId) {
		this.contractId = contractId;
	}

	public String getIdCardNum() {
		return idCardNum;
	}

	public void setIdCardNum(String idCardNum) {
		this.idCardNum = idCardNum;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
	
	
}
