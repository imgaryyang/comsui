/**
 * 
 */
package com.suidifu.berkshire;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.web.EmbeddedServletContainerAutoConfiguration;
import org.springframework.boot.autoconfigure.web.ServerPropertiesAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.suidifu.berkshire.configuration.PropertiesConfig;
import com.suidifu.berkshire.configuration.BeanConfig;
import com.suidifu.berkshire.configuration.TransactionConfig;
import com.suidifu.berkshire.configuration.XmlConfiguration;

/**
 * @author wukai
 *
 */
@Configuration
@Import({
	EmbeddedServletContainerAutoConfiguration.class,
	ServerPropertiesAutoConfiguration.class,
	PropertiesConfig.class,
	TransactionConfig.class,
	XmlConfiguration.class,
	BeanConfig.class,
	
})
@ComponentScan(basePackages = { "com.suidifu", "com.zufangbao" })
public class Application {
	
	public static void main(String[] args){
		
		SpringApplication.run(Application.class, args);
	}

}
