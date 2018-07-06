package com.suidifu.berkshire.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.demo2do.core.persistence.GenericDaoSupport;

/**
 * 
 * @author wukai
 * 
 *
 */
@Configuration
@ImportResource("classpath*:context/applicationContext-configuration.xml")
@EnableWebMvc
public class XmlConfiguration {

	@Bean
	public GenericDaoSupport genericDaoSupport() {
		return new com.demo2do.core.persistence.GenericDaoSupport();
	}
}
