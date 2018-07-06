package com.suidifu.berkshire.configuration;

import com.suidifu.hathaway.configurations.RedisConfig;
import com.suidifu.mq.config.RabbitMqConfig;
import com.zaxxer.hikari.HikariConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.suidifu.swift.notifyserver.notifyserver.mq.config.RabbitMqConnectionConfig;
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
		return new HikariConfig();
	}

	@Bean(name = "cfg_redis")
	@ConfigurationProperties(prefix = "spring.redis")
	public RedisConfig redis() {
		return new RedisConfig();
	}

	@Bean(name = "cfg_rabbitmq_consumer1")
	@ConfigurationProperties(prefix = "rabbitmq.consumer1")
	public RabbitMqConfig consumer() {
		return new RabbitMqConfig();
	}
	
	@Bean(name = "cfg_rabbitmq_consumer_munichre")
	@ConfigurationProperties(prefix = "mq.multi.munichre.consumer")
	public RabbitMqConfig munichre() {
		return new RabbitMqConfig();
	}
	
	@Bean(name = "cfg_rabbitmq_micro_service")
	@ConfigurationProperties(prefix = "mq.micro.service")
	public RabbitMqConnectionConfig rabbitMqConnectionConfig() {
		return new RabbitMqConnectionConfig();
	}
}