//package com.zufangbao.earth.yunxin.web;
//
//import java.math.BigDecimal;
//import java.util.Date;
//import java.util.Map;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.transaction.Transactional;
//
//import org.codehaus.janino.Java.NewAnonymousClassInstance;
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.mock.web.MockHttpServletRequest;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.jdbc.Sql;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//import org.springframework.test.context.transaction.TransactionConfiguration;
//
//import com.demo2do.core.utils.DateUtils;
//import com.zufangbao.sun.entity.security.Principal;
//import com.zufangbao.earth.web.controller.financial.FinancialContractController;
//import com.zufangbao.sun.entity.account.Account;
//import com.zufangbao.sun.entity.financial.FinancialContract;
//import com.zufangbao.sun.entity.financial.TemporaryRepurchaseJson;
//import com.zufangbao.sun.service.FinancialContractService;
//import com.zufangbao.sun.utils.JsonUtils;
//import com.zufangbao.sun.yunxin.entity.model.CreateFinancialContractModel;
//import com.zufangbao.sun.yunxin.entity.model.ModifyFinancialContractBasicInfoModel;
//import com.zufangbao.sun.yunxin.entity.model.ModifyFinancialContractRemittanceInfoModel;
//import com.zufangbao.sun.yunxin.entity.model.ModifyFinancialContractRepaymentInfoModel;
//
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = {
//		"classpath:/local/applicationContext-*.xml", "classpath:/local/DispatcherServlet.xml" })
//@TransactionConfiguration(defaultRollback = false)
//@Transactional()
////@WebAppConfiguration(value="webapp")
//public class FinancialContractControllerTest{
//	@Autowired
//	private FinancialContractService financialContractService;
//	@Autowired
//	private FinancialContractController financialContractController;
//
//	private Principal principal;
//	private HttpServletRequest request;
//	private HttpServletResponse httpResponse;
//
//	@Before
//	public void setUp() {
//		principal = new Principal();
//		principal.setId(1L);
//
//		request = new MockHttpServletRequest();
//	}
//
//	@Test
//	@Sql("classpath:test/testAddNewFinancialContract.sql")
//	public void testAddNewFinancialContract() {
//
//		String model = "{\"financialContractNo\":\"jiangxu\"," +
//				"\"financialContractName\":\"test_repurchase\"," +
//				"\"companyUuid\":\"a02c02b9-6f98-11e6-bf08-00163e002839\"," +
//				"\"appId\":1,\"financialContractType\":\"0\"," +
//				"\"accountName\":\"aaaaa\",\"bankName\":\"工商银行\"," +
//				"\"accountNo\":\"vvvvv\",\"advaStartDate\":\"2016-03-28\",\"thruDate\":\"2019-03-30\",\"transactionLimitPerTranscation\":\"1\",\"transactionLimitPerDay\":\"6\",\"remittanceStrategyMode\":0,\"appAccounts\":\"\",\"assetPackageFormat\":\"0\",\"allowOnlineRepayment\":true,\"allowOfflineRepayment\":true,\"sysNormalDeductFlag\":true,\"sysOverdueDeductFlag\":true,\"allowAdvanceDeductFlag\":true,\"advaRepoTerm\":\"3\",\"advaRepaymentTerm\":\"1\",\"advaMatuterm\":\"\",\"sysCreatePenaltyFlag\":true,\"penalty\":\"\",\"overdueDefaultFee\":\"\",\"overdueServiceFee\":\"\",\"overdueOtherFee\":\"\",\"repurchaseCycle\":\"\",\"daysOfMonth\":[],\"temporaryRepurchases\":[],\"repurchaseRule\":\"0\",\"repurchaseApproach\":\"1\",\"repurchasePrincipalAlgorithm\":\"7+未偿利息+未偿逾期费用合计\",\"repurchaseInterestAlgorithm\":\"\",\"repurchasePenaltyAlgorithm\":\"\",\"repurchaseOtherChargesAlgorithm\":\"\",\"financialType\":\"0\",\"remittanceObject\":\"0\"}";
//		CreateFinancialContractModel createFinancialContractModel = JsonUtils.parse(model, CreateFinancialContractModel.class);
//
//		String response = financialContractController.addNewFinancialContract(model, principal,
//				request);
//		Map<String, Object> mso = JsonUtils.parse(response);
//		Assert.assertEquals("成功", mso.get("message"));
//		FinancialContract financialContract = financialContractService.getUniqueFinancialContractBy("jiangxu");
//		Assert.assertEquals("jiangxu", financialContract.getContractNo());
//		Assert.assertEquals("test_repurchase", financialContract.getContractName());
//		Assert.assertEquals(1, financialContract.getCompany().getId().intValue());
//		Assert.assertEquals(1, financialContract.getApp().getId().intValue());
//		Assert.assertEquals(0, financialContract.getFinancialContractType().ordinal());
//		Account account = financialContract.getCapitalAccount();
//		Assert.assertEquals("aaaaa", account.getAccountName());
//		Assert.assertEquals("工商银行", account.getBankName());
//		Assert.assertEquals("vvvvv", account.getAccountNo());
//		Assert.assertEquals("2016-03-28", DateUtils.format(financialContract.getAdvaStartDate()));
//		Assert.assertEquals("2019-03-30", DateUtils.format(financialContract.getThruDate()));
//		Assert.assertEquals(3, financialContract.getAdvaRepoTerm());
//		Assert.assertEquals(1, financialContract.getAdvaRepaymentTerm());
//		Assert.assertEquals(0, financialContract.getAdvaMatuterm());
//
//	}
//
//	@Test
//	@Sql("classpath:test/testAddNewFinancialContract.sql")
//	public void testAddNewFinancialContract1() {
//
//		String model = "{\"financialContractNo\":\"jiangxu\",\"financialContractName\":\"test_repurchase\",\"companyUuid\":\"a02c02b9-6f98-11e6-bf08-00163e002839\",\"appId\":1,\"financialContractType\":\"0\",\"accountName\":\"aaaaa\",\"bankName\":\"工商银行\",\"accountNo\":\"vvvvv\",\"advaStartDate\":\"2016-03-28\",\"thruDate\":\"2019-03-30\",\"transactionLimitPerTranscation\":\"1\",\"transactionLimitPerDay\":\"6\",\"remittanceStrategyMode\":1,\"appAccounts\":\"\",\"assetPackageFormat\":\"0\",\"allowOnlineRepayment\":true,\"allowOfflineRepayment\":true,\"sysNormalDeductFlag\":true,\"sysOverdueDeductFlag\":true,\"allowAdvanceDeductFlag\":true,\"advaRepoTerm\":\"3\",\"advaRepaymentTerm\":\"1\",\"advaMatuterm\":\"\",\"sysCreatePenaltyFlag\":true,\"penalty\":\"\",\"overdueDefaultFee\":\"\",\"overdueServiceFee\":\"\",\"overdueOtherFee\":\"\",\"repurchaseCycle\":\"\",\"daysOfMonth\":[],\"temporaryRepurchases\":[],\"repurchaseRule\":\"0\",\"repurchaseApproach\":\"1\",\"repurchasePrincipalAlgorithm\":\"7+未偿利息+未偿逾期费用合计\",\"repurchaseInterestAlgorithm\":\"\",\"repurchasePenaltyAlgorithm\":\"\",\"repurchaseOtherChargesAlgorithm\":\"\"}";
//		CreateFinancialContractModel createFinancialContractModel = JsonUtils.parse(model, CreateFinancialContractModel.class);
//
//		String response = financialContractController.addNewFinancialContract(model, principal,
//				request);
//
//		Map<String, Object> mso = JsonUtils.parse(response);
//		Assert.assertEquals("请求参数错误！", mso.get("message"));
//
//	}
//
//	@Test
//	@Sql("classpath:test/test4editFinancialContractBasicInfo.sql")
//	public void test_editFinancialContractBasicInfo() {
//		ModifyFinancialContractBasicInfoModel contractBasicInfoModel = new ModifyFinancialContractBasicInfoModel(
//				"nfqtest001", "测试合同", "a02c02b9-6f98-11e6-bf08-00163e002839", 1L, 0, "account_name", "bank_name", "account_no", "2016-11-11", "2016-11-15");
//
////		String model = JsonUtils.toJSONString(contractBasicInfoModel);
//        String model = "{\"accountName\":\"account_name\",\"accountNo\":\"account_no\",\"advaStartDate\":\"2016-11-11\",\"appId\":1,\"bankName\":\"bank_name\",\"companyUuid\":\"a02c02b9-6f98-11e6-bf08-00163e002839\",\"financialContractName\":\"测试合同\",\"financialContractNo\":\"nfqtest001\",\"financialContractType\":0,\"thruDate\":\"2016-11-15\",}";
//
//		String modifyResult = financialContractController.editFinancialContractBasicInfo(
//				"2d380fe1-7157-490d-9474-12c5a9901e29", model, principal, request);
//		Map<String, Object> mso = JsonUtils.parse(modifyResult);
//		Assert.assertEquals("成功", mso.get("message"));
//		FinancialContract fc = financialContractService.getFinancialContractBy("2d380fe1-7157-490d-9474-12c5a9901e29");
//		Assert.assertEquals("nfqtest001", fc.getContractNo());
//		Assert.assertEquals("测试合同", fc.getContractName());
//		Assert.assertEquals(1, fc.getCompany().getId().intValue());
//	}
//
//	@Test
//	@Sql("classpath:test/test4editFinancialContractBasicInfo.sql")
//	public void test_editFinancialContractBasicInfo1() {
//		ModifyFinancialContractBasicInfoModel contractBasicInfoModel = new ModifyFinancialContractBasicInfoModel(
//				"nfqtest001", "测试合同", "a02c02b9-6f98-11e6-bf08-00163e002839", 1L, 0, "account_name", "bank_name", "account_no", "2016-11-11", "2016-11-15");
//
////		String model = JsonUtils.toJSONString(contractBasicInfoModel);
//        String model = "{\"accountName\":\"account_name\",\"accountNo\":\"account_no\",\"advaStartDate\":\"2016-11-11\",\"appId\":1,\"bankName\":\"bank_name\",\"companyUuid\":\"a02c02b9-6f98-11e6-bf08-00163e002839\",\"financialContractName\":\"测试合同\",\"financialContractNo\":\"nfqtest001\",\"financialContractType\":0,\"thruDate\":\"2016-11-15\",}";
//
//		String modifyResult = financialContractController.editFinancialContractBasicInfo(
//				"2d380fe1-7157-490d-9474-12c5a9901e20", model, principal, request);
//		Map<String, Object> mso = JsonUtils.parse(modifyResult);
//		Assert.assertEquals("信托合同不存在！", mso.get("message"));
//	}
//
//	@Test
//	@Sql("classpath:test/test4editFinancialContractBasicInfo.sql")
//	public void test_editFinancialContractBasicInfo2() {
//		ModifyFinancialContractBasicInfoModel contractBasicInfoModel = new ModifyFinancialContractBasicInfoModel(
//				"nfqtest001", "测试合同", "a02c02b9-6f98-11e6-bf08-00163e002839", 1L, 0, "account_name", "bank_name", "account_no", null, "2016-11-15");
//
////		String model = JsonUtils.toJSONString(contractBasicInfoModel);
//        String model = "{\"accountName\":\"account_name\",\"accountNo\":\"account_no\",\"advaStartDate\":null,\"appId\":1,\"bankName\":\"bank_name\",\"companyUuid\":\"a02c02b9-6f98-11e6-bf08-00163e002839\",\"financialContractName\":\"测试合同\",\"financialContractNo\":\"nfqtest001\",\"financialContractType\":0,\"thruDate\":\"2016-11-15\",}";
//
//		String modifyResult = financialContractController.editFinancialContractBasicInfo(
//				"2d380fe1-7157-490d-9474-12c5a9901e29", model, principal, request);
//		Map<String, Object> mso = JsonUtils.parse(modifyResult);
//		Assert.assertEquals("请求参数错误！", mso.get("message"));
//	}
//
//	@Test
//	@Sql("classpath:test/test4editFinancialContractBasicInfo.sql")
//	public void test_editFinancialContractRemittanceInfo() {
//		ModifyFinancialContractRemittanceInfoModel contractRemittanceInfoModel = new ModifyFinancialContractRemittanceInfoModel(
//				new BigDecimal(10000), new BigDecimal(50000), 3, null);
//
////		String model = JsonUtils.toJSONString(contractRemittanceInfoModel);
//		String model = "{\"remittanceStrategyMode\":3,\"transactionLimitPerDay\":50000,\"transactionLimitPerTranscation\":10000}";
//		String fangkuan = financialContractController.editFinancialContractRemittanceInfo(
//				"2d380fe1-7157-490d-9474-12c5a9901e29", model, principal, request);
//		Map<String, Object> mso = JsonUtils.parse(fangkuan);
//		Assert.assertEquals("成功", mso.get("message"));
//		FinancialContract fc = financialContractService.getFinancialContractBy("2d380fe1-7157-490d-9474-12c5a9901e29");
//		Assert.assertEquals(50000, fc.getTransactionLimitPerDay().intValue());
//		Assert.assertEquals(10000, fc.getTransactionLimitPerTranscation().intValue());
//	}
//
//	@Test
//	@Sql("classpath:test/test4editFinancialContractBasicInfo.sql")
//	public void test_editFinancialContractRemittanceInfo1() {
//		ModifyFinancialContractRemittanceInfoModel contractRemittanceInfoModel = new ModifyFinancialContractRemittanceInfoModel(
//				new BigDecimal(10000), new BigDecimal(50000), 3, null);
//
////		String model = JsonUtils.toJSONString(contractRemittanceInfoModel);
//		String model = "{\"remittanceStrategyMode\":3,\"transactionLimitPerDay\":50000,\"transactionLimitPerTranscation\":10000}";
//		String fangkuan = financialContractController.editFinancialContractRemittanceInfo(
//				"2d380fe1-7157-490d-9474-12c5a9901e20", model, principal, request);
//		Map<String, Object> mso = JsonUtils.parse(fangkuan);
//		Assert.assertEquals("信托合同不存在！", mso.get("message"));
//	}
//
//	@Test
//	@Sql("classpath:test/test4editFinancialContractBasicInfo.sql")
//	public void test_editFinancialContractRemittanceInfo2() {
//		ModifyFinancialContractRemittanceInfoModel contractRemittanceInfoModel = new ModifyFinancialContractRemittanceInfoModel(
//				new BigDecimal(10000), new BigDecimal(50000), 1, null);
//
////		String model = JsonUtils.toJSONString(contractRemittanceInfoModel);
//		String model = "{\"remittanceStrategyMode\":1,\"transactionLimitPerDay\":50000,\"transactionLimitPerTranscation\":10000}";
//		String fangkuan = financialContractController.editFinancialContractRemittanceInfo(
//				"2d380fe1-7157-490d-9474-12c5a9901e29", model, principal, request);
//		Map<String, Object> mso = JsonUtils.parse(fangkuan);
//		Assert.assertEquals("请求参数错误！", mso.get("message"));
//	}
//
//	@Test
//	@Sql("classpath:test/test4editFinancialContractRepaymentInfo.sql")
//	public void test_editFinancialContractRepaymentInfo() {
//		String model = "{\"advaMatuterm\":0,\"advaRepaymentTerm\":1,\"advaRepoTerm\":3,\"allowAdvanceDeductFlag\":true,\"allowOfflineRepayment\":true,\"allowOnlineRepayment\":true,\"assetPackageFormat\":\"0\",\"daysOfCycle\":[4,5],\"financialContractUuid\":\"e2c178ac-be53-4574-b936-c5d40ba00691\",\"penalty\":\"\",\"repurchaseAlgorithm\":\"\",\"repurchaseApproach\":\"1\",\"repurchaseCycle\":1,\"repurchaseInterestAlgorithm\":\"\",\"repurchaseOtherChargesAlgorithm\":\"\",\"repurchasePenaltyAlgorithm\":\"\",\"repurchasePrincipalAlgorithm\":\"\",\"repurchaseRule\":\"1\",\"sysCreatePenaltyFlag\":true,\"sysNormalDeductFlag\":true,\"sysOverdueDeductFlag\":true,\"temporaryRepurchases\":[{\"repurchaseDate\":\"2017-05-13\",\"effectStartDate\":\"2017-05-01\",\"effectEndDate\":\"2017-05-18\"}],\"overdueDefaultFee\":\"\",\"overdueServiceFee\":\"\",\"overdueOtherFee\":\"\",\"daysOfMonth\":[]}";
//		ModifyFinancialContractRepaymentInfoModel modifyFinancialContractRepaymentInfoModel = JsonUtils.parse(model, ModifyFinancialContractRepaymentInfoModel.class);
//
//		String results = financialContractController.editFinancialContractRepaymentInfo(
//				"e2c178ac-be53-4574-b936-c5d40ba00691", model, principal, request);
//		Map<String, Object> mso = JsonUtils.parse(results);
//		Assert.assertEquals("成功", mso.get("message"));
//		FinancialContract fc = financialContractService.getFinancialContractBy("e2c178ac-be53-4574-b936-c5d40ba00691");
////		Assert.assertEquals(1000, fc.getOverdueDefaultFee().intValue());
////		Assert.assertEquals(100, fc.getOverdueServiceFee().intValue());
//		Assert.assertEquals(new Integer(4), fc.getDaysOfCycleList().get(0));
//		Assert.assertEquals(DateUtils.asDay("2017-05-13"), fc.getTemporaryRepurchaseList().get(0).getRepurchaseDate());
//	}
//
//	@Test
//	@Sql("classpath:test/test4editFinancialContractRepaymentInfo.sql")
//	public void test_editFinancialContractRepaymentInfo1() {
//		String model = "{\"advaMatuterm\":0,\"advaRepaymentTerm\":1,\"advaRepoTerm\":3,\"allowAdvanceDeductFlag\":true,\"allowOfflineRepayment\":true,\"allowOnlineRepayment\":true,\"assetPackageFormat\":\"0\",\"daysOfCycle\":[4,5],\"financialContractUuid\":\"e2c178ac-be53-4574-b936-c5d40ba00691\",\"penalty\":\"\",\"repurchaseAlgorithm\":\"\",\"repurchaseApproach\":\"1\",\"repurchaseCycle\":1,\"repurchaseInterestAlgorithm\":\"\",\"repurchaseOtherChargesAlgorithm\":\"\",\"repurchasePenaltyAlgorithm\":\"\",\"repurchasePrincipalAlgorithm\":\"\",\"repurchaseRule\":\"1\",\"sysCreatePenaltyFlag\":true,\"sysNormalDeductFlag\":true,\"sysOverdueDeductFlag\":true,\"temporaryRepurchases\":[{\"repurchaseDate\":\"2017-05-03\",\"effectStartDate\":\"2017-05-01\",\"effectEndDate\":\"2017-05-18\"}],\"overdueDefaultFee\":\"\",\"overdueServiceFee\":\"\",\"overdueOtherFee\":\"\",\"daysOfMonth\":[]}";
//		ModifyFinancialContractRepaymentInfoModel modifyFinancialContractRepaymentInfoModel = JsonUtils.parse(model, ModifyFinancialContractRepaymentInfoModel.class);
//
//		String results = financialContractController.editFinancialContractRepaymentInfo(
//				"2d380fe1-7157-490d-9474-12c5a9901e20", model, principal, request);
//		Map<String, Object> mso = JsonUtils.parse(results);
//		Assert.assertEquals("信托合同不存在！", mso.get("message"));
//	}
//
//	@Test
//	@Sql("classpath:test/test4editFinancialContractRepaymentInfo.sql")
//	public void test_editFinancialContractRepaymentInfo2() {
//		String model = "{\"advaMatuterm\":null,\"advaRepaymentTerm\":null,\"advaRepoTerm\":3,\"allowAdvanceDeductFlag\":true,\"allowOfflineRepayment\":true,\"allowOnlineRepayment\":true,\"assetPackageFormat\":\"0\",\"daysOfCycle\":[4,5],\"financialContractUuid\":\"e2c178ac-be53-4574-b936-c5d40ba00691\",\"penalty\":\"\",\"repurchaseAlgorithm\":\"\",\"repurchaseApproach\":\"1\",\"repurchaseCycle\":1,\"repurchaseInterestAlgorithm\":\"\",\"repurchaseOtherChargesAlgorithm\":\"\",\"repurchasePenaltyAlgorithm\":\"\",\"repurchasePrincipalAlgorithm\":\"\",\"repurchaseRule\":\"1\",\"sysCreatePenaltyFlag\":true,\"sysNormalDeductFlag\":true,\"sysOverdueDeductFlag\":true,\"temporaryRepurchases\":[{\"repurchaseDate\":\"2017-05-03\",\"effectStartDate\":\"2017-05-01\",\"effectEndDate\":\"2017-05-18\"}],\"overdueDefaultFee\":\"\",\"overdueServiceFee\":\"\",\"overdueOtherFee\":\"\",\"daysOfMonth\":[]}";
//		ModifyFinancialContractRepaymentInfoModel modifyFinancialContractRepaymentInfoModel = JsonUtils.parse(model, ModifyFinancialContractRepaymentInfoModel.class);
//
//		String results = financialContractController.editFinancialContractRepaymentInfo(
//				"e2c178ac-be53-4574-b936-c5d40ba00691", model, principal, request);
//		Map<String, Object> mso = JsonUtils.parse(results);
//		Assert.assertEquals("请求参数错误！", mso.get("message"));
//	}
//
//	@Test
//	@Sql("classpath:test/test4editFinancialContractRepaymentInfo.sql")
//	public void test_createTemporaryRepurchases() {
//		String model = "{\"repurchaseDate\":\"2017-05-11\",\"effectStartDate\":\"2017-05-02\",\"effectEndDate\":\"2017-05-31\"}";
//		TemporaryRepurchaseJson tr = JsonUtils.parse(model, TemporaryRepurchaseJson.class);
//
//		String results = financialContractController.createTemporaryRepurchases(
//				"e2c178ac-be53-4574-b936-c5d40ba00691", model, principal, request);
//		Map<String, Object> mso = JsonUtils.parse(results);
//		Assert.assertEquals("成功", mso.get("message"));
//		FinancialContract fc = financialContractService.getFinancialContractBy("e2c178ac-be53-4574-b936-c5d40ba00691");
//		Assert.assertEquals(DateUtils.asDay("2017-05-03"), fc.getTemporaryRepurchaseList().get(0).getRepurchaseDate());
//		Assert.assertEquals(DateUtils.asDay("2017-05-18"), fc.getTemporaryRepurchaseList().get(1).getRepurchaseDate());
//		Assert.assertEquals(DateUtils.asDay("2017-05-11"), fc.getTemporaryRepurchaseList().get(2).getRepurchaseDate());
//	}
//
//	@Test
//	@Sql("classpath:test/test4editFinancialContractRepaymentInfo.sql")
//	public void test_deleteTemporaryRepurchases() {
//		String model = "{\"effectEndDate\":\"2017-05-25\",\"effectStartDate\":\"2017-05-16\",\"repurchaseDate\":\"2017-05-18\",\"repurchaseUuid\":\"bda64296-e19d-49ed-81e4-c99628b3943d\"}";
//		TemporaryRepurchaseJson tr = JsonUtils.parse(model, TemporaryRepurchaseJson.class);
//
//		String results = financialContractController.deleteTemporaryRepurchases(
//				"e2c178ac-be53-4574-b936-c5d40ba00691", model, principal, request);
//		Map<String, Object> mso = JsonUtils.parse(results);
//		Assert.assertEquals("成功", mso.get("message"));
//		FinancialContract fc = financialContractService.getFinancialContractBy("e2c178ac-be53-4574-b936-c5d40ba00691");
//		Assert.assertEquals(1, fc.getTemporaryRepurchaseList().size());
//		Assert.assertEquals(DateUtils.asDay("2017-05-03"), fc.getTemporaryRepurchaseList().get(0).getRepurchaseDate());
//	}
//
//	@Test
//	@Sql("classpath:test/test4editFinancialContractRepaymentInfo.sql")
//	public void test_editTemporaryRepurchases() {
//		String model = "{\"effectEndDate\":\"2017-05-25\",\"effectStartDate\":\"2017-04-16\",\"repurchaseDate\":\"2017-05-18\",\"repurchaseUuid\":\"bda64296-e19d-49ed-81e4-c99628b3943d\"}";
//		TemporaryRepurchaseJson tr = JsonUtils.parse(model, TemporaryRepurchaseJson.class);
//
//		String results = financialContractController.editTemporaryRepurchases(
//				"e2c178ac-be53-4574-b936-c5d40ba00691", model, principal, request);
//		Map<String, Object> mso = JsonUtils.parse(results);
//		Assert.assertEquals("成功", mso.get("message"));
//		FinancialContract fc = financialContractService.getFinancialContractBy("e2c178ac-be53-4574-b936-c5d40ba00691");
//		Assert.assertEquals(2, fc.getTemporaryRepurchaseList().size());
//		Assert.assertEquals(DateUtils.asDay("2017-05-03"), fc.getTemporaryRepurchaseList().get(0).getRepurchaseDate());
//		Assert.assertEquals(DateUtils.asDay("2017-05-18"), fc.getTemporaryRepurchaseList().get(1).getRepurchaseDate());
//		Assert.assertEquals(DateUtils.asDay("2017-04-16"), fc.getTemporaryRepurchaseList().get(1).getEffectStartDate());
//	}
//
//}
