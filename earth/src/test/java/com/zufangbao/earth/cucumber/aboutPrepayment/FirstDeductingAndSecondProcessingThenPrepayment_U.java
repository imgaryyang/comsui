package com.zufangbao.earth.cucumber.aboutPrepayment;

import com.zufangbao.cucumber.method.CucumberBaseTest;
import com.zufangbao.cucumber.method.PrepaymentCucumberMethod;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.yunxin.service.remittance.IRemittanceApplicationService;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.UUID;

/**
 * Created by FanT on 2017/3/27.
 */
public class FirstDeductingAndSecondProcessingThenPrepayment_U extends CucumberBaseTest {
    @Autowired
    private IRemittanceApplicationService remittanceApplicationService;

    private String productCode = "CS0001";

    private String uniqueId = UUID.randomUUID().toString();

    private String totalAmount = "1500";

    private String amount = "500";
    String result1 = "";
    String result2 = "";
    String firstPlanDate = DateUtils.format(new Date());
    String secondPlanDate = DateUtils.format(new Date());
    String thirdPlanDate = DateUtils.format(DateUtils.addMonths(new Date(), 1));
    String applyDate = DateUtils.format(new Date());
    PrepaymentCucumberMethod prepaymentCucumberMethod = new PrepaymentCucumberMethod();

    @Given("^有三期还款计划，一期正在扣款中，一期在处理中，一期未到期U$")
    public void 有三期还款计划_一期正在扣款中_一期在处理中_一期未到期U() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        prepaymentCucumberMethod.importAssetPackage(totalAmount,productCode,uniqueId,"0",amount,firstPlanDate,secondPlanDate,thirdPlanDate);
        String firstPlanNo = prepaymentCucumberMethod.queryFirstRepaymentPlan(uniqueId);
        prepaymentCucumberMethod.deductRepaymentPlan(firstPlanNo,uniqueId,productCode,amount,"1");

    }

    @When("^对此贷款合同下未到期的还款计划进行提前还款申请且申请日在当天U$")
    public void 对此贷款合同下未到期的还款计划进行提前还款申请且申请日在当天u() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        result1 = prepaymentCucumberMethod.applyPrepaymentPlan(uniqueId,amount,amount,applyDate);

    }

    @When("^对此贷款合同进行正常的提前还款申请且申请日在当天U$")
    public void 对此贷款合同进行正常的提前还款申请且申请日在当天u() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        int applyAmount = Integer.parseInt(amount)*2;
        String amount = String.valueOf(applyAmount);
        result2 = prepaymentCucumberMethod.applyPrepaymentPlan(uniqueId,amount,amount,applyDate);

    }

    @Then("^返回结果\"([^\"]*)\"，\"([^\"]*)\"并且查询此贷款合同下一共有\"([^\"]*)\"条还款计划U$")
    public void 返回结果_并且查询此贷款合同下一共有_条还款计划U(String result1, String result2, String repaymentPlanCount) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        result1 = this.result1;
        result2 = this.result2;
        boolean outcome = result1.contains("提前还款总金额或本金错误");
        boolean outcome2 = result2.contains("成功");
        repaymentPlanCount = prepaymentCucumberMethod.queryRepaymentPlanCount(uniqueId);
        Assert.assertEquals(true,outcome);
        Assert.assertEquals(true,outcome2);
        Assert.assertEquals("2",repaymentPlanCount);


    }



}
