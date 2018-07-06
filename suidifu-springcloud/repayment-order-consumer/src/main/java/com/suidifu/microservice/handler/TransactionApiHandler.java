package com.suidifu.microservice.handler;


public interface TransactionApiHandler {

	void queryCreditModelByNotifyServer(String tradeUuid, Integer paymentGateWayInt, String financialContractUuid, String batchNo);

}
