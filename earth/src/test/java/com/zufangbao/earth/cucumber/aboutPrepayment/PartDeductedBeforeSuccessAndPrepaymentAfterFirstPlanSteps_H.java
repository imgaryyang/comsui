package com.zufangbao.earth.cucumber.aboutPrepayment;

import com.zufangbao.cucumber.method.CucumberBaseTest;
import com.zufangbao.cucumber.method.PrepaymentCucumberMethod;
import com.zufangbao.sun.service.PrepaymentApplicationService;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.yunxin.entity.AssetSet;
import com.zufangbao.sun.yunxin.entity.AssetSetActiveStatus;
import com.zufangbao.sun.yunxin.entity.DeductionStatus;
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


public class PartDeductedBeforeSuccessAndPrepaymentAfterFirstPlanSteps_H extends CucumberBaseTest {
	
	
    PrepaymentCucumberMethod prepaymentCucumberMethod = new PrepaymentCucumberMethod();
	
	@Autowired
	private IRemittanceApplicationService remittanceApplicationService;
	
	@Autowired
	private RepaymentPlanService repaymentPlanService;
	
	@Autowired
	private PrepaymentApplicationService prepaymentApplicationService;
	
	String productCode = "CS0001";
	private String uniqueId = UUID.randomUUID().toString();
    String totalAmount = "1500";
    String amount = "500";
    
    String firstPlanDate = DateUtils.format(DateUtils.addMonths(new Date(), 1));
	String secondPlanDate = DateUtils.format(DateUtils.addMonths(new Date(), 2));
	String thirdPlanDate = DateUtils.format(DateUtils.addMonths(new Date(), 3));
	String applyDate = DateUtils.format(DateUtils.addDays(DateUtils.parseDate(firstPlanDate), 2));
    
    
    @Given("^有三期未到期的还款计划H$")
    public void 有三期未到期的还款计划h() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
    	prepaymentCucumberMethod.importAssetPackage(totalAmount, productCode, uniqueId, "0", amount, firstPlanDate, secondPlanDate, thirdPlanDate);
    }

    @When("^对第一期未到期的还款计划做部分提前划拨且扣款成功H$")
    public void 对第一期未到期的还款计划做部分提前划拨且扣款成功h() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
    	String repaymentNumber = prepaymentCucumberMethod.queryFirstRepaymentPlan(uniqueId);
    	prepaymentCucumberMethod.deductRepaymentPlan(repaymentNumber, uniqueId, productCode, "200", "0");
    	while(true){
			AssetSet assetSet = repaymentPlanService.getRepaymentPlanByRepaymentCode(repaymentNumber);
			ExecutingStatus executingStatus = assetSet.getExecutingStatus();
			DeductionStatus deductionStatus = assetSet.getDeductionStatus();
			if(ExecutingStatus.PROCESSING == executingStatus && DeductionStatus.SUCCESS == deductionStatus){
				break;
			}
			Thread.sleep(10000);
		}
    }

    @When("^然后对后面的还款计划做提前还款申请且申请日在首期之后H$")
    public void 然后对后面的还款计划做提前还款申请且申请日在首期之后h() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
    	prepaymentCucumberMethod.applyPrepaymentPlan(uniqueId, "1000", "1000", applyDate);
    }

    @Then("^查询出这条贷款合同下有\"([^\"]*)\"条还款计划且提前还款计划状态为\"([^\"]*)\"H$")
    public void 查询出这条贷款合同下有_条还款计划且提前还款计划状态为_H(String repaymentPlanCount, String arg) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
    	repaymentPlanCount = prepaymentCucumberMethod.queryRepaymentPlanCount(uniqueId);
    	String prepaymentNumber = prepaymentApplicationService.getPrepaymentNumber(uniqueId);
    	AssetSet assetSet = repaymentPlanService.getRepaymentPlanByRepaymentCode(prepaymentNumber);
    	AssetSetActiveStatus activeStatus = assetSet.getActiveStatus();
    	Assert.assertEquals("2", repaymentPlanCount);
    	Assert.assertEquals(AssetSetActiveStatus.FROZEN, activeStatus);
    	
    }

}
