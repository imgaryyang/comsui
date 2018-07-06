package com.zufangbao.earth.yunxin.api;

import com.demo2do.core.utils.JsonUtils;
import com.zufangbao.earth.yunxin.api.util.ApiMessageUtil;
import com.zufangbao.gluon.exception.ApiException;
import com.zufangbao.gluon.exception.GlobalRuntimeException;
import com.zufangbao.sun.api.model.modify.RepaymentPlanModifyModel;
import com.zufangbao.sun.api.model.repayment.RepaymentPlanModifyDetail;
import com.zufangbao.sun.entity.contract.Contract;
import com.zufangbao.sun.service.ContractService;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.yunxin.entity.AssetSet;
import com.zufangbao.sun.yunxin.entity.model.api.modify.RepaymentPlanModifyRequestDataModel;
import com.zufangbao.sun.yunxin.service.RepaymentPlanService;
import com.zufangbao.wellsfargo.yunxin.handler.AssetSetApiHandler;
import com.zufangbao.wellsfargo.yunxin.handler.ContractApiHandler;
import com.zufangbao.wellsfargo.yunxin.handler.impl.AssetSetApiHandlerImpl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.zufangbao.gluon.spec.earth.v3.ApiResponseCode.CONTRACT_NOT_EXIST;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {

		"classpath:/local/applicationContext-*.xml" })
@TransactionConfiguration(defaultRollback = true)
@Transactional
public class AssetSetApiHandlerTest extends AbstractTransactionalJUnit4SpringContextTests{

	@Autowired
	private ContractApiHandler contractApiHandler;
	@Autowired
	private AssetSetApiHandler assetSetApiHandler;
	@Autowired
	private ContractService contractService;
	@Autowired
	private RepaymentPlanService repaymentPlanService;

	@Test
	@Sql("classpath:test/yunxin/api/GetContract_throw_error.sql")
	public void testGetContract_throw_error() {
		try {
			contractApiHandler.getContractBy("", "");
			Assert.fail();
		} catch (ApiException e) {
			Assert.assertEquals(CONTRACT_NOT_EXIST, e.getCode());
		}
	}

	@Test
	@Sql("classpath:test/yunxin/api/GetContract.sql")
	public void testGetContract() {
		try {
			String contractNo = "云信信2016-78-DK(ZQ2016000000001)";
			Contract contract = contractApiHandler.getContractBy("", contractNo);
			Assert.assertEquals(contractNo, contract.getContractNo());
			Assert.assertEquals(54340L, contract.getId().longValue());
			Assert.fail();
		} catch (ApiException e) {
		}
	}

	@Test
	@Sql("classpath:test/yunxin/api/GetContract_1.sql")
	public void testGetContract_1() {
		try {
			String unique = "e568793f-a44c-4362-9e78-0ce433131f3e";
			Contract contract = contractApiHandler.getContractBy(unique, "");
			Assert.assertEquals(unique, contract.getUniqueId());
			Assert.assertEquals(54340L, contract.getId().longValue());

		} catch (ApiException e) {
			Assert.fail();
		}
	}


	/*************** test hasDeductingAssetIn start ***************/
	@Test
	public void test_isCanUpdateAssetListEmpty_EmptyList() {
		List<AssetSet> assetSets = new ArrayList<>();
		AssetSetApiHandlerImpl assetSetApiHandlerImpl = new AssetSetApiHandlerImpl();
		try {
			assetSetApiHandlerImpl.hasDeductingAssetIn(assetSets);
			Assert.fail();
		} catch (ApiException e) {
			Assert.assertEquals("当前贷款合同无法变更!", ApiMessageUtil.getMessage(e.getCode()));
		}
	}

	@Test
	public void test_hasDeductingAssetIn() {

		AssetSet assetSet1 = new AssetSet();
		AssetSet assetSet2 = new AssetSet();
		assetSet1.setActiveDeductApplicationUuid("empty_deduct_uuid");
		assetSet2.setActiveDeductApplicationUuid("empty_deduct_uuid");

		List<AssetSet> assetSets = new ArrayList<>();
		assetSets.add(assetSet1);
		assetSets.add(assetSet2);

		AssetSetApiHandlerImpl assetSetApiHandlerImpl = new AssetSetApiHandlerImpl();
		assetSetApiHandlerImpl.hasDeductingAssetIn(assetSets);
	}

	@Test
	public void test_hasDeductingAssetIn_ApplicationUuidNotEmpty() {

		AssetSet assetSet1 = new AssetSet();
		AssetSet assetSet2 = new AssetSet();
		assetSet1.setActiveDeductApplicationUuid("12345");
		assetSet2.setActiveDeductApplicationUuid("empty_deduct_uuid");

		List<AssetSet> assetSets = new ArrayList<>();
		assetSets.add(assetSet1);
		assetSets.add(assetSet2);

		AssetSetApiHandlerImpl assetSetApiHandlerImpl = new AssetSetApiHandlerImpl();
		try {
			assetSetApiHandlerImpl.hasDeductingAssetIn(assetSets);
			Assert.fail();
		} catch (ApiException e) {
			Assert.assertEquals("存在当日扣款成功或处理中的还款计划!", ApiMessageUtil.getMessage(e.getCode()));
		}
	}
	/*************** test isCanUpdateAssetListEmpty end ***************/


	/*************** test modifyRepaymentPlan start ***************/
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

		List<RepaymentPlanModifyRequestDataModel> requestDataModels = new ArrayList<>();
		requestDataModels.add(repaymentPlanModifyRequestDataModel1);
		requestDataModels.add(repaymentPlanModifyRequestDataModel2);
		requestDataModels.add(repaymentPlanModifyRequestDataModel3);
		String requestData = JsonUtils.toJsonString(requestDataModels);

		RepaymentPlanModifyModel modifyModel = new RepaymentPlanModifyModel();
		modifyModel.setFn("FNTEST");
		modifyModel.setRequestNo("AAA");
		modifyModel.setUniqueId("e568793f-a44c-4362-9e78-0ce433131f3e");
		modifyModel.setContractNo("云信信2016-241-DK(428522112675736881)");
		modifyModel.setRequestReason("1");
		modifyModel.setRequestData(requestData);

		Contract contract = contractService.getContract(54340L);
		Integer oldActiveVersionNo = contract.getActiveVersionNo();
		try {
			List<RepaymentPlanModifyDetail> stringList = assetSetApiHandler.modifyRepaymentPlan(contract, oldActiveVersionNo, modifyModel, "192.168.1.1");
//			List<String> stringList = assetSetApiHandler.modifyRepaymentPlan(contract, oldActiveVersionNo, modifyModel, "192.168.1.1");
			System.out.printf("stringList");
		} catch (Exception e) {
			e.printStackTrace();
			//Assert.fail();
		}
	}

	@Test
	@Sql("classpath:test/yunxin/api/test_modify_repaymentPlan_2.sql")
	public void test_modify_repaymentPlan_abnormal() {
		RepaymentPlanModifyRequestDataModel repaymentPlanModifyRequestDataModel1 = new RepaymentPlanModifyRequestDataModel();
		RepaymentPlanModifyRequestDataModel repaymentPlanModifyRequestDataModel2 = new RepaymentPlanModifyRequestDataModel();
		RepaymentPlanModifyRequestDataModel repaymentPlanModifyRequestDataModel3 = new RepaymentPlanModifyRequestDataModel();

		repaymentPlanModifyRequestDataModel1.setAssetRecycleDate(DateUtils.format(DateUtils.addDays(DateUtils.getToday(), 5)));
		repaymentPlanModifyRequestDataModel2.setAssetRecycleDate(DateUtils.format(DateUtils.addDays(DateUtils.getToday(), 15)));
		repaymentPlanModifyRequestDataModel3.setAssetRecycleDate(DateUtils.format(DateUtils.addDays(DateUtils.getToday(), 25)));

		repaymentPlanModifyRequestDataModel1.setAssetPrincipal(new BigDecimal("35000.00"));
		repaymentPlanModifyRequestDataModel2.setAssetPrincipal(new BigDecimal("25000.00"));
		repaymentPlanModifyRequestDataModel3.setAssetPrincipal(new BigDecimal("20000.00"));

		List<RepaymentPlanModifyRequestDataModel> requestDataModels = new ArrayList<>();
		requestDataModels.add(repaymentPlanModifyRequestDataModel1);
		requestDataModels.add(repaymentPlanModifyRequestDataModel2);
		requestDataModels.add(repaymentPlanModifyRequestDataModel3);
		String requestData = JsonUtils.toJsonString(requestDataModels);

		RepaymentPlanModifyModel modifyModel = new RepaymentPlanModifyModel();
		modifyModel.setFn("FNTEST");
		modifyModel.setRequestNo("AAA");
		modifyModel.setUniqueId("e568793f-a44c-4362-9e78-0ce433131f3e");
		modifyModel.setContractNo("云信信2016-241-DK(428522112675736881)");
		modifyModel.setRequestReason("1");
		modifyModel.setRequestData(requestData);

		Contract contract = contractService.getContract(54340L);
		Integer oldActiveVersionNo = contract.getActiveVersionNo();
		try {
			assetSetApiHandler.modifyRepaymentPlan(contract, oldActiveVersionNo, modifyModel, "192.168.1.1");
		} catch (Exception e) {
			e.printStackTrace();
			//Assert.fail();
		}
	}

	@Test
	@Sql("classpath:test/yunxin/api/test_modify_repaymentPlan_3.sql")
	public void test_modify_repaymentPlan_graceDay() {
		RepaymentPlanModifyRequestDataModel repaymentPlanModifyRequestDataModel1 = new RepaymentPlanModifyRequestDataModel();
		RepaymentPlanModifyRequestDataModel repaymentPlanModifyRequestDataModel2 = new RepaymentPlanModifyRequestDataModel();
		RepaymentPlanModifyRequestDataModel repaymentPlanModifyRequestDataModel3 = new RepaymentPlanModifyRequestDataModel();

		repaymentPlanModifyRequestDataModel1.setAssetRecycleDate(DateUtils.format(DateUtils.addDays(DateUtils.getToday(), 5)));
		repaymentPlanModifyRequestDataModel2.setAssetRecycleDate(DateUtils.format(DateUtils.addDays(DateUtils.getToday(), 15)));
		repaymentPlanModifyRequestDataModel3.setAssetRecycleDate(DateUtils.format(DateUtils.addDays(DateUtils.getToday(), 25)));

		repaymentPlanModifyRequestDataModel1.setAssetPrincipal(new BigDecimal("35000.00"));
		repaymentPlanModifyRequestDataModel2.setAssetPrincipal(new BigDecimal("25000.00"));
		repaymentPlanModifyRequestDataModel3.setAssetPrincipal(new BigDecimal("20000.00"));

		List<RepaymentPlanModifyRequestDataModel> requestDataModels = new ArrayList<>();
		requestDataModels.add(repaymentPlanModifyRequestDataModel1);
		requestDataModels.add(repaymentPlanModifyRequestDataModel2);
		requestDataModels.add(repaymentPlanModifyRequestDataModel3);
		String requestData = JsonUtils.toJsonString(requestDataModels);

		RepaymentPlanModifyModel modifyModel = new RepaymentPlanModifyModel();
		modifyModel.setFn("FNTEST");
		modifyModel.setRequestNo("AAA");
		modifyModel.setUniqueId("e568793f-a44c-4362-9e78-0ce433131f3e");
		modifyModel.setContractNo("云信信2016-241-DK(428522112675736881)");
		modifyModel.setRequestReason("1");
		modifyModel.setRequestData(requestData);

		Contract contract = contractService.getContract(54340L);
		Integer oldActiveVersionNo = contract.getActiveVersionNo();
		try {
			assetSetApiHandler.modifyRepaymentPlan(contract, oldActiveVersionNo, modifyModel, "192.168.1.1");
		} catch (Exception e) {
			e.printStackTrace();
			//Assert.fail();
		}
	}

	/**
	 * 计划还款日期早于贷款合同开始日期
	 */
	@Test
	@Sql("classpath:test/yunxin/api/test_modify_repaymentPlan_wrongDate_1.sql")
	public void test_modify_repaymentPlan_wrongDate_1() {
		RepaymentPlanModifyRequestDataModel repaymentPlanModifyRequestDataModel = new RepaymentPlanModifyRequestDataModel();

		repaymentPlanModifyRequestDataModel.setAssetRecycleDate("2018-10-05");
		repaymentPlanModifyRequestDataModel.setAssetPrincipal(new BigDecimal("80000.00"));

		List<RepaymentPlanModifyRequestDataModel> requestDataModels = new ArrayList<>();
		requestDataModels.add(repaymentPlanModifyRequestDataModel);
		String requestData = JsonUtils.toJsonString(requestDataModels);

		RepaymentPlanModifyModel modifyModel = new RepaymentPlanModifyModel();
		modifyModel.setFn("FNTEST");
		modifyModel.setRequestNo("AAAbbb");
		modifyModel.setUniqueId("e568793f-a44c-4362-9e78-0ce433131f3e");
		modifyModel.setContractNo("云信信2016-241-DK(428522112675736881)");
		modifyModel.setRequestReason("1");
		modifyModel.setRequestData(requestData);

		Contract contract = contractService.getContract(54340L);
		Integer oldActiveVersionNo = contract.getActiveVersionNo();
		try {
			assetSetApiHandler.modifyRepaymentPlan(contract, oldActiveVersionNo, modifyModel, "192.168.1.1");
			Assert.fail();
		} catch (GlobalRuntimeException e) {
			Assert.assertEquals("计划还款日期不能早于贷款合同开始日期!", ApiMessageUtil.getMessage(e.getCode()));
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	/**
	 * 存在当日扣款成功或处理中的还款计划
	 */
	@Test
	@Sql("classpath:test/yunxin/api/test_modify_repaymentPlan_onProcessing.sql")
	public void test_modify_repaymentPlan_onProcessing() {
		RepaymentPlanModifyRequestDataModel repaymentPlanModifyRequestDataModel = new RepaymentPlanModifyRequestDataModel();

		repaymentPlanModifyRequestDataModel.setAssetRecycleDate("2018-12-30");
		repaymentPlanModifyRequestDataModel.setAssetPrincipal(new BigDecimal("80000.00"));

		List<RepaymentPlanModifyRequestDataModel> requestDataModels = new ArrayList<>();
		requestDataModels.add(repaymentPlanModifyRequestDataModel);
		String requestData = JsonUtils.toJsonString(requestDataModels);

		RepaymentPlanModifyModel modifyModel = new RepaymentPlanModifyModel();
		modifyModel.setFn("FNTEST");
		modifyModel.setRequestNo("AAAbbb");
		modifyModel.setUniqueId("e568793f-a44c-4362-9e78-0ce433131f3e");
		modifyModel.setContractNo("云信信2016-241-DK(428522112675736881)");
		modifyModel.setRequestReason("1");
		modifyModel.setRequestData(requestData);

		Contract contract = contractService.getContract(54340L);
		Integer oldActiveVersionNo = contract.getActiveVersionNo();
		try {
			assetSetApiHandler.modifyRepaymentPlan(contract, oldActiveVersionNo, modifyModel, "192.168.1.1");
			Assert.fail();
		} catch (GlobalRuntimeException e) {
			Assert.assertEquals("存在当日扣款成功或处理中的还款计划", e.getMessage());
		}
		//Assert.fail();
	}

	/**
	 * 计划还款日期排序错误
	 */
	@Test
	@Sql("classpath:test/yunxin/api/modify_repaymentPlan.sql")
	public void test_modify_repaymentPlan_wrongDate_3() {
		RepaymentPlanModifyRequestDataModel repaymentPlanModifyRequestDataModel1 = new RepaymentPlanModifyRequestDataModel();
		RepaymentPlanModifyRequestDataModel repaymentPlanModifyRequestDataModel2 = new RepaymentPlanModifyRequestDataModel();
		RepaymentPlanModifyRequestDataModel repaymentPlanModifyRequestDataModel3 = new RepaymentPlanModifyRequestDataModel();

		repaymentPlanModifyRequestDataModel1.setAssetRecycleDate(DateUtils.format(DateUtils.addDays(DateUtils.getToday(), 15)));
		repaymentPlanModifyRequestDataModel2.setAssetRecycleDate(DateUtils.format(DateUtils.addDays(DateUtils.getToday(), 5)));
		repaymentPlanModifyRequestDataModel3.setAssetRecycleDate(DateUtils.format(DateUtils.addDays(DateUtils.getToday(), 25)));

		repaymentPlanModifyRequestDataModel1.setAssetPrincipal(new BigDecimal("35000.00"));
		repaymentPlanModifyRequestDataModel2.setAssetPrincipal(new BigDecimal("25000.00"));
		repaymentPlanModifyRequestDataModel3.setAssetPrincipal(new BigDecimal("20000.00"));

		List<RepaymentPlanModifyRequestDataModel> requestDataModels = new ArrayList<>();
		requestDataModels.add(repaymentPlanModifyRequestDataModel1);
		requestDataModels.add(repaymentPlanModifyRequestDataModel2);
		requestDataModels.add(repaymentPlanModifyRequestDataModel3);
		String requestData = JsonUtils.toJsonString(requestDataModels);

		RepaymentPlanModifyModel modifyModel = new RepaymentPlanModifyModel();
		modifyModel.setFn("FNTEST");
		modifyModel.setRequestNo("AAAbbb");
		modifyModel.setUniqueId("e568793f-a44c-4362-9e78-0ce433131f3e");
		modifyModel.setContractNo("云信信2016-241-DK(428522112675736881)");
		modifyModel.setRequestReason("1");
		modifyModel.setRequestData(requestData);

		Contract contract = contractService.getContract(54340L);
		Integer oldActiveVersionNo = contract.getActiveVersionNo();
		try {
			assetSetApiHandler.modifyRepaymentPlan(contract, oldActiveVersionNo, modifyModel, "192.168.1.1");
			Assert.fail();
		} catch (GlobalRuntimeException e) {
			Assert.assertEquals("计划还款日期排序错误，需按计划还款日期递增!", ApiMessageUtil.getMessage(e.getCode()));
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	/**
	 * 计划本金总额错误
	 */
	@Test
	@Sql("classpath:test/yunxin/api/modify_repaymentPlan.sql")
	public void test_modify_repaymentPlan_wrongPrincipal() {
		RepaymentPlanModifyRequestDataModel repaymentPlanModifyRequestDataModel = new RepaymentPlanModifyRequestDataModel();

		repaymentPlanModifyRequestDataModel.setAssetRecycleDate(DateUtils.format(DateUtils.addDays(DateUtils.getToday(), +5)));
		repaymentPlanModifyRequestDataModel.setAssetPrincipal(new BigDecimal("20000.00"));

		List<RepaymentPlanModifyRequestDataModel> requestDataModels = new ArrayList<>();
		requestDataModels.add(repaymentPlanModifyRequestDataModel);
		String requestData = JsonUtils.toJsonString(requestDataModels);

		RepaymentPlanModifyModel modifyModel = new RepaymentPlanModifyModel();
		modifyModel.setFn("FNTEST");
		modifyModel.setRequestNo("AAAbbb");
		modifyModel.setUniqueId("e568793f-a44c-4362-9e78-0ce433131f3e");
		modifyModel.setContractNo("云信信2016-241-DK(428522112675736881)");
		modifyModel.setRequestReason("1");
		modifyModel.setRequestData(requestData);

		Contract contract = contractService.getContract(54340L);
		Integer oldActiveVersionNo = contract.getActiveVersionNo();
		try {
			assetSetApiHandler.modifyRepaymentPlan(contract, oldActiveVersionNo, modifyModel, "192.168.1.1");
			Assert.fail();
		} catch (GlobalRuntimeException e) {
			Assert.assertEquals("无效的计划本金总额!", ApiMessageUtil.getMessage(e.getCode()));
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	/**
	 * 信托合同不存在
	 */
	@Test
	@Sql("classpath:test/yunxin/api/modify_repaymentPlan_financialContract_notExist.sql")
	public void test_modify_repaymentPlan_financialContract_notExist() {
		RepaymentPlanModifyRequestDataModel repaymentPlanModifyRequestDataModel = new RepaymentPlanModifyRequestDataModel();

		repaymentPlanModifyRequestDataModel.setAssetRecycleDate(DateUtils.format(DateUtils.addDays(DateUtils.getToday(), +5)));
		repaymentPlanModifyRequestDataModel.setAssetPrincipal(new BigDecimal("80000.00"));

		List<RepaymentPlanModifyRequestDataModel> requestDataModels = new ArrayList<>();
		requestDataModels.add(repaymentPlanModifyRequestDataModel);
		String requestData = JsonUtils.toJsonString(requestDataModels);

		RepaymentPlanModifyModel modifyModel = new RepaymentPlanModifyModel();
		modifyModel.setFn("FNTEST");
		modifyModel.setRequestNo("AAA");
		modifyModel.setUniqueId("e568793f-a44c-4362-9e78-0ce433131f3e");
		modifyModel.setContractNo("云信信2016-241-DK(428522112675736881)");
		modifyModel.setRequestReason("1");
		modifyModel.setRequestData(requestData);

		Contract contract = contractService.getContract(54340L);
		Integer oldActiveVersionNo = contract.getActiveVersionNo();
		try {
			assetSetApiHandler.modifyRepaymentPlan(contract, oldActiveVersionNo, modifyModel, "192.168.1.1");
			Assert.fail();
		} catch (GlobalRuntimeException e) {
			Assert.assertEquals("信托合同不存在", ApiMessageUtil.getMessage(e.getCode()));
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	/**
	 * 贷款合同中不存在可变更的还款计划
	 */
	@Test
	@Sql("classpath:test/yunxin/api/modify_repaymentPlan_canUpdateAssetList_empty.sql")
	public void test_modify_repaymentPlan_canUpdateAssetList_empty() {
		RepaymentPlanModifyRequestDataModel repaymentPlanModifyRequestDataModel = new RepaymentPlanModifyRequestDataModel();

		repaymentPlanModifyRequestDataModel.setAssetRecycleDate(DateUtils.format(DateUtils.addDays(DateUtils.getToday(), +5)));
		repaymentPlanModifyRequestDataModel.setAssetPrincipal(new BigDecimal("80000.00"));

		List<RepaymentPlanModifyRequestDataModel> requestDataModels = new ArrayList<>();
		requestDataModels.add(repaymentPlanModifyRequestDataModel);
		String requestData = JsonUtils.toJsonString(requestDataModels);

		RepaymentPlanModifyModel modifyModel = new RepaymentPlanModifyModel();
		modifyModel.setFn("FNTEST");
		modifyModel.setRequestNo("AAAbbb");
		modifyModel.setUniqueId("e568793f-a44c-4362-9e78-0ce433131f3e");
		modifyModel.setContractNo("云信信2016-241-DK(428522112675736881)");
		modifyModel.setRequestReason("1");
		modifyModel.setRequestData(requestData);

		Contract contract = contractService.getContract(54340L);
		Integer oldActiveVersionNo = contract.getActiveVersionNo();
		try {
			assetSetApiHandler.modifyRepaymentPlan(contract, oldActiveVersionNo, modifyModel, "192.168.1.1");
			Assert.fail();
		} catch (GlobalRuntimeException e) {
			Assert.assertEquals("当前贷款合同无法变更!", ApiMessageUtil.getMessage(e.getCode()));
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}


	/**
	 * 贷款合同中所有还款计划均不可变更
	 */
	@Test
	@Sql("classpath:test/yunxin/api/test_modify_repaymentPlan_repeatAsset.sql")
	public void test_modify_repaymentPlan_repeatAsset() {
		RepaymentPlanModifyRequestDataModel repaymentPlanModifyRequestDataModel = new RepaymentPlanModifyRequestDataModel();

		repaymentPlanModifyRequestDataModel.setAssetRecycleDate(DateUtils.format(DateUtils.parseDate("2018-11-10")));
		repaymentPlanModifyRequestDataModel.setAssetPrincipal(new BigDecimal("80000.00"));

		List<RepaymentPlanModifyRequestDataModel> requestDataModels = new ArrayList<>();
		requestDataModels.add(repaymentPlanModifyRequestDataModel);

		repaymentPlanModifyRequestDataModel = new RepaymentPlanModifyRequestDataModel();

		repaymentPlanModifyRequestDataModel.setAssetRecycleDate(DateUtils.format(DateUtils.parseDate("2018-11-10")));
		repaymentPlanModifyRequestDataModel.setAssetPrincipal(new BigDecimal("80000.00"));
		requestDataModels.add(repaymentPlanModifyRequestDataModel);

		String requestData = JsonUtils.toJsonString(requestDataModels);

		RepaymentPlanModifyModel modifyModel = new RepaymentPlanModifyModel();
		modifyModel.setFn("FNTEST");
		modifyModel.setRequestNo("AAAbbb");
		modifyModel.setUniqueId("e568793f-a44c-4362-9e78-0ce433131f3e");
		modifyModel.setContractNo("云信信2016-241-DK(428522112675736881)");
		modifyModel.setRequestReason("1");
		modifyModel.setRequestData(requestData);

		Contract contract = contractService.getContract(54340L);
		Integer oldActiveVersionNo = contract.getActiveVersionNo();
		try {
			assetSetApiHandler.modifyRepaymentPlan(contract, oldActiveVersionNo, modifyModel, "192.168.1.1");
			Assert.fail();
		} catch (GlobalRuntimeException e) {
			Assert.assertEquals("与原计划一致，不予变更!", ApiMessageUtil.getMessage(e.getCode()));
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	/**
	 * 贷款合同中存在可变更的还款计划
	 */
	@Test
	@Sql("classpath:test/yunxin/api/test_modify_repaymentPlan_repeatAsset2.sql")
	public void test_modify_repaymentPlan_repeatAsset_2() {
		RepaymentPlanModifyRequestDataModel repaymentPlanModifyRequestDataModel = new RepaymentPlanModifyRequestDataModel();

		repaymentPlanModifyRequestDataModel.setAssetRecycleDate(DateUtils.format(DateUtils.parseDate("2018-11-10")));
		repaymentPlanModifyRequestDataModel.setAssetPrincipal(new BigDecimal("80000.00"));

		List<RepaymentPlanModifyRequestDataModel> requestDataModels = new ArrayList<>();
		requestDataModels.add(repaymentPlanModifyRequestDataModel);

		repaymentPlanModifyRequestDataModel = new RepaymentPlanModifyRequestDataModel();

		repaymentPlanModifyRequestDataModel.setAssetRecycleDate(DateUtils.format(DateUtils.parseDate("2018-12-10")));
		repaymentPlanModifyRequestDataModel.setAssetPrincipal(new BigDecimal("80000.00"));

		requestDataModels.add(repaymentPlanModifyRequestDataModel);

		String requestData = JsonUtils.toJsonString(requestDataModels);

		RepaymentPlanModifyModel modifyModel = new RepaymentPlanModifyModel();
		modifyModel.setFn("FNTEST");
		modifyModel.setRequestNo("AAAbbb");
		modifyModel.setUniqueId("e568793f-a44c-4362-9e78-0ce433131f3e");
		modifyModel.setContractNo("云信信2016-241-DK(428522112675736881)");
		modifyModel.setRequestReason("1");
		modifyModel.setRequestData(requestData);

		Contract contract = contractService.getContract(54340L);
		Integer oldActiveVersionNo = contract.getActiveVersionNo();
		try {
			assetSetApiHandler.modifyRepaymentPlan(contract, oldActiveVersionNo, modifyModel, "192.168.1.1");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 商户还款计划编号重复 repayScheduleNoMD5值重复异常
	 */
	@Test
	@Sql("classpath:test/yunxin/api/test_modify_repaymentPlan_repeat_outer_repayment_plan_no.sql")
	public void test_modify_repaymentPlan_repeat_outer_repayment_plan_no() {
		RepaymentPlanModifyRequestDataModel repaymentPlanModifyRequestDataModel1 = new RepaymentPlanModifyRequestDataModel();
		RepaymentPlanModifyRequestDataModel repaymentPlanModifyRequestDataModel2 = new RepaymentPlanModifyRequestDataModel();
		RepaymentPlanModifyRequestDataModel repaymentPlanModifyRequestDataModel3 = new RepaymentPlanModifyRequestDataModel();

		repaymentPlanModifyRequestDataModel1.setAssetRecycleDate(DateUtils.format(DateUtils.addDays(DateUtils.getToday(), 5)));
		repaymentPlanModifyRequestDataModel2.setAssetRecycleDate(DateUtils.format(DateUtils.addDays(DateUtils.getToday(), 15)));
		repaymentPlanModifyRequestDataModel3.setAssetRecycleDate(DateUtils.format(DateUtils.addDays(DateUtils.getToday(), 25)));

		repaymentPlanModifyRequestDataModel1.setAssetPrincipal(new BigDecimal("35000.00"));
		repaymentPlanModifyRequestDataModel2.setAssetPrincipal(new BigDecimal("25000.00"));
		repaymentPlanModifyRequestDataModel3.setAssetPrincipal(new BigDecimal("20000.00"));

		//repayScheduleNo
		repaymentPlanModifyRequestDataModel1.setRepayScheduleNo("outer1");
		repaymentPlanModifyRequestDataModel2.setRepayScheduleNo("outer2");
		repaymentPlanModifyRequestDataModel3.setRepayScheduleNo("outer3");

		List<RepaymentPlanModifyRequestDataModel> requestDataModels = new ArrayList<>();
		requestDataModels.add(repaymentPlanModifyRequestDataModel1);
		requestDataModels.add(repaymentPlanModifyRequestDataModel2);
		requestDataModels.add(repaymentPlanModifyRequestDataModel3);
		String requestData = JsonUtils.toJsonString(requestDataModels);

		RepaymentPlanModifyModel modifyModel = new RepaymentPlanModifyModel();
		modifyModel.setFn("FNTEST");
		modifyModel.setRequestNo("AAA");
		modifyModel.setUniqueId("e568793f-a44c-4362-9e78-0ce433131f3e");
		modifyModel.setContractNo("云信信2016-241-DK(428522112675736881)");
		modifyModel.setRequestReason("1");
		modifyModel.setRequestData(requestData);

		Contract contract = contractService.getContract(54340L);
		Integer oldActiveVersionNo = contract.getActiveVersionNo();
		try {
//			String repayScheduleNo4MD5 = repaymentPlanService.getRepayScheduleNoMD5("G32000", "outer1");
//			System.out.println(repayScheduleNo4MD5);
			assetSetApiHandler.modifyRepaymentPlan(contract, oldActiveVersionNo, modifyModel, "127.0.0.1");
			Assert.fail();
		} catch (GlobalRuntimeException e) {
			Assert.assertEquals("商户还款计划编号重复", e.getMessage());
		}
	}

	/*************** test modifyRepaymentPlan end ***************/
}
