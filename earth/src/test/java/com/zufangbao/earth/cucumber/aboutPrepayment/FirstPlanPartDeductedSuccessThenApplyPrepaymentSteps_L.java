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
import com.zufangbao.sun.yunxin.service.remittance.IRemittanceApplicationService;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.UUID;

public class FirstPlanPartDeductedSuccessThenApplyPrepaymentSteps_L extends CucumberBaseTest {
	
	@Autowired
	private IRemittanceApplicationService remittanceApplicationService; 
	
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
	
	@Given("^有三期还款计划且首期已到期L$")
	public void 有三期还款计划且首期已到期l() throws Throwable {
	    // Write code here that turns the phrase above into concrete actions
		prepaymentCucumberMethod.importAssetPackage(totalAmount, productCode, uniqueId, "0", amount, firstPlanDate, secondPlanDate, thirdPlanDate);
	}

	@When("^对第一期还款计划进行部分扣款L$")
	public void 对第一期还款计划进行部分扣款l() throws Throwable {
	    // Write code here that turns the phrase above into concrete actions
		firstPlanNumber = prepaymentCucumberMethod.queryFirstRepaymentPlan(uniqueId);
		prepaymentCucumberMethod.deductRepaymentPlan(firstPlanNumber, uniqueId, productCode, "200", "1");
		while(true){
			AssetSet asset = repaymentPlanService.getRepaymentPlanByRepaymentCode(firstPlanNumber);
			ExecutingStatus executingStatus = asset.getExecutingStatus();
			DeductionStatus deductionStatus = asset.getDeductionStatus();
			if(ExecutingStatus.PROCESSING == executingStatus && DeductionStatus.SUCCESS == deductionStatus){
				break;
			}
			Thread.sleep(10000);
		}
	}

	@When("^然后对所有的还款计划做提前还款申请L$")
	public void 然后对所有的还款计划做提前还款申请l() throws Throwable {
	    // Write code here that turns the phrase above into concrete actions
		result = prepaymentCucumberMethod.applyPrepaymentPlan(uniqueId, "1300", "1300", applyDate);
	}

	@When("^对后面未到期的还款计划做提前还款申请L$")
	public void 对后面未到期的还款计划做提前还款申请l() throws Throwable {
	    // Write code here that turns the phrase above into concrete actions
		String result2 = prepaymentCucumberMethod.applyPrepaymentPlan(uniqueId, "1000", "1000", applyDate);
		if(result2.contains("成功")){
			throw new Exception("提前还款失败");
		}
	}

	@Then("^返回结果\"([^\"]*)\"并且查询提前还款计划状态为\"([^\"]*)\"L$")
	public void 返回结果_并且查询提前还款计划状态为_L(String result, String arg) throws Throwable {
	    // Write code here that turns the phrase above into concrete actions
		result = this.result;
		boolean outcome = result.contains("提前还款总金额或本金错误");
		prepaymentPlanNumber = prepaymentApplicationService.getPrepaymentNumber(uniqueId);
		AssetSet assetSet = repaymentPlanService.getRepaymentPlanByRepaymentCode(prepaymentPlanNumber);
		AssetSetActiveStatus activeStatus = assetSet.getActiveStatus();
		Assert.assertEquals(true, outcome);
		Assert.assertEquals(AssetSetActiveStatus.FROZEN, activeStatus);
	}


}
