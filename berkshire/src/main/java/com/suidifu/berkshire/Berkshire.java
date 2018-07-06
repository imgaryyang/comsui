package com.suidifu.berkshire;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = { "com.suidifu", "com.zufangbao" })
@SpringBootApplication
public class Berkshire {
	public static void main(String[] args) throws Exception {
		
		SpringApplication.run(Berkshire.class, args);
	}

}
