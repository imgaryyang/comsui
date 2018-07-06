package com.suidifu.mq.consumer.simple;

import java.util.List;
import com.suidifu.mq.config.RabbitMqQueueConfig;
import com.suidifu.mq.consumer.consumerworker.ConsistentHashConsumer;

public abstract class SimpleConsistentHashConsumer extends ConsistentHashConsumer {

	@Override
    protected final List<RabbitMqQueueConfig> consistentHashConfigList() {
		return getConsistentHashConfigList();
	}

}
