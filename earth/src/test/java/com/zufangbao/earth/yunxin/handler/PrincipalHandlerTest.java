package com.zufangbao.earth.yunxin.handler;

import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.zufangbao.sun.entity.security.Principal;
import com.zufangbao.earth.handler.PrincipalHandler;
import com.zufangbao.earth.service.PrincipalService;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.yunxin.entity.model.financialcontract.FinancialContractQueryModel;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {

		"classpath:/local/applicationContext-*.xml" })

@Transactional
@TransactionConfiguration(defaultRollback = true)

public class PrincipalHandlerTest extends AbstractTransactionalJUnit4SpringContextTests{
	
	@Autowired
	private PrincipalHandler principalHandler;
	@Autowired
	private PrincipalService principalService;
	
	@Test
	public void testGet_can_access_financialContract_list_empty() {
		Principal principal = null;
		List<FinancialContract> result = principalHandler.get_can_access_financialContract_list(principal);
		Assert.assertTrue(CollectionUtils.isEmpty(result));
	}
	
	@Test
	public void testGet_can_access_financialContract_list_empty_2() {
		Long principalId = null;
		List<FinancialContract> result = principalHandler.get_can_access_financialContract_list(principalId);
		Assert.assertTrue(CollectionUtils.isEmpty(result));
	}
	
	@Test
	@Sql("classpath:test/yunxin/principal/get_can_access_financialContract_list.sql")
	public void testGet_can_access_financialContract_list_super_user() {
		Principal principal = principalService.load(Principal.class, 2L);
		List<FinancialContract> result = principalHandler.get_can_access_financialContract_list(principal);
		Assert.assertTrue(CollectionUtils.isNotEmpty(result));
		Assert.assertEquals(5, result.size());
	}

	@Test
	@Sql("classpath:test/yunxin/principal/get_can_access_financialContract_list.sql")
	public void testGet_can_access_financialContract_list_super_user_2() {
		Long principalId = 2L;
		List<FinancialContract> result = principalHandler.get_can_access_financialContract_list(principalId);
		Assert.assertTrue(CollectionUtils.isNotEmpty(result));
		Assert.assertEquals(5, result.size());
	}
	
	@Test
	@Sql("classpath:test/yunxin/principal/get_can_access_financialContract_list.sql")
	public void testGet_can_access_financialContract_list_normal_user() {
		Principal principal = principalService.load(Principal.class, 1L);
		List<FinancialContract> result = principalHandler.get_can_access_financialContract_list(principal);
		Assert.assertTrue(CollectionUtils.isNotEmpty(result));
		Assert.assertEquals(3, result.size());
	}

	@Test
	@Sql("classpath:test/yunxin/principal/get_can_access_financialContract_list.sql")
	public void testGet_can_access_financialContract_list_normal_user_2() {
		Long principalId = 1L;
		List<FinancialContract> result = principalHandler.get_can_access_financialContract_list(principalId);
		Assert.assertTrue(CollectionUtils.isNotEmpty(result));
		Assert.assertEquals(3, result.size());
	}
	
	@Test
	@Sql("classpath:test/yunxin/principal/get_unbind_financialContract_list.sql")
	public void testGet_unbind_financialContract_list_normal_user_all() {
		Long principalId = null;
		List<FinancialContract> result = principalHandler.get_unbind_financialContract_list(principalId);
		Assert.assertTrue(CollectionUtils.isNotEmpty(result));
		Assert.assertEquals(5, result.size());
	}
	
	@Test
	@Sql("classpath:test/yunxin/principal/get_unbind_financialContract_list.sql")
	public void testGet_unbind_financialContract_list_normal_user() {
		Long principalId = 1L;
		List<FinancialContract> result = principalHandler.get_unbind_financialContract_list(principalId);
		Assert.assertTrue(CollectionUtils.isNotEmpty(result));
		Assert.assertEquals(2, result.size());
	}

	@Test
	@Sql("classpath:test/yunxin/principal/get_unbind_financialContract_list.sql")
	public void testGet_unbind_financialContract_list_super_user() {
		Long principalId = 2L;
		List<FinancialContract> result = principalHandler.get_unbind_financialContract_list(principalId);
		Assert.assertTrue(CollectionUtils.isEmpty(result));
	}
	
	@Test
	@Sql("classpath:test/yunxin/principal/queryFinancialContractListByPrincipal.sql")
	public void testQueryBindFinancialContract_bind_normal_user() {
		Long principalId = 1L;
		int bindState = 1;
		FinancialContractQueryModel queryModel = new FinancialContractQueryModel();
		queryModel.setPrincipalId(principalId);
		queryModel.setBindState(bindState);
		Map<String, Object> map = principalHandler.queryFinancialContractListByPrincipal(queryModel, null);
		Assert.assertEquals(3, map.get("size"));
	}

	@Test
	@Sql("classpath:test/yunxin/principal/queryFinancialContractListByPrincipal.sql")
	public void testQueryBindFinancialContract_bind_super_user() {
		Long principalId = 2L;
		int bindState = 1;
		FinancialContractQueryModel queryModel = new FinancialContractQueryModel();
		queryModel.setPrincipalId(principalId);
		queryModel.setBindState(bindState);
		Map<String, Object> map = principalHandler.queryFinancialContractListByPrincipal(queryModel, null);
		Assert.assertEquals(5, map.get("size"));
	}
	
	@Test
	@Sql("classpath:test/yunxin/principal/queryFinancialContractListByPrincipal.sql")
	public void testQueryBindFinancialContract_all() {
		Long principalId = 1L;
		int bindState = -1;
		FinancialContractQueryModel queryModel = new FinancialContractQueryModel();
		queryModel.setPrincipalId(principalId);
		queryModel.setBindState(bindState);
		Map<String, Object> map = principalHandler.queryFinancialContractListByPrincipal(queryModel, null);
		Assert.assertEquals(5, map.get("size"));
	}

	@Test
	@Sql("classpath:test/yunxin/principal/queryFinancialContractListByPrincipal.sql")
	public void testQueryBindFinancialContract_unbind_normal_user() {
		Long principalId = 1L;
		int bindState = 0;
		FinancialContractQueryModel queryModel = new FinancialContractQueryModel();
		queryModel.setPrincipalId(principalId);
		queryModel.setBindState(bindState);
		Map<String, Object> map = principalHandler.queryFinancialContractListByPrincipal(queryModel, null);
		Assert.assertEquals(2, map.get("size"));
	}

	@Test
	@Sql("classpath:test/yunxin/principal/queryFinancialContractListByPrincipal.sql")
	public void testQueryBindFinancialContract_unbind_super_user() {
		Long principalId = 2L;
		int bindState = 0;
		FinancialContractQueryModel queryModel = new FinancialContractQueryModel();
		queryModel.setPrincipalId(principalId);
		queryModel.setBindState(bindState);
		Map<String, Object> map = principalHandler.queryFinancialContractListByPrincipal(queryModel, null);
		Assert.assertEquals(0, map.get("size"));
	}

	@Test
	@Sql("classpath:test/yunxin/principal/queryFinancialContractListByPrincipal.sql")
	public void testQueryBindFinancialContract_empty() {
		Map<String, Object> map = principalHandler.queryFinancialContractListByPrincipal(null, null);
		Assert.assertEquals(0, map.get("size"));
	}
}
