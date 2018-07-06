package com.zufangbao.earth.cucumber.prepayment;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.PendingException;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(
format = {"json:target/json-report/dw.json"}
, features = {"classpath:cucumber/feature/提前还款/提前还款扣款失败回滚.feature"}
, glue = {"com.zufangbao.earth.cucumber.prepayment"}
)
public class PrepaymentWhenDeductFialThenRollbackTest {
	
}
