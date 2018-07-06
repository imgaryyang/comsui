package com.suidifu.jpmorgan.task;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.suidifu.jpmorgan.entity.AccountSide;
import com.suidifu.jpmorgan.entity.GatewayType;
import com.suidifu.jpmorgan.exception.TradeScheduleParseException;
import com.suidifu.jpmorgan.handler.TradeScheduleHandler;
import com.suidifu.jpmorgan.util.UUIDUtil;

@Component
public class SenderTradeScheduleTask {
	
	@Autowired
	private TradeScheduleHandler tradeScheduleHandler;
	
	private long i = 0;
	
	private static final Log logger = LogFactory
			.getLog(SenderTradeScheduleTask.class);

    //@Scheduled(fixedDelay = 1000)
	public void saveTradeScheduleList() {
		
		try {
			tradeScheduleHandler.parseAndSaveSinglePayment(sendTradeScheduleList());
			
		} catch (TradeScheduleParseException e) {
			
			e.printStackTrace();
			
		}
	}
	
	private String sendTradeScheduleList() {
		
		i++;
		Map<String, Object> map = new HashMap<String, Object>();

		map.put("accountSide", AccountSide.CREDIT.ordinal());
		//map.put("sourceAccountNo", "571907757810703");
		map.put("destinationAccountName", "汪水");
		map.put("destinationAccountNo", "5685968545868856");
		map.put("destinationBankInfo", "{\"bankCode\":\"C10102\"}");
		//map.put("currencyCode", "10");
		map.put("outlierTransactionUuid", UUID.randomUUID().toString());
		//if(i % 2 == 0) {
		map.put("fstSlotUuid", UUIDUtil.uuid());
		//map.put("fstGatewayType", GatewayType.SuperBank.ordinal());
		map.put("fstTransactionAmount", new BigDecimal("0.07"));
		map.put("fstPaymentChannelUuid", "f8bb9956-1952-4893-98c8-66683d25d7ce");
		
		map.put("sndSlotUuid", UUIDUtil.uuid());
		//map.put("sndGatewayType", GatewayType.UnionPay.ordinal());
		map.put("sndTransactionAmount", new BigDecimal("0.09"));
		map.put("sndPaymentChannelUuid", "f8bb9956-1952-4893-98c8-66683d25d7ce");
		map.put("batchUuid", "25821cae-2537-48fa-9722-5fa9d48ba02d");
		map.put("executionPrecond", "{\"abandon\":[\"a5007855-0d93-4a4c-92dd-f1ca07425a13\"],\"start\":[\"a5007855-0d93-4a4c-92dd-f1ca07425a13\"]}");
			
//		} else {
//			map.put("sndSlotUuid", UUID.randomUUID().toString());
//			map.put("sndGatewayType", GatewayType.SuperBank.ordinal());
//			map.put("sndTransactionAmount", new BigDecimal("0.01"));
//			map.put("sndPaymentChannelUuid", "f8bb9956-1952-4893-98c8-66683d25d7ce");
//			
//			map.put("fstSlotUuid", UUID.randomUUID().toString());
//			map.put("fstGatewayType", GatewayType.UnionPay.ordinal());
//			map.put("fstTransactionAmount", new BigDecimal("0.01"));
//			map.put("fstPaymentChannelUuid", "25821cae-2537-48fa-9722-5fa9d48ba02d");
//		}
		
				
		String stringTradeScheduleListJSON = JSON.toJSONString(map);
		
		return stringTradeScheduleListJSON;
		
		
	}
}
