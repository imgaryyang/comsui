package com.suidifu.jpmorgan.service;

import java.util.List;

import com.demo2do.core.service.GenericService;
import com.suidifu.jpmorgan.entity.TradeSchedule;
import com.suidifu.jpmorgan.entity.TradeScheduleLog;

public interface TradeScheduleLogService extends GenericService<TradeScheduleLog> {

	List<TradeSchedule> getTradeScheduleListBy(String transactionUuid);
	
	List<TradeSchedule> getTradeScheduleListByBatch(String batchUuid);
	
}
