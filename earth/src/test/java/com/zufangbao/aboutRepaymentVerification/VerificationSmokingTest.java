package com.zufangbao.aboutRepaymentVerification;

import com.demo2do.core.utils.DateUtils;
import com.zufangbao.refactor.method.RefactorMethod;
import com.zufangbao.sun.entity.account.AuditStatus;
import com.zufangbao.sun.entity.directbank.business.CashFlow;
import com.zufangbao.sun.entity.repayment.order.PaymentOrder;
import com.zufangbao.sun.entity.repayment.order.RepaymentOrder;
import com.zufangbao.sun.entity.repayment.order.RepaymentStatus;
import com.zufangbao.sun.service.CashFlowService;
import com.zufangbao.sun.service.PaymentOrderService;
import com.zufangbao.sun.service.RepaymentOrderService;
import com.zufangbao.sun.service.RepurchaseService;
import org.apache.commons.collections.CollectionUtils;
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
/**
 * 还款订单核销冒烟测试
 */
public class VerificationSmokingTest {
    @Autowired
    private CashFlowService cashFlowService;

    @Autowired
    private RepaymentOrderService repaymentOrderService;

    @Autowired
    private PaymentOrderService paymentOrderService;

    @Autowired
    private RepurchaseService repurchaseService;

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
     * 还款订单，线下转账-转托转付核销
     * @throws Exception
     */
    @Test
    public void repaymentOrderVerification1() throws Exception{
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

        List<String> paymentNoList = new ArrayList<>();
        paymentNoList.add(orderRequestNo);

        cashFlowService.insertCashFlow(uniqueId,productCode,repaymentAccountNo,paymentAccountName,new BigDecimal(orderAmount));

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

        List<PaymentOrder> paymentOrderList = paymentOrderService.getPaymentOrderListByPaymentNos(paymentNoList);
        if(CollectionUtils.isEmpty(paymentOrderList)){
            throw new Exception("支付单不存在");
        }

        PaymentOrder paymentOrder = paymentOrderList.get(0);

        String cashFlowUuid = paymentOrder.getOutlierDocumentUuid();
        AuditStatus auditStatus;
        while (true) {
            CashFlow cashFlow = cashFlowService.getCashFlowByCashFlowUuid(cashFlowUuid);
            if (null == cashFlow) {
                throw new Exception("流水不存在");
            }
            auditStatus = cashFlow.getAuditStatus();
            if(AuditStatus.CREATE != auditStatus){
                break;
            }
            Thread.sleep(5000l);
        }

        while (true) {
            RepaymentOrder repaymentOrder = repaymentOrderService.getRepaymentOrderByUniqueId(uniqueId, productCode);
            repaymentStatus = repaymentOrder.transferToRepaymentStatus();

            if (repaymentStatus != RepaymentStatus.PAYMENT_END) {
                break;
            }
            Thread.sleep(5000l);
        }

        Assert.assertEquals(AuditStatus.ISSUED,auditStatus);
        Assert.assertEquals(RepaymentStatus.RECOVER_END,repaymentStatus);
    }

    /**
     * 还款订单，线下转账-回购核销
     * @throws Exception
     */
    @Test
    public void repaymentOrderVerification2() throws Exception{
        String orderRequestNo = uniqueId;
        String orderAmount = "40140";
        Long firstRepaymentWay = 2001l;
        Long secondRepaymentWay = 2003l;
        String firstDetailTotalAmount = "10080";
        String secondDetailTotalAmount = "30060";
        String firstDetailPrincipal = "10000";
        String secontDetailPrincipal = "30000";
        String firstDetailInterest = "20";
        Long firstFeeType = 1l;
        Long secondFeeType = 2l;
        String payWay = "0";

        String paymentAccountName = "秦操";
        String paymentGateWay = "0";
        String tradeUuid = UUID.randomUUID().toString();
        SimpleDateFormat sim  = new SimpleDateFormat("YYYY-MM-DD HH:mm:ss");
        String transactionTime = sim.format(new Date());

        String totalAmount = "30000";
        String secondUniqueId = UUID.randomUUID().toString();
        String repaymentAccountNo = "6217857600016839330";
        String loanCustomerName = "秦操";
        String interest = "20";
        String firstLoanCustomerNo = "11111117";
        String firstPlanDate = DateUtils.format(DateUtils.addMonths(new Date(), -1));
        String secondPlanDate = DateUtils.format(new Date());
        String thirdPlanDate = DateUtils.format(DateUtils.addMonths(new Date(), 1));

        refactorMethod.importAssetPackage(totalAmount,productCode,secondUniqueId,repaymentAccountNo,interest,firstLoanCustomerNo,
                firstPlanDate,secondPlanDate,thirdPlanDate,loanCustomerName);

        List<String> paymentNoList = new ArrayList<>();
        paymentNoList.add(orderRequestNo);

        refactorMethod.applyRepurchase(productCode,secondUniqueId,secontDetailPrincipal,firstDetailInterest,secondDetailTotalAmount);
        cashFlowService.insertCashFlow(uniqueId,productCode,repaymentAccountNo,paymentAccountName,new BigDecimal(orderAmount));

        String repurchaseDocUuid = repurchaseService.getRepurchaseDocUuidBy(secondUniqueId);
        if (null == repurchaseDocUuid || "".equals(repurchaseDocUuid)){
            throw new Exception("回购单不存在");
        }
        String result = refactorMethod.applyRepaymentOderForTwoDetails(orderRequestNo,productCode,orderAmount,uniqueId,secondUniqueId,
                firstRepaymentNumber,repurchaseDocUuid,firstRepaymentWay,secondRepaymentWay,
                firstDetailTotalAmount,secondDetailTotalAmount,firstDetailPrincipal,secontDetailPrincipal,
                firstDetailInterest,firstDetailInterest,firstFeeType,secondFeeType);

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

        List<PaymentOrder> paymentOrderList = paymentOrderService.getPaymentOrderListByPaymentNos(paymentNoList);
        if(CollectionUtils.isEmpty(paymentOrderList)){
            throw new Exception("支付单不存在");
        }

        PaymentOrder paymentOrder = paymentOrderList.get(0);

        String cashFlowUuid = paymentOrder.getOutlierDocumentUuid();
        AuditStatus auditStatus;
        while (true) {
            CashFlow cashFlow = cashFlowService.getCashFlowByCashFlowUuid(cashFlowUuid);
            if (null == cashFlow) {
                throw new Exception("流水不存在");
            }
            auditStatus = cashFlow.getAuditStatus();
            if(AuditStatus.CREATE != auditStatus){
                break;
            }
            Thread.sleep(5000l);
        }

        while (true) {
            RepaymentOrder repaymentOrder = repaymentOrderService.getRepaymentOrderByUniqueId(uniqueId, productCode);
            repaymentStatus = repaymentOrder.transferToRepaymentStatus();

            if (repaymentStatus != RepaymentStatus.PAYMENT_END) {
                break;
            }
            Thread.sleep(5000l);
        }

        Assert.assertEquals(AuditStatus.ISSUED,auditStatus);
        Assert.assertEquals(RepaymentStatus.RECOVER_END,repaymentStatus);
    }

    /**
     * 还款订单，线下转账-主动付款核销
     * @throws Exception
     */
    @Test
    public void repaymentOrderVerification3() throws Exception{
        String orderRequestNo = uniqueId;
        String transType = "0";
        String orderAmount = "10080";
        Long firstRepaymentWay = 3001l;
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

        List<String> paymentNoList = new ArrayList<>();
        paymentNoList.add(orderRequestNo);

        cashFlowService.insertCashFlow(uniqueId,productCode,repaymentAccountNo,paymentAccountName,new BigDecimal(orderAmount));

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

        List<PaymentOrder> paymentOrderList = paymentOrderService.getPaymentOrderListByPaymentNos(paymentNoList);
        if(CollectionUtils.isEmpty(paymentOrderList)){
            throw new Exception("支付单不存在");
        }

        PaymentOrder paymentOrder = paymentOrderList.get(0);

        String cashFlowUuid = paymentOrder.getOutlierDocumentUuid();
        AuditStatus auditStatus;
        while (true) {
            CashFlow cashFlow = cashFlowService.getCashFlowByCashFlowUuid(cashFlowUuid);
            if (null == cashFlow) {
                throw new Exception("流水不存在");
            }
            auditStatus = cashFlow.getAuditStatus();
            if(AuditStatus.CREATE != auditStatus){
                break;
            }
            Thread.sleep(5000l);
        }

        while (true) {
            RepaymentOrder repaymentOrder = repaymentOrderService.getRepaymentOrderByUniqueId(uniqueId, productCode);
            repaymentStatus = repaymentOrder.transferToRepaymentStatus();

            if (repaymentStatus != RepaymentStatus.PAYMENT_END) {
                break;
            }
            Thread.sleep(5000l);
        }

        Assert.assertEquals(AuditStatus.ISSUED,auditStatus);
        Assert.assertEquals(RepaymentStatus.RECOVER_END,repaymentStatus);
    }


    /**
     * 还款订单，快捷支付核销
     */
    @Test
    public void repaymentOrderVerification4() throws Exception{
        String orderRequestNo = uniqueId;
        String transType = "0";
        String orderAmount = "10080";
        Long firstRepaymentWay = 5001l;
        String firstDetailTotalAmount = "10080";
        String firstDetailPrincipal = "10000";
        String firstDetailInterest = "20";
        Long firstFeeType = 1l;
        String repayScheduleNo = "";


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

        if(RepaymentStatus.PAYMENT_IN_PROCESS != repaymentStatus){
            throw new Exception("还款订单状态不正确");
        }


        while (true) {
            RepaymentOrder repaymentOrder = repaymentOrderService.getRepaymentOrderByUniqueId(uniqueId, productCode);
            repaymentStatus = repaymentOrder.transferToRepaymentStatus();

            if (repaymentStatus !=RepaymentStatus.PAYMENT_IN_PROCESS && repaymentStatus != RepaymentStatus.PAYMENT_END) {
                break;
            }
            Thread.sleep(5000l);
        }

        Assert.assertEquals(RepaymentStatus.RECOVER_END,repaymentStatus);
    }


    /**
     * 还款订单，线上代扣核销
     */
    @Test
    public void repaymentOrderVerification5() throws Exception{
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

            if (repaymentStatus != RepaymentStatus.PAYMENT_IN_PROCESS && repaymentStatus != RepaymentStatus.PAYMENT_END) {
                break;
            }
            Thread.sleep(5000l);
        }

        Assert.assertEquals(RepaymentStatus.RECOVER_END,repaymentStatus);
    }


    /**
     * 还款订单，在线支付核销
     */
    @Test
    public void repaymentOrderVerification6() throws Exception{
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

            if (repaymentStatus != RepaymentStatus.PAYMENT_IN_PROCESS && repaymentStatus != RepaymentStatus.PAYMENT_END) {
                break;
            }
            Thread.sleep(5000l);
        }

        Assert.assertEquals(RepaymentStatus.RECOVER_END,repaymentStatus);
    }


    /**
     * 还款订单，预收核销
     */
    @Test
    public void repaymentOrderVerification7() throws Exception {
        String orderRequestNo = uniqueId;
        String transType = "0";
        String orderAmount = "30000";
        Long firstRepaymentWay = 5001l;
        String firstDetailTotalAmount = "30000";
        String firstDetailPrincipal = "30000";
        String firstDetailInterest = "0";
        Long firstFeeType = 1l;
        String interest = "0";
        Integer AssetType = 0;
        String requestReason = "1";
        String planDate = DateUtils.format(new Date());
        String repayScheduleNo = "";
        String contractNo = "";

        String result = refactorMethod.applyRepaymentOderForOneDetail(orderRequestNo, orderRequestNo, transType, productCode, orderAmount, uniqueId,
                "", firstRepaymentWay,
                firstDetailTotalAmount, firstDetailPrincipal,
                firstDetailInterest, firstFeeType,repayScheduleNo);

        if (!result.contains("订单已受理")) {
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

        if (RepaymentStatus.PAYMENT_IN_PROCESS != repaymentStatus) {
            throw new Exception("还款订单状态不正确");
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

        String modifyResult = refactorMethod.modifyRepaymentPlanForOne(uniqueId,orderAmount,interest,planDate,AssetType,requestReason,repayScheduleNo,contractNo);
        if(!modifyResult.contains("成功")){
            throw new Exception("变更还款计划失败");
        }

        RepaymentOrder repaymentOrder = repaymentOrderService.getRepaymentOrderByUniqueId(uniqueId, productCode);
        repaymentStatus = repaymentOrder.transferToRepaymentStatus();
        Assert.assertEquals(RepaymentStatus.RECOVER_END, repaymentStatus);
    }
}
