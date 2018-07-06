package com.suidifu.jpmorgan.handler.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.suidifu.jpmorgan.entity.DistributeStat;
import com.suidifu.jpmorgan.handler.DistributeStatHandler;
import com.suidifu.jpmorgan.service.DistributeStatService;

@Component("distributeStatHandler")
public class DistributeStatHandlerImpl implements DistributeStatHandler {

	@Autowired
	private DistributeStatService distributeStatService;
	
	@Override
	public void distributeStat(Date date, String paymentChannelUuid, BigDecimal amount) {
		
		try {
			List<DistributeStat> distributeStatList = distributeStatService.select(paymentChannelUuid);
			
			if(CollectionUtils.isEmpty(distributeStatList)){
				
				distributeStatService.insert(date, paymentChannelUuid, amount);
			}else {
				
				DistributeStat distributeStat = distributeStatList.get(0);
				distributeStatService.update(date, distributeStat, amount);
			}
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}

}
