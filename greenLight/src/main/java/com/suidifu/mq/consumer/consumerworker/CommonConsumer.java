package com.suidifu.mq.consumer.consumerworker;

import org.springframework.amqp.rabbit.core.RabbitAdmin;

import com.suidifu.mq.config.RabbitMqQueueConfig;
import com.suidifu.mq.consumer.AbstractConsumer;
import com.suidifu.mq.consumer.ConsumerMessageManager;

/**
 * Common消费者
 * 
 * @author lisf
 *
 */
public abstract class CommonConsumer extends AbstractConsumer {

	protected RabbitAdmin rabbitAdmin;

	protected abstract RabbitMqQueueConfig commonConfig();

	/**
	 * 注册registerMqService
	 */
	@Override
	public final void registerMqService() {
		RabbitMqQueueConfig queueConfig = commonConfig();
		this.rabbitAdmin = new RabbitAdmin(this.connectionFactory);
		declareQueueBinding(this.rabbitAdmin, queueConfig);
		ConsumerMessageManager consumerMessageManager = getMessageManager(getMessageListenerContainer(queueConfig));
		consumerMessageManager.start();
	}

}
