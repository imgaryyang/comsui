package com.zufangbao.earth.model;

import com.demo2do.core.utils.DateUtils;
import com.zufangbao.sun.entity.contract.Contract;
import com.zufangbao.sun.entity.customer.Customer;
import com.zufangbao.sun.entity.directbank.business.ContractAccount;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.entity.order.Order;
import com.zufangbao.sun.yunxin.entity.AssetSet;
import com.zufangbao.sun.yunxin.entity.ExecutingStatus;
import com.zufangbao.sun.yunxin.entity.RepaymentExecutionStateMapUtil;
import com.zufangbao.sun.yunxin.entity.SettlementOrder;
import com.zufangbao.sun.yunxin.entity.api.deduct.DeductApplication;
import com.zufangbao.sun.yunxin.entity.model.RepaymentChargesDetail;
import com.zufangbao.sun.yunxin.entity.model.assetset.GuaranteeOrderShowModel;
import com.zufangbao.sun.yunxin.entity.model.assetset.NormalOrderShowModel;
import com.zufangbao.sun.yunxin.entity.model.assetset.OverdueFeeShowModel;
import com.zufangbao.sun.yunxin.entity.model.assetset.SettlementOrderShowModel;
import com.zufangbao.sun.yunxin.entity.model.deduct.DeductApplicationShowModel;
import com.zufangbao.wellsfargo.silverpool.cashauditing.entity.BalanceRefundOrderShowModel;
import com.zufangbao.wellsfargo.silverpool.cashauditing.entity.voucher.RepaymentPlanDocumentModel;
import com.zufangbao.wellsfargo.silverpool.cashauditing.entity.voucher.RepaymentRecordDetail;
import com.zufangbao.wellsfargo.yunxin.PaymentRecordAssetModel;
import org.apache.commons.collections.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 还款单详情展示数据
 * @author zhanghongbing
 *
 */
public class AssetSetDetailShowModel {

	/** 贷款信息 **/
	//合同id
	private Long contractId;
	
	//合同编号
	private String contractNo;
	
	//贷款客户编号
	private String customerNo;
	
	//资产编号
	private String assetNo;
	
	//还款计划唯一编号
	private String repaymentPlanUuid;
	
	//还款编号
	private String repaymentPlanNo;
	
	//商户还款计划编号
	private String outerRepaymentPlanNo;
	
	/** 还款信息 **/
	//计划还款日期
	private String assetRecycleDate;
	
	// 计划还款明细
	private RepaymentChargesDetail planChargesDetail = new RepaymentChargesDetail();
	
	// 逾期费用明细
	private List<OverdueFeeShowModel> overdueChargesDetail = new LinkedList<OverdueFeeShowModel>();
	
	//差异天数
	private String overDueDays;
	
	//逾期天数
	private String auditOverdueDays;
	
	//实际还款日期
	private String actualRecycleDate;
	
	// 实际还款金额
	private String actualPaidAmount;
	
	//担保状态
	private String guaranteeStatus;
	
	//逾期状态
	private String overdueStatus;
	
	//还款状态
	private String paymentStatus;
	
	//提前还款是否可以作废
	private boolean canBeInvalid;

	// 计划状态
	private String planStatus;

	// 还款类型
	private String repaymentType;
	
	//备注
	private String comment;
	
	/** 账户信息 **/
	//客户姓名
	private String customerName;
	
	//账户开户行
	private String accountBank;
	
	//开户行所在省
	private String accountProvince;
	
	//开户行所在市
	private String accountCity;

	//账户号
	private String accountNo;
	
	//系统账户余额
	private BigDecimal accountBalance;

	// 标志--允许拆分结算单
	private boolean allowSplitSettlement;

	//是否可以新增浮动费用明细
	private boolean canCreate;

	//结算单列表
	private List<NormalOrderShowModel> normalOrders = new ArrayList<NormalOrderShowModel>();
	
	//担保单列表
	private List<GuaranteeOrderShowModel> guaranteeOrders = new ArrayList<GuaranteeOrderShowModel>();
	
	//清算单列表
	private List<SettlementOrderShowModel> settelementOrders = new ArrayList<SettlementOrderShowModel>();
	
	// 扣款订单
	private List<DeductApplicationShowModel> deductApplications = new ArrayList<DeductApplicationShowModel>();
	
	//支付记录
	private List<PaymentRecordAssetModel> paymentRecords = new ArrayList<PaymentRecordAssetModel>();
	
	// 相关凭证
	private List<RepaymentPlanDocumentModel> vouchers = new ArrayList<RepaymentPlanDocumentModel>();

	private List<RepaymentRecordDetail> repaymentRecordDetails = new ArrayList<RepaymentRecordDetail>();
	
	// 入账资金明细
	private RepaymentChargesDetail actualChargesDetail = new RepaymentChargesDetail();
	
	//在途资金明细
	private RepaymentChargesDetail intransitChargesDetail = new RepaymentChargesDetail();
	
	//实还总资金明细
	private RepaymentChargesDetail paidUpChargesDetail = new RepaymentChargesDetail();
	
	//退款记录
	private List<BalanceRefundOrderShowModel> refundRecord = new ArrayList<BalanceRefundOrderShowModel>();

	public Long getContractId() {
		return contractId;
	}

	public boolean getAllowSplitSettlement() {
		return allowSplitSettlement;
	}

	public void setAllowSplitSettlement(boolean allowSplitSettlement) {
		this.allowSplitSettlement = allowSplitSettlement;
	}

	public void setContractId(Long contractId) {
		this.contractId = contractId;
	}

	public String getContractNo() {
		return contractNo;
	}

	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}

	public String getCustomerNo() {
		return customerNo;
	}

	public void setCustomerNo(String customerNo) {
		this.customerNo = customerNo;
	}

	public String getAssetNo() {
		return assetNo;
	}

	public void setAssetNo(String assetNo) {
		this.assetNo = assetNo;
	}

	public String getRepaymentPlanUuid() {
		return repaymentPlanUuid;
	}

	public void setRepaymentPlanUuid(String repaymentPlanUuid) {
		this.repaymentPlanUuid = repaymentPlanUuid;
	}

	public String getRepaymentPlanNo() {
		return repaymentPlanNo;
	}

	public void setRepaymentPlanNo(String repaymentPlanNo) {
		this.repaymentPlanNo = repaymentPlanNo;
	}

	public String getOuterRepaymentPlanNo() {
		return outerRepaymentPlanNo;
	}

	public void setOuterRepaymentPlanNo(String outerRepaymentPlanNo) {
		this.outerRepaymentPlanNo = outerRepaymentPlanNo;
	}

	public String getAssetRecycleDate() {
		return assetRecycleDate;
	}

	public void setAssetRecycleDate(String assetRecycleDate) {
		this.assetRecycleDate = assetRecycleDate;
	}

	public RepaymentChargesDetail getPlanChargesDetail() {
		return planChargesDetail;
	}

	public void setPlanChargesDetail(RepaymentChargesDetail planChargesDetail) {
		this.planChargesDetail = planChargesDetail;
	}
	
	public List<OverdueFeeShowModel> getOverdueChargesDetail() {
		return overdueChargesDetail;
	}

	public void setOverdueChargesDetail(
			List<OverdueFeeShowModel> overdueChargesDetail) {
		this.overdueChargesDetail = overdueChargesDetail;
	}

	public String getOverDueDays() {
		return overDueDays;
	}

	public void setOverDueDays(String overDueDays) {
		this.overDueDays = overDueDays;
	}

	public String getAuditOverdueDays() {
		return auditOverdueDays;
	}

	public void setAuditOverdueDays(String auditOverdueDays) {
		this.auditOverdueDays = auditOverdueDays;
	}

	public String getActualRecycleDate() {
		return actualRecycleDate;
	}

	public void setActualRecycleDate(String actualRecycleDate) {
		this.actualRecycleDate = actualRecycleDate;
	}

	public String getActualPaidAmount() {
		return actualPaidAmount;
	}

	public void setActualPaidAmount(String actualPaidAmount) {
		this.actualPaidAmount = actualPaidAmount;
	}

	public String getGuaranteeStatus() {
		return guaranteeStatus;
	}

	public void setGuaranteeStatus(String guaranteeStatus) {
		this.guaranteeStatus = guaranteeStatus;
	}

	public String getOverdueStatus() {
		return overdueStatus;
	}

	public void setOverdueStatus(String overdueStatus) {
		this.overdueStatus = overdueStatus;
	}

	public String getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public String getPlanStatus() {
		return planStatus;
	}

	public void setPlanStatus(String planStatus) {
		this.planStatus = planStatus;
	}

	public String getRepaymentType() {
		return repaymentType;
	}

	public void setRepaymentType(String repaymentType) {
		this.repaymentType = repaymentType;
	}

	public boolean isCanBeInvalid() {
		return canBeInvalid;
	}

	public void setCanBeInvalid(boolean canBeInvalid) {
		this.canBeInvalid = canBeInvalid;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getAccountBank() {
		return accountBank;
	}

	public void setAccountBank(String accountBank) {
		this.accountBank = accountBank;
	}

	public String getAccountProvince() {
		return accountProvince;
	}

	public void setAccountProvince(String accountProvince) {
		this.accountProvince = accountProvince;
	}

	public String getAccountCity() {
		return accountCity;
	}

	public void setAccountCity(String accountCity) {
		this.accountCity = accountCity;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}
	
	public BigDecimal getAccountBalance() {
		return accountBalance;
	}

	public void setAccountBalance(BigDecimal accountBalance) {
		this.accountBalance = accountBalance;
	}

	public List<NormalOrderShowModel> getNormalOrders() {
		return normalOrders;
	}

	public void setNormalOrders(List<NormalOrderShowModel> normalOrders) {
		this.normalOrders = normalOrders;
	}

	public List<GuaranteeOrderShowModel> getGuaranteeOrders() {
		return guaranteeOrders;
	}

	public void setGuaranteeOrders(List<GuaranteeOrderShowModel> guaranteeOrders) {
		this.guaranteeOrders = guaranteeOrders;
	}

	public List<SettlementOrderShowModel> getSettelementOrders() {
		return settelementOrders;
	}

	public void setSettelementOrders(
			List<SettlementOrderShowModel> settelementOrders) {
		this.settelementOrders = settelementOrders;
	}
	
	public List<DeductApplicationShowModel> getDeductApplications() {
		return deductApplications;
	}

	public void setDeductApplications(List<DeductApplicationShowModel> deductApplications) {
		this.deductApplications = deductApplications;
	}
	
	public List<PaymentRecordAssetModel> getPaymentRecords() {
		return paymentRecords;
	}

	public void setPaymentRecords(List<PaymentRecordAssetModel> paymentRecords) {
		this.paymentRecords = paymentRecords;
	}

	public List<RepaymentPlanDocumentModel> getVouchers() {
		return vouchers;
	}

	public void setVouchers(List<RepaymentPlanDocumentModel> vouchers) {
		this.vouchers = vouchers;
	}

	public boolean isCanCreate() {
		return canCreate;
	}

	public void setCanCreate(boolean canCreate) {
		this.canCreate = canCreate;
	}


	public List<RepaymentRecordDetail> getRepaymentRecordDetails() {
		return repaymentRecordDetails;
	}

	public void setRepaymentRecordDetails(List<RepaymentRecordDetail> repaymentRecordDetails) {
		this.repaymentRecordDetails = repaymentRecordDetails;
	}
	

	public RepaymentChargesDetail getActualChargesDetail() {
		return actualChargesDetail;
	}

	public void setActualChargesDetail(RepaymentChargesDetail actualChargesDetail) {
		this.actualChargesDetail = actualChargesDetail;
	}

	public RepaymentChargesDetail getIntransitChargesDetail() {
		return intransitChargesDetail;
	}

	public void setIntransitChargesDetail(RepaymentChargesDetail intransitChargesDetail) {
		this.intransitChargesDetail = intransitChargesDetail;
	}

	public RepaymentChargesDetail getPaidUpChargesDetail() {
		return paidUpChargesDetail;
	}

	public void setPaidUpChargesDetail(RepaymentChargesDetail paidUpChargesDetail) {
		this.paidUpChargesDetail = paidUpChargesDetail;
	}

	public List<BalanceRefundOrderShowModel> getRefundRecord() {
		return refundRecord;
	}

	public void setRefundRecord(List<BalanceRefundOrderShowModel> refundRecord) {
		this.refundRecord = refundRecord;
	}

	public AssetSetDetailShowModel() {
		super();
	}

	public AssetSetDetailShowModel(List<RepaymentRecordDetail> repaymentRecordDetails,FinancialContract financialContract, AssetSet repaymentPlan, ContractAccount contractAccount,
								   RepaymentChargesDetail planChargesDetail, Date receivableChargesModifyTime, Contract contract,
								   List<Order> normalOrders, List<Order> guaranteeOrders, List<SettlementOrder> settlementOrders,
								   List<DeductApplication> deductApplications, List<PaymentRecordAssetModel> paymentRecords,
								   BigDecimal accountBalance, OverdueFeeShowModel overdueActualPriceShowModel, List<RepaymentPlanDocumentModel> vouchers, boolean overDueIsAllowModify, 
								   boolean allowSplitSettlement, BigDecimal actualPaidAmount, RepaymentChargesDetail actualChargesDetail, RepaymentChargesDetail intransitChargesDetail, 
								   RepaymentChargesDetail paidUpChargesDetail, List<BalanceRefundOrderShowModel> refundRecord) {
		//贷款信息
		this.contractId = contract.getId();
		this.contractNo = contract.getContractNo();
		Customer customer = contract.getCustomer();
		this.customerNo = customer.getSource();
		this.assetNo = "";
		this.repaymentPlanUuid = repaymentPlan.getAssetUuid();
		this.repaymentPlanNo = repaymentPlan.getSingleLoanContractNo();
		this.outerRepaymentPlanNo = repaymentPlan.getOuterRepaymentPlanNo();
		//还款信息
		if(repaymentPlan.getAssetRecycleDate() != null) {
			this.assetRecycleDate = DateUtils.format(repaymentPlan.getAssetRecycleDate());
		}
		this.planChargesDetail = planChargesDetail;
		this.overDueDays = String.valueOf(repaymentPlan.getOverDueDays());
		this.auditOverdueDays = String.valueOf(repaymentPlan.getAuditOverdueDays(financialContract.getLoanOverdueStartDay()));
		if(repaymentPlan.getActualRecycleDate() != null) {
			this.actualRecycleDate = DateUtils.format(repaymentPlan.getActualRecycleDate(), "yyyy-MM-dd HH:mm:ss");
		}
		this.actualPaidAmount = actualPaidAmount == null ? "" : actualPaidAmount.toString();
		this.guaranteeStatus = repaymentPlan.getGuaranteeStatus().getChineseMessage();
		this.overdueStatus = repaymentPlan.getOverdueStatus().getChineseMessage();
		this.paymentStatus = RepaymentExecutionStateMapUtil.getRepaymentExecutionState(repaymentPlan);
		this.planStatus = repaymentPlan.getPlanStatusCn();
		this.repaymentType = repaymentPlan.getRepaymentTypeCn();
		this.canBeInvalid = repaymentPlan.canBeInvalid();
		this.comment = repaymentPlan.getComment();
		this.canCreate = isCanCreate(repaymentPlan.getExecutingStatus());
		//账户信息
		this.customerName = customer.getName();
		if(contractAccount != null) {
			this.accountBank = contractAccount.getBank();
			this.accountProvince = contractAccount.getProvince();
			this.accountCity = contractAccount.getCity();
			this.accountNo = contractAccount.getPayAcNo();
			this.accountBalance = accountBalance;
		}

		this.allowSplitSettlement = allowSplitSettlement;
		
		if(overdueActualPriceShowModel != null){
			this.overdueChargesDetail.add(overdueActualPriceShowModel);
		}
		OverdueFeeShowModel receivableOverdueFees = OverdueFeeShowModel.initializeReceivable(planChargesDetail, receivableChargesModifyTime,overDueIsAllowModify);
		this.overdueChargesDetail.add(receivableOverdueFees);
		
		//结算单列表
		if(CollectionUtils.isNotEmpty(normalOrders)){
			this.normalOrders = normalOrders.stream().sorted((o1,o2)->o2.getModifyTime().compareTo(o1.getModifyTime())).map(NormalOrderShowModel::new).collect(Collectors.toList());
		}
		//担保单列表
		if(CollectionUtils.isNotEmpty(guaranteeOrders)){
			this.guaranteeOrders = guaranteeOrders.stream().map(go -> new GuaranteeOrderShowModel(go, repaymentPlan.getGuaranteeStatus())).collect(Collectors.toList());
		}
		//担保清算单列表
		if(CollectionUtils.isNotEmpty(settlementOrders)){
			this.settelementOrders = settlementOrders.stream().map(so -> new SettlementOrderShowModel(so, repaymentPlan.getSettlementStatus())).collect(Collectors.toList());
		}
		//扣款订单
		if(CollectionUtils.isNotEmpty(deductApplications)){
			this.deductApplications = deductApplications.stream().sorted((o1,o2)->o2.getLastModifiedTime().compareTo(o1.getLastModifiedTime())).map(DeductApplicationShowModel::new).collect(Collectors.toList());
		}
		//支付记录
		this.paymentRecords = paymentRecords;
		// 相关凭证
		this.vouchers = vouchers;
		this.repaymentRecordDetails = repaymentRecordDetails;
		this.actualChargesDetail = actualChargesDetail;
		this.intransitChargesDetail = intransitChargesDetail;
		this.paidUpChargesDetail = paidUpChargesDetail;
		this.refundRecord = refundRecord;
	}

	private boolean isCanCreate(ExecutingStatus executingStatus) {
		switch (executingStatus) {
			case SUCCESSFUL:
				return false;
			case REPURCHASED:
				return false;
			case REPURCHASING:
				return false;
			case DEFAULT:
				return false;
			default:
				return true;
		}
	}
	
}
