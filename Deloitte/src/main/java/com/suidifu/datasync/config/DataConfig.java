package com.suidifu.datasync.config;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import redis.clients.jedis.JedisPoolConfig;

/**
 * 数据存储配置相关
 * 
 * @author lisf
 *
 */
@Configuration
@EnableTransactionManagement
// @EnableAspectJAutoProxy(proxyTargetClass = true)
public class DataConfig {

	@Resource(name = "cfg_datasource")
	private HikariConfig hikariConfig;

	@Resource(name = "cfg_redis_pool")
	private JedisPoolConfig jedisPoolConfig;

	@Value("${spring.redis.host}")
	private String redisHost;
	@Value("${spring.redis.port}")
	private int redisPort;
	@Value("${spring.redis.password}")
	private String redisPassword;
	@Value("${spring.redis.database}")
	private int redisDatabase;
	@Value("${spring.redis.timeout}")
	private int redisTimeout;

	@Primary
	@Bean(name = "dataSource", destroyMethod = "close")
	public DataSource dataSource() {
		return new HikariDataSource(hikariConfig);
	}

	@Bean
	public JedisConnectionFactory jedisConnectionFactory() {
		JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(jedisPoolConfig);
		jedisConnectionFactory.setHostName(redisHost);
		jedisConnectionFactory.setPort(redisPort);
		jedisConnectionFactory.setPassword(redisPassword);
		jedisConnectionFactory.setDatabase(redisDatabase);
		jedisConnectionFactory.setTimeout(redisTimeout);
		return jedisConnectionFactory;
	}

	@Bean
	public StringRedisTemplate template() {
		return new StringRedisTemplate(jedisConnectionFactory());
	}
}
