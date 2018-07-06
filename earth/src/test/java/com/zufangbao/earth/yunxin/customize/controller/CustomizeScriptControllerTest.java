package com.zufangbao.earth.yunxin.customize.controller;

import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.suidifu.matryoshka.productCategory.ProductCategory;
import com.suidifu.matryoshka.productCategory.ProductCategoryStatus;
import com.suidifu.matryoshka.service.ProductCategoryService;
import com.suidifu.xcode.exception.XcodeException;
import com.suidifu.xcode.pojo.SourceRepository;
import com.suidifu.xcode.service.SourceRepositoryPersistenceService;
import com.zufangbao.sun.entity.security.Principal;
import com.zufangbao.earth.model.CustomizeScriptModel;
import com.zufangbao.earth.web.controller.customizeScript.CustomizeScriptController;
import com.zufangbao.earth.web.controller.customizeScript.CustomizeScriptControllerSpec;
import com.zufangbao.gluon.spec.global.GlobalCodeSpec;
import com.zufangbao.sun.utils.JsonUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/local/applicationContext-*.xml",
		"classpath:/local/DispatcherServlet.xml" })
@Transactional
@Rollback(true)
public class CustomizeScriptControllerTest extends AbstractTransactionalJUnit4SpringContextTests {

	@Autowired
	private CustomizeScriptController customizeScriptController;

	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	@Autowired
	private ProductCategoryService productCategoryService;

	@Autowired
	@Qualifier("sourceRepositoryDBService")
	private SourceRepositoryPersistenceService sourceRepositoryDBService;

	@Test
	@Sql("classpath:test/yunxin/customize/controller/test_submitPreScript_null_param.sql")
	public void test_submitPreScript_null_param() {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		String preProcessInterfaceUrl = "productLv2Code/productLv1Code/productLv3Code";
		stringRedisTemplate.delete("sr:" + preProcessInterfaceUrl);
		Principal principal = new Principal();
		principal.setName("sys");
		CustomizeScriptModel model = new CustomizeScriptModel();
		model.setProductLv1Code("productLv1Code");
		model.setProductLv1Name("productLv1Name");
		model.setProductLv2Code("productLv2Code");
		model.setProductLv2Name("productLv2Name");
		model.setProductLv3Code("productLv3Code");
		model.setProductLv3Name("productLv3Name");
		model.setCompileImport("");
		model.setDelayTaskConfigUuid(null);
		String script = "public boolean evaluate(SandboxDataSetHandler sandboxDataSetHandler, Map<String, String> preRequest, Map<String, String> postRequest, Log logger) {\r\n		logger.info(\"SXHDeferredPaymentServices has been started\");\r\n		String uniqueId = (String) preRequest.getOrDefault(\"uniqueId\",\"\");\r\n		String contractNo = (String)preRequest.getOrDefault(\"contractNo\",\"\");\r\n		if (StringUtils.isEmpty(uniqueId) && StringUtils.isEmpty(contractNo)) {\r\n			logger.error(\"SXHDeferredPaymentServices:uniqueId和contractNo需要赋值一个\");\r\n			postRequest.put(\"errorMsg\", \"uniqueId和contractNo需要赋值一个\");\r\n			return false;\r\n		}\r\n		String requestData = (String)preRequest.getOrDefault(\"requestData\",\"\");\r\n		logger.info(\"SXHDeferredPaymentServices get sandboxDataSet\");\r\n		SandboxDataSet sandboxDataSet = sandboxDataSetHandler.get_sandbox_for_apply_repurchase_by_contract_uniqueId_contractNo(uniqueId, contractNo);\r\n		if (null == sandboxDataSet) {\r\n			logger.error(\"SXHDeferredPaymentServices:sandboxDataSet is null\");\r\n			postRequest.put(\"errorMsg\", \"sandboxDataSet is null\");\r\n			return false;\r\n		}\r\n		FinancialContractSnapshot financialContractSnapshot = sandboxDataSet.getFinancialContractSnapshot();\r\n		if (null == financialContractSnapshot) {\r\n			logger.error(\"SXHDeferredPaymentServices:financialContractSnapshot is null\");\r\n			postRequest.put(\"errorMsg\", \"financialContractSnapshot is null\");\r\n			return false;\r\n		}\r\n		if(!financialContractSnapshot.isAllowFreewheelingRepayment()){\r\n			logger.error(\"SXHDeferredPaymentServices:信托合同[\"+financialContractSnapshot.getContractNo()+\"]不支持随心还\");\r\n			postRequest.put(\"errorMsg\", \"信托合同不支持随心还\");\r\n			return false;\r\n		}\r\n		List<PaymentPlanSnapshot> assetSetSnapshotList = sandboxDataSet.getPaymentPlanSnapshotList();\r\n		List<RepaymentPlanModifyRequestDataModel> models = JsonUtils.parseArray(requestData, RepaymentPlanModifyRequestDataModel.class);\r\n		try{\r\n			if(CollectionUtils.isEmpty(assetSetSnapshotList)){\r\n				logger.error(\"SXHDeferredPaymentServices:assetSetSnapshotList is Empty\");\r\n				postRequest.put(\"errorMsg\", \"assetSetSnapshotList is Empty\");\r\n				return false;\r\n			}\r\n			if(CollectionUtils.isEmpty(models) || models.size() != assetSetSnapshotList.size()){\r\n				logger.error(\"SXHDeferredPaymentServices:还款计划数不匹配\");\r\n				postRequest.put(\"errorMsg\", \"还款计划数不匹配\");\r\n				return false;\r\n			}\r\n			int globalMonDelta = 0;\r\n			for(int i = 0, len = models.size(); i < len; i ++){\r\n				PaymentPlanSnapshot assetSetSnapshot = (PaymentPlanSnapshot)assetSetSnapshotList.get(i);\r\n				RepaymentPlanModifyRequestDataModel modifyRequestDataModel = (RepaymentPlanModifyRequestDataModel)models\r\n						.get(i);\r\n				String asset_interest_principal_fingerPrint = modifyRequestDataModel\r\n						.asset_interest_principal_fingerPrint();\r\n				if (!assetSetSnapshot\r\n						.check_asset_interest_principal_fingerPrint(asset_interest_principal_fingerPrint)) {\r\n					logger.error(\"SXHDeferredPaymentServices:还款计划基础费用不匹配\");\r\n					postRequest.put(\"errorMsg\", \"还款计划基础费用不匹配\");\r\n					return false;\r\n				}\r\n				if (assetSetSnapshot.is_clear_repayment_plan()) {\r\n					logger.error(\"SXHDeferredPaymentServices:还款计划[\" + assetSetSnapshot.getSingleLoanContractNo() + \"]已结清不可变更\");\r\n					postRequest.put(\"errorMsg\", \"还款计划已结清\");\r\n					return false;\r\n				}\r\n				if (assetSetSnapshot.is_overdue_repayment_plan()) {\r\n					logger.error(\"SXHDeferredPaymentServices:还款计划[\" + assetSetSnapshot.getSingleLoanContractNo() + \"]已逾期不可变更\");\r\n					postRequest.put(\"errorMsg\", \"还款计划已逾期\");\r\n					return false;\r\n				}\r\n				OnAccountStatus onAccountStatus = assetSetSnapshot.getOnAccountStatus();\r\n				if (OnAccountStatus.ON_ACCOUNT != onAccountStatus) {\r\n					logger.error(\"SXHDeferredPaymentServices:还款计划[\" + assetSetSnapshot.getSingleLoanContractNo() + \"]部分结算不可变更\");\r\n					postRequest.put(\"errorMsg\", \"还款计划部分结算\");\r\n					return false;\r\n				}\r\n				if (assetSetSnapshot.is_today_repayment_plan()) {\r\n					logger.error(\"SXHDeferredPaymentServices:还款计划[\" + assetSetSnapshot.getSingleLoanContractNo() + \"]还款当日不允许变更\");\r\n					postRequest.put(\"errorMsg\", \"还款当日不允许变更\");\r\n					return false;\r\n				}\r\n				PaymentPlanExtraChargeSnapshot assetSetExtraChargeSnapshot = assetSetSnapshot\r\n						.getAssetSetExtraChargeSnapshot();\r\n				if (null == assetSetExtraChargeSnapshot) {\r\n					logger.error(\"SXHDeferredPaymentServices:assetSetExtraChargeSnapshot is null\");\r\n					postRequest.put(\"errorMsg\", \"assetSetExtraChargeSnapshot is null\");\r\n					return false;\r\n				}\r\n\r\n				String assetExtraFeeFingerPrint = modifyRequestDataModel.assetExtraFeeFingerPrint();\r\n				if (!assetSetExtraChargeSnapshot.check_assetExtraFeeFingerPrint(assetExtraFeeFingerPrint)) {\r\n					logger.error(\"SXHDeferredPaymentServices:还款计划其他费用不匹配\");\r\n					postRequest.put(\"errorMsg\", \"还款计划其他费用不匹配\");\r\n					return false;\r\n				}\r\n				// 延期还款仅仅变更月份\r\n				Date assetRecycleDate = assetSetSnapshot.getAssetRecycleDate();\r\n				Date model_date = modifyRequestDataModel.getDate();\r\n				int snapshot_day = DateUtils.day(assetRecycleDate);\r\n				int model_day = DateUtils.day(model_date);\r\n				if (snapshot_day != model_day) {\r\n					logger.error(\"SXHDeferredPaymentServices:延期还款仅可变更月份\");\r\n					postRequest.put(\"errorMsg\", \"延期还款仅可变更月份\");\r\n					return false;\r\n				}\r\n				\r\n				// 还款日为29/30/31日时不得延期\r\n				if (Arrays.asList(29, 30, 31).contains(model_day)) {\r\n					logger.error(\"SXHDeferredPaymentServices:还款日为29/30/31日时不得延期\");\r\n					postRequest.put(\"errorMsg\", \"还款日为29/30/31日时不得延期\");\r\n					return false;\r\n				}\r\n\r\n				Date addOneMonth = DateUtils.addMonths(assetRecycleDate, 1);\r\n				boolean one_month_late = DateUtils.isSameMonth(addOneMonth, model_date);\r\n				Date addTwoMonths = DateUtils.addMonths(assetRecycleDate, 2);\r\n				boolean two_months_late = DateUtils.isSameMonth(addTwoMonths, model_date);\r\n\r\n				if (!one_month_late && !two_months_late) {\r\n					logger.error(\"SXHDeferredPaymentServices:延期还款月份变更在两个月内\");\r\n					postRequest.put(\"errorMsg\", \"延期还款月份变更在两个月内\");\r\n					return false;\r\n				}\r\n				int monDelta = one_month_late ? 1 : 2;\r\n				// 月份变化\r\n				if (globalMonDelta == 0) {\r\n					globalMonDelta = monDelta;\r\n					continue;\r\n				}\r\n				// 月份变化量相同\r\n				if (globalMonDelta != monDelta) {\r\n					logger.error(\"SXHDeferredPaymentServices:月份变化量必须相同\");\r\n					postRequest.put(\"errorMsg\", \"月份变化量必须相同\");\r\n					return false;\r\n				}\r\n			}\r\n			postRequest.putAll(preRequest);\r\n			postRequest.put(\"fn\",\"200001\");\r\n			postRequest.put(\"requestReason\", RepaymentPlanModifyReason.REASON_4.getOrdinal() + \"\");\r\n			logger.info(\"SXHDeferredPaymentServices is end\");\r\n			return true;\r\n		} catch (Exception e) {\r\n			e.printStackTrace();\r\n			logger.error(\"SXHDeferredPaymentServices exception occurred\");\r\n			return false;\r\n		}\r\n	}";

		String result1 = customizeScriptController.submitPreScript(request, response, model, script, principal);
		if (productCategoryService.check_by_pre_url(preProcessInterfaceUrl, ProductCategoryStatus.VALID)) {
			Assert.fail();
		}
		System.out.println(result1);

		model.setPreProcessInterfaceUrl(preProcessInterfaceUrl);
		String result2 = customizeScriptController.submitPreScript(request, response, model, "", principal);

		if (productCategoryService.check_by_pre_url(preProcessInterfaceUrl, ProductCategoryStatus.VALID)) {
			Assert.fail();
		}
		System.out.println(result2);

	}

	@Test
	@Sql("classpath:test/yunxin/customize/controller/test_submitPreScript_create.sql")
	public void test_submitPreScript_create() {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		String preProcessInterfaceUrl = "productLv2Code/productLv1Code/productLv3Code";
		stringRedisTemplate.delete("sr:" + preProcessInterfaceUrl);
		Principal principal = new Principal();
		principal.setName("sys");
		CustomizeScriptModel model = new CustomizeScriptModel();
		model.setProductLv1Code("productLv1Code");
		model.setProductLv1Name("productLv1Name");
		model.setProductLv2Code("productLv2Code");
		model.setProductLv2Name("productLv2Name");
		model.setProductLv3Code("productLv3Code");
		model.setProductLv3Name("productLv3Name");
		model.setCompileImport("");
		model.setDelayTaskConfigUuid(null);
		model.setPreProcessInterfaceUrl(preProcessInterfaceUrl);
		String script = "public boolean evaluate(SandboxDataSetHandler sandboxDataSetHandler, Map<String, String> preRequest, Map<String, String> postRequest, Log logger) {\r\n		logger.info(\"SXHDeferredPaymentServices has been started\");\r\n		String uniqueId = (String) preRequest.getOrDefault(\"uniqueId\",\"\");\r\n		String contractNo = (String)preRequest.getOrDefault(\"contractNo\",\"\");\r\n		if (StringUtils.isEmpty(uniqueId) && StringUtils.isEmpty(contractNo)) {\r\n			logger.error(\"SXHDeferredPaymentServices:uniqueId和contractNo需要赋值一个\");\r\n			postRequest.put(\"errorMsg\", \"uniqueId和contractNo需要赋值一个\");\r\n			return false;\r\n		}\r\n		String requestData = (String)preRequest.getOrDefault(\"requestData\",\"\");\r\n		logger.info(\"SXHDeferredPaymentServices get sandboxDataSet\");\r\n		SandboxDataSet sandboxDataSet = sandboxDataSetHandler.get_sandbox_for_apply_repurchase_by_contract_uniqueId_contractNo(uniqueId, contractNo);\r\n		if (null == sandboxDataSet) {\r\n			logger.error(\"SXHDeferredPaymentServices:sandboxDataSet is null\");\r\n			postRequest.put(\"errorMsg\", \"sandboxDataSet is null\");\r\n			return false;\r\n		}\r\n		FinancialContractSnapshot financialContractSnapshot = sandboxDataSet.getFinancialContractSnapshot();\r\n		if (null == financialContractSnapshot) {\r\n			logger.error(\"SXHDeferredPaymentServices:financialContractSnapshot is null\");\r\n			postRequest.put(\"errorMsg\", \"financialContractSnapshot is null\");\r\n			return false;\r\n		}\r\n		if(!financialContractSnapshot.isAllowFreewheelingRepayment()){\r\n			logger.error(\"SXHDeferredPaymentServices:信托合同[\"+financialContractSnapshot.getContractNo()+\"]不支持随心还\");\r\n			postRequest.put(\"errorMsg\", \"信托合同不支持随心还\");\r\n			return false;\r\n		}\r\n		List<PaymentPlanSnapshot> assetSetSnapshotList = sandboxDataSet.getPaymentPlanSnapshotList();\r\n		List<RepaymentPlanModifyRequestDataModel> models = JsonUtils.parseArray(requestData, RepaymentPlanModifyRequestDataModel.class);\r\n		try{\r\n			if(CollectionUtils.isEmpty(assetSetSnapshotList)){\r\n				logger.error(\"SXHDeferredPaymentServices:assetSetSnapshotList is Empty\");\r\n				postRequest.put(\"errorMsg\", \"assetSetSnapshotList is Empty\");\r\n				return false;\r\n			}\r\n			if(CollectionUtils.isEmpty(models) || models.size() != assetSetSnapshotList.size()){\r\n				logger.error(\"SXHDeferredPaymentServices:还款计划数不匹配\");\r\n				postRequest.put(\"errorMsg\", \"还款计划数不匹配\");\r\n				return false;\r\n			}\r\n			int globalMonDelta = 0;\r\n			for(int i = 0, len = models.size(); i < len; i ++){\r\n				PaymentPlanSnapshot assetSetSnapshot = (PaymentPlanSnapshot)assetSetSnapshotList.get(i);\r\n				RepaymentPlanModifyRequestDataModel modifyRequestDataModel = (RepaymentPlanModifyRequestDataModel)models\r\n						.get(i);\r\n				String asset_interest_principal_fingerPrint = modifyRequestDataModel\r\n						.asset_interest_principal_fingerPrint();\r\n				if (!assetSetSnapshot\r\n						.check_asset_interest_principal_fingerPrint(asset_interest_principal_fingerPrint)) {\r\n					logger.error(\"SXHDeferredPaymentServices:还款计划基础费用不匹配\");\r\n					postRequest.put(\"errorMsg\", \"还款计划基础费用不匹配\");\r\n					return false;\r\n				}\r\n				if (assetSetSnapshot.is_clear_repayment_plan()) {\r\n					logger.error(\"SXHDeferredPaymentServices:还款计划[\" + assetSetSnapshot.getSingleLoanContractNo() + \"]已结清不可变更\");\r\n					postRequest.put(\"errorMsg\", \"还款计划已结清\");\r\n					return false;\r\n				}\r\n				if (assetSetSnapshot.is_overdue_repayment_plan()) {\r\n					logger.error(\"SXHDeferredPaymentServices:还款计划[\" + assetSetSnapshot.getSingleLoanContractNo() + \"]已逾期不可变更\");\r\n					postRequest.put(\"errorMsg\", \"还款计划已逾期\");\r\n					return false;\r\n				}\r\n				OnAccountStatus onAccountStatus = assetSetSnapshot.getOnAccountStatus();\r\n				if (OnAccountStatus.ON_ACCOUNT != onAccountStatus) {\r\n					logger.error(\"SXHDeferredPaymentServices:还款计划[\" + assetSetSnapshot.getSingleLoanContractNo() + \"]部分结算不可变更\");\r\n					postRequest.put(\"errorMsg\", \"还款计划部分结算\");\r\n					return false;\r\n				}\r\n				if (assetSetSnapshot.is_today_repayment_plan()) {\r\n					logger.error(\"SXHDeferredPaymentServices:还款计划[\" + assetSetSnapshot.getSingleLoanContractNo() + \"]还款当日不允许变更\");\r\n					postRequest.put(\"errorMsg\", \"还款当日不允许变更\");\r\n					return false;\r\n				}\r\n				PaymentPlanExtraChargeSnapshot assetSetExtraChargeSnapshot = assetSetSnapshot\r\n						.getAssetSetExtraChargeSnapshot();\r\n				if (null == assetSetExtraChargeSnapshot) {\r\n					logger.error(\"SXHDeferredPaymentServices:assetSetExtraChargeSnapshot is null\");\r\n					postRequest.put(\"errorMsg\", \"assetSetExtraChargeSnapshot is null\");\r\n					return false;\r\n				}\r\n\r\n				String assetExtraFeeFingerPrint = modifyRequestDataModel.assetExtraFeeFingerPrint();\r\n				if (!assetSetExtraChargeSnapshot.check_assetExtraFeeFingerPrint(assetExtraFeeFingerPrint)) {\r\n					logger.error(\"SXHDeferredPaymentServices:还款计划其他费用不匹配\");\r\n					postRequest.put(\"errorMsg\", \"还款计划其他费用不匹配\");\r\n					return false;\r\n				}\r\n				// 延期还款仅仅变更月份\r\n				Date assetRecycleDate = assetSetSnapshot.getAssetRecycleDate();\r\n				Date model_date = modifyRequestDataModel.getDate();\r\n				int snapshot_day = DateUtils.day(assetRecycleDate);\r\n				int model_day = DateUtils.day(model_date);\r\n				if (snapshot_day != model_day) {\r\n					logger.error(\"SXHDeferredPaymentServices:延期还款仅可变更月份\");\r\n					postRequest.put(\"errorMsg\", \"延期还款仅可变更月份\");\r\n					return false;\r\n				}\r\n				\r\n				// 还款日为29/30/31日时不得延期\r\n				if (Arrays.asList(29, 30, 31).contains(model_day)) {\r\n					logger.error(\"SXHDeferredPaymentServices:还款日为29/30/31日时不得延期\");\r\n					postRequest.put(\"errorMsg\", \"还款日为29/30/31日时不得延期\");\r\n					return false;\r\n				}\r\n\r\n				Date addOneMonth = DateUtils.addMonths(assetRecycleDate, 1);\r\n				boolean one_month_late = DateUtils.isSameMonth(addOneMonth, model_date);\r\n				Date addTwoMonths = DateUtils.addMonths(assetRecycleDate, 2);\r\n				boolean two_months_late = DateUtils.isSameMonth(addTwoMonths, model_date);\r\n\r\n				if (!one_month_late && !two_months_late) {\r\n					logger.error(\"SXHDeferredPaymentServices:延期还款月份变更在两个月内\");\r\n					postRequest.put(\"errorMsg\", \"延期还款月份变更在两个月内\");\r\n					return false;\r\n				}\r\n				int monDelta = one_month_late ? 1 : 2;\r\n				// 月份变化\r\n				if (globalMonDelta == 0) {\r\n					globalMonDelta = monDelta;\r\n					continue;\r\n				}\r\n				// 月份变化量相同\r\n				if (globalMonDelta != monDelta) {\r\n					logger.error(\"SXHDeferredPaymentServices:月份变化量必须相同\");\r\n					postRequest.put(\"errorMsg\", \"月份变化量必须相同\");\r\n					return false;\r\n				}\r\n			}\r\n			postRequest.putAll(preRequest);\r\n			postRequest.put(\"fn\",\"200001\");\r\n			postRequest.put(\"requestReason\", RepaymentPlanModifyReason.REASON_4.getOrdinal() + \"\");\r\n			logger.info(\"SXHDeferredPaymentServices is end\");\r\n			return true;\r\n		} catch (Exception e) {\r\n			e.printStackTrace();\r\n			logger.error(\"SXHDeferredPaymentServices exception occurred\");\r\n			return false;\r\n		}\r\n	}";

		String result = customizeScriptController.submitPreScript(request, response, model, script, principal);

		if (!productCategoryService.check_by_pre_url(preProcessInterfaceUrl, ProductCategoryStatus.VALID)) {
			Assert.fail();
		}
		System.out.println(result);
	}

	@Test
	@Sql("classpath:test/yunxin/customize/controller/test_submitPreScript_update_unchange.sql")
	public void test_submitPreScript_update_unchange() {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		String preProcessInterfaceUrl = "modify-repaymentPlan/zhongan/SXH-QXSXH";
		String compileImport = "java.math.*";
		String script = "public boolean evaluate(SandboxDataSetHandler sandboxDataSetHandler, Map<String, String> preRequest, Map<String, String> postRequest, Log logger) {\r\n		logger.info(\"SXHCancelServices has been started\");\r\n		String uniqueId = (String) preRequest.getOrDefault(\"uniqueId\",\"\");\r\n		String contractNo = (String)preRequest.getOrDefault(\"contractNo\",\"\");\r\n		if (StringUtils.isEmpty(uniqueId) && StringUtils.isEmpty(contractNo)) {\r\n			logger.error(\"SXHCancelServices:uniqueId和contractNo需要赋值一个\");\r\n			postRequest.put(\"errorMsg\", \"uniqueId和contractNo需要赋值一个\");\r\n			return false;\r\n		}\r\n		String requestData = (String)preRequest.getOrDefault(\"requestData\",\"\");\r\n		List<RepaymentPlanModifyRequestDataModel> models = JsonUtils.parseArray(requestData, RepaymentPlanModifyRequestDataModel.class);\r\n		logger.info(\"SXHCancelServices get sandboxDataSet\");\r\n		SandboxDataSet sandboxDataSet = sandboxDataSetHandler.get_sandbox_for_apply_repurchase_by_contract_uniqueId_contractNo(uniqueId, contractNo);\r\n		if (null == sandboxDataSet) {\r\n			logger.error(\"SXHCancelServices:sandboxDataSet is null\");\r\n			postRequest.put(\"errorMsg\", \"sandboxDataSet is null\");\r\n			return false;\r\n		}\r\n		if (null == sandboxDataSet.getFinancialContractSnapshot()) {\r\n			logger.error(\"SXHCancelServices:financialContractSnapshot is null\");\r\n			postRequest.put(\"errorMsg\", \"financialContractSnapshot is null\");\r\n			return false;\r\n		}\r\n		if(!sandboxDataSet.getFinancialContractSnapshot().isAllowFreewheelingRepayment()){\r\n			logger.error(\"SXHCancelServices:信托合同[\"+sandboxDataSet.getFinancialContractSnapshot().getContractNo()+\"]不支持随心还\");\r\n			postRequest.put(\"errorMsg\", \"信托合同不支持随心还\");\r\n			return false;\r\n		}\r\n		List<PaymentPlanSnapshot> assetSetSnapshotList = sandboxDataSet.getPaymentPlanSnapshotList();\r\n		if (CollectionUtils.isEmpty(models) || models.size() != assetSetSnapshotList.size()) {\r\n			logger.error(\"SXHCancelServices:还款计划数不匹配\");\r\n			postRequest.put(\"errorMsg\", \"还款计划数不匹配\");\r\n			return false;\r\n		}\r\n		try{\r\n			for (int i = 0; i < assetSetSnapshotList.size(); i++) {\r\n				PaymentPlanSnapshot assetSetSnapshot = (PaymentPlanSnapshot) assetSetSnapshotList.get(i);\r\n				RepaymentPlanModifyRequestDataModel modifyRequestDataModel = (RepaymentPlanModifyRequestDataModel) models\r\n						.get(i);\r\n				String asset_interest_principal_fingerPrint = modifyRequestDataModel.assetFingerPrint();\r\n				if (!assetSetSnapshot.checkAssetFingerPrint(asset_interest_principal_fingerPrint)) {\r\n					logger.error(\"SXHCancelServices:还款计划基础费用不匹配\");\r\n					postRequest.put(\"errorMsg\", \"还款计划基础费用不匹配\");\r\n					return false;\r\n				}\r\n					//还款成功和逾期\r\n				if (assetSetSnapshot.is_clear_repayment_plan()) {\r\n					logger.error(\"SXHCancelServices:还款计划[\" + assetSetSnapshot.getSingleLoanContractNo() + \"]已结清不可变更\");\r\n					postRequest.put(\"errorMsg\", \"还款计划已结清\");\r\n					return false;\r\n				}\r\n				if (assetSetSnapshot.is_overdue_repayment_plan()) {\r\n					logger.error(\"SXHCancelServices:还款计划[\" + assetSetSnapshot.getSingleLoanContractNo() + \"]已逾期不可变更\");\r\n					postRequest.put(\"errorMsg\", \"还款计划已逾期\");\r\n					return false;\r\n				}\r\n				OnAccountStatus onAccountStatus = assetSetSnapshot.getOnAccountStatus();\r\n				if (OnAccountStatus.ON_ACCOUNT != onAccountStatus) {\r\n					logger.error(\"SXHCancelServices:还款计划[\" + assetSetSnapshot.getSingleLoanContractNo() + \"]部分结算不可变更\");\r\n					postRequest.put(\"errorMsg\", \"还款计划部分结算\");\r\n					return false;\r\n				}\r\n				if (assetSetSnapshot.is_today_repayment_plan()) {\r\n					logger.error(\"SXHCancelServices:还款计划[\" + assetSetSnapshot.getSingleLoanContractNo() + \"]还款当日不允许变更\");\r\n					postRequest.put(\"errorMsg\", \"还款当日不允许变更\");\r\n					return false;\r\n				}\r\n\r\n			}\r\n\r\n			postRequest.putAll(preRequest);\r\n			postRequest.put(\"fn\",\"200001\");\r\n			postRequest.put(\"requestReason\", RepaymentPlanModifyReason.REASON_7.getOrdinal() + \"\");\r\n			logger.info(\"SXHCancelServices is end\");\r\n			return true;\r\n		} catch (Exception e) {\r\n			e.printStackTrace();\r\n			logger.error(\"SXHCancelServices exception occurred\");\r\n			return false;\r\n		}\r\n	}";
		stringRedisTemplate.delete("sr:" + preProcessInterfaceUrl);
		Principal principal = new Principal();
		principal.setName("sys");
		CustomizeScriptModel model = new CustomizeScriptModel();
		model.setCompileImport(compileImport);
		model.setPreProcessInterfaceUrl(preProcessInterfaceUrl);

		String result = customizeScriptController.submitPreScript(request, response, model, script, principal);
		Map<String, Object> map = JsonUtils.parse(result);
		String code = (String) map.get("code");
		String message = (String) map.get("message");
		Assert.assertEquals(code, GlobalCodeSpec.CODE_FAILURE + "");
		Assert.assertEquals(message, "脚本未变更");
	}

	@Test
	@Sql("classpath:test/yunxin/customize/controller/test_submitPreScript_update_invalid_URL.sql")
	public void test_submitPreScript_update_invalid_URL() {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		String preProcessInterfaceUrl = "modify-repaymentPlan/zhongan/SXH-QXSXH";
		String compileImport = "java.math.*,import org.junit.*";
		String script = "public boolean evaluate(SandboxDataSetHandler sandboxDataSetHandler, Map<String, String> preRequest, Map<String, String> postRequest, Log logger) {\r\n		logger.info(\"SXHCancelServices has been started\");\r\n		String uniqueId = (String) preRequest.getOrDefault(\"uniqueId\",\"\");\r\n		String contractNo = (String)preRequest.getOrDefault(\"contractNo\",\"\");\r\n		if (StringUtils.isEmpty(uniqueId) && StringUtils.isEmpty(contractNo)) {\r\n			logger.error(\"SXHCancelServices:uniqueId和contractNo需要赋值一个\");\r\n			postRequest.put(\"errorMsg\", \"uniqueId和contractNo需要赋值一个\");\r\n			return false;\r\n		}\r\n		String requestData = (String)preRequest.getOrDefault(\"requestData\",\"\");\r\n		List<RepaymentPlanModifyRequestDataModel> models = JsonUtils.parseArray(requestData, RepaymentPlanModifyRequestDataModel.class);\r\n		logger.info(\"SXHCancelServices get sandboxDataSet\");\r\n		SandboxDataSet sandboxDataSet = sandboxDataSetHandler.get_sandbox_for_apply_repurchase_by_contract_uniqueId_contractNo(uniqueId, contractNo);\r\n		if (null == sandboxDataSet) {\r\n			logger.error(\"SXHCancelServices:sandboxDataSet is null\");\r\n			postRequest.put(\"errorMsg\", \"sandboxDataSet is null\");\r\n			return false;\r\n		}\r\n		if (null == sandboxDataSet.getFinancialContractSnapshot()) {\r\n			logger.error(\"SXHCancelServices:financialContractSnapshot is null\");\r\n			postRequest.put(\"errorMsg\", \"financialContractSnapshot is null\");\r\n			return false;\r\n		}\r\n		if(!sandboxDataSet.getFinancialContractSnapshot().isAllowFreewheelingRepayment()){\r\n			logger.error(\"SXHCancelServices:信托合同[\"+sandboxDataSet.getFinancialContractSnapshot().getContractNo()+\"]不支持随心还\");\r\n			postRequest.put(\"errorMsg\", \"信托合同不支持随心还\");\r\n			return false;\r\n		}\r\n		List<PaymentPlanSnapshot> assetSetSnapshotList = sandboxDataSet.getPaymentPlanSnapshotList();\r\n		if (CollectionUtils.isEmpty(models) || models.size() != assetSetSnapshotList.size()) {\r\n			logger.error(\"SXHCancelServices:还款计划数不匹配\");\r\n			postRequest.put(\"errorMsg\", \"还款计划数不匹配\");\r\n			return false;\r\n		}\r\n		try{\r\n			for (int i = 0; i < assetSetSnapshotList.size(); i++) {\r\n				PaymentPlanSnapshot assetSetSnapshot = (PaymentPlanSnapshot) assetSetSnapshotList.get(i);\r\n				RepaymentPlanModifyRequestDataModel modifyRequestDataModel = (RepaymentPlanModifyRequestDataModel) models\r\n						.get(i);\r\n				String asset_interest_principal_fingerPrint = modifyRequestDataModel.assetFingerPrint();\r\n				if (!assetSetSnapshot.checkAssetFingerPrint(asset_interest_principal_fingerPrint)) {\r\n					logger.error(\"SXHCancelServices:还款计划基础费用不匹配\");\r\n					postRequest.put(\"errorMsg\", \"还款计划基础费用不匹配\");\r\n					return false;\r\n				}\r\n					//还款成功和逾期\r\n				if (assetSetSnapshot.is_clear_repayment_plan()) {\r\n					logger.error(\"SXHCancelServices:还款计划[\" + assetSetSnapshot.getSingleLoanContractNo() + \"]已结清不可变更\");\r\n					postRequest.put(\"errorMsg\", \"还款计划已结清\");\r\n					return false;\r\n				}\r\n				if (assetSetSnapshot.is_overdue_repayment_plan()) {\r\n					logger.error(\"SXHCancelServices:还款计划[\" + assetSetSnapshot.getSingleLoanContractNo() + \"]已逾期不可变更\");\r\n					postRequest.put(\"errorMsg\", \"还款计划已逾期\");\r\n					return false;\r\n				}\r\n				OnAccountStatus onAccountStatus = assetSetSnapshot.getOnAccountStatus();\r\n				if (OnAccountStatus.ON_ACCOUNT != onAccountStatus) {\r\n					logger.error(\"SXHCancelServices:还款计划[\" + assetSetSnapshot.getSingleLoanContractNo() + \"]部分结算不可变更\");\r\n					postRequest.put(\"errorMsg\", \"还款计划部分结算\");\r\n					return false;\r\n				}\r\n				if (assetSetSnapshot.is_today_repayment_plan()) {\r\n					logger.error(\"SXHCancelServices:还款计划[\" + assetSetSnapshot.getSingleLoanContractNo() + \"]还款当日不允许变更\");\r\n					postRequest.put(\"errorMsg\", \"还款当日不允许变更\");\r\n					return false;\r\n				}\r\n\r\n			}\r\n\r\n			postRequest.putAll(preRequest);\r\n			postRequest.put(\"fn\",\"200001\");\r\n			postRequest.put(\"requestReason\", RepaymentPlanModifyReason.REASON_7.getOrdinal() + \"\");\r\n			logger.info(\"SXHCancelServices is end\");\r\n			return true;\r\n		} catch (Exception e) {\r\n			e.printStackTrace();\r\n			logger.error(\"SXHCancelServices exception occurred\");\r\n			return false;\r\n		}\r\n	}";
		stringRedisTemplate.delete("sr:" + preProcessInterfaceUrl);
		Principal principal = new Principal();
		principal.setName("sys");
		CustomizeScriptModel model = new CustomizeScriptModel();
		model.setCompileImport(compileImport);
		model.setPreProcessInterfaceUrl(preProcessInterfaceUrl);

		String result = customizeScriptController.submitPreScript(request, response, model, script, principal);
		Map<String, Object> map = JsonUtils.parse(result);
		String code = (String) map.get("code");
		String message = (String) map.get("message");
		Assert.assertEquals(code, GlobalCodeSpec.CODE_FAILURE + "");
		Assert.assertEquals(message, "请先将[modify-repaymentPlan/zhongan/SXH-QXSXH]脚本生效");
	}

	@Test
	@Sql("classpath:test/yunxin/customize/controller/test_submitPreScript_update_illegal_URL.sql")
	public void test_submitPreScript_update_illegal_URL() {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		String preProcessInterfaceUrl = "modify-repaymentPlan/zhongan/SXH-QXSXH";
		String compileImport = "java.math.*,import org.junit.*";
		String script = "public boolean evaluate(SandboxDataSetHandler sandboxDataSetHandler, Map<String, String> preRequest, Map<String, String> postRequest, Log logger) {\r\n		logger.info(\"SXHCancelServices has been started\");\r\n		String uniqueId = (String) preRequest.getOrDefault(\"uniqueId\",\"\");\r\n		String contractNo = (String)preRequest.getOrDefault(\"contractNo\",\"\");\r\n		if (StringUtils.isEmpty(uniqueId) && StringUtils.isEmpty(contractNo)) {\r\n			logger.error(\"SXHCancelServices:uniqueId和contractNo需要赋值一个\");\r\n			postRequest.put(\"errorMsg\", \"uniqueId和contractNo需要赋值一个\");\r\n			return false;\r\n		}\r\n		String requestData = (String)preRequest.getOrDefault(\"requestData\",\"\");\r\n		List<RepaymentPlanModifyRequestDataModel> models = JsonUtils.parseArray(requestData, RepaymentPlanModifyRequestDataModel.class);\r\n		logger.info(\"SXHCancelServices get sandboxDataSet\");\r\n		SandboxDataSet sandboxDataSet = sandboxDataSetHandler.get_sandbox_for_apply_repurchase_by_contract_uniqueId_contractNo(uniqueId, contractNo);\r\n		if (null == sandboxDataSet) {\r\n			logger.error(\"SXHCancelServices:sandboxDataSet is null\");\r\n			postRequest.put(\"errorMsg\", \"sandboxDataSet is null\");\r\n			return false;\r\n		}\r\n		if (null == sandboxDataSet.getFinancialContractSnapshot()) {\r\n			logger.error(\"SXHCancelServices:financialContractSnapshot is null\");\r\n			postRequest.put(\"errorMsg\", \"financialContractSnapshot is null\");\r\n			return false;\r\n		}\r\n		if(!sandboxDataSet.getFinancialContractSnapshot().isAllowFreewheelingRepayment()){\r\n			logger.error(\"SXHCancelServices:信托合同[\"+sandboxDataSet.getFinancialContractSnapshot().getContractNo()+\"]不支持随心还\");\r\n			postRequest.put(\"errorMsg\", \"信托合同不支持随心还\");\r\n			return false;\r\n		}\r\n		List<PaymentPlanSnapshot> assetSetSnapshotList = sandboxDataSet.getPaymentPlanSnapshotList();\r\n		if (CollectionUtils.isEmpty(models) || models.size() != assetSetSnapshotList.size()) {\r\n			logger.error(\"SXHCancelServices:还款计划数不匹配\");\r\n			postRequest.put(\"errorMsg\", \"还款计划数不匹配\");\r\n			return false;\r\n		}\r\n		try{\r\n			for (int i = 0; i < assetSetSnapshotList.size(); i++) {\r\n				PaymentPlanSnapshot assetSetSnapshot = (PaymentPlanSnapshot) assetSetSnapshotList.get(i);\r\n				RepaymentPlanModifyRequestDataModel modifyRequestDataModel = (RepaymentPlanModifyRequestDataModel) models\r\n						.get(i);\r\n				String asset_interest_principal_fingerPrint = modifyRequestDataModel.assetFingerPrint();\r\n				if (!assetSetSnapshot.checkAssetFingerPrint(asset_interest_principal_fingerPrint)) {\r\n					logger.error(\"SXHCancelServices:还款计划基础费用不匹配\");\r\n					postRequest.put(\"errorMsg\", \"还款计划基础费用不匹配\");\r\n					return false;\r\n				}\r\n					//还款成功和逾期\r\n				if (assetSetSnapshot.is_clear_repayment_plan()) {\r\n					logger.error(\"SXHCancelServices:还款计划[\" + assetSetSnapshot.getSingleLoanContractNo() + \"]已结清不可变更\");\r\n					postRequest.put(\"errorMsg\", \"还款计划已结清\");\r\n					return false;\r\n				}\r\n				if (assetSetSnapshot.is_overdue_repayment_plan()) {\r\n					logger.error(\"SXHCancelServices:还款计划[\" + assetSetSnapshot.getSingleLoanContractNo() + \"]已逾期不可变更\");\r\n					postRequest.put(\"errorMsg\", \"还款计划已逾期\");\r\n					return false;\r\n				}\r\n				OnAccountStatus onAccountStatus = assetSetSnapshot.getOnAccountStatus();\r\n				if (OnAccountStatus.ON_ACCOUNT != onAccountStatus) {\r\n					logger.error(\"SXHCancelServices:还款计划[\" + assetSetSnapshot.getSingleLoanContractNo() + \"]部分结算不可变更\");\r\n					postRequest.put(\"errorMsg\", \"还款计划部分结算\");\r\n					return false;\r\n				}\r\n				if (assetSetSnapshot.is_today_repayment_plan()) {\r\n					logger.error(\"SXHCancelServices:还款计划[\" + assetSetSnapshot.getSingleLoanContractNo() + \"]还款当日不允许变更\");\r\n					postRequest.put(\"errorMsg\", \"还款当日不允许变更\");\r\n					return false;\r\n				}\r\n\r\n			}\r\n\r\n			postRequest.putAll(preRequest);\r\n			postRequest.put(\"fn\",\"200001\");\r\n			postRequest.put(\"requestReason\", RepaymentPlanModifyReason.REASON_7.getOrdinal() + \"\");\r\n			logger.info(\"SXHCancelServices is end\");\r\n			return true;\r\n		} catch (Exception e) {\r\n			e.printStackTrace();\r\n			logger.error(\"SXHCancelServices exception occurred\");\r\n			return false;\r\n		}\r\n	}";
		stringRedisTemplate.delete("sr:" + preProcessInterfaceUrl);
		Principal principal = new Principal();
		principal.setName("sys");
		CustomizeScriptModel model = new CustomizeScriptModel();
		model.setCompileImport(compileImport);
		model.setPreProcessInterfaceUrl(preProcessInterfaceUrl);

		String result = customizeScriptController.submitPreScript(request, response, model, script, principal);
		Map<String, Object> map = JsonUtils.parse(result);
		String code = (String) map.get("code");
		String message = (String) map.get("message");
		Assert.assertEquals(code, GlobalCodeSpec.CODE_FAILURE + "");
		Assert.assertEquals(message, "请检查[modify-repaymentPlan/zhongan/SXH-QXSXH]是否为合法处理类型");
	}

	@Test
	@Sql("classpath:test/yunxin/customize/controller/test_submitPreScript_update_suc.sql")
	public void test_submitPreScript_update_suc() {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		String preProcessInterfaceUrl = "modify-repaymentPlan/zhongan/SXH-QXSXH";
		String compileImport = "java.math.*,import org.junit.*";
		String script = "public boolean evaluate(SandboxDataSetHandler sandboxDataSetHandler, Map<String, String> preRequest, Map<String, String> postRequest, Log logger) {\r\n		logger.info(\"SXHCancelServices has been started\");\r\n		String uniqueId = (String) preRequest.getOrDefault(\"uniqueId\",\"\");\r\n		String contractNo = (String)preRequest.getOrDefault(\"contractNo\",\"\");\r\n		if (StringUtils.isEmpty(uniqueId) && StringUtils.isEmpty(contractNo)) {\r\n			logger.error(\"SXHCancelServices:uniqueId和contractNo需要赋值一个\");\r\n			postRequest.put(\"errorMsg\", \"uniqueId和contractNo需要赋值一个\");\r\n			return false;\r\n		}\r\n		String requestData = (String)preRequest.getOrDefault(\"requestData\",\"\");\r\n		List<RepaymentPlanModifyRequestDataModel> models = JsonUtils.parseArray(requestData, RepaymentPlanModifyRequestDataModel.class);\r\n		logger.info(\"SXHCancelServices get sandboxDataSet\");\r\n		SandboxDataSet sandboxDataSet = sandboxDataSetHandler.get_sandbox_for_apply_repurchase_by_contract_uniqueId_contractNo(uniqueId, contractNo);\r\n		if (null == sandboxDataSet) {\r\n			logger.error(\"SXHCancelServices:sandboxDataSet is null\");\r\n			postRequest.put(\"errorMsg\", \"sandboxDataSet is null\");\r\n			return false;\r\n		}\r\n		if (null == sandboxDataSet.getFinancialContractSnapshot()) {\r\n			logger.error(\"SXHCancelServices:financialContractSnapshot is null\");\r\n			postRequest.put(\"errorMsg\", \"financialContractSnapshot is null\");\r\n			return false;\r\n		}\r\n		if(!sandboxDataSet.getFinancialContractSnapshot().isAllowFreewheelingRepayment()){\r\n			logger.error(\"SXHCancelServices:信托合同[\"+sandboxDataSet.getFinancialContractSnapshot().getContractNo()+\"]不支持随心还\");\r\n			postRequest.put(\"errorMsg\", \"信托合同不支持随心还\");\r\n			return false;\r\n		}\r\n		List<PaymentPlanSnapshot> assetSetSnapshotList = sandboxDataSet.getPaymentPlanSnapshotList();\r\n		if (CollectionUtils.isEmpty(models) || models.size() != assetSetSnapshotList.size()) {\r\n			logger.error(\"SXHCancelServices:还款计划数不匹配\");\r\n			postRequest.put(\"errorMsg\", \"还款计划数不匹配\");\r\n			return false;\r\n		}\r\n		try{\r\n			for (int i = 0; i < assetSetSnapshotList.size(); i++) {\r\n				PaymentPlanSnapshot assetSetSnapshot = (PaymentPlanSnapshot) assetSetSnapshotList.get(i);\r\n				RepaymentPlanModifyRequestDataModel modifyRequestDataModel = (RepaymentPlanModifyRequestDataModel) models\r\n						.get(i);\r\n				String asset_interest_principal_fingerPrint = modifyRequestDataModel.assetFingerPrint();\r\n				if (!assetSetSnapshot.checkAssetFingerPrint(asset_interest_principal_fingerPrint)) {\r\n					logger.error(\"SXHCancelServices:还款计划基础费用不匹配\");\r\n					postRequest.put(\"errorMsg\", \"还款计划基础费用不匹配\");\r\n					return false;\r\n				}\r\n					//还款成功和逾期\r\n				if (assetSetSnapshot.is_clear_repayment_plan()) {\r\n					logger.error(\"SXHCancelServices:还款计划[\" + assetSetSnapshot.getSingleLoanContractNo() + \"]已结清不可变更\");\r\n					postRequest.put(\"errorMsg\", \"还款计划已结清\");\r\n					return false;\r\n				}\r\n				if (assetSetSnapshot.is_overdue_repayment_plan()) {\r\n					logger.error(\"SXHCancelServices:还款计划[\" + assetSetSnapshot.getSingleLoanContractNo() + \"]已逾期不可变更\");\r\n					postRequest.put(\"errorMsg\", \"还款计划已逾期\");\r\n					return false;\r\n				}\r\n				OnAccountStatus onAccountStatus = assetSetSnapshot.getOnAccountStatus();\r\n				if (OnAccountStatus.ON_ACCOUNT != onAccountStatus) {\r\n					logger.error(\"SXHCancelServices:还款计划[\" + assetSetSnapshot.getSingleLoanContractNo() + \"]部分结算不可变更\");\r\n					postRequest.put(\"errorMsg\", \"还款计划部分结算\");\r\n					return false;\r\n				}\r\n				if (assetSetSnapshot.is_today_repayment_plan()) {\r\n					logger.error(\"SXHCancelServices:还款计划[\" + assetSetSnapshot.getSingleLoanContractNo() + \"]还款当日不允许变更\");\r\n					postRequest.put(\"errorMsg\", \"还款当日不允许变更\");\r\n					return false;\r\n				}\r\n\r\n			}\r\n\r\n			postRequest.putAll(preRequest);\r\n			postRequest.put(\"fn\",\"200001\");\r\n			postRequest.put(\"requestReason\", RepaymentPlanModifyReason.REASON_7.getOrdinal() + \"\");\r\n			logger.info(\"SXHCancelServices is end\");\r\n			return true;\r\n		} catch (Exception e) {\r\n			e.printStackTrace();\r\n			logger.error(\"SXHCancelServices exception occurred\");\r\n			return false;\r\n		}\r\n	}";
		stringRedisTemplate.delete("sr:" + preProcessInterfaceUrl);
		Principal principal = new Principal();
		principal.setName("sys");
		CustomizeScriptModel model = new CustomizeScriptModel();
		model.setCompileImport(compileImport);
		model.setPreProcessInterfaceUrl(preProcessInterfaceUrl);

		String result = customizeScriptController.submitPreScript(request, response, model, script, principal);

		if (!productCategoryService.check_by_pre_url(preProcessInterfaceUrl, ProductCategoryStatus.VALID)) {
			Assert.fail();
		}
		ProductCategory productCategory = productCategoryService.getByPreUrl(preProcessInterfaceUrl);
		Assert.assertEquals(preProcessInterfaceUrl, productCategory.getPreProcessInterfaceUrl());
		try {
			SourceRepository sourceRepository = sourceRepositoryDBService.getByBusinessType(preProcessInterfaceUrl);
			Assert.assertEquals(productCategory.getScriptMd5Version(), sourceRepository.getSignature());
			Assert.assertEquals(script, sourceRepository.getSourceCode());
			Assert.assertEquals(compileImport, sourceRepository.getCompileImport());
		} catch (XcodeException e) {
			e.printStackTrace();
			Assert.fail();
		}
		System.out.println(result);
	}

	@Test
	@Sql("classpath:test/yunxin/customize/controller/test_enablePreScript_vaild.sql")
	public void test_enablePreScript_vaild() {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		String preProcessInterfaceUrl = "modify-repaymentPlan/zhongan/SXH-QXSXH";
		String result = customizeScriptController.enablePreScript(request, response, preProcessInterfaceUrl);
		Map<String, Object> map = JsonUtils.parse(result);
		String code = (String) map.get("code");
		String message = (String) map.get("message");
		Assert.assertEquals(code, GlobalCodeSpec.CODE_FAILURE + "");
		Assert.assertEquals(message, "脚本[modify-repaymentPlan/zhongan/SXH-QXSXH]已生效");
	}

	@Test
	@Sql("classpath:test/yunxin/customize/controller/test_enablePreScript_suc.sql")
	public void test_enablePreScript_suc() {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		String preProcessInterfaceUrl = "modify-repaymentPlan/zhongan/SXH-QXSXH";
		String result = customizeScriptController.enablePreScript(request, response, preProcessInterfaceUrl);
		Assert.assertTrue(productCategoryService.check_by_pre_url(preProcessInterfaceUrl, ProductCategoryStatus.VALID));
		System.out.println(result);
	}

	@Test
	@Sql("classpath:test/yunxin/customize/controller/test_unablePreScript_invaild.sql")
	public void test_unablePreScript_invaild() {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		String preProcessInterfaceUrl = "modify-repaymentPlan/zhongan/SXH-QXSXH";
		String result = customizeScriptController.unablePreScript(request, response, preProcessInterfaceUrl);
		Map<String, Object> map = JsonUtils.parse(result);
		String code = (String) map.get("code");
		String message = (String) map.get("message");
		Assert.assertEquals(code, GlobalCodeSpec.CODE_FAILURE + "");
		Assert.assertEquals(message, "脚本[modify-repaymentPlan/zhongan/SXH-QXSXH]尚未生效");
	}

	@Test
	@Sql("classpath:test/yunxin/customize/controller/test_unablePreScript_suc.sql")
	public void test_unablePreScript_suc() {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		String preProcessInterfaceUrl = "modify-repaymentPlan/zhongan/SXH-QXSXH";
		String result = customizeScriptController.unablePreScript(request, response, preProcessInterfaceUrl);
		Assert.assertTrue(
				productCategoryService.check_by_pre_url(preProcessInterfaceUrl, ProductCategoryStatus.INVALID));
		System.out.println(result);
	}

	@Test
	@Sql("classpath:test/yunxin/customize/controller/test_getCatalog_no_catalog.sql")
	public void test_getCatalog_no_catalog() {
		String result = customizeScriptController.getCatalog();
		Map<String, Object> map = JsonUtils.parse(result);
		String message = (String) map.get("message");
		Assert.assertEquals(message, "无脚本配置信息");

		System.out.println(result);
	}

	@Test
	@Sql("classpath:test/yunxin/customize/controller/test_getCatalog.sql")
	@SuppressWarnings("unchecked")
	public void test_getCatalog() {
		String result = customizeScriptController.getCatalog();
		Map<String, Object> map = JsonUtils.parse(result);
		Map<String, Object> data = (Map<String, Object>) map.get("data");
		Map<String, Object> mapLv1 = (Map<String, Object>) data.get(CustomizeScriptControllerSpec.INFO.CATA_INFO_LIST);
		Assert.assertEquals(1, mapLv1.size());
		for (String key : mapLv1.keySet()) {
			Map<String, Object> mapLv2 = (Map<String, Object>) mapLv1.get(key);
			Assert.assertEquals(1, mapLv2.size());
			for (String keyLv2 : mapLv2.keySet()) {
				Map<String, Object> mapLv3 = (Map<String, Object>) mapLv2.get(keyLv2);
				Assert.assertEquals(6, mapLv3.size());
			}
		}
		System.out.println(result);
	}

	@Test
	@Sql("classpath:test/yunxin/customize/controller/test_getSource_illegal_URL.sql")
	public void test_getSource_illegal_URL() {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		request.setRequestURI("/customize-script/modify-repaymentPlan/zhongan/SXH-QXSXH_1");
		try {
			String result = customizeScriptController.getSource(request, response);
			Map<String, Object> map = JsonUtils.parse(result);
			String code = (String) map.get("code");
			String message = (String) map.get("message");
			Assert.assertEquals(code, GlobalCodeSpec.CODE_FAILURE + "");
			Assert.assertEquals(message, "请检查[modify-repaymentPlan/zhongan/SXH-QXSXH_1]是否为合法处理类型");;
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	@Test
	@Sql("classpath:test/yunxin/customize/controller/test_getSource.sql")
	@SuppressWarnings("unchecked")
	public void test_getSource() {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		request.setRequestURI("/customize-script/modify-repaymentPlan/zhongan/SXH-QXSXH");
		try {
			String compileImport = "java.math.*";
			String script = "public boolean evaluate(SandboxDataSetHandler sandboxDataSetHandler, Map<String, String> preRequest, Map<String, String> postRequest, Log logger) {\r\n		logger.info(\"SXHCancelServices has been started\");\r\n		String uniqueId = (String) preRequest.getOrDefault(\"uniqueId\",\"\");\r\n		String contractNo = (String)preRequest.getOrDefault(\"contractNo\",\"\");\r\n		if (StringUtils.isEmpty(uniqueId) && StringUtils.isEmpty(contractNo)) {\r\n			logger.error(\"SXHCancelServices:uniqueId和contractNo需要赋值一个\");\r\n			postRequest.put(\"errorMsg\", \"uniqueId和contractNo需要赋值一个\");\r\n			return false;\r\n		}\r\n		String requestData = (String)preRequest.getOrDefault(\"requestData\",\"\");\r\n		List<RepaymentPlanModifyRequestDataModel> models = JsonUtils.parseArray(requestData, RepaymentPlanModifyRequestDataModel.class);\r\n		logger.info(\"SXHCancelServices get sandboxDataSet\");\r\n		SandboxDataSet sandboxDataSet = sandboxDataSetHandler.get_sandbox_for_apply_repurchase_by_contract_uniqueId_contractNo(uniqueId, contractNo);\r\n		if (null == sandboxDataSet) {\r\n			logger.error(\"SXHCancelServices:sandboxDataSet is null\");\r\n			postRequest.put(\"errorMsg\", \"sandboxDataSet is null\");\r\n			return false;\r\n		}\r\n		if (null == sandboxDataSet.getFinancialContractSnapshot()) {\r\n			logger.error(\"SXHCancelServices:financialContractSnapshot is null\");\r\n			postRequest.put(\"errorMsg\", \"financialContractSnapshot is null\");\r\n			return false;\r\n		}\r\n		if(!sandboxDataSet.getFinancialContractSnapshot().isAllowFreewheelingRepayment()){\r\n			logger.error(\"SXHCancelServices:信托合同[\"+sandboxDataSet.getFinancialContractSnapshot().getContractNo()+\"]不支持随心还\");\r\n			postRequest.put(\"errorMsg\", \"信托合同不支持随心还\");\r\n			return false;\r\n		}\r\n		List<PaymentPlanSnapshot> assetSetSnapshotList = sandboxDataSet.getPaymentPlanSnapshotList();\r\n		if (CollectionUtils.isEmpty(models) || models.size() != assetSetSnapshotList.size()) {\r\n			logger.error(\"SXHCancelServices:还款计划数不匹配\");\r\n			postRequest.put(\"errorMsg\", \"还款计划数不匹配\");\r\n			return false;\r\n		}\r\n		try{\r\n			for (int i = 0; i < assetSetSnapshotList.size(); i++) {\r\n				PaymentPlanSnapshot assetSetSnapshot = (PaymentPlanSnapshot) assetSetSnapshotList.get(i);\r\n				RepaymentPlanModifyRequestDataModel modifyRequestDataModel = (RepaymentPlanModifyRequestDataModel) models\r\n						.get(i);\r\n				String asset_interest_principal_fingerPrint = modifyRequestDataModel.assetFingerPrint();\r\n				if (!assetSetSnapshot.checkAssetFingerPrint(asset_interest_principal_fingerPrint)) {\r\n					logger.error(\"SXHCancelServices:还款计划基础费用不匹配\");\r\n					postRequest.put(\"errorMsg\", \"还款计划基础费用不匹配\");\r\n					return false;\r\n				}\r\n					//还款成功和逾期\r\n				if (assetSetSnapshot.is_clear_repayment_plan()) {\r\n					logger.error(\"SXHCancelServices:还款计划[\" + assetSetSnapshot.getSingleLoanContractNo() + \"]已结清不可变更\");\r\n					postRequest.put(\"errorMsg\", \"还款计划已结清\");\r\n					return false;\r\n				}\r\n				if (assetSetSnapshot.is_overdue_repayment_plan()) {\r\n					logger.error(\"SXHCancelServices:还款计划[\" + assetSetSnapshot.getSingleLoanContractNo() + \"]已逾期不可变更\");\r\n					postRequest.put(\"errorMsg\", \"还款计划已逾期\");\r\n					return false;\r\n				}\r\n				OnAccountStatus onAccountStatus = assetSetSnapshot.getOnAccountStatus();\r\n				if (OnAccountStatus.ON_ACCOUNT != onAccountStatus) {\r\n					logger.error(\"SXHCancelServices:还款计划[\" + assetSetSnapshot.getSingleLoanContractNo() + \"]部分结算不可变更\");\r\n					postRequest.put(\"errorMsg\", \"还款计划部分结算\");\r\n					return false;\r\n				}\r\n				if (assetSetSnapshot.is_today_repayment_plan()) {\r\n					logger.error(\"SXHCancelServices:还款计划[\" + assetSetSnapshot.getSingleLoanContractNo() + \"]还款当日不允许变更\");\r\n					postRequest.put(\"errorMsg\", \"还款当日不允许变更\");\r\n					return false;\r\n				}\r\n\r\n			}\r\n\r\n			postRequest.putAll(preRequest);\r\n			postRequest.put(\"fn\",\"200001\");\r\n			postRequest.put(\"requestReason\", RepaymentPlanModifyReason.REASON_7.getOrdinal() + \"\");\r\n			logger.info(\"SXHCancelServices is end\");\r\n			return true;\r\n		} catch (Exception e) {\r\n			e.printStackTrace();\r\n			logger.error(\"SXHCancelServices exception occurred\");\r\n			return false;\r\n		}\r\n	}";
			String result = customizeScriptController.getSource(request, response);
			Map<String, Object> map = JsonUtils.parse(result);
			Map<String, String> data = (Map<String, String>) map.get("data");
			Assert.assertEquals(compileImport, data.get(CustomizeScriptControllerSpec.INFO.SOURCE_INFO_COMPILE_IMPORT));
			Assert.assertEquals(script, data.get(CustomizeScriptControllerSpec.INFO.SOURCE_INFO_SCRIPT));
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	@Test
	@SuppressWarnings("unchecked")
	public void test_runSource_suc() {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		String compileImport = "java.math.BigDecimal,java.util.*,com.suidifu.matryoshka.customize.CustomizeServices,com.zufangbao.sun.utils.*,com.suidifu.matryoshka.snapshot.*,com.zufangbao.sun.yunxin.entity.OnAccountStatus,com.zufangbao.sun.yunxin.entity.model.api.modify.*,org.apache.commons.collections.CollectionUtils";
		String script = "public boolean evaluate(SandboxDataSetHandler sandboxDataSetHandler, Map<String, String> preRequest, Map<String, String> postRequest, Log logger) {\r\n		logger.info(\"RefundsServices has been started\");\r\n		String uniqueId = (String) preRequest.getOrDefault(\"uniqueId\",\"\");\r\n		String contractNo = (String)preRequest.getOrDefault(\"contractNo\",\"\");\r\n		if (StringUtils.isEmpty(uniqueId) && StringUtils.isEmpty(contractNo)) {\r\n			logger.error(\"RefundsServices: uniqueId和contractNo需要赋值一个\");\r\n			postRequest.put(\"errorMsg\", \"uniqueId和contractNo需要赋值一个\");\r\n			return false;\r\n		}\r\n		String requestData = (String)preRequest.getOrDefault(\"requestData\",\"\");\r\n		BigDecimal assetPrincipalAmount = BigDecimal.ZERO;\r\n		List<RepaymentPlanModifyRequestDataModel> models = JsonUtils.parseArray(requestData, RepaymentPlanModifyRequestDataModel.class);\r\n		logger.info(\"RefundsServices get sandboxDataSet\");\r\n		SandboxDataSet sandboxDataSet = sandboxDataSetHandler.get_sandbox_for_apply_repurchase_by_contract_uniqueId_contractNo(uniqueId, contractNo);\r\n		if (null == sandboxDataSet) {\r\n			logger.error(\"RefundsServices:sandboxDataSet is null\");\r\n			postRequest.put(\"errorMsg\", \"sandboxDataSet is null\");\r\n			return false;\r\n		}\r\n		if (null == sandboxDataSet.getFinancialContractSnapshot()) {\r\n			logger.error(\"RefundsServices:financialContractSnapshot is null\");\r\n			postRequest.put(\"errorMsg\", \"financialContractSnapshot is null\");\r\n			return false;\r\n		}\r\n		if(!sandboxDataSet.getFinancialContractSnapshot().isAllowFreewheelingRepayment()){\r\n			logger.error(\"RefundsServices:信托合同[\"+sandboxDataSet.getFinancialContractSnapshot().getContractNo()+\"]不支持随心还\");\r\n			postRequest.put(\"errorMsg\", \"信托合同不支持随心还\");\r\n			return false;\r\n		}\r\n		List<PaymentPlanSnapshot> assetSetSnapshotList = sandboxDataSet.getPaymentPlanSnapshotList();\r\n		if (CollectionUtils.isEmpty(assetSetSnapshotList)) {\r\n			logger.error(\"RefundsServices:assetSetSnapshotList is Empty\");\r\n			postRequest.put(\"errorMsg\", \"assetSetSnapshotList is Empty\");\r\n			return false;\r\n		}\r\n		try{\r\n			if (models.size() != 1) {\r\n				logger.error(\"RefundsServices:退货后还款计划只可剩余一期\");\r\n				postRequest.put(\"errorMsg\", \"退货后还款计划只可剩余一期\");\r\n				return false;\r\n			}\r\n			boolean isFirstPeriodMutable = false;\r\n			for(PaymentPlanSnapshot assetSetSnapshot: assetSetSnapshotList){\r\n				assetPrincipalAmount = assetPrincipalAmount.add(assetSetSnapshot.getAssetPrincipalValue());\r\n				if(1 == assetSetSnapshot.getCurrentPeriod()) {\r\n					if (assetSetSnapshot.is_clear_repayment_plan()) {\r\n						logger.error(\"RefundsServices:还款计划[\" + assetSetSnapshot.getSingleLoanContractNo() + \"]已结清不可变更\");\r\n						postRequest.put(\"errorMsg\", \"还款计划已结清\");\r\n						return false;\r\n					}\r\n					if (assetSetSnapshot.is_overdue_repayment_plan()) {\r\n						logger.error(\"RefundsServices:还款计划[\" + assetSetSnapshot.getSingleLoanContractNo() + \"]已逾期不可变更\");\r\n						postRequest.put(\"errorMsg\", \"还款计划已逾期\");\r\n						return false;\r\n					}\r\n					OnAccountStatus onAccountStatus = assetSetSnapshot.getOnAccountStatus();\r\n					if (OnAccountStatus.ON_ACCOUNT != onAccountStatus) {\r\n						logger.error(\"RefundsServices:还款计划[\" + assetSetSnapshot.getSingleLoanContractNo() + \"]部分结算不可变更\");\r\n						postRequest.put(\"errorMsg\", \"还款计划部分结算\");\r\n						return false;\r\n					}\r\n					if (assetSetSnapshot.is_today_repayment_plan()) {\r\n						logger.error(\"RefundsServices:还款计划[\" + assetSetSnapshot.getSingleLoanContractNo() + \"]还款当日不允许变更\");\r\n						postRequest.put(\"errorMsg\", \"还款当日不允许变更\");\r\n						return false;\r\n					}\r\n					isFirstPeriodMutable = true;\r\n				}\r\n			}\r\n			if(!isFirstPeriodMutable){\r\n				logger.error(\"RefundsServices:首期判定失败\");\r\n				postRequest.put(\"errorMsg\", \"首期判定失败\");\r\n				return false;\r\n			}\r\n\r\n			RepaymentPlanModifyRequestDataModel modifyRequestDataModel = (RepaymentPlanModifyRequestDataModel)models.get(0);\r\n			//变更后本金不可小于原有本金\r\n			if(modifyRequestDataModel.getAssetPrincipal().compareTo(assetPrincipalAmount) < 0){\r\n				logger.error(\"RefundsServices:非法本金\");\r\n				postRequest.put(\"errorMsg\", \"非法本金\");\r\n				return false;\r\n			}\r\n\r\n			postRequest.putAll(preRequest);\r\n			postRequest.put(\"fn\",\"200001\");\r\n			postRequest.put(\"requestReason\", RepaymentPlanModifyReason.REASON_9.getOrdinal() + \"\");\r\n			logger.info(\"RefundsServices is end\");\r\n			return true;\r\n		} catch (Exception e) {\r\n			e.printStackTrace();\r\n			logger.error(\"RefundsServices exception occurred\");\r\n			return false;\r\n		}\r\n	}";
		request.addParameter(CustomizeScriptControllerSpec.REQUEST.PRE_SCRIPT, script);
		request.addParameter(CustomizeScriptControllerSpec.REQUEST.COMPILE_IMPORT, compileImport);
		String result = customizeScriptController.runSource(response, request);
		Map<String, Object> map = JsonUtils.parse(result);
		Map<String, String> data = (Map<String, String>) map.get("data");
		String code = (String) map.get("code");
		Assert.assertEquals(code, GlobalCodeSpec.CODE_SUC + "");
		Assert.assertEquals(data.get(CustomizeScriptControllerSpec.INFO.INFO_KEY), "编译成功");
		System.out.println(result);
	}

	@Test
	public void test_runSource_miss_import() {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		String compileImport = "";
		String script = "public boolean evaluate(SandboxDataSetHandler sandboxDataSetHandler, Map<String, String> preRequest, Map<String, String> postRequest, Log logger) {\r\n		logger.info(\"RefundsServices has been started\");\r\n		String uniqueId = (String) preRequest.getOrDefault(\"uniqueId\",\"\");\r\n		String contractNo = (String)preRequest.getOrDefault(\"contractNo\",\"\");\r\n		if (StringUtils.isEmpty(uniqueId) && StringUtils.isEmpty(contractNo)) {\r\n			logger.error(\"RefundsServices: uniqueId和contractNo需要赋值一个\");\r\n			postRequest.put(\"errorMsg\", \"uniqueId和contractNo需要赋值一个\");\r\n			return false;\r\n		}\r\n		String requestData = (String)preRequest.getOrDefault(\"requestData\",\"\");\r\n		BigDecimal assetPrincipalAmount = BigDecimal.ZERO;\r\n		List<RepaymentPlanModifyRequestDataModel> models = JsonUtils.parseArray(requestData, RepaymentPlanModifyRequestDataModel.class);\r\n		logger.info(\"RefundsServices get sandboxDataSet\");\r\n		SandboxDataSet sandboxDataSet = sandboxDataSetHandler.get_sandbox_for_apply_repurchase_by_contract_uniqueId_contractNo(uniqueId, contractNo);\r\n		if (null == sandboxDataSet) {\r\n			logger.error(\"RefundsServices:sandboxDataSet is null\");\r\n			postRequest.put(\"errorMsg\", \"sandboxDataSet is null\");\r\n			return false;\r\n		}\r\n		if (null == sandboxDataSet.getFinancialContractSnapshot()) {\r\n			logger.error(\"RefundsServices:financialContractSnapshot is null\");\r\n			postRequest.put(\"errorMsg\", \"financialContractSnapshot is null\");\r\n			return false;\r\n		}\r\n		if(!sandboxDataSet.getFinancialContractSnapshot().isAllowFreewheelingRepayment()){\r\n			logger.error(\"RefundsServices:信托合同[\"+sandboxDataSet.getFinancialContractSnapshot().getContractNo()+\"]不支持随心还\");\r\n			postRequest.put(\"errorMsg\", \"信托合同不支持随心还\");\r\n			return false;\r\n		}\r\n		List<PaymentPlanSnapshot> assetSetSnapshotList = sandboxDataSet.getPaymentPlanSnapshotList();\r\n		if (CollectionUtils.isEmpty(assetSetSnapshotList)) {\r\n			logger.error(\"RefundsServices:assetSetSnapshotList is Empty\");\r\n			postRequest.put(\"errorMsg\", \"assetSetSnapshotList is Empty\");\r\n			return false;\r\n		}\r\n		try{\r\n			if (models.size() != 1) {\r\n				logger.error(\"RefundsServices:退货后还款计划只可剩余一期\");\r\n				postRequest.put(\"errorMsg\", \"退货后还款计划只可剩余一期\");\r\n				return false;\r\n			}\r\n			boolean isFirstPeriodMutable = false;\r\n			for(PaymentPlanSnapshot assetSetSnapshot: assetSetSnapshotList){\r\n				assetPrincipalAmount = assetPrincipalAmount.add(assetSetSnapshot.getAssetPrincipalValue());\r\n				if(1 == assetSetSnapshot.getCurrentPeriod()) {\r\n					if (assetSetSnapshot.is_clear_repayment_plan()) {\r\n						logger.error(\"RefundsServices:还款计划[\" + assetSetSnapshot.getSingleLoanContractNo() + \"]已结清不可变更\");\r\n						postRequest.put(\"errorMsg\", \"还款计划已结清\");\r\n						return false;\r\n					}\r\n					if (assetSetSnapshot.is_overdue_repayment_plan()) {\r\n						logger.error(\"RefundsServices:还款计划[\" + assetSetSnapshot.getSingleLoanContractNo() + \"]已逾期不可变更\");\r\n						postRequest.put(\"errorMsg\", \"还款计划已逾期\");\r\n						return false;\r\n					}\r\n					OnAccountStatus onAccountStatus = assetSetSnapshot.getOnAccountStatus();\r\n					if (OnAccountStatus.ON_ACCOUNT != onAccountStatus) {\r\n						logger.error(\"RefundsServices:还款计划[\" + assetSetSnapshot.getSingleLoanContractNo() + \"]部分结算不可变更\");\r\n						postRequest.put(\"errorMsg\", \"还款计划部分结算\");\r\n						return false;\r\n					}\r\n					if (assetSetSnapshot.is_today_repayment_plan()) {\r\n						logger.error(\"RefundsServices:还款计划[\" + assetSetSnapshot.getSingleLoanContractNo() + \"]还款当日不允许变更\");\r\n						postRequest.put(\"errorMsg\", \"还款当日不允许变更\");\r\n						return false;\r\n					}\r\n					isFirstPeriodMutable = true;\r\n				}\r\n			}\r\n			if(!isFirstPeriodMutable){\r\n				logger.error(\"RefundsServices:首期判定失败\");\r\n				postRequest.put(\"errorMsg\", \"首期判定失败\");\r\n				return false;\r\n			}\r\n\r\n			RepaymentPlanModifyRequestDataModel modifyRequestDataModel = (RepaymentPlanModifyRequestDataModel)models.get(0);\r\n			//变更后本金不可小于原有本金\r\n			if(modifyRequestDataModel.getAssetPrincipal().compareTo(assetPrincipalAmount) < 0){\r\n				logger.error(\"RefundsServices:非法本金\");\r\n				postRequest.put(\"errorMsg\", \"非法本金\");\r\n				return false;\r\n			}\r\n\r\n			postRequest.putAll(preRequest);\r\n			postRequest.put(\"fn\",\"200001\");\r\n			postRequest.put(\"requestReason\", RepaymentPlanModifyReason.REASON_9.getOrdinal() + \"\");\r\n			logger.info(\"RefundsServices is end\");\r\n			return true;\r\n		} catch (Exception e) {\r\n			e.printStackTrace();\r\n			logger.error(\"RefundsServices exception occurred\");\r\n			return false;\r\n		}\r\n	}";
		request.addParameter(CustomizeScriptControllerSpec.REQUEST.PRE_SCRIPT, script);
		request.addParameter(CustomizeScriptControllerSpec.REQUEST.COMPILE_IMPORT, compileImport);
		String result = customizeScriptController.runSource(response, request);
		Map<String, Object> map = JsonUtils.parse(result);
		String code = (String) map.get("code");
		Assert.assertEquals(code, GlobalCodeSpec.CODE_FAILURE + "");
		System.out.println(result);
	}

	@Test
	public void test_runSource_wrong_script() {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		String compileImport = "java.math.*";
		String script = "public boolean evaluate(SandboxDataSetHandler sandboxDataSetHandler, Map<String, String> preRequest, Map<String, String> postRequest, Log logger) {\r\n		logger.info(\"RefundsServices has been started\");\r\n		String uniqueId = (String) preRequest.getOrDefault(\"uniqueId\",\"\");\r\n		String contractNo = (String)preRequest.getOrDefault(\"contractNo\",\"\");\r\n		if (StringUtils.isEmpty(uniqueId) && StringUtils.isEmpty(contractNo)) {\r\n			logger.error(\"RefundsServices: uniqueId和contractNo需要赋值一个\");\r\n			postRequest.put(\"errorMsg\", \"uniqueId和contractNo需要赋值一个\");\r\n			return false;\r\n		}\r\n		String requestData = (String)preRequest.getOrDefault(\"requestData\",\"\");\r\n		BigDecimal assetPrincipalAmount = BigDecimal.ZERO;\r\n		List<RepaymentPlanModifyRequestDataModel> models = JsonUtils.parseArray(requestData, RepaymentPlanModifyRequestDataModel.class);\r\n		logger.info(\"RefundsServices get sandboxDataSet\");\r\n		SandboxDataSet sandboxDataSet = sandboxDataSetHandler.get_sandbox_for_apply_repurchase_by_contract_uniqueId_contractNo(uniqueId, contractNo);\r\n		if (null == sandboxDataSet) {\r\n			logger.error(\"RefundsServices:sandboxDataSet is null\");\r\n			postRequest.put(\"errorMsg\", \"sandboxDataSet is null\");\r\n			return false;\r\n		}\r\n		if (null == sandboxDataSet.getFinancialContractSnapshot()) {\r\n			logger.error(\"RefundsServices:financialContractSnapshot is null\");\r\n			postRequest.put(\"errorMsg\", \"financialContractSnapshot is null\");\r\n			return false;\r\n		}\r\n		if(!sandboxDataSet.getFinancialContractSnapshot().isAllowFreewheelingRepayment()){\r\n			logger.error(\"RefundsServices:信托合同[\"+sandboxDataSet.getFinancialContractSnapshot().getContractNo()+\"]不支持随心还\");\r\n			postRequest.put(\"errorMsg\", \"信托合同不支持随心还\");\r\n			return false;\r\n		}\r\n		List<PaymentPlanSnapshot> assetSetSnapshotList = sandboxDataSet.getPaymentPlanSnapshotList();\r\n		if (CollectionUtils.isEmpty(assetSetSnapshotList)) {\r\n			logger.error(\"RefundsServices:assetSetSnapshotList is Empty\");\r\n			postRequest.put(\"errorMsg\", \"assetSetSnapshotList is Empty\");\r\n			return false;\r\n		}\r\n		try{\r\n			if (models.size() != 1) {\r\n				logger.error(\"RefundsServices:退货后还款计划只可剩余一期\");\r\n				postRequest.put(\"errorMsg\", \"退货后还款计划只可剩余一期\");\r\n				return false;\r\n			}\r\n			boolean isFirstPeriodMutable = false;\r\n			for(PaymentPlanSnapshot assetSetSnapshot: assetSetSnapshotList){\r\n				assetPrincipalAmount = assetPrincipalAmount.add(assetSetSnapshot.getAssetPrincipalValue());\r\n				if(1 == assetSetSnapshot.getCurrentPeriod()) {\r\n					if (assetSetSnapshot.is_clear_repayment_plan()) {\r\n						logger.error(\"RefundsServices:还款计划[\" + assetSetSnapshot.getSingleLoanContractNo() + \"]已结清不可变更\");\r\n						postRequest.put(\"errorMsg\", \"还款计划已结清\");\r\n						return false;\r\n					}\r\n					if (assetSetSnapshot.is_overdue_repayment_plan()) {\r\n						logger.error(\"RefundsServices:还款计划[\" + assetSetSnapshot.getSingleLoanContractNo() + \"]已逾期不可变更\");\r\n						postRequest.put(\"errorMsg\", \"还款计划已逾期\");\r\n						return false;\r\n					}\r\n					OnAccountStatus onAccountStatus = assetSetSnapshot.getOnAccountStatus();\r\n					if (OnAccountStatus.ON_ACCOUNT != onAccountStatus) {\r\n						logger.error(\"RefundsServices:还款计划[\" + assetSetSnapshot.getSingleLoanContractNo() + \"]部分结算不可变更\");\r\n						postRequest.put(\"errorMsg\", \"还款计划部分结算\");\r\n						return false;\r\n					}\r\n					if (assetSetSnapshot.is_today_repayment_plan()) {\r\n						logger.error(\"RefundsServices:还款计划[\" + assetSetSnapshot.getSingleLoanContractNo() + \"]还款当日不允许变更\");\r\n						postRequest.put(\"errorMsg\", \"还款当日不允许变更\");\r\n						return false;\r\n					}\r\n					isFirstPeriodMutable = true;\r\n				}\r\n			}\r\n			if(!isFirstPeriodMutable){\r\n				logger.error(\"RefundsServices:首期判定失败\");\r\n				postRequest.put(\"errorMsg\", \"首期判定失败\");\r\n				return false;\r\n			}\r\n\r\n			RepaymentPlanModifyRequestDataModel modifyRequestDataModel = (RepaymentPlanModifyRequestDataModel)models.get(0);\r\n			//变更后本金不可小于原有本金\r\n			if(modifyRequestDataModel.getAssetPrincipal().compareTo(assetPrincipalAmount) < 0){\r\n				logger.error(\"RefundsServices:非法本金\");\r\n				postRequest.put(\"errorMsg\", \"非法本金\");\r\n				return false;\r\n			}\r\n\r\n			postRequest.putAll(preRequest);\r\n			postRequest.put(\"fn\",\"200001\");\r\n			postRequest.put(\"requestReason\", RepaymentPlanModifyReason.REASON_9.getOrdinal() + \"\");\r\n			logger.info(\"RefundsServices is end\");\r\n			return true;\r\n		} catch (Exception e) {\r\n			e.printStackTrace();\r\n			logger.error(\"RefundsServices exception occurred\");\r\n			return false;\r\n		}\r\n	";
		request.addParameter(CustomizeScriptControllerSpec.REQUEST.PRE_SCRIPT, script);
		request.addParameter(CustomizeScriptControllerSpec.REQUEST.COMPILE_IMPORT, compileImport);
		String result = customizeScriptController.runSource(response, request);
		Map<String, Object> map = JsonUtils.parse(result);
		String code = (String) map.get("code");
		Assert.assertEquals(code, GlobalCodeSpec.CODE_FAILURE + "");
		System.out.println(result);
	}
}
