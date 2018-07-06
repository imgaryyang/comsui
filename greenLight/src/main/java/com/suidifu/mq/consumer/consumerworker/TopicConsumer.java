package com.suidifu.mq.consumer.consumerworker;

import java.util.List;

import org.springframework.amqp.rabbit.core.RabbitAdmin;

import com.suidifu.mq.config.RabbitMqQueueConfig;
import com.suidifu.mq.consumer.AbstractConsumer;
import com.suidifu.mq.consumer.ConsumerMessageManager;
import com.suidifu.mq.util.MqUtil;

public abstract class TopicConsumer extends AbstractConsumer {

	protected RabbitAdmin rabbitAdmin;

	protected abstract List<RabbitMqQueueConfig> topicConfigList();

	@Override
	protected void declareQueueBinding(RabbitAdmin rabbitAdmin, RabbitMqQueueConfig queueConfig) {
		MqUtil.topicExchange(rabbitAdmin, queueConfig);
	}

	@Override
	public final void registerMqService() {
		List<RabbitMqQueueConfig> queueConfigList = topicConfigList();
		this.rabbitAdmin = new RabbitAdmin(this.connectionFactory);
		for (RabbitMqQueueConfig queueConfig : queueConfigList) {
			declareQueueBinding(this.rabbitAdmin, queueConfig);
			ConsumerMessageManager consumerMessageManager = getMessageManager(getMessageListenerContainer(queueConfig));
			consumerMessageManager.start();
		}
	}
}
