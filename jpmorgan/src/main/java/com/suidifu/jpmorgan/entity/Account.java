package com.suidifu.jpmorgan.entity;

import java.util.Collections;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.demo2do.core.utils.JsonUtils;
import com.demo2do.core.utils.StringUtils;

/**
 * 
 * @author zjm
 *
 */
@Entity
@Table(name = "account")
public class Account {

	@Id
	@GeneratedValue
	private Long id;

	private String uuid;

	private String accountNo;

	private String accountName;

	private String alias;

	private String bankUuid;

	@Column(columnDefinition = "text")
	private String attr;

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

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getBankUuid() {
		return bankUuid;
	}

	public void setBankUuid(String bankUuid) {
		this.bankUuid = bankUuid;
	}

	public Map<String, Object> getAttr() {
		if (StringUtils.isEmpty(this.attr)) {
			return Collections.emptyMap();
		}
		return JsonUtils.parse(this.attr);
	}

	public void setAttr(String attr) {
		this.attr = attr;
	}

}
