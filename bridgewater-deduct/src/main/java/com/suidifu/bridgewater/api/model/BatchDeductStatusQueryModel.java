package com.suidifu.bridgewater.api.model;

import java.util.ArrayList;
import java.util.List;
import com.alibaba.fastjson.annotation.JSONField;
import com.zufangbao.sun.utils.JsonUtils;
import com.zufangbao.sun.utils.StringUtils;


public class BatchDeductStatusQueryModel {
	private static final int MAX_REQUEST_NUM = 5000;

	/**
	 * 请求唯一编号
	 */
	
	private String requestNo;
	
	private String deductIdList;
	/**
	 * 还款计划编号集
	 */
	private String repaymentPlanCodeList;

	/**
	 * 信托产品代码
	 */
	private String financialProductCode;

	/**
	 * 校验失败信息
	 */
	private String checkFailedMsg;
	
	public String getRequestNo() {
		return requestNo;
	}
	public void setRequestNo(String requestNo) {
		this.requestNo = requestNo;
	}

	public String getFinancialProductCode() {
		return financialProductCode;
	}

	public void setFinancialProductCode(String financialProductCode) {
		this.financialProductCode = financialProductCode;
	}

	public String getCheckFailedMsg() {
		return checkFailedMsg;
	}
	public void setCheckFailedMsg(String checkFailedMsg) {
		this.checkFailedMsg = checkFailedMsg;
	}
	

	public boolean isValid() {
		if(StringUtils.isEmpty(this.requestNo)) {
			this.checkFailedMsg = "请求唯一标识［requestNo］，不能为空！";
			return false;
		}
		if(!(StringUtils.isEmpty(getDeductIdList())^StringUtils.isEmpty(getRepaymentPlanCodeList()))){
			this.checkFailedMsg = "请选填其中一种编号［deductIdList,repaymentPlanCodeList］！";
			return false;
		}
		if(!StringUtils.isEmpty(getDeductIdList())){
			if(getDeductIdListJson().size()>MAX_REQUEST_NUM){
				this.checkFailedMsg = "deudctIds的数量必须小于等于"+MAX_REQUEST_NUM+"! ";
				return false;
			}
		}
		if(!StringUtils.isEmpty(getRepaymentPlanCodeList())){
			if(getRepaymentPlanCodeListJson().size()>MAX_REQUEST_NUM){
				this.checkFailedMsg = "repaymentPlanCodes的数量必须小于等于"+MAX_REQUEST_NUM+"! ";
				return false;
			}
		}
		return true;
	}
	public String getDeductIdList() {
		return this.deductIdList;
	}
	public void setDeductIdList(String deductIdList) {
		this.deductIdList = deductIdList;
	}
	public void setRepaymentPlanCodeList(String repaymentPlanCodeList) {
		this.repaymentPlanCodeList = repaymentPlanCodeList;
	}
	public String getRepaymentPlanCodeList() {
		return this.repaymentPlanCodeList;
	}
	
	
	@JSONField(serialize = false)
	public List<String> getRepaymentPlanCodeListJson() {
		if(StringUtils.isEmpty(this.repaymentPlanCodeList)) {
			return new ArrayList<String>();
		}
		return JsonUtils.parseArray(this.repaymentPlanCodeList, String.class);
	}

	@JSONField(serialize = false)
	public List<String> getDeductIdListJson(){
		if(StringUtils.isEmpty(this.deductIdList)){
			return new ArrayList<String>();
		}
		return JsonUtils.parseArray(this.deductIdList, String.class);
	}

	
}
