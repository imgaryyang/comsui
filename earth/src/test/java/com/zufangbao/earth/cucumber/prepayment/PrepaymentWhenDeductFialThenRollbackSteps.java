package com.zufangbao.earth.cucumber.prepayment;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.junit.Assert;

import com.zufangbao.earth.api.test.post.BaseApiTestPost;
import com.zufangbao.earth.api.test.post.PostTestUtil;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class PrepaymentWhenDeductFialThenRollbackSteps {
	
	private String result;
	
	@Given("^有一期已到期的提前还款$")
	public void 有一期已到期的提前还款() throws Throwable {
	    // Write code here that turns the phrase above into concrete actions
		
		//TODO 造数据
	}

	@When("^对这一期提前还款进行扣款且扣款失败$")
	public void 对这一期提前还款进行扣款且扣款失败() throws Throwable {
	    // Write code here that turns the phrase above into concrete actions
		
		Map<String, String> requestParams = new HashMap<String, String>();
		
		requestParams.put("fn", "300001");
		requestParams.put("requestNo", UUID.randomUUID().toString());
		requestParams.put("deductId",  UUID.randomUUID().toString());
		requestParams.put("financialProductCode", "G31700");
		requestParams.put("uniqueId", "TEST201");
		requestParams.put("apiCalledTime", "2017-02-15");
		requestParams.put("amount",  "1000");
		requestParams.put("repaymentType", "1");
		requestParams.put("mobile", "13777847783");
		requestParams.put("repaymentDetails", "[{'loanFee':0.00,'otherFee':0.00,'repaymentAmount':1000,'repaymentInterest':0.00,'repaymentPlanNo':'ZC937591295234674688','repaymentPrincipal':1000,'techFee':0.00,'overDueFeeDetail':{"
				+ "'penaltyFee':0.00,'latePenalty':0.00,'lateFee':0.00,'lateOtherCost':0.00,'totalOverdueFee':0.00}}]");
	//	+ "'totalOverdueFee':0.00}}]");
		try {
			result= PostTestUtil.sendPost("http://192.168.1.109:9091/api/command", requestParams, new BaseApiTestPost().getIdentityInfoMap(requestParams));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();	
		}
		
	}

	@Then("^提前还款计划的还款状态\"([^\"]*)\"为作废$")
	public void 提前还款计划的还款状态_为作废(String activeStatus) throws Throwable {
	    // Write code here that turns the phrase above into concrete actions
//		数据库中查看activeStatus = 1
		Assert.assertEquals("1",activeStatus);
		Assert.assertEquals("{\"code\":0,\"message\":\"成功!\"}",result);
		
	}

}
