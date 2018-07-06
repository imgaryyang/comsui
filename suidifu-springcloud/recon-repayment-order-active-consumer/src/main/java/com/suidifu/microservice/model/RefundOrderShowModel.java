package com.suidifu.microservice.model;

import com.zufangbao.sun.entity.account.VirtualAccount;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.entity.order.Order;
import com.zufangbao.sun.yunxin.entity.AssetSet;
import com.zufangbao.sun.yunxin.entity.OrderType;
import com.zufangbao.sun.yunxin.entity.model.BaseQueryModel;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 支付退款列表模型
 * @author dcc
 *
 */
public class RefundOrderShowModel extends BaseQueryModel {
	
	//退款单号
	private String journalVoucherRefundNo;
	//商户还款计划编号
	private String outerRepaymentPlanNo;
	//账户编号
	private String virtualAccountNo;
	//账户名称
	private String virtualAccountName;
	//还款编号
	private String assetSetNo;
	
	//订单编号
	private String orderNo;
	//支付单号
	private String journalVoucherNo;
	//信托项目编号
	private String financialContractNo;
	//创建时间
	private Date createTime;
	//退款金额
	private BigDecimal amount;
	//备注
	private String summary;
	
	private String journalVoucherRefundUuid;
	private String virtualAccountUuid;
	private String orderId;
	private OrderType orderType;
	private String journalVoucherUuid;
	private String financialContractUuid;
	private String assetSetId;
	private String assetSetUuid;

	public RefundOrderShowModel(JournalVoucher journalVoucherRefund,VirtualAccount virtualAccount,FinancialContract financialContract,
			Order order,JournalVoucher journalVoucher,AssetSet assetSet){
		
		this.journalVoucherRefundNo = journalVoucherRefund.getJournalVoucherNo();
		this.outerRepaymentPlanNo = assetSet.getOuterRepaymentPlanNo()==null?"":assetSet.getOuterRepaymentPlanNo();
		this.virtualAccountNo = virtualAccount==null?"":virtualAccount.getVirtualAccountNo();
		this.virtualAccountName = virtualAccount==null?"":virtualAccount.getVirtualAccountAlias();
		this.orderNo = order==null?"":order.getOrderNo();
		this.journalVoucherNo = journalVoucher==null?"":journalVoucher.getJournalVoucherNo();
		this.financialContractNo = financialContract==null?"":financialContract.getContractNo();
		this.createTime = journalVoucherRefund.getCreatedDate();
		this.amount = journalVoucherRefund.getBookingAmount();
		this.summary = journalVoucherRefund.getRemarkInAppendix();
		this.assetSetNo = assetSet==null?"":assetSet.getSingleLoanContractNo();
		
		this.journalVoucherRefundUuid = journalVoucherRefund==null?"":journalVoucherRefund.getJournalVoucherUuid();
		this.virtualAccountUuid = virtualAccount==null?"":virtualAccount.getVirtualAccountUuid();
		if(order!=null){
			this.orderId = order.getId().toString();
			this.orderType = order.getOrderType();
		}
		this.journalVoucherUuid = journalVoucher==null?"":journalVoucher.getJournalVoucherUuid();
		this.financialContractUuid = financialContract==null?"":financialContract.getFinancialContractUuid();
		this.assetSetId = assetSet==null?"":assetSet.getId().toString();
		this.assetSetUuid = assetSet==null?"":assetSet.getAssetUuid();
	}
	
	public String getOrderTypeName(){
		return orderType==null?"":orderType.getChineseName();
	}
	
	public String getAssetSetNo() {
		return assetSetNo;
	}

	public String getAssetSetId() {
		return assetSetId;
	}

	public void setAssetSetNo(String assetSetNo) {
		this.assetSetNo = assetSetNo;
	}

	public void setAssetSetId(String assetSetId) {
		this.assetSetId = assetSetId;
	}

	public String getJournalVoucherRefundNo() {
		return journalVoucherRefundNo;
	}
	public String getVirtualAccountNo() {
		return virtualAccountNo;
	}
	
	
	
	public String getVirtualAccountName() {
		return virtualAccountName;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public String getJournalVoucherNo() {
		return journalVoucherNo;
	}
	public String getFinancialContractNo() {
		return financialContractNo;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public String getSummary() {
		return summary;
	}
	public String getJournalVoucherRefundUuid() {
		return journalVoucherRefundUuid;
	}
	public String getVirtualAccountUuid() {
		return virtualAccountUuid;
	}
	public String getOrderId() {
		return orderId;
	}
	public OrderType getOrderType() {
		return orderType;
	}
	public String getJournalVoucherUuid() {
		return journalVoucherUuid;
	}
	public String getFinancialContractUuid() {
		return financialContractUuid;
	}
	public void setJournalVoucherRefundNo(String journalVoucherRefundNo) {
		this.journalVoucherRefundNo = journalVoucherRefundNo;
	}
	
	
	public String getOuterRepaymentPlanNo() {
		return outerRepaymentPlanNo;
	}

	public void setOuterRepaymentPlanNo(String outerRepaymentPlanNo) {
		this.outerRepaymentPlanNo = outerRepaymentPlanNo;
	}

	public void setVirtualAccountNo(String virtualAccountNo) {
		this.virtualAccountNo = virtualAccountNo;
	}
	public void setVirtualAccountName(String virtualAccountName) {
		this.virtualAccountName = virtualAccountName;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public void setJournalVoucherNo(String journalVoucherNo) {
		this.journalVoucherNo = journalVoucherNo;
	}
	public void setFinancialContractNo(String financialContractNo) {
		this.financialContractNo = financialContractNo;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public void setJournalVoucherRefundUuid(String journalVoucherRefundUuid) {
		this.journalVoucherRefundUuid = journalVoucherRefundUuid;
	}
	public void setVirtualAccountUuid(String virtualAccountUuid) {
		this.virtualAccountUuid = virtualAccountUuid;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public void setOrderType(OrderType orderType) {
		this.orderType = orderType;
	}
	public void setJournalVoucherUuid(String journalVoucherUuid) {
		this.journalVoucherUuid = journalVoucherUuid;
	}
	public void setFinancialContractUuid(String financialContractUuid) {
		this.financialContractUuid = financialContractUuid;
	}

	public String getAssetSetUuid() {
		return assetSetUuid;
	}

	public void setAssetSetUuid(String assetSetUuid) {
		this.assetSetUuid = assetSetUuid;
	}
}


