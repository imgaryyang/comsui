package com.suidifu.config.server;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * 统一配置中心
 *
 * @author louguanyang
 */
@Slf4j
@SpringBootApplication
@EnableDiscoveryClient
@EnableConfigServer
@EnableEurekaClient
public class SuidifuConfigServerApplication {

    public static void main(String[] args) {
        log.info("SuidifuConfigServerApplication startup begin");
        SpringApplication.run(SuidifuConfigServerApplication.class, args);
        log.info("SuidifuConfigServerApplication startup completed");
    }
}
