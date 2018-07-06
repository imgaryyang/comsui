package com.suidifu.bridgewater.handler.common.v2;

import com.zufangbao.sun.api.model.remittance.PaymentChannelSummaryInfo;
import com.zufangbao.sun.entity.financial.BusinessType;
import com.zufangbao.sun.yunxin.entity.remittance.AccountSide;

import java.math.BigDecimal;
import java.util.List;

public interface IPaymentChannelHandler {

	List<PaymentChannelSummaryInfo> getPaymentChannelSummaryInfosBy(String financialContractUuid,
	                                                                BusinessType businessType, AccountSide accountSide, String bankCode, BigDecimal plannedAmount);

	PaymentChannelSummaryInfo getFirstPaymentChannelSummaryInfoBy(String financialContractUuid,
	                                                              BusinessType businessType, AccountSide accountSide, String bankCode, BigDecimal plannedAmount);
	
}
