package com.zufangbao.earth.web.controller.repayment;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.demo2do.core.entity.Result;
import com.demo2do.core.utils.DateUtils;
import com.demo2do.core.utils.JsonUtils;
import com.suidifu.hathaway.job.ExecutingResult;
import com.suidifu.hathaway.job.ExecutingStatus;
import com.suidifu.hathaway.job.Job;
import com.suidifu.hathaway.job.Priority;
import com.suidifu.hathaway.job.service.JobService;
import com.zufangbao.earth.service.PrincipalService;
import com.zufangbao.earth.yunxin.api.controller.Api_V3_Controller;
import com.zufangbao.earth.yunxin.api.handler.PaymentOrderApiHandler;
import com.zufangbao.earth.yunxin.web.controller.RepaymentOrderController;
import com.zufangbao.sun.api.model.deduct.*;
import com.zufangbao.sun.api.model.repayment.PaymentOrderRequestModel;
import com.zufangbao.sun.entity.account.AccountSide;
import com.zufangbao.sun.entity.directbank.business.CashFlow;
import com.zufangbao.sun.entity.payment.order.PaymentOrderCashFlowShowModel;
import com.zufangbao.sun.entity.payment.order.RepaymentOrderVoucherShowModel;
import com.zufangbao.sun.entity.repayment.order.*;
import com.zufangbao.sun.entity.security.Principal;
import com.zufangbao.sun.ledgerbook.ChartOfAccount;
import com.zufangbao.sun.service.CashFlowService;
import com.zufangbao.sun.service.PaymentOrderService;
import com.zufangbao.sun.service.RepaymentOrderService;
import com.zufangbao.sun.utils.StringUtils;
import com.zufangbao.sun.yunxin.api.deduct.service.DeductApplicationDetailService;
import com.zufangbao.sun.yunxin.api.deduct.service.DeductApplicationService;
import com.zufangbao.sun.yunxin.api.deduct.service.DeductPlanService;
import com.zufangbao.sun.yunxin.entity.RepaymentOrderDetailModel;
import com.zufangbao.sun.yunxin.entity.api.deduct.*;
import com.zufangbao.sun.yunxin.entity.audit.ClearingStatus;
import com.zufangbao.sun.yunxin.entity.barclays.ThirdPartyTransactionRecord;
import com.zufangbao.sun.yunxin.entity.model.api.ExtraChargeSpec;
import com.zufangbao.sun.yunxin.entity.model.repaymentOrder.RepaymentOrderItemModel;
import com.zufangbao.sun.yunxin.entity.model.repaymentOrder.RepaymentOrderQueryModelForMerge;
import com.zufangbao.sun.yunxin.entity.model.thirdPartVoucher.VoucherLogIssueStatus;
import com.zufangbao.sun.yunxin.entity.model.thirdPartVoucher.VoucherLogStatus;
import com.zufangbao.sun.yunxin.entity.model.thirdPartVoucher.VoucherSource;
import com.zufangbao.sun.yunxin.entity.remittance.CertificateType;
import com.zufangbao.sun.yunxin.entity.remittance.TransactionRecipient;
import com.zufangbao.sun.yunxin.entity.repayment.RepaymentWay;
import com.zufangbao.sun.yunxin.entity.thirdPartVoucher.ThirdPartyPayExecStatus;
import com.zufangbao.sun.yunxin.entity.thirdPartVoucher.ThirdPartyPaymentVoucher;
import com.zufangbao.sun.yunxin.entity.thirdPartVoucher.ThirdPartyPaymentVoucherDetail;
import com.zufangbao.sun.yunxin.service.barclays.ThirdPartyTransactionRecordService;
import com.zufangbao.sun.yunxin.service.repayment.RepaymentOrderItemChargeService;
import com.zufangbao.sun.yunxin.service.repayment.RepaymentOrderItemCheckFailLogService;
import com.zufangbao.sun.yunxin.service.repayment.RepaymentOrderItemService;
import com.zufangbao.wellsfargo.silverpool.cashauditing.handler.BusinessPaymentVoucherSession;
import com.zufangbao.wellsfargo.silverpool.cashauditing.handler.CashFlowHandler;
import com.zufangbao.wellsfargo.silverpool.cashauditing.service.ThirdPartyPaymentVoucherDetailService;
import com.zufangbao.wellsfargo.silverpool.cashauditing.service.ThirdPartyPaymentVoucherService;
import com.zufangbao.wellsfargo.yunxin.handler.PaymentOrderHandler;
import com.zufangbao.wellsfargo.yunxin.handler.PaymentOrderHandlerProxy;
import com.zufangbao.wellsfargo.yunxin.handler.RepaymentOrderHandler;

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.SessionFactory;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.transaction.Transactional;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {

        "classpath:/local/applicationContext-*.xml",
        "classpath:/DispatcherServlet.xml"})

@WebAppConfiguration(value="webapp")
@Transactional
@Rollback(false)
public class RepaymentOrderControllerTest extends AbstractTransactionalJUnit4SpringContextTests {
    @Autowired
    private RepaymentOrderController repaymentOrderController;
    @Autowired
    private Api_V3_Controller api_V3_Controller;

    @Autowired
    private PaymentOrderApiHandler paymentOrderApiHandler;

    @Autowired
    private PrincipalService principalService;

    @Autowired
    private PaymentOrderService paymentOrderService;

    @Autowired
    private RepaymentOrderService repaymentOrderService;

    @Autowired
    private CashFlowService cashFlowService;

    @Autowired
    private PaymentOrderHandler paymentOrderHandler;

    @Autowired
    private CashFlowHandler cashFlowHandler;

    @Autowired
	private PaymentOrderHandlerProxy paymentOrderHandlerProxy;
    @Autowired
    private RepaymentOrderHandler repaymentOrderHandler;
    
    @Autowired
	private ThirdPartyTransactionRecordService thirdPartyTransactionRecordService;
    
    @Autowired
    private ThirdPartyPaymentVoucherService thirdPartyPaymentVoucherService;
    
    @Autowired
    private DeductApplicationService deductApplicationService; 
    
    @Autowired
    private DeductApplicationDetailService deductApplicationDetailService; 
    
    @Autowired
    private DeductPlanService deductPlanService; 
    
    @Autowired
    private ThirdPartyPaymentVoucherDetailService thirdPartyPaymentVoucherDetailService; 
    
    @Autowired
    private RepaymentOrderItemService repaymentOrderItemService;
    
    @Autowired
    private BusinessPaymentVoucherSession businessPaymentVoucherSession;
    
    @Autowired	
	private SessionFactory sessionFactory;
    
    @Autowired
    private RepaymentOrderItemChargeService repaymentOrderItemChargeService;
    
    @Autowired
    private JobService jobService;
    
    @Autowired
    private RepaymentOrderItemCheckFailLogService repaymentOrderItemCheckFailLogService;
    
    
    private MockHttpServletResponse response = new MockHttpServletResponse();

    private MockHttpServletRequest request = new MockHttpServletRequest();
    

    @Test
    @Sql("classpath:/test/yunxin/repaymentOrder/select_cashFlow_payment_order.sql")
    public void testSelectCashFlow() {

        Principal principal = principalService.load(Principal.class, 1l);
        String paymentOrderUuid = "paymentOrderUuid_1";

        String resultString = repaymentOrderController.selectCashFlow(request, principal, paymentOrderUuid);
        Result result = JsonUtils.parse(resultString, Result.class);

        PaymentOrder paymentOrder = paymentOrderService.getActivePaymenrOrderByUuid(paymentOrderUuid);


        Map<String, Object> map = result.getData();
        JSONArray paymentOrderCashFlowShowModelList = (JSONArray) map.get("list");
        List<PaymentOrderCashFlowShowModel> paymentOrderCashFlowShowModels = JsonUtils.parseArray(JSONObject.toJSONString(paymentOrderCashFlowShowModelList),
                PaymentOrderCashFlowShowModel.class);
        Assert.assertEquals(2, paymentOrderCashFlowShowModels.size());

        PaymentOrderCashFlowShowModel model1 = paymentOrderCashFlowShowModels.get(0);

        Assert.assertEquals("平账", model1.getAuditStatus());
        Assert.assertEquals("cash_flow_no_1", model1.getBankSequenceNo());
        Assert.assertEquals(new BigDecimal("1960.00"), model1.getCashFlowAmount());
        Assert.assertEquals("cash_flow_uuid_1", model1.getCashFlowUuid());
        Assert.assertEquals(paymentOrder.getCounterAccountName(), model1.getCountAccountName());
        Assert.assertEquals(paymentOrder.getCounterAccountNo(), model1.getCountAccountNo());
        Assert.assertEquals("remark_1", model1.getRemark());
        Assert.assertEquals(DateUtils.parseDate("2016-09-05 10:51:02", com.zufangbao.sun.utils.DateUtils.LONG_DATE_FORMAT), model1.getTransactionTime());

        PaymentOrderCashFlowShowModel model2 = paymentOrderCashFlowShowModels.get(1);

        Assert.assertEquals("部分平账", model2.getAuditStatus());
        Assert.assertEquals("cash_flow_no_2", model2.getBankSequenceNo());
        Assert.assertEquals(new BigDecimal("1960.00"), model2.getCashFlowAmount());
        Assert.assertEquals("cash_flow_uuid_2", model2.getCashFlowUuid());
        Assert.assertEquals(paymentOrder.getCounterAccountName(), model2.getCountAccountName());
        Assert.assertEquals(paymentOrder.getCounterAccountNo(), model2.getCountAccountNo());
        Assert.assertEquals("remark_2", model2.getRemark());
        Assert.assertEquals(DateUtils.parseDate("2016-09-06 10:51:02", com.zufangbao.sun.utils.DateUtils.LONG_DATE_FORMAT), model2.getTransactionTime());
    }

    @Test
    @Sql("classpath:/test/yunxin/repaymentOrder/select_cashFlow_payment_order.sql")
    public void testSelectCashFlowAfter() {

        Principal principal = principalService.load(Principal.class, 1l);
        String paymentOrderUuid = "paymentOrderUuid_1";
        String cashFlowUuid = "cash_flow_uuid_1";

        String resultString = repaymentOrderController.selectCashFlowAfter(request, principal, paymentOrderUuid, cashFlowUuid);
        Result result = JsonUtils.parse(resultString, Result.class);

        Assert.assertEquals("0", result.getCode());
        Assert.assertEquals("成功", result.getMessage());

        PaymentOrder paymentOrder = paymentOrderService.getActivePaymenrOrderByUuid(paymentOrderUuid);

        RepaymentOrder order = repaymentOrderService.getRepaymentOrderByUuid(paymentOrder.getOrderUuid());

        CashFlow cashFlow = cashFlowService.getCashFlowByCashFlowUuid(cashFlowUuid);


        Assert.assertEquals(OrderPayStatus.PAYING, order.getOrderPayStatus());
        Assert.assertEquals("cash_flow_uuid_1", paymentOrder.getOutlierDocumentUuid());
        Assert.assertEquals("cash_flow_no_1", paymentOrder.getOutlierDocumentIdentity());
        Assert.assertEquals(RepaymentOrderPayResult.PAY_SUCCESS, paymentOrder.getOrderPayResultStatus());
        Assert.assertEquals(cashFlow.getTransactionTime(), paymentOrder.getCashFlowTime());
    }

    @Test
    @Sql("classpath:/test/yunxin/repaymentOrder/update_payment_order_status_fail.sql")
    public void testUpdatePaymentOrderStatus() {

        Principal principal = principalService.load(Principal.class, 1l);
        String paymentOrderUuid = "paymentOrderUuid_1";
        String cashFlowUuid = "cash_flow_uuid_1";

        String resultString = repaymentOrderController.updatePaymentOrderFail(request, principal, paymentOrderUuid, 2, "");
        Result result = JsonUtils.parse(resultString, Result.class);

        Assert.assertEquals("0", result.getCode());
        Assert.assertEquals("成功", result.getMessage());

        PaymentOrder paymentOrder = paymentOrderService.getActivePaymenrOrderByUuid(paymentOrderUuid);

        RepaymentOrder order = repaymentOrderService.getRepaymentOrderByUuid(paymentOrder.getOrderUuid());

        Assert.assertEquals(RepaymentOrderPayResult.PAY_FAIL, paymentOrder.getOrderPayResultStatus());
        Assert.assertEquals(OrderPayStatus.PAY_ABNORMAL, order.getOrderPayStatus());
    }

    @Test
    @Sql("classpath:/test/yunxin/repaymentOrder/update_payment_order_status_fail2.sql")
    public void testUpdatePaymentOrderStatus2() {

        Principal principal = principalService.load(Principal.class, 1l);
        String paymentOrderUuid = "paymentOrderUuid_1";
        String cashFlowUuid = "cash_flow_uuid_1";

        String resultString = repaymentOrderController.updatePaymentOrderFail(request, principal, paymentOrderUuid, 2, "");
        Result result = JsonUtils.parse(resultString, Result.class);

        Assert.assertEquals("0", result.getCode());
        Assert.assertEquals("成功", result.getMessage());

        PaymentOrder paymentOrder = paymentOrderService.getActivePaymenrOrderByUuid(paymentOrderUuid);

        RepaymentOrder order = repaymentOrderService.getRepaymentOrderByUuid(paymentOrder.getOrderUuid());

        Assert.assertEquals(RepaymentOrderPayResult.PAY_FAIL, paymentOrder.getOrderPayResultStatus());
        Assert.assertEquals(OrderPayStatus.PAYING, order.getOrderPayStatus());
    }


    @Test
    @Sql("classpath:/test/yunxin/repaymentOrder/match_cash_flow_for_payment_order.sql")
    public void testMatchCashFlow() {

        Principal principal = principalService.load(Principal.class, 1l);
        String paymentOrderUuid = "paymentOrderUuid_1";
        String cashFlowUuid = "cash_flow_uuid_1";
        String accountNo = "600000000001";

        PaymentOrder paymentOrder = paymentOrderService.getActivePaymenrOrderByUuid(paymentOrderUuid);

        RepaymentOrder order = repaymentOrderService.getRepaymentOrderByUuid(paymentOrder.getOrderUuid());
        CashFlow cashFlow = cashFlowService.getCashFlowByCashFlowUuid(cashFlowUuid);

        paymentOrderHandler.paymentOrderMatchCashFlow(order, paymentOrder, accountNo);

        //匹配到一条流水
        Assert.assertEquals(OrderPayStatus.PAYING, order.getOrderPayStatus());
        Assert.assertEquals("cash_flow_uuid_1", paymentOrder.getOutlierDocumentUuid());
        Assert.assertEquals("cash_flow_no_1", paymentOrder.getOutlierDocumentIdentity());
        Assert.assertEquals(RepaymentOrderPayResult.PAY_SUCCESS, paymentOrder.getOrderPayResultStatus());
        Assert.assertEquals(cashFlow.getTransactionTime(), paymentOrder.getCashFlowTime());
    }

    @Test
    @Sql("classpath:/test/yunxin/repaymentOrder/match_cash_flow_for_payment_order.sql")
    public void testMatchCashFlow2() {

        Principal principal = principalService.load(Principal.class, 1l);
        String paymentOrderUuid = "paymentOrderUuid_1";
        String cashFlowUuid = "cash_flow_uuid_1";

        PaymentOrder paymentOrder = paymentOrderService.getActivePaymenrOrderByUuid(paymentOrderUuid);

        RepaymentOrder order = repaymentOrderService.getRepaymentOrderByUuid(paymentOrder.getOrderUuid());
        CashFlow cashFlow = cashFlowService.getCashFlowByCashFlowUuid(cashFlowUuid);

        String resultString = repaymentOrderController.matchCashFlow(request, principal, paymentOrderUuid);
        Result result = JsonUtils.parse(resultString, Result.class);

        Assert.assertEquals("0", result.getCode());
        Assert.assertEquals("成功", result.getMessage());

        Assert.assertEquals(OrderPayStatus.PAYING, order.getOrderPayStatus());
        Assert.assertEquals("cash_flow_uuid_1", paymentOrder.getOutlierDocumentUuid());
        Assert.assertEquals("cash_flow_no_1", paymentOrder.getOutlierDocumentIdentity());
        Assert.assertEquals(RepaymentOrderPayResult.PAY_SUCCESS, paymentOrder.getOrderPayResultStatus());
        Assert.assertEquals(cashFlow.getTransactionTime(), paymentOrder.getCashFlowTime());
    }

    @Test
    @Sql("classpath:/test/yunxin/repaymentOrder/match_cash_flow_for_payment_order2.sql")
    public void testMatchCashFlow3() {

        Principal principal = principalService.load(Principal.class, 1l);
        String paymentOrderUuid = "paymentOrderUuid_1";
        String cashFlowUuid = "cash_flow_uuid_1";

        PaymentOrder paymentOrder = paymentOrderService.getActivePaymenrOrderByUuid(paymentOrderUuid);

        RepaymentOrder order = repaymentOrderService.getRepaymentOrderByUuid(paymentOrder.getOrderUuid());
        CashFlow cashFlow = cashFlowService.getCashFlowByCashFlowUuid(cashFlowUuid);

        String resultString = repaymentOrderController.matchCashFlow(request, principal, paymentOrderUuid);
        Result result = JsonUtils.parse(resultString, Result.class);

        Assert.assertEquals("0", result.getCode());
        Assert.assertEquals("成功", result.getMessage());
    }

    @Test
    @Sql("classpath:/test/yunxin/repaymentOrder/get_cash_flow_for_payment_order.sql")
    public void testGetCashFlowBy() {

        Principal principal = principalService.load(Principal.class, 1l);
        String paymentOrderUuid = "paymentOrderUuid_1";
        String cashFlowUuid = "cash_flow_uuid_1";
        String accountNo = "600000000001";

        PaymentOrder paymentOrder = paymentOrderService.getActivePaymenrOrderByUuid(paymentOrderUuid);

        RepaymentOrder order = repaymentOrderService.getRepaymentOrderByUuid(paymentOrder.getOrderUuid());
        CashFlow cashFlow = cashFlowService.getCashFlowByCashFlowUuid(cashFlowUuid);


        List<CashFlow> cashFlowList = cashFlowHandler.getCashFlowBy(accountNo, paymentOrder, Boolean.TRUE, Boolean.TRUE);
        Assert.assertEquals(0, cashFlowList.size());
    }

    @Test
    @Sql("classpath:/test/yunxin/repaymentOrder/get_cash_flow_for_payment_order2.sql")
    public void testGetCashFlowBy2() {

        Principal principal = principalService.load(Principal.class, 1l);
        String paymentOrderUuid = "paymentOrderUuid_1";
        String cashFlowUuid = "cash_flow_uuid_1";
        String accountNo = "600000000001";

        PaymentOrder paymentOrder = paymentOrderService.getActivePaymenrOrderByUuid(paymentOrderUuid);

        RepaymentOrder order = repaymentOrderService.getRepaymentOrderByUuid(paymentOrder.getOrderUuid());
        CashFlow cashFlow = cashFlowService.getCashFlowByCashFlowUuid(cashFlowUuid);


        List<CashFlow> cashFlowList = cashFlowHandler.getCashFlowBy(accountNo, paymentOrder, Boolean.FALSE, Boolean.TRUE);
        Assert.assertEquals(1, cashFlowList.size());

        Assert.assertEquals("cash_flow_uuid_1", cashFlowList.get(0).getCashFlowUuid());
        Assert.assertEquals("付款户名不一致", paymentOrder.getRemark());
    }

    @Test
    @Sql("classpath:/test/yunxin/repaymentOrder/get_cash_flow_for_payment_order3.sql")
    public void testGetCashFlowBy3() {

        Principal principal = principalService.load(Principal.class, 1l);
        String paymentOrderUuid = "paymentOrderUuid_1";
        String cashFlowUuid = "cash_flow_uuid_1";
        String accountNo = "600000000001";

        PaymentOrder paymentOrder = paymentOrderService.getActivePaymenrOrderByUuid(paymentOrderUuid);

        RepaymentOrder order = repaymentOrderService.getRepaymentOrderByUuid(paymentOrder.getOrderUuid());
        CashFlow cashFlow = cashFlowService.getCashFlowByCashFlowUuid(cashFlowUuid);


        List<CashFlow> cashFlowList = cashFlowHandler.getCashFlowBy(accountNo, paymentOrder, Boolean.FALSE, Boolean.TRUE);
        Assert.assertEquals(2, cashFlowList.size());

        Assert.assertEquals("cash_flow_uuid_1", cashFlowList.get(0).getCashFlowUuid());
        Assert.assertEquals("cash_flow_uuid_2", cashFlowList.get(1).getCashFlowUuid());
    }


    @Test
    @Sql("classpath:/test/yunxin/repaymentOrder/match_cash_flow_for_payment_order.sql")
    public void testUpdatePaymentOrderFail() {

        String paymentOrderUuid = "paymentOrderUuid_1";
        String cashFlowUuid = "cash_flow_uuid_1";

        PaymentOrder paymentOrder = paymentOrderService.getActivePaymenrOrderByUuid(paymentOrderUuid);

        RepaymentOrder order = repaymentOrderService.getRepaymentOrderByUuid(paymentOrder.getOrderUuid());
        CashFlow cashFlow = cashFlowService.getCashFlowByCashFlowUuid(cashFlowUuid);

        paymentOrderHandler.updatePaymentOrderAndRpaymentOrderFail(paymentOrder, order, 2, "haodebeee");

        Assert.assertEquals(OrderPayStatus.PAYING, order.getOrderPayStatus());
        Assert.assertEquals(RepaymentOrderPayResult.PAY_FAIL, paymentOrder.getOrderPayResultStatus());
        Assert.assertEquals(PayStatus.PAY_FAIL, paymentOrder.getPayStatus());
        Assert.assertEquals("haodebeee", paymentOrder.getRemark());
    }

    @Test
    @Sql("classpath:/test/yunxin/repaymentOrder/match_cash_flow_for_payment_order.sql")
    public void testUpdatePaymentOrderAndOrderStatus() {

        String paymentOrderUuid = "paymentOrderUuid_1";
        String cashFlowUuid = "cash_flow_uuid_1";

        PaymentOrder paymentOrder = paymentOrderService.getActivePaymenrOrderByUuid(paymentOrderUuid);

        RepaymentOrder order = repaymentOrderService.getRepaymentOrderByUuid(paymentOrder.getOrderUuid());
        CashFlow cashFlow = cashFlowService.getCashFlowByCashFlowUuid(cashFlowUuid);

        paymentOrderHandler.updatePaymentOrderAndOrderStatus(cashFlow, paymentOrder, order);

        Assert.assertEquals(OrderPayStatus.PAYING, order.getOrderPayStatus());
        Assert.assertEquals("cash_flow_uuid_1", paymentOrder.getOutlierDocumentUuid());
        Assert.assertEquals("cash_flow_no_1", paymentOrder.getOutlierDocumentIdentity());
        Assert.assertEquals(RepaymentOrderPayResult.PAY_SUCCESS, paymentOrder.getOrderPayResultStatus());
        Assert.assertEquals(PayStatus.PAY_SUCCESS, paymentOrder.getPayStatus());
    }

    @Test
    @Sql("classpath:/test/yunxin/repaymentOrder/test_PaymentOrder_online_deduct_online_payment.sql")
    public void testCommandApiPaymentOrderOnline() {

        PaymentOrderRequestModel commandModel = new PaymentOrderRequestModel();
        commandModel.setAmount(new BigDecimal("1000").toString());
        commandModel.setFinancialContractNo("G08200");
        commandModel.setOrderUniqueId("");
        commandModel.setOrderUuid("order_uuid_1");
        commandModel.setPaymentAccountName("范腾");
        commandModel.setPaymentAccountNo("6214855712107780");
        commandModel.setPaymentBankCode("C10102");
        commandModel.setPaymentNo("payment_no");
        commandModel.setRequestNo("edtfgrfgttr");
        commandModel.setPayWay("0");
        commandModel.setMobile("");
        commandModel.setTransactionTime("2017-04-17 00:00:00");
        commandModel.setTradeUuid("fdgfdz");
        commandModel.setReceivableBankCode("C10105"); //中国建设银行

        try {
            List<PaymentOrder> paymentOrders = paymentOrderService.loadAll(PaymentOrder.class);
            Assert.assertEquals(2, paymentOrders.size());

            paymentOrderHandler.offlineTransferPaymentOrderPay("order_uuid_1", commandModel,"", Priority.High.getPriority());

//			String message = commandApiPaymentOrderController.commandRepaymentOrder(request, response, commandModel);

            List<PaymentOrder> paymentOrderList = paymentOrderService.loadAll(PaymentOrder.class);
            Assert.assertEquals(3, paymentOrderList.size());
            paymentOrderList.removeAll(paymentOrders);

            PaymentOrder paymentOrder = paymentOrderList.get(2);

            RepaymentOrder order = repaymentOrderService.getRepaymentOrderByUuid(paymentOrder.getOrderUuid());
            Assert.assertEquals(OrderPayStatus.PAY_END, order.getOrderPayStatus());

            //校验 支付单  paymentOrder
            Assert.assertEquals(new BigDecimal("1000.00"), paymentOrder.getAmount());
            Assert.assertEquals(AccountSide.DEBIT, paymentOrder.getAccountSide());
            Assert.assertEquals("中国工商银行", paymentOrder.getCounterAccountBankName());
            Assert.assertEquals("范腾", paymentOrder.getCounterAccountName());
            Assert.assertEquals("6214855712107780", paymentOrder.getCounterAccountNo());
            Assert.assertEquals("中国建设银行", paymentOrder.getHostAccountBankName());
            Assert.assertEquals("", paymentOrder.getHostAccountName());
            Assert.assertEquals("", paymentOrder.getHostAccountNo());
            Assert.assertEquals(AliveStatus.CREATE, paymentOrder.getAliveStatus());
            Assert.assertEquals("financial_contract_uuid_1", paymentOrder.getFinancialContractUuid());
            Assert.assertEquals("G08200", paymentOrder.getFinancialContractNo());
            Assert.assertEquals(RepaymentOrderPayResult.PAY_SUCCESS, paymentOrder.getOrderPayResultStatus());
            Assert.assertEquals("cash_flow_no_1", paymentOrder.getOutlierDocumentIdentity());
            Assert.assertEquals("cash_flow_uuid_1", paymentOrder.getOutlierDocumentUuid());
            Assert.assertEquals(PayWay.OFFLINE_TRANSFER, paymentOrder.getPayWay());
            Assert.assertEquals(RecoverStatus.UNRECORDED, paymentOrder.getRecoverStatus());
            Assert.assertEquals("edtfgrfgttr", paymentOrder.getRequestNo());
            Assert.assertEquals("orderUniqueId", paymentOrder.getOrderUniqueId());
            Assert.assertEquals("order_uuid_1", paymentOrder.getOrderUuid());
            Assert.assertEquals("fdgfdz", paymentOrder.getTradeUuid());
            Assert.assertEquals("payment_no", paymentOrder.getPaymentNo());
            Assert.assertEquals(PayStatus.PAY_SUCCESS, paymentOrder.getPayStatus());
            Assert.assertEquals(DateUtils.parseDate("2017-04-17 00:00:000", com.zufangbao.sun.utils.DateUtils.LONG_DATE_FORMAT), paymentOrder.getTransactionTime());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    @Sql("classpath:/test/yunxin/repaymentOrder/test_PaymentOrder_online_deduct_online_payment2.sql")
    public void testCommandApiPaymentOrderOnline2() {

        PaymentOrderRequestModel commandModel = new PaymentOrderRequestModel();
        commandModel.setAmount(new BigDecimal("1000").toString());
        commandModel.setFinancialContractNo("G08200");
        commandModel.setOrderUniqueId("");
        commandModel.setOrderUuid("order_uuid_1");
        commandModel.setPaymentAccountName("pay_account_name");
        commandModel.setPaymentAccountNo("pay_account_no");
        commandModel.setPaymentBankCode("C10102");
        commandModel.setPaymentNo("payment_no");
        commandModel.setRequestNo("edtfgrfgttr");
        commandModel.setPayWay("0");
        commandModel.setMobile("");
        commandModel.setTransactionTime("2017-04-17 00:00:00");
        commandModel.setTradeUuid("fdgfdz");
        commandModel.setReceivableBankCode("C10105"); //中国建设银行

        try {
            List<PaymentOrder> paymentOrders = paymentOrderService.loadAll(PaymentOrder.class);
            Assert.assertEquals(2, paymentOrders.size());

            paymentOrderHandler.offlineTransferPaymentOrderPay("order_uuid_1", commandModel,"", Priority.High.getPriority());

//			String message = commandApiPaymentOrderController.commandRepaymentOrder(request, response, commandModel);
			
			List<PaymentOrder> paymentOrderList = paymentOrderService.loadAll(PaymentOrder.class);
			Assert.assertEquals(3, paymentOrderList.size());
			paymentOrderList.removeAll(paymentOrders);
			
			PaymentOrder paymentOrder = paymentOrderList.get(2);
			
			RepaymentOrder order = repaymentOrderService.getRepaymentOrderByUuid(paymentOrder.getOrderUuid());
			Assert.assertEquals(OrderPayStatus.PAYING, order.getOrderPayStatus());
			
			//校验 支付单  paymentOrder
			Assert.assertEquals(new BigDecimal("1000.00"), paymentOrder.getAmount());
			Assert.assertEquals(AccountSide.DEBIT, paymentOrder.getAccountSide());
			Assert.assertEquals("中国工商银行", paymentOrder.getCounterAccountBankName());
			Assert.assertEquals("pay_account_name", paymentOrder.getCounterAccountName());
			Assert.assertEquals("pay_account_no", paymentOrder.getCounterAccountNo());
			Assert.assertEquals("中国建设银行", paymentOrder.getHostAccountBankName());
			Assert.assertEquals("", paymentOrder.getHostAccountName());
			Assert.assertEquals("", paymentOrder.getHostAccountNo());
			Assert.assertEquals(AliveStatus.CREATE, paymentOrder.getAliveStatus());
			Assert.assertEquals("financial_contract_uuid_1", paymentOrder.getFinancialContractUuid());
			Assert.assertEquals("G08200", paymentOrder.getFinancialContractNo());
			Assert.assertEquals(RepaymentOrderPayResult.PAYING, paymentOrder.getOrderPayResultStatus());
			Assert.assertEquals(null, paymentOrder.getOutlierDocumentIdentity());
			Assert.assertEquals(null, paymentOrder.getOutlierDocumentUuid());
			Assert.assertEquals(PayWay.OFFLINE_TRANSFER, paymentOrder.getPayWay());
			Assert.assertEquals(RecoverStatus.UNRECORDED, paymentOrder.getRecoverStatus());
			Assert.assertEquals(PayStatus.IN_PAY, paymentOrder.getPayStatus());
			Assert.assertEquals("edtfgrfgttr", paymentOrder.getRequestNo());
			Assert.assertEquals("orderUniqueId", paymentOrder.getOrderUniqueId());
			Assert.assertEquals("order_uuid_1", paymentOrder.getOrderUuid());
			Assert.assertEquals("fdgfdz", paymentOrder.getTradeUuid());
			Assert.assertEquals("payment_no", paymentOrder.getPaymentNo());
			Assert.assertEquals(DateUtils.parseDate("2017-04-17 00:00:000",com.zufangbao.sun.utils.DateUtils.LONG_DATE_FORMAT), paymentOrder.getTransactionTime());
			
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	    @Sql("classpath:/test/yunxin/repaymentOrder/repayment_order_for_voucher_show_model.sql")
	    public void testQueryRepaymentOrderOfVoucher(){

	    	String orderUuid = "order_uuid_1";

	    	String resultString = repaymentOrderController.queryRepaymentOrderOfVoucher(orderUuid);
	    	Result result = JsonUtils.parse(resultString, Result.class);

	    	List<RepaymentOrderVoucherShowModel> shows = JSON.parseArray(result.get("list").toString(), RepaymentOrderVoucherShowModel.class);

	    	Assert.assertEquals("0",result.getCode());

	    	Assert.assertEquals(1,shows.size());
	    	RepaymentOrderVoucherShowModel repaymentOrderVoucherShowModel =  shows.get(0);

	    	Assert.assertEquals("ee28ab63-d097-42c8-b759-8c4a238f982e",repaymentOrderVoucherShowModel.getVoucherNo());
	    	Assert.assertEquals("6214855712117990",repaymentOrderVoucherShowModel.getPaymentAccountNo());
	    	Assert.assertEquals("中国农业银行",repaymentOrderVoucherShowModel.getPaymentBank());
	    	Assert.assertEquals("高渐",repaymentOrderVoucherShowModel.getPaymentName());
	    	Assert.assertEquals("6001",repaymentOrderVoucherShowModel.getReceivableAccountNo());
	    	Assert.assertEquals("",repaymentOrderVoucherShowModel.getRemark());
	    	Assert.assertEquals(new BigDecimal("980.00"),repaymentOrderVoucherShowModel.getTranscationAmount());
	    	Assert.assertEquals("已核销",repaymentOrderVoucherShowModel.getVoucherLogIssueStatus());
	    	Assert.assertEquals("线上支付单",repaymentOrderVoucherShowModel.getVoucherSource());
	    	Assert.assertEquals("",repaymentOrderVoucherShowModel.getVoucherType());

	    }

	@Test
	public void test_query_unclear_asset(){
		String lapsedItemUuid="order_detail_uuid_1";
		String result=repaymentOrderController.query_unclear_asset(lapsedItemUuid);
		Result result2=JsonUtils.parse(result, Result.class);
		Map<String, Object> modelmap=result2.getData();
		String modeljson=(String)modelmap.get("itemModel");
		List<RepaymentOrderItemModel> models=JsonUtils.parseArray(modeljson, RepaymentOrderItemModel.class);
		Assert.assertEquals(models.size(), 4);
	}

	@Test
//	@Sql("classpath:/test/yunxin/repaymentOrder/test_query_unclear_asset.sql")
	public void test_down_loan_file() throws Exception{
		repaymentOrderController.downloadFile(request, response);
		response.getContentAsByteArray();
		response.getOutputStream();
		File file = new File("/E:/data/b.zip");
		OutputStream os = new FileOutputStream(file);
		os.write(response.getContentAsByteArray());
//		OutputStream stream = mockHttpServletResponse.getOutputStream();
		os.close();
	}


	@Test
	@Sql("classpath:/test/yunxin/repaymentOrder/repayment_order_add.sql")
	public void test_repayment_order_add(){

		List<RepaymentOrderDetailModel> detailModels = new ArrayList<RepaymentOrderDetailModel>();
		List<RepaymentOrderDetailModel> detailModels2 = new ArrayList<RepaymentOrderDetailModel>();

		RepaymentOrderDetailModel model1 = new RepaymentOrderDetailModel();
		String detailsAmountJsonList1 = "[{\"actualAmount\":100.00,\"feeType\":\"1000\",\"feeTypeEnum\":\"PRINCIPAL\"},{\"actualAmount\":12.00,\"feeType\":\"1001\",\"feeTypeEnum\":\"INTEREST\"},{\"actualAmount\":5.00,\"feeType\":\"1002\",\"feeTypeEnum\":\"LOAN_SERVICE_FEE\"},{\"actualAmount\":0,\"feeType\":\"1003\",\"feeTypeEnum\":\"LOAN_TECH_FEE\"},{\"actualAmount\":0.00,\"feeType\":\"1004\",\"feeTypeEnum\":\"LOAN_OTHER_FEE\"},{\"actualAmount\":0.00,\"feeType\":\"1005\",\"feeTypeEnum\":\"PENALTY\"},{\"actualAmount\":0.00,\"feeType\":\"1006\",\"feeTypeEnum\":\"OVERDUE_FEE_OBLIGATION\"},{\"actualAmount\":0.00,\"feeType\":\"1007\",\"feeTypeEnum\":\"OVERDUE_FEE_SERVICE_FEE\"},{\"actualAmount\":0.00,\"feeType\":\"1008\",\"feeTypeEnum\":\"OVERDUE_FEE_OTHER_FEE\"}]";
		model1.setContractNo("contract_no_1");
		model1.setPlannedDate("2016-05-27 18:27:17");
		model1.setRepaymentAmount(new BigDecimal("117.00"));
		model1.setRepaymentBusinessNo("ZC123456");
		model1.setRepaymentWayCode("2001");
		model1.setRepayScheduleNo("repayScheduleNo1");
		model1.setDetailsAmountJsonList(detailsAmountJsonList1);

		RepaymentOrderDetailModel model2 = new RepaymentOrderDetailModel();
		String detailsAmountJsonList2 = "[{\"actualAmount\":200.00,\"feeType\":\"1000\",\"feeTypeEnum\":\"PRINCIPAL\"},{\"actualAmount\":50.00,\"feeType\":\"1001\",\"feeTypeEnum\":\"INTEREST\"},{\"actualAmount\":40.00,\"feeType\":\"1002\",\"feeTypeEnum\":\"LOAN_SERVICE_FEE\"},{\"actualAmount\":0.00,\"feeType\":\"1003\",\"feeTypeEnum\":\"LOAN_TECH_FEE\"},{\"actualAmount\":0.00,\"feeType\":\"1004\",\"feeTypeEnum\":\"LOAN_OTHER_FEE\"},{\"actualAmount\":0.00,\"feeType\":\"1005\",\"feeTypeEnum\":\"PENALTY\"},{\"actualAmount\":0.00,\"feeType\":\"1006\",\"feeTypeEnum\":\"OVERDUE_FEE_OBLIGATION\"},{\"actualAmount\":0.00,\"feeType\":\"1007\",\"feeTypeEnum\":\"OVERDUE_FEE_SERVICE_FEE\"},{\"actualAmount\":0.00,\"feeType\":\"1008\",\"feeTypeEnum\":\"OVERDUE_FEE_OTHER_FEE\"}]";
		model2.setContractNo("contract_no_2");
		model2.setPlannedDate("2016-10-10 18:27:17");
		model2.setRepaymentAmount(new BigDecimal("290.00"));
		model2.setRepaymentBusinessNo("ZC7890");
		model2.setRepaymentWayCode("2002");
		model2.setRepayScheduleNo("repayScheduleNo2");
		model2.setDetailsAmountJsonList(detailsAmountJsonList2);

		RepaymentOrderDetailModel model3 = new RepaymentOrderDetailModel();
		String detailsAmountJsonList3 = "[{\"actualAmount\":300.00,\"feeType\":\"2000\",\"feeTypeEnum\":\"REPURCHASE_PRINCIPAL\"},{\"actualAmount\":88.00,\"feeType\":\"2001\",\"feeTypeEnum\":\"REPURCHASE_INTEREST\"},{\"actualAmount\":0.00,\"feeType\":\"2002\",\"feeTypeEnum\":\"REPURCHASE_PENALTY\"},{\"actualAmount\":0.00,\"feeType\":\"2003\",\"feeTypeEnum\":\"REPURCHASE_OTHER_FEE\"}]";
		model3.setContractNo("contract_no_1");
		model3.setPlannedDate("");
		model3.setRepaymentAmount(new BigDecimal("388.00"));
		model3.setRepaymentBusinessNo("repurchaseDocUuid");
		model3.setRepaymentWayCode("2003");
		model3.setRepayScheduleNo("");
		model3.setDetailsAmountJsonList(detailsAmountJsonList3);

		detailModels.add(model1);
		detailModels.add(model2);
		detailModels2.add(model3);

		String modelsStr = JSON.toJSONString(detailModels);
		String modelRepurchaseSr = JSON.toJSONString(detailModels2);


		String financialContractNo = "G08200";

		repaymentOrderController.submitRepaymentOrder(request, response, modelsStr,modelRepurchaseSr, "remark", financialContractNo, "795.00");
//		repaymentOrderHandler.repaymentOrderAdd(detailModels, financialContractNo, "remark", new BigDecimal("500"), repaymentOrderPath, merchantId, sourceStatus);

		List<RepaymentOrder> orderList = repaymentOrderService.loadAll(RepaymentOrder.class);
		RepaymentOrder order = orderList.get(0);

		Assert.assertEquals("测试合同", order.getFinancialContractName());
		Assert.assertEquals("G08200", order.getFinancialContractNo());
		Assert.assertEquals("financial_contract_uuid_1", order.getFinancialContractUuid());
		Assert.assertEquals("", order.getFirstContractUuid());
		Assert.assertEquals("", order.getFirstCustomerName());
		Assert.assertEquals("", order.getFirstCustomerSource());
		Assert.assertEquals(RepaymentWayGroupType.ALTER_OFFLINE_REPAYMENT_ORDER_TYPE, order.getFirstRepaymentWayGroup());
		Assert.assertEquals("suidifu", order.getMerId());
		Assert.assertEquals("", order.getIp());
		Assert.assertEquals(OrderAliveStatus.PAYMENT_ORDER_CREATE, order.getOrderAliveStatus());
		Assert.assertEquals(new BigDecimal("795.00"), order.getOrderAmount());
		Assert.assertEquals(OrderCheckStatus.TO_VERIFY, order.getOrderCheckStatus());
		Assert.assertEquals(OrderDBStatus.PAYMENT_ORDER_NOT_DOWN, order.getOrderDbStatus());
		Assert.assertEquals(OrderPayStatus.TO_PAY, order.getOrderPayStatus());
		Assert.assertEquals(OrderRecoverResult.NONE, order.getOrderRecoverResult());
		Assert.assertEquals(OrderRecoverStatus.TO_PAY, order.getOrderRecoverStatus());
		Assert.assertEquals("", order.getOrderRequestNo());
		Assert.assertEquals("系统生成", order.getOrderSourceStatusCh());
		Assert.assertEquals("", order.getOrderUniqueId());
		Assert.assertEquals(BigDecimal.ZERO, order.getPaidAmount());
		Assert.assertEquals("remark", order.getRemark());

	}
	
	@Test 
	@Sql("classpath:/test/yunxin/repaymentOrder/test_PaymentOrder_business_deduct.sql")
	public void testCommandApiPaymentOrderBusinessDeduct(){
		
		PaymentOrderRequestModel commandModel = new PaymentOrderRequestModel();
		commandModel.setAmount(new BigDecimal("1000").toString());
		commandModel.setFinancialContractNo("G08200");
		commandModel.setOrderUniqueId("");
		commandModel.setOrderUuid("order_uuid_1");
		commandModel.setPaymentAccountName("pay_account_name");
		commandModel.setPaymentAccountNo("pay_account_no");
		commandModel.setPaymentBankCode("C10102");
		commandModel.setPaymentNo("payment_no");
		commandModel.setRequestNo("edtfgrfgttr");
		commandModel.setPayWay("3");
		commandModel.setMobile("");
		commandModel.setTransactionTime("2017-04-17 00:00:00");
		commandModel.setTradeUuid("qwerty;JIAOYIQINGQIU100_200");
		commandModel.setReceivableBankCode("C10105"); //中国建设银行
		commandModel.setPaymentGateWay("2");
		
		try {
			List<PaymentOrder> paymentOrders = paymentOrderService.loadAll(PaymentOrder.class);
			Assert.assertEquals(2, paymentOrders.size());
			
			paymentOrderHandler.businessDeductPaymentOrderPay("order_uuid_1", commandModel,"", Priority.High.getPriority());
			
//			String message = commandApiPaymentOrderController.commandRepaymentOrder(request, response, commandModel);
			
			List<PaymentOrder> paymentOrderList = paymentOrderService.loadAll(PaymentOrder.class);
			Assert.assertEquals(3, paymentOrderList.size());
			paymentOrderList.removeAll(paymentOrders);
			
			PaymentOrder paymentOrder = paymentOrderList.get(0);
			
			RepaymentOrder order = repaymentOrderService.getRepaymentOrderByUuid(paymentOrder.getOrderUuid());
			Assert.assertEquals(OrderPayStatus.PAYING, order.getOrderPayStatus());
			
			//校验 支付单  paymentOrder
			Assert.assertEquals(new BigDecimal("1000"), paymentOrder.getAmount());
			Assert.assertEquals(AccountSide.DEBIT, paymentOrder.getAccountSide());
			Assert.assertEquals("中国工商银行", paymentOrder.getCounterAccountBankName());
			Assert.assertEquals("pay_account_name", paymentOrder.getCounterAccountName());
			Assert.assertEquals("pay_account_no", paymentOrder.getCounterAccountNo());
			Assert.assertEquals("中国建设银行", paymentOrder.getHostAccountBankName());
			Assert.assertEquals("", paymentOrder.getHostAccountName());
			Assert.assertEquals("", paymentOrder.getHostAccountNo());
			Assert.assertEquals(AliveStatus.CREATE, paymentOrder.getAliveStatus());
			Assert.assertEquals("financial_contract_uuid_1", paymentOrder.getFinancialContractUuid());
			Assert.assertEquals("G08200", paymentOrder.getFinancialContractNo());
			Assert.assertEquals(RepaymentOrderPayResult.PAYING, paymentOrder.getOrderPayResultStatus());
			Assert.assertEquals(null, paymentOrder.getOutlierDocumentIdentity());
			Assert.assertEquals(null, paymentOrder.getOutlierDocumentUuid());
			Assert.assertEquals(PayWay.BUSINESS_DEDUCT, paymentOrder.getPayWay());
			Assert.assertEquals(RecoverStatus.UNRECORDED, paymentOrder.getRecoverStatus());
			Assert.assertEquals(PayStatus.IN_PAY, paymentOrder.getPayStatus());
			Assert.assertEquals("edtfgrfgttr", paymentOrder.getRequestNo());
			Assert.assertEquals("orderUniqueId", paymentOrder.getOrderUniqueId());
			Assert.assertEquals("order_uuid_1", paymentOrder.getOrderUuid());
			Assert.assertEquals("JIAOYIQINGQIU100_200", paymentOrder.getTradeUuid());
			Assert.assertEquals("payment_no", paymentOrder.getPaymentNo());
			Assert.assertEquals("qwerty", paymentOrder.getBatchNo());
			Assert.assertEquals(DateUtils.parseDate("2017-04-17 00:00:000",com.zufangbao.sun.utils.DateUtils.LONG_DATE_FORMAT), paymentOrder.getTransactionTime());
			
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//到重试次数，paymentOrder置失败
	@Test 
	@Sql("classpath:/test/yunxin/repaymentOrder/test_PaymentOrder_business_deduct.sql")
	public void testCommandApiPaymentOrderBusinessDeduct2(){
		
		paymentOrderHandler.handlerPaymentOrderForBusinessDeduct("order_uuid_1", "JIAOYIQINGQIU100_200",6);
		
		PaymentOrder paymentOrder = paymentOrderService.getPaymenrOrderByUuid("paymentOrderUuid_1");
		RepaymentOrder order = repaymentOrderService.getRepaymentOrderByUuid("order_uuid_1");
		Assert.assertEquals(PayStatus.PAY_FAIL, paymentOrder.getPayStatus());
		Assert.assertEquals(RepaymentOrderPayResult.PAY_FAIL, paymentOrder.getOrderPayResultStatus());
		Assert.assertEquals(OrderPayStatus.PAY_ABNORMAL, order.getOrderPayStatus());
		
	}
	
	//验真失败，paymentOrder置失败
	@Test 
	@Sql("classpath:/test/yunxin/repaymentOrder/test_PaymentOrder_business_deduct2.sql")
	public void testCommandApiPaymentOrderBusinessDeduct4(){
			
		PaymentOrder paymentOrderDb = paymentOrderService.getPaymenrOrderByUuid("paymentOrderUuid_1");
		paymentOrderDb.setRetriedTransactionRecordNums(6);
		paymentOrderService.saveOrUpdate(paymentOrderDb);
		
		paymentOrderHandler.handlerPaymentOrderForBusinessDeduct("order_uuid_1", "JIAOYIQINGQIU100_200",6);
		
		PaymentOrder paymentOrder = paymentOrderService.getPaymenrOrderByUuid("paymentOrderUuid_1");
		RepaymentOrder order = repaymentOrderService.getRepaymentOrderByUuid("order_uuid_1");
		Assert.assertEquals(PayStatus.PAY_FAIL, paymentOrder.getPayStatus());
		Assert.assertEquals(RepaymentOrderPayResult.PAY_FAIL, paymentOrder.getOrderPayResultStatus());
		Assert.assertEquals(OrderPayStatus.PAY_ABNORMAL, order.getOrderPayStatus());
			
	}
	
	//验真成功  生成第三方支付凭证和 生成deductPlan deductApplication deductApplicationDetail
	@Test 
	@Sql("classpath:/test/yunxin/repaymentOrder/test_PaymentOrder_business_deduct_transaction_record.sql")
	public void testCommandApiPaymentOrderBusinessDeduct3(){
		
		try {
			
			paymentOrderHandler.handlerPaymentOrderForBusinessDeduct("order_uuid_1", "JIAOYIQINGQIU100_200",6);
			
			PaymentOrder paymentOrder = paymentOrderService.getPaymenrOrderByUuid("paymentOrderUuid_1");
			RepaymentOrder order = repaymentOrderService.getRepaymentOrderByUuid("order_uuid_1");
			Assert.assertEquals(PayStatus.PAY_SUCCESS, paymentOrder.getPayStatus());
			Assert.assertEquals(RepaymentOrderPayResult.PAY_SUCCESS, paymentOrder.getOrderPayResultStatus());
			Assert.assertEquals(OrderPayStatus.PAY_END, order.getOrderPayStatus());
			Assert.assertEquals(new BigDecimal("2000.00"), order.getPaidAmount());
			
			List<RepaymentOrderItem> items = repaymentOrderItemService.getRepaymentOrderItems(order.getOrderUuid());
			
			
			ThirdPartyTransactionRecord transactionRecord = thirdPartyTransactionRecordService.getThirdPartyTransactionRecordBytradeUuid(paymentOrder.getTradeUuid());
			
			//校验第三方支付凭证
			List<ThirdPartyPaymentVoucher> thirdPartyPaymentVoucherList = thirdPartyPaymentVoucherService.loadAll(ThirdPartyPaymentVoucher.class);
			ThirdPartyPaymentVoucher paymentVoucher = thirdPartyPaymentVoucherList.get(0);
			Assert.assertEquals(paymentOrder.getTradeUuid(), paymentVoucher.getTradeUuid());
			Assert.assertEquals(paymentOrder.getFinancialContractNo(), paymentVoucher.getFinancialContractNo());
			Assert.assertEquals(paymentOrder.getFinancialContractUuid(), paymentVoucher.getFinancialContractUuid());
			Assert.assertEquals("contract_no", paymentVoucher.getContractNo());
			Assert.assertEquals(paymentOrder.getAmount(), paymentVoucher.getTranscationAmount());
			Assert.assertEquals(paymentOrder.getTransactionTime(), paymentVoucher.getTransactionTime());
			Assert.assertEquals(paymentOrder.getPaymentGateWay(), paymentVoucher.getTranscationGateway());
			Assert.assertEquals(ThirdPartyPayExecStatus.SUCCESS, paymentVoucher.getExecutionStatus());
			Assert.assertEquals( VoucherLogStatus.PROCESSING, paymentVoucher.getProcessStatus());
			Assert.assertEquals(VoucherLogStatus.SUCCESS, paymentVoucher.getVoucherLogStatus());
			Assert.assertEquals(VoucherLogIssueStatus.NOT_ISSUED, paymentVoucher.getVoucherLogIssueStatus());
			Assert.assertEquals(paymentOrder.getHostAccountNo(), paymentVoucher.getReceivableAccountNo());
			Assert.assertEquals(paymentOrder.getCounterAccountNo(), paymentVoucher.getPaymentAccountNo());
			Assert.assertEquals(paymentOrder.getCounterAccountName(), paymentVoucher.getPaymentName());
			Assert.assertEquals(null, paymentVoucher.getPaymentBankName());
			Assert.assertEquals(VoucherSource.ONLINE_PAYMENT, paymentVoucher.getVoucherSource());
			Assert.assertEquals("contract_uuid_1", paymentVoucher.getContractUuid());
			Assert.assertEquals(paymentOrder.getBatchNo(), paymentVoucher.getBatchNo());
			Assert.assertEquals("contract_unique_id_1", paymentVoucher.getContractUniqueId());
			Assert.assertEquals(StringUtils.EMPTY, paymentVoucher.getBankTransactionNo());
			
			//校验第三方支付凭证 明细
			List<ThirdPartyPaymentVoucherDetail> voucherDetails = thirdPartyPaymentVoucherDetailService.getThirdPartyPaymentVoucherDetailByVoucherUuid(paymentVoucher.getVoucherUuid());
			Assert.assertEquals(2, voucherDetails.size());
			ThirdPartyPaymentVoucherDetail voucherDetail1 = voucherDetails.get(0);
			ThirdPartyPaymentVoucherDetail voucherDetail2 = voucherDetails.get(1);
			
			//voucherDetail1
			Assert.assertEquals(paymentVoucher.getVoucherUuid(), voucherDetail1.getVoucherUuid());
			Assert.assertEquals(paymentVoucher.getVoucherNo(), voucherDetail1.getVoucherNo());
			Assert.assertEquals(new BigDecimal("1000.00"), voucherDetail1.getAmount());
			Assert.assertEquals("ZC133104659475144704", voucherDetail1.getRepaymentBusinessNo());
			Assert.assertEquals("c9c50683-30c8-4bb3-ace9-49ce9b8cdd01", voucherDetail1.getRepaymentBusinessUuid());
			Assert.assertEquals(paymentVoucher.getFinancialContractNo(), voucherDetail1.getFinancialContractNo());
			Assert.assertEquals(paymentVoucher.getFinancialContractUuid(), voucherDetail1.getFinancialContractUuid());
			Assert.assertEquals(paymentVoucher.getContractNo(), voucherDetail1.getContractNo());
			Assert.assertEquals(paymentVoucher.getContractUuid(), voucherDetail1.getContractUuid());
			Assert.assertEquals("repay_schedule_no_1", voucherDetail1.getRepayScheduleNo());
			Assert.assertEquals("outer_repayment_plan_no_1", voucherDetail1.getOuterRepaymentPlanNo());
			Assert.assertEquals(items.get(0).getChargeDetail(), voucherDetail1.getDetailAmountJson());
			Assert.assertEquals(2, voucherDetail1.getCurrentPeriod());
			
			//voucherDetail2
			Assert.assertEquals(paymentVoucher.getVoucherUuid(), voucherDetail2.getVoucherUuid());
			Assert.assertEquals(paymentVoucher.getVoucherNo(), voucherDetail2.getVoucherNo());
			Assert.assertEquals(new BigDecimal("1000.00"), voucherDetail2.getAmount());
			Assert.assertEquals("ZC133104659449978880", voucherDetail2.getRepaymentBusinessNo());
			Assert.assertEquals("76e328c5-3ca4-45e6-b503-0a9cfa194dff", voucherDetail2.getRepaymentBusinessUuid());
			Assert.assertEquals(paymentVoucher.getFinancialContractNo(), voucherDetail2.getFinancialContractNo());
			Assert.assertEquals(paymentVoucher.getFinancialContractUuid(), voucherDetail2.getFinancialContractUuid());
			Assert.assertEquals(paymentVoucher.getContractNo(), voucherDetail2.getContractNo());
			Assert.assertEquals(paymentVoucher.getContractUuid(), voucherDetail2.getContractUuid());
			Assert.assertEquals("repay_schedule_no_2", voucherDetail2.getRepayScheduleNo());
			Assert.assertEquals("outer_repayment_plan_no_2", voucherDetail2.getOuterRepaymentPlanNo());
			Assert.assertEquals(items.get(1).getChargeDetail(), voucherDetail2.getDetailAmountJson());
			Assert.assertEquals(1, voucherDetail2.getCurrentPeriod());
			
			
			List<DeductApplication> deductApplications = deductApplicationService.loadAll(DeductApplication.class);
			Assert.assertEquals(1, deductApplications.size());
			List<DeductApplicationDetail> deductApplicationDetail = deductApplicationDetailService.loadAll(DeductApplicationDetail.class);
			Assert.assertEquals(6, deductApplicationDetail.size());
			List<DeductPlan> deductPlans = deductPlanService.loadAll(DeductPlan.class);
			Assert.assertEquals(1, deductPlans.size());
			
			//校验dedcutApplication
			List<String> repaymentPlanCodeList = new ArrayList<String>();
			repaymentPlanCodeList.add("ZC133104659475144704");
			repaymentPlanCodeList.add("ZC133104659449978880");
			
			DeductApplication deductApplication = deductApplications.get(0);
			Assert.assertEquals(paymentOrder.getDeductRequestNo(), deductApplication.getRequestNo());
			Assert.assertEquals(paymentVoucher.getVoucherNo(), deductApplication.getDeductId());
			Assert.assertEquals(paymentOrder.getFinancialContractUuid(), deductApplication.getFinancialContractUuid());
			Assert.assertEquals(paymentOrder.getFinancialContractNo(), deductApplication.getFinancialProductCode());
			Assert.assertEquals(paymentVoucher.getContractNo(), deductApplication.getContractNo());
			Assert.assertEquals(paymentVoucher.getContractUniqueId(), deductApplication.getContractUniqueId());
			Assert.assertEquals(paymentOrder.getCounterAccountName(), deductApplication.getCustomerName());
			Assert.assertEquals(paymentOrder.getAmount(), deductApplication.getPlannedDeductTotalAmount());
			Assert.assertEquals(paymentOrder.getAmount(), deductApplication.getActualDeductTotalAmount());
			Assert.assertEquals(AccountSide.DEBIT.ordinal(), deductApplication.getTranscationType().ordinal());
			Assert.assertEquals(TransactionRecipient.OPPOSITE, deductApplication.getTransactionRecipient());
			Assert.assertEquals(DeductApplicationExecutionStatus.SUCCESS, deductApplication.getExecutionStatus());
			Assert.assertEquals(RecordStatus.UNRECORDED, deductApplication.getRecordStatus());
			Assert.assertEquals(IsAvailable.AVAILABLE, deductApplication.getIsAvailable());
			Assert.assertEquals(ThirdPartVoucherStatus.NOTCREATE, deductApplication.getThirdPartVoucherStatus());
			Assert.assertEquals("", deductApplication.getExecutionRemark());
			Assert.assertEquals(transactionRecord.getTransactionTime(), deductApplication.getCompleteTime());
			Assert.assertEquals(transactionRecord.getTransactionCreateTime(), deductApplication.getTransactionTime());
			Assert.assertEquals(SourceType.APPUPLOAD, deductApplication.getSourceType());
			Assert.assertEquals(JsonUtils.toJsonString(repaymentPlanCodeList), deductApplication.getRepaymentPlanCodeList());
			Assert.assertEquals(RepaymentType.ADVANCE, deductApplication.getRepaymentType());
			
			//校验dedcutApplicationDetail
			DeductApplicationDetail detail1 = deductApplicationDetail.get(0);
			DeductApplicationDetail detail2 = deductApplicationDetail.get(1);
			DeductApplicationDetail detail3 = deductApplicationDetail.get(2);
			
			//detail1
			Assert.assertEquals(deductApplication.getDeductApplicationUuid(), detail1.getDeductApplicationUuid());
			Assert.assertEquals(deductApplication.getRequestNo(), detail1.getRequestNo());
			Assert.assertEquals(deductApplication.getFinancialContractUuid(), detail1.getFinancialContractUuid());
			Assert.assertEquals(deductApplication.getContractUniqueId(), detail1.getContractUniqueId());
			Assert.assertEquals("ZC133104659475144704", detail1.getRepaymentPlanCode());
			Assert.assertEquals("c9c50683-30c8-4bb3-ace9-49ce9b8cdd01", detail1.getAssetSetUuid());
			Assert.assertEquals(deductApplication.getRepaymentType(), detail1.getRepaymentType());
			Assert.assertEquals(deductApplication.getTranscationType(), detail1.getTransactionType());
			Assert.assertEquals(deductApplication.getCreatorName(), detail1.getCreatorName());
			Assert.assertEquals(IsTotal.DETAIL, detail1.getIsTotal());
			Assert.assertEquals(new BigDecimal("800.00"), detail1.getAccountAmount());
			Assert.assertEquals("20000", detail1.getFirstAccountUuid());
			Assert.assertEquals(ChartOfAccount.FST_RECIEVABLE_ASSET, detail1.getFirstAccountName());
			Assert.assertEquals("20000.01", detail1.getSecondAccountUuid());
			Assert.assertEquals(ChartOfAccount.SND_RECIEVABLE_LOAN_ASSET, detail1.getSecondAccountName());
			Assert.assertEquals("20000.01.01", detail1.getThirdAccountUuid());
			Assert.assertEquals(ChartOfAccount.TRD_RECIEVABLE_LOAN_ASSET_PRINCIPLE, detail1.getThirdAccountName());
			
			//detail2
			Assert.assertEquals(deductApplication.getDeductApplicationUuid(), detail2.getDeductApplicationUuid());
			Assert.assertEquals(deductApplication.getRequestNo(), detail2.getRequestNo());
			Assert.assertEquals(deductApplication.getFinancialContractUuid(), detail2.getFinancialContractUuid());
			Assert.assertEquals(deductApplication.getContractUniqueId(), detail2.getContractUniqueId());
			Assert.assertEquals("ZC133104659475144704", detail2.getRepaymentPlanCode());
			Assert.assertEquals("c9c50683-30c8-4bb3-ace9-49ce9b8cdd01", detail2.getAssetSetUuid());
			Assert.assertEquals(deductApplication.getRepaymentType(), detail2.getRepaymentType());
			Assert.assertEquals(deductApplication.getTranscationType(), detail2.getTransactionType());
			Assert.assertEquals(deductApplication.getCreatorName(), detail2.getCreatorName());
			Assert.assertEquals(IsTotal.DETAIL, detail2.getIsTotal());
			Assert.assertEquals(new BigDecimal("200.00"), detail2.getAccountAmount());
			Assert.assertEquals("20000", detail2.getFirstAccountUuid());
			Assert.assertEquals(ChartOfAccount.FST_RECIEVABLE_ASSET, detail2.getFirstAccountName());
			Assert.assertEquals("20000.01", detail2.getSecondAccountUuid());
			Assert.assertEquals(ChartOfAccount.SND_RECIEVABLE_LOAN_ASSET, detail2.getSecondAccountName());
			Assert.assertEquals("20000.01.02", detail2.getThirdAccountUuid());
			Assert.assertEquals(ChartOfAccount.TRD_RECIEVABLE_LOAN_ASSET_INTEREST, detail2.getThirdAccountName());
			
			//detail3
			Assert.assertEquals(deductApplication.getDeductApplicationUuid(), detail3.getDeductApplicationUuid());
			Assert.assertEquals(deductApplication.getRequestNo(), detail3.getRequestNo());
			Assert.assertEquals(deductApplication.getFinancialContractUuid(), detail3.getFinancialContractUuid());
			Assert.assertEquals(deductApplication.getContractUniqueId(), detail3.getContractUniqueId());
			Assert.assertEquals("ZC133104659475144704", detail3.getRepaymentPlanCode());
			Assert.assertEquals("c9c50683-30c8-4bb3-ace9-49ce9b8cdd01", detail3.getAssetSetUuid());
			Assert.assertEquals(deductApplication.getRepaymentType(), detail3.getRepaymentType());
			Assert.assertEquals(deductApplication.getTranscationType(), detail3.getTransactionType());
			Assert.assertEquals(deductApplication.getCreatorName(), detail3.getCreatorName());
			Assert.assertEquals(IsTotal.TOTAL, detail3.getIsTotal());
			Assert.assertEquals(new BigDecimal("1000.00"), detail3.getAccountAmount());
			Assert.assertEquals(ExtraChargeSpec.TOTAL_RECEIVABEL_AMOUNT, detail3.getFirstAccountName());
			
			//校验deductPlan
			DeductPlan deductPlan = deductPlans.get(0);
			Assert.assertEquals(deductApplication.getDeductApplicationUuid(), deductPlan.getDeductApplicationUuid());
			Assert.assertEquals(paymentOrder.getFinancialContractUuid(), deductPlan.getFinancialContractUuid());
			Assert.assertEquals(paymentVoucher.getContractNo(), deductPlan.getContractNo());
			Assert.assertEquals(paymentVoucher.getContractUniqueId(), deductPlan.getContractUniqueId());
			Assert.assertEquals(paymentOrder.getPaymentGateWay(), deductPlan.getPaymentGateway());
			Assert.assertEquals("f8bb9956-1952-4893-98c8-66683d25d7ce", deductPlan.getPaymentChannelUuid());
			Assert.assertEquals("600000000001", deductPlan.getPgAccount());
			Assert.assertEquals("clearing_no", deductPlan.getPgClearingAccount());
			Assert.assertEquals(AccountSide.DEBIT.ordinal(), deductPlan.getTransactionType().ordinal());
			Assert.assertEquals(paymentOrder.getCounterAccountNo(), deductPlan.getCpBankCardNo());
			Assert.assertEquals(paymentOrder.getCounterAccountName(), deductPlan.getCpBankAccountHolder());
			Assert.assertEquals(CertificateType.ID_CARD, deductPlan.getCpIdType());
			Assert.assertEquals(paymentOrder.getIdCardNumInAppendix(), deductPlan.getCpIdNumber());
			Assert.assertEquals(paymentOrder.getCounterAccountBankName(), deductPlan.getCpBankName());
			Assert.assertEquals(null, deductPlan.getPlannedPaymentDate());
			Assert.assertEquals(paymentOrder.getAmount(), deductPlan.getPlannedTotalAmount());
			Assert.assertEquals(paymentOrder.getAmount(), deductPlan.getActualTotalAmount());
			Assert.assertEquals(DeductApplicationExecutionStatus.SUCCESS, deductPlan.getExecutionStatus());
			Assert.assertEquals(transactionRecord.getTransactionCreateTime(), deductPlan.getTransactionTime());
			Assert.assertEquals(transactionRecord.getTransactionTime(), deductPlan.getCompleteTime());
			Assert.assertEquals(TransactionRecipient.OPPOSITE, deductPlan.getTransactionRecipient());
			Assert.assertEquals(deductApplication.getRepaymentType(), deductPlan.getRepaymentType());
			Assert.assertEquals(paymentOrder.getTradeUuid(), deductPlan.getTradeUuid());
			Assert.assertEquals(ClearingStatus.UNDO, deductPlan.getClearingStatus());
			
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test 
	@Sql("classpath:/test/yunxin/repaymentOrder/test_repayment_order_modify_check_and_save2.sql")
	public void testRepaymentOrderModifyCheck() {
		
		try {
			
			RepaymentOrder order = repaymentOrderService.getRepaymentOrderByUuid("order_uuid_1");
			RepaymentOrder order2 = repaymentOrderService.getRepaymentOrderByUuid("order_uuid_2");
			
			Assert.assertEquals(OrderAliveStatus.PATYMENT_TEMPORARY_FREEZING, order.getOrderAliveStatus());
			Assert.assertEquals(OrderAliveStatus.PAYMENT_ORDER_CREATE, order2.getOrderAliveStatus());
			
			businessPaymentVoucherSession.handler_repayment_order_modify();
			
			sessionFactory.getCurrentSession().flush();
			sessionFactory.getCurrentSession().clear();
			
			RepaymentOrder order3 = repaymentOrderService.getRepaymentOrderByUuid("order_uuid_1");
			RepaymentOrder afterOrder2 = repaymentOrderService.getRepaymentOrderByUuid("order_uuid_2");
			Assert.assertEquals(OrderCheckStatus.VERIFICATION_SUCCESS, afterOrder2.getOrderCheckStatus());
			Assert.assertEquals(new BigDecimal("100.00"), afterOrder2.getPaidAmount());
			Assert.assertEquals(OrderPayStatus.PAYING, afterOrder2.getOrderPayStatus());
			
			//原订单作废
			Assert.assertEquals(OrderAliveStatus.PAYMENT_ORDER_CANCEL, order3.getOrderAliveStatus());
			//原订单明细作废
			List<RepaymentOrderItem> items = repaymentOrderItemService.getRepaymentOrderItems("order_uuid_1");
			for (RepaymentOrderItem repaymentOrderItem : items) {
				Assert.assertEquals(DetailAliveStatus.INVALID, repaymentOrderItem.getDetailAliveStatus());
			}
			
			//原订单 支付单重新关联到新支付单
			PaymentOrder paymentOrder = paymentOrderService.getActivePaymenrOrderByUuid("paymentOrderUuid_1");
			Assert.assertEquals("order_uuid_2", paymentOrder.getOrderUuid());
			
			
			List<RepaymentOrderItem> itemList2 = repaymentOrderItemService.loadAll(RepaymentOrderItem.class);
			Assert.assertEquals(5, itemList2.size());
			
			//校验item
			RepaymentOrderItem item = itemList2.get(4);
			Assert.assertEquals(1, item.getCurrentPeriod());
			Assert.assertEquals(new BigDecimal("350.00"), item.getAmount());
			Assert.assertEquals("contract_no", item.getContractNo());
			Assert.assertEquals("contract_unique_id_1", item.getContractUniqueId());
			Assert.assertEquals("contract_uuid_1", item.getContractUuid());
			Assert.assertEquals(DetailAliveStatus.CREATED, item.getDetailAliveStatus());
			Assert.assertEquals(DetailPayShowStatus.UNPAID, item.getDetailPayShowStatus());
			Assert.assertEquals(DetailPayStatus.UNPAID, item.getDetailPayStatus());
			Assert.assertEquals("financial_contract_uuid_1", item.getFinancialContractUuid());
			Assert.assertEquals("merId", item.getMerId());
			Assert.assertEquals("orderUniqueId", item.getOrderUniqueId());
			Assert.assertEquals("order_uuid_2", item.getOrderUuid());
			Assert.assertEquals("123213", item.getRepaymentBusinessNo());
			Assert.assertEquals(RepaymentBusinessType.REPAYMENT_PLAN, item.getRepaymentBusinessType());
			Assert.assertEquals("asset_uuid_1", item.getRepaymentBusinessUuid());
			Assert.assertEquals(RepaymentWay.MERCHANT_DEDUCT_EASY_PAY, item.getRepaymentWay());
			Assert.assertEquals(DateUtils.parseDate("2017-06-06 01:02:03", com.zufangbao.sun.utils.DateUtils.LONG_DATE_FORMAT), item.getRepaymentPlanTime());
			
			//校验ItemCharge
			List<RepaymentOrderItemCharge> chargeList = repaymentOrderItemChargeService.loadAll(RepaymentOrderItemCharge.class);
			Assert.assertEquals(6, chargeList.size());
			
			RepaymentOrderItemCharge charge1 = chargeList.get(3);
			Assert.assertEquals(new BigDecimal("150.00"), charge1.getAccountAmount());
			Assert.assertEquals("contract_uuid_1", charge1.getContractUuid());
			Assert.assertEquals("FST_UNEARNED_LOAN_ASSET", charge1.getFirstAccountName());
			Assert.assertEquals("10000", charge1.getFirstAccountUuid());
			Assert.assertEquals("order_uuid_2", charge1.getOrderUuid());
			Assert.assertEquals("123213", charge1.getRepaymentBusinessNo());
			Assert.assertEquals("asset_uuid_1", charge1.getRepaymentBusinessUuid());
			Assert.assertEquals(item.getOrderDetailUuid(), charge1.getRepaymentOrderItemUuid());
			Assert.assertEquals("SND_UNEARNED_LOAN_ASSET_PRINCIPLE", charge1.getSecondAccountName());
			Assert.assertEquals("10000.02", charge1.getSecondAccountUuid());
			Assert.assertEquals(null, charge1.getThirdAccountName());
			Assert.assertEquals(null, charge1.getThirdAccountUuid());
			
			RepaymentOrderItemCharge charge2 = chargeList.get(4);
			Assert.assertEquals(new BigDecimal("100.00"), charge2.getAccountAmount());
			Assert.assertEquals("FST_UNEARNED_LOAN_ASSET", charge2.getFirstAccountName());
			Assert.assertEquals("10000", charge2.getFirstAccountUuid());
			Assert.assertEquals("SND_UNEARNED_LOAN_ASSET_INTEREST", charge2.getSecondAccountName());
			Assert.assertEquals("10000.01", charge2.getSecondAccountUuid());
			
			RepaymentOrderItemCharge charge3 = chargeList.get(5);
			Assert.assertEquals(new BigDecimal("100.00"), charge3.getAccountAmount());
			Assert.assertEquals("FST_UNEARNED_LOAN_ASSET", charge3.getFirstAccountName());
			Assert.assertEquals("10000", charge3.getFirstAccountUuid());
			Assert.assertEquals("SND_UNEARNED_LOAN_ASSET_LOAN_SERVICE_FEE", charge3.getSecondAccountName());
			Assert.assertEquals("10000.03", charge3.getSecondAccountUuid());
			
			Job job = jobService.getJobBy("6e8d5b01-56de-4460-acd7-02f964778ec4");
			Assert.assertEquals(ExecutingResult.SUC, job.getExcutingResult());
			Assert.assertEquals(ExecutingStatus.DONE, job.getExcutingStatus());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	@Test 
	@Sql("classpath:/test/yunxin/repaymentOrder/test_repayment_order_modify_check_and_save.sql")
	public void testRepaymentOrderModifyCheck2() {
		
		try {
			
			RepaymentOrder order = repaymentOrderService.getRepaymentOrderByUuid("order_uuid_1");
			RepaymentOrder order2 = repaymentOrderService.getRepaymentOrderByUuid("order_uuid_2");
			
			Assert.assertEquals(OrderAliveStatus.PATYMENT_TEMPORARY_FREEZING, order.getOrderAliveStatus());
			Assert.assertEquals(OrderAliveStatus.PAYMENT_ORDER_CREATE, order2.getOrderAliveStatus());
			
			
			businessPaymentVoucherSession.handler_repayment_order_modify();
			
			sessionFactory.getCurrentSession().flush();
			sessionFactory.getCurrentSession().clear();
			
			RepaymentOrder order3 = repaymentOrderService.getRepaymentOrderByUuid("order_uuid_1");
			RepaymentOrder newOrder = repaymentOrderService.getRepaymentOrderByUuid("order_uuid_2");
			Assert.assertEquals(OrderCheckStatus.VERIFICATION_SUCCESS, newOrder.getOrderCheckStatus());
			Assert.assertEquals(true, newOrder.isDetailAmountEmpty());
			
			
			Assert.assertEquals(OrderAliveStatus.PAYMENT_ORDER_CANCEL, order3.getOrderAliveStatus());
			
			List<RepaymentOrderItem> items = repaymentOrderItemService.loadAll(RepaymentOrderItem.class);
			Assert.assertEquals(1, items.size());
			
			//校验item
			RepaymentOrderItem item = items.get(0);
			Assert.assertEquals(1, item.getCurrentPeriod());
			Assert.assertEquals(new BigDecimal("600.00"), item.getAmount());
			Assert.assertEquals("contract_no", item.getContractNo());
			Assert.assertEquals("contract_unique_id_1", item.getContractUniqueId());
			Assert.assertEquals("contract_uuid_1", item.getContractUuid());
			Assert.assertEquals(DetailAliveStatus.CREATED, item.getDetailAliveStatus());
			Assert.assertEquals(DetailPayShowStatus.UNPAID, item.getDetailPayShowStatus());
			Assert.assertEquals(DetailPayStatus.UNPAID, item.getDetailPayStatus());
			Assert.assertEquals("financial_contract_uuid_1", item.getFinancialContractUuid());
			Assert.assertEquals("merId", item.getMerId());
			Assert.assertEquals("orderUniqueId", item.getOrderUniqueId());
			Assert.assertEquals("order_uuid_2", item.getOrderUuid());
			Assert.assertEquals("123213", item.getRepaymentBusinessNo());
			Assert.assertEquals(RepaymentBusinessType.REPAYMENT_PLAN, item.getRepaymentBusinessType());
			Assert.assertEquals("asset_uuid_1", item.getRepaymentBusinessUuid());
			Assert.assertEquals(RepaymentWay.MERCHANT_DEDUCT_EASY_PAY, item.getRepaymentWay());
			Assert.assertEquals(DateUtils.parseDate("2017-06-06 01:02:03", com.zufangbao.sun.utils.DateUtils.LONG_DATE_FORMAT), item.getRepaymentPlanTime());
			
			//校验ItemCharge
			List<RepaymentOrderItemCharge> chargeList = repaymentOrderItemChargeService.loadAll(RepaymentOrderItemCharge.class);
			Assert.assertEquals(3, chargeList.size());
			
			RepaymentOrderItemCharge charge1 = chargeList.get(0);
			Assert.assertEquals(new BigDecimal("300.00"), charge1.getAccountAmount());
			Assert.assertEquals("contract_uuid_1", charge1.getContractUuid());
			Assert.assertEquals("FST_UNEARNED_LOAN_ASSET", charge1.getFirstAccountName());
			Assert.assertEquals("10000", charge1.getFirstAccountUuid());
			Assert.assertEquals("order_uuid_2", charge1.getOrderUuid());
			Assert.assertEquals("123213", charge1.getRepaymentBusinessNo());
			Assert.assertEquals("asset_uuid_1", charge1.getRepaymentBusinessUuid());
			Assert.assertEquals(item.getOrderDetailUuid(), charge1.getRepaymentOrderItemUuid());
			Assert.assertEquals("SND_UNEARNED_LOAN_ASSET_PRINCIPLE", charge1.getSecondAccountName());
			Assert.assertEquals("10000.02", charge1.getSecondAccountUuid());
			Assert.assertEquals(null, charge1.getThirdAccountName());
			Assert.assertEquals(null, charge1.getThirdAccountUuid());
			
			RepaymentOrderItemCharge charge2 = chargeList.get(1);
			Assert.assertEquals(new BigDecimal("200.00"), charge2.getAccountAmount());
			Assert.assertEquals("FST_UNEARNED_LOAN_ASSET", charge2.getFirstAccountName());
			Assert.assertEquals("10000", charge2.getFirstAccountUuid());
			Assert.assertEquals("SND_UNEARNED_LOAN_ASSET_INTEREST", charge2.getSecondAccountName());
			Assert.assertEquals("10000.01", charge2.getSecondAccountUuid());
			
			RepaymentOrderItemCharge charge3 = chargeList.get(2);
			Assert.assertEquals(new BigDecimal("100.00"), charge3.getAccountAmount());
			Assert.assertEquals("FST_UNEARNED_LOAN_ASSET", charge3.getFirstAccountName());
			Assert.assertEquals("10000", charge3.getFirstAccountUuid());
			Assert.assertEquals("SND_UNEARNED_LOAN_ASSET_LOAN_SERVICE_FEE", charge3.getSecondAccountName());
			Assert.assertEquals("10000.03", charge3.getSecondAccountUuid());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	// 业务金额明细为空
	@Test 
	@Sql("classpath:/test/yunxin/repaymentOrder/test_repayment_order_modify_check_and_save3.sql")
	public void testRepaymentOrderModifyCheck3() {
		
		try {
			RepaymentOrder order = repaymentOrderService.getRepaymentOrderByUuid("order_uuid_1");
			RepaymentOrder order2 = repaymentOrderService.getRepaymentOrderByUuid("order_uuid_2");
			
			Assert.assertEquals(OrderAliveStatus.PATYMENT_TEMPORARY_FREEZING, order.getOrderAliveStatus());
			Assert.assertEquals(OrderAliveStatus.PAYMENT_ORDER_CREATE, order2.getOrderAliveStatus());
			Assert.assertEquals(RepaymentOrder.CHECK, order.getActiveOrderUuid());
			Assert.assertEquals(RepaymentOrder.CHECK, order2.getActiveOrderUuid());
			
			//校验ItemCharge
			List<RepaymentOrderItemCharge> chargeList2 = repaymentOrderItemChargeService.loadAll(RepaymentOrderItemCharge.class);
			Assert.assertEquals(3, chargeList2.size());
			
			businessPaymentVoucherSession.handler_repayment_order_modify();
			
			sessionFactory.getCurrentSession().flush();
			sessionFactory.getCurrentSession().clear();
			
			RepaymentOrder order3 = repaymentOrderService.getRepaymentOrderByUuid("order_uuid_1");
			RepaymentOrder afterOrder2 = repaymentOrderService.getRepaymentOrderByUuid("order_uuid_2");
			Assert.assertEquals(OrderCheckStatus.VERIFICATION_SUCCESS, afterOrder2.getOrderCheckStatus());
			Assert.assertEquals(false, afterOrder2.isDetailAmountEmpty());
			Assert.assertEquals(RepaymentOrder.EMPTY, order3.getActiveOrderUuid());
			Assert.assertEquals(new BigDecimal("100.00"), afterOrder2.getPaidAmount());
			Assert.assertEquals(OrderPayStatus.PAYING, afterOrder2.getOrderPayStatus());
			
			//原订单作废
			Assert.assertEquals(OrderAliveStatus.PAYMENT_ORDER_CANCEL, order3.getOrderAliveStatus());
			//原订单明细作废
			List<RepaymentOrderItem> items = repaymentOrderItemService.getRepaymentOrderItemsForSuccess("order_uuid_1",null);
			for (RepaymentOrderItem repaymentOrderItem : items) {
				Assert.assertEquals(DetailAliveStatus.INVALID, repaymentOrderItem.getDetailAliveStatus());
			}
			
			//原订单 支付单重新关联到新支付单
			PaymentOrder paymentOrder = paymentOrderService.getActivePaymenrOrderByUuid("paymentOrderUuid_1");
			Assert.assertEquals("order_uuid_2", paymentOrder.getOrderUuid());
			
			List<RepaymentOrderItem> itemList2 = repaymentOrderItemService.loadAll(RepaymentOrderItem.class);
			Assert.assertEquals(5, itemList2.size());
			
			//校验item
			RepaymentOrderItem item = itemList2.get(4);
			Assert.assertEquals(1, item.getCurrentPeriod());
			Assert.assertEquals(new BigDecimal("350.00"), item.getAmount());
			Assert.assertEquals("contract_no", item.getContractNo());
			Assert.assertEquals("contract_unique_id_1", item.getContractUniqueId());
			Assert.assertEquals("contract_uuid_1", item.getContractUuid());
			Assert.assertEquals(DetailAliveStatus.CREATED, item.getDetailAliveStatus());
			Assert.assertEquals(DetailPayShowStatus.UNPAID, item.getDetailPayShowStatus());
			Assert.assertEquals(DetailPayStatus.UNPAID, item.getDetailPayStatus());
			Assert.assertEquals("financial_contract_uuid_1", item.getFinancialContractUuid());
			Assert.assertEquals("merId", item.getMerId());
			Assert.assertEquals("orderUniqueId", item.getOrderUniqueId());
			Assert.assertEquals("order_uuid_2", item.getOrderUuid());
			Assert.assertEquals("123213", item.getRepaymentBusinessNo());
			Assert.assertEquals(RepaymentBusinessType.REPAYMENT_PLAN, item.getRepaymentBusinessType());
			Assert.assertEquals("asset_uuid_1", item.getRepaymentBusinessUuid());
			Assert.assertEquals(RepaymentWay.MERCHANT_DEDUCT_EASY_PAY, item.getRepaymentWay());
			Assert.assertEquals(DateUtils.parseDate("2017-06-06 01:02:03", com.zufangbao.sun.utils.DateUtils.LONG_DATE_FORMAT), item.getRepaymentPlanTime());
			
			//校验ItemCharge
			List<RepaymentOrderItemCharge> chargeList = repaymentOrderItemChargeService.loadAll(RepaymentOrderItemCharge.class);
			Assert.assertEquals(3, chargeList.size());
			
			Job job = jobService.getJobBy("6e8d5b01-56de-4460-acd7-02f964778ec4");
			Assert.assertEquals(ExecutingResult.SUC, job.getExcutingResult());
			Assert.assertEquals(ExecutingStatus.DONE, job.getExcutingStatus());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	//校验失败  roll_back
	@Test 
	@Sql("classpath:/test/yunxin/repaymentOrder/test_repayment_order_modify_check_and_save_fail.sql")
	public void testRepaymentOrderModifyCheck_fail() {
		
		try {
			
			RepaymentOrder order = repaymentOrderService.getRepaymentOrderByUuid("order_uuid_1");
			RepaymentOrder order2 = repaymentOrderService.getRepaymentOrderByUuid("order_uuid_2");
			
			Assert.assertEquals(OrderAliveStatus.PATYMENT_TEMPORARY_FREEZING, order.getOrderAliveStatus());
			Assert.assertEquals(OrderAliveStatus.PAYMENT_ORDER_CREATE, order2.getOrderAliveStatus());
			Assert.assertEquals(RepaymentOrder.CHECK, order.getActiveOrderUuid());
			Assert.assertEquals(RepaymentOrder.CHECK, order2.getActiveOrderUuid());
			
			
			businessPaymentVoucherSession.handler_repayment_order_modify();
			
			sessionFactory.getCurrentSession().flush();
			sessionFactory.getCurrentSession().clear();
			
			RepaymentOrder order3 = repaymentOrderService.getRepaymentOrderByUuid("order_uuid_1");
			RepaymentOrder newOrder = repaymentOrderService.getRepaymentOrderByUuid("order_uuid_2");
			Assert.assertEquals(OrderCheckStatus.VERIFICATION_FAILURE, newOrder.getOrderCheckStatus());
			Assert.assertEquals(true, newOrder.isDetailAmountEmpty());
			Assert.assertEquals(OrderAliveStatus.PAYMENT_ORDER_CREATE, order3.getOrderAliveStatus());
			
			Assert.assertEquals(RepaymentOrder.EMPTY, order3.getActiveOrderUuid());
//			Assert.assertEquals("empty", newOrder.getActiveOrderUuid());
			
			
			//失败明细
			List<RepaymentOrderItemCheckFailLog> failItems = repaymentOrderItemCheckFailLogService.loadAll(RepaymentOrderItemCheckFailLog.class);
			Assert.assertEquals(1, failItems.size());
			
			RepaymentOrderItemCheckFailLog failLog = failItems.get(0);
			Assert.assertEquals(0, failLog.getCurrentPeriod());
			Assert.assertEquals(new BigDecimal("600.00"), failLog.getAmount());
			Assert.assertEquals("contract_no", failLog.getContractNo());
			Assert.assertEquals("contract_unique_id_1", failLog.getContractUniqueId());
			Assert.assertEquals("financial_contract_uuid_1", failLog.getFinancialContractUuid());
			Assert.assertEquals(RepaymentWay.MERCHANT_DEDUCT_EASY_PAY, failLog.getRepaymentWay());
			Assert.assertEquals(DateUtils.parseDate("2017-06-06 01:02:03", com.zufangbao.sun.utils.DateUtils.LONG_DATE_FORMAT), failLog.getRepaymentPlanTime());
			Assert.assertEquals(RepaymentBusinessType.REPAYMENT_PLAN, failLog.getRepaymentBusinessType());
			Assert.assertEquals("1232131111", failLog.getRepaymentBusinessNo());
			Assert.assertEquals("还款计划编号错误,无该还款计划", failLog.getRemark());
			Assert.assertEquals("order_uuid_2", failLog.getOrderUuid());
			Assert.assertEquals("orderUniqueId", failLog.getOrderUniqueId());
			Assert.assertEquals("merId", failLog.getMerId());
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	// 业务金额明细为空 [] [{}]  ""
	@Test 
	@Sql("classpath:/test/yunxin/repaymentOrder/test_repayment_order_modify_check_and_save4.sql")
	public void testRepaymentOrderModifyCheck4() {
		
		try {
			RepaymentOrder order = repaymentOrderService.getRepaymentOrderByUuid("order_uuid_1");
			RepaymentOrder order2 = repaymentOrderService.getRepaymentOrderByUuid("order_uuid_2");
			
			Assert.assertEquals(OrderAliveStatus.PATYMENT_TEMPORARY_FREEZING, order.getOrderAliveStatus());
			Assert.assertEquals(OrderAliveStatus.PAYMENT_ORDER_CREATE, order2.getOrderAliveStatus());
			Assert.assertEquals(RepaymentOrder.CHECK, order.getActiveOrderUuid());
			Assert.assertEquals(RepaymentOrder.CHECK, order2.getActiveOrderUuid());
			
			//校验ItemCharge
			List<RepaymentOrderItemCharge> chargeList2 = repaymentOrderItemChargeService.loadAll(RepaymentOrderItemCharge.class);
			Assert.assertEquals(3, chargeList2.size());
			
			businessPaymentVoucherSession.handler_repayment_order_modify();
			
			sessionFactory.getCurrentSession().flush();
			sessionFactory.getCurrentSession().clear();
			
			RepaymentOrder order3 = repaymentOrderService.getRepaymentOrderByUuid("order_uuid_1");
			RepaymentOrder afterOrder2 = repaymentOrderService.getRepaymentOrderByUuid("order_uuid_2");
			Assert.assertEquals(OrderCheckStatus.VERIFICATION_SUCCESS, afterOrder2.getOrderCheckStatus());
			Assert.assertEquals(false, afterOrder2.isDetailAmountEmpty());
			Assert.assertEquals(RepaymentOrder.EMPTY, order3.getActiveOrderUuid());
			Assert.assertEquals(new BigDecimal("100.00"), afterOrder2.getPaidAmount());
			Assert.assertEquals(OrderPayStatus.PAYING, afterOrder2.getOrderPayStatus());
			
			//原订单作废
			Assert.assertEquals(OrderAliveStatus.PAYMENT_ORDER_CANCEL, order3.getOrderAliveStatus());
			//原订单明细作废
			List<RepaymentOrderItem> items = repaymentOrderItemService.getRepaymentOrderItemsForSuccess("order_uuid_1",null);
			for (RepaymentOrderItem repaymentOrderItem : items) {
				Assert.assertEquals(DetailAliveStatus.INVALID, repaymentOrderItem.getDetailAliveStatus());
			}
			
			//原订单 支付单重新关联到新支付单
			PaymentOrder paymentOrder = paymentOrderService.getActivePaymenrOrderByUuid("paymentOrderUuid_1");
			Assert.assertEquals("order_uuid_2", paymentOrder.getOrderUuid());
			
			List<RepaymentOrderItem> itemList2 = repaymentOrderItemService.loadAll(RepaymentOrderItem.class);
			Assert.assertEquals(5, itemList2.size());
			
			Job job = jobService.getJobBy("6e8d5b01-56de-4460-acd7-02f964778ec4");
			Assert.assertEquals(ExecutingResult.SUC, job.getExcutingResult());
			Assert.assertEquals(ExecutingStatus.DONE, job.getExcutingStatus());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	//
	@Test 
	@Sql("classpath:/test/yunxin/repaymentOrder/test_repayment_order_modify_check_and_save5.sql")
	public void testRepaymentOrderModifyCheck5() {
		
		try {
			RepaymentOrder order = repaymentOrderService.getRepaymentOrderByUuid("order_uuid_1");
			RepaymentOrder order2 = repaymentOrderService.getRepaymentOrderByUuid("order_uuid_2");
			
			Assert.assertEquals(OrderAliveStatus.PATYMENT_TEMPORARY_FREEZING, order.getOrderAliveStatus());
			Assert.assertEquals(OrderAliveStatus.PAYMENT_ORDER_CREATE, order2.getOrderAliveStatus());
			Assert.assertEquals(RepaymentOrder.CHECK, order.getActiveOrderUuid());
			Assert.assertEquals(RepaymentOrder.CHECK, order2.getActiveOrderUuid());
			
			//校验ItemCharge
			List<RepaymentOrderItemCharge> chargeList2 = repaymentOrderItemChargeService.loadAll(RepaymentOrderItemCharge.class);
			Assert.assertEquals(3, chargeList2.size());
			
			businessPaymentVoucherSession.handler_repayment_order_modify();
			
			sessionFactory.getCurrentSession().flush();
			sessionFactory.getCurrentSession().clear();
			
			RepaymentOrder order3 = repaymentOrderService.getRepaymentOrderByUuid("order_uuid_1");
			RepaymentOrder afterOrder2 = repaymentOrderService.getRepaymentOrderByUuid("order_uuid_2");
			Assert.assertEquals(OrderCheckStatus.VERIFICATION_SUCCESS, afterOrder2.getOrderCheckStatus());
			Assert.assertEquals(false, afterOrder2.isDetailAmountEmpty());
			Assert.assertEquals(RepaymentOrder.EMPTY, order3.getActiveOrderUuid());
			Assert.assertEquals(new BigDecimal("100.00"), afterOrder2.getPaidAmount());
			Assert.assertEquals(OrderPayStatus.PAYING, afterOrder2.getOrderPayStatus());
			
			//原订单作废
			Assert.assertEquals(OrderAliveStatus.PAYMENT_ORDER_CANCEL, order3.getOrderAliveStatus());
			//原订单明细作废
			List<RepaymentOrderItem> items = repaymentOrderItemService.getRepaymentOrderItemsForSuccess("order_uuid_1",null);
			for (RepaymentOrderItem repaymentOrderItem : items) {
				Assert.assertEquals(DetailAliveStatus.INVALID, repaymentOrderItem.getDetailAliveStatus());
			}
			
			//原订单 支付单重新关联到新支付单
			PaymentOrder paymentOrder = paymentOrderService.getActivePaymenrOrderByUuid("paymentOrderUuid_1");
			Assert.assertEquals("order_uuid_2", paymentOrder.getOrderUuid());
			
			List<RepaymentOrderItem> itemList2 = repaymentOrderItemService.loadAll(RepaymentOrderItem.class);
			Assert.assertEquals(5, itemList2.size());
			
			Job job = jobService.getJobBy("6e8d5b01-56de-4460-acd7-02f964778ec4");
			Assert.assertEquals(ExecutingResult.SUC, job.getExcutingResult());
			Assert.assertEquals(ExecutingStatus.DONE, job.getExcutingStatus());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@Test 
	public void testRepaymentOrderQueryModelForMerge() {
		RepaymentOrderQueryModelForMerge queryMergeModel = new RepaymentOrderQueryModelForMerge();
		queryMergeModel.setFirstRepaymentWayGroup(JsonUtils.toJsonString(RepaymentWayGroupType.getMergeRepaymentType()));
		
		if (CollectionUtils.isNotEmpty(queryMergeModel.parseRepaymentWayGroupType())) {
		
		   queryMergeModel.getFirstRepaymentWayGroup();
		}

	}
}
