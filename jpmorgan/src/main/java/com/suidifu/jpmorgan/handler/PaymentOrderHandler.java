package com.suidifu.jpmorgan.handler;

import com.suidifu.jpmorgan.entity.DistributeWorkingContext;
import com.suidifu.jpmorgan.entity.GatewaySlot;
import com.suidifu.jpmorgan.entity.PaymentOrder;

public interface PaymentOrderHandler {
	
	public void paymentOrderListInitAndSave(PaymentOrder paymentOrder, DistributeWorkingContext workingContext);
		
	public GatewaySlot getRealtimeInfo(String paymentOrderUuid, String tableName, String logTableName);
	
	public void handleInnerCallback(PaymentOrder readyTask, String lobTableName) throws Exception;
	
}

