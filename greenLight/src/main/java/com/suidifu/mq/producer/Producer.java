package com.suidifu.mq.producer;

import org.springframework.amqp.rabbit.core.RabbitTemplate;

import com.suidifu.mq.config.RabbitMqQueueConfig;
import com.suidifu.mq.rpc.CodecType;
import com.suidifu.mq.rpc.RpcClient;

public interface Producer {

	/**
	 * RpcClient
	 * 
	 * @return
	 */
	public RpcClient rpc();

	/**
	 * RpcClient
	 * 
	 * @param codecType
	 * @return
	 */
	public RpcClient rpc(CodecType codecType);

	/**
	 * 生产者RabbitTemplate
	 * 
	 * @param queueConfig
	 * @return
	 */
	public abstract RabbitTemplate getRabbitTemplate(RabbitMqQueueConfig queueConfig);
}
