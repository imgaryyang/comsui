package com.suidifu.mq.consumer.simple;

import java.util.List;

import com.suidifu.mq.config.RabbitMqQueueConfig;
import com.suidifu.mq.consumer.consumerworker.TopicConsumer;

public abstract class SimpleTopicConsumer extends TopicConsumer {

	@Override
	protected List<RabbitMqQueueConfig> topicConfigList() {
		return getTopicConfigList();
	}

}
