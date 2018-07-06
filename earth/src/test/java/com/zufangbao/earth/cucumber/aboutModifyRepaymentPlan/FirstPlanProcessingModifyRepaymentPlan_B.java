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
public class FirstPlanProcessingModifyRepaymentPlan_B extends CucumberBaseTest{
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

    String firstPlanDate = DateUtils.format(new Date());
    String secondPlanDate = DateUtils.format(DateUtils.addMonths(new Date(), 2));
    String thirdPlanDate = DateUtils.format(DateUtils.addMonths(new Date(), 3));
    String expiryDate = DateUtils.format(DateUtils.addMonths(new Date(), 4));
    String modifyFirstPlanDate = DateUtils.format(DateUtils.addDays(new Date(),2));
    String modifySecondPlanDate = DateUtils.format(DateUtils.addDays(new Date(),4));

    @Given("^有一个贷款合同，包含三期还款计划且首期已到期B$")
    public void 有一个贷款合同_包含三期未到期的还款计划B() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        modifyRepaymentPlanCucumberMethod.makeLoan(productCode, uniqueId, totalAmount);
        while(true){
            int executionStatus = remittanceApplicationService.queryExecutionStatus(uniqueId);
            if(executionStatus == 2){
                break;
            }
            Thread.sleep(10000);
        }
        modifyRepaymentPlanCucumberMethod.importAssetPackage(totalAmount,productCode,uniqueId,"0",amount,expiryDate,firstPlanDate,secondPlanDate,thirdPlanDate);

    }

    @When("^对此合同中的所有计划做变更B$")
    public void 对此合同中的所有计划做变更b() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        String firstPlanAmount = "1000";
        String secondPlanAmount = "500";
        result1 = modifyRepaymentPlanCucumberMethod.modifyRepaymentPlan(uniqueId,firstPlanAmount,secondPlanAmount,modifyFirstPlanDate,modifySecondPlanDate);
    }

    @When("^对此合同中的所有计划做变更且变更后有一期在当日B$")
    public void 对此合同中的所有计划做变更且变更后有一期在当日b() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        String firstPlanAmount = "700";
        String secondPlanAmount = "800";
        String firstDate = DateUtils.format(new Date());
        String secondDate = DateUtils.format(DateUtils.addMonths(new Date(),1));
        result2 = modifyRepaymentPlanCucumberMethod.modifyRepaymentPlan(uniqueId,firstPlanAmount,secondPlanAmount,firstDate,secondDate);
    }

    @When("^对此合同中的未到期的计划做变更B$")
    public void 对此合同中的未到期的计划做变更b() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        String firstPlanAmount = "400";
        String secondPlanAmount = "400";
        result3 = modifyRepaymentPlanCucumberMethod.modifyRepaymentPlan(uniqueId,firstPlanAmount,secondPlanAmount,modifyFirstPlanDate,modifySecondPlanDate);
    }

    @Then("^返回结果\"([^\"]*)\"\"([^\"]*)\"\"([^\"]*)\"这个贷款合同下可查询出\"([^\"]*)\"条还款计划B$")
    public void 返回结果_这个贷款合同下可查询出_条还款计划B(String result1, String result2, String result3, String repaymentPlanCount) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        result1 = this.result1;
        result2 = this.result2;
        result3 = this.result3;
        repaymentPlanCount = modifyRepaymentPlanCucumberMethod.queryRepaymentPlanCount(uniqueId);
        Assert.assertEquals("{\"code\":21004,\"message\":\"无效的计划本金总额!\"}",result1);
        Assert.assertEquals("{\"code\":0,\"message\":\"成功!\"}",result2);
        Assert.assertEquals("{\"code\":0,\"message\":\"成功!\"}",result3);
        Assert.assertEquals("3",repaymentPlanCount);

    }

}
