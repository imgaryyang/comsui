package com.zufangbao.earth.yunxin.api.model;

import com.demo2do.core.utils.DateUtils;
import com.zufangbao.sun.entity.contract.Contract;
import com.zufangbao.sun.yunxin.entity.AssetSet;
import com.zufangbao.sun.yunxin.entity.OfflineBill;
import com.zufangbao.sun.yunxin.entity.api.deduct.DeductApplication;
import com.zufangbao.sun.yunxin.entity.api.deduct.DeductPlan;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class RepaymentListDetail {

	private String deductRequestNo;

	private String deductDate;

	private String contractNo;
	
	private String uniqueId="";

	private String assetSetNo;

	private String flowNo;

	private String completeDate;

	private String deductAmount;

	private String deductAccountName;

	private String deductAccountNo;

	private String deductAccountBank;

	private String results;

	public RepaymentListDetail() {

	}

	public RepaymentListDetail(OfflineBill offlineBill,
			Set<AssetSet> assetSets, Set<Contract> contracts) {
		this.deductRequestNo = offlineBill.getOfflineBillNo();
		if (assetSets != null) {
			this.assetSetNo = assetSets.stream().map(fc -> fc.getSingleLoanContractNo())
					.collect(Collectors.joining(","));
		}
		if (contracts != null && !contracts.isEmpty()) {
			this.contractNo = contracts.stream()
					.map(fc -> fc.getContractNo())
					.collect(Collectors.joining(","));
			this.uniqueId = contracts.stream()
					.map(fc -> fc.getUniqueId())
					.collect(Collectors.joining(","));
		}
		
		this.flowNo = offlineBill.getSerialNo();
		if(offlineBill.getTradeTime() !=null){
		this.completeDate = DateUtils.format(offlineBill.getTradeTime());
		this.deductDate = DateUtils.format(offlineBill.getTradeTime());
		}
		this.deductAmount = offlineBill.getAmount().toString();
		this.deductAccountName = offlineBill.getPayerAccountName();
		this.deductAccountNo = offlineBill.getPayerAccountNo();
		this.deductAccountBank = offlineBill.getBankShowName();
		this.results = "扣款成功";

	}

	public RepaymentListDetail(DeductPlan deductPlan,DeductApplication deductApplication) {
		this.deductRequestNo = deductPlan.getDeductApplicationUuid();
		this.deductDate = DateUtils.format(deductPlan.getCreateTime());
		List<String> repaymentCodeList = deductApplication.getRepaymentPlanCodeListJsonString();
		if(repaymentCodeList!=null){
			this.assetSetNo = deductApplication.getRepaymentPlanCodeListJsonString().stream().collect(Collectors.joining(","));
		}
		this.contractNo = deductApplication.getContractNo();
		this.uniqueId   = deductApplication.getContractUniqueId();
		this.flowNo 	= deductPlan.getTransactionSerialNo();
		if(deductPlan.isEndDeductPlan()){
			this.completeDate = DateUtils.format(deductPlan.getCompleteTime());
		}
		this.deductAmount = deductPlan.getPlannedTotalAmount().toString();
		this.deductAccountName = deductPlan.getCpBankAccountHolder();
		this.deductAccountNo = deductPlan.getCpBankCardNo();
		this.deductAccountBank = deductPlan.getCpBankName();
		this.results = deductPlan.getExecutionStatus().getChineseMessage();
	}

	public String getDeductRequestNo() {
		return deductRequestNo;
	}

	public void setDeductRequestNo(String deductRequestNo) {
		this.deductRequestNo = deductRequestNo;
	}

	public String getDeductDate() {
		return deductDate;
	}

	public void setDeductDate(String deductDate) {
		this.deductDate = deductDate;
	}

	public String getContractNo() {
		return contractNo;
	}

	public void setContractNo(String loanContractNo) {
		this.contractNo = loanContractNo;
	}

	public String getAssetSetNo() {
		return assetSetNo;
	}

	public void setAssetSetNo(String assetSetNo) {
		this.assetSetNo = assetSetNo;
	}

	public String getFlowNo() {
		return flowNo;
	}

	public void setFlowNo(String flowNo) {
		this.flowNo = flowNo;
	}

	public String getCompleteDate() {
		return completeDate;
	}

	public void setCompleteDate(String completeDate) {
		this.completeDate = completeDate;
	}

	public String getDeductAmount() {
		return deductAmount;
	}

	public void setDeductAmount(String deductAmount) {
		this.deductAmount = deductAmount;
	}

	public String getDeductAccountName() {
		return deductAccountName;
	}

	public void setDeductAccountName(String deductAccountName) {
		this.deductAccountName = deductAccountName;
	}

	public String getDeductAccountNo() {
		return deductAccountNo;
	}

	public void setDeductAccountNo(String deductAccountNo) {
		this.deductAccountNo = deductAccountNo;
	}

	public String getDeductAccountBank() {
		return deductAccountBank;
	}

	public void setDeductAccountBank(String deductAccountBank) {
		this.deductAccountBank = deductAccountBank;
	}

	public String getResults() {
		return results;
	}

	public void setResults(String results) {
		this.results = results;
	}
	
	public String getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}
}
