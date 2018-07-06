package com.zufangbao.aboutPrepayment;


import com.zufangbao.refactor.method.RefactorMethod;
import com.zufangbao.sun.service.PrepaymentApplicationService;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.yunxin.entity.*;
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
public class CheckPrepaymentBusiness {
    RefactorMethod refactorMethod = new RefactorMethod();

    @Autowired
    private RepaymentPlanService repaymentPlanService;

    @Autowired
    private PrepaymentApplicationService prepaymentApplicationService;

    private String uniqueId = "";
    String productCode = "CS0001";
    String totalAmount = "30000";
    String firstRepaymentPlan = "";
    String interest = "";

    @Before
    public void setUp() {
        refactorMethod.deleteAllCashFlow();
        uniqueId = UUID.randomUUID().toString();
        String repaymentAccountNo = "6217857600016839330";
        String loanCustomerName = "秦操";
        interest = "20";
        String loanCustomerNo = "11111117";
        String firstPlanDate = DateUtils.format(DateUtils.addMonths(new Date(), 1));
        String secondPlanDate = DateUtils.format(DateUtils.addMonths(new Date(), 2));
        String thirdPlanDate = DateUtils.format(DateUtils.addMonths(new Date(), 3));

        refactorMethod.importAssetPackage(totalAmount, productCode, uniqueId, repaymentAccountNo, interest, loanCustomerNo,
                firstPlanDate, secondPlanDate, thirdPlanDate, loanCustomerName);
        firstRepaymentPlan = refactorMethod.queryRepaymentPlan(productCode,uniqueId,0);
    }

    /**
     * 提前还款业务校验-申请提前还款且申请日期在当日之前
     */
    @Test
    public void checkParams1(){
        String assetPrincipal = this.totalAmount;
        String interest = this.interest;
        String assetInitialValue = String.valueOf(Double.valueOf(assetPrincipal)+Double.valueOf(interest)*4);
        String contractNo = "";
        String type = "0";
        String applyDate = DateUtils.format(DateUtils.addDays(new Date(),-1));
        String result = refactorMethod.applyPrepaymentPlan(uniqueId,contractNo,applyDate,assetInitialValue,assetPrincipal,interest,type);
        boolean outcome = result.contains("日期不应早于当日");
        String repaymentCount = refactorMethod.queryRepaymentPlanCount(this.uniqueId);
        Assert.assertEquals(true,outcome);
        Assert.assertEquals("3",repaymentCount);
    }

    /**
     * 提前还款业务校验-申请提前还款且申请日期在第一期之后
     */
    @Test
    public void checkParams2(){
        String firstPlanDate = DateUtils.format(DateUtils.addMonths(new Date(), 1));
        String assetPrincipal = totalAmount;
        String interest = this.interest;
        String assetInitialValue = String.valueOf(Double.valueOf(assetPrincipal)+Double.valueOf(interest)*4);
        String contractNo = "";
        String type = "0";
        String applyDate = DateUtils.format(DateUtils.addDays(DateUtils.parseDate(firstPlanDate), 1));
        String result = refactorMethod.applyPrepaymentPlan(uniqueId,contractNo,applyDate,assetInitialValue,assetPrincipal,interest,type);
        boolean outcome = result.contains("提前还款日期错误");
        String repaymentCount = refactorMethod.queryRepaymentPlanCount(this.uniqueId);
        Assert.assertEquals(true,outcome);
        Assert.assertEquals("3",repaymentCount);
    }

    /**
     * 提前还款业务校验-申请提前还款且提前还款金额小于未还本金
     */
    @Test
    public void checkParams3(){
        String assetPrincipal = String.valueOf(Double.valueOf(totalAmount)-100);
        String interest = this.interest;
        String assetInitialValue = String.valueOf(Double.valueOf(assetPrincipal)+Double.valueOf(interest)*4);
        String contractNo = "";
        String type = "0";
        String applyDate = DateUtils.format(new Date());
        String result = refactorMethod.applyPrepaymentPlan(uniqueId,contractNo,applyDate,assetInitialValue,assetPrincipal,interest,type);
        boolean outcome = result.contains("提前还款本金应与未偿本金总额一致");
        String repaymentCount = refactorMethod.queryRepaymentPlanCount(this.uniqueId);
        Assert.assertEquals(true,outcome);
        Assert.assertEquals("3",repaymentCount);
    }

    /**
     * 提前还款业务校验-申请提前还款且提前还款金额大于未还本金
     */
    @Test
    public void checkParams4(){
        String assetPrincipal = String.valueOf(Double.valueOf(totalAmount)+100);
        String interest = this.interest;
        String assetInitialValue = String.valueOf(Double.valueOf(assetPrincipal)+Double.valueOf(interest)*4);
        String contractNo = "";
        String type = "0";
        String applyDate = DateUtils.format(new Date());
        String result = refactorMethod.applyPrepaymentPlan(uniqueId,contractNo,applyDate,assetInitialValue,assetPrincipal,interest,type);
        boolean outcome = result.contains("提前还款本金应与未偿本金总额一致");
        String repaymentCount = refactorMethod.queryRepaymentPlanCount(this.uniqueId);
        Assert.assertEquals(true,outcome);
        Assert.assertEquals("3",repaymentCount);
    }

    /**
     * 提前还款业务校验-申请提前还款且各项参数都正确
     */
    @Test
    public void checkParams5(){
        String assetPrincipal = totalAmount;
        String interest = this.interest;
        String assetInitialValue = String.valueOf(Double.valueOf(assetPrincipal)+Double.valueOf(interest)*4);
        String contractNo = "";
        String type = "0";
        String applyDate = DateUtils.format(new Date());
        String result = refactorMethod.applyPrepaymentPlan(uniqueId,contractNo,applyDate,assetInitialValue,assetPrincipal,interest,type);
        boolean outcome = result.contains("成功");
        String repaymentCount = refactorMethod.queryRepaymentPlanCount(this.uniqueId);
        Assert.assertEquals(true,outcome);
        Assert.assertEquals("1",repaymentCount);
    }

    /**
     * 提前还款业务校验-对第一期未到期的还款计划做全额提前划拨且扣款完成
     * 然后对后面的还款计划做提前还款
     */
    @Test
    public void checkParams6() throws Exception{
        String assetPrincipal = String.valueOf(Double.valueOf(totalAmount)/3*2);
        String interest = this.interest;
        String assetInitialValue = String.valueOf(Double.valueOf(assetPrincipal)+Double.valueOf(interest)*4);
        String contractNo = "";
        String type = "0";
        String applyDate = DateUtils.format(new Date());

        String payAcNo = "6217857600016839330";
        String repaymentType = "0";
        String principal = String.valueOf(Double.valueOf(totalAmount)/3);
        String deductInterest = this.interest;
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
        String result = refactorMethod.applyPrepaymentPlan(uniqueId,contractNo,applyDate,assetInitialValue,assetPrincipal,interest,type);
        boolean outcome = result.contains("成功");
        String prepaymentNumber = prepaymentApplicationService.getPrepaymentNumber(this.uniqueId);
        if(null == prepaymentNumber){
            throw new Exception("无提前还款计划");
        }
        assetSet = repaymentPlanService.getRepaymentPlanByRepaymentCode(prepaymentNumber);
        String repaymentCount = refactorMethod.queryRepaymentPlanCount(this.uniqueId);
        Assert.assertEquals(true,outcome);
        Assert.assertEquals("2",repaymentCount);
        Assert.assertEquals(AssetSetActiveStatus.OPEN,assetSet.getActiveStatus());
    }

    /**
     * 提前还款业务校验-对第一期未到期的还款计划做全额提前划拨且正在扣款中
     * 然后对后面的还款计划做提前还款且申请日期在第一期还款计划日期之前
     * 预期结果与实际结果不一致，需开发确认业务逻辑
     */
    @Test
    public void checkParams7() throws Exception{
        String firstPlanDate = DateUtils.format(DateUtils.addMonths(new Date(), 1));
        String assetPrincipal = String.valueOf(Double.valueOf(totalAmount)/3*2);
        String interest = this.interest;
        String assetInitialValue = String.valueOf(Double.valueOf(assetPrincipal)+Double.valueOf(interest)*4);
        String contractNo = "";
        String type = "0";
        String applyDate = DateUtils.format(DateUtils.addDays(DateUtils.parseDate(firstPlanDate),-1));

        String payAcNo = "6217857600016839330";
        String repaymentType = "0";
        String principal = String.valueOf(Double.valueOf(totalAmount)/3);
        String deductInterest = this.interest;
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
        Thread.sleep(3000);
        String result = refactorMethod.applyPrepaymentPlan(uniqueId,contractNo,applyDate,assetInitialValue,assetPrincipal,interest,type);
        boolean outcome = result.contains("提前还款日期错误");
        String repaymentCount = refactorMethod.queryRepaymentPlanCount(this.uniqueId);
        Assert.assertEquals(true,outcome);
        Assert.assertEquals("3",repaymentCount);
        Assert.assertEquals(AssetSetActiveStatus.OPEN,assetSet.getActiveStatus());
    }

    /**
     * 提前还款业务校验-对第一期未到期的还款计划做全额提前划拨且正在扣款中
     * 然后对后面的还款计划做提前还款且申请日期在第一期还款计划日期后
     * 当首期还款计划还款成功后，查询提前还款计划状态
     * 预期结果与实际结果不一致，需开发确认业务逻辑
     */
    @Test
    public void checkParams8() throws Exception{
        String firstPlanDate = DateUtils.format(DateUtils.addMonths(new Date(), 1));
        String assetPrincipal = String.valueOf(Double.valueOf(totalAmount)/3*2);
        String interest = this.interest;
        String assetInitialValue = String.valueOf(Double.valueOf(assetPrincipal)+Double.valueOf(interest)*4);
        String contractNo = "";
        String type = "0";
        String applyDate = DateUtils.format(DateUtils.addDays(DateUtils.parseDate(firstPlanDate),2));

        String payAcNo = "6217857600016839330";
        String repaymentType = "0";
        String principal = String.valueOf(Double.valueOf(totalAmount)/3);
        String deductInterest = this.interest;
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
        Thread.sleep(3000);
        String result = refactorMethod.applyPrepaymentPlan(uniqueId,contractNo,applyDate,assetInitialValue,assetPrincipal,interest,type);
        if(!result.contains("成功")){
            throw new Exception("申请提前还款失败");
        }
        String prepaymentNumber = prepaymentApplicationService.getPrepaymentNumber(this.uniqueId);
        if(null == prepaymentNumber){
            throw new Exception("无提前还款计划");
        }
        AssetSet assetSet1 = repaymentPlanService.getRepaymentPlanByRepaymentCode(prepaymentNumber);
        while (true){
            assetSet = repaymentPlanService.getRepaymentPlanByRepaymentCode(firstPlanNo);
            if(PaymentStatus.SUCCESS.equals(assetSet.getPaymentStatus())){
                break;
            }
            Thread.sleep(10000l);
        }
        AssetSet assetSet2 = repaymentPlanService.getRepaymentPlanByRepaymentCode(prepaymentNumber);
        String repaymentCount = refactorMethod.queryRepaymentPlanCount(this.uniqueId);
        Assert.assertEquals("2",repaymentCount);
        Assert.assertEquals(AssetSetActiveStatus.FROZEN,assetSet1.getActiveStatus());
        Assert.assertEquals(AssetSetActiveStatus.OPEN,assetSet2.getActiveStatus());
    }

    /**
     * 提前还款业务校验-对第一期未到期的还款计划做全额提前划拨且正在扣款中
     * 然后对后面的还款计划做提前还款且申请日期在第一期还款计划日期后
     * 当首期还款计划还款失败后，查询提前还款计划状态
     * 预期结果与实际结果不一致，需开发确认业务逻辑
     */
    @Test
    public void checkParams9() throws Exception{
        String firstPlanDate = DateUtils.format(DateUtils.addMonths(new Date(), 1));
        String assetPrincipal = String.valueOf(Double.valueOf(totalAmount)/3*2);
        String interest = this.interest;
        String assetInitialValue = String.valueOf(Double.valueOf(assetPrincipal)+Double.valueOf(interest)*4);
        String contractNo = "";
        String type = "0";
        String applyDate = DateUtils.format(DateUtils.addDays(DateUtils.parseDate(firstPlanDate),2));

        String payAcNo = "6217857600016839337";
        String repaymentType = "0";
        String principal = String.valueOf(Double.valueOf(totalAmount)/3);
        String deductInterest = this.interest;
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
        Thread.sleep(3000);
        String result = refactorMethod.applyPrepaymentPlan(uniqueId,contractNo,applyDate,assetInitialValue,assetPrincipal,interest,type);
        if(!result.contains("成功")){
            throw new Exception("申请提前还款失败");
        }
        String prepaymentNumber = prepaymentApplicationService.getPrepaymentNumber(this.uniqueId);
        if(null == prepaymentNumber){
            throw new Exception("无提前还款计划");
        }
        AssetSet assetSet1 = repaymentPlanService.getRepaymentPlanByRepaymentCode(prepaymentNumber);
        while (true){
            assetSet = repaymentPlanService.getRepaymentPlanByRepaymentCode(firstPlanNo);
            ExecutingStatus excutingStatus = assetSet.getExecutingStatus();
            if(ExecutingStatus.UNEXECUTED.equals(excutingStatus)){
                break;
            }
            Thread.sleep(10000l);
        }
        AssetSet assetSet2 = repaymentPlanService.getRepaymentPlanByRepaymentCode(prepaymentNumber);
        String repaymentCount = refactorMethod.queryRepaymentPlanCount(this.uniqueId);
        Assert.assertEquals("2",repaymentCount);
        Assert.assertEquals(AssetSetActiveStatus.FROZEN,assetSet1.getActiveStatus());
        Assert.assertEquals(AssetSetActiveStatus.FROZEN,assetSet2.getActiveStatus());
    }

    /**
     * 提前还款业务校验-对第一期未到期的还款计划做全额提前划拨且正在扣款中
     * 然后对后面的还款计划做提前还款且申请日期在第一期还款计划日期后
     * 预期结果与实际结果不一致，需开发确认业务逻辑
    **/
    @Test
    public void checkParams10() throws Exception{
        String firstPlanDate = DateUtils.format(DateUtils.addMonths(new Date(), 1));
        String assetPrincipal = String.valueOf(Double.valueOf(totalAmount)/3*2);
        String interest = this.interest;
        String assetInitialValue = String.valueOf(Double.valueOf(assetPrincipal)+Double.valueOf(interest)*4);
        String contractNo = "";
        String type = "0";
        String applyDate = DateUtils.format(DateUtils.addDays(DateUtils.parseDate(firstPlanDate),2));

        String payAcNo = "6217857600016839330";
        String repaymentType = "0";
        String principal = String.valueOf(Double.valueOf(totalAmount)/3);
        String deductInterest = this.interest;
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
        Thread.sleep(3000);
        String result = refactorMethod.applyPrepaymentPlan(uniqueId,contractNo,applyDate,assetInitialValue,assetPrincipal,interest,type);
        if(!result.contains("成功")){
            throw new Exception("申请提前还款失败");
        }
        String prepaymentNumber = prepaymentApplicationService.getPrepaymentNumber(this.uniqueId);
        if(null == prepaymentNumber){
            throw new Exception("无提前还款计划");
        }
        AssetSet assetSet1 = repaymentPlanService.getRepaymentPlanByRepaymentCode(prepaymentNumber);
        String repaymentCount = refactorMethod.queryRepaymentPlanCount(this.uniqueId);
        Assert.assertEquals("2",repaymentCount);
        Assert.assertEquals(AssetSetActiveStatus.FROZEN,assetSet1.getActiveStatus());
    }

    @Test
    public void checkParams11() throws Exception{
        String assetPrincipal = totalAmount;
        String interest = this.interest;
        String assetInitialValue = String.valueOf(Double.valueOf(assetPrincipal)+Double.valueOf(interest)*4);
        String contractNo = "";
        String type = "0";
        String applyDate = DateUtils.format(DateUtils.addDays(new Date(),1));

        String payAcNo = "6217857600016839337";
        String repaymentType = "0";
        String principal = String.valueOf(Double.valueOf(totalAmount)/3);
        String deductInterest = this.interest;
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
        while(true) {
            assetSet = repaymentPlanService.getRepaymentPlanByRepaymentCode(firstPlanNo);
            if(ExecutingStatus.UNEXECUTED.equals(assetSet.getExecutingStatus())){
                break;
            }
        }
        Thread.sleep(3000);
        String result = refactorMethod.applyPrepaymentPlan(uniqueId,contractNo,applyDate,assetInitialValue,assetPrincipal,interest,type);
        if(!result.contains("成功")){
            throw new Exception("申请提前还款失败");
        }
        String prepaymentNumber = prepaymentApplicationService.getPrepaymentNumber(this.uniqueId);
        if(null == prepaymentNumber){
            throw new Exception("无提前还款计划");
        }
        String deductResult2 = refactorMethod.dedcutRepaymentPlan(productCode,uniqueId,assetInitialValue,payAcNo,repaymentType,prepaymentNumber,assetPrincipal,
                deductInterest,overDueFee,totalOverDueFee);
        if(!deductResult2.contains("成功")){
            throw new Exception("扣款参数校验失败");
        }
        while(true){
            AssetSet assetSet1 = repaymentPlanService.getRepaymentPlanByRepaymentCode(prepaymentNumber);
            AssetSetActiveStatus activeStatus = assetSet1.getActiveStatus();
            if(AssetSetActiveStatus.INVALID == activeStatus){
                break;
            }
            Thread.sleep(10000);
        }
        String repaymentCount = refactorMethod.queryRepaymentPlanCount(this.uniqueId);
        Assert.assertEquals("3",repaymentCount);
    }


}
