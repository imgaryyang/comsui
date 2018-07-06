package com.zufangbao.earth.cucumber.prepaymentAboutTime;

import com.zufangbao.cucumber.method.CucumberBaseTest;
import com.zufangbao.cucumber.method.PrepaymentCucumberMethod;
import com.zufangbao.sun.service.PrepaymentApplicationService;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.yunxin.entity.AssetSet;
import com.zufangbao.sun.yunxin.entity.AssetSetActiveStatus;
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

public class ApplyPrepaymentAndPartDeductedThenOverdueSteps_S extends CucumberBaseTest {

    PrepaymentCucumberMethod prepaymentCucumberMethod = new PrepaymentCucumberMethod();
    
    @Autowired
	private IRemittanceApplicationService remittanceApplicationService; 
    
    @Autowired
	private PrepaymentApplicationService prepaymentApplicationService;
    
    @Autowired
	private RepaymentPlanService repaymentPlanService;

	private String productCode = "CS0001";
    private String uniqueId = UUID.randomUUID().toString();
	private String totalAmount = "1500";
	private String amount = "500";
	String prepaymentPlanNo = "";
	String firstPlanDate = DateUtils.format(DateUtils.addMonths(new Date(), 1));
	String secondPlanDate = DateUtils.format(DateUtils.addMonths(new Date(), 2));
	String thirdPlanDate = DateUtils.format(DateUtils.addMonths(new Date(), 3));
	String applyDate = DateUtils.format(new Date());
	
	@Given("^有三期未到期的还款计划S$")
	public void 有三期未到期的还款计划s() throws Throwable {
	    // Write code here that turns the phrase above into concrete actions
	    prepaymentCucumberMethod.importAssetPackage(totalAmount, productCode, uniqueId, "0", amount, firstPlanDate, secondPlanDate, thirdPlanDate);
	}

	@When("^对此贷款合同下所有未到期的还款计划进行提前还款申请S$")
	public void 对此贷款合同下所有未到期的还款计划进行提前还款申请s() throws Throwable {
	    // Write code here that turns the phrase above into concrete actions
		String result = prepaymentCucumberMethod.applyPrepaymentPlan(uniqueId, totalAmount, totalAmount, applyDate);
	}

	@When("^对此提前还款计划进行部分扣S$")
	public void 对此提前还款计划进行部分扣s() throws Throwable {
	    // Write code here that turns the phrase above into concrete actions
		prepaymentPlanNo = prepaymentApplicationService.getPrepaymentNumber(uniqueId);
		prepaymentCucumberMethod.deductRepaymentPlan(prepaymentPlanNo, uniqueId, productCode, amount, "1");
	}

	@Then("^第二天查询此提前还款计划的状态为\"([^\"]*)\",执行状态为\"([^\"]*)\"S$")
	public void 第二天查询此提前还款计划的状态为_执行状态为_S(String arg1, String arg2) throws Throwable {
	    // Write code here that turns the phrase above into concrete actions
		Date firstDate = DateUtils.parseDate(applyDate);
		while(true){
			int res = DateUtils.compareTwoDatesOnDay(firstDate, new Date());
			AssetSet asset = repaymentPlanService.getRepaymentPlanByRepaymentCode(prepaymentPlanNo);
			ExecutingStatus executingStatus1 = asset.getExecutingStatus();
			if(res<0 && ExecutingStatus.PROCESSING == executingStatus1){
				break;
			}
			Thread.sleep(1800000);
		}
		AssetSet assetSet = repaymentPlanService.getRepaymentPlanByRepaymentCode(prepaymentPlanNo);
		AssetSetActiveStatus activeStatus = assetSet.getActiveStatus();
		ExecutingStatus executingStatus = assetSet.getExecutingStatus();
		Assert.assertEquals(AssetSetActiveStatus.OPEN, activeStatus);
		Assert.assertEquals(ExecutingStatus.PROCESSING, executingStatus);
	}
}
