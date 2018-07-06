package com.zufangbao.canal.core.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.zufangbao.canal.core.bean.CanalServerConfig;

/**
 * 
 * @author lisf
 *
 */
@Configuration
public class PropertiesConfig {

	@Bean(name = "cfg_canal")
	@ConfigurationProperties(prefix = "canal.server")
	public CanalServerConfig canal() {
		return new CanalServerConfig();
	}

}
