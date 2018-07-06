package com.suidifu.mq.test.config.consumer.common;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.suidifu.mq.config.RabbitMqConfig;
import com.suidifu.mq.consumer.messagehandler.MessageHandler;
import com.suidifu.mq.consumer.simple.SimpleCommonConsumer;
import com.suidifu.mq.test.config.messagehandler.StringMessageHandler;

@Component
public class TestCommonConsumer2 extends SimpleCommonConsumer {

	@Resource(name = "cfg_common_consumer")
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
