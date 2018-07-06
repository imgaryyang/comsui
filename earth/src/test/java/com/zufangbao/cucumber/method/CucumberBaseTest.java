package com.zufangbao.cucumber.method;

import org.springframework.test.context.ContextConfiguration;

/**
 * Created by FanT on 2017/3/16.
 */
@ContextConfiguration(locations = {
        "classpath:/local/applicationContext-*.xml",
        "classpath:/cucumber_runtime/applicationContext-*.xml"})
public class CucumberBaseTest {
}
