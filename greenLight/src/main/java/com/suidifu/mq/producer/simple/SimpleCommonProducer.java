package com.suidifu.mq.producer.simple;

import com.suidifu.mq.config.RabbitMqQueueConfig;
import com.suidifu.mq.producer.producerworker.CommonProducer;

public abstract class SimpleCommonProducer extends CommonProducer {

	@Override
    protected RabbitMqQueueConfig commonConfig() {
		return getCommonConfig();
	}

}
