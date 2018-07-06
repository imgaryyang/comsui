package com.zufangbao.earth.cucumber.aboutPrepayment;


import com.demo2do.core.utils.DateUtils;
import com.zufangbao.cucumber.method.CucumberBaseTest;
import com.zufangbao.cucumber.method.PrepaymentCucumberMethod;
import com.zufangbao.sun.entity.order.Order;
import com.zufangbao.sun.service.ContractService;
import com.zufangbao.sun.service.OrderService;
import com.zufangbao.sun.service.PrepaymentApplicationService;
import com.zufangbao.sun.yunxin.entity.AssetSet;
import com.zufangbao.sun.yunxin.entity.AssetSetActiveStatus;
import com.zufangbao.sun.yunxin.entity.ExecutingSettlingStatus;
import com.zufangbao.sun.yunxin.entity.OrderSource;
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
import java.util.List;
import java.util.UUID;

/**
 * 
 * 系统扣款，扣款失败
 *
 */
public class PrepaymentAndDeductInteractionProblemsDateStepDefs4_SF extends CucumberBaseTest {
	private static final Log logger = LogFactory.getLog(PrepaymentAndDeductInteractionProblemsDateStepDefs4_SF.class);
	@Autowired
	private RepaymentPlanService repaymentPlanService;
	
	@Autowired 
	private PrepaymentApplicationService	prepaymentApplicationService;
	
	@Autowired 
	private OrderService	orderService;
	
	@Autowired 
	private ContractService contractService;
	
	@Autowired
	private IRemittanceApplicationService remittanceApplicationService;
	
	PrepaymentCucumberMethod prepaymentCucumberMethod =  new PrepaymentCucumberMethod();
	private String uniqueId = UUID.randomUUID().toString();
	String totalAmount = "1500";
	String productCode = "G32000";
	String amount = "500";
	String firstPlanNo = "";
	Order order = null;

	@Given("^申请完立即扣款_SF$")
	public void 申请完立即扣款_sf() throws Throwable {
		Date firstPeriodDate = new Date();
		Date secondePeriodDate = DateUtils.addMonths(firstPeriodDate, 1);
		Date thirdPeriodDate = DateUtils.addMonths(firstPeriodDate, 2);
		prepaymentCucumberMethod.importAssetPackage(totalAmount, productCode, uniqueId, "7", amount,DateUtils.format(firstPeriodDate),DateUtils.format(secondePeriodDate),DateUtils.format(thirdPeriodDate));
		
		firstPlanNo = prepaymentCucumberMethod.queryFirstRepaymentPlan(uniqueId);
		
		logger.info("申请提前还款");
		prepaymentCucumberMethod.applyPrepaymentPlan(uniqueId,totalAmount,totalAmount,DateUtils.format(new Date()));
		AssetSet asset = repaymentPlanService.getRepaymentPlanByRepaymentCode(firstPlanNo);
		List<Order> orders = orderService.getOrderListByAssetSetId(asset.getId());
		if(orders.size()>1){
			 order = orders.get(orders.size()-1);
		}else {
			 order = orders.get(0);
		}
		ExecutingSettlingStatus executingSettlingStatus = order.getExecutingSettlingStatus();
		Assert.assertEquals(ExecutingSettlingStatus.CLOSED,executingSettlingStatus);
		
		String prepaymentNumber = prepaymentApplicationService.getPrepaymentNumber(uniqueId);
		AssetSet assetSet = repaymentPlanService.getRepaymentPlanByRepaymentPlanNo(prepaymentNumber);
		List<Order> orderList = orderService.getOrderListByAssetSetId(assetSet.getId());
		Assert.assertEquals(OrderSource.MANUAL,orderList.get(0).getOrderSource());
	}

	@When("^扣款失败_SF$")
	public void 扣款失败_sf() throws Throwable {
		while(true){
			Thread.sleep(20000);
			String prepaymentNumber = prepaymentApplicationService.getPrepaymentNumber(uniqueId);
			AssetSet assetSet = repaymentPlanService.getRepaymentPlanByRepaymentCode(prepaymentNumber);
			if(assetSet.getActiveStatus()==AssetSetActiveStatus.INVALID){
				break;
			}
		}
		
	}


	@Then("^查询到ABC还款计划_SF$")
	public void 查询到abc还款计划_sf() throws Throwable {
		String prepaymentNumber = prepaymentApplicationService.getPrepaymentNumber(uniqueId);
		AssetSet assetSet = repaymentPlanService.getRepaymentPlanByRepaymentCode(prepaymentNumber);
		List<Order> orderList = orderService.getOrderListByAssetSetId(assetSet.getId());
		Assert.assertEquals(ExecutingSettlingStatus.FAIL,orderList.get(0).getExecutingSettlingStatus());
		
		
		AssetSet asset = repaymentPlanService.getRepaymentPlanByRepaymentCode(firstPlanNo);
		List<Order> orders = orderService.getOrderListByAssetSetId(asset.getId());
		Order order = orders.get(0);
		Assert.assertEquals(ExecutingSettlingStatus.CREATE,order.getExecutingSettlingStatus());
		
		
		String prepaymentNum = prepaymentCucumberMethod.queryRepaymentPlanCount(uniqueId);
		Assert.assertEquals(prepaymentNum,"3");
	}
	
}
