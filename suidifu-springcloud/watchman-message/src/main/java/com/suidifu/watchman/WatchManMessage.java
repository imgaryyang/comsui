package com.suidifu.watchman;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author louguanyang
 */
@EnableCaching
@EnableAutoConfiguration
@ComponentScan(basePackages = {"com.suidifu"})
@SpringBootApplication
@Slf4j
public class WatchManMessage {
    public static void main(String[] args) {
        SpringApplication.run(WatchManMessage.class, args);
    }
}
