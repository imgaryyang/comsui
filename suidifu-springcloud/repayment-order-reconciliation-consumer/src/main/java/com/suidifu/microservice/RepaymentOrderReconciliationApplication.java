package com.suidifu.microservice;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;

@Log4j2
@EnableCaching
@EnableDiscoveryClient
@EnableAutoConfiguration
@ComponentScan(basePackages = {"com.suidifu", "com.zufangbao"})
@ImportResource("classpath:context/applicationContext-configuration.xml")
@SpringBootApplication
public class RepaymentOrderReconciliationApplication {

    public static void main(String[] args) {
        log.info("RepaymentOrderReconciliationApplication startup begin");
        SpringApplication.run(RepaymentOrderReconciliationApplication.class, args);
        log.info("RepaymentOrderReconciliationApplication startup completed");
    }
}