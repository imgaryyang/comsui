package com.zufangbao.earth.cucumber.aboutPrepayment;

import com.zufangbao.cucumber.method.CucumberBaseTest;
import com.zufangbao.cucumber.method.PrepaymentCucumberMethod;
import com.zufangbao.sun.service.PrepaymentApplicationService;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.yunxin.entity.AssetSet;
import com.zufangbao.sun.yunxin.entity.AssetSetActiveStatus;
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

public class DeductingFirstPlanAndPrepaymentSteps_I extends CucumberBaseTest {
	
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
	
	String result = "";
	String firstPlanDate = DateUtils.format(new Date());
	String secondPlanDate = DateUtils.format(DateUtils.addMonths(new Date(), 1));
	String thirdPlanDate = DateUtils.format(DateUtils.addMonths(new Date(), 2));
	String applyDate = DateUtils.format(new Date());
	String prepaymentPlanNumber = "";
	AssetSet assetSet = null;
	AssetSetActiveStatus activeBeforeSuccessStatus = null;
	String firstPlanNumber = "";

	
	@Given("^有三期还款计划且首期已到期I$")
	public void 有三期还款计划且首期已到期i() throws Throwable {
	    // Write code here that turns the phrase above into concrete actions
		prepaymentCucumberMethod.importAssetPackage(totalAmount, productCode, uniqueId, "0", amount, firstPlanDate,  secondPlanDate,  thirdPlanDate);
	}

	@When("^对第一期还款计划进行扣款I$")
	public void 对第一期还款计划进行扣款i() throws Throwable {
	    // Write code here that turns the phrase above into concrete actions
		firstPlanNumber = prepaymentCucumberMethod.queryFirstRepaymentPlan(uniqueId);
		prepaymentCucumberMethod.deductRepaymentPlan(firstPlanNumber, uniqueId, productCode, amount, "1");
	}

	@When("^然后对所有的还款计划做提前还款申请I$")
	public void 然后对所有的还款计划做提前还款申请i() throws Throwable {
	    // Write code here that turns the phrase above into concrete actions
		result = prepaymentCucumberMethod.applyPrepaymentPlan(uniqueId, totalAmount, totalAmount, applyDate);
	}

	@When("^对后面未到期的还款计划做提前还款申请I$")
	public void 对后面未到期的还款计划做提前还款申请i() throws Throwable {
	    // Write code here that turns the phrase above into concrete actions
		String result2 = prepaymentCucumberMethod.applyPrepaymentPlan(uniqueId, "1000", "1000", applyDate);
		if(!result2.contains("成功")){
			throw new Exception("提前还款失败");
		}
		prepaymentPlanNumber = prepaymentApplicationService.getPrepaymentNumber(uniqueId);
		assetSet = repaymentPlanService.getRepaymentPlanByRepaymentCode(prepaymentPlanNumber);
		activeBeforeSuccessStatus = assetSet.getActiveStatus();
		while(true){
			AssetSet asset = repaymentPlanService.getRepaymentPlanByRepaymentCode(firstPlanNumber);
			PaymentStatus paymentStatus = asset.getPaymentStatus();
			if(PaymentStatus.SUCCESS == paymentStatus){
				break;
			}
			Thread.sleep(10000);
		}
		Thread.sleep(180000);
		
	}

	@Then("^返回结果\"([^\"]*)\"并且查询出这条贷款合同下有\"([^\"]*)\"条还款计划和首期还款成功后提前还款计划状态为\"([^\"]*)\"I$")
	public void 返回结果_并且查询出这条贷款合同下有_条还款计划和首期还款成功后提前还款计划状态为_I(String result, String repaymentPlanCount, String arg) throws Throwable {
	    // Write code here that turns the phrase above into concrete actions
		AssetSet asset = repaymentPlanService.getRepaymentPlanByRepaymentCode(prepaymentPlanNumber);
		AssetSetActiveStatus activeStatus = asset.getActiveStatus();
		repaymentPlanCount = prepaymentCucumberMethod.queryRepaymentPlanCount(uniqueId);
		boolean outcome = result.contains("提前还款总金额或本金错误");
		result = this.result;
		Assert.assertEquals(true, outcome);
		Assert.assertEquals("2", repaymentPlanCount);
		Assert.assertEquals(AssetSetActiveStatus.FROZEN, activeBeforeSuccessStatus);
		Assert.assertEquals(AssetSetActiveStatus.OPEN, activeStatus);
		
	}

}
