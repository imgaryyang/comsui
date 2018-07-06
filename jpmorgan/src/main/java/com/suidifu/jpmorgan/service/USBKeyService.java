package com.suidifu.jpmorgan.service;

import com.demo2do.core.service.GenericService;
import com.suidifu.jpmorgan.entity.USBKey;

/**
 * 
 * @author zjm
 *
 */
public interface USBKeyService extends GenericService<USBKey> {

	USBKey getUSBKeyByUUID(String uuid);
}
