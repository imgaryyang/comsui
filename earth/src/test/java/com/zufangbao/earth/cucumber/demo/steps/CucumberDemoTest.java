/**
 * 
 */
package com.zufangbao.earth.cucumber.demo.steps;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

/**
 * @author wukai
 *
 */
@RunWith(Cucumber.class)
@CucumberOptions(
format = {"json:target/json-report/dw.json"}
, features = {"classpath:cucumber/demo/features/FixedAmountWithdraw.feature"}
, glue = {"com.zufangbao.earth.cucumber.demo.steps"}
)
public class CucumberDemoTest {

}
