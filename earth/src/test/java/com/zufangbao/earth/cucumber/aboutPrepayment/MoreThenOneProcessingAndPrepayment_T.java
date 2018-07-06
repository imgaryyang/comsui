package com.zufangbao.earth.cucumber.aboutPrepayment;

import com.zufangbao.cucumber.method.CucumberBaseTest;
import com.zufangbao.cucumber.method.PrepaymentCucumberMethod;
import com.zufangbao.sun.utils.DateUtils;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;

import java.util.Date;
import java.util.UUID;

/**
 * Created by FanT on 2017/3/27.
 */
public class MoreThenOneProcessingAndPrepayment_T extends CucumberBaseTest {

    private String productCode = "CS0001";

    private String uniqueId = UUID.randomUUID().toString();

    private String totalAmount = "1500";

    private String amount = "500";
    String result = "";
    String firstPlanDate = DateUtils.format(new Date());
    String secondPlanDate = DateUtils.format(new Date());
    String thirdPlanDate = DateUtils.format(DateUtils.addMonths(new Date(), 1));
    String applyDate = DateUtils.format(new Date());
    PrepaymentCucumberMethod prepaymentCucumberMethod = new PrepaymentCucumberMethod();

    @Given("^有三期还款计划，前两期已到期并且在处理中T$")
    public void 有三期还款计划_前两期已到期并且在处理中T() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        prepaymentCucumberMethod.importAssetPackage(totalAmount,productCode,uniqueId,"0",amount,firstPlanDate,secondPlanDate,thirdPlanDate);


    }

    @When("^对此贷款合同下所有的还款计划进行提前还款申请且申请日在当天T$")
    public void 对此贷款合同下所有的还款计划进行提前还款申请且申请日在当天t() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        result = prepaymentCucumberMethod.applyPrepaymentPlan(uniqueId,totalAmount,totalAmount,applyDate);
    }

    @Then("^返回结果\"([^\"]*)\"并且查询此贷款合同下一共有\"([^\"]*)\"条还款计划T$")
    public void 返回结果_并且查询此贷款合同下一共有_条还款计划T(String result, String repaymentPlanCount) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        result = this.result;
        boolean outcome = result.contains("成功");
        repaymentPlanCount = prepaymentCucumberMethod.queryRepaymentPlanCount(uniqueId);
        Assert.assertEquals(true,outcome);
        Assert.assertEquals("1",repaymentPlanCount);

    }

}
