package com.zufangbao.earth.yunxin.api.model.logs;

import com.zufangbao.earth.yunxin.api.model.command.RepurchaseCommandModel;
import com.zufangbao.sun.utils.DateUtils;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * 回购申请提交及撤销Log
 */
@Entity
@Table(name = "t_interface_repurchase_command")
public class RepurchaseCommandLog {
	@Id
	@GeneratedValue
	private Long id;
	
	/**
	 * 请求编号
	 */
	private String requestNo;
	/**
	 * 批次号
	 */
	private String batchNo;
	/**
	 * 交易类型(0:提交，1:撤销)
	 */
	private Integer transactionType;
	/**
	 * 信托合同代码
	 */
	private String financialContractNo;
	/**
	 * 回购详情jsonList
	 */
	private String repurchaseDetail;
	/**
	 * 审核人
	 */
	private String reviewer;
	/**
	 * 审核时间
	 */
	private Date reviewTime;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 请求IP地址
	 */
	private String ip;
	/**
	 * 
	 */
	public RepurchaseCommandLog() {
		super();
	}
	
	public RepurchaseCommandLog(RepurchaseCommandModel model, String ip){
		super();
		this.requestNo = model.getRequestNo();
		this.batchNo = model.getBatchNo();
		this.transactionType = model.getTransactionType();
		this.financialContractNo = model.getFinancialContractNo();
		this.repurchaseDetail = model.getRepurchaseDetail();
		this.reviewer = model.getReviewer();
		this.reviewTime = model.getReviewTimeString() == null ? null : DateUtils.parseDate(model.getReviewTimeString(), "yyyy-MM-dd HH:mm:ss");
		this.createTime = new Date();
		this.ip = ip;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRequestNo() {
		return requestNo;
	}

	public void setRequestNo(String requestNo) {
		this.requestNo = requestNo;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public Integer getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(Integer transactionType) {
		this.transactionType = transactionType;
	}

	public String getFinancialContractNo() {
		return financialContractNo;
	}

	public void setFinancialContractNo(String financialContractNo) {
		this.financialContractNo = financialContractNo;
	}

	public String getRepurchaseDetail() {
		return repurchaseDetail;
	}

	public void setRepurchaseDetail(String repurchaseDetail) {
		this.repurchaseDetail = repurchaseDetail;
	}

	public String getReviewer() {
		return reviewer;
	}

	public void setReviewer(String reviewer) {
		this.reviewer = reviewer;
	}

	public Date getReviewTime() {
		return reviewTime;
	}

	public void setReviewTime(Date reviewTime) {
		this.reviewTime = reviewTime;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}
	
	
}
