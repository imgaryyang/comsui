/**
 * 
 */
package com.suidifu.hathaway.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.util.ClassUtils;

import com.suidifu.hathaway.exceptions.DuplicateMethodNameException;

/**
 * @author wukai
 *
 */
public class ReflectionUtils{
	
	private static final Log LOG = LogFactory.getLog(ReflectionUtils.class);
	
	public static Method findOnlyPublicAccessMethod(Class<?> targetBeanClazz, String methodName) throws DuplicateMethodNameException,NoSuchMethodException{
		
		Class<?>[] interfaces = targetBeanClazz.getInterfaces();
		
		List<Method> targetMethods = new ArrayList<Method>();
		
		for (Class<?> clazz : interfaces) {
			
			Method[] methods = clazz.getDeclaredMethods();
			
			for (Method method : methods) {
				
//				if(Modifier.isPublic(method.getModifiers()))
				
					if(StringUtils.equals(method.getName(), methodName)){
						
						targetMethods.add(method);
					}
				
			}
		
			
		}
		if(targetMethods.size() == 0){
			throw new NoSuchMethodException("not found method name  is "+methodName);
		}
		if(targetMethods.size() > 1){
			throw new DuplicateMethodNameException("duplicate method name is "+methodName);
		}
		return targetMethods.get(0);
	
	}
	
	/**
	 * 判断一个对象是否为代理对象
	 * @param object
	 * @return
	 */
	public static boolean isProxyObject(Object object){
		return object != null && (Proxy.isProxyClass(object.getClass()) || ClassUtils.isCglibProxyClass(object.getClass()));
	}
	
	/**
	 * 获取一个对象的所有定义的Field
	 * @param object
	 * @return
	 */
	public static Field[] getDeclaredFields(Object object){
		
		Object targetObject = getTargetObject(object);
		
		if(null == targetObject){
			
			return new Field[0];
		}
		return targetObject.getClass().getDeclaredFields();
		
	}
	/**
	 * 获取代理对象后的真实对象
	 * @param candidate
	 * @return
	 */
	public static Object getTargetObject(Object candidate){
		
		if(isHibernateProxy(candidate)){
			
			return getHibernateTargetObject(candidate);
		}
		if(isJdkDynamicProxy(candidate)){
			
			return getJdkDynamicProxyTargetObject(candidate);
		}
		
		if((isJCglibProxy(candidate))){
			
			return getCglibProxyTargetObject(candidate);
		}
		
		return candidate;
	}
	
	private static boolean isJdkDynamicProxy(Object proxy){
		return proxy != null && Proxy.isProxyClass(proxy.getClass());
	}
	
	private static boolean isJCglibProxy(Object proxy){
		return proxy != null && ClassUtils.isCglibProxy(proxy.getClass());
	}
	private static boolean isHibernateProxy(Object proxy){
		return proxy != null && (proxy instanceof HibernateProxy);
	}
	
	private static Object getHibernateTargetObject(Object proxy){
		return ((HibernateProxy)proxy).getHibernateLazyInitializer().getImplementation();
	}
	
	private static Object getCglibProxyTargetObject(Object proxy)  {
		
		try {
			return AopTargetUtils.getCglibProxyTargetObject(proxy);
			
		} catch (Exception e) {
			
			LOG.error("#getCglibProxyTargetObject# occur exception with full stack trace["+ExceptionUtils.getFullStackTrace(e)+"]");
			
			return null;
		}
	}


	private static Object getJdkDynamicProxyTargetObject(Object proxy) {
		try {
		
			return AopTargetUtils.getJdkDynamicProxyTargetObject(proxy);
			
		}  catch (Exception e) {
			
			LOG.error("#getJdkDynamicProxyTargetObject# occur exception with full stack trace["+ExceptionUtils.getFullStackTrace(e)+"]");
			
			return null;
		}
	}
	
}
