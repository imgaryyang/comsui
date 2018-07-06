package com.suidifu.mq.test.config.consumer.consistenthash;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.suidifu.mq.config.RabbitMqConfig;
import com.suidifu.mq.consumer.messagehandler.MessageHandler;
import com.suidifu.mq.consumer.simple.SimpleConsistentHashConsumer;
import com.suidifu.mq.test.config.messagehandler.StringMessageHandler;

@Component
public class  TestConsistentHashConsumer1 extends SimpleConsistentHashConsumer {

	@Resource(name ="cfg_consistenthash_consumer1")
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
