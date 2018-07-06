package com.zufangbao.earth.cucumber.aboutPrepayment;

import com.zufangbao.cucumber.method.CucumberBaseTest;
import com.zufangbao.cucumber.method.PrepaymentCucumberMethod;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.yunxin.entity.AssetSet;
import com.zufangbao.sun.yunxin.entity.DeductionStatus;
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

public class PartDeductedBeforeSuccessAndPrepaymentSteps_G extends CucumberBaseTest {

	PrepaymentCucumberMethod prepaymentCucumberMethod = new PrepaymentCucumberMethod();
	
	@Autowired
	private IRemittanceApplicationService remittanceApplicationService;
	
	@Autowired
	private RepaymentPlanService repaymentPlanService;
	
	String firstPlanDate = DateUtils.format(DateUtils.addMonths(new Date(), 1));
	String secondPlanDate = DateUtils.format(DateUtils.addMonths(new Date(), 2));
	String thirdPlanDate = DateUtils.format(DateUtils.addMonths(new Date(), 3));
	
	private String productCode = "CS0001";
	private String uniqueId = UUID.randomUUID().toString();
	private String totalAmount = "1500";
	private String amount = "500";
    String result1 = "";
	
	@Given("^有三期未到期的还款计划G$")
	public void 有三期未到期的还款计划g() throws Throwable {
	    // Write code here that turns the phrase above into concrete actions
		prepaymentCucumberMethod.importAssetPackage(totalAmount, productCode, uniqueId, "0", amount, firstPlanDate, secondPlanDate, thirdPlanDate);
	}

	@When("^对第一期未到期的还款计划做部分提前划拨且扣款成功G$")
	public void 对第一期未到期的还款计划做部分提前划拨且扣款成功g() throws Throwable {
	    // Write code here that turns the phrase above into concrete actions
		String repaymentNumber = prepaymentCucumberMethod.queryFirstRepaymentPlan(uniqueId);
		prepaymentCucumberMethod.deductRepaymentPlan(repaymentNumber, uniqueId, productCode, "200", "0");
		while(true){
			AssetSet assetSet = repaymentPlanService.getRepaymentPlanByRepaymentCode(repaymentNumber);
			ExecutingStatus executingStatus = assetSet.getExecutingStatus();
			DeductionStatus deductionStatus = assetSet.getDeductionStatus();
			if(ExecutingStatus.PROCESSING == executingStatus && DeductionStatus.SUCCESS == deductionStatus){
				break;
			}
			Thread.sleep(10000);
		}
	}

	@When("^然后对后面的还款计划做提前还款申请且申请日在首期之前G$")
	public void 然后对后面的还款计划做提前还款申请且申请日在首期之前g() throws Throwable {
	    // Write code here that turns the phrase above into concrete actions
		result1 = prepaymentCucumberMethod.applyPrepaymentPlan(uniqueId, "1000", "1000", DateUtils.format(new Date()));
	}

	@Then("^返回结果\"([^\"]*)\"G$")
	public void 返回结果_G(String result) {
	    // Write code here that turns the phrase above into concrete actions
		result = result1;
		boolean outcome = result.contains("提前还款日期错误");
		Assert.assertEquals(true, outcome);
	}

}
