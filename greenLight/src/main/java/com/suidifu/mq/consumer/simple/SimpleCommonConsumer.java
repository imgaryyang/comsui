package com.suidifu.mq.consumer.simple;

import com.suidifu.mq.config.RabbitMqQueueConfig;
import com.suidifu.mq.consumer.consumerworker.CommonConsumer;

public abstract class SimpleCommonConsumer extends CommonConsumer {

	@Override
    protected RabbitMqQueueConfig commonConfig() {
		return getCommonConfig();
	}

}
