package com.zufangbao.earth.cucumber.aboutPrepayment;

import com.zufangbao.cucumber.method.CucumberBaseTest;
import com.zufangbao.cucumber.method.ImportAndApplyPrepaymentMethod;
import com.zufangbao.cucumber.method.PrepaymentCucumberMethod;
import com.zufangbao.sun.service.PrepaymentApplicationService;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.yunxin.service.RepaymentPlanService;
import com.zufangbao.sun.yunxin.service.remittance.IRemittanceApplicationService;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.UUID;


public class NormalApplyPrepaymentProcessSteps_M extends CucumberBaseTest {
	
	@Autowired
	private IRemittanceApplicationService remittanceApplicationService; 
	
	@Autowired
	private PrepaymentApplicationService prepaymentApplicationService;
	
	@Autowired
	private RepaymentPlanService repaymentPlanService;
	
	PrepaymentCucumberMethod prepaymentCucumberMethod = new PrepaymentCucumberMethod();
	
	ImportAndApplyPrepaymentMethod importAndApplyPrepaymentMethod = new ImportAndApplyPrepaymentMethod();
	
	private String productCode = "CS0001";
	private String uniqueId = UUID.randomUUID().toString();
	private String totalAmount = "1500";
	private String amount = "500";
	String firstPlanDate = DateUtils.format(DateUtils.addMonths(new Date(), 1));
	String secondPlanDate = DateUtils.format(DateUtils.addMonths(new Date(), 2));
	String thirdPlanDate = DateUtils.format(DateUtils.addMonths(new Date(), 3));
	String applyDate1 = DateUtils.format(DateUtils.addDays(new Date(), -1));
	String applyDate2 = DateUtils.format(DateUtils.addDays(DateUtils.parseDate(firstPlanDate), 1));
	String applyDate = DateUtils.format(new Date());
	String repaymentInterest = "20";
	String loanServiceFee = "20";
	String techMaintenanceFee = "20";
	String otheFee = "20";
	String assetInterest = Integer.parseInt(repaymentInterest)*3+"";
	String serviceCharge = Integer.parseInt(loanServiceFee)*3+"";
	String maintenanceCharge = Integer.parseInt(techMaintenanceFee)*3+"";
	String otherCharge = Integer.parseInt(otheFee)*3+"";
	String assetInitialValue =Integer.parseInt(totalAmount)+Integer.parseInt(assetInterest)+Integer.parseInt(serviceCharge)+Integer.parseInt(maintenanceCharge)+Integer.parseInt(otherCharge)+"";
	
	String result1 = "";
	String result2 = "";
	String result3 = "";
	String result4 = "";
	String result5 = "";
	String result6 = "";
	@Given("^有三期未到期的还款计划M$")
	public void 有三期未到期的还款计划m() throws Throwable {
	    // Write code here that turns the phrase above into concrete actions
		importAndApplyPrepaymentMethod.importAssetPackage(totalAmount, productCode, uniqueId, "0", amount, repaymentInterest, loanServiceFee, techMaintenanceFee, otheFee, firstPlanDate, secondPlanDate, thirdPlanDate);
	}

	@When("^申请提前还款且申请日期在当日之前M$")
	public void 申请提前还款且申请日期在当日之前m() throws Throwable {
	    // Write code here that turns the phrase above into concrete actions
		result1 = importAndApplyPrepaymentMethod.applyPrepaymentPlan(uniqueId, assetInitialValue, totalAmount, applyDate1, assetInterest, serviceCharge, maintenanceCharge, otherCharge);
	}

	@When("^申请提前还款且申请日期在第一期之后M$")
	public void 申请提前还款且申请日期在第一期之后m() throws Throwable {
	    // Write code here that turns the phrase above into concrete actions
		result2 = importAndApplyPrepaymentMethod.applyPrepaymentPlan(uniqueId, assetInitialValue, totalAmount, applyDate2, assetInterest, serviceCharge, maintenanceCharge, otherCharge);
	}

	@When("^申请提前还款且提前还款金额小于未还本金M$")
	public void 申请提前还款且提前还款金额小于未还本金m() throws Throwable {
	    // Write code here that turns the phrase above into concrete actions
		String assetInitialValue = Integer.parseInt(totalAmount)-100+"";
		String assetPrincipal = Integer.parseInt(totalAmount)-100+"";
		result3 = prepaymentCucumberMethod.applyPrepaymentPlan(uniqueId, assetInitialValue, assetPrincipal, applyDate);
	}

	@When("^申请提前还款且提前还款金额大于未还本金\\*(\\d+)\\.(\\d+)M$")
	public void 申请提前还款且提前还款金额大于未还本金_M(String assetInitialValue, String assetPrincipal) throws Throwable {
	    // Write code here that turns the phrase above into concrete actions
		assetInitialValue = Integer.parseInt(totalAmount)*1.3+"";
		assetPrincipal = Integer.parseInt(totalAmount)*1.0+"";
		result4 = importAndApplyPrepaymentMethod.applyPrepaymentPlan(uniqueId, assetInitialValue, assetPrincipal, applyDate, "100", "100", "100", "150");
	}

	@When("^申请提前还款且提前还款金额与明细总额不相等M$")
	public void 申请提前还款且提前还款金额与明细总额不相等m() throws Throwable {
	    // Write code here that turns the phrase above into concrete actions
		result5 = importAndApplyPrepaymentMethod.applyPrepaymentPlan(uniqueId, totalAmount, totalAmount, applyDate, assetInterest, serviceCharge, maintenanceCharge, otherCharge);
	}

	@When("^申请提前还款且各项参数都正确M$")
	public void 申请提前还款且各项参数都正确m() throws Throwable {
	    // Write code here that turns the phrase above into concrete actions
		result6 = importAndApplyPrepaymentMethod.applyPrepaymentPlan(uniqueId, assetInitialValue, totalAmount, applyDate, assetInterest, serviceCharge, maintenanceCharge, otherCharge);
	}

	@Then("^返回结果\"([^\"]*)\"，\"([^\"]*)\"，\"([^\"]*)\"，\"([^\"]*)\"，\"([^\"]*)\"，\"([^\"]*)\"M$")
	public void 返回结果_M(String result1, String result2, String result3, String result4, String result5, String result6) throws Throwable {
	    // Write code here that turns the phrase above into concrete actions
		result1 = this.result1;
		result2 = this.result2;
		result3 = this.result3;
		result4 = this.result4;
		result5 = this.result5;
		result6 = this.result6;
		boolean outcome = result1.contains("日期不应早于当日");
		boolean outcome2 = result2.contains("提前还款日期错误");
		boolean outcome3 = result3.contains("提前还款本金应与未偿本金总额一致");
		boolean outcome4 = result4.contains("提前还款本金应与未偿本金总额一致");
		boolean outcome5 = result5.contains("提前还款明细金额之和与总金额不匹配");
		boolean outcome6 = result6.contains("成功");
		Assert.assertEquals(true, outcome);
		Assert.assertEquals(true, outcome2);
		Assert.assertEquals(true, outcome3);
		Assert.assertEquals(true, outcome4);
		Assert.assertEquals(true, outcome5);
		Assert.assertEquals(true, outcome6);
	}

}
