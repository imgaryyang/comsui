package com.suidifu.bridgewater.controller;

import javax.transaction.Transactional;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.demo2do.core.entity.Result;
import com.demo2do.core.utils.JsonUtils;
import com.suidifu.bridgewater.api.model.RemittanceBlackListCommandModel;
import com.suidifu.bridgewater.controller.api.remittance.CommandApiRemittanceController;
import com.zufangbao.gluon.spec.earth.v3.ApiResponseCode;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:/context/applicationContext-*.xml",
		"classpath:/local/applicationContext-*.xml",
		"classpath:/DispatcherServlet.xml" })

@Transactional()
@TransactionConfiguration(defaultRollback = true)
public class CommandApiRemittanceControllerTest extends AbstractTransactionalJUnit4SpringContextTests {
	
	@Autowired
	private CommandApiRemittanceController remittanceController;
	
	@Test
	@Sql("classpath:test/remittance/test_cancelContract.sql")
	public void test_cancelContract() {
		RemittanceBlackListCommandModel model = new RemittanceBlackListCommandModel();
		model.setRequestNo("test_requestNo_2");
		model.setProductCode("test_product_code");
		model.setUniqueId("test_contract_unique_id");
		//model.setContractNo("test_contract_no");
		
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		String message = remittanceController.recordRemittanceBlackList(model, request, response);
		
		Result result = JsonUtils.parse(message, Result.class);
		Assert.assertEquals("成功!", result.getMessage());
	}
	
	@Test
	@Sql("classpath:test/remittance/test_cancelContract.sql")
	public void test_cancelContract_noParams() {
		RemittanceBlackListCommandModel model = new RemittanceBlackListCommandModel();
		model.setRequestNo("test_requestNo_2");
		model.setProductCode("test_product_code");
		//model.setUniqueId("test_contract_unique_id");
		//model.setContractNo("test_contract_no");
		
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		String message = remittanceController.recordRemittanceBlackList(model, request, response);
		
		Result result = JsonUtils.parse(message, Result.class);
		Assert.assertEquals(ApiResponseCode.INVALID_PARAMS, Integer.parseInt(result.getCode()));
	}
}
