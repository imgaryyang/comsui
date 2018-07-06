package com.suidifu.pricewaterhouse.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.suidifu.hathaway.configurations.RedisConfig;
import com.suidifu.mq.config.RabbitMqConfig;
import com.suidifu.swift.notifyserver.notifyserver.configurations.NotifyServerConfig;
import com.zaxxer.hikari.HikariConfig;

/**
 * 集中属性配置文件
 * 
 * @author lisf
 *
 */
@Configuration
public class PropertiesConfig {

	@Bean(name = "cfg_datasource")
	@ConfigurationProperties(prefix = "hikaricp")
	public HikariConfig datasource() {
		return  new HikariConfig();
	}

	@Bean(name = "cfg_redis")
	@ConfigurationProperties(prefix = "spring.redis")
	public RedisConfig redis() {
		return new RedisConfig();
	}

	@Bean(name = "cfg_rabbitmq_producer")
	@ConfigurationProperties(prefix = "rabbitmq.producer")
	public RabbitMqConfig mq1() {
		return new RabbitMqConfig();
	}
	
	@Bean(name = "cfg_rabbitmq_log_producer")
	@ConfigurationProperties(prefix = "rabbitmq.logproducer")
	public RabbitMqConfig logmq1() {
		return new RabbitMqConfig();
	}
	
	@Bean(name = "cfg_notify_sever")
	@ConfigurationProperties(prefix = "notifyserver")
	public NotifyServerConfig notifyServer() {
		return new NotifyServerConfig();
	}
}
