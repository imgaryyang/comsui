package com.zufangbao.aboutMutableFee;

import com.demo2do.core.utils.DateUtils;
import com.zufangbao.refactor.method.RefactorMethod;
import com.zufangbao.sun.entity.repayment.order.RepaymentOrder;
import com.zufangbao.sun.entity.repayment.order.RepaymentStatus;
import com.zufangbao.sun.service.RepaymentOrderService;
import com.zufangbao.sun.yunxin.entity.AssetSet;
import com.zufangbao.sun.yunxin.entity.DeductionStatus;
import com.zufangbao.sun.yunxin.entity.ExecutingStatus;
import com.zufangbao.sun.yunxin.entity.PaymentStatus;
import com.zufangbao.sun.yunxin.entity.model.api.ExtraChargeSpec;
import com.zufangbao.sun.yunxin.service.RepaymentPlanExtraChargeService;
import com.zufangbao.sun.yunxin.service.RepaymentPlanService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:/local/applicationContext-*.xml" })
public class CheckMutableFeeParams {
    @Autowired
    private RepaymentPlanService repaymentPlanService;

    @Autowired
    private RepaymentPlanExtraChargeService repaymentPlanExtraChargeService;

    @Autowired
    private RepaymentOrderService repaymentOrderService;

    RefactorMethod refactorMethod = new RefactorMethod();

    private String uniqueId = "";
    String productCode = "CS0001";
    String totalAmount = "30000";
    String repaymentPlanNo = "";
    String interest = "";

    @Before
    public void setUp() {
        refactorMethod.deleteAllCashFlow();
        uniqueId = UUID.randomUUID().toString();
        String repaymentAccountNo = "6217857600016839330";
        String loanCustomerName = "秦操";
        interest = "20";
        String loanCustomerNo = "11111117";
        String firstPlanDate = DateUtils.format(DateUtils.addMonths(new Date(), -1));
        String secondPlanDate = DateUtils.format(new Date());
        String thirdPlanDate = DateUtils.format(DateUtils.addMonths(new Date(), 1));

        refactorMethod.importAssetPackage(totalAmount, productCode, uniqueId, repaymentAccountNo, interest, loanCustomerNo,
                firstPlanDate, secondPlanDate, thirdPlanDate, loanCustomerName);
        repaymentPlanNo = refactorMethod.queryRepaymentPlan(productCode,uniqueId,0);
    }

    /**
     * 浮动费用接口参数校验-productCode为空
     * @throws Exception
     */
    @Test
    public void checkParams1() throws Exception{
        String productCode = "";
        String repayScheduleNo = "";
        String reasonCode = "0";
        String approvedTime = DateUtils.format(new Date());
        Integer firstFeeType = 1;
        Integer secondFeeType = 2;
        Integer thirdFeeType = 3;
        Integer fourthFeeType = 4;
        String firstInterest = "30";
        String secondInterest = "30";
        String thirdInterest = "30";
        String fourthInterest = "30";
        String result = refactorMethod.mutableFee(productCode,uniqueId,uniqueId,repaymentPlanNo,repayScheduleNo,
                reasonCode,approvedTime,firstFeeType,secondFeeType,thirdFeeType,fourthFeeType,
                firstInterest,secondInterest,thirdInterest,fourthInterest);
        AssetSet assetSet = repaymentPlanService.getRepaymentPlanByRepaymentCode(repaymentPlanNo);
        String assetSetUuid = assetSet.getAssetUuid();
        boolean outcome = result.contains("信托产品代码[financialProductCode]不能为空");
        Map<String,BigDecimal> assetSetExtraCharges = repaymentPlanExtraChargeService.getAssetSetExtraChargeModels(assetSetUuid);
        String interest = assetSet.getAssetInterestValue().stripTrailingZeros().toPlainString();
        String serviceFee = assetSetExtraCharges.get(ExtraChargeSpec.LOAN_SERVICE_FEE_KEY).stripTrailingZeros().toPlainString();
        String tecFee = assetSetExtraCharges.get(ExtraChargeSpec.LOAN_TECH_FEE_KEY).stripTrailingZeros().toPlainString();
        String otherFee = assetSetExtraCharges.get(ExtraChargeSpec.LOAN_OTHER_FEE_KEY).stripTrailingZeros().toPlainString();
        Assert.assertEquals(true,outcome);
        Assert.assertEquals("20",interest);
        Assert.assertEquals("20",serviceFee);
        Assert.assertEquals("20",tecFee);
        Assert.assertEquals("20",otherFee);
    }

    /**
     * 浮动费用接口参数校验-productCode不匹配
     * @throws Exception
     */
    @Test
    public void checkParams2() throws Exception{
        String productCode = "G32000";
        String repayScheduleNo = "";
        String reasonCode = "0";
        String approvedTime = DateUtils.format(new Date());
        Integer firstFeeType = 1;
        Integer secondFeeType = 2;
        Integer thirdFeeType = 3;
        Integer fourthFeeType = 4;
        String firstInterest = "30";
        String secondInterest = "30";
        String thirdInterest = "30";
        String fourthInterest = "30";
        String result = refactorMethod.mutableFee(productCode,uniqueId,uniqueId,repaymentPlanNo,repayScheduleNo,
                reasonCode,approvedTime,firstFeeType,secondFeeType,thirdFeeType,fourthFeeType,
                firstInterest,secondInterest,thirdInterest,fourthInterest);
        AssetSet assetSet = repaymentPlanService.getRepaymentPlanByRepaymentCode(repaymentPlanNo);
        String assetSetUuid = assetSet.getAssetUuid();
        boolean outcome = result.contains("信托计划与贷款合同不匹配");
        Map<String,BigDecimal> assetSetExtraCharges = repaymentPlanExtraChargeService.getAssetSetExtraChargeModels(assetSetUuid);
        String interest = assetSet.getAssetInterestValue().stripTrailingZeros().toPlainString();
        String serviceFee = assetSetExtraCharges.get(ExtraChargeSpec.LOAN_SERVICE_FEE_KEY).stripTrailingZeros().toPlainString();
        String tecFee = assetSetExtraCharges.get(ExtraChargeSpec.LOAN_TECH_FEE_KEY).stripTrailingZeros().toPlainString();
        String otherFee = assetSetExtraCharges.get(ExtraChargeSpec.LOAN_OTHER_FEE_KEY).stripTrailingZeros().toPlainString();
        Assert.assertEquals(true,outcome);
        Assert.assertEquals("20",interest);
        Assert.assertEquals("20",serviceFee);
        Assert.assertEquals("20",tecFee);
        Assert.assertEquals("20",otherFee);
    }

    /**
     * 浮动费用接口参数校验-productCode不存在
     * @throws Exception
     */
    @Test
    public void checkParams3() throws Exception{
        String productCode = "G1234";
        String repayScheduleNo = "";
        String reasonCode = "0";
        String approvedTime = DateUtils.format(new Date());
        Integer firstFeeType = 1;
        Integer secondFeeType = 2;
        Integer thirdFeeType = 3;
        Integer fourthFeeType = 4;
        String firstInterest = "30";
        String secondInterest = "30";
        String thirdInterest = "30";
        String fourthInterest = "30";
        String result = refactorMethod.mutableFee(productCode,uniqueId,uniqueId,repaymentPlanNo,repayScheduleNo,
                reasonCode,approvedTime,firstFeeType,secondFeeType,thirdFeeType,fourthFeeType,
                firstInterest,secondInterest,thirdInterest,fourthInterest);
        AssetSet assetSet = repaymentPlanService.getRepaymentPlanByRepaymentCode(repaymentPlanNo);
        String assetSetUuid = assetSet.getAssetUuid();
        boolean outcome = result.contains("信托合同不存在");
        Map<String,BigDecimal> assetSetExtraCharges = repaymentPlanExtraChargeService.getAssetSetExtraChargeModels(assetSetUuid);
        String interest = assetSet.getAssetInterestValue().stripTrailingZeros().toPlainString();
        String serviceFee = assetSetExtraCharges.get(ExtraChargeSpec.LOAN_SERVICE_FEE_KEY).stripTrailingZeros().toPlainString();
        String tecFee = assetSetExtraCharges.get(ExtraChargeSpec.LOAN_TECH_FEE_KEY).stripTrailingZeros().toPlainString();
        String otherFee = assetSetExtraCharges.get(ExtraChargeSpec.LOAN_OTHER_FEE_KEY).stripTrailingZeros().toPlainString();
        Assert.assertEquals(true,outcome);
        Assert.assertEquals("20",interest);
        Assert.assertEquals("20",serviceFee);
        Assert.assertEquals("20",tecFee);
        Assert.assertEquals("20",otherFee);
    }

    /**
     * 浮动费用接口参数校验-uniqueId和contractNo都为空
     * @throws Exception
     */
    @Test
    public void checkParams4() throws Exception{
        String uniqueId = "";
        String contractNo = "";
        String repayScheduleNo = "";
        String reasonCode = "0";
        String approvedTime = DateUtils.format(new Date());
        Integer firstFeeType = 1;
        Integer secondFeeType = 2;
        Integer thirdFeeType = 3;
        Integer fourthFeeType = 4;
        String firstInterest = "30";
        String secondInterest = "30";
        String thirdInterest = "30";
        String fourthInterest = "30";
        String result = refactorMethod.mutableFee(productCode,uniqueId,contractNo,repaymentPlanNo,repayScheduleNo,
                reasonCode,approvedTime,firstFeeType,secondFeeType,thirdFeeType,fourthFeeType,
                firstInterest,secondInterest,thirdInterest,fourthInterest);
        AssetSet assetSet = repaymentPlanService.getRepaymentPlanByRepaymentCode(repaymentPlanNo);
        String assetSetUuid = assetSet.getAssetUuid();
        boolean outcome = result.contains("请选填其中一种编号［uniqueId，contractNo］");
        Map<String,BigDecimal> assetSetExtraCharges = repaymentPlanExtraChargeService.getAssetSetExtraChargeModels(assetSetUuid);
        String interest = assetSet.getAssetInterestValue().stripTrailingZeros().toPlainString();
        String serviceFee = assetSetExtraCharges.get(ExtraChargeSpec.LOAN_SERVICE_FEE_KEY).stripTrailingZeros().toPlainString();
        String tecFee = assetSetExtraCharges.get(ExtraChargeSpec.LOAN_TECH_FEE_KEY).stripTrailingZeros().toPlainString();
        String otherFee = assetSetExtraCharges.get(ExtraChargeSpec.LOAN_OTHER_FEE_KEY).stripTrailingZeros().toPlainString();
        Assert.assertEquals(true,outcome);
        Assert.assertEquals("20",interest);
        Assert.assertEquals("20",serviceFee);
        Assert.assertEquals("20",tecFee);
        Assert.assertEquals("20",otherFee);
    }

    /**
     * 浮动费用接口参数校验-uniqueId错误，contractNo正确
     * @throws Exception
     */
    @Test
    public void checkParams5() throws Exception{
        String uniqueId = this.uniqueId+1;
        String contractNo = this.uniqueId;
        String repayScheduleNo = "";
        String reasonCode = "0";
        String approvedTime = DateUtils.format(new Date());
        Integer firstFeeType = 1;
        Integer secondFeeType = 2;
        Integer thirdFeeType = 3;
        Integer fourthFeeType = 4;
        String firstInterest = "30";
        String secondInterest = "30";
        String thirdInterest = "30";
        String fourthInterest = "30";
        String result = refactorMethod.mutableFee(productCode,uniqueId,contractNo,repaymentPlanNo,repayScheduleNo,
                reasonCode,approvedTime,firstFeeType,secondFeeType,thirdFeeType,fourthFeeType,
                firstInterest,secondInterest,thirdInterest,fourthInterest);
        AssetSet assetSet = repaymentPlanService.getRepaymentPlanByRepaymentCode(repaymentPlanNo);
        String assetSetUuid = assetSet.getAssetUuid();
        boolean outcome = result.contains("贷款合同不存在");
        Map<String,BigDecimal> assetSetExtraCharges = repaymentPlanExtraChargeService.getAssetSetExtraChargeModels(assetSetUuid);
        String interest = assetSet.getAssetInterestValue().stripTrailingZeros().toPlainString();
        String serviceFee = assetSetExtraCharges.get(ExtraChargeSpec.LOAN_SERVICE_FEE_KEY).stripTrailingZeros().toPlainString();
        String tecFee = assetSetExtraCharges.get(ExtraChargeSpec.LOAN_TECH_FEE_KEY).stripTrailingZeros().toPlainString();
        String otherFee = assetSetExtraCharges.get(ExtraChargeSpec.LOAN_OTHER_FEE_KEY).stripTrailingZeros().toPlainString();
        Assert.assertEquals(true,outcome);
        Assert.assertEquals("20",interest);
        Assert.assertEquals("20",serviceFee);
        Assert.assertEquals("20",tecFee);
        Assert.assertEquals("20",otherFee);
    }


    /**
     * 浮动费用接口参数校验-uniqueId正确，contractNo错误
     * @throws Exception
     */
    @Test
    public void checkParams6() throws Exception{
        String uniqueId = this.uniqueId;
        String contractNo = this.uniqueId+1;
        String repayScheduleNo = "";
        String reasonCode = "0";
        String approvedTime = DateUtils.format(new Date());
        Integer firstFeeType = 1;
        Integer secondFeeType = 2;
        Integer thirdFeeType = 3;
        Integer fourthFeeType = 4;
        String firstInterest = "30";
        String secondInterest = "30";
        String thirdInterest = "30";
        String fourthInterest = "30";
        String result = refactorMethod.mutableFee(productCode,uniqueId,contractNo,repaymentPlanNo,repayScheduleNo,
                reasonCode,approvedTime,firstFeeType,secondFeeType,thirdFeeType,fourthFeeType,
                firstInterest,secondInterest,thirdInterest,fourthInterest);
        AssetSet assetSet = repaymentPlanService.getRepaymentPlanByRepaymentCode(repaymentPlanNo);
        String assetSetUuid = assetSet.getAssetUuid();
        boolean outcome = result.contains("成功");
        Map<String,BigDecimal> assetSetExtraCharges = repaymentPlanExtraChargeService.getAssetSetExtraChargeModels(assetSetUuid);
        String interest = assetSet.getAssetInterestValue().stripTrailingZeros().toPlainString();
        String serviceFee = assetSetExtraCharges.get(ExtraChargeSpec.LOAN_SERVICE_FEE_KEY).stripTrailingZeros().toPlainString();
        String tecFee = assetSetExtraCharges.get(ExtraChargeSpec.LOAN_TECH_FEE_KEY).stripTrailingZeros().toPlainString();
        String otherFee = assetSetExtraCharges.get(ExtraChargeSpec.LOAN_OTHER_FEE_KEY).stripTrailingZeros().toPlainString();
        Assert.assertEquals(true,outcome);
        Assert.assertEquals("30",interest);
        Assert.assertEquals("30",serviceFee);
        Assert.assertEquals("30",tecFee);
        Assert.assertEquals("30",otherFee);
    }

    /**
     * 浮动费用接口参数校验-只填contractNo
     * @throws Exception
     */
    @Test
    public void checkParams7() throws Exception{
        String uniqueId = "";
        String contractNo = this.uniqueId;
        String repayScheduleNo = "";
        String reasonCode = "0";
        String approvedTime = DateUtils.format(new Date());
        Integer firstFeeType = 1;
        Integer secondFeeType = 2;
        Integer thirdFeeType = 3;
        Integer fourthFeeType = 4;
        String firstInterest = "30";
        String secondInterest = "30";
        String thirdInterest = "30";
        String fourthInterest = "30";
        String result = refactorMethod.mutableFee(productCode,uniqueId,contractNo,repaymentPlanNo,repayScheduleNo,
                reasonCode,approvedTime,firstFeeType,secondFeeType,thirdFeeType,fourthFeeType,
                firstInterest,secondInterest,thirdInterest,fourthInterest);
        AssetSet assetSet = repaymentPlanService.getRepaymentPlanByRepaymentCode(repaymentPlanNo);
        String assetSetUuid = assetSet.getAssetUuid();
        boolean outcome = result.contains("成功");
        Map<String,BigDecimal> assetSetExtraCharges = repaymentPlanExtraChargeService.getAssetSetExtraChargeModels(assetSetUuid);
        String interest = assetSet.getAssetInterestValue().stripTrailingZeros().toPlainString();
        String serviceFee = assetSetExtraCharges.get(ExtraChargeSpec.LOAN_SERVICE_FEE_KEY).stripTrailingZeros().toPlainString();
        String tecFee = assetSetExtraCharges.get(ExtraChargeSpec.LOAN_TECH_FEE_KEY).stripTrailingZeros().toPlainString();
        String otherFee = assetSetExtraCharges.get(ExtraChargeSpec.LOAN_OTHER_FEE_KEY).stripTrailingZeros().toPlainString();
        Assert.assertEquals(true,outcome);
        Assert.assertEquals("30",interest);
        Assert.assertEquals("30",serviceFee);
        Assert.assertEquals("30",tecFee);
        Assert.assertEquals("30",otherFee);
    }

    /**
     * 浮动费用接口参数校验-repaymentPlanNo和repayScheduleNo都不填
     * @throws Exception
     */
    @Test
    public void checkParams8() throws Exception{
        String repaymentPlanNo = "";
        String repayScheduleNo = "";
        String reasonCode = "0";
        String approvedTime = DateUtils.format(new Date());
        Integer firstFeeType = 1;
        Integer secondFeeType = 2;
        Integer thirdFeeType = 3;
        Integer fourthFeeType = 4;
        String firstInterest = "30";
        String secondInterest = "30";
        String thirdInterest = "30";
        String fourthInterest = "30";
        String result = refactorMethod.mutableFee(productCode,uniqueId,uniqueId,repaymentPlanNo,repayScheduleNo,
                reasonCode,approvedTime,firstFeeType,secondFeeType,thirdFeeType,fourthFeeType,
                firstInterest,secondInterest,thirdInterest,fourthInterest);
        AssetSet assetSet = repaymentPlanService.getRepaymentPlanByRepaymentCode(this.repaymentPlanNo);
        String assetSetUuid = assetSet.getAssetUuid();
        boolean outcome = result.contains("请选填其中一种编号［repayScheduleNo，repaymentPlanNo］");
        Map<String,BigDecimal> assetSetExtraCharges = repaymentPlanExtraChargeService.getAssetSetExtraChargeModels(assetSetUuid);
        String interest = assetSet.getAssetInterestValue().stripTrailingZeros().toPlainString();
        String serviceFee = assetSetExtraCharges.get(ExtraChargeSpec.LOAN_SERVICE_FEE_KEY).stripTrailingZeros().toPlainString();
        String tecFee = assetSetExtraCharges.get(ExtraChargeSpec.LOAN_TECH_FEE_KEY).stripTrailingZeros().toPlainString();
        String otherFee = assetSetExtraCharges.get(ExtraChargeSpec.LOAN_OTHER_FEE_KEY).stripTrailingZeros().toPlainString();
        Assert.assertEquals(true,outcome);
        Assert.assertEquals("20",interest);
        Assert.assertEquals("20",serviceFee);
        Assert.assertEquals("20",tecFee);
        Assert.assertEquals("20",otherFee);
    }

    /**
     * 浮动费用接口参数校验-repaymentPlanNo错误，repayScheduleNo正确
     * @throws Exception
     */
    @Test
    public void checkParams9() throws Exception{
        String repaymentPlanNo = this.repaymentPlanNo+1;
        String reasonCode = "0";
        String approvedTime = DateUtils.format(new Date());
        Integer firstFeeType = 1;
        Integer secondFeeType = 2;
        Integer thirdFeeType = 3;
        Integer fourthFeeType = 4;
        String firstInterest = "30";
        String secondInterest = "30";
        String thirdInterest = "30";
        String fourthInterest = "30";
        AssetSet assetSet = repaymentPlanService.getRepaymentPlanByRepaymentCode(this.repaymentPlanNo);
        if (null == assetSet){
            throw new Exception("还款计划不存在");
        }
        String repayScheduleNo = assetSet.getOuterRepaymentPlanNo();
        if (null == repayScheduleNo || "".equals(repayScheduleNo)){
            throw new Exception("商户还款计划编号不存在");
        }
        String result = refactorMethod.mutableFee(productCode,uniqueId,uniqueId,repaymentPlanNo,repayScheduleNo,
                reasonCode,approvedTime,firstFeeType,secondFeeType,thirdFeeType,fourthFeeType,
                firstInterest,secondInterest,thirdInterest,fourthInterest);
        assetSet = repaymentPlanService.getRepaymentPlanByRepaymentCode(this.repaymentPlanNo);
        String assetSetUuid = assetSet.getAssetUuid();
        boolean outcome = result.contains("成功");
        Map<String,BigDecimal> assetSetExtraCharges = repaymentPlanExtraChargeService.getAssetSetExtraChargeModels(assetSetUuid);
        String interest = assetSet.getAssetInterestValue().stripTrailingZeros().toPlainString();
        String serviceFee = assetSetExtraCharges.get(ExtraChargeSpec.LOAN_SERVICE_FEE_KEY).stripTrailingZeros().toPlainString();
        String tecFee = assetSetExtraCharges.get(ExtraChargeSpec.LOAN_TECH_FEE_KEY).stripTrailingZeros().toPlainString();
        String otherFee = assetSetExtraCharges.get(ExtraChargeSpec.LOAN_OTHER_FEE_KEY).stripTrailingZeros().toPlainString();
        Assert.assertEquals(true,outcome);
        Assert.assertEquals("30",interest);
        Assert.assertEquals("30",serviceFee);
        Assert.assertEquals("30",tecFee);
        Assert.assertEquals("30",otherFee);
    }

    /**
     * 浮动费用接口参数校验-repaymentPlanNo正确，repayScheduleNo错误
     * @throws Exception
     */
    @Test
    public void checkParams10() throws Exception{
        String repaymentPlanNo = this.repaymentPlanNo;
        String reasonCode = "0";
        String approvedTime = DateUtils.format(new Date());
        Integer firstFeeType = 1;
        Integer secondFeeType = 2;
        Integer thirdFeeType = 3;
        Integer fourthFeeType = 4;
        String firstInterest = "30";
        String secondInterest = "30";
        String thirdInterest = "30";
        String fourthInterest = "30";
        AssetSet assetSet = repaymentPlanService.getRepaymentPlanByRepaymentCode(this.repaymentPlanNo);
        if (null == assetSet){
            throw new Exception("还款计划不存在");
        }
        String repayScheduleNo = assetSet.getOuterRepaymentPlanNo()+1;
        if (null == repayScheduleNo || "".equals(repayScheduleNo)){
            throw new Exception("商户还款计划编号不存在");
        }
        String result = refactorMethod.mutableFee(productCode,uniqueId,uniqueId,repaymentPlanNo,repayScheduleNo,
                reasonCode,approvedTime,firstFeeType,secondFeeType,thirdFeeType,fourthFeeType,
                firstInterest,secondInterest,thirdInterest,fourthInterest);
        assetSet = repaymentPlanService.getRepaymentPlanByRepaymentCode(this.repaymentPlanNo);
        String assetSetUuid = assetSet.getAssetUuid();
        boolean outcome = result.contains("不存在该有效还款计划或者还款计划不在贷款合同内");
        Map<String,BigDecimal> assetSetExtraCharges = repaymentPlanExtraChargeService.getAssetSetExtraChargeModels(assetSetUuid);
        String interest = assetSet.getAssetInterestValue().stripTrailingZeros().toPlainString();
        String serviceFee = assetSetExtraCharges.get(ExtraChargeSpec.LOAN_SERVICE_FEE_KEY).stripTrailingZeros().toPlainString();
        String tecFee = assetSetExtraCharges.get(ExtraChargeSpec.LOAN_TECH_FEE_KEY).stripTrailingZeros().toPlainString();
        String otherFee = assetSetExtraCharges.get(ExtraChargeSpec.LOAN_OTHER_FEE_KEY).stripTrailingZeros().toPlainString();
        Assert.assertEquals(true,outcome);
        Assert.assertEquals("20",interest);
        Assert.assertEquals("20",serviceFee);
        Assert.assertEquals("20",tecFee);
        Assert.assertEquals("20",otherFee);
    }

    /**
     * 浮动费用接口参数校验-只填repayScheduleNo
     * @throws Exception
     */
    @Test
    public void checkParams11() throws Exception{
        String repaymentPlanNo = "";
        String reasonCode = "0";
        String approvedTime = DateUtils.format(new Date());
        Integer firstFeeType = 1;
        Integer secondFeeType = 2;
        Integer thirdFeeType = 3;
        Integer fourthFeeType = 4;
        String firstInterest = "30";
        String secondInterest = "30";
        String thirdInterest = "30";
        String fourthInterest = "30";
        AssetSet assetSet = repaymentPlanService.getRepaymentPlanByRepaymentCode(this.repaymentPlanNo);
        if (null == assetSet){
            throw new Exception("还款计划不存在");
        }
        String repayScheduleNo = assetSet.getOuterRepaymentPlanNo();
        if (null == repayScheduleNo || "".equals(repayScheduleNo)){
            throw new Exception("商户还款计划编号不存在");
        }
        String result = refactorMethod.mutableFee(productCode,uniqueId,uniqueId,repaymentPlanNo,repayScheduleNo,
                reasonCode,approvedTime,firstFeeType,secondFeeType,thirdFeeType,fourthFeeType,
                firstInterest,secondInterest,thirdInterest,fourthInterest);
        assetSet = repaymentPlanService.getRepaymentPlanByRepaymentCode(this.repaymentPlanNo);
        String assetSetUuid = assetSet.getAssetUuid();
        boolean outcome = result.contains("成功");
        Map<String,BigDecimal> assetSetExtraCharges = repaymentPlanExtraChargeService.getAssetSetExtraChargeModels(assetSetUuid);
        String interest = assetSet.getAssetInterestValue().stripTrailingZeros().toPlainString();
        String serviceFee = assetSetExtraCharges.get(ExtraChargeSpec.LOAN_SERVICE_FEE_KEY).stripTrailingZeros().toPlainString();
        String tecFee = assetSetExtraCharges.get(ExtraChargeSpec.LOAN_TECH_FEE_KEY).stripTrailingZeros().toPlainString();
        String otherFee = assetSetExtraCharges.get(ExtraChargeSpec.LOAN_OTHER_FEE_KEY).stripTrailingZeros().toPlainString();
        Assert.assertEquals(true,outcome);
        Assert.assertEquals("30",interest);
        Assert.assertEquals("30",serviceFee);
        Assert.assertEquals("30",tecFee);
        Assert.assertEquals("30",otherFee);
    }

    /**
     * 浮动费用接口参数校验-只填repaymentPlanNo且错误
     * @throws Exception
     */
    @Test
    public void checkParams12() throws Exception{
        String repaymentPlanNo = this.repaymentPlanNo+1;
        String repayScheduleNo = "";
        String reasonCode = "0";
        String approvedTime = DateUtils.format(new Date());
        Integer firstFeeType = 1;
        Integer secondFeeType = 2;
        Integer thirdFeeType = 3;
        Integer fourthFeeType = 4;
        String firstInterest = "30";
        String secondInterest = "30";
        String thirdInterest = "30";
        String fourthInterest = "30";
        String result = refactorMethod.mutableFee(productCode,uniqueId,uniqueId,repaymentPlanNo,repayScheduleNo,
                reasonCode,approvedTime,firstFeeType,secondFeeType,thirdFeeType,fourthFeeType,
                firstInterest,secondInterest,thirdInterest,fourthInterest);
        AssetSet assetSet = repaymentPlanService.getRepaymentPlanByRepaymentCode(this.repaymentPlanNo);
        String assetSetUuid = assetSet.getAssetUuid();
        boolean outcome = result.contains("不存在该有效还款计划或者还款计划不在贷款合同内");
        Map<String,BigDecimal> assetSetExtraCharges = repaymentPlanExtraChargeService.getAssetSetExtraChargeModels(assetSetUuid);
        String interest = assetSet.getAssetInterestValue().stripTrailingZeros().toPlainString();
        String serviceFee = assetSetExtraCharges.get(ExtraChargeSpec.LOAN_SERVICE_FEE_KEY).stripTrailingZeros().toPlainString();
        String tecFee = assetSetExtraCharges.get(ExtraChargeSpec.LOAN_TECH_FEE_KEY).stripTrailingZeros().toPlainString();
        String otherFee = assetSetExtraCharges.get(ExtraChargeSpec.LOAN_OTHER_FEE_KEY).stripTrailingZeros().toPlainString();
        Assert.assertEquals(true,outcome);
        Assert.assertEquals("20",interest);
        Assert.assertEquals("20",serviceFee);
        Assert.assertEquals("20",tecFee);
        Assert.assertEquals("20",otherFee);
    }

    /**
     * 浮动费用接口参数校验-reasonCode为空
     * @throws Exception
     */
    @Test
    public void checkParams13() throws Exception{
        String repaymentPlanNo = this.repaymentPlanNo;
        String repayScheduleNo = "";
        String reasonCode = "";
        String approvedTime = DateUtils.format(new Date());
        Integer firstFeeType = 1;
        Integer secondFeeType = 2;
        Integer thirdFeeType = 3;
        Integer fourthFeeType = 4;
        String firstInterest = "30";
        String secondInterest = "30";
        String thirdInterest = "30";
        String fourthInterest = "30";
        String result = refactorMethod.mutableFee(productCode,uniqueId,uniqueId,repaymentPlanNo,repayScheduleNo,
                reasonCode,approvedTime,firstFeeType,secondFeeType,thirdFeeType,fourthFeeType,
                firstInterest,secondInterest,thirdInterest,fourthInterest);
        AssetSet assetSet = repaymentPlanService.getRepaymentPlanByRepaymentCode(this.repaymentPlanNo);
        String assetSetUuid = assetSet.getAssetUuid();
        boolean outcome = result.contains("更新原因[reasonCode]不能为空");
        Map<String,BigDecimal> assetSetExtraCharges = repaymentPlanExtraChargeService.getAssetSetExtraChargeModels(assetSetUuid);
        String interest = assetSet.getAssetInterestValue().stripTrailingZeros().toPlainString();
        String serviceFee = assetSetExtraCharges.get(ExtraChargeSpec.LOAN_SERVICE_FEE_KEY).stripTrailingZeros().toPlainString();
        String tecFee = assetSetExtraCharges.get(ExtraChargeSpec.LOAN_TECH_FEE_KEY).stripTrailingZeros().toPlainString();
        String otherFee = assetSetExtraCharges.get(ExtraChargeSpec.LOAN_OTHER_FEE_KEY).stripTrailingZeros().toPlainString();
        Assert.assertEquals(true,outcome);
        Assert.assertEquals("20",interest);
        Assert.assertEquals("20",serviceFee);
        Assert.assertEquals("20",tecFee);
        Assert.assertEquals("20",otherFee);
    }

    /**
     * 浮动费用接口参数校验-reasonCode未定义
     * @throws Exception
     */
    @Test
    public void checkParams14() throws Exception{
        String repaymentPlanNo = this.repaymentPlanNo;
        String repayScheduleNo = "";
        String reasonCode = "4";
        String approvedTime = DateUtils.format(new Date());
        Integer firstFeeType = 1;
        Integer secondFeeType = 2;
        Integer thirdFeeType = 3;
        Integer fourthFeeType = 4;
        String firstInterest = "30";
        String secondInterest = "30";
        String thirdInterest = "30";
        String fourthInterest = "30";
        String result = refactorMethod.mutableFee(productCode,uniqueId,uniqueId,repaymentPlanNo,repayScheduleNo,
                reasonCode,approvedTime,firstFeeType,secondFeeType,thirdFeeType,fourthFeeType,
                firstInterest,secondInterest,thirdInterest,fourthInterest);
        AssetSet assetSet = repaymentPlanService.getRepaymentPlanByRepaymentCode(this.repaymentPlanNo);
        String assetSetUuid = assetSet.getAssetUuid();
        boolean outcome = result.contains("更新原因[reasonCode]内容不合法");
        Map<String,BigDecimal> assetSetExtraCharges = repaymentPlanExtraChargeService.getAssetSetExtraChargeModels(assetSetUuid);
        String interest = assetSet.getAssetInterestValue().stripTrailingZeros().toPlainString();
        String serviceFee = assetSetExtraCharges.get(ExtraChargeSpec.LOAN_SERVICE_FEE_KEY).stripTrailingZeros().toPlainString();
        String tecFee = assetSetExtraCharges.get(ExtraChargeSpec.LOAN_TECH_FEE_KEY).stripTrailingZeros().toPlainString();
        String otherFee = assetSetExtraCharges.get(ExtraChargeSpec.LOAN_OTHER_FEE_KEY).stripTrailingZeros().toPlainString();
        Assert.assertEquals(true,outcome);
        Assert.assertEquals("20",interest);
        Assert.assertEquals("20",serviceFee);
        Assert.assertEquals("20",tecFee);
        Assert.assertEquals("20",otherFee);
    }

    /**
     * 浮动费用接口参数校验-信托合同不支持随借随还
     * @throws Exception
     */
    @Test
    public void checkParams15() throws Exception{
        String productCode = "G32000";
        String uniqueId = UUID.randomUUID().toString();
        String repaymentAccountNo = "6217857600016839330";
        String loanCustomerName = "秦操";
        String loanCustomerNo = "11111117";
        String firstPlanDate = DateUtils.format(DateUtils.addMonths(new Date(), -1));
        String secondPlanDate = DateUtils.format(new Date());
        String thirdPlanDate = DateUtils.format(DateUtils.addMonths(new Date(), 1));
        refactorMethod.importAssetPackage(totalAmount, productCode, uniqueId, repaymentAccountNo, interest, loanCustomerNo,
                firstPlanDate, secondPlanDate, thirdPlanDate, loanCustomerName);
        String repaymentPlanNo = refactorMethod.queryRepaymentPlan(productCode,uniqueId,0);
        String repayScheduleNo = "";
        String reasonCode = "0";
        String approvedTime = DateUtils.format(new Date());
        Integer firstFeeType = 1;
        Integer secondFeeType = 2;
        Integer thirdFeeType = 3;
        Integer fourthFeeType = 4;
        String firstInterest = "30";
        String secondInterest = "30";
        String thirdInterest = "30";
        String fourthInterest = "30";
        String result = refactorMethod.mutableFee(productCode,uniqueId,uniqueId,repaymentPlanNo,repayScheduleNo,
                reasonCode,approvedTime,firstFeeType,secondFeeType,thirdFeeType,fourthFeeType,
                firstInterest,secondInterest,thirdInterest,fourthInterest);
        AssetSet assetSet = repaymentPlanService.getRepaymentPlanByRepaymentCode(repaymentPlanNo);
        String assetSetUuid = assetSet.getAssetUuid();
        boolean outcome = result.contains("信托合同不支持随借随还");
        Map<String,BigDecimal> assetSetExtraCharges = repaymentPlanExtraChargeService.getAssetSetExtraChargeModels(assetSetUuid);
        String interest = assetSet.getAssetInterestValue().stripTrailingZeros().toPlainString();
        String serviceFee = assetSetExtraCharges.get(ExtraChargeSpec.LOAN_SERVICE_FEE_KEY).stripTrailingZeros().toPlainString();
        String tecFee = assetSetExtraCharges.get(ExtraChargeSpec.LOAN_TECH_FEE_KEY).stripTrailingZeros().toPlainString();
        String otherFee = assetSetExtraCharges.get(ExtraChargeSpec.LOAN_OTHER_FEE_KEY).stripTrailingZeros().toPlainString();
        Assert.assertEquals(true,outcome);
        Assert.assertEquals("20",interest);
        Assert.assertEquals("20",serviceFee);
        Assert.assertEquals("20",tecFee);
        Assert.assertEquals("20",otherFee);
    }

    /**
     * 浮动费用接口参数校验-费用类型错误
     * @throws Exception
     */
    @Test
    public void checkParams16() throws Exception{
        String repaymentPlanNo = this.repaymentPlanNo;
        String repayScheduleNo = "";
        String reasonCode = "0";
        String approvedTime = DateUtils.format(new Date());
        Integer firstFeeType = 5;
        Integer secondFeeType = 2;
        Integer thirdFeeType = 3;
        Integer fourthFeeType = 4;
        String firstInterest = "30";
        String secondInterest = "30";
        String thirdInterest = "30";
        String fourthInterest = "30";
        String result = refactorMethod.mutableFee(productCode,uniqueId,uniqueId,repaymentPlanNo,repayScheduleNo,
                reasonCode,approvedTime,firstFeeType,secondFeeType,thirdFeeType,fourthFeeType,
                firstInterest,secondInterest,thirdInterest,fourthInterest);
        AssetSet assetSet = repaymentPlanService.getRepaymentPlanByRepaymentCode(this.repaymentPlanNo);
        String assetSetUuid = assetSet.getAssetUuid();
        boolean outcome = result.contains("无效费用类型");
        Map<String,BigDecimal> assetSetExtraCharges = repaymentPlanExtraChargeService.getAssetSetExtraChargeModels(assetSetUuid);
        String interest = assetSet.getAssetInterestValue().stripTrailingZeros().toPlainString();
        String serviceFee = assetSetExtraCharges.get(ExtraChargeSpec.LOAN_SERVICE_FEE_KEY).stripTrailingZeros().toPlainString();
        String tecFee = assetSetExtraCharges.get(ExtraChargeSpec.LOAN_TECH_FEE_KEY).stripTrailingZeros().toPlainString();
        String otherFee = assetSetExtraCharges.get(ExtraChargeSpec.LOAN_OTHER_FEE_KEY).stripTrailingZeros().toPlainString();
        Assert.assertEquals(true,outcome);
        Assert.assertEquals("20",interest);
        Assert.assertEquals("20",serviceFee);
        Assert.assertEquals("20",tecFee);
        Assert.assertEquals("20",otherFee);
    }

    /**
     * 浮动费用接口参数校验-费用类型重复
     * @throws Exception
     */
    @Test
    public void checkParams17() throws Exception{
        String repaymentPlanNo = this.repaymentPlanNo;
        String repayScheduleNo = "";
        String reasonCode = "0";
        String approvedTime = DateUtils.format(new Date());
        Integer firstFeeType = 1;
        Integer secondFeeType = 1;
        Integer thirdFeeType = 3;
        Integer fourthFeeType = 4;
        String firstInterest = "30";
        String secondInterest = "40";
        String thirdInterest = "30";
        String fourthInterest = "30";
        String result = refactorMethod.mutableFee(productCode,uniqueId,uniqueId,repaymentPlanNo,repayScheduleNo,
                reasonCode,approvedTime,firstFeeType,secondFeeType,thirdFeeType,fourthFeeType,
                firstInterest,secondInterest,thirdInterest,fourthInterest);
        AssetSet assetSet = repaymentPlanService.getRepaymentPlanByRepaymentCode(this.repaymentPlanNo);
        String assetSetUuid = assetSet.getAssetUuid();
        boolean outcome = result.contains("费用重复");
        Map<String,BigDecimal> assetSetExtraCharges = repaymentPlanExtraChargeService.getAssetSetExtraChargeModels(assetSetUuid);
        String interest = assetSet.getAssetInterestValue().stripTrailingZeros().toPlainString();
        String serviceFee = assetSetExtraCharges.get(ExtraChargeSpec.LOAN_SERVICE_FEE_KEY).stripTrailingZeros().toPlainString();
        String tecFee = assetSetExtraCharges.get(ExtraChargeSpec.LOAN_TECH_FEE_KEY).stripTrailingZeros().toPlainString();
        String otherFee = assetSetExtraCharges.get(ExtraChargeSpec.LOAN_OTHER_FEE_KEY).stripTrailingZeros().toPlainString();
        Assert.assertEquals(true,outcome);
        Assert.assertEquals("20",interest);
        Assert.assertEquals("20",serviceFee);
        Assert.assertEquals("20",tecFee);
        Assert.assertEquals("20",otherFee);
    }

    /**
     * 浮动费用接口参数校验-审核日期格式错误
     * @throws Exception
     */
    @Test
    public void checkParams18() throws Exception{
        String repaymentPlanNo = this.repaymentPlanNo;
        String repayScheduleNo = "";
        String reasonCode = "0";
        String approvedTime = "123321";
        Integer firstFeeType = 1;
        Integer secondFeeType = 2;
        Integer thirdFeeType = 3;
        Integer fourthFeeType = 4;
        String firstInterest = "30";
        String secondInterest = "30";
        String thirdInterest = "30";
        String fourthInterest = "30";
        String result = refactorMethod.mutableFee(productCode,uniqueId,uniqueId,repaymentPlanNo,repayScheduleNo,
                reasonCode,approvedTime,firstFeeType,secondFeeType,thirdFeeType,fourthFeeType,
                firstInterest,secondInterest,thirdInterest,fourthInterest);
        AssetSet assetSet = repaymentPlanService.getRepaymentPlanByRepaymentCode(this.repaymentPlanNo);
        String assetSetUuid = assetSet.getAssetUuid();
        boolean outcome = result.contains("审核日期格式错误");
        Map<String,BigDecimal> assetSetExtraCharges = repaymentPlanExtraChargeService.getAssetSetExtraChargeModels(assetSetUuid);
        String interest = assetSet.getAssetInterestValue().stripTrailingZeros().toPlainString();
        String serviceFee = assetSetExtraCharges.get(ExtraChargeSpec.LOAN_SERVICE_FEE_KEY).stripTrailingZeros().toPlainString();
        String tecFee = assetSetExtraCharges.get(ExtraChargeSpec.LOAN_TECH_FEE_KEY).stripTrailingZeros().toPlainString();
        String otherFee = assetSetExtraCharges.get(ExtraChargeSpec.LOAN_OTHER_FEE_KEY).stripTrailingZeros().toPlainString();
        Assert.assertEquals(true,outcome);
        Assert.assertEquals("20",interest);
        Assert.assertEquals("20",serviceFee);
        Assert.assertEquals("20",tecFee);
        Assert.assertEquals("20",otherFee);
    }

    /**
     * 浮动费用接口参数校验-利息为负数
     * @throws Exception
     */
    @Test
    public void checkParams19() throws Exception{
        String repaymentPlanNo = this.repaymentPlanNo;
        String repayScheduleNo = "";
        String reasonCode = "0";
        String approvedTime = DateUtils.format(new Date());
        Integer firstFeeType = 1;
        Integer secondFeeType = 2;
        Integer thirdFeeType = 3;
        Integer fourthFeeType = 4;
        String firstInterest = "-30";
        String secondInterest = "30";
        String thirdInterest = "30";
        String fourthInterest = "30";
        String result = refactorMethod.mutableFee(productCode,uniqueId,uniqueId,repaymentPlanNo,repayScheduleNo,
                reasonCode,approvedTime,firstFeeType,secondFeeType,thirdFeeType,fourthFeeType,
                firstInterest,secondInterest,thirdInterest,fourthInterest);
        AssetSet assetSet = repaymentPlanService.getRepaymentPlanByRepaymentCode(this.repaymentPlanNo);
        String assetSetUuid = assetSet.getAssetUuid();
        boolean outcome = result.contains("非法还息金额");
        Map<String,BigDecimal> assetSetExtraCharges = repaymentPlanExtraChargeService.getAssetSetExtraChargeModels(assetSetUuid);
        String interest = assetSet.getAssetInterestValue().stripTrailingZeros().toPlainString();
        String serviceFee = assetSetExtraCharges.get(ExtraChargeSpec.LOAN_SERVICE_FEE_KEY).stripTrailingZeros().toPlainString();
        String tecFee = assetSetExtraCharges.get(ExtraChargeSpec.LOAN_TECH_FEE_KEY).stripTrailingZeros().toPlainString();
        String otherFee = assetSetExtraCharges.get(ExtraChargeSpec.LOAN_OTHER_FEE_KEY).stripTrailingZeros().toPlainString();
        Assert.assertEquals(true,outcome);
        Assert.assertEquals("20",interest);
        Assert.assertEquals("20",serviceFee);
        Assert.assertEquals("20",tecFee);
        Assert.assertEquals("20",otherFee);
    }

    /**
     * 浮动费用接口参数校验-贷款服务费为负数
     * @throws Exception
     */
    @Test
    public void checkParams20() throws Exception{
        String repaymentPlanNo = this.repaymentPlanNo;
        String repayScheduleNo = "";
        String reasonCode = "0";
        String approvedTime = DateUtils.format(new Date());
        Integer firstFeeType = 1;
        Integer secondFeeType = 2;
        Integer thirdFeeType = 3;
        Integer fourthFeeType = 4;
        String firstInterest = "30";
        String secondInterest = "-30";
        String thirdInterest = "30";
        String fourthInterest = "30";
        String result = refactorMethod.mutableFee(productCode,uniqueId,uniqueId,repaymentPlanNo,repayScheduleNo,
                reasonCode,approvedTime,firstFeeType,secondFeeType,thirdFeeType,fourthFeeType,
                firstInterest,secondInterest,thirdInterest,fourthInterest);
        AssetSet assetSet = repaymentPlanService.getRepaymentPlanByRepaymentCode(this.repaymentPlanNo);
        String assetSetUuid = assetSet.getAssetUuid();
        boolean outcome = result.contains("非法还息金额");
        Map<String,BigDecimal> assetSetExtraCharges = repaymentPlanExtraChargeService.getAssetSetExtraChargeModels(assetSetUuid);
        String interest = assetSet.getAssetInterestValue().stripTrailingZeros().toPlainString();
        String serviceFee = assetSetExtraCharges.get(ExtraChargeSpec.LOAN_SERVICE_FEE_KEY).stripTrailingZeros().toPlainString();
        String tecFee = assetSetExtraCharges.get(ExtraChargeSpec.LOAN_TECH_FEE_KEY).stripTrailingZeros().toPlainString();
        String otherFee = assetSetExtraCharges.get(ExtraChargeSpec.LOAN_OTHER_FEE_KEY).stripTrailingZeros().toPlainString();
        Assert.assertEquals(true,outcome);
        Assert.assertEquals("20",interest);
        Assert.assertEquals("20",serviceFee);
        Assert.assertEquals("20",tecFee);
        Assert.assertEquals("20",otherFee);
    }

    /**
     * 浮动费用接口参数校验-技术服务费为负数
     * @throws Exception
     */
    @Test
    public void checkParams21() throws Exception{
        String repaymentPlanNo = this.repaymentPlanNo;
        String repayScheduleNo = "";
        String reasonCode = "0";
        String approvedTime = DateUtils.format(new Date());
        Integer firstFeeType = 1;
        Integer secondFeeType = 2;
        Integer thirdFeeType = 3;
        Integer fourthFeeType = 4;
        String firstInterest = "30";
        String secondInterest = "30";
        String thirdInterest = "-30";
        String fourthInterest = "30";
        String result = refactorMethod.mutableFee(productCode,uniqueId,uniqueId,repaymentPlanNo,repayScheduleNo,
                reasonCode,approvedTime,firstFeeType,secondFeeType,thirdFeeType,fourthFeeType,
                firstInterest,secondInterest,thirdInterest,fourthInterest);
        AssetSet assetSet = repaymentPlanService.getRepaymentPlanByRepaymentCode(this.repaymentPlanNo);
        String assetSetUuid = assetSet.getAssetUuid();
        boolean outcome = result.contains("非法还息金额");
        Map<String,BigDecimal> assetSetExtraCharges = repaymentPlanExtraChargeService.getAssetSetExtraChargeModels(assetSetUuid);
        String interest = assetSet.getAssetInterestValue().stripTrailingZeros().toPlainString();
        String serviceFee = assetSetExtraCharges.get(ExtraChargeSpec.LOAN_SERVICE_FEE_KEY).stripTrailingZeros().toPlainString();
        String tecFee = assetSetExtraCharges.get(ExtraChargeSpec.LOAN_TECH_FEE_KEY).stripTrailingZeros().toPlainString();
        String otherFee = assetSetExtraCharges.get(ExtraChargeSpec.LOAN_OTHER_FEE_KEY).stripTrailingZeros().toPlainString();
        Assert.assertEquals(true,outcome);
        Assert.assertEquals("20",interest);
        Assert.assertEquals("20",serviceFee);
        Assert.assertEquals("20",tecFee);
        Assert.assertEquals("20",otherFee);
    }

    /**
     * 浮动费用接口参数校验-其他费用为负数
     * @throws Exception
     */
    @Test
    public void checkParams22() throws Exception{
        String repaymentPlanNo = this.repaymentPlanNo;
        String repayScheduleNo = "";
        String reasonCode = "0";
        String approvedTime = DateUtils.format(new Date());
        Integer firstFeeType = 1;
        Integer secondFeeType = 2;
        Integer thirdFeeType = 3;
        Integer fourthFeeType = 4;
        String firstInterest = "30";
        String secondInterest = "30";
        String thirdInterest = "30";
        String fourthInterest = "-30";
        String result = refactorMethod.mutableFee(productCode,uniqueId,uniqueId,repaymentPlanNo,repayScheduleNo,
                reasonCode,approvedTime,firstFeeType,secondFeeType,thirdFeeType,fourthFeeType,
                firstInterest,secondInterest,thirdInterest,fourthInterest);
        AssetSet assetSet = repaymentPlanService.getRepaymentPlanByRepaymentCode(this.repaymentPlanNo);
        String assetSetUuid = assetSet.getAssetUuid();
        boolean outcome = result.contains("非法还息金额");
        Map<String,BigDecimal> assetSetExtraCharges = repaymentPlanExtraChargeService.getAssetSetExtraChargeModels(assetSetUuid);
        String interest = assetSet.getAssetInterestValue().stripTrailingZeros().toPlainString();
        String serviceFee = assetSetExtraCharges.get(ExtraChargeSpec.LOAN_SERVICE_FEE_KEY).stripTrailingZeros().toPlainString();
        String tecFee = assetSetExtraCharges.get(ExtraChargeSpec.LOAN_TECH_FEE_KEY).stripTrailingZeros().toPlainString();
        String otherFee = assetSetExtraCharges.get(ExtraChargeSpec.LOAN_OTHER_FEE_KEY).stripTrailingZeros().toPlainString();
        Assert.assertEquals(true,outcome);
        Assert.assertEquals("20",interest);
        Assert.assertEquals("20",serviceFee);
        Assert.assertEquals("20",tecFee);
        Assert.assertEquals("20",otherFee);
    }

    /**
     * 浮动费用接口参数校验-费用变更后与原来一样
     * @throws Exception
     */
    @Test
    public void checkParams23() throws Exception{
        String repaymentPlanNo = this.repaymentPlanNo;
        String repayScheduleNo = "";
        String reasonCode = "0";
        String approvedTime = DateUtils.format(new Date());
        Integer firstFeeType = 1;
        Integer secondFeeType = 2;
        Integer thirdFeeType = 3;
        Integer fourthFeeType = 4;
        String firstInterest = "20";
        String secondInterest = "20";
        String thirdInterest = "20";
        String fourthInterest = "20";
        String result = refactorMethod.mutableFee(productCode,uniqueId,uniqueId,repaymentPlanNo,repayScheduleNo,
                reasonCode,approvedTime,firstFeeType,secondFeeType,thirdFeeType,fourthFeeType,
                firstInterest,secondInterest,thirdInterest,fourthInterest);
        AssetSet assetSet = repaymentPlanService.getRepaymentPlanByRepaymentCode(this.repaymentPlanNo);
        String assetSetUuid = assetSet.getAssetUuid();
        boolean outcome = result.contains("成功");
        Map<String,BigDecimal> assetSetExtraCharges = repaymentPlanExtraChargeService.getAssetSetExtraChargeModels(assetSetUuid);
        String interest = assetSet.getAssetInterestValue().stripTrailingZeros().toPlainString();
        String serviceFee = assetSetExtraCharges.get(ExtraChargeSpec.LOAN_SERVICE_FEE_KEY).stripTrailingZeros().toPlainString();
        String tecFee = assetSetExtraCharges.get(ExtraChargeSpec.LOAN_TECH_FEE_KEY).stripTrailingZeros().toPlainString();
        String otherFee = assetSetExtraCharges.get(ExtraChargeSpec.LOAN_OTHER_FEE_KEY).stripTrailingZeros().toPlainString();
        Assert.assertEquals(true,outcome);
        Assert.assertEquals("20",interest);
        Assert.assertEquals("20",serviceFee);
        Assert.assertEquals("20",tecFee);
        Assert.assertEquals("20",otherFee);
    }

    /**
     * 浮动费用接口业务校验-对扣款中的还款计划浮动费用
     * @throws Exception
     */
    @Test
    public void checkParams24() throws Exception{
        String repaymentPlanNo = this.repaymentPlanNo;
        String repayScheduleNo = "";
        String reasonCode = "0";
        String approvedTime = DateUtils.format(new Date());
        Integer firstFeeType = 1;
        Integer secondFeeType = 2;
        Integer thirdFeeType = 3;
        Integer fourthFeeType = 4;
        String firstInterest = "30";
        String secondInterest = "30";
        String thirdInterest = "30";
        String fourthInterest = "30";

        String payAcNo = "6217857600016839330";
        String repaymentType = "1";
        String principal = String.valueOf(Double.valueOf(totalAmount)/3);
        String deductInterest = "20";
        String deductAmount = String.valueOf(Double.valueOf(principal)+Double.valueOf(deductInterest)*4);
        String overDueFee = "0";
        String totalOverDueFee = String.valueOf(Double.valueOf(overDueFee)*4);
        String deductResult = refactorMethod.dedcutRepaymentPlan(productCode,uniqueId,deductAmount,payAcNo,repaymentType,repaymentPlanNo,principal,
                deductInterest,overDueFee,totalOverDueFee);
        if(!deductResult.contains("成功")){
            throw new Exception("扣款参数校验失败");
        }
        Thread.sleep(2000l);
        AssetSet assetSet = repaymentPlanService.getRepaymentPlanByRepaymentCode(repaymentPlanNo);
        if(!assetSet.getDeductionStatus().equals(DeductionStatus.LOCAL_PROCESSING) && !assetSet.getDeductionStatus().equals(DeductionStatus.OPPOSITE_PROCESSING)){
            throw new Exception("扣款失败");
        }
        String result = refactorMethod.mutableFee(productCode,uniqueId,uniqueId,repaymentPlanNo,repayScheduleNo,
                reasonCode,approvedTime,firstFeeType,secondFeeType,thirdFeeType,fourthFeeType,
                firstInterest,secondInterest,thirdInterest,fourthInterest);
        assetSet = repaymentPlanService.getRepaymentPlanByRepaymentCode(this.repaymentPlanNo);
        String assetSetUuid = assetSet.getAssetUuid();
        boolean outcome = result.contains("还款计划被锁定");
        Map<String,BigDecimal> assetSetExtraCharges = repaymentPlanExtraChargeService.getAssetSetExtraChargeModels(assetSetUuid);
        String interest = assetSet.getAssetInterestValue().stripTrailingZeros().toPlainString();
        String serviceFee = assetSetExtraCharges.get(ExtraChargeSpec.LOAN_SERVICE_FEE_KEY).stripTrailingZeros().toPlainString();
        String tecFee = assetSetExtraCharges.get(ExtraChargeSpec.LOAN_TECH_FEE_KEY).stripTrailingZeros().toPlainString();
        String otherFee = assetSetExtraCharges.get(ExtraChargeSpec.LOAN_OTHER_FEE_KEY).stripTrailingZeros().toPlainString();
        Assert.assertEquals(true,outcome);
        Assert.assertEquals("20",interest);
        Assert.assertEquals("20",serviceFee);
        Assert.assertEquals("20",tecFee);
        Assert.assertEquals("20",otherFee);
    }

    /**
     * 浮动费用接口业务校验-对作废的还款计划浮动费用
     * @throws Exception
     */
    @Test
    public void checkParams25() throws Exception{
        String repaymentPlanNo = this.repaymentPlanNo;
        String repayScheduleNo = "";
        String reasonCode = "0";
        String approvedTime = DateUtils.format(new Date());
        Integer firstFeeType = 1;
        Integer secondFeeType = 2;
        Integer thirdFeeType = 3;
        Integer fourthFeeType = 4;
        String firstInterest = "30";
        String secondInterest = "30";
        String thirdInterest = "30";
        String fourthInterest = "30";
        String modifyResult = refactorMethod.modifyRepaymentPlanForOne(uniqueId, totalAmount, interest, approvedTime, 1, "3", repayScheduleNo, uniqueId);
        if(!modifyResult.contains("成功")){
            throw new Exception("还款计划变更失败");
        }
        String result = refactorMethod.mutableFee(productCode,uniqueId,uniqueId,repaymentPlanNo,repayScheduleNo,
                reasonCode,approvedTime,firstFeeType,secondFeeType,thirdFeeType,fourthFeeType,
                firstInterest,secondInterest,thirdInterest,fourthInterest);
        AssetSet assetSet = repaymentPlanService.getRepaymentPlanByRepaymentCode(this.repaymentPlanNo);
        String assetSetUuid = assetSet.getAssetUuid();
        boolean outcome = result.contains("不存在该有效还款计划或者还款计划不在贷款合同内");
        Map<String,BigDecimal> assetSetExtraCharges = repaymentPlanExtraChargeService.getAssetSetExtraChargeModels(assetSetUuid);
        String interest = assetSet.getAssetInterestValue().stripTrailingZeros().toPlainString();
        String serviceFee = assetSetExtraCharges.get(ExtraChargeSpec.LOAN_SERVICE_FEE_KEY).stripTrailingZeros().toPlainString();
        String tecFee = assetSetExtraCharges.get(ExtraChargeSpec.LOAN_TECH_FEE_KEY).stripTrailingZeros().toPlainString();
        String otherFee = assetSetExtraCharges.get(ExtraChargeSpec.LOAN_OTHER_FEE_KEY).stripTrailingZeros().toPlainString();
        Assert.assertEquals(true,outcome);
        Assert.assertEquals("20",interest);
        Assert.assertEquals("20",serviceFee);
        Assert.assertEquals("20",tecFee);
        Assert.assertEquals("20",otherFee);
    }

    /**
     * 浮动费用接口业务校验-对还款成功的还款计划浮动费用
     * @throws Exception
     */
    @Test
    public void checkParams26() throws Exception{
        String repaymentPlanNo = this.repaymentPlanNo;
        String repayScheduleNo = "";
        String reasonCode = "0";
        String approvedTime = DateUtils.format(new Date());
        Integer firstFeeType = 1;
        Integer secondFeeType = 2;
        Integer thirdFeeType = 3;
        Integer fourthFeeType = 4;
        String firstInterest = "30";
        String secondInterest = "30";
        String thirdInterest = "30";
        String fourthInterest = "30";

        String payAcNo = "6217857600016839330";
        String repaymentType = "1";
        String principal = String.valueOf(Double.valueOf(totalAmount)/3);
        String deductInterest = "20";
        String deductAmount = String.valueOf(Double.valueOf(principal)+Double.valueOf(deductInterest)*4);
        String overDueFee = "0";
        String totalOverDueFee = String.valueOf(Double.valueOf(overDueFee)*4);
        String deductResult = refactorMethod.dedcutRepaymentPlan(productCode,uniqueId,deductAmount,payAcNo,repaymentType,repaymentPlanNo,principal,
                deductInterest,overDueFee,totalOverDueFee);
        if(!deductResult.contains("成功")){
            throw new Exception("扣款参数校验失败");
        }
        Thread.sleep(2000l);
        AssetSet assetSet = repaymentPlanService.getRepaymentPlanByRepaymentCode(repaymentPlanNo);
        if(!assetSet.getDeductionStatus().equals(DeductionStatus.LOCAL_PROCESSING) && !assetSet.getDeductionStatus().equals(DeductionStatus.OPPOSITE_PROCESSING)){
            throw new Exception("扣款失败");
        }
        while (true){
            assetSet = repaymentPlanService.getRepaymentPlanByRepaymentCode(repaymentPlanNo);
            if(PaymentStatus.SUCCESS.equals(assetSet.getPaymentStatus())){
                break;
            }
            Thread.sleep(10000l);
        }
        Thread.sleep(5000l);
        String result = refactorMethod.mutableFee(productCode,uniqueId,uniqueId,repaymentPlanNo,repayScheduleNo,
                reasonCode,approvedTime,firstFeeType,secondFeeType,thirdFeeType,fourthFeeType,
                firstInterest,secondInterest,thirdInterest,fourthInterest);
        assetSet = repaymentPlanService.getRepaymentPlanByRepaymentCode(this.repaymentPlanNo);
        String assetSetUuid = assetSet.getAssetUuid();
        boolean outcome = result.contains("还款计划已还款成功");
        Map<String,BigDecimal> assetSetExtraCharges = repaymentPlanExtraChargeService.getAssetSetExtraChargeModels(assetSetUuid);
        String interest = assetSet.getAssetInterestValue().stripTrailingZeros().toPlainString();
        String serviceFee = assetSetExtraCharges.get(ExtraChargeSpec.LOAN_SERVICE_FEE_KEY).stripTrailingZeros().toPlainString();
        String tecFee = assetSetExtraCharges.get(ExtraChargeSpec.LOAN_TECH_FEE_KEY).stripTrailingZeros().toPlainString();
        String otherFee = assetSetExtraCharges.get(ExtraChargeSpec.LOAN_OTHER_FEE_KEY).stripTrailingZeros().toPlainString();
        Assert.assertEquals(true,outcome);
        Assert.assertEquals("20",interest);
        Assert.assertEquals("20",serviceFee);
        Assert.assertEquals("20",tecFee);
        Assert.assertEquals("20",otherFee);
    }

    /**
     * 浮动费用接口业务校验-对支付中的还款计划浮动费用且费用大于支付中的金额
     * @throws Exception
     */
    @Test
    public void checkParams27() throws Exception{
        String repaymentPlanNo = this.repaymentPlanNo;
        String repayScheduleNo = "";
        String reasonCode = "0";
        String approvedTime = DateUtils.format(new Date());
        Integer firstFeeType = 1;
        Integer secondFeeType = 2;
        Integer thirdFeeType = 3;
        Integer fourthFeeType = 4;
        String firstInterest = "30";
        String secondInterest = "30";
        String thirdInterest = "30";
        String fourthInterest = "30";
        String orderRequestNo = uniqueId;
        String transType = "0";
        String orderAmount = "10080";
        Long firstRepaymentWay = 1000l;
        String firstDetailTotalAmount = "10080";
        String firstDetailPrincipal = "10000";
        String firstDetailInterest = "20";
        Long orderFirstFeeType = 1l;
        String OrderResult = refactorMethod.applyRepaymentOderForOneDetail(orderRequestNo,orderRequestNo,transType,productCode,orderAmount,uniqueId,
                repaymentPlanNo,firstRepaymentWay,
                firstDetailTotalAmount,firstDetailPrincipal,
                firstDetailInterest,orderFirstFeeType,repayScheduleNo);
        if(!OrderResult.contains("订单已受理")){
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
        String result = refactorMethod.mutableFee(productCode,uniqueId,uniqueId,repaymentPlanNo,repayScheduleNo,
                reasonCode,approvedTime,firstFeeType,secondFeeType,thirdFeeType,fourthFeeType,
                firstInterest,secondInterest,thirdInterest,fourthInterest);
        AssetSet assetSet = repaymentPlanService.getRepaymentPlanByRepaymentCode(this.repaymentPlanNo);
        String assetSetUuid = assetSet.getAssetUuid();
        boolean outcome = result.contains("成功");
        Map<String,BigDecimal> assetSetExtraCharges = repaymentPlanExtraChargeService.getAssetSetExtraChargeModels(assetSetUuid);
        String interest = assetSet.getAssetInterestValue().stripTrailingZeros().toPlainString();
        String serviceFee = assetSetExtraCharges.get(ExtraChargeSpec.LOAN_SERVICE_FEE_KEY).stripTrailingZeros().toPlainString();
        String tecFee = assetSetExtraCharges.get(ExtraChargeSpec.LOAN_TECH_FEE_KEY).stripTrailingZeros().toPlainString();
        String otherFee = assetSetExtraCharges.get(ExtraChargeSpec.LOAN_OTHER_FEE_KEY).stripTrailingZeros().toPlainString();
        Assert.assertEquals(true,outcome);
        Assert.assertEquals("30",interest);
        Assert.assertEquals("30",serviceFee);
        Assert.assertEquals("30",tecFee);
        Assert.assertEquals("30",otherFee);
    }

    /**
     * 浮动费用接口业务校验-对支付中的还款计划浮动费用且费用小于支付中的金额
     * @throws Exception
     */
    @Test
    public void checkParams28() throws Exception{
        String repaymentPlanNo = this.repaymentPlanNo;
        String repayScheduleNo = "";
        String reasonCode = "0";
        String approvedTime = DateUtils.format(new Date());
        Integer firstFeeType = 1;
        Integer secondFeeType = 2;
        Integer thirdFeeType = 3;
        Integer fourthFeeType = 4;
        String firstInterest = "19";
        String secondInterest = "30";
        String thirdInterest = "30";
        String fourthInterest = "30";
        String orderRequestNo = uniqueId;
        String transType = "0";
        String orderAmount = "10080";
        Long firstRepaymentWay = 1000l;
        String firstDetailTotalAmount = "10080";
        String firstDetailPrincipal = "10000";
        String firstDetailInterest = "20";
        Long orderFirstFeeType = 1l;
        String OrderResult = refactorMethod.applyRepaymentOderForOneDetail(orderRequestNo,orderRequestNo,transType,productCode,orderAmount,uniqueId,
                repaymentPlanNo,firstRepaymentWay,
                firstDetailTotalAmount,firstDetailPrincipal,
                firstDetailInterest,orderFirstFeeType,repayScheduleNo);
        if(!OrderResult.contains("订单已受理")){
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
        String result = refactorMethod.mutableFee(productCode,uniqueId,uniqueId,repaymentPlanNo,repayScheduleNo,
                reasonCode,approvedTime,firstFeeType,secondFeeType,thirdFeeType,fourthFeeType,
                firstInterest,secondInterest,thirdInterest,fourthInterest);
        AssetSet assetSet = repaymentPlanService.getRepaymentPlanByRepaymentCode(this.repaymentPlanNo);
        String assetSetUuid = assetSet.getAssetUuid();
        boolean outcome = result.contains("非法还息金额");
        Map<String,BigDecimal> assetSetExtraCharges = repaymentPlanExtraChargeService.getAssetSetExtraChargeModels(assetSetUuid);
        String interest = assetSet.getAssetInterestValue().stripTrailingZeros().toPlainString();
        String serviceFee = assetSetExtraCharges.get(ExtraChargeSpec.LOAN_SERVICE_FEE_KEY).stripTrailingZeros().toPlainString();
        String tecFee = assetSetExtraCharges.get(ExtraChargeSpec.LOAN_TECH_FEE_KEY).stripTrailingZeros().toPlainString();
        String otherFee = assetSetExtraCharges.get(ExtraChargeSpec.LOAN_OTHER_FEE_KEY).stripTrailingZeros().toPlainString();
        Assert.assertEquals(true,outcome);
        Assert.assertEquals("20",interest);
        Assert.assertEquals("20",serviceFee);
        Assert.assertEquals("20",tecFee);
        Assert.assertEquals("20",otherFee);
    }


    /**
     * 浮动费用接口业务校验-对部分扣款成功的还款计划浮动费用且费用大于扣款成功的金额
     * @throws Exception
     */
    @Test
    public void checkParams29() throws Exception{
        String repaymentPlanNo = this.repaymentPlanNo;
        String repayScheduleNo = "";
        String reasonCode = "0";
        String approvedTime = DateUtils.format(new Date());
        Integer firstFeeType = 1;
        Integer secondFeeType = 2;
        Integer thirdFeeType = 3;
        Integer fourthFeeType = 4;
        String firstInterest = "15";
        String secondInterest = "30";
        String thirdInterest = "30";
        String fourthInterest = "30";

        String payAcNo = "6217857600016839330";
        String repaymentType = "1";
        String principal = String.valueOf(Double.valueOf(totalAmount)/3);
        String deductInterest = "10";
        String deductAmount = String.valueOf(Double.valueOf(principal)+Double.valueOf(deductInterest)*4);
        String overDueFee = "0";
        String totalOverDueFee = String.valueOf(Double.valueOf(overDueFee)*4);
        String deductResult = refactorMethod.dedcutRepaymentPlan(productCode,uniqueId,deductAmount,payAcNo,repaymentType,repaymentPlanNo,principal,
                deductInterest,overDueFee,totalOverDueFee);
        if(!deductResult.contains("成功")){
            throw new Exception("扣款参数校验失败");
        }
        Thread.sleep(2000l);
        AssetSet assetSet = repaymentPlanService.getRepaymentPlanByRepaymentCode(repaymentPlanNo);
        if(!assetSet.getDeductionStatus().equals(DeductionStatus.LOCAL_PROCESSING) && !assetSet.getDeductionStatus().equals(DeductionStatus.OPPOSITE_PROCESSING)){
            throw new Exception("扣款失败");
        }
        while (true){
            assetSet = repaymentPlanService.getRepaymentPlanByRepaymentCode(repaymentPlanNo);
            if(ExecutingStatus.PROCESSING.equals(assetSet.getExecutingStatus()) && DeductionStatus.SUCCESS.equals(assetSet.getDeductionStatus())){
                break;
            }
            Thread.sleep(10000l);
        }
        Thread.sleep(5000l);
        String result = refactorMethod.mutableFee(productCode,uniqueId,uniqueId,repaymentPlanNo,repayScheduleNo,
                reasonCode,approvedTime,firstFeeType,secondFeeType,thirdFeeType,fourthFeeType,
                firstInterest,secondInterest,thirdInterest,fourthInterest);
        assetSet = repaymentPlanService.getRepaymentPlanByRepaymentCode(this.repaymentPlanNo);
        String assetSetUuid = assetSet.getAssetUuid();
        boolean outcome = result.contains("成功");
        Map<String,BigDecimal> assetSetExtraCharges = repaymentPlanExtraChargeService.getAssetSetExtraChargeModels(assetSetUuid);
        String interest = assetSet.getAssetInterestValue().stripTrailingZeros().toPlainString();
        String serviceFee = assetSetExtraCharges.get(ExtraChargeSpec.LOAN_SERVICE_FEE_KEY).stripTrailingZeros().toPlainString();
        String tecFee = assetSetExtraCharges.get(ExtraChargeSpec.LOAN_TECH_FEE_KEY).stripTrailingZeros().toPlainString();
        String otherFee = assetSetExtraCharges.get(ExtraChargeSpec.LOAN_OTHER_FEE_KEY).stripTrailingZeros().toPlainString();
        Assert.assertEquals(true,outcome);
        Assert.assertEquals("15",interest);
        Assert.assertEquals("30",serviceFee);
        Assert.assertEquals("30",tecFee);
        Assert.assertEquals("30",otherFee);
    }


    /**
     * 浮动费用接口业务校验-对部分扣款成功的还款计划浮动费用且费用小于扣款成功的金额
     * @throws Exception
     */
    @Test
    public void checkParams30() throws Exception{
        String repaymentPlanNo = this.repaymentPlanNo;
        String repayScheduleNo = "";
        String reasonCode = "0";
        String approvedTime = DateUtils.format(new Date());
        Integer firstFeeType = 1;
        Integer secondFeeType = 2;
        Integer thirdFeeType = 3;
        Integer fourthFeeType = 4;
        String firstInterest = "9";
        String secondInterest = "30";
        String thirdInterest = "30";
        String fourthInterest = "30";

        String payAcNo = "6217857600016839330";
        String repaymentType = "1";
        String principal = String.valueOf(Double.valueOf(totalAmount)/3);
        String deductInterest = "10";
        String deductAmount = String.valueOf(Double.valueOf(principal)+Double.valueOf(deductInterest)*4);
        String overDueFee = "0";
        String totalOverDueFee = String.valueOf(Double.valueOf(overDueFee)*4);
        String deductResult = refactorMethod.dedcutRepaymentPlan(productCode,uniqueId,deductAmount,payAcNo,repaymentType,repaymentPlanNo,principal,
                deductInterest,overDueFee,totalOverDueFee);
        if(!deductResult.contains("成功")){
            throw new Exception("扣款参数校验失败");
        }
        Thread.sleep(2000l);
        AssetSet assetSet = repaymentPlanService.getRepaymentPlanByRepaymentCode(repaymentPlanNo);
        if(!assetSet.getDeductionStatus().equals(DeductionStatus.LOCAL_PROCESSING) && !assetSet.getDeductionStatus().equals(DeductionStatus.OPPOSITE_PROCESSING)){
            throw new Exception("扣款失败");
        }
        while (true){
            assetSet = repaymentPlanService.getRepaymentPlanByRepaymentCode(repaymentPlanNo);
            if(ExecutingStatus.PROCESSING.equals(assetSet.getExecutingStatus()) && DeductionStatus.SUCCESS.equals(assetSet.getDeductionStatus())){
                break;
            }
            Thread.sleep(10000l);
        }
        Thread.sleep(5000l);
        String result = refactorMethod.mutableFee(productCode,uniqueId,uniqueId,repaymentPlanNo,repayScheduleNo,
                reasonCode,approvedTime,firstFeeType,secondFeeType,thirdFeeType,fourthFeeType,
                firstInterest,secondInterest,thirdInterest,fourthInterest);
        assetSet = repaymentPlanService.getRepaymentPlanByRepaymentCode(this.repaymentPlanNo);
        String assetSetUuid = assetSet.getAssetUuid();
        boolean outcome = result.contains("非法还息金额");
        Map<String,BigDecimal> assetSetExtraCharges = repaymentPlanExtraChargeService.getAssetSetExtraChargeModels(assetSetUuid);
        String interest = assetSet.getAssetInterestValue().stripTrailingZeros().toPlainString();
        String serviceFee = assetSetExtraCharges.get(ExtraChargeSpec.LOAN_SERVICE_FEE_KEY).stripTrailingZeros().toPlainString();
        String tecFee = assetSetExtraCharges.get(ExtraChargeSpec.LOAN_TECH_FEE_KEY).stripTrailingZeros().toPlainString();
        String otherFee = assetSetExtraCharges.get(ExtraChargeSpec.LOAN_OTHER_FEE_KEY).stripTrailingZeros().toPlainString();
        Assert.assertEquals(true,outcome);
        Assert.assertEquals("20",interest);
        Assert.assertEquals("20",serviceFee);
        Assert.assertEquals("20",tecFee);
        Assert.assertEquals("20",otherFee);
    }

    /**
     * 浮动费用接口业务校验-对部分扣款成功的还款计划浮动费用且费用等于扣款成功的金额
     * @throws Exception
     */
    @Test
    public void checkParams31() throws Exception{
        String repaymentPlanNo = this.repaymentPlanNo;
        String repayScheduleNo = "";
        String reasonCode = "0";
        String approvedTime = DateUtils.format(new Date());
        Integer firstFeeType = 1;
        Integer secondFeeType = 2;
        Integer thirdFeeType = 3;
        Integer fourthFeeType = 4;
        String firstInterest = "10";
        String secondInterest = "10";
        String thirdInterest = "10";
        String fourthInterest = "10";

        String payAcNo = "6217857600016839330";
        String repaymentType = "1";
        String principal = String.valueOf(Double.valueOf(totalAmount)/3);
        String deductInterest = "10";
        String deductAmount = String.valueOf(Double.valueOf(principal)+Double.valueOf(deductInterest)*4);
        String overDueFee = "0";
        String totalOverDueFee = String.valueOf(Double.valueOf(overDueFee)*4);
        String deductResult = refactorMethod.dedcutRepaymentPlan(productCode,uniqueId,deductAmount,payAcNo,repaymentType,repaymentPlanNo,principal,
                deductInterest,overDueFee,totalOverDueFee);
        if(!deductResult.contains("成功")){
            throw new Exception("扣款参数校验失败");
        }
        Thread.sleep(2000l);
        AssetSet assetSet = repaymentPlanService.getRepaymentPlanByRepaymentCode(repaymentPlanNo);
        if(!assetSet.getDeductionStatus().equals(DeductionStatus.LOCAL_PROCESSING) && !assetSet.getDeductionStatus().equals(DeductionStatus.OPPOSITE_PROCESSING)){
            throw new Exception("扣款失败");
        }
        while (true){
            assetSet = repaymentPlanService.getRepaymentPlanByRepaymentCode(repaymentPlanNo);
            if(ExecutingStatus.PROCESSING.equals(assetSet.getExecutingStatus()) && DeductionStatus.SUCCESS.equals(assetSet.getDeductionStatus())){
                break;
            }
            Thread.sleep(10000l);
        }
        Thread.sleep(5000l);
        String result = refactorMethod.mutableFee(productCode,uniqueId,uniqueId,repaymentPlanNo,repayScheduleNo,
                reasonCode,approvedTime,firstFeeType,secondFeeType,thirdFeeType,fourthFeeType,
                firstInterest,secondInterest,thirdInterest,fourthInterest);
        assetSet = repaymentPlanService.getRepaymentPlanByRepaymentCode(this.repaymentPlanNo);
        String assetSetUuid = assetSet.getAssetUuid();
        boolean outcome = result.contains("成功");
        Map<String,BigDecimal> assetSetExtraCharges = repaymentPlanExtraChargeService.getAssetSetExtraChargeModels(assetSetUuid);
        String interest = assetSet.getAssetInterestValue().stripTrailingZeros().toPlainString();
        String serviceFee = assetSetExtraCharges.get(ExtraChargeSpec.LOAN_SERVICE_FEE_KEY).stripTrailingZeros().toPlainString();
        String tecFee = assetSetExtraCharges.get(ExtraChargeSpec.LOAN_TECH_FEE_KEY).stripTrailingZeros().toPlainString();
        String otherFee = assetSetExtraCharges.get(ExtraChargeSpec.LOAN_OTHER_FEE_KEY).stripTrailingZeros().toPlainString();
        Assert.assertEquals(true,outcome);
        Assert.assertEquals("10",interest);
        Assert.assertEquals("10",serviceFee);
        Assert.assertEquals("10",tecFee);
        Assert.assertEquals("10",otherFee);
        Assert.assertEquals(PaymentStatus.SUCCESS,assetSet.getPaymentStatus());
    }

    /**
     * 浮动费用接口业务校验-费用变为0后继续变更
     * @throws Exception
     */
    @Test
    public void checkParams32() throws Exception{
        String repaymentPlanNo = this.repaymentPlanNo;
        String repayScheduleNo = "";
        String reasonCode = "0";
        String approvedTime = DateUtils.format(new Date());
        Integer firstFeeType = 1;
        Integer secondFeeType = 2;
        Integer thirdFeeType = 3;
        Integer fourthFeeType = 4;
        String firstInterest = "0";
        String secondInterest = "0";
        String thirdInterest = "0";
        String fourthInterest = "0";
        String result = refactorMethod.mutableFee(productCode,uniqueId,uniqueId,repaymentPlanNo,repayScheduleNo,
                reasonCode,approvedTime,firstFeeType,secondFeeType,thirdFeeType,fourthFeeType,
                firstInterest,secondInterest,thirdInterest,fourthInterest);
        if(!result.contains("成功")){
            throw new Exception("浮动费用失败");
        }
        firstInterest = "10";
        secondInterest = "10";
        thirdInterest = "10";
        fourthInterest = "10";
        String result2 = refactorMethod.mutableFee(productCode,uniqueId,uniqueId,repaymentPlanNo,repayScheduleNo,
                reasonCode,approvedTime,firstFeeType,secondFeeType,thirdFeeType,fourthFeeType,
                firstInterest,secondInterest,thirdInterest,fourthInterest);
        AssetSet assetSet = repaymentPlanService.getRepaymentPlanByRepaymentCode(this.repaymentPlanNo);
        String assetSetUuid = assetSet.getAssetUuid();
        boolean outcome = result2.contains("成功");
        Map<String,BigDecimal> assetSetExtraCharges = repaymentPlanExtraChargeService.getAssetSetExtraChargeModels(assetSetUuid);
        String interest = assetSet.getAssetInterestValue().stripTrailingZeros().toPlainString();
        String serviceFee = assetSetExtraCharges.get(ExtraChargeSpec.LOAN_SERVICE_FEE_KEY).stripTrailingZeros().toPlainString();
        String tecFee = assetSetExtraCharges.get(ExtraChargeSpec.LOAN_TECH_FEE_KEY).stripTrailingZeros().toPlainString();
        String otherFee = assetSetExtraCharges.get(ExtraChargeSpec.LOAN_OTHER_FEE_KEY).stripTrailingZeros().toPlainString();
        Assert.assertEquals(true,outcome);
        Assert.assertEquals("10",interest);
        Assert.assertEquals("10",serviceFee);
        Assert.assertEquals("10",tecFee);
        Assert.assertEquals("10",otherFee);
    }
}
