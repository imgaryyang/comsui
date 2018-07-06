package com.suidifu.mq.rpc.client;

import org.springframework.amqp.rabbit.core.RabbitTemplate;

import com.suidifu.mq.rpc.CodecType;
import com.suidifu.mq.rpc.RpcClient;

/**
 * 普通模式RpcClient
 * 
 * @author lisf
 *
 */
public class CommonRPCClient extends RpcClient {

	private RabbitTemplate rabbitTemplate;

	public CommonRPCClient(CodecType codecTyep, RabbitTemplate rabbitTemplate) {
		super(codecTyep);
		this.rabbitTemplate = rabbitTemplate;
	}

	@Override
	public RabbitTemplate rabbitTemplate(String businessId) {
		return this.rabbitTemplate;
	}

}
