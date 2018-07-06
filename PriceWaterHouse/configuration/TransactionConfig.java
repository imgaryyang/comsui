package com.suidifu.berkshire.configuration;

import java.util.Properties;

import org.springframework.aop.framework.autoproxy.BeanNameAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.interceptor.TransactionInterceptor;

/**
 * 
 * @author lisf
 *
 */
@Configuration
@EnableTransactionManagement
public class TransactionConfig {

	@Bean(name = "transactionInterceptor")
	public TransactionInterceptor transactionInterceptor(PlatformTransactionManager platformTransactionManager) {
		TransactionInterceptor ti = new TransactionInterceptor();
		Properties transactionAttributes = new Properties();
		transactionAttributes.setProperty("get*", "PROPAGATION_REQUIRED,readOnly");
		transactionAttributes.setProperty("list*", "PROPAGATION_REQUIRED,readOnly");
		transactionAttributes.setProperty("load*", "PROPAGATION_REQUIRED,readOnly");
		transactionAttributes.setProperty("*", "PROPAGATION_REQUIRED");
		ti.setTransactionAttributes(transactionAttributes);
		ti.setTransactionManager(platformTransactionManager);
		return ti;
	}

	@Bean(name = "beanNameAutoProxyCreator")
	public BeanNameAutoProxyCreator beanNameAutoProxyCreator() {
		BeanNameAutoProxyCreator proxyCreator = new BeanNameAutoProxyCreator();
		proxyCreator.setProxyTargetClass(true);
		proxyCreator.setBeanNames("*Service", "*Handler");
		proxyCreator.setInterceptorNames("transactionInterceptor");
		return proxyCreator;
	}
}
