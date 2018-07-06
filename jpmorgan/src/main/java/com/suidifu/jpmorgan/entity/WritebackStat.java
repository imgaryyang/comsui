package com.suidifu.jpmorgan.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "writeback_stat")
public class WritebackStat {
	
	@Id
	@GeneratedValue
	private Long id;
	
	private String paymentChannelUuid;
	
	private long successNum;
	
	private long failedNum;
	
	private Date startTime;
	
	private Date endTime;
	
	private  BigDecimal successAmount;
	
	private  BigDecimal failedAmount;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public long getSuccessNum() {
		return successNum;
	}

	public String getPaymentChannelUuid() {
		return paymentChannelUuid;
	}

	public void setPaymentChannelUuid(String paymentChannelUuid) {
		this.paymentChannelUuid = paymentChannelUuid;
	}

	public void setSuccessNum(long successNum) {
		this.successNum = successNum;
	}

	public long getFailedNum() {
		return failedNum;
	}

	public void setFailedNum(long failedNum) {
		this.failedNum = failedNum;
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
	
	public BigDecimal getSuccessAmount() {
		return successAmount;
	}

	public void setSuccessAmount(BigDecimal successAmount) {
		this.successAmount = successAmount;
	}

	public BigDecimal getFailedAmount() {
		return failedAmount;
	}

	public void setFailedAmount(BigDecimal failedAmount) {
		this.failedAmount = failedAmount;
	}
	
	public WritebackStat(String paymentChannelUuid) {
		this.paymentChannelUuid = paymentChannelUuid;
	}
	
	public WritebackStat() {
		
	}

}
