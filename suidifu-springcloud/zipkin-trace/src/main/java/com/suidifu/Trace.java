package com.suidifu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import zipkin.server.EnableZipkinServer;

@EnableZipkinServer
@SpringBootApplication
public class Trace {
    public static void main(String[] args) {
        SpringApplication.run(Trace.class, args);
    }
}