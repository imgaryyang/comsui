package com.suidifu.mq.producer.simple;

import java.util.List;

import com.suidifu.mq.config.RabbitMqQueueConfig;
import com.suidifu.mq.producer.producerworker.TopicProducer;

/**
 * 例serviceName=table1.*,table2.*|table2.*,table4.*会产生2个队列,
 * topicProducer.rpc().routingKey("table2.update").sendAsync..会同时往2个队列发数据
 * 
 * @author lisf
 *
 */
public abstract class SimpleTopicProducer extends TopicProducer {

	@Override
	protected List<RabbitMqQueueConfig> topicConfigList() {
		return getTopicConfigList();
	}

}
