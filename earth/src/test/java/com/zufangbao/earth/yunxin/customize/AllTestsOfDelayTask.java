package com.zufangbao.earth.yunxin.customize;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * @author louguanyang on 2017/5/15.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        ReconciliationDelayTaskProcessTest.class,
        RepurchaseDelayTaskServicesTest.class,
        ZhongAnRepaymentPlanChargeDelayTaskServicesTest.class,
        ZhongAnRepaymentPlanDateDelayTaskServicesTest.class
})
public class AllTestsOfDelayTask {
}
