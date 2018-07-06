package com.zufangbao.canal.core.rowprocesser;

import java.util.List;

import com.alibaba.otter.canal.protocol.CanalEntry.Column;
import com.alibaba.otter.canal.protocol.CanalEntry.EventType;

/**
 * RowProcessor
 * 
 * @author lisf
 *
 */
public abstract class RowProcessor {

	protected String[] table_arr;

	public abstract void init();

	public abstract void process(String tableName, List<Column> beforeColumnsList, List<Column> afterColumnsList, EventType eventType);

	public int table(String tableName) {
		int i = 0;
		for (String table : this.table_arr) {
			if (table.equalsIgnoreCase(tableName))
				return i;
			i++;
		}
		return -1;
	}
}
