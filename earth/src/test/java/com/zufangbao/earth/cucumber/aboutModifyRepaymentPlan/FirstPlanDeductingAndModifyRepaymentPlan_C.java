package com.zufangbao.earth.cucumber.aboutModifyRepaymentPlan;

import com.zufangbao.cucumber.method.CucumberBaseTest;
import com.zufangbao.cucumber.method.ModifyRepaymentPlanCucumberMethod;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.yunxin.entity.AssetSet;
import com.zufangbao.sun.yunxin.entity.DeductionStatus;
import com.zufangbao.sun.yunxin.entity.ExecutingStatus;
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
 * Created by FanT on 2017/3/20.
 */
public class FirstPlanDeductingAndModifyRepaymentPlan_C extends CucumberBaseTest {
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
    String result4 = "";
    String firstPlanDate = DateUtils.format(new Date());
    String secondPlanDate = DateUtils.format(DateUtils.addMonths(new Date(), 2));
    String thirdPlanDate = DateUtils.format(DateUtils.addMonths(new Date(), 3));
    String expiryDate = DateUtils.format(DateUtils.addMonths(new Date(), 4));

    String firstPlanNo = "";

    @Given("^有一个贷款合同，包含三期还款计划且首期已到期C$")
    public void 有一个贷款合同_包含三期还款计划且首期已到期C() throws Throwable {
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

    @When("^对首期进行部分扣款C$")
    public void 对首期进行部分扣款c() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        String deductAmount = "200";
        firstPlanNo = modifyRepaymentPlanCucumberMethod.queryFirstRepaymentPlan(uniqueId);
        modifyRepaymentPlanCucumberMethod.deductRepaymentPlan(firstPlanNo,uniqueId,productCode,deductAmount,"1");
    }

    @When("^对此合同中的未到期的计划做变更且有一期在当日C$")
    public void 对此合同中的未到期的计划做变更且有一期在当日c() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        String firstModifyDate = DateUtils.format(new Date());
        String secondModifyDate = DateUtils.format(DateUtils.addDays(DateUtils.parseDate(firstModifyDate),5));
        result1 = modifyRepaymentPlanCucumberMethod.modifyRepaymentPlan(uniqueId,amount,amount,firstModifyDate,secondModifyDate);
    }

    @When("^对未到期的计划做正常变更C$")
    public void 对未到期的计划做正常变更c() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        String firstModifyDate = DateUtils.format(DateUtils.addMonths(new Date(),1));
        String secondModifyDate = DateUtils.format(DateUtils.addMonths(new Date(),2));
        result2 = modifyRepaymentPlanCucumberMethod.modifyRepaymentPlan(uniqueId,amount,amount,firstModifyDate,secondModifyDate);

    }

    @When("^首期部分扣款成功后对未到期的计划做变更且有一期在当日C$")
    public void 首期部分扣款成功后对未到期的计划做变更且有一期在当日c() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        while(true){
            AssetSet assetSet = repaymentPlanService.getRepaymentPlanByRepaymentCode(firstPlanNo);
            ExecutingStatus executingStatus = assetSet.getExecutingStatus();
            DeductionStatus deductionStatus = assetSet.getDeductionStatus();
            if(ExecutingStatus.PROCESSING == executingStatus && DeductionStatus.SUCCESS == deductionStatus){
                break;
            }
            Thread.sleep(10000);
        }
        String firstModifyDate = DateUtils.format(new Date());
        String secondModifyDate = DateUtils.format(DateUtils.addDays(DateUtils.parseDate(firstModifyDate),10));
        result3 = modifyRepaymentPlanCucumberMethod.modifyRepaymentPlan(uniqueId,amount,amount,firstModifyDate,secondModifyDate);
    }

    @When("^对首期进行全额扣款，扣款成功后对未到期的计划做变更且有一期在当日C$")
    public void 对首期进行全额扣款_扣款成功后对未到期的计划做变更且有一期在当日C() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        String deductAmount = "300";
        modifyRepaymentPlanCucumberMethod.deductRepaymentPlan(firstPlanNo,uniqueId,productCode,deductAmount,"1");
        while(true){
            AssetSet assetSet = repaymentPlanService.getRepaymentPlanByRepaymentCode(firstPlanNo);
            PaymentStatus paymentStatus = assetSet.getPaymentStatus();
            if(PaymentStatus.SUCCESS == paymentStatus){
                break;
            }
            Thread.sleep(10000);
        }
        String firstModifyDate = DateUtils.format(new Date());
        String secondModifyDate = DateUtils.format(DateUtils.addDays(DateUtils.parseDate(firstModifyDate),10));
        result4 = modifyRepaymentPlanCucumberMethod.modifyRepaymentPlan(uniqueId,amount,amount,firstModifyDate,secondModifyDate);
    }

    @Then("^返回结果\"([^\"]*)\"\"([^\"]*)\"\"([^\"]*)\"\"([^\"]*)\"这个贷款合同下可查询出\"([^\"]*)\"条还款计划C$")
    public void 返回结果_这个贷款合同下可查询出_条还款计划C(String result1, String result2, String result3, String result4, String repaymentPlanCount) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        result1 = this.result1;
        result2 = this.result2;
        result3 = this.result3;
        result4 = this.result4;
        repaymentPlanCount = modifyRepaymentPlanCucumberMethod.queryRepaymentPlanCount(uniqueId);
        Assert.assertEquals("{\"code\":21018,\"message\":\"存在当日扣款成功或处理中的还款计划!\"}",result1);
        Assert.assertEquals("{\"code\":0,\"message\":\"成功!\"}",result2);
        Assert.assertEquals("{\"code\":21004,\"message\":\"无效的计划本金总额!\"}",result3);
        Assert.assertEquals("{\"code\":0,\"message\":\"成功!\"}",result4);
        Assert.assertEquals("3",repaymentPlanCount);


    }

}
