package com.suidifu.datasync.canal.rowprocesser.redis;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Component;

import com.alibaba.otter.canal.protocol.CanalEntry.Column;
import com.alibaba.otter.canal.protocol.CanalEntry.EventType;
import com.suidifu.datasync.canal.RowProcessor;
import com.suidifu.datasync.canal.StaticsConfig.TABLE;
import com.suidifu.datasync.canal.rowprocesser.TableProcessor;
import com.suidifu.datasync.util.SpringContextUtil;

/**
 * RedisRowProcessor
 * 
 * @author lisf
 *
 */
@Component("redisRowProcessor")
public class RedisRowProcessor extends RowProcessor {

	protected static HashMap<String, TableProcessor> filterMap;

	@Override
	public void init() {
		filterMap = new HashMap<String, TableProcessor>();
		for (TABLE table : TABLE.values())
			filterMap.put(table.name(), SpringContextUtil.getBean(table.name(), TableProcessor.class));
	}

	@Override
	public void pushToRedis(String tableName, List<Column> beforeColumnsList, List<Column> afterColumnsList, EventType eventType) {
		if (EventType.INSERT == eventType) {
			filterMap.get(tableName).onInsert(afterColumnsList);
			return;
		}
		if (EventType.UPDATE == eventType) {
			filterMap.get(tableName).onUpdate(beforeColumnsList, afterColumnsList);
			return;
		}
		if (EventType.DELETE == eventType) {
			filterMap.get(tableName).onDelete(beforeColumnsList);
			return;
		}
	}

}
