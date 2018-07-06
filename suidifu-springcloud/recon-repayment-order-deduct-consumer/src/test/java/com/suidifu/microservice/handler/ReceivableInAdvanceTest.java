package com.suidifu.microservice.handler;


import static org.junit.Assert.fail;

import com.suidifu.hathaway.job.Priority;
import com.suidifu.microservice.handler.impl.ThirdPartyVoucherRepaymentOrderWithReconciliationImpl;
import com.zufangbao.sun.ledgerbookv2.utils.GeneralAccountTemplateHelperForTest;
import org.hibernate.SessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ReceivableInAdvanceTest {
	@Autowired 
	private ThirdPartyVoucherRepaymentOrderWithReconciliationImpl thirdPartyVoucherRepaymentOrderWithReconciliationNoSessionImpl;

	@Autowired
	private GeneralAccountTemplateHelperForTest generalAccountTemplateHelperForTest;

	@Autowired
	protected SessionFactory sessionFactory;

	@Test
	@Sql(value = { "classpath:test/yunxin/delete_all_table.sql",
			"classpath:test/yunxin/repaymentOrder/receivable_in_advance_test_ww1.sql" })
	public void receivable_in_advance() {
		String orderUuid = "order_uuid_1";
		String detailUuid = "order_detail_uuid_1";

//		generalAccountTemplateHelperForTest.createTemplateBy("yunxin_ledger_book", EventType.REVEIVABLE_INADVANCE_ORDER_WRITE_OFF);
		String contractUuid = "contract_uuid_1";

//		generalAccountTemplateHelperForTest.createTemplateBy("yunxin_ledger_book", EventType.CREATE_RECEIVABLE_IN_ADVANCE);

		try {
			thirdPartyVoucherRepaymentOrderWithReconciliationNoSessionImpl.generateThirdPartVoucherWithReconciliation(
					contractUuid, orderUuid, "paymentOrder_1", Priority.High.getPriority());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}

	}

}
