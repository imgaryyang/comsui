/**
 * 
 */
package com.suidifu.contra.mq.parameter;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

/**
 * @author wukai
 *
 */
public class MessageReceiverParameter {

	private Method targetMethod = null;
	
	private Object targetBean = null;
	
	private Object proxyBean = null;
	
	private String methodName = null;
	
	private String beanName = null;
	
	private Type[] types = null;
	
	private Object[] args = null;

	public MessageReceiverParameter(Method targetMethod, Object targetBean,
			Object proxyBean, String methodName, String beanName,
			Type[] types, Object[] args) {
		super();
		this.targetMethod = targetMethod;
		this.targetBean = targetBean;
		this.proxyBean = proxyBean;
		this.methodName = methodName;
		this.beanName = beanName;
		this.types = types;
		this.args = args;
	}

	public Method getTargetMethod() {
		return targetMethod;
	}

	public void setTargetMethod(Method targetMethod) {
		this.targetMethod = targetMethod;
	}

	public Object getTargetBean() {
		return targetBean;
	}

	public void setTargetBean(Object targetBean) {
		this.targetBean = targetBean;
	}

	public Object getProxyBean() {
		return proxyBean;
	}

	public void setProxyBean(Object proxyBean) {
		this.proxyBean = proxyBean;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public String getBeanName() {
		return beanName;
	}

	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}

	public Type[] getTypes() {
		return types;
	}

	public void setTypes(Type[] types) {
		this.types = types;
	}

	public Object[] getArgs() {
		return args;
	}

	public void setArgs(Object[] args) {
		this.args = args;
	}
}
