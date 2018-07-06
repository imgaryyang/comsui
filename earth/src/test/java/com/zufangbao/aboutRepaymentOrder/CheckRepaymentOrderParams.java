package com.zufangbao.aboutRepaymentOrder;

import com.demo2do.core.utils.DateUtils;
import com.zufangbao.refactor.method.RefactorMethod;
import com.zufangbao.sun.entity.repayment.order.RepaymentOrder;
import com.zufangbao.sun.entity.repayment.order.RepaymentStatus;
import com.zufangbao.sun.service.CashFlowService;
import com.zufangbao.sun.service.RepaymentOrderService;
import com.zufangbao.sun.service.RepurchaseService;
import com.zufangbao.sun.yunxin.entity.AssetSet;
import com.zufangbao.sun.yunxin.service.RepaymentPlanService;
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
 * 还款订单参数校验
 */
public class CheckRepaymentOrderParams {
    @Autowired
    private CashFlowService cashFlowService;

    @Autowired
    private RepaymentOrderService repaymentOrderService;

    @Autowired
    private RepurchaseService repurchaseService;

    @Autowired
    private RepaymentPlanService repaymentPlanService;

    RefactorMethod refactorMethod = new RefactorMethod();

    private String firstUniqueId = "";
    String productCode = "CS0001";
    private String firstRepaymentNumber = "";
    @Before
    public void setUp(){
        refactorMethod.deleteAllCashFlow();
        String totalAmount = "30000";
        firstUniqueId = UUID.randomUUID().toString() + 1;
        String repaymentAccountNo = "6217857600016839330";
        String loanCustomerName = "秦操";
        String interest = "20";
        String firstLoanCustomerNo = "11111117";
        String firstPlanDate = DateUtils.format(DateUtils.addMonths(new Date(), -1));
        String secondPlanDate = DateUtils.format(new Date());
        String thirdPlanDate = DateUtils.format(DateUtils.addMonths(new Date(), 1));

        refactorMethod.importAssetPackage(totalAmount,productCode,firstUniqueId,repaymentAccountNo,interest,firstLoanCustomerNo,
                firstPlanDate,secondPlanDate,thirdPlanDate,loanCustomerName);

        firstRepaymentNumber = refactorMethod.queryRepaymentPlan(productCode,firstUniqueId,0);
        System.out.println(firstRepaymentNumber);

    }

    /**
     * 还款订单参数校验--orderRequestNo重复
     * @throws Exception
     */
    @Test
    public void checkParams1() throws Exception{
        String orderRequestNo = firstUniqueId;
        String orderUniqueId = orderRequestNo + 1;
        String transType = "0";
        String orderAmount = "5040";
        Long firstRepaymentWay = 1000l;
        String firstDetailTotalAmount = "5040";
        String firstDetailPrincipal = "5000";
        String firstDetailInterest = "10";
        Long firstFeeType = 1l;
        String repayScheduleNo = "";

        String result = refactorMethod.applyRepaymentOderForOneDetail(orderRequestNo,orderUniqueId,transType,productCode,orderAmount,firstUniqueId,
                firstRepaymentNumber,firstRepaymentWay,
                firstDetailTotalAmount,firstDetailPrincipal,
                firstDetailInterest,firstFeeType,repayScheduleNo);

        if(!result.contains("订单已受理")){
            throw new Exception("还款订单校验错误!");
        }

        String result2 = refactorMethod.applyRepaymentOderForOneDetail(orderRequestNo,orderUniqueId+1,transType,productCode,orderAmount,firstUniqueId,
                firstRepaymentNumber,firstRepaymentWay,
                firstDetailTotalAmount,firstDetailPrincipal,
                firstDetailInterest,firstFeeType,repayScheduleNo);

        boolean outcome = result2.contains("请求编号重复");

        Assert.assertEquals(true,outcome);
    }

    /**
     * 还款订单参数校验--orderRequestNo为空
     * @throws Exception
     */
    @Test
    public void checkParams2() throws Exception{
        String orderRequestNo = "";
        String orderUniqueId = orderRequestNo + 1;
        String transType = "0";
        String orderAmount = "5040";
        Long firstRepaymentWay = 1000l;
        String firstDetailTotalAmount = "5040";
        String firstDetailPrincipal = "5000";
        String firstDetailInterest = "10";
        Long firstFeeType = 1l;
        String repayScheduleNo = "";

        String result = refactorMethod.applyRepaymentOderForOneDetail(orderRequestNo,orderUniqueId,transType,productCode,orderAmount,firstUniqueId,
                firstRepaymentNumber,firstRepaymentWay,
                firstDetailTotalAmount,firstDetailPrincipal,
                firstDetailInterest,firstFeeType,repayScheduleNo);

        boolean outcome = result.contains("订单请求编号[orderRquestNo], 不能为空");

        Assert.assertEquals(true,outcome);
    }


    /**
     * 还款订单参数校验--orderUniqueId为空
     * @throws Exception
     */
    @Test
    public void checkParams3() throws Exception{
        String orderRequestNo = firstUniqueId;
        String orderUniqueId = "";
        String transType = "0";
        String orderAmount = "5040";
        Long firstRepaymentWay = 1000l;
        String firstDetailTotalAmount = "5040";
        String firstDetailPrincipal = "5000";
        String firstDetailInterest = "10";
        Long firstFeeType = 1l;
        String repayScheduleNo = "";

        String result = refactorMethod.applyRepaymentOderForOneDetail(orderRequestNo,orderUniqueId,transType,productCode,orderAmount,firstUniqueId,
                firstRepaymentNumber,firstRepaymentWay,
                firstDetailTotalAmount,firstDetailPrincipal,
                firstDetailInterest,firstFeeType,repayScheduleNo);

        boolean outcome = result.contains("商户订单号［orderUniqueId],不能为空");

        Assert.assertEquals(true,outcome);
    }

    /**
     * 还款订单参数校验--orderUniqueId重复
     * @throws Exception
     */
    @Test
    public void checkParams4() throws Exception{
        String orderRequestNo = firstUniqueId;
        String orderUniqueId = orderRequestNo + 1;
        String transType = "0";
        String orderAmount = "5040";
        Long firstRepaymentWay = 1000l;
        String firstDetailTotalAmount = "5040";
        String firstDetailPrincipal = "5000";
        String firstDetailInterest = "10";
        Long firstFeeType = 1l;
        String repayScheduleNo = "";

        String result = refactorMethod.applyRepaymentOderForOneDetail(orderRequestNo,orderUniqueId,transType,productCode,orderAmount,firstUniqueId,
                firstRepaymentNumber,firstRepaymentWay,
                firstDetailTotalAmount,firstDetailPrincipal,
                firstDetailInterest,firstFeeType,repayScheduleNo);

        if(!result.contains("订单已受理")){
            throw new Exception("还款订单校验错误!");
        }

        String result2 = refactorMethod.applyRepaymentOderForOneDetail(orderRequestNo+1,orderUniqueId,transType,productCode,orderAmount,firstUniqueId,
                firstRepaymentNumber,firstRepaymentWay,
                firstDetailTotalAmount,firstDetailPrincipal,
                firstDetailInterest,firstFeeType,repayScheduleNo);

        boolean outcome = result2.contains("存在相应的还款订单");

        Assert.assertEquals(true,outcome);
    }


    /**
     * 还款订单正常撤销
     * @throws Exception
     */
    @Test
    public void checkParams5() throws Exception{
        String orderRequestNo = firstUniqueId;
        String transType = "0";
        String transType2 = "1";
        String orderAmount = "10080";
        Long firstRepaymentWay = 1000l;
        String firstDetailTotalAmount = "10080";
        String firstDetailPrincipal = "10000";
        String firstDetailInterest = "20";
        Long firstFeeType = 1l;
        String repayScheduleNo = "";

        String result = refactorMethod.applyRepaymentOderForOneDetail(orderRequestNo,orderRequestNo,transType,productCode,orderAmount,firstUniqueId,
                firstRepaymentNumber,firstRepaymentWay,
                firstDetailTotalAmount,firstDetailPrincipal,
                firstDetailInterest,firstFeeType,repayScheduleNo);


        if(!result.contains("订单已受理")){
            throw new Exception("还款订单校验错误!");
        }
        RepaymentStatus repaymentStatus;
        while (true) {
            RepaymentOrder repaymentOrder = repaymentOrderService.getRepaymentOrderByUniqueId(firstUniqueId, productCode);
            if (null == repaymentOrder) {
                throw new Exception("还款订单不存在!");
            }
            repaymentStatus = repaymentOrder.transferToRepaymentStatus();

            if (repaymentStatus != RepaymentStatus.TO_VERIFY && repaymentStatus != RepaymentStatus.VERIFICATION_IN_PROCESS) {
                break;
            }
            Thread.sleep(5000l);
        }
        if(repaymentStatus != RepaymentStatus.VERIFICATION_SUCCESS){
            throw new Exception("还款订单状态有误");
        }

        String result2 = refactorMethod.applyRepaymentOderForOneDetail(orderRequestNo,orderRequestNo,transType2,productCode,orderAmount,firstUniqueId,
                firstRepaymentNumber,firstRepaymentWay,
                firstDetailTotalAmount,firstDetailPrincipal,
                firstDetailInterest,firstFeeType,repayScheduleNo);

        if(!result2.contains("订单撤销已受理")){
            throw new Exception("还款订单撤销失败");
        }

        while (true) {
            RepaymentOrder repaymentOrder = repaymentOrderService.getRepaymentOrderByUniqueId(firstUniqueId, productCode);
            if (null == repaymentOrder) {
                throw new Exception("还款订单不存在!");
            }
            repaymentStatus = repaymentOrder.transferToRepaymentStatus();

            if (repaymentStatus != RepaymentStatus.VERIFICATION_SUCCESS) {
                break;
            }
            Thread.sleep(5000l);
            }

            Assert.assertEquals(RepaymentStatus.PAYMENT_CANCEL,repaymentStatus);
    }


    /**
     * 撤销不存在的还款订单
     * @throws Exception
     */
    @Test
    public void checkParams6() throws Exception{
        String orderRequestNo = firstUniqueId;
        String transType2 = "1";
        String orderAmount = "10080";
        Long firstRepaymentWay = 1000l;
        String firstDetailTotalAmount = "10080";
        String firstDetailPrincipal = "10000";
        String firstDetailInterest = "20";
        Long firstFeeType = 1l;
        String repayScheduleNo = "";

        String result = refactorMethod.applyRepaymentOderForOneDetail(orderRequestNo,orderRequestNo,transType2,productCode,orderAmount,firstUniqueId,
                firstRepaymentNumber,firstRepaymentWay,
                firstDetailTotalAmount,firstDetailPrincipal,
                firstDetailInterest,firstFeeType,repayScheduleNo);

        boolean outcome = result.contains("相应的还款订单不存在");

        Assert.assertEquals(true,outcome);
    }


    /**
     * 撤销校验中的还款订单
     * @throws Exception
     */
    @Test
    public void checkParams7() throws Exception{
        String orderRequestNo = firstUniqueId;
        String transType = "0";
        String transType2 = "1";
        String orderAmount = "10080";
        Long firstRepaymentWay = 2001l;
        String firstDetailTotalAmount = "10080";
        String firstDetailPrincipal = "10000";
        String firstDetailInterest = "20";
        Long firstFeeType = 1l;
        String repayScheduleNo = "";

        String result = refactorMethod.applyRepaymentOderForOneDetail(orderRequestNo,orderRequestNo,transType,productCode,orderAmount,firstUniqueId,
                firstRepaymentNumber,firstRepaymentWay,
                firstDetailTotalAmount,firstDetailPrincipal,
                firstDetailInterest,firstFeeType,repayScheduleNo);


        if(!result.contains("订单已受理")){
            throw new Exception("还款订单校验错误!");
        }
        RepaymentStatus repaymentStatus;
        while (true) {
            RepaymentOrder repaymentOrder = repaymentOrderService.getRepaymentOrderByUniqueId(firstUniqueId, productCode);
            if (null == repaymentOrder) {
                throw new Exception("还款订单不存在!");
            }
            repaymentStatus = repaymentOrder.transferToRepaymentStatus();

            if (repaymentStatus != RepaymentStatus.TO_VERIFY) {
                break;
            }
            Thread.sleep(1000l);
        }
        if(repaymentStatus != RepaymentStatus.VERIFICATION_IN_PROCESS){
            throw new Exception("还款订单状态有误");
        }

        String result2 = refactorMethod.applyRepaymentOderForOneDetail(orderRequestNo,orderRequestNo,transType2,productCode,orderAmount,firstUniqueId,
                firstRepaymentNumber,firstRepaymentWay,
                firstDetailTotalAmount,firstDetailPrincipal,
                firstDetailInterest,firstFeeType,repayScheduleNo);

        boolean outcome = result2.contains("还款订单不允许该操作");

        Assert.assertEquals(true,outcome);

    }


    /**
     * 撤销校验失败的还款订单
     * @throws Exception
     */
    @Test
    public void checkParams8() throws Exception{
        String orderRequestNo = firstUniqueId;
        String transType = "0";
        String transType2 = "1";
        String orderAmount = "10120";
        Long firstRepaymentWay = 1000l;
        String firstDetailTotalAmount = "10120";
        String firstDetailPrincipal = "10000";
        String firstDetailInterest = "30";
        Long firstFeeType = 1l;
        String repayScheduleNo = "";

        String result = refactorMethod.applyRepaymentOderForOneDetail(orderRequestNo,orderRequestNo,transType,productCode,orderAmount,firstUniqueId,
                firstRepaymentNumber,firstRepaymentWay,
                firstDetailTotalAmount,firstDetailPrincipal,
                firstDetailInterest,firstFeeType,repayScheduleNo);


        if(!result.contains("订单已受理")){
            throw new Exception("还款订单校验错误!");
        }
        RepaymentStatus repaymentStatus;
        while (true) {
            RepaymentOrder repaymentOrder = repaymentOrderService.getRepaymentOrderByUniqueId(firstUniqueId, productCode);
            if (null == repaymentOrder) {
                throw new Exception("还款订单不存在!");
            }
            repaymentStatus = repaymentOrder.transferToRepaymentStatus();

            if (repaymentStatus != RepaymentStatus.TO_VERIFY && repaymentStatus != RepaymentStatus.VERIFICATION_IN_PROCESS) {
                break;
            }
            Thread.sleep(5000l);
        }
        if(repaymentStatus != RepaymentStatus.VERIFICATION_FAILURE){
            throw new Exception("还款订单状态有误");
        }

        String result2 = refactorMethod.applyRepaymentOderForOneDetail(orderRequestNo,orderRequestNo,transType2,productCode,orderAmount,firstUniqueId,
                firstRepaymentNumber,firstRepaymentWay,
                firstDetailTotalAmount,firstDetailPrincipal,
                firstDetailInterest,firstFeeType,repayScheduleNo);


        boolean outcome = result2.contains("还款订单不允许该操作");

        Assert.assertEquals(true,outcome);

    }


    /**
     * 还款订单参数校验--productCode为空
     * @throws Exception
     */
    @Test
    public void checkParams9() throws Exception{
        productCode = "";
        String orderRequestNo = firstUniqueId;
        String transType = "0";
        String orderAmount = "10080";
        Long firstRepaymentWay = 1000l;
        String firstDetailTotalAmount = "10080";
        String firstDetailPrincipal = "10000";
        String firstDetailInterest = "20";
        Long firstFeeType = 1l;
        String repayScheduleNo = "";

        String result = refactorMethod.applyRepaymentOderForOneDetail(orderRequestNo,orderRequestNo,transType,productCode,orderAmount,firstUniqueId,
                firstRepaymentNumber,firstRepaymentWay,
                firstDetailTotalAmount,firstDetailPrincipal,
                firstDetailInterest,firstFeeType,repayScheduleNo);

        boolean outcome = result.contains("信托合同编号［financialContractNo],不能为空");

        Assert.assertEquals(true,outcome);
    }


    /**
     * 还款订单参数校验--productCode为不存在
     * @throws Exception
     */
    @Test
    public void checkParams10() throws Exception{
        productCode = productCode + 1;
        String orderRequestNo = firstUniqueId;
        String transType = "0";
        String orderAmount = "10080";
        Long firstRepaymentWay = 1000l;
        String firstDetailTotalAmount = "10080";
        String firstDetailPrincipal = "10000";
        String firstDetailInterest = "20";
        Long firstFeeType = 1l;
        String repayScheduleNo = "";

        String result = refactorMethod.applyRepaymentOderForOneDetail(orderRequestNo,orderRequestNo,transType,productCode,orderAmount,firstUniqueId,
                firstRepaymentNumber,firstRepaymentWay,
                firstDetailTotalAmount,firstDetailPrincipal,
                firstDetailInterest,firstFeeType,repayScheduleNo);

        boolean outcome = result.contains("信托合同不存在");

        Assert.assertEquals(true,outcome);
    }

    /**
     * 还款订单参数校验--orderAmount为空
     * @throws Exception
     */
    @Test
    public void checkParams11() throws Exception{
        String orderRequestNo = firstUniqueId;
        String transType = "0";
        String orderAmount = "";
        Long firstRepaymentWay = 1000l;
        String firstDetailTotalAmount = "10080";
        String firstDetailPrincipal = "10000";
        String firstDetailInterest = "20";
        Long firstFeeType = 1l;
        String repayScheduleNo = "";

        String result = refactorMethod.applyRepaymentOderForOneDetail(orderRequestNo,orderRequestNo,transType,productCode,orderAmount,firstUniqueId,
                firstRepaymentNumber,firstRepaymentWay,
                firstDetailTotalAmount,firstDetailPrincipal,
                firstDetailInterest,firstFeeType,repayScheduleNo);

        boolean outcome = result.contains("订单总金额[orderAmount],不能为空");

        Assert.assertEquals(true,outcome);
    }


    /**
     * 还款订单参数校验--orderAmount为0
     * @throws Exception
     */
    @Test
    public void checkParams12() throws Exception{
        String orderRequestNo = firstUniqueId;
        String transType = "0";
        String orderAmount = "0";
        Long firstRepaymentWay = 1000l;
        String firstDetailTotalAmount = "10080";
        String firstDetailPrincipal = "10000";
        String firstDetailInterest = "20";
        Long firstFeeType = 1l;
        String repayScheduleNo = "";

        String result = refactorMethod.applyRepaymentOderForOneDetail(orderRequestNo,orderRequestNo,transType,productCode,orderAmount,firstUniqueId,
                firstRepaymentNumber,firstRepaymentWay,
                firstDetailTotalAmount,firstDetailPrincipal,
                firstDetailInterest,firstFeeType,repayScheduleNo);

        boolean outcome = result.contains("金额需高于0.00");

        Assert.assertEquals(true,outcome);
    }

    /**
     * 还款订单参数校验--orderAmount为负数
     * @throws Exception
     */
    @Test
    public void checkParams13() throws Exception{
        String orderRequestNo = firstUniqueId;
        String transType = "0";
        String orderAmount = "-1";
        Long firstRepaymentWay = 1000l;
        String firstDetailTotalAmount = "10080";
        String firstDetailPrincipal = "10000";
        String firstDetailInterest = "20";
        Long firstFeeType = 1l;
        String repayScheduleNo = "";

        String result = refactorMethod.applyRepaymentOderForOneDetail(orderRequestNo,orderRequestNo,transType,productCode,orderAmount,firstUniqueId,
                firstRepaymentNumber,firstRepaymentWay,
                firstDetailTotalAmount,firstDetailPrincipal,
                firstDetailInterest,firstFeeType,repayScheduleNo);

        boolean outcome = result.contains("金额需高于0.00");

        Assert.assertEquals(true,outcome);
    }


    /**
     * 还款订单参数校验--orderAmount ！= detailTotalAmount
     * @throws Exception
     */
    @Test
    public void checkParams14() throws Exception{
        String orderRequestNo = firstUniqueId;
        String transType = "0";
        String orderAmount = "10000";
        Long firstRepaymentWay = 1000l;
        String firstDetailTotalAmount = "10080";
        String firstDetailPrincipal = "10000";
        String firstDetailInterest = "20";
        Long firstFeeType = 1l;
        String repayScheduleNo = "";

        String result = refactorMethod.applyRepaymentOderForOneDetail(orderRequestNo,orderRequestNo,transType,productCode,orderAmount,firstUniqueId,
                firstRepaymentNumber,firstRepaymentWay,
                firstDetailTotalAmount,firstDetailPrincipal,
                firstDetailInterest,firstFeeType,repayScheduleNo);

        boolean outcome = result.contains("订单总金额不等于明细金额总和");

        Assert.assertEquals(true,outcome);
    }


    /**
     * 还款订单参数校验，明细金额累加与明细总额不相等
     * @throws Exception
     */
    @Test
    public void checkParams15() throws Exception{
        String orderRequestNo = firstUniqueId;
        String transType = "0";
        String orderAmount = "10080";
        Long firstRepaymentWay = 1000l;
        String firstDetailTotalAmount = "10080";
        String firstDetailPrincipal = "10000";
        String firstDetailInterest = "10";
        Long firstFeeType = 1l;
        String repayScheduleNo = "";

        String result = refactorMethod.applyRepaymentOderForOneDetail(orderRequestNo,orderRequestNo,transType,productCode,orderAmount,firstUniqueId,
                firstRepaymentNumber,firstRepaymentWay,
                firstDetailTotalAmount,firstDetailPrincipal,
                firstDetailInterest,firstFeeType,repayScheduleNo);

        if(!result.contains("订单已受理")){
            throw new Exception("还款订单校验错误!");
        }
        RepaymentStatus repaymentStatus;
        while (true) {
            RepaymentOrder repaymentOrder = repaymentOrderService.getRepaymentOrderByUniqueId(firstUniqueId, productCode);
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
     * 还款订单参数校验，contractUniqueId和contractNo都为空
     * @throws Exception
     */
    @Test
    public void checkParams16() throws Exception{
        String orderRequestNo = firstUniqueId;
        String transType = "0";
        String orderAmount = "5040";
        Long firstRepaymentWay = 1000l;
        String firstDetailTotalAmount = "5040";
        String firstDetailPrincipal = "5000";
        String firstDetailInterest = "10";
        Long firstFeeType = 1l;
        String repayScheduleNo = "";

        String result = refactorMethod.applyRepaymentOderForOneDetail(orderRequestNo,orderRequestNo,transType,productCode,orderAmount,"",
                firstRepaymentNumber,firstRepaymentWay,
                firstDetailTotalAmount,firstDetailPrincipal,
                firstDetailInterest,firstFeeType,repayScheduleNo);

        boolean outcome = result.contains("贷款合同唯一编号［uniqueId]和贷款合同编号[contractNo]，不能都为空");

        Assert.assertEquals(true,outcome);
    }


    /**
     * 还款订单参数校验，该contractUniqueId不在该productCode下
     * @throws Exception
     */
    @Test
    public void checkParams17() throws Exception{
        String orderRequestNo = firstUniqueId;
        productCode = "G31700";
        String transType = "0";
        String orderAmount = "10080";
        Long firstRepaymentWay = 1000l;
        String firstDetailTotalAmount = "10080";
        String firstDetailPrincipal = "10000";
        String firstDetailInterest = "20";
        Long firstFeeType = 1l;
        String repayScheduleNo = "";

        String result = refactorMethod.applyRepaymentOderForOneDetail(orderRequestNo,orderRequestNo,transType,productCode,orderAmount,firstUniqueId,
                firstRepaymentNumber,firstRepaymentWay,
                firstDetailTotalAmount,firstDetailPrincipal,
                firstDetailInterest,firstFeeType,repayScheduleNo);

        if(!result.contains("订单已受理")){
            throw new Exception("还款订单校验错误!");
        }
        RepaymentStatus repaymentStatus;
        while (true) {
            RepaymentOrder repaymentOrder = repaymentOrderService.getRepaymentOrderByUniqueId(firstUniqueId, productCode);
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
     * 还款订单参数校验，填入不存在的还款方式repaymentWay
     * @throws Exception
     */
    @Test
    public void checkParams18() throws Exception{
        String orderRequestNo = firstUniqueId;
        String transType = "0";
        String orderAmount = "10080";
        Long firstRepaymentWay = 1001l;
        String firstDetailTotalAmount = "10080";
        String firstDetailPrincipal = "10000";
        String firstDetailInterest = "20";
        Long firstFeeType = 1l;
        String repayScheduleNo = "";

        String result = refactorMethod.applyRepaymentOderForOneDetail(orderRequestNo,orderRequestNo,transType,productCode,orderAmount,firstUniqueId,
                firstRepaymentNumber,firstRepaymentWay,
                firstDetailTotalAmount,firstDetailPrincipal,
                firstDetailInterest,firstFeeType,repayScheduleNo);

        if(!result.contains("订单已受理")){
            throw new Exception("还款订单校验错误!");
        }
        RepaymentStatus repaymentStatus;
        while (true) {
            RepaymentOrder repaymentOrder = repaymentOrderService.getRepaymentOrderByUniqueId(firstUniqueId, productCode);
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
     * 还款订单参数校验，非线上代扣和快捷支付，还款计划编号repaymentNumber为空
     * @throws Exception
     */
    @Test
    public void checkParams19() throws Exception{
        String orderRequestNo = firstUniqueId;
        String transType = "0";
        String orderAmount = "10080";
        firstRepaymentNumber = "";
        Long firstRepaymentWay = 2001l;
        String firstDetailTotalAmount = "10080";
        String firstDetailPrincipal = "10000";
        String firstDetailInterest = "20";
        Long firstFeeType = 1l;
        String repayScheduleNo = "";

        String result = refactorMethod.applyRepaymentOderForOneDetail(orderRequestNo,orderRequestNo,transType,productCode,orderAmount,firstUniqueId,
                firstRepaymentNumber,firstRepaymentWay,
                firstDetailTotalAmount,firstDetailPrincipal,
                firstDetailInterest,firstFeeType,repayScheduleNo);

        if(!result.contains("订单已受理")){
            throw new Exception("还款订单校验错误!");
        }
        RepaymentStatus repaymentStatus;
        while (true) {
            RepaymentOrder repaymentOrder = repaymentOrderService.getRepaymentOrderByUniqueId(firstUniqueId, productCode);
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
     * 还款订单参数校验,填入的还款计划编号repaymentNumber不存在
     * @throws Exception
     */
    @Test
    public void checkParams20() throws Exception{
        String orderRequestNo = firstUniqueId;
        String transType = "0";
        String orderAmount = "10080";
        firstRepaymentNumber = firstRepaymentNumber + 1;
        Long firstRepaymentWay = 1000l;
        String firstDetailTotalAmount = "10080";
        String firstDetailPrincipal = "10000";
        String firstDetailInterest = "20";
        Long firstFeeType = 1l;
        String repayScheduleNo = "";

        String result = refactorMethod.applyRepaymentOderForOneDetail(orderRequestNo,orderRequestNo,transType,productCode,orderAmount,firstUniqueId,
                firstRepaymentNumber,firstRepaymentWay,
                firstDetailTotalAmount,firstDetailPrincipal,
                firstDetailInterest,firstFeeType,repayScheduleNo);

        if(!result.contains("订单已受理")){
            throw new Exception("还款订单校验错误!");
        }
        RepaymentStatus repaymentStatus;
        while (true) {
            RepaymentOrder repaymentOrder = repaymentOrderService.getRepaymentOrderByUniqueId(firstUniqueId, productCode);
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
     * 还款订单参数校验,填入的还款计划编号repaymentNumber不在该贷款合同下
     * @throws Exception
     */
    @Test
    public void checkParams21() throws Exception{
        String orderRequestNo = firstUniqueId;
        String transType = "0";
        String orderAmount = "10080";
        Long firstRepaymentWay = 1000l;
        String firstDetailTotalAmount = "10080";
        String firstDetailPrincipal = "10000";
        String firstDetailInterest = "20";
        Long firstFeeType = 1l;
        String repayScheduleNo = "";


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

        String secondRepaymentNumber = refactorMethod.queryRepaymentPlan(productCode,secondUniqueId,0);

        String result = refactorMethod.applyRepaymentOderForOneDetail(orderRequestNo,orderRequestNo,transType,productCode,orderAmount,firstUniqueId,
                secondRepaymentNumber,firstRepaymentWay,
                firstDetailTotalAmount,firstDetailPrincipal,
                firstDetailInterest,firstFeeType,repayScheduleNo);

        if(!result.contains("订单已受理")){
            throw new Exception("还款订单校验错误!");
        }
        RepaymentStatus repaymentStatus;
        while (true) {
            RepaymentOrder repaymentOrder = repaymentOrderService.getRepaymentOrderByUniqueId(firstUniqueId, productCode);
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
     * 还款订单参数校验,还款类型repaymentWay与费用类型feeType不匹配
     * @throws Exception
     */
    @Test
    public void checkParams22() throws Exception{

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
        Long secondFeeType = 1l;

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
        Assert.assertEquals(RepaymentStatus.VERIFICATION_FAILURE,repaymentStatus);

    }


    /**
     * 还款订单参数校验,回购单号不存在
     * @throws Exception
     */
    @Test
    public void checkParams23() throws Exception{

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


        refactorMethod.applyRepurchase(productCode,secondUniqueId,secontDetailPrincipal,firstDetailInterest,secondDetailTotalAmount);

        String repurchaseDocUuid = repurchaseService.getRepurchaseDocUuidBy(secondUniqueId);
        if (null == repurchaseDocUuid || "".equals(repurchaseDocUuid)){
            throw new Exception("回购单不存在");
        }

        String result = refactorMethod.applyRepaymentOderForTwoDetails(orderRequestNo,productCode,orderAmount,firstUniqueId,secondUniqueId,
                firstRepaymentNumber,repurchaseDocUuid+1,firstRepaymentWay,secondRepaymentWay,
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
     * 还款订单参数校验，回购单不在该贷款合同下
     * @throws Exception
     */
    @Test
    public void checkParams24() throws Exception{

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

        String totalAmount = "30000";
        String secondUniqueId = UUID.randomUUID().toString();
        String thirdUniqueId = UUID.randomUUID().toString();
        String repaymentAccountNo = "6217857600016839330";
        String loanCustomerName = "秦操";
        String interest = "20";
        String firstLoanCustomerNo = "11111117";
        String firstPlanDate = DateUtils.format(DateUtils.addMonths(new Date(), -1));
        String secondPlanDate = DateUtils.format(new Date());
        String thirdPlanDate = DateUtils.format(DateUtils.addMonths(new Date(), 1));

        refactorMethod.importAssetPackage(totalAmount,productCode,secondUniqueId,repaymentAccountNo,interest,firstLoanCustomerNo,
                firstPlanDate,secondPlanDate,thirdPlanDate,loanCustomerName);

        refactorMethod.importAssetPackage(totalAmount,productCode,thirdUniqueId,repaymentAccountNo,interest,firstLoanCustomerNo,
                firstPlanDate,secondPlanDate,thirdPlanDate,loanCustomerName);


        refactorMethod.applyRepurchase(productCode,secondUniqueId,secontDetailPrincipal,firstDetailInterest,secondDetailTotalAmount);

        refactorMethod.applyRepurchase(productCode,thirdUniqueId,secontDetailPrincipal,firstDetailInterest,secondDetailTotalAmount);

        String repurchaseDocUuid = repurchaseService.getRepurchaseDocUuidBy(thirdUniqueId);
        if (null == repurchaseDocUuid || "".equals(repurchaseDocUuid)){
            throw new Exception("回购单不存在");
        }

        String result = refactorMethod.applyRepaymentOderForTwoDetails(orderRequestNo,productCode,orderAmount,firstUniqueId,secondUniqueId,
                firstRepaymentNumber,repurchaseDocUuid+1,firstRepaymentWay,secondRepaymentWay,
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
     * 还款订单参数校验，回购单号为空
     * @throws Exception
     */
    @Test
    public void checkParams25() throws Exception{

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

        String repurchaseDocUuid = "";

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
        Assert.assertEquals(RepaymentStatus.VERIFICATION_FAILURE,repaymentStatus);

    }


    /**
     * 还款订单参数校验,明细金额为负数
     * @throws Exception
     */
    @Test
    public void checkParams26() throws Exception{
        String orderRequestNo = firstUniqueId;
        String transType = "0";
        String orderAmount = "9940";
        firstRepaymentNumber = firstRepaymentNumber + 1;
        Long firstRepaymentWay = 1000l;
        String firstDetailTotalAmount = "9940";
        String firstDetailPrincipal = "10000";
        String firstDetailInterest = "-20";
        Long firstFeeType = 1l;
        String repayScheduleNo = "";

        String result = refactorMethod.applyRepaymentOderForOneDetail(orderRequestNo,orderRequestNo,transType,productCode,orderAmount,firstUniqueId,
                firstRepaymentNumber,firstRepaymentWay,
                firstDetailTotalAmount,firstDetailPrincipal,
                firstDetailInterest,firstFeeType,repayScheduleNo);

        if(!result.contains("订单已受理")){
            throw new Exception("还款订单校验错误!");
        }
        RepaymentStatus repaymentStatus;
        while (true) {
            RepaymentOrder repaymentOrder = repaymentOrderService.getRepaymentOrderByUniqueId(firstUniqueId, productCode);
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
     * 还款订单参数校验,明细金额大于该还款计划未结清金额
     * @throws Exception
     */
    @Test
    public void checkParams27() throws Exception{
        String orderRequestNo = firstUniqueId;
        String transType = "0";
        String orderAmount = "10120";
        firstRepaymentNumber = firstRepaymentNumber + 1;
        Long firstRepaymentWay = 1000l;
        String firstDetailTotalAmount = "10120";
        String firstDetailPrincipal = "10000";
        String firstDetailInterest = "30";
        Long firstFeeType = 1l;
        String repayScheduleNo = "";

        String result = refactorMethod.applyRepaymentOderForOneDetail(orderRequestNo,orderRequestNo,transType,productCode,orderAmount,firstUniqueId,
                firstRepaymentNumber,firstRepaymentWay,
                firstDetailTotalAmount,firstDetailPrincipal,
                firstDetailInterest,firstFeeType,repayScheduleNo);

        if(!result.contains("订单已受理")){
            throw new Exception("还款订单校验错误!");
        }
        RepaymentStatus repaymentStatus;
        while (true) {
            RepaymentOrder repaymentOrder = repaymentOrderService.getRepaymentOrderByUniqueId(firstUniqueId, productCode);
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
     * 还款订单参数校验,一笔还款提交两个还款订单
     * @throws Exception
     */
    @Test
    public void checkParams28() throws Exception{
        String orderRequestNo = firstUniqueId;
        String transType = "0";
        String orderAmount = "5040";
        Long firstRepaymentWay = 1000l;
        String firstDetailTotalAmount = "5040";
        String firstDetailPrincipal = "5000";
        String firstDetailInterest = "10";
        Long firstFeeType = 1l;
        String repayScheduleNo = "";

        String result = refactorMethod.applyRepaymentOderForOneDetail(orderRequestNo,orderRequestNo,transType,productCode,orderAmount,firstUniqueId,
                firstRepaymentNumber,firstRepaymentWay,
                firstDetailTotalAmount,firstDetailPrincipal,
                firstDetailInterest,firstFeeType,repayScheduleNo);

        if(!result.contains("订单已受理")){
            throw new Exception("还款订单校验错误!");
        }
        RepaymentStatus repaymentStatus;
        while (true) {
            RepaymentOrder repaymentOrder = repaymentOrderService.getRepaymentOrderByUniqueId(firstUniqueId, productCode);
            if (null == repaymentOrder) {
                throw new Exception("还款订单不存在!");
            }
            repaymentStatus = repaymentOrder.transferToRepaymentStatus();

            if (repaymentStatus != RepaymentStatus.TO_VERIFY && repaymentStatus != RepaymentStatus.VERIFICATION_IN_PROCESS) {
                break;
            }
            Thread.sleep(5000l);
        }

        if(repaymentStatus != RepaymentStatus.VERIFICATION_SUCCESS){
            throw new Exception("还款订单状态不正确");
        }

        String result2 = refactorMethod.applyRepaymentOderForOneDetail(orderRequestNo+1,orderRequestNo+1,transType,productCode,orderAmount,firstUniqueId,
                firstRepaymentNumber,firstRepaymentWay,
                firstDetailTotalAmount,firstDetailPrincipal,
                firstDetailInterest,firstFeeType,repayScheduleNo);

        if(!result2.contains("订单已受理")){
            throw new Exception("还款订单校验错误!");
        }
        RepaymentStatus repaymentStatus2;
        while (true) {
            RepaymentOrder repaymentOrder2 = repaymentOrderService.getRepaymentOrderByUniqueId(firstUniqueId, productCode);
            if (null == repaymentOrder2) {
                throw new Exception("还款订单不存在!");
            }
            repaymentStatus2 = repaymentOrder2.transferToRepaymentStatus();

            if (repaymentStatus2 != RepaymentStatus.TO_VERIFY && repaymentStatus2 != RepaymentStatus.VERIFICATION_IN_PROCESS) {
                break;
            }
            Thread.sleep(5000l);
        }


        Assert.assertEquals(RepaymentStatus.VERIFICATION_SUCCESS,repaymentStatus2);
    }


    /**
     * 还款订单参数校验,明细金额大于该还款计划总金额-支付中的金额
     * @throws Exception
     */
    @Test
    public void checkParams29() throws Exception{
        String orderRequestNo = firstUniqueId;
        String transType = "0";
        String orderAmount = "5080";
        Long firstRepaymentWay = 1000l;
        String firstDetailTotalAmount = "5080";
        String firstDetailPrincipal = "5000";
        String firstDetailInterest = "20";
        Long firstFeeType = 1l;
        String repayScheduleNo = "";

        String result = refactorMethod.applyRepaymentOderForOneDetail(orderRequestNo,orderRequestNo,transType,productCode,orderAmount,firstUniqueId,
                firstRepaymentNumber,firstRepaymentWay,
                firstDetailTotalAmount,firstDetailPrincipal,
                firstDetailInterest,firstFeeType,repayScheduleNo);

        if(!result.contains("订单已受理")){
            throw new Exception("还款订单校验错误!");
        }
        RepaymentStatus repaymentStatus;
        while (true) {
            RepaymentOrder repaymentOrder = repaymentOrderService.getRepaymentOrderByUniqueId(firstUniqueId, productCode);
            if (null == repaymentOrder) {
                throw new Exception("还款订单不存在!");
            }
            repaymentStatus = repaymentOrder.transferToRepaymentStatus();

            if (repaymentStatus != RepaymentStatus.TO_VERIFY && repaymentStatus != RepaymentStatus.VERIFICATION_IN_PROCESS) {
                break;
            }
            Thread.sleep(5000l);
        }

        if(repaymentStatus != RepaymentStatus.VERIFICATION_SUCCESS){
            throw new Exception("还款订单状态不正确");
        }

        String orderRequestNo2 = orderRequestNo + 1;
        String result2 = refactorMethod.applyRepaymentOderForOneDetail(orderRequestNo2,orderRequestNo2,transType,productCode,orderAmount,firstUniqueId,
                firstRepaymentNumber,firstRepaymentWay,
                firstDetailTotalAmount,firstDetailPrincipal,
                firstDetailInterest,firstFeeType,repayScheduleNo);

        if(!result2.contains("订单已受理")){
            throw new Exception("还款订单校验错误!");
        }
        RepaymentStatus repaymentStatus2;
        while (true) {
            RepaymentOrder repaymentOrder2 = repaymentOrderService.getRepaymentOrderByUniqueId(orderRequestNo2, productCode);
            if (null == repaymentOrder2) {
                throw new Exception("还款订单不存在!");
            }
            repaymentStatus2 = repaymentOrder2.transferToRepaymentStatus();

            if (repaymentStatus2 != RepaymentStatus.TO_VERIFY && repaymentStatus2 != RepaymentStatus.VERIFICATION_IN_PROCESS) {
                break;
            }
            Thread.sleep(5000l);
        }

        Assert.assertEquals(RepaymentStatus.VERIFICATION_FAILURE,repaymentStatus2);
    }


    /**
     * 还款订单参数校验,填入的repayScheduleNo错误,repaymentPlanNo正确(线下)
     * @throws Exception
     */
    @Test
    public void checkParams30() throws Exception{
        String orderRequestNo = firstUniqueId;
        String transType = "0";
        String orderAmount = "10080";
        Long firstRepaymentWay = 2001l;
        String firstDetailTotalAmount = "10080";
        String firstDetailPrincipal = "10000";
        String firstDetailInterest = "20";
        String repaymentPlanNo = this.firstRepaymentNumber;
        Long firstFeeType = 1l;

        AssetSet assetSet = repaymentPlanService.getRepaymentPlanByRepaymentCode(repaymentPlanNo);
        if(null == assetSet){
            throw new Exception("还款计划不存在");
        }
        String repayScheduleNo = assetSet.getOuterRepaymentPlanNo();
        if(null == repayScheduleNo || "".equals(repayScheduleNo)){
            throw new Exception("商户还款计划编号为空");
        }
        String useRepayScheduleNo = repayScheduleNo + 1;
        String result = refactorMethod.applyRepaymentOderForOneDetail(orderRequestNo,orderRequestNo,transType,productCode,orderAmount,firstUniqueId,
                repaymentPlanNo,firstRepaymentWay,
                firstDetailTotalAmount,firstDetailPrincipal,
                firstDetailInterest,firstFeeType,useRepayScheduleNo);

        if(!result.contains("订单已受理")){
            throw new Exception("还款订单校验错误!");
        }
        RepaymentStatus repaymentStatus;
        while (true) {
            RepaymentOrder repaymentOrder = repaymentOrderService.getRepaymentOrderByUniqueId(firstUniqueId, productCode);
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
     * 还款订单参数校验,填入的repayScheduleNo错误,repaymentPlanNo正确(线上)
     * @throws Exception
     */
    @Test
    public void checkParams31() throws Exception{
        String orderRequestNo = firstUniqueId;
        String transType = "0";
        String orderAmount = "10080";
        Long firstRepaymentWay = 1000l;
        String firstDetailTotalAmount = "10080";
        String firstDetailPrincipal = "10000";
        String firstDetailInterest = "20";
        String repaymentPlanNo = this.firstRepaymentNumber;
        Long firstFeeType = 1l;

        AssetSet assetSet = repaymentPlanService.getRepaymentPlanByRepaymentCode(repaymentPlanNo);
        if(null == assetSet){
            throw new Exception("还款计划不存在");
        }
        String repayScheduleNo = assetSet.getOuterRepaymentPlanNo();
        if(null == repayScheduleNo || "".equals(repayScheduleNo)){
            throw new Exception("商户还款计划编号为空");
        }
        String useRepayScheduleNo = repayScheduleNo + 1;
        String result = refactorMethod.applyRepaymentOderForOneDetail(orderRequestNo,orderRequestNo,transType,productCode,orderAmount,firstUniqueId,
                repaymentPlanNo,firstRepaymentWay,
                firstDetailTotalAmount,firstDetailPrincipal,
                firstDetailInterest,firstFeeType,useRepayScheduleNo);

        if(!result.contains("订单已受理")){
            throw new Exception("还款订单校验错误!");
        }
        RepaymentStatus repaymentStatus;
        while (true) {
            RepaymentOrder repaymentOrder = repaymentOrderService.getRepaymentOrderByUniqueId(firstUniqueId, productCode);
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
     * 还款订单参数校验,填入的repayScheduleNo正确,repaymentPlanNo错误(线上)
     * @throws Exception
     */
    @Test
    public void checkParams32() throws Exception{
        String orderRequestNo = firstUniqueId;
        String transType = "0";
        String orderAmount = "10080";
        Long firstRepaymentWay = 1000l;
        String firstDetailTotalAmount = "10080";
        String firstDetailPrincipal = "10000";
        String firstDetailInterest = "20";
        String repaymentPlanNo = this.firstRepaymentNumber+1;
        Long firstFeeType = 1l;

        AssetSet assetSet = repaymentPlanService.getRepaymentPlanByRepaymentCode(this.firstRepaymentNumber);
        if(null == assetSet){
            throw new Exception("还款计划不存在");
        }
        String repayScheduleNo = assetSet.getOuterRepaymentPlanNo();
        if(null == repayScheduleNo || "".equals(repayScheduleNo)){
            throw new Exception("商户还款计划编号为空");
        }
        String result = refactorMethod.applyRepaymentOderForOneDetail(orderRequestNo,orderRequestNo,transType,productCode,orderAmount,firstUniqueId,
                repaymentPlanNo,firstRepaymentWay,
                firstDetailTotalAmount,firstDetailPrincipal,
                firstDetailInterest,firstFeeType,repayScheduleNo);

        if(!result.contains("订单已受理")){
            throw new Exception("还款订单校验错误!");
        }
        RepaymentStatus repaymentStatus;
        while (true) {
            RepaymentOrder repaymentOrder = repaymentOrderService.getRepaymentOrderByUniqueId(firstUniqueId, productCode);
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
     * 还款订单参数校验,填入的repayScheduleNo正确,repaymentPlanNo错误(线下)
     * @throws Exception
     */
    @Test
    public void checkParams33() throws Exception{
        String orderRequestNo = firstUniqueId;
        String transType = "0";
        String orderAmount = "10080";
        Long firstRepaymentWay = 2001l;
        String firstDetailTotalAmount = "10080";
        String firstDetailPrincipal = "10000";
        String firstDetailInterest = "20";
        String repaymentPlanNo = this.firstRepaymentNumber+1;
        Long firstFeeType = 1l;

        AssetSet assetSet = repaymentPlanService.getRepaymentPlanByRepaymentCode(this.firstRepaymentNumber);
        if(null == assetSet){
            throw new Exception("还款计划不存在");
        }
        String repayScheduleNo = assetSet.getOuterRepaymentPlanNo();
        if(null == repayScheduleNo || "".equals(repayScheduleNo)){
            throw new Exception("商户还款计划编号为空");
        }
        String result = refactorMethod.applyRepaymentOderForOneDetail(orderRequestNo,orderRequestNo,transType,productCode,orderAmount,firstUniqueId,
                repaymentPlanNo,firstRepaymentWay,
                firstDetailTotalAmount,firstDetailPrincipal,
                firstDetailInterest,firstFeeType,repayScheduleNo);

        if(!result.contains("订单已受理")){
            throw new Exception("还款订单校验错误!");
        }
        RepaymentStatus repaymentStatus;
        while (true) {
            RepaymentOrder repaymentOrder = repaymentOrderService.getRepaymentOrderByUniqueId(firstUniqueId, productCode);
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
     * 还款订单参数校验,填入的repayScheduleNo不在该贷款合同下(线上)
     * @throws Exception
     */
    @Test
    public void checkParams34() throws Exception{
        String orderRequestNo = firstUniqueId;
        String transType = "0";
        String orderAmount = "10080";
        Long firstRepaymentWay = 1000l;
        String firstDetailTotalAmount = "10080";
        String firstDetailPrincipal = "10000";
        String firstDetailInterest = "20";
        Long firstFeeType = 1l;

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

        String secondRepaymentNumber = refactorMethod.queryRepaymentPlan(productCode,secondUniqueId,0);
        AssetSet assetSet = repaymentPlanService.getRepaymentPlanByRepaymentCode(secondRepaymentNumber);
        if(null == assetSet){
            throw new Exception("还款计划不存在");
        }
        String repayScheduleNo = assetSet.getOuterRepaymentPlanNo();
        if(null == repayScheduleNo || "".equals(repayScheduleNo)){
            throw new Exception("商户还款计划编号为空");
        }

        String result = refactorMethod.applyRepaymentOderForOneDetail(orderRequestNo,orderRequestNo,transType,productCode,orderAmount,firstUniqueId,
                this.firstRepaymentNumber,firstRepaymentWay,
                firstDetailTotalAmount,firstDetailPrincipal,
                firstDetailInterest,firstFeeType,repayScheduleNo);

        if(!result.contains("订单已受理")){
            throw new Exception("还款订单校验错误!");
        }
        RepaymentStatus repaymentStatus;
        while (true) {
            RepaymentOrder repaymentOrder = repaymentOrderService.getRepaymentOrderByUniqueId(firstUniqueId, productCode);
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
     * 还款订单参数校验,填入的repayScheduleNo不在该贷款合同下(商户代扣)
     * @throws Exception
     */
    @Test
    public void checkParams35() throws Exception{
        String orderRequestNo = firstUniqueId;
        String transType = "0";
        String orderAmount = "10080";
        Long firstRepaymentWay = 4001l;
        String firstDetailTotalAmount = "10080";
        String firstDetailPrincipal = "10000";
        String firstDetailInterest = "20";
        Long firstFeeType = 1l;

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

        String secondRepaymentNumber = refactorMethod.queryRepaymentPlan(productCode,secondUniqueId,0);
        AssetSet assetSet = repaymentPlanService.getRepaymentPlanByRepaymentCode(secondRepaymentNumber);
        if(null == assetSet){
            throw new Exception("还款计划不存在");
        }
        String repayScheduleNo = assetSet.getOuterRepaymentPlanNo();
        if(null == repayScheduleNo || "".equals(repayScheduleNo)){
            throw new Exception("商户还款计划编号为空");
        }

        String result = refactorMethod.applyRepaymentOderForOneDetail(orderRequestNo,orderRequestNo,transType,productCode,orderAmount,firstUniqueId,
                this.firstRepaymentNumber,firstRepaymentWay,
                firstDetailTotalAmount,firstDetailPrincipal,
                firstDetailInterest,firstFeeType,repayScheduleNo);

        if(!result.contains("订单已受理")){
            throw new Exception("还款订单校验错误!");
        }
        RepaymentStatus repaymentStatus;
        while (true) {
            RepaymentOrder repaymentOrder = repaymentOrderService.getRepaymentOrderByUniqueId(firstUniqueId, productCode);
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
