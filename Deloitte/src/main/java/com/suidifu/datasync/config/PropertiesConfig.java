package com.suidifu.datasync.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.suidifu.datasync.config.domain.CanalServerConfig;
import com.zaxxer.hikari.HikariConfig;
import redis.clients.jedis.JedisPoolConfig;

/**
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

	@Bean(name = "cfg_canal")
	@ConfigurationProperties(prefix = "canal.server")
	public CanalServerConfig canal() {
		return new CanalServerConfig();
	}

	@Bean(name = "cfg_redis_pool")
	@ConfigurationProperties(prefix = "spring.redis.pool")
	public JedisPoolConfig getJedisPoolConfig() {
		return new JedisPoolConfig();
	}

}
