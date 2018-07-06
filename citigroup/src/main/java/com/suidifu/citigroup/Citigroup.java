package com.suidifu.citigroup;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = { "com.suidifu", "com.zufangbao" })
@SpringBootApplication
public class Citigroup extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Citigroup.class);
    }

    public static void main(String[] args) throws Exception {

        SpringApplication.run(Citigroup.class, args);
    }

}