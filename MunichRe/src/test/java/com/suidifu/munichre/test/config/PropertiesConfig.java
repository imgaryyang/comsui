package com.suidifu.munichre.test.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.suidifu.mq.config.RabbitMqConfig;

@Configuration("TestPropertiesConfig")
public class PropertiesConfig {
	@Bean(name = "cfg_munichre_mq_consumer")
	@ConfigurationProperties(prefix = "mq.multi.munichre.consumer")
	public RabbitMqConfig consumer() {
		return new RabbitMqConfig();
	}
}
