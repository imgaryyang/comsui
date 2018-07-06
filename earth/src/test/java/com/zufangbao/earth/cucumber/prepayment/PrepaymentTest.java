/**
 * 
 */
package com.zufangbao.earth.cucumber.prepayment;

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
, features = {"classpath:cucumber/feature/提前还款/提前还款.feature"}
, glue = {"com.zufangbao.earth.cucumber.prepayment"}
)
public class PrepaymentTest {

}
