/**
 * 
 */
package com.zufangbao.earth.cucumber.ThirdPartPayVoucher;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

/**
 * @author wukai
 *
 */
@RunWith(Cucumber.class)
@CucumberOptions(
//        format = {"json:target/json-report/dw.json"}
//        , features = {"classpath:com/zufangbao/earth/cucumber/ThirdPartPayVoucher/第三方本端多凭证校验1.feature"}
//        , glue = {"com.zufangbao.earth.cucumber.ThirdPartPayVoucher"}
        tags = {"@Prepayment1"}
)
public class ThirdPartPayTest1 {

}
