package com.suidifu.morganstanley;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;
import org.springframework.test.context.ActiveProfiles;

/**
 * @author louguanyang
 */
@ComponentScan(basePackages = {"com.suidifu.morganstanley", "com.zufangbao"})
@SpringBootApplication
@Log4j2
@ImportResource("classpath:context/applicationContext-configuration.xml")
@ActiveProfiles("runtime")
@EnableCaching
public class MorganStanley {
    public static void main(String[] args) {
        log.info("start execute MorganStanley....\n");
        SpringApplication.run(MorganStanley.class, args);
        log.info("end execute MorganStanley....\n");
    }
}