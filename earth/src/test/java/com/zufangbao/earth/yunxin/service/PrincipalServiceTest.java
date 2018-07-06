package com.zufangbao.earth.yunxin.service;

import java.util.List;

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
import com.zufangbao.earth.service.PrincipalService;
import com.zufangbao.sun.yunxin.entity.model.principal.PrincipalQueryModel;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={

		"classpath:/local/applicationContext-*.xml",
})
@TransactionConfiguration(defaultRollback = true)
public class PrincipalServiceTest extends AbstractTransactionalJUnit4SpringContextTests{
	@Autowired
	private PrincipalService principalService;
	
	@Test
	@Sql("classpath:test/yunxin/principal/principal.sql")
	public void test_queryPrincipal() {
		PrincipalQueryModel queryModel = new PrincipalQueryModel();
		String companyUuids = "[\"a02c0830-6f98-11e6-bf08-00163e002839\"]";
		String authorities = "[\"ROLE_SUPER_USER\"]";
		String id = "1";
		queryModel.setRoleId(authorities);
		queryModel.setId(id);
		queryModel.setCompanyId(companyUuids);
		List<Principal> principals = principalService.queryPrincipal(queryModel, null);
		
		Principal principal = principals.get(0);
		Assert.assertTrue(!CollectionUtils.isEmpty(principals));
		Assert.assertEquals("a02c0830-6f98-11e6-bf08-00163e002839", principal.gettUser().getCompany().getUuid());
		Assert.assertEquals("ROLE_SUPER_USER", principal.getAuthority());
		Assert.assertEquals("1", principal.getId().toString());
	}
	
	@Test
	@Sql("classpath:test/yunxin/principal/principal.sql")
	public void test_queryPrincipal_numberFormatException() {
		PrincipalQueryModel queryModel = new PrincipalQueryModel();
		String companyUuids = "[\"a02c0830-6f98-11e6-bf08-00163e002839\"]";
		String authorities = "[\"ROLE_SUPER_USER\"]";
		String id = "1a";
		queryModel.setRoleId(authorities);
		queryModel.setId(id);
		queryModel.setCompanyId(companyUuids);
		try {
			principalService.queryPrincipal(queryModel, null);
			Assert.fail();
		} catch (Exception e) {
			Assert.assertEquals(NumberFormatException.class, e.getClass());
		}
	}
	
	@Test
	@Sql("classpath:test/yunxin/principal/principal.sql")
	public void test_queryPrincipal_noResult() {
		
		PrincipalQueryModel queryModel1 = new PrincipalQueryModel();
		String companyUuids = "[\"a02c0830-6f98-11e6-bf08-00163e002839\"]";
		queryModel1.setRoleId(companyUuids);
		List<Principal> principals1 = principalService.queryPrincipal(queryModel1, null);
		Assert.assertTrue(CollectionUtils.isEmpty(principals1));
		
		PrincipalQueryModel queryModel2 = new PrincipalQueryModel();
		String authorities = "[\"ROLE_SUPER_USER\"]";
		queryModel2.setRoleId(authorities);
		List<Principal> principals2 = principalService.queryPrincipal(queryModel2, null);
		Assert.assertTrue(CollectionUtils.isNotEmpty(principals2));
		
		PrincipalQueryModel queryModel3 = new PrincipalQueryModel();
		String id = "2";
		queryModel3.setRoleId(authorities);
		queryModel3.setId(id);
		queryModel3.setCompanyId(companyUuids);
		List<Principal> principals3 = principalService.queryPrincipal(queryModel3, null);
		Assert.assertTrue(CollectionUtils.isEmpty(principals3));
	}
	
	
	
	
	
	
	
	
	
	
	@Test
	@Sql("classpath:test/yunxin/principal/principal.sql")
	public void test_countPrincipal() {
		PrincipalQueryModel queryModel = new PrincipalQueryModel();
		String companyUuids = "[\"a02c0830-6f98-11e6-bf08-00163e002839\"]";
		String authorities = "[\"ROLE_SUPER_USER\"]";
		String id = "1";
		queryModel.setRoleId(authorities);
		queryModel.setId(id);
		queryModel.setCompanyId(companyUuids);
		int count = principalService.countPrincipal(queryModel);
		Assert.assertTrue(Integer.compare(count, 0)>0);
	}
	
	@Test
	@Sql("classpath:test/yunxin/principal/principal.sql")
	public void test_countPrincipal_numberFormatException() {
		PrincipalQueryModel queryModel = new PrincipalQueryModel();
		String companyUuids = "[\"a02c0830-6f98-11e6-bf08-00163e002839\"]";
		String authorities = "[\"ROLE_SUPER_USER\"]";
		String id = "1a";
		queryModel.setRoleId(authorities);
		queryModel.setId(id);
		queryModel.setCompanyId(companyUuids);
		try {
			principalService.countPrincipal(queryModel);
			Assert.fail();
		} catch (Exception e) {
			Assert.assertEquals(NumberFormatException.class, e.getClass());
		}
	}
	
	@Test
	@Sql("classpath:test/yunxin/principal/principal.sql")
	public void test_countPrincipal_noResult() {
		
		PrincipalQueryModel queryModel1 = new PrincipalQueryModel();
		String companyUuids = "[\"a02c0830-6f98-11e6-bf08-00163e002839\"]";
		queryModel1.setRoleId(companyUuids);
		int count1 = principalService.countPrincipal(queryModel1);
		Assert.assertTrue(Integer.compare(count1, 0)==0);
		
		PrincipalQueryModel queryModel2 = new PrincipalQueryModel();
		String authorities = "[\"ROLE_SUPER_USER\"]";
		queryModel2.setRoleId(authorities);
		int count2 = principalService.countPrincipal(queryModel1);
		Assert.assertTrue(Integer.compare(count2, 0)==0);
		
		PrincipalQueryModel queryModel3 = new PrincipalQueryModel();
		String id = "2";
		queryModel3.setRoleId(authorities);
		queryModel3.setId(id);
		queryModel3.setCompanyId(companyUuids);
		int count3 = principalService.countPrincipal(queryModel1);
		Assert.assertTrue(Integer.compare(count3, 0)==0);
	}
	
	
	
	@Test
	@Sql("classpath:test/yunxin/principal/principal.sql")
	public void test_getPrincipal() {
		String userName = "zhanghongbing";
		Principal principal = principalService.getPrincipal(userName);
		Assert.assertNotNull(principal);
		Assert.assertEquals("zhanghongbing", principal.getName());
	}
	
	@Test
	@Sql("classpath:test/yunxin/principal/principal.sql")
	public void test_getPrincipal_noResult() {
		String userName = "zhanghongbing111";
		Principal principal = principalService.getPrincipal(userName);
		Assert.assertNull(principal);
	}
}
