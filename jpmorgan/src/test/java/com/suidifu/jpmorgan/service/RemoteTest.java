package com.suidifu.jpmorgan.service;

import java.util.HashMap;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.demo2do.core.persistence.GenericDaoSupport;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={
		"classpath:/context/applicationContext-*.xml",
		"classpath:/local/applicationContext-*.xml",
})
public class RemoteTest extends AbstractTransactionalJUnit4SpringContextTests {

	@Autowired
	GenericDaoSupport genericDaoSupport;
	
	@Test
	public void test() throws InterruptedException {
		while(true) {
			System.out.println("exe update...");
			genericDaoSupport.executeSQL("update payment_order_gzunion_001053110000001 set business_status = '4', business_result_msg='账户户名不符' where business_status ='2'", new HashMap<String, Object>());
			Thread.sleep(60000);
		}
	}
}