/**
 * 
 */
package com.suidifu.berkshire.mq.receiver;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.suidifu.berkshire.mq.receiver.adapter.MqMessageReceiver;
import com.suidifu.mq.config.RabbitMqConfig;
import com.suidifu.mq.consumer.messagehandler.MessageHandler;
import com.suidifu.mq.consumer.simple.SimpleCommonConsumer;

/**
 * @author wukai
 *
 */
@Component
public class MunichReConsumer extends SimpleCommonConsumer {

	@Resource(name = "cfg_rabbitmq_consumer_munichre")
	private RabbitMqConfig rabbitMqConfig;
	
	@Autowired
	private MqMessageReceiver mqMessageReceiver;

	@Override
	public RabbitMqConfig getRabbitMqConfig() {
		return rabbitMqConfig;
	}

	@Override
	public MessageHandler messageHandler() {
		return mqMessageReceiver;
	}
}
