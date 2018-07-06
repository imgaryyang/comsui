package com.zufangbao.aboutPrepayment;

import com.demo2do.core.utils.DateUtils;
import com.zufangbao.refactor.method.RefactorMethod;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.UUID;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:/local/applicationContext-*.xml" })
public class CheckPrepaymentParams {
    RefactorMethod refactorMethod = new RefactorMethod();

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
        String firstPlanDate = DateUtils.format(new Date());
        String secondPlanDate = DateUtils.format(DateUtils.addMonths(new Date(), 1));
        String thirdPlanDate = DateUtils.format(DateUtils.addMonths(new Date(), 2));

        refactorMethod.importAssetPackage(totalAmount, productCode, uniqueId, repaymentAccountNo, interest, loanCustomerNo,
                firstPlanDate, secondPlanDate, thirdPlanDate, loanCustomerName);
        firstRepaymentPlan = refactorMethod.queryRepaymentPlan(productCode,uniqueId,0);
    }

    /**
     * 提前还款接口参数校验-uniqueId和contractNo都为空
     */
    @Test
    public void checkParams1(){
        String uniqueId = "";
        String contractNo = "";
        String assetRecycleDate = DateUtils.format(new Date());
        String assetInitialValue = totalAmount;
        String assetPrincipal = totalAmount;
        String interest = "0";
        String type = "0";
        String result = refactorMethod.applyPrepaymentPlan(uniqueId,contractNo,assetRecycleDate,assetInitialValue,assetPrincipal,interest,type);
        boolean outcome = result.contains("贷款合同不存在");
        String repaymentPlanCount = refactorMethod.queryRepaymentPlanCount(this.uniqueId);
        Assert.assertEquals(true,outcome);
        Assert.assertEquals("3",repaymentPlanCount);
    }

    /**
     * 提前还款接口参数校验-uniqueId和contractNo都填
     */
    @Test
    public void checkParams2(){
        String uniqueId = this.uniqueId;
        String contractNo = this.uniqueId;
        String assetRecycleDate = DateUtils.format(new Date());
        String assetInitialValue = totalAmount;
        String assetPrincipal = totalAmount;
        String interest = "0";
        String type = "0";
        String result = refactorMethod.applyPrepaymentPlan(uniqueId,contractNo,assetRecycleDate,assetInitialValue,assetPrincipal,interest,type);
        boolean outcome = result.contains("成功");
        String repaymentPlanCount = refactorMethod.queryRepaymentPlanCount(this.uniqueId);
        Assert.assertEquals(true,outcome);
        Assert.assertEquals("1",repaymentPlanCount);
    }

    /**
     * 提前还款接口参数校验-uniqueId正确，contractNo错误
     */
    @Test
    public void checkParams3(){
        String uniqueId = this.uniqueId;
        String contractNo = this.uniqueId+1;
        String assetRecycleDate = DateUtils.format(new Date());
        String assetInitialValue = totalAmount;
        String assetPrincipal = totalAmount;
        String interest = "0";
        String type = "0";
        String result = refactorMethod.applyPrepaymentPlan(uniqueId,contractNo,assetRecycleDate,assetInitialValue,assetPrincipal,interest,type);
        boolean outcome = result.contains("成功");
        String repaymentPlanCount = refactorMethod.queryRepaymentPlanCount(this.uniqueId);
        Assert.assertEquals(true,outcome);
        Assert.assertEquals("1",repaymentPlanCount);
    }

    /**
     * 提前还款接口参数校验-uniqueId错误，contractNo正确
     */
    @Test
    public void checkParams4(){
        String uniqueId = this.uniqueId+1;
        String contractNo = this.uniqueId;
        String assetRecycleDate = DateUtils.format(new Date());
        String assetInitialValue = totalAmount;
        String assetPrincipal = totalAmount;
        String interest = "0";
        String type = "0";
        String result = refactorMethod.applyPrepaymentPlan(uniqueId,contractNo,assetRecycleDate,assetInitialValue,assetPrincipal,interest,type);
        boolean outcome = result.contains("贷款合同不存在");
        String repaymentPlanCount = refactorMethod.queryRepaymentPlanCount(this.uniqueId);
        Assert.assertEquals(true,outcome);
        Assert.assertEquals("3",repaymentPlanCount);
    }

    /**
     * 提前还款接口参数校验-只填contractNo
     */
    @Test
    public void checkParams5(){
        String uniqueId = "";
        String contractNo = this.uniqueId;
        String assetRecycleDate = DateUtils.format(new Date());
        String assetInitialValue = totalAmount;
        String assetPrincipal = totalAmount;
        String interest = "0";
        String type = "0";
        String result = refactorMethod.applyPrepaymentPlan(uniqueId,contractNo,assetRecycleDate,assetInitialValue,assetPrincipal,interest,type);
        boolean outcome = result.contains("成功");
        String repaymentPlanCount = refactorMethod.queryRepaymentPlanCount(this.uniqueId);
        Assert.assertEquals(true,outcome);
        Assert.assertEquals("1",repaymentPlanCount);
    }

    /**
     * 提前还款接口参数校验-还款日期格式错误
     */
    @Test
    public void checkParams6(){
        String uniqueId = this.uniqueId;
        String contractNo = "";
        String assetRecycleDate = "11111";
        String assetInitialValue = totalAmount;
        String assetPrincipal = totalAmount;
        String interest = "0";
        String type = "0";
        String result = refactorMethod.applyPrepaymentPlan(uniqueId,contractNo,assetRecycleDate,assetInitialValue,assetPrincipal,interest,type);
        boolean outcome = result.contains("计划还款日期格式错误");
        String repaymentPlanCount = refactorMethod.queryRepaymentPlanCount(this.uniqueId);
        Assert.assertEquals(true,outcome);
        Assert.assertEquals("3",repaymentPlanCount);
    }

    /**
     * 提前还款接口参数校验-assetInitialValue为空
     */
    @Test
    public void checkParams7(){
        String uniqueId = this.uniqueId;
        String contractNo = "";
        String assetRecycleDate = DateUtils.format(new Date());
        String assetInitialValue = "";
        String assetPrincipal = totalAmount;
        String interest = "0";
        String type = "0";
        String result = refactorMethod.applyPrepaymentPlan(uniqueId,contractNo,assetRecycleDate,assetInitialValue,assetPrincipal,interest,type);
        boolean outcome = result.contains("无效参数-assetInitialValue");
        String repaymentPlanCount = refactorMethod.queryRepaymentPlanCount(this.uniqueId);
        Assert.assertEquals(true,outcome);
        Assert.assertEquals("3",repaymentPlanCount);
    }

    /**
     * 提前还款接口参数校验-assetInitialValue为0
     */
    @Test
    public void checkParams8(){
        String uniqueId = this.uniqueId;
        String contractNo = "";
        String assetRecycleDate = DateUtils.format(new Date());
        String assetInitialValue = "0";
        String assetPrincipal = totalAmount;
        String interest = "0";
        String type = "0";
        String result = refactorMethod.applyPrepaymentPlan(uniqueId,contractNo,assetRecycleDate,assetInitialValue,assetPrincipal,interest,type);
        boolean outcome = result.contains("无效参数-assetInitialValue");
        String repaymentPlanCount = refactorMethod.queryRepaymentPlanCount(this.uniqueId);
        Assert.assertEquals(true,outcome);
        Assert.assertEquals("3",repaymentPlanCount);
    }

    /**
     * 提前还款接口参数校验-assetPrincipal为空
     */
    @Test
    public void checkParams9(){
        String uniqueId = this.uniqueId;
        String contractNo = "";
        String assetRecycleDate = DateUtils.format(new Date());
        String assetInitialValue = totalAmount;
        String assetPrincipal = "";
        String interest = "0";
        String type = "0";
        String result = refactorMethod.applyPrepaymentPlan(uniqueId,contractNo,assetRecycleDate,assetInitialValue,assetPrincipal,interest,type);
        boolean outcome = result.contains("无效参数-assetPrincipal:金额格式错误");
        String repaymentPlanCount = refactorMethod.queryRepaymentPlanCount(this.uniqueId);
        Assert.assertEquals(true,outcome);
        Assert.assertEquals("3",repaymentPlanCount);
    }

    /**
     * 提前还款接口参数校验-assetPrincipal为0
     */
    @Test
    public void checkParams10(){
        String uniqueId = this.uniqueId;
        String contractNo = "";
        String assetRecycleDate = DateUtils.format(new Date());
        String assetInitialValue = totalAmount;
        String assetPrincipal = "0";
        String interest = "0";
        String type = "0";
        String result = refactorMethod.applyPrepaymentPlan(uniqueId,contractNo,assetRecycleDate,assetInitialValue,assetPrincipal,interest,type);
        boolean outcome = result.contains("无效参数-assetPrincipal");
        String repaymentPlanCount = refactorMethod.queryRepaymentPlanCount(this.uniqueId);
        Assert.assertEquals(true,outcome);
        Assert.assertEquals("3",repaymentPlanCount);
    }

    /**
     * 提前还款接口参数校验-利息为空
     */
    @Test
    public void checkParams11(){
        String uniqueId = this.uniqueId;
        String contractNo = "";
        String assetRecycleDate = DateUtils.format(new Date());
        String assetInitialValue = totalAmount;
        String assetPrincipal = totalAmount;
        String interest = "";
        String type = "0";
        String result = refactorMethod.applyPrepaymentPlan(uniqueId,contractNo,assetRecycleDate,assetInitialValue,assetPrincipal,interest,type);
        boolean outcome = result.contains("无效参数");
        String repaymentPlanCount = refactorMethod.queryRepaymentPlanCount(this.uniqueId);
        Assert.assertEquals(true,outcome);
        Assert.assertEquals("3",repaymentPlanCount);
    }

    /**
     * 提前还款接口参数校验-提前还款明细金额之和与总金额不相等
     */
    @Test
    public void checkParams12(){
        String uniqueId = this.uniqueId;
        String contractNo = "";
        String assetRecycleDate = DateUtils.format(new Date());
        String assetInitialValue = totalAmount;
        String assetPrincipal = totalAmount;
        String interest = "20";
        String type = "0";
        String result = refactorMethod.applyPrepaymentPlan(uniqueId,contractNo,assetRecycleDate,assetInitialValue,assetPrincipal,interest,type);
        boolean outcome = result.contains("提前还款明细金额之和与总金额[assetInitialValue]不匹配");
        String repaymentPlanCount = refactorMethod.queryRepaymentPlanCount(this.uniqueId);
        Assert.assertEquals(true,outcome);
        Assert.assertEquals("3",repaymentPlanCount);
    }

    /**
     * 提前还款接口参数校验-type非0
     */
    @Test
    public void checkParams13(){
        String uniqueId = this.uniqueId;
        String contractNo = "";
        String assetRecycleDate = DateUtils.format(new Date());
        String assetInitialValue = totalAmount;
        String assetPrincipal = totalAmount;
        String interest = "0";
        String type = "1";
        String result = refactorMethod.applyPrepaymentPlan(uniqueId,contractNo,assetRecycleDate,assetInitialValue,assetPrincipal,interest,type);
        boolean outcome = result.contains("无效参数-type:目前仅支持:");
        String repaymentPlanCount = refactorMethod.queryRepaymentPlanCount(this.uniqueId);
        Assert.assertEquals(true,outcome);
        Assert.assertEquals("3",repaymentPlanCount);
    }
}
