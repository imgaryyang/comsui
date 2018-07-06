package com.suidifu.dowjones;

import com.suidifu.dowjones.config.AuthorBanner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class Dowjones {
    public static void main(String[] args) {
        log.info("start execute Dowjones....\n");
        SpringApplication application = new SpringApplication(Dowjones.class);
        application.setBanner(new AuthorBanner());
        application.run(args);
        log.info("end execute Dowjones....\n");
    }
}