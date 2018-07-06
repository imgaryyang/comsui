package com.suidifu.datasync.canal;

import java.util.List;

import com.alibaba.otter.canal.protocol.CanalEntry.Column;
import com.alibaba.otter.canal.protocol.CanalEntry.EventType;
import com.suidifu.datasync.canal.StaticsConfig.TABLE;

/**
 * RowProcessor
 * 
 * @author lisf
 *
 */
public abstract class RowProcessor {

	public abstract void init();

	public abstract void pushToRedis(String tableName, List<Column> beforeColumnsList, List<Column> afterColumnsList, EventType eventType);

	public int table(String tableName) {
		for (TABLE table : TABLE.values()) {
			if (table.name().equalsIgnoreCase(tableName))
				return table.ordinal();
		}
		return -1;
	}
}
