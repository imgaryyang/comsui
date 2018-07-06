package com.zufangbao.earth.yunxin.web;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.zufangbao.sun.entity.order.OrderAssetSet;
import com.zufangbao.sun.yunxin.entity.AssetSet;
import com.zufangbao.sun.yunxin.service.RepaymentPlanService;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import com.alibaba.fastjson.JSONObject;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.demo2do.core.entity.Result;
import com.demo2do.core.utils.DateUtils;
import com.demo2do.core.utils.JsonUtils;
import com.demo2do.core.web.resolver.Page;
import com.zufangbao.sun.entity.security.Principal;
import com.zufangbao.earth.service.PrincipalService;
import com.zufangbao.earth.yunxin.web.controller.YunxinPaymentController;
import com.zufangbao.earth.yunxin.web.controller.YunxinPaymentOrderController;
import com.zufangbao.sun.entity.customer.Customer;
import com.zufangbao.sun.entity.directbank.business.ContractAccount;
import com.zufangbao.sun.entity.financial.TransferApplication;
import com.zufangbao.sun.entity.order.Order;
import com.zufangbao.sun.yunxin.entity.ExecutingDeductStatus;
import com.zufangbao.sun.yunxin.entity.ExecutingSettlingStatus;
import com.zufangbao.sun.yunxin.entity.OverDueStatus;
import com.zufangbao.sun.yunxin.entity.PaymentWay;
import com.zufangbao.sun.yunxin.entity.api.deduct.DeductApplication;
import com.zufangbao.sun.yunxin.entity.model.OrderQueryModel;
import com.zufangbao.wellsfargo.yunxin.model.order.OrderViewDetail;
import com.zufangbao.sun.yunxin.entity.model.RepaymentChargesDetail;
import com.zufangbao.sun.yunxin.entity.model.TransferApplicationQueryModel;
import com.zufangbao.wellsfargo.yunxin.PaymentRecordModel;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {

		"classpath:/local/applicationContext-*.xml",
		"classpath:/DispatcherServlet.xml" })
@TransactionConfiguration(defaultRollback = true)
@Transactional()
@WebAppConfiguration(value="webapp")
public class PaymentControllerTest extends
		AbstractTransactionalJUnit4SpringContextTests {

	@Autowired
	private PrincipalService principalService;
	@Autowired
	private YunxinPaymentOrderController yunxinPaymentOrderController;
	@Autowired
	private YunxinPaymentController yunxinPaymentController;
	@Autowired
	private RepaymentPlanService repaymentPlanService;

	private MockHttpServletRequest request = new MockHttpServletRequest();

	private Principal principal = new Principal();

	@Before
	public void init() {
		yunxinPaymentController = this.applicationContext
				.getBean(YunxinPaymentController.class);
	}

	@Test
	@Sql("classpath:test/yunxin/testPaymentNoStatusList.sql")
	public void testPaymentNoStatusList() {

		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setQueryString("ashd&sabdlj");

		String paymentWay = "0";
		String executingDeductStatus = "1";
		String AccountName = "payer_name_1";
		String paymentNo = "DKHD-001-01-20160308-1910";
		String repaymentNo = "DKHD-1-01";
		String billingNo = "DKHD-001-01-20160308";
		String payAcNo = "pay_ac_no_1";
		String startDate = "2016-10-10";
		String endDate = "2016-10-10";
		Principal principal = principalService.load(Principal.class, 1l);
		Page page = new Page(1, 12);
		TransferApplicationQueryModel transferApplicationQueryModel = new TransferApplicationQueryModel(
				Integer.valueOf(paymentWay).intValue(), Integer.valueOf(
						executingDeductStatus).intValue(), AccountName,
				paymentNo, repaymentNo, billingNo, payAcNo, startDate, endDate);
		transferApplicationQueryModel.setFinancialContractIds("[\"1\"]");
		String resultStr = yunxinPaymentController.paymentShowView(
				request, transferApplicationQueryModel, principal, page);
		Result result = JSON.parseObject(resultStr, Result.class);
		List<Map<String, Object>> resultList = (List<Map<String, Object>>) result.getData().get("list");
		Map<String, Object> resultMap = resultList.get(0);
		Assert.assertEquals(new String("payer_name_1"), resultMap.get("contractAccount.payerName"));
		Assert.assertEquals(new String("DKHD-001-01-20160308-1910"),resultMap.get("transferApplicationNo"));
		Assert.assertEquals(new BigDecimal("1.00"), resultMap.get("amount"));
		Assert.assertEquals(new String("DKHD-001-01-20160308"),resultMap.get("order.orderNo"));
		assertEquals(new String("处理中"),resultMap.get("executingDeductStatusMsg"));
		Assert.assertEquals(new String("中国银行滨江支行"), resultMap.get("contractAccount.bank"));
	}

	@Test
	@Sql("classpath:test/yunxin/testPaymentList.sql")
	public void testPaymentList() {

		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setQueryString("ashd&sabdlj");

	
		String AccountName = "payer_name_1";
		String paymentNo = "zxcvbnm";
		String repaymentNo = "DKHD-1-01";
		String billingNo = "DKHD-001-01-20160308";
		String bankNo = "pay_ac_no_1";
		Principal principal = principalService.load(Principal.class, 1l);
		Page page = new Page(1, 12);
		TransferApplicationQueryModel transferApplicationQueryModel = new TransferApplicationQueryModel(
				-1, -1, AccountName, paymentNo, repaymentNo, billingNo, bankNo, "", "");
		transferApplicationQueryModel.setFinancialContractIds("[\"1\"]");
		String resultStr = yunxinPaymentController.paymentShowView(
				request, transferApplicationQueryModel, principal, page);
		Result result = JSON.parseObject(resultStr, Result.class);
		List<Map<String, Object>> resultList = (List<Map<String, Object>>) result.getData().get("list");
		Map<String, Object> resultMap = resultList.get(0);
		Assert.assertEquals(new String("payer_name_1"), resultMap.get("contractAccount.payerName"));
		Assert.assertEquals(new String("zxcvbnm"),resultMap.get("transferApplicationNo"));
		Assert.assertEquals(new BigDecimal("1.00"), resultMap.get("amount"));
		assertEquals(new String("处理中"),resultMap.get("executingDeductStatusMsg"));
		Assert.assertEquals(new String("DKHD-001-01-20160308"),resultMap.get("order.orderNo"));
		Assert.assertEquals(new String("中国银行滨江支行"), resultMap.get("contractAccount.bank"));
	}

	@Test
	@Sql("classpath:test/yunxin/testPaymentDetailList.sql")
	public void testPaymentDetailList() {

		Long transferApplicationId = 1L;
		Principal principal = principalService.load(Principal.class, 1l);
		Page page = new Page(1, 12);
		ModelAndView modelAndView = yunxinPaymentController.showOnlinePaymentOrderDetail(
				transferApplicationId, principal,page);
		Map<String, Object> map = modelAndView.getModel();
		TransferApplication transferApplication = (TransferApplication) map
				.get("transferApplication");
		Assert.assertEquals(new String("DKHD-001-01-20160308-1910"),
				transferApplication.getTransferApplicationNo());
		Assert.assertEquals(new String("王二"), transferApplication
				.getContractAccount().getPayerName());
		Assert.assertEquals(new String("2016-03-31 19:10:46.0"),
				transferApplication.getLastModifiedTime().toString());
		Assert.assertEquals(new String("中国银行滨江支行"), transferApplication
				.getContractAccount().getBank());
		Assert.assertEquals(new BigDecimal("1.00"),
				transferApplication.getAmount());
		Assert.assertEquals(ExecutingDeductStatus.CREATE,
				transferApplication.getExecutingDeductStatus());
		Assert.assertEquals(new String("123456789x"), transferApplication
				.getContractAccount().getBindId());
		Assert.assertEquals(PaymentWay.UNIONDEDUCT,
				transferApplication.getPaymentWay());
		Assert.assertEquals(new String("123456789"),
				transferApplication.getUnionPayOrderNo());
		Assert.assertEquals(new String(""), transferApplication.getComment());

	}

	@Test
	@Sql("classpath:test/yunxin/testPaymentNoStatusList.sql")
	public void testPaymentList_endDate_earlier_startDate() {

		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setQueryString("ashd&sabdlj");
		/*
		 * String paymentWay = "0"; String executingDeductStatus = "0";
		 */
		String AccountName = "payer_name_1";
		String paymentNo = "zxcvbnm";
		String repaymentNo = "DKHD-1-01";
		String billingNo = "DKHD-001-01-20160308";
		String bankNo = "pay_ac_no_1";
		String startDate = "2016-03-03";
		String endDate = "2016-03-04";
		Principal principal = principalService.load(Principal.class, 1l);
		Page page = new Page(1, 12);
		TransferApplicationQueryModel transferApplicationQueryModel = new TransferApplicationQueryModel(
				-1, -1, AccountName, paymentNo, repaymentNo, billingNo, bankNo,
				startDate, endDate);
		transferApplicationQueryModel.setFinancialContractIds("[\"1\"]");
//		ModelAndView modelAndView = yunxinPaymentController.paymentShowView(
//				request, transferApplicationQueryModel, principal, page);
//		Map<String, Object> map = modelAndView.getModel();
//		List<TransferApplication> transferApplications = (List<TransferApplication>) map
//				.get("transferApplicationList");
		
		String resultStr = yunxinPaymentController.paymentShowView(
				request, transferApplicationQueryModel, principal, page);
		Result result = JSON.parseObject(resultStr, Result.class);
		
		List<TransferApplication> transferApplicationList = (List<TransferApplication>) result.getData().get("list");
		Assert.assertEquals(0, transferApplicationList.size());

	}

	@Test
	@Sql("classpath:test/yunxin/settlementOrder/testListOrder.sql")
	public void testListOrder() {
		OrderQueryModel orderQueryModel = new OrderQueryModel();
		Page page = new Page();
		page.setEveryPage(12);

		orderQueryModel.setFinancialContractIds("[1]");
		orderQueryModel.setOrderNo("DKHD-001-02-201");
		orderQueryModel.setOverDueStatus(OverDueStatus.NOT_OVERDUE.ordinal());
		orderQueryModel.setSingleLoanContractNo("DKHD-001-02");
		orderQueryModel.setSettlementStartDateString("2016-10-09");
		orderQueryModel.setSettlementStartDateString("2016-10-09");
		orderQueryModel.setAssetRecycleStartDateString("2016-10-10");
		orderQueryModel.setAssetRecycleEndDateString("2016-10-10");
		String resultStr = yunxinPaymentOrderController.queryOrders(orderQueryModel, page, principal, request);
		Result result = JSON.parseObject(resultStr, Result.class);
		List<Order> orderList = JSON.parseArray(result.get("list").toString(), Order.class);
		assertEquals(2, orderList.size());
		Order order = orderList.get(1);
		AssetSet assetSet = repaymentPlanService.getUniqueRepaymentPlanByUuid(order.getAssetSetUuid());

		OrderAssetSet oa = new OrderAssetSet(order, assetSet);

		assertEquals(new Long(3L), oa.getOrder().getId());
		assertEquals("DKHD-001-02-20160409", oa.getOrder().getOrderNo());
		assertEquals("DKHD-001-02", oa.getSingleLoanContractNo());
		assertEquals("2016-10-10",
				DateUtils.format(oa.getAssetRecycleDate()));
		assertEquals("D001", oa.getCustomerNo());
		assertEquals(0,
				new BigDecimal("1000").compareTo(oa.getAssetInitialValue()));
		assertEquals(0, BigDecimal.ZERO.compareTo(oa.getPenaltyAmount()));
		assertEquals(new Integer(0), oa.getNumbersOfOverdueDays());
		assertEquals("2016-03-19", DateUtils.format(oa.getOrder().getModifyTime()));
		assertEquals(0, new BigDecimal("1000").compareTo(oa.getOrder().getTotalRent()));
		assertEquals(ExecutingSettlingStatus.DOING,
				order.getExecutingSettlingStatus());
	}
	
	@Test
	@Sql("classpath:test/yunxin/settlementOrder/testListOrder.sql")
	public void testQueryOrders(){
		OrderQueryModel orderQueryModel = new OrderQueryModel();
		Page page = new Page();
		page.setEveryPage(12);
		
		orderQueryModel.setFinancialContractIds("[1]");
		orderQueryModel.setOrderNo("DKHD-001-02-201");
		orderQueryModel.setOverDueStatus(OverDueStatus.NOT_OVERDUE.ordinal());
		orderQueryModel.setSingleLoanContractNo("DKHD-001-02");
		orderQueryModel.setSettlementStartDateString("2016-10-09");
		orderQueryModel.setSettlementStartDateString("2016-10-09");
		orderQueryModel.setAssetRecycleStartDateString("2016-10-10");
		orderQueryModel.setAssetRecycleEndDateString("2016-10-10");
		String resultJson = yunxinPaymentOrderController.queryOrders(orderQueryModel, page, principal, request);
		Result result = JsonUtils.parse(resultJson,Result.class);
		assertTrue(result.isValid());
		List<Order> orderList = JSON.parseArray((result.get("list").toString()),Order.class);

		assertEquals(2, orderList.size());
		Order order = orderList.get(1);
		AssetSet assetSet = repaymentPlanService.getUniqueRepaymentPlanByUuid(order.getAssetSetUuid());

		OrderAssetSet oa = new OrderAssetSet(order, assetSet);


		assertEquals(new Long(3L), oa.getOrder().getId());
		assertEquals("DKHD-001-02-20160409", oa.getOrder().getOrderNo());
		assertEquals("DKHD-001-02", oa.getSingleLoanContractNo());
		assertEquals("2016-10-10",
				DateUtils.format(oa.getAssetRecycleDate()));
		assertEquals("D001", oa.getCustomerNo());
		assertEquals(0,
				new BigDecimal("1000").compareTo(oa.getAssetInitialValue()));
		assertEquals(0, BigDecimal.ZERO.compareTo(oa.getPenaltyAmount()));
		assertEquals(new Integer(0), oa.getNumbersOfOverdueDays());
		assertEquals("2016-03-19", DateUtils.format(oa.getOrder().getModifyTime()));
		assertEquals(0, new BigDecimal("1000").compareTo(oa.getOrder().getTotalRent()));
		assertEquals(ExecutingSettlingStatus.DOING,
				oa.getOrder().getExecutingSettlingStatus());
	}

	@Test
	@Sql("classpath:test/yunxin/settlementOrder/testOrderDetailView.sql")
	public void testShowOrderDetail() {
		
//		Page page = new Page(1, 12);
//		ModelAndView modelAndView = yunxinPaymentOrderController.showOrderDetail(2L);
//		assertEquals("error",modelAndView.getViewName());
//
//		page = new Page(1, 12);
//		modelAndView = yunxinPaymentOrderController.showOrderDetail(1L);
//		assertEquals("yunxin/payment/order-show-detail",modelAndView.getViewName());
//		OrderViewDetail orderDetail = (OrderViewDetail) modelAndView.getModel().get("orderViewDetail");

		String result_jstr = yunxinPaymentOrderController.showOrderDetail(1L);
		Result result = JSON.parseObject(result_jstr,Result.class );
		OrderViewDetail orderDetail = JSON.parseObject(JSON.toJSONString(result.get("detail")), OrderViewDetail.class);

//		OrderViewDetail orderDetail = (OrderViewDetail) result.get("detail");

		assertNotNull(orderDetail);

//		Order orderView = orderDetail.getOrder();
		assertNotNull(orderDetail);
		assertEquals(new Long(1L), orderDetail.getOrderId());
//		assertEquals("DKHD-001-01-20160307", orderView.getOrderNo());
		assertEquals("DKHD-001-01-20160307", orderDetail.getOrderNo());
//		Customer customer = orderDetail.getCustomer();
//		assertEquals("D001", customer.getSource());
//		ContractAccount contractAccount = orderDetail.getContractAccount();
//		assertEquals("payer_name_1", contractAccount.getPayerName());
//		assertEquals("pay_ac_no_1", contractAccount.getPayAcNo());
		assertEquals("pay_ac_no_1", orderDetail.getCustomerAccount());
//		assertEquals("bind_id", contractAccount.getBindId());

		List<DeductApplication> deductApplications = (List<DeductApplication>) orderDetail.getDeductApplications();
		assertEquals(2, deductApplications.size());
		assertEquals("9d13d98f-6b5a-47f0-ae0c-537bd54afcc1", deductApplications.get(0).getDeductApplicationUuid());
		assertEquals("9d13d98f-6b5a-47f0-ae0c-537bd54afcc2", deductApplications.get(1).getDeductApplicationUuid());
		
		List<PaymentRecordModel> paymentRecordModels = (List<PaymentRecordModel>) orderDetail.getPaymentRecordModels();
		assertEquals(4, paymentRecordModels.size());
		assertEquals(1, paymentRecordModels.get(0).getRecordType());
		assertEquals("transfer_application_no_1", paymentRecordModels.get(0).getPaymentRecordNo());
		assertEquals(2, paymentRecordModels.get(2).getRecordType());
		assertEquals("304b26c6-de3e-4f65-bd21-57dc3bd87dc3", paymentRecordModels.get(2).getPaymentRecordNo());
		assertEquals(4, paymentRecordModels.get(3).getRecordType());
		assertEquals("8d1fce0d-80c7-11e6-b7d3-00163e002839", paymentRecordModels.get(3).getPaymentRecordNo());
	}

//	@Test
//	@Sql("classpath:test/yunxin/settlementOrder/testOrderDetailView.sql")
//	public void testShowOrderDetailForValid() {
//		Page page = new Page(1, 12);
//		ModelAndView modelAndView = yunxinPaymentOrderController.showOrderDetail(1L,principal, request,page);
//		assertEquals("yunxin/payment/order-show-detail",modelAndView.getViewName());
//		assertNotNull(modelAndView.getModel().get("orderViewDetail"));
//
//	}
	
	@Test
	@Sql("classpath:test/yunxin/editOrder/edit_order_amount_and_value_asset.sql")
	public void testPreEditOrder(){
		String repaymentBillUuid = "repayment_bill_id_1";
		ModelAndView modelAndView = yunxinPaymentOrderController.preEditOrder(repaymentBillUuid, principal, request);
		assertEquals("error-modal",modelAndView.getViewName());
		
		repaymentBillUuid = "repayment_bill_id_2";
		modelAndView = yunxinPaymentOrderController.preEditOrder(repaymentBillUuid, principal, request);
		assertEquals("error-modal",modelAndView.getViewName());
		
		repaymentBillUuid = "repayment_bill_id_3";
		modelAndView = yunxinPaymentOrderController.preEditOrder(repaymentBillUuid, principal, request);
		assertEquals("yunxin/payment/order-pre-edit",modelAndView.getViewName());
		Order order = (Order)modelAndView.getModel().get("order");
		
		assertEquals(repaymentBillUuid,order.getRepaymentBillId());
	}
	
	@Test
	@Sql("classpath:test/yunxin/settlementOrder/testChargesDetail.sql")
	public void testChargesDetail(){
		Long orderId = 4l;
		String resultStr = yunxinPaymentOrderController.chargesDetail(orderId);
		Result result = JSON.parseObject(resultStr, Result.class);
		Map<String, Object> data = result.getData();
		assertEquals("结算单不存在！",result.getMessage());
		
//		RepaymentChargesDetail repaymentChargesDetail = new RepaymentChargesDetail();
//		repaymentChargesDetail.setLoanAssetInterest(new BigDecimal(1.11));
//		repaymentChargesDetail.setLoanAssetPrincipal(new BigDecimal(2.22));
//		String data = repaymentChargesDetail.toJsonString();
//		assertNotNull(data);
		
		orderId = 1l;
		resultStr = yunxinPaymentOrderController.chargesDetail(orderId);
		result = JSON.parseObject(resultStr, Result.class);
		RepaymentChargesDetail  de =JSON.parseObject(result.getData().get("data").toString(), RepaymentChargesDetail.class);
		assertEquals("成功",result.getMessage());
		assertEquals(new BigDecimal("1.11"),de.getLoanAssetInterest());
		assertEquals(new BigDecimal("2.22"),de.getLoanAssetPrincipal());
		assertEquals(new BigDecimal("3.33"),de.getTotalFee());
		assertEquals(BigDecimal.ZERO,de.getTotalOverdueFee());
	}
	
}
