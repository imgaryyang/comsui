package morganStanley.controller;

import com.demo2do.core.entity.Result;
import com.demo2do.core.utils.DateUtils;
import com.demo2do.core.utils.JsonUtils;
import com.suidifu.morganstanley.TestMorganStanley;
import com.suidifu.morganstanley.controller.api.notify.InternalNotifyController;
import com.suidifu.morganstanley.controller.api.order.CommandApiRepaymentOrderController;
import com.zufangbao.sun.api.model.deduct.DeductApplicationExecutionStatus;
import com.zufangbao.sun.api.model.deduct.RecordStatus;
import com.zufangbao.sun.api.model.repayment.OrderRequestModel;
import com.zufangbao.sun.api.model.repayment.RepaymentOrderDetail;
import com.zufangbao.sun.api.model.repayment.RepaymentOrderDetailInfoModel;
import com.zufangbao.sun.entity.account.AccountSide;
import com.zufangbao.sun.entity.account.VirtualAccount;
import com.zufangbao.sun.entity.order.RepaymentAuditStatus;
import com.zufangbao.sun.entity.repayment.order.AliveStatus;
import com.zufangbao.sun.entity.repayment.order.OrderAliveStatus;
import com.zufangbao.sun.entity.repayment.order.OrderCheckStatus;
import com.zufangbao.sun.entity.repayment.order.OrderDBStatus;
import com.zufangbao.sun.entity.repayment.order.OrderPayStatus;
import com.zufangbao.sun.entity.repayment.order.OrderRecoverResult;
import com.zufangbao.sun.entity.repayment.order.OrderRecoverStatus;
import com.zufangbao.sun.entity.repayment.order.PayStatus;
import com.zufangbao.sun.entity.repayment.order.PayWay;
import com.zufangbao.sun.entity.repayment.order.PaymentOrder;
import com.zufangbao.sun.entity.repayment.order.RecoverStatus;
import com.zufangbao.sun.entity.repayment.order.RepaymentOrder;
import com.zufangbao.sun.entity.repayment.order.RepaymentOrderItem;
import com.zufangbao.sun.entity.repayment.order.RepaymentOrderItemCharge;
import com.zufangbao.sun.entity.repayment.order.RepaymentOrderPayResult;
import com.zufangbao.sun.entity.repayment.order.RepaymentOrderSourceStatus;
import com.zufangbao.sun.entity.repayment.order.RepaymentWayGroupType;
import com.zufangbao.sun.ledgerbook.stat.LedgerBookStatHandler;
import com.zufangbao.sun.service.PaymentOrderService;
import com.zufangbao.sun.service.RepaymentOrderService;
import com.zufangbao.sun.service.ThirdPartVoucherCommandLogService;
import com.zufangbao.sun.service.VirtualAccountService;
import com.zufangbao.sun.utils.excel.ExcelUtil;
import com.zufangbao.sun.yunxin.api.deduct.service.DeductApplicationService;
import com.zufangbao.sun.yunxin.api.deduct.service.DeductPlanService;
import com.zufangbao.sun.yunxin.entity.AssetClearStatus;
import com.zufangbao.sun.yunxin.entity.AssetSet;
import com.zufangbao.sun.yunxin.entity.OnAccountStatus;
import com.zufangbao.sun.yunxin.entity.OrderPaymentStatus;
import com.zufangbao.sun.yunxin.entity.RepaymentPlanExtraData;
import com.zufangbao.sun.yunxin.entity.SourceDocumentStatus;
import com.zufangbao.sun.yunxin.entity.api.VoucherPayer;
import com.zufangbao.sun.yunxin.entity.api.VoucherType;
import com.zufangbao.sun.yunxin.entity.api.deduct.DeductApplication;
import com.zufangbao.sun.yunxin.entity.api.deduct.DeductPlan;
import com.zufangbao.sun.yunxin.entity.api.deduct.SourceType;
import com.zufangbao.sun.yunxin.entity.model.thirdPartVoucher.VoucherLogIssueStatus;
import com.zufangbao.sun.yunxin.entity.model.thirdPartVoucher.VoucherLogStatus;
import com.zufangbao.sun.yunxin.entity.model.thirdPartVoucher.VoucherSource;
import com.zufangbao.sun.yunxin.entity.repayment.FeeType;
import com.zufangbao.sun.yunxin.entity.repayment.RepaymentWay;
import com.zufangbao.sun.yunxin.entity.thirdPartVoucher.ThirdPartyPayExecStatus;
import com.zufangbao.sun.yunxin.entity.thirdPartVoucher.ThirdPartyVoucherCommandLog;
import com.zufangbao.sun.yunxin.service.RepaymentPlanExtraDataService;
import com.zufangbao.sun.yunxin.service.RepaymentPlanService;
import com.zufangbao.sun.yunxin.service.repayment.RepaymentOrderItemChargeService;
import com.zufangbao.sun.yunxin.service.repayment.RepaymentOrderItemService;
import com.zufangbao.wellsfargo.silverpool.cashauditing.entity.voucher.CounterAccountType;
import com.zufangbao.wellsfargo.silverpool.cashauditing.entity.voucher.JournalVoucher;
import com.zufangbao.wellsfargo.silverpool.cashauditing.entity.voucher.JournalVoucherStatus;
import com.zufangbao.sun.yunxin.enums.voucher.JournalVoucherType;
import com.zufangbao.wellsfargo.silverpool.cashauditing.entity.voucher.SettlementModes;
import com.zufangbao.wellsfargo.silverpool.cashauditing.entity.voucher.SourceDocument;
import com.zufangbao.wellsfargo.silverpool.cashauditing.entity.voucher.SourceDocumentDetail;
import com.zufangbao.wellsfargo.silverpool.cashauditing.entity.voucher.SourceDocumentDetailCheckState;
import com.zufangbao.wellsfargo.silverpool.cashauditing.entity.voucher.SourceDocumentDetailStatus;
import com.zufangbao.wellsfargo.silverpool.cashauditing.entity.voucher.SourceDocumentType;
import com.zufangbao.wellsfargo.silverpool.cashauditing.handler.LedgerBookVirtualAccountHandler;
import com.zufangbao.wellsfargo.silverpool.cashauditing.service.JournalVoucherService;
import com.zufangbao.wellsfargo.silverpool.cashauditing.service.SourceDocumentDetailService;
import com.zufangbao.wellsfargo.silverpool.cashauditing.service.SourceDocumentService;
import com.zufangbao.wellsfargo.yunxin.model.order.DeductReturnModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestMorganStanley.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration
@Slf4j
@ActiveProfiles(value = "test")
public class CommandApiRepaymentOrderControllerTest {

    @Autowired
    private CommandApiRepaymentOrderController commandApiRepaymentOrderController;

    @Autowired
    private RepaymentOrderService repaymentOrderService;

    @Autowired
    private PaymentOrderService paymentOrderService;

    @Autowired
    private DeductApplicationService deductApplicationService;

    @Autowired
    private RepaymentOrderItemChargeService repaymentOrderItemChargeService;

    @Autowired
    private RepaymentOrderItemService repaymentOrderItemService;

    @Autowired
    private RepaymentPlanExtraDataService repaymentPlanExtraDataService;

    @Autowired
    private RepaymentPlanService repaymentPlanService;

    @Autowired
    private InternalNotifyController internalNotifyController;

    @Autowired
    private ThirdPartVoucherCommandLogService thirdPartVoucherCommandLogService;

    @Autowired
    private DeductPlanService deductPlanService;

    @Autowired
    private SourceDocumentService sourceDocumentService;

    @Autowired
    private SourceDocumentDetailService sourceDocumentDetailService;

    @Autowired
    private JournalVoucherService journalVoucherService;

    @Autowired
    private LedgerBookVirtualAccountHandler ledgerBookVirtualAccountHandler;

    @Autowired
    private VirtualAccountService virtualAccountService;

    @Autowired
    private LedgerBookStatHandler ledgerBookStatHandler;

    private static final Log logger = LogFactory.getLog(CommandApiRepaymentOrderControllerTest.class);

    @Test
    @Sql("classpath:test_contra/test_AddRepaymentOrder.sql")
    public void test1() {
        String fn = "90001";
        String orderRequestNo = UUID.randomUUID().toString();
        String orderUniqueId = UUID.randomUUID().toString();
        String transType = "0";
        String financialContractNo = "G08200";
        String orderAmount = "1000.00";
        String repaymentOrderDetail = "";
        String remark = "";
        String orderNotifyUrl = "";


        List<RepaymentOrderDetailInfoModel> repaymentOrderDetailInfoList = new ArrayList<>();
        String feeType = FeeType.PRINCIPAL.getCode();
        BigDecimal actualAmount = new BigDecimal("100");
        String feeType2 = FeeType.INTEREST.getCode();
        BigDecimal actualAmount2 = new BigDecimal("200");
        String feeType3 = FeeType.LOAN_SERVICE_FEE.getCode();
        BigDecimal actualAmount3 = new BigDecimal("300");
        repaymentOrderDetailInfoList.add(new RepaymentOrderDetailInfoModel(feeType, actualAmount));
        repaymentOrderDetailInfoList.add(new RepaymentOrderDetailInfoModel(feeType2, actualAmount2));
        repaymentOrderDetailInfoList.add(new RepaymentOrderDetailInfoModel(feeType3, actualAmount3));


        List<RepaymentOrderDetail> orderDetailList = new ArrayList<>();

        String contractNo1 = "192992";
        String contractUniqueId1 = "";
        String repaymentWay1 = RepaymentWay.MERCHANT_TRANSFER.getCode();
        String repaymentBusinessNo1 = "123213";
        String plannedDate = "20170605";
        String detailsTotalAmount = "500.00";
        String contractNo2 = "1212";
        String contractUniqueId2 = "";
        String repaymentWay2 = RepaymentWay.MERCHANT_TRANSFER.getCode();
        String repaymentBusinessNo2 = "1321321";
        String plannedDate2 = "20170405";
        String detailsTotalAmount2 = "500.00";
        String detailsAmountJsonList1 = JsonUtils.toJsonString(repaymentOrderDetailInfoList);

        RepaymentOrderDetail detail1 = new RepaymentOrderDetail("", contractNo1, contractUniqueId1, repaymentWay1,
                repaymentBusinessNo1, plannedDate, detailsTotalAmount, detailsAmountJsonList1, 0, "");
        detail1.setPlannedDate("2017-06-06 01:02:03");
        orderDetailList.add(detail1);

        RepaymentOrderDetail detail2 = new RepaymentOrderDetail("", contractNo2, contractUniqueId2, repaymentWay2,
                repaymentBusinessNo2, plannedDate2, detailsTotalAmount2, detailsAmountJsonList1, 0, "");
        detail2.setPlannedDate("2017-06-06 04:05:06");

        orderDetailList.add(detail2);


        repaymentOrderDetail = JsonUtils.toJsonString(orderDetailList);
        OrderRequestModel orderRequestModel = new OrderRequestModel(orderRequestNo, orderUniqueId,
                transType, financialContractNo, orderAmount, repaymentOrderDetail, remark, orderNotifyUrl);

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        String message = commandApiRepaymentOrderController.addRepaymentOrder(request, response, orderRequestModel);
        logger.info("result: " + message);
        Result result = JsonUtils.parse(message, Result.class);

        Assert.assertEquals("成功!", result.getMessage());
    }

    @Test
    public void test2() throws IOException {
        String orderDetailFilePath = "./src/test/resources/data/donotdeleteme/donotDeleteMe.csv";
        File file = new File(orderDetailFilePath);
        ExcelUtil<RepaymentOrderDetail> repaymentOrderDetailExcelUtil = new ExcelUtil<>(RepaymentOrderDetail.class);
        List<RepaymentOrderDetail> details = repaymentOrderDetailExcelUtil.importCSV(file);
        Assert.assertEquals(2, details.size());
        System.out.println(JsonUtils.toJsonString(details));
        int i = 0;
        int j = 0;
        for (RepaymentOrderDetail repaymentOrderDetail : details) {
            i++;
            j = 0;
            System.out.println(JsonUtils.toJsonString(repaymentOrderDetail));
            List<RepaymentOrderDetailInfoModel> detailInfoModelList = repaymentOrderDetail.getFormatRepaymentOrderDetailInfoList();
            assertNotNull(detailInfoModelList);
            for (RepaymentOrderDetailInfoModel model : detailInfoModelList) {
                j++;
                if (model == null || model.getActualAmount() == null) {
                    fail();
                }
                assertNotNull(model.getActualAmount());
                assertNotNull(model.getFeeType());
            }
        }

        List<RepaymentOrderDetail> orderDetails = JsonUtils.parseArray(JsonUtils.toJsonString(details), RepaymentOrderDetail.class);
        Assert.assertEquals(2, orderDetails.size());

    }


    @Test
    @Sql("classpath:test_contra/test_RepaymentOrderForSingleContractEasyPay.sql")
    public void testRepaymentOrderSingleForEasyPay() {

        String fn = "90001";
        String orderRequestNo = UUID.randomUUID().toString();
        String orderUniqueId = UUID.randomUUID().toString();
        String transType = "0";
        String financialContractNo = "G08200";
        String orderAmount = "1000.00";
        String repaymentOrderDetail = "";
        String remark = "";
        String orderNotifyUrl = "";

        List<RepaymentOrderDetailInfoModel> repaymentOrderDetailInfoList = new ArrayList<>();
        String feeType = FeeType.PRINCIPAL.getCode();
        BigDecimal actualAmount = new BigDecimal("100");
        String feeType2 = FeeType.INTEREST.getCode();
        BigDecimal actualAmount2 = new BigDecimal("200");
        String feeType3 = FeeType.LOAN_SERVICE_FEE.getCode();
        BigDecimal actualAmount3 = new BigDecimal("300");
        repaymentOrderDetailInfoList.add(new RepaymentOrderDetailInfoModel(feeType, actualAmount));
        repaymentOrderDetailInfoList.add(new RepaymentOrderDetailInfoModel(feeType2, actualAmount2));
        repaymentOrderDetailInfoList.add(new RepaymentOrderDetailInfoModel(feeType3, actualAmount3));

        List<RepaymentOrderDetail> orderDetailList = new ArrayList<>();

        String contractNo1 = "contract_no";
        String contractUniqueId1 = "";
        String repaymentWay1 = RepaymentWay.MERCHANT_DEDUCT_EASY_PAY.getCode();
        String repaymentBusinessNo1 = "123213";
        String plannedDate = "20170605";
        String detailsTotalAmount = "500.00";
        String contractNo2 = "contract_no";
        String contractUniqueId2 = "";
        String repaymentWay2 = RepaymentWay.MERCHANT_DEDUCT_EASY_PAY.getCode();
        String repaymentBusinessNo2 = "1321321";
        String plannedDate2 = "20170405";
        String detailsTotalAmount2 = "500.00";
        String detailsAmountJsonList1 = JsonUtils.toJsonString(repaymentOrderDetailInfoList);

        RepaymentOrderDetail detail1 = new RepaymentOrderDetail("", contractNo1, contractUniqueId1, repaymentWay1,
                repaymentBusinessNo1, plannedDate, detailsTotalAmount, detailsAmountJsonList1, 0, "");
        detail1.setPlannedDate("2017-06-06 01:02:03");
        orderDetailList.add(detail1);

        RepaymentOrderDetail detail2 = new RepaymentOrderDetail("", contractNo2, contractUniqueId2, repaymentWay2,
                repaymentBusinessNo2, plannedDate2, detailsTotalAmount2, detailsAmountJsonList1, 0, "");
        detail2.setPlannedDate("2017-06-06 04:05:06");

        orderDetailList.add(detail2);


        repaymentOrderDetail = JsonUtils.toJsonString(orderDetailList);
        OrderRequestModel orderRequestModel = new OrderRequestModel(orderRequestNo, orderUniqueId,
                transType, financialContractNo, orderAmount, repaymentOrderDetail, remark, orderNotifyUrl);

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        String message = commandApiRepaymentOrderController.addRepaymentOrder(request, response, orderRequestModel);
        logger.info("result: " + message);
        Result result = JsonUtils.parse(message, Result.class);
        Assert.assertEquals("成功!", result.getMessage());


        //校验 还款订单  repaymentOrder
        List<RepaymentOrder> orderList = repaymentOrderService.loadAll(RepaymentOrder.class);
        Assert.assertEquals(1, orderList.size());

        RepaymentOrder repaymentOrder = orderList.get(0);
        Assert.assertEquals(new BigDecimal("1000.00"), repaymentOrder.getOrderAmount());
        Assert.assertEquals("contract_uuid_1", repaymentOrder.getFirstContractUuid());
        Assert.assertEquals("financial_contract_uuid_1", repaymentOrder.getFinancialContractUuid());
        Assert.assertEquals("G08200", repaymentOrder.getFinancialContractNo());
        Assert.assertEquals(RepaymentWayGroupType.ONLINE_DEDUCT_REPAYMENT_ORDER_EASY_PAY_TYPE, repaymentOrder.getFirstRepaymentWayGroup());
        Assert.assertEquals(OrderAliveStatus.PAYMENT_ORDER_CREATE, repaymentOrder.getOrderAliveStatus());
        Assert.assertEquals(OrderCheckStatus.VERIFICATION_SUCCESS, repaymentOrder.getOrderCheckStatus());
        Assert.assertEquals(OrderDBStatus.PAYMENT_ORDER_DOWN, repaymentOrder.getOrderDbStatus());
        Assert.assertEquals(OrderPayStatus.PAYING, repaymentOrder.getOrderPayStatus());
        Assert.assertEquals(OrderRecoverResult.NONE, repaymentOrder.getOrderRecoverResult());
        Assert.assertEquals(OrderRecoverStatus.TO_PAY, repaymentOrder.getOrderRecoverStatus());
        Assert.assertEquals(RepaymentOrderSourceStatus.ORDINARY, repaymentOrder.getSourceStatus());

        List<PaymentOrder> paymentOrderList = paymentOrderService.loadAll(PaymentOrder.class);
        Assert.assertEquals(1, paymentOrderList.size());

        PaymentOrder paymentOrder = paymentOrderList.get(0);

        //校验 deductApplication
        List<DeductApplication> deductApplicationList = deductApplicationService.loadAll(DeductApplication.class);
        Assert.assertEquals(1, deductApplicationList.size());
        DeductApplication deductApplication = deductApplicationList.get(0);
        Assert.assertEquals(new BigDecimal("0.00"), deductApplication.getActualDeductTotalAmount());
        Assert.assertEquals(new BigDecimal("1000.00"), deductApplication.getPlannedDeductTotalAmount());
        Assert.assertEquals(paymentOrder.getDeductRequestNo(), deductApplication.getDeductId());
        Assert.assertEquals(paymentOrder.getDeductRequestNo(), deductApplication.getRequestNo());
        Assert.assertEquals(new Integer(0), deductApplication.getActualNotifyNumber());
        Assert.assertEquals(new Integer(0), deductApplication.getBusinessCheckStatus());
        Assert.assertEquals(DeductApplicationExecutionStatus.PROCESSING, deductApplication.getExecutionStatus());
        Assert.assertEquals("financial_contract_uuid_1", deductApplication.getFinancialContractUuid());
        Assert.assertEquals(null, deductApplication.getGateway());
        Assert.assertEquals(repaymentOrder.getIp(), deductApplication.getIp());
        Assert.assertEquals(new Integer(0), deductApplication.getNotifyStatus());
//		Assert.assertEquals(paymentOrder.getUuid(), deductApplication.getPaymentOrderUuid());
        Assert.assertEquals(new Integer(1), deductApplication.getPlanNotifyNumber());
        Assert.assertEquals(RecordStatus.UNRECORDED, deductApplication.getRecordStatus());
        Assert.assertEquals(null, deductApplication.getRepaymentType());
        Assert.assertEquals("G08200", deductApplication.getFinancialProductCode());
        Assert.assertEquals(SourceType.REPAYMENTORDER, deductApplication.getSourceType());
        Assert.assertEquals("contract_no", deductApplication.getContractNo());
        Assert.assertEquals("contract_unique_id_1", deductApplication.getContractUniqueId());
        Assert.assertEquals("customer_name", deductApplication.getCustomerName());


        //校验 支付单  paymentOrder
        Assert.assertEquals(1, paymentOrderList.size());
        Assert.assertEquals(new BigDecimal("1000.00"), paymentOrder.getAmount());
        Assert.assertEquals(AccountSide.DEBIT, paymentOrder.getAccountSide());
        Assert.assertEquals("中国邮政储蓄银行", paymentOrder.getCounterAccountBankName());
        Assert.assertEquals("测试用户1", paymentOrder.getCounterAccountName());
        Assert.assertEquals("pay_ac_no_1", paymentOrder.getCounterAccountNo());
        Assert.assertEquals("云南国际信托有限公司", paymentOrder.getHostAccountBankName());
        Assert.assertEquals("云南信托国际有限公司", paymentOrder.getHostAccountName());
        Assert.assertEquals("600000000001", paymentOrder.getHostAccountNo());
        Assert.assertEquals(AliveStatus.CREATE, paymentOrder.getAliveStatus());
        Assert.assertEquals("financial_contract_uuid_1", paymentOrder.getFinancialContractUuid());
        Assert.assertEquals("G08200", paymentOrder.getFinancialContractNo());
        Assert.assertEquals(RepaymentOrderPayResult.TO_PAY, paymentOrder.getOrderPayResultStatus());
        Assert.assertEquals(null, paymentOrder.getOutlierDocumentIdentity());
        Assert.assertEquals(deductApplication.getDeductApplicationUuid(), paymentOrder.getOutlierDocumentUuid());
        Assert.assertEquals(PayWay.MERCHANT_DEDUCT_EASY_PAY, paymentOrder.getPayWay());
        Assert.assertEquals(RecoverStatus.UNRECORDED, paymentOrder.getRecoverStatus());
        Assert.assertEquals(PayStatus.IN_PAY, paymentOrder.getPayStatus());

    }

    @Test
    @Sql("classpath:test_contra/test_RepaymentOrderForSingleContractEasyPay_cancel.sql")
    public void testRepaymentOrderSingleForEasyPayCancel() {

        String fn = "90001";
        String orderRequestNo = UUID.randomUUID().toString();
        String orderUniqueId = "orderUniqueId";
        String transType = "1";
        String financialContractNo = "G08200";
        String orderAmount = "1000.00";
        String repaymentOrderDetail = "";
        String remark = "";
        String orderNotifyUrl = "";

        List<RepaymentOrderDetailInfoModel> repaymentOrderDetailInfoList = new ArrayList<>();
        String feeType = FeeType.PRINCIPAL.getCode();
        BigDecimal actualAmount = new BigDecimal("100");
        String feeType2 = FeeType.INTEREST.getCode();
        BigDecimal actualAmount2 = new BigDecimal("200");
        String feeType3 = FeeType.LOAN_SERVICE_FEE.getCode();
        BigDecimal actualAmount3 = new BigDecimal("300");
        repaymentOrderDetailInfoList.add(new RepaymentOrderDetailInfoModel(feeType, actualAmount));
        repaymentOrderDetailInfoList.add(new RepaymentOrderDetailInfoModel(feeType2, actualAmount2));
        repaymentOrderDetailInfoList.add(new RepaymentOrderDetailInfoModel(feeType3, actualAmount3));

        List<RepaymentOrderDetail> orderDetailList = new ArrayList<>();

        String contractNo1 = "contract_no";
        String contractUniqueId1 = "";
        String repaymentWay1 = RepaymentWay.MERCHANT_DEDUCT_EASY_PAY.getCode();
        String repaymentBusinessNo1 = "123213";
        String plannedDate = "20170605";
        String detailsTotalAmount = "500.00";
        String contractNo2 = "contract_no";
        String contractUniqueId2 = "";
        String repaymentWay2 = RepaymentWay.MERCHANT_DEDUCT_EASY_PAY.getCode();
        String repaymentBusinessNo2 = "1321321";
        String plannedDate2 = "20170405";
        String detailsTotalAmount2 = "500.00";
        String detailsAmountJsonList1 = JsonUtils.toJsonString(repaymentOrderDetailInfoList);

        RepaymentOrderDetail detail1 = new RepaymentOrderDetail("", contractNo1, contractUniqueId1, repaymentWay1,
                repaymentBusinessNo1, plannedDate, detailsTotalAmount, detailsAmountJsonList1, 0, "");
        detail1.setPlannedDate("2017-06-06 01:02:03");
        orderDetailList.add(detail1);

        RepaymentOrderDetail detail2 = new RepaymentOrderDetail("", contractNo2, contractUniqueId2, repaymentWay2,
                repaymentBusinessNo2, plannedDate2, detailsTotalAmount2, detailsAmountJsonList1, 0, "");
        detail2.setPlannedDate("2017-06-06 04:05:06");

        orderDetailList.add(detail2);


        repaymentOrderDetail = JsonUtils.toJsonString(orderDetailList);
        OrderRequestModel orderRequestModel = new OrderRequestModel(orderRequestNo, orderUniqueId,
                transType, financialContractNo, orderAmount, repaymentOrderDetail, remark, orderNotifyUrl);


        List<RepaymentOrderItemCharge> itemCharges = repaymentOrderItemChargeService.loadAll(RepaymentOrderItemCharge.class);
        assertEquals(1, itemCharges.size());

        List<RepaymentOrderItem> orderItems = repaymentOrderItemService.loadAll(RepaymentOrderItem.class);
        assertEquals(1, orderItems.size());


        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        String message = commandApiRepaymentOrderController.addRepaymentOrder(request, response, orderRequestModel);
        logger.info("result: " + message);
        Result result = JsonUtils.parse(message, Result.class);
        Assert.assertEquals("成功!", result.getMessage());


        List<RepaymentOrderItemCharge> itemCharges2 = repaymentOrderItemChargeService.getRepaymentOrderItemChargeByItemUuid("order_detail_uuid_1");
        assertEquals(0, itemCharges2.size());

        List<RepaymentOrderItem> orderItems2 = repaymentOrderItemService.getRepaymentOrderItems("order_uuid_1");
        assertEquals(0, orderItems2.size());


        AssetSet assetSet2 = repaymentPlanService.getUniqueRepaymentPlanByUuid("asset_uuid_1");
        RepaymentPlanExtraData extraData = repaymentPlanExtraDataService.getRepaymentPlanExtraDataByAssetUuid(assetSet2.getAssetUuid());

        assertEquals(OrderPaymentStatus.UNEXECUTINGORDER, assetSet2.getOrderPaymentStatus());
        assertEquals("EMPTY_UUID", assetSet2.getVersionLock());

        assertEquals(new BigDecimal("0.00"), extraData.getOrderPayingAmount());
        assertEquals(new BigDecimal("0.00"), extraData.getOrderTotalAmount());
        assertEquals(0, extraData.getOrderItemCount());
    }

    @Test
    @Sql("classpath:test_contra/test_RepaymentOrderForSingleContractEasyPay_notify_deduct.sql")
    public void testNotifyDeduct() {

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        DeductReturnModel deductReturnModel = new DeductReturnModel();
        deductReturnModel.setStatus(DeductApplicationExecutionStatus.SUCCESS.getOrdinal());
        deductReturnModel.setOrderNo("request_Id");
        deductReturnModel.setLastModifiedTime(DateUtils.format(new Date()));
        deductReturnModel.setComment("remark");

        String deductReturnModelString= com.zufangbao.sun.utils.JsonUtils.toJSONString(deductReturnModel);
        internalNotifyController.notifyDeduct(deductReturnModelString);

        PaymentOrder paymentOrder = paymentOrderService.getActivePaymenrOrderByUuid("payment_order_uuid");
        RepaymentOrder order = repaymentOrderService.getRepaymentOrderByUuid("order_uuid_1");
        DeductPlan deductPlan = deductPlanService.getDeductPlanByDeductApplicationUuid("deduct_application_uuid", DeductApplicationExecutionStatus.SUCCESS);

        assertEquals(PayStatus.PAY_SUCCESS, paymentOrder.getPayStatus());
        assertEquals(RepaymentOrderPayResult.PAY_SUCCESS, paymentOrder.getOrderPayResultStatus());

        assertEquals(new BigDecimal("500.00"), order.getPaidAmount());
        assertEquals(OrderPayStatus.PAY_END, order.getOrderPayStatus());

        //校验 第三方支付凭证
        List<ThirdPartyVoucherCommandLog> thirdPartVoucherCommandLogList = thirdPartVoucherCommandLogService.loadAll(ThirdPartyVoucherCommandLog.class);
        assertEquals(1, thirdPartVoucherCommandLogList.size());

        ThirdPartyVoucherCommandLog thirdPartyVoucherCommandLog = thirdPartVoucherCommandLogList.get(0);

        assertEquals("outlier_document_identity", thirdPartyVoucherCommandLog.getBankTransactionNo());
        assertEquals("request_Id", thirdPartyVoucherCommandLog.getBatchNo());
        assertNotNull(thirdPartyVoucherCommandLog.getBatchUuid());
        assertEquals("contract_unique_id_1", thirdPartyVoucherCommandLog.getContractUniqueId());
        assertEquals(ThirdPartyPayExecStatus.SUCCESS, thirdPartyVoucherCommandLog.getExecutionStatus());
        assertEquals("financial_contract_code_1", thirdPartyVoucherCommandLog.getFinancialContractNo());
        assertEquals("financial_contract_uuid_1", thirdPartyVoucherCommandLog.getFinancialContractUuid());
        assertEquals("", thirdPartyVoucherCommandLog.getIp());

        assertEquals("pay_ac_no_1", thirdPartyVoucherCommandLog.getPaymentAccountNo());
        assertEquals("测试用户1", thirdPartyVoucherCommandLog.getPaymentName());
        assertEquals(VoucherLogStatus.PROCESSING, thirdPartyVoucherCommandLog.getProcessStatus());
        assertEquals("600000000001", thirdPartyVoucherCommandLog.getReceivableAccountNo());
        assertNotNull(thirdPartyVoucherCommandLog.getRequestNo());
        assertEquals("request_Id", thirdPartyVoucherCommandLog.getTradeUuid());
        assertEquals(new BigDecimal("500.00"), thirdPartyVoucherCommandLog.getTranscationAmount());
        assertEquals(deductPlan.getPaymentGateway(), thirdPartyVoucherCommandLog.getTranscationGateway());
        assertNotNull(thirdPartyVoucherCommandLog.getVersionNo());
        assertNotNull(thirdPartyVoucherCommandLog.getVoucherUuid());
        assertEquals(VoucherSource.APP_UPLOAD, thirdPartyVoucherCommandLog.getVoucherSource());
        assertEquals(VoucherLogIssueStatus.NOT_ISSUED, thirdPartyVoucherCommandLog.getVoucherLogIssueStatus());

        //校验资产状态
        AssetSet assetSet = repaymentPlanService.getRepaymentPlanByRepaymentCode("repayment_plan_no_1");

        assertEquals(AssetClearStatus.CLEAR, assetSet.getAssetStatus());
        assertEquals(OrderPaymentStatus.UNEXECUTINGORDER, assetSet.getOrderPaymentStatus());
        assertEquals(OnAccountStatus.WRITE_OFF, assetSet.getOnAccountStatus());

        VirtualAccount virtualAccount = virtualAccountService.getVirtualAccountByCustomerUuid("customerUuid1");

        //校验ledger book明细余额,不变
        BigDecimal balance = ledgerBookVirtualAccountHandler.get_balance_of_customer("yunxin_ledger_book", "company_customer_uuid_1");
        assertEquals(0, balance.compareTo(virtualAccount.getTotalBalance()));

        //校验ledger_book的应收资产
        BigDecimal receivable_amount = ledgerBookStatHandler.get_receivable_amount("yunxin_ledger_book", assetSet.getAssetUuid(), "customerUuid1");
        assertEquals(0, new BigDecimal(0).compareTo(receivable_amount));

        //校验 SourceDocument
        List<SourceDocument> sourceDocumentList = sourceDocumentService.loadAll(SourceDocument.class);
        assertEquals(1, sourceDocumentList.size());
        SourceDocument sourceDocument = sourceDocumentList.get(0);

        assertEquals(SourceDocumentType.NOTIFY, sourceDocument.getSourceDocumentType());
        assertEquals(SourceDocumentStatus.SIGNED, sourceDocument.getSourceDocumentStatus());
        assertEquals(AccountSide.DEBIT, sourceDocument.getSourceAccountSide());
        assertEquals(new BigDecimal("500.00"), sourceDocument.getBookingAmount());
        assertEquals(new BigDecimal("500.00"), sourceDocument.getPlanBookingAmount());
        assertEquals(RepaymentAuditStatus.CREATE, sourceDocument.getAuditStatus());
        assertEquals("deduct_application_uuid", sourceDocument.getOutlierDocumentUuid());
        assertEquals("pay_ac_no_1", sourceDocument.getOutlierCounterPartyAccount());
        assertEquals("测试用户1", sourceDocument.getOutlierCounterPartyName());
        assertEquals("600000000001", sourceDocument.getOutlierAccount());
        assertEquals("云南信托国际有限公司", sourceDocument.getOutlieAccountName());
        assertEquals("financial_contract_uuid_1", sourceDocument.getFinancialContractUuid());
        assertEquals("outlier_document_identity", sourceDocument.getOutlierSerialGlobalIdentity());
        assertEquals(SettlementModes.REMITTANCE, sourceDocument.getOutlierSettlementModes());
        assertEquals(SourceDocument.FIRSTOUTLIER_PAYMENT_ORDER, sourceDocument.getFirstOutlierDocType());
        assertEquals("payment_order_uuid", sourceDocument.getFirstAccountId());
        assertEquals("1", sourceDocument.getSecondOutlierDocType());

        assertEquals("request_Id", sourceDocument.getSecondAccountId());
        assertEquals("contract_uuid_1", sourceDocument.getRelatedContractUuid());
        assertEquals("companyuuid", sourceDocument.getFirstPartyId());
        assertEquals("云南信托", sourceDocument.getFirstPartyName());
        assertEquals("customerUuid1", sourceDocument.getSecondPartyId());


        //校验  SourceDocumentDetail
        List<SourceDocumentDetail> details = sourceDocumentDetailService.loadAll(SourceDocumentDetail.class);
        assertEquals(2, details.size());
        SourceDocumentDetail detail1 = details.get(0);
        SourceDocumentDetail detail2 = details.get(1);

        assertEquals(sourceDocument.getSourceDocumentUuid(), detail1.getSourceDocumentUuid());
        assertEquals("contract_unique_id_1", detail1.getContractUniqueId());
        assertEquals("repayment_plan_no_1", detail1.getRepaymentPlanNo());
        assertEquals(new BigDecimal("400.00"), detail1.getAmount());
        assertEquals(com.zufangbao.sun.yunxin.entity.api.VoucherSource.THIRD_PARTY_DEDUCTION_VOUCHER.getKey(), detail1.getFirstType());
        assertEquals("order_uuid_1", detail1.getFirstNo());
        assertEquals("order_detail_uuid_1", detail1.getSecondNo());
        assertEquals(VoucherType.THIRD_PARTY_DEDUCTION_VOUCHER.getKey(), detail1.getSecondType());
        assertEquals(VoucherPayer.LOANER, detail1.getPayer());
        assertEquals("600000000001", detail1.getReceivableAccountNo());
        assertEquals("pay_ac_no_1", detail1.getPaymentAccountNo());
        assertEquals("中国邮政储蓄银行", detail1.getPaymentBank());
        assertEquals("测试用户1", detail1.getPaymentName());
        assertEquals(SourceDocumentDetailCheckState.CHECK_SUCCESS, detail1.getCheckState());
        assertEquals(SourceDocumentDetailStatus.SUCCESS, detail1.getStatus());
        assertEquals("financial_contract_uuid_1", detail1.getFinancialContractUuid());

        assertEquals(sourceDocument.getSourceDocumentUuid(), detail2.getSourceDocumentUuid());
        assertEquals("contract_unique_id_1", detail2.getContractUniqueId());
        assertEquals("repayment_plan_no_1", detail2.getRepaymentPlanNo());
        assertEquals(new BigDecimal("100.00"), detail2.getAmount());
        assertEquals(com.zufangbao.sun.yunxin.entity.api.VoucherSource.THIRD_PARTY_DEDUCTION_VOUCHER.getKey(), detail2.getFirstType());
        assertEquals("order_uuid_1", detail2.getFirstNo());
        assertEquals("order_detail_uuid_2", detail2.getSecondNo());
        assertEquals(VoucherType.THIRD_PARTY_DEDUCTION_VOUCHER.getKey(), detail2.getSecondType());
        assertEquals(VoucherPayer.LOANER, detail2.getPayer());
        assertEquals("600000000001", detail2.getReceivableAccountNo());
        assertEquals("pay_ac_no_1", detail2.getPaymentAccountNo());
        assertEquals("中国邮政储蓄银行", detail2.getPaymentBank());
        assertEquals("测试用户1", detail2.getPaymentName());
        assertEquals(SourceDocumentDetailCheckState.CHECK_SUCCESS, detail2.getCheckState());
        assertEquals(SourceDocumentDetailStatus.SUCCESS, detail2.getStatus());
        assertEquals("financial_contract_uuid_1", detail2.getFinancialContractUuid());

        //校验 jv
        List<JournalVoucher> jvs = journalVoucherService.loadAll(JournalVoucher.class);
        assertEquals(2, jvs.size());
        JournalVoucher jv1 = jvs.get(0);
        JournalVoucher jv2 = jvs.get(1);

        assertEquals(sourceDocument.getSourceDocumentUuid(), jv1.getSourceDocumentIdentity());
        assertEquals(detail1.getUuid(), jv1.getSourceDocumentUuid());
        assertEquals(CounterAccountType.BankAccount, jv1.getCounterAccountType());
        assertEquals("pay_ac_no_1", jv1.getCounterPartyAccount());
        assertEquals("测试用户1", jv1.getCounterPartyName());

        assertEquals(JournalVoucherType.THIRD_PARTY_DEDUCT_VOUCHER, jv1.getJournalVoucherType());
        assertEquals(JournalVoucherStatus.VOUCHER_ISSUED, jv1.getStatus());

        assertEquals("financial_contract_uuid_1", jv1.getRelatedBillContractInfoLv1());
        assertEquals("contract_uuid_1", jv1.getRelatedBillContractInfoLv2());
        assertEquals("asset_uuid_1", jv1.getRelatedBillContractInfoLv3());
        assertEquals("云南信托", jv1.getRelatedBillContractNoLv1());
        assertEquals("contract_no_1", jv1.getRelatedBillContractNoLv2());
        assertEquals("repayment_plan_no_1", jv1.getRelatedBillContractNoLv3());
        assertEquals(new BigDecimal("500.00"), jv1.getCashFlowAmount());
        assertEquals(new BigDecimal("400.00"), jv1.getBookingAmount());
        assertEquals(null, jv1.getCashFlowChannelType());
        assertEquals("中国邮政储蓄银行", jv1.getBankIdentity());
        assertEquals("pay_ac_no_1", jv1.getSourceDocumentCounterPartyAccount());
        assertEquals("测试用户1", jv1.getSourceDocumentCounterPartyName());


        assertEquals(sourceDocument.getSourceDocumentUuid(), jv2.getSourceDocumentIdentity());
        assertEquals(detail2.getUuid(), jv2.getSourceDocumentUuid());
        assertEquals(CounterAccountType.BankAccount, jv2.getCounterAccountType());
        assertEquals("pay_ac_no_1", jv2.getCounterPartyAccount());
        assertEquals("测试用户1", jv2.getCounterPartyName());

        assertEquals(JournalVoucherType.THIRD_PARTY_DEDUCT_VOUCHER, jv2.getJournalVoucherType());
        assertEquals(JournalVoucherStatus.VOUCHER_ISSUED, jv2.getStatus());

        assertEquals("financial_contract_uuid_1", jv2.getRelatedBillContractInfoLv1());
        assertEquals("contract_uuid_1", jv2.getRelatedBillContractInfoLv2());
        assertEquals("asset_uuid_1", jv2.getRelatedBillContractInfoLv3());
        assertEquals("云南信托", jv2.getRelatedBillContractNoLv1());
        assertEquals("contract_no_1", jv2.getRelatedBillContractNoLv2());
        assertEquals("repayment_plan_no_1", jv2.getRelatedBillContractNoLv3());
        assertEquals(new BigDecimal("500.00"), jv2.getCashFlowAmount());
        assertEquals(new BigDecimal("100.00"), jv2.getBookingAmount());
        assertEquals(null, jv2.getCashFlowChannelType());
        assertEquals("中国邮政储蓄银行", jv2.getBankIdentity());
        assertEquals("pay_ac_no_1", jv2.getSourceDocumentCounterPartyAccount());
        assertEquals("测试用户1", jv2.getSourceDocumentCounterPartyName());

    }

    @Test
    @Sql("classpath:test_contra/test_RepaymentOrderForSingleContractEasyPay_notify_deduct.sql")
    public void testNotifyDeduct2() {

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        DeductReturnModel deductReturnModel = new DeductReturnModel();
        deductReturnModel.setStatus(DeductApplicationExecutionStatus.PART_OF_SUCCESS.getOrdinal());
        deductReturnModel.setOrderNo("request_Id");
        deductReturnModel.setLastModifiedTime(DateUtils.format(new Date()));
        deductReturnModel.setComment("remark");

        String deductReturnModelString= com.zufangbao.sun.utils.JsonUtils.toJSONString(deductReturnModel);
        internalNotifyController.notifyDeduct(deductReturnModelString);
        PaymentOrder paymentOrder = paymentOrderService.getActivePaymenrOrderByUuid("payment_order_uuid");
        RepaymentOrder order = repaymentOrderService.getRepaymentOrderByUuid("order_uuid_1");
        DeductPlan deductPlan = deductPlanService.getDeductPlanByDeductApplicationUuid("deduct_application_uuid", DeductApplicationExecutionStatus.SUCCESS);

        assertEquals(PayStatus.PAY_SUCCESS, paymentOrder.getPayStatus());
        assertEquals(RepaymentOrderPayResult.PAYING, paymentOrder.getOrderPayResultStatus());

        assertEquals(new BigDecimal("500.00"), order.getPaidAmount());
        assertEquals(OrderPayStatus.PAY_END, order.getOrderPayStatus());

    }

    @Test
    @Sql("classpath:test_contra/test_RepaymentOrderForSingleContractEasyPay_notify_deduct.sql")
    public void testNotifyDeduct3() {

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        DeductReturnModel deductReturnModel = new DeductReturnModel();
        deductReturnModel.setStatus(DeductApplicationExecutionStatus.FAIL.getOrdinal());
        deductReturnModel.setOrderNo("request_Id");
        deductReturnModel.setLastModifiedTime(DateUtils.format(new Date()));
        deductReturnModel.setComment("remark");

        String deductReturnModelString= com.zufangbao.sun.utils.JsonUtils.toJSONString(deductReturnModel);
        internalNotifyController.notifyDeduct(deductReturnModelString);
        PaymentOrder paymentOrder = paymentOrderService.getActivePaymenrOrderByUuid("payment_order_uuid");
        RepaymentOrder order = repaymentOrderService.getRepaymentOrderByUuid("order_uuid_1");

        assertEquals(PayStatus.PAY_FAIL, paymentOrder.getPayStatus());
        assertEquals(RepaymentOrderPayResult.PAY_FAIL, paymentOrder.getOrderPayResultStatus());

        assertEquals(new BigDecimal("0.00"), order.getPaidAmount());
        assertEquals(OrderPayStatus.PAYING, order.getOrderPayStatus());

    }

}
