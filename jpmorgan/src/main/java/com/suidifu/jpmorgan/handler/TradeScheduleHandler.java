package com.suidifu.jpmorgan.handler;

import java.util.List;

import com.demo2do.core.entity.Result;
import com.suidifu.coffer.entity.QueryCreditResult;
import com.suidifu.jpmorgan.entity.*;
import com.suidifu.jpmorgan.exception.QueryStatusException;
import com.suidifu.jpmorgan.exception.TradeScheduleParseException;
import com.suidifu.jpmorgan.exception.UpdateSlotException;

public interface TradeScheduleHandler {
	
	PaymentReturnModel parseAndSaveSinglePayment(String reqPacket) throws TradeScheduleParseException;
	
	List<PaymentReturnModel> parseAndSaveBatchPayment(String reqPacket) throws TradeScheduleParseException;
			
	Result distributeToWorker(TradeSchedule tradeSchedule, GatewaySlot gatewaySlot, int nthSlot);
	
	void transferToTradeScheduleLog(TradeSchedule tradeSchedule);
	
	boolean updateSlot(SlotInfo slotInfo) throws UpdateSlotException;
	
	List<QueryStatusResult> queryStatus(String transactionUuid, String batchUuid) throws QueryStatusException;
		
	boolean meetExecutionPrecond(TradeSchedule tradeSchedule);
	
	QueryStatusResult extractQueryInfo(TradeSchedule tradeSchedule);
	
	QueryStatusResult extractQueryInfo(TradeSchedule tradeSchedule, int nthSlot);
	
	List<TradeSchedule> getTradeScheduleByBatch(String batchUuid);
	
	QueryCreditResult queryOppositeStatus(String paymentChannelUuid, String transactionVoucherNo);
	
	List<SupplyCallbackResultModel> supplyCallback(List<String> transactionUuidList);
	
	void handleInnerCallback(String tradeUuid, int nthSlot, String gatewayName, GatewaySlot gatewaySlotUpdate);

	Boolean executionNextSlot(TradeSchedule tradeSchedule);
}
