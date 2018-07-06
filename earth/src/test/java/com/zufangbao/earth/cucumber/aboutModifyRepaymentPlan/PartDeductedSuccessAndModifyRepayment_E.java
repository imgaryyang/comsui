package com.zufangbao.earth.cucumber.aboutModifyRepaymentPlan;

import com.zufangbao.cucumber.method.CucumberBaseTest;
import com.zufangbao.cucumber.method.ModifyRepaymentPlanCucumberMethod;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.yunxin.entity.AssetSet;
import com.zufangbao.sun.yunxin.entity.ExecutingStatus;
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
public class PartDeductedSuccessAndModifyRepayment_E extends CucumberBaseTest {
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
    String result3 = "";

    String firstPlanDate = DateUtils.format(DateUtils.addMonths(new Date(), 1));
    String secondPlanDate = DateUtils.format(DateUtils.addMonths(new Date(), 2));
    String thirdPlanDate = DateUtils.format(DateUtils.addMonths(new Date(), 3));
    String expiryDate = DateUtils.format(DateUtils.addMonths(new Date(), 4));

    String firstPlanNo = "";


    @Given("^有一个贷款合同，包含三期未到期的还款计划E$")
    public void 有一个贷款合同_包含三期未到期的还款计划E() throws Throwable {
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

    @When("^对第一期做部分提前划拨E$")
    public void 对第一期做部分提前划拨e() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        firstPlanNo = modifyRepaymentPlanCucumberMethod.queryFirstRepaymentPlan(uniqueId);
        String deductAmount = "200";
        modifyRepaymentPlanCucumberMethod.deductRepaymentPlan(firstPlanNo,uniqueId, productCode, deductAmount,"0");
    }

    @When("^在第一期还在扣款中的时候对后面的还款计划做变更且日期都在原首期之后E$")
    public void 在第一期还在扣款中的时候对后面的还款计划做变更且日期都在原首期之后e() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        String firstModifyDate = DateUtils.format(DateUtils.addDays(DateUtils.parseDate(firstPlanDate),1));
        String secondModifyDate = DateUtils.format(DateUtils.addDays(DateUtils.parseDate(firstPlanDate),2));
        result1 = modifyRepaymentPlanCucumberMethod.modifyRepaymentPlan(uniqueId, amount, amount, firstModifyDate, secondModifyDate);
    }

    @When("^在第一期部分提前划拨成功后对后面的还款计划做变更且有一期日期在原首期之前E$")
    public void 在第一期部分提前划拨成功后对后面的还款计划做变更且有一期日期在原首期之前e() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        while(true){
            AssetSet assetSet = repaymentPlanService.getRepaymentPlanByRepaymentCode(firstPlanNo);
            ExecutingStatus executingStatus = assetSet.getExecutingStatus();
            if(ExecutingStatus.PROCESSING == executingStatus){
                break;
            }
            Thread.sleep(10000);
        }
        String firstModifyDate = DateUtils.format(DateUtils.addDays(DateUtils.parseDate(firstPlanDate),-1));
        String secondModifyDate = DateUtils.format(DateUtils.addDays(DateUtils.parseDate(firstPlanDate),1));
        result2 = modifyRepaymentPlanCucumberMethod.modifyRepaymentPlan(uniqueId, amount, amount, firstModifyDate, secondModifyDate);
    }

    @When("^在第一期部分提前划拨成功后对后面的还款计划做变更且日期都在原首期之后E$")
    public void 在第一期部分提前划拨成功后对后面的还款计划做变更且日期都在原首期之后e() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        String firstModifyDate = DateUtils.format(DateUtils.addDays(DateUtils.parseDate(firstPlanDate),1));
        String secondModifyDate = DateUtils.format(DateUtils.addDays(DateUtils.parseDate(firstPlanDate),2));
        result3 = modifyRepaymentPlanCucumberMethod.modifyRepaymentPlan(uniqueId, amount, amount, firstModifyDate, secondModifyDate);
    }

    @Then("^返回结果\"([^\"]*)\"\"([^\"]*)\"\"([^\"]*)\"E$")
    public void 返回结果_E(String result1, String result2, String result3) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        result1 = this.result1;
        result2 = this.result2;
        result3 = this.result3;
        Assert.assertEquals("{\"code\":21018,\"message\":\"存在当日扣款成功或处理中的还款计划!\"}",result1);
        Assert.assertEquals("{\"code\":21004,\"message\":\"无效的计划本金总额!\"}",result2);
        Assert.assertEquals("{\"code\":21004,\"message\":\"无效的计划本金总额!\"}",result3);

    }


}
