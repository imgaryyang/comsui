package com.zufangbao.aboutMoidfyRepaymentPlan;

import com.demo2do.core.utils.DateUtils;
import com.zufangbao.refactor.method.RefactorMethod;
import com.zufangbao.sun.entity.repayment.order.RepaymentOrder;
import com.zufangbao.sun.entity.repayment.order.RepaymentStatus;
import com.zufangbao.sun.service.RepaymentOrderService;
import com.zufangbao.sun.service.RepurchaseService;
import com.zufangbao.sun.yunxin.entity.AssetSet;
import com.zufangbao.sun.yunxin.entity.DeductionStatus;
import com.zufangbao.sun.yunxin.entity.ExecutingStatus;
import com.zufangbao.sun.yunxin.entity.PaymentStatus;
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
public class CheckModifyRepaymentPlanParams {
    @Autowired
    private RepaymentPlanService repaymentPlanService;

    @Autowired
    private RepaymentOrderService repaymentOrderService;


    @Autowired
    private RepurchaseService repurchaseService;

    RefactorMethod refactorMethod = new RefactorMethod();

    private String uniqueId = "";
    String productCode = "CS0001";
    String totalAmount = "30000";

    @Before
    public void setUp() {
        refactorMethod.deleteAllCashFlow();
        uniqueId = UUID.randomUUID().toString();
        String repaymentAccountNo = "6217857600016839330";
        String loanCustomerName = "秦操";
        String interest = "20";
        String loanCustomerNo = "11111117";
        String firstPlanDate = DateUtils.format(DateUtils.addMonths(new Date(), -1));
        String secondPlanDate = DateUtils.format(new Date());
        String thirdPlanDate = DateUtils.format(DateUtils.addMonths(new Date(), 1));

        refactorMethod.importAssetPackage(totalAmount, productCode, uniqueId, repaymentAccountNo, interest, loanCustomerNo,
                firstPlanDate, secondPlanDate, thirdPlanDate, loanCustomerName);
    }

    /**
     * 接口参数校验-uniqueId与contractNo都为空
     */
    @Test
    public void checkParams1() {
        String uniqueId = "";
        String PlanAmount = totalAmount;
        String interest = "0";
        String PlanDate = DateUtils.format(new Date());
        Integer assetType = 0;
        String requestReason = "1";
        String repayScheduleNo = UUID.randomUUID().toString();
        String contractNo = "";
        String result = refactorMethod.modifyRepaymentPlanForOne(uniqueId, PlanAmount, interest, PlanDate, assetType, requestReason, repayScheduleNo, contractNo);
        boolean outcome = result.contains("请选填其中一种编号[uniqueId，contractNo]");
        String planCount = refactorMethod.queryRepaymentPlanCount(this.uniqueId);
        Assert.assertEquals(outcome, true);
        Assert.assertEquals(planCount,"3");
    }

    /**
     * 接口参数校验-uniqueId不存在
     */
    @Test
    public void checkParams2() {
        String uniqueId = this.uniqueId + 1;
        String PlanAmount = totalAmount;
        String interest = "0";
        String PlanDate = DateUtils.format(new Date());
        Integer assetType = 0;
        String requestReason = "1";
        String repayScheduleNo = UUID.randomUUID().toString();
        String contractNo = "";
        String result = refactorMethod.modifyRepaymentPlanForOne(uniqueId, PlanAmount, interest, PlanDate, assetType, requestReason, repayScheduleNo, contractNo);
        boolean outcome = result.contains("贷款合同不存在");
        String planCount = refactorMethod.queryRepaymentPlanCount(this.uniqueId);
        Assert.assertEquals(outcome, true);
        Assert.assertEquals(planCount,"3");
    }

    /**
     * 接口参数校验-contractNo不存在
     */
    @Test
    public void checkParams3() {
        String uniqueId = "";
        String PlanAmount = totalAmount;
        String interest = "0";
        String PlanDate = DateUtils.format(new Date());
        Integer assetType = 0;
        String requestReason = "1";
        String repayScheduleNo = UUID.randomUUID().toString();
        String contractNo = this.uniqueId + 1;
        String result = refactorMethod.modifyRepaymentPlanForOne(uniqueId, PlanAmount, interest, PlanDate, assetType, requestReason, repayScheduleNo, contractNo);
        boolean outcome = result.contains("贷款合同不存在");
        String planCount = refactorMethod.queryRepaymentPlanCount(this.uniqueId);
        Assert.assertEquals(outcome, true);
        Assert.assertEquals(planCount,"3");
    }

    /**
     * 接口参数校验-contractNo存在,uniqueId不存在
     */
    @Test
    public void checkParams4() {
        String uniqueId = this.uniqueId + 1;
        String PlanAmount = totalAmount;
        String interest = "0";
        String PlanDate = DateUtils.format(new Date());
        Integer assetType = 0;
        String requestReason = "1";
        String repayScheduleNo = UUID.randomUUID().toString();
        String contractNo = this.uniqueId;
        String result = refactorMethod.modifyRepaymentPlanForOne(uniqueId, PlanAmount, interest, PlanDate, assetType, requestReason, repayScheduleNo, contractNo);
        boolean outcome = result.contains("贷款合同不存在");
        String planCount = refactorMethod.queryRepaymentPlanCount(this.uniqueId);
        Assert.assertEquals(outcome, true);
        Assert.assertEquals(planCount,"3");
    }

    /**
     * 接口参数校验-assetType与requestReason不匹配
     */
    @Test
    public void checkParams5() {
        String uniqueId = this.uniqueId;
        String PlanAmount = totalAmount;
        String interest = "0";
        String PlanDate = DateUtils.format(new Date());
        Integer assetType = 0;
        String requestReason = "3";
        String repayScheduleNo = UUID.randomUUID().toString();
        String contractNo = "";
        String result = refactorMethod.modifyRepaymentPlanForOne(uniqueId, PlanAmount, interest, PlanDate, assetType, requestReason, repayScheduleNo, contractNo);
        boolean outcome = result.contains("计划变更原因不是提前结清和提前部分还款时，还款计划类型不能为提前");
        String planCount = refactorMethod.queryRepaymentPlanCount(this.uniqueId);
        Assert.assertEquals(outcome, true);
        Assert.assertEquals(planCount,"3");
    }

    /**
     * 接口参数校验-assetType与requestReason不匹配2
     */
    @Test
    public void checkParams24() {
        String uniqueId = this.uniqueId;
        String PlanAmount = totalAmount;
        String interest = "0";
        String PlanDate = DateUtils.format(new Date());
        Integer assetType = 1;
        String requestReason = "1";
        String repayScheduleNo = UUID.randomUUID().toString();
        String contractNo = "";
        String result = refactorMethod.modifyRepaymentPlanForOne(uniqueId, PlanAmount, interest, PlanDate, assetType, requestReason, repayScheduleNo, contractNo);
        boolean outcome = result.contains("计划变更原因为提前结清或提前部分还款时，还款计划类型应为提前");
        String planCount = refactorMethod.queryRepaymentPlanCount(this.uniqueId);
        Assert.assertEquals(outcome, true);
        Assert.assertEquals(planCount,"3");
    }
    /**
     * 接口参数校验-requestReason为空
     */
    @Test
    public void checkParams6() {
        String uniqueId = this.uniqueId;
        String PlanAmount = totalAmount;
        String interest = "0";
        String PlanDate = DateUtils.format(new Date());
        Integer assetType = 0;
        String requestReason = "";
        String repayScheduleNo = UUID.randomUUID().toString();
        String contractNo = "";
        String result = refactorMethod.modifyRepaymentPlanForOne(uniqueId, PlanAmount, interest, PlanDate, assetType, requestReason, repayScheduleNo, contractNo);
        boolean outcome = result.contains("请求原因[requestReason]不能为空");
        String planCount = refactorMethod.queryRepaymentPlanCount(this.uniqueId);
        Assert.assertEquals(outcome, true);
        Assert.assertEquals(planCount,"3");
    }

    /**
     * 接口参数校验-requestReason为非数字字符串
     */
    @Test
    public void checkParams7() {
        String uniqueId = this.uniqueId;
        String PlanAmount = totalAmount;
        String interest = "0";
        String PlanDate = DateUtils.format(new Date());
        Integer assetType = 0;
        String requestReason = "s";
        String repayScheduleNo = UUID.randomUUID().toString();
        String contractNo = "";
        String result = refactorMethod.modifyRepaymentPlanForOne(uniqueId, PlanAmount, interest, PlanDate, assetType, requestReason, repayScheduleNo, contractNo);
        boolean outcome = result.contains("请求原因[requestReason]内容不合法");
        String planCount = refactorMethod.queryRepaymentPlanCount(this.uniqueId);
        Assert.assertEquals(outcome, true);
        Assert.assertEquals(planCount,"3");
    }

    /**
     * 接口参数校验-变更金额与本金总额不想等
     */
    @Test
    public void checkParams8() {
        String uniqueId = this.uniqueId;
        String PlanAmount = String.valueOf(Double.valueOf(totalAmount) - 5000);
        String interest = "0";
        String PlanDate = DateUtils.format(new Date());
        Integer assetType = 2;
        String requestReason = "3";
        String repayScheduleNo = UUID.randomUUID().toString();
        String contractNo = "";
        String result = refactorMethod.modifyRepaymentPlanForOne(uniqueId, PlanAmount, interest, PlanDate, assetType, requestReason, repayScheduleNo, contractNo);
        boolean outcome = result.contains("无效的计划本金总额");
        String planCount = refactorMethod.queryRepaymentPlanCount(this.uniqueId);
        Assert.assertEquals(outcome, true);
        Assert.assertEquals(planCount,"3");
    }

    /**
     * 接口参数校验-变更日期超过还款宽限日
     */
    @Test
    public void checkParams9() {
        String uniqueId = this.uniqueId;
        String PlanAmount = totalAmount;
        String interest = "0";
        String PlanDate = DateUtils.format(DateUtils.addDays(new Date(), -34));
        Integer assetType = 2;
        String requestReason = "3";
        String repayScheduleNo = UUID.randomUUID().toString();
        String contractNo = "";
        String result = refactorMethod.modifyRepaymentPlanForOne(uniqueId, PlanAmount, interest, PlanDate, assetType, requestReason, repayScheduleNo, contractNo);
        boolean outcome = result.contains("计划还款日期排序错误，需按计划还款日期递增");
        String planCount = refactorMethod.queryRepaymentPlanCount(this.uniqueId);
        Assert.assertEquals(outcome, true);
        Assert.assertEquals(planCount,"3");
    }

    /**
     * 接口参数校验-利息为负数
     */
    @Test
    public void checkParams10() {
        String uniqueId = this.uniqueId;
        String PlanAmount = totalAmount;
        String interest = "-10";
        String PlanDate = DateUtils.format(new Date());
        Integer assetType = 2;
        String requestReason = "3";
        String repayScheduleNo = UUID.randomUUID().toString();
        String contractNo = "";
        String result = refactorMethod.modifyRepaymentPlanForOne(uniqueId, PlanAmount, interest, PlanDate, assetType, requestReason, repayScheduleNo, contractNo);
        boolean outcome = result.contains("无效的计划利息总额");
        String planCount = refactorMethod.queryRepaymentPlanCount(this.uniqueId);
        Assert.assertEquals(outcome, true);
        Assert.assertEquals(planCount,"3");
    }

    /**
     * 业务校验-变更包含超过还款宽限日的还款计划
     */
    @Test
    public void checkParams11() {
        uniqueId = UUID.randomUUID().toString();
        String repaymentAccountNo = "6217857600016839330";
        String loanCustomerName = "秦操";
        String interest = "20";
        String loanCustomerNo = "11111117";
        String firstPlanDate = DateUtils.format(DateUtils.addMonths(new Date(), -2));
        String secondPlanDate = DateUtils.format(new Date());
        String thirdPlanDate = DateUtils.format(DateUtils.addMonths(new Date(), 1));

        refactorMethod.importAssetPackage(totalAmount, productCode, uniqueId, repaymentAccountNo, interest, loanCustomerNo,
                firstPlanDate, secondPlanDate, thirdPlanDate, loanCustomerName);
        String PlanAmount = totalAmount;
        String PlanDate = DateUtils.format(new Date());
        Integer assetType = 2;
        String requestReason = "3";
        String repayScheduleNo = UUID.randomUUID().toString();
        String contractNo = "";
        String result = refactorMethod.modifyRepaymentPlanForOne(uniqueId, PlanAmount, interest, PlanDate, assetType, requestReason, repayScheduleNo, contractNo);
        boolean outcome = result.contains("无效的计划本金总额");
        String planCount = refactorMethod.queryRepaymentPlanCount(uniqueId);
        Assert.assertEquals(outcome, true);
        Assert.assertEquals(planCount,"3");
    }

    /**
     * 业务校验-两次相同的变更
     * @throws Exception
     */
    @Test
    public void checkParams12() throws Exception{
        String uniqueId = this.uniqueId;
        String PlanAmount = totalAmount;
        String interest = "0";
        String PlanDate = DateUtils.format(new Date());
        Integer assetType = 2;
        String requestReason = "3";
        String repayScheduleNo = UUID.randomUUID().toString();
        String contractNo = "";
        String result = refactorMethod.modifyRepaymentPlanForOne(uniqueId, PlanAmount, interest, PlanDate, assetType, requestReason, repayScheduleNo, contractNo);
        if(!result.contains("成功")){
            throw new Exception("变更失败");
        }
        String result2 = refactorMethod.modifyRepaymentPlanForOne(uniqueId, PlanAmount, interest, PlanDate, assetType, requestReason, repayScheduleNo, contractNo);
        boolean outcome = result2.contains("成功");
        String planCount = refactorMethod.queryRepaymentPlanCount(uniqueId);
        Assert.assertEquals(outcome, true);
        Assert.assertEquals(planCount,"1");
    }

    /**
     * 业务校验-不同合同下商户还款计划编号重复
     * @throws Exception
     */
    @Test
    public void checkParams13() throws Exception{
        String uniqueId = this.uniqueId;
        String PlanAmount = totalAmount;
        String interest = "0";
        String PlanDate = DateUtils.format(new Date());
        Integer assetType = 2;
        String requestReason = "3";
        String repayScheduleNo = UUID.randomUUID().toString();
        String contractNo = "";
        String result = refactorMethod.modifyRepaymentPlanForOne(uniqueId, PlanAmount, interest, PlanDate, assetType, requestReason, repayScheduleNo, contractNo);
        if(!result.contains("成功")){
            throw new Exception("变更失败");
        }
        String uniqueId2 = UUID.randomUUID().toString();
        String repaymentAccountNo = "6217857600016839330";
        String loanCustomerName = "秦操";
        String contractInterest = "20";
        String loanCustomerNo = "11111117";
        String firstPlanDate = DateUtils.format(DateUtils.addMonths(new Date(), -1));
        String secondPlanDate = DateUtils.format(new Date());
        String thirdPlanDate = DateUtils.format(DateUtils.addMonths(new Date(), 1));
        refactorMethod.importAssetPackage(totalAmount, productCode, uniqueId2, repaymentAccountNo, contractInterest, loanCustomerNo,
                firstPlanDate, secondPlanDate, thirdPlanDate, loanCustomerName);
        String result2 = refactorMethod.modifyRepaymentPlanForOne(uniqueId2, PlanAmount, interest, PlanDate, assetType, requestReason, repayScheduleNo, contractNo);
        boolean outcome = result2.contains("商户还款计划编号重复");
        String planCount = refactorMethod.queryRepaymentPlanCount(uniqueId2);
        Assert.assertEquals(outcome, true);
        Assert.assertEquals(planCount,"3");
    }

    /**
     * 业务校验-变更日期在合同终止日之后
     */
    @Test
    public void checkParams14(){
        String uniqueId = this.uniqueId;
        String PlanAmount = totalAmount;
        String interest = "0";
        String PlanDate = DateUtils.format(DateUtils.addMonths(new Date(),3));
        Integer assetType = 2;
        String requestReason = "3";
        String repayScheduleNo = UUID.randomUUID().toString();
        String contractNo = "";
        String result = refactorMethod.modifyRepaymentPlanForOne(uniqueId, PlanAmount, interest, PlanDate, assetType, requestReason, repayScheduleNo, contractNo);
        boolean outcome = result.contains("成功");
        String planCount = refactorMethod.queryRepaymentPlanCount(uniqueId);
        Assert.assertEquals(outcome, true);
        Assert.assertEquals(planCount,"1");
    }

    /**
     * 业务校验-变更未包含超过还款宽限日的还款计划
     */
    @Test
    public void checkParams15(){
        uniqueId = UUID.randomUUID().toString();
        String repaymentAccountNo = "6217857600016839330";
        String loanCustomerName = "秦操";
        String interest = "20";
        String loanCustomerNo = "11111117";
        String firstPlanDate = DateUtils.format(DateUtils.addMonths(new Date(), -2));
        String secondPlanDate = DateUtils.format(new Date());
        String thirdPlanDate = DateUtils.format(DateUtils.addMonths(new Date(), 1));

        refactorMethod.importAssetPackage(totalAmount, productCode, uniqueId, repaymentAccountNo, interest, loanCustomerNo,
                firstPlanDate, secondPlanDate, thirdPlanDate, loanCustomerName);
        String PlanAmount = String.valueOf((Double.valueOf(totalAmount)/3)*2);
        String PlanDate = DateUtils.format(new Date());
        Integer assetType = 2;
        String requestReason = "3";
        String repayScheduleNo = UUID.randomUUID().toString();
        String contractNo = "";
        String result = refactorMethod.modifyRepaymentPlanForOne(uniqueId, PlanAmount, interest, PlanDate, assetType, requestReason, repayScheduleNo, contractNo);
        boolean outcome = result.contains("成功");
        String planCount = refactorMethod.queryRepaymentPlanCount(uniqueId);
        Assert.assertEquals(outcome, true);
        Assert.assertEquals(planCount,"2");
    }

    /**
     * 业务校验-变更时间晚于合同截止日后108天
     */
    @Test
    public void checkParams16(){
        String uniqueId = this.uniqueId;
        String PlanAmount = totalAmount;
        String interest = "0";
        String PlanDate = DateUtils.format(DateUtils.addMonths(new Date(),6));
        Integer assetType = 2;
        String requestReason = "3";
        String repayScheduleNo = UUID.randomUUID().toString();
        String contractNo = "";
        String result = refactorMethod.modifyRepaymentPlanForOne(uniqueId, PlanAmount, interest, PlanDate, assetType, requestReason, repayScheduleNo, contractNo);
        boolean outcome = result.contains("计划还款日期不能晚于贷款合同结束日108天");
        String planCount = refactorMethod.queryRepaymentPlanCount(uniqueId);
        Assert.assertEquals(outcome, true);
        Assert.assertEquals(planCount,"3");
    }

    /**
     * 业务校验-变更还款计划包含扣款中的还款计划
     * @throws Exception
     */
    @Test
    public void checkParams17() throws Exception{
        String uniqueId = this.uniqueId;
        String PlanAmount = totalAmount;
        String interest = "0";
        String PlanDate = DateUtils.format(new Date());
        Integer assetType = 2;
        String requestReason = "3";
        String repayScheduleNo = UUID.randomUUID().toString();
        String contractNo = "";

        String payAcNo = "6217857600016839330";
        String repaymentType = "1";
        String principal = String.valueOf(Double.valueOf(totalAmount)/3);
        String deductInterest = "20";
        String deductAmount = String.valueOf(Double.valueOf(principal)+Double.valueOf(deductInterest)*4);
        String overDueFee = "0";
        String totalOverDueFee = String.valueOf(Double.valueOf(overDueFee)*4);
        String firstPlanNo = refactorMethod.queryRepaymentPlan(productCode,uniqueId,0);
        String deductResult = refactorMethod.dedcutRepaymentPlan(productCode,uniqueId,deductAmount,payAcNo,repaymentType,firstPlanNo,principal,
                                                                    deductInterest,overDueFee,totalOverDueFee);
        if(!deductResult.contains("成功")){
            throw new Exception("扣款参数校验失败");
        }
        Thread.sleep(2000l);
        AssetSet assetSet = repaymentPlanService.getRepaymentPlanByRepaymentCode(firstPlanNo);
        if(!assetSet.getDeductionStatus().equals(DeductionStatus.LOCAL_PROCESSING) && !assetSet.getDeductionStatus().equals(DeductionStatus.OPPOSITE_PROCESSING)){
            throw new Exception("扣款失败");
        }
        String result = refactorMethod.modifyRepaymentPlanForOne(uniqueId, PlanAmount, interest, PlanDate, assetType, requestReason, repayScheduleNo, contractNo);
        boolean outcome = result.contains("存在当日扣款成功或处理中的还款计划");
        String planCount = refactorMethod.queryRepaymentPlanCount(uniqueId);
        Assert.assertEquals(outcome, true);
        Assert.assertEquals(planCount,"3");
    }

    /**
     * 业务校验-变更还款计划不包含扣款中的还款计划
     * @throws Exception
     */
    @Test
    public void checkParams18() throws Exception{
        String uniqueId = this.uniqueId;
        String PlanAmount = String.valueOf((Double.valueOf(totalAmount)/3)*2);
        String interest = "0";
        String PlanDate = DateUtils.format(new Date());
        Integer assetType = 2;
        String requestReason = "3";
        String repayScheduleNo = UUID.randomUUID().toString();
        String contractNo = "";

        String payAcNo = "6217857600016839330";
        String repaymentType = "1";
        String principal = String.valueOf(Double.valueOf(totalAmount)/3);
        String deductInterest = "20";
        String deductAmount = String.valueOf(Double.valueOf(principal)+Double.valueOf(deductInterest)*4);
        String overDueFee = "0";
        String totalOverDueFee = String.valueOf(Double.valueOf(overDueFee)*4);
        String firstPlanNo = refactorMethod.queryRepaymentPlan(productCode,uniqueId,0);
        String deductResult = refactorMethod.dedcutRepaymentPlan(productCode,uniqueId,deductAmount,payAcNo,repaymentType,firstPlanNo,principal,
                deductInterest,overDueFee,totalOverDueFee);
        if(!deductResult.contains("成功")){
            throw new Exception("扣款参数校验失败");
        }
        Thread.sleep(2000l);
        AssetSet assetSet = repaymentPlanService.getRepaymentPlanByRepaymentCode(firstPlanNo);
        if(!assetSet.getDeductionStatus().equals(DeductionStatus.LOCAL_PROCESSING) && !assetSet.getDeductionStatus().equals(DeductionStatus.OPPOSITE_PROCESSING)){
            throw new Exception("扣款失败");
        }
        String result = refactorMethod.modifyRepaymentPlanForOne(uniqueId, PlanAmount, interest, PlanDate, assetType, requestReason, repayScheduleNo, contractNo);
        boolean outcome = result.contains("存在当日扣款成功或处理中的还款计划");
        String planCount = refactorMethod.queryRepaymentPlanCount(uniqueId);
        Assert.assertEquals(outcome, true);
        Assert.assertEquals(planCount,"3");
    }

    /**
     * 业务校验-变更还款计划包含部分扣款成功的还款计划但不包含扣款成功金额
     * @throws Exception
     */
    @Test
    public void checkParams19() throws Exception{
        String uniqueId = this.uniqueId;
        String interest = "0";
        String PlanDate = DateUtils.format(new Date());
        Integer assetType = 2;
        String requestReason = "3";
        String repayScheduleNo = UUID.randomUUID().toString();
        String contractNo = "";

        String payAcNo = "6217857600016839330";
        String repaymentType = "1";
        String principal = String.valueOf(Double.valueOf(totalAmount)/6);
        String deductInterest = "20";
        String deductAmount = String.valueOf(Double.valueOf(principal)+Double.valueOf(deductInterest)*4);
        String PlanAmount = String.valueOf(Double.valueOf(totalAmount)-Double.valueOf(principal));
        String overDueFee = "0";
        String totalOverDueFee = String.valueOf(Double.valueOf(overDueFee)*4);
        String firstPlanNo = refactorMethod.queryRepaymentPlan(productCode,uniqueId,0);
        String deductResult = refactorMethod.dedcutRepaymentPlan(productCode,uniqueId,deductAmount,payAcNo,repaymentType,firstPlanNo,principal,
                deductInterest,overDueFee,totalOverDueFee);
        if(!deductResult.contains("成功")){
            throw new Exception("扣款参数校验失败");
        }
        Thread.sleep(2000l);
        AssetSet assetSet = repaymentPlanService.getRepaymentPlanByRepaymentCode(firstPlanNo);
        if(!assetSet.getDeductionStatus().equals(DeductionStatus.LOCAL_PROCESSING) && !assetSet.getDeductionStatus().equals(DeductionStatus.OPPOSITE_PROCESSING)){
            throw new Exception("扣款失败");
        }
        while (true){
            assetSet = repaymentPlanService.getRepaymentPlanByRepaymentCode(firstPlanNo);
            if(ExecutingStatus.PROCESSING.equals(assetSet.getExecutingStatus()) && DeductionStatus.SUCCESS.equals(assetSet.getDeductionStatus())){
                break;
            }
            Thread.sleep(10000l);
        }
        Thread.sleep(5000l);
        String result = refactorMethod.modifyRepaymentPlanForOne(uniqueId, PlanAmount, interest, PlanDate, assetType, requestReason, repayScheduleNo, contractNo);
        boolean outcome = result.contains("无效的计划本金总额");
        String planCount = refactorMethod.queryRepaymentPlanCount(uniqueId);
        Assert.assertEquals(outcome, true);
        Assert.assertEquals(planCount,"3");
    }

    /**
     * 业务校验-变更还款计划包含部分扣款成功的还款计划
     * @throws Exception
     */
    @Test
    public void checkParams20() throws Exception{
        String uniqueId = this.uniqueId;
        String PlanAmount = totalAmount;
        String interest = "0";
        String PlanDate = DateUtils.format(new Date());
        Integer assetType = 2;
        String requestReason = "3";
        String repayScheduleNo = UUID.randomUUID().toString();
        String contractNo = "";

        String payAcNo = "6217857600016839330";
        String repaymentType = "1";
        String principal = String.valueOf(Double.valueOf(totalAmount)/6);
        String deductInterest = "20";
        String deductAmount = String.valueOf(Double.valueOf(principal)+Double.valueOf(deductInterest)*4);
        String overDueFee = "0";
        String totalOverDueFee = String.valueOf(Double.valueOf(overDueFee)*4);
        String firstPlanNo = refactorMethod.queryRepaymentPlan(productCode,uniqueId,0);
        String deductResult = refactorMethod.dedcutRepaymentPlan(productCode,uniqueId,deductAmount,payAcNo,repaymentType,firstPlanNo,principal,
                deductInterest,overDueFee,totalOverDueFee);
        if(!deductResult.contains("成功")){
            throw new Exception("扣款参数校验失败");
        }
        Thread.sleep(2000l);
        AssetSet assetSet = repaymentPlanService.getRepaymentPlanByRepaymentCode(firstPlanNo);
        if(!assetSet.getDeductionStatus().equals(DeductionStatus.LOCAL_PROCESSING) && !assetSet.getDeductionStatus().equals(DeductionStatus.OPPOSITE_PROCESSING)){
            throw new Exception("扣款失败");
        }
        while (true){
            assetSet = repaymentPlanService.getRepaymentPlanByRepaymentCode(firstPlanNo);
            if(ExecutingStatus.PROCESSING.equals(assetSet.getExecutingStatus()) && DeductionStatus.SUCCESS.equals(assetSet.getDeductionStatus())){
                break;
            }
            Thread.sleep(10000l);
        }
        Thread.sleep(5000l);
        String result = refactorMethod.modifyRepaymentPlanForOne(uniqueId, PlanAmount, interest, PlanDate, assetType, requestReason, repayScheduleNo, contractNo);
        boolean outcome = result.contains("无效的计划本金总额");
        String planCount = refactorMethod.queryRepaymentPlanCount(uniqueId);
        Assert.assertEquals(outcome, true);
        Assert.assertEquals(planCount,"3");
    }

    /**
     * 业务校验-变更还款计划不包含部分扣款成功的还款计划
     * @throws Exception
     */
    @Test
    public void checkParams21() throws Exception{
        String uniqueId = this.uniqueId;
        String PlanAmount = String.valueOf((Double.valueOf(totalAmount)/3)*2);
        String interest = "0";
        String PlanDate = DateUtils.format(new Date());
        Integer assetType = 2;
        String requestReason = "3";
        String repayScheduleNo = UUID.randomUUID().toString();
        String contractNo = "";

        String payAcNo = "6217857600016839330";
        String repaymentType = "1";
        String principal = String.valueOf(Double.valueOf(totalAmount)/6);
        String deductInterest = "20";
        String deductAmount = String.valueOf(Double.valueOf(principal)+Double.valueOf(deductInterest)*4);
        String overDueFee = "0";
        String totalOverDueFee = String.valueOf(Double.valueOf(overDueFee)*4);
        String firstPlanNo = refactorMethod.queryRepaymentPlan(productCode,uniqueId,0);
        String deductResult = refactorMethod.dedcutRepaymentPlan(productCode,uniqueId,deductAmount,payAcNo,repaymentType,firstPlanNo,principal,
                deductInterest,overDueFee,totalOverDueFee);
        if(!deductResult.contains("成功")){
            throw new Exception("扣款参数校验失败");
        }
        Thread.sleep(2000l);
        AssetSet assetSet = repaymentPlanService.getRepaymentPlanByRepaymentCode(firstPlanNo);
        if(!assetSet.getDeductionStatus().equals(DeductionStatus.LOCAL_PROCESSING) && !assetSet.getDeductionStatus().equals(DeductionStatus.OPPOSITE_PROCESSING)){
            throw new Exception("扣款失败");
        }
        while (true){
            assetSet = repaymentPlanService.getRepaymentPlanByRepaymentCode(firstPlanNo);
            if(ExecutingStatus.PROCESSING.equals(assetSet.getExecutingStatus()) && DeductionStatus.SUCCESS.equals(assetSet.getDeductionStatus())){
                break;
            }
            Thread.sleep(10000l);
        }
        Thread.sleep(5000l);
        String result = refactorMethod.modifyRepaymentPlanForOne(uniqueId, PlanAmount, interest, PlanDate, assetType, requestReason, repayScheduleNo, contractNo);
        boolean outcome = result.contains("成功");
        String planCount = refactorMethod.queryRepaymentPlanCount(uniqueId);
        Assert.assertEquals(outcome, true);
        Assert.assertEquals(planCount,"2");
    }

    /**
     * 业务校验-变更还款计划包含还款成功的还款计划
     * @throws Exception
     */
    @Test
    public void checkParams22() throws Exception{
        String uniqueId = this.uniqueId;
        String PlanAmount = totalAmount;
        String interest = "0";
        String PlanDate = DateUtils.format(new Date());
        Integer assetType = 2;
        String requestReason = "3";
        String repayScheduleNo = UUID.randomUUID().toString();
        String contractNo = "";

        String payAcNo = "6217857600016839330";
        String repaymentType = "1";
        String principal = String.valueOf(Double.valueOf(totalAmount)/3);
        String deductInterest = "20";
        String deductAmount = String.valueOf(Double.valueOf(principal)+Double.valueOf(deductInterest)*4);
        String overDueFee = "0";
        String totalOverDueFee = String.valueOf(Double.valueOf(overDueFee)*4);
        String firstPlanNo = refactorMethod.queryRepaymentPlan(productCode,uniqueId,0);
        String deductResult = refactorMethod.dedcutRepaymentPlan(productCode,uniqueId,deductAmount,payAcNo,repaymentType,firstPlanNo,principal,
                deductInterest,overDueFee,totalOverDueFee);
        if(!deductResult.contains("成功")){
            throw new Exception("扣款参数校验失败");
        }
        Thread.sleep(2000l);
        AssetSet assetSet = repaymentPlanService.getRepaymentPlanByRepaymentCode(firstPlanNo);
        if(!assetSet.getDeductionStatus().equals(DeductionStatus.LOCAL_PROCESSING) && !assetSet.getDeductionStatus().equals(DeductionStatus.OPPOSITE_PROCESSING)){
            throw new Exception("扣款失败");
        }
        while (true){
            assetSet = repaymentPlanService.getRepaymentPlanByRepaymentCode(firstPlanNo);
            if(PaymentStatus.SUCCESS.equals(assetSet.getPaymentStatus())){
                break;
            }
            Thread.sleep(10000l);
        }
        Thread.sleep(5000l);
        String result = refactorMethod.modifyRepaymentPlanForOne(uniqueId, PlanAmount, interest, PlanDate, assetType, requestReason, repayScheduleNo, contractNo);
        boolean outcome = result.contains("无效的计划本金总额");
        String planCount = refactorMethod.queryRepaymentPlanCount(uniqueId);
        Assert.assertEquals(outcome, true);
        Assert.assertEquals(planCount,"3");
    }

    /**
     * 业务校验-变更还款计划包含还款成功的还款计划
     * @throws Exception
     */
    @Test
    public void checkParams23() throws Exception{
        String uniqueId = this.uniqueId;
        String PlanAmount = String.valueOf((Double.valueOf(totalAmount)/3)*2);
        String interest = "0";
        String PlanDate = DateUtils.format(new Date());
        Integer assetType = 2;
        String requestReason = "3";
        String repayScheduleNo = UUID.randomUUID().toString();
        String contractNo = "";

        String payAcNo = "6217857600016839330";
        String repaymentType = "1";
        String principal = String.valueOf(Double.valueOf(totalAmount)/3);
        String deductInterest = "20";
        String deductAmount = String.valueOf(Double.valueOf(principal)+Double.valueOf(deductInterest)*4);
        String overDueFee = "0";
        String totalOverDueFee = String.valueOf(Double.valueOf(overDueFee)*4);
        String firstPlanNo = refactorMethod.queryRepaymentPlan(productCode,uniqueId,0);
        String deductResult = refactorMethod.dedcutRepaymentPlan(productCode,uniqueId,deductAmount,payAcNo,repaymentType,firstPlanNo,principal,
                deductInterest,overDueFee,totalOverDueFee);
        if(!deductResult.contains("成功")){
            throw new Exception("扣款参数校验失败");
        }
        Thread.sleep(2000l);
        AssetSet assetSet = repaymentPlanService.getRepaymentPlanByRepaymentCode(firstPlanNo);
        if(!assetSet.getDeductionStatus().equals(DeductionStatus.LOCAL_PROCESSING) && !assetSet.getDeductionStatus().equals(DeductionStatus.OPPOSITE_PROCESSING)){
            throw new Exception("扣款失败");
        }
        while (true){
            assetSet = repaymentPlanService.getRepaymentPlanByRepaymentCode(firstPlanNo);
            if(PaymentStatus.SUCCESS.equals(assetSet.getPaymentStatus())){
                break;
            }
            Thread.sleep(10000l);
        }
        Thread.sleep(5000l);
        String result = refactorMethod.modifyRepaymentPlanForOne(uniqueId, PlanAmount, interest, PlanDate, assetType, requestReason, repayScheduleNo, contractNo);
        boolean outcome = result.contains("成功");
        String planCount = refactorMethod.queryRepaymentPlanCount(uniqueId);
        Assert.assertEquals(outcome, true);
        Assert.assertEquals(planCount,"2");
    }

    /**
     * 业务校验-同一个贷款合同内还款计划变更为两笔时商户还款计划编号重复
     */
    @Test
    public void checkParams25(){
        String uniqueId = this.uniqueId;
        String firstPlanAmount = String.valueOf((Double.valueOf(totalAmount)/3)*2);
        String secondPlanAmount = String.valueOf(Double.valueOf(totalAmount)/3);
        String interest = "0";
        String firstPlanDate = DateUtils.format(new Date());
        String secondPlanDate = DateUtils.format(DateUtils.addDays(new Date(),1));
        Integer firstAssetType = 1;
        Integer secondAssetType = 1;
        String requestReason = "3";
        String firstRepayScheduleNo = UUID.randomUUID().toString();
        String secondRepayScheduleNo = firstRepayScheduleNo;
        String result = refactorMethod.modifyRepaymentPlanForTwo(uniqueId,firstPlanAmount,secondPlanAmount,interest,firstPlanDate,secondPlanDate,firstAssetType,secondAssetType,requestReason,firstRepayScheduleNo,secondRepayScheduleNo);
        boolean outcome = result.contains("商户还款计划编号重复");
        String planCount = refactorMethod.queryRepaymentPlanCount(uniqueId);
        Assert.assertEquals(outcome, true);
        Assert.assertEquals(planCount,"3");
    }

    /**
     * 冒烟测试-单笔变更成功
     * @throws Exception
     */
    @Test
    public void checkParams26() throws Exception{
        String uniqueId = this.uniqueId;
        String PlanAmount = totalAmount;
        String interest = "0";
        String PlanDate = DateUtils.format(new Date());
        Integer assetType = 2;
        String requestReason = "3";
        String repayScheduleNo = UUID.randomUUID().toString();
        String contractNo = "";
        String result = refactorMethod.modifyRepaymentPlanForOne(uniqueId, PlanAmount, interest, PlanDate, assetType, requestReason, repayScheduleNo, contractNo);
        boolean outcome = result.contains("成功");
        String planCount = refactorMethod.queryRepaymentPlanCount(uniqueId);
        Assert.assertEquals(outcome, true);
        Assert.assertEquals(planCount,"1");
    }

    /**
     * 冒烟测试-多笔变更成功
     */
    @Test
    public void checkParams27(){
        String uniqueId = this.uniqueId;
        String firstPlanAmount = String.valueOf((Double.valueOf(totalAmount)/3)*2);
        String secondPlanAmount = String.valueOf(Double.valueOf(totalAmount)/3);
        String interest = "0";
        String firstPlanDate = DateUtils.format(new Date());
        String secondPlanDate = DateUtils.format(DateUtils.addDays(new Date(),1));
        Integer firstAssetType = 1;
        Integer secondAssetType = 1;
        String requestReason = "3";
        String firstRepayScheduleNo = UUID.randomUUID().toString();
        String secondRepayScheduleNo = UUID.randomUUID().toString();
        String result = refactorMethod.modifyRepaymentPlanForTwo(uniqueId,firstPlanAmount,secondPlanAmount,interest,firstPlanDate,secondPlanDate,firstAssetType,secondAssetType,requestReason,firstRepayScheduleNo,secondRepayScheduleNo);
        boolean outcome = result.contains("成功");
        String planCount = refactorMethod.queryRepaymentPlanCount(uniqueId);
        Assert.assertEquals(outcome, true);
        Assert.assertEquals(planCount,"2");
    }

    /**
     * 业务校验-对还款成功的贷款合同做变更
     */
    @Test
    public void checkParams28() throws Exception{
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
        String repayScheduleNo = UUID.randomUUID().toString();
        String contractNo = "";
        String repayScheduleNo1 = "";

        String result = refactorMethod.applyRepaymentOderForOneDetail(orderRequestNo, orderRequestNo, transType, productCode, orderAmount, uniqueId,
                "", firstRepaymentWay,
                firstDetailTotalAmount, firstDetailPrincipal,
                firstDetailInterest, firstFeeType,repayScheduleNo1);

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

        String firstPlanNo = refactorMethod.queryRepaymentPlan(productCode,uniqueId,0);
        AssetSet assetSet = repaymentPlanService.getRepaymentPlanByRepaymentCode(firstPlanNo);
        if(!PaymentStatus.SUCCESS.equals(assetSet.getPaymentStatus())){
            throw new Exception("还款计划状态不正确");
        }
//        Thread.sleep(5000l);
        String modifyResult2 = refactorMethod.modifyRepaymentPlanForOne(uniqueId,orderAmount,interest,planDate,AssetType,requestReason,repayScheduleNo+1,contractNo);
        boolean outcome = modifyResult2.contains("当前贷款合同无法变更");
        String planCount = refactorMethod.queryRepaymentPlanCount(uniqueId);
        Assert.assertEquals(outcome, true);
        Assert.assertEquals(planCount,"1");
    }

    /**
     * 业务校验-对回购中的贷款合同做变更
     */
    @Test
    public void checkParams29() throws Exception{
        String uniqueId = this.uniqueId;
        String PlanAmount = totalAmount;
        String interest = "0";
        String PlanDate = DateUtils.format(DateUtils.addMonths(new Date(),3));
        Integer assetType = 2;
        String requestReason = "3";
        String repayScheduleNo = UUID.randomUUID().toString();
        String contractNo = "";
        refactorMethod.applyRepurchase(productCode,uniqueId,PlanAmount,interest,PlanAmount);
        String result = refactorMethod.modifyRepaymentPlanForOne(uniqueId, PlanAmount, interest, PlanDate, assetType, requestReason, repayScheduleNo, contractNo);
        String repurchaseDocUuid = repurchaseService.getRepurchaseDocUuidBy(uniqueId);
        if (null == repurchaseDocUuid || "".equals(repurchaseDocUuid)){
            throw new Exception("回购单不存在");
        }
        boolean outcome = result.contains("当前贷款合同无法变更");
        String planCount = refactorMethod.queryRepaymentPlanCount(uniqueId);
        Assert.assertEquals(outcome, true);
        Assert.assertEquals(planCount,"0");
    }

    /**
     * 业务校验-变更还款计划中包含支付中的还款计划
     */
    @Test
    public void checkParams30() throws Exception{
        String uniqueId = this.uniqueId;
        String PlanAmount = totalAmount;
        String interest = "0";
        String PlanDate = DateUtils.format(DateUtils.addMonths(new Date(),3));
        Integer assetType = 2;
        String requestReason = "3";
        String repayScheduleNo = UUID.randomUUID().toString();
        String contractNo = "";

        String orderRequestNo = uniqueId;
        String transType = "0";
        String orderAmount = "10080";
        Long firstRepaymentWay = 1000l;
        String firstDetailTotalAmount = "10080";
        String firstDetailPrincipal = "10000";
        String firstDetailInterest = "20";
        Long firstFeeType = 1l;
        String repayScheduleNo1 = "";

        String firstRepaymentNumber = refactorMethod.queryRepaymentPlan(productCode,uniqueId,0);
        String result = refactorMethod.applyRepaymentOderForOneDetail(orderRequestNo,orderRequestNo,transType,productCode,orderAmount,uniqueId,
                firstRepaymentNumber,firstRepaymentWay,
                firstDetailTotalAmount,firstDetailPrincipal,
                firstDetailInterest,firstFeeType,repayScheduleNo1);


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
        if(repaymentStatus != RepaymentStatus.VERIFICATION_SUCCESS){
            throw new Exception("还款订单状态有误");
        }
        String modifyResult = refactorMethod.modifyRepaymentPlanForOne(uniqueId, PlanAmount, interest, PlanDate, assetType, requestReason, repayScheduleNo, contractNo);
        boolean outcome = modifyResult.contains("处于 支付中 的还款计划不允许变更");
        String planCount = refactorMethod.queryRepaymentPlanCount(uniqueId);
        Assert.assertEquals(outcome, true);
        Assert.assertEquals(planCount,"3");
    }

    /**
     * 业务校验-变更还款计划中不包含支付中的还款计划
     */
    @Test
    public void checkParams31() throws Exception{
        String uniqueId = this.uniqueId;
        String PlanAmount = String.valueOf((Double.valueOf(totalAmount)/3)*2);
        String interest = "0";
        String PlanDate = DateUtils.format(DateUtils.addMonths(new Date(),3));
        Integer assetType = 2;
        String requestReason = "3";
        String repayScheduleNo = UUID.randomUUID().toString();
        String contractNo = "";

        String orderRequestNo = uniqueId;
        String transType = "0";
        String orderAmount = "10080";
        Long firstRepaymentWay = 1000l;
        String firstDetailTotalAmount = "10080";
        String firstDetailPrincipal = "10000";
        String firstDetailInterest = "20";
        Long firstFeeType = 1l;
        String repayScheduleNo1 = "";

        String firstRepaymentNumber = refactorMethod.queryRepaymentPlan(productCode,uniqueId,0);
        String result = refactorMethod.applyRepaymentOderForOneDetail(orderRequestNo,orderRequestNo,transType,productCode,orderAmount,uniqueId,
                firstRepaymentNumber,firstRepaymentWay,
                firstDetailTotalAmount,firstDetailPrincipal,
                firstDetailInterest,firstFeeType,repayScheduleNo1);


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
        if(repaymentStatus != RepaymentStatus.VERIFICATION_SUCCESS){
            throw new Exception("还款订单状态有误");
        }
        String modifyResult = refactorMethod.modifyRepaymentPlanForOne(uniqueId, PlanAmount, interest, PlanDate, assetType, requestReason, repayScheduleNo, contractNo);
        boolean outcome = modifyResult.contains("处于 支付中 的还款计划不允许变更");
        String planCount = refactorMethod.queryRepaymentPlanCount(uniqueId);
        Assert.assertEquals(outcome, true);
        Assert.assertEquals(planCount,"3");
    }

}
