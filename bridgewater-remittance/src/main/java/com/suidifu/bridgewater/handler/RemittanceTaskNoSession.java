package com.suidifu.bridgewater.handler;

import java.util.List;
import java.util.Map;

import com.zufangbao.sun.yunxin.entity.remittance.RemittanceApplication;

public interface RemittanceTaskNoSession {
	void sendToCitigroupForBussinessValidation(RemittanceApplication remittanceApplication);

	void sendToCitigroupForQuotaValidation(RemittanceApplication remittanceApplication);

	Map<String, Object> callBackQueryStatusForRemittance(List<String> remittanceApplicationUuids);
}
