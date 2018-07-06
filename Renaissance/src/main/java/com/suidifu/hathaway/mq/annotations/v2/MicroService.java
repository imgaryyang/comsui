/**
 * 
 */
package com.suidifu.hathaway.mq.annotations.v2;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.suidifu.hathaway.mq.annotations.MqRpcMethod;

/**
 * @author wukai
 * RPC
 */
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@MqRpcMethod
public @interface MicroService  {

	/**
	 * bean名称
	 * @return
	 */
	String beanName() default "";
	
	/**
	 * 方法名
	 * @return
	 */
	String methodName() default "";
	
	/**
	 * 优先级
	 * @return
	 */
	int priority() default 0;
	
	/**
	 * 虚拟空间名
	 * @return
	 */
	String vhostName() default "business";
	
	/**
	 * 交换机名
	 * @return
	 */
	String exchangeName() default "exchange-business";
	
	/**
	 * 路由健
	 * @return
	 */
	String routingKey();
	
	/**
	 * 尝试
	 * @return
	 */
	boolean retry() default false;
	
	/**
	 * 同步
	 * @return
	 */
	boolean sync() default true;
	
	
}
