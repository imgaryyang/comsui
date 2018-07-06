package com.suidifu.bridgewater.handler;

import java.util.Map;

public interface RemittanceNotifyJobReceiver {
	
	void receiveCitiGroupCallbackAfterBusinessValidation(Map<String, String> receiveParams);

	void receiveCitiGroupCallbackAfterQuotaValidation(Map<String, String> receiveParams);
	
	void receiveJpmorganCallback(Map<String, String> receiveParams);

	void receiveCitiGroupAfterResendQuotaValidation(Map<String, String> receiveParams);

	void receiveCitiGroupAfterSecondQuotaValidation(Map<String, String> receiveParams);
}
