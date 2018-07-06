package com.suidifu.mq.consumer;

import org.springframework.amqp.rabbit.listener.AbstractMessageListenerContainer;

import com.suidifu.mq.MqWorkerFactory;
import com.suidifu.mq.config.RabbitMqQueueConfig;
import com.suidifu.mq.util.MqUtil;

/**
 * 
 * @author lisf
 *
 */
public abstract class AbstractConsumer extends MqWorkerFactory implements Consumer {

	@Override
	public ConsumerMessageManager getMessageManager(AbstractMessageListenerContainer container) {
		return new ConsumerMessageManager(container);
	}

	@Override
	public AbstractMessageListenerContainer getMessageListenerContainer(RabbitMqQueueConfig queueConfig) {
		return MqUtil.getSimpleConsumerMessageListenerContainer(this.connectionFactory, messageHandler(), queueConfig);
	}

}
