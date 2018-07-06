package com.zufangbao.earth.cucumber.aboutModifyRepaymentPlan;

import com.zufangbao.cucumber.method.CucumberBaseTest;
import com.zufangbao.cucumber.method.ModifyRepaymentPlanCucumberMethod;
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
 * Created by FanT on 2017/3/20.
 */
public class NomalModifyRepaymentPlan_A extends CucumberBaseTest {
    ModifyRepaymentPlanCucumberMethod modifyRepaymentPlanCucumberMethod = new ModifyRepaymentPlanCucumberMethod();

    @Autowired
    IRemittanceApplicationService remittanceApplicationService;
    private String productCode = "G31700";

    private String uniqueId = UUID.randomUUID().toString();

    private String totalAmount = "1500";

    private String amount = "500";

    String result1 = "";
    String result2 = "";
    String result3 = "";
    String result4 = "";
    String firstPlanDate = DateUtils.format(DateUtils.addMonths(new Date(), 1));
    String secondPlanDate = DateUtils.format(DateUtils.addMonths(new Date(), 2));
    String thirdPlanDate = DateUtils.format(DateUtils.addMonths(new Date(), 3));
    String expiryDate = DateUtils.format(DateUtils.addMonths(new Date(), 4));

    @Given("^有一个贷款合同，包含三期未到期的还款计划A$")
    public void 有一个贷款合同_包含三期未到期的还款计划A() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        modifyRepaymentPlanCucumberMethod.makeLoan(productCode,uniqueId,totalAmount);
        while(true){
            int executionStatus = remittanceApplicationService.queryExecutionStatus(uniqueId);
            if(executionStatus == 2){
                break;
            }
            Thread.sleep(10000);
        }
        modifyRepaymentPlanCucumberMethod.importAssetPackage(totalAmount,productCode,uniqueId,"0",amount,expiryDate,firstPlanDate,secondPlanDate,thirdPlanDate);
    }

    @When("^对此合同中的还款计划做变更且第一期计划日期在当日之前A$")
    public void 对此合同中的还款计划做变更且第一期计划日期在当日之前a() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        String firstRequestDate = DateUtils.format(DateUtils.addDays(new Date(),-1));
        String secondRequestDate = DateUtils.format(DateUtils.addDays(new Date(),10));
        String firstPlanAmout = "750";
        String secondPlanAmout = "750";
        result1 = modifyRepaymentPlanCucumberMethod.modifyRepaymentPlan(uniqueId,firstPlanAmout,secondPlanAmout, firstRequestDate, secondRequestDate);
    }

    @When("^对此合同中的还款计划做变更且有一期计划日子在合同终止日之后A$")
    public void 对此合同中的还款计划做变更且有一期计划日子在合同终止日之后a() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        String firstRequestDate = DateUtils.format(DateUtils.addDays(new Date(),10));
        String secondRequestDate = DateUtils.format(DateUtils.addDays(DateUtils.parseDate(expiryDate),1));
        String firstPlanAmout = "750";
        String secondPlanAmout = "750";
        result2 = modifyRepaymentPlanCucumberMethod.modifyRepaymentPlan(uniqueId,firstPlanAmout,secondPlanAmout, firstRequestDate, secondRequestDate);
    }

    @When("^对此合同中的还款计划做变更且本金总额错误A$")
    public void 对此合同中的还款计划做变更且本金总额错误a() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        String firstRequestDate = DateUtils.format(DateUtils.addDays(new Date(),10));
        String secondRequestDate = DateUtils.format(DateUtils.addDays(new Date(),20));
        String firstPlanAmout = "1000";
        String secondPlanAmout = "750";
        result3 = modifyRepaymentPlanCucumberMethod.modifyRepaymentPlan(uniqueId,firstPlanAmout,secondPlanAmout, firstRequestDate, secondRequestDate);
    }

    @When("^将此合同中的还款计划变更未两期并且参数都正确A$")
    public void 将此合同中的还款计划变更未两期并且参数都正确a() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        String firstRequestDate = DateUtils.format(DateUtils.addDays(new Date(),10));
        String secondRequestDate = DateUtils.format(DateUtils.addDays(new Date(),20));
        String firstPlanAmout = "1000";
        String secondPlanAmout = "500";
        result4 = modifyRepaymentPlanCucumberMethod.modifyRepaymentPlan(uniqueId,firstPlanAmout,secondPlanAmout, firstRequestDate, secondRequestDate);
    }

    @Then("^返回结果\"([^\"]*)\"\"([^\"]*)\"\"([^\"]*)\"\"([^\"]*)\"这个贷款合同下可查询出\"([^\"]*)\"条还款计划A$")
    public void 返回结果_这个贷款合同下可查询出_条还款计划A(String result1, String result2, String result3, String result4, String repaymentPlanCount) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        result1 = this.result1;
        result2 = this.result2;
        result3 = this.result3;
        result4 = this.result4;
        Assert.assertEquals("{\"code\":21009,\"message\":\"计划还款日期排序错误，需按计划还款日期递增!\"}",result1);
        Assert.assertEquals("{\"code\":21016,\"message\":\"计划还款日期不能晚于贷款合同终止日期!\"}",result2);
        Assert.assertEquals("{\"code\":21004,\"message\":\"无效的计划本金总额!\"}",result3);
        Assert.assertEquals("{\"code\":0,\"message\":\"成功!\"}",result4);
        repaymentPlanCount = modifyRepaymentPlanCucumberMethod.queryRepaymentPlanCount(uniqueId);
        Assert.assertEquals("2",repaymentPlanCount);
    }

}
