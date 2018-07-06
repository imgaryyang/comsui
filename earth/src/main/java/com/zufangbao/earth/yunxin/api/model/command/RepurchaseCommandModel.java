package com.zufangbao.earth.yunxin.api.model.command;

import com.alibaba.fastjson.annotation.JSONField;
import com.demo2do.core.utils.JsonUtils;
import com.zufangbao.sun.entity.repurchase.RepurchaseDetails;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import java.util.Collections;
import java.util.List;

/**
 * 回购指令模型
 * @author xwq
 *
 */
public class RepurchaseCommandModel {
	public static final int SUBMIT = 0;
	public static final int UNDO = 1;
	/**
	 * 请求编号（必填）
	 */
	@ApiParam(value = "请求编号", required = true)
	private String requestNo;
	/**
	 * 批次号（提交时必填且唯一；批量撤销时必填，单条撤销时批次号和回购详情二选一）
	 */
	@ApiParam(value = "批次号（提交时必填且唯一；批量撤销时必填，单条撤销时批次号和回购详情二选一）")
	private String batchNo;
	/**
	 * 交易类型(0:提交，1:撤销)（必填）
	 */
	@ApiParam(value = "交易类型(0:提交，1:撤销)", required = true)
	private Integer transactionType;
	/**
	 * 信托合同代码（必填）
	 */
	@ApiParam(value = "信托合同代码", required = true)
	private String financialContractNo;
	/**
	 * 回购详情jsonList（提交时必填；单条撤销时批次号和回购详情二选一）
	 */
	@ApiParam(value = "回购详情jsonList（提交时必填；单条撤销时批次号和回购详情二选一）")
	private String repurchaseDetail;
	/**
	 * 审核人（选填）
	 */
	@ApiParam(value = "审核人")
	private String reviewer;
	/**
	 * 审核时间（选填）
	 */
	@ApiParam(value = "审核时间")
	private String reviewTimeString;
	/**
	 * 校验失败信息
	 */
	@ApiModelProperty(hidden = true)
	private String checkFailedMsg;
	
	
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
	public String getReviewTimeString() {
		return reviewTimeString;
	}
	public void setReviewTimeString(String reviewTimeString) {
		this.reviewTimeString = reviewTimeString;
	}
	public String getCheckFailedMsg() {
		return checkFailedMsg;
	}
	public void setCheckFailedMsg(String checkFailedMsg) {
		this.checkFailedMsg = checkFailedMsg;
	}
	

	public RepurchaseCommandModel() {

	}
	
	@JSONField(serialize = false)
	public List<RepurchaseDetails> getRepurchaseDetailModel() {
		List<RepurchaseDetails> repurchaseDetailModels = JsonUtils.parseArray(this.repurchaseDetail, RepurchaseDetails.class);
		if(CollectionUtils.isEmpty(repurchaseDetailModels)) {
			return Collections.emptyList();
		}
		return repurchaseDetailModels;
	}
	
	@JSONField(serialize = false)
	public boolean submitParamsError() {
		if(StringUtils.isBlank(this.requestNo)){
			this.checkFailedMsg = "请求编号［requestNo］，不能为空！";
			return true;
		}
		if(this.transactionType == null || this.transactionType != SUBMIT){
			this.checkFailedMsg = "交易类型［transactionType］错误！";
			return true;
		}
//		if(StringUtils.isBlank(this.batchNo)){
//			this.checkFailedMsg = "批次号［batchNo］，不能为空！";
//			return true;
//		}
		if(StringUtils.isBlank(this.financialContractNo)){
			this.checkFailedMsg = "信托合同代码［financialContractNo］，不能为空！";
			return true;
		}
		return submitDetailHasError();
	}
	
	@JSONField(serialize = false)
	private boolean submitDetailHasError() {
		if (StringUtils.isBlank(this.repurchaseDetail)) {
			this.checkFailedMsg = "回购详情［repurchaseDetail］，不能为空！";
			return true;
		}
		List<RepurchaseDetails> repurchaseDetailModels = getRepurchaseDetailModel();
		if(CollectionUtils.isEmpty(repurchaseDetailModels)){
			this.checkFailedMsg = "回购详情［repurchaseDetail］格式异常！";
			return true;
		}
		if (StringUtils.isBlank(this.batchNo) && repurchaseDetailModels.size() != 1) {
			this.checkFailedMsg = "回购多条时，批次号［batchNo］不能为空！";
			return true;
		}
		for (RepurchaseDetails detail : repurchaseDetailModels) {
			if(StringUtils.isNotBlank(detail.getSubmitCheckFailedMsgDetail())){
				this.checkFailedMsg = detail.getSubmitCheckFailedMsgDetail();
				return true;
			}
		}
		return false;
	}
	
	@JSONField(serialize = false)
	public boolean undoParamsError() {
		if(StringUtils.isBlank(this.requestNo)){
			this.checkFailedMsg = "请求编号［requestNo］，不能为空！";
			return true;
		}
		if(this.transactionType == null || this.transactionType != UNDO){
			this.checkFailedMsg = "交易类型［transactionType］错误！";
			return true;
		}
		if(StringUtils.isBlank(this.financialContractNo)){
			this.checkFailedMsg = "信托合同代码［financialContractNo］，不能为空！";
			return true;
		}
		return undoDetailHasError();
	}
	
	@JSONField(serialize = false)
	private boolean undoDetailHasError() {
		
		if(StringUtils.isBlank(this.batchNo) == StringUtils.isBlank(this.repurchaseDetail)){
			this.checkFailedMsg = "请选填其中一种［batchNo，repurchaseDetail］！";
			return true;
		}
		
		if(StringUtils.isNotBlank(this.repurchaseDetail)){
			List<RepurchaseDetails> repurchaseDetailModels = getRepurchaseDetailModel();
			if(CollectionUtils.isEmpty(repurchaseDetailModels)){
				this.checkFailedMsg = "回购详情［repurchaseDetail］格式异常！";
				return true;
			}
			if(repurchaseDetailModels.size() > 1){
				this.checkFailedMsg = "批量撤销时批次号［batchNo］，不能为空！";
				return true;
			}
			for (RepurchaseDetails detail : repurchaseDetailModels) {
				if(StringUtils.isNotBlank(detail.getUndoCheckFailedMsgDetail())){
					this.checkFailedMsg = detail.getUndoCheckFailedMsgDetail();
					return true;
				}
			}
		}
		return false;
	}
	
}
