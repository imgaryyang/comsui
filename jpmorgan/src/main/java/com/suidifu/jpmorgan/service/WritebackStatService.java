package com.suidifu.jpmorgan.service;

import java.util.Date;
import java.util.List;

import com.suidifu.jpmorgan.entity.GatewaySlot;
import com.suidifu.jpmorgan.entity.WritebackStat;

public interface WritebackStatService {
	
	public void update(WritebackStat writebackStat, GatewaySlot gatewaySlotUpdate, Date date);
	public List<WritebackStat> select(String paymentChannelUuid);
	public void insert(String paymentChannelUuid, GatewaySlot gatewaySlotUpdate, Date date);
}
