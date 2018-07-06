/**
 * 
 */
package com.suidifu.berkshire.mq.rpc.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.suidifu.hathaway.mq.annotations.MqRpcBusinessUuid;
import com.suidifu.hathaway.mq.annotations.MqRpcPriority;
import com.suidifu.hathaway.mq.annotations.v2.MicroService;
import com.suidifu.watchman.message.amqp.request.AmqpRequest;
/**
 * @author wukai
 *
 */
public class RequestHelper {

	/**
	 * 通过注解提取Mq的请求
	 * @param joinPoint
	 * @param isSync
	 * @return
	 */
	public static AmqpRequest buildReqeust(ProceedingJoinPoint joinPoint) {
		
		Object target = joinPoint.getTarget();
		
		String signatureMethodName = joinPoint.getSignature().getName();
		
		Object[] args = joinPoint.getArgs();
		
		Method method = findMethodBy(target, signatureMethodName);
		
		MicroService methodMQRpc = findMqRpcAnnotation(method);
		
		String businessUuid = findBusinessUuid(args,method);
		
		String methodName = extractMethodName(signatureMethodName,methodMQRpc);
		
		String beanName = extractBeanName(target,methodMQRpc);
		
		String requestUuid = UUID.randomUUID().toString();
		
		int priority = findPriority(args, method, methodMQRpc);
		
		String vhostName = methodMQRpc.vhostName();
		
		String exchangeName = methodMQRpc.exchangeName();
		
		String routingKey = methodMQRpc.routingKey();
		
		boolean retry = methodMQRpc.retry();
		
		boolean sync = methodMQRpc.sync();

		List<String> paramTypes = buildParamTypes(method);
		
		AmqpRequest request =  new AmqpRequest(requestUuid, businessUuid, beanName, methodName, args, vhostName, exchangeName, routingKey, retry, priority,sync,paramTypes.toArray(new String[args.length]));

		return request;
		
	}

	private static List<String> buildParamTypes(Method method){

		Type[] parameterTypes = method.getGenericParameterTypes();

		List<String> params = new ArrayList<>();

		for (Type param: parameterTypes
				) {

			if((param instanceof ParameterizedType)) {

				Type[] typeArgument = ((ParameterizedType) param).getActualTypeArguments();

				if (typeArgument != null && typeArgument.length > 0) {

					params.add(typeArgument[0].getTypeName());
				}
			}
			else{
				params.add(param.getTypeName());
			}
		}
		return params;
	}
	
	private static String extractMethodName(String signatureMethodName, MicroService mqRpc) {
		
		if(null != mqRpc ){
			
			String methodName = mqRpc.methodName();
			
			if(StringUtils.isNotBlank(methodName)){
				
				return methodName;
			}
		}
		return signatureMethodName;
	}
	private  static MicroService findMqRpcAnnotation(Method method){
		
		if(null == method){
			return null;
		}
		return method.getAnnotation(MicroService.class);
	}
	
	private  static Method findMethodBy(Object target,String methodName){
		
		Method[] methods = target.getClass().getMethods();
		
		for (Method method : methods) {
			
			if(StringUtils.equals(method.getName(), methodName)){
				
				return method;
			}
		}
		return null;
	}
	private static String findBusinessUuid(Object[] args,Method method){
		
		int index = findTargetParameterIndex(method.getParameters(),MqRpcBusinessUuid.class);
		
		if(index == -1 || index > args.length){
			
			return StringUtils.EMPTY;
		}
		return args[index].toString();
	}
	private static int findPriority(Object[] args,Method method,MicroService mqRpc){
		
		int index = findTargetParameterIndex(method.getParameters(),MqRpcPriority.class);
		
		if(index == -1 || index > args.length){
			
			return mqRpc.priority();
		}
		return Integer.parseInt(args[index].toString());
	}
	private static int findTargetParameterIndex(Parameter[] parameters,Class<? extends Annotation> clazz){
		
		for (int index = 0;	index<parameters.length;index++) {
			
			Parameter parameter = parameters[index];
			
			if(parameter.isAnnotationPresent(clazz)){
				
				return index;
			}
		}
		return -1;
	}
	private static String extractBeanName(Object target,MicroService mqRpc){
		
		if(null != mqRpc){
			
			String beanName = mqRpc.beanName();
			
			if(StringUtils.isNotBlank(beanName)){
				
				return beanName;
			}
		}
		
		Class<?> targetClass = target.getClass();

		Component component = targetClass.getAnnotation(Component.class);
		
		if(null != component){
			
			return component.value();
		}
		Service service = targetClass.getAnnotation(Service.class);
		
		if(null != service){
			
			return service.value();
		}
		return targetClass.getSimpleName();
		
	}
}
