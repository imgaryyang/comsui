package com.suidifu.jpmorgan.service.impl.cache;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.demo2do.core.utils.JsonUtils;
import com.suidifu.coffer.entity.QueryCreditResult;
import com.suidifu.jpmorgan.entity.PaymentOrder;
import com.suidifu.jpmorgan.service.cache.PaymentOrderCacheService;
import com.suidifu.jpmorgan.service.impl.PaymentOrderServiceImpl;

@Component("paymentOrderCacheService")
public class PaymentOrderCacheServiceImpl extends PaymentOrderServiceImpl implements PaymentOrderCacheService {
	
	private static final Log logger = LogFactory.getLog(PaymentOrderCacheServiceImpl.class);
	
	 
	@Autowired
	private StringRedisTemplate redisTemplate;
	

	@Override
	public void taskInQueue(PaymentOrder task, String tableName, Integer queueIndex) {
		//save in db
		super.taskInQueue(task, tableName, queueIndex);
		
		//save in cache
		try {
			String cacheKey = getWaitingPayCacheKey(tableName, queueIndex);
			
			this.pushTaskToCache(cacheKey, task);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("occur error when taskInQueue in cache!");
		}
	}


//	private void pushTaskListToCache(String tableName, List<PaymentOrder> taskList) {
//		
//		if (CollectionUtils.isEmpty(taskList)) {
//			return;
//		}
//		for (PaymentOrder paymentOrder : taskList) {
//			//consistent hash
//			String cacheKey = getWaitingPayCacheKey(tableName, paymentOrder);
//
//			this.pushTaskToCache(cacheKey, paymentOrder);
//		}
//	}


	@Override
	public void pushTaskToCache(String cacheKey, PaymentOrder task) {
		
		BoundListOperations<String, String> ops = redisTemplate.boundListOps(cacheKey);
		
		ops.rightPush(JsonUtils.toJsonString(task));

	}
	
	
	@Override
	public List<PaymentOrder> peekIdleTasks(String tableName, int limit, int workerNo) {
		try {
			String cacheKey = tableName + WAITING_PAY_QUEUE_CACHE_KEY_SUFFIX + workerNo;
			List<PaymentOrder> idelTasksInCache = this.peekIdleTasksInCache(cacheKey);
			if(! CollectionUtils.isEmpty(idelTasksInCache)) {
				return idelTasksInCache;
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("occur error when peekIdleTasks in cache!");
		}

		return super.peekIdleTasks(tableName, limit, workerNo);
	}


	@Override
	public List<PaymentOrder> peekIdleTasksInCache(String cacheKey) {
		
        List<String> taskStrList = redisTemplate.opsForList().range(cacheKey, 0, -1);

        List<PaymentOrder> idleTasksList = new ArrayList<>(taskStrList.size());
        if (null != taskStrList && taskStrList.size() > 0) {
            for (String taskStr : taskStrList) {
            	idleTasksList.add(JSON.parseObject(taskStr, PaymentOrder.class));
            	//delete
            	deleteTaskInCache(cacheKey, taskStr);
            }
        }
        return idleTasksList;
	}

	private void deleteTaskInCache(String cacheKey, String taskStr) {
		
		BoundListOperations<String, String> ops = redisTemplate.boundListOps(cacheKey);
		
		ops.remove(0, taskStr);
	}

	@Override
	public void deleteTaskInCache(String cacheKey, PaymentOrder task) {
		
		deleteTaskInCache(cacheKey, JsonUtils.toJsonString(task));
	}
	
	
	@Override
	public boolean atomFeedBack(int nthSlot, PaymentOrder dstPaymentOrder,
			int tryTimes, String tableName) {
		// push to waiting query cache queue
		try {
			String cacheKey = getWaitingQueryCacheKey(tableName, dstPaymentOrder);
			this.pushTaskToCache(cacheKey, dstPaymentOrder);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("occur error when push to waiting query cache queue during atomFeedBack!");
		}
		
		//update in db
		return super.atomFeedBack(nthSlot, dstPaymentOrder, tryTimes, tableName);

	}
	
	@Override
	public List<PaymentOrder> getReadyTasksForUpdateBusinessStatus(String queueTableName, int workerNo) {
		try {
			String cacheKey = queueTableName + WAITING_QUERY_QUEUE_CACHE_KEY_SUFFIX + workerNo;
			List<PaymentOrder> readyTasksInCache = this.peekReadyTasksInCache(cacheKey);
			if(! CollectionUtils.isEmpty(readyTasksInCache)) {
				return readyTasksInCache;
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("occur error when getReadyTasksForUpdateBusinessStatus in cache!");
		}

		return super.getReadyTasksForUpdateBusinessStatus(queueTableName, workerNo);
	}
	

	@Override
	public List<PaymentOrder> peekReadyTasksInCache(String cacheKey) {
		 List<String> taskStrList = redisTemplate.opsForList().range(cacheKey, 0, -1);

	        List<PaymentOrder> readyTasksList = new ArrayList<>(taskStrList.size());
	        if (null != taskStrList && taskStrList.size() > 0) {
	            for (String taskStr : taskStrList) {
	            	readyTasksList.add(JSON.parseObject(taskStr, PaymentOrder.class));
	            }
	        }
	        return readyTasksList;
	}
	
	@Override
	public boolean updateBusinessStatus(PaymentOrder dstPaymentOrder,
			QueryCreditResult queryCreditResult, int tryTimes, String tableName) {
		// remove from cache
		try {
			//String cacheKey = getWaitingQueryCacheKey(tableName);
			String cacheKey = getWaitingQueryCacheKey(tableName, dstPaymentOrder);
			this.deleteTaskInCache(cacheKey, dstPaymentOrder);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("occur error when remove task in cache during updateBusinessStatus!");
		}
		//update status in db
		return super.updateBusinessStatus(dstPaymentOrder, queryCreditResult, tryTimes, tableName);
	}
	

	private String getWaitingPayCacheKey(String tableName, Integer queueIndex) {
		
		return tableName + WAITING_PAY_QUEUE_CACHE_KEY_SUFFIX + queueIndex;
	}
	
	private String getWaitingQueryCacheKey(String tableName, PaymentOrder paymentOrder) {
		//String node = cachedTaskSource.getNode(paymentOrder.getUuid());
		
		return tableName + WAITING_QUERY_QUEUE_CACHE_KEY_SUFFIX + paymentOrder.getLongFieldOne();
	}
	
//	 @PostConstruct
//	 public void init(){
//		 this.cachedTaskSource = new ConsistentHash<>();
//	 }

}
