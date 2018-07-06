package com.zufangbao.earth.cucumber.aboutPrepayment;


import com.demo2do.core.utils.DateUtils;
import com.zufangbao.cucumber.method.CucumberBaseTest;
import com.zufangbao.cucumber.method.PrepaymentCucumberMethod;
import com.zufangbao.sun.yunxin.entity.AssetSet;
import com.zufangbao.sun.yunxin.entity.ExecutingStatus;
import com.zufangbao.sun.yunxin.service.RepaymentPlanService;
import com.zufangbao.sun.yunxin.service.remittance.IRemittanceApplicationService;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.UUID;

/**
 * 
 * 接口扣款，不调用扣款接口
 *
 */

public class PrepaymentAndDeductInteractionProblemsDateStepDefs1_IN extends CucumberBaseTest {
	private static final Log logger = LogFactory.getLog(PrepaymentAndDeductInteractionProblemsDateStepDefs1_IN.class);
	@Autowired
	private RepaymentPlanService repaymentPlanService;
	
	@Autowired
	private IRemittanceApplicationService remittanceApplicationService;
	
	PrepaymentCucumberMethod prepaymentCucumberMethod =  new PrepaymentCucumberMethod();
	String uniqueId = UUID.randomUUID().toString();
	String totalAmount = "1500";
	String productCode = "CS0001";
	String amount = "500";
	
	@Given("^申请提前还款_IN$")
	public void 申请提前还款_IN() throws Throwable {
		Date firstPeriodDate = DateUtils.addDays(new Date(), 1);
		Date secondePeriodDate = DateUtils.addMonths(firstPeriodDate, 1);
		Date thirdPeriodDate = DateUtils.addMonths(firstPeriodDate, 2);
		prepaymentCucumberMethod.importAssetPackage(totalAmount, productCode, uniqueId, "0", amount,DateUtils.format(firstPeriodDate),DateUtils.format(secondePeriodDate),DateUtils.format(thirdPeriodDate));
		prepaymentCucumberMethod.applyPrepaymentPlan(uniqueId,totalAmount,totalAmount,DateUtils.format(new Date()));
	}

	@When("^未调用扣款_IN$")
	public void 未调用扣款_IN() throws Throwable {
		
	}

	@When("^D还款计划不回滚_IN$")
	public void d还款计划不回滚_IN() throws Throwable {
		String repaymentPlanUniqueId =  prepaymentCucumberMethod.queryFirstRepaymentPlan(uniqueId);
		AssetSet assetSet = repaymentPlanService.getRepaymentPlanByRepaymentPlanNo(repaymentPlanUniqueId);
		Assert.assertNotNull(assetSet);
	}

	@Then("^查询到D还款计划未完成_IN$")
	public void 查询到d还款计划未完成_1() throws Throwable {
		String repaymentPlanUniqueId =  prepaymentCucumberMethod.queryFirstRepaymentPlan(uniqueId);
		AssetSet assetSet = repaymentPlanService.getRepaymentPlanByRepaymentPlanNo(repaymentPlanUniqueId);
		Assert.assertEquals(1,assetSet.getPlanType().ordinal());
		Assert.assertEquals(ExecutingStatus.PROCESSING, assetSet.getExecutingStatus());
	}
	
}
