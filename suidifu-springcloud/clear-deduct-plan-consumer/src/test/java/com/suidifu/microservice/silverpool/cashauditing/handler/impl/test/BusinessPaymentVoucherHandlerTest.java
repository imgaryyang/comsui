package com.suidifu.microservice.silverpool.cashauditing.handler.impl.test;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringRunner;

import com.suidifu.microservice.handler.LedgerBookVirtualAccountHandler;
import com.zufangbao.sun.entity.account.VirtualAccount;
import com.zufangbao.sun.service.VirtualAccountService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BusinessPaymentVoucherHandlerTest extends AbstractTransactionalJUnit4SpringContextTests {

	@Autowired
	private VirtualAccountService virtualAccountService;
	@Autowired
	private LedgerBookVirtualAccountHandler ledgerBookVirtualAccountHandler;
	/**
	 * * 商户付款 代偿 核销,滞留单
	 */

	@Test
	@Sql("classpath:test/yunxin/scan_sd_and_transfer_by_business_pay_voucher_temp_doc.sql")
	public void test_check_and_transfer_asstes_tmp_deposit_doc() {
		String company_customerUuid = "company_customer_uuid_1";
		String ledgerBookNo = "yunxin_ledger_book";
		VirtualAccount virtualAccount = virtualAccountService.getVirtualAccountByCustomerUuid(company_customerUuid);
		BigDecimal balance = ledgerBookVirtualAccountHandler.get_balance_of_customer(ledgerBookNo,
				company_customerUuid);
		assertEquals(0, balance.compareTo(virtualAccount.getTotalBalance()));
	}

}
