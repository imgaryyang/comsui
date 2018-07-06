package com.suidifu.bridgewater.api.model;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import com.alibaba.fastjson.JSONObject;
import com.demo2do.core.utils.DateUtils;
import com.demo2do.core.utils.StringUtils;
import com.zufangbao.sun.api.model.deduct.DeductApplicationExecutionStatus;
import com.zufangbao.sun.yunxin.entity.api.deduct.DeductApplication;


public class BatchDeductStatusQueryResult {
	
    private	String deductId;
    
    private	List<String> repaymentPlanCodeList;
    
    private List<String> repayScheduleNoList;
    
    private	BigDecimal deductAmount;
    
    private	String deductAcceptTime;
    
    private	String  paymentFlowNo;
    
    private	BigDecimal receiveAmount;
    
    private	int executionStatus; 
    
    private String businessResultMessage;
    
    private String deductSuccessTime;
    
    public BatchDeductStatusQueryResult(){}
    
	public BatchDeductStatusQueryResult(DeductApplication deductApplication,String paymentFlowNo,String repaymentPlanCode) {
		this.deductId = deductApplication.getDeductId();
		this.deductAmount  = deductApplication.getPlannedDeductTotalAmount();
		this.deductAcceptTime = DateUtils.format(deductApplication.getCreateTime(),"yyyy-MM-dd HH:mm:ss");
		this.paymentFlowNo = paymentFlowNo;
		this.receiveAmount = deductApplication.getPlannedDeductTotalAmount();
		this.executionStatus = deductApplication.getExecutionStatus().getOrdinal();
		this.repaymentPlanCodeList =  Arrays.asList(repaymentPlanCode);
		this.businessResultMessage = deductApplication.getExecutionRemark();
		if(deductApplication.getExecutionStatus() == DeductApplicationExecutionStatus.SUCCESS){
			this.deductSuccessTime = DateUtils.format(deductApplication.getCompleteTime(),"yyyy-MM-dd HH:mm:ss");
		}else{
			this.deductSuccessTime = "";
		}
	}
	public BatchDeductStatusQueryResult(DeductApplication deductApplication,String paymentFlowNo) {
		this.deductId = deductApplication.getDeductId();
		this.deductAmount  = deductApplication.getPlannedDeductTotalAmount();
		this.deductAcceptTime = DateUtils.format(deductApplication.getCreateTime(),"yyyy-MM-dd HH:mm:ss");
		this.paymentFlowNo = paymentFlowNo;
		this.receiveAmount = deductApplication.getPlannedDeductTotalAmount();
		this.executionStatus = deductApplication.getExecutionStatus().getOrdinal();
		if(!StringUtils.isEmpty(deductApplication.getRepaymentPlanCodeList()))
		this.repaymentPlanCodeList =  JSONObject.parseArray(deductApplication.getRepaymentPlanCodeList(),String.class);
		this.businessResultMessage = deductApplication.getExecutionRemark();
		if(deductApplication.getExecutionStatus() == DeductApplicationExecutionStatus.SUCCESS){
			this.deductSuccessTime = DateUtils.format(deductApplication.getCompleteTime(),"yyyy-MM-dd HH:mm:ss");
		}else{
			this.deductSuccessTime = "";
		}
	}
   

	public List<String> getRepaymentPlanCodeList() {
		return repaymentPlanCodeList;
	}

	public void setRepaymentPlanCodeList(List<String> repaymentPlanCodeList) {
		this.repaymentPlanCodeList = repaymentPlanCodeList;
	}
	
	public List<String> getRepayScheduleNoList() {
		return repayScheduleNoList;
	}

	public void setRepayScheduleNoList(List<String> repayScheduleNoList) {
		this.repayScheduleNoList = repayScheduleNoList;
	}

	public String getDeductId() {
		return deductId;
	}

	public void setDeductId(String deductId) {
		this.deductId = deductId;
	}

	public BigDecimal getDeductAmount() {
		return deductAmount;
	}

	public void setDeductAmount(BigDecimal deductAmount) {
		this.deductAmount = deductAmount;
	}

	public String getDeductAcceptTime() {
		return deductAcceptTime;
	}

	public void setDeductAcceptTime(String deductAcceptTime) {
		this.deductAcceptTime = deductAcceptTime;
	}

	public String getPaymentFlowNo() {
		return paymentFlowNo;
	}

	public void setPaymentFlowNo(String paymentFlowNo) {
		this.paymentFlowNo = paymentFlowNo;
	}

	public BigDecimal getReceiveAmount() {
		return receiveAmount;
	}

	public void setReceiveAmount(BigDecimal receiveAmount) {
		this.receiveAmount = receiveAmount;
	}

	public int getExecutionStatus() {
		return executionStatus;
	}

	public void setExecutionStatus(int deductApplicationExecutionStatus) {
		this.executionStatus = deductApplicationExecutionStatus;
	}

	public String getBusinessResultMessage() {
		return businessResultMessage;
	}

	public void setBusinessResultMessage(String businessResultMessage) {
		this.businessResultMessage = businessResultMessage;
	}

	public String getDeductSuccessTime() {
		return deductSuccessTime;
	}

	public void setDeductSuccessTime(String deductSuccessTime) {
		this.deductSuccessTime = deductSuccessTime;
	}   
	
	
}
