package com.suidifu.microservice.handler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.zufangbao.sun.entity.account.BankAccountAdapter;
import com.zufangbao.sun.entity.account.VirtualAccount;
import com.zufangbao.sun.entity.customer.CustomerType;
import com.zufangbao.sun.entity.directbank.business.ContractAccount;
import com.zufangbao.sun.service.BankAccountService;
import com.zufangbao.sun.service.ContractAccountService;
import com.zufangbao.sun.service.OrderService;
import com.zufangbao.sun.service.VirtualAccountService;
import com.zufangbao.sun.utils.EnumUtil;
import com.zufangbao.sun.yunxin.BankCardStatus;
import com.zufangbao.sun.yunxin.exception.VirtualAccountNotExsitException;
import com.zufangbao.sun.yunxin.handler.RepaymentPlanHandler;
import com.zufangbao.sun.yunxin.service.RepaymentPlanService;
import java.math.BigDecimal;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class VirtualAccountHandlerTest {

	@Autowired
	private RepaymentPlanService repaymentPlanService;
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private RepaymentPlanHandler repaymentPlanHandler;
	
	@Autowired
	private VirtualAccountHandler virtualAccountHandler;
	
	@Autowired
	private VirtualAccountService virtualAccountService;
	
	@Autowired
	private ContractAccountService contractAccountService;
	
	@Test
	@Sql("classpath:test/yunxin/virtualAccount/test_refresh_virtual_accoun.sql")
	public void testRefreshVirtualAccountBalance_CustomerUuid() {
		
		String ledgerBookNo = "7dd4ee73-8dcf-4dbc-94ee-932ff9f48b58";
		String customerUuid = "customerUuid1";
		String financialContractUuid = "b674a323-0c30-4e4b-9eba-b14e05a9d80a";
		String oldVirtualAccountVersion = "48ea0da5-0c4f-4e2e-aad0-35b1e2efabfa";
		
		BigDecimal balance = new BigDecimal("10.00"); 
		
		virtualAccountHandler.refreshVirtualAccountBalance(ledgerBookNo, customerUuid, financialContractUuid, oldVirtualAccountVersion);
		
		VirtualAccount virtualAccount = virtualAccountService.getVirtualAccountByCustomerUuid(customerUuid);
		
		assertEquals(balance,virtualAccount.getTotalBalance());
		
		String newVersion = virtualAccount.getVersion();
		
		String selectVersion = virtualAccountService.selectVirtualAccountVersion(virtualAccount.getVirtualAccountUuid());
		
		assertEquals(newVersion,selectVersion);
	}
	
	
	@Test
	@Sql("classpath:test/yunxin/virtualAccount/test_refresh_virtual_accoun2.sql")
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
	
	@Test
	@Sql("classpath:test/yunxin/virtualAccount/test_refresh_virtual_account_virtualAccount_no.sql")
	public void testRefreshVirtualAccountBalance_VirtualAccountNo() {
		
		String ledgerBookNo = "7dd4ee73-8dcf-4dbc-94ee-932ff9f48b58";
		String virtualAccountNo = "6217001210031616480";
		String oldVirtualAccountVersion = "48ea0da5-0c4f-4e2e-aad0-35b1e2efabfa";
		
		BigDecimal balance = new BigDecimal("20.00"); 
		
		virtualAccountHandler.refreshVirtualAccountBalance(ledgerBookNo, virtualAccountNo, oldVirtualAccountVersion);
		
		VirtualAccount virtualAccount = virtualAccountService.getVirtualAccountByVirtualAccountNo(virtualAccountNo);
		
		assertEquals(balance,virtualAccount.getTotalBalance());
		
		String newVersion = virtualAccount.getVersion();
		
		String selectVersion = virtualAccountService.selectVirtualAccountVersion(virtualAccount.getVirtualAccountUuid());
		
		assertEquals(newVersion,selectVersion);
	}
	
	@Test    //Exception
	@Sql("classpath:test/yunxin/virtualAccount/test_refresh_virtual_account_virtualAccount_no.sql")
	public void testRefreshVirtualAccountBalance_VirtualAccountNotExsitException() {
		
		String ledgerBookNo = "7dd4ee73-8dcf-4dbc-94ee-932ff9f48b58";
		String virtualAccountNo = "62170012100316164808";
		String oldVirtualAccountVersion = "48ea0da5-0c4f-4e2e-aad0-35b1e2efabfa";
		
		try {
			virtualAccountHandler.refreshVirtualAccountBalance(ledgerBookNo, virtualAccountNo, oldVirtualAccountVersion);
			fail();	
		} catch (VirtualAccountNotExsitException e) {
			assertTrue(true);
		}
		
	}
	
	
	@Test 
	@Sql("classpath:test/yunxin/virtualAccount/contract_account_edit_test.sql")
	public void testEditContractAccount() {
		
		BankAccountAdapter bankAccountAdapter = new BankAccountAdapter();
		bankAccountAdapter.setAccountName("11");
		bankAccountAdapter.setAccountNo("000");
		bankAccountAdapter.setBankCardStatus(BankCardStatus.BINDING);
		bankAccountAdapter.setBankCode("123");
		bankAccountAdapter.setBankName("中国银行");
		bankAccountAdapter.setCity("杭州");
		bankAccountAdapter.setCityCode("10001");
		bankAccountAdapter.setProvince("浙江省");
		bankAccountAdapter.setProvinceCode("0001");
		CustomerType customerType =  EnumUtil.fromOrdinal(CustomerType.class, 0);
		BankAccountService bankAccountService=BankAccountService.bankAccountServiceFactory(customerType);	
		
		
		bankAccountService.update(bankAccountAdapter, "c3adbaa4-c2c1-11e6-abc5-00163e002839");
		
		ContractAccount account = contractAccountService.getContractAccountByUuid("c3adbaa4-c2c1-11e6-abc5-00163e002839");
		
		assertEquals("11",account.getPayerName());
		assertEquals("000",account.getPayAcNo());
		assertEquals(BankCardStatus.BINDING,account.getBankCardStatus());
		assertEquals("123",account.getBankCode());
		assertEquals("中国银行",account.getBank());
		assertEquals("杭州",account.getCity());
		assertEquals("10001",account.getCityCode());
		assertEquals("浙江省",account.getProvince());
		assertEquals("0001",account.getProvinceCode());
	}
	
	
}
