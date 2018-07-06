package com.suidifu.munichre.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.suidifu.mq.config.RabbitMqConfig;
import com.zaxxer.hikari.HikariConfig;

/**
 * 
 * @author lisf
 *
 */
@Configuration
public class PropertiesConfig {

	@Bean(name = "cfg_munichre_mq_producer")
	@ConfigurationProperties(prefix = "mq.multi.munichre.producer")
	public RabbitMqConfig producer() {
		return new RabbitMqConfig();
	}
	
	@Bean(name = "cfg_datasource")
	@ConfigurationProperties(prefix = "hikaricp")
	public HikariConfig datasource() {
		return new HikariConfig();
		
	}
}
