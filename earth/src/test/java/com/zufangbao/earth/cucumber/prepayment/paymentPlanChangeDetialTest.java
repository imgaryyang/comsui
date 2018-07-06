package com.zufangbao.earth.cucumber.prepayment;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(
format = {"json:target/json-report/dw.json"}
, features = {"classpath:cucumber/feature/提前还款-对多个时间点的还款计划判断/提前还款-对多个时间点的还款计划判断.feature"}
, glue = {"com.zufangbao.earth.cucumber.prepayment"}
)
public class paymentPlanChangeDetialTest {

}
