package com.suidifu.morganstanley;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;
import org.springframework.test.context.ActiveProfiles;

@ComponentScan(basePackages = {"com.suidifu.morganstanley", "com.zufangbao"})
@SpringBootApplication
@Log4j2
@ImportResource("classpath:context/applicationContext-configuration.xml")
@ActiveProfiles("test")
@EnableCaching
public class TestMorganStanley extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
	    return application.sources(TestMorganStanley.class);
    }

    public static void main(String[] args) throws Exception {

	    SpringApplication.run(TestMorganStanley.class, args);
    }

}