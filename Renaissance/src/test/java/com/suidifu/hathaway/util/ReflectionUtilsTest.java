package com.suidifu.hathaway.util;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.math.BigDecimal;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.aop.support.AopUtils;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import com.suidifu.hathaway.exceptions.DuplicateMethodNameException;

public class ReflectionUtilsTest {
	
	interface A {
		
		public String aa(String bb);
		
		public String aa(String aa,String bb);
		
		public String bb();
		
	}
	class AA implements A{
		
		private BigDecimal bg;

		@Override
		public String aa(String bb) {
			// TODO Auto-generated method stub
			return null;
		}
		@Override
		public String aa(String aa, String bb) {
			// TODO Auto-generated method stub
			return null;
		}
		public String aa(String aa, String bb,String cc) {
			// TODO Auto-generated method stub
			return null;
		}
		public String bb(String bb){
			return "hi,only one method in AB";
		}
		@Override
		public String bb(){
			return "hi,only one method in AA";
		}
		
		public AA(){
			
		}
		
	}
	class BB {
		
		public static final String A = "AA";
		
		public BB(){
			
		}
	}
	
	class AAJdkDynamicProxy implements InvocationHandler{
		
		private Object target;

		public Object bind(Object target){
			this.target = target;
			return Proxy.newProxyInstance(this.target.getClass().getClassLoader(), target.getClass().getInterfaces(), this);
		}
		
		@Override
		public Object invoke(Object proxy, Method method, Object[] args)
				throws Throwable {
			return method.invoke(proxy, args);
		}
		
	}
	class BBCglibProxy implements MethodInterceptor {
		
		private Object target;
		
		public Object getInstance(Object target){
			
			this.target = target;
			Enhancer enhancer = new Enhancer();
			enhancer.setSuperclass(this.target.getClass());
			enhancer.setCallback(this);
			return enhancer.create();
		}

		@Override
		public Object intercept(Object arg0, Method method, Object[] args,
				MethodProxy proxy) throws Throwable {
			
			proxy.invokeSuper(arg0, args);
			
			return null;
		}
		
	}

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}
	
	@Test
	public void testFindOnlyMethod() throws DuplicateMethodNameException, NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		
		A aa = new AA();
		
		Method method = ReflectionUtils.findOnlyPublicAccessMethod(aa.getClass(), "bb");
		
		String expectedValue =  "hi,only one method in AA";
		
		assertEquals(expectedValue,method.invoke(aa).toString());
		
	}
	
	@Test(expected=NoSuchMethodException.class)
	public void testNoSuchMethodException() throws DuplicateMethodNameException, NoSuchMethodException {
		
		ReflectionUtils.findOnlyPublicAccessMethod(new AA().getClass(), "xxxx");
	}
	@Test(expected=DuplicateMethodNameException.class)
	public void testDuplicateMethodNameException() throws DuplicateMethodNameException, NoSuchMethodException {
		
		ReflectionUtils.findOnlyPublicAccessMethod(new AA().getClass(), "aa");
	}
	
	@Test
	public void testisProxyObject() throws Exception {
		
		assertFalse(ReflectionUtils.isProxyObject(new AA()));
		assertFalse(ReflectionUtils.isProxyObject(null));
		
		AAJdkDynamicProxy jdkDynamicProxy = new AAJdkDynamicProxy();
		
		A aJdkProxy = (A) jdkDynamicProxy.bind(new AA());
		
		assertTrue(ReflectionUtils.isProxyObject(aJdkProxy));
		
		BBCglibProxy cglibProxy = new BBCglibProxy();
		
		BB aaIncglibProxy = (BB) cglibProxy.getInstance(new BB());
		
		assertTrue(ReflectionUtils.isProxyObject(aaIncglibProxy));
	}
	
	@Test
	public void testgetDeclaredFields(){
		
		A a = new AA();
		
		Field[] fields = ReflectionUtils.getDeclaredFields(a);
		
		System.out.println("开始原始的字段打印1");
		
		for (Field field : fields) {
			
			System.out.println(field.getName());
			
		}
		System.out.println("结束原始的字段打印1");
		
		assertEquals(20,fields.length);
		
		AAJdkDynamicProxy jdkDynamicProxy = new AAJdkDynamicProxy();
		
		A aJdkProxy = (A) jdkDynamicProxy.bind(a);
		
		fields = aJdkProxy.getClass().getDeclaredFields();
		
		System.out.println("开始原始的字段打印2");
		
		for (Field field : fields) {
			System.out.println(field.getName());
		}
		System.out.println("结束原始的字段打印2");
		
		assertEquals(6,fields.length);
		
		AopUtils.getTargetClass("xx");
		
		fields = ReflectionUtils.getDeclaredFields(aJdkProxy);
		
		System.out.println("开始原始的字段打印3");
		
		for (Field field : fields) {
			System.out.println(field.getName());
		}
		System.out.println("结束原始的字段打印3");
		
		assertEquals(20,fields.length);
		
		
	}
	

}
