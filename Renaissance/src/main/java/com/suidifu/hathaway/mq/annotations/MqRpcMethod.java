/**
 * 
 */
package com.suidifu.hathaway.mq.annotations;

import java.lang.annotation.*;

/**
 * @author wukai
 *
 */
@Target({ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface MqRpcMethod {

	String beanName() default "";
	
	String methodName() default "";
	
	int priority() default 0;
	
}
