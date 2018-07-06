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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.UUID;

public class AllDeductingBeforeAndPrepaymentSteps_E extends CucumberBaseTest {
	@Autowired
	private IRemittanceApplicationService remittanceApplicationService;
	
	@Autowired
	private RepaymentPlanService repaymentPlanService;
	
	@Autowired
	private PrepaymentApplicationService prepaymentApplicationService;
	
    private String productCode = "CS0001";
	
    private String uniqueId = UUID.randomUUID().toString();
	
	private String totalAmount = "1500";
	
	private String amount = "500";
	
	String result = "";
	
	String firstPlanDate = DateUtils.format(DateUtils.addMonths(new Date(), 1));
	String secondPlanDate = DateUtils.format(DateUtils.addMonths(new Date(), 2));
	String thirdPlanDate = DateUtils.format(DateUtils.addMonths(new Date(), 3));
	String applyDate = DateUtils.format(DateUtils.addDays(DateUtils.parseDate(firstPlanDate), 2));
	private static Log logger = LogFactory.getLog(AllDeductingBeforeAndPrepaymentSteps_E.class);


	PrepaymentCucumberMethod prepaymentCucumberMethod = new PrepaymentCucumberMethod();
	@Given("^有三期未到期的还款计划E$")
	public void 有三期未到期的还款计划e() throws Throwable {
	    prepaymentCucumberMethod.importAssetPackage(totalAmount, productCode, uniqueId, "0", amount, firstPlanDate,  secondPlanDate,  thirdPlanDate);
	}

	@When("^对第一期未到期的还款计划做全额提前划拨E$")
	public void 对第一期未到期的还款计划做全额提前划拨e() throws Throwable {
		String repaymentNumber = prepaymentCucumberMethod.queryFirstRepaymentPlan(uniqueId);
		prepaymentCucumberMethod.deductRepaymentPlan(repaymentNumber, uniqueId, productCode, amount, "0");

	}

	@When("^当第一期还在扣款中的时候对后面的还款计划做提前还款且申请日期在第一期之后E$")
	public void 当第一期还在扣款中的时候对后面的还款计划做提前还款且申请日期在第一期之后e() throws Throwable {
	     result = prepaymentCucumberMethod.applyPrepaymentPlan(uniqueId, "1000", "1000", applyDate);
	}

	@Then("^这个贷款合同下可查询出\"([^\"]*)\"条还款计划E$")
	public void 这个贷款合同下可查询出_条还款计划E(String repaymentPlanCount) throws Throwable {
		boolean outcome = result.contains("成功");
		repaymentPlanCount = prepaymentCucumberMethod.queryRepaymentPlanCount(uniqueId);
		String prepaymentNumber = prepaymentApplicationService.getPrepaymentNumber(uniqueId);
		System.out.println(prepaymentNumber);
		AssetSet assetSet = repaymentPlanService.getRepaymentPlanByRepaymentCode(prepaymentNumber);
		AssetSetActiveStatus activeStatus = assetSet.getActiveStatus();
		Assert.assertEquals(AssetSetActiveStatus.FROZEN, activeStatus);
        Assert.assertEquals("2",  repaymentPlanCount);
        Assert.assertEquals(true, outcome);
	}


}
