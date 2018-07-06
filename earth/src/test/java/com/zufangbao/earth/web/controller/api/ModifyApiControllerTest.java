package com.zufangbao.earth.web.controller.api;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.transaction.Transactional;

import org.junit.Assert;
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

import com.demo2do.core.entity.Result;
import com.demo2do.core.utils.DateUtils;
import com.demo2do.core.utils.JsonUtils;
import com.demo2do.core.web.resolver.Page;
import com.zufangbao.earth.yunxin.api.controller.ModifyApiController;
import com.zufangbao.sun.api.model.modify.ImportAssetPackageContent;
import com.zufangbao.sun.api.model.modify.ImportAssetPackageRequestModel;
import com.zufangbao.sun.api.model.modify.ModifyOverDueFeeRequestModel;
import com.zufangbao.sun.entity.contract.Contract;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.service.ContractService;
import com.zufangbao.sun.yunxin.entity.AssetSet;
import com.zufangbao.sun.yunxin.entity.RepaymentPlanExtraData;
import com.zufangbao.sun.yunxin.entity.model.api.ContractDetail;
import com.zufangbao.sun.yunxin.entity.model.api.ImportRepaymentPlanDetail;
import com.zufangbao.sun.yunxin.service.RepaymentPlanExtraDataService;
import com.zufangbao.sun.yunxin.service.RepaymentPlanService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {

		"classpath:/local/applicationContext-*.xml", "classpath:/DispatcherServlet.xml" })
@TransactionConfiguration(defaultRollback = false)
@Transactional
@WebAppConfiguration(value = "webapp")
public class ModifyApiControllerTest extends AbstractTransactionalJUnit4SpringContextTests {

	@Autowired
	private ModifyApiController modifyApiController;

	@Autowired
	private RepaymentPlanService repaymentPlanService;

	@Autowired
	private ContractService contractService;

	@Autowired
	private RepaymentPlanExtraDataService repaymentPlanExtraDataService;

	@Test
	@Sql("classpath:test/yunxin/assetPackage/testImportAssetPackageApi.sql")
	public void testImportAssetPackage() {

		ImportAssetPackageContent importAssetPackageContent = new ImportAssetPackageContent();
		importAssetPackageContent.setThisBatchContractsTotalNumber(1);
		importAssetPackageContent.setThisBatchContractsTotalAmount("2000.00");
		importAssetPackageContent.setFinancialProductCode("G0000000");

		List<ContractDetail> contracts = new ArrayList<ContractDetail>();
		ContractDetail contractDetail = new ContractDetail();
		contractDetail.setUniqueId("34567890");
		contractDetail.setLoanContractNo("contractNo1");
		contractDetail.setLoanCustomerNo("customerNo1");
		contractDetail.setLoanCustomerName("郑航波");
		contractDetail.setSubjectMatterassetNo("234567");
		contractDetail.setIDCardNo("330683199403062411");
		contractDetail.setBankCode("C10102");
		contractDetail.setBankOfTheProvince("330000");
		contractDetail.setBankOfTheCity("330300");
		contractDetail.setRepaymentAccountNo("23456787654323456");
		contractDetail.setLoanTotalAmount("2000.00");
		contractDetail.setLoanPeriods(1);
		contractDetail.setEffectDate("2016-8-1");
		contractDetail.setExpiryDate("2099-01-01");
		contractDetail.setLoanRates("0.156");
		contractDetail.setInterestRateCycle(1);
		contractDetail.setPenalty("0.0005");
		contractDetail.setRepaymentWay(1);

		List<ImportRepaymentPlanDetail> repaymentPlanDetails = new ArrayList<ImportRepaymentPlanDetail>();

		ImportRepaymentPlanDetail repaymentPlanDetail1 = new ImportRepaymentPlanDetail();
		repaymentPlanDetail1.setRepaymentPrincipal("2000.00");
		repaymentPlanDetail1.setRepaymentInterest("20.00");
		repaymentPlanDetail1.setRepaymentDate("2017-11-09");
		repaymentPlanDetail1.setOtheFee("0.00");
		repaymentPlanDetail1.setLoanServiceFee("0.00");
		repaymentPlanDetail1.setTechMaintenanceFee("0.00");
		repaymentPlanDetail1.setRepayScheduleNo("outerRepaymentPlanNo1");
		repaymentPlanDetails.add(repaymentPlanDetail1);

//		ImportRepaymentPlanDetail repaymentPlanDetail2 = new ImportRepaymentPlanDetail();
//		repaymentPlanDetail2.setRepaymentPrincipal("2000.00");
//		repaymentPlanDetail2.setRepaymentInterest("20.00");
//		repaymentPlanDetail2.setRepaymentDate("2016-10-04");
//		repaymentPlanDetail2.setOtheFee("0.00");
//		repaymentPlanDetail2.setLoanServiceFee("0.00");
//		repaymentPlanDetail2.setTechMaintenanceFee("0.00");
////		repaymentPlanDetail2.setRepayScheduleNo("outerRepaymentPlanNo2");
//		repaymentPlanDetails.add(repaymentPlanDetail2);
		contractDetail.setRepaymentPlanDetails(repaymentPlanDetails);

		contracts.add(contractDetail);
		importAssetPackageContent.setContractDetails(contracts);

		ImportAssetPackageRequestModel requestModel = new ImportAssetPackageRequestModel();
		requestModel.setFn("200004");
		requestModel.setRequestNo("13456789");
		requestModel.setImportAssetPackageContent(JsonUtils.toJsonString(importAssetPackageContent));
		// System.out.println(JsonUtils.toJsonString(importAssetPackageContent));
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();

		String result = modifyApiController.importAssetPackage(requestModel, request, response);
		// validate
		Map<String, Object> resultMap = JsonUtils.parse(result);
		Assert.assertEquals(resultMap.get("code").toString(), "0");
		Assert.assertEquals(resultMap.get("message").toString(), "成功!");
		FinancialContract financialContract = new FinancialContract();
		financialContract.setFinancialContractUuid("uuid");
		Page page = new Page(1, 1);
		Contract contract = contractService.getContractsByFinancialContract(financialContract, page).get(0);
		Assert.assertEquals("contractNo1", contract.getContractNo());
		List<AssetSet> assetSets = repaymentPlanService.getAllRepaymentPlanList(contract);
		Assert.assertEquals(repaymentPlanDetails.size(), assetSets.size());
//		for (AssetSet assetSet : assetSets) {
//			if(assetSet.getCurrentPeriod()==1){
//				Assert.assertNotNull(assetSet.getRepayScheduleNo());
//				RepaymentPlanExtraData repaymentPlanExtraData = repaymentPlanExtraDataService
//						.getRepaymentPlanExtraDataByAssetUuid(assetSet.getAssetUuid());
//				Assert.assertNotNull(repaymentPlanExtraData);
//			}
//		}
	}

	@Test
	@Sql("classpath:test/yunxin/api/testModifyOverDueFee.sql")
	public void testModifyOverDueFee() {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();

		// 测试requestNo为Null
		ModifyOverDueFeeRequestModel model = new ModifyOverDueFeeRequestModel();
		model.setRequestNo(null);
		String responseString = modifyApiController.modifyOverDueFee(model, request, response);
		Result result = JsonUtils.parse(responseString, Result.class);
		Assert.assertEquals("请求唯一标识［requestNo］，不能为空！", result.getMessage());

		// 测试ModifyOverDueFeeDetail为空
		ModifyOverDueFeeRequestModel model1 = new ModifyOverDueFeeRequestModel();
		model1.setRequestNo(UUID.randomUUID().toString());
		String responseString1 = modifyApiController.modifyOverDueFee(model1, request, response);
		Result result1 = JsonUtils.parse(responseString1, Result.class);
		Assert.assertEquals("逾期费用明细解析失败！！！", result1.getMessage());

		// 请求唯一标识为空
		String modifyOverDueFeeDetails2 = "[" + "{" + "'contractUniqueId': ''," + "'lateFee': '100.00',"
				+ "'lateOtherCost': '0.00'," + "'latePenalty': '100.00'," + "'overDueFeeCalcDate': '"
				+ DateUtils.format(new Date()) + "'," + "'penaltyFee': '90.00',"
				+ "'repaymentPlanNo': 'ZC2730FAE4092E0A6E'," + "'totalOverdueFee': '290.00'" + "}" + "]";
		model1.setModifyOverDueFeeDetails(modifyOverDueFeeDetails2);
		String responseString2 = modifyApiController.modifyOverDueFee(model1, request, response);
		Result result2 = JsonUtils.parse(responseString2, Result.class);
		Assert.assertEquals("请求唯一标识［uniqueId］,不能为空！", result2.getMessage());

		// 还款计划为空
		String modifyOverDueFeeDetails3 = "[" + "{" + "'contractUniqueId': '123456'," + "'lateFee': '100.00',"
				+ "'lateOtherCost': '0.00'," + "'latePenalty': '100.00'," + "'overDueFeeCalcDate': '"
				+ DateUtils.format(new Date()) + "'," + "'penaltyFee': '90.00'," + "'repaymentPlanNo': '','repayScheduleNo': '',"
				+ "'totalOverdueFee': '290.00'" + "}" + "]";
		model1.setModifyOverDueFeeDetails(modifyOverDueFeeDetails3);
		String responseString3 = modifyApiController.modifyOverDueFee(model1, request, response);
		Result result3 = JsonUtils.parse(responseString3, Result.class);
		Assert.assertEquals("还款计划编号［repaymentPlanNo］，商户还款计划编号［repayScheduleNo］，不能都为空！", result3.getMessage());

		// 请求唯一标识不能为空
		String modifyOverDueFeeDetails4 = "[" + "{" + "'contractUniqueId': '123456'," + "'lateFee': '100.00',"
				+ "'lateOtherCost': '0.00'," + "'latePenalty': '100.00'," + "'overDueFeeCalcDate': '',"
				+ "'penaltyFee': '90.00'," + "'repaymentPlanNo': 'ZC2730FAE4092E0A6E'," + "'totalOverdueFee': '290.00'"
				+ "}" + "]";
		model1.setModifyOverDueFeeDetails(modifyOverDueFeeDetails4);
		String responseString4 = modifyApiController.modifyOverDueFee(model1, request, response);
		Result result4 = JsonUtils.parse(responseString4, Result.class);
		Assert.assertEquals("请求唯一标识［overDueFeeCalcDate］，不能为空！", result4.getMessage());

		// 日期有误
		String modifyOverDueFeeDetails5 = "[" + "{" + "'contractUniqueId': '123456'," + "'lateFee': '100.00',"
				+ "'lateOtherCost': '0.00'," + "'latePenalty': '100.00'," + "'overDueFeeCalcDate': '2016-09-01',"
				+ "'penaltyFee': '90.00'," + "'repaymentPlanNo': 'ZC2730FAE4092E0A6E'," + "'totalOverdueFee': '290.00'"
				+ "}" + "]";
		model1.setModifyOverDueFeeDetails(modifyOverDueFeeDetails5);
		String responseString5 = modifyApiController.modifyOverDueFee(model1, request, response);
		Result result5 = JsonUtils.parse(responseString5, Result.class);
		Assert.assertEquals("请求唯一标识［overDueFeeCalcDate］,日期有误", result5.getMessage());

		String modifyOverDueFeeDetails6 = "[" + "{" + "'contractUniqueId': '1234567890'," + "'lateFee': '300.00',"
				+ "'lateOtherCost': '200.00'," + "'latePenalty': '100.00'," + "'overDueFeeCalcDate': '"
				+ DateUtils.format(new Date()) + "'," + "'penaltyFee': '90.00',"
				+ "'repaymentPlanNo': 'ZC27375ACFF4234805'," + "'totalOverdueFee': '690.00'" + "}" + "]";
		model1.setModifyOverDueFeeDetails(modifyOverDueFeeDetails6);
		String responseString6 = modifyApiController.modifyOverDueFee(model1, request, response);
		Result result6 = JsonUtils.parse(responseString6, Result.class);
		 Assert.assertEquals("成功!",result6.getMessage());

	}

	@Test
	@Sql("classpath:test/yunxin/api/testModifyOverDueFee_overDueFeeCalcAfterAssetRecycleDate.sql")
	public void testModifyOverDueFee2() {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();

		ModifyOverDueFeeRequestModel model = new ModifyOverDueFeeRequestModel();
		model.setRequestNo(UUID.randomUUID().toString());

		String modifyOverDueFeeDetails4 = "[" + "{" + "'contractUniqueId': '123456'," + "'lateFee': '100.00',"
				+ "'lateOtherCost': '0.00'," + "'latePenalty': '100.00'," + "'overDueFeeCalcDate': '"
				+ DateUtils.format(new Date()) + "'," + "'penaltyFee': '90.00',"
				+ "'repaymentPlanNo': 'ZC2730FAE4092E0A6E'," + "'totalOverdueFee': '290.00'" + "}" + "]";

		model.setModifyOverDueFeeDetails(modifyOverDueFeeDetails4);

		String responseString4 = modifyApiController.modifyOverDueFee(model, request, response);
		Result result4 = JsonUtils.parse(responseString4, Result.class);
		Assert.assertEquals("23107", result4.getCode());

		ModifyOverDueFeeRequestModel model2 = new ModifyOverDueFeeRequestModel();
		model2.setRequestNo(UUID.randomUUID().toString());
		String modifyOverDueFeeDetails = "[" + "{" + "'contractUniqueId': '123456'," + "'lateFee': '100.00',"
				+ "'lateOtherCost': '0.00'," + "'latePenalty': '100.00'," + "'overDueFeeCalcDate': '"
				+ DateUtils.format(DateUtils.addDays(new Date(), 1)) + "'," + "'penaltyFee': '90.00',"
				+ "'repaymentPlanNo': 'ZC2730FAE4092E0A6E'," + "'totalOverdueFee': '290.00'" + "}" + "]";

		model2.setModifyOverDueFeeDetails(modifyOverDueFeeDetails);

		String responseString = modifyApiController.modifyOverDueFee(model2, request, response);
		Result result = JsonUtils.parse(responseString, Result.class);
		Assert.assertEquals("23107", result.getCode());
	}

	@Test
	@Sql("classpath:test/yunxin/api/testModifyOverDueFee_overDueFeeCalcAfterAssetRecycleDate2.sql")
	public void testModifyOverDueFee3() {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();

		ModifyOverDueFeeRequestModel model = new ModifyOverDueFeeRequestModel();
		model.setRequestNo(UUID.randomUUID().toString());

		String modifyOverDueFeeDetails4 = "[" + "{" + "'contractUniqueId': '123456'," + "'lateFee': '100.00',"
				+ "'lateOtherCost': '0.00'," + "'latePenalty': '100.00'," + "'overDueFeeCalcDate': '"
				+ DateUtils.format(DateUtils.addDays(new Date(), 1)) + "'," + "'penaltyFee': '90.00',"
				+ "'repaymentPlanNo': 'ZC2730FAE4092E0A6E'," + "'totalOverdueFee': '290.00'" + "}" + "]";

		model.setModifyOverDueFeeDetails(modifyOverDueFeeDetails4);

		String responseString4 = modifyApiController.modifyOverDueFee(model, request, response);
		Result result4 = JsonUtils.parse(responseString4, Result.class);
		Assert.assertEquals("成功!", result4.getMessage());
	}

	// 校验是否存在支付中的还款计划
	@Test
	@Sql("classpath:test/yunxin/api/testModifyOverDueFee_paymenting_asset.sql")
	public void testModifyOverDueFee_paymenting_assetset() {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();

		ModifyOverDueFeeRequestModel model = new ModifyOverDueFeeRequestModel();
		model.setRequestNo(UUID.randomUUID().toString());

		String modifyOverDueFeeDetails4 = "[" + "{" + "'contractUniqueId': '123456'," + "'lateFee': '100.00',"
				+ "'lateOtherCost': '0.00'," + "'latePenalty': '100.00'," + "'overDueFeeCalcDate': '"
				+ DateUtils.format(DateUtils.addDays(new Date(), 1)) + "'," + "'penaltyFee': '90.00',"
				+ "'repaymentPlanNo': 'ZC2730FAE4092E0A6E'," + "'totalOverdueFee': '290.00'" + "}" + "]";

		model.setModifyOverDueFeeDetails(modifyOverDueFeeDetails4);

		String responseString4 = modifyApiController.modifyOverDueFee(model, request, response);
		Result result4 = JsonUtils.parse(responseString4, Result.class);
		Assert.assertEquals("逾期费用修改值必须大于等于相对应的实收金额和支付中逾期金额之和", result4.getMessage());
	}
}
