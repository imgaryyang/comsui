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
 * Created by FanT on 2017/3/21.
 */
public class ModifyRepaymentPlanSomeoneDuplicate_F extends CucumberBaseTest {
    ModifyRepaymentPlanCucumberMethod modifyRepaymentPlanCucumberMethod = new ModifyRepaymentPlanCucumberMethod();

    @Autowired
    IRemittanceApplicationService remittanceApplicationService;
    private String productCode = "G31700";

    private String uniqueId = UUID.randomUUID().toString();

    private String totalAmount = "1500";

    private String amount = "500";

    String result1 = "";
    String result2 = "";

    String firstPlanDate = DateUtils.format(DateUtils.addMonths(new Date(), 1));
    String secondPlanDate = DateUtils.format(DateUtils.addMonths(new Date(), 2));
    String thirdPlanDate = DateUtils.format(DateUtils.addMonths(new Date(), 3));
    String expiryDate = DateUtils.format(DateUtils.addMonths(new Date(), 4));

    @Given("^有一个贷款合同，包含三期未到期的还款计划F$")
    public void 有一个贷款合同_包含三期未到期的还款计划F() throws Throwable {
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

    @When("^对此合同中的还款计划做一次变更F$")
    public void 对此合同中的还款计划做一次变更f() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        String firstModifyDate = DateUtils.format(DateUtils.addDays(new Date(), 1));
        String secondModifyDate = DateUtils.format(DateUtils.addDays(new Date(), 2));
        result1 = modifyRepaymentPlanCucumberMethod.modifyRepaymentPlan(uniqueId, "1000", amount, firstModifyDate, secondModifyDate);
    }

    @When("^对此合同中的还款计划再做一次变更且有一期信息与第一次完全重复F$")
    public void 对此合同中的还款计划再做一次变更且有一期信息与第一次完全重复f() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        String firstModifyDate = DateUtils.format(DateUtils.addDays(new Date(), 1));
        String secondModifyDate = DateUtils.format(DateUtils.addDays(new Date(), 3));
        result2 = modifyRepaymentPlanCucumberMethod.modifyRepaymentPlan(uniqueId, "1000", amount, firstModifyDate, secondModifyDate);
    }

    @Then("^返回结果\"([^\"]*)\"\"([^\"]*)\"F$")
    public void 返回结果_F(String result1, String result2) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        result1 = this.result1;
        result2 = this.result2;
        Assert.assertEquals("{\"code\":0,\"message\":\"成功!\"}",result1);
        Assert.assertEquals("{\"code\":21021,\"message\":\"与原计划一致，不予变更!\"}",result2);
    }

}
