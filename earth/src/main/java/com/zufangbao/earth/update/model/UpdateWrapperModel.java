package com.zufangbao.earth.update.model;



import io.swagger.annotations.ApiParam;

public class UpdateWrapperModel {

	/**
	 * 模板ID
	 */
	@ApiParam(value = "模板ID", required = true)
	private String updateId;
	
	 /**
     *  放款流水号
     */
    @ApiParam(value = "放款流水号"  )
    private String bankSequenceNo;
    /**
     *  多个合同id时,用逗号分割
     */
    @ApiParam(value = "多个合同id时,用逗号分割" )
    private String contractId;
    /**
     *  信托合同uuid
     */
    @ApiParam(value = "信托合同uuid" )
    private String financialContractUuid;
    /**
     *  打款流水
     */
    @ApiParam(value = "打款流水" )
    private String secondNo;
    /**
     *  备注
     */
    @ApiParam(value = "备注" )
    private String comment;
    /**
     *  金额
     */
    @ApiParam(value = "金额" )
    private String amount;
    
   
    @ApiParam(value = "更新前的回购金额" )
    private String repuchaseAmountBefore; 
    @ApiParam(value = "更新后的回购金额" )
    private String repuchaseAmountAfter; 
    @ApiParam(value = "回购单uuid" )
    private String repurchaseUuid;
    @ApiParam(value = "客户部分还款的还款编号" )
    private String assetNo;   
    @ApiParam(value = "结算单金额" )
    private String totalOrderRent; 
    @ApiParam(value = "客户部分还款的金额" )
    private String bankDebitAmount; 
    @ApiParam(value = "还款明细的key(ExtraChargeSpec)" )
    private String extraChargeSpecKey; 
    @ApiParam(value = "部分还款的还款时间" )
    private String payoutTime; 
    
    @ApiParam(value = "回购单号" )
    private String repurchaseDocUuid; 
    @ApiParam(value = "回购本金" )
    private String repurchasePrincipal;

    @ApiParam(value = "回购利息" )
    private String repurchaseInterest;
    @ApiParam(value = "回购罚息" )
    private String repurchasePenalty;
    @ApiParam(value = "回购其他费用" )
    private String repurchaseOtherCharges;

	@ApiParam(value = "还款编号" )
	private String singleLoanContractNo;

	@ApiParam(value = "利息,贷款服务费,技术服务费,其他费用" )
	private String valueAndFees;

	@ApiParam(value = "信托合同代码" )
	private String contractNo;

	@ApiParam(value = "" )
	private String offlineBillNo;

	@ApiParam(value = "凭证编号" )
	private String voucherNo;

	@ApiParam(value = "扣款计划uuid" )
	private String deductPlanUuid;

	public String getDeductPlanUuid() {
		return deductPlanUuid;
	}

	public void setDeductPlanUuid(String deductPlanUuid) {
		this.deductPlanUuid = deductPlanUuid;
	}

	public String getVoucherNo() {
		return voucherNo;
	}

	public void setVoucherNo(String voucherNo) {
		this.voucherNo = voucherNo;
	}

	public String getContractNo() {
		return contractNo;
	}

	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}

	public String getOfflineBillNo() {
		return offlineBillNo;
	}

	public void setOfflineBillNo(String offlineBillNo) {
		this.offlineBillNo = offlineBillNo;
	}

	public String getSingleLoanContractNo() {
		return singleLoanContractNo;
	}

	public void setSingleLoanContractNo(String singleLoanContractNo) {
		this.singleLoanContractNo = singleLoanContractNo;
	}

	public String getValueAndFees() {
		return valueAndFees;
	}

	public void setValueAndFees(String valueAndFees) {
		this.valueAndFees = valueAndFees;
	}

	public String getUpdateId() {
		return updateId;
	}

	public void setUpdateId(String updateId) {
		this.updateId = updateId;
	}

	public UpdateWrapperModel(){
    	super();
    }
    
	public String getBankSequenceNo() {
		return bankSequenceNo;
	}
	public void setBankSequenceNo(String bankSequenceNo) {
		this.bankSequenceNo = bankSequenceNo;
	}
	public String getContractId() {
		return contractId;
	}
	public void setContractId(String contractId) {
		this.contractId = contractId;
	}
	
	public String getFinancialContractUuid() {
		return financialContractUuid;
	}

	public void setFinancialContractUuid(String financialContractUuid) {
		this.financialContractUuid = financialContractUuid;
	}

	public String getSecondNo() {
		return secondNo;
	}
	public void setSecondNo(String secondNo) {
		this.secondNo = secondNo;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getRepuchaseAmountBefore() {
		return repuchaseAmountBefore;
	}

	public void setRepuchaseAmountBefore(String repuchaseAmountBefore) {
		this.repuchaseAmountBefore = repuchaseAmountBefore;
	}

	public String getRepuchaseAmountAfter() {
		return repuchaseAmountAfter;
	}

	public void setRepuchaseAmountAfter(String repuchaseAmountAfter) {
		this.repuchaseAmountAfter = repuchaseAmountAfter;
	}

	public String getRepurchaseUuid() {
		return repurchaseUuid;
	}

	public void setRepurchaseUuid(String repurchaseUuid) {
		this.repurchaseUuid = repurchaseUuid;
	}

	public String getAssetNo() {
		return assetNo;
	}

	public void setAssetNo(String assetNo) {
		this.assetNo = assetNo;
	}

	public String getTotalOrderRent() {
		return totalOrderRent;
	}

	public void setTotalOrderRent(String totalOrderRent) {
		this.totalOrderRent = totalOrderRent;
	}

	public String getBankDebitAmount() {
		return bankDebitAmount;
	}

	public void setBankDebitAmount(String bankDebitAmount) {
		this.bankDebitAmount = bankDebitAmount;
	}

	

	public String getExtraChargeSpecKey() {
		return extraChargeSpecKey;
	}

	public void setExtraChargeSpecKey(String extraChargeSpecKey) {
		this.extraChargeSpecKey = extraChargeSpecKey;
	}

	public String getPayoutTime() {
		return payoutTime;
	}

	public void setPayoutTime(String payoutTime) {
		this.payoutTime = payoutTime;
	}

	public String getRepurchaseDocUuid() {
		return repurchaseDocUuid;
	}

	public void setRepurchaseDocUuid(String repurchaseDocUuid) {
		this.repurchaseDocUuid = repurchaseDocUuid;
	}

	public String getRepurchasePrincipal() {
		return repurchasePrincipal;
	}

	public void setRepurchasePrincipal(String repurchasePrincipal) {
		this.repurchasePrincipal = repurchasePrincipal;
	}

	public String getRepurchaseInterest() {
		return repurchaseInterest;
	}

	public void setRepurchaseInterest(String repurchaseInterest) {
		this.repurchaseInterest = repurchaseInterest;
	}

	public String getRepurchasePenalty() {
		return repurchasePenalty;
	}

	public void setRepurchasePenalty(String repurchasePenalty) {
		this.repurchasePenalty = repurchasePenalty;
	}

	public String getRepurchaseOtherCharges() {
		return repurchaseOtherCharges;
	}

	public void setRepurchaseOtherCharges(String repurchaseOtherCharges) {
		this.repurchaseOtherCharges = repurchaseOtherCharges;
	}

	
}
