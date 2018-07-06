package com.suidifu.bridgewater.api.model;

import com.zufangbao.sun.api.model.deduct.DeductInfo;
import com.zufangbao.sun.api.model.deduct.RepaymentResInfo;

import java.util.List;

public class DeductQueryResult {
	
	 private  String  queryRequestNo;
	 
	 private  String  contractUniqueId;
	 
	 private  String  contractNo;
	 
	 private  List<DeductInfo> deductInfos;
	 
	 private   List<RepaymentResInfo> repaymentDetails;


	public DeductQueryResult(String requestNo, String uniqueId,String  contractNo2,
			List<DeductInfo> deductInfos2) {
		this.queryRequestNo = requestNo;
		this.contractUniqueId = uniqueId;
		this.deductInfos = deductInfos2;
		this.contractNo  = contractNo2;
	}

	public String getQueryRequestNo() {
		return queryRequestNo;
	}

	public void setQueryRequestNo(String queryRequestNo) {
		this.queryRequestNo = queryRequestNo;
	}


	public List<RepaymentResInfo> getRepaymentDetails() {
		return repaymentDetails;
	}

	public void setRepaymentDetails(List<RepaymentResInfo> repaymentDetails) {
		this.repaymentDetails = repaymentDetails;
	}

	 public String getContractUniqueId() {
		return contractUniqueId;
	}

	public void setContractUniqueId(String contractUniqueId) {
		this.contractUniqueId = contractUniqueId;
	}

	public List<DeductInfo> getDeductInfos() {
		return deductInfos;
	}

	public void setDeductInfos(List<DeductInfo> deductInfos) {
		this.deductInfos = deductInfos;
	}

	public String getContractNo() {
		return contractNo;
	}

	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}
	 
	
}