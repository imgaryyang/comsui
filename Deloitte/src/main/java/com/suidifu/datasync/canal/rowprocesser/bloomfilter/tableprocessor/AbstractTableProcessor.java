package com.suidifu.datasync.canal.rowprocesser.bloomfilter.tableprocessor;

import java.util.List;

import com.alibaba.otter.canal.protocol.CanalEntry.Column;
import com.suidifu.datasync.canal.rowprocesser.TableProcessor;

public abstract class AbstractTableProcessor implements TableProcessor {

	@Override
	public void onInsert(List<Column> afterColumnsList) {

	}

	@Override
	public void onUpdate(List<Column> beforeColumnsList, List<Column> afterColumnsList) {

	}

	@Override
	public void onDelete(List<Column> beforeColumnsList) {

	}

}
