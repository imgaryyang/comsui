package com.suidifu.microservice.model;

import com.suidifu.owlman.microservice.enumation.JournalVoucherStatus;
import com.zufangbao.sun.entity.account.VirtualAccount;
import com.zufangbao.sun.entity.customer.CustomerType;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.utils.EnumUtil;
import com.zufangbao.sun.yunxin.entity.AssetSet;
import com.zufangbao.sun.yunxin.entity.model.BaseQueryModel;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 支付退款详情模型
 * @author dcc
 *
 */
public class RefundOrderDetailModel extends BaseQueryModel {
	
	//退款单号
	private String journalVoucherRefundNo;
	//退款金额
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
	
	//支付单号
	private String journalVoucherNo;
	//日志
	private String obejctUuid;
	
	private String contractId;
	
	private String financialContractUuid;
	
	private String assetSetId;

	private String assetSetUuid;

	private String orderId;
	
	private String orderType;
	
	private String virtualAccountUuid;
	private String journalVoucherUuid;

	
	public String getOrderType() {
		return orderType;
	}


	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}


	public String getFinancialContractUuid() {
		return financialContractUuid;
	}


	public void setFinancialContractUuid(String financialContractUuid) {
		this.financialContractUuid = financialContractUuid;
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


	public String getVirtualAccountUuid() {
		return virtualAccountUuid;
	}

	public String getJournalVoucherUuid() {
		return journalVoucherUuid;
	}

	public void setVirtualAccountUuid(String virtualAccountUuid) {
		this.virtualAccountUuid = virtualAccountUuid;
	}

	public void setJournalVoucherUuid(String journalVoucherUuid) {
		this.journalVoucherUuid = journalVoucherUuid;
	}

	public String getObejctUuid() {
		return obejctUuid;
	}

	public void setObejctUuid(String obejctUuid) {
		this.obejctUuid = obejctUuid;
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
	
	

	public String getOuterRepaymentPlanNo() {
		return outerRepaymentPlanNo;
	}


	public void setOuterRepaymentPlanNo(String outerRepaymentPlanNo) {
		this.outerRepaymentPlanNo = outerRepaymentPlanNo;
	}


	public String getRelatedBillContractNoLv4() {
		return relatedBillContractNoLv4;
	}

	public String getJournalVoucherRefundNo() {
		return journalVoucherRefundNo;
	}

	public void setJournalVoucherRefundNo(String journalVoucherRefundNo) {
		this.journalVoucherRefundNo = journalVoucherRefundNo;
	}

	public void setBookingAmount(BigDecimal bookingAmount) {
		this.bookingAmount = bookingAmount;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public JournalVoucherStatus getJournalVoucherStatus() {
		return journalVoucherStatus;
	}

	public void setJournalVoucherStatus(JournalVoucherStatus journalVoucherStatus) {
		this.journalVoucherStatus = journalVoucherStatus;
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

	

	public void setRelatedBillContractNoLv4(String relatedBillContractNoLv4) {
		this.relatedBillContractNoLv4 = relatedBillContractNoLv4;
	}

	public void setJournalVoucherNo(String journalVoucherNo) {
		this.journalVoucherNo = journalVoucherNo;
	}
	
	public RefundOrderDetailModel(JournalVoucher journalVoucherRefund,VirtualAccount virtualAccount,
			FinancialContract financialContract,String journalVoucherNo,String journalVoucherUuid,String orderId,
			String contractId,String assetSetId,String orderType,AssetSet assetSet){
		
		this.journalVoucherRefundNo = journalVoucherRefund.getJournalVoucherNo();
		this.bookingAmount = journalVoucherRefund.getBookingAmount();
		this.createTime = journalVoucherRefund.getCreatedDate();
		this.lastModifyTime = journalVoucherRefund.getLastModifiedTime();
		this.journalVoucherStatus = journalVoucherRefund.getStatus();
		if(virtualAccount != null){
			this.customerType = EnumUtil.fromOrdinal(CustomerType.class, virtualAccount.getCustomerType());
		}
		this.virtualAccountName = journalVoucherRefund.getLocalPartyName()==null?"":journalVoucherRefund.getLocalPartyName();
		this.virtualAccountNo = journalVoucherRefund.getLocalPartyAccount()==null?"":journalVoucherRefund.getLocalPartyAccount();
		this.financialContractNo = financialContract == null?"":financialContract.getContractNo();
		this.financialContractName = financialContract == null?"":financialContract.getContractName();
		this.contractNo = journalVoucherRefund.getRelatedBillContractNoLv2()==null?"":journalVoucherRefund.getRelatedBillContractNoLv2();
		this.relatedBillContractNoLv3 = journalVoucherRefund.getRelatedBillContractNoLv3()==null?"":journalVoucherRefund.getRelatedBillContractNoLv3();
		this.outerRepaymentPlanNo = assetSet.getOuterRepaymentPlanNo()==null?"":assetSet.getOuterRepaymentPlanNo();
		this.relatedBillContractNoLv4 = journalVoucherRefund.getRelatedBillContractNoLv4()==null?"":journalVoucherRefund.getRelatedBillContractNoLv4();
		this.journalVoucherNo = journalVoucherNo;
		this.obejctUuid = journalVoucherRefund.getJournalVoucherUuid();
		
		this.contractId = contractId;
		this.financialContractUuid = financialContract==null?"":financialContract.getFinancialContractUuid();
		this.assetSetId = assetSetId;
		this.assetSetUuid = assetSet.getAssetUuid();
		this.orderId = orderId;

		this.virtualAccountUuid = virtualAccount==null?"":virtualAccount.getVirtualAccountUuid();
		this.journalVoucherUuid = journalVoucherUuid;
		this.orderType = orderType;
		
		
	}

	public String getAssetSetUuid() {
		return assetSetUuid;
	}

	public void setAssetSetUuid(String assetSetUuid) {
		this.assetSetUuid = assetSetUuid;
	}

	public String getCustomerTypeName(){
		return customerType==null?"":customerType.getChineseName();
	}
	
	public String getJournalVoucherStatusName(){
		return journalVoucherStatus==null?"":journalVoucherStatus.getChineseName();
	}
}
