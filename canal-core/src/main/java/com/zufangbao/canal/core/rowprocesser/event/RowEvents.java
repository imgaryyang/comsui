package com.zufangbao.canal.core.rowprocesser.event;

import java.util.List;

import com.alibaba.otter.canal.protocol.CanalEntry.Column;
import com.alibaba.otter.canal.protocol.CanalEntry.EventType;

/**
 * RowEvents
 * 
 * @author lisf
 *
 */
public interface RowEvents {

	/**
	 * 数据库添加记录时触发
	 * 
	 * @param afterColumnsList
	 */
	public void onInsert(List<Column> afterColumnsList);

	/**
	 * 数据库更新记录时触发
	 * 
	 * @param beforeColumnsList
	 * @param afterColumnsList
	 */
	public void onUpdate(List<Column> beforeColumnsList, List<Column> afterColumnsList);

	/**
	 * 数据库删除记录时触发
	 * 
	 * @param beforeColumnsList
	 */
	public void onDelete(List<Column> beforeColumnsList);

	/**
	 * bloomfilterElement自定义事件实现
	 * 
	 * @param columns
	 * @param eventType
	 * @return
	 */
	public String bloomfilterElement(List<Column> columns, EventType eventType);

}
