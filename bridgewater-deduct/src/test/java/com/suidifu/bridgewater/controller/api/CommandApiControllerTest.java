package com.suidifu.bridgewater.controller.api;

import java.util.UUID;

import javax.transaction.Transactional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.demo2do.core.utils.JsonUtils;
import com.suidifu.bridgewater.controller.api.deduct.CommandApiDeductController;
import com.suidifu.bridgewater.controller.post.BaseApiTestPost;
import com.zufangbao.gluon.api.earth.v3.model.ApiResult;
import com.zufangbao.gluon.exception.ApiException;
import com.zufangbao.gluon.spec.earth.v3.ApiResponseCode;
import com.zufangbao.sun.api.model.deduct.DeductCommandRequestModel;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:/context/applicationContext-*.xml",
		"classpath:/local/applicationContext-*.xml",
		"classpath:/DispatcherServlet.xml" })
@Transactional
public class CommandApiControllerTest extends BaseApiTestPost{
	
	private MockHttpServletRequest request = new MockHttpServletRequest();
	
	@Autowired
	private CommandApiDeductController commandApiDeductController;

	
	@Before
	public void setUp() {
	}
	

	
	@Test
	@Sql("classpath:test/yunxin/api/testCommandDeductAmountIsNegativeNumber.sql")
	public void testCommandDeductAmountIsNegativeNumber() {
		DeductCommandRequestModel commandModel = new DeductCommandRequestModel();
		commandModel.setRequestNo(UUID.randomUUID().toString());
		commandModel.setDeductId(UUID.randomUUID().toString());
		commandModel.setContractNo("629测试(ZQ2016002000001)");
		commandModel.setAmount("-100.00");
		commandModel.setApiCalledTime("2016-03-05");
		commandModel.setFinancialProductCode("G00001");
		commandModel.setRepaymentDetails("[{\"loanFee\":0.00,\"otherFee\":0.00,\"repaymentAmount\":100.00,\"repaymentInterest\":100.00,\"repaymentPlanNo\":\"ZC27375ACFF4234805\",\"repaymentPrincipal\":0.00,\"techFee\":0.00,\"totalOverdueFee\":0.00}]");
		commandModel.setRepaymentType(0);
		MockHttpServletResponse response = new MockHttpServletResponse();
		String jsonResult = commandApiDeductController.commandDeduct(request, response, commandModel);
		ApiResult apiResult = JsonUtils.parse(jsonResult, ApiResult.class);
		Assert.assertEquals(ApiResponseCode.INVALID_PARAMS, apiResult.getCode());
		Assert.assertEquals("扣款金额格式有误！！！", apiResult.getMessage());
	}
	
	@Test
	@Sql("classpath:test/yunxin/api/testCommandDeductAmountRepaymentTypeIsNull.sql")
	public void testCommandDeductAmountRepaymentTypeIsNull() {
		DeductCommandRequestModel commandModel = new DeductCommandRequestModel();
		commandModel.setRequestNo(UUID.randomUUID().toString());
		commandModel.setDeductId(UUID.randomUUID().toString());
		commandModel.setContractNo("629测试(ZQ2016002000001)");
		commandModel.setAmount("100.00");
		commandModel.setApiCalledTime("2016-03-05");
		commandModel.setFinancialProductCode("G00001");
		commandModel.setRepaymentDetails("[{\"loanFee\":0.00,\"otherFee\":0.00,\"repaymentAmount\":100.00,\"repaymentInterest\":100.00,\"repaymentPlanNo\":\"ZC27375ACFF4234805\",\"repaymentPrincipal\":0.00,\"techFee\":0.00,'overDueFeeDetail':{'penaltyFee':0.00,'latePenalty':0.00,'lateFee':0.00,'lateOtherCost':0.00,'totalOverdueFee':0.00}]");
		commandModel.setRepaymentType(3);
		MockHttpServletResponse response = new MockHttpServletResponse();
		String jsonResult = commandApiDeductController.commandDeduct(request, response, commandModel);
		ApiResult apiResult = JsonUtils.parse(jsonResult, ApiResult.class);
		Assert.assertEquals(ApiResponseCode.INVALID_PARAMS, apiResult.getCode());
		Assert.assertEquals("还款类型格式错误！！！", apiResult.getMessage());
	}
	
	
	
	@Test
	@Sql("classpath:test/yunxin/api/testCommandDeductAmountNoRepaymentDetails.sql")
	public void testCommandDeductAmountNoRepaymentDetails() {
		DeductCommandRequestModel commandModel = new DeductCommandRequestModel();
		commandModel.setRequestNo(UUID.randomUUID().toString());
		commandModel.setDeductId(UUID.randomUUID().toString());
		commandModel.setContractNo("629测试(ZQ2016002000001)");
		commandModel.setAmount("100.00");
		commandModel.setApiCalledTime("2016-03-05");
		commandModel.setFinancialProductCode("G00001");
		commandModel.setRepaymentType(0);
		MockHttpServletResponse response = new MockHttpServletResponse();
		String jsonResult = commandApiDeductController.commandDeduct(request, response, commandModel);
		ApiResult apiResult = JsonUtils.parse(jsonResult, ApiResult.class);
		Assert.assertEquals(ApiResponseCode.INVALID_PARAMS, apiResult.getCode());
		Assert.assertEquals("还款明细不能为空！！！", apiResult.getMessage());
	}
	
	@Test
	@Sql("classpath:test/yunxin/api/testCommandDeductAmountDoubleRepaymentNo.sql")
	public void testCommandDeductAmountDoubleRepaymentNo() {
		DeductCommandRequestModel commandModel = new DeductCommandRequestModel();
		commandModel.setRequestNo(UUID.randomUUID().toString());
		commandModel.setDeductId(UUID.randomUUID().toString());
		commandModel.setContractNo("629测试(ZQ2016002000001)");
		commandModel.setAmount("100.00");
		commandModel.setApiCalledTime("2016-03-05");
		commandModel.setFinancialProductCode("G00001");
		commandModel.setRepaymentType(0);
		commandModel.setRepaymentDetails("[{\"loanFee\":0.00,\"otherFee\":0.00,\"repaymentAmount\":100.00,\"repaymentInterest\":100.00,\"repaymentPlanNo\":\"ZC27375ACFF4234805\",\"repaymentPrincipal\":0.00,\"techFee\":0.00,'overDueFeeDetail':{'penaltyFee':0.00,'latePenalty':0.00,'lateFee':0.00,'lateOtherCost':0.00,'totalOverdueFee':0.00}},{\"loanFee\":0.00,\"otherFee\":0.00,\"repaymentAmount\":100.00,\"repaymentInterest\":100.00,\"repaymentPlanNo\":\"ZC27375ACFF4234805\",\"repaymentPrincipal\":0.00,\"techFee\":0.00,'overDueFeeDetail':{'penaltyFee':0.00,'latePenalty':0.00,'lateFee':0.00,'lateOtherCost':0.00,'totalOverdueFee':0.00}}]");
		MockHttpServletResponse response = new MockHttpServletResponse();
		String jsonResult = commandApiDeductController.commandDeduct(request, response, commandModel);
		ApiResult apiResult = JsonUtils.parse(jsonResult, ApiResult.class);
		Assert.assertEquals(ApiResponseCode.INVALID_PARAMS, apiResult.getCode());
		Assert.assertEquals("还款明细中有重复还款计划编号！！！", apiResult.getMessage());
	}
	
	@Test
	@Sql("classpath:test/yunxin/api/testCommandDeductAmountDoubleRepaymentNo.sql")
	public void testCommandDeductAmountError() {
		DeductCommandRequestModel commandModel = new DeductCommandRequestModel();
		commandModel.setRequestNo(UUID.randomUUID().toString());
		commandModel.setDeductId(UUID.randomUUID().toString());
		commandModel.setContractNo("629测试(ZQ2016002000001)");
		commandModel.setAmount("100.00");
		commandModel.setApiCalledTime("2016-03-05");
		commandModel.setFinancialProductCode("G00001");
		commandModel.setRepaymentType(0);
		commandModel.setRepaymentDetails("[{\"loanFee\":0.00,\"otherFee\":-10.00,\"repaymentAmount\":100.00,\"repaymentInterest\":100.00,\"repaymentPlanNo\":\"ZC27375ACFF4234805\",\"repaymentPrincipal\":0.00,\"techFee\":0.00,\"totalOverdueFee\":0.00}]");
		MockHttpServletResponse response = new MockHttpServletResponse();
		String jsonResult = commandApiDeductController.commandDeduct(request, response, commandModel);
		ApiResult apiResult = JsonUtils.parse(jsonResult, ApiResult.class);
		Assert.assertEquals(ApiResponseCode.INVALID_PARAMS, apiResult.getCode());
		Assert.assertEquals("还款计划明细金额不能为空！！！", apiResult.getMessage());
	}
	
	
	@Test
	@Sql("classpath:test/yunxin/api/testCommandDeductNoContract.sql")
	public void testCommandDeductNoContract() {
		DeductCommandRequestModel commandModel = new DeductCommandRequestModel();
		commandModel.setRequestNo(UUID.randomUUID().toString());
		commandModel.setDeductId("345679");
		commandModel.setContractNo("9测试(ZQ2016002000001)");
		commandModel.setAmount("100.00");
		commandModel.setApiCalledTime("2016-03-05");
		commandModel.setFinancialProductCode("G00001");
		commandModel.setRepaymentType(0);
		commandModel.setRepaymentDetails("[{\"loanFee\":0.00,\"otherFee\":0.00,\"repaymentAmount\":100.00,\"repaymentInterest\":100.00,\"repaymentPlanNo\":\"ZC27375ACFF4234805\",\"repaymentPrincipal\":0.00,\"techFee\":0.00,'overDueFeeDetail':{'penaltyFee':0.00,'latePenalty':0.00,'lateFee':0.00,'lateOtherCost':0.00,'totalOverdueFee':0.00}}]");
		MockHttpServletResponse response = new MockHttpServletResponse();
		String jsonResult = commandApiDeductController.commandDeduct(request, response, commandModel);
		ApiResult apiResult = JsonUtils.parse(jsonResult, ApiResult.class);
		Assert.assertEquals(ApiResponseCode.CONTRACT_NOT_EXIST, apiResult.getCode());
	}
	
	@Test
	@Sql("classpath:test/yunxin/api/testCommandDeductNoContract.sql")
	public void testCommandDeductErrorFinancialContract() {
		DeductCommandRequestModel commandModel = new DeductCommandRequestModel();
		commandModel.setRequestNo(UUID.randomUUID().toString());
		commandModel.setDeductId("345679");
		commandModel.setContractNo("629测试(ZQ2016002000001)");
		commandModel.setAmount("100.00");
		commandModel.setApiCalledTime("2016-03-05");
		commandModel.setFinancialProductCode("G00000001");
		commandModel.setRepaymentType(0);
		commandModel.setRepaymentDetails("[{\"loanFee\":0.00,\"otherFee\":0.00,\"repaymentAmount\":100.00,\"repaymentInterest\":100.00,\"repaymentPlanNo\":\"ZC27375ACFF4234805\",\"repaymentPrincipal\":0.00,\"techFee\":0.00,'overDueFeeDetail':{'penaltyFee':0.00,'latePenalty':0.00,'lateFee':0.00,'lateOtherCost':0.00,'totalOverdueFee':0.00}]");
		MockHttpServletResponse response = new MockHttpServletResponse();
		try {
			String jsonResult = commandApiDeductController.commandDeduct(request, response, commandModel);
		} catch (ApiException e) {
			// TODO: handle exception
			Assert.assertEquals(ApiResponseCode.FINANCIAL_PRODUCT_CODE_ERROR, e.getCode());
		}
	}

	
	
	@Test
	@Sql("classpath:test/yunxin/api/testConcurrentDeductAmount.sql")
	public void testConcurrentDeductAmount(){
		DeductCommandRequestModel commandModel = new DeductCommandRequestModel();
		commandModel.setRequestNo(UUID.randomUUID().toString());
		commandModel.setDeductId(UUID.randomUUID().toString());
		commandModel.setContractNo("629测试(ZQ2016002000001)");
		commandModel.setAmount("100");
		commandModel.setApiCalledTime("2016-03-05");
		commandModel.setFinancialProductCode("G31700");
		commandModel.setRepaymentDetails("[{\"loanFee\":0.00,\"otherFee\":0.00,\"repaymentAmount\":100.00,\"repaymentInterest\":100.00,\"repaymentPlanNo\":\"ZC27375ACFF4234805\",\"repaymentPrincipal\":0.00,\"techFee\":0.00,'overDueFeeDetail':{'penaltyFee':0.00,'latePenalty':0.00,'lateFee':0.00,'lateOtherCost':0.00,'totalOverdueFee':0.00}}]");
		commandModel.setRepaymentType(0);
		MockHttpServletResponse response = new MockHttpServletResponse();
		String jsonResult = commandApiDeductController.commandDeduct(request, response, commandModel);
		ApiResult apiResult = JsonUtils.parse(jsonResult, ApiResult.class);
		Assert.assertEquals(ApiResponseCode.DEDUCT_AMOUNT_LESS_THAN_RECEIVE_AMOUNT, apiResult.getCode());
	}
	
	@Test
	@Sql({"classpath:test/yunxin/api/deduct_delete_all_table.sql","classpath:test/yunxin/api/testCommandDeductAmountIsNegativeNumber_ww1.sql"})
	public void test(){
		DeductCommandRequestModel commandModel = new DeductCommandRequestModel();
		commandModel.setRequestNo(UUID.randomUUID().toString());
		commandModel.setDeductId(UUID.randomUUID().toString());
		commandModel.setContractNo("DKHD-001");
		commandModel.setAmount("100");
		commandModel.setApiCalledTime("2016-03-05");
		commandModel.setFinancialProductCode("G31700");
		commandModel.setRepaymentDetails("[{\"loanFee\":0.00,\"otherFee\":0.00,\"repaymentAmount\":100.00,\"repaymentInterest\":100.00,\"repaymentPlanNo\":\"repayment_plan_no_2\",\"repaymentPrincipal\":0.00,\"techFee\":0.00,'overDueFeeDetail':{'penaltyFee':0.00,'latePenalty':0.00,'lateFee':0.00,'lateOtherCost':0.00,'totalOverdueFee':0.00}}]");
		commandModel.setRepaymentType(0);
		MockHttpServletResponse response = new MockHttpServletResponse();
		String jsonResult = commandApiDeductController.commandDeduct(request, response, commandModel);
		ApiResult apiResult = JsonUtils.parse(jsonResult, ApiResult.class);
		Assert.assertEquals("还款计划编号错误", apiResult.getMessage());
	}
	
	@Test
	@Sql({"classpath:test/yunxin/api/deduct_delete_all_table.sql","classpath:test/yunxin/api/testCommandDeductAmountIsNegativeNumber_ww1.sql"})
	public void test2(){
		DeductCommandRequestModel commandModel = new DeductCommandRequestModel();
		commandModel.setRequestNo(UUID.randomUUID().toString());
		commandModel.setDeductId(UUID.randomUUID().toString());
		commandModel.setContractNo("DKHD-001");
		commandModel.setAmount("100");
		commandModel.setApiCalledTime("2016-03-05");
		commandModel.setFinancialProductCode("G31700");
		commandModel.setRepaymentDetails("[{\"loanFee\":0.00,\"otherFee\":0.00,\"repaymentAmount\":100.00,\"repaymentInterest\":100.00,\"repayScheduleNo\":\"repay_schedule_no_2\",\"repaymentPrincipal\":0.00,\"techFee\":0.00,'overDueFeeDetail':{'penaltyFee':0.00,'latePenalty':0.00,'lateFee':0.00,'lateOtherCost':0.00,'totalOverdueFee':0.00}}]");
		commandModel.setRepaymentType(0);
		MockHttpServletResponse response = new MockHttpServletResponse();
		String jsonResult = commandApiDeductController.commandDeduct(request, response, commandModel);
		ApiResult apiResult = JsonUtils.parse(jsonResult, ApiResult.class);
		Assert.assertEquals("商户还款编号错误", apiResult.getMessage());
	}
	
	
	@Test
	@Sql({"classpath:test/yunxin/api/deduct_delete_all_table.sql","classpath:test/yunxin/api/testCommandDeductAmountIsNegativeNumber_ww1.sql"})
	public void test2_1(){
		DeductCommandRequestModel commandModel = new DeductCommandRequestModel();
		commandModel.setRequestNo(UUID.randomUUID().toString());
		commandModel.setDeductId(UUID.randomUUID().toString());
		commandModel.setContractNo("DKHD-001");
		commandModel.setAmount("100");
		commandModel.setApiCalledTime("2016-03-05");
		commandModel.setFinancialProductCode("G31700");
		commandModel.setRepaymentDetails("[{\"loanFee\":0.00,\"otherFee\":0.00,\"repaymentAmount\":100.00,\"repaymentInterest\":100.00,\"repayScheduleNo\":\"repay_schedule_no_1\",\"repaymentPrincipal\":0.00,\"techFee\":0.00,'overDueFeeDetail':{'penaltyFee':0.00,'latePenalty':0.00,'lateFee':0.00,'lateOtherCost':0.00,'totalOverdueFee':0.00}}]");
		commandModel.setRepaymentType(0);
		MockHttpServletResponse response = new MockHttpServletResponse();
		String jsonResult = commandApiDeductController.commandDeduct(request, response, commandModel);
		ApiResult apiResult = JsonUtils.parse(jsonResult, ApiResult.class);
		Assert.assertEquals("支付通道不存在", apiResult.getMessage());
	}
	
	
	@Test
	@Sql({"classpath:test/yunxin/api/deduct_delete_all_table.sql","classpath:test/yunxin/api/testCommandDeductAmountIsNegativeNumber_ww1.sql"})
	public void test3(){
		DeductCommandRequestModel commandModel = new DeductCommandRequestModel();
		commandModel.setRequestNo(UUID.randomUUID().toString());
		commandModel.setDeductId(UUID.randomUUID().toString());
		commandModel.setContractNo("DKHD-001");
		commandModel.setAmount("100");
		commandModel.setApiCalledTime("2016-03-05");
		commandModel.setFinancialProductCode("G31700");
		commandModel.setRepaymentDetails("[{\"loanFee\":0.00,\"otherFee\":0.00,\"repaymentAmount\":100.00,\"repaymentInterest\":100.00,\"currentPeriod\":\"2\",\"repaymentPrincipal\":0.00,\"techFee\":0.00,'overDueFeeDetail':{'penaltyFee':0.00,'latePenalty':0.00,'lateFee':0.00,'lateOtherCost':0.00,'totalOverdueFee':0.00}}]");
		commandModel.setRepaymentType(0);
		MockHttpServletResponse response = new MockHttpServletResponse();
		String jsonResult = commandApiDeductController.commandDeduct(request, response, commandModel);
		ApiResult apiResult = JsonUtils.parse(jsonResult, ApiResult.class);
		Assert.assertEquals("期数错误", apiResult.getMessage());
	}
	
	@Test
	@Sql({"classpath:test/yunxin/api/deduct_delete_all_table.sql","classpath:test/yunxin/api/testCommandDeductAmountIsNegativeNumber_ww1.sql"})
	public void test3_1(){
		DeductCommandRequestModel commandModel = new DeductCommandRequestModel();
		commandModel.setRequestNo(UUID.randomUUID().toString());
		commandModel.setDeductId(UUID.randomUUID().toString());
		commandModel.setContractNo("DKHD-001");
		commandModel.setAmount("100");
		commandModel.setApiCalledTime("2016-03-05");
		commandModel.setFinancialProductCode("G31700");
		commandModel.setRepaymentDetails("[{\"loanFee\":0.00,\"otherFee\":0.00,\"repaymentAmount\":100.00,\"repaymentInterest\":100.00,\"currentPeriod\":\"1\",\"repaymentPrincipal\":0.00,\"techFee\":0.00,'overDueFeeDetail':{'penaltyFee':0.00,'latePenalty':0.00,'lateFee':0.00,'lateOtherCost':0.00,'totalOverdueFee':0.00}}]");
		commandModel.setRepaymentType(0);
		MockHttpServletResponse response = new MockHttpServletResponse();
		String jsonResult = commandApiDeductController.commandDeduct(request, response, commandModel);
		ApiResult apiResult = JsonUtils.parse(jsonResult, ApiResult.class);
		Assert.assertEquals("支付通道不存在", apiResult.getMessage());
	}
	
	@Test
	@Sql({"classpath:test/yunxin/api/deduct_delete_all_table.sql","classpath:test/yunxin/api/testCommandDeductAmountIsNegativeNumber_ww1.sql"})
	public void test4(){
		DeductCommandRequestModel commandModel = new DeductCommandRequestModel();
		commandModel.setRequestNo(UUID.randomUUID().toString());
		commandModel.setDeductId(UUID.randomUUID().toString());
		commandModel.setContractNo("DKHD-001");
		commandModel.setAmount("100");
		commandModel.setApiCalledTime("2016-03-05");
		commandModel.setFinancialProductCode("G31700");
		commandModel.setRepaymentDetails("[{\"loanFee\":0.00,\"otherFee\":0.00,\"repaymentAmount\":100.00,\"repaymentInterest\":100.00,\"currentPeriod\":\"-2\",\"repaymentPrincipal\":0.00,\"techFee\":0.00,'overDueFeeDetail':{'penaltyFee':0.00,'latePenalty':0.00,'lateFee':0.00,'lateOtherCost':0.00,'totalOverdueFee':0.00}}]");
		commandModel.setRepaymentType(0);
		MockHttpServletResponse response = new MockHttpServletResponse();
		String jsonResult = commandApiDeductController.commandDeduct(request, response, commandModel);
		ApiResult apiResult = JsonUtils.parse(jsonResult, ApiResult.class);
		Assert.assertEquals("商户还款编号、还款计划编号、期数未填", apiResult.getMessage());
	}
}
