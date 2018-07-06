package com.zufangbao.earth;

import com.zufangbao.earth.util.EnumUtilTest;
import com.zufangbao.earth.web.controller.RepurchaseControllerTest;
import com.zufangbao.earth.yunxin.service.AmountStatisticsTest;
import com.zufangbao.earth.yunxin.service.FileStorageServiceTest;
import com.zufangbao.earth.yunxin.service.RemittancePlanExecLogHandlerTest;
import com.zufangbao.earth.yunxin.web.BankReconciliationControllerTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
	RemittancePlanExecLogHandlerTest.class,
	RepurchaseControllerTest.class,
	AmountStatisticsTest.class,
	FileStorageServiceTest.class,
	EnumUtilTest.class,
	BankReconciliationControllerTest.class
	})
public class AllTestOfXieWenQianInEarth {

}
