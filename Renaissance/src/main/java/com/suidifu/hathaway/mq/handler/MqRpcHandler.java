/**
 * 
 */
package com.suidifu.hathaway.mq.handler;

import com.suidifu.hathaway.mq.annotations.MqAsyncRpcMethod;
import com.suidifu.hathaway.mq.annotations.MqRpcBusinessUuid;
import com.suidifu.hathaway.mq.annotations.MqRpcPriority;
import com.suidifu.hathaway.mq.annotations.MqSyncRpcMethod;
import com.suidifu.hathaway.mq.parameter.MqParameter;
import com.suidifu.hathaway.mq.sender.MessageSender;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.UUID;

/**
 * @author wukai
 *
 */
@Aspect
public class MqRpcHandler {
	
	private MessageSender messageSender;
	
	private static Log logger = LogFactory.getLog(MqRpcHandler.class);

	@Around("@annotation (com.suidifu.hathaway.mq.annotations.MqSyncRpcMethod)")
	public Object handleSyncMessage(ProceedingJoinPoint joinPoint) throws Throwable {
		
		logger.info("#handleSyncMessage#thread name["+Thread.currentThread().getName()+"],thread id["+Thread.currentThread().getId()+"]");

		MqParameter mqParameter = extractParameter(joinPoint, true);
		
		this.messageSender.registerProducerName(mqParameter.getProducerName());
		
		return messageSender.publishSyncRPCMessage(mqParameter.getRequestUuid(), mqParameter.getBusinessUuid(), mqParameter.getBeanName(), mqParameter.getMethodName(), mqParameter.getArgs(), mqParameter.getPrority());
	}
	@Around("@annotation (com.suidifu.hathaway.mq.annotations.MqAsyncRpcMethod)")
	public void handleASyncMessage(ProceedingJoinPoint joinPoint) throws Throwable {
		
		logger.info("#handleASyncMessage#thread name["+Thread.currentThread().getName()+"],thread id["+Thread.currentThread().getId()+"]");
			
		MqParameter mqParameter = extractParameter(joinPoint, false);
		
		this.messageSender.registerProducerName(mqParameter.getProducerName());
		
		this.messageSender.publishASyncRPCMessage(mqParameter.getRequestUuid(), mqParameter.getBusinessUuid(),mqParameter.getBeanName(), mqParameter.getMethodName(), mqParameter.getArgs(), mqParameter.getPrority());
			
	}
	
	private MqParameter extractParameter(ProceedingJoinPoint joinPoint, boolean isSync){
		
		Object target = joinPoint.getTarget();
		
		String signatureMethodName = joinPoint.getSignature().getName();
		
		Object[] args = joinPoint.getArgs();
		
		Method method = findMethodBy(target, signatureMethodName);
		
		Annotation methodMQRpc = findMqRpcAnnotation(method, isSync);
		
		String businessUuid = findBusinessUuid(args,method);
		
		String methodName = extractMethodName(signatureMethodName,methodMQRpc, isSync);
		
		String beanName = extractBeanName(target,methodMQRpc, isSync);
		
		String producerName = extractProducerName(methodMQRpc,isSync);
		
		String requestUuid = UUID.randomUUID().toString();
		
		int priority = findPriority(args, method, methodMQRpc, isSync);
		
		return new MqParameter(requestUuid, businessUuid, methodName, beanName, priority, args, producerName);
		
	}
	
	private String extractProducerName(Annotation mqRpc, boolean isSync) {
		
		String producerName = "messageProducer";
		
		if(null != mqRpc ){
			
			
			
			if(isSync){
				
				producerName =  ((MqSyncRpcMethod)(mqRpc)).producerName();
				
			}else{
				
				producerName =  ((MqAsyncRpcMethod)(mqRpc)).producerName();
			}
			if(StringUtils.isNotBlank(producerName)){
				
				return producerName;
			}
		}
		return producerName;
	}
	
	private String extractMethodName(String signatureMethodName, Annotation mqRpc, boolean isSync) {
		
		if(null != mqRpc ){
			
			String methodName = "";
			
			if(isSync){
				
				methodName =  ((MqSyncRpcMethod)(mqRpc)).methodName();
				
			}else{
				
				methodName =  ((MqAsyncRpcMethod)(mqRpc)).methodName();
			}
			if(StringUtils.isNotBlank(methodName)){
				
				return methodName;
			}
		}
		return signatureMethodName;
	}
	private Annotation findMqRpcAnnotation(Method method, boolean isSync){
		
		if(null == method){
			return null;
		}
		if(isSync){
			return method.getAnnotation(MqSyncRpcMethod.class);
		}
		
		return method.getAnnotation(MqAsyncRpcMethod.class);
	}
	
	private Method findMethodBy(Object target,String methodName){
		
		Method[] methods = target.getClass().getMethods();
		
		for (Method method : methods) {
			
			if(StringUtils.equals(method.getName(), methodName)){
				
				return method;
			}
		}
		return null;
	}
	private String findBusinessUuid(Object[] args,Method method){
		
		int index = findTargetParameterIndex(method.getParameters(),MqRpcBusinessUuid.class);
		
		if(index == -1 || index > args.length){
			
			return StringUtils.EMPTY;
		}
		return args[index].toString();
	}
	private int findPriority(Object[] args,Method method,Annotation mqRpc, boolean isSync){
		
		int index = findTargetParameterIndex(method.getParameters(),MqRpcPriority.class);
		
		if(index == -1 || index > args.length){
			
			if(isSync){
				return ((MqSyncRpcMethod)(mqRpc)).priority();
			}
			
			return ((MqAsyncRpcMethod)(mqRpc)).priority();
		}
		return Integer.parseInt(args[index].toString());
	}
	private int findTargetParameterIndex(Parameter[] parameters,Class<? extends Annotation> clazz){
		
		for (int index = 0;	index<parameters.length;index++) {
			
			Parameter parameter = parameters[index];
			
			if(parameter.isAnnotationPresent(clazz)){
				
				return index;
			}
		}
		return -1;
	}

	private String extractBeanName(Object target,Annotation mqRpc, boolean isSync){
		
		if(null != mqRpc){
			
			String beanName = StringUtils.EMPTY;
			
			if(isSync){
				
				beanName = ((MqSyncRpcMethod)mqRpc).beanName();
				
			}else{
				
				beanName = ((MqAsyncRpcMethod)mqRpc).beanName();
			}
			
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
	public void setMessageSender(MessageSender messageSender) {
		this.messageSender = messageSender;
	}
}
