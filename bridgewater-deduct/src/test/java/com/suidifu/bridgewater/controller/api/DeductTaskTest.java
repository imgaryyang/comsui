package com.suidifu.bridgewater.controller.api;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.suidifu.bridgewater.task.DeductTask;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:/context/applicationContext-*.xml",
		"classpath:/local/applicationContext-*.xml",
		"classpath:/DispatcherServlet.xml" })
public class DeductTaskTest {
	
	@Autowired
	private DeductTask deductTask;
	
	
	@Test
	public void testExecDeductResultQuery(){
		//deductTask.execDeductResultQuery();
	}
	

}
