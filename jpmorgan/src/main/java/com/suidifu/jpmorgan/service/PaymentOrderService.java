package com.suidifu.jpmorgan.service;

import java.util.List;

import com.demo2do.core.service.GenericService;
import com.suidifu.coffer.entity.QueryCreditResult;
import com.suidifu.jpmorgan.entity.PaymentOrder;

public interface PaymentOrderService extends GenericService<PaymentOrder> {
	
	void taskInQueue(PaymentOrder task, String tableName, Integer queueIndex);
	
	//void taskInQueue(List<PaymentOrder> taskList, String tableName, ConsistentHash<String> consistentHashSource);
		
	List<PaymentOrder> peekIdleTasks(String tableName, int limit, int workerNo);
	
	//Integer queueSelectAlgorithm(List<Integer> piorityList, int index);
	
	boolean atomOccupy(int nthSlot, PaymentOrder dstPaymentOrder, int tryTimes, String workerUuid, String tableName);
	
	///ready to sent
	boolean atomSentOut(int nthSlot, PaymentOrder dstPaymentOrder, int tryTimes, String tableName);
	///sent real msg
	
	boolean atomFeedBack(int nthSlot, PaymentOrder dstPaymentOrder, int tryTimes, String tableName);
	//catch
	/*
	 * atomeFeedBack
	 */
	List<PaymentOrder> peekProcessingTasks(String queueTableName);
	
	boolean atomForceStop(int nthSlot, PaymentOrder dstPaymentOrder, int tryTimes, String queueTableName);

	List<PaymentOrder> getReadyTasksForUpdateBusinessStatus(String queueTableName, int workerNo);
	
	List<PaymentOrder> getSendingTimeOutTasks(int timeoutMinute, String queueTableName);
	
	boolean updateBusinessStatus(PaymentOrder dstPaymentOrder, QueryCreditResult queryCreditResult, int tryTimes, String tableName);

	void transferToPaymentOrderLog(PaymentOrder paymentOrder, String queueTableName, String logTableName);
	
	PaymentOrder getPaymentOrderBy(String paymentOrderUuid, String tableName);
}
