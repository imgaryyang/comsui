package com.zufangbao.earth.yunxin.service;

import com.zufangbao.earth.yunxin.api.util.ApiMessageUtil;
import com.zufangbao.gluon.exception.ApiException;
import com.zufangbao.sun.service.UpdateAssetLogService;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
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
public class UpdateAssetLogServiceTest extends AbstractTransactionalJUnit4SpringContextTests {
	
	@Autowired
	private UpdateAssetLogService updateAssetLogService;
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	/*************** test checkRequestNo start ***************/
	@Test
	@Sql("classpath:/test/yunxin/updateAssetLog/emptyDatabase.sql")
	public void test_checkRequestNo() {
		String requestNo = "12345";
		updateAssetLogService.checkByRequestNo(requestNo);
	}
	
	@Test
	@Sql("classpath:test/yunxin/updateAssetLog/repeatRequestNo.sql")
	public void test_checkRequestNo_repeatRequestNo() {
		String requestNo = "12345";
			try {
				updateAssetLogService.checkByRequestNo(requestNo);
				Assert.fail();
			} catch (ApiException e) {
				Assert.assertEquals("请求编号重复!", ApiMessageUtil.getMessage(e.getCode()));
			}
	}
	/*************** test checkRequestNo end ***************/
}
