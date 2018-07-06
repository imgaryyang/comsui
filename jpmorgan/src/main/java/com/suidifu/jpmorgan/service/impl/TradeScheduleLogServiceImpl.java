package com.suidifu.jpmorgan.service.impl;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.demo2do.core.service.impl.GenericServiceImpl;
import com.suidifu.jpmorgan.entity.TradeSchedule;
import com.suidifu.jpmorgan.entity.TradeScheduleLog;
import com.suidifu.jpmorgan.service.TradeScheduleLogService;

@Service("tradeScheduleLogService")
public class TradeScheduleLogServiceImpl extends
		GenericServiceImpl<TradeScheduleLog> implements TradeScheduleLogService {

	@Override
	public List<TradeSchedule> getTradeScheduleListBy(String transactionUuid) {
		if(StringUtils.isEmpty(transactionUuid)) {
			return Collections.EMPTY_LIST;
		}
		
		return genericDaoSupport.queryForList("select * from trade_schedule_log where outlier_transaction_uuid =:transactionUuid", "transactionUuid", transactionUuid, TradeSchedule.class);
		
	}


	@Override
	public List<TradeSchedule> getTradeScheduleListByBatch(String batchUuid) {
		if(StringUtils.isEmpty(batchUuid)) {
			return Collections.EMPTY_LIST;
		}
		
		return genericDaoSupport.queryForList("select * from trade_schedule_log where batch_uuid =:batchUuid", "batchUuid", batchUuid, TradeSchedule.class);
	}

}
