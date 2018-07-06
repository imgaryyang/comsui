package com.suidifu.microservice;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;

@Log4j2
@EnableCaching
@EnableEurekaClient
@EnableAutoConfiguration
@ComponentScan(basePackages = {"com.suidifu", "com.zufangbao"})
@ImportResource("classpath:context/applicationContext-configuration.xml")
@SpringBootApplication
public class ReconRepaymentOrderDeductConsumerApplication {

	public static void main(String[] args) {
		log.info("ReconRepaymentOrderDeductConsumer startup begin");
		SpringApplication.run(ReconRepaymentOrderDeductConsumerApplication.class, args);
		log.info("ReconRepaymentOrderDeductConsumer startup completed");
	}
}
