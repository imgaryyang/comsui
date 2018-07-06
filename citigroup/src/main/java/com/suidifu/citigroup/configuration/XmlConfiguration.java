package com.suidifu.citigroup.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

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
	
	
}
