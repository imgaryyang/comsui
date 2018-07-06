package com.suidifu.berkshire.configuration;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.zaxxer.hikari.HikariConfig;

/**
 * 数据库事物相关配置
 * 
 * @author lisf
 *
 */
@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class BeanConfig {

	@Resource(name = "cfg_datasource")
	private HikariConfig hikariConfig;
	
	@Bean(name="namedParameterJdbcTemplate")
	public NamedParameterJdbcTemplate namedParameterJdbcTemplate(DataSource dataSource){
		
		NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
		
		return namedParameterJdbcTemplate;
	}
}