package com.suidifu.jpmorgan.entity.unionpay;

import java.math.BigDecimal;
import java.util.List;

public class RealTimePaymentInfoModel  extends UnionPayBasicInfoModel{
	
	private String businessCode; //业务代码
	
	private String merchantId; //商户代码

	private int totalItem; //总记录数
	
	private BigDecimal totalSum; //总金额
	
	private PaymentDetailInfoModel detailInfo; //付款详情列表

	public String getBusinessCode() {
		return businessCode;
	}

	public void setBusinessCode(String businessCode) {
		this.businessCode = businessCode;
	}

	public String getMerchantId() {
		return merchantId;
	}
	
	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public int getTotalItem() {
		return totalItem;
	}

	public void setTotalItem(int totalItem) {
		this.totalItem = totalItem;
	}

	public BigDecimal getTotalSum() {
		return totalSum;
	}

	public void setTotalSum(BigDecimal totalSum) {
		this.totalSum = totalSum;
	}


	public RealTimePaymentInfoModel() {
		super();
	}

	public PaymentDetailInfoModel getDetailInfo() {
		return detailInfo;
	}

	public void setDetailInfo(PaymentDetailInfoModel detailInfo) {
		this.detailInfo = detailInfo;
	}
	
}
