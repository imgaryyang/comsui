package com.zufangbao.earth.yunxin.customize;

import com.alibaba.fastjson.JSONObject;
import com.demo2do.core.entity.Result;
import com.demo2do.core.persistence.GenericDaoSupport;
import com.suidifu.matryoshka.customize.DelayTaskServices;
import com.suidifu.matryoshka.delayPosition.handler.DelayProcessingTaskCacheHandler;
import com.suidifu.matryoshka.delayPosition.handler.DelayTaskConfigCacheHandler;
import com.suidifu.matryoshka.delayTask.DelayProcessingTask;
import com.suidifu.matryoshka.handler.SandboxDataSetHandler;
import com.suidifu.matryoshka.service.delayTask.DelayProcessingTaskService;
import com.suidifu.matryoshka.snapshot.SandboxDataSetSpec;
import com.zufangbao.earth.yunxin.customize.scripts.ZhongAnRepaymentPlanChargeDelayTaskServices;
import com.zufangbao.sun.utils.JsonUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

//import com.zufangbao.sun.entity.perInterface.snapshots.SandboxDataSetSpec;
//import com.zufangbao.sun.yunxin.delayTask.DelayProcessingTask;
//import com.zufangbao.sun.yunxin.service.delayTask.DelayProcessingTaskService;
//import com.zufangbao.wellsfargo.yunxin.customize.DelayTaskServices;
//import com.zufangbao.wellsfargo.yunxin.handler.SandboxDataSetHandler;
//import com.zufangbao.wellsfargo.yunxin.handler.delayTask.DelayProcessingTaskCacheHandler;
//import com.zufangbao.wellsfargo.yunxin.handler.delayTask.DelayTaskConfigCacheHandler;

/**
 * Created by fanxiaofan on 2017/5/10.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/local/applicationContext-*.xml" })
@Transactional
@Rollback(true)
public class ZhongAnRepaymentPlanChargeDelayTaskServicesTest extends AbstractTransactionalJUnit4SpringContextTests {
	private final static Log logger = LogFactory.getLog(ZhongAnRepaymentPlanChargeDelayTaskServicesTest.class);
	@Autowired
	private DelayTaskConfigCacheHandler delayTaskConfigCacheHandler;

	@Autowired
	private DelayProcessingTaskCacheHandler delayProcessingTaskHandler;

	@Autowired
	private SandboxDataSetHandler sandboxDataSetHandler;

	@Autowired
	private GenericDaoSupport genericDaoSupport;

	@Autowired
	@Qualifier("delayProcessingTaskDBService")
	private DelayProcessingTaskService delayProcessingTaskService;

	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	@Before
	public void init_cache() {
		delayTaskConfigCacheHandler.clearAll();
		stringRedisTemplate.delete("sr:delayTaskConfigUuid_1");
	}

	@Test
	@Sql("classpath:test/yunxin/customize/test_delay_task_null_param.sql")
	public void test_delay_task_null_param() {
		DelayTaskServices delayTaskServices = new ZhongAnRepaymentPlanChargeDelayTaskServices();
		Result processedResult = new Result("0");
		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put(SandboxDataSetSpec.FINANCIAL_CONTRACT_UUID, "2d380fe1-7157-490d-9474-12c5a9901e29");
		inputMap.put(SandboxDataSetSpec.CONFING_UUID, "ee101fb3-042e-40f3-80f1-9f592c485fd3");
		inputMap.put(SandboxDataSetSpec.CONTRACT_UUID, "315317d0-438a-4e6d-a4b8-20a260624b6e");
		List<String> repaymentPlanNoSrcList = Arrays.asList("ZC1750723569322520543", "ZC1750723569590956032",
				"ZC1750723569859391488");
		List<String> repaymentPlanNoTarList = Arrays.asList("ZC1691381619185422336", "ZC1691381619856510976",
				"ZC1691381620527599616");
		inputMap.put(SandboxDataSetSpec.ORIGINAL_REPAYMENT_PLAN_NO_LIST, repaymentPlanNoSrcList);
		inputMap.put(SandboxDataSetSpec.REPAYMENT_PLAN_NO_LIST, repaymentPlanNoTarList);
		Map<String, Object> resultMap = new HashMap<>();
		boolean result1 = delayTaskServices.evaluate(null, delayProcessingTaskHandler, sandboxDataSetHandler, inputMap,
				resultMap, logger);
		Assert.assertFalse(result1);

		boolean result2 = delayTaskServices.evaluate(processedResult, delayProcessingTaskHandler, sandboxDataSetHandler,
				null, resultMap, logger);
		Assert.assertFalse(result2);

		boolean result3 = delayTaskServices.evaluate(processedResult, null, sandboxDataSetHandler, inputMap, resultMap, logger);
		Assert.assertFalse(result3);

		boolean result4 = delayTaskServices.evaluate(processedResult, delayProcessingTaskHandler, null, inputMap,
				resultMap, logger);
		Assert.assertFalse(result4);
	}

	@Test
	public void test_delay_task_prepost_unsuccess() {
		DelayTaskServices delayTaskServices = new ZhongAnRepaymentPlanChargeDelayTaskServices();
		Result processedResult = new Result("1");
		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put(SandboxDataSetSpec.FINANCIAL_CONTRACT_UUID, "2d380fe1-7157-490d-9474-12c5a9901e29");
		inputMap.put(SandboxDataSetSpec.CONFING_UUID, "ee101fb3-042e-40f3-80f1-9f592c485fd3");
		inputMap.put(SandboxDataSetSpec.CONTRACT_UUID, "315317d0-438a-4e6d-a4b8-20a260624b6e");
		List<String> repaymentPlanNoSrcList = Arrays.asList("ZC1750723569322520543", "ZC1750723569590956032",
				"ZC1750723569859391488");
		List<String> repaymentPlanNoTarList = Arrays.asList("ZC1691381619185422336", "ZC1691381619856510976",
				"ZC1691381620527599616");
		inputMap.put(SandboxDataSetSpec.ORIGINAL_REPAYMENT_PLAN_NO_LIST, repaymentPlanNoSrcList);
		inputMap.put(SandboxDataSetSpec.REPAYMENT_PLAN_NO_LIST, repaymentPlanNoTarList);
		Map<String, Object> resultMap = new HashMap<>();
		boolean result = delayTaskServices.evaluate(processedResult, delayProcessingTaskHandler, sandboxDataSetHandler,
				inputMap, resultMap, logger);
		Assert.assertFalse(result);
	}

	@Test
	@Sql("classpath:test/yunxin/customize/test_delay_task_other_change_type.sql")
	public void test_delay_task_other_change_type() {
		DelayTaskServices delayTaskServices = new ZhongAnRepaymentPlanChargeDelayTaskServices();
		Result processedResult = new Result("0");
		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put(SandboxDataSetSpec.FINANCIAL_CONTRACT_UUID, "2d380fe1-7157-490d-9474-12c5a9901e29");
		inputMap.put(SandboxDataSetSpec.CONFING_UUID, "ee101fb3-042e-40f3-80f1-9f592c485fd3");
		inputMap.put(SandboxDataSetSpec.CONTRACT_UUID, "315317d0-438a-4e6d-a4b8-20a260624b6e");
		List<String> repaymentPlanNoSrcList = Arrays.asList("ZC1750723569322520543", "ZC1750723569590956032",
				"ZC1750723569859391488");
		List<String> repaymentPlanNoTarList = Arrays.asList("ZC1691381619185422336", "ZC1691381619856510976",
				"ZC1691381620527599616");
		inputMap.put(SandboxDataSetSpec.ORIGINAL_REPAYMENT_PLAN_NO_LIST, repaymentPlanNoSrcList);
		inputMap.put(SandboxDataSetSpec.REPAYMENT_PLAN_NO_LIST, repaymentPlanNoTarList);
		Map<String, Object> resultMap = new HashMap<>();
		boolean result = delayTaskServices.evaluate(processedResult, delayProcessingTaskHandler, sandboxDataSetHandler,
				inputMap, resultMap, logger);
		Assert.assertFalse(result);
	}

	// 随心还-取消随心还服务
	@Test
	@Sql("classpath:test/yunxin/customize/test_SXH_cancel_delay_task.sql")
	public void test_SXH_cancel_delay_task() {
		DelayTaskServices delayTaskServices = new ZhongAnRepaymentPlanChargeDelayTaskServices();
		Result processedResult = new Result("0");
		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put(SandboxDataSetSpec.FINANCIAL_CONTRACT_UUID, "2d380fe1-7157-490d-9474-12c5a9901e29");
		inputMap.put(SandboxDataSetSpec.CONFING_UUID, "ee101fb3-042e-40f3-80f1-9f592c485fd3");
		inputMap.put(SandboxDataSetSpec.CONTRACT_UUID, "315317d0-438a-4e6d-a4b8-20a260624b6e");
		List<String> repaymentPlanNoSrcList = Arrays.asList("ZC1750723569322520543", "ZC1750723569590956032",
				"ZC1750723569859391488");
		List<String> repaymentPlanNoTarList = Arrays.asList("ZC1691381619185422336", "ZC1691381619856510976",
				"ZC1691381620527599616");
		inputMap.put(SandboxDataSetSpec.ORIGINAL_REPAYMENT_PLAN_NO_LIST, repaymentPlanNoSrcList);
		inputMap.put(SandboxDataSetSpec.REPAYMENT_PLAN_NO_LIST, repaymentPlanNoTarList);
		Map<String, Object> resultMap = new HashMap<>();
		boolean result = delayTaskServices.evaluate(processedResult, delayProcessingTaskHandler, sandboxDataSetHandler,
				inputMap, resultMap, logger);
		Assert.assertTrue(result);
		List<DelayProcessingTask> taskList = getAllDelayProcessingTask();
		Assert.assertNotNull(taskList);
		Assert.assertEquals(taskList.size(), repaymentPlanNoTarList.size());
		for (DelayProcessingTask task : taskList) {
			Map<String, Object> map = JsonUtils.parse(task.getWorkParams());
			String changeType = (String) map.get("changeType");
			Assert.assertEquals(changeType, "3");
			String installmentNo = map.get("installmentNo").toString();
			Assert.assertNotEquals(installmentNo, "0");
			BigDecimal principal = (BigDecimal) map.get("principal");
			BigDecimal interest = (BigDecimal) map.get("interest");
			BigDecimal repayCharge = (BigDecimal) map.get("repayCharge");
			Assert.assertTrue(principal.signum() <= 0);
			Assert.assertTrue(interest.signum() <= 0);
			Assert.assertTrue(repayCharge.signum() <= 0);
		}
	}

	// 提前结清（无提前结清手续费）
	@Test
	@Sql("classpath:test/yunxin/customize/test_SXH_prepayment_delay_task.sql")
	public void test_SXH_prepayment_delay_task() {
		DelayTaskServices delayTaskServices = new ZhongAnRepaymentPlanChargeDelayTaskServices();
		Result processedResult = new Result("0");
		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put(SandboxDataSetSpec.FINANCIAL_CONTRACT_UUID, "2d380fe1-7157-490d-9474-12c5a9901e29");
		inputMap.put(SandboxDataSetSpec.CONFING_UUID, "ee101fb3-042e-40f3-80f1-9f592c485fd3");
		inputMap.put(SandboxDataSetSpec.CONTRACT_UUID, "315317d0-438a-4e6d-a4b8-20a260624b6e");
		List<String> repaymentPlanNoSrcList = Arrays.asList("ZC1750723569322520543", "ZC1750723569590956032",
				"ZC1750723569859391488");
		List<String> repaymentPlanNoTarList = Arrays.asList("ZC1691381619185422336");
		inputMap.put(SandboxDataSetSpec.ORIGINAL_REPAYMENT_PLAN_NO_LIST, repaymentPlanNoSrcList);
		inputMap.put(SandboxDataSetSpec.REPAYMENT_PLAN_NO_LIST, repaymentPlanNoTarList);
		Map<String, Object> resultMap = new HashMap<>();
		boolean result = delayTaskServices.evaluate(processedResult, delayProcessingTaskHandler, sandboxDataSetHandler,
				inputMap, resultMap, logger);
		Assert.assertTrue(result);
		List<DelayProcessingTask> taskList = getAllDelayProcessingTask();
		Assert.assertNotNull(taskList);
		Assert.assertEquals(taskList.size(), repaymentPlanNoTarList.size());
		for (DelayProcessingTask task : taskList) {
			Map<String, Object> map = JsonUtils.parse(task.getWorkParams());
			String changeType = (String) map.get("changeType");
			Assert.assertEquals(changeType, "5");
			String installmentNo = map.get("installmentNo").toString();
			Assert.assertEquals(installmentNo, "1");
			BigDecimal principal = (BigDecimal) map.get("principal");
			BigDecimal interest = (BigDecimal) map.get("interest");
			BigDecimal repayCharge = (BigDecimal) map.get("repayCharge");
			Assert.assertTrue(principal.signum() >= 0);
			Assert.assertTrue(interest.signum() >= 0);
			Assert.assertTrue(repayCharge.signum() >= 0);
		}
	}

	// 提前结清（有提前结清手续费）
	@Test
	@Sql("classpath:test/yunxin/customize/test_prepayment_delay_task.sql")
	public void test_prepayment_delay_task() {
		DelayTaskServices delayTaskServices = new ZhongAnRepaymentPlanChargeDelayTaskServices();
		Result processedResult = new Result("0");
		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put(SandboxDataSetSpec.FINANCIAL_CONTRACT_UUID, "2d380fe1-7157-490d-9474-12c5a9901e29");
		inputMap.put(SandboxDataSetSpec.CONFING_UUID, "ee101fb3-042e-40f3-80f1-9f592c485fd3");
		inputMap.put(SandboxDataSetSpec.CONTRACT_UUID, "315317d0-438a-4e6d-a4b8-20a260624b6e");
		List<String> repaymentPlanNoSrcList = Arrays.asList("ZC1750723569322520543", "ZC1750723569590956032",
				"ZC1750723569859391488");
		List<String> repaymentPlanNoTarList = Arrays.asList("ZC1691381619185422336");
		inputMap.put(SandboxDataSetSpec.ORIGINAL_REPAYMENT_PLAN_NO_LIST, repaymentPlanNoSrcList);
		inputMap.put(SandboxDataSetSpec.REPAYMENT_PLAN_NO_LIST, repaymentPlanNoTarList);
		Map<String, Object> resultMap = new HashMap<>();
		boolean result = delayTaskServices.evaluate(processedResult, delayProcessingTaskHandler, sandboxDataSetHandler,
				inputMap, resultMap, logger);
		Assert.assertTrue(result);
		List<DelayProcessingTask> taskList = getAllDelayProcessingTask();
		Assert.assertNotNull(taskList);
		Assert.assertEquals(taskList.size(), repaymentPlanNoTarList.size());
		for (DelayProcessingTask task : taskList) {
			Map<String, Object> map = JsonUtils.parse(task.getWorkParams());
			String changeType = (String) map.get("changeType");
			Assert.assertEquals(changeType, "4");
			String installmentNo = map.get("installmentNo").toString();
			Assert.assertEquals(installmentNo, "1");
			BigDecimal principal = (BigDecimal) map.get("principal");
			BigDecimal interest = (BigDecimal) map.get("interest");
			BigDecimal repayCharge = (BigDecimal) map.get("repayCharge");
			Assert.assertTrue(principal.signum() >= 0);
			Assert.assertTrue(interest.signum() >= 0);
			Assert.assertTrue(repayCharge.signum() >= 0);
		}
	}

	// 退货
	@Test
	@Sql("classpath:test/yunxin/customize/test_refunds_delay_task.sql")
	public void test_refunds_delay_task() {
		DelayTaskServices delayTaskServices = new ZhongAnRepaymentPlanChargeDelayTaskServices();
		Result processedResult = new Result("0");
		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put(SandboxDataSetSpec.FINANCIAL_CONTRACT_UUID, "2d380fe1-7157-490d-9474-12c5a9901e29");
		inputMap.put(SandboxDataSetSpec.CONFING_UUID, "ee101fb3-042e-40f3-80f1-9f592c485fd3");
		inputMap.put(SandboxDataSetSpec.CONTRACT_UUID, "315317d0-438a-4e6d-a4b8-20a260624b6e");
		List<String> repaymentPlanNoSrcList = Arrays.asList("ZC1750723569322520543", "ZC1750723569590956032",
				"ZC1750723569859391488");
		List<String> repaymentPlanNoTarList = Arrays.asList("ZC1691381619185422336");
		inputMap.put(SandboxDataSetSpec.ORIGINAL_REPAYMENT_PLAN_NO_LIST, repaymentPlanNoSrcList);
		inputMap.put(SandboxDataSetSpec.REPAYMENT_PLAN_NO_LIST, repaymentPlanNoTarList);
		Map<String, Object> resultMap = new HashMap<>();
		boolean result = delayTaskServices.evaluate(processedResult, delayProcessingTaskHandler, sandboxDataSetHandler,
				inputMap, resultMap, logger);
		Assert.assertTrue(result);
		List<DelayProcessingTask> taskList = getAllDelayProcessingTask();
		Assert.assertNotNull(taskList);
		Assert.assertEquals(taskList.size(), repaymentPlanNoTarList.size());
		for (DelayProcessingTask task : taskList) {
			Map<String, Object> map = JsonUtils.parse(task.getWorkParams());
			String changeType = (String) map.get("changeType");
			Assert.assertEquals(changeType, "6");
			String installmentNo = map.get("installmentNo").toString();
			Assert.assertEquals(installmentNo, "1");
			BigDecimal principal = (BigDecimal) map.get("principal");
			BigDecimal interest = (BigDecimal) map.get("interest");
			BigDecimal repayCharge = (BigDecimal) map.get("repayCharge");
			Assert.assertTrue(principal.signum() >= 0);
			Assert.assertTrue(interest.signum() >= 0);
			Assert.assertTrue(repayCharge.signum() >= 0);
		}
	}

	@Test
	@Sql("classpath:test/yunxin/delayTask/test_save_plan_charge_by_db_source_code.sql")
	public void test_save_plan_charge_by_db_source_code() {
		try {
			String delayTaskConfigUuid = "delayTaskConfigUuid_1";

			DelayTaskServices delayTaskServices = (DelayTaskServices) delayTaskConfigCacheHandler
					.getCompiledObjectDelayTaskConfigUuid(delayTaskConfigUuid);
			Assert.assertNotNull(delayTaskServices);

			Result processedResult = new Result("0");
			Map<String, Object> inputMap = new HashMap<String, Object>();
			inputMap.put(SandboxDataSetSpec.FINANCIAL_CONTRACT_UUID, "2d380fe1-7157-490d-9474-12c5a9901e29");
			inputMap.put(SandboxDataSetSpec.CONFING_UUID, "ee101fb3-042e-40f3-80f1-9f592c485fd3");
			inputMap.put(SandboxDataSetSpec.CONTRACT_UUID, "315317d0-438a-4e6d-a4b8-20a260624b6e");
			List<String> repaymentPlanNoSrcList = Arrays.asList("ZC1750723569322520543", "ZC1750723569590956032",
					"ZC1750723569859391488");
			List<String> repaymentPlanNoTarList = Arrays.asList("ZC1691381619185422336", "ZC1691381619856510976",
					"ZC1691381620527599616");
			inputMap.put(SandboxDataSetSpec.ORIGINAL_REPAYMENT_PLAN_NO_LIST, repaymentPlanNoSrcList);
			inputMap.put(SandboxDataSetSpec.REPAYMENT_PLAN_NO_LIST, repaymentPlanNoTarList);
			JSONObject jsonObject = new JSONObject();
			List<JSONObject> planNoList = new ArrayList<>();
			for(String planNo : repaymentPlanNoTarList) {
				jsonObject.put("repaymentNumber", planNo);
				planNoList.add(jsonObject);
			}

				inputMap.put("repaymentPlanList", planNoList);
			Map<String, Object> resultMap = new HashMap<>();
			processedResult.setData(inputMap);
			boolean result = delayTaskServices.evaluate(processedResult, delayProcessingTaskHandler,
					sandboxDataSetHandler, inputMap, resultMap, logger);
			Assert.assertTrue(result);
			List<DelayProcessingTask> taskList = getAllDelayProcessingTask();
			Assert.assertNotNull(taskList);
			Assert.assertEquals(taskList.size(), repaymentPlanNoTarList.size());
			for (DelayProcessingTask task : taskList) {
				Map<String, Object> map = JsonUtils.parse(task.getWorkParams());
				String changeType = (String) map.get("changeType");
				Assert.assertEquals(changeType, "3");
				String installmentNo = map.get("installmentNo").toString();
				Assert.assertNotEquals(installmentNo, "0");
				BigDecimal principal = (BigDecimal) map.get("principal");
				BigDecimal interest = (BigDecimal) map.get("interest");
				BigDecimal repayCharge = (BigDecimal) map.get("repayCharge");
				Assert.assertTrue(principal.signum() <= 0);
				Assert.assertTrue(interest.signum() <= 0);
				Assert.assertTrue(repayCharge.signum() <= 0);
			}

		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}

	}

	@Test
	@Sql("classpath:test/yunxin/delayTask/test_save_plan_charge_by_db_source_code_two.sql")
	public void test_save_plan_charge_by_db_source_code_two() {
		try {
			String delayTaskConfigUuid = "delayTaskConfigUuid_1";

			DelayTaskServices delayTaskServices = (DelayTaskServices) delayTaskConfigCacheHandler
					.getCompiledObjectDelayTaskConfigUuid(delayTaskConfigUuid);
			Assert.assertNotNull(delayTaskServices);

			Result processedResult = new Result("0");
			Map<String, Object> inputMap = new HashMap<String, Object>();
			inputMap.put(SandboxDataSetSpec.FINANCIAL_CONTRACT_UUID, "2d380fe1-7157-490d-9474-12c5a9901e29");
			inputMap.put(SandboxDataSetSpec.CONFING_UUID, "ee101fb3-042e-40f3-80f1-9f592c485fd3");
			inputMap.put(SandboxDataSetSpec.CONTRACT_UUID, "315317d0-438a-4e6d-a4b8-20a260624b6e");
			List<String> repaymentPlanNoSrcList = Arrays.asList("ZC1750723569322520543", "ZC1750723569590956032",
					"ZC1750723569859391488");
			List<String> repaymentPlanNoTarList = Arrays.asList("ZC1691381619185422336", "ZC1691381619856510976",
					"ZC1691381620527599616");
			inputMap.put(SandboxDataSetSpec.ORIGINAL_REPAYMENT_PLAN_NO_LIST, repaymentPlanNoSrcList);
			inputMap.put(SandboxDataSetSpec.REPAYMENT_PLAN_NO_LIST, repaymentPlanNoTarList);
			JSONObject jsonObject = new JSONObject();
			List<JSONObject> planNoList = new ArrayList<>();
			for(String planNo : repaymentPlanNoTarList) {
				jsonObject.put("repaymentNumber", planNo);
				planNoList.add(jsonObject);
			}

			inputMap.put("repaymentPlanList", planNoList);
			Map<String, Object> resultMap = new HashMap<>();
			processedResult.setData(inputMap);
			boolean result = delayTaskServices.evaluate(processedResult, delayProcessingTaskHandler,
					sandboxDataSetHandler, inputMap, resultMap, logger);
			Assert.assertTrue(result);
			List<DelayProcessingTask> taskList = getAllDelayProcessingTask();
			Assert.assertNotNull(taskList);
			Assert.assertEquals(taskList.size(), repaymentPlanNoTarList.size());
			for (DelayProcessingTask task : taskList) {
				Map<String, Object> map = JsonUtils.parse(task.getWorkParams());
				String changeType = (String) map.get("changeType");
				Assert.assertEquals(changeType, "3");
				String installmentNo = map.get("installmentNo").toString();
				Assert.assertNotEquals(installmentNo, "0");
				BigDecimal principal = (BigDecimal) map.get("principal");
				BigDecimal interest = (BigDecimal) map.get("interest");
				BigDecimal repayCharge = (BigDecimal) map.get("repayCharge");
				Assert.assertTrue(principal.signum() <= 0);
				Assert.assertTrue(interest.signum() <= 0);
				Assert.assertTrue(repayCharge.signum() <= 0);
			}

		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}

	}

	private List<DelayProcessingTask> getAllDelayProcessingTask() {
		String hql = "from DelayProcessingTask";
		@SuppressWarnings("unchecked")
		List<DelayProcessingTask> taskList = genericDaoSupport.searchForList(hql);
		if (CollectionUtils.isEmpty(taskList)) {
			return null;
		}
		return taskList;
	}
}