package com.zufangbao.aboutRepaymentOrder;

import com.demo2do.core.utils.DateUtils;
import com.zufangbao.refactor.method.RefactorMethod;
import com.zufangbao.sun.entity.repayment.order.RepaymentOrder;
import com.zufangbao.sun.entity.repayment.order.RepaymentStatus;
import com.zufangbao.sun.service.RepaymentOrderService;
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
 * 还款订单正常冒烟测试TEST
 */
public class CheckRepaymentOrderOneContract {

    @Autowired
    private RepaymentOrderService repaymentOrderService;

    RefactorMethod refactorMethod = new RefactorMethod();

    private String uniqueId = "";
    String productCode = "CS0001";
    private String firstRepaymentNumber = "";
    private String secondRepaymentNumber = "";
    @Before
    public void setUp(){
        refactorMethod.deleteAllCashFlow();
        String totalAmount = "30000";
        uniqueId = UUID.randomUUID().toString() + 1;
        String repaymentAccountNo = "6217857600016839330";
        String loanCustomerName = "秦操";
        String interest = "20";
        String loanCustomerNo = "11111117";
        String firstPlanDate = DateUtils.format(DateUtils.addMonths(new Date(), -1));
        String secondPlanDate = DateUtils.format(new Date());
        String thirdPlanDate = DateUtils.format(DateUtils.addMonths(new Date(), 1));

        refactorMethod.importAssetPackage(totalAmount,productCode,uniqueId,repaymentAccountNo,interest,loanCustomerNo,
                firstPlanDate,secondPlanDate,thirdPlanDate,loanCustomerName);

        firstRepaymentNumber = refactorMethod.queryRepaymentPlan(productCode,uniqueId,0);
        secondRepaymentNumber = refactorMethod.queryRepaymentPlan(productCode,uniqueId,1);
        System.out.println(firstRepaymentNumber);
        System.out.println(secondRepaymentNumber);

    }

    /**
     * 冒烟测试
     * 还款订单，同一合同下两个还款计划的商户订单
     * @throws Exception
     */
    @Test
    public void testNormalBusinessOrder() throws Exception{
        String orderRequestNo = uniqueId;
        String orderAmount = "20140";
        Long firstRepaymentWay = 2001l;
        Long secondRepaymentWay = 2001l;
        String firstDetailTotalAmount = "10080";
        String secondDetailTotalAmount = "10060";
        String firstDetailPrincipal = "10000";
        String firstDetailInterest = "20";
        Long firstFeeType = 1l;

        String result = refactorMethod.applyRepaymentOderForTwoDetails(orderRequestNo,productCode,orderAmount,uniqueId,uniqueId,
                                    firstRepaymentNumber,secondRepaymentNumber,firstRepaymentWay,secondRepaymentWay,
                                    firstDetailTotalAmount,secondDetailTotalAmount,firstDetailPrincipal,firstDetailPrincipal,
                                    firstDetailInterest,firstDetailInterest,firstFeeType,firstFeeType);


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
        Assert.assertEquals(RepaymentStatus.VERIFICATION_SUCCESS,repaymentStatus);
    }


    /**
     * 冒烟测试
     * 还款订单，同一合同下两个还款计划的主动(主动付款,他人代偿)订单
     * @throws Exception
     */
    @Test
    public void testNormalActiveOrder() throws Exception{
        String orderRequestNo = uniqueId;
        String orderAmount = "20140";
        Long firstRepaymentWay = 3001l;
        Long secondRepaymentWay = 3002l;
        String firstDetailTotalAmount = "10080";
        String secondDetailTotalAmount = "10060";
        String firstDetailPrincipal = "10000";
        String firstDetailInterest = "20";
        Long firstFeeType = 1l;

        String result = refactorMethod.applyRepaymentOderForTwoDetails(orderRequestNo,productCode,orderAmount,uniqueId,uniqueId,
                firstRepaymentNumber,secondRepaymentNumber,firstRepaymentWay,secondRepaymentWay,
                firstDetailTotalAmount,secondDetailTotalAmount,firstDetailPrincipal,firstDetailPrincipal,
                firstDetailInterest,firstDetailInterest,firstFeeType,firstFeeType);


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
        Assert.assertEquals(RepaymentStatus.VERIFICATION_SUCCESS,repaymentStatus);
    }


    /**
     * 冒烟测试
     * 还款订单，同一合同下两笔还款计划的线上代扣订单
     * @throws Exception
     */
    @Test
    public void testOrderDeductedOnline() throws Exception{
        String orderRequestNo = uniqueId;
        String orderAmount = "20140";
        Long firstRepaymentWay = 1000l;
        Long secondRepaymentWay = 1000l;
        String firstDetailTotalAmount = "10080";
        String secondDetailTotalAmount = "10060";
        String firstDetailPrincipal = "10000";
        String firstDetailInterest = "20";
        Long firstFeeType = 1l;

        String result = refactorMethod.applyRepaymentOderForTwoDetails(orderRequestNo,productCode,orderAmount,uniqueId,uniqueId,
                firstRepaymentNumber,secondRepaymentNumber,firstRepaymentWay,secondRepaymentWay,
                firstDetailTotalAmount,secondDetailTotalAmount,firstDetailPrincipal,firstDetailPrincipal,
                firstDetailInterest,firstDetailInterest,firstFeeType,firstFeeType);


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
        Assert.assertEquals(RepaymentStatus.VERIFICATION_SUCCESS,repaymentStatus);
        
       
    }


    /**
     * 冒烟测试
     * 还款订单，同一合同下两笔还款计划的在线支付订单
     * @throws Exception
     */
    @Test
    public void testOrderPayOnline() throws Exception{
            String orderRequestNo = uniqueId;
            String orderAmount = "20140";
            Long firstRepaymentWay = 6001l;
            Long secondRepaymentWay = 6001l;
            String firstDetailTotalAmount = "10080";
            String secondDetailTotalAmount = "10060";
            String firstDetailPrincipal = "10000";
            String firstDetailInterest = "20";
            Long firstFeeType = 1l;

            String result = refactorMethod.applyRepaymentOderForTwoDetails(orderRequestNo,productCode,orderAmount,uniqueId,uniqueId,
                    firstRepaymentNumber,secondRepaymentNumber,firstRepaymentWay,secondRepaymentWay,
                    firstDetailTotalAmount,secondDetailTotalAmount,firstDetailPrincipal,firstDetailPrincipal,
                    firstDetailInterest,firstDetailInterest,firstFeeType,firstFeeType);


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
            Assert.assertEquals(RepaymentStatus.VERIFICATION_SUCCESS,repaymentStatus);
        }


    /**
     * 冒烟测试
     * 还款订单，同一合同下两笔还款计划的商户代扣订单
     * @throws Exception
     */
    @Test
    public void testOrderPayByBusiness() throws Exception{
        String orderRequestNo = uniqueId;
        String orderAmount = "20140";
        Long firstRepaymentWay = 4001l;
        Long secondRepaymentWay = 4001l;
        String firstDetailTotalAmount = "10080";
        String secondDetailTotalAmount = "10060";
        String firstDetailPrincipal = "10000";
        String firstDetailInterest = "20";
        Long firstFeeType = 1l;

        String result = refactorMethod.applyRepaymentOderForTwoDetails(orderRequestNo,productCode,orderAmount,uniqueId,uniqueId,
                firstRepaymentNumber,secondRepaymentNumber,firstRepaymentWay,secondRepaymentWay,
                firstDetailTotalAmount,secondDetailTotalAmount,firstDetailPrincipal,firstDetailPrincipal,
                firstDetailInterest,firstDetailInterest,firstFeeType,firstFeeType);


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
        Assert.assertEquals(RepaymentStatus.VERIFICATION_SUCCESS,repaymentStatus);
    }


    /**
     * 冒烟测试
     * 还款订单，同一合同下两笔还款计划的快捷支付订单
     * @throws Exception
     */
    @Test
    public void testOrderQuickPayment() throws Exception{
        String orderRequestNo = uniqueId;
        String orderAmount = "20140";
        Long firstRepaymentWay = 5001l;
        Long secondRepaymentWay = 5001l;
        String firstDetailTotalAmount = "10080";
        String secondDetailTotalAmount = "10060";
        String firstDetailPrincipal = "10000";
        String firstDetailInterest = "20";
        Long firstFeeType = 1l;

        String result = refactorMethod.applyRepaymentOderForTwoDetails(orderRequestNo,productCode,orderAmount,uniqueId,uniqueId,
                firstRepaymentNumber,secondRepaymentNumber,firstRepaymentWay,secondRepaymentWay,
                firstDetailTotalAmount,secondDetailTotalAmount,firstDetailPrincipal,firstDetailPrincipal,
                firstDetailInterest,firstDetailInterest,firstFeeType,firstFeeType);


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

            if (repaymentStatus != RepaymentStatus.TO_VERIFY && repaymentStatus != RepaymentStatus.VERIFICATION_IN_PROCESS
                    && repaymentStatus != RepaymentStatus.VERIFICATION_SUCCESS && repaymentStatus != RepaymentStatus.VERIFICATION_FAILURE) {
                break;
            }
            Thread.sleep(5000l);
        }
        Assert.assertEquals(RepaymentStatus.PAYMENT_IN_PROCESS,repaymentStatus);
    }

    /**
     * 冒烟测试
     * 还款订单，线上代扣-预收
     * 还款计划编号firstRepaymentNumber为空字符串
     * @throws Exception
     */
    @Test
    public void testOrderDeductedOnlineInAdvance() throws Exception{
        String orderRequestNo = uniqueId;
        String transType = "0";
        String orderAmount = "10080";
        Long firstRepaymentWay = 1000l;
        String firstDetailTotalAmount = "10080";
        String firstDetailPrincipal = "10000";
        String firstDetailInterest = "20";
        Long firstFeeType = 1l;
        String repayScheduleNo = "";

        //进预收，还款计划编号firstRepaymentNumber为空字符串
        String result = refactorMethod.applyRepaymentOderForOneDetail(orderRequestNo,orderRequestNo,transType,productCode,orderAmount,uniqueId,
                                                                                    "",firstRepaymentWay,
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
        Assert.assertEquals(RepaymentStatus.VERIFICATION_SUCCESS,repaymentStatus);
    }

    /**
     * 冒烟测试
     * 还款订单，线上代扣-预收
     * 还款计划编号firstRepaymentNumber为null
     * @throws Exception
     */
    @Test
    public void testOrderDeductedOnlineInAdvance2() throws Exception{
        String orderRequestNo = uniqueId;
        String transType = "0";
        String orderAmount = "10080";
        Long firstRepaymentWay = 1000l;
        String firstDetailTotalAmount = "10080";
        String firstDetailPrincipal = "10000";
        String firstDetailInterest = "20";
        Long firstFeeType = 1l;
        String repayScheduleNo = "";

        //进预收，还款计划编号firstRepaymentNumber为null
        String result = refactorMethod.applyRepaymentOderForOneDetail(orderRequestNo,orderRequestNo,transType,productCode,orderAmount,uniqueId,
                                                                                null,firstRepaymentWay,
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
        Assert.assertEquals(RepaymentStatus.VERIFICATION_SUCCESS,repaymentStatus);
    }


    /**
     * 冒烟测试
     * 还款订单，快捷支付-预收
     * 还款计划编号firstRepaymentNumber为null
     * @throws Exception
     */
    @Test
    public void testOrderQuickPaymentInAdvance() throws Exception{
        String orderRequestNo = uniqueId;
        String transType = "0";
        String orderAmount = "10080";
        Long firstRepaymentWay = 5001l;
        String firstDetailTotalAmount = "10080";
        String firstDetailPrincipal = "10000";
        String firstDetailInterest = "20";
        Long firstFeeType = 1l;
        String repayScheduleNo = "";

        //进预收，还款计划编号firstRepaymentNumber为null
        String result = refactorMethod.applyRepaymentOderForOneDetail(orderRequestNo,orderRequestNo,transType,productCode,orderAmount,uniqueId,
                                                                                null,firstRepaymentWay,
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

            if (repaymentStatus != RepaymentStatus.TO_VERIFY && repaymentStatus != RepaymentStatus.VERIFICATION_IN_PROCESS
                    && repaymentStatus != RepaymentStatus.VERIFICATION_SUCCESS && repaymentStatus != RepaymentStatus.VERIFICATION_FAILURE) {
                break;
            }
            Thread.sleep(5000l);
        }
        Assert.assertEquals(RepaymentStatus.PAYMENT_IN_PROCESS,repaymentStatus);
    }
}
