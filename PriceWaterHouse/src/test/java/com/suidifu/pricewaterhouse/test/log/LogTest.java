package com.suidifu.pricewaterhouse.test.log;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

import com.suidifu.pricewaterhouse.BaseTestContext;

public class LogTest extends BaseTestContext {

	private static Log logger = LogFactory.getLog(LogTest.class);

	@Test
	public void testLogTest() {

		System.out.println("test stout");

		logger.debug("test debug");

		logger.info("test info");

		logger.error("test error...");
		
		try{
			
			int a = 1/0;
			
		}catch(Exception e){
			
			logger.error("calculate error"+ExceptionUtils.getStackTrace(e));
			
		}
	}
}
