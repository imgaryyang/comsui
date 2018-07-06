package com.suidifu.mq.test.spring;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.MethodInvoker;

import com.suidifu.mq.util.SpringContextUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = { TestApplication.class })
@WebIntegrationTest
public class TestMain {
	@Autowired
	TestBean1 bean1;
	@Autowired
	TestBean1 bean11;

	@Test
	public void test() {
		bean1.test();
		bean11.test();
	}

	@Test
	public void test2() {
		try {
			Object targetObject = SpringContextUtil.getBean("hello_bean3");
			MethodInvoker invoker = new MethodInvoker();
			invoker.setTargetObject(targetObject);
			invoker.setTargetMethod("test2");
			invoker.setArguments(new Object[] { 11, "hello",bean11 });
			invoker.prepare();
			Object rs=invoker.invoke();
			System.out.println("结果："+rs);
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
}
