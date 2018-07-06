package com.suidifu.microservice;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.springframework.boot.test.context.SpringBootTest;

import com.suidifu.microservice.silverpool.cashauditing.handler.impl.test.BeneficiaryAuditControllerTest;
import com.suidifu.microservice.silverpool.cashauditing.handler.impl.test.BusinessPaymentVoucherHandlerTest;
import com.suidifu.microservice.silverpool.cashauditing.handler.impl.test.LedgerbookClearingVoucherHandlerTest;
import com.suidifu.microservice.silverpool.cashauditing.handler.impl.test.LedgerbookClearingVoucherV2HandlerTest;
import com.suidifu.microservice.silverpool.cashauditing.handler.impl.test.VirtualAccountHandlerTest;
import com.suidifu.microservice.silverpool.cashauditing.service.impl.ClearingExecLogOnlineReconciliationTest;
import com.suidifu.microservice.silverpool.cashauditing.service.impl.ClearingVoucherTest;
import com.suidifu.microservice.silverpool.cashauditing.service.impl.SourceDocumentServiceTest;

@RunWith(Suite.class)
@SpringBootTest
@SuiteClasses({
	BeneficiaryAuditControllerTest.class,
	BusinessPaymentVoucherHandlerTest.class,
	LedgerbookClearingVoucherHandlerTest.class,
	LedgerbookClearingVoucherV2HandlerTest.class,
	VirtualAccountHandlerTest.class,
	ClearingExecLogOnlineReconciliationTest.class,
	ClearingVoucherTest.class,
	SourceDocumentServiceTest.class,
	
}
		)
public class ClearDeductPlanConsumerApplicationTests {

	@Test
	public void contextLoads() {
	}

}
