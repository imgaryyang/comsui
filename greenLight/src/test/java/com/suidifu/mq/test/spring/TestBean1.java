package com.suidifu.mq.test.spring;

import javax.annotation.PostConstruct;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.suidifu.mq.util.SpringContextUtil;

@Component("hello_bean1")
public class TestBean1 implements ApplicationListener<ContextRefreshedEvent> {
	
	@PostConstruct
	public void init(){
		System.out.println("init...TestBean1");
//		TestBean3 bean3=SpringContextUtil.getBean("hello_bean3");
//		bean3.test();
	}
	
	public void test() {
		System.out.println("test1...");
	}
	
	public String getJson() {
		System.out.println("getJson...");
		return "{a:1,b:2}";
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		if (event.getApplicationContext().getParent() == null) {
			System.out.println("onApplicationEvent....hello_bean3.");
			TestBean3 bean3=SpringContextUtil.getBean("hello_bean3");
			bean3.test();
		}
	}
}
