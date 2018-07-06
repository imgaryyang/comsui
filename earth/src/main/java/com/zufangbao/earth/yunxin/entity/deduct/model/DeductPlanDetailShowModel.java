package com.zufangbao.earth.yunxin.entity.deduct.model;


import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.yunxin.entity.api.deduct.DeductPlan;

/** 
* @author 作者 zhenghangbo
* @version 创建时间：Nov 2, 2016 7:32:27 PM 
* 类说明 
*/
public class DeductPlanDetailShowModel {
	
	
	private String payerName;
	
	private String deductPlanNo;
	
	private String paymentInstitution;
	
	private String  bankName;
	
	private String  paymentInterfaceNo;
	
	private String  paymentInstitutionFlowNo;
	
	private String occurTiem;
	
	private String paymentStatus;
	
	private String  paymentWay;
	
	private String occurAmount;
	
	private String remark;
	
	private String bindId;

	
	public DeductPlanDetailShowModel(){
		
	}
	public DeductPlanDetailShowModel(DeductPlan deductPlan){
		this.payerName = deductPlan.getCpBankAccountHolder();
		this.deductPlanNo = deductPlan.getDeductPlanUuid();
		if(deductPlan.getPaymentGateway() != null){
		this.paymentInstitution = deductPlan.getPaymentGateway().getChineseMessage();
		this.paymentWay =deductPlan.getPaymentGateway().getChineseMessage();
		}
		this.bankName = deductPlan.getCpBankName();
		this.paymentInterfaceNo = deductPlan.getPaymentChannelUuid();
		this.paymentInstitutionFlowNo = deductPlan.getTradeUuid();
		this.occurTiem = DateUtils.format(deductPlan.getCreateTime(),"yyyy-MM-dd HH:mm:ss");

		this.paymentStatus = deductPlan.getExecutionStatus().getChineseMessage();
		this.occurAmount = deductPlan.getPlannedTotalAmount().toString();
		this.remark = deductPlan.getExecutionRemark();
		
		this.bindId = "";
	}
	
	public String getPayerName() {
		return payerName;
	}

	public void setPayerName(String payerName) {
		this.payerName = payerName;
	}

	public String getDeductPlanNo() {
		return deductPlanNo;
	}

	public void setDeductPlanNo(String deductPlanNo) {
		this.deductPlanNo = deductPlanNo;
	}

	public String getPaymentInstitution() {
		return paymentInstitution;
	}

	public void setPaymentInstitution(String paymentInstitution) {
		this.paymentInstitution = paymentInstitution;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getPaymentInterfaceNo() {
		return paymentInterfaceNo;
	}

	public void setPaymentInterfaceNo(String paymentInterfaceNo) {
		this.paymentInterfaceNo = paymentInterfaceNo;
	}

	public String getPaymentInstitutionFlowNo() {
		return paymentInstitutionFlowNo;
	}

	public void setPaymentInstitutionFlowNo(String paymentInstitutionFlowNo) {
		this.paymentInstitutionFlowNo = paymentInstitutionFlowNo;
	}

	public String getOccurTiem() {
		return occurTiem;
	}

	public void setOccurTiem(String occurTiem) {
		this.occurTiem = occurTiem;
	}

	public String getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public String getPaymentWay() {
		return paymentWay;
	}

	public void setPaymentWay(String paymentWay) {
		this.paymentWay = paymentWay;
	}

	public String getOccurAmount() {
		return occurAmount;
	}

	public void setOccurAmount(String occurAmount) {
		this.occurAmount = occurAmount;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getBindId() {
		return bindId;
	}

	public void setBindId(String bindId) {
		this.bindId = bindId;
	}
	
	

}
