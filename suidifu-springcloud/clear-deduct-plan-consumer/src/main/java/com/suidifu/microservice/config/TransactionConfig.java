package com.suidifu.microservice.config;

import com.demo2do.core.persistence.SmartNamingStrategy;
import java.io.IOException;
import java.util.Properties;
import javax.annotation.Resource;
import javax.sql.DataSource;
import org.springframework.aop.framework.autoproxy.BeanNameAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.interceptor.TransactionInterceptor;

/**
 * @author wukai mail: wukai@hzsuidifu.com time: 2018-02-07 18:03 description:
 */
@Configuration
@EnableTransactionManagement
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class TransactionConfig {

  @Resource
  private DataSource dataSource;

  @Primary
  @Bean(name = "transactionManager", autowire = Autowire.BY_NAME)
  public HibernateTransactionManager transactionManager() {
    return new HibernateTransactionManager();
  }

  @Bean(name = "transactionInterceptor")
  public TransactionInterceptor transactionInterceptor() {
    TransactionInterceptor ti = new TransactionInterceptor();
    Properties transactionAttributes = new Properties();
    transactionAttributes.setProperty("get*", "PROPAGATION_REQUIRED,readOnly");
    transactionAttributes.setProperty("list*", "PROPAGATION_REQUIRED,readOnly");
    transactionAttributes.setProperty("load*", "PROPAGATION_REQUIRED,readOnly");
    transactionAttributes.setProperty("*", "PROPAGATION_REQUIRED");
    ti.setTransactionAttributes(transactionAttributes);
    ti.setTransactionManager(transactionManager());
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

  @Primary
  @Bean(name = "sessionFactory")
  public LocalSessionFactoryBean sessionFactory() throws IOException {
    LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
    sessionFactory.setDataSource(dataSource);
    sessionFactory.setNamingStrategy(new SmartNamingStrategy());
    sessionFactory.setPackagesToScan("com.zufangbao", "com.suidifu");
    Properties hibernateProperties = new Properties();
    hibernateProperties.load(new ClassPathResource("hibernate.properties").getInputStream());
    sessionFactory.setHibernateProperties(hibernateProperties);
    return sessionFactory;
  }
}
