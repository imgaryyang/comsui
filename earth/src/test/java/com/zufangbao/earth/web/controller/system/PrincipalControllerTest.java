//package com.zufangbao.earth.web.controller.system;
//
//import java.util.List;
//
//import javax.annotation.Resource;
//import javax.transaction.Transactional;
//
//import org.junit.Assert;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.jdbc.Sql;
//import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//import org.springframework.test.context.transaction.TransactionConfiguration;
//import org.springframework.test.context.web.WebAppConfiguration;
//import org.springframework.web.servlet.ModelAndView;
//
//import com.demo2do.core.entity.Result;
//import com.demo2do.core.persistence.GenericDaoSupport;
//import com.demo2do.core.utils.JsonUtils;
//import com.demo2do.core.web.resolver.Page;
//import com.zufangbao.sun.entity.security.Principal;
//import com.zufangbao.earth.service.PrincipalService;
//import com.zufangbao.sun.entity.user.TUser;
//import com.zufangbao.sun.yunxin.entity.model.financialcontract.FinancialContractQueryModel;
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations={
//
//		"classpath:/local/applicationContext-*.xml",
//		"classpath:/DispatcherServlet.xml"
//
//})
//@Transactional
//@TransactionConfiguration(defaultRollback = true)
//@WebAppConfiguration(value="webapp")
//public class PrincipalControllerTest extends AbstractTransactionalJUnit4SpringContextTests{
//	@Resource
//	private PrincipalController principalController;
//	@Resource
//	private GenericDaoSupport genericDaoSupport;
//	@Resource
//	private PrincipalService principalService;
//
//	@Test
//	public void postToUpdatePasswordTest(){
//		Principal principal = this.genericDaoSupport.load(Principal.class, 1L);
//		ModelAndView result = this.principalController.postToUpdatePasswordPage(principal);
//		Assert.assertEquals("index", result.getViewName());
//	}
//	@Test
//	@Sql("classpath:test/updatePasswordDataTest.sql")
//	public void updatePasswordTest(){
//		Principal principal = this.genericDaoSupport.load(Principal.class, 3L);
//		String message = this.principalController.updatePassword(principal, "e10adc3949ba59abbe56e057f20f883e", "a82a92061f9ad7a549a843658107141b");
//		Result result = JsonUtils.parse(message, Result.class);
//		Assert.assertTrue(result.isValid());
//		Assert.assertEquals("成功", result.getMessage());
//	}
//
//	@Test
//	@Sql("classpath:test/updatePasswordDataTest.sql")
//	public void updatePasswordTest_samePassword(){
//		Principal principal = this.genericDaoSupport.load(Principal.class, 1L);
//		String message = this.principalController.updatePassword(principal, "zufangbao@905", "zufangbao@905");
//		Result result = JsonUtils.parse(message, Result.class);
//		Assert.assertFalse(result.isValid());
//		Assert.assertEquals("原密码输入有误", result.getMessage());
//	}
//
//	@Test
//	@Sql("classpath:test/updatePasswordDataTest.sql")
//	public void updatePasswordWrongOldPassTest(){
//		Principal principal = this.genericDaoSupport.load(Principal.class, 1L);
//		String message = this.principalController.updatePassword(principal, "1234567", "zufangbao@905");
//		Result result = JsonUtils.parse(message, Result.class);
//		Assert.assertFalse(result.isValid());
//		Assert.assertEquals("原密码输入有误", result.getMessage());
//	}
//
////	@Test
////	@Sql("classpath:test/getUserRoleListData.sql")
////	public void getUserRoleListTest(){
////		Page page = new Page();
////		page.setBeginIndex(1);
////		page.setEveryPage(12);
////		page.setCurrentPage(1);
////		List<Principal> principal = principalController.getRoleList(page);
////		Assert.assertEquals(principal.size(), 12);
////	}
//
////	@Test
////	@Sql("classpath:test/deleteUser.sql")
////	public void deleteUserTest(){
////		boolean flag = true;
////		Page page = new Page();
////		page.setBeginIndex(1);
////		page.setEveryPage(12);
////		page.setCurrentPage(1);
////		List<Principal> principalList = principalController.getRoleList(page);
////		for(Principal principal : principalList)
////		 principalController.deleteUser(principal.getId(), page);
////
////		List<Principal> principalList2 = principalController.getRoleList(page);
////		for(Principal principal : principalList2)
////		 if(principal.getThruDate()==null)flag=false;
////		Assert.assertTrue(flag);
////	}
//
//	@Test
//	public void test_addUser() {
//		String username1 = "aaaaaa";
//		String message1=principalController.isValidPrincipalName(username1);
//		Result result1 = JsonUtils.parse(message1, Result.class);
//		Assert.assertEquals("0", result1.getCode());
//		Assert.assertEquals("成功", result1.getMessage());
//
//		String username2 = "aaaaaa_";
//		String message2=principalController.isValidPrincipalName(username2);
//		Result result2 = JsonUtils.parse(message2, Result.class);
//		Assert.assertEquals("0", result2.getCode());
//		Assert.assertEquals("成功", result2.getMessage());
//
//		String username3 = "aaaaaa_111";
//		String message3=principalController.isValidPrincipalName(username3);
//		Result result3 = JsonUtils.parse(message3, Result.class);
//		Assert.assertEquals("0", result3.getCode());
//		Assert.assertEquals("成功", result3.getMessage());
//	}
//
//	@Test
//	public void test_addUser_error() {
//		String username1 = "";
//		String message1=principalController.isValidPrincipalName(username1);
//		Result result1 = JsonUtils.parse(message1, Result.class);
//		Assert.assertEquals("-1", result1.getCode());
//		Assert.assertEquals("请输入格式有效的用户名！", result1.getMessage());
//
//		String username2= "2aaaaa";
//		String message2=principalController.isValidPrincipalName(username2);
//		Result result2 = JsonUtils.parse(message2, Result.class);
//		Assert.assertEquals("-1", result2.getCode());
//		Assert.assertEquals("请输入格式有效的用户名！", result2.getMessage());
//
//		String username3= "aaaaa";
//		String message3=principalController.isValidPrincipalName(username3);
//		Result result3 = JsonUtils.parse(message3, Result.class);
//		Assert.assertEquals("-1", result3.getCode());
//		Assert.assertEquals("请输入格式有效的用户名！", result3.getMessage());
//
//		String username4= "aaaaabbbbbcccccddddde";
//		String message4=principalController.isValidPrincipalName(username4);
//		Result result4 = JsonUtils.parse(message4, Result.class);
//		Assert.assertEquals("-1", result4.getCode());
//		Assert.assertEquals("请输入格式有效的用户名！", result4.getMessage());
//
//		String username5= "aaaaa bbbbb";
//		String message5=principalController.isValidPrincipalName(username5);
//		Result result5 = JsonUtils.parse(message5, Result.class);
//		Assert.assertEquals("-1", result5.getCode());
//		Assert.assertEquals("请输入格式有效的用户名！", result5.getMessage());
//	}
//
//	@Test
//	@Sql("classpath:test/yunxin/principal/principal_query_bind.sql")
//	public void testQueryBindFinancialContract_bind() {
//		Long principalId = 1L;
//		int bindState = 1;
//		FinancialContractQueryModel queryModel = new FinancialContractQueryModel();
//		queryModel.setPrincipalId(principalId);
//		queryModel.setBindState(bindState);
//		String message = principalController.queryBindFinancialContract(queryModel, null);
//		Result result = JsonUtils.parse(message, Result.class);
//		Assert.assertEquals("0", result.getCode());
//		Assert.assertEquals(3, result.getData().get("size"));
//	}
//
//	@Test
//	@Sql("classpath:test/yunxin/principal/principal_query_bind.sql")
//	public void testQueryBindFinancialContract_all() {
//		Long principalId = 1L;
//		int bindState = -1;
//		FinancialContractQueryModel queryModel = new FinancialContractQueryModel();
//		queryModel.setPrincipalId(principalId);
//		queryModel.setBindState(bindState);
//		String message = principalController.queryBindFinancialContract(queryModel, null);
//		Result result = JsonUtils.parse(message, Result.class);
//		Assert.assertEquals("0", result.getCode());
//		Assert.assertEquals(5, result.getData().get("size"));
//	}
//
//	@Test
//	@Sql("classpath:test/yunxin/principal/testQueryBindFinancialContract_unbind.sql")
//	public void testQueryBindFinancialContract_unbind() {
//		Long principalId = 1L;
//		int bindState = 0;
//		FinancialContractQueryModel queryModel = new FinancialContractQueryModel();
//		queryModel.setPrincipalId(principalId);
//		queryModel.setBindState(bindState);
//		String message = principalController.queryBindFinancialContract(queryModel, null);
//		Result result = JsonUtils.parse(message, Result.class);
//		Assert.assertEquals("0", result.getCode());
//		Assert.assertEquals(2, result.getData().get("size"));
//	}
//
//	@Test
//	@Sql("classpath:test/yunxin/principal/principal_bind.sql")
//	public void testBindFinancialContract() {
//		String financialContractIds = "[1,2]";
//		Long principalId = 1L;
//		principalController.bindFinancialContract(financialContractIds, principalId);
//		Principal principal = principalService.load(Principal.class, principalId);
//		Assert.assertNotNull(principal);
//		TUser user = principal.gettUser();
//		Assert.assertNotNull(user);
//		String financialContractIds2 = user.getFinancialContractIds();
//		Assert.assertEquals(financialContractIds, financialContractIds2);
//	}
//
//	@Test
//	@Sql("classpath:test/yunxin/principal/principal_bind.sql")
//	public void testBindFinancialContract_distinct() {
//		String financialContractIds = "[1,2,2]";
//		Long principalId = 1L;
//		principalController.bindFinancialContract(financialContractIds, principalId);
//		Principal principal = principalService.load(Principal.class, principalId);
//		Assert.assertNotNull(principal);
//		TUser user = principal.gettUser();
//		Assert.assertNotNull(user);
//		String financialContractIds2 = user.getFinancialContractIds();
//		String result = "[1,2]";
//		Assert.assertEquals(result, financialContractIds2);
//	}
//
//	@Test
//	@Sql("classpath:test/yunxin/principal/principal_bind.sql")
//	public void testBindFinancialContract_empty() {
//		String financialContractIds = "[]";
//		Long principalId = 1L;
//		principalController.bindFinancialContract(financialContractIds, principalId);
//		Principal principal = principalService.load(Principal.class, principalId);
//		Assert.assertNotNull(principal);
//		TUser user = principal.gettUser();
//		Assert.assertNotNull(user);
//		String financialContractIds2 = user.getFinancialContractIds();
//		String result = "[]";
//		Assert.assertEquals(result, financialContractIds2);
//	}
//	@Test
//	@Sql("classpath:test/yunxin/principal/principal_unbind.sql")
//	public void testUnBindFinancialContract() {
//		String financialContractIds = "[2]";
//		Long principalId = 1L;
//		principalController.unBindFinancialContract(financialContractIds, principalId);
//		Principal principal = principalService.load(Principal.class, principalId);
//		Assert.assertNotNull(principal);
//		TUser user = principal.gettUser();
//		Assert.assertNotNull(user);
//		String financialContractIds2 = user.getFinancialContractIds();
//		String result = "[1,3]";
//		Assert.assertEquals(result, financialContractIds2);
//	}
//
//	@Test
//	@Sql("classpath:test/yunxin/principal/principal_unbind.sql")
//	public void testUnBindFinancialContract_distinct() {
//		String financialContractIds = "[2,2]";
//		Long principalId = 1L;
//		principalController.unBindFinancialContract(financialContractIds, principalId);
//		Principal principal = principalService.load(Principal.class, principalId);
//		Assert.assertNotNull(principal);
//		TUser user = principal.gettUser();
//		Assert.assertNotNull(user);
//		String financialContractIds2 = user.getFinancialContractIds();
//		String result = "[1,3]";
//		Assert.assertEquals(result, financialContractIds2);
//	}
//
//	@Test
//	@Sql("classpath:test/yunxin/principal/principal_unbind.sql")
//	public void testUnBindFinancialContract_empty() {
//		String financialContractIds = "[]";
//		Long principalId = 1L;
//		principalController.unBindFinancialContract(financialContractIds, principalId);
//		Principal principal = principalService.load(Principal.class, principalId);
//		Assert.assertNotNull(principal);
//		TUser user = principal.gettUser();
//		Assert.assertNotNull(user);
//		String financialContractIds2 = user.getFinancialContractIds();
//		String result = "[1,2,3]";
//		Assert.assertEquals(result, financialContractIds2);
//	}
//
//}
