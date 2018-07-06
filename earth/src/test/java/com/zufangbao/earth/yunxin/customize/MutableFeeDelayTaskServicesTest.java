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
import com.zufangbao.earth.yunxin.customize.scripts.MutableFeeDelayTaskServices;
import com.zufangbao.sun.utils.JsonUtils;
import com.zufangbao.sun.yunxin.entity.api.mutableFee.MutableFeeReasonCode;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by fanxiaofan on 2017/5/10.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/local/applicationContext-*.xml" })
@Transactional
@Rollback(true)
public class MutableFeeDelayTaskServicesTest extends AbstractTransactionalJUnit4SpringContextTests {

	private final static Log logger = LogFactory.getLog(MutableFeeDelayTaskServicesTest.class);
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
		stringRedisTemplate.delete("sr:delayTaskConfigUuid_2");
	}

	@Test
	@Sql("classpath:test/yunxin/customize/test_delay_task_null_param.sql")
	public void test_delay_task_null_param() {
		DelayTaskServices delayTaskServices = new MutableFeeDelayTaskServices();
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

		boolean result1 = delayTaskServices.evaluate(processedResult, delayProcessingTaskHandler, sandboxDataSetHandler,
				null, resultMap, logger);
		Assert.assertFalse(result1);

		boolean result2 = delayTaskServices.evaluate(processedResult, null, sandboxDataSetHandler, inputMap, resultMap, logger);
		Assert.assertFalse(result2);

		boolean result3 = delayTaskServices.evaluate(processedResult, delayProcessingTaskHandler, null, inputMap,
				resultMap, logger);
		Assert.assertFalse(result3);

		boolean result4 = delayTaskServices.evaluate(processedResult, delayProcessingTaskHandler, sandboxDataSetHandler, inputMap,
				resultMap, logger);
		Assert.assertFalse(result4);
	}

	@Test
	@Sql("classpath:test/yunxin/customize/test_delay_task_other_change_type.sql")
	public void test_delay_task_other_change_type() {
		DelayTaskServices delayTaskServices = new MutableFeeDelayTaskServices();
		Result processedResult = new Result("0");
		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put(SandboxDataSetSpec.FINANCIAL_CONTRACT_UUID, "2d380fe1-7157-490d-9474-12c5a9901e29");
		inputMap.put(SandboxDataSetSpec.CONFING_UUID, "ee101fb3-042e-40f3-80f1-9f592c485fd3");
		inputMap.put(SandboxDataSetSpec.CONTRACT_UUID, "315317d0-438a-4e6d-a4b8-20a260624b6e");
		List<String> repaymentPlanNoList = Arrays.asList("ZC1750723569322520543");
		inputMap.put(SandboxDataSetSpec.REPAYMENT_PLAN_NO_LIST, repaymentPlanNoList);
		Map<String, Object> resultMap = new HashMap<>();

		Map<String, Object> extraParam = new HashMap<>();
		extraParam.put(SandboxDataSetSpec.ExtraItem.REASON_CODE, MutableFeeReasonCode.PAY_AS_YOU_GO.ordinal() + "");
		extraParam.put(SandboxDataSetSpec.ExtraItem.PRINCIPAL, BigDecimal.ZERO);
		extraParam.put(SandboxDataSetSpec.ExtraItem.INTEREST, BigDecimal.ZERO);
		extraParam.put(SandboxDataSetSpec.ExtraItem.REPAY_CHARGE, BigDecimal.TEN.negate());
		JSONObject jsonObject = new JSONObject(extraParam);
		String extraItem = JsonUtils.toJSONString(jsonObject);

		inputMap.put(SandboxDataSetSpec.EXTRA_ITEM, extraItem);

		boolean result = delayTaskServices.evaluate(processedResult, delayProcessingTaskHandler, sandboxDataSetHandler,
				inputMap, resultMap, logger);
		Assert.assertFalse(result);
	}

	@Test
	@Sql("classpath:test/yunxin/customize/test_mutablefee_charge_change_delay_task.sql")
	public void test_mutablefee_charge_change_delay_task() {
		DelayTaskServices delayTaskServices = new MutableFeeDelayTaskServices();
		Result processedResult = new Result("0");
		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put(SandboxDataSetSpec.FINANCIAL_CONTRACT_UUID, "2d380fe1-7157-490d-9474-12c5a9901e29");
		inputMap.put(SandboxDataSetSpec.CONFING_UUID, "ee101fb3-042e-40f3-80f1-9f592c485fd3");
		inputMap.put(SandboxDataSetSpec.CONTRACT_UUID, "315317d0-438a-4e6d-a4b8-20a260624b6e");
		List<String> repaymentPlanNoList = Arrays.asList("ZC1750723569322520543");
		inputMap.put(SandboxDataSetSpec.REPAYMENT_PLAN_NO_LIST, repaymentPlanNoList);
		Map<String, Object> resultMap = new HashMap<>();

		Map<String, Object> extraParam = new HashMap<>();
		extraParam.put(SandboxDataSetSpec.ExtraItem.REASON_CODE, MutableFeeReasonCode.CHARGE_CHANGE.ordinal() + "");
		extraParam.put(SandboxDataSetSpec.ExtraItem.PRINCIPAL, BigDecimal.ZERO);
		extraParam.put(SandboxDataSetSpec.ExtraItem.INTEREST, BigDecimal.ZERO);
		extraParam.put(SandboxDataSetSpec.ExtraItem.REPAY_CHARGE, BigDecimal.TEN.negate());
		JSONObject jsonObject = new JSONObject(extraParam);
		String extraItem = JsonUtils.toJSONString(jsonObject);

		inputMap.put(SandboxDataSetSpec.EXTRA_ITEM, extraItem);

		boolean result = delayTaskServices.evaluate(processedResult, delayProcessingTaskHandler, sandboxDataSetHandler,
				inputMap, resultMap, logger);
		Assert.assertTrue(result);
		List<DelayProcessingTask> taskList = getAllDelayProcessingTask();
		Assert.assertNotNull(taskList);
		Assert.assertEquals(taskList.size(), 1);
		for (DelayProcessingTask task : taskList) {
			Map<String, Object> map = JsonUtils.parse(task.getWorkParams());
			String changeType = (String) map.get("changeType");
			Assert.assertEquals(changeType, "9");
			Object principal = map.get("principal");
			Object interest = map.get("interest");
			Object repayCharge = map.get("repayCharge");
			System.out.println(
					String.format("principal:%s,interest:%s,repayCharge:%s", principal, interest, repayCharge));
		}
	}

	@Test
	@Sql("classpath:test/yunxin/customize/test_mutablefee_overdue_delay_task.sql")
	public void test_mutablefee_overdue_delay_task() {
		DelayTaskServices delayTaskServices = new MutableFeeDelayTaskServices();
		Result processedResult = new Result("0");
		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put(SandboxDataSetSpec.FINANCIAL_CONTRACT_UUID, "2d380fe1-7157-490d-9474-12c5a9901e29");
		inputMap.put(SandboxDataSetSpec.CONFING_UUID, "ee101fb3-042e-40f3-80f1-9f592c485fd3");
		inputMap.put(SandboxDataSetSpec.CONTRACT_UUID, "315317d0-438a-4e6d-a4b8-20a260624b6e");
		List<String> repaymentPlanNoList = Arrays.asList("ZC1750723569322520543");
		inputMap.put(SandboxDataSetSpec.REPAYMENT_PLAN_NO_LIST, repaymentPlanNoList);
		Map<String, Object> resultMap = new HashMap<>();

		Map<String, Object> extraParam = new HashMap<>();
		extraParam.put(SandboxDataSetSpec.ExtraItem.REASON_CODE, MutableFeeReasonCode.OVERDUE.ordinal() + "");
		extraParam.put(SandboxDataSetSpec.ExtraItem.PRINCIPAL, BigDecimal.ZERO);
		extraParam.put(SandboxDataSetSpec.ExtraItem.INTEREST, BigDecimal.ZERO);
		extraParam.put(SandboxDataSetSpec.ExtraItem.REPAY_CHARGE, BigDecimal.TEN.negate());
		JSONObject jsonObject = new JSONObject(extraParam);
		String extraItem = JsonUtils.toJSONString(jsonObject);

		inputMap.put(SandboxDataSetSpec.EXTRA_ITEM, extraItem);

		boolean result = delayTaskServices.evaluate(processedResult, delayProcessingTaskHandler, sandboxDataSetHandler,
				inputMap, resultMap, logger);
		Assert.assertTrue(result);
		List<DelayProcessingTask> taskList = getAllDelayProcessingTask();
		Assert.assertNotNull(taskList);
		Assert.assertEquals(taskList.size(), 1);
		for (DelayProcessingTask task : taskList) {
			Map<String, Object> map = JsonUtils.parse(task.getWorkParams());
			String changeType = (String) map.get("changeType");
			Assert.assertEquals(changeType, "7");
			Object principal = map.get("principal");
			Object interest = map.get("interest");
			Object repayCharge = map.get("repayCharge");
			System.out.println(
					String.format("principal:%s,interest:%s,repayCharge:%s", principal, interest, repayCharge));
		}
	}

	@Test
	@Sql("classpath:test/yunxin/delayTask/test_save_mutablefee_delay_task_by_db_source_code.sql")
	public void test_save_mutablefee_delay_task_by_db_source_code() {
		try {
			String delayTaskConfigUuid = "delayTaskConfigUuid_2";

			DelayTaskServices delayTaskServices = (DelayTaskServices) delayTaskConfigCacheHandler
					.getCompiledObjectDelayTaskConfigUuid(delayTaskConfigUuid);
			Assert.assertNotNull(delayTaskServices);

			Result processedResult = new Result("0");
			Map<String, Object> inputMap = new HashMap<String, Object>();
			inputMap.put(SandboxDataSetSpec.FINANCIAL_CONTRACT_UUID, "2d380fe1-7157-490d-9474-12c5a9901e29");
			inputMap.put(SandboxDataSetSpec.CONFING_UUID, "ee101fb3-042e-40f3-80f1-9f592c485fd3");
			inputMap.put(SandboxDataSetSpec.CONTRACT_UUID, "315317d0-438a-4e6d-a4b8-20a260624b6e");
			List<String> repaymentPlanNoList = Arrays.asList("ZC1750723569322520543");
			inputMap.put(SandboxDataSetSpec.REPAYMENT_PLAN_NO_LIST, repaymentPlanNoList);
			Map<String, Object> resultMap = new HashMap<>();

			Map<String, Object> extraParam = new HashMap<>();
			extraParam.put(SandboxDataSetSpec.ExtraItem.REASON_CODE, MutableFeeReasonCode.OVERDUE.ordinal() + "");
			extraParam.put(SandboxDataSetSpec.ExtraItem.PRINCIPAL, BigDecimal.ZERO);
			extraParam.put(SandboxDataSetSpec.ExtraItem.INTEREST, BigDecimal.ZERO);
			extraParam.put(SandboxDataSetSpec.ExtraItem.REPAY_CHARGE, BigDecimal.TEN.negate());
			JSONObject jsonObject = new JSONObject(extraParam);
			String extraItem = JsonUtils.toJSONString(jsonObject);

			inputMap.put(SandboxDataSetSpec.EXTRA_ITEM, extraItem);

			boolean result = delayTaskServices.evaluate(processedResult, delayProcessingTaskHandler, sandboxDataSetHandler,
					inputMap, resultMap, logger);
			Assert.assertTrue(result);
			List<DelayProcessingTask> taskList = getAllDelayProcessingTask();
			Assert.assertNotNull(taskList);
			Assert.assertEquals(taskList.size(), 1);
			for (DelayProcessingTask task : taskList) {
				Map<String, Object> map = JsonUtils.parse(task.getWorkParams());
				String changeType = (String) map.get("changeType");
				Assert.assertEquals(changeType, "7");
				Object principal = map.get("principal");
				Object interest = map.get("interest");
				Object repayCharge = map.get("repayCharge");
				System.out.println(
						String.format("principal:%s,interest:%s,repayCharge:%s", principal, interest, repayCharge));
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