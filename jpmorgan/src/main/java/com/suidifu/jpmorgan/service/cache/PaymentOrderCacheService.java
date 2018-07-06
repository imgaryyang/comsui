package com.suidifu.jpmorgan.service.cache;

import java.util.List;

import com.suidifu.jpmorgan.entity.PaymentOrder;

public interface PaymentOrderCacheService {
	
	void pushTaskToCache(String cacheKey, PaymentOrder task);
	
	//void pushTaskListToCache(List<PaymentOrder> taskList);
	
	List<PaymentOrder> peekIdleTasksInCache(String cacheKey);
	
	//String queueSelectAlgorithm(List<Integer> piorityList, int index);
	
	void deleteTaskInCache(String cacheKey, PaymentOrder task);
	
	List<PaymentOrder> peekReadyTasksInCache(String cacheKey);
}
