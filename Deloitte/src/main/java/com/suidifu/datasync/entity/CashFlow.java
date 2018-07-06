package com.suidifu.datasync.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class CashFlow implements Serializable {
	private static final long serialVersionUID = -6655047909286930247L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private String id;
	private String cashFlowUuid;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCashFlowUuid() {
		return cashFlowUuid;
	}

	public void setCashFlowUuid(String cashFlowUuid) {
		this.cashFlowUuid = cashFlowUuid;
	}

}
