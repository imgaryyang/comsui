package com.zufangbao.canal.core.rowprocesser;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.alibaba.otter.canal.protocol.CanalEntry.Column;
import com.alibaba.otter.canal.protocol.CanalEntry.EventType;
import com.zufangbao.canal.core.bean.CanalServerConfig;
import com.zufangbao.canal.core.rowprocesser.event.RowEvents;

/**
 * SimpleRowProcessor
 * 
 * @author lisf
 *
 */
@Component("simpleRowProcessor")
public class SimpleRowProcessor extends RowProcessor {

	@Autowired
	private ApplicationContext context;

	@Resource(name = "cfg_canal")
	protected CanalServerConfig canalServerConfig;

	protected static HashMap<String, RowEvents> filterMap;

	@Override
	public void init() {
		this.table_arr = canalServerConfig.getSubscribe().split(",");
		for (int i = 0; i < this.table_arr.length; i++) {
			this.table_arr[i] = table_arr[i].split("\\.")[1];
		}
		filterMap = new HashMap<String, RowEvents>();
		for (String table : this.table_arr)
			filterMap.put(table, context.getBean(table, RowEvents.class));
	}

	@Override
	public void process(String tableName, List<Column> beforeColumnsList, List<Column> afterColumnsList, EventType eventType) {
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
