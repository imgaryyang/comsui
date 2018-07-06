package com.suidifu.datasync.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Remittance implements Serializable {
	private static final long serialVersionUID = 12789733004645260L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private String id;
	private String remittanceApplicationUuid;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRemittanceApplicationUuid() {
		return remittanceApplicationUuid;
	}

	public void setRemittanceApplicationUuid(String remittanceApplicationUuid) {
		this.remittanceApplicationUuid = remittanceApplicationUuid;
	}

}
