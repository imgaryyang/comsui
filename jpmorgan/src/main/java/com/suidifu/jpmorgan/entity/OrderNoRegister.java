package com.suidifu.jpmorgan.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "order_no_register")
public class OrderNoRegister {

	@Id
	@GeneratedValue
	private Long id;

	private String outlierTransactionUuid;

	// private RegisterStatus registerStatus;

	private Date createTime;

	// private Date lastModifyTime;

	private Long longFieldOne;

	private String stringFieldOne;

	private Date dateFieldOne;

	public OrderNoRegister() {
		super();
	}

	public OrderNoRegister(String outlierTransactionUuid,
			Date createTime) {
		super();
		this.outlierTransactionUuid = outlierTransactionUuid;
		this.createTime = createTime;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOutlierTransactionUuid() {
		return outlierTransactionUuid;
	}

	public void setOutlierTransactionUuid(String outlierTransactionUuid) {
		this.outlierTransactionUuid = outlierTransactionUuid;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

}
