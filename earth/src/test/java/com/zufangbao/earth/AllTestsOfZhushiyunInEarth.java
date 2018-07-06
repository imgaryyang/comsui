package com.zufangbao.earth;

import com.zufangbao.earth.web.controller.repayment.RepaymentOrderControllerTest;
import com.zufangbao.earth.yunxin.handler.ActivePaymentVoucherHandlerTest;
import com.zufangbao.earth.yunxin.handler.RepaymentPlanRecoverHandlerTest;
import com.zufangbao.earth.yunxin.handler.impl.BusinessPaymentVoucherHandlerTest;
import com.zufangbao.earth.yunxin.handler.impl.repaymentOrderRecon.ReconRepaymentOrderHandlerTest;
import com.zufangbao.earth.yunxin.ledgerBook.LedgerBookEntryTest;
import com.zufangbao.earth.yunxin.ledgerBook.LedgerBookIntegrationTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

//import com.zufangbao.wellsfargo.yunxin.handler.ActivePaymentVoucherNoSessionTest;

@RunWith(Suite.class)
@SuiteClasses({
	LedgerBookEntryTest.class,
	LedgerBookIntegrationTest.class,
	BusinessPaymentVoucherHandlerTest.class,
	ActivePaymentVoucherHandlerTest.class,
//	BeneficiaryAuditControllerTest.class,
		ReconRepaymentOrderHandlerTest.class,
		RepaymentPlanRecoverHandlerTest.class,
		RepaymentOrderControllerTest.class
	
})

public class AllTestsOfZhushiyunInEarth {
	
}
