package com.suidifu.mq;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import com.suidifu.mq.config.RabbitMqConfig;
import com.suidifu.mq.config.RabbitMqQueueConfig;
import com.suidifu.mq.util.MqUtil;

public abstract class MqWorkerFactory implements ApplicationListener<ContextRefreshedEvent> {
	private static Log LOG = LogFactory.getLog(MqWorkerFactory.class);
	protected String serviceName;
	protected RabbitMqConfig rabbitMqConfig;
	protected ConnectionFactory connectionFactory;

	/**
	 * 注册Consumer or Producer服务
	 * 
	 * @param registerMqService
	 */
	protected abstract void registerMqService();

	/**
	 * 获取ConnectionFactory
	 * 
	 * @param rabbitMqConfig
	 * @return
	 */
	protected ConnectionFactory getConnectionFactory() {
		return getDefaultCachingConnectionFactory(this.rabbitMqConfig);
	}

	/**
	 * MQ配置文件
	 * 
	 * @return
	 */
	protected abstract RabbitMqConfig getRabbitMqConfig();

	/**
	 * 声明绑定队列
	 * 
	 * @param rabbitAdmin
	 * @param queueConfig
	 */
	protected void declareQueueBinding(RabbitAdmin rabbitAdmin, RabbitMqQueueConfig queueConfig) {
		MqUtil.fanoutExchange(rabbitAdmin, queueConfig);
	}

	/**
	 * 初始化
	 * 
	 * @throws Exception
	 */
	private final void init() throws Exception {
		this.rabbitMqConfig = getRabbitMqConfig();
		this.serviceName = this.rabbitMqConfig.getServiceName();
		this.connectionFactory = getConnectionFactory();
		registerMqService();
	}

	/**
	 * 容器启动后执行
	 */
	@Override
	public final void onApplicationEvent(ContextRefreshedEvent event) {
		if (event.getApplicationContext().getParent() == null) {
			try {
				this.init();
			} catch (Exception e) {
				LOG.error("MqWorkerFactory.onApplicationEvent.error", e);
			}
		}
	}

	/**
	 * 通用获取CachingConnectionFactory
	 * 
	 * @param getRabbitMqConfig
	 * @return
	 */
	public static final CachingConnectionFactory getDefaultCachingConnectionFactory(RabbitMqConfig rabbitMqConfig) {
		CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory(rabbitMqConfig.getHost(), rabbitMqConfig.getPort());
		cachingConnectionFactory.setUsername(rabbitMqConfig.getUserName());
		cachingConnectionFactory.setPassword(rabbitMqConfig.getPassword());
		cachingConnectionFactory.setRequestedHeartBeat(15);
		return cachingConnectionFactory;
	}

	/**
	 * ConsistentHash队列配置
	 * 
	 * @return
	 */
	protected final List<RabbitMqQueueConfig> getConsistentHashConfigList() {
		int start = this.rabbitMqConfig.getStart();
		int end = this.rabbitMqConfig.getEnd();
		List<RabbitMqQueueConfig> queueConfigList = new ArrayList<RabbitMqQueueConfig>(end - start + 1);
		for (int i = start; i <= end; i++) {
			RabbitMqQueueConfig config = getInitConfig(i);
			config.setConcurrentConsumers(1);
			queueConfigList.add(config);
		}
		return queueConfigList;
	}

	/**
	 * Common队列配置
	 */
	protected final RabbitMqQueueConfig getCommonConfig() {
		return getInitConfig(-1);
	}

	/**
	 * Topic队列配置
	 * 
	 * @return
	 */
	protected final List<RabbitMqQueueConfig> getTopicConfigList() {
		List<RabbitMqQueueConfig> queueConfigList = new ArrayList<RabbitMqQueueConfig>();
		String[] topicRoutingList = this.rabbitMqConfig.getTopicRouting().split("\\|");
		for (String routingKeys : topicRoutingList) {
			RabbitMqQueueConfig config = getInitConfig(-1);
			config.setRequestQueueName(requestQueueName(-1, this.serviceName + ":" + routingKeys));
			config.setConcurrentConsumers(1);
			config.setRoutingKeyList(routingKeys.split(","));
			queueConfigList.add(config);
		}
		return queueConfigList;
	}

	protected final RabbitMqQueueConfig getInitConfig(int index) {
		RabbitMqQueueConfig config = new RabbitMqQueueConfig();
		config.setRequestQueueName(requestQueueName(index, this.serviceName));
		config.setReplyQueueName(replyQueueName(index, this.serviceName));
		config.setExchange(exchangeName(index, this.serviceName));
		config.setRoutingKey(routingKeyName(index, this.serviceName));
		config.setConcurrentConsumers(this.rabbitMqConfig.getConsumers());
		config.setReceiveTimeout(this.rabbitMqConfig.getReceiveTimeout());
		config.setReplyTimeout(this.rabbitMqConfig.getReplyTimeout());
		return config;
	}

	protected final String requestQueueName(int index, String name) {
		return String.format(index < 0 ? "queue-%s-request" : "queue-%s-request-%s", name, index);
	}

	protected final String replyQueueName(int index, String name) {
		return String.format(index < 0 ? "queue-%s-reply" : "queue-%s-reply-%s", name, index);
	}

	protected final String exchangeName(int index, String name) {
		return String.format(index < 0 ? "exchange-%s" : "exchange-%s-%s", name, index);
	}

	protected final String routingKeyName(int index, String name) {
		return String.format(index < 0 ? "queue-%s-request" : "queue-%s-request-%s", name, index);
	}
}
