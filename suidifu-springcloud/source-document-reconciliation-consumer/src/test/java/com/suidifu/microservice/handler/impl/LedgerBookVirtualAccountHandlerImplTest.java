package com.suidifu.microservice.handler.impl;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

import com.suidifu.microservice.SourceDocumentReconciliationApplication;
import com.suidifu.microservice.handler.LedgerBookVirtualAccountHandler;
import java.math.BigDecimal;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author: frankwoo(吴峻申) <br>
 * @date: 2018/3/7 <br>
 * @time: 18:27 <br>
 * @mail: frank_wjs@hotmail.com <br>
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SourceDocumentReconciliationApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(profiles = "test")
@Slf4j
public class LedgerBookVirtualAccountHandlerImplTest {
    @Resource
    private LedgerBookVirtualAccountHandler ledgerBookVirtualAccountHandler;

    private String ledgerBookNo;
    private String customerUuid;

    @Before
    public void setUp() {
        ledgerBookNo = "7dd4ee73-8dcf-4dbc-94ee-932ff9f48b58";
        customerUuid = "uuid_5d-4166-44cb-b406-9b41eaaaaaaa";
    }

    @After
    public void tearDown() {
        ledgerBookNo = null;
        customerUuid = null;
    }

    @Test
    public void getBalanceOfCustomer() {
        BigDecimal amount = ledgerBookVirtualAccountHandler.getBalanceOfCustomer(ledgerBookNo,
                customerUuid);

        log.info("amount is:{}", amount);
        assertThat(amount, notNullValue());
        assertThat(amount, equalTo(new BigDecimal("0.00")));
    }

    @Test
    public void getBalanceOfCustomerNullAmount() {
        ledgerBookNo = "7dd4ee73-8dcf-4dbc-94ee-932ff9f48b5";

        BigDecimal amount = ledgerBookVirtualAccountHandler.getBalanceOfCustomer(ledgerBookNo,
                customerUuid);

        log.info("amount is:{}", amount);
        assertThat(amount, notNullValue());
        assertThat(amount, equalTo(BigDecimal.ZERO));
    }

    @Test
    public void getBalanceOfFrozenCapital() {
        BigDecimal amount = ledgerBookVirtualAccountHandler.getBalanceOfFrozenCapital(ledgerBookNo,
                customerUuid, "", "");

        log.info("amount is:{}", amount);
        assertThat(amount, notNullValue());
        assertThat(amount, equalTo(new BigDecimal("0.00")));
    }

    @Test
    public void getBalanceOfFrozenCapitalNullAmount() {
        ledgerBookNo = "7dd4ee73-8dcf-4dbc-94ee-932ff9f48b5";

        BigDecimal amount = ledgerBookVirtualAccountHandler.getBalanceOfFrozenCapital(ledgerBookNo,
                customerUuid, "", "");

        log.info("amount is:{}", amount);
        assertThat(amount, notNullValue());
        assertThat(amount, equalTo(BigDecimal.ZERO));
    }
}