package com.suidifu.microservice.model;

import com.suidifu.owlman.microservice.enumation.JournalVoucherStatus;
import com.suidifu.owlman.microservice.enumation.JournalVoucherType;
import com.zufangbao.sun.entity.account.VirtualAccount;
import com.zufangbao.sun.entity.customer.CustomerType;
import com.zufangbao.sun.entity.directbank.business.CashFlow;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.utils.EnumUtil;
import com.zufangbao.sun.yunxin.entity.AssetSet;
import com.zufangbao.sun.yunxin.entity.model.BaseQueryModel;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 余额支付详情模型
 * @author dcc
 *
 */
public class PaymentOrderDetailModel extends BaseQueryModel {
	
	//支付单号
	private String journalVoucherNo;
	//支付金额
	private BigDecimal bookingAmount;
	//创建时间
	private Date createTime;
	//状态
	private JournalVoucherStatus journalVoucherStatus;
	//发生时间 
	private Date lastModifyTime;
	
	//账户编号
	private String virtualAccountNo;
	//账户名称
	private String virtualAccountName;
	//客户类型
	private CustomerType customerType;
	
	
	//信托合同名称
	private String financialContractName;
	//信托合同编号
	private String financialContractNo;
	//贷款合同编号
	private String contractNo;
	//还款编号
	private String relatedBillContractNoLv3;
	//商户还款计划编号
	private String outerRepaymentPlanNo;	
	//订单编号
	private String relatedBillContractNoLv4;
	
	//银行流水
	private String bankSequenceNo;
	//流水总金额
	private BigDecimal transactionAmount;
	//凭证编码
	private String sourcedoumentNo;
	
	
	//退款单号
	private String journalVoucherRefundNo;
	//备注
	private String summaryRefund;
	
	//日志
	private String objectUuid;
	
	private String virtualAccountUuid;
	//Id
	private String contractId;
	
	private String financialContractUuid;
	//Id
	private String assetSetId;

	private String assetSetUuid;
	//Id
	private String orderId;
	
	private String orderType;
	
	private String cashFlowUuid;
	
	private String sourceDocumentUuid;
	
	private String journalVoucherRefundUuid;
	
	private String accountNo;
	
	private boolean isCanRefund;
	
	public boolean isCanRefund() {
		return isCanRefund;
	}

	public void setCanRefund(boolean isCanRefund) {
		this.isCanRefund = isCanRefund;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getJournalVoucherRefundUuid() {
		return journalVoucherRefundUuid;
	}

	public void setJournalVoucherRefundUuid(String journalVoucherRefundUuid) {
		this.journalVoucherRefundUuid = journalVoucherRefundUuid;
	}

	public String getVirtualAccountUuid() {
		return virtualAccountUuid;
	}

	public String getFinancialContractUuid() {
		return financialContractUuid;
	}

	public String getCashFlowUuid() {
		return cashFlowUuid;
	}

	public void setVirtualAccountUuid(String virtualAccountUuid) {
		this.virtualAccountUuid = virtualAccountUuid;
	}

	public void setFinancialContractUuid(String financialContractUuid) {
		this.financialContractUuid = financialContractUuid;
	}

	public void setCashFlowUuid(String cashFlowUuid) {
		this.cashFlowUuid = cashFlowUuid;
	}

	public String getObjectUuid() {
		return objectUuid;
	}

	public void setObjectUuid(String objectUuid) {
		this.objectUuid = objectUuid;
	}

	public String getJournalVoucherNo() {
		return journalVoucherNo;
	}

	public BigDecimal getBookingAmount() {
		return bookingAmount;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public Date getLastModifyTime() {
		return lastModifyTime;
	}

	public String getVirtualAccountNo() {
		return virtualAccountNo;
	}

	public String getVirtualAccountName() {
		return virtualAccountName;
	}

	public CustomerType getCustomerType() {
		return customerType;
	}

	public String getFinancialContractName() {
		return financialContractName;
	}

	public String getFinancialContractNo() {
		return financialContractNo;
	}

	public String getContractNo() {
		return contractNo;
	}

	public String getRelatedBillContractNoLv3() {
		return relatedBillContractNoLv3;
	}
	
	public String getOuterRepaymentPlanNo(){
		return outerRepaymentPlanNo;
	}
	
	public String getRelatedBillContractNoLv4() {
		return relatedBillContractNoLv4;
	}

	public String getBankSequenceNo() {
		return bankSequenceNo;
	}

	public BigDecimal getTransactionAmount() {
		return transactionAmount;
	}

	public String getSourcedoumentNo() {
		return sourcedoumentNo;
	}

	public String getJournalVoucherRefundNo() {
		return journalVoucherRefundNo;
	}


	public void setJournalVoucherNo(String journalVoucherNo) {
		this.journalVoucherNo = journalVoucherNo;
	}

	public void setBookingAmount(BigDecimal bookingAmount) {
		this.bookingAmount = bookingAmount;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public void setLastModifyTime(Date lastModifyTime) {
		this.lastModifyTime = lastModifyTime;
	}

	public void setVirtualAccountNo(String virtualAccountNo) {
		this.virtualAccountNo = virtualAccountNo;
	}

	public void setVirtualAccountName(String virtualAccountName) {
		this.virtualAccountName = virtualAccountName;
	}

	public void setCustomerType(CustomerType customerType) {
		this.customerType = customerType;
	}

	public void setFinancialContractName(String financialContractName) {
		this.financialContractName = financialContractName;
	}

	public void setFinancialContractNo(String financialContractNo) {
		this.financialContractNo = financialContractNo;
	}

	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}

	public void setRelatedBillContractNoLv3(String relatedBillContractNoLv3) {
		this.relatedBillContractNoLv3 = relatedBillContractNoLv3;
	}
	
	public void setOuterRepaymentPlanNo(String outerRepaymentPlanNo){
		this.outerRepaymentPlanNo = outerRepaymentPlanNo;
	}

	public void setRelatedBillContractNoLv4(String relatedBillContractNoLv4) {
		this.relatedBillContractNoLv4 = relatedBillContractNoLv4;
	}

	public void setBankSequenceNo(String bankSequenceNo) {
		this.bankSequenceNo = bankSequenceNo;
	}

	public void setTransactionAmount(BigDecimal transactionAmount) {
		this.transactionAmount = transactionAmount;
	}

	public void setSourcedoumentNo(String sourcedoumentNo) {
		this.sourcedoumentNo = sourcedoumentNo;
	}

	
	public void setJournalVoucherRefundNo(String journalVoucherRefundNo) {
		this.journalVoucherRefundNo = journalVoucherRefundNo;
	}

	public String getSummaryRefund() {
		return summaryRefund;
	}

	public void setSummaryRefund(String summaryRefund) {
		this.summaryRefund = summaryRefund;
	}
	
	public JournalVoucherStatus getJournalVoucherStatus() {
		return journalVoucherStatus;
	}

	public void setJournalVoucherStatus(JournalVoucherStatus journalVoucherStatus) {
		this.journalVoucherStatus = journalVoucherStatus;
	}

	public String getContractId() {
		return contractId;
	}

	public String getAssetSetId() {
		return assetSetId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setContractId(String contractId) {
		this.contractId = contractId;
	}

	public void setAssetSetId(String assetSetId) {
		this.assetSetId = assetSetId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	
	public String getSourceDocumentUuid() {
		return sourceDocumentUuid;
	}

	public String getAssetSetUuid() {
		return assetSetUuid;
	}

	public void setAssetSetUuid(String assetSetUuid) {
		this.assetSetUuid = assetSetUuid;
	}

	public void setSourceDocumentUuid(String sourceDocumentUuid) {
		this.sourceDocumentUuid = sourceDocumentUuid;
	}

	public PaymentOrderDetailModel(JournalVoucher journalVoucher,VirtualAccount virtualAccount,CashFlow cashFlow ,AssetSet assetSet, 
			FinancialContract financialContract,String journalVoucherRefundNo,String summaryRefund,
			SourceDocument sourceDocument,String journalVoucherRefundUuid,String orderId,String contractId,String assetSetId,String orderType,String voucherNo,String accountNo){
		
		this.journalVoucherNo = journalVoucher.getJournalVoucherNo();
		this.bookingAmount = journalVoucher.getBookingAmount();
		this.createTime = journalVoucher.getCreatedDate();
		this.lastModifyTime = journalVoucher.getLastModifiedTime();
		this.journalVoucherStatus = journalVoucher.getStatus();
		if(virtualAccount != null){
			this.customerType = EnumUtil.fromOrdinal(CustomerType.class, virtualAccount.getCustomerType());
		}
		this.virtualAccountName = journalVoucher.getCounterPartyName()==null?"":journalVoucher.getCounterPartyName();
		this.virtualAccountNo = journalVoucher.getCounterPartyAccount()==null?"":journalVoucher.getCounterPartyAccount();
		this.financialContractNo = financialContract == null?"":financialContract.getContractNo();
		this.financialContractName = financialContract == null?"":financialContract.getContractName();
		this.contractNo = journalVoucher.getRelatedBillContractNoLv2()==null?"":journalVoucher.getRelatedBillContractNoLv2();
		this.relatedBillContractNoLv3 = journalVoucher.getRelatedBillContractNoLv3()==null?"":journalVoucher.getRelatedBillContractNoLv3();
		this.outerRepaymentPlanNo = assetSet.getOuterRepaymentPlanNo()==null?"":assetSet.getOuterRepaymentPlanNo();
		this.relatedBillContractNoLv4 = journalVoucher.getRelatedBillContractNoLv4()==null?"":journalVoucher.getRelatedBillContractNoLv4();
		this.journalVoucherRefundNo = journalVoucherRefundNo;
		this.summaryRefund = summaryRefund;
		this.objectUuid = journalVoucher.getJournalVoucherUuid();
		this.bankSequenceNo = cashFlow == null?"":cashFlow.getBankSequenceNo();
		this.sourcedoumentNo = voucherNo;
		this.transactionAmount = cashFlow == null?BigDecimal.ZERO:cashFlow.getTransactionAmount();
		
		this.contractId = contractId;
		this.financialContractUuid = financialContract == null?"":financialContract.getFinancialContractUuid();
		this.assetSetId = assetSetId;
		this.assetSetUuid = assetSet.getAssetUuid();
		this.virtualAccountUuid = virtualAccount==null?"":virtualAccount.getVirtualAccountUuid();
		this.sourceDocumentUuid = sourceDocument== null?"":sourceDocument.getSourceDocumentUuid();
		this.journalVoucherRefundUuid = journalVoucherRefundUuid;
		this.orderId = orderId;
		this.orderType = orderType;
		this.accountNo = accountNo;

        this.isCanRefund = (journalVoucher.getJournalVoucherType() == JournalVoucherType.VIRTUAL_ACCOUNT_TRANSFER_REPAYMENT || journalVoucher.getJournalVoucherType
                () == JournalVoucherType.VIRTUAL_ACCOUNT_TRANSFER_GUARANTEE)
                && journalVoucher.getStatus() == JournalVoucherStatus.VOUCHER_ISSUED;
	}
	
	public String getCustomerTypeName(){
		return customerType==null?"":customerType.getChineseName();
	}
	
	public String getJournalVoucherStatusName(){
		return journalVoucherStatus==null?"":journalVoucherStatus.getChineseName();
	}



}
