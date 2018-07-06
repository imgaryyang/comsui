package com.suidifu.mq.consumer.consumerworker;

import java.util.List;

import org.springframework.amqp.rabbit.core.RabbitAdmin;

import com.suidifu.mq.config.RabbitMqQueueConfig;
import com.suidifu.mq.consumer.AbstractConsumer;
import com.suidifu.mq.consumer.ConsumerMessageManager;

/**
 * ConsistentHash消费者
 * 
 * @author lisf
 *
 */
public abstract class ConsistentHashConsumer extends AbstractConsumer {
	protected String[] queueNames;
	protected RabbitAdmin rabbitAdmin;

	protected abstract List<RabbitMqQueueConfig> consistentHashConfigList();

	/**
	 * 注册registerMqService
	 */
	@Override
	public final void registerMqService() {
		List<RabbitMqQueueConfig> queueConfigList = consistentHashConfigList();
		this.rabbitAdmin = new RabbitAdmin(this.connectionFactory);
		this.queueNames = new String[2 * queueConfigList.size()];
		int i = 0;
		for (RabbitMqQueueConfig queueConfig : queueConfigList) {
			declareQueueBinding(this.rabbitAdmin, queueConfig);
			ConsumerMessageManager consumerMessageManager = getMessageManager(getMessageListenerContainer(queueConfig));
			consumerMessageManager.start();
			this.queueNames[i++] = queueConfig.getRequestQueueName();
			this.queueNames[i++] = queueConfig.getReplyQueueName();
		}
	}

	/**
	 * 删除队列
	 */
	public void deleteQueue(String... qnames) {
		String[] deletequeue = this.queueNames;
		if (qnames != null && qnames.length > 0)
			deletequeue = qnames;
		for (String queueName : deletequeue)
			this.rabbitAdmin.deleteQueue(queueName);
	}

	/**
	 * 清空队列数据
	 */
	public void purgeQueue(String... qnames) {
		String[] deletequeue = this.queueNames;
		if (qnames != null && qnames.length > 0)
			deletequeue = qnames;
		for (String queueName : deletequeue)
			this.rabbitAdmin.purgeQueue(queueName, Boolean.FALSE);
	}

}
