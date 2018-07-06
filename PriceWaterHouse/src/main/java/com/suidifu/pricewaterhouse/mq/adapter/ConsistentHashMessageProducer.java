package com.suidifu.pricewaterhouse.mq.adapter;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.suidifu.mq.config.RabbitMqConfig;
import com.suidifu.mq.producer.simple.SimpleConsistentHashProducer;

@Component("messageProducer")
public class ConsistentHashMessageProducer extends SimpleConsistentHashProducer {
	@Resource(name = "cfg_rabbitmq_producer")
	private RabbitMqConfig rabbitMqConfig;

	@Override
	protected RabbitMqConfig getRabbitMqConfig() {
		return rabbitMqConfig;
	}
}
