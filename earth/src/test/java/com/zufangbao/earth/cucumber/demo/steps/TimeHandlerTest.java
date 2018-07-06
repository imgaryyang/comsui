package com.zufangbao.earth.cucumber.demo.steps;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import mockit.Mock;
import mockit.MockUp;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {

		"classpath:/local/applicationContext-*.xml" })
@Transactional
@TransactionConfiguration(defaultRollback = true)
public class TimeHandlerTest extends AbstractTransactionalJUnit4SpringContextTests{

	@Autowired
	private TimeHandler timeHandler;
	
	@Test
	@Ignore("测试Mockup")
	public void testgetSystemTimeMillis(){
		
		assertTrue("mock之前应该大于0",(timeHandler.getSystemTimeMillis()>0));
		
		new MockUp<System>(){
			
			@Mock public long currentTimeMillis(){return 0;}
		};
		
		assertEquals("mock之前应该等于0",0,timeHandler.getSystemTimeMillis());
		
	}
	
}
