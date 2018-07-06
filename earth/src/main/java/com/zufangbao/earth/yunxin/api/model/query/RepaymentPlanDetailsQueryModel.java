package com.zufangbao.earth.yunxin.api.model.query;

import com.zufangbao.sun.utils.StringUtils;

public class RepaymentPlanDetailsQueryModel {

	//查询请求编号
	private String requestNo;
	//信托产品代码
	private String contractNo;
	//还款计划编号
	private String singleLoanContractNo;
	//商户还款计划编号
	private String outerRepaymentPlanNo;
	
	
	public String getRequestNo() {
		return requestNo;
	}
	public void setRequestNo(String requestNo) {
		this.requestNo = requestNo;
	}
	public String getContractNo() {
		return contractNo;
	}
	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}
	public String getSingleLoanContractNo() {
		return singleLoanContractNo;
	}
	public void setSingleLoanContractNo(String singleLoanContractNo) {
		this.singleLoanContractNo = singleLoanContractNo;
	}
	public String getOuterRepaymentPlanNo() {
		return outerRepaymentPlanNo;
	}
	public void setOuterRepaymentPlanNo(String outerRepaymentPlanNo) {
		this.outerRepaymentPlanNo = outerRepaymentPlanNo;
	}
	public boolean hasError() {
		return (StringUtils.isEmpty(requestNo) || StringUtils.isEmpty(contractNo) || (StringUtils.isEmpty(singleLoanContractNo) && StringUtils.isEmpty(outerRepaymentPlanNo)));
	}
}
