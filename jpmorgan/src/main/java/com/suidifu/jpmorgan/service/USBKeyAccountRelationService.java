package com.suidifu.jpmorgan.service;

import com.demo2do.core.service.GenericService;
import com.suidifu.jpmorgan.entity.GatewayType;
import com.suidifu.jpmorgan.entity.USBKeyAccountRelation;

/**
 * 
 * @author zjm
 *
 */
public interface USBKeyAccountRelationService extends GenericService<USBKeyAccountRelation> {

	String getUSBKeyBy(String accountUuid, GatewayType gateWayType);
}
