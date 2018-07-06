package com.suidifu.dowjones;

import com.suidifu.dowjones.controller.CashFingerPrinterControllerTest;
import com.suidifu.dowjones.dao.impl.CashFingerPrinterDAOImplTest;
import com.suidifu.dowjones.quartz.ScheduledJobTest;
import com.suidifu.dowjones.service.impl.CashFingerPrinterServiceImplTest;
import com.suidifu.dowjones.utils.FTPUtils;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * @author: frankwoo(吴峻申) <br>
 * @date: 2017/12/5 <br>
 * @time: 下午9:43 <br>
 * @mail: frank_wjs@hotmail.com <br>
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
//        OverdueAnalyzeControllerTest.class,
//        OverdueAnalyzeServiceImplTest.class,
//        OverdueAnalyzeDAOImplTest.class,
        CashFingerPrinterDAOImplTest.class,
        CashFingerPrinterServiceImplTest.class,
        CashFingerPrinterControllerTest.class,
        ScheduledJobTest.class,
        FTPUtils.class
})
public class TestAll {
}