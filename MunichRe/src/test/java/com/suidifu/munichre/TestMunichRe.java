package com.suidifu.munichre;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import com.zufangbao.canal.core.ImportCanalDependencies;

@Import({ ImportCanalDependencies.class })
@SpringBootApplication
public class TestMunichRe {
	public static void main(String[] args) {
		SpringApplication.run(TestMunichRe.class, args);
	}
}
