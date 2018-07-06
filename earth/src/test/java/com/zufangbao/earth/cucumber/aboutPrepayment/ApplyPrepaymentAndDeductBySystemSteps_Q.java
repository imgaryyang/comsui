package com.zufangbao.earth.cucumber.aboutPrepayment;

import com.zufangbao.cucumber.method.CucumberBaseTest;
import com.zufangbao.cucumber.method.PrepaymentCucumberMethod;
import com.zufangbao.sun.entity.order.Order;
import com.zufangbao.sun.service.OrderService;
import com.zufangbao.sun.service.PrepaymentApplicationService;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.yunxin.entity.AssetSet;
import com.zufangbao.sun.yunxin.entity.OrderSource;
import com.zufangbao.sun.yunxin.service.RepaymentPlanService;
import com.zufangbao.sun.yunxin.service.remittance.IRemittanceApplicationService;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import java.util.UUID;


public class ApplyPrepaymentAndDeductBySystemSteps_Q extends CucumberBaseTest {
	PrepaymentCucumberMethod prepaymentCucumberMethod = new PrepaymentCucumberMethod();
	@Autowired
	IRemittanceApplicationService remittanceApplicationService;
	
	@Autowired
	PrepaymentApplicationService prepaymentApplicationService;
	
	@Autowired
	OrderService orderService;
	
	@Autowired
	RepaymentPlanService repaymentPlanService;
	private String uniqueId = UUID.randomUUID().toString();
	String productCode = "G32000";
	String totalAmount = "1500";
	String amount = "500";
	String firstPlanDate = DateUtils.format(DateUtils.addMonths(new Date(), 1));
	String secondPlanDate = DateUtils.format(DateUtils.addMonths(new Date(), 2));
	String thirdPlanDate = DateUtils.format(DateUtils.addMonths(new Date(), 3));
	String applyDate = DateUtils.format(new Date());
	
	@Given("^有三期未到期的还款计划Q$")
	public void 有三期未到期的还款计划q() throws Throwable {
	    // Write code here that turns the phrase above into concrete actions
		prepaymentCucumberMethod.importAssetPackage(totalAmount, productCode, uniqueId, "0", amount, firstPlanDate, secondPlanDate, thirdPlanDate);
	}

	@When("^对此贷款合同下所有的还款计划进行提前还款申请且申请日期为当日Q$")
	public void 对此贷款合同下所有的还款计划进行提前还款申请且申请日期为当日q() throws Throwable {
	    // Write code here that turns the phrase above into concrete actions
		String result = prepaymentCucumberMethod.applyPrepaymentPlan(uniqueId, totalAmount, totalAmount, applyDate);
		if(!result.contains("成功")){
			throw new Exception("提前还款失败");
		}
	}

	@Then("^查看此提前还款计划下结算单状态\"([^\"]*)\"Q$")
	public void 查看此提前还款计划下结算单状态_Q(String arg) throws Throwable {
	    // Write code here that turns the phrase above into concrete actions
		String prepaymentPlanNo = prepaymentApplicationService.getPrepaymentNumber(uniqueId);
		AssetSet assetSet = repaymentPlanService.getRepaymentPlanByRepaymentCode(prepaymentPlanNo);
		List<Order> orders = orderService.getOrderListByAssetSetUuid(assetSet.getAssetUuid());
		Order order = orders.get(0);
		OrderSource orderSource = order.getOrderSource();
		Assert.assertEquals(OrderSource.MANUAL, orderSource);
	}


}
