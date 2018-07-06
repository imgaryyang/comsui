package com.suidifu.mq.producer.producerworker;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import com.suidifu.mq.config.RabbitMqQueueConfig;
import com.suidifu.mq.producer.AbstractProducer;
import com.suidifu.mq.rpc.CodecType;
import com.suidifu.mq.rpc.client.TopicRPCClient;
import com.suidifu.mq.util.MqUtil;

public abstract class TopicProducer extends AbstractProducer {
	private static Log LOG = LogFactory.getLog(TopicProducer.class);

	private RabbitTemplate rabbitTemplate;

	protected abstract List<RabbitMqQueueConfig> topicConfigList();

	@Override
	protected void declareQueueBinding(RabbitAdmin rabbitAdmin, RabbitMqQueueConfig queueConfig) {
		MqUtil.topicExchange(rabbitAdmin, queueConfig);
	}

	@Override
	protected void registerMqService() {
		List<RabbitMqQueueConfig> queueConfigList = topicConfigList();
		this.rabbitTemplate = getRabbitTemplate(queueConfigList.get(0));
		this.rabbitTemplate.setQueue(null);
		RabbitAdmin _rabbitAdmin = new RabbitAdmin(this.connectionFactory);
		for (RabbitMqQueueConfig queueConfig : queueConfigList) {
			declareQueueBinding(_rabbitAdmin, queueConfig);
			LOG.info("正常启动Topic Producer：" + queueConfig.getRequestQueueName());
		}
	}

	@Override
    public TopicRPCClient rpc() {
		return rpc(CodecType.KYRO);
	}

	@Override
    public TopicRPCClient rpc(CodecType codecType) {
		return new TopicRPCClient(codecType, this.rabbitTemplate);
	}

}
