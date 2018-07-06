package com.zufangbao.earth.web.controller.api;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zufangbao.sun.entity.security.Principal;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartHttpServletRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.demo2do.core.entity.Result;
import com.demo2do.core.utils.JsonUtils;
import com.zufangbao.earth.api.test.post.BaseApiTestPost;
import com.zufangbao.earth.yunxin.api.controller.CommandApiController;
import com.zufangbao.earth.yunxin.api.model.command.ActivePaymentVoucherCommandModel;
import com.zufangbao.earth.yunxin.api.model.command.BusinessPaymentVoucherCommandModel;
import com.zufangbao.earth.yunxin.web.controller.RepaymentOrderController;
import com.zufangbao.gluon.spec.earth.v3.ApiResponseCode;
import com.zufangbao.sun.api.model.repayment.RepaymentOrderDetailInfoModel;
import com.zufangbao.sun.yunxin.entity.model.repaymentOrder.RepaymentOrderQueryModel;
import com.zufangbao.wellsfargo.silverpool.cashauditing.entity.voucher.ActivePaymentVoucherDetail;
import com.zufangbao.wellsfargo.silverpool.cashauditing.entity.voucher.BusinessPaymentVoucherDetail;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {

		"classpath:/local/applicationContext-*.xml",
		"classpath:/DispatcherServlet.xml" })
@Transactional()
@TransactionConfiguration(defaultRollback = false)
@WebAppConfiguration(value="webapp")
public class CommandApiControllerTest extends BaseApiTestPost {
	
	
	@Autowired
	private CommandApiController commandApiController;
	
	@Autowired
	private RepaymentOrderController repaymentOrderController;

	@Test
	public void test_submitActivePaymentVoucher_errorMsg() {
		MockHttpServletRequest request = new MockMultipartHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		ActivePaymentVoucherCommandModel model = new ActivePaymentVoucherCommandModel();
		String message = commandApiController.submitActivePaymentVoucher(request, response, model);
		Result result = JsonUtils.parse(message, Result.class);
		Assert.assertEquals("20001", result.getCode());
	}

//	@Test
//	@Sql("classpath:test/yunxin/activepaymentvoucher/test_submitActivePaymentVoucher.sql")
//	public void test_submitActivePaymentVoucher() {
//		MockHttpServletRequest request = new MockMultipartHttpServletRequest();
//		MockHttpServletResponse response = new MockHttpServletResponse();
//		ActivePaymentVoucherCommandModel model = new ActivePaymentVoucherCommandModel();
//		
//		model.setRequestNo("requestNo");
//		model.setContractNo("云信信2016-241-DK(428522112675736882)");
//		
//		String repaymentPlanNo = "[\"ZC275016985BF712EF\",\"ZC275016985BF712EG\"]";
//		String requestNo = "noway";
//		Integer transactionType = 0;
//		Integer voucherType = 5;
//		String receivableAccountNo = "20001";
//		String paymentBank = "中国建设银行";
//		String bankTransactionNo = "transactionNo1";
//		BigDecimal voucherAmount = new BigDecimal(35000.00);
//		String paymentName = "payment";
//		String paymentAccountNo = "paymentno1";
//		
//		model.setRequestNo(requestNo);
//		model.setTransactionType(transactionType);
//		model.setVoucherType(voucherType);
//		model.setRepaymentPlanNo(repaymentPlanNo);
//		model.setReceivableAccountNo(receivableAccountNo);
//		model.setPaymentBank(paymentBank);
//		model.setBankTransactionNo(bankTransactionNo);
//		model.setVoucherAmount(voucherAmount);
//		model.setPaymentName(paymentName);
//		model.setPaymentAccountNo(paymentAccountNo);
//		String message = commandApiController.submitActivePaymentVoucher(request, response, model);
//		Result result = JsonUtils.parse(message, Result.class);
//		Assert.assertEquals("0", result.getCode());
//	}
	/**
	 * 主动还款接口测试
	 */
	@Test
	@Sql("classpath:test/yunxin/activepaymentvoucher/test_submitActivePaymentVoucher.sql")
	public void test_submitActivePaymentVoucher() {
		MockHttpServletRequest request = new MockMultipartHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		ActivePaymentVoucherCommandModel model = new ActivePaymentVoucherCommandModel();
		
		model.setRequestNo("requestNo");
		//model.setContractNo("云信信2016-241-DK(428522112675736882)");

		String requestNo = "noway";
		Integer transactionType = 0;
		Integer voucherType = 5;
		String receivableAccountNo = "20001";
		String paymentBank = "中国建设银行";
		String bankTransactionNo = "transactionNo1";
		BigDecimal voucherAmount = new BigDecimal("15000");
		String paymentName = "payment";
		String paymentAccountNo = "paymentno1";
		model.setFinancialContractNo("G32000");
		model.setRequestNo(requestNo);
		model.setTransactionType(transactionType);
		model.setVoucherType(voucherType);
		model.setReceivableAccountNo(receivableAccountNo);
		model.setPaymentBank(paymentBank);
		model.setBankTransactionNo(bankTransactionNo);
		model.setVoucherAmount(voucherAmount);
		model.setPaymentName(paymentName);
		model.setPaymentAccountNo(paymentAccountNo);

		List<ActivePaymentVoucherDetail> detailList = new ArrayList<>();
		ActivePaymentVoucherDetail detail = new ActivePaymentVoucherDetail();
		detail.setRepaymentPlanNo("ZC275016985BF712EF");
		detail.setAmount(new BigDecimal("1500"));
//		detailList.add(detail);

		ActivePaymentVoucherDetail detail2 = new ActivePaymentVoucherDetail();
		detail2.setRepaymentPlanNo("ZC275016985BF712EB");
		detail2.setAmount(new BigDecimal("15000"));
		detail2.setRepayScheduleNo("3432423423");
//		detail2.setRepaymentNumber(7777);
//		detail2.setContractNo("云信信2016-241-DK(428522112675736881)");
		detail2.setUniqueId("e568793f-a44c-4362-9e78-0ce433131f3e");
		detailList.add(detail2);

		model.setDetail(com.zufangbao.sun.utils.JsonUtils.toJSONString(detailList));

		String message = commandApiController.submitActivePaymentVoucher(request, response, model);
		Result result = JsonUtils.parse(message, Result.class);
		//Assert.assertEquals("0", result.getCode());
	}
	
	@Test
	public void test_undoActivePaymentVoucher_errorMsg() {
		MockHttpServletRequest request = new MockMultipartHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		ActivePaymentVoucherCommandModel model = new ActivePaymentVoucherCommandModel();
		String message = commandApiController.undoActivePaymentVoucher(request, response, model);
		Result result = JsonUtils.parse(message, Result.class);
		Assert.assertEquals("20001", result.getCode());
	}

	@Test
	@Sql("classpath:test/yunxin/activepaymentvoucher/test_undoActivePaymentVoucher.sql")
	public void test_undoActivePaymentVoucher() {
		MockHttpServletRequest request = new MockMultipartHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		ActivePaymentVoucherCommandModel model = new ActivePaymentVoucherCommandModel();
		
		model.setRequestNo("requestNo");
		//model.setContractNo("云信信2016-241-DK(428522112675736882)");

		String requestNo = "noway";
		Integer transactionType = 0;
		Integer voucherType = 5;
		String receivableAccountNo = "20001";
		String paymentBank = "中国建设银行";
		String bankTransactionNo = "transactionNo1";
		BigDecimal voucherAmount = new BigDecimal(35000.00);
		String paymentName = "payment";
		String paymentAccountNo = "paymentno1";
		
		model.setRequestNo(requestNo);
		model.setTransactionType(transactionType);
		model.setVoucherType(voucherType);
//		model.setRepaymentPlanNo(repaymentPlanNo);
		model.setReceivableAccountNo(receivableAccountNo);
		model.setPaymentBank(paymentBank);
		model.setBankTransactionNo(bankTransactionNo);
		model.setVoucherAmount(voucherAmount);
		model.setPaymentName(paymentName);
		model.setPaymentAccountNo(paymentAccountNo);

		List<ActivePaymentVoucherDetail> detailList = new ArrayList<>();
		ActivePaymentVoucherDetail detail = new ActivePaymentVoucherDetail();
		detail.setRepaymentPlanNo("ZC275016985BF712EF");
		detail.setAmount(new BigDecimal("15000"));

		ActivePaymentVoucherDetail detail2 = new ActivePaymentVoucherDetail();
		detail2.setRepaymentPlanNo("ZC275016985BF712EG");
		detail2.setAmount(new BigDecimal("20000"));

		detailList.add(detail2);
		model.setDetail(com.zufangbao.sun.utils.JsonUtils.toJSONString(detailList));

		String message = commandApiController.undoActivePaymentVoucher(request, response, model);
		Result result = JsonUtils.parse(message, Result.class);
		Assert.assertEquals("0", result.getCode());
		//System.out.println(result.getMessage());
	}
	
	
	/*************** test submitBusinessPaymentVoucher start ***************/
	@Test
	@Sql("classpath:test/yunxin/businessPaymentVoucher/test_businessPaymentVoucher.sql")
	public void test_submitBusinessPaymentVoucher() {
		HttpServletRequest request = new MockHttpServletRequest();
		HttpServletResponse response = new MockHttpServletResponse();
		List<BusinessPaymentVoucherDetail> details = new ArrayList<>();
		BusinessPaymentVoucherDetail detail1 = new BusinessPaymentVoucherDetail();
		detail1.setAmount(new BigDecimal("10000.00"));
		detail1.setUniqueId("e96bdb77-c051-4db0-b046-daca68baa203");
//		detail1.setRepaymentPlanNo("plan_no1");
		detail1.setPayer(0);
//		detail1.setCurrentPeriod(99);
//		detail1.setRepayScheduleNo("uifsohfjsk");
		
		BusinessPaymentVoucherDetail detail2 = new BusinessPaymentVoucherDetail();
		detail2.setAmount(new BigDecimal("10000.00"));
		detail2.setUniqueId("e96bdb77-c051-4db0-b046-daca68baa204");
//		detail2.setRepaymentPlanNo("plan_no2");
		detail2.setPayer(0);
//		detail2.setCurrentPeriod(993);
//		detail2.setRepayScheduleNo("uifsohfjsk");
		
		details.add(detail1);
		details.add(detail2);
		String jsonString = JsonUtils.toJsonString(details);

		
		
		BusinessPaymentVoucherCommandModel model = new BusinessPaymentVoucherCommandModel();
		model.setRequestNo("test_request_no_2");
		model.setTransactionType(0);
		model.setVoucherType(2);
		model.setFinancialContractNo("test_contract_no_1");
		model.setReceivableAccountNo("test_receivable_account_no_1");
		model.setPaymentAccountNo("test_payment_account_no_1");
		model.setPaymentName("test_payment_name_1");
		model.setPaymentBank("招商银行");
		model.setVoucherAmount(new BigDecimal("20000.00"));
		model.setBankTransactionNo("test_transaction_no_2");
		model.setDetail(jsonString);
		
		String message = commandApiController.submitBusinessPaymentVoucher(request, response, model);
		Result result = JsonUtils.parse(message,Result.class);
		Assert.assertEquals("成功!", result.getMessage());
	}
	
	@Test
	@Sql("classpath:test/yunxin/businessPaymentVoucher/test_businessPaymentVoucher.sql")
	public void test_submitBusinessPaymentVoucher_invalidParams() {
		HttpServletRequest request = new MockHttpServletRequest();
		HttpServletResponse response = new MockHttpServletResponse();
		List<BusinessPaymentVoucherDetail> details = new ArrayList<>();
		BusinessPaymentVoucherDetail detail1 = new BusinessPaymentVoucherDetail();
		detail1.setAmount(new BigDecimal("10000.00"));
		detail1.setUniqueId("e96bdb77-c051-4db0-b046-daca68baa203");
//		detail1.setRepaymentPlanNo("plan_no1");
		detail1.setPayer(0);
		details.add(detail1);
		String jsonString = JsonUtils.toJsonString(details);
		
		BusinessPaymentVoucherCommandModel model = new BusinessPaymentVoucherCommandModel();
		//model.setRequestNo("test_request_no_2");
		model.setTransactionType(0);
		model.setVoucherType(0);
		model.setFinancialContractNo("test_contract_no_1");
		model.setReceivableAccountNo("test_receivable_account_no_1");
		model.setPaymentAccountNo("test_payment_account_no_1");
		model.setPaymentName("test_payment_name_1");
		model.setPaymentBank("招商银行");
		model.setVoucherAmount(new BigDecimal("10000.00"));
		model.setBankTransactionNo("test_transaction_no_2");
		model.setDetail(jsonString);
		
		String message = commandApiController.submitBusinessPaymentVoucher(request, response, model);
		Result result = JsonUtils.parse(message,Result.class);
		Assert.assertEquals(ApiResponseCode.INVALID_PARAMS, Integer.parseInt(result.getCode()));
	}

	@Test
	@Sql("classpath:test/yunxin/businessPaymentVoucher/test_businessPaymentVoucher.sql")
	public void test_submitBusinessPaymentVoucher_invalidParams_no_repayment_plan() {
		HttpServletRequest request = new MockHttpServletRequest();
		HttpServletResponse response = new MockHttpServletResponse();
		List<BusinessPaymentVoucherDetail> details = new ArrayList<>();
		BusinessPaymentVoucherDetail detail1 = new BusinessPaymentVoucherDetail();
		detail1.setAmount(new BigDecimal("10000.00"));
		detail1.setUniqueId("e96bdb77-c051-4db0-b046-daca68baa203");
//		detail1.setRepaymentPlanNo("plan_no1");
		detail1.setPayer(0);
		details.add(detail1);
		String jsonString = JsonUtils.toJsonString(details);
		
		BusinessPaymentVoucherCommandModel model = new BusinessPaymentVoucherCommandModel();
		model.setRequestNo("test_request_no_2");
		model.setTransactionType(0);
		model.setVoucherType(0);
		model.setFinancialContractNo("test_contract_no_1");
		model.setReceivableAccountNo("test_receivable_account_no_1");
		model.setPaymentAccountNo("test_payment_account_no_1");
		model.setPaymentName("test_payment_name_1");
		model.setPaymentBank("招商银行");
		model.setVoucherAmount(new BigDecimal("10000.00"));
		model.setBankTransactionNo("test_transaction_no_2");
		model.setDetail(jsonString);
		
		String message = commandApiController.submitBusinessPaymentVoucher(request, response, model);
		Result result = JsonUtils.parse(message,Result.class);
		Assert.assertEquals(ApiResponseCode.INVALID_PARAMS, Integer.parseInt(result.getCode()));
		Assert.assertEquals("凭证明细内容错误［detail］，字段格式错误！", result.getMessage());
	}
	
	@Test
	@Sql("classpath:test/yunxin/businessPaymentVoucher/test_businessPaymentVoucher.sql")
	public void test_submitBusinessPaymentVoucher_invalidParams_detail() {
		HttpServletRequest request = new MockHttpServletRequest();
		HttpServletResponse response = new MockHttpServletResponse();
		List<BusinessPaymentVoucherDetail> details = new ArrayList<>();
		BusinessPaymentVoucherDetail detail1 = new BusinessPaymentVoucherDetail();
		detail1.setAmount(new BigDecimal("10000.00"));
		detail1.setUniqueId("e96bdb77-c051-4db0-b046-daca68baa203");
		detail1.setRepaymentPlanNo("plan_no1");
		detail1.setPayer(0);
		detail1.setPrincipal(new BigDecimal("9000.00"));
		details.add(detail1);
		String jsonString = JsonUtils.toJsonString(details);
		
		BusinessPaymentVoucherCommandModel model = new BusinessPaymentVoucherCommandModel();
		model.setRequestNo(UUID.randomUUID().toString());
		model.setTransactionType(0);
		model.setVoucherType(0);
		model.setFinancialContractNo("test_contract_no_1");
		model.setReceivableAccountNo("test_receivable_account_no_1");
		model.setPaymentAccountNo("test_payment_account_no_1");
		model.setPaymentName("test_payment_name_1");
		model.setPaymentBank("招商银行");
		model.setVoucherAmount(new BigDecimal("10000.00"));
		model.setBankTransactionNo("test_transaction_no_2");
		model.setDetail(jsonString);
		
		String message = commandApiController.submitBusinessPaymentVoucher(request, response, model);
		Result result = JsonUtils.parse(message,Result.class);
		Assert.assertEquals(ApiResponseCode.INVALID_PARAMS, Integer.parseInt(result.getCode()));
		Assert.assertEquals("凭证明细金额与凭证金额不匹配", result.getMessage());
	}

	@Test
	@Sql("classpath:test/yunxin/businessPaymentVoucher/test_businessPaymentVoucher.sql")
	@Ignore("不明原因，等待后续修复")
	public void test_submitBusinessPaymentVoucher_invalidParams_2() {
		HttpServletRequest request = new MockHttpServletRequest();
		HttpServletResponse response = new MockHttpServletResponse();
		List<BusinessPaymentVoucherDetail> details = new ArrayList<>();
		BusinessPaymentVoucherDetail detail1 = new BusinessPaymentVoucherDetail();
		detail1.setAmount(new BigDecimal("10000.00"));
		detail1.setUniqueId("e96bdb77-c051-4db0-b046-daca68baa203");
		detail1.setRepaymentPlanNo("plan_no1");
		detail1.setPayer(0);
		details.add(detail1);

		BusinessPaymentVoucherDetail detail2 = new BusinessPaymentVoucherDetail();
		detail2.setAmount(new BigDecimal("10000.00"));
		detail2.setUniqueId("e96bdb77-c051-4db0-b046-daca68baa204");
		detail2.setRepaymentPlanNo("plan_no1");
		detail2.setPayer(0);
		details.add(detail2);
		String jsonString = JsonUtils.toJsonString(details);
		
		BusinessPaymentVoucherCommandModel model = new BusinessPaymentVoucherCommandModel();
		model.setRequestNo(UUID.randomUUID().toString());
		model.setTransactionType(0);
		model.setVoucherType(0);
		model.setFinancialContractNo("test_contract_no_1");
		model.setReceivableAccountNo("test_receivable_account_no_1");
		model.setPaymentAccountNo("test_payment_account_no_1");
		model.setPaymentName("test_payment_name_1");
		model.setPaymentBank("招商银行");
		model.setVoucherAmount(new BigDecimal("10000.00"));
		model.setBankTransactionNo("test_transaction_no_2");
		model.setDetail(jsonString);
		
		String message = commandApiController.submitBusinessPaymentVoucher(request, response, model);
		Result result = JsonUtils.parse(message,Result.class);
		Assert.assertEquals(ApiResponseCode.INVALID_PARAMS, Integer.parseInt(result.getCode()));
		Assert.assertEquals("凭证金额与明细总金额不匹配！", result.getMessage());
	}


	@Test
	public void test_submitBusinessPaymentVoucher_REPEAT_REQUEST_NO() {

	}

	@Test
	public void test_submitBusinessPaymentVoucher_NO_SUCH_CASH_FLOW() {

	}

	/*************** test submitBusinessPaymentVoucher end ***************/
	
	/*************** test undoBusinessPaymentVoucher  start ***************/
	@Test
	@Sql("classpath:test/yunxin/businessPaymentVoucher/test_undoBusinessPaymentVoucher.sql")
	public void test_undoBusinessPaymentVoucher() {
		HttpServletRequest request = new MockHttpServletRequest();
		HttpServletResponse response = new MockHttpServletResponse();
		BusinessPaymentVoucherCommandModel model = new BusinessPaymentVoucherCommandModel();
		model.setRequestNo("test_request_no_2");
		model.setTransactionType(0);
		model.setVoucherType(0);
		model.setFinancialContractNo("test_contract_no_1");
		model.setReceivableAccountNo("test_receivable_account_no_1");
		model.setPaymentAccountNo("test_payment_account_no_1");
		model.setPaymentName("test_payment_name_1");
		model.setPaymentBank("招商银行");
		model.setBankTransactionNo("test_transaction_no_1");
		
		String message = commandApiController.undoBusinessPaymentVoucher(request, response, model);
		Result result = JsonUtils.parse(message,Result.class);
		Assert.assertEquals("成功!", result.getMessage());
		
	}
	
	@Test
	@Sql("classpath:test/yunxin/businessPaymentVoucher/test_undoBusinessPaymentVoucher.sql")
	public void test_undoBusinessPaymentVoucher_invalidParams() {
		HttpServletRequest request = new MockHttpServletRequest();
		HttpServletResponse response = new MockHttpServletResponse();
		BusinessPaymentVoucherCommandModel model = new BusinessPaymentVoucherCommandModel();
		//model.setRequestNo("test_request_no_2");
		model.setTransactionType(0);
		model.setVoucherType(0);
		model.setFinancialContractNo("test_contract_no_1");
		model.setReceivableAccountNo("test_receivable_account_no_1");
		model.setPaymentAccountNo("test_payment_account_no_1");
		model.setPaymentName("test_payment_name_1");
		model.setPaymentBank("招商银行");
		model.setBankTransactionNo("test_transaction_no_1");
		
		String message = commandApiController.undoBusinessPaymentVoucher(request, response, model);
		Result result = JsonUtils.parse(message,Result.class);
		Assert.assertEquals(ApiResponseCode.INVALID_PARAMS, Integer.parseInt(result.getCode()));
		
	}
	/*************** test undoBusinessPaymentVoucher  end ***************/
	
	/*************** test submitBusinessPaymentVoucher start ***************/
	@Test
	@Sql("classpath:test/yunxin/businessPaymentVoucher/test_businessPaymentVoucher.sql")
	public void test_submitBusinessPaymentVoucher2() {
		HttpServletRequest request = new MockHttpServletRequest();
		HttpServletResponse response = new MockHttpServletResponse();
		List<BusinessPaymentVoucherDetail> details = new ArrayList<>();
		BusinessPaymentVoucherDetail detail1 = new BusinessPaymentVoucherDetail();
		detail1.setAmount(new BigDecimal("10000.00"));
		detail1.setUniqueId("e96bdb77-c051-4db0-b046-daca68baa203");
		detail1.setRepaymentPlanNo("plan_no1");
		detail1.setPayer(0);
		details.add(detail1);
		String jsonString = JsonUtils.toJsonString(details);
		
		BusinessPaymentVoucherCommandModel model = new BusinessPaymentVoucherCommandModel();
		model.setRequestNo("test_request_no_2");
		model.setTransactionType(0);
		model.setVoucherType(2);
		model.setFinancialContractNo("test_contract_no_1");
		model.setReceivableAccountNo("test_receivable_account_no_1");
		model.setPaymentAccountNo("test_payment_account_no_1");
		model.setPaymentName("test_payment_name_1");
		model.setPaymentBank("招商银行");
		model.setVoucherAmount(new BigDecimal("10000.00"));
		model.setBankTransactionNo("test_transaction_no_2");
		model.setDetail(jsonString);
		
		String message = commandApiController.submitBusinessPaymentVoucher(request, response, model);
		Result result = JsonUtils.parse(message,Result.class);
		Assert.assertEquals("成功!", result.getMessage());
	}
	
	@Test
	@Sql("classpath:test/yunxin/businessPaymentVoucher/test_businessPaymentVoucher.sql")
	public void test_submitBusinessPaymentVoucher3() {
		HttpServletRequest request = new MockHttpServletRequest();
		HttpServletResponse response = new MockHttpServletResponse();
		List<BusinessPaymentVoucherDetail> details = new ArrayList<>();
		BusinessPaymentVoucherDetail detail1 = new BusinessPaymentVoucherDetail();
		detail1.setAmount(new BigDecimal("10000.00"));
		detail1.setPrincipal(new BigDecimal("5000"));
		detail1.setLateFee(new BigDecimal("5000"));
		detail1.setUniqueId("e96bdb77-c051-4db0-b046-daca68baa203");
		detail1.setRepaymentPlanNo("plan_no1");
		detail1.setPayer(0);
		details.add(detail1);
		String jsonString = JsonUtils.toJsonString(details);
		
		BusinessPaymentVoucherCommandModel model = new BusinessPaymentVoucherCommandModel();
		model.setRequestNo("test_request_no_2");
		model.setTransactionType(0);
		model.setVoucherType(2);
		model.setFinancialContractNo("test_contract_no_1");
		model.setReceivableAccountNo("test_receivable_account_no_1");
		model.setPaymentAccountNo("test_payment_account_no_1");
		model.setPaymentName("test_payment_name_1");
		model.setPaymentBank("招商银行");
		model.setVoucherAmount(new BigDecimal("10000.00"));
		model.setBankTransactionNo("test_transaction_no_2");
		model.setDetail(jsonString);
		
		String message = commandApiController.submitBusinessPaymentVoucher(request, response, model);
		Result result = JsonUtils.parse(message,Result.class);
		Assert.assertEquals("回购凭证明细总金额与明细其他金额总和不匹配！", result.getMessage());
	}
	
	@Test
	@Sql("classpath:test/yunxin/businessPaymentVoucher/test_businessPaymentVoucher.sql")
	public void test_submitBusinessPaymentVoucher4() {
		HttpServletRequest request = new MockHttpServletRequest();
		HttpServletResponse response = new MockHttpServletResponse();
		List<BusinessPaymentVoucherDetail> details = new ArrayList<>();
		BusinessPaymentVoucherDetail detail1 = new BusinessPaymentVoucherDetail();
		detail1.setAmount(new BigDecimal("10000.00"));
		detail1.setPrincipal(new BigDecimal("3000"));
		detail1.setInterest(new BigDecimal("2000"));
		detail1.setPenaltyFee(new BigDecimal("4000"));
		detail1.setOtherCharge(new BigDecimal("1000"));
		detail1.setUniqueId("e96bdb77-c051-4db0-b046-daca68baa203");
		detail1.setRepaymentPlanNo("plan_no1");
		detail1.setPayer(0);
		details.add(detail1);
		String jsonString = JsonUtils.toJsonString(details);
		
		BusinessPaymentVoucherCommandModel model = new BusinessPaymentVoucherCommandModel();
		model.setRequestNo("test_request_no_2");
		model.setTransactionType(0);
		model.setVoucherType(2);
		model.setFinancialContractNo("test_contract_no_1");
		model.setReceivableAccountNo("test_receivable_account_no_1");
		model.setPaymentAccountNo("test_payment_account_no_1");
		model.setPaymentName("test_payment_name_1");
		model.setPaymentBank("招商银行");
		model.setVoucherAmount(new BigDecimal("10000.00"));
		model.setBankTransactionNo("test_transaction_no_2");
		model.setDetail(jsonString);
		
		String message = commandApiController.submitBusinessPaymentVoucher(request, response, model);
		Result result = JsonUtils.parse(message,Result.class);
		Assert.assertEquals("成功!", result.getMessage());
	}
	
	@Test
	public void test_thirdPartVoucherCommandModel(){
		
	}
	
	@Test
	public void testExportRepaymentOrderFile() throws IOException{
		
		List<String> list=new ArrayList<>();
		list.add("c31ac9c0-801f-44b4-9072-eb824aa478bd");
		String financialContractUuidsStr=JsonUtils.toJsonString(list);
		RepaymentOrderQueryModel repaymentOrderQueryModel=new RepaymentOrderQueryModel();

		repaymentOrderQueryModel.setFinancialContractUuids(financialContractUuidsStr);
		String orderUUid="653f6fa1-ab79-401f-ac75-f337f05e4055";
		repaymentOrderQueryModel.setOrderUuid(orderUUid);
//		String requestBody = JSON.toJSONString(repaymentOrderQueryModel, SerializerFeature.DisableCircularReferenceDetect);

		repaymentOrderQueryModel.setStartDateString("2017-08-16 00:00:00");
		repaymentOrderQueryModel.setEndDateString("2017-07-30 00:00:00");
//		repaymentOrderQueryModel.setRepaymentStatus(2);
//		for (int i=0; i<1;i++) {
//			
		for (int i=0; i<1;i++) {
			
//		
//		Thread thread = new Thread(new Runnable() {
//			
//			@Override
//			public void run() {
				MockHttpServletRequest request = new MockHttpServletRequest();
				MockHttpServletResponse response = new MockHttpServletResponse();
				Principal principal = new Principal();
//				Map<String, String> headerParams=new HashMap<>();
//				byte[] result = HTTP("http://192.168.0.128/repayment-order/exportRepaymentOrderFile",requestBody,headerParams);
				repaymentOrderController.exportRepaymentOrderFile(request, response, repaymentOrderQueryModel, principal);

				response.getContentAsByteArray();
				response.getOutputStream();
				//File file = new File("/home/hjl/桌面/a_"+UUID.randomUUID().toString()+".zip");
				File file = new File("/home/hjl/桌面/"+orderUUid+".zip");
				OutputStream os=null;
				try {
					os = new FileOutputStream(file);
					os.write(response.getContentAsByteArray());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					if(os!=null){
						try {
							os.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
//		});
//		thread.start();
		
//		}
		
		
	}
	
//	private byte[] HTTP(String url,String requestBody,Map<String, String> headerParams){
//		headerParams.put("Cookie", "JSESSIONID=B8270A47182B245118AF79D16723F1EB");
//		HttpPost httpPost = new HttpPost(url);
//		try {
//			HttpEntity reqEntity=new StringEntity(requestBody, "UTF-8");
//			httpPost.setEntity(reqEntity);
//			if(headerParams != null) {
//				for (Entry<String, String> entry : headerParams.entrySet()) {
//					httpPost.addHeader(entry.getKey(), entry.getValue());
//				}
//			}
//			
//			CloseableHttpClient httpclient = HttpClients.createDefault();
//			
//			//设置请求和传输超时时间
//			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(60000).setConnectTimeout(60000).setConnectionRequestTimeout(60000).build();
//			httpPost.setConfig(requestConfig);
//
//			HttpResponse httpResp = httpclient.execute(httpPost);
//			
//			HttpEntity rspEntity = httpResp.getEntity(); 
//			
//			InputStream in = rspEntity.getContent();
//	
//			String strResp = StreamUtils.copyToString(in, Charset.forName("UTF-8"));
//			
//			byte[] bytes=strResp.getBytes();
//			return bytes;
//		}catch (Exception e) {
//			e.printStackTrace();
//			return null;
//		} finally {
//			//释放连接
//			httpPost.releaseConnection();
//		}
//	}
	
	@Test
	public void ssss(){
		String ss="[{\"actualAmount\":2700,\"feeType\":\"2000\",\"feeTypeEnum\":\"REPURCHASE_PRINCIPAL\"},{\"actualAmount\":60,\"feeType\":\"2001\",\"feeTypeEnum\":\"REPURCHASE_INTEREST\"},{\"actualAmount\":60,\"feeType\":\"2002\",\"feeTypeEnum\":\"REPURCHASE_PENALTY\"},{\"actualAmount\":120,\"feeType\":\"2003\",\"feeTypeEnum\":\"REPURCHASE_OTHER_FEE\"}]";
		List<RepaymentOrderDetailInfoModel> xx=JsonUtils.parseArray(ss,RepaymentOrderDetailInfoModel.class);
		System.out.println(xx);
		Map<String,BigDecimal> aa=RepaymentOrderDetailInfoModel.parseChargeDetailMap(xx);
		System.out.println(aa);
	}
	
}
