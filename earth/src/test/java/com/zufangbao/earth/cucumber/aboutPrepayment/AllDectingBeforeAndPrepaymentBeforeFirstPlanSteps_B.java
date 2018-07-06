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
    public class AllDectingBeforeAndPrepaymentBeforeFirstPlanSteps_B extends CucumberBaseTest {
    @Autowired
    private IRemittanceApplicationService remittanceApplicationService;
    
    private String productCode = "CS0001";
	
    private String uniqueId = UUID.randomUUID().toString();
	
	private String totalAmount = "1500";
	
	private String amount = "500";

    String result = "";
    String firstPlanDate = DateUtils.format(DateUtils.addMonths(new Date(), 1));
	String secondPlanDate = DateUtils.format(DateUtils.addMonths(new Date(), 2));
	String thirdPlanDate = DateUtils.format(DateUtils.addMonths(new Date(), 3));
	String applyDate = DateUtils.format(DateUtils.addDays(DateUtils.parseDate(firstPlanDate), -1));

    PrepaymentCucumberMethod prepaymentCucumberMethod = new PrepaymentCucumberMethod();
    @Given("^有三期未到期的还款计划B$")
    public void 有三期未到期的还款计划b() throws Throwable {
       // Write code here that turns the phrase above into concrete actions
       prepaymentCucumberMethod.importAssetPackage(totalAmount, productCode, uniqueId, "0", amount, firstPlanDate,  secondPlanDate,  thirdPlanDate);
 }

    @When("^对第一期未到期的还款计划做全额提前划拨B$")
    public void 对第一期未到期的还款计划做全额提前划拨b() throws Throwable {
       // Write code here that turns the phrase above into concrete actions
	   String repaymentNumber = prepaymentCucumberMethod.queryFirstRepaymentPlan(uniqueId);
	   prepaymentCucumberMethod.deductRepaymentPlan(repaymentNumber, uniqueId, productCode, amount, "0");
 }

    @When("^当第一期还在扣款中的时候对后面的还款计划做提前还款且申请日期在第一期之前B$")
    public void 当第一期还在扣款中的时候对后面的还款计划做提前还款且申请日期在第一期之前b() throws Throwable {
       // Write code here that turns the phrase above into concrete actions
	   result = prepaymentCucumberMethod.applyPrepaymentPlan(uniqueId, "1000", "1000", applyDate);
}

	@Then("^返回结果\"([^\"]*)\"且这个贷款合同下可查询出\"([^\"]*)\"条还款计划B$")
	public void 返回结果_且这个贷款合同下可查询出_条还款计划B(String repaymentPlanCount, String result) throws Throwable {
	    // Write code here that turns the phrase above into concrete actions
                result = this.result;
				boolean outcome = result.contains("提前还款日期错误");
				repaymentPlanCount = prepaymentCucumberMethod.queryRepaymentPlanCount(uniqueId);
				Assert.assertEquals(true, outcome);
		        Assert.assertEquals("3",  repaymentPlanCount);
	}


}
