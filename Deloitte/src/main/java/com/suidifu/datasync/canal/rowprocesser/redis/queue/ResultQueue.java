package com.suidifu.datasync.canal.rowprocesser.redis.queue;

import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.alibaba.fastjson.JSON;
import com.suidifu.datasync.canal.StaticsConfig.Event;
import com.suidifu.datasync.canal.StaticsConfig.RemittanceType;
import com.suidifu.datasync.canal.rowprocesser.redis.domain.ResultEvent;
import com.suidifu.datasync.repository.RemittanceAuditResultRepository;

/**
 * ResultQueue同步结果队列到mysql
 * 
 * @author lisf
 *
 */
public abstract class ResultQueue implements Runnable {
	private static final Logger LOG = LoggerFactory.getLogger(ResultQueue.class);
	private StringRedisTemplate redistemp;
	private RemittanceAuditResultRepository mysqlRepository;

	protected int queueNum;
	protected int batchSize;

	private ConcurrentHashMap<String, ResultEvent> resultkeyPopMap = null;
	private int popSize = 0;

	public ResultQueue(StringRedisTemplate redistemp, RemittanceAuditResultRepository mysqlRepository, int queueNum, int batchSize) {
		this.redistemp = redistemp;
		this.mysqlRepository = mysqlRepository;
		this.queueNum = queueNum;
		this.batchSize = batchSize;
		this.resultkeyPopMap = new ConcurrentHashMap<String, ResultEvent>(this.batchSize);
	}

	/**
	 * 队列处理
	 * 
	 * @param remittanceType
	 */
	protected void process(RemittanceType remittanceType) {
		String rediskey = null;
		int eventCode = Event.未知.code();
		ResultEvent resultEvent = null;
		try {
			range_batch_init(remittanceType);
			if (this.popSize == 0)
				return;
			for (Entry<String, ResultEvent> entry : this.resultkeyPopMap.entrySet()) {
				rediskey = entry.getKey();
				resultEvent = entry.getValue();
				eventCode = resultEvent.getEventCode();
				LOG.info(eventCode + ">>" + rediskey);
				if (Event.删除.eq(eventCode))
					mysqlRepository.delete(rediskey);
				else if (Event.添加或更新.eq(eventCode))
					mysqlRepository.saveOrUpdate(resultEvent.getResult());
			}
			pop_range(remittanceType);
		} catch (Exception e) {
			LOG.error("ResultQueue.Error", e);
			sleep(8);
		} finally {
			rediskey = null;
			resultEvent = null;
			this.resultkeyPopMap.clear();
		}
	}

	private void range_batch_init(RemittanceType remittanceType) {
		this.resultkeyPopMap.clear();
		List<String> keys = range_list(remittanceType);
		this.popSize = keys.size();
		for (String result_key_string : keys) {
			if (result_key_string == null)
				continue;
			initResultKeyPopMap(result_key_string);
		}
	}

	/**
	 * 批量同步数据合并，减少数据库压力
	 * 
	 * @param result_key_string
	 */
	private void initResultKeyPopMap(String result_key_string) {
		ResultEvent resultEvent = JSON.parseObject(result_key_string, ResultEvent.class);
		String rediskey = resultEvent.getRediskey();
		int eventCode = resultEvent.getEventCode();
		ResultEvent _resultEvent = this.resultkeyPopMap.get(rediskey);
		if (_resultEvent == null) {
			this.resultkeyPopMap.put(rediskey, resultEvent);
			return;
		}
		if (Event.删除.eq(eventCode) && Event.添加或更新.eq(_resultEvent.getEventCode())) {
			this.resultkeyPopMap.remove(rediskey);
			return;
		}
		this.resultkeyPopMap.put(rediskey, resultEvent);
	}

	private List<String> range_list(RemittanceType remittanceType) {
		return redistemp.opsForList().range(__queueKey(remittanceType), 0, this.batchSize - 1);
	}

	/**
	 * 移除当前批次
	 * 
	 * @param remittanceType
	 */
	private void pop_range(RemittanceType remittanceType) {
		for (int i = 0; i < this.popSize; i++)
			pop_first(remittanceType);
		this.popSize = 0;
	}

	private String pop_first(RemittanceType remittanceType) {
		return redistemp.opsForList().leftPop(__queueKey(remittanceType));
	}

	private String __queueKey(RemittanceType remittanceType) {
		return remittanceType.syncResultRedisKey(this.queueNum);
	}

	private void sleep(int time) {
		try {
			Thread.sleep(time * 1000);
		} catch (InterruptedException e) {
			LOG.warn("ResultQueue.Error", e);
		}
	}
}
