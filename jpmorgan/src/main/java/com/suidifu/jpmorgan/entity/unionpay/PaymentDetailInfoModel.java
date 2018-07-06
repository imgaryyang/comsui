package com.suidifu.jpmorgan.entity.unionpay;

import java.math.BigDecimal;

public class PaymentDetailInfoModel {
	
	private String sn; // 记录序号

	private String bankCode; // 银行代码

	private String accountNo; // 银行卡号

	private String accountName; // 持卡人姓名
	
	private String province; //开户行所在省
	
	private String city; //开户行所在市

	private BigDecimal amount; // 金额

	private String remark; // 备注
	
	private String idType; // 证件类型
	
	private String idNum; // 证件编号

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getIdType() {
		return idType;
	}

	public void setIdType(String idType) {
		this.idType = idType;
	}

	public String getIdNum() {
		return idNum;
	}

	public void setIdNum(String idNum) {
		this.idNum = idNum;
	}

	public PaymentDetailInfoModel() {
		super();
	}
	
}
