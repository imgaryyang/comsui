package com.suidifu.watchman;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;


@EnableCaching
@EnableAutoConfiguration
@ComponentScan(basePackages = {"com.suidifu"})
@SpringBootApplication
public class WatchManMessageTests {

    public static void main(String[] args) {
        SpringApplication.run(WatchManMessage.class, args);
    }

}
