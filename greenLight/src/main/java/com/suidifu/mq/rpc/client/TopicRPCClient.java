package com.suidifu.mq.rpc.client;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import com.suidifu.mq.rpc.CodecType;
import com.suidifu.mq.rpc.RpcClient;

/**
 * Topic模式RpcClient
 *
 * @author lisf
 *
 */
public class TopicRPCClient extends RpcClient {

	private RabbitTemplate rabbitTemplate;
	private String routingKey;

	public TopicRPCClient(CodecType codecTyep, RabbitTemplate rabbitTemplate) {
		super(codecTyep);
		this.rabbitTemplate = rabbitTemplate;
	}

	@Override
	public RabbitTemplate rabbitTemplate(String businessId) {
		return this.rabbitTemplate;
	}

	public TopicRPCClient routingKey(String routingKey) {
		this.routingKey = routingKey;
		return this;
	}

	@Override
    protected void convertAndSend(RabbitTemplate rabbitTemplate, Message buildMessage) {
		check();
		rabbitTemplate.convertAndSend(this.routingKey, buildMessage);
	}

	@Override
    protected Object convertSendAndReceive(RabbitTemplate rabbitTemplate, Message buildMessage) {
		check();
		return rabbitTemplate.convertSendAndReceive(this.routingKey, buildMessage);
	}

	private void check() {
		if (this.routingKey == null || this.routingKey.trim().length() == 0)
			throw new AmqpException("the topic routingKey must not be null");
	}

}
