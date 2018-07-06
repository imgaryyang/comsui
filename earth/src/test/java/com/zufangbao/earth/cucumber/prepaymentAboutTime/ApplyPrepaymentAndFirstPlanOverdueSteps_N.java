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

public class ApplyPrepaymentAndFirstPlanOverdueSteps_N extends CucumberBaseTest {
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
	String firstPlanDate = DateUtils.format(DateUtils.addMonths(new Date(), -1));
	String secondPlanDate = DateUtils.format(DateUtils.addMonths(new Date(), 1));
	String thirdPlanDate = DateUtils.format(DateUtils.addMonths(new Date(), 2));
	String applyDate = DateUtils.format(new Date());
	String result = "";
	@Given("^有三期还款计划且首期已逾期N$")
	public void 有三期还款计划且首期已逾期n() throws Throwable {
	    // Write code here that turns the phrase above into concrete actions
		prepaymentCucumberMethod.importAssetPackage(totalAmount, productCode, uniqueId, "0", amount, firstPlanDate, secondPlanDate, thirdPlanDate);
	}

	@When("^对此贷款合同下所有的还款计划进行提前还款申请N$")
	public void 对此贷款合同下所有的还款计划进行提前还款申请n() throws Throwable {
	    // Write code here that turns the phrase above into concrete actions
		result = prepaymentCucumberMethod.applyPrepaymentPlan(uniqueId, totalAmount, totalAmount, applyDate);
	}

	@When("^对此贷款合同下所有未到期的还款计划进行提前还款申请N$")
	public void 对此贷款合同下所有未到期的还款计划进行提前还款申请n() throws Throwable {
	    // Write code here that turns the phrase above into concrete actions
		String result = prepaymentCucumberMethod.applyPrepaymentPlan(uniqueId, "1000", "1000", applyDate);
	}

	@Then("^返回结果\"([^\"]*)\"并且查询此提前还款计划的状态为\"([^\"]*)\"N$")
	public void 返回结果_并且查询此提前还款计划的状态为_N(String result, String arg) throws Throwable {
	    // Write code here that turns the phrase above into concrete actions
		result = this.result;
		boolean outcome = result.contains("提前还款本金应与未偿本金总额一致");
		String prepaymentNumber = prepaymentApplicationService.getPrepaymentNumber(uniqueId);
		AssetSet asset = repaymentPlanService.getRepaymentPlanByRepaymentCode(prepaymentNumber);
		AssetSetActiveStatus activeStatus = asset.getActiveStatus();
		Assert.assertEquals(true, outcome);
		Assert.assertEquals(AssetSetActiveStatus.FROZEN, activeStatus);
	}


}
