package com.suidifu.microservice.handler;

import static org.junit.Assert.assertEquals;

import com.suidifu.microservice.ConsumerTest;
import com.zufangbao.sun.entity.account.VirtualAccount;
import com.zufangbao.sun.service.ContractAccountService;
import com.zufangbao.sun.service.VirtualAccountService;
import java.math.BigDecimal;
import lombok.extern.log4j.Log4j2;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author louguanyang at 2018/3/6 20:54
 * @mail louguanyang@hzsuidifu.com
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ConsumerTest.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Log4j2
@ActiveProfiles(value = "test")
public class VirtualAccountHandlerTest {
  @Autowired
  private VirtualAccountHandler virtualAccountHandler;

  @Autowired
  private VirtualAccountService virtualAccountService;

  @Autowired
  private ContractAccountService contractAccountService;

  @Test
  @Sql("classpath:test/sql/virtualAccount/test_refresh_virtual_account.sql")
  public void testRefreshVirtualAccountBalance_CustomerUuid() {

    String ledgerBookNo = "7dd4ee73-8dcf-4dbc-94ee-932ff9f48b58";
    String customerUuid = "customerUuid1";
    String financialContractUuid = "b674a323-0c30-4e4b-9eba-b14e05a9d80a";
    String oldVirtualAccountVersion = "48ea0da5-0c4f-4e2e-aad0-35b1e2efabfa";

    BigDecimal balance = new BigDecimal("10.00");

    virtualAccountHandler
        .refreshVirtualAccountBalance(ledgerBookNo, customerUuid, financialContractUuid, oldVirtualAccountVersion);

    VirtualAccount virtualAccount = virtualAccountService.getVirtualAccountByCustomerUuid(customerUuid);

    assertEquals(balance, virtualAccount.getTotalBalance());

    String newVersion = virtualAccount.getVersion();

    String selectVersion = virtualAccountService.selectVirtualAccountVersion(virtualAccount.getVirtualAccountUuid());

    assertEquals(newVersion, selectVersion);
  }


  @Test
  @Sql("classpath:test/sql/virtualAccount/test_refresh_virtual_account2.sql")
  public void testRefreshVirtualAccountBalance2() {

    String ledgerBookNo = "7dd4ee73-8dcf-4dbc-94ee-932ff9f48b58";
    String customerUuid = "customerUuid1";
    String financialContractUuid = "b674a323-0c30-4e4b-9eba-b14e05a9d80a";
    String oldVirtualAccountVersion = "48ea0da5-0c4f-4e2e-aad0-35b1e2efabfa";

    BigDecimal balance = new BigDecimal("10.00");

    virtualAccountHandler
        .refreshVirtualAccountBalance(ledgerBookNo, customerUuid, financialContractUuid, oldVirtualAccountVersion);

    VirtualAccount virtualAccount = virtualAccountService.getVirtualAccountByCustomerUuid(customerUuid);
    assertEquals(balance, virtualAccount.getTotalBalance());

    String version = virtualAccount.getVersion();
    String version2 = virtualAccountService.selectVirtualAccountVersion(virtualAccount.getVirtualAccountUuid());

    assertEquals(version, version2);
  }


}