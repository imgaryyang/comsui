package com.suidifu.bridgewater.handler.test;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.demo2do.core.persistence.support.Filter;
import com.suidifu.bridgewater.api.model.BatchDeductStatusQueryModel;
import com.suidifu.bridgewater.handler.BatchDeductStatusQureyLogHandler;
import com.zufangbao.gluon.exception.ApiException;
import com.zufangbao.sun.yunxin.api.deduct.service.BatchDeductStatusQueryLogService;
import com.zufangbao.sun.yunxin.entity.api.deduct.BatchDeductStatusQueryLog;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:/context/applicationContext-*.xml",
		"classpath:/local/applicationContext-*.xml" })
//@Transactional
public class BatchDeductStatusQureyLogHandlerTest {
	@Autowired
	private BatchDeductStatusQureyLogHandler batchDeductStatusQureyLogHandler;
	@Autowired
	private BatchDeductStatusQueryLogService batchDeductStatusQueryLogService;
	
	@Test
	@Sql("classpath:/test/yunxin/api/testCheckAndSaveRequest.sql")
	public void testCheckAndSaveRequest(){
		MockHttpServletRequest request = new MockHttpServletRequest();  
		BatchDeductStatusQueryModel  queryModel = new BatchDeductStatusQueryModel();
		queryModel.setDeductIdList("['94d1f220-21e6-4642-a681-d0f1816a0d20','c906d969-4654-4972-b284-40cb93331ade','fe98397b-fd90-438e-9d73-5c8faf43834e']");
		queryModel.setRequestNo("123");
		batchDeductStatusQureyLogHandler.checkAndSaveRequest(queryModel, request);
		Assert.assertEquals("123", batchDeductStatusQueryLogService.list(BatchDeductStatusQueryLog.class,new Filter().addEquals("requestNo", "123")).get(0).getRequestNo());
		
	}
	
	@Test
	@Sql("classpath:/test/yunxin/api/testCheckAndSaveRequest.sql")
	public void testCheckAndSaveRequest1(){
		try{
			MockHttpServletRequest request = new MockHttpServletRequest();  
			BatchDeductStatusQueryModel  queryModel = new BatchDeductStatusQueryModel();
			queryModel.setDeductIdList("['94d1f220-21e6-4642-a681-d0f1816a0d20','c906d969-4654-4972-b284-40cb93331ade','fe98397b-fd90-438e-9d73-5c8faf43834e']");
			queryModel.setRequestNo("1");
			batchDeductStatusQureyLogHandler.checkAndSaveRequest(queryModel, request);
			Assert.fail();
		}catch (ApiException e) {
			Assert.assertEquals(21002,e.getCode());
		}
	
	}
}
