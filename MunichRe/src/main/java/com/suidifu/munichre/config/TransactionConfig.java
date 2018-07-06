package com.suidifu.munichre.config;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Primary;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.suidifu.mq.util.AesUtil;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

/**
 * 数据库事物相关配置
 * 
 * @author lisf
 *
 */
@Configuration
@EnableTransactionManagement
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class TransactionConfig {

	@Resource(name = "cfg_datasource")
	private HikariConfig hikariConfig;

	@Primary
	@Bean(name = "dataSource")
	public DataSource dataSource() {
		hikariConfig.setPassword(AesUtil.decrypt(hikariConfig.getPassword()));
		return new HikariDataSource(hikariConfig);
	}
}
