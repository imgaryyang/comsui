package com.zufangbao.earth.cucumber.aboutPrepayment;

import com.zufangbao.cucumber.method.CucumberBaseTest;
import com.zufangbao.cucumber.method.PrepaymentCucumberMethod;
import com.zufangbao.sun.service.PrepaymentApplicationService;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.yunxin.entity.AssetSet;
import com.zufangbao.sun.yunxin.entity.AssetSetActiveStatus;
import com.zufangbao.sun.yunxin.entity.DeductionStatus;
import com.zufangbao.sun.yunxin.entity.ExecutingStatus;
import com.zufangbao.sun.yunxin.service.RepaymentPlanService;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.UUID;

public class ApplyPrepaymentThenFirstDeductedFailedSteps_J extends CucumberBaseTest {

	@Autowired
	private PrepaymentApplicationService prepaymentApplicationService;
	
	@Autowired
	private RepaymentPlanService repaymentPlanService;
	
	PrepaymentCucumberMethod prepaymentCucumberMethod = new PrepaymentCucumberMethod();
	
	private String productCode = "CS0001";
	private String uniqueId = UUID.randomUUID().toString();
	private String totalAmount = "1500";
	private String amount = "500";
	String result = "";
	String firstPlanDate = DateUtils.format(new Date());
	String secondPlanDate = DateUtils.format(DateUtils.addMonths(new Date(), 1));
	String thirdPlanDate = DateUtils.format(DateUtils.addMonths(new Date(), 2));
	String applyDate = DateUtils.format(new Date());
	String firstPlanNumber = "";
	String prepaymentPlanNumber = "";
	@Given("^有三期还款计划且首期已到期J$")
	public void 有三期还款计划且首期已到期j() throws Throwable {
	    // Write code here that turns the phrase above into concrete actions
		prepaymentCucumberMethod.importAssetPackage(totalAmount, productCode, uniqueId, "7", amount, firstPlanDate,  secondPlanDate,  thirdPlanDate);
	}

	@When("^对第一期还款计划进行扣款J$")
	public void 对第一期还款计划进行扣款j() throws Throwable {
	    // Write code here that turns the phrase above into concrete actions
		firstPlanNumber = prepaymentCucumberMethod.queryFirstRepaymentPlan(uniqueId);
		prepaymentCucumberMethod.deductRepaymentPlan(firstPlanNumber, uniqueId, productCode, amount, "1");
	}

	@When("^对后面未到期的还款计划做提前还款申请J$")
	public void 对后面未到期的还款计划做提前还款申请j() throws Throwable {
	    // Write code here that turns the phrase above into concrete actions
		result = prepaymentCucumberMethod.applyPrepaymentPlan(uniqueId, "1000", "1000", applyDate);
	}

	@Then("^返回结果\"([^\"]*)\"并且查询出首期还款失败后提前还款计划状态为\"([^\"]*)\"J$")
	public void 返回结果_并且查询出首期还款失败后提前还款计划状态为_J(String result, String arg) throws Throwable {
	    // Write code here that turns the phrase above into concrete actions
		result = this.result;
		boolean outcome = result.contains("成功");
		while(true){
			AssetSet asset = repaymentPlanService.getRepaymentPlanByRepaymentCode(firstPlanNumber);
			ExecutingStatus executingStatus = asset.getExecutingStatus();
			DeductionStatus deductionStatus = asset.getDeductionStatus();
			if(ExecutingStatus.PROCESSING == executingStatus && DeductionStatus.FAIL == deductionStatus){
				break;
			}
			Thread.sleep(10000);
		}
		prepaymentPlanNumber = prepaymentApplicationService.getPrepaymentNumber(uniqueId);
		AssetSet assetSet = repaymentPlanService.getRepaymentPlanByRepaymentCode(prepaymentPlanNumber);
		AssetSetActiveStatus activeStatus = assetSet.getActiveStatus();
		Assert.assertEquals(true, outcome);
		Assert.assertEquals(AssetSetActiveStatus.FROZEN,activeStatus);
	}

}
