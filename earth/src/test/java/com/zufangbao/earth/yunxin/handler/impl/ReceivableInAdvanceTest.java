package com.zufangbao.earth.yunxin.handler.impl;


import com.suidifu.hathaway.job.Priority;
import com.zufangbao.sun.ledgerbookv2.enums.EventType;
import com.zufangbao.sun.ledgerbookv2.utils.GeneralAccountTemplateHelperForTest;
import com.zufangbao.wellsfargo.yunxin.handler.impl.ThirdPartyVoucherRepaymentOrderWithReconciliationNoSessionImpl;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.Assert.fail;

public class ReceivableInAdvanceTest extends AbstractNeverRollBackApplicationTest {
	@Autowired 
	private ThirdPartyVoucherRepaymentOrderWithReconciliationNoSessionImpl thirdPartyVoucherRepaymentOrderWithReconciliationNoSessionImpl;

	@Autowired
	private GeneralAccountTemplateHelperForTest generalAccountTemplateHelperForTest;


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
		sessionClear();
		
		
		

	}
}
