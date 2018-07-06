package com.suidifu.bridgewater.handler;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.suidifu.bridgewater.api.model.RemittanceBlackListCommandModel;
import com.zufangbao.gluon.exception.ApiException;
import com.zufangbao.gluon.spec.earth.v3.ApiResponseCode;
import com.zufangbao.sun.yunxin.entity.remittance.RemittanceBlackList;
import com.zufangbao.sun.yunxin.service.remittance.RemittanceBlackListService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:/context/applicationContext-*.xml",
		"classpath:/local/applicationContext-*.xml" })
@Transactional
public class RemittanceBlackListHandlerTest extends AbstractTransactionalJUnit4SpringContextTests{

	@Autowired
	private RemittanceBlackListHandler remittanceBlackListHandler;
	
	@Autowired
	private RemittanceBlackListService remittanceBlackListService;
	
	@Test
	@Sql("classpath:test/remittance/test_cancelContract.sql")
	public void test_cancelContract() {
		RemittanceBlackListCommandModel model = new RemittanceBlackListCommandModel();
		model.setRequestNo("test_requestNo_2");
		model.setProductCode("test_product_code");
		model.setUniqueId("test_contract_unique_id");
		//model.setContractNo("test_contract_no");
		
		String ip = "127.0.0.1";
		String creatorName = "test_creator";
		remittanceBlackListHandler.recordRemittanceBlackList(model, ip, creatorName);
		
		List<RemittanceBlackList> blackLists = remittanceBlackListService.loadAll(RemittanceBlackList.class);
		RemittanceBlackList actualBlackList = blackLists.get(1);
		
		Assert.assertEquals("test_requestNo_2", actualBlackList.getRequestNo());
		Assert.assertEquals("test_product_code", actualBlackList.getFinancialProductCode());
		Assert.assertEquals("test_contract_unique_id", actualBlackList.getContractUniqueId());
	}
	
	@Test
	@Sql("classpath:test/remittance/test_cancelContract.sql")
	public void test_cancelContract_repeatRequestNo() {
		RemittanceBlackListCommandModel model = new RemittanceBlackListCommandModel();
		model.setRequestNo("test_request_no");//重复请求编号
		model.setProductCode("test_product_code");
		model.setUniqueId("test_contract_unique_id");
		//model.setContractNo("test_contract_no");
		
		String ip = "127.0.0.1";
		String creatorName = "test_creator";
		
		try {
			remittanceBlackListHandler.recordRemittanceBlackList(model, ip, creatorName);
			Assert.fail();
		} catch (ApiException e) {
			Assert.assertEquals(ApiResponseCode.REPEAT_REQUEST_NO, e.getCode());
		}
		
	}
	
	@Test
	@Sql("classpath:test/remittance/test_cancelContract.sql")
	public void test_cancelContract_wrongProductCode() {
		RemittanceBlackListCommandModel model = new RemittanceBlackListCommandModel();
		model.setRequestNo("test_request_no_2");
		model.setProductCode("product_code");//错误productcode
		model.setUniqueId("test_contract_unique_id");
		//model.setContractNo("test_contract_no");
		
		String ip = "127.0.0.1";
		String creatorName = "test_creator";
		
		try {
			remittanceBlackListHandler.recordRemittanceBlackList(model, ip, creatorName);
			Assert.fail();
		} catch (ApiException e) {
			Assert.assertEquals(ApiResponseCode.FINANCIAL_PRODUCT_CODE_ERROR, e.getCode());
		}
		
	}
	
	@Test
	@Sql("classpath:test/remittance/test_cancelContract.sql")
	public void test_cancelContract_financialContractNotExist() {
		RemittanceBlackListCommandModel model = new RemittanceBlackListCommandModel();
		model.setRequestNo("test_request_no_2");
		model.setProductCode("xxxproduct_code");//数据库中无该productcode对应的financialcontract
		model.setUniqueId("test_contract_unique_id");
		//model.setContractNo("test_contract_no");
		
		String ip = "127.0.0.1";
		String creatorName = "test_creator";
		
		try {
			remittanceBlackListHandler.recordRemittanceBlackList(model, ip, creatorName);
			Assert.fail();
		} catch (ApiException e) {
			Assert.assertEquals(ApiResponseCode.FINANCIAL_CONTRACT_NOT_EXIST, e.getCode());
		}
		
	}
	
	@Test
	@Sql("classpath:test/remittance/test_cancelContract_applicationSuccess.sql")
	public void test_cancelContract_applicationSuccess() {
		RemittanceBlackListCommandModel model = new RemittanceBlackListCommandModel();
		model.setRequestNo("test_request_no_2");
		model.setProductCode("test_product_code");
		model.setUniqueId("test_contract_unique_id");
		//model.setContractNo("test_contract_no");
		
		String ip = "127.0.0.1";
		String creatorName = "test_creator";
		
		try {
			remittanceBlackListHandler.recordRemittanceBlackList(model, ip, creatorName);
			Assert.fail();
		} catch (ApiException e) {
			Assert.assertEquals(ApiResponseCode.CONTRACT_REMITTANCE_SUCCESS, e.getCode());
		}
		
	}
	
	@Test
	@Sql("classpath:test/remittance/test_cancelContract_applicationProcessing.sql")
	public void test_cancelContract_applicationProcessing() {
		RemittanceBlackListCommandModel model = new RemittanceBlackListCommandModel();
		model.setRequestNo("test_request_no_2");
		model.setProductCode("test_product_code");
		model.setUniqueId("test_contract_unique_id");
		//model.setContractNo("test_contract_no");
		
		String ip = "127.0.0.1";
		String creatorName = "test_creator";
		
		try {
			remittanceBlackListHandler.recordRemittanceBlackList(model, ip, creatorName);
			Assert.fail();
		} catch (ApiException e) {
			Assert.assertEquals(ApiResponseCode.CONTRACT_REMITTANCE_PROCESSING, e.getCode());
		}
		
	}
	
}
