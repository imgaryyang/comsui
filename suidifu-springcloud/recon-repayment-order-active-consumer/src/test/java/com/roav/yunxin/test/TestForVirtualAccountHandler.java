package com.roav.yunxin.test;

import com.suidifu.microservice.handler.VirtualAccountHandler;
import com.zufangbao.sun.entity.account.BankAccountAdapter;
import com.zufangbao.sun.entity.account.VirtualAccount;
import com.zufangbao.sun.entity.customer.CustomerType;
import com.zufangbao.sun.entity.directbank.business.ContractAccount;
import com.zufangbao.sun.service.BankAccountService;
import com.zufangbao.sun.service.ContractAccountService;
import com.zufangbao.sun.service.VirtualAccountService;
import com.zufangbao.sun.utils.EnumUtil;
import com.zufangbao.sun.yunxin.BankCardStatus;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

/**
 * Created by MieLongJun on 18-3-7.
 */
public class TestForVirtualAccountHandler extends TestBase{

    @Autowired
    private VirtualAccountHandler virtualAccountHandler;

    @Autowired
    private VirtualAccountService virtualAccountService;

    @Autowired
    private ContractAccountService contractAccountService;

    @Test
    @Sql("classpath:test/yunxin/test_refresh_virtual_accoun.sql")
    public void testRefreshVirtualAccountBalance_CustomerUuid() {

        String ledgerBookNo = "7dd4ee73-8dcf-4dbc-94ee-932ff9f48b58";
        String customerUuid = "customerUuid1";
        String financialContractUuid = "b674a323-0c30-4e4b-9eba-b14e05a9d80a";
        String oldVirtualAccountVersion = "48ea0da5-0c4f-4e2e-aad0-35b1e2efabfa";

        BigDecimal balance = new BigDecimal("10.00");

        virtualAccountHandler.refreshVirtualAccountBalance(ledgerBookNo, customerUuid, financialContractUuid, oldVirtualAccountVersion);

        VirtualAccount virtualAccount = virtualAccountService.getVirtualAccountByCustomerUuid(customerUuid);

        assertEquals(balance, virtualAccount.getTotalBalance());

        String newVersion = virtualAccount.getVersion();

        String selectVersion = virtualAccountService.selectVirtualAccountVersion(virtualAccount.getVirtualAccountUuid());

        assertEquals(newVersion, selectVersion);
    }

    @Test
    @Sql("classpath:test/yunxin/contract_account_edit_test.sql")
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
        CustomerType customerType = EnumUtil.fromOrdinal(CustomerType.class, 0);
        BankAccountService bankAccountService = BankAccountService.bankAccountServiceFactory(customerType);


        bankAccountService.update(bankAccountAdapter, "c3adbaa4-c2c1-11e6-abc5-00163e002839");

        ContractAccount account = contractAccountService.getContractAccountByUuid("c3adbaa4-c2c1-11e6-abc5-00163e002839");

        assertEquals("11", account.getPayerName());
        assertEquals("000", account.getPayAcNo());
        assertEquals(BankCardStatus.BINDING, account.getBankCardStatus());
        assertEquals("123", account.getBankCode());
        assertEquals("中国银行", account.getBank());
        assertEquals("杭州", account.getCity());
        assertEquals("10001", account.getCityCode());
        assertEquals("浙江省", account.getProvince());
        assertEquals("0001", account.getProvinceCode());
    }

}
