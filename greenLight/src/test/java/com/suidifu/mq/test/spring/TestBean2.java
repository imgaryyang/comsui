package com.suidifu.mq.test.spring;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

@Component("hello_bean2")
public class TestBean2 {
	@PostConstruct
	public void init() {
		System.out.println("init...TestBean2");
	}

	public void test() {
		System.out.println("test2...");
	}
}
