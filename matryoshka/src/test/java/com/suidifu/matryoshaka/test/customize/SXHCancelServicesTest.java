package com.suidifu.matryoshaka.test.customize;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.suidifu.matryoshaka.test.scripts.SXHCancelServices;
import com.suidifu.matryoshka.handler.SandboxDataSetHandler;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.utils.JsonUtils;
import com.zufangbao.sun.yunxin.entity.model.api.modify.RepaymentPlanModifyRequestDataModel;

/**
 * Created by louguanyang on 2017/4/23.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/context/applicationContext-*.xml",
		"classpath:/local/applicationContext-*.xml" })

@Transactional()
@Rollback(true)
public class SXHCancelServicesTest {
	private Log logger = LogFactory.getLog(ChargeChangeServicesTest.class);

	@Autowired
	private SandboxDataSetHandler sandboxDataSetHandler;
	// 无数据验证不通过
		@Test
		@Sql("classpath:test/yunxin/script/test_customize_services_no_data.sql")
		public void test_charge_change_services_no_data() {

			Map<String, String> preRequest = new HashMap<>();
			List<RepaymentPlanModifyRequestDataModel> requestDataList = new ArrayList<>();
			RepaymentPlanModifyRequestDataModel model = new RepaymentPlanModifyRequestDataModel();
			model.setAssetInterest(new BigDecimal("100"));
			model.setAssetPrincipal(new BigDecimal("1000"));
			model.setMaintenanceCharge(new BigDecimal("0"));
			model.setServiceCharge(new BigDecimal("0"));
			model.setOtherCharge(new BigDecimal("0"));
			model.setAssetType(0);
			model.setAssetRecycleDate("2099-04-20");
			requestDataList.add(model);

			RepaymentPlanModifyRequestDataModel model2 = new RepaymentPlanModifyRequestDataModel();
			model2.setAssetInterest(new BigDecimal("100"));
			model2.setAssetPrincipal(new BigDecimal("1000"));
			model2.setMaintenanceCharge(new BigDecimal("0"));
			model2.setServiceCharge(new BigDecimal("0"));
			model2.setOtherCharge(new BigDecimal("0"));
			model2.setAssetType(0);
			model2.setAssetRecycleDate("2099-05-20");

			requestDataList.add(model2);
			String requestData = JsonUtils.toJSONString(requestDataList);
			preRequest.put("requestData", requestData);
			preRequest.put("uniqueId", "ad6b3053-8625-4eb6-a78a-dcabc6132b5d");
			SXHCancelServices services = new SXHCancelServices();

			boolean result = services.evaluate(sandboxDataSetHandler, preRequest, new HashMap<String, String>(), logger);
			Assert.assertFalse(result);

		}
	// 非佰仟信贷合同不允许变更
	@Test
	@Sql("classpath:test/yunxin/script/test_Cancel_services_not_allow_freewheeling_repayment.sql")
	public void test_Cancel_services_not_allow_freewheeling_repayment() {

		Map<String, String> preRequest = new HashMap<>();
		List<RepaymentPlanModifyRequestDataModel> requestDataList = new ArrayList<>();
		RepaymentPlanModifyRequestDataModel model = new RepaymentPlanModifyRequestDataModel();
		model.setAssetInterest(new BigDecimal("100"));
		model.setAssetPrincipal(new BigDecimal("1000"));
		model.setMaintenanceCharge(new BigDecimal("0"));
		model.setServiceCharge(new BigDecimal("0"));
		model.setOtherCharge(new BigDecimal("0"));
		model.setAssetType(0);
		model.setAssetRecycleDate("2099-04-20");
		requestDataList.add(model);

		RepaymentPlanModifyRequestDataModel model2 = new RepaymentPlanModifyRequestDataModel();
		model2.setAssetInterest(new BigDecimal("100"));
		model2.setAssetPrincipal(new BigDecimal("1000"));
		model2.setMaintenanceCharge(new BigDecimal("0"));
		model2.setServiceCharge(new BigDecimal("0"));
		model2.setOtherCharge(new BigDecimal("0"));
		model2.setAssetType(0);
		model2.setAssetRecycleDate("2099-05-20");

		requestDataList.add(model2);
		String requestData = JsonUtils.toJSONString(requestDataList);
		preRequest.put("requestData", requestData);
		preRequest.put("uniqueId", "ad6b3053-8625-4eb6-a78a-dcabc6132b5d");


		SXHCancelServices services = new SXHCancelServices();
		boolean result = services.evaluate(sandboxDataSetHandler, preRequest, new HashMap<>(), logger);
		Assert.assertFalse(result);
	}

	// 应还日当日不允许变更
	@Test
	@Sql("classpath:test/yunxin/script/test_Cancel_services_at_asset_recycle_date.sql")
	public void test_Cancel_services_at_asset_recycle_date() {

		Date today = DateUtils.parseDate(DateUtils.format(new Date()));
		Map<String, String> preRequest = new HashMap<>();
		List<RepaymentPlanModifyRequestDataModel> requestDataList = new ArrayList<>();
		RepaymentPlanModifyRequestDataModel model = new RepaymentPlanModifyRequestDataModel();
		model.setAssetInterest(new BigDecimal("100"));
		model.setAssetPrincipal(new BigDecimal("1000"));
		model.setMaintenanceCharge(new BigDecimal("0"));
		model.setServiceCharge(new BigDecimal("0"));
		model.setOtherCharge(new BigDecimal("0"));
		model.setAssetType(0);
		model.setAssetRecycleDate(DateUtils.format(today));
		requestDataList.add(model);

		RepaymentPlanModifyRequestDataModel model2 = new RepaymentPlanModifyRequestDataModel();
		model2.setAssetInterest(new BigDecimal("100"));
		model2.setAssetPrincipal(new BigDecimal("1000"));
		model2.setMaintenanceCharge(new BigDecimal("0"));
		model2.setServiceCharge(new BigDecimal("0"));
		model2.setOtherCharge(new BigDecimal("0"));
		model2.setAssetType(0);
		model2.setAssetRecycleDate("2099-05-20");

		requestDataList.add(model2);
		String requestData = JsonUtils.toJSONString(requestDataList);
		preRequest.put("requestData", requestData);
		preRequest.put("uniqueId", "ad6b3053-8625-4eb6-a78a-dcabc6132b5d");


		SXHCancelServices services = new SXHCancelServices();
		boolean result = services.evaluate(sandboxDataSetHandler, preRequest, new HashMap<>(), logger);
		Assert.assertFalse(result);
	}

	// 已过应还日不允许变更
	@Test
	@Sql("classpath:test/yunxin/script/test_Cancel_services_overdue.sql")
	public void test_Cancel_services_overdue() {

		Map<String, String> preRequest = new HashMap<>();
		List<RepaymentPlanModifyRequestDataModel> requestDataList = new ArrayList<>();
		RepaymentPlanModifyRequestDataModel model = new RepaymentPlanModifyRequestDataModel();
		model.setAssetInterest(new BigDecimal("100"));
		model.setAssetPrincipal(new BigDecimal("1000"));
		model.setMaintenanceCharge(new BigDecimal("0"));
		model.setServiceCharge(new BigDecimal("0"));
		model.setOtherCharge(new BigDecimal("0"));
		model.setAssetType(0);
		model.setAssetRecycleDate("2017-04-01");
		requestDataList.add(model);

		RepaymentPlanModifyRequestDataModel model2 = new RepaymentPlanModifyRequestDataModel();
		model2.setAssetInterest(new BigDecimal("100"));
		model2.setAssetPrincipal(new BigDecimal("1000"));
		model2.setMaintenanceCharge(new BigDecimal("0"));
		model2.setServiceCharge(new BigDecimal("0"));
		model2.setOtherCharge(new BigDecimal("0"));
		model2.setAssetType(0);
		model2.setAssetRecycleDate("2099-05-20");

		requestDataList.add(model2);
		String requestData = JsonUtils.toJSONString(requestDataList);
		preRequest.put("requestData", requestData);
		preRequest.put("uniqueId", "ad6b3053-8625-4eb6-a78a-dcabc6132b5d");


		SXHCancelServices services = new SXHCancelServices();
		boolean result = services.evaluate(sandboxDataSetHandler, preRequest, new HashMap<>(), logger);
		Assert.assertFalse(result);
	}

	// 还款成功不可变更
	@Test
	@Sql("classpath:test/yunxin/script/test_Cancel_services_execute_success.sql")
	public void test_Cancel_services_execute_success() {

		Map<String, String> preRequest = new HashMap<>();
		List<RepaymentPlanModifyRequestDataModel> requestDataList = new ArrayList<>();
		RepaymentPlanModifyRequestDataModel model = new RepaymentPlanModifyRequestDataModel();
		model.setAssetInterest(new BigDecimal("100"));
		model.setAssetPrincipal(new BigDecimal("1000"));
		model.setMaintenanceCharge(new BigDecimal("0"));
		model.setServiceCharge(new BigDecimal("0"));
		model.setOtherCharge(new BigDecimal("0"));
		model.setAssetType(0);
		model.setAssetRecycleDate("2099-04-20");
		requestDataList.add(model);

		RepaymentPlanModifyRequestDataModel model2 = new RepaymentPlanModifyRequestDataModel();
		model2.setAssetInterest(new BigDecimal("100"));
		model2.setAssetPrincipal(new BigDecimal("1000"));
		model2.setMaintenanceCharge(new BigDecimal("0"));
		model2.setServiceCharge(new BigDecimal("0"));
		model2.setOtherCharge(new BigDecimal("0"));
		model2.setAssetType(0);
		model2.setAssetRecycleDate("2099-05-20");

		requestDataList.add(model2);
		String requestData = JsonUtils.toJSONString(requestDataList);
		preRequest.put("requestData", requestData);
		preRequest.put("uniqueId", "ad6b3053-8625-4eb6-a78a-dcabc6132b5d");


		SXHCancelServices services = new SXHCancelServices();
		boolean result = services.evaluate(sandboxDataSetHandler, preRequest, new HashMap<>(), logger);
		Assert.assertFalse(result);
	}

	// 实收>0不允许变更
	@Test
	@Sql("classpath:test/yunxin/script/test_Cancel_services_have_actual_amount.sql")
	public void test_Cancel_services_have_actual_amount() {

		Map<String, String> preRequest = new HashMap<>();
		List<RepaymentPlanModifyRequestDataModel> requestDataList = new ArrayList<>();
		RepaymentPlanModifyRequestDataModel model = new RepaymentPlanModifyRequestDataModel();
		model.setAssetInterest(new BigDecimal("100"));
		model.setAssetPrincipal(new BigDecimal("1000"));
		model.setMaintenanceCharge(new BigDecimal("0"));
		model.setServiceCharge(new BigDecimal("0"));
		model.setOtherCharge(new BigDecimal("0"));
		model.setAssetType(0);
		model.setAssetRecycleDate("2099-04-20");
		requestDataList.add(model);

		RepaymentPlanModifyRequestDataModel model2 = new RepaymentPlanModifyRequestDataModel();
		model2.setAssetInterest(new BigDecimal("100"));
		model2.setAssetPrincipal(new BigDecimal("1000"));
		model2.setMaintenanceCharge(new BigDecimal("0"));
		model2.setServiceCharge(new BigDecimal("0"));
		model2.setOtherCharge(new BigDecimal("0"));
		model2.setAssetType(0);
		model2.setAssetRecycleDate("2099-05-20");

		requestDataList.add(model2);
		String requestData = JsonUtils.toJSONString(requestDataList);
		preRequest.put("requestData", requestData);
		preRequest.put("uniqueId", "ad6b3053-8625-4eb6-a78a-dcabc6132b5d");


		SXHCancelServices services = new SXHCancelServices();
		boolean result = services.evaluate(sandboxDataSetHandler, preRequest, new HashMap<>(), logger);
		Assert.assertFalse(result);
	}

	@Test
	@Sql("classpath:test/yunxin/script/test_Cancel_services.sql")
	public void test_Cancel_services() {

		Map<String, String> preRequest = new HashMap<>();
		List<RepaymentPlanModifyRequestDataModel> requestDataList = new ArrayList<>();
		RepaymentPlanModifyRequestDataModel model = new RepaymentPlanModifyRequestDataModel();
		model.setAssetInterest(new BigDecimal("100"));
		model.setAssetPrincipal(new BigDecimal("1000"));
		model.setMaintenanceCharge(new BigDecimal("0"));
		model.setServiceCharge(new BigDecimal("0"));
		model.setOtherCharge(new BigDecimal("0"));
		model.setAssetType(0);
		model.setAssetRecycleDate("2099-04-20");
		requestDataList.add(model);

		RepaymentPlanModifyRequestDataModel model2 = new RepaymentPlanModifyRequestDataModel();
		model2.setAssetInterest(new BigDecimal("100"));
		model2.setAssetPrincipal(new BigDecimal("1000"));
		model2.setMaintenanceCharge(new BigDecimal("0"));
		model2.setServiceCharge(new BigDecimal("0"));
		model2.setOtherCharge(new BigDecimal("0"));
		model2.setAssetType(0);
		model2.setAssetRecycleDate("2099-05-20");

		requestDataList.add(model2);
		String requestData = JsonUtils.toJSONString(requestDataList);
		preRequest.put("requestData", requestData);
		preRequest.put("uniqueId", "ad6b3053-8625-4eb6-a78a-dcabc6132b5d");


		SXHCancelServices services = new SXHCancelServices();
		boolean result = services.evaluate(sandboxDataSetHandler, preRequest, new HashMap<>(), logger);
		Assert.assertTrue(result);
	}
}