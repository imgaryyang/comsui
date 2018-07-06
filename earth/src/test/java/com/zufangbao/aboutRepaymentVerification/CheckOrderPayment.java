package com.zufangbao.aboutRepaymentVerification;

import com.demo2do.core.utils.DateUtils;
import com.zufangbao.refactor.method.RefactorMethod;
import com.zufangbao.sun.entity.repayment.order.RepaymentOrder;
import com.zufangbao.sun.entity.repayment.order.RepaymentStatus;
import com.zufangbao.sun.service.CashFlowService;
import com.zufangbao.sun.service.RepaymentOrderService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:/local/applicationContext-*.xml" })
public class CheckOrderPayment {
    @Autowired
    private CashFlowService cashFlowService;

    @Autowired
    private RepaymentOrderService repaymentOrderService;

    RefactorMethod refactorMethod = new RefactorMethod();

    private String uniqueId = "";
    String productCode = "CS0001";
    private String firstRepaymentNumber = "";
    String repaymentAccountNo = "";
    @Before
    public void setUp(){
        refactorMethod.deleteAllCashFlow();
        String totalAmount = "30000";
        uniqueId = UUID.randomUUID().toString() + 1;
        repaymentAccountNo = "6217857600016839330";
        String loanCustomerName = "秦操";
        String interest = "20";
        String loanCustomerNo = "11111117";
        String firstPlanDate = DateUtils.format(DateUtils.addMonths(new Date(), -1));
        String secondPlanDate = DateUtils.format(new Date());
        String thirdPlanDate = DateUtils.format(DateUtils.addMonths(new Date(), 1));

        refactorMethod.importAssetPackage(totalAmount,productCode,uniqueId,repaymentAccountNo,interest,loanCustomerNo,
                firstPlanDate,secondPlanDate,thirdPlanDate,loanCustomerName);

        firstRepaymentNumber = refactorMethod.queryRepaymentPlan(productCode,uniqueId,0);

        System.out.println(firstRepaymentNumber);
    }

    /**
     * 支付订单参数校验，paymentNo重复
     * @throws Exception
     */
    @Test
    public void checkPaymentParams1() throws Exception {
        String orderRequestNo = uniqueId;
        String transType = "0";
        String orderAmount = "10080";
        Long firstRepaymentWay = 2001l;
        String firstDetailTotalAmount = "10080";
        String firstDetailPrincipal = "10000";
        String firstDetailInterest = "20";
        String payWay = "0";
        Long firstFeeType = 1l;
        String repayScheduleNo = "";

        String paymentAccountName = "秦操";
        String paymentAmount = "5040";
        String paymentGateWay = "0";
        String tradeUuid = UUID.randomUUID().toString();
        SimpleDateFormat sim = new SimpleDateFormat("YYYY-MM-DD HH:mm:ss");
        String transactionTime = sim.format(new Date());

        String result = refactorMethod.applyRepaymentOderForOneDetail(orderRequestNo, orderRequestNo, transType, productCode, orderAmount, uniqueId,
                firstRepaymentNumber, firstRepaymentWay,
                firstDetailTotalAmount, firstDetailPrincipal,
                firstDetailInterest, firstFeeType,repayScheduleNo);

        if (!result.contains("订单已受理")) {
            throw new Exception("还款订单校验错误!");
        }
        RepaymentStatus repaymentStatus;
        while (true) {
            RepaymentOrder repaymentOrder = repaymentOrderService.getRepaymentOrderByUniqueId(uniqueId, productCode);
            if (null == repaymentOrder) {
                throw new Exception("还款订单不存在!");
            }
            repaymentStatus = repaymentOrder.transferToRepaymentStatus();

            if (repaymentStatus != RepaymentStatus.TO_VERIFY && repaymentStatus != RepaymentStatus.VERIFICATION_IN_PROCESS) {
                break;
            }
            Thread.sleep(5000l);
        }

        if (RepaymentStatus.VERIFICATION_SUCCESS != repaymentStatus) {
            throw new Exception("还款订单状态不正确");
        }

        String payResult = refactorMethod.payOrder(orderRequestNo, orderRequestNo, productCode, payWay, repaymentAccountNo,
                paymentAccountName, paymentGateWay
                , tradeUuid, paymentAmount, transactionTime);
        if (!payResult.contains("订单支付已受理")) {
            throw new Exception("订单支付失败");
        }

        String payResult2 = refactorMethod.payOrder(orderRequestNo, orderRequestNo, productCode, payWay, repaymentAccountNo,
                paymentAccountName, paymentGateWay
                , tradeUuid, paymentAmount, transactionTime);

        boolean outcome = payResult2.contains("商户支付编号不能重复");
        Assert.assertEquals(true,outcome);

    }

    /**
     * 支付订单参数校验，orderUniqueId和orderUuid都为空
     * @throws Exception
     */
    @Test
    public void checkPaymentParams2() throws Exception {
        String orderRequestNo = uniqueId;
        String transType = "0";
        String orderAmount = "10080";
        Long firstRepaymentWay = 1000l;
        String firstDetailTotalAmount = "10080";
        String firstDetailPrincipal = "10000";
        String firstDetailInterest = "20";
        String payWay = "0";
        Long firstFeeType = 1l;
        String repayScheduleNo = "";

        String paymentAccountName = "秦操";
        String paymentGateWay = "0";
        String tradeUuid = UUID.randomUUID().toString();
        SimpleDateFormat sim = new SimpleDateFormat("YYYY-MM-DD HH:mm:ss");
        String transactionTime = sim.format(new Date());

        String result = refactorMethod.applyRepaymentOderForOneDetail(orderRequestNo, orderRequestNo, transType, productCode, orderAmount, uniqueId,
                firstRepaymentNumber, firstRepaymentWay,
                firstDetailTotalAmount, firstDetailPrincipal,
                firstDetailInterest, firstFeeType,repayScheduleNo);

        if (!result.contains("订单已受理")) {
            throw new Exception("还款订单校验错误!");
        }
        RepaymentStatus repaymentStatus;
        while (true) {
            RepaymentOrder repaymentOrder = repaymentOrderService.getRepaymentOrderByUniqueId(uniqueId, productCode);
            if (null == repaymentOrder) {
                throw new Exception("还款订单不存在!");
            }
            repaymentStatus = repaymentOrder.transferToRepaymentStatus();

            if (repaymentStatus != RepaymentStatus.TO_VERIFY && repaymentStatus != RepaymentStatus.VERIFICATION_IN_PROCESS) {
                break;
            }
            Thread.sleep(5000l);
        }

        if (RepaymentStatus.VERIFICATION_SUCCESS != repaymentStatus) {
            throw new Exception("还款订单状态不正确");
        }

        String payResult = refactorMethod.payOrder(orderRequestNo, "", productCode, payWay, repaymentAccountNo,
                paymentAccountName, paymentGateWay
                , tradeUuid, orderAmount, transactionTime);

        boolean outcome = payResult.contains("不能同时为空");
        Assert.assertEquals(true,outcome);

    }


    /**
     * 支付订单参数校验，financialContractNo为空
     * @throws Exception
     */
    @Test
    public void checkPaymentParams3() throws Exception {
        String orderRequestNo = uniqueId;
        String transType = "0";
        String orderAmount = "10080";
        Long firstRepaymentWay = 1000l;
        String firstDetailTotalAmount = "10080";
        String firstDetailPrincipal = "10000";
        String firstDetailInterest = "20";
        String payWay = "0";
        Long firstFeeType = 1l;
        String repayScheduleNo = "";

        String financialContractNo = "";
        String paymentAccountName = "秦操";
        String paymentGateWay = "0";
        String tradeUuid = UUID.randomUUID().toString();
        SimpleDateFormat sim = new SimpleDateFormat("YYYY-MM-DD HH:mm:ss");
        String transactionTime = sim.format(new Date());

        String result = refactorMethod.applyRepaymentOderForOneDetail(orderRequestNo, orderRequestNo, transType, productCode, orderAmount, uniqueId,
                firstRepaymentNumber, firstRepaymentWay,
                firstDetailTotalAmount, firstDetailPrincipal,
                firstDetailInterest, firstFeeType,repayScheduleNo);

        if (!result.contains("订单已受理")) {
            throw new Exception("还款订单校验错误!");
        }
        RepaymentStatus repaymentStatus;
        while (true) {
            RepaymentOrder repaymentOrder = repaymentOrderService.getRepaymentOrderByUniqueId(uniqueId, productCode);
            if (null == repaymentOrder) {
                throw new Exception("还款订单不存在!");
            }
            repaymentStatus = repaymentOrder.transferToRepaymentStatus();

            if (repaymentStatus != RepaymentStatus.TO_VERIFY && repaymentStatus != RepaymentStatus.VERIFICATION_IN_PROCESS) {
                break;
            }
            Thread.sleep(5000l);
        }

        if (RepaymentStatus.VERIFICATION_SUCCESS != repaymentStatus) {
            throw new Exception("还款订单状态不正确");
        }

        String payResult = refactorMethod.payOrder(orderRequestNo, orderRequestNo, financialContractNo, payWay, repaymentAccountNo,
                paymentAccountName, paymentGateWay
                , tradeUuid, orderAmount, transactionTime);

        boolean outcome = payResult.contains("信托合同编号［financialContractNo],不能为空");
        Assert.assertEquals(true,outcome);

    }

    /**
     * 支付订单参数校验，financialContractNo不存在
     * @throws Exception
     */
    @Test
    public void checkPaymentParams4() throws Exception {
        String orderRequestNo = uniqueId;
        String transType = "0";
        String orderAmount = "10080";
        Long firstRepaymentWay = 1000l;
        String firstDetailTotalAmount = "10080";
        String firstDetailPrincipal = "10000";
        String firstDetailInterest = "20";
        String payWay = "0";
        Long firstFeeType = 1l;
        String repayScheduleNo = "";

        String financialContractNo = "CS00011";
        String paymentAccountName = "秦操";
        String paymentGateWay = "0";
        String tradeUuid = UUID.randomUUID().toString();
        SimpleDateFormat sim = new SimpleDateFormat("YYYY-MM-DD HH:mm:ss");
        String transactionTime = sim.format(new Date());

        String result = refactorMethod.applyRepaymentOderForOneDetail(orderRequestNo, orderRequestNo, transType, productCode, orderAmount, uniqueId,
                firstRepaymentNumber, firstRepaymentWay,
                firstDetailTotalAmount, firstDetailPrincipal,
                firstDetailInterest, firstFeeType,repayScheduleNo);

        if (!result.contains("订单已受理")) {
            throw new Exception("还款订单校验错误!");
        }
        RepaymentStatus repaymentStatus;
        while (true) {
            RepaymentOrder repaymentOrder = repaymentOrderService.getRepaymentOrderByUniqueId(uniqueId, productCode);
            if (null == repaymentOrder) {
                throw new Exception("还款订单不存在!");
            }
            repaymentStatus = repaymentOrder.transferToRepaymentStatus();

            if (repaymentStatus != RepaymentStatus.TO_VERIFY && repaymentStatus != RepaymentStatus.VERIFICATION_IN_PROCESS) {
                break;
            }
            Thread.sleep(5000l);
        }

        if (RepaymentStatus.VERIFICATION_SUCCESS != repaymentStatus) {
            throw new Exception("还款订单状态不正确");
        }

        String payResult = refactorMethod.payOrder(orderRequestNo, orderRequestNo, financialContractNo, payWay, repaymentAccountNo,
                paymentAccountName, paymentGateWay
                , tradeUuid, orderAmount, transactionTime);

        boolean outcome = payResult.contains("还款订单不存在");
        Assert.assertEquals(true,outcome);

    }


    /**
     * 支付订单参数校验，还款订单不在该信托合同financialContractNo下
     * @throws Exception
     */
    @Test
    public void checkPaymentParams5() throws Exception {
        String orderRequestNo = uniqueId;
        String transType = "0";
        String orderAmount = "10080";
        Long firstRepaymentWay = 1000l;
        String firstDetailTotalAmount = "10080";
        String firstDetailPrincipal = "10000";
        String firstDetailInterest = "20";
        String payWay = "0";
        Long firstFeeType = 1l;
        String repayScheduleNo = "";

        String financialContractNo = "G31700";
        String paymentAccountName = "秦操";
        String paymentGateWay = "0";
        String tradeUuid = UUID.randomUUID().toString();
        SimpleDateFormat sim = new SimpleDateFormat("YYYY-MM-DD HH:mm:ss");
        String transactionTime = sim.format(new Date());

        String result = refactorMethod.applyRepaymentOderForOneDetail(orderRequestNo, orderRequestNo, transType, productCode, orderAmount, uniqueId,
                firstRepaymentNumber, firstRepaymentWay,
                firstDetailTotalAmount, firstDetailPrincipal,
                firstDetailInterest, firstFeeType,repayScheduleNo);

        if (!result.contains("订单已受理")) {
            throw new Exception("还款订单校验错误!");
        }
        RepaymentStatus repaymentStatus;
        while (true) {
            RepaymentOrder repaymentOrder = repaymentOrderService.getRepaymentOrderByUniqueId(uniqueId, productCode);
            if (null == repaymentOrder) {
                throw new Exception("还款订单不存在!");
            }
            repaymentStatus = repaymentOrder.transferToRepaymentStatus();

            if (repaymentStatus != RepaymentStatus.TO_VERIFY && repaymentStatus != RepaymentStatus.VERIFICATION_IN_PROCESS) {
                break;
            }
            Thread.sleep(5000l);
        }

        if (RepaymentStatus.VERIFICATION_SUCCESS != repaymentStatus) {
            throw new Exception("还款订单状态不正确");
        }

        String payResult = refactorMethod.payOrder(orderRequestNo, orderRequestNo, financialContractNo, payWay, repaymentAccountNo,
                paymentAccountName, paymentGateWay
                , tradeUuid, orderAmount, transactionTime);

        boolean outcome = payResult.contains("还款订单不存在");
        Assert.assertEquals(true,outcome);
    }

    /**
     * 支付订单参数校验，orderUniqueId不存在
     * @throws Exception
     */
    @Test
    public void checkPaymentParams6() throws Exception {
        String orderRequestNo = uniqueId;
        String transType = "0";
        String orderAmount = "10080";
        Long firstRepaymentWay = 1000l;
        String firstDetailTotalAmount = "10080";
        String firstDetailPrincipal = "10000";
        String firstDetailInterest = "20";
        String payWay = "0";
        Long firstFeeType = 1l;
        String repayScheduleNo = "";

        String orderUniqueId = orderRequestNo + 1;
        String paymentAccountName = "秦操";
        String paymentGateWay = "0";
        String tradeUuid = UUID.randomUUID().toString();
        SimpleDateFormat sim = new SimpleDateFormat("YYYY-MM-DD HH:mm:ss");
        String transactionTime = sim.format(new Date());

        String result = refactorMethod.applyRepaymentOderForOneDetail(orderRequestNo, orderRequestNo, transType, productCode, orderAmount, uniqueId,
                firstRepaymentNumber, firstRepaymentWay,
                firstDetailTotalAmount, firstDetailPrincipal,
                firstDetailInterest, firstFeeType,repayScheduleNo);

        if (!result.contains("订单已受理")) {
            throw new Exception("还款订单校验错误!");
        }
        RepaymentStatus repaymentStatus;
        while (true) {
            RepaymentOrder repaymentOrder = repaymentOrderService.getRepaymentOrderByUniqueId(uniqueId, productCode);
            if (null == repaymentOrder) {
                throw new Exception("还款订单不存在!");
            }
            repaymentStatus = repaymentOrder.transferToRepaymentStatus();

            if (repaymentStatus != RepaymentStatus.TO_VERIFY && repaymentStatus != RepaymentStatus.VERIFICATION_IN_PROCESS) {
                break;
            }
            Thread.sleep(5000l);
        }

        if (RepaymentStatus.VERIFICATION_SUCCESS != repaymentStatus) {
            throw new Exception("还款订单状态不正确");
        }

        String payResult = refactorMethod.payOrder(orderRequestNo, orderUniqueId, productCode, payWay, repaymentAccountNo,
                paymentAccountName, paymentGateWay
                , tradeUuid, orderAmount, transactionTime);

        boolean outcome = payResult.contains("还款订单不存在");
        Assert.assertEquals(true,outcome);
    }

    /**
     * 支付订单参数校验，支付方式payWay为空
     * @throws Exception
     */
    @Test
    public void checkPaymentParams7() throws Exception {
        String orderRequestNo = uniqueId;
        String transType = "0";
        String orderAmount = "10080";
        Long firstRepaymentWay = 1000l;
        String firstDetailTotalAmount = "10080";
        String firstDetailPrincipal = "10000";
        String firstDetailInterest = "20";
        String payWay = "";
        Long firstFeeType = 1l;
        String repayScheduleNo = "";

        String paymentAccountName = "秦操";
        String paymentGateWay = "0";
        String tradeUuid = UUID.randomUUID().toString();
        SimpleDateFormat sim = new SimpleDateFormat("YYYY-MM-DD HH:mm:ss");
        String transactionTime = sim.format(new Date());

        String result = refactorMethod.applyRepaymentOderForOneDetail(orderRequestNo, orderRequestNo, transType, productCode, orderAmount, uniqueId,
                firstRepaymentNumber, firstRepaymentWay,
                firstDetailTotalAmount, firstDetailPrincipal,
                firstDetailInterest, firstFeeType,repayScheduleNo);

        if (!result.contains("订单已受理")) {
            throw new Exception("还款订单校验错误!");
        }
        RepaymentStatus repaymentStatus;
        while (true) {
            RepaymentOrder repaymentOrder = repaymentOrderService.getRepaymentOrderByUniqueId(uniqueId, productCode);
            if (null == repaymentOrder) {
                throw new Exception("还款订单不存在!");
            }
            repaymentStatus = repaymentOrder.transferToRepaymentStatus();

            if (repaymentStatus != RepaymentStatus.TO_VERIFY && repaymentStatus != RepaymentStatus.VERIFICATION_IN_PROCESS) {
                break;
            }
            Thread.sleep(5000l);
        }

        if (RepaymentStatus.VERIFICATION_SUCCESS != repaymentStatus) {
            throw new Exception("还款订单状态不正确");
        }

        String payResult = refactorMethod.payOrder(orderRequestNo, orderRequestNo, productCode, payWay, repaymentAccountNo,
                paymentAccountName, paymentGateWay
                , tradeUuid, orderAmount, transactionTime);

        boolean outcome = payResult.contains("支付方式[payWay],不能为空");
        Assert.assertEquals(true,outcome);
    }

    /**
     * 支付订单参数校验，支付方式payWay不存在
     * @throws Exception
     */
    @Test
    public void checkPaymentParams8() throws Exception {
        String orderRequestNo = uniqueId;
        String transType = "0";
        String orderAmount = "10080";
        Long firstRepaymentWay = 1000l;
        String firstDetailTotalAmount = "10080";
        String firstDetailPrincipal = "10000";
        String firstDetailInterest = "20";
        String payWay = "s";
        Long firstFeeType = 1l;
        String repayScheduleNo = "";

        String paymentAccountName = "秦操";
        String paymentGateWay = "0";
        String tradeUuid = UUID.randomUUID().toString();
        SimpleDateFormat sim = new SimpleDateFormat("YYYY-MM-DD HH:mm:ss");
        String transactionTime = sim.format(new Date());

        String result = refactorMethod.applyRepaymentOderForOneDetail(orderRequestNo, orderRequestNo, transType, productCode, orderAmount, uniqueId,
                firstRepaymentNumber, firstRepaymentWay,
                firstDetailTotalAmount, firstDetailPrincipal,
                firstDetailInterest, firstFeeType,repayScheduleNo);

        if (!result.contains("订单已受理")) {
            throw new Exception("还款订单校验错误!");
        }
        RepaymentStatus repaymentStatus;
        while (true) {
            RepaymentOrder repaymentOrder = repaymentOrderService.getRepaymentOrderByUniqueId(uniqueId, productCode);
            if (null == repaymentOrder) {
                throw new Exception("还款订单不存在!");
            }
            repaymentStatus = repaymentOrder.transferToRepaymentStatus();

            if (repaymentStatus != RepaymentStatus.TO_VERIFY && repaymentStatus != RepaymentStatus.VERIFICATION_IN_PROCESS) {
                break;
            }
            Thread.sleep(5000l);
        }

        if (RepaymentStatus.VERIFICATION_SUCCESS != repaymentStatus) {
            throw new Exception("还款订单状态不正确");
        }

        String payResult = refactorMethod.payOrder(orderRequestNo, orderRequestNo, productCode, payWay, repaymentAccountNo,
                paymentAccountName, paymentGateWay
                , tradeUuid, orderAmount, transactionTime);

        boolean outcome = payResult.contains("未开放该支付方式");
        Assert.assertEquals(true,outcome);
    }


    /**
     * 支付订单参数校验，支付方式payWay与还款方式repaymentWay不匹配
     * @throws Exception
     */
    @Test
    public void checkPaymentParams9() throws Exception {
        String orderRequestNo = uniqueId;
        String transType = "0";
        String orderAmount = "10080";
        Long firstRepaymentWay = 1000l;
        String firstDetailTotalAmount = "10080";
        String firstDetailPrincipal = "10000";
        String firstDetailInterest = "20";
        String payWay = "0";
        Long firstFeeType = 1l;
        String repayScheduleNo = "";

        String paymentAccountName = "秦操";
        String paymentGateWay = "0";
        String tradeUuid = UUID.randomUUID().toString();
        SimpleDateFormat sim = new SimpleDateFormat("YYYY-MM-DD HH:mm:ss");
        String transactionTime = sim.format(new Date());

        String result = refactorMethod.applyRepaymentOderForOneDetail(orderRequestNo, orderRequestNo, transType, productCode, orderAmount, uniqueId,
                firstRepaymentNumber, firstRepaymentWay,
                firstDetailTotalAmount, firstDetailPrincipal,
                firstDetailInterest, firstFeeType,repayScheduleNo);

        if (!result.contains("订单已受理")) {
            throw new Exception("还款订单校验错误!");
        }
        RepaymentStatus repaymentStatus;
        while (true) {
            RepaymentOrder repaymentOrder = repaymentOrderService.getRepaymentOrderByUniqueId(uniqueId, productCode);
            if (null == repaymentOrder) {
                throw new Exception("还款订单不存在!");
            }
            repaymentStatus = repaymentOrder.transferToRepaymentStatus();

            if (repaymentStatus != RepaymentStatus.TO_VERIFY && repaymentStatus != RepaymentStatus.VERIFICATION_IN_PROCESS) {
                break;
            }
            Thread.sleep(5000l);
        }

        if (RepaymentStatus.VERIFICATION_SUCCESS != repaymentStatus) {
            throw new Exception("还款订单状态不正确");
        }

        String payResult = refactorMethod.payOrder(orderRequestNo, orderRequestNo, productCode, payWay, repaymentAccountNo,
                paymentAccountName, paymentGateWay
                , tradeUuid, orderAmount, transactionTime);

        boolean outcome = payResult.contains("支付方式与还款订单还款方式不匹配");
        Assert.assertEquals(true,outcome);
    }

    /**
     * 支付订单参数校验，支付通道paymentGateWay不存在
     * @throws Exception
     */
    @Test
    public void checkPaymentParams10() throws Exception {
        String orderRequestNo = uniqueId;
        String transType = "0";
        String orderAmount = "10080";
        Long firstRepaymentWay = 1000l;
        String firstDetailTotalAmount = "10080";
        String firstDetailPrincipal = "10000";
        String firstDetailInterest = "20";
        String payWay = "2";
        Long firstFeeType = 1l;
        String repayScheduleNo = "";

        String paymentAccountName = "秦操";
        String paymentGateWay = "5";
        String tradeUuid = UUID.randomUUID().toString();
        SimpleDateFormat sim = new SimpleDateFormat("YYYY-MM-DD HH:mm:ss");
        String transactionTime = sim.format(new Date());

        String result = refactorMethod.applyRepaymentOderForOneDetail(orderRequestNo, orderRequestNo, transType, productCode, orderAmount, uniqueId,
                firstRepaymentNumber, firstRepaymentWay,
                firstDetailTotalAmount, firstDetailPrincipal,
                firstDetailInterest, firstFeeType,repayScheduleNo);

        if (!result.contains("订单已受理")) {
            throw new Exception("还款订单校验错误!");
        }
        RepaymentStatus repaymentStatus;
        while (true) {
            RepaymentOrder repaymentOrder = repaymentOrderService.getRepaymentOrderByUniqueId(uniqueId, productCode);
            if (null == repaymentOrder) {
                throw new Exception("还款订单不存在!");
            }
            repaymentStatus = repaymentOrder.transferToRepaymentStatus();

            if (repaymentStatus != RepaymentStatus.TO_VERIFY && repaymentStatus != RepaymentStatus.VERIFICATION_IN_PROCESS) {
                break;
            }
            Thread.sleep(5000l);
        }

        if (RepaymentStatus.VERIFICATION_SUCCESS != repaymentStatus) {
            throw new Exception("还款订单状态不正确");
        }

        String payResult = refactorMethod.payOrder(orderRequestNo, orderRequestNo, productCode, payWay, repaymentAccountNo,
                paymentAccountName, paymentGateWay
                , tradeUuid, orderAmount, transactionTime);

        boolean outcome = payResult.contains("该支付通道不支持");
        Assert.assertEquals(true,outcome);
    }


    /**
     * 支付订单参数校验，支付通道paymentGateWay未配置
     * @throws Exception
     */
    @Test
    public void checkPaymentParams11() throws Exception {
        String orderRequestNo = uniqueId;
        String transType = "0";
        String orderAmount = "10080";
        Long firstRepaymentWay = 1000l;
        String firstDetailTotalAmount = "10080";
        String firstDetailPrincipal = "10000";
        String firstDetailInterest = "20";
        String payWay = "2";
        Long firstFeeType = 1l;
        String repayScheduleNo = "";

        String paymentAccountName = "秦操";
        String paymentGateWay = "1";
        String tradeUuid = UUID.randomUUID().toString();
        SimpleDateFormat sim = new SimpleDateFormat("YYYY-MM-DD HH:mm:ss");
        String transactionTime = sim.format(new Date());

        String result = refactorMethod.applyRepaymentOderForOneDetail(orderRequestNo, orderRequestNo, transType, productCode, orderAmount, uniqueId,
                firstRepaymentNumber, firstRepaymentWay,
                firstDetailTotalAmount, firstDetailPrincipal,
                firstDetailInterest, firstFeeType,repayScheduleNo);

        if (!result.contains("订单已受理")) {
            throw new Exception("还款订单校验错误!");
        }
        RepaymentStatus repaymentStatus;
        while (true) {
            RepaymentOrder repaymentOrder = repaymentOrderService.getRepaymentOrderByUniqueId(uniqueId, productCode);
            if (null == repaymentOrder) {
                throw new Exception("还款订单不存在!");
            }
            repaymentStatus = repaymentOrder.transferToRepaymentStatus();

            if (repaymentStatus != RepaymentStatus.TO_VERIFY && repaymentStatus != RepaymentStatus.VERIFICATION_IN_PROCESS) {
                break;
            }
            Thread.sleep(5000l);
        }

        if (RepaymentStatus.VERIFICATION_SUCCESS != repaymentStatus) {
            throw new Exception("还款订单状态不正确");
        }

        String payResult = refactorMethod.payOrder(orderRequestNo, orderRequestNo, productCode, payWay, repaymentAccountNo,
                paymentAccountName, paymentGateWay
                , tradeUuid, orderAmount, transactionTime);

        boolean outcome = payResult.contains("系统没有配置接口传的支付通道");
        Assert.assertEquals(true,outcome);
    }


    /**
     * 支付订单参数校验，线下转账-付款流水号tradeUuid为空
     * @throws Exception
     */
    @Test
    public void checkPaymentParams12() throws Exception {
        String orderRequestNo = uniqueId;
        String transType = "0";
        String orderAmount = "10080";
        Long firstRepaymentWay = 2001l;
        String firstDetailTotalAmount = "10080";
        String firstDetailPrincipal = "10000";
        String firstDetailInterest = "20";
        String payWay = "0";
        Long firstFeeType = 1l;
        String repayScheduleNo = "";

        String paymentAccountName = "秦操";
        String paymentGateWay = "0";
        String tradeUuid = "";
        SimpleDateFormat sim = new SimpleDateFormat("YYYY-MM-DD HH:mm:ss");
        String transactionTime = sim.format(new Date());

        String result = refactorMethod.applyRepaymentOderForOneDetail(orderRequestNo, orderRequestNo, transType, productCode, orderAmount, uniqueId,
                firstRepaymentNumber, firstRepaymentWay,
                firstDetailTotalAmount, firstDetailPrincipal,
                firstDetailInterest, firstFeeType,repayScheduleNo);

        if (!result.contains("订单已受理")) {
            throw new Exception("还款订单校验错误!");
        }
        RepaymentStatus repaymentStatus;
        while (true) {
            RepaymentOrder repaymentOrder = repaymentOrderService.getRepaymentOrderByUniqueId(uniqueId, productCode);
            if (null == repaymentOrder) {
                throw new Exception("还款订单不存在!");
            }
            repaymentStatus = repaymentOrder.transferToRepaymentStatus();

            if (repaymentStatus != RepaymentStatus.TO_VERIFY && repaymentStatus != RepaymentStatus.VERIFICATION_IN_PROCESS) {
                break;
            }
            Thread.sleep(5000l);
        }

        if (RepaymentStatus.VERIFICATION_SUCCESS != repaymentStatus) {
            throw new Exception("还款订单状态不正确");
        }

        String payResult = refactorMethod.payOrder(orderRequestNo, orderRequestNo, productCode, payWay, repaymentAccountNo,
                paymentAccountName, paymentGateWay
                , tradeUuid, orderAmount, transactionTime);

        boolean outcome = payResult.contains("付款流水号[tradeUuid],不能为空");
        Assert.assertEquals(true,outcome);
    }


    /**
     * 支付订单参数校验，线下转账-付款流水号tradeUuid重复
     * @throws Exception
     */
    @Test
    public void checkPaymentParams13() throws Exception {
        String orderRequestNo = uniqueId;
        String transType = "0";
        String orderAmount = "10080";
        Long firstRepaymentWay = 2001l;
        String firstDetailTotalAmount = "10080";
        String firstDetailPrincipal = "10000";
        String firstDetailInterest = "20";
        String payWay = "0";
        Long firstFeeType = 1l;
        String repayScheduleNo = "";

        String paymentAmount = "5040";
        String paymentAccountName = "秦操";
        String paymentGateWay = "0";
        String tradeUuid = UUID.randomUUID().toString();
        SimpleDateFormat sim = new SimpleDateFormat("YYYY-MM-DD HH:mm:ss");
        String transactionTime = sim.format(new Date());

        String result = refactorMethod.applyRepaymentOderForOneDetail(orderRequestNo, orderRequestNo, transType, productCode, orderAmount, uniqueId,
                firstRepaymentNumber, firstRepaymentWay,
                firstDetailTotalAmount, firstDetailPrincipal,
                firstDetailInterest, firstFeeType,repayScheduleNo);

        cashFlowService.insertCashFlow(uniqueId,productCode,repaymentAccountNo,paymentAccountName,new BigDecimal(paymentAmount));

        if (!result.contains("订单已受理")) {
            throw new Exception("还款订单校验错误!");
        }
        RepaymentStatus repaymentStatus;
        while (true) {
            RepaymentOrder repaymentOrder = repaymentOrderService.getRepaymentOrderByUniqueId(uniqueId, productCode);
            if (null == repaymentOrder) {
                throw new Exception("还款订单不存在!");
            }
            repaymentStatus = repaymentOrder.transferToRepaymentStatus();

            if (repaymentStatus != RepaymentStatus.TO_VERIFY && repaymentStatus != RepaymentStatus.VERIFICATION_IN_PROCESS) {
                break;
            }
            Thread.sleep(5000l);
        }

        if (RepaymentStatus.VERIFICATION_SUCCESS != repaymentStatus) {
            throw new Exception("还款订单状态不正确");
        }

        String payResult = refactorMethod.payOrder(orderRequestNo, orderRequestNo, productCode, payWay, repaymentAccountNo,
                paymentAccountName, paymentGateWay
                , tradeUuid, paymentAmount, transactionTime);
        if (!payResult.contains("订单支付已受理")) {
            throw new Exception("订单支付失败");
        }
        Thread.sleep(5000l);
        String payResult2 = refactorMethod.payOrder(orderRequestNo+1, orderRequestNo, productCode, payWay, repaymentAccountNo,
                paymentAccountName, paymentGateWay
                , tradeUuid, paymentAmount, transactionTime);

        boolean outcome = payResult2.contains("通道交易号重复");
        Assert.assertEquals(true,outcome);
    }

    /**
     * 支付订单参数校验，商户代扣-通道交易号tradeUuid为空
     * @throws Exception
     */
    @Test
    public void checkPaymentParams14() throws Exception {
        String orderRequestNo = uniqueId;
        String transType = "0";
        String orderAmount = "10080";
        Long firstRepaymentWay = 4001l;
        String firstDetailTotalAmount = "10080";
        String firstDetailPrincipal = "10000";
        String firstDetailInterest = "20";
        String payWay = "3";
        Long firstFeeType = 1l;
        String repayScheduleNo = "";

        String paymentAccountName = "秦操";
        String paymentGateWay = "0";
        String tradeUuid = "";
        SimpleDateFormat sim = new SimpleDateFormat("YYYY-MM-DD HH:mm:ss");
        String transactionTime = sim.format(new Date());

        String result = refactorMethod.applyRepaymentOderForOneDetail(orderRequestNo, orderRequestNo, transType, productCode, orderAmount, uniqueId,
                firstRepaymentNumber, firstRepaymentWay,
                firstDetailTotalAmount, firstDetailPrincipal,
                firstDetailInterest, firstFeeType,repayScheduleNo);

        if (!result.contains("订单已受理")) {
            throw new Exception("还款订单校验错误!");
        }
        RepaymentStatus repaymentStatus;
        while (true) {
            RepaymentOrder repaymentOrder = repaymentOrderService.getRepaymentOrderByUniqueId(uniqueId, productCode);
            if (null == repaymentOrder) {
                throw new Exception("还款订单不存在!");
            }
            repaymentStatus = repaymentOrder.transferToRepaymentStatus();

            if (repaymentStatus != RepaymentStatus.TO_VERIFY && repaymentStatus != RepaymentStatus.VERIFICATION_IN_PROCESS) {
                break;
            }
            Thread.sleep(5000l);
        }

        if (RepaymentStatus.VERIFICATION_SUCCESS != repaymentStatus) {
            throw new Exception("还款订单状态不正确");
        }

        String payResult = refactorMethod.payOrder(orderRequestNo, orderRequestNo, productCode, payWay, repaymentAccountNo,
                paymentAccountName, paymentGateWay
                , tradeUuid, orderAmount, transactionTime);

        boolean outcome = payResult.contains("通道交易号[tradeUuid],不能为空");
        Assert.assertEquals(true,outcome);
    }

    /**
     * 支付订单参数校验，商户代扣-交易时间transactionTime为空
     * @throws Exception
     */
    @Test
    public void checkPaymentParams15() throws Exception {
        String orderRequestNo = uniqueId;
        String transType = "0";
        String orderAmount = "10080";
        Long firstRepaymentWay = 4001l;
        String firstDetailTotalAmount = "10080";
        String firstDetailPrincipal = "10000";
        String firstDetailInterest = "20";
        String payWay = "3";
        Long firstFeeType = 1l;
        String repayScheduleNo = "";

        String paymentAccountName = "秦操";
        String paymentGateWay = "0";
        String tradeUuid = UUID.randomUUID().toString();
        String transactionTime = "";

        String result = refactorMethod.applyRepaymentOderForOneDetail(orderRequestNo, orderRequestNo, transType, productCode, orderAmount, uniqueId,
                firstRepaymentNumber, firstRepaymentWay,
                firstDetailTotalAmount, firstDetailPrincipal,
                firstDetailInterest, firstFeeType,repayScheduleNo);

        if (!result.contains("订单已受理")) {
            throw new Exception("还款订单校验错误!");
        }
        RepaymentStatus repaymentStatus;
        while (true) {
            RepaymentOrder repaymentOrder = repaymentOrderService.getRepaymentOrderByUniqueId(uniqueId, productCode);
            if (null == repaymentOrder) {
                throw new Exception("还款订单不存在!");
            }
            repaymentStatus = repaymentOrder.transferToRepaymentStatus();

            if (repaymentStatus != RepaymentStatus.TO_VERIFY && repaymentStatus != RepaymentStatus.VERIFICATION_IN_PROCESS) {
                break;
            }
            Thread.sleep(5000l);
        }

        if (RepaymentStatus.VERIFICATION_SUCCESS != repaymentStatus) {
            throw new Exception("还款订单状态不正确");
        }

        String payResult = refactorMethod.payOrder(orderRequestNo, orderRequestNo, productCode, payWay, repaymentAccountNo,
                paymentAccountName, paymentGateWay
                , tradeUuid, orderAmount, transactionTime);

        boolean outcome = payResult.contains("交易时间[transactionTime],不能为空");
        Assert.assertEquals(true,outcome);
    }

    /**
     * 支付订单参数校验，支付非校验成功的订单
     * @throws Exception
     */
    @Test
    public void checkPaymentParams16() throws Exception {
        String orderRequestNo = uniqueId;
        String transType = "0";
        String orderAmount = "10120";
        Long firstRepaymentWay = 1000l;
        String firstDetailTotalAmount = "10120";
        String firstDetailPrincipal = "10000";
        String firstDetailInterest = "30";
        String payWay = "2";
        Long firstFeeType = 1l;
        String repayScheduleNo = "";

        String paymentAccountName = "秦操";
        String paymentGateWay = "0";
        String tradeUuid = UUID.randomUUID().toString();
        SimpleDateFormat sim = new SimpleDateFormat("YYYY-MM-DD HH:mm:ss");
        String transactionTime = sim.format(new Date());

        String result = refactorMethod.applyRepaymentOderForOneDetail(orderRequestNo, orderRequestNo, transType, productCode, orderAmount, uniqueId,
                firstRepaymentNumber, firstRepaymentWay,
                firstDetailTotalAmount, firstDetailPrincipal,
                firstDetailInterest, firstFeeType,repayScheduleNo);

        if (!result.contains("订单已受理")) {
            throw new Exception("还款订单校验错误!");
        }
        RepaymentStatus repaymentStatus;
        while (true) {
            RepaymentOrder repaymentOrder = repaymentOrderService.getRepaymentOrderByUniqueId(uniqueId, productCode);
            if (null == repaymentOrder) {
                throw new Exception("还款订单不存在!");
            }
            repaymentStatus = repaymentOrder.transferToRepaymentStatus();

            if (repaymentStatus != RepaymentStatus.TO_VERIFY && repaymentStatus != RepaymentStatus.VERIFICATION_IN_PROCESS) {
                break;
            }
            Thread.sleep(5000l);
        }

        if (RepaymentStatus.VERIFICATION_FAILURE != repaymentStatus) {
            throw new Exception("还款订单状态不正确");
        }

        String payResult = refactorMethod.payOrder(orderRequestNo, orderRequestNo, productCode, payWay, repaymentAccountNo,
                paymentAccountName, paymentGateWay
                , tradeUuid, orderAmount, transactionTime);

        boolean outcome = payResult.contains("状态不正确");
        Assert.assertEquals(true,outcome);
    }


    /**
     * 支付订单参数校验，线下转账-分两次支付
     * @throws Exception
     */
    @Test
    public void checkPaymentParams17() throws Exception {
        String orderRequestNo = uniqueId;
        String transType = "0";
        String orderAmount = "10080";
        Long firstRepaymentWay = 2001l;
        String firstDetailTotalAmount = "10080";
        String firstDetailPrincipal = "10000";
        String firstDetailInterest = "20";
        String payWay = "0";
        Long firstFeeType = 1l;
        String repayScheduleNo = "";

        String paymentAmount = "5040";
        String paymentAccountName = "秦操";
        String paymentGateWay = "0";
        String tradeUuid = UUID.randomUUID().toString();
        SimpleDateFormat sim = new SimpleDateFormat("YYYY-MM-DD HH:mm:ss");
        String transactionTime = sim.format(new Date());

        String result = refactorMethod.applyRepaymentOderForOneDetail(orderRequestNo, orderRequestNo, transType, productCode, orderAmount, uniqueId,
                firstRepaymentNumber, firstRepaymentWay,
                firstDetailTotalAmount, firstDetailPrincipal,
                firstDetailInterest, firstFeeType,repayScheduleNo);

        cashFlowService.insertCashFlow(uniqueId,productCode,repaymentAccountNo,paymentAccountName,new BigDecimal(paymentAmount));

        if (!result.contains("订单已受理")) {
            throw new Exception("还款订单校验错误!");
        }
        RepaymentStatus repaymentStatus;
        while (true) {
            RepaymentOrder repaymentOrder = repaymentOrderService.getRepaymentOrderByUniqueId(uniqueId, productCode);
            if (null == repaymentOrder) {
                throw new Exception("还款订单不存在!");
            }
            repaymentStatus = repaymentOrder.transferToRepaymentStatus();

            if (repaymentStatus != RepaymentStatus.TO_VERIFY && repaymentStatus != RepaymentStatus.VERIFICATION_IN_PROCESS) {
                break;
            }
            Thread.sleep(5000l);
        }

        if (RepaymentStatus.VERIFICATION_SUCCESS != repaymentStatus) {
            throw new Exception("还款订单状态不正确");
        }

        String payResult = refactorMethod.payOrder(orderRequestNo, orderRequestNo, productCode, payWay, repaymentAccountNo,
                paymentAccountName, paymentGateWay
                , tradeUuid, paymentAmount, transactionTime);
        if (!payResult.contains("订单支付已受理")) {
            throw new Exception("订单支付失败");
        }

        Thread.sleep(20000);
        cashFlowService.insertCashFlow(uniqueId,productCode,repaymentAccountNo,paymentAccountName,new BigDecimal(paymentAmount));
        String payResult2 = refactorMethod.payOrder(orderRequestNo+1, orderRequestNo, productCode, payWay, repaymentAccountNo,
                paymentAccountName, paymentGateWay
                , tradeUuid+1, paymentAmount, transactionTime);
        if (!payResult2.contains("订单支付已受理")) {
            throw new Exception("订单支付失败");
        }

        while (true) {
            RepaymentOrder repaymentOrder = repaymentOrderService.getRepaymentOrderByUniqueId(uniqueId, productCode);
            repaymentStatus = repaymentOrder.transferToRepaymentStatus();

            if (repaymentStatus != RepaymentStatus.PAYMENT_IN_PROCESS) {
                break;
            }
            Thread.sleep(5000l);
        }

        if(RepaymentStatus.PAYMENT_END != repaymentStatus){
            throw new Exception("支付状态不正确");
        }

        while (true) {
            RepaymentOrder repaymentOrder = repaymentOrderService.getRepaymentOrderByUniqueId(uniqueId, productCode);
            repaymentStatus = repaymentOrder.transferToRepaymentStatus();

            if (repaymentStatus != RepaymentStatus.PAYMENT_END) {
                break;
            }
            Thread.sleep(5000l);
        }
        Assert.assertEquals(RepaymentStatus.RECOVER_END,repaymentStatus);
    }


    /**
     * 支付订单参数校验，线下转账-交易金额大于还款订单剩余未付金额
     * @throws Exception
     */
    @Test
    public void checkPaymentParams19() throws Exception {
        String orderRequestNo = uniqueId;
        String transType = "0";
        String orderAmount = "10080";
        Long firstRepaymentWay = 2001l;
        String firstDetailTotalAmount = "10080";
        String firstDetailPrincipal = "10000";
        String firstDetailInterest = "20";
        String payWay = "0";
        Long firstFeeType = 1l;
        String repayScheduleNo = "";

        String paymentAmount = "5040";
        String paymentAmount2 = "5050";
        String paymentAccountName = "秦操";
        String paymentGateWay = "0";
        String tradeUuid = UUID.randomUUID().toString();
        SimpleDateFormat sim = new SimpleDateFormat("YYYY-MM-DD HH:mm:ss");
        String transactionTime = sim.format(new Date());

        String result = refactorMethod.applyRepaymentOderForOneDetail(orderRequestNo, orderRequestNo, transType, productCode, orderAmount, uniqueId,
                firstRepaymentNumber, firstRepaymentWay,
                firstDetailTotalAmount, firstDetailPrincipal,
                firstDetailInterest, firstFeeType,repayScheduleNo);

        cashFlowService.insertCashFlow(uniqueId,productCode,repaymentAccountNo,paymentAccountName,new BigDecimal(paymentAmount));

        if (!result.contains("订单已受理")) {
            throw new Exception("还款订单校验错误!");
        }
        RepaymentStatus repaymentStatus;
        while (true) {
            RepaymentOrder repaymentOrder = repaymentOrderService.getRepaymentOrderByUniqueId(uniqueId, productCode);
            if (null == repaymentOrder) {
                throw new Exception("还款订单不存在!");
            }
            repaymentStatus = repaymentOrder.transferToRepaymentStatus();

            if (repaymentStatus != RepaymentStatus.TO_VERIFY && repaymentStatus != RepaymentStatus.VERIFICATION_IN_PROCESS) {
                break;
            }
            Thread.sleep(5000l);
        }

        if (RepaymentStatus.VERIFICATION_SUCCESS != repaymentStatus) {
            throw new Exception("还款订单状态不正确");
        }

        String payResult = refactorMethod.payOrder(orderRequestNo, orderRequestNo, productCode, payWay, repaymentAccountNo,
                paymentAccountName, paymentGateWay
                , tradeUuid, paymentAmount, transactionTime);
        if (!payResult.contains("订单支付已受理")) {
            throw new Exception("订单支付失败");
        }

        String payResult2 = refactorMethod.payOrder(orderRequestNo+1, orderRequestNo, productCode, payWay, repaymentAccountNo,
                paymentAccountName, paymentGateWay
                , tradeUuid+1, paymentAmount2, transactionTime);

        boolean outcome = payResult2.contains("交易金额不能大于还款订单剩余未付金额");

        Assert.assertEquals(true,outcome);
    }

    /**
     * 支付订单参数校验，线上代扣-付款方账户号paymentAccountNo为空
     * @throws Exception
     */
    @Test
    public void checkPaymentParams20() throws Exception {
        String orderRequestNo = uniqueId;
        String transType = "0";
        String orderAmount = "10080";
        Long firstRepaymentWay = 1000l;
        String firstDetailTotalAmount = "10080";
        String firstDetailPrincipal = "10000";
        String firstDetailInterest = "20";
        String payWay = "2";
        Long firstFeeType = 1l;
        String repayScheduleNo = "";

        String paymentAccountName = "秦操";
        String accountNo = "";
        String paymentGateWay = "0";
        String tradeUuid = UUID.randomUUID().toString();
        SimpleDateFormat sim = new SimpleDateFormat("YYYY-MM-DD HH:mm:ss");
        String transactionTime = sim.format(new Date());
        String result = refactorMethod.applyRepaymentOderForOneDetail(orderRequestNo, orderRequestNo, transType, productCode, orderAmount, uniqueId,
                firstRepaymentNumber, firstRepaymentWay,
                firstDetailTotalAmount, firstDetailPrincipal,
                firstDetailInterest, firstFeeType,repayScheduleNo);

        if (!result.contains("订单已受理")) {
            throw new Exception("还款订单校验错误!");
        }
        RepaymentStatus repaymentStatus;
        while (true) {
            RepaymentOrder repaymentOrder = repaymentOrderService.getRepaymentOrderByUniqueId(uniqueId, productCode);
            if (null == repaymentOrder) {
                throw new Exception("还款订单不存在!");
            }
            repaymentStatus = repaymentOrder.transferToRepaymentStatus();

            if (repaymentStatus != RepaymentStatus.TO_VERIFY && repaymentStatus != RepaymentStatus.VERIFICATION_IN_PROCESS) {
                break;
            }
            Thread.sleep(5000l);
        }

        if (RepaymentStatus.VERIFICATION_SUCCESS != repaymentStatus) {
            throw new Exception("还款订单状态不正确");
        }

        String payResult = refactorMethod.payOrder(orderRequestNo, orderRequestNo, productCode, payWay, accountNo,
                paymentAccountName, paymentGateWay
                , tradeUuid, orderAmount, transactionTime);

        boolean outcome = payResult.contains("付款方账户号[paymentAccountNo],不能为空");
        Assert.assertEquals(true,outcome);
    }


    /**
     * 支付订单参数校验，线上代扣-付款方账户名称paymentAccountName为空
     * @throws Exception
     */
    @Test
    public void checkPaymentParams21() throws Exception {
        String orderRequestNo = uniqueId;
        String transType = "0";
        String orderAmount = "10080";
        Long firstRepaymentWay = 1000l;
        String firstDetailTotalAmount = "10080";
        String firstDetailPrincipal = "10000";
        String firstDetailInterest = "20";
        String payWay = "2";
        Long firstFeeType = 1l;
        String repayScheduleNo = "";

        String paymentAccountName = "";
        String paymentGateWay = "0";
        String tradeUuid = UUID.randomUUID().toString();
        SimpleDateFormat sim = new SimpleDateFormat("YYYY-MM-DD HH:mm:ss");
        String transactionTime = sim.format(new Date());
        String result = refactorMethod.applyRepaymentOderForOneDetail(orderRequestNo, orderRequestNo, transType, productCode, orderAmount, uniqueId,
                firstRepaymentNumber, firstRepaymentWay,
                firstDetailTotalAmount, firstDetailPrincipal,
                firstDetailInterest, firstFeeType,repayScheduleNo);

        if (!result.contains("订单已受理")) {
            throw new Exception("还款订单校验错误!");
        }
        RepaymentStatus repaymentStatus;
        while (true) {
            RepaymentOrder repaymentOrder = repaymentOrderService.getRepaymentOrderByUniqueId(uniqueId, productCode);
            if (null == repaymentOrder) {
                throw new Exception("还款订单不存在!");
            }
            repaymentStatus = repaymentOrder.transferToRepaymentStatus();

            if (repaymentStatus != RepaymentStatus.TO_VERIFY && repaymentStatus != RepaymentStatus.VERIFICATION_IN_PROCESS) {
                break;
            }
            Thread.sleep(5000l);
        }

        if (RepaymentStatus.VERIFICATION_SUCCESS != repaymentStatus) {
            throw new Exception("还款订单状态不正确");
        }

        String payResult = refactorMethod.payOrder(orderRequestNo, orderRequestNo, productCode, payWay, repaymentAccountNo,
                paymentAccountName, paymentGateWay
                , tradeUuid, orderAmount, transactionTime);

        boolean outcome = payResult.contains("付款方账户名称[paymentAccountName],不能为空");
        Assert.assertEquals(true,outcome);
    }

    /**
     * 支付订单参数校验，快捷支付-支付失败
     * @throws Exception
     */
    @Test
    public void checkPaymentParams22() throws Exception{
        String totalAmount = "30000";
        String uniqueId = UUID.randomUUID().toString();
        repaymentAccountNo = "6217857600016839337";
        String loanCustomerName = "秦操";
        String loanCustomerNo = "11111117";
        String firstPlanDate = DateUtils.format(DateUtils.addMonths(new Date(), -1));
        String secondPlanDate = DateUtils.format(new Date());
        String thirdPlanDate = DateUtils.format(DateUtils.addMonths(new Date(), 1));

        String orderRequestNo = uniqueId;
        String transType = "0";
        String orderAmount = "10080";
        Long firstRepaymentWay = 5001l;
        String firstDetailTotalAmount = "10080";
        String firstDetailPrincipal = "10000";
        String firstDetailInterest = "20";
        Long firstFeeType = 1l;
        String repayScheduleNo = "";

        refactorMethod.importAssetPackage(totalAmount,productCode,uniqueId,repaymentAccountNo,firstDetailInterest,loanCustomerNo,
                firstPlanDate,secondPlanDate,thirdPlanDate,loanCustomerName);

        String repaymentNumber = refactorMethod.queryRepaymentPlan(productCode,uniqueId,0);

        String result = refactorMethod.applyRepaymentOderForOneDetail(orderRequestNo,orderRequestNo,transType,productCode,orderAmount,uniqueId,
                repaymentNumber,firstRepaymentWay,
                firstDetailTotalAmount,firstDetailPrincipal,
                firstDetailInterest,firstFeeType,repayScheduleNo);

        if(!result.contains("订单已受理")){
            throw new Exception("还款订单校验错误!");
        }
        RepaymentStatus repaymentStatus;
        while (true) {
            RepaymentOrder repaymentOrder = repaymentOrderService.getRepaymentOrderByUniqueId(orderRequestNo, productCode);
            if (null == repaymentOrder) {
                throw new Exception("还款订单不存在!");
            }
            repaymentStatus = repaymentOrder.transferToRepaymentStatus();

            if (repaymentStatus != RepaymentStatus.TO_VERIFY && repaymentStatus != RepaymentStatus.VERIFICATION_IN_PROCESS) {
                break;
            }
            Thread.sleep(5000l);
        }

        if(RepaymentStatus.PAYMENT_IN_PROCESS != repaymentStatus){
            throw new Exception("还款订单状态不正确");
        }

        while (true) {
            RepaymentOrder repaymentOrder = repaymentOrderService.getRepaymentOrderByUniqueId(orderRequestNo, productCode);
            repaymentStatus = repaymentOrder.transferToRepaymentStatus();

            if (repaymentStatus !=RepaymentStatus.PAYMENT_IN_PROCESS) {
                break;
            }
            Thread.sleep(20000l);
        }

        Assert.assertEquals(RepaymentStatus.PAYMENT_FAIL,repaymentStatus);
    }


    /**
     * 支付订单参数校验，在线支付-支付失败
     * @throws Exception
     */
    @Test
    public void checkPaymentParams23() throws Exception{
        String orderRequestNo = uniqueId;
        String transType = "0";
        String orderAmount = "10080";
        Long firstRepaymentWay = 6001l;
        String firstDetailTotalAmount = "10080";
        String firstDetailPrincipal = "10000";
        String firstDetailInterest = "20";
        String payWay = "2";
        Long firstFeeType = 1l;
        String repayScheduleNo = "";

        repaymentAccountNo = "6217857600016839337";
        String paymentAccountName = "秦操";
        String paymentGateWay = "0";
        String tradeUuid = UUID.randomUUID().toString();
        SimpleDateFormat sim  = new SimpleDateFormat("YYYY-MM-DD HH:mm:ss");
        String transactionTime = sim.format(new Date());

        List<String> paymentNoList = new ArrayList<>();
        paymentNoList.add(orderRequestNo);


        String result = refactorMethod.applyRepaymentOderForOneDetail(orderRequestNo,orderRequestNo,transType,productCode,orderAmount,uniqueId,
                firstRepaymentNumber,firstRepaymentWay,
                firstDetailTotalAmount,firstDetailPrincipal,
                firstDetailInterest,firstFeeType,repayScheduleNo);

        if(!result.contains("订单已受理")){
            throw new Exception("还款订单校验错误!");
        }
        RepaymentStatus repaymentStatus;
        while (true) {
            RepaymentOrder repaymentOrder = repaymentOrderService.getRepaymentOrderByUniqueId(orderRequestNo, productCode);
            if (null == repaymentOrder) {
                throw new Exception("还款订单不存在!");
            }
            repaymentStatus = repaymentOrder.transferToRepaymentStatus();

            if (repaymentStatus != RepaymentStatus.TO_VERIFY && repaymentStatus != RepaymentStatus.VERIFICATION_IN_PROCESS) {
                break;
            }
            Thread.sleep(5000l);
        }

        if(RepaymentStatus.VERIFICATION_SUCCESS != repaymentStatus){
            throw new Exception("还款订单状态不正确");
        }

        String payResult = refactorMethod.payOrder(orderRequestNo,orderRequestNo,productCode,payWay,repaymentAccountNo,
                paymentAccountName,paymentGateWay
                ,tradeUuid,orderAmount,transactionTime);
        if(!payResult.contains("订单支付已受理")){
            throw new Exception("订单支付失败");
        }

        while (true) {
            RepaymentOrder repaymentOrder = repaymentOrderService.getRepaymentOrderByUniqueId(uniqueId, productCode);
            repaymentStatus = repaymentOrder.transferToRepaymentStatus();

            if (repaymentStatus != RepaymentStatus.PAYMENT_IN_PROCESS) {
                break;
            }
            Thread.sleep(5000l);
        }

        Assert.assertEquals(RepaymentStatus.PAYMENT_FAIL,repaymentStatus);
    }


    /**
     * 支付订单参数校验，线上代扣-支付异常
     * @throws Exception
     */
    @Test
    public void checkPaymentParams24() throws Exception{
        String orderRequestNo = uniqueId;
        String transType = "0";
        String orderAmount = "10080";
        Long firstRepaymentWay = 1000l;
        String firstDetailTotalAmount = "10080";
        String firstDetailPrincipal = "10000";
        String firstDetailInterest = "20";
        String payWay = "2";
        Long firstFeeType = 1l;
        String repayScheduleNo = "";

        repaymentAccountNo = "6217857600016839337";
        String paymentAccountName = "秦操";
        String paymentGateWay = "0";
        String tradeUuid = UUID.randomUUID().toString();
        SimpleDateFormat sim  = new SimpleDateFormat("YYYY-MM-DD HH:mm:ss");
        String transactionTime = sim.format(new Date());

        List<String> paymentNoList = new ArrayList<>();
        paymentNoList.add(orderRequestNo);


        String result = refactorMethod.applyRepaymentOderForOneDetail(orderRequestNo,orderRequestNo,transType,productCode,orderAmount,uniqueId,
                firstRepaymentNumber,firstRepaymentWay,
                firstDetailTotalAmount,firstDetailPrincipal,
                firstDetailInterest,firstFeeType,repayScheduleNo);

        if(!result.contains("订单已受理")){
            throw new Exception("还款订单校验错误!");
        }
        RepaymentStatus repaymentStatus;
        while (true) {
            RepaymentOrder repaymentOrder = repaymentOrderService.getRepaymentOrderByUniqueId(orderRequestNo, productCode);
            if (null == repaymentOrder) {
                throw new Exception("还款订单不存在!");
            }
            repaymentStatus = repaymentOrder.transferToRepaymentStatus();

            if (repaymentStatus != RepaymentStatus.TO_VERIFY && repaymentStatus != RepaymentStatus.VERIFICATION_IN_PROCESS) {
                break;
            }
            Thread.sleep(5000l);
        }

        if(RepaymentStatus.VERIFICATION_SUCCESS != repaymentStatus){
            throw new Exception("还款订单状态不正确");
        }

        String payResult = refactorMethod.payOrder(orderRequestNo,orderRequestNo,productCode,payWay,repaymentAccountNo,
                paymentAccountName,paymentGateWay
                ,tradeUuid,orderAmount,transactionTime);
        if(!payResult.contains("订单支付已受理")){
            throw new Exception("订单支付失败");
        }

        while (true) {
            RepaymentOrder repaymentOrder = repaymentOrderService.getRepaymentOrderByUniqueId(uniqueId, productCode);
            repaymentStatus = repaymentOrder.transferToRepaymentStatus();

            if (repaymentStatus != RepaymentStatus.PAYMENT_IN_PROCESS) {
                break;
            }
            Thread.sleep(5000l);
        }

        Assert.assertEquals(RepaymentStatus.PAYMENT_ABNORMAL,repaymentStatus);
    }


    /**
     * 支付订单参数校验，线上代扣-支付异常-再支付
     * @throws Exception
     */
    @Test
    public void checkPaymentParams25() throws Exception{
        String orderRequestNo = uniqueId;
        String transType = "0";
        String orderAmount = "10080";
        Long firstRepaymentWay = 1000l;
        String firstDetailTotalAmount = "10080";
        String firstDetailPrincipal = "10000";
        String firstDetailInterest = "20";
        String payWay = "2";
        Long firstFeeType = 1l;
        String repayScheduleNo = "";

        repaymentAccountNo = "6217857600016839337";
        String paymentAccountName = "秦操";
        String paymentGateWay = "0";
        String tradeUuid = UUID.randomUUID().toString();
        SimpleDateFormat sim  = new SimpleDateFormat("YYYY-MM-DD HH:mm:ss");
        String transactionTime = sim.format(new Date());

        List<String> paymentNoList = new ArrayList<>();
        paymentNoList.add(orderRequestNo);


        String result = refactorMethod.applyRepaymentOderForOneDetail(orderRequestNo,orderRequestNo,transType,productCode,orderAmount,uniqueId,
                firstRepaymentNumber,firstRepaymentWay,
                firstDetailTotalAmount,firstDetailPrincipal,
                firstDetailInterest,firstFeeType,repayScheduleNo);

        if(!result.contains("订单已受理")){
            throw new Exception("还款订单校验错误!");
        }
        RepaymentStatus repaymentStatus;
        while (true) {
            RepaymentOrder repaymentOrder = repaymentOrderService.getRepaymentOrderByUniqueId(orderRequestNo, productCode);
            if (null == repaymentOrder) {
                throw new Exception("还款订单不存在!");
            }
            repaymentStatus = repaymentOrder.transferToRepaymentStatus();

            if (repaymentStatus != RepaymentStatus.TO_VERIFY && repaymentStatus != RepaymentStatus.VERIFICATION_IN_PROCESS) {
                break;
            }
            Thread.sleep(5000l);
        }

        if(RepaymentStatus.VERIFICATION_SUCCESS != repaymentStatus){
            throw new Exception("还款订单状态不正确");
        }

        String payResult = refactorMethod.payOrder(orderRequestNo+1,orderRequestNo,productCode,payWay,repaymentAccountNo,
                paymentAccountName,paymentGateWay
                ,tradeUuid,orderAmount,transactionTime);
        if(!payResult.contains("订单支付已受理")){
            throw new Exception("订单支付失败");
        }

        while (true) {
            RepaymentOrder repaymentOrder = repaymentOrderService.getRepaymentOrderByUniqueId(uniqueId, productCode);
            repaymentStatus = repaymentOrder.transferToRepaymentStatus();

            if (repaymentStatus != RepaymentStatus.PAYMENT_IN_PROCESS) {
                break;
            }
            Thread.sleep(5000l);
        }

        if(RepaymentStatus.PAYMENT_ABNORMAL != repaymentStatus) {
            throw new Exception("支付状态不正确");
        }

        repaymentAccountNo = "6217857600016839330";
        String payResult2 = refactorMethod.payOrder(orderRequestNo,orderRequestNo,productCode,payWay,repaymentAccountNo,
                paymentAccountName,paymentGateWay
                ,tradeUuid,orderAmount,transactionTime);
        if(!payResult2.contains("订单支付已受理")){
            throw new Exception("订单支付失败");
        }

        while (true) {
            RepaymentOrder repaymentOrder = repaymentOrderService.getRepaymentOrderByUniqueId(uniqueId, productCode);
            repaymentStatus = repaymentOrder.transferToRepaymentStatus();

            if (repaymentStatus != RepaymentStatus.PAYMENT_IN_PROCESS && repaymentStatus != RepaymentStatus.PAYMENT_END) {
                break;
            }
            Thread.sleep(5000l);
        }

        Assert.assertEquals(RepaymentStatus.RECOVER_END,repaymentStatus);
    }


    /**
     * 支付订单参数校验，线下转账-无流水
     * @throws Exception
     */
    @Test
    public void checkPaymentParams26() throws Exception{
        String orderRequestNo = uniqueId;
        String transType = "0";
        String orderAmount = "10080";
        Long firstRepaymentWay = 2001l;
        String firstDetailTotalAmount = "10080";
        String firstDetailPrincipal = "10000";
        String firstDetailInterest = "20";
        String payWay = "0";
        Long firstFeeType = 1l;
        String repayScheduleNo = "";

        String paymentAccountName = "秦操";
        String paymentGateWay = "0";
        String tradeUuid = UUID.randomUUID().toString();
        SimpleDateFormat sim  = new SimpleDateFormat("YYYY-MM-DD HH:mm:ss");
        String transactionTime = sim.format(new Date());

        String result = refactorMethod.applyRepaymentOderForOneDetail(orderRequestNo,orderRequestNo,transType,productCode,orderAmount,uniqueId,
                firstRepaymentNumber,firstRepaymentWay,
                firstDetailTotalAmount,firstDetailPrincipal,
                firstDetailInterest,firstFeeType,repayScheduleNo);

        if(!result.contains("订单已受理")){
            throw new Exception("还款订单校验错误!");
        }
        RepaymentStatus repaymentStatus;
        while (true) {
            RepaymentOrder repaymentOrder = repaymentOrderService.getRepaymentOrderByUniqueId(uniqueId, productCode);
            if (null == repaymentOrder) {
                throw new Exception("还款订单不存在!");
            }
            repaymentStatus = repaymentOrder.transferToRepaymentStatus();

            if (repaymentStatus != RepaymentStatus.TO_VERIFY && repaymentStatus != RepaymentStatus.VERIFICATION_IN_PROCESS) {
                break;
            }
            Thread.sleep(5000l);
        }

        if(RepaymentStatus.VERIFICATION_SUCCESS != repaymentStatus){
            throw new Exception("还款订单状态不正确");
        }

        String payResult = refactorMethod.payOrder(orderRequestNo,orderRequestNo,productCode,payWay,repaymentAccountNo,
                paymentAccountName,paymentGateWay
                ,tradeUuid,orderAmount,transactionTime);
        if(!payResult.contains("订单支付已受理")){
            throw new Exception("订单支付失败");
        }

        RepaymentOrder repaymentOrder = repaymentOrderService.getRepaymentOrderByUniqueId(uniqueId, productCode);
        repaymentStatus = repaymentOrder.transferToRepaymentStatus();

        Assert.assertEquals(RepaymentStatus.PAYMENT_IN_PROCESS,repaymentStatus);
    }


    /**
     * 支付订单参数校验，线下转账-无对应流水
     * @throws Exception
     */
    @Test
    public void checkPaymentParams27() throws Exception{
        String orderRequestNo = uniqueId;
        String transType = "0";
        String orderAmount = "10080";
        Long firstRepaymentWay = 2001l;
        String firstDetailTotalAmount = "10080";
        String firstDetailPrincipal = "10000";
        String firstDetailInterest = "20";
        String payWay = "0";
        Long firstFeeType = 1l;
        String repayScheduleNo = "";

        String paymentAccountName = "秦操";
        String paymentGateWay = "0";
        String tradeUuid = UUID.randomUUID().toString();
        SimpleDateFormat sim  = new SimpleDateFormat("YYYY-MM-DD HH:mm:ss");
        String transactionTime = sim.format(new Date());

        String cashFlowAccountNo = "6217857600016839337";

        cashFlowService.insertCashFlow(uniqueId,productCode,cashFlowAccountNo,paymentAccountName,new BigDecimal(orderAmount));

        String result = refactorMethod.applyRepaymentOderForOneDetail(orderRequestNo,orderRequestNo,transType,productCode,orderAmount,uniqueId,
                firstRepaymentNumber,firstRepaymentWay,
                firstDetailTotalAmount,firstDetailPrincipal,
                firstDetailInterest,firstFeeType,repayScheduleNo);

        if(!result.contains("订单已受理")){
            throw new Exception("还款订单校验错误!");
        }
        RepaymentStatus repaymentStatus;
        while (true) {
            RepaymentOrder repaymentOrder = repaymentOrderService.getRepaymentOrderByUniqueId(uniqueId, productCode);
            if (null == repaymentOrder) {
                throw new Exception("还款订单不存在!");
            }
            repaymentStatus = repaymentOrder.transferToRepaymentStatus();

            if (repaymentStatus != RepaymentStatus.TO_VERIFY && repaymentStatus != RepaymentStatus.VERIFICATION_IN_PROCESS) {
                break;
            }
            Thread.sleep(5000l);
        }

        if(RepaymentStatus.VERIFICATION_SUCCESS != repaymentStatus){
            throw new Exception("还款订单状态不正确");
        }

        String payResult = refactorMethod.payOrder(orderRequestNo,orderRequestNo,productCode,payWay,repaymentAccountNo,
                paymentAccountName,paymentGateWay
                ,tradeUuid,orderAmount,transactionTime);
        if(!payResult.contains("订单支付已受理")){
            throw new Exception("订单支付失败");
        }

        RepaymentOrder repaymentOrder = repaymentOrderService.getRepaymentOrderByUniqueId(uniqueId, productCode);
        repaymentStatus = repaymentOrder.transferToRepaymentStatus();

        Assert.assertEquals(RepaymentStatus.PAYMENT_IN_PROCESS,repaymentStatus);
    }


    /**
     * 支付订单参数校验，线下转账-accoutNo相同，accountName不同
     * @throws Exception
     */
    @Test
    public void checkPaymentParams28() throws Exception{
        String orderRequestNo = uniqueId;
        String transType = "0";
        String orderAmount = "10080";
        Long firstRepaymentWay = 2001l;
        String firstDetailTotalAmount = "10080";
        String firstDetailPrincipal = "10000";
        String firstDetailInterest = "20";
        String payWay = "0";
        Long firstFeeType = 1l;
        String repayScheduleNo = "";

        String paymentAccountName = "秦操";
        String paymentGateWay = "0";
        String tradeUuid = UUID.randomUUID().toString();
        SimpleDateFormat sim  = new SimpleDateFormat("YYYY-MM-DD HH:mm:ss");
        String transactionTime = sim.format(new Date());

        String cashFlowAccountName = "秦草草";


        cashFlowService.insertCashFlow(uniqueId,productCode,repaymentAccountNo,cashFlowAccountName,new BigDecimal(orderAmount));

        String result = refactorMethod.applyRepaymentOderForOneDetail(orderRequestNo,orderRequestNo,transType,productCode,orderAmount,uniqueId,
                firstRepaymentNumber,firstRepaymentWay,
                firstDetailTotalAmount,firstDetailPrincipal,
                firstDetailInterest,firstFeeType,repayScheduleNo);

        if(!result.contains("订单已受理")){
            throw new Exception("还款订单校验错误!");
        }
        RepaymentStatus repaymentStatus;
        while (true) {
            RepaymentOrder repaymentOrder = repaymentOrderService.getRepaymentOrderByUniqueId(uniqueId, productCode);
            if (null == repaymentOrder) {
                throw new Exception("还款订单不存在!");
            }
            repaymentStatus = repaymentOrder.transferToRepaymentStatus();

            if (repaymentStatus != RepaymentStatus.TO_VERIFY && repaymentStatus != RepaymentStatus.VERIFICATION_IN_PROCESS) {
                break;
            }
            Thread.sleep(5000l);
        }

        if(RepaymentStatus.VERIFICATION_SUCCESS != repaymentStatus){
            throw new Exception("还款订单状态不正确");
        }

        String payResult = refactorMethod.payOrder(orderRequestNo,orderRequestNo,productCode,payWay,repaymentAccountNo,
                paymentAccountName,paymentGateWay
                ,tradeUuid,orderAmount,transactionTime);
        if(!payResult.contains("订单支付已受理")){
            throw new Exception("订单支付失败");
        }

        RepaymentOrder repaymentOrder = repaymentOrderService.getRepaymentOrderByUniqueId(uniqueId, productCode);
        repaymentStatus = repaymentOrder.transferToRepaymentStatus();

        Assert.assertEquals(RepaymentStatus.PAYMENT_IN_PROCESS,repaymentStatus);
    }
}
