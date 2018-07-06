package com.zufangbao.earth.yunxin.api;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.demo2do.core.utils.JsonUtils;
import com.zufangbao.earth.yunxin.api.handler.RepaymentPlanApiHandler;
import com.zufangbao.earth.yunxin.api.model.query.RepaymentPlanBatchQueryModel;
import com.zufangbao.earth.yunxin.api.model.query.RepaymentPlanDetail;
import com.zufangbao.earth.yunxin.api.model.query.RepaymentPlanQueryModel;
import com.zufangbao.gluon.exception.ApiException;
import com.zufangbao.gluon.spec.earth.v3.ApiResponseCode;
import com.zufangbao.sun.entity.contract.Contract;
import com.zufangbao.sun.entity.repurchase.RepurchaseDoc;
import com.zufangbao.sun.service.ContractService;
import com.zufangbao.sun.service.RepurchaseService;
import com.zufangbao.sun.yunxin.entity.AssetSet;
import com.zufangbao.sun.yunxin.service.RepaymentPlanService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {

		"classpath:/local/applicationContext-*.xml" })
@Transactional
public class RepaymentPlanAPIHandlerTest extends AbstractTransactionalJUnit4SpringContextTests{

	@Autowired
	private RepaymentPlanApiHandler repaymentPlanAPIHandler;

	@Autowired
	private ContractService contractService;

	@Autowired
	private RepaymentPlanService repaymentPlanService;

	@Autowired
	private RepurchaseService repurchaseService;

	@Test
	@Sql("classpath:test/yunxin/api/testQueryRepaymentPlanDetail.sql")
	public void testQueryRepaymentPlanDetail(){
		String requestParamInfo = new  String("{\"contractNo\":\"云信信2016-78-DK(ZQ2016042522479)\",\"requestNo\":\"123566\",\"uniqueId\":\"\"}");
		RepaymentPlanQueryModel queryModel = JsonUtils.parse(requestParamInfo, RepaymentPlanQueryModel.class);
		List<RepaymentPlanDetail> repaymentPlanDetails = repaymentPlanAPIHandler.queryRepaymentPlanDetail(queryModel);
	
		//asset2 为已作废的asset
		Assert.assertEquals(1, repaymentPlanDetails.size());
		Assert.assertEquals("ZC27367D23EF4A36F4", repaymentPlanDetails.get(0).getRepaymentNumber());
		Assert.assertEquals("2016-05-17", repaymentPlanDetails.get(0).getPlanRepaymentDate());
		Assert.assertEquals("0.00", repaymentPlanDetails.get(0).getPlanRepaymentPrincipal());
		Assert.assertEquals("1200.00", repaymentPlanDetails.get(0).getPlanRepaymentInterest());
		Assert.assertEquals("未到期", repaymentPlanDetails.get(0).getRepaymentExecutionState());
		Assert.assertEquals("0", repaymentPlanDetails.get(0).getTechnicalServicesFees());
		Assert.assertEquals("0", repaymentPlanDetails.get(0).getOtherFees());
		Assert.assertEquals("0", repaymentPlanDetails.get(0).getLoanFees());
	}
	
	@Test
	@Sql("classpath:test/yunxin/api/testQueryRepaymentPlanDetail1.sql")
	public void testQueryRepaymentPlanDetail1(){
		String requestParamInfo = new  String("{\"contractNo\":\"云信信2016-78-DK(ZQ2016042522479)\",\"requestNo\":\"123566\",\"uniqueId\":\"\"}");
		RepaymentPlanQueryModel queryModel = JsonUtils.parse(requestParamInfo, RepaymentPlanQueryModel.class);
		List<RepaymentPlanDetail> repaymentPlanDetails = repaymentPlanAPIHandler.queryRepaymentPlanDetail(queryModel);
	
		//asset2 为已作废的asset
		Assert.assertEquals(1, repaymentPlanDetails.size());
		Assert.assertEquals("ZC27367D23EF4A36F4", repaymentPlanDetails.get(0).getRepaymentNumber());
		Assert.assertEquals("2016-05-17", repaymentPlanDetails.get(0).getPlanRepaymentDate());
		Assert.assertEquals("0.00", repaymentPlanDetails.get(0).getPlanRepaymentPrincipal());
		Assert.assertEquals("1200.00", repaymentPlanDetails.get(0).getPlanRepaymentInterest());
		Assert.assertEquals("成功", repaymentPlanDetails.get(0).getRepaymentExecutionState());
		Assert.assertEquals("0", repaymentPlanDetails.get(0).getTechnicalServicesFees());
		Assert.assertEquals("0", repaymentPlanDetails.get(0).getOtherFees());
		Assert.assertEquals("0", repaymentPlanDetails.get(0).getLoanFees());
	}
	
	@Test
	@Sql("classpath:test/yunxin/api/testQueryRepaymentPlanDetailError_HaveUniqueIdAndContractNo.sql")
	public  void testQueryRepaymentPlanDetailError_HaveUniqueIdAndContractNo(){
		String requestParamInfo = new String("{\"contractNo\":\"云信信2016-78-DK(ZQ2016042522479)\",\"requestNo\":\"123566\",\"uniqueId\":\"12345789\"}");
		RepaymentPlanQueryModel queryModel = JsonUtils.parse(requestParamInfo, RepaymentPlanQueryModel.class);
		Assert.assertFalse(queryModel.isValid());
	} 
	
	@Test
	@Sql("classpath:test/yunxin/api/testQueryRepaymentPlanDetailError_noRequestNo.sql")
	public  void testQueryRepaymentPlanDetailError_noRequestNo(){
		String requestParamInfo = new String("{\"contractNo\":\"云信信2016-78-DK(ZQ2016042522479)\",\"requestNo\":\"\",\"uniqueId\":\"\"}");
		RepaymentPlanQueryModel queryModel = JsonUtils.parse(requestParamInfo, RepaymentPlanQueryModel.class);
		Assert.assertFalse(queryModel.isValid());
	} 
	
	@Test
	@Sql("classpath:test/yunxin/api/testQueryRepaymentPlanDetailError_No_data.sql")
	public  void testQueryRepaymentPlanDetailError_No_data(){
		try {
			String requestParamInfo = new  String("{\"contractNo\":\"云信信2016-78-DK(ZQ2016042522479)\",\"requestNo\":\"123566\",\"uniqueId\":\"\"}");
			RepaymentPlanQueryModel queryModel = JsonUtils.parse(requestParamInfo, RepaymentPlanQueryModel.class);
			repaymentPlanAPIHandler.queryRepaymentPlanDetail(queryModel);
			Assert.fail();
		} catch (ApiException e) {
			Assert.assertEquals(ApiResponseCode.CONTRACT_NOT_EXIST, e.getCode());
		}
	
	}
	@Test
	@Sql("classpath:test/yunxin/api/testQueryRepaymentPlanDetailError_justHaveUniqueId.sql")
	public  void testQueryRepaymentPlanDetailError_justHaveUniqueId(){
			String requestParamInfo = new  String("{\"contractNo\":\"\",\"requestNo\":\"123566\",\"uniqueId\":\"1234567\"}");
			RepaymentPlanQueryModel queryModel = JsonUtils.parse(requestParamInfo, RepaymentPlanQueryModel.class);
			List<RepaymentPlanDetail> repaymentPlanDetails = repaymentPlanAPIHandler.queryRepaymentPlanDetail(queryModel);
			Assert.assertEquals(1, repaymentPlanDetails.size());
			Assert.assertEquals("ZC27367D23EF4A36F4", repaymentPlanDetails.get(0).getRepaymentNumber());
			Assert.assertEquals("2016-05-17", repaymentPlanDetails.get(0).getPlanRepaymentDate());
			Assert.assertEquals("0.00", repaymentPlanDetails.get(0).getPlanRepaymentPrincipal());
			Assert.assertEquals("1200.00", repaymentPlanDetails.get(0).getPlanRepaymentInterest());
			Assert.assertEquals("未到期", repaymentPlanDetails.get(0).getRepaymentExecutionState());
			Assert.assertEquals("0", repaymentPlanDetails.get(0).getTechnicalServicesFees());
			Assert.assertEquals("0", repaymentPlanDetails.get(0).getOtherFees());
			Assert.assertEquals("0", repaymentPlanDetails.get(0).getLoanFees());		
	}
	
	
	
	@Test
	@Sql("classpath:test/yunxin/api/testQueryRepaymentPlanDetail.sql")
	public void testQueryRepaymentPlanDetail_testPenaltyFee(){
		String requestParamInfo = new  String("{\"contractNo\":\"云信信2016-78-DK(ZQ2016042522479)\",\"requestNo\":\"123566\",\"uniqueId\":\"\"}");
		RepaymentPlanQueryModel queryModel = JsonUtils.parse(requestParamInfo, RepaymentPlanQueryModel.class);
		List<RepaymentPlanDetail> repaymentPlanDetails = repaymentPlanAPIHandler.queryRepaymentPlanDetail(queryModel);
	
		//asset2 为已作废的asset
		Assert.assertEquals(1, repaymentPlanDetails.size());
		Assert.assertEquals("ZC27367D23EF4A36F4", repaymentPlanDetails.get(0).getRepaymentNumber());
		Assert.assertEquals("2016-05-17", repaymentPlanDetails.get(0).getPlanRepaymentDate());
		Assert.assertEquals("0.00", repaymentPlanDetails.get(0).getPlanRepaymentPrincipal());
		Assert.assertEquals("1200.00", repaymentPlanDetails.get(0).getPlanRepaymentInterest());
		Assert.assertEquals("未到期", repaymentPlanDetails.get(0).getRepaymentExecutionState());
		Assert.assertEquals("0", repaymentPlanDetails.get(0).getTechnicalServicesFees());
		Assert.assertEquals("0", repaymentPlanDetails.get(0).getOtherFees());
		Assert.assertEquals("0", repaymentPlanDetails.get(0).getLoanFees());
		Assert.assertEquals("0", repaymentPlanDetails.get(0).getPenaltyFee());
	}



	/**
	 * 信托合同代码错误
	 */
	@Test
	@Sql("classpath:test/yunxin/api/repaymentPlanApi/test_queryRepaymentPlanDetail_productCode_not_exist.sql")
	public void test_queryRepaymentPlanDetail_productCode_not_exist() {
		RepaymentPlanBatchQueryModel batchQueryModel = new RepaymentPlanBatchQueryModel();
		batchQueryModel.setProductCode("123456");
		batchQueryModel.setPlanRepaymentDate("2017-07-06");
		try {
			List<Map<String, Object>> repaymentPlanDetails = repaymentPlanAPIHandler.queryRepaymentPlanDetail(batchQueryModel);
			Assert.fail();
		} catch (Exception e) {
			ApiException exception = (ApiException) e;
			Assert.assertEquals(ApiResponseCode.FINANCIAL_PRODUCT_CODE_ERROR, ((ApiException) e).getCode());
		}
	}

	/**
	 * 只通过UniqueId查询
	 */
	@Test
	@Sql("classpath:test/yunxin/api/repaymentPlanApi/test_queryRepaymentPlanDetail.sql")
	public void test_queryRepaymentPlanDetail() {
		RepaymentPlanBatchQueryModel batchQueryModel = new RepaymentPlanBatchQueryModel();
		batchQueryModel.setProductCode("G31700");
//		batchQueryModel.setPlanRepaymentDate("2017-06-23");
		String uniqueId_1 = "wb111452341271";
		String uniqueIds = com.zufangbao.sun.utils.JsonUtils.toJSONString(Arrays.asList(uniqueId_1));
		batchQueryModel.setUniqueIds(uniqueIds);
		try {
			List<Map<String, Object>> repaymentPlanDetails = repaymentPlanAPIHandler.queryRepaymentPlanDetail(batchQueryModel);
			Assert.assertEquals(1, repaymentPlanDetails.size());
			Map<String, Object> map = repaymentPlanDetails.get(0);
			String result_uniqueId = (String) map.get("uniqueId");
			Assert.assertEquals(uniqueId_1, result_uniqueId);
			List<RepaymentPlanDetail> planDetails = (List<RepaymentPlanDetail>) map.get("repaymentPlanDetails");
			Assert.assertEquals(3, planDetails.size());

			RepaymentPlanDetail planDetail_1 = planDetails.get(0);
			Assert.assertEquals("ZC80354562385854464", planDetail_1.getRepaymentNumber());
			Assert.assertEquals("2017-07-06", planDetail_1.getPlanRepaymentDate());

			RepaymentPlanDetail planDetail_2 = planDetails.get(1);
			Assert.assertEquals("ZC80354562402631680", planDetail_2.getRepaymentNumber());

			RepaymentPlanDetail planDetail_3 = planDetails.get(2);
			Assert.assertEquals("ZC80354562411020288", planDetail_3.getRepaymentNumber());

		} catch (Exception e) {
			Assert.fail();
		}
	}

	/**
	 * 通过UniqueId + PlanRepaymentDate 查询
	 */
	@Test
	@Sql("classpath:test/yunxin/api/repaymentPlanApi/test_queryRepaymentPlanDetail.sql")
	public void test_queryRepaymentPlanDetail_2() {
		RepaymentPlanBatchQueryModel batchQueryModel = new RepaymentPlanBatchQueryModel();
		batchQueryModel.setProductCode("G31700");
		batchQueryModel.setPlanRepaymentDate("2017-07-06");
		String uniqueId_1 = "wb111452341271";
		String uniqueIds = com.zufangbao.sun.utils.JsonUtils.toJSONString(Arrays.asList(uniqueId_1));
		batchQueryModel.setUniqueIds(uniqueIds);
		try {
			List<Map<String, Object>> repaymentPlanDetails = repaymentPlanAPIHandler.queryRepaymentPlanDetail(batchQueryModel);
			Assert.assertEquals(1, repaymentPlanDetails.size());
			Map<String, Object> map = repaymentPlanDetails.get(0);
			String result_uniqueId = (String) map.get("uniqueId");
			Assert.assertEquals(uniqueId_1, result_uniqueId);
			List<RepaymentPlanDetail> planDetails = (List<RepaymentPlanDetail>) map.get("repaymentPlanDetails");
			Assert.assertEquals(1, planDetails.size());

			RepaymentPlanDetail planDetail_1 = planDetails.get(0);
			Assert.assertEquals("ZC80354562385854464", planDetail_1.getRepaymentNumber());
			Assert.assertEquals("2017-07-06", planDetail_1.getPlanRepaymentDate());
		} catch (Exception e) {
			Assert.fail();
		}
	}

	/**
	 * 信托产品代码和贷款合同唯一编号没有对应关系
	 */
	@Test
	@Sql("classpath:test/yunxin/api/repaymentPlanApi/test_queryRepaymentPlanDetail_failMessage.sql")
	public void test_test_queryRepaymentPlanDetail_3() {
		RepaymentPlanBatchQueryModel batchQueryModel = new RepaymentPlanBatchQueryModel();
		batchQueryModel.setProductCode("G31700");
		batchQueryModel.setPlanRepaymentDate("2017-07-06");
		String uniqueId_1 = "wb111452341271";
		String uniqueIds = com.zufangbao.sun.utils.JsonUtils.toJSONString(Arrays.asList(uniqueId_1));
		batchQueryModel.setUniqueIds(uniqueIds);
		try {
			List<Map<String, Object>> repaymentPlanDetails = repaymentPlanAPIHandler.queryRepaymentPlanDetail(batchQueryModel);
			Assert.assertEquals(1, repaymentPlanDetails.size());
			Map<String, Object> map = repaymentPlanDetails.get(0);
			String result_uniqueId = (String) map.get("uniqueId");
			Assert.assertEquals(uniqueId_1, result_uniqueId);
			String result_failMsg = (String)map.get("failMsg");
			Assert.assertEquals("贷款合同唯一编号[wb111452341271]，该贷款合同唯一编号与信托产品代码无对应关系",result_failMsg);

		} catch (Exception e) {
			System.out.println(e.getMessage());
			Assert.fail();
		}
	}

	/**
	 * 还款计划批量查询：获取contract贷款合同为空
	 */
	@Test
	@Sql("classpath:test/yunxin/api/repaymentPlanApi/test_queryRepaymentPlanDetail.sql")
	public void test_queryRepaymentPlanDetail_3() {
		RepaymentPlanBatchQueryModel batchQueryModel = new RepaymentPlanBatchQueryModel();
		batchQueryModel.setProductCode("G31700");
		batchQueryModel.setPlanRepaymentDate("2010-07-06");
		String uniqueId_1 = "wb111452341272";
		String uniqueIds = com.zufangbao.sun.utils.JsonUtils.toJSONString(Arrays.asList(uniqueId_1));
		batchQueryModel.setUniqueIds(uniqueIds);
		List<String> uniqueIdsList = batchQueryModel.getUniqueIdsList(batchQuerySize);
		for(String uniqueId : uniqueIdsList){
			Contract contract = contractService.getContractByUniqueId(uniqueId);
			Assert.assertNull(null,contract);
		}
	}

	/**
	 * 还款计划批量查询：获取repaymentPlanList还款计划列表为空
	 */
	@Test
	@Sql("classpath:test/yunxin/api/repaymentPlanApi/test_queryRepaymentPlanDetail.sql")
	public void test_queryRepaymentPlanDetail_4() {
		RepaymentPlanBatchQueryModel batchQueryModel = new RepaymentPlanBatchQueryModel();
		batchQueryModel.setProductCode("G31700");
		batchQueryModel.setPlanRepaymentDate("2010-07-06");
		String uniqueId_1 = "wb111452341271";
		String uniqueIds = com.zufangbao.sun.utils.JsonUtils.toJSONString(Arrays.asList(uniqueId_1));
		batchQueryModel.setUniqueIds(uniqueIds);
		//构造一没有记录的查询参数
		List<AssetSet> repaymentPlanList = repaymentPlanService.getEffectiveRepaymentPlansBy("17c900cb-c2c6-42a3-ae80-818192b43696", "d2812bc5-5057-4a91-b3fd-9019506f0499", batchQueryModel.getDateTypePlanRepaymentDate());
		Assert.assertEquals(0,repaymentPlanList.size());
	}

	/**
	 * 还款计划批量查询：有回购订单时返回回购单
	 */
	@Test
	@Sql("classpath:test/yunxin/api/repaymentPlanApi/test_repurchaseDoc.sql")
	public void test_getRepurchaseDocBy(){
		RepaymentPlanBatchQueryModel batchQueryModel = new RepaymentPlanBatchQueryModel();
		String uniqueId_1 = "wb9988760016";
		String uniqueIds = com.zufangbao.sun.utils.JsonUtils.toJSONString(Arrays.asList(uniqueId_1));
		batchQueryModel.setUniqueIds(uniqueIds);
		List<String> uniqueIdsList = batchQueryModel.getUniqueIdsList(batchQuerySize);
		Contract contract = contractService.getContractByUniqueId(uniqueIdsList.get(0));
		RepurchaseDoc repurchaseDoc = repurchaseService.getRepurchaseDocBy(contract.getId());
		Assert.assertNotNull(repurchaseDoc);

	}

	@Value("#{config['yx.batchQuery.size']}")
	@JSONField(serialize = false)
	private String batchQuerySize = "";

	/**
	 * 批量查询还款计划:限流上限200个 测试list为100个
	 */
	@Test
	public void test_getUniqueIdsList(){
		JSONObject uniqueId = new JSONObject();
		JSONArray uniqueIds = new JSONArray();
		for (int i = 0; i<100;i++){
			uniqueId.put(i+"",i);
			uniqueIds.add(uniqueId);
		}
		String ss = uniqueIds.toString();
		RepaymentPlanBatchQueryModel queryModel = new RepaymentPlanBatchQueryModel();
		queryModel.setUniqueIds(ss);
		List<String> list = queryModel.getUniqueIdsList(batchQuerySize);
		Assert.assertEquals(100,list.size());
	}

	/**
	 * 批量查询还款计划:限流上限200个 测试list为1000个
	 */
	@Test
	public void test_getUniqueIdsList_1(){
		JSONObject uniqueId = new JSONObject();
		JSONArray uniqueIds = new JSONArray();
		for (int i = 0; i<1000;i++){
			uniqueId.put(i+"",i);
			uniqueIds.add(uniqueId);
		}
		String ss = uniqueIds.toString();
		RepaymentPlanBatchQueryModel queryModel = new RepaymentPlanBatchQueryModel();
		queryModel.setUniqueIds(ss);
		List<String> list = queryModel.getUniqueIdsList(batchQuerySize);
		Assert.assertEquals(Integer.valueOf(batchQuerySize).intValue(),list.size());
	}
	@Test
	@Sql("classpath:test/yunxin/api/testQueryRepaymentPlanDetail_assetSet_is_advance.sql")
	public void testQueryRepaymentPlanDetail_assetSet_is_advance(){
		String requestParamInfo = new  String("{\"contractNo\":\"云信信2016-78-DK(ZQ2016042522479)\",\"requestNo\":\"123566\",\"uniqueId\":\"\"}");
		RepaymentPlanQueryModel queryModel = JsonUtils.parse(requestParamInfo, RepaymentPlanQueryModel.class);
		List<RepaymentPlanDetail> repaymentPlanDetails = repaymentPlanAPIHandler.queryRepaymentPlanDetail(queryModel);
	
		//asset2 为已作废的asset
		Assert.assertEquals(1, repaymentPlanDetails.size());
		Assert.assertEquals("single_loan_contract_no_3", repaymentPlanDetails.get(0).getRepaymentNumber());
		Assert.assertEquals("2016-05-17", repaymentPlanDetails.get(0).getPlanRepaymentDate());
		Assert.assertEquals("0.00", repaymentPlanDetails.get(0).getPlanRepaymentPrincipal());
		Assert.assertEquals("1200.00", repaymentPlanDetails.get(0).getPlanRepaymentInterest());
		Assert.assertEquals("未到期", repaymentPlanDetails.get(0).getRepaymentExecutionState());
		Assert.assertEquals("0", repaymentPlanDetails.get(0).getTechnicalServicesFees());
		Assert.assertEquals("0", repaymentPlanDetails.get(0).getOtherFees());
		Assert.assertEquals("0", repaymentPlanDetails.get(0).getLoanFees());
	}
	
	@Test
	@Sql("classpath:test/yunxin/api/testQueryRepaymentPlanDetail_assetSet_is_repurchasing.sql")
	public void testQueryRepaymentPlanDetail_assetSet_is_repurchasing(){
		String requestParamInfo = new  String("{\"contractNo\":\"云信信2016-78-DK(ZQ2016042522479)\",\"requestNo\":\"123566\",\"uniqueId\":\"\"}");
		RepaymentPlanQueryModel queryModel = JsonUtils.parse(requestParamInfo, RepaymentPlanQueryModel.class);
		List<RepaymentPlanDetail> repaymentPlanDetails = repaymentPlanAPIHandler.queryRepaymentPlanDetail(queryModel);
		Assert.assertEquals(0, repaymentPlanDetails.size());
	}
	
	
}