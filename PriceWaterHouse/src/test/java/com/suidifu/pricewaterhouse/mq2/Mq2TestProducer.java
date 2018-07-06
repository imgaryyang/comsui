package com.suidifu.pricewaterhouse.mq2;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.suidifu.mq.config.RabbitMqConfig;
import com.suidifu.mq.producer.simple.SimpleConsistentHashProducer;

@Component
public class Mq2TestProducer extends SimpleConsistentHashProducer {

	@Resource(name = "cfg_rabbitmq_producer")
	private RabbitMqConfig rabbitMqConfig;

	@Override
	protected RabbitMqConfig getRabbitMqConfig() {
		return rabbitMqConfig;
	}

}
