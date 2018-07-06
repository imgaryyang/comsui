package com.suidifu.microservice;

import com.suidifu.microservice.handler.CashFlowAutoIssueHandlerTest;
import com.suidifu.microservice.handler.LedgerbookClearingVoucherHandlerTest;
import com.suidifu.microservice.handler.LedgerbookClearingVoucherV2HandlerTest;
import com.suidifu.microservice.handler.ReceivableInAdvanceTest;
import com.suidifu.microservice.handler.ReconRepaymentOrderHandlerTest;
import com.suidifu.microservice.handler.VirtualAccountHandlerTest;
import com.suidifu.microservice.service.ThirdPartVoucherCommandLogServicetest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.springframework.boot.test.context.SpringBootTest;

@RunWith(Suite.class)
@SpringBootTest
@SuiteClasses({ReceivableInAdvanceTest.class,
		ReconRepaymentOrderHandlerTest.class,
		CashFlowAutoIssueHandlerTest.class,
		LedgerbookClearingVoucherHandlerTest.class,
		LedgerbookClearingVoucherV2HandlerTest.class,
		VirtualAccountHandlerTest.class,
		ThirdPartVoucherCommandLogServicetest.class,
})
public class ReconRepaymentOrderDeductConsumerApplicationTests {

	@Test
	public void contextLoads() {
	}

}
