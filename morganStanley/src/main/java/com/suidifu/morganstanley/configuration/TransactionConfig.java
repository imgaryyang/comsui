package com.suidifu.morganstanley.configuration;

import com.demo2do.core.persistence.GenericDaoSupport;
import com.demo2do.core.persistence.GenericJdbcSupport;
import com.demo2do.core.persistence.SmartNamingStrategy;
import com.suidifu.hathaway.mq.handler.MqRpcHandler;
import com.suidifu.hathaway.mq.sender.MessageSender;
import com.suidifu.matryoshka.delayPosition.handler.DelayProcessingTaskCacheHandler;
import com.suidifu.matryoshka.delayPosition.handler.DelayTaskConfigCacheHandler;
import com.suidifu.matryoshka.delayPosition.handler.impl.DelayProcessingTaskCacheHandlerImpl;
import com.suidifu.matryoshka.delayPosition.handler.impl.DelayTaskConfigCacheHandlerImpl;
import com.suidifu.morganstanley.servers.MorganStanleyNotifyServer;
import com.suidifu.swift.notifyserver.notifyserver.Exceptions.NotifyJobLocationCorruptException;
import com.suidifu.swift.notifyserver.notifyserver.configurations.NotifyServerConfig;
import com.zufangbao.sun.ledgerbookv2.handler.LedgerCacheHandler;
import com.zufangbao.sun.ledgerbookv2.handler.impl.LedgerCacheHandlerImpl;
import com.zufangbao.sun.ledgerbookv2.handler.impl.LedgerTemplateParser;
import com.zufangbao.sun.ledgerbookv2.service.InsideAccountService;
import com.zufangbao.sun.ledgerbookv2.service.impl.CacheableInsideAccountServiceImpl;
import com.zufangbao.wellsfargo.mq.sender.MqMessageSender;
import com.zufangbao.wellsfargo.silverpool.ledgerbookv2.template.CachecableLedgerTemplateParser;
import org.springframework.aop.framework.autoproxy.BeanNameAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.interceptor.TransactionInterceptor;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

/**
 * 数据库事物相关配置
 *
 * @author lisf
 */
@Configuration
@EnableTransactionManagement
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class TransactionConfig {

    @Bean
    @ConfigurationProperties("spring.datasource")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    @Primary
    @Bean(name = "sessionFactory")
    public LocalSessionFactoryBean sessionFactory() throws IOException {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource());
        sessionFactory.setNamingStrategy(new SmartNamingStrategy());
        sessionFactory.setPackagesToScan("com.zufangbao", "com.suidifu");
        Properties hibernateProperties = new Properties();
        hibernateProperties.load(new ClassPathResource("hibernate.properties").getInputStream());
        sessionFactory.setHibernateProperties(hibernateProperties);
        return sessionFactory;
    }

    @Primary
    @Bean(name = "genericDaoSupport", autowire = Autowire.BY_TYPE)
    public GenericDaoSupport genericDaoSupport() {
        return new GenericDaoSupport();
    }

    @Bean(name = "genericJdbcSupport", autowire = Autowire.BY_TYPE)
    public GenericJdbcSupport genericJdbcSupport() {
        GenericJdbcSupport genericJdbcSupport = new GenericJdbcSupport();
        genericJdbcSupport.setDataSource(dataSource());
        return genericJdbcSupport;
    }

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

    @Bean(name = "MorganStanleyNotifyServer")
    public MorganStanleyNotifyServer morganStanleyNotifyServer() throws NotifyJobLocationCorruptException {
        // 一个项目只能有一个 ServerIdentity
        MorganStanleyNotifyServer morganStanleyNotifyServer = new MorganStanleyNotifyServer();
        NotifyServerConfig notifyServerConfig = getNotifyServerConfigByProperties();
        morganStanleyNotifyServer.setNotifyServerConfig(notifyServerConfig);
        return morganStanleyNotifyServer;
    }

    @Resource
    private MorganStanleyNotifyConfig morganStanleyNotifyConfig;

    private NotifyServerConfig getNotifyServerConfigByProperties() {
        NotifyServerConfig notifyServerConfig = new NotifyServerConfig();
        notifyServerConfig.setCachedJobQueueSize(this.morganStanleyNotifyConfig.getCachedJobQueueSize());
        notifyServerConfig.setPersistenceMode(this.morganStanleyNotifyConfig.getPersistenceMode());
        notifyServerConfig.setServerIdentity(this.morganStanleyNotifyConfig.getServerIdentity());
        notifyServerConfig.setResponseResultFileDir(this.morganStanleyNotifyConfig.getResponseResultFileDir());
        notifyServerConfig.setGroupCacheJobQueueMap(this.morganStanleyNotifyConfig.getGroupCacheJobQueueMap());
        return notifyServerConfig;
    }

    @Bean(name = "delayProcessingTaskCacheHandler")
    public DelayProcessingTaskCacheHandler delayProcessingTaskCacheHandler() {
        return new DelayProcessingTaskCacheHandlerImpl();
    }

    @Bean(name = "delayTaskConfigCacheHandler")
    public DelayTaskConfigCacheHandler delayTaskConfigCacheHandler() {
        return new DelayTaskConfigCacheHandlerImpl();
    }

    @Bean(name = "ledgerCacheHandler")
    public LedgerCacheHandler ledgerCacheHandler() {
        return new LedgerCacheHandlerImpl();
    }

    @Bean(name = "ledgerTemplateParser")
    public LedgerTemplateParser ledgerTemplateParser() {
        return new CachecableLedgerTemplateParser();
    }

    @Bean(name = "insideAccountService")
    public InsideAccountService insideAccountService() {
        return new CacheableInsideAccountServiceImpl();
    }

    @Bean
    public MessageSender consistentHashSender() {
        MessageSender messageSender = new MqMessageSender();
        messageSender.registerProducerName("messageProducer");
        return messageSender;
    }

    @Bean
    public MqRpcHandler mqRpcHandler(MessageSender messageSender) {
        MqRpcHandler mqRpcHandler = new MqRpcHandler();
        mqRpcHandler.setMessageSender(messageSender);
        return mqRpcHandler;
    }
}