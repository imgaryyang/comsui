package com.suidifu.bridgewater.handler.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.suidifu.bridgewater.handler.RemittanceApplicationNoSession;
import com.suidifu.bridgewater.handler.RemittanceNotifyJobReceiver;

@Component("remittanceNotifyJobReceiver")
public class RemittanceNotifyJobReceiverImpl implements RemittanceNotifyJobReceiver{
	
	@Autowired
	private RemittanceApplicationNoSession remittanceApplicationNoSession;

	@Override
	public void receiveCitiGroupCallbackAfterBusinessValidation(Map<String, String> receiveParams) {
		remittanceApplicationNoSession.receiveCitiGroupCallbackAfterBusinessValidation(receiveParams);		
	}

	@Override
	public void receiveCitiGroupCallbackAfterQuotaValidation(Map<String, String> receiveParams) {
		remittanceApplicationNoSession.receiveCitiGroupCallbackAfterQuotaValidation(receiveParams);
	}

	@Override
	public void receiveJpmorganCallback(Map<String, String> receiveParams) {
		remittanceApplicationNoSession.receiveJpmorganCallback(receiveParams);
	}
	
	@Override
	public void receiveCitiGroupAfterResendQuotaValidation(Map<String, String> receiveParams) {
		remittanceApplicationNoSession.receiveCitiGroupAfterResendQuotaValidation(receiveParams);
	}
	

	@Override
	public void receiveCitiGroupAfterSecondQuotaValidation(Map<String, String> receiveParams) {
		remittanceApplicationNoSession.receiveCitiGroupAfterSecondQuotaValidation(receiveParams);
	}
}
