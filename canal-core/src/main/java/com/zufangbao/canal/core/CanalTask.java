package com.zufangbao.canal.core;

import java.net.InetSocketAddress;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.alibaba.otter.canal.protocol.CanalEntry.Entry;
import com.alibaba.otter.canal.protocol.CanalEntry.EntryType;
import com.alibaba.otter.canal.protocol.CanalEntry.EventType;
import com.alibaba.otter.canal.protocol.CanalEntry.RowChange;
import com.alibaba.otter.canal.protocol.CanalEntry.RowData;
import com.alibaba.otter.canal.protocol.Message;
import com.zufangbao.canal.core.bean.CanalServerConfig;
import com.zufangbao.canal.core.rowprocesser.RowProcessor;

@Component("canalTask")
public class CanalTask implements Runnable {

	private static final Logger LOG = LoggerFactory.getLogger(CanalTask.class);

	@Resource(name = "cfg_canal")
	private CanalServerConfig canalServerConfig;

	@Resource(name = "simpleRowProcessor")
	private RowProcessor rowProcesser;

	@Override
	public void run() {
		CanalConnector connector = null;
		try {
			connector = CanalConnectors.newSingleConnector(new InetSocketAddress(canalServerConfig.getHost(), canalServerConfig.getPort()), canalServerConfig.getDestination(), canalServerConfig.getUsername(), canalServerConfig.getPassword());
			connector.connect();
			connector.subscribe(canalServerConfig.getSubscribe());
			connector.rollback();
			Message message = connector.getWithoutAck(canalServerConfig.getBatchSize());
			long batchId = message.getId();
			int size = message.getEntries().size();
			int checkSize = 1;
			if (batchId == -1 || size == 0) {
				checkSize = 0;
				sleep(5);
			} else {
				checkSize = entry(size, message.getEntries());
			}
			if (checkSize <= 0) {
				connector.ack(batchId);
			} else {
				LOG.error("CanalTask.Error", "ack error!");
			}
		} catch (Exception e) {
			LOG.error("CanalTask.Error", e);
			sleep(8);
		} finally {
			if (connector != null)
				connector.disconnect();
		}
	}

	private int entry(int size, List<Entry> entrys) {
		for (Entry entry : entrys) {
			if (entry.getEntryType() == EntryType.TRANSACTIONBEGIN || entry.getEntryType() == EntryType.TRANSACTIONEND) {
				size--;
				continue;
			}
			RowChange rowChage = null;
			try {
				rowChage = RowChange.parseFrom(entry.getStoreValue());
			} catch (Exception e) {
				throw new RuntimeException("ERROR ## parser of eromanga-event has an error , data:" + entry.toString(), e);
			}
			// 单条 binlog sql
			String tableName = entry.getHeader().getTableName();
			int tableOrdinal = rowProcesser.table(tableName);
			if (tableOrdinal < 0) {
				size--;
				continue;
			}
			EventType eventType = rowChage.getEventType();
			if (EventType.INSERT != eventType && EventType.DELETE != eventType && EventType.UPDATE != eventType) {
				size--;
				continue;
			}
			// 受影响 数据行
			for (RowData rowData : rowChage.getRowDatasList())
				rowProcesser.process(tableName, rowData.getBeforeColumnsList(), rowData.getAfterColumnsList(), eventType);
			size--;
		}
		return size;
	}

	private void sleep(int time) {
		try {
			Thread.sleep(time * 1000);
		} catch (InterruptedException e) {
			LOG.warn("CanalTask.Error", e);
		}
	}
}
