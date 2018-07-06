package com.suidifu.mq.test.config.producer.topic;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.suidifu.mq.config.RabbitMqConfig;
import com.suidifu.mq.producer.simple.SimpleTopicProducer;

@Component
public class TestTopicProducer2 extends SimpleTopicProducer {

	@Resource(name = "cfg_topic_producer")
	private RabbitMqConfig rabbitMqConfig;

	@Override
	protected RabbitMqConfig getRabbitMqConfig() {
		return rabbitMqConfig;
	}

}
