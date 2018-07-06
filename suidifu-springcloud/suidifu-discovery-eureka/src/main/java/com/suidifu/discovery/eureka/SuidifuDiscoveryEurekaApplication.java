package com.suidifu.discovery.eureka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @author louguanyang
 */
@Slf4j
@SpringBootApplication
@EnableEurekaServer
public class SuidifuDiscoveryEurekaApplication {

    public static void main(String[] args) {
        log.info("SuidifuDiscoveryEurekaApplication startup begin");
        SpringApplication.run(SuidifuDiscoveryEurekaApplication.class, args);
        log.info("SuidifuDiscoveryEurekaApplication startup completed");
    }
}
