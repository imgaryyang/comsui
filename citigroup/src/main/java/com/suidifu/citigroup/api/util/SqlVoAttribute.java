package com.suidifu.citigroup.api.util;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ java.lang.annotation.ElementType.FIELD })
public @interface SqlVoAttribute {

	/**
	 * 表中字段名;
	 */
	public abstract String name();

}