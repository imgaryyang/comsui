/**
 * 
 */
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

/**
 * @author wukai
 *
 */
public class PrepaymentStepDefs {
	
	private String result = null;
	
	@Given("^存在一期未到期的还款计划$")
	public void 存在一期未到期的还款计划() throws Throwable {
	    // Write code here that turns the phrase above into concrete actions
//		TODO 造数据 导入资产包
		
	}

	@When("^根据贷款合同唯一编号\"([^\"]*)\"、申请的日期\"([^\"]*)\"、未还总金额\"([^\"]*)\"、未还本金\"([^\"]*)\"来申请一期提前还款$")
	public void 根据贷款合同唯一编号_申请的日期_未还总金额_未还本金_来申请一期提前还款(String uniqueId, String assetRecycleDate, String assetInitialValue, String assetPricipal) throws Throwable {
	    // Write code here that turns the phrase above into concrete actions
		
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

	@Then("^至少有\"([^\"]*)\"个还款计划合并成(\\d+)个提前还款计划，返回信息\"([^\"]*)\"$")
	public void 至少有m_个还款计划合并成_个提前还款计划(String repaymentCount, String prePaymentCount,String expectedResult) throws Throwable {
	    // Write code here that turns the phrase above into concrete actions
		Assert.assertEquals(expectedResult,result);
		Assert.assertEquals(1,prePaymentCount);
		//TODO 查询合并计划的情况是不是为1
		//TODO 查询ledger_book的状态
		
	}

}
