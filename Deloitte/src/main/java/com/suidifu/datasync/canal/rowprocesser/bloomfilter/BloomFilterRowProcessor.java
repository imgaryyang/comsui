package com.suidifu.datasync.canal.rowprocesser.bloomfilter;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.otter.canal.protocol.CanalEntry.Column;
import com.alibaba.otter.canal.protocol.CanalEntry.EventType;
import com.suidifu.datasync.canal.StaticsConfig.TABLE;
import com.suidifu.datasync.canal.rowprocesser.redis.RedisRowProcessor;

import orestes.bloomfilter.CountingBloomFilter;
import orestes.bloomfilter.FilterBuilder;

/**
 * BloomFilterRowProcessor
 * 
 * @author lisf
 *
 */
@Component("bloomFilterRowProcessor")
public class BloomFilterRowProcessor extends RedisRowProcessor {

	private static final Logger LOG = LoggerFactory.getLogger(BloomFilterRowProcessor.class);
	private static FilterBuilder filterBuilder;

	@Value("${spring.redis.host}")
	private String redisHost;
	@Value("${spring.redis.port}")
	private int redisPort;

	@Override
	public void init() {
		super.init();
		if (filterBuilder == null)
			filterBuilder = new FilterBuilder(100_0000, 0.01).redisBacked(true).redisHost(redisHost).redisPort(redisPort);
	}

	private CountingBloomFilter<String> __bloomFilter(String filterName) {
		return filterBuilder.name(filterName).buildCountingBloomFilter();
	}

	@Override
	public void pushToRedis(String tableName, List<Column> beforeColumnsList, List<Column> afterColumnsList, EventType eventType) {
		if (EventType.DELETE == eventType) {
			String element_before = filterMap.get(tableName).bloomfilterElement(beforeColumnsList, eventType);
			pushToRedis(tableName, eventType, element_before, null);
			return;
		}
		String element_after = filterMap.get(tableName).bloomfilterElement(afterColumnsList, eventType);
		if (EventType.UPDATE == eventType) {
			String element_before = filterMap.get(tableName).bloomfilterElement(beforeColumnsList, eventType);
			pushToRedis(tableName, eventType, element_before, element_after);
			return;
		}
		pushToRedis(tableName, eventType, null, element_after);
	}

	public void pushToRedis(String tableName, EventType eventType, String element_before, String element_after) {
		LOG.info(tableName + "：" + eventType + "<<<<<element_after<<<<<" + element_after + "<<<<<element_before<<<<<" + element_before);
		if (EventType.INSERT == eventType) {
			this.add(tableName, element_after);
			return;
		}
		if (EventType.DELETE == eventType) {
			this.remove(tableName, element_before);
			return;
		}
		if (EventType.UPDATE == eventType) {
			this.remove(tableName, element_before);
			this.add(tableName, element_after);
			return;
		}
	}

	public void add(TABLE table, String element) {
		this.add(table.name(), element);
	}

	public void add(String filterName, String element) {
		__bloomFilter(filterName).add(element);
	}

	public void remove(TABLE table, String element) {
		this.remove(table.name(), element);
	}

	public void remove(String filterName, String element) {
		__bloomFilter(filterName).remove(element);
	}

	public void remove(TABLE table) {
		this.remove(table.name());
	}

	public void remove(String filterName) {
		__bloomFilter(filterName).remove();
	}

	public void clear(TABLE table) {
		this.clear(table.name());
	}

	public void clear(String filterName) {
		__bloomFilter(filterName).clear();
	}
}
