package com.zufangbao.aboutRepaymentOrder;

import com.demo2do.core.utils.DateUtils;
import com.zufangbao.refactor.method.RefactorMethod;
import com.zufangbao.sun.entity.repayment.order.RepaymentOrder;
import com.zufangbao.sun.entity.repayment.order.RepaymentStatus;
import com.zufangbao.sun.service.RepaymentOrderService;
import com.zufangbao.sun.service.RepurchaseService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.UUID;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:/local/applicationContext-*.xml" })
/**
 * 还款订单组合类型校验TEST
 */
public class CheckRepaymentOrderTwoContract {

    @Autowired
    private RepaymentOrderService repaymentOrderService;

    @Autowired
    private RepurchaseService repurchaseService;

    RefactorMethod refactorMethod = new RefactorMethod();

    private String firstUniqueId = "";
    private String secondUniqueId = "";
    String productCode = "CS0001";
    private String firstRepaymentNumber = "";
    private String secondRepaymentNumber = "";
    @Before
    public void setUp(){
        refactorMethod.deleteAllCashFlow();
        String totalAmount = "30000";
        firstUniqueId = UUID.randomUUID().toString() + 1;
        secondUniqueId = UUID.randomUUID().toString() + 2;
        String repaymentAccountNo = "6217857600016839330";
        String loanCustomerName = "秦操";
        String interest = "20";
        String firstLoanCustomerNo = "11111117";
        String secondLoanCustomerNo = "11111118";
        String firstPlanDate = DateUtils.format(DateUtils.addMonths(new Date(), -1));
        String secondPlanDate = DateUtils.format(new Date());
        String thirdPlanDate = DateUtils.format(DateUtils.addMonths(new Date(), 1));

        refactorMethod.importAssetPackage(totalAmount,productCode,firstUniqueId,repaymentAccountNo,interest,firstLoanCustomerNo,
                firstPlanDate,secondPlanDate,thirdPlanDate,loanCustomerName);
        refactorMethod.importAssetPackage(totalAmount,productCode,secondUniqueId,repaymentAccountNo,interest,secondLoanCustomerNo,
                firstPlanDate,secondPlanDate,thirdPlanDate,loanCustomerName);

        firstRepaymentNumber = refactorMethod.queryRepaymentPlan(productCode,firstUniqueId,0);
        secondRepaymentNumber = refactorMethod.queryRepaymentPlan(productCode,secondUniqueId,0);
        System.out.println(firstRepaymentNumber);

    }

    /**
     * 两个贷款合同下的还款计划，委托转付-委托转付
     * 校验成功
     * @throws Exception
     */
    @Test
    public void testOrderForTwoContract() throws Exception{
        String orderRequestNo = UUID.randomUUID().toString();
        String orderAmount = "20140";
        Long firstRepaymentWay = 2001l;
        Long secondRepaymentWay = 2001l;
        String firstDetailTotalAmount = "10080";
        String secondDetailTotalAmount = "10060";
        String firstDetailPrincipal = "10000";
        String firstDetailInterest = "20";
        Long firstFeeType = 1l;

        String result = refactorMethod.applyRepaymentOderForTwoDetails(orderRequestNo,productCode,orderAmount,firstUniqueId,secondUniqueId,
                firstRepaymentNumber,secondRepaymentNumber,firstRepaymentWay,secondRepaymentWay,
                firstDetailTotalAmount,secondDetailTotalAmount,firstDetailPrincipal,firstDetailPrincipal,
                firstDetailInterest,firstDetailInterest,firstFeeType,firstFeeType);


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
        Assert.assertEquals(RepaymentStatus.VERIFICATION_SUCCESS,repaymentStatus);
    }

    /**
     * 两个贷款合同下的还款计划，委托转付-代偿
     * 校验成功
     * @throws Exception
     */
    @Test
    public void testOrderForTwoContract2() throws Exception{
        String orderRequestNo = UUID.randomUUID().toString();
        String orderAmount = "20140";
        Long firstRepaymentWay = 2001l;
        Long secondRepaymentWay = 2005l;
        String firstDetailTotalAmount = "10080";
        String secondDetailTotalAmount = "10060";
        String firstDetailPrincipal = "10000";
        String firstDetailInterest = "20";
        Long firstFeeType = 1l;

        String result = refactorMethod.applyRepaymentOderForTwoDetails(orderRequestNo,productCode,orderAmount,firstUniqueId,secondUniqueId,
                firstRepaymentNumber,secondRepaymentNumber,firstRepaymentWay,secondRepaymentWay,
                firstDetailTotalAmount,secondDetailTotalAmount,firstDetailPrincipal,firstDetailPrincipal,
                firstDetailInterest,firstDetailInterest,firstFeeType,firstFeeType);


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
        Assert.assertEquals(RepaymentStatus.VERIFICATION_SUCCESS,repaymentStatus);
    }


    /**
     * 两个贷款合同下的还款计划，委托转付-主动付款
     * 校验失败
     * @throws Exception
     */
    @Test
    public void testOrderForTwoContract3() throws Exception{
        String orderRequestNo = UUID.randomUUID().toString();
        String orderAmount = "20140";
        Long firstRepaymentWay = 2001l;
        Long secondRepaymentWay = 3001l;
        String firstDetailTotalAmount = "10080";
        String secondDetailTotalAmount = "10060";
        String firstDetailPrincipal = "10000";
        String firstDetailInterest = "20";
        Long firstFeeType = 1l;

        String result = refactorMethod.applyRepaymentOderForTwoDetails(orderRequestNo,productCode,orderAmount,firstUniqueId,secondUniqueId,
                firstRepaymentNumber,secondRepaymentNumber,firstRepaymentWay,secondRepaymentWay,
                firstDetailTotalAmount,secondDetailTotalAmount,firstDetailPrincipal,firstDetailPrincipal,
                firstDetailInterest,firstDetailInterest,firstFeeType,firstFeeType);


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
        Assert.assertEquals(RepaymentStatus.VERIFICATION_FAILURE,repaymentStatus);
    }


    /**
     * 两个贷款合同下的还款计划，在线支付-线上代扣
     * 校验失败
     * @throws Exception
     */
    @Test
    public void testOrderForTwoContract4() throws Exception{
        String orderRequestNo = UUID.randomUUID().toString();
        String orderAmount = "20140";
        Long firstRepaymentWay = 6001l;
        Long secondRepaymentWay = 1000l;
        String firstDetailTotalAmount = "10080";
        String secondDetailTotalAmount = "10060";
        String firstDetailPrincipal = "10000";
        String firstDetailInterest = "20";
        Long firstFeeType = 1l;

        String result = refactorMethod.applyRepaymentOderForTwoDetails(orderRequestNo,productCode,orderAmount,firstUniqueId,secondUniqueId,
                firstRepaymentNumber,secondRepaymentNumber,firstRepaymentWay,secondRepaymentWay,
                firstDetailTotalAmount,secondDetailTotalAmount,firstDetailPrincipal,firstDetailPrincipal,
                firstDetailInterest,firstDetailInterest,firstFeeType,firstFeeType);


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
        Assert.assertEquals(RepaymentStatus.VERIFICATION_FAILURE,repaymentStatus);
    }

    /**
     * 两个贷款合同下的还款计划，在线支付-商户代扣
     * 校验失败
     * @throws Exception
     */
    @Test
    public void testOrderForTwoContract5() throws Exception{
        String orderRequestNo = UUID.randomUUID().toString();
        String orderAmount = "20140";
        Long firstRepaymentWay = 6001l;
        Long secondRepaymentWay = 4001l;
        String firstDetailTotalAmount = "10080";
        String secondDetailTotalAmount = "10060";
        String firstDetailPrincipal = "10000";
        String firstDetailInterest = "20";
        Long firstFeeType = 1l;

        String result = refactorMethod.applyRepaymentOderForTwoDetails(orderRequestNo,productCode,orderAmount,firstUniqueId,secondUniqueId,
                firstRepaymentNumber,secondRepaymentNumber,firstRepaymentWay,secondRepaymentWay,
                firstDetailTotalAmount,secondDetailTotalAmount,firstDetailPrincipal,firstDetailPrincipal,
                firstDetailInterest,firstDetailInterest,firstFeeType,firstFeeType);


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
        Assert.assertEquals(RepaymentStatus.VERIFICATION_FAILURE,repaymentStatus);
    }

    /**
     * 两个贷款合同下的还款计划，委托转付-商户代扣
     * 校验失败
     * @throws Exception
     */
    @Test
    public void testOrderForTwoContract6() throws Exception{
        String orderRequestNo = UUID.randomUUID().toString();
        String orderAmount = "20140";
        Long firstRepaymentWay = 2001l;
        Long secondRepaymentWay = 4001l;
        String firstDetailTotalAmount = "10080";
        String secondDetailTotalAmount = "10060";
        String firstDetailPrincipal = "10000";
        String firstDetailInterest = "20";
        Long firstFeeType = 1l;

        String result = refactorMethod.applyRepaymentOderForTwoDetails(orderRequestNo,productCode,orderAmount,firstUniqueId,secondUniqueId,
                firstRepaymentNumber,secondRepaymentNumber,firstRepaymentWay,secondRepaymentWay,
                firstDetailTotalAmount,secondDetailTotalAmount,firstDetailPrincipal,firstDetailPrincipal,
                firstDetailInterest,firstDetailInterest,firstFeeType,firstFeeType);


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
        Assert.assertEquals(RepaymentStatus.VERIFICATION_FAILURE,repaymentStatus);
    }

    /**
     * 两个贷款合同下的还款计划，委托转付-线上代扣
     * 校验失败
     * @throws Exception
     */
    @Test
    public void testOrderForTwoContract7() throws Exception{
        String orderRequestNo = UUID.randomUUID().toString();
        String orderAmount = "20140";
        Long firstRepaymentWay = 2001l;
        Long secondRepaymentWay = 1000l;
        String firstDetailTotalAmount = "10080";
        String secondDetailTotalAmount = "10060";
        String firstDetailPrincipal = "10000";
        String firstDetailInterest = "20";
        Long firstFeeType = 1l;

        String result = refactorMethod.applyRepaymentOderForTwoDetails(orderRequestNo,productCode,orderAmount,firstUniqueId,secondUniqueId,
                firstRepaymentNumber,secondRepaymentNumber,firstRepaymentWay,secondRepaymentWay,
                firstDetailTotalAmount,secondDetailTotalAmount,firstDetailPrincipal,firstDetailPrincipal,
                firstDetailInterest,firstDetailInterest,firstFeeType,firstFeeType);


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
        Assert.assertEquals(RepaymentStatus.VERIFICATION_FAILURE,repaymentStatus);
    }


    /**
     * 两个贷款合同下的还款计划，线上代扣-快捷支付
     * 校验失败
     * @throws Exception
     */
    @Test
    public void testOrderForTwoContract8() throws Exception{
        String orderRequestNo = UUID.randomUUID().toString();
        String orderAmount = "20140";
        Long firstRepaymentWay = 1000l;
        Long secondRepaymentWay = 5001l;
        String firstDetailTotalAmount = "10080";
        String secondDetailTotalAmount = "10060";
        String firstDetailPrincipal = "10000";
        String firstDetailInterest = "20";
        Long firstFeeType = 1l;

        String result = refactorMethod.applyRepaymentOderForTwoDetails(orderRequestNo,productCode,orderAmount,firstUniqueId,secondUniqueId,
                firstRepaymentNumber,secondRepaymentNumber,firstRepaymentWay,secondRepaymentWay,
                firstDetailTotalAmount,secondDetailTotalAmount,firstDetailPrincipal,firstDetailPrincipal,
                firstDetailInterest,firstDetailInterest,firstFeeType,firstFeeType);


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
        Assert.assertEquals(RepaymentStatus.VERIFICATION_FAILURE,repaymentStatus);
    }


    /**
     * 两个贷款合同下的还款计划，线上代扣-线上代扣
     * 校验失败
     * @throws Exception
     */
    @Test
    public void testOrderForTwoContract9() throws Exception{
        String orderRequestNo = UUID.randomUUID().toString();
        String orderAmount = "20140";
        Long firstRepaymentWay = 1000l;
        Long secondRepaymentWay = 1000l;
        String firstDetailTotalAmount = "10080";
        String secondDetailTotalAmount = "10060";
        String firstDetailPrincipal = "10000";
        String firstDetailInterest = "20";
        Long firstFeeType = 1l;

        String result = refactorMethod.applyRepaymentOderForTwoDetails(orderRequestNo,productCode,orderAmount,firstUniqueId,secondUniqueId,
                firstRepaymentNumber,secondRepaymentNumber,firstRepaymentWay,secondRepaymentWay,
                firstDetailTotalAmount,secondDetailTotalAmount,firstDetailPrincipal,firstDetailPrincipal,
                firstDetailInterest,firstDetailInterest,firstFeeType,firstFeeType);


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
        Assert.assertEquals(RepaymentStatus.VERIFICATION_FAILURE,repaymentStatus);
    }

    /**
     * 两个贷款合同下的还款计划，快捷支付-快捷支付
     * 校验失败
     * @throws Exception
     */
    @Test
    public void testOrderForTwoContract10() throws Exception{
        String orderRequestNo = UUID.randomUUID().toString();
        String orderAmount = "20140";
        Long firstRepaymentWay = 5001l;
        Long secondRepaymentWay = 5001l;
        String firstDetailTotalAmount = "10080";
        String secondDetailTotalAmount = "10060";
        String firstDetailPrincipal = "10000";
        String firstDetailInterest = "20";
        Long firstFeeType = 1l;

        String result = refactorMethod.applyRepaymentOderForTwoDetails(orderRequestNo,productCode,orderAmount,firstUniqueId,secondUniqueId,
                firstRepaymentNumber,secondRepaymentNumber,firstRepaymentWay,secondRepaymentWay,
                firstDetailTotalAmount,secondDetailTotalAmount,firstDetailPrincipal,firstDetailPrincipal,
                firstDetailInterest,firstDetailInterest,firstFeeType,firstFeeType);


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
        Assert.assertEquals(RepaymentStatus.VERIFICATION_FAILURE,repaymentStatus);
    }


    /**
     * 两个贷款合同下的还款计划，在线支付-在线支付
     * 校验失败
     * @throws Exception
     */
    @Test
    public void testOrderForTwoContract11() throws Exception{
        String orderRequestNo = UUID.randomUUID().toString();
        String orderAmount = "20140";
        Long firstRepaymentWay = 6001l;
        Long secondRepaymentWay = 6001l;
        String firstDetailTotalAmount = "10080";
        String secondDetailTotalAmount = "10060";
        String firstDetailPrincipal = "10000";
        String firstDetailInterest = "20";
        Long firstFeeType = 1l;

        String result = refactorMethod.applyRepaymentOderForTwoDetails(orderRequestNo,productCode,orderAmount,firstUniqueId,secondUniqueId,
                firstRepaymentNumber,secondRepaymentNumber,firstRepaymentWay,secondRepaymentWay,
                firstDetailTotalAmount,secondDetailTotalAmount,firstDetailPrincipal,firstDetailPrincipal,
                firstDetailInterest,firstDetailInterest,firstFeeType,firstFeeType);


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
        Assert.assertEquals(RepaymentStatus.VERIFICATION_FAILURE,repaymentStatus);
    }


    /**
     * 两个贷款合同下的还款计划，商户代扣-商户代扣
     * 校验失败
     * @throws Exception
     */
    @Test
    public void testOrderForTwoContract12() throws Exception{
        String orderRequestNo = UUID.randomUUID().toString();
        String orderAmount = "20140";
        Long firstRepaymentWay = 4001l;
        Long secondRepaymentWay = 4001l;
        String firstDetailTotalAmount = "10080";
        String secondDetailTotalAmount = "10060";
        String firstDetailPrincipal = "10000";
        String firstDetailInterest = "20";
        Long firstFeeType = 1l;

        String result = refactorMethod.applyRepaymentOderForTwoDetails(orderRequestNo,productCode,orderAmount,firstUniqueId,secondUniqueId,
                firstRepaymentNumber,secondRepaymentNumber,firstRepaymentWay,secondRepaymentWay,
                firstDetailTotalAmount,secondDetailTotalAmount,firstDetailPrincipal,firstDetailPrincipal,
                firstDetailInterest,firstDetailInterest,firstFeeType,firstFeeType);


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
        Assert.assertEquals(RepaymentStatus.VERIFICATION_FAILURE,repaymentStatus);
    }


    /**
     * 两个贷款合同下的还款计划，loanCustomerNo不同，主动付款-他人代偿（3）
     * 校验失败
     * @throws Exception
     */
    @Test
    public void testOrderForTwoContract13() throws Exception{
        String orderRequestNo = UUID.randomUUID().toString();
        String orderAmount = "20140";
        Long firstRepaymentWay = 3001l;
        Long secondRepaymentWay = 3002l;
        String firstDetailTotalAmount = "10080";
        String secondDetailTotalAmount = "10060";
        String firstDetailPrincipal = "10000";
        String firstDetailInterest = "20";
        Long firstFeeType = 1l;

        String result = refactorMethod.applyRepaymentOderForTwoDetails(orderRequestNo,productCode,orderAmount,firstUniqueId,secondUniqueId,
                firstRepaymentNumber,secondRepaymentNumber,firstRepaymentWay,secondRepaymentWay,
                firstDetailTotalAmount,secondDetailTotalAmount,firstDetailPrincipal,firstDetailPrincipal,
                firstDetailInterest,firstDetailInterest,firstFeeType,firstFeeType);


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
        Assert.assertEquals(RepaymentStatus.VERIFICATION_FAILURE,repaymentStatus);
    }


    /**
     * 两个贷款合同下的还款计划，loanCustomerNo相同，主动付款-他人代偿（3）
     * 校验成功
     * @throws Exception
     */
    @Test
    public void testOrderForTwoContract14() throws Exception{
        String totalAmount = "30000";
        secondUniqueId = UUID.randomUUID().toString() + 2;
        String repaymentAccountNo = "6217857600016839330";
        String loanCustomerName = "秦操";
        String interest = "20";
        String secondLoanCustomerNo = "11111117";
        String firstPlanDate = DateUtils.format(DateUtils.addMonths(new Date(), -1));
        String secondPlanDate = DateUtils.format(new Date());
        String thirdPlanDate = DateUtils.format(DateUtils.addMonths(new Date(), 1));

        refactorMethod.importAssetPackage(totalAmount,productCode,secondUniqueId,repaymentAccountNo,interest,secondLoanCustomerNo,
                firstPlanDate,secondPlanDate,thirdPlanDate,loanCustomerName);

        secondRepaymentNumber = refactorMethod.queryRepaymentPlan(productCode,secondUniqueId,0);
        System.out.println(secondRepaymentNumber);

        String orderRequestNo = UUID.randomUUID().toString();
        String orderAmount = "20140";
        Long firstRepaymentWay = 3001l;
        Long secondRepaymentWay = 3002l;
        String firstDetailTotalAmount = "10080";
        String secondDetailTotalAmount = "10060";
        String firstDetailPrincipal = "10000";
        String firstDetailInterest = "20";
        Long firstFeeType = 1l;

        String result = refactorMethod.applyRepaymentOderForTwoDetails(orderRequestNo,productCode,orderAmount,firstUniqueId,secondUniqueId,
                firstRepaymentNumber,secondRepaymentNumber,firstRepaymentWay,secondRepaymentWay,
                firstDetailTotalAmount,secondDetailTotalAmount,firstDetailPrincipal,firstDetailPrincipal,
                firstDetailInterest,firstDetailInterest,firstFeeType,firstFeeType);


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
        Assert.assertEquals(RepaymentStatus.VERIFICATION_SUCCESS,repaymentStatus);
    }

    /**
     * 两个贷款合同下的还款计划，委托转付-回购
     * 校验成功
     * @throws Exception
     */
    @Test
    public void testOrderForTwoContract15() throws Exception{

        String orderRequestNo = UUID.randomUUID().toString();
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

        refactorMethod.applyRepurchase(productCode,secondUniqueId,secontDetailPrincipal,firstDetailInterest,secondDetailTotalAmount);

        String repurchaseDocUuid = repurchaseService.getRepurchaseDocUuidBy(secondUniqueId);
        if (null == repurchaseDocUuid || "".equals(repurchaseDocUuid)){
            throw new Exception("回购单不存在");
        }

        String result = refactorMethod.applyRepaymentOderForTwoDetails(orderRequestNo,productCode,orderAmount,firstUniqueId,secondUniqueId,
                firstRepaymentNumber,repurchaseDocUuid,firstRepaymentWay,secondRepaymentWay,
                firstDetailTotalAmount,secondDetailTotalAmount,firstDetailPrincipal,secontDetailPrincipal,
                firstDetailInterest,firstDetailInterest,firstFeeType,secondFeeType);


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
        Assert.assertEquals(RepaymentStatus.VERIFICATION_SUCCESS,repaymentStatus);

    }


    /**
     * 还款订单，线上代扣-预收，2条明细
     * 校验失败
     */
    @Test
    public void testOrderForTwoContract16() throws Exception{

        String orderRequestNo = UUID.randomUUID().toString();
        String orderAmount = "40140";
        Long firstRepaymentWay = 1000l;
        Long secondRepaymentWay = 1000l;
        String firstDetailTotalAmount = "10080";
        String secondDetailTotalAmount = "30060";
        String firstDetailPrincipal = "10000";
        String secontDetailPrincipal = "30000";
        String firstDetailInterest = "20";
        Long firstFeeType = 1l;
        Long secondFeeType = 1l;


        String result = refactorMethod.applyRepaymentOderForTwoDetails(orderRequestNo,productCode,orderAmount,firstUniqueId,secondUniqueId,
                "",null,firstRepaymentWay,secondRepaymentWay,
                firstDetailTotalAmount,secondDetailTotalAmount,firstDetailPrincipal,secontDetailPrincipal,
                firstDetailInterest,firstDetailInterest,firstFeeType,secondFeeType);


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
        Assert.assertEquals(RepaymentStatus.VERIFICATION_FAILURE,repaymentStatus);

    }


    /**
     * 还款订单，快捷支付-预收，2条明细
     * 校验失败
     */
    @Test
    public void testOrderForTwoContract17() throws Exception{

        String orderRequestNo = UUID.randomUUID().toString();
        String orderAmount = "40140";
        Long firstRepaymentWay = 5001l;
        Long secondRepaymentWay = 5001l;
        String firstDetailTotalAmount = "10080";
        String secondDetailTotalAmount = "30060";
        String firstDetailPrincipal = "10000";
        String secontDetailPrincipal = "30000";
        String firstDetailInterest = "20";
        Long firstFeeType = 1l;
        Long secondFeeType = 1l;


        String result = refactorMethod.applyRepaymentOderForTwoDetails(orderRequestNo,productCode,orderAmount,firstUniqueId,secondUniqueId,
                "",null,firstRepaymentWay,secondRepaymentWay,
                firstDetailTotalAmount,secondDetailTotalAmount,firstDetailPrincipal,secontDetailPrincipal,
                firstDetailInterest,firstDetailInterest,firstFeeType,secondFeeType);


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
        Assert.assertEquals(RepaymentStatus.VERIFICATION_FAILURE,repaymentStatus);

    }
}
