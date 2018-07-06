package com.suidifu.mq.test.config.consumer.topic;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.suidifu.mq.config.RabbitMqConfig;
import com.suidifu.mq.consumer.messagehandler.MessageHandler;
import com.suidifu.mq.consumer.simple.SimpleTopicConsumer;
import com.suidifu.mq.test.config.messagehandler.StringMessageHandler;

@Component
public class TestTopicConsumer1 extends SimpleTopicConsumer {

	@Resource(name = "cfg_topic_consumer1")
	private RabbitMqConfig rabbitMqConfig;

	@Resource(name = "consumerMessageHandler")
	private StringMessageHandler messageHandler;

	@Override
	public RabbitMqConfig getRabbitMqConfig() {
		return rabbitMqConfig;
	}

	@Override
	public MessageHandler messageHandler() {
		return messageHandler;
	}

}
