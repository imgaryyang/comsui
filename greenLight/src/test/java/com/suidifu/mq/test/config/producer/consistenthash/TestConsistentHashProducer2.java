package com.suidifu.mq.test.config.producer.consistenthash;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.suidifu.mq.config.RabbitMqConfig;
import com.suidifu.mq.producer.simple.SimpleConsistentHashProducer;

@Component
public class TestConsistentHashProducer2 extends SimpleConsistentHashProducer {

	@Resource(name = "cfg_consistenthash_producer")
	private RabbitMqConfig rabbitMqConfig;

	@Override
	protected RabbitMqConfig getRabbitMqConfig() {
		return rabbitMqConfig;
	}

}
