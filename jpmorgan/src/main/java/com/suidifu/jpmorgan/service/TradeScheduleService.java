package com.suidifu.jpmorgan.service;

import java.util.Date;
import java.util.List;

import com.demo2do.core.service.GenericService;
import com.suidifu.jpmorgan.entity.GatewaySlot;
import com.suidifu.jpmorgan.entity.SlotInfo;
import com.suidifu.jpmorgan.entity.TradeSchedule;

public interface TradeScheduleService extends GenericService<TradeSchedule> {
	
	void tradeScheduleInQueue(TradeSchedule tradeSchedule);

	void tradeScheduleInQueue(List<TradeSchedule> tradeScheduleList);
	
	TradeSchedule getTradeScheduleBy(Long tradeScheduleId);
	
	List<TradeSchedule> peekIdleSchedules(int limit, List<Integer> piorityList, int modIndex, int workerNo);
	
	boolean updateTransactionTime(Long tradeScheduleId, int nthSlot, Date TranBegin, Date TranEnd, int tryTimes);
		
	boolean updateSlotInSchedule(SlotInfo slotInfo);
	
	List<TradeSchedule> peekBusinessProcessingSchedules(int limit, List<Integer> piorityList, int modIndex, int workerNo);
	
	 boolean updateBusinessStatusAndCommunicationStatusInSlot(TradeSchedule tradeSchedule, int nthSlot, GatewaySlot gatewaySlotUpdate, String gatewayName);
	
	boolean abandonSchedule(TradeSchedule tradeSchedule);
	
	boolean abandonSlotInSchedule(String tradeUuid,int nth);

	boolean ProcessScheduleInSlot(Long tradeScheduleId, int nthSlot);
	
	List<TradeSchedule> getTradeScheduleListBy(String transactionUuid);
	
	List<TradeSchedule> getTradeScheduleListByBatch(String batchUuid);
	
	TradeSchedule getTradeScheduleByTradeUuid(String tradeUuid);
	
}
