package com.suidifu.datasync.canal.rowprocesser.redis;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.suidifu.datasync.canal.StaticsConfig;
import com.suidifu.datasync.canal.StaticsConfig.EXEC_CASHFLOW_TBS;
import com.suidifu.datasync.canal.StaticsConfig.Event;
import com.suidifu.datasync.canal.StaticsConfig.REFUND_CASHFLOW_TBS;
import com.suidifu.datasync.canal.StaticsConfig.RemittanceType;
import com.suidifu.datasync.canal.rowprocesser.redis.domain.Result;
import com.suidifu.datasync.canal.rowprocesser.redis.domain.ResultEvent;

/**
 * 模拟事物回滚，保持数据一致性
 * 
 * @author lisf
 *
 */
@Component("RollbackData")
public class RollbackData {

	private final ConcurrentHashMap<String, String> map = new ConcurrentHashMap<String, String>();
	@Autowired
	private StringRedisTemplate redistemp;

	private String record_key = null;
	private String now_key = null;
	private Set<Object> record_keys = null;
	private Object old_value = null;
	private String last_clear = null;

	/**
	 * 恢复各个hashKey到第一次改动前的状态
	 * 
	 * @param batchId
	 * @param size
	 */
	public void rollback(long batchId, int size) {
		try {
			if (batchId == -1 || size == 0)
				return;
			last_clear = redistemp.opsForValue().get(StaticsConfig.LAST_STATUS);
			if (last_clear == null || "4".equals(last_clear)) {
				return;
			}
			if ("3".equals(last_clear)) {
				clear(batchId, size);
				return;
			}
			for (EXEC_CASHFLOW_TBS exec_cashflow_tb : EXEC_CASHFLOW_TBS.values()) {
				record_key = RemittanceType.贷.recordRedisKey(exec_cashflow_tb);
				now_key = RemittanceType.贷.redisKey(exec_cashflow_tb);
				_rollback(record_key, now_key, exec_cashflow_tb == EXEC_CASHFLOW_TBS.execid_result, RemittanceType.贷);
			}
			for (REFUND_CASHFLOW_TBS refund_cashflow_tb : REFUND_CASHFLOW_TBS.values()) {
				record_key = RemittanceType.借.recordRedisKey(refund_cashflow_tb);
				now_key = RemittanceType.借.redisKey(refund_cashflow_tb);
				_rollback(record_key, now_key, refund_cashflow_tb == REFUND_CASHFLOW_TBS.refundid_result, RemittanceType.借);
			}
		} finally {
			redistemp.opsForValue().set(StaticsConfig.LAST_STATUS, "1");
			record_key = null;
			now_key = null;
			old_value = null;
			record_keys = null;
			map.clear();
		}
	}

	/**
	 * 批次执行成功清除记录
	 * 
	 * @param batchId
	 * @param size
	 */
	public void clear(long batchId, int size) {
		if (batchId == -1 || size == 0)
			return;
		redistemp.opsForValue().set(StaticsConfig.LAST_STATUS, "3");
		for (EXEC_CASHFLOW_TBS exec_cashflow_tb : EXEC_CASHFLOW_TBS.values())
			redistemp.delete(RemittanceType.贷.recordRedisKey(exec_cashflow_tb));
		for (REFUND_CASHFLOW_TBS refund_cashflow_tb : REFUND_CASHFLOW_TBS.values())
			redistemp.delete(RemittanceType.借.recordRedisKey(refund_cashflow_tb));
		redistemp.opsForValue().set(StaticsConfig.LAST_STATUS, "4");
	}

	/**
	 * 存储更新数据时调用
	 * 
	 * @param key
	 * @param hashKey
	 * @param hashValue
	 */
	public void put(String key, String hashKey, String hashValue) {
		record_data(key, hashKey);
		redistemp.opsForHash().put(key, hashKey, hashValue);
	}

	/**
	 * 删除数据时调用
	 * 
	 * @param key
	 * @param hashKey
	 */
	public void delete(String key, String hashKey) {
		record_data(key, hashKey);
		redistemp.opsForHash().delete(key, hashKey);
	}

	private void _rollback(String record_key, String now_key, boolean tb_result, RemittanceType remittanceType) {
		record_keys = this.redistemp.opsForHash().keys(record_key);
		if (record_keys == null || record_keys.size() == 0)
			return;
		for (Object hashKey : record_keys) {
			if (hashKey == null)
				continue;
			old_value = this.redistemp.opsForHash().get(record_key, hashKey);
			if (old_value.toString().length() == 0) {
				this.redistemp.opsForHash().delete(now_key, hashKey);
				if (tb_result)
					result_to_queue(remittanceType, hashKey.toString(), null, Event.删除);
			} else {
				this.redistemp.opsForHash().put(now_key, hashKey, old_value);
				if (tb_result)
					result_to_queue(remittanceType, hashKey.toString(), JSON.parseObject(old_value.toString(), Result.class), Event.添加或更新);
			}
			this.redistemp.opsForHash().delete(record_key, hashKey);
		}
		record_keys.clear();
	}

	private void result_to_queue(RemittanceType remittanceType, String hashKey, Result result, Event event) {
		redistemp.opsForList().rightPush(remittanceType.getNode(hashKey), JSON.toJSONString(new ResultEvent(remittanceType, hashKey, event.code(), result)));
	}

	/**
	 * 只记录更改数据的第一次记录
	 * 
	 * @param key
	 * @param hashKey
	 */
	private void record_data(String key, String hashKey) {
		String record_key = _record_key(key);
		if (map.containsKey(record_key + hashKey))
			return;
		map.put(record_key + hashKey, StaticsConfig.EMPTY);
		old_value = redistemp.opsForHash().get(key, hashKey);
		redistemp.opsForHash().put(record_key, hashKey, old_value == null ? StaticsConfig.EMPTY : old_value);
	}

	private String _record_key(String key) {
		return StaticsConfig.ROLLBACK_UNACK + ":" + key;
	}
}
