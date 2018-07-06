package com.zufangbao.earth;

import com.zufangbao.earth.yunxin.handler.ActivePaymentVoucherHandlerTest;
import com.zufangbao.earth.yunxin.handler.impl.BusinessPaymentVoucherHandlerTest;
import com.zufangbao.earth.yunxin.handler.impl.CashFlowHandlerTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;


@RunWith(Suite.class)
@SuiteClasses({
	ActivePaymentVoucherHandlerTest.class,
	BusinessPaymentVoucherHandlerTest.class,
	CashFlowHandlerTest.class
	})
public class AllTestsOfVoucherInEarth {

}
