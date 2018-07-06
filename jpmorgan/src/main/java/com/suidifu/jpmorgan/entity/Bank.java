package com.suidifu.jpmorgan.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "bank")
public class Bank {

	@Id
	@GeneratedValue
	private Long id;

	private String uuid;

	private String stdBankCode;

	private String bankFullName;

	private String headOfficeName;

	private String province;

	private String city;

	public Bank() {
		super();
	}

	public Bank(String uuid, String stdBankCode, String bankFullName,
			String headOfficeName, String province, String city) {
		super();
		this.uuid = uuid;
		this.stdBankCode = stdBankCode;
		this.bankFullName = bankFullName;
		this.headOfficeName = headOfficeName;
		this.province = province;
		this.city = city;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getStdBankCode() {
		return stdBankCode;
	}

	public void setStdBankCode(String stdBankCode) {
		this.stdBankCode = stdBankCode;
	}

	public String getBankFullName() {
		return bankFullName;
	}

	public void setBankFullName(String bankFullName) {
		this.bankFullName = bankFullName;
	}

	public String getHeadOfficeName() {
		return headOfficeName;
	}

	public void setHeadOfficeName(String headOfficeName) {
		this.headOfficeName = headOfficeName;
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

}
