package com.zufangbao.earth.cucumber.aboutPrepayment;

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


public class AllDeductingBeforeAndPrepaymentThenDeductedFailSteps_D extends CucumberBaseTest {
	
	@Autowired
	private IRemittanceApplicationService remittanceApplicationService;
	
	@Autowired
	PrepaymentApplicationService prepaymentApplicationService;
	
	@Autowired
	RepaymentPlanService repaymentPlanService;
	
	PrepaymentCucumberMethod prepaymentCucumberMethod = new PrepaymentCucumberMethod();

	private String productCode = "CS0001";
	
	private String uniqueId = UUID.randomUUID().toString();
	
	private String totalAmount = "1500";
	
	private String amount = "500";
	
	String repaymentNumber = "";
	
	AssetSetActiveStatus activeStatusBeforeSuccsee = null;
	
	AssetSet assetSet = null;
	
	String prepaymentNumber = "";
	
	String firstPlanDate = DateUtils.format(DateUtils.addMonths(new Date(), 1));
	String secondPlanDate = DateUtils.format(DateUtils.addMonths(new Date(), 2));
	String thirdPlanDate = DateUtils.format(DateUtils.addMonths(new Date(), 3));
	String applyDate = DateUtils.format(DateUtils.addDays(DateUtils.parseDate(firstPlanDate), 2));

	@Given("^有三期未到期的还款计划D$")
	public void 有三期未到期的还款计划d() throws Throwable {
	    // Write code here that turns the phrase above into concrete actions
		prepaymentCucumberMethod.importAssetPackage(totalAmount, productCode, uniqueId, "7", amount, firstPlanDate, secondPlanDate, thirdPlanDate);
	}

	@Given("^对第一期未到期的还款计划做全额提前划拨且正在扣款中D$")
	public void 对第一期未到期的还款计划做全额提前划拨且正在扣款中d() throws Throwable {
	    // Write code here that turns the phrase above into concrete actions
		repaymentNumber = prepaymentCucumberMethod.queryFirstRepaymentPlan(uniqueId);
		prepaymentCucumberMethod.deductRepaymentPlan(repaymentNumber, uniqueId, productCode, amount, "0");
	}

	@When("^对后面未到期的还款计划做提前还款申请D$")
	public void 对后面未到期的还款计划做提前还款申请d() throws Throwable {
	    // Write code here that turns the phrase above into concrete actions
		String result = prepaymentCucumberMethod.applyPrepaymentPlan(uniqueId, "1000", "1000", applyDate);
		if(!result.contains("成功")){
			throw new Exception("提前还款失败");
		}
	    prepaymentNumber = prepaymentApplicationService.getPrepaymentNumber(uniqueId);
		assetSet = repaymentPlanService.getRepaymentPlanByRepaymentCode(prepaymentNumber);
		activeStatusBeforeSuccsee = assetSet.getActiveStatus();
		while(true){
			AssetSet assetSet2 = repaymentPlanService.getRepaymentPlanByRepaymentCode(repaymentNumber);
			ExecutingStatus excutingStatus = assetSet2.getExecutingStatus();
			if(ExecutingStatus.UNEXECUTED == excutingStatus){
				break;
			}
			Thread.sleep(10000);
		}
	}

	@Then("^当首期提前划扣失败以后查询提前还款计划的状态为\"([^\"]*)\"D$")
	public void 当首期提前划扣失败以后查询提前还款计划的状态为_D(String arg) throws Throwable {
	    // Write code here that turns the phrase above into concrete actions
		AssetSet asset = repaymentPlanService.getRepaymentPlanByRepaymentCode(prepaymentNumber);
		AssetSetActiveStatus activeStatus = asset.getActiveStatus();
		Assert.assertEquals(AssetSetActiveStatus.FROZEN, activeStatusBeforeSuccsee);
		Assert.assertEquals(AssetSetActiveStatus.FROZEN, activeStatus);
	}

}
