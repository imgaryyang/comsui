package com.zufangbao.earth.cucumber.prepaymentAboutTime;

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

public class ApplyPrepaymentAndOverdueSteps_R extends CucumberBaseTest {
	
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
	String firstPlanDate = DateUtils.format(DateUtils.addMonths(new Date(), 1));
	String secondPlanDate = DateUtils.format(DateUtils.addMonths(new Date(), 2));
	String thirdPlanDate = DateUtils.format(DateUtils.addMonths(new Date(), 3));
	String applyDate = DateUtils.format(new Date());
	@Given("^有三期未到期的还款计划R$")
	public void 有三期未到期的还款计划r() throws Throwable {
	    // Write code here that turns the phrase above into concrete actions
		prepaymentCucumberMethod.importAssetPackage(totalAmount, productCode, uniqueId, "0", amount, firstPlanDate, secondPlanDate, thirdPlanDate);
	}

	@When("^对此贷款合同下所有未到期的还款计划进行提前还款申请R$")
	public void 对此贷款合同下所有未到期的还款计划进行提前还款申请r() throws Throwable {
	    // Write code here that turns the phrase above into concrete actions
		String result = prepaymentCucumberMethod.applyPrepaymentPlan(uniqueId, totalAmount, totalAmount, applyDate);
	}

	@Then("^第二天查询此提前还款计划的状态为\"([^\"]*)\"R$")
	public void 第二天查询此提前还款计划的状态为_R(String arg) throws Throwable {
	    // Write code here that turns the phrase above into concrete actions
		Date firstDate = DateUtils.parseDate(applyDate);
		while(true){
		int res = DateUtils.compareTwoDatesOnDay(firstDate, new Date());
		if(res<0){
			break;
		}
		Thread.sleep(1800000);
		}
		String prepaymentPlanNo = prepaymentApplicationService.getPrepaymentNumber(uniqueId);
		AssetSet asset = repaymentPlanService.getRepaymentPlanByRepaymentCode(prepaymentPlanNo);
		AssetSetActiveStatus activeStatus = asset.getActiveStatus();
		Assert.assertEquals(AssetSetActiveStatus.INVALID, activeStatus);
		
	}


}
