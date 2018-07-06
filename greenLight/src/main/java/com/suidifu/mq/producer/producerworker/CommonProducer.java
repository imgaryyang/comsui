package com.suidifu.mq.producer.producerworker;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import com.suidifu.mq.config.RabbitMqQueueConfig;
import com.suidifu.mq.producer.AbstractProducer;
import com.suidifu.mq.rpc.CodecType;
import com.suidifu.mq.rpc.client.CommonRPCClient;

/**
 * 多消费者模式的生产者
 *
 * @author lisf
 *
 */
public abstract class CommonProducer extends AbstractProducer {
	private static Log LOG = LogFactory.getLog(CommonProducer.class);
	protected RabbitTemplate rabbitTemplate;
	protected abstract RabbitMqQueueConfig commonConfig();
	/**
	 * 注册registerMqService
	 */
	@Override
	public void registerMqService() {
		RabbitMqQueueConfig queueConfig = commonConfig();
		this.rabbitTemplate = getRabbitTemplate(queueConfig);
		declareQueueBinding(new RabbitAdmin(this.connectionFactory), queueConfig);
		LOG.info("正常启动Producer：" + queueConfig.getRequestQueueName());
		//ProducerMessageManager producerMessageManager = getMessageManager(getMessageListenerContainer(rabbitTemplate, queueConfig));
		//producerMessageManager.start();
	}
	@Override
    public CommonRPCClient rpc() {
		return rpc(CodecType.KYRO);
	}

	@Override
    public CommonRPCClient rpc(CodecType codecType) {
		return new CommonRPCClient(codecType, this.rabbitTemplate);
	}
}
