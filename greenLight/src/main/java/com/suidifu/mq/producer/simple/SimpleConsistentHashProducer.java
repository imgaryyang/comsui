package com.suidifu.mq.producer.simple;

import java.util.List;

import com.suidifu.mq.config.RabbitMqQueueConfig;
import com.suidifu.mq.producer.producerworker.ConsistentHashProducer;

public abstract class SimpleConsistentHashProducer extends ConsistentHashProducer {

	@Override
    protected final List<RabbitMqQueueConfig> consistentHashConfigList() {
		return getConsistentHashConfigList();
	}

}
