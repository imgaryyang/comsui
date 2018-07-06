package com.suidifu.bridgewater.handler.v2;

import com.zufangbao.sun.api.model.remittance.RemittanceSqlModel;

import java.util.Map;

public interface ZhongHangRemittanceApplicationHandler {

	String getRemittanceResultsForBatchNotice(RemittanceSqlModel remittanceApplication,
	                                          String remittanceApplicationUuid);

	Map buildHeaderParamsForNotifyRemittanceResult(String content, String financialContractUuid);
}
