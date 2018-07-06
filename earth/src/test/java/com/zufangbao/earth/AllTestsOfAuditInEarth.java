package com.zufangbao.earth;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
//import com.zufangbao.wellsfargo.yunxin.handler.ActivePaymentVoucherNoSessionTest;
//import com.zufangbao.earth.yunxin.web.BeneficiaryAuditControllerTest;

@RunWith(Suite.class)
@SuiteClasses({
	AllTestsOfZhushiyunInEarth.class,
	AllTestsOfDucancanInEarth.class,
	AllTestsOfMieLongInEarth.class
})

public class AllTestsOfAuditInEarth {
	
}
