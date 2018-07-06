package com.suidifu.mq.test.config.producer.common;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.suidifu.mq.config.RabbitMqConfig;
import com.suidifu.mq.producer.simple.SimpleCommonProducer;

@Component
public class TestCommonProducer2 extends SimpleCommonProducer {

	@Resource(name = "cfg_common_producer")
	private RabbitMqConfig rabbitMqConfig;

	@Override
	protected RabbitMqConfig getRabbitMqConfig() {
		return rabbitMqConfig;
	}

}
