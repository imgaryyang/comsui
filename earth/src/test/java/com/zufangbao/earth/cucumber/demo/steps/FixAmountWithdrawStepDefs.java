/**
 * 
 */
package com.zufangbao.earth.cucumber.demo.steps;

import static org.awaitility.Awaitility.await;

import java.util.concurrent.Callable;

import org.junit.Assert;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

/**
 * @author wukai
 *
 */
public class FixAmountWithdrawStepDefs {
	
	private Callable<Boolean> moneyIsArrived() {
	      return new Callable<Boolean>() {
	            public Boolean call() throws Exception {
	                  return account.getRemaingBalance() != null; // The condition that must be fulfilled
	            }
	      };
	}
	private Account account = null;
	
	@Given("^我的账户中有余额\"([^\"]*)\"元$")
	public void 我的账户中有余额_元(String arg1) throws Throwable {
	    // Write code here that turns the phrase above into concrete actions
		this.account = new Account(arg1);
		
	}

	@When("^我选择固定金额取款方式取出\"([^\"]*)\"元$")
	public void 我选择固定金额取款方式取出_元(String arg1) throws Throwable {
	    // Write code here that turns the phrase above into concrete actions
		account.withDraw(arg1);
	}

	@Then("^我应该收到现金\"([^\"]*)\"元$")
	public void 我应该收到现金_元(String arg1) throws Throwable {
	    // Write code here that turns the phrase above into concrete actions
		await().until(moneyIsArrived());
	    Assert.assertEquals(account.getReceivedAmount().toString(),arg1);
	}

	@Then("^我的账号余额是\"([^\"]*)\"元$")
	public void 我的账号余额是_元(String arg1) throws Throwable {
		
		 Assert.assertEquals(account.getRemaingBalance().toString(),arg1);
	}

}
