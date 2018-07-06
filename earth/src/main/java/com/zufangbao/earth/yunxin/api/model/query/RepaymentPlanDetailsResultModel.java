package com.zufangbao.earth.yunxin.api.model.query;

import com.demo2do.core.utils.DateUtils;
import com.zufangbao.earth.yunxin.api.model.GuaranteeOrderShowModel;
import com.zufangbao.earth.yunxin.api.model.PaymentRecordShowModel;
import com.zufangbao.earth.yunxin.api.model.RepurchaseShowModel;
import com.zufangbao.earth.yunxin.api.model.SettlementOrderShowModel;
import com.zufangbao.sun.entity.contract.Contract;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.entity.order.Order;
import com.zufangbao.sun.entity.repurchase.RepurchaseDoc;
import com.zufangbao.sun.service.RepaymentPlanSpec;
import com.zufangbao.sun.service.RepaymentPlanSpec.RepaymentTypeMsg;
import com.zufangbao.sun.yunxin.entity.*;
import com.zufangbao.sun.yunxin.entity.model.RepaymentChargesDetail;
import org.apache.commons.collections.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class RepaymentPlanDetailsResultModel {
	private final static int NORMAL = 0;
	private final static int PREREPAYMENT = 1;
	private final static int MODIFYPRE = 2;
	private final static int LOCK = 3;
	private final static int OFF = 4;
	
	//还款计划编号
	private String singleLoanContractNo;
	//商户还款计划编号
	private String outerRepaymentPlanNo;
	//贷款合同编号
	private String contractNo;
	//总期数
	private Integer periods;
	//当前期数
	private Integer currentPeriod;
	//计划状态
	private Integer planStatus;
	//还款状态
	private Integer paymentStatus;
	//还款类型
	private Integer repaymentType;
	//逾期状态
	private Integer overdueStatus;
	//差异天数
	private Integer overDueDays;
	//逾期天数
	private Integer auditOverdueDays;
	//回购状态
	private Integer repurchaseStatus;
	//担保清算状态
	private Integer guaranteeAndSettlementOrderStatus;
	//计划还款日期
	private String assetRecycleDate;
	//实际还款日期
	private String actualRecycleDate;
	//收款机构帐户号
	private String accountNo;
	//计划还款本金
	private BigDecimal loanAssetPrincipal;
	//计划还款利息
	private BigDecimal loanAssetInterest;
	//贷款服务费
	private BigDecimal loanServiceFee;
	//技术维护费
	private BigDecimal loanTechFee;
	//其他费用
	private BigDecimal loanOtherFee;
	//逾期罚息
	private BigDecimal overdueFeePenalty;
	//逾期违约金
	private BigDecimal overdueFeeObligation;
	//逾期服务费
	private BigDecimal overdueFeeService;
	//逾期其他费用
	private BigDecimal overdueFeeOther;
	//当前应收累计金额
	private BigDecimal planTotalFee;
	//当前实收累计金额
	private BigDecimal paidUpTotalFee;
	//查询时间
	private String queryTime;
	
	//还款记录详情
	private List<PaymentRecordShowModel> paymentRecords = new ArrayList<PaymentRecordShowModel>();
	//回购单详情
	private List<RepurchaseShowModel> repurchaseDocs = new ArrayList<RepurchaseShowModel>();
	//担保单详情
	private List<GuaranteeOrderShowModel> guaranteeOrders = new ArrayList<GuaranteeOrderShowModel>();
	//担保清算单详情
	private List<SettlementOrderShowModel> settelementOrders = new ArrayList<SettlementOrderShowModel>();
	
	public RepaymentPlanDetailsResultModel(Date queryTime, AssetSet assetSet, Contract contract, FinancialContract financialContract, RepaymentChargesDetail chargesDetail, RepaymentChargesDetail paidChargesDetail, List<PaymentRecordShowModel> paymentRecords, List<RepurchaseDoc> repurchaseDocs, List<Order> guaranteeOrders,List<SettlementOrder> settlementOrders) {
		this.singleLoanContractNo = assetSet.getSingleLoanContractNo();
		this.outerRepaymentPlanNo = assetSet.getOuterRepaymentPlanNo();
		this.contractNo = contract.getContractNo();
		this.periods = contract.getPeriods();
		this.currentPeriod = assetSet.getCurrentPeriod();
		this.planStatus = getPlanStatusOrdinal(assetSet);
		this.paymentStatus = RepaymentExecutionStateMapUtil.getRepaymentExecutionStateEnum(assetSet).ordinal();
		this.repaymentType = getRepaymentType(assetSet);
		this.overdueStatus = assetSet.getOverdueStatus().ordinal();
		this.overDueDays = assetSet.getOverDueDays();
		this.auditOverdueDays = assetSet.getAuditOverdueDays(financialContract.getLoanOverdueStartDay());
		this.repurchaseStatus = getRepurchaseStatusOrdinal(assetSet.getExecutingStatus());
		this.guaranteeAndSettlementOrderStatus = GuaranteeAndSettlementStatusMapUtil.getGuaranteeAndSettlementStatus(assetSet);
		if(assetSet.getAssetRecycleDate() != null ){
			this.assetRecycleDate = DateUtils.format(assetSet.getAssetRecycleDate());
		}
		if(assetSet.getActualRecycleDate() != null){
			this.actualRecycleDate = DateUtils.format(assetSet.getActualRecycleDate());
		}
		this.accountNo = financialContract.getCapitalAccount().getMarkedAccountNo();
		this.loanAssetPrincipal = assetSet.getAssetPrincipalValue();
		this.loanAssetInterest = assetSet.getAssetInterestValue();
		this.loanServiceFee = chargesDetail.getLoanServiceFee();
		this.loanTechFee = chargesDetail.getLoanTechFee();
		this.loanOtherFee = chargesDetail.getLoanOtherFee();
		this.overdueFeePenalty = chargesDetail.getOverdueFeePenalty();
		this.overdueFeeObligation = chargesDetail.getOverdueFeeObligation();
		this.overdueFeeService = chargesDetail.getOverdueFeeService();
		this.overdueFeeOther = chargesDetail.getOverdueFeeOther();
		this.planTotalFee = chargesDetail.getTotalFee();
		this.paidUpTotalFee = paidChargesDetail.getTotalFee();
		if(queryTime != null){
			this.queryTime = DateUtils.format(queryTime, "yyyy-MM-dd HH:mm:ss");
		}
		//还款记录详情
		this.paymentRecords = paymentRecords;
		//回购单列表
		if(CollectionUtils.isNotEmpty(repurchaseDocs)){
			this.repurchaseDocs = repurchaseDocs.stream().map(go -> new RepurchaseShowModel(go)).collect(Collectors.toList());
		}
		//担保单列表
		if(CollectionUtils.isNotEmpty(guaranteeOrders)){
			this.guaranteeOrders = guaranteeOrders.stream().map(go -> new GuaranteeOrderShowModel(go, assetSet.getGuaranteeStatus())).collect(Collectors.toList());
		}
		//担保清算单列表
		if(CollectionUtils.isNotEmpty(settlementOrders)){
			this.settelementOrders = settlementOrders.stream().map(so -> new SettlementOrderShowModel(so, assetSet.getSettlementStatus(), financialContract)).collect(Collectors.toList());
		}
	}
	
	public Integer getPlanStatusOrdinal(AssetSet assetSet){
		if(assetSet != null){
			if(assetSet.getActiveStatus() == AssetSetActiveStatus.FROZEN){
				return LOCK;
			}else if(assetSet.getActiveStatus() == AssetSetActiveStatus.INVALID){
				return OFF;
			}else{
				if(assetSet.getPlanType() == PlanType.NORMAL){
					return NORMAL;
				}else if(assetSet.getPlanType() == PlanType.PREPAYMENT && assetSet.isCanBeRollbacked() == true){
					return PREREPAYMENT;
				}else if(assetSet.getPlanType() == PlanType.PREPAYMENT && assetSet.isCanBeRollbacked() == false){
					return MODIFYPRE;
				}
			}
		}
		return null;
	}
	
	public Integer getRepaymentType(AssetSet assetSet){
		if(assetSet != null){
			if(assetSet.getAssetStatus() == AssetClearStatus.CLEAR){
				if(assetSet.getRepaymentPlanType() == RepaymentPlanType.NORMAL){
					return RepaymentPlanSpec.repaymentTypeMap.get(RepaymentTypeMsg.NORMAL);
				}else if(assetSet.getRepaymentPlanType() == RepaymentPlanType.OVERDUE_REPAYMENT){
					return RepaymentPlanSpec.repaymentTypeMap.get(RepaymentTypeMsg.OVERDUE);
				}else if(assetSet.getRepaymentPlanType() == RepaymentPlanType.PREPAYMENT){
					if(assetSet.getContractFundingStatus() == ContractFundingStatus.ALL){
						return RepaymentPlanSpec.repaymentTypeMap.get(RepaymentTypeMsg.PRE_ALL);
					}else if(assetSet.getContractFundingStatus() == ContractFundingStatus.PART){
						return RepaymentPlanSpec.repaymentTypeMap.get(RepaymentTypeMsg.PRE_PART);
					}
				}
			}
		}
		return null;
	}
	
	private Integer getRepurchaseStatusOrdinal(ExecutingStatus executingStatus){
		if(executingStatus == ExecutingStatus.REPURCHASING || executingStatus == ExecutingStatus.REPURCHASED || executingStatus == ExecutingStatus.DEFAULT){
			return executingStatus.ordinal();
		}
		return null;
	}

	public RepaymentPlanDetailsResultModel() {
		
	}

	public String getSingleLoanContractNo() {
		return singleLoanContractNo;
	}
	
	public String getOuterRepaymentPlanNo() {
		return outerRepaymentPlanNo;
	}

	public String getContractNo() {
		return contractNo;
	}

	public Integer getPeriods() {
		return periods;
	}

	public Integer getCurrentPeriod() {
		return currentPeriod;
	}

	public Integer getPlanStatus() {
		return planStatus;
	}

	public Integer getPaymentStatus() {
		return paymentStatus;
	}

	public Integer getRepaymentType() {
		return repaymentType;
	}

	public Integer getOverdueStatus() {
		return overdueStatus;
	}

	public Integer getOverDueDays() {
		return overDueDays;
	}

	public Integer getAuditOverdueDays() {
		return auditOverdueDays;
	}

	public Integer getRepurchaseStatus() {
		return repurchaseStatus;
	}

	public Integer getGuaranteeAndSettlementOrderStatus() {
		return guaranteeAndSettlementOrderStatus;
	}

	public String getAssetRecycleDate() {
		return assetRecycleDate;
	}

	public String getActualRecycleDate() {
		return actualRecycleDate;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public BigDecimal getLoanAssetPrincipal() {
		return loanAssetPrincipal;
	}

	public BigDecimal getLoanAssetInterest() {
		return loanAssetInterest;
	}

	public BigDecimal getLoanServiceFee() {
		return loanServiceFee;
	}

	public BigDecimal getLoanTechFee() {
		return loanTechFee;
	}

	public BigDecimal getLoanOtherFee() {
		return loanOtherFee;
	}

	public BigDecimal getOverdueFeePenalty() {
		return overdueFeePenalty;
	}

	public BigDecimal getOverdueFeeObligation() {
		return overdueFeeObligation;
	}

	public BigDecimal getOverdueFeeService() {
		return overdueFeeService;
	}

	public BigDecimal getOverdueFeeOther() {
		return overdueFeeOther;
	}

	public BigDecimal getPlanTotalFee() {
		return planTotalFee;
	}

	public BigDecimal getPaidUpTotalFee() {
		return paidUpTotalFee;
	}

	public String getQueryTime() {
		return queryTime;
	}

	public List<PaymentRecordShowModel> getPaymentRecords() {
		return paymentRecords;
	}

	public List<RepurchaseShowModel> getRepurchaseDocs() {
		return repurchaseDocs;
	}

	public List<GuaranteeOrderShowModel> getGuaranteeOrders() {
		return guaranteeOrders;
	}

	public List<SettlementOrderShowModel> getSettelementOrders() {
		return settelementOrders;
	}

	public void setSingleLoanContractNo(String singleLoanContractNo) {
		this.singleLoanContractNo = singleLoanContractNo;
	}

	public void setOuterRepaymentPlanNo(String outerRepaymentPlanNo) {
		this.outerRepaymentPlanNo = outerRepaymentPlanNo;
	}

	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}

	public void setPeriods(Integer periods) {
		this.periods = periods;
	}

	public void setCurrentPeriod(Integer currentPeriod) {
		this.currentPeriod = currentPeriod;
	}

	public void setPlanStatus(Integer planStatus) {
		this.planStatus = planStatus;
	}

	public void setPaymentStatus(Integer paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public void setRepaymentType(Integer repaymentType) {
		this.repaymentType = repaymentType;
	}

	public void setOverdueStatus(Integer overdueStatus) {
		this.overdueStatus = overdueStatus;
	}

	public void setOverDueDays(Integer overDueDays) {
		this.overDueDays = overDueDays;
	}

	public void setAuditOverdueDays(Integer auditOverdueDays) {
		this.auditOverdueDays = auditOverdueDays;
	}

	public void setRepurchaseStatus(Integer repurchaseStatus) {
		this.repurchaseStatus = repurchaseStatus;
	}

	public void setGuaranteeAndSettlementOrderStatus(
			Integer guaranteeAndSettlementOrderStatus) {
		this.guaranteeAndSettlementOrderStatus = guaranteeAndSettlementOrderStatus;
	}

	public void setAssetRecycleDate(String assetRecycleDate) {
		this.assetRecycleDate = assetRecycleDate;
	}

	public void setActualRecycleDate(String actualRecycleDate) {
		this.actualRecycleDate = actualRecycleDate;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public void setLoanAssetPrincipal(BigDecimal loanAssetPrincipal) {
		this.loanAssetPrincipal = loanAssetPrincipal;
	}

	public void setLoanAssetInterest(BigDecimal loanAssetInterest) {
		this.loanAssetInterest = loanAssetInterest;
	}

	public void setLoanServiceFee(BigDecimal loanServiceFee) {
		this.loanServiceFee = loanServiceFee;
	}

	public void setLoanTechFee(BigDecimal loanTechFee) {
		this.loanTechFee = loanTechFee;
	}

	public void setLoanOtherFee(BigDecimal loanOtherFee) {
		this.loanOtherFee = loanOtherFee;
	}

	public void setOverdueFeePenalty(BigDecimal overdueFeePenalty) {
		this.overdueFeePenalty = overdueFeePenalty;
	}

	public void setOverdueFeeObligation(BigDecimal overdueFeeObligation) {
		this.overdueFeeObligation = overdueFeeObligation;
	}

	public void setOverdueFeeService(BigDecimal overdueFeeService) {
		this.overdueFeeService = overdueFeeService;
	}

	public void setOverdueFeeOther(BigDecimal overdueFeeOther) {
		this.overdueFeeOther = overdueFeeOther;
	}

	public void setPlanTotalFee(BigDecimal planTotalFee) {
		this.planTotalFee = planTotalFee;
	}

	public void setPaidUpTotalFee(BigDecimal paidUpTotalFee) {
		this.paidUpTotalFee = paidUpTotalFee;
	}

	public void setQueryTime(String queryTime) {
		this.queryTime = queryTime;
	}

	public void setPaymentRecords(List<PaymentRecordShowModel> paymentRecords) {
		this.paymentRecords = paymentRecords;
	}

	public void setRepurchaseDocs(List<RepurchaseShowModel> repurchaseDocs) {
		this.repurchaseDocs = repurchaseDocs;
	}

	public void setGuaranteeOrders(List<GuaranteeOrderShowModel> guaranteeOrders) {
		this.guaranteeOrders = guaranteeOrders;
	}

	public void setSettelementOrders(
			List<SettlementOrderShowModel> settelementOrders) {
		this.settelementOrders = settelementOrders;
	}
	
}
