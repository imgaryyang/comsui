package com.suidifu.jpmorgan.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "distribute_stat")
public class DistributeStat {
	
	@Id
	@GeneratedValue
	private long id;
	
	private String paymentChannelUuid;
	
	private Long distributeTotalCount;
	
	private Date startTime;
	
	private Date endTime;
	
	private BigDecimal distributeTotalAmount;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getPaymentChannelUuid() {
		return paymentChannelUuid;
	}

	public void setPaymentChannelUuid(String paymentChannelUuid) {
		this.paymentChannelUuid = paymentChannelUuid;
	}

	
	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Long getDistributeTotalCount() {
		return distributeTotalCount;
	}

	public void setDistributeTotalCount(Long distributeTotalCount) {
		this.distributeTotalCount = distributeTotalCount;
	}

	public BigDecimal getDistributeTotalAmount() {
		return distributeTotalAmount;
	}

	public void setDistributeTotalAmount(BigDecimal distributeTotalAmount) {
		this.distributeTotalAmount = distributeTotalAmount;
	}


	
}
