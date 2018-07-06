package com.suidifu.jpmorgan.handler.impl;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.suidifu.jpmorgan.entity.GatewaySlot;
import com.suidifu.jpmorgan.entity.WritebackStat;
import com.suidifu.jpmorgan.handler.WritebackStatHandler;
import com.suidifu.jpmorgan.service.WritebackStatService;

@Component("writebackStatHandler")
public class WritebackStatHandlerImpl implements WritebackStatHandler{
	
	@Autowired
	private WritebackStatService writebackStatService;

	@Override
	public void writebackStat(String paymentChannelUuid, Date date, GatewaySlot gatewaySlotUpdate) {
		try {
			if(gatewaySlotUpdate.isBusinessProcessing()) {
				return;
			}
			
			List<WritebackStat> listWriteback = writebackStatService.select(paymentChannelUuid);
			
			if(CollectionUtils.isNotEmpty(listWriteback)) {
				
				WritebackStat writebackStat = listWriteback.get(0);
				writebackStatService.update(writebackStat, gatewaySlotUpdate, date);
				
			}else {
				writebackStatService.insert(paymentChannelUuid, gatewaySlotUpdate, date);
			}
		} catch (Exception e) {
			e.printStackTrace();
			//TODO log
		}
		
		
			
	}

}
