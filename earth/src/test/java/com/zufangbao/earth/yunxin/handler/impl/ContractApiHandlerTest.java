package com.zufangbao.earth.yunxin.handler.impl;

import com.zufangbao.earth.yunxin.api.util.ApiMessageUtil;
import com.zufangbao.gluon.exception.ApiException;
import com.zufangbao.sun.entity.contract.Contract;
import com.zufangbao.wellsfargo.yunxin.handler.ContractApiHandler;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={

		"classpath:/local/applicationContext-*.xml",
})
@Transactional
@TransactionConfiguration(defaultRollback = true)
public class ContractApiHandlerTest extends AbstractTransactionalJUnit4SpringContextTests{
	
	@Autowired
	private ContractApiHandler contractApiHandler;
	
	/*************** test getContractBy start ***************/
	@Test
	public void test_getContractBy_paramsNull() {
		try {
			contractApiHandler.getContractBy(null, null);
			Assert.fail();
		} catch (ApiException e) {
			Assert.assertEquals("贷款合同不存在!", ApiMessageUtil.getMessage(e.getCode()));
		}
	}
	@Test
	@Sql("classpath:test/yunxin/contract/test_getContractBy_paramsEmpty.sql")
	public void test_getContractBy_paramsEmpty() {
		try {
			contractApiHandler.getContractBy("", "");
			Assert.fail();
		} catch (ApiException e) {
			Assert.assertEquals("贷款合同不存在!", ApiMessageUtil.getMessage(e.getCode()));
		}
	}
	
	@Test
	@Sql("classpath:test/yunxin/contract/test_getContractBy.sql")
	public void test_getContractBy_uniqueId() {
		String uniqueId = "e568793f-a44c-4362-9e78-0ce433131f3e";
		Contract actualContract = contractApiHandler.getContractBy(uniqueId, "");
		Assert.assertEquals(uniqueId, actualContract.getUniqueId());
	}
	
	@Test
	@Sql("classpath:test/yunxin/contract/test_getContractBy.sql") 
	public void test_getContractBy_ContractNo() {
		String contractNo = "云信信2016-241-DK(428522112675736881)";
		Contract actualContract = contractApiHandler.getContractBy("", contractNo);
		Assert.assertEquals(contractNo, actualContract.getContractNo());
	}
	/*************** test getContractBy end ***************/
}
