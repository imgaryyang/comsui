package com.zufangbao.earth.cucumber.aboutPrepayment;

import com.zufangbao.cucumber.method.CucumberBaseTest;
import com.zufangbao.cucumber.method.PrepaymentCucumberMethod;
import com.zufangbao.sun.service.PrepaymentApplicationService;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.yunxin.entity.AssetSet;
import com.zufangbao.sun.yunxin.entity.AssetSetActiveStatus;
import com.zufangbao.sun.yunxin.service.RepaymentPlanService;
import com.zufangbao.sun.yunxin.service.remittance.IRemittanceApplicationService;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.UUID;

public class PrepaymentDeductedFailedAndRollBackSteps_O extends CucumberBaseTest {
	
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
	String repaymentPlanCountBeforeDeduct = "";
	String prepaymentPlanNo = "";
	String firstPlanDate = DateUtils.format(DateUtils.addMonths(new Date(), 1));
	String secondPlanDate = DateUtils.format(DateUtils.addMonths(new Date(),2));
	String thirdPlanDate = DateUtils.format(DateUtils.addMonths(new Date(), 3));
	String applyDate = DateUtils.format(new Date());
	
	@Given("^有三期未到期的还款计划O$")
	public void 有三期未到期的还款计划o() throws Throwable {
	    // Write code here that turns the phrase above into concrete actions
		prepaymentCucumberMethod.importAssetPackage(totalAmount, productCode, uniqueId, "7", amount, firstPlanDate, secondPlanDate, thirdPlanDate);
	}

	@When("^对此贷款合同下所有的还款计划进行提前还款申请O$")
	public void 对此贷款合同下所有的还款计划进行提前还款申请o() throws Throwable {
	    // Write code here that turns the phrase above into concrete actions
		String result = prepaymentCucumberMethod.applyPrepaymentPlan(uniqueId, totalAmount, totalAmount, applyDate);
		repaymentPlanCountBeforeDeduct = prepaymentCucumberMethod.queryRepaymentPlanCount(uniqueId);
	}

	@When("^对此提前还款计划进行扣款且扣款失败O$")
	public void 对此提前还款计划进行扣款且扣款失败o() throws Throwable {
	    // Write code here that turns the phrase above into concrete actions
		prepaymentPlanNo = prepaymentApplicationService.getPrepaymentNumber(uniqueId);
		prepaymentCucumberMethod.deductRepaymentPlan(prepaymentPlanNo, uniqueId, productCode, totalAmount, "1");
	}

	@Then("^查询此贷款合同下在提前还款计划扣款前有\"([^\"]*)\"条还款计划和扣款失败后有\"([^\"]*)\"条还款计划O$")
	public void 查询此贷款合同下在提前还款计划扣款前有_条还款计划和扣款失败后有_条还款计划O(String repaymentPlanCountBeforeDeduct, String repaymentPlanCountAfterDeduct) throws Throwable {
	    // Write code here that turns the phrase above into concrete actions
		repaymentPlanCountBeforeDeduct = this.repaymentPlanCountBeforeDeduct;
		while(true){
			AssetSet asset = repaymentPlanService.getRepaymentPlanByRepaymentCode(prepaymentPlanNo);
			AssetSetActiveStatus activeStatus = asset.getActiveStatus();
			if(AssetSetActiveStatus.INVALID == activeStatus){
				break;
			}
			Thread.sleep(10000);
		}
		repaymentPlanCountAfterDeduct = prepaymentCucumberMethod.queryRepaymentPlanCount(uniqueId);
		Assert.assertEquals("1", repaymentPlanCountBeforeDeduct);
		Assert.assertEquals("3", repaymentPlanCountAfterDeduct);
	}


}
