package com.zufangbao.canal.core.rowprocesser.event;

import java.math.BigDecimal;
import java.util.List;

import com.alibaba.otter.canal.protocol.CanalEntry.Column;
import com.alibaba.otter.canal.protocol.CanalEntry.EventType;

/**
 * AbstractRowEvents
 * 
 * @author lisf
 *
 */
public abstract class AbstractRowEvents implements RowEvents {

	@Override
	public String bloomfilterElement(List<Column> columns, EventType eventType) {
		return null;
	}

	protected int getInteger(String s) {
		return (s == null || s.trim().length() == 0) ? -1 : Integer.parseInt(s);
	}

	protected BigDecimal getBigDecimal(String s) {
		return (s == null || s.trim().length() == 0) ? null : new BigDecimal(s);
	}
}
