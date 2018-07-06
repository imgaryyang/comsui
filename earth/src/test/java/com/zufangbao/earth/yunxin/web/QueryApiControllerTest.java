//package com.zufangbao.earth.yunxin.web;
//
//import com.alibaba.fastjson.JSONObject;
//import com.demo2do.core.utils.JsonUtils;
//import com.zufangbao.earth.yunxin.api.controller.QueryApiController;
//import com.zufangbao.earth.yunxin.api.model.query.AccountTradeDetailModel;
//import com.zufangbao.earth.yunxin.api.model.query.RepaymentListQueryModel;
//import com.zufangbao.earth.yunxin.api.model.query.RepaymentPlanBatchQueryModel;
//import com.zufangbao.earth.yunxin.api.model.query.RepaymentPlanQueryModel;
//import com.zufangbao.gluon.exception.ApiException;
//import com.zufangbao.gluon.spec.earth.v3.ApiResponseCode;
//import com.zufangbao.sun.api.model.repayment.AssetPackageBatchQueryModel;
//import org.junit.Assert;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.mock.web.MockHttpServletRequest;
//import org.springframework.mock.web.MockHttpServletResponse;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.jdbc.Sql;
//import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//import org.springframework.test.context.transaction.TransactionConfiguration;
//import org.springframework.test.context.web.WebAppConfiguration;
//
//import javax.transaction.Transactional;
//import java.util.UUID;
//
//import static org.junit.Assert.assertEquals;
//
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = {
//
//		"classpath:/local/applicationContext-*.xml",
//		"classpath:/DispatcherServlet.xml" })
//@TransactionConfiguration(defaultRollback = true)
//@Transactional()
//@WebAppConfiguration(value="webapp")
//public class QueryApiControllerTest extends AbstractTransactionalJUnit4SpringContextTests{
//
//	@Autowired
//	private QueryApiController queryApiController;
//
//	private MockHttpServletResponse response = new MockHttpServletResponse();
//
//	private MockHttpServletRequest request = new MockHttpServletRequest();
//
//
//	@Test
//	@Sql("classpath:test/yunxin/queryApi/testQueryRepaymentPlan.sql")
//	public void testQueryRepaymentPlan_WRONG_FORMAT(){
//		RepaymentPlanQueryModel queryModel = null;
//		try {
//			String result = queryApiController.queryRepaymentPlan(queryModel, response);
//		} catch (ApiException e) {
//			Assert.assertEquals(ApiResponseCode.WRONG_FORMAT,e.getCode());
//		}
//	}
//	@Test
//	@Sql("classpath:test/yunxin/queryApi/testQueryRepaymentPlan.sql")
//	public void testQueryRepaymentPlan_INVALID_PARAMS(){
//		String requestParamInfo = new  String("{\"contractNo\":\"云信信2016-78-DK(ZQ2016042522479)\",\"requestNo\":\"\",\"uniqueId\":\"123\"}");
//		RepaymentPlanQueryModel queryModel = JsonUtils.parse(requestParamInfo, RepaymentPlanQueryModel.class);
//		try {
//			String result = queryApiController.queryRepaymentPlan(queryModel, response);
//		} catch (ApiException e) {
//			Assert.assertEquals(ApiResponseCode.INVALID_PARAMS,e.getCode());
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//	}
//	@Test
//	@Sql("classpath:test/yunxin/queryApi/testQueryRepaymentPlan.sql")
//	public void testQueryRepaymentPlan(){
//		String requestParamInfo = new  String("{\"contractNo\":\"云信信2016-78-DK(ZQ2016042522478)\",\"requestNo\":\"123566\",\"uniqueId\":\"\"}");
//		RepaymentPlanQueryModel queryModel = JsonUtils.parse(requestParamInfo, RepaymentPlanQueryModel.class);
//			String result = queryApiController.queryRepaymentPlan(queryModel, response);
//			Assert.assertEquals("{\"code\":0,\"data\":{\"repaymentPlanDetails\":[{\"currentPeriod\":1,\"loanFees\":\"0\",\"otherFees\":\"0\",\"outerRepaymentPlanNo\":\"repayScheduleNo2MD5\",\"penaltyFee\":\"0\",\"planRepaymentDate\":\"2016-05-17\",\"planRepaymentInterest\":\"1200.00\",\"planRepaymentPrincipal\":\"0.00\",\"repayScheduleNo\":\"repayScheduleNo2\",\"repaymentExecutionState\":\"未到期\",\"repaymentNumber\":\"ZC27367D23EF4A36F3\",\"technicalServicesFees\":\"0\",\"totalOverdueFee\":\"0\"}]},\"message\":\"成功!\"}",result);
////			Assert.assertEquals("{\"code\":0,\"data\":{\"repaymentPlanDetails\":[{\"loanFees\":\"0\",\"otherFees\":\"0\",\"penaltyFee\":\"0\",\"planRepaymentDate\":\"2016-05-17\",\"planRepaymentInterest\":\"1200.00\",\"planRepaymentPrincipal\":\"0.00\",\"repaymentExecutionState\":\"未到期\",\"repaymentNumber\":\"ZC27367D23EF4A36F3\",\"technicalServicesFees\":\"0\",\"totalOverdueFee\":\"0\"}]},\"message\":\"成功!\"}",result);
//	}
//	@Test
//	@Sql("classpath:test/yunxin/queryApi/testQueryRepaymentPlan.sql")
//	public void testQueryRepaymentList_WRONG_FORMAT(){
//		RepaymentListQueryModel queryModel = null;
//		try {
//			String result = queryApiController.queryRepaymentList(queryModel, response);
//		} catch (ApiException e) {
//			Assert.assertEquals(ApiResponseCode.WRONG_FORMAT,e.getCode());
//		}
//	}
//	@Test
//	@Sql("classpath:test/yunxin/queryApi/testQueryRepaymentPlan.sql")
//	public void testQueryRepaymentList_DATE_RANGE_ERROR(){
//		String requestParamInfo = new  String("{\"requestNo\":\"\",\"financialContractNo\":\"22\",\"queryStartDate\":\"2016-05-17\",\"queryEndDate\":\"2016-05-17\"}");
//		RepaymentListQueryModel queryModel = JsonUtils.parse(requestParamInfo, RepaymentListQueryModel.class);
//		try {
//			String result = queryApiController.queryRepaymentList(queryModel, response);
//		} catch (ApiException e) {
//			Assert.assertEquals(ApiResponseCode.DATE_RANGE_ERROR,e.getCode());
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//	}
//	@Test
//	@Sql("classpath:test/yunxin/queryApi/testQueryRepaymentPlan.sql")
//	public void testQueryRepaymentList_DATE_RANGE_NOT_ALLOWED_THAN_THREE_MONTHS(){
//		String requestParamInfo = new  String("{\"requestNo\":\"123456\",\"financialContractNo\":\"22\",\"queryStartDate\":\"2016-05-17\",\"queryEndDate\":\"2016-10-17\"}");
//		RepaymentListQueryModel queryModel = JsonUtils.parse(requestParamInfo, RepaymentListQueryModel.class);
//		try {
//			String result = queryApiController.queryRepaymentList(queryModel, response);
//		} catch (ApiException e) {
//			Assert.assertEquals(ApiResponseCode.DATE_RANGE_NOT_ALLOWED_THAN_THREE_MONTHS,e.getCode());
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//	}
//	@Test
//	@Sql("classpath:test/yunxin/queryApi/testQueryRepaymentPlan.sql")
//	public void testQueryRepaymentList(){
//		String requestParamInfo = new  String("{\"requestNo\":\"123456\",\"financialContractNo\":\"云信信2016-78-DK(ZQ2016042522479)\",\"queryStartDate\":\"2016-05-17\",\"queryEndDate\":\"2016-7-17\"}");
//		RepaymentListQueryModel queryModel = JsonUtils.parse(requestParamInfo, RepaymentListQueryModel.class);
//			String result = queryApiController.queryRepaymentList(queryModel, response);
//			Assert.assertEquals("{\"code\":0,\"data\":{\"repaymentListDetail\":[{\"assetSetNo\":\"\",\"contractNo\":\"云信信2016-78-DK(ZQ2016042522479)\",\"deductAccountBank\":\"1\",\"deductAccountName\":\"1\",\"deductAccountNo\":\"1\",\"deductAmount\":\"1.00\",\"deductDate\":\"2016-05-17\",\"deductRequestNo\":\"dfasdf231231\",\"flowNo\":\"1\",\"results\":\"处理中\",\"uniqueId\":\"123\"}]},\"message\":\"成功!\"}",result);
//	}
//	@Test
//	@Sql("classpath:test/yunxin/queryApi/testQueryRepaymentPlan.sql")
//	public void queryBatchRepaymentPlans_INVALID_PARAMS(){
//		String requestParamInfo = new  String("{\"requestNo\":\"123456\",\"uniqueIds\":[{\"uniqueId\":\"123\"}],\"uniqueIdsList\":[{\"uniqueId\":\"1234\"}],\"productCode\":\"\",\"planRepaymentDate\":\"2016-06-02\",\"checkFailedMsg\":\"校验信息\"}");
//		RepaymentPlanBatchQueryModel queryModel = JsonUtils.parse(requestParamInfo, RepaymentPlanBatchQueryModel.class);
//			try {
//				String result = queryApiController.queryBatchRepaymentPlans(queryModel, response);
//			} catch (ApiException e) {
//				Assert.assertEquals(ApiResponseCode.INVALID_PARAMS,e.getCode());
//			}
//	}
//	@Test
//	@Sql("classpath:test/yunxin/queryApi/testQueryBatchRepaymentPlans.sql")
//	public void queryBatchRepaymentPlans(){
//		String requestParamInfo = new  String("{\"requestNo\":\"123456\",\"uniqueIds\":[\"123\"],\"uniqueIdsList\":[\"1234\"],\"productCode\":\"云信信2016-78-DK(ZQ2016042522479)\",\"planRepaymentDate\":\"2016-06-02\",\"checkFailedMsg\":\"校验信息\"}");
//		RepaymentPlanBatchQueryModel queryModel = JsonUtils.parse(requestParamInfo, RepaymentPlanBatchQueryModel.class);
//				String result = queryApiController.queryBatchRepaymentPlans(queryModel, response);
//				Assert.assertEquals("{\"code\":0,\"data\":{\"repaymentPlanDatas\":[{\"repaymentPlanDetails\":[{\"loanFees\":\"0\",\"otherFees\":\"0\",\"outerRepaymentPlanNo\":\"repayScheduleNo1\",\"penaltyFee\":\"0\",\"planRepaymentDate\":\"2016-06-02\",\"planRepaymentInterest\":\"1200.00\",\"planRepaymentPrincipal\":\"0.00\",\"repayScheduleNo\":\"repayScheduleNo1MD5\",\"repaymentExecutionState\":\"未到期\",\"repaymentNumber\":\"ZC27367D23EF4A36F3\",\"technicalServicesFees\":\"0\",\"totalOverdueFee\":\"0\"}],\"uniqueId\":\"123\"}]},\"message\":\"成功!\"}",result);
//	}
//
//	@Test
//	@Sql("classpath:test/yunxin/queryApi/test_queryBatchAssetPackages.sql")
//	public void test_queryBatchAssetPackages(){
//		String requestParamInfo = new  String("{\"requestNo\":\"123456\"}");
//		AssetPackageBatchQueryModel queryModel = JsonUtils.parse(requestParamInfo,AssetPackageBatchQueryModel.class);
//				String result = queryApiController.queryBatchAssetPackages(queryModel, response);
//				Assert.assertEquals("{\"code\":20001,\"message\":\"信托/信贷产品代码［productCode］，不能为空！\"}",result);
//	}
//
//	@Test
//	@Sql("classpath:test/yunxin/queryApi/test_queryBatchAssetPackages.sql")
//	public void test_queryBatchAssetPackages_1(){
//		String requestParamInfo = new  String("{\"requestNo\":\"123456\",\"productCode\":\"G31700\",\"uniqueIds\":[\"CONTRACT_UNIQUEID_ID001\"],\"contractNos\":[\"CONTRACT_NO001\"]}");
//		AssetPackageBatchQueryModel queryModel = JsonUtils.parse(requestParamInfo,AssetPackageBatchQueryModel.class);
//		String result = queryApiController.queryBatchAssetPackages(queryModel, response);
//		Assert.assertEquals("{\"code\":20001,\"message\":\"贷款合同唯一编号集［uniqueIds］、贷款合同编号集［contractNos］不能同时存在！\"}",result);
//	}
//
//	@Test
//	@Sql("classpath:test/yunxin/queryApi/test_queryBatchAssetPackages.sql")
//	public void test_queryBatchAssetPackages_2(){
//		String requestParamInfo = new  String("{\"requestNo\":\"123456\",\"productCode\":\"G08200\",\"uniqueIds\":[\"CONTRACT_UNIQUEID_ID001\"]}");
//		AssetPackageBatchQueryModel queryModel = JsonUtils.parse(requestParamInfo,AssetPackageBatchQueryModel.class);
//				String result = queryApiController.queryBatchAssetPackages(queryModel, response);
//				Assert.assertEquals("{\"code\":0,\"data\":{\"assetPackageList\":[{\"contractNo\":\"CONTRACT_NO001\",\"importTime\":\"2017-02-15\",\"status\":0,\"uniqueId\":\"CONTRACT_UNIQUEID_ID001\"}],\"productCode\":\"G08200\"},\"message\":\"成功!\"}",result);
//	}
//
//	@Test
//	@Sql("classpath:test/yunxin/queryApi/test_queryBatchAssetPackages.sql")
//	public void test_queryBatchAssetPackages_3(){
//		String requestParamInfo = new  String("{\"requestNo\":\"123456\",\"productCode\":\"G08200\",\"contractNos\":[\"CONTRACT_NO001\"]}");
//		AssetPackageBatchQueryModel queryModel = JsonUtils.parse(requestParamInfo,AssetPackageBatchQueryModel.class);
//				String result = queryApiController.queryBatchAssetPackages(queryModel, response);
//				Assert.assertEquals("{\"code\":0,\"data\":{\"assetPackageList\":[{\"contractNo\":\"CONTRACT_NO001\",\"importTime\":\"2017-02-15\",\"status\":0,\"uniqueId\":\"CONTRACT_UNIQUEID_ID001\"}],\"productCode\":\"G08200\"},\"message\":\"成功!\"}",result);
//	}
//
//	@Test
//	@Sql("classpath:test/yunxin/queryApi/test_queryBatchAssetPackages.sql")
//	public void test_queryBatchAssetPackages_4(){
//		String requestParamInfo = new  String("{\"requestNo\":\"123456\",\"productCode\":\"G08200\",\"contractNos\":[\"CONTRACT_NULL\"]}");
//		AssetPackageBatchQueryModel queryModel = JsonUtils.parse(requestParamInfo,AssetPackageBatchQueryModel.class);
//				String result = queryApiController.queryBatchAssetPackages(queryModel, response);
//				Assert.assertEquals("{\"code\":0,\"data\":{\"assetPackageList\":[{\"contractNo\":\"CONTRACT_NULL\",\"importTime\":\"\",\"status\":2,\"uniqueId\":\"\"}],\"productCode\":\"G08200\"},\"message\":\"成功!\"}",result);
//	}
//
//	@Test
//	@Sql("classpath:test/yunxin/queryApi/test_queryBatchAssetPackages.sql")
//	public void test_queryBatchAssetPackages_5(){
//		String requestParamInfo = new  String("{\"requestNo\":\"123456\",\"productCode\":\"G08200\",\"uniqueIds\":[\"uniqueId_null\"]}");
//		AssetPackageBatchQueryModel queryModel = JsonUtils.parse(requestParamInfo,AssetPackageBatchQueryModel.class);
//				String result = queryApiController.queryBatchAssetPackages(queryModel, response);
//				Assert.assertEquals("{\"code\":0,\"data\":{\"assetPackageList\":[{\"contractNo\":\"\",\"importTime\":\"\",\"status\":2,\"uniqueId\":\"uniqueId_null\"}],\"productCode\":\"G08200\"},\"message\":\"成功!\"}",result);
//	}
//
//	@Test
//	@Sql("classpath:test/yunxin/queryApi/test_queryBatchAssetPackages.sql")
//	public void test_queryBatchAssetPackages_6(){
//		String requestParamInfo = new  String("{\"requestNo\":\"123456\",\"productCode\":\"G08200\")}");
//		AssetPackageBatchQueryModel queryModel = JsonUtils.parse(requestParamInfo,AssetPackageBatchQueryModel.class);
//				String result = queryApiController.queryBatchAssetPackages(queryModel, response);
//				Assert.assertEquals("{\"code\":20001,\"message\":\"startTime或endTime格式不正确，应为[yyyy-MM-dd]。\"}",result);
//	}
//
//	@Test
//	@Sql("classpath:test/yunxin/queryApi/test_queryBatchAssetPackages.sql")
//	public void test_queryBatchAssetPackages_7(){
//		String requestParamInfo = new  String("{\"requestNo\":\"123456\",\"productCode\":\"G08200\"),\"startTime\":\"2016-09-01\",\"endTime\":\"2016-09-01\"}");
//		AssetPackageBatchQueryModel queryModel = JsonUtils.parse(requestParamInfo,AssetPackageBatchQueryModel.class);
//				String result = queryApiController.queryBatchAssetPackages(queryModel, response);
//				Assert.assertEquals("{\"code\":0,\"data\":{\"assetPackageList\":[],\"productCode\":\"G08200\"},\"message\":\"成功!\"}",result);
//	}
//
//	/**
//	 * 错误的产品代码
//	 */
//	@Test
//	@Sql("classpath:test/yunxin/queryApi/test_queryApiTradeListAccount.sql")
//	public void testDirectBankApiWrongParameterProductCode(){
//		AccountTradeDetailModel accountTradeDetailModel = new AccountTradeDetailModel();
//		accountTradeDetailModel.setRequestNo(UUID.randomUUID().toString());
//		accountTradeDetailModel.setProductCode("111");
//		accountTradeDetailModel.setCapitalAccountNo("591902896710203");
//		accountTradeDetailModel.setPaymentInstitutionName(2);
//		accountTradeDetailModel.setStartTime("2017-01-01 08:00:00");
//		accountTradeDetailModel.setEndTime("2017-08-01 08:00:00");
//		accountTradeDetailModel.setPage(1);
//		String result = queryApiController
//				.queryAccountTradeDetailDirectBank(accountTradeDetailModel, request, response);
//		JSONObject jsonObject = JSONObject.parseObject(result);
//		assertEquals("30003", jsonObject.getString("code"));
//	}
//
//	/**
//	 * 错误的帐号
//	 */
//	@Test
//	@Sql("classpath:test/yunxin/queryApi/test_queryApiTradeListAccount.sql")
//	public void testDirectBankApiWrongParameterCapitalAccountNo(){
//		AccountTradeDetailModel accountTradeDetailModel = new AccountTradeDetailModel();
//		accountTradeDetailModel.setRequestNo(UUID.randomUUID().toString());
//		accountTradeDetailModel.setProductCode("AND001");
//		accountTradeDetailModel.setCapitalAccountNo("");
//		accountTradeDetailModel.setPaymentInstitutionName(2);
//		accountTradeDetailModel.setStartTime("2017-01-01 08:00:00");
//		accountTradeDetailModel.setEndTime("2017-08-01 08:00:00");
//		accountTradeDetailModel.setPage(1);
//		String result = queryApiController
//				.queryAccountTradeDetailDirectBank(accountTradeDetailModel, request, response);
//		JSONObject jsonObject = JSONObject.parseObject(result);
//	}
//	/**
//	 * 支付网关非法
//	 */
//	@Test
//	@Sql("classpath:test/yunxin/queryApi/test_queryApiTradeListAccount.sql")
//	public void testDirectBankApiWrongParameterPayment(){
//		AccountTradeDetailModel accountTradeDetailModel = new AccountTradeDetailModel();
//		accountTradeDetailModel.setRequestNo(UUID.randomUUID().toString());
//		accountTradeDetailModel.setProductCode("AND001");
//		accountTradeDetailModel.setCapitalAccountNo("591902896710203");
//		accountTradeDetailModel.setPaymentInstitutionName(-1);
//		accountTradeDetailModel.setStartTime("2017-01-01 08:00:00");
//		accountTradeDetailModel.setEndTime("2017-08-01 08:00:00");
//		accountTradeDetailModel.setPage(1);
//		String result = queryApiController
//				.queryAccountTradeDetailDirectBank(accountTradeDetailModel, request, response);
//		JSONObject jsonObject = JSONObject.parseObject(result);
//		assertEquals("38006",jsonObject.getString("code") );
//	}
//
//	/**
//	 * 开始时间为空
//	 */
//	@Test
//	@Sql("classpath:test/yunxin/queryApi/test_queryApiTradeListAccount.sql")
//	public void testDirectBankApiWrongParameterStartTime(){
//		AccountTradeDetailModel accountTradeDetailModel = new AccountTradeDetailModel();
//		accountTradeDetailModel.setRequestNo(UUID.randomUUID().toString());
//		accountTradeDetailModel.setProductCode("AND001");
//		accountTradeDetailModel.setCapitalAccountNo("591902896710203");
//		accountTradeDetailModel.setPaymentInstitutionName(1);
//		accountTradeDetailModel.setStartTime("");
//		accountTradeDetailModel.setEndTime("2017-08-01 08:00:00");
//		accountTradeDetailModel.setPage(1);
//		String result = queryApiController
//				.queryAccountTradeDetailDirectBank(accountTradeDetailModel, request, response);
//		JSONObject jsonObject = JSONObject.parseObject(result);
//		assertEquals("38007", jsonObject.getString("code"));
//	}
//	/**
//	 * 结束时间为空
//	 */
//	@Test
//	@Sql("classpath:test/yunxin/queryApi/test_queryApiTradeListAccount.sql")
//	public void testDirectBankApiWrongParameterEndTime(){
//		AccountTradeDetailModel accountTradeDetailModel = new AccountTradeDetailModel();
//		accountTradeDetailModel.setRequestNo(UUID.randomUUID().toString());
//		accountTradeDetailModel.setProductCode("AND001");
//		accountTradeDetailModel.setCapitalAccountNo("591902896710203");
//		accountTradeDetailModel.setPaymentInstitutionName(1);
//		accountTradeDetailModel.setStartTime("2017-08-01 08:00:00");
//		accountTradeDetailModel.setEndTime("");
//		accountTradeDetailModel.setPage(1);
//		String result = queryApiController
//				.queryAccountTradeDetailDirectBank(accountTradeDetailModel, request, response);
//		JSONObject jsonObject = JSONObject.parseObject(result);
//		assertEquals("38008", jsonObject.getString("code"));
//	}
//
//	/**
//	 * 开始时间大于结束时间
//	 */
//	@Test
//	@Sql("classpath:test/yunxin/queryApi/test_queryApiTradeListAccount.sql")
//	public void testDirectBankApiWrongParameterStartAndEndTime(){
//		AccountTradeDetailModel accountTradeDetailModel = new AccountTradeDetailModel();
//		accountTradeDetailModel.setRequestNo(UUID.randomUUID().toString());
//		accountTradeDetailModel.setProductCode("AND001");
//		accountTradeDetailModel.setCapitalAccountNo("591902896710203");
//		accountTradeDetailModel.setPaymentInstitutionName(2);
//		accountTradeDetailModel.setStartTime("2017-08-01 08:00:00");
//		accountTradeDetailModel.setEndTime("2017-01-01 08:00:00");
//		accountTradeDetailModel.setPage(1);
//		String result = queryApiController
//				.queryAccountTradeDetailDirectBank(accountTradeDetailModel, request, response);
//		JSONObject jsonObject = JSONObject.parseObject(result);
//		assertEquals("38002", jsonObject.getString("code"));
//	}
//
//	/**
//	 * 页数参数为空
//	 */
//	@Test
//	@Sql("classpath:test/yunxin/queryApi/test_queryApiTradeListAccount.sql")
//	public void testDirectBankApiWrongParameterPage(){
//		AccountTradeDetailModel accountTradeDetailModel = new AccountTradeDetailModel();
//		accountTradeDetailModel.setRequestNo(UUID.randomUUID().toString());
//		accountTradeDetailModel.setProductCode("AND001");
//		accountTradeDetailModel.setCapitalAccountNo("591902896710203");
//		accountTradeDetailModel.setPaymentInstitutionName(2);
//		accountTradeDetailModel.setStartTime("2017-08-01 08:00:00");
//		accountTradeDetailModel.setEndTime("2017-01-01 08:00:00");
//		accountTradeDetailModel.setPage(null);
//		String result = queryApiController
//				.queryAccountTradeDetailDirectBank(accountTradeDetailModel, request, response);
//		JSONObject jsonObject = JSONObject.parseObject(result);
//		assertEquals("38009", jsonObject.getString("code"));
//	}
//	/**
//	 * 查询第一页
//	 */
//	@Test
//	@Sql({"classpath:test/yunxin/queryApi/test_queryApiTradeListAccount.sql"
//			,"classpath:test/yunxin/queryApi/test_insert_into_cash_flow_with1000rows.sql"})
//	public void testDirectBankApiPage1(){
//		AccountTradeDetailModel accountTradeDetailModel = new AccountTradeDetailModel();
//		accountTradeDetailModel.setRequestNo(UUID.randomUUID().toString());
//		accountTradeDetailModel.setProductCode("AND001");
//		accountTradeDetailModel.setCapitalAccountNo("591902896710203");
//		accountTradeDetailModel.setPaymentInstitutionName(2);
//		accountTradeDetailModel.setStartTime("2017-05-18 00:00:00");
//		accountTradeDetailModel.setEndTime("2017-05-19 00:00:00");
//		accountTradeDetailModel.setPage(1);
//		String result = queryApiController
//				.queryAccountTradeDetailDirectBank(accountTradeDetailModel, request, response);
//		JSONObject jsonObject = JSONObject.parseObject(result);
//		assertEquals("0", jsonObject.getString("code"));
//		assertEquals(false, jsonObject.getJSONObject("data").getJSONObject("accountTradeDetail").getBoolean("hasNextPage"));
//		assertEquals(1000, jsonObject.getJSONObject("data")
//				.getJSONObject("accountTradeDetail")
//				.getJSONArray("accountTradeList")
//				.size());
//	}
//
//	/**
//	 * 查询在有2000页的情况下
//	 */
//	@Test
//	@Sql({"classpath:test/yunxin/queryApi/test_queryApiTradeListAccount.sql"
//			,"classpath:test/yunxin/queryApi/test_insert_into_cash_flow_with2000rows.sql"})
//	public void testDirectBankApiPage2(){
//		AccountTradeDetailModel accountTradeDetailModel = new AccountTradeDetailModel();
//		accountTradeDetailModel.setRequestNo(UUID.randomUUID().toString());
//		accountTradeDetailModel.setProductCode("AND001");
//		accountTradeDetailModel.setCapitalAccountNo("591902896710203");
//		accountTradeDetailModel.setPaymentInstitutionName(2);
//		accountTradeDetailModel.setStartTime("2017-05-18 00:00:00");
//		accountTradeDetailModel.setEndTime("2017-05-19 00:00:00");
//		accountTradeDetailModel.setPage(1);
//		String result = queryApiController
//				.queryAccountTradeDetailDirectBank(accountTradeDetailModel, request, response);
//		JSONObject jsonObject = JSONObject.parseObject(result);
//		assertEquals("0", jsonObject.getString("code"));
//		assertEquals(true, jsonObject.getJSONObject("data").getJSONObject("accountTradeDetail").getBoolean("hasNextPage"));
//		assertEquals(1000, jsonObject.getJSONObject("data")
//				.getJSONObject("accountTradeDetail")
//				.getJSONArray("accountTradeList")
//				.size());
//
//
//		accountTradeDetailModel.setPage(2);
//		result = queryApiController
//				.queryAccountTradeDetailDirectBank(accountTradeDetailModel, request, response);
//		jsonObject = JSONObject.parseObject(result);
//		assertEquals("0", jsonObject.getString("code"));
//		assertEquals(false, jsonObject.getJSONObject("data").getJSONObject("accountTradeDetail").getBoolean("hasNextPage"));
//		assertEquals(1000, jsonObject.getJSONObject("data")
//				.getJSONObject("accountTradeDetail")
//				.getJSONArray("accountTradeList")
//				.size());
//
//		accountTradeDetailModel.setPage(3);
//		result = queryApiController
//				.queryAccountTradeDetailDirectBank(accountTradeDetailModel, request, response);
//		jsonObject = JSONObject.parseObject(result);
//		assertEquals("0", jsonObject.getString("code"));
//		assertEquals(false, jsonObject.getJSONObject("data").getJSONObject("accountTradeDetail").getBoolean("hasNextPage"));
//		assertEquals(0, jsonObject.getJSONObject("data")
//				.getJSONObject("accountTradeDetail")
//				.getJSONArray("accountTradeList")
//				.size());
//	}
//	/**
//	 * 查询在有1000页的情况下
//	 */
//	@Test
//	@Sql({"classpath:test/yunxin/queryApi/test_queryApiTradeListAccount.sql"
//			,"classpath:test/yunxin/queryApi/test_insert_into_third_party_audit_bill_with1000rows.sql"})
//	public void testThirdPartyDirectBankApiPage1(){
//		AccountTradeDetailModel accountTradeDetailModel = new AccountTradeDetailModel();
//		accountTradeDetailModel.setRequestNo(UUID.randomUUID().toString());
//		accountTradeDetailModel.setProductCode("AND001");
//		accountTradeDetailModel.setCapitalAccountNo("591902896710203");
//		accountTradeDetailModel.setPaymentInstitutionName(3);
//		accountTradeDetailModel.setStartTime("2017-06-23 00:00:00");
//		accountTradeDetailModel.setEndTime("2017-06-24 00:00:00");
//		accountTradeDetailModel.setPage(1);
//		String result = queryApiController
//				.queryAccountTradeDetailDirectBank(accountTradeDetailModel, request, response);
//		JSONObject jsonObject = JSONObject.parseObject(result);
//		assertEquals("0", jsonObject.getString("code"));
//		assertEquals(false, jsonObject.getJSONObject("data").getJSONObject("accountTradeDetail").getBoolean("hasNextPage"));
//		assertEquals(1000, jsonObject.getJSONObject("data")
//				.getJSONObject("accountTradeDetail")
//				.getJSONArray("accountTradeList")
//				.size());
//	}
//	/**
//	 * 查询在有2000页的情况下
//	 */
//	@Test
//	@Sql({"classpath:test/yunxin/queryApi/test_queryApiTradeListAccount.sql"
//			,"classpath:test/yunxin/queryApi/test_insert_into_third_party_audit_bill_with2000rows.sql"})
//	public void testThirdPartyDirectBankApiPage2(){
//		AccountTradeDetailModel accountTradeDetailModel = new AccountTradeDetailModel();
//		accountTradeDetailModel.setRequestNo(UUID.randomUUID().toString());
//		accountTradeDetailModel.setProductCode("AND001");
//		accountTradeDetailModel.setCapitalAccountNo("591902896710203");
//		accountTradeDetailModel.setPaymentInstitutionName(3);
//		accountTradeDetailModel.setStartTime("2017-06-23 00:00:00");
//		accountTradeDetailModel.setEndTime("2017-06-24 00:00:00");
//		accountTradeDetailModel.setPage(1);
//		String result = queryApiController
//				.queryAccountTradeDetailDirectBank(accountTradeDetailModel, request, response);
//		JSONObject jsonObject = JSONObject.parseObject(result);
//		assertEquals("0", jsonObject.getString("code"));
//		assertEquals(true, jsonObject.getJSONObject("data").getJSONObject("accountTradeDetail").getBoolean("hasNextPage"));
//		assertEquals(1000, jsonObject.getJSONObject("data")
//				.getJSONObject("accountTradeDetail")
//				.getJSONArray("accountTradeList")
//				.size());
//
//
//		accountTradeDetailModel.setPage(2);
//		result = queryApiController
//				.queryAccountTradeDetailDirectBank(accountTradeDetailModel, request, response);
//		jsonObject = JSONObject.parseObject(result);
//		assertEquals("0", jsonObject.getString("code"));
//		assertEquals(false, jsonObject.getJSONObject("data").getJSONObject("accountTradeDetail").getBoolean("hasNextPage"));
//		assertEquals(1000, jsonObject.getJSONObject("data")
//				.getJSONObject("accountTradeDetail")
//				.getJSONArray("accountTradeList")
//				.size());
//
//		accountTradeDetailModel.setPage(3);
//		result = queryApiController
//				.queryAccountTradeDetailDirectBank(accountTradeDetailModel, request, response);
//		jsonObject = JSONObject.parseObject(result);
//		assertEquals("0", jsonObject.getString("code"));
//		assertEquals(false, jsonObject.getJSONObject("data").getJSONObject("accountTradeDetail").getBoolean("hasNextPage"));
//		assertEquals(0, jsonObject.getJSONObject("data")
//				.getJSONObject("accountTradeDetail")
//				.getJSONArray("accountTradeList")
//				.size());
//	}
//
//
//}