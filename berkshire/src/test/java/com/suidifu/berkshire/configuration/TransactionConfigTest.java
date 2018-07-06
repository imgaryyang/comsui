package com.suidifu.berkshire.configuration;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.aop.framework.autoproxy.BeanNameAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.transaction.interceptor.TransactionInterceptor;

import com.demo2do.core.persistence.GenericDaoSupport;
import com.demo2do.core.persistence.GenericJdbcSupport;
import com.suidifu.berkshire.BaseTestContext;
import com.zufangbao.sun.ledgerbook.BankAccountCache;
/**
 * 
 * @author wukai
 *
 */
public class TransactionConfigTest extends BaseTestContext{

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}
	
	@Autowired
	private GenericJdbcSupport genericJdbcSupport;
	
	@Autowired
	private GenericDaoSupport genericDaoSupport;
	
	@Autowired
	private DataSource dataSource;
	
	@Autowired
	private LocalSessionFactoryBean localSessionFactoryBean;

	@Test
	public void testDataSource() throws SQLException {
		
		assertNotNull(dataSource);
	}

	@Test
	public void testSessionFactory() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		
		assertNotNull(localSessionFactoryBean);
		
		Field packagesToScanField = localSessionFactoryBean.getClass().getDeclaredField("packagesToScan");
		
		packagesToScanField.setAccessible(true);
		
		String[] packageToScan = (String[]) packagesToScanField.get(localSessionFactoryBean);
		
		assertArrayEquals(new String[]{"com.zufangbao", "com.suidifu"}, packageToScan);
		
		assertNull(localSessionFactoryBean.getHibernateProperties().getProperty("hibernate.hbm2ddl.auto"));
		assertEquals("true",localSessionFactoryBean.getHibernateProperties().getProperty("hibernate.cache.use_second_level_cache"));
		assertEquals("true",localSessionFactoryBean.getHibernateProperties().getProperty("hibernate.cache.use_query_cache"));
	}

	@Test
	public void testGenericDaoSupport() {
		
		assertNotNull(genericDaoSupport);
	}

	@Test
	public void testGenericJdbcSupport() {
	
		assertNotNull(genericJdbcSupport);
		
	}
	@Autowired
	private HibernateTransactionManager hibernateTransactionManager;

	@Test
	public void testTransactionManager() {
		
		assertNotNull(hibernateTransactionManager);
	}
	
	@Autowired
	private TransactionInterceptor transactionInterceptor;

	@Test
	public void testTransactionInterceptor() {
		
		assertNotNull(transactionInterceptor);
		
		transactionInterceptor.getTransactionAttributeSource();
	}

	@Autowired
	private BeanNameAutoProxyCreator beanNameAutoProxyCreator;
	
	@Test
	public void testBeanNameAutoProxyCreator() {
		
		assertNotNull(beanNameAutoProxyCreator);
	}
	@Autowired
	private BankAccountCache bankAccountCache;
	
	@Test
	public void testbankAccountCache() throws Exception {
		
		assertNotNull(bankAccountCache);
		
	}
	@Autowired
	private CacheManager cacheManager;
	
	@Test
	public void testCacheManager() throws Exception {
		assertNotNull(cacheManager);
	}
	
	@Autowired
	private EhCacheManagerFactoryBean ehCacheManagerFactoryBean;

	@Test
	public void testEhCacheManagerFactoryBean() throws Exception {
		assertNotNull(ehCacheManagerFactoryBean);
	}
	
}
