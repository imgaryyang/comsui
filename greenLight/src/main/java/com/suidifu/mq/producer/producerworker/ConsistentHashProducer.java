package com.suidifu.mq.producer.producerworker;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import com.suidifu.mq.config.RabbitMqQueueConfig;
import com.suidifu.mq.hash.ConsistentHash;
import com.suidifu.mq.hash.HashFunction;
import com.suidifu.mq.hash.hashfunction.MurmurHashFunction;
import com.suidifu.mq.producer.AbstractProducer;
import com.suidifu.mq.rpc.CodecType;
import com.suidifu.mq.rpc.client.ConsistentHashRPCClient;

/**
 * ConsistentHash生产者
 *
 * @author lisf
 *
 */
public abstract class ConsistentHashProducer extends AbstractProducer {
	private static final Log LOG = LogFactory.getLog(ConsistentHashProducer.class);
	protected List<RabbitTemplate> rabbitTemplateList;
	protected ConsistentHash consistentHash;
	protected abstract List<RabbitMqQueueConfig> consistentHashConfigList();
	/**
	 * 注册registerMqService
	 */
	@Override
	public void registerMqService() {
		List<RabbitMqQueueConfig> queueConfigList = consistentHashConfigList();
		this.rabbitTemplateList = new ArrayList<RabbitTemplate>(queueConfigList.size());
		RabbitAdmin _rabbitAdmin = new RabbitAdmin(this.connectionFactory);
		for (RabbitMqQueueConfig queueConfig : queueConfigList) {
			declareQueueBinding(_rabbitAdmin, queueConfig);
			RabbitTemplate rabbitTemplate = getRabbitTemplate(queueConfig);
			//ProducerMessageManager producerMessageManager = getMessageManager(getMessageListenerContainer(rabbitTemplate, queueConfig));
			//producerMessageManager.start();
			this.rabbitTemplateList.add(rabbitTemplate);
			LOG.info("正常启动Producer：" + queueConfig.getRequestQueueName());
		}
		this.consistentHash = new ConsistentHash(this.rabbitTemplateList, hashFunction());
	}

	@Override
    public ConsistentHashRPCClient rpc() {
		return rpc(CodecType.KYRO);
	}

	@Override
    public ConsistentHashRPCClient rpc(CodecType codecType) {
		return new ConsistentHashRPCClient(codecType, this.consistentHash);
	}

	/**
	 * 参考Md5HashFunction,MurmurHashFunction
	 *
	 * @return
	 */
	protected HashFunction hashFunction() {
		return new MurmurHashFunction();
	}
}
