package com.zufangbao.earth.cucumber.prepayment;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.junit.Assert;

import com.zufangbao.earth.api.test.post.BaseApiTestPost;
import com.zufangbao.earth.api.test.post.PostTestUtil;

import cucumber.api.PendingException;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class PrepaymentDateLessThanFirstDateStepDefs {
	
	private String result = null;
	@Given("^为三笔还款计划申请提前还款$")
	public void 为三笔还款计划申请提前还款() throws Throwable {
	    // Write code here that turns the phrase above into concrete actions
	    throw new PendingException();
	}

	@When("^根据贷款合同唯一编号\"([^\"]*)\"、申请的日期\"([^\"]*)\"、未还总金额\"([^\"]*)\"、未还本金\"([^\"]*)\"来申请一期提前还款$")
	public void 根据贷款合同唯一编号_申请的日期_未还总金额_未还本金_来申请一期提前还款(String uniqueId, String assetRecycleDate, String assetInitialValue, String assetPricipal) throws Throwable {
		Map<String, String> requestParams = new HashMap<String, String>();
		requestParams.put("fn", "200002");
		requestParams.put("uniqueId", uniqueId);
		requestParams.put("contractNo", "");
		requestParams.put("requestNo", UUID.randomUUID().toString());
		requestParams.put("assetRecycleDate", assetRecycleDate);
		requestParams.put("assetInitialValue",assetInitialValue);
		requestParams.put("assetPrincipal", assetPricipal);
		requestParams.put("type", "0");
		requestParams.put("payWay","0");
//		requestParams.put("hasDeducted", "-1");
		
		try {
			
			result  = PostTestUtil.sendPost("http://192.168.1.109:9090/api/modify", requestParams,  new BaseApiTestPost().getIdentityInfoMap(requestParams));//192.168.0.204
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Then("^至少有\"([^\"]*)\"个还款计划合并成一个提前还款计划，返回信息\"([^\"]*)\"$")
	public void 至少有_个还款计划合并成一个提前还款计划_返回信息(String prePaymentCount, String expectedResult) throws Throwable {
	    // Write code here that turns the phrase above into concrete actions
		Assert.assertEquals(expectedResult,result);
		Assert.assertEquals(3,prePaymentCount);
		//TODO 查询合并计划的情况是不是为1
		//TODO 查询ledger_book的状态
	}

	@When("^对合同编号为\"([^\"]*)\"的还款计划进行扣款$")
	public void 对合同编号为_的还款计划进行扣款(String uniqueId) throws Throwable {
		Map<String, String> requestParams = new HashMap<String, String>();
		
		requestParams.put("fn", "300001");
		requestParams.put("requestNo", UUID.randomUUID().toString());
		requestParams.put("deductId",  UUID.randomUUID().toString());
		requestParams.put("financialProductCode", "G31700");
		requestParams.put("uniqueId", uniqueId);
		requestParams.put("apiCalledTime", "2017-01-12");
		requestParams.put("amount",  "444");
		requestParams.put("repaymentType", "1");
		requestParams.put("mobile", "13777847783");
		requestParams.put("repaymentDetails", "[{'loanFee':0.00,'otherFee':0.00,'repaymentAmount':444,'repaymentInterest':0.00,'repaymentPlanNo':'ZC624151723578687488','repaymentPrincipal':440,'techFee':0.00,'overDueFeeDetail':{"
				+ "'penaltyFee':1.00,'latePenalty':1.00,'lateFee':1.00,'lateOtherCost':1.00,'totalOverdueFee':4.00}}]");
	//	+ "'totalOverdueFee':0.00}}]");
		try {
			String sr = PostTestUtil.sendPost("http://192.168.0.204:83/api/command", requestParams, new BaseApiTestPost().getIdentityInfoMap(requestParams));
			System.out.println(sr);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();	
		}
	}

	@Then("^返回信息 \"([^\"]*)\"$")
	public void 返回信息(String arg1) throws Throwable {
	    // Write code here that turns the phrase above into concrete actions
	    throw new PendingException();
	}
	

}
