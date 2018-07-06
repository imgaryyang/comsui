package com.suidifu.jpmorgan.handler;

import java.util.Date;

import com.suidifu.jpmorgan.entity.GatewaySlot;

public interface WritebackStatHandler {
	
	public void writebackStat(String paymentChannelUuid, Date date, GatewaySlot gatewaySlotUpdate);
}
