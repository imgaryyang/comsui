package com.suidifu.munichre.test.config;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.suidifu.mq.config.RabbitMqConfig;
import com.suidifu.mq.consumer.messagehandler.MessageHandler;
import com.suidifu.mq.consumer.simple.SimpleCommonConsumer;

@Component("mqMunichreConsumer")
public class MqConsumer extends SimpleCommonConsumer {

	@Resource(name = "cfg_munichre_mq_consumer")
	private RabbitMqConfig rabbitMqConfig;

	@Resource(name = "mqMunichreMessageHandler")
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
