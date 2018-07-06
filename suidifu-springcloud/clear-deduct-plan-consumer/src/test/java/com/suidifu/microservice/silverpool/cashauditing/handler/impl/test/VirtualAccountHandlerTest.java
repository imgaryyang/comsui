package com.suidifu.microservice.silverpool.cashauditing.handler.impl.test;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import com.suidifu.microservice.handler.VirtualAccountHandler;
import com.zufangbao.sun.entity.account.VirtualAccount;
import com.zufangbao.sun.service.VirtualAccountService;

/*@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:/context/applicationContext-*.xml",
		"classpath:/local/applicationContext-*.xml" })
@TransactionConfiguration(defaultRollback=false)
@Transactional*/
@RunWith(SpringRunner.class)
@SpringBootTest
public class VirtualAccountHandlerTest {

	@Autowired
	private VirtualAccountHandler virtualAccountHandler;
	
	@Autowired
	private VirtualAccountService virtualAccountService;
	
	
	@Test
	@Sql("classpath:test/yunxin/test_refresh_virtual_accoun2.sql")
	public void testRefreshVirtualAccountBalance2() {
		
		String ledgerBookNo = "7dd4ee73-8dcf-4dbc-94ee-932ff9f48b58";
		String customerUuid = "customerUuid1";
		String financialContractUuid = "b674a323-0c30-4e4b-9eba-b14e05a9d80a";
		String oldVirtualAccountVersion = "48ea0da5-0c4f-4e2e-aad0-35b1e2efabfa";
		
		BigDecimal balance = new BigDecimal("10.00"); 
		
		virtualAccountHandler.refreshVirtualAccountBalance(ledgerBookNo, customerUuid, financialContractUuid, oldVirtualAccountVersion);
		
		VirtualAccount virtualAccount = virtualAccountService.getVirtualAccountByCustomerUuid(customerUuid);
		assertEquals(balance,virtualAccount.getTotalBalance());
		
		String version = virtualAccount.getVersion();
		String version2 = virtualAccountService.selectVirtualAccountVersion(virtualAccount.getVirtualAccountUuid());
		
		assertEquals(version,version2);
	}
	
}
