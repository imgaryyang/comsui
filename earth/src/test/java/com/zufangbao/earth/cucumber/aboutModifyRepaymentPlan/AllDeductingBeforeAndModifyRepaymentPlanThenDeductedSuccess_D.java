package com.zufangbao.earth.cucumber.aboutModifyRepaymentPlan;

import com.zufangbao.cucumber.method.CucumberBaseTest;
import com.zufangbao.cucumber.method.ModifyRepaymentPlanCucumberMethod;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.yunxin.entity.AssetSet;
import com.zufangbao.sun.yunxin.entity.PaymentStatus;
import com.zufangbao.sun.yunxin.service.RepaymentPlanService;
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
public class AllDeductingBeforeAndModifyRepaymentPlanThenDeductedSuccess_D extends CucumberBaseTest{
    ModifyRepaymentPlanCucumberMethod modifyRepaymentPlanCucumberMethod = new ModifyRepaymentPlanCucumberMethod();

    @Autowired
    IRemittanceApplicationService remittanceApplicationService;
    @Autowired
    RepaymentPlanService repaymentPlanService;

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

    String firstPlanNo = "";
    String firstModifyDate = DateUtils.format(DateUtils.addDays(DateUtils.parseDate(firstPlanDate),-1));
    String secondModifyDate = DateUtils.format(DateUtils.addDays(DateUtils.parseDate(firstPlanDate),1));
    @Given("^有一个贷款合同，包含三期未到期的还款计划D$")
    public void 有一个贷款合同_包含三期未到期的还款计划D() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        modifyRepaymentPlanCucumberMethod.makeLoan(productCode, uniqueId, totalAmount);
        while(true){
            int executionStatus = remittanceApplicationService.queryExecutionStatus(uniqueId);
            if(executionStatus == 2){
                break;
            }
            Thread.sleep(10000);
        }
        modifyRepaymentPlanCucumberMethod.importAssetPackage(totalAmount, productCode, uniqueId,"0",amount, expiryDate, firstPlanDate, secondPlanDate, thirdPlanDate);
    }

    @When("^对第一期做全额提前划拨D$")
    public void 对第一期做全额提前划拨d() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        firstPlanNo = modifyRepaymentPlanCucumberMethod.queryFirstRepaymentPlan(uniqueId);
        modifyRepaymentPlanCucumberMethod.deductRepaymentPlan(firstPlanNo,uniqueId, productCode, amount, "0");

    }

    @When("^在第一期还在扣款中的时候对后面的还款计划做变更且有一期日期在原首期之前D$")
    public void 在第一期还在扣款中的时候对后面的还款计划做变更且有一期日期在原首期之前d() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        result1 = modifyRepaymentPlanCucumberMethod.modifyRepaymentPlan(uniqueId, amount, amount, firstModifyDate, secondModifyDate);
    }

    @When("^在第一期还款成功后对后面的还款计划做变更且有一期日期在原首期之前D$")
    public void 在第一期还款成功后对后面的还款计划做变更且有一期日期在原首期之前d() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        while(true){
            AssetSet assetSet = repaymentPlanService.getRepaymentPlanByRepaymentCode(firstPlanNo);
            PaymentStatus paymentStatus = assetSet.getPaymentStatus();
            if(PaymentStatus.SUCCESS == paymentStatus){
                break;
            }
            Thread.sleep(10000);
        }
        result2 = modifyRepaymentPlanCucumberMethod.modifyRepaymentPlan(uniqueId, amount, amount, firstModifyDate, secondModifyDate);
    }

    @Then("^返回结果\"([^\"]*)\"\"([^\"]*)\"D$")
    public void 返回结果_D(String result1, String result2) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        result1 = this.result1;
        result2 = this.result2;
        Assert.assertEquals("{\"code\":21018,\"message\":\"存在当日扣款成功或处理中的还款计划!\"}",result1);
        Assert.assertEquals("{\"code\":0,\"message\":\"成功!\"}",result2);
    }


}
