package com.suidifu.mq.test.spring;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

@Component("hello_bean3")
public class TestBean3 {
	@PostConstruct
	public void init() {
		System.out.println("init...TestBean3");
	}

	public void test() {
		System.out.println("test3...");
	}

	public String test2(int a, String b,TestBean1 bean1) {
		String rs = "test3...MethodInvoker.." + a + ">>>>>>>>>>." + b;
		System.out.println(rs);
		return rs+bean1.getJson();
	}
}
