package com.suidifu.munichre.config;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.suidifu.mq.config.RabbitMqConfig;
import com.suidifu.mq.producer.simple.SimpleCommonProducer;

@Component("mqMunichreProducer")
public class MqProducer extends SimpleCommonProducer {

	@Resource(name = "cfg_munichre_mq_producer")
	private RabbitMqConfig rabbitMqConfig;

	@Override
	protected RabbitMqConfig getRabbitMqConfig() {
		return rabbitMqConfig;
	}

}
