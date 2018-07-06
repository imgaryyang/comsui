package com.suidifu.datasync.canal.rowprocesser.bloomfilter.tableprocessor;

import java.util.List;

import com.alibaba.otter.canal.protocol.CanalEntry.Column;
import com.alibaba.otter.canal.protocol.CanalEntry.EventType;

//@Component("cash_flow")
public class CashFlowFilter extends AbstractTableProcessor {
	@Override
	public String bloomfilterElement(List<Column> columns, EventType eventType) {
		StringBuilder element = new StringBuilder();
		try {
			for (Column column : columns) {
				String column_name = column.getName();
				String column_value = column.getValue();
				// cash_flow_uuid列作为element
				if ("cash_flow_uuid".equalsIgnoreCase(column_name)) {
					element.append(column_value);
					break;
				}
			}
			return element.toString();
		} finally {
			element.setLength(0);
			element = null;
		}
	}

}
