/**
 * 
 */
package com.suidifu.pricewaterhouse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author wukai
 *
 */
@ComponentScan(basePackages = { "com.suidifu", "com.zufangbao" })
@SpringBootApplication
public class Pricewaterhouse extends SpringBootServletInitializer{

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(Pricewaterhouse.class);
	}
	
	public static void main(String[] args) throws Exception {
		
		SpringApplication.run(Pricewaterhouse.class, args);
	}

}
