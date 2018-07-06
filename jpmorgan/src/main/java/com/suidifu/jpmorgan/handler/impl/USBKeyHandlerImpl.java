package com.suidifu.jpmorgan.handler.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.suidifu.jpmorgan.entity.Account;
import com.suidifu.jpmorgan.entity.GatewayType;
import com.suidifu.jpmorgan.entity.USBKey;
import com.suidifu.jpmorgan.exception.UnknownUkeyException;
import com.suidifu.jpmorgan.handler.USBKeyHandler;
import com.suidifu.jpmorgan.service.AccountService;
import com.suidifu.jpmorgan.service.USBKeyAccountRelationService;
import com.suidifu.jpmorgan.service.USBKeyService;
import com.suidifu.jpmorgan.spec.BankCorpMsgSpec;

/**
 * 
 * @author zjm
 *
 */
@Component("usbKeyHandler")
public class USBKeyHandlerImpl implements USBKeyHandler {
	
	@Autowired
	private AccountService accountService;
	@Autowired
	private USBKeyAccountRelationService usbKeyAccountRelationService;
	@Autowired
	private USBKeyService usbKeyService;

	@Override
	public USBKey getUSBKeyByPayerAccount(String payerAccountNo, GatewayType gateWayType) throws UnknownUkeyException {
		
		Account payerAccount = accountService.getAccountBy(payerAccountNo);
		
		if(null == payerAccount) {
			throw new UnknownUkeyException(BankCorpMsgSpec.MSG_PAYERACCOUNT_EMPTY_ERROR);
		}
		
		String usbKeyUuid = usbKeyAccountRelationService.getUSBKeyBy(payerAccount.getUuid(), gateWayType);
		
		return usbKeyService.getUSBKeyByUUID(usbKeyUuid);
	}

	
}
