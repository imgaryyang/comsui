package com.zufangbao.earth.web.controller;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.demo2do.core.entity.Result;
import com.demo2do.core.utils.JsonUtils;
import com.zufangbao.earth.yunxin.web.controller.RepurchaseController;
import com.zufangbao.sun.yunxin.entity.model.repurchase.RepurchaseQueryModel;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {

		"classpath:/local/applicationContext-*.xml",
		"classpath:/DispatcherServlet.xml" })

@WebAppConfiguration(value="webapp")
public class RepurchaseControllerTest extends AbstractTransactionalJUnit4SpringContextTests{
	
	@Autowired
	private RepurchaseController repurchaseController;
	
	@Test
	@Sql("classpath:/test/yunxin/RepurchaseController/test4RepurchaseController.sql")
	public void testQueryRepurchase(){
		RepurchaseQueryModel queryModel = new RepurchaseQueryModel();
		String resultStr = repurchaseController.queryRepurchase(queryModel,null);
		Result result = JsonUtils.parse(resultStr, Result.class);
		Assert.assertEquals(0, result.get("size"));
		
		queryModel.setFinancialContractUuids("[\"2\"]");
		resultStr = repurchaseController.queryRepurchase(queryModel,null);
		result = JsonUtils.parse(resultStr, Result.class);
		Assert.assertEquals(0, result.get("size"));
		
		queryModel.setFinancialContractUuids("[\"financial_contract_uuid\"]");
		resultStr = repurchaseController.queryRepurchase(queryModel,null);
		result = JsonUtils.parse(resultStr, Result.class);
		Assert.assertEquals(10, result.get("size"));
		
		queryModel.setAppId(null);
		resultStr = repurchaseController.queryRepurchase(queryModel,null);
		result = JsonUtils.parse(resultStr, Result.class);
		Assert.assertEquals(10, result.get("size"));
		
		queryModel.setAppId(1l);
		resultStr = repurchaseController.queryRepurchase(queryModel,null);
		result = JsonUtils.parse(resultStr, Result.class);
		Assert.assertEquals(9, result.get("size"));
		
		queryModel.setContractNo("contract_no");;
		resultStr = repurchaseController.queryRepurchase(queryModel,null);
		result = JsonUtils.parse(resultStr, Result.class);
		Assert.assertEquals(8, result.get("size"));
		
		queryModel.setCustomerName("customer_name");
		resultStr = repurchaseController.queryRepurchase(queryModel,null);
		result = JsonUtils.parse(resultStr, Result.class);
		Assert.assertEquals(7, result.get("size"));
		
		queryModel.setRepoStartDate("2016-10-31");
		resultStr = repurchaseController.queryRepurchase(queryModel,null);
		result = JsonUtils.parse(resultStr, Result.class);
		Assert.assertEquals(6, result.get("size"));
		
		queryModel.setRepoEndDate("2016-11-5");
		resultStr = repurchaseController.queryRepurchase(queryModel,null);
		result = JsonUtils.parse(resultStr, Result.class);
		Assert.assertEquals(5, result.get("size"));
		
		queryModel.setRepurchaseStatusOrdinals("[\"0\"]");
		resultStr = repurchaseController.queryRepurchase(queryModel,null);
		result = JsonUtils.parse(resultStr, Result.class);
		Assert.assertEquals(1, result.get("size"));
	}
	
	
	
	
}
