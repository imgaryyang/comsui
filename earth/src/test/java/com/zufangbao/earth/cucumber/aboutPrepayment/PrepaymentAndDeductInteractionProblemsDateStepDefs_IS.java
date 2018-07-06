package com.zufangbao.earth.cucumber.aboutPrepayment;


import com.demo2do.core.utils.DateUtils;
import com.zufangbao.cucumber.method.CucumberBaseTest;
import com.zufangbao.cucumber.method.PrepaymentCucumberMethod;
import com.zufangbao.sun.service.PrepaymentApplicationService;
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
 * 接口扣款，扣款成功
 *
 */
public class PrepaymentAndDeductInteractionProblemsDateStepDefs_IS extends CucumberBaseTest {
	private static final Log logger = LogFactory.getLog(PrepaymentAndDeductInteractionProblemsDateStepDefs_IS.class);
	
	@Autowired
	private RepaymentPlanService	repaymentPlanService;
	
	@Autowired
	private PrepaymentApplicationService	prepaymentApplicationService;
	
	@Autowired
	private IRemittanceApplicationService remittanceApplicationService;
	
	PrepaymentCucumberMethod prepaymentCucumberMethod =  new PrepaymentCucumberMethod();
	//String uniqueId = UUID.randomUUID().toString();
	private String uniqueId = UUID.randomUUID().toString();
	String totalAmount = "1500";
	String productCode = "CS0001";
	String amount = "500";
	
	@Given("^申请完发起扣款_IS$")
	public void 申请完发起扣款_IS() throws Throwable {
		logger.info("导入资产包");
		Date firstPeriodDate = DateUtils.addDays(new Date(), 1);
		Date secondePeriodDate = DateUtils.addMonths(firstPeriodDate, 1);
		Date thirdPeriodDate = DateUtils.addMonths(firstPeriodDate, 2);
		prepaymentCucumberMethod.importAssetPackage(totalAmount, productCode, uniqueId, "0", amount,DateUtils.format(firstPeriodDate),DateUtils.format(secondePeriodDate),DateUtils.format(thirdPeriodDate));
		
		
		logger.info("申请提前还款");
		prepaymentCucumberMethod.applyPrepaymentPlan(uniqueId,totalAmount,totalAmount,DateUtils.format(new Date()));
		
		logger.info("对还款计划D调用接口扣款");
		
		String repaymentPlanUniqueId =  prepaymentCucumberMethod.queryFirstRepaymentPlan(uniqueId);
		prepaymentCucumberMethod.deductRepaymentPlan(repaymentPlanUniqueId, uniqueId, productCode, totalAmount,"1");
	}

	@When("^扣款成功_IS$")
	public void 扣款成功_IS() throws Throwable {
		while(true){
			Thread.sleep(20000);
			String prepaymentNumber = prepaymentApplicationService.getPrepaymentNumber(uniqueId);
			AssetSet assetSet = repaymentPlanService.getRepaymentPlanByRepaymentCode(prepaymentNumber);
			if(assetSet.getExecutingStatus()==ExecutingStatus.SUCCESSFUL){
				break;
			}
		}
		
	}


	@When("^D不回滚_IS$")
	public void d不回滚_IS() throws Throwable {
		String prepaymentNum = prepaymentCucumberMethod.queryRepaymentPlanCount(uniqueId);
		Assert.assertEquals(prepaymentNum,"1");
	}

	@Then("^查询到D还款计划完成_IS$")
	public void 查询到d还款计划完成_IS() throws Throwable {
		String repaymentPlanUniqueId =  prepaymentCucumberMethod.queryFirstRepaymentPlan(uniqueId);
		AssetSet assetSet = repaymentPlanService.getRepaymentPlanByRepaymentPlanNo(repaymentPlanUniqueId);
		Assert.assertEquals(1,assetSet.getPlanType().ordinal());
		Assert.assertEquals(ExecutingStatus.SUCCESSFUL, assetSet.getExecutingStatus());
	}
}
