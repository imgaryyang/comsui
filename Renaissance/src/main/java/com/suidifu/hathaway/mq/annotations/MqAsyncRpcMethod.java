/**
 * 
 */
package com.suidifu.hathaway.mq.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author wukai
 *
 */
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@MqRpcMethod
public @interface MqAsyncRpcMethod  {

	String beanName() default "";
	
	String methodName() default "";
	
	int priority() default 0;
	
	String producerName() default "messageProducer";
	
	
}
