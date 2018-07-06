package com.suidifu.mq.test.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.suidifu.mq.config.RabbitMqConfig;

/**
 * 集中属性配置文件
 * 
 * @author lisf
 *
 */
@Configuration
public class PropertiesConfig {

	@Bean(name = "cfg_consistenthash_producer")
	@ConfigurationProperties(prefix = "mq.consistenthash.producer")
	public RabbitMqConfig consistenthash_producer() {
		return new RabbitMqConfig();
	}

	@Bean(name = "cfg_consistenthash_consumer1")
	@ConfigurationProperties(prefix = "mq.consistenthash.consumer1")
	public RabbitMqConfig consistenthash_consumer1() {
		return new RabbitMqConfig();
	}

	@Bean(name = "cfg_consistenthash_consumer2")
	@ConfigurationProperties(prefix = "mq.consistenthash.consumer2")
	public RabbitMqConfig consistenthash_consumer2() {
		return new RabbitMqConfig();
	}

	@Bean(name = "cfg_common_producer")
	@ConfigurationProperties(prefix = "mq.common.producer")
	public RabbitMqConfig common_producer() {
		return new RabbitMqConfig();
	}

	@Bean(name = "cfg_common_consumer")
	@ConfigurationProperties(prefix = "mq.common.consumer")
	public RabbitMqConfig common_consumer() {
		return new RabbitMqConfig();
	}

	@Bean(name = "cfg_topic_producer")
	@ConfigurationProperties(prefix = "mq.topic.producer")
	public RabbitMqConfig topic_producer() {
		return new RabbitMqConfig();
	}

	@Bean(name = "cfg_topic_consumer1")
	@ConfigurationProperties(prefix = "mq.topic.consumer1")
	public RabbitMqConfig topic_consumer1() {
		return new RabbitMqConfig();
	}

	@Bean(name = "cfg_topic_consumer2")
	@ConfigurationProperties(prefix = "mq.topic.consumer2")
	public RabbitMqConfig topic_consumer2() {
		return new RabbitMqConfig();
	}
}
