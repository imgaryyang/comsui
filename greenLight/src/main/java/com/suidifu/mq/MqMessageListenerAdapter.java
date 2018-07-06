package com.suidifu.mq;

import java.util.Map;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;

import com.rabbitmq.client.Channel;
import com.suidifu.mq.consumer.messagehandler.MessageHandler;

/**
 * 增加MessageProperties处理参数
 * 
 * @author lisf
 *
 */
public class MqMessageListenerAdapter extends MessageListenerAdapter {

	private MessageHandler messageHandler;
	// 区分timeout里的null
	private static final byte[] NULLOBJ = new byte[0];

	public MqMessageListenerAdapter(MessageHandler messageHandler) {
		super(NULLOBJ);
		this.messageHandler = messageHandler;
	}

	@Override
	public void onMessage(Message originalMessage, Channel channel) throws Exception {
		MessageProperties msgprops = originalMessage.getMessageProperties();
		Map<String, Object> msgpropheaders = msgprops.getHeaders();
		boolean sync = Boolean.valueOf(msgpropheaders.get("sync").toString());
		String result = this.messageHandler.handleMessage(extractMessage(originalMessage).toString(), msgprops.getConsumerQueue(), sync, Integer.valueOf(msgpropheaders.get("codec").toString()));
		if (!sync)
			return;
		handleResult(result, originalMessage, channel);
	}
}
