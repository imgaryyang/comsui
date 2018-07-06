package com.zufangbao.earth.cucumber.aboutPrepayment;


import com.demo2do.core.utils.DateUtils;
import com.zufangbao.cucumber.method.CucumberBaseTest;
import com.zufangbao.cucumber.method.PrepaymentCucumberMethod;
import com.zufangbao.sun.service.PrepaymentApplicationService;
import com.zufangbao.sun.yunxin.entity.AssetSet;
import com.zufangbao.sun.yunxin.entity.AssetSetActiveStatus;
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
 * 接口扣款，扣款失败
 *
 */
public class PrepaymentAndDeductInteractionProblemsDateStepDefs2_IF extends CucumberBaseTest {
	private static final Log logger = LogFactory.getLog(PrepaymentAndDeductInteractionProblemsDateStepDefs2_IF.class);
	@Autowired
	private RepaymentPlanService repaymentPlanService;
	@Autowired
	private PrepaymentApplicationService	prepaymentApplicationService;
	
	@Autowired
	private IRemittanceApplicationService remittanceApplicationService;
	
	PrepaymentCucumberMethod prepaymentCucumberMethod =  new PrepaymentCucumberMethod();
	private String uniqueId = UUID.randomUUID().toString();
	String totalAmount = "1500";
	String productCode = "CS0001";
	String amount = "500";
	
	@Given("^申请完立即扣款_IF$")
	public void 申请完立即扣款_IF() throws Throwable {
		Date firstPeriodDate = DateUtils.addDays(new Date(), 1);
		Date secondePeriodDate = DateUtils.addMonths(firstPeriodDate, 1);
		Date thirdPeriodDate = DateUtils.addMonths(firstPeriodDate, 2);
		prepaymentCucumberMethod.importAssetPackage(totalAmount, productCode, uniqueId, "7", amount,DateUtils.format(firstPeriodDate),DateUtils.format(secondePeriodDate),DateUtils.format(thirdPeriodDate));
		
		
		logger.info("申请提前还款");
		prepaymentCucumberMethod.applyPrepaymentPlan(uniqueId,totalAmount,totalAmount,DateUtils.format(new Date()));
		
		logger.info("对还款计划D调用接口扣款");
		
		String repaymentPlanUniqueId =  prepaymentCucumberMethod.queryFirstRepaymentPlan(uniqueId);
		prepaymentCucumberMethod.deductRepaymentPlan(repaymentPlanUniqueId, uniqueId, productCode, totalAmount,"1");
	}

	@When("^扣款失败_IF$")
	public void 扣款失败_IF() throws Throwable {
		while(true){
			Thread.sleep(20000);
			String prepaymentNumber = prepaymentApplicationService.getPrepaymentNumber(uniqueId);
			AssetSet assetSet = repaymentPlanService.getRepaymentPlanByRepaymentCode(prepaymentNumber);
			if(assetSet.getActiveStatus()==AssetSetActiveStatus.INVALID){
				break;
			}
		}
	}
    
	@When("^D还款计划回滚_IF$")
	public void d还款计划回滚_IF() throws Throwable {
		
	}

	@Then("^查询到ABC还款计划_IF$")
	public void 查询到ABC还款计划_IF() throws Throwable {
		String prepaymentNum = prepaymentCucumberMethod.queryRepaymentPlanCount(uniqueId);
		Assert.assertEquals(prepaymentNum,"3");
	}
	
}
