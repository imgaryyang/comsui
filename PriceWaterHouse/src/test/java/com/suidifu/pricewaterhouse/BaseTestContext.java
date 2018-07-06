/**
 * 
 */
package com.suidifu.pricewaterhouse;

import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.suidifu.pricewaterhouse.yunxin.task.VoucherTaskV2_0;

/**
 * @author wukai
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes= {Pricewaterhouse.class})
@ComponentScan(excludeFilters= @ComponentScan.Filter(type=FilterType.ASSIGNABLE_TYPE,value=VoucherTaskV2_0.class))
@WebIntegrationTest
@ActiveProfiles(value="test")
public class BaseTestContext {

}
