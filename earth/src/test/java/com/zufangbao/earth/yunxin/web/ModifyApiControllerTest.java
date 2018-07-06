package com.zufangbao.earth.yunxin.web;

import com.demo2do.core.entity.Result;
import com.demo2do.core.utils.JsonUtils;
import com.zufangbao.earth.yunxin.api.controller.ModifyApiController;
import com.zufangbao.sun.api.model.modify.RepaymentPlanModifyModel;
import com.zufangbao.sun.entity.modify.RepaymentInformationModifyModel;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.yunxin.entity.AssetSet;
import com.zufangbao.sun.yunxin.entity.RepaymentPlanType;
import com.zufangbao.sun.yunxin.entity.model.api.PrepaymentModifyModel;
import com.zufangbao.sun.yunxin.entity.model.api.modify.RepaymentPlanModifyRequestDataModel;
import com.zufangbao.sun.yunxin.service.RepaymentPlanService;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {

		"classpath:/local/applicationContext-*.xml",
		"classpath:/DispatcherServlet.xml" })

@Transactional()
@TransactionConfiguration(defaultRollback = true)
@WebAppConfiguration(value="webapp")
public class ModifyApiControllerTest extends AbstractTransactionalJUnit4SpringContextTests {

	@Autowired
	ModifyApiController modifyApiController;

	@Autowired
	RepaymentPlanService repaymentPlanService;

	@Test
	@Sql("classpath:test/yunxin/web/modifyRepaymentPlan.sql")
	public void test_modifyRepaymentPlan() {
		RepaymentPlanModifyRequestDataModel repaymentPlanModifyRequestDataModel1 = new RepaymentPlanModifyRequestDataModel();
		RepaymentPlanModifyRequestDataModel repaymentPlanModifyRequestDataModel2 = new RepaymentPlanModifyRequestDataModel();
		RepaymentPlanModifyRequestDataModel repaymentPlanModifyRequestDataModel3 = new RepaymentPlanModifyRequestDataModel();

		repaymentPlanModifyRequestDataModel1.setAssetRecycleDate(DateUtils.format(DateUtils.addDays(DateUtils.getToday(), 5)));
		repaymentPlanModifyRequestDataModel2.setAssetRecycleDate(DateUtils.format(DateUtils.addDays(DateUtils.getToday(), 15)));
		repaymentPlanModifyRequestDataModel3.setAssetRecycleDate(DateUtils.format(DateUtils.addDays(DateUtils.getToday(), 25)));

		repaymentPlanModifyRequestDataModel1.setAssetPrincipal(new BigDecimal("70000.00"));
		repaymentPlanModifyRequestDataModel2.setAssetPrincipal(new BigDecimal("50000.00"));
		repaymentPlanModifyRequestDataModel3.setAssetPrincipal(new BigDecimal("40000.00"));

		List<RepaymentPlanModifyRequestDataModel> requestDataModels = new ArrayList<>();
		requestDataModels.add(repaymentPlanModifyRequestDataModel1);
		requestDataModels.add(repaymentPlanModifyRequestDataModel2);
		requestDataModels.add(repaymentPlanModifyRequestDataModel3);
		String requestData = JsonUtils.toJsonString(requestDataModels);

		RepaymentPlanModifyModel modifyModel = new RepaymentPlanModifyModel();
		modifyModel.setFn("200001");
		modifyModel.setRequestNo("AAAbbb");
		modifyModel.setUniqueId("e568793f-a44c-4362-9e78-0ce433131f3e");
		//modifyModel.setContractNo("云信信2016-241-DK(428522112675736881)");
		modifyModel.setRequestReason("2");
		modifyModel.setRequestData(requestData);

		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();

		String message = modifyApiController.modifyRepaymentPlan(modifyModel, request, response);
		Assert.assertTrue(message.contains("成功"));
	}

	/**
	 * uniqueId与contract 为空 或 为null 或 两者都存在
	 */
	@Test
	@Sql("classpath:test/yunxin/web/modifyRepaymentPlan.sql")
	public void test_modifyRepaymentPlan_uniqueId_contractNo_empty_or_null() {
		RepaymentPlanModifyModel modifyModel = new RepaymentPlanModifyModel();
		modifyModel.setFn("200001");
		modifyModel.setRequestNo("AAA");
		modifyModel.setUniqueId("");
		modifyModel.setContractNo("");
		modifyModel.setRequestReason("2");

		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();

		String message1 = modifyApiController.modifyRepaymentPlan(modifyModel, request, response);
		Assert.assertTrue(message1.contains("请选填其中一种编号［uniqueId，contractNo］"));

		modifyModel.setUniqueId(null);
		modifyModel.setContractNo(null);

		String message2 = modifyApiController.modifyRepaymentPlan(modifyModel, request, response);
		Assert.assertTrue(message2.contains("请选填其中一种编号［uniqueId，contractNo］"));

		modifyModel.setUniqueId("e568793f-a44c-4362-9e78-0ce433131f3e");
		modifyModel.setContractNo("云信信2016-241-DK(428522112675736881)");

		String message3 = modifyApiController.modifyRepaymentPlan(modifyModel, request, response);
		Assert.assertTrue(message3.contains("请选填其中一种编号［uniqueId，contractNo］"));
	}

	/**
	 * requestNo为空或null
	 */
	@Test
	@Sql("classpath:test/yunxin/web/modifyRepaymentPlan.sql")
	public void test_modifyRepaymentPlan_requestNo_empty_or_null() {
		RepaymentPlanModifyModel modifyModel = new RepaymentPlanModifyModel();
		modifyModel.setFn("200001");
		modifyModel.setRequestNo("");
		modifyModel.setUniqueId("e568793f-a44c-4362-9e78-0ce433131f3e");
		modifyModel.setRequestReason("2");

		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();

		String message1 = modifyApiController.modifyRepaymentPlan(modifyModel, request, response);
		Assert.assertTrue(message1.contains("请求唯一标识［requestNo］，不能为空"));

		modifyModel.setRequestNo(null);

		String message2 = modifyApiController.modifyRepaymentPlan(modifyModel, request, response);
		Assert.assertTrue(message2.contains("请求唯一标识［requestNo］，不能为空"));
	}

	/**
	 * requestReason为空或null
	 */
	@Test
	@Sql("classpath:test/yunxin/web/modifyRepaymentPlan.sql")
	public void test_modifyRepaymentPlan_requestReason_empty_or_null() {
		RepaymentPlanModifyModel modifyModel = new RepaymentPlanModifyModel();
		modifyModel.setFn("200001");
		modifyModel.setRequestNo("AAA");
		modifyModel.setUniqueId("e568793f-a44c-4362-9e78-0ce433131f3e");
		modifyModel.setRequestReason("");

		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();

		String message1 = modifyApiController.modifyRepaymentPlan(modifyModel, request, response);
		Assert.assertTrue(message1.contains("请求原因［requestReason］，不能为空"));

		modifyModel.setRequestReason(null);

		String message2 = modifyApiController.modifyRepaymentPlan(modifyModel, request, response);
		Assert.assertTrue(message2.contains("请求原因［requestReason］，不能为空"));
	}

	/**
	 * 请求原因不合法（<0 或 >2)
	 */
	@Test
	@Sql("classpath:test/yunxin/web/modifyRepaymentPlan.sql")
	public void test_modifyRepaymentPlan_requestReason_illegal() {
		RepaymentPlanModifyModel modifyModel = new RepaymentPlanModifyModel();
		modifyModel.setFn("200001");
		modifyModel.setRequestNo("AAA");
		modifyModel.setUniqueId("e568793f-a44c-4362-9e78-0ce433131f3e");
		modifyModel.setRequestReason("10");

		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();

		String message = modifyApiController.modifyRepaymentPlan(modifyModel, request, response);
		Assert.assertTrue(message.contains("请求原因［requestReason］，内容不合法"));
	}

	/**
	 * 请求原因不是数字
	 */
	@Test
	@Sql("classpath:test/yunxin/web/modifyRepaymentPlan.sql")
	public void test_modifyRepaymentPlan_requestReason_is_not_number() {
		RepaymentPlanModifyModel modifyModel = new RepaymentPlanModifyModel();
		modifyModel.setFn("200001");
		modifyModel.setRequestNo("AAA");
		modifyModel.setUniqueId("e568793f-a44c-4362-9e78-0ce433131f3e");
		modifyModel.setRequestReason("abc");

		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();

		String message = modifyApiController.modifyRepaymentPlan(modifyModel, request, response);
		Assert.assertTrue(message.contains("请求原因［requestReason］，内容不合法！"));
	}

	/**
	 * requestData为null或空
	 */
	@Test
	@Sql("classpath:test/yunxin/web/modifyRepaymentPlan.sql")
	public void test_modifyRepaymentPlan_requestData_empty_or_null() {
		RepaymentPlanModifyModel modifyModel = new RepaymentPlanModifyModel();
		modifyModel.setFn("200001");
		modifyModel.setRequestNo("AAA");
		modifyModel.setUniqueId("e568793f-a44c-4362-9e78-0ce433131f3e");
		modifyModel.setRequestReason("2");
		modifyModel.setRequestData("");

		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();

		String message1 = modifyApiController.modifyRepaymentPlan(modifyModel, request, response);
		Assert.assertTrue(message1.contains("具体变更内容［requestData］，不能为空"));

		modifyModel.setRequestData(null);

		String message2 = modifyApiController.modifyRepaymentPlan(modifyModel, request, response);
		Assert.assertTrue(message2.contains("具体变更内容［requestData］，不能为空"));
	}

	/**
	 * 计划本金非法
	 */
	@Test
	@Sql("classpath:test/yunxin/web/modifyRepaymentPlan.sql")
	public void test_modifyRepaymentPlan_assetPrincipal_illegal() {
		RepaymentPlanModifyRequestDataModel requestDataModel = new RepaymentPlanModifyRequestDataModel();
		requestDataModel.setAssetRecycleDate(DateUtils.format(DateUtils.addDays(DateUtils.getToday(), 5)));
		requestDataModel.setAssetPrincipal(new BigDecimal("-1999.00"));
		requestDataModel.setAssetInterest(new BigDecimal("20.00"));

		List<RepaymentPlanModifyRequestDataModel> requestDataModels = new ArrayList<>();
		requestDataModels.add(requestDataModel);
		String requestData = JsonUtils.toJsonString(requestDataModels);

		RepaymentPlanModifyModel modifyModel = new RepaymentPlanModifyModel();
		modifyModel.setFn("200001");
		modifyModel.setRequestNo("AAA");
		modifyModel.setUniqueId("e568793f-a44c-4362-9e78-0ce433131f3e");
		//modifyModel.setContractNo("云信信2016-241-DK(428522112675736881)");
		modifyModel.setRequestReason("2");
		modifyModel.setRequestData(requestData);

		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();

		String message = modifyApiController.modifyRepaymentPlan(modifyModel, request, response);
		Assert.assertTrue(message.contains("无效的计划本金总额!"));
	}

	/**
	 * 计划利息非法
	 */
	@Test
	@Sql("classpath:test/yunxin/web/modifyRepaymentPlan.sql")
	public void test_modifyRepaymentPlan_assetInsterest_illegal() {
		RepaymentPlanModifyRequestDataModel requestDataModel = new RepaymentPlanModifyRequestDataModel();
		requestDataModel.setAssetRecycleDate(DateUtils.format(DateUtils.addDays(DateUtils.getToday(), 5)));
		requestDataModel.setAssetPrincipal(new BigDecimal("1999.00"));
		requestDataModel.setAssetInterest(new BigDecimal("-20.00"));

		List<RepaymentPlanModifyRequestDataModel> requestDataModels = new ArrayList<>();
		requestDataModels.add(requestDataModel);
		String requestData = JsonUtils.toJsonString(requestDataModels);

		RepaymentPlanModifyModel modifyModel = new RepaymentPlanModifyModel();
		modifyModel.setFn("200001");
		modifyModel.setRequestNo("AAA");
		modifyModel.setUniqueId("e568793f-a44c-4362-9e78-0ce433131f3e");
		//modifyModel.setContractNo("云信信2016-241-DK(428522112675736881)");
		modifyModel.setRequestReason("2");
		modifyModel.setRequestData(requestData);

		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();

		String message = modifyApiController.modifyRepaymentPlan(modifyModel, request, response);
		Assert.assertTrue(message.contains("无效的计划利息总额!"));
	}

	/**
	 * 计划还款日期错误
	 */
	@Test
	@Sql("classpath:test/yunxin/web/modifyRepaymentPlan.sql")
	public void test_modifyRepaymentPlan_assetRecycleDate_illegal() {
		RepaymentPlanModifyRequestDataModel requestDataModel = new RepaymentPlanModifyRequestDataModel();
		requestDataModel.setAssetRecycleDate(DateUtils.format(DateUtils.addDays(DateUtils.getToday(), -5)));
		requestDataModel.setAssetPrincipal(new BigDecimal("1999.00"));
		requestDataModel.setAssetInterest(new BigDecimal("20.00"));

		List<RepaymentPlanModifyRequestDataModel> requestDataModels = new ArrayList<>();
		requestDataModels.add(requestDataModel);
		String requestData = JsonUtils.toJsonString(requestDataModels);

		RepaymentPlanModifyModel modifyModel = new RepaymentPlanModifyModel();
		modifyModel.setFn("200001");
		modifyModel.setRequestNo("AAA");
		modifyModel.setUniqueId("e568793f-a44c-4362-9e78-0ce433131f3e");
		//modifyModel.setContractNo("云信信2016-241-DK(428522112675736881)");
		modifyModel.setRequestReason("2");
		modifyModel.setRequestData(requestData);

		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();

		String message = modifyApiController.modifyRepaymentPlan(modifyModel, request, response);
		Assert.assertTrue(message.contains("计划还款日期排序错误，需按计划还款日期递增"));
	}

	/**
	 * 计划变更原因为提前结清或提前部分还款时，还款计划类型应为提前
	 */
	@Test
	@Sql("classpath:test/yunxin/web/modifyRepaymentPlan.sql")
	public void test_modifyRepaymentPlan_assetType_illegal() {
		RepaymentPlanModifyRequestDataModel repaymentPlanModifyRequestDataModel3 = new RepaymentPlanModifyRequestDataModel();

		repaymentPlanModifyRequestDataModel3.setAssetRecycleDate(DateUtils.format(DateUtils.addDays(DateUtils.getToday(), 25)));

		repaymentPlanModifyRequestDataModel3.setAssetPrincipal(new BigDecimal("160000.00"));

//		repaymentPlanModifyRequestDataModel3.setAssetType(1);

		List<RepaymentPlanModifyRequestDataModel> requestDataModels = new ArrayList<>();
		requestDataModels.add(repaymentPlanModifyRequestDataModel3);
		String requestData = JsonUtils.toJsonString(requestDataModels);

		RepaymentPlanModifyModel modifyModel = new RepaymentPlanModifyModel();
		modifyModel.setFn("200001");
		modifyModel.setRequestNo("AAAbbb");
		modifyModel.setUniqueId("e568793f-a44c-4362-9e78-0ce433131f3e");
		//modifyModel.setContractNo("云信信2016-241-DK(428522112675736881)");
		modifyModel.setRequestReason("1");
		modifyModel.setRequestData(requestData);

		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();

		String message = modifyApiController.modifyRepaymentPlan(modifyModel, request, response);
		Assert.assertTrue(message.contains("计划变更原因为提前结清或提前部分还款时，还款计划类型应为提前！"));
	}

	/**
	 * 计划变更原因不是提前结清和提前部分还款时，还款计划类型不能为提前
	 */
	@Test
	@Sql("classpath:test/yunxin/web/modifyRepaymentPlan.sql")
	public void test_modifyRepaymentPlan_assetType_illegal_1() {
		RepaymentPlanModifyRequestDataModel repaymentPlanModifyRequestDataModel3 = new RepaymentPlanModifyRequestDataModel();

		repaymentPlanModifyRequestDataModel3.setAssetRecycleDate(DateUtils.format(DateUtils.addDays(DateUtils.getToday(), 25)));

		repaymentPlanModifyRequestDataModel3.setAssetPrincipal(new BigDecimal("160000.00"));

		repaymentPlanModifyRequestDataModel3.setAssetType(0);

		List<RepaymentPlanModifyRequestDataModel> requestDataModels = new ArrayList<>();
		requestDataModels.add(repaymentPlanModifyRequestDataModel3);
		String requestData = JsonUtils.toJsonString(requestDataModels);

		RepaymentPlanModifyModel modifyModel = new RepaymentPlanModifyModel();
		modifyModel.setFn("200001");
		modifyModel.setRequestNo("AAAbbb");
		modifyModel.setUniqueId("e568793f-a44c-4362-9e78-0ce433131f3e");
		//modifyModel.setContractNo("云信信2016-241-DK(428522112675736881)");
		modifyModel.setRequestReason("9");
		modifyModel.setRequestData(requestData);

		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();

		String message = modifyApiController.modifyRepaymentPlan(modifyModel, request, response);
		Assert.assertTrue(message.contains("计划变更原因不是提前结清和提前部分还款时，还款计划类型不能为提前！"));
	}

	/**
	 * 提前结清时未偿还款计划只能有一条
	 */
	@Test
	@Sql("classpath:test/yunxin/web/modifyRepaymentPlan.sql")
	public void test_modifyRepaymentPlan_preClear_sizeIllegal() {
		RepaymentPlanModifyRequestDataModel repaymentPlanModifyRequestDataModel1 = new RepaymentPlanModifyRequestDataModel();
		RepaymentPlanModifyRequestDataModel repaymentPlanModifyRequestDataModel2 = new RepaymentPlanModifyRequestDataModel();
		RepaymentPlanModifyRequestDataModel repaymentPlanModifyRequestDataModel3 = new RepaymentPlanModifyRequestDataModel();

		repaymentPlanModifyRequestDataModel1.setAssetRecycleDate(DateUtils.format(DateUtils.addDays(DateUtils.getToday(), 5)));
		repaymentPlanModifyRequestDataModel2.setAssetRecycleDate(DateUtils.format(DateUtils.addDays(DateUtils.getToday(), 15)));
		repaymentPlanModifyRequestDataModel3.setAssetRecycleDate(DateUtils.format(DateUtils.addDays(DateUtils.getToday(), 25)));

		repaymentPlanModifyRequestDataModel1.setAssetPrincipal(new BigDecimal("70000.00"));
		repaymentPlanModifyRequestDataModel2.setAssetPrincipal(new BigDecimal("50000.00"));
		repaymentPlanModifyRequestDataModel3.setAssetPrincipal(new BigDecimal("40000.00"));

		repaymentPlanModifyRequestDataModel1.setAssetType(0);
		repaymentPlanModifyRequestDataModel2.setAssetType(0);
		repaymentPlanModifyRequestDataModel3.setAssetType(0);

		List<RepaymentPlanModifyRequestDataModel> requestDataModels = new ArrayList<>();
		requestDataModels.add(repaymentPlanModifyRequestDataModel1);
		requestDataModels.add(repaymentPlanModifyRequestDataModel2);
		requestDataModels.add(repaymentPlanModifyRequestDataModel3);
		String requestData = JsonUtils.toJsonString(requestDataModels);

		RepaymentPlanModifyModel modifyModel = new RepaymentPlanModifyModel();
		modifyModel.setFn("200001");
		modifyModel.setRequestNo("AAAbbb");
		modifyModel.setUniqueId("e568793f-a44c-4362-9e78-0ce433131f3e");
		//modifyModel.setContractNo("云信信2016-241-DK(428522112675736881)");
		modifyModel.setRequestReason("0");
		modifyModel.setRequestData(requestData);

		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();

		String message = modifyApiController.modifyRepaymentPlan(modifyModel, request, response);
		Assert.assertTrue(message.contains("提前结清时未偿还款计划只能有一条"));
	}

	/**
	 * 计划还款日期不能晚于贷款合同终止日期后108天
	 */
	@Test
	@Sql("classpath:test/yunxin/web/modifyRepaymentPlan.sql")
	public void test_modifyRepaymentPlan_assetRecycleDate_tooLate() {
		RepaymentPlanModifyRequestDataModel repaymentPlanModifyRequestDataModel3 = new RepaymentPlanModifyRequestDataModel();

		Date contractEndDate = com.demo2do.core.utils.DateUtils.parseDate("2018-12-09", "yy-MM-dd");
		repaymentPlanModifyRequestDataModel3.setAssetRecycleDate(DateUtils.format(DateUtils.addDays(contractEndDate, 109)));

		repaymentPlanModifyRequestDataModel3.setAssetPrincipal(new BigDecimal("160000.00"));

		repaymentPlanModifyRequestDataModel3.setAssetType(0);

		List<RepaymentPlanModifyRequestDataModel> requestDataModels = new ArrayList<>();
		requestDataModels.add(repaymentPlanModifyRequestDataModel3);
		String requestData = JsonUtils.toJsonString(requestDataModels);

		RepaymentPlanModifyModel modifyModel = new RepaymentPlanModifyModel();
		modifyModel.setFn("200001");
		modifyModel.setRequestNo("AAAbbb");
		modifyModel.setUniqueId("e568793f-a44c-4362-9e78-0ce433131f3e");
		//modifyModel.setContractNo("云信信2016-241-DK(428522112675736881)");
		modifyModel.setRequestReason("1");
		modifyModel.setRequestData(requestData);

		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();

		String message = modifyApiController.modifyRepaymentPlan(modifyModel, request, response);
//		Assert.assertTrue(message.contains("计划还款日期不能晚于贷款合同终止日期后108天!"));
		Assert.assertTrue(message.contains("计划还款日期不能晚于贷款合同结束日108天"));

	}

	@Test
	public void test_modifyRepaymentInformation_error1() {
		RepaymentInformationModifyModel modifyModel = new RepaymentInformationModifyModel();
		HttpServletRequest request = new MockHttpServletRequest();
		HttpServletResponse response = new MockHttpServletResponse();
		String message = modifyApiController.modifyRepaymentInformation(modifyModel, request, response);
		Assert.assertTrue(message.contains("请必填其中编号［bankCode，bankAccount，bankCity，bankProvince，bankName］"));

	}

	@Test
	public void test_modifyRepaymentInformation_error2() {
		RepaymentInformationModifyModel modifyModel = new RepaymentInformationModifyModel();
		HttpServletRequest request = new MockHttpServletRequest();
		HttpServletResponse response = new MockHttpServletResponse();

		modifyModel.setBankCode("C10641");
		modifyModel.setBankProvince("500000");
		modifyModel.setBankCity("141000");
		modifyModel.setBankAccount("sdfghjkl");
		modifyModel.setBankName("奥地利中央合作银行");
		String message = modifyApiController.modifyRepaymentInformation( modifyModel, request, response);
		Assert.assertTrue(message.contains("请选填其中一种编号［uniqueId，contractNo］"));

	}

	@Test
	@Sql("classpath:test/yunxin/api/testModifyRepaymentInformationNormal.sql")
	public void test_modifyRepaymentInformation_error3() {
		RepaymentInformationModifyModel modifyModel = new RepaymentInformationModifyModel();
		HttpServletRequest request = new MockHttpServletRequest();
		HttpServletResponse response = new MockHttpServletResponse();

		modifyModel.setBankCode("C10641");
		modifyModel.setBankProvince("500000");
		modifyModel.setBankCity("141000");
		modifyModel.setBankAccount("sdfghjkl");
		modifyModel.setBankName("奥地利中央合作银行");
		String contractNo = "云信信2016-78-DK(ZQ2016042522479)";
		modifyModel.setContractNo(contractNo);
		String message = modifyApiController.modifyRepaymentInformation(modifyModel, request, response);
		Assert.assertTrue(message.contains("请求唯一标识［requestNo］，不能为空"));

	}

	@Test
	@Sql("classpath:test/yunxin/api/testModifyRepaymentInformationNormal.sql")
	@Ignore("整体跑的时候报错，单独跑的时候ok，现在不明确什么问题导致")
	public void test_modifyRepaymentInformation() {
		RepaymentInformationModifyModel modifyModel = new RepaymentInformationModifyModel();
		HttpServletRequest request = new MockHttpServletRequest();
		HttpServletResponse response = new MockHttpServletResponse();

		modifyModel.setBankCode("C10641");
		modifyModel.setBankProvince("500000");
		modifyModel.setBankCity("141000");
		modifyModel.setBankAccount("sdfghjkl");
		modifyModel.setBankName("中国建设银行");
		String contractNo = "云信信2016-78-DK(ZQ2016042522479)";
		modifyModel.setContractNo(contractNo);
		modifyModel.setRequestNo("requestNo111222");
		String message = modifyApiController.modifyRepaymentInformation( modifyModel, request, response);
		Assert.assertTrue(message.contains("成功"));
	}
	@Test
	@Sql("classpath:test/yunxin/api/testModifyRepaymentInformationWithFinancialContractNoNormal.sql")
	public void test_modifyRepaymentInformationWithFinancialContractNoNormal() {
		RepaymentInformationModifyModel modifyModel = new RepaymentInformationModifyModel();
		HttpServletRequest request = new MockHttpServletRequest();
		HttpServletResponse response = new MockHttpServletResponse();

		modifyModel.setBankCode("C10641");
		modifyModel.setBankProvince("500000");
		modifyModel.setBankCity("141000");
		modifyModel.setBankAccount("sdfghjkl");
		modifyModel.setBankName("中国建设银行");
		String contractNo = "云信信2016-78-DK(ZQ2016042522479)";
		modifyModel.setContractNo(contractNo);
		modifyModel.setRequestNo("requestNo111222");
		String message = modifyApiController.modifyRepaymentInformation( modifyModel, request, response);
		Assert.assertTrue(message.contains("成功"));
	}

	@Test
	@Sql("classpath:test/yunxin/api/testModifyRepaymentInformationWithFinancialContractNoUnvalid.sql")
	public void test_modifyRepaymentInformationWithFinancialContractNoNotExist() {
		RepaymentInformationModifyModel modifyModel = new RepaymentInformationModifyModel();
		HttpServletRequest request = new MockHttpServletRequest();
		HttpServletResponse response = new MockHttpServletResponse();

		modifyModel.setBankCode("C10641");
		modifyModel.setBankProvince("500000");
		modifyModel.setBankCity("141000");
		modifyModel.setBankAccount("sdfghjkl");
		modifyModel.setBankName("中国建设银行");
		String contractNo = "云信信2016-78-DK(ZQ2016042522479)";
		modifyModel.setContractNo(contractNo);
		modifyModel.setRequestNo("requestNo111222");
		String message = modifyApiController.modifyRepaymentInformation( modifyModel, request, response);
		Assert.assertTrue(message.contains("信托合同不存在"));
	}

	@Test
	@Sql("classpath:test/yunxin/api/testModifyRepaymentInformationWithFinancialContractNoUnvalid.sql")
	public void test_modifyRepaymentInformationWithFinancialContractNoNotIncludeContract() {
		RepaymentInformationModifyModel modifyModel = new RepaymentInformationModifyModel();
		HttpServletRequest request = new MockHttpServletRequest();
		HttpServletResponse response = new MockHttpServletResponse();

		modifyModel.setBankCode("C10641");
		modifyModel.setBankProvince("500000");
		modifyModel.setBankCity("141000");
		modifyModel.setBankAccount("sdfghjkl");
		modifyModel.setBankName("中国建设银行");
		String contractNo = "云信信2016-78-DK(ZQ2016042522479)";
		modifyModel.setContractNo(contractNo);
		modifyModel.setRequestNo("requestNo111222");
		String message = modifyApiController.modifyRepaymentInformation( modifyModel, request, response);
		Assert.assertTrue(message.contains("信托计划与贷款合同不匹配"));
	}
	@Test
	@Sql("classpath:test/yunxin/prepayment/test_prepayment_localProcessingRepaymentPlanExisted.sql")
	public void test_prepayment() {
		PrepaymentModifyModel model = new PrepaymentModifyModel();
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();

		String uniqueId = "";
		String contractNo = "test_contract_no";
		String requestNo = "testRequestNo";
		String assetRecycleDate =DateUtils.format(DateUtils.getToday());
		String assetInitialValue = "30000.00";
		String assetPrincipalValue = "30000.00";
		int type = 0;
		int payWay = 0;
		int hasDeducted = 0;

		model.setUniqueId(uniqueId);
		model.setContractNo(contractNo);
		model.setRequestNo(requestNo);
		model.setAssetRecycleDate(assetRecycleDate);
		model.setAssetInitialValue(assetInitialValue);
		model.setType(type);
		model.setPayWay(payWay);
		model.setAssetPrincipal(assetPrincipalValue);
		model.setHasDeducted(hasDeducted);
		String message = modifyApiController.prepayment(model, request, response);

		List<AssetSet> assetSets = repaymentPlanService.loadAll(AssetSet.class);
		AssetSet prepaymentPlan = null;
		for(AssetSet assetSet : assetSets) {
			if(assetSet.getRepaymentPlanType() == RepaymentPlanType.PREPAYMENT && assetSet.isCanBeRollbacked()) {
				prepaymentPlan = assetSet;
			}
		}
		Assert.assertTrue(message.contains(prepaymentPlan.getSingleLoanContractNo()));
		Assert.assertTrue(message.contains("成功"));
	}

	@Test
	@Sql("classpath:test/yunxin/prepayment/prepayment.sql")
	public void test_prepayment_errorMsg() {
		PrepaymentModifyModel model = new PrepaymentModifyModel();
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();

//		String requestNo = "ttteeesssttt";
//		String uniqueId = "";
//		String contractNo = "test-contract-no-1";
//		//String requestNo = "testRequestNo";
//		String assetRecycleDate = "2017-11-30";
//		String assetInitialValue = "10000.00";
//		int type = 0;

		model.setUniqueId("test_unique_id_333");
		model.setAssetRecycleDate("2017-3-16");
		model.setRequestNo("test_request_no");
		model.setAssetInitialValue("37200.00");
		model.setAssetPrincipal("30000.00");
		model.setAssetInterest("7000.00");
		model.setOtherCharge("200.00");
		model.setType(0);
		//model.setServiceCharge("100.00");
		//model.setMaintenanceCharge("100.00");
		model.setHasDeducted(-1);
		model.setPayWay(0);
//		model.setRequestNo(requestNo);
//		model.setUniqueId(uniqueId);
//		model.setContractNo(contractNo);
//		//model.setRequestNo(requestNo);
//		model.setAssetRecycleDate(assetRecycleDate);
//		model.setAssetInitialValue(assetInitialValue);
//		model.setType(type);
		String message = modifyApiController.prepayment(model, request, response);
		System.out.println(message);
		Result result = JsonUtils.parse(message,Result.class);
		//Assert.assertEquals(ApiResponseCode.INVALID_PARAMS, Integer.parseInt(result.getCode()));
	}

	@Test
	@Sql("classpath:test/yunxin/api/modify_repaymentPlan.sql")
	public void test_modify_repaymentPlan() {
		RepaymentPlanModifyRequestDataModel repaymentPlanModifyRequestDataModel1 = new RepaymentPlanModifyRequestDataModel();
		RepaymentPlanModifyRequestDataModel repaymentPlanModifyRequestDataModel2 = new RepaymentPlanModifyRequestDataModel();
		RepaymentPlanModifyRequestDataModel repaymentPlanModifyRequestDataModel3 = new RepaymentPlanModifyRequestDataModel();

		repaymentPlanModifyRequestDataModel1.setAssetRecycleDate(DateUtils.format(DateUtils.addDays(DateUtils.getToday(), 5)));
		repaymentPlanModifyRequestDataModel2.setAssetRecycleDate(DateUtils.format(DateUtils.addDays(DateUtils.getToday(), 15)));
		repaymentPlanModifyRequestDataModel3.setAssetRecycleDate(DateUtils.format(DateUtils.addDays(DateUtils.getToday(), 25)));

		repaymentPlanModifyRequestDataModel1.setAssetPrincipal(new BigDecimal("35000.00"));
		repaymentPlanModifyRequestDataModel2.setAssetPrincipal(new BigDecimal("25000.00"));
		repaymentPlanModifyRequestDataModel3.setAssetPrincipal(new BigDecimal("20000.00"));

		repaymentPlanModifyRequestDataModel1.setAssetType(0);
		repaymentPlanModifyRequestDataModel2.setAssetType(0);
		repaymentPlanModifyRequestDataModel3.setAssetType(0);

		repaymentPlanModifyRequestDataModel1.setRepayScheduleNo("repayScheduleNo1");
		repaymentPlanModifyRequestDataModel2.setRepayScheduleNo("repayScheduleNo2");
		repaymentPlanModifyRequestDataModel3.setRepayScheduleNo("repayScheduleNo3");

		List<RepaymentPlanModifyRequestDataModel> requestDataModels = new ArrayList<>();
		requestDataModels.add(repaymentPlanModifyRequestDataModel1);
		requestDataModels.add(repaymentPlanModifyRequestDataModel2);
		requestDataModels.add(repaymentPlanModifyRequestDataModel3);
		String requestData = JsonUtils.toJsonString(requestDataModels);

		RepaymentPlanModifyModel modifyModel = new RepaymentPlanModifyModel();
		modifyModel.setFn("FNTEST");
		modifyModel.setRequestNo("AAA");
		modifyModel.setUniqueId("e568793f-a44c-4362-9e78-0ce433131f3e");
		//modifyModel.setContractNo("云信信2016-241-DK(428522112675736881)");
		modifyModel.setRequestReason("1");
		modifyModel.setRequestData(requestData);

		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		try {
			String message = modifyApiController.modifyRepaymentPlan(modifyModel, request, response);
			System.out.println("================================"+message+"===============================");
			//assetSetApiHandler.modifyRepaymentPlan(contract, oldActiveVersionNo, modifyModel, "192.168.1.1");
		} catch (Exception e) {
			e.printStackTrace();
			//Assert.fail();
		}
	}
}
