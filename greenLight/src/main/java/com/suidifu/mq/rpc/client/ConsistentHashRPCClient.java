package com.suidifu.mq.rpc.client;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.amqp.rabbit.core.RabbitTemplate;

import com.suidifu.mq.hash.ConsistentHash;
import com.suidifu.mq.rpc.CodecType;
import com.suidifu.mq.rpc.RpcClient;

/**
 * consistentHash模式RPC
 *
 * @author lisf
 *
 */
public class ConsistentHashRPCClient extends RpcClient {

	private ConsistentHash consistentHash;

	public ConsistentHashRPCClient(ConsistentHash consistentHash) {
		this(CodecType.KYRO, consistentHash);
	}

	public ConsistentHashRPCClient(CodecType codecTyep, ConsistentHash consistentHash) {
		super(codecTyep);
		this.consistentHash = consistentHash;
	}

	/**
	 * 相同业务进入同一个队列处理
	 *
	 * @return
	 */
	@Override
    public RabbitTemplate rabbitTemplate(String businessId) {
		RabbitTemplate rabbitTemplate = this.consistentHash.getNode(businessId);
		LOG.debug(businessId + ",进入队列：" + rabbitTemplate.getExchange());
		return rabbitTemplate;
	}

	public String selectQueue(String businessId) {
		return this.rabbitTemplate(businessId).getExchange();
	}

	public Collection<String> selectAllQueueIndex() {
		Collection<RabbitTemplate> queues = this.consistentHash.getAll();
		Collection<String> queueIndexes = queues.stream().map(template -> template.getExchange()).collect(Collectors.toList());
		return queueIndexes;
	}
}
