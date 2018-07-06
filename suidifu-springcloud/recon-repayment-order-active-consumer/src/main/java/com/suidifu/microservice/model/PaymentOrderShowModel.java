package com.suidifu.microservice.model;

import com.suidifu.owlman.microservice.enumation.JournalVoucherStatus;
import com.suidifu.owlman.microservice.enumation.JournalVoucherType;
import com.zufangbao.sun.entity.account.VirtualAccount;
import com.zufangbao.sun.entity.contract.Contract;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.entity.order.Order;
import com.zufangbao.sun.yunxin.entity.AssetSet;
import com.zufangbao.sun.yunxin.entity.OrderType;
import com.zufangbao.sun.yunxin.entity.model.BaseQueryModel;
import java.math.BigDecimal;
import java.util.Date;

public class PaymentOrderShowModel extends BaseQueryModel{
	
	//支付单号
	private String journalVoucherNo;
	//账户编号
	private String virtualAccountNo;
	//商户还款计划编号
	private String outerRepaymentPlanNo;
	//账户名称
	private String virtualAccountName;
	//信托项目编号
	private String financialContractNo;
	//贷款合同编号
	private String contractNo;
	//还款编号
	private String assetSetNo;
	//订单编号
	private String orderNo;
	//创建时间
	private Date createTime;
	//支付金额
	private BigDecimal amount;
	//凭证编号
	private String sourceDocumentNo;
	//状态
	private JournalVoucherStatus status;
	
	private JournalVoucherType journalVoucherType;
	
	private String journalVoucherUuid;
	private String virtualAccountUuid;
	private String financialContractUuid;
	private String contractId;
	private String assetsetId;
	private String assetSetUuid;
	private String orderId;
	private OrderType orderType;
	private String sourceDocumentUuid;
	
	public PaymentOrderShowModel(JournalVoucher journalVoucher,VirtualAccount virtualAccount,FinancialContract financialContract,
			Contract contract,AssetSet assetSet,Order order,SourceDocument sourceDocument,String voucherNo){
		
		this.journalVoucherNo = journalVoucher.getJournalVoucherNo();
		this.virtualAccountNo = journalVoucher.getCounterPartyAccount();
		this.outerRepaymentPlanNo = assetSet==null?"":assetSet.getOuterRepaymentPlanNo();
		this.virtualAccountName = journalVoucher.getCounterPartyName();
		this.financialContractNo = financialContract==null?"":financialContract.getContractNo();
		this.contractNo = contract==null?"":contract.getContractNo();
		this.assetSetNo = assetSet==null?"":assetSet.getSingleLoanContractNo();
		this.orderNo = order==null?"":order.getOrderNo();
		this.createTime = journalVoucher.getCreatedDate();
		this.amount = journalVoucher.getBookingAmount();
		this.sourceDocumentNo = voucherNo;
		this.status = journalVoucher.getStatus();
		this.journalVoucherType = journalVoucher.getJournalVoucherType();
		
		this.journalVoucherUuid = journalVoucher.getJournalVoucherUuid();
		this.virtualAccountUuid = virtualAccount==null?"":virtualAccount.getVirtualAccountUuid();
		this.financialContractUuid =financialContract==null?"":financialContract.getFinancialContractUuid();
		this.contractId = contract==null?"":contract.getId().toString();
		this.assetsetId = assetSet==null?"":assetSet.getId().toString();
		this.assetSetUuid = assetSet==null?"":assetSet.getAssetUuid();
		if(order!=null){
			this.orderId = order.getId().toString();
			this.orderType = order.getOrderType();
		}
		this.sourceDocumentUuid = sourceDocument==null?"":sourceDocument.getSourceDocumentUuid();
	}
	
	public String getOrderTypeName(){
		return orderType==null?"":orderType.getChineseName();
	}
	
	public String getStatusName(){
		return status==null?"":status.getChineseName();
	}
	public JournalVoucherType getJournalVoucherType() {
		return journalVoucherType;
	}

	public void setJournalVoucherType(JournalVoucherType journalVoucherType) {
		this.journalVoucherType = journalVoucherType;
	}

	public String getOuterRepaymentPlanNo() {
		return outerRepaymentPlanNo;
	}

	public void setOuterRepaymentPlanNo(String outerRepaymentPlanNo) {
		this.outerRepaymentPlanNo = outerRepaymentPlanNo;
	}

	public String getJournalVoucherNo() {
		return journalVoucherNo;
	}
	public String getVirtualAccountNo() {
		return virtualAccountNo;
	}
	public String getVirtualAccountName() {
		return virtualAccountName;
	}
	public String getFinancialContractNo() {
		return financialContractNo;
	}
	public String getContractNo() {
		return contractNo;
	}
	public String getAssetSetNo() {
		return assetSetNo;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public String getSourceDocumentNo() {
		return sourceDocumentNo;
	}
	public JournalVoucherStatus getStatus() {
		return status;
	}
	public String getJournalVoucherUuid() {
		return journalVoucherUuid;
	}
	public String getVirtualAccountUuid() {
		return virtualAccountUuid;
	}
	public String getFinancialContractUuid() {
		return financialContractUuid;
	}
	public String getContractId() {
		return contractId;
	}
	public String getAssetsetId() {
		return assetsetId;
	}
	public String getOrderId() {
		return orderId;
	}
	public String getSourceDocumentUuid() {
		return sourceDocumentUuid;
	}
	public void setJournalVoucherNo(String journalVoucherNo) {
		this.journalVoucherNo = journalVoucherNo;
	}
	public void setVirtualAccountNo(String virtualAccountNo) {
		this.virtualAccountNo = virtualAccountNo;
	}
	public void setVirtualAccountName(String virtualAccountName) {
		this.virtualAccountName = virtualAccountName;
	}
	public void setFinancialContractNo(String financialContractNo) {
		this.financialContractNo = financialContractNo;
	}
	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}
	public void setAssetSetNo(String assetSetNo) {
		this.assetSetNo = assetSetNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public void setSourceDocumentNo(String sourceDocumentNo) {
		this.sourceDocumentNo = sourceDocumentNo;
	}
	public void setStatus(JournalVoucherStatus status) {
		this.status = status;
	}
	public void setJournalVoucherUuid(String journalVoucherUuid) {
		this.journalVoucherUuid = journalVoucherUuid;
	}
	public void setVirtualAccountUuid(String virtualAccountUuid) {
		this.virtualAccountUuid = virtualAccountUuid;
	}
	public void setFinancialContractUuid(String financialContractUuid) {
		this.financialContractUuid = financialContractUuid;
	}
	public void setContractId(String contractId) {
		this.contractId = contractId;
	}
	public void setAssetsetId(String assetsetId) {
		this.assetsetId = assetsetId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public void setSourceDocumentUuid(String sourceDocumentUuid) {
		this.sourceDocumentUuid = sourceDocumentUuid;
	}

	public OrderType getOrderType() {
		return orderType;
	}

	public void setOrderType(OrderType orderType) {
		this.orderType = orderType;
	}
	
	public boolean isCanRefund(){
		return (this.journalVoucherType==JournalVoucherType.VIRTUAL_ACCOUNT_TRANSFER_REPAYMENT ||this.journalVoucherType==JournalVoucherType.VIRTUAL_ACCOUNT_TRANSFER_GUARANTEE) 
				&& this.status == JournalVoucherStatus.VOUCHER_ISSUED;
	}

	public String getAssetSetUuid() {
		return assetSetUuid;
	}

	public void setAssetSetUuid(String assetSetUuid) {
		this.assetSetUuid = assetSetUuid;
	}
}
