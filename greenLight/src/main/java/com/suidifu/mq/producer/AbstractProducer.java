package com.suidifu.mq.producer;

import org.springframework.amqp.rabbit.core.RabbitTemplate;

import com.suidifu.mq.MqWorkerFactory;
import com.suidifu.mq.config.RabbitMqQueueConfig;
import com.suidifu.mq.util.MqUtil;

public abstract class AbstractProducer extends MqWorkerFactory implements Producer {

	/**
	 * 初始化RabbitTemplate
	 */
	@Override
    public RabbitTemplate getRabbitTemplate(RabbitMqQueueConfig queueConfig) {
		return MqUtil.getSimpleProducerRabbitTemplate(this.connectionFactory, queueConfig);
	}

}
