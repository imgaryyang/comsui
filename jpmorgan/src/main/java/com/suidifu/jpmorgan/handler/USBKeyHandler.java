package com.suidifu.jpmorgan.handler;

import com.suidifu.jpmorgan.entity.GatewayType;
import com.suidifu.jpmorgan.entity.USBKey;
import com.suidifu.jpmorgan.exception.UnknownUkeyException;

/**
 * 
 * @author zjm
 *
 */
public interface USBKeyHandler {

	USBKey getUSBKeyByPayerAccount(String payerAccountNo, GatewayType gateWayType) throws UnknownUkeyException;
}
