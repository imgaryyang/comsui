package com.suidifu.datasync.canal;

import java.net.InetSocketAddress;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.alibaba.otter.canal.protocol.CanalEntry.Entry;
import com.alibaba.otter.canal.protocol.CanalEntry.EntryType;
import com.alibaba.otter.canal.protocol.CanalEntry.EventType;
import com.alibaba.otter.canal.protocol.CanalEntry.RowChange;
import com.alibaba.otter.canal.protocol.CanalEntry.RowData;
import com.alibaba.otter.canal.protocol.Message;
import com.suidifu.datasync.canal.rowprocesser.redis.RollbackData;
import com.suidifu.datasync.canal.rowprocesser.redis.queue.ResultQueueFactory;
import com.suidifu.datasync.config.domain.CanalServerConfig;

/**
 * CanalClient监控数据库表更改
 * 
 * @author lisf
 *
 */
@Component
public class CanalClient implements ApplicationListener<ContextRefreshedEvent> {
	private static final Logger LOG = LoggerFactory.getLogger(CanalClient.class);

	@Resource(name = "cfg_canal")
	private CanalServerConfig canalServerConfig;

	@Resource(name = "redisRowProcessor")
	private RowProcessor rowProcesser;

	@Resource(name = "resultQueueFactory")
	private ResultQueueFactory resultQueueFactory;

	@Resource(name = "RollbackData")
	private RollbackData rollbackData;

	@Scheduled(fixedDelayString = "${canal.server.fixed-delay}")
	private void schedule() {
		CanalConnector connector = null;
		try {
			connector = CanalConnectors.newSingleConnector(new InetSocketAddress(canalServerConfig.getHost(), canalServerConfig.getPort()),
					canalServerConfig.getDestination(), canalServerConfig.getUsername(), canalServerConfig.getPassword());
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
				rollbackData.rollback(batchId, size);
				checkSize = entry(size, message.getEntries());
			}
			if (checkSize <= 0) {
				connector.ack(batchId);
				rollbackData.clear(batchId, size);
			} else {
				LOG.error("CanalClient.Error", "ack error!");
			}
		} catch (Exception e) {
			LOG.error("CanalClient.Error", e);
			sleep(8);
		} finally {
			if (connector != null)
				connector.disconnect();
		}
	}

	private void init() {
		resultQueueFactory.init();
		if (resultQueueFactory.hashNotNull())
			rowProcesser.init();
		else
			throw new RuntimeException("hash初始化失败！");
		LOG.info("CanalClient已启动....");
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
				rowProcesser.pushToRedis(tableName, rowData.getBeforeColumnsList(), rowData.getAfterColumnsList(), eventType);
			size--;
		}
		return size;
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		init();
	}

	private void sleep(int time) {
		try {
			Thread.sleep(time * 1000);
		} catch (InterruptedException e) {
			LOG.warn("CanalClient.Error", e);
		}
	}

}
