package com.suidifu.mq.consumer;

import org.springframework.amqp.rabbit.listener.AbstractMessageListenerContainer;

import com.suidifu.mq.config.RabbitMqQueueConfig;
import com.suidifu.mq.consumer.messagehandler.MessageHandler;

public interface Consumer {
	/**
	 * 消息处理
	 * 
	 * @return
	 */
	public MessageHandler messageHandler();

	/**
	 * 消息容器管理
	 * 
	 * @param container
	 * @return
	 */
	public ConsumerMessageManager getMessageManager(AbstractMessageListenerContainer container);

	/**
	 * AbstractMessageListenerContainer
	 * 
	 * @param queueConfig
	 * @return
	 */
	public AbstractMessageListenerContainer getMessageListenerContainer(RabbitMqQueueConfig queueConfig);
}
