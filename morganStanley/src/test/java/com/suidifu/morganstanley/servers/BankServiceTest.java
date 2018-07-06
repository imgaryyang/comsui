package com.suidifu.morganstanley.servers;

import com.suidifu.morganstanley.TestMorganStanley;
import com.zufangbao.sun.entity.bank.Bank;
import com.zufangbao.sun.yunxin.service.BankService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestMorganStanley.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration
@Slf4j
@ActiveProfiles(value = "test")
public class BankServiceTest {

    @Autowired
    BankService bankService;

    @Before
    public void before() {
        bankService.evictCachedBanks();
    }

    @Test
    @Sql("classpath:test/yunxin/geography/bank.sql")
    public void testGetCachedBanksFromBD() {
        for (int index = 1; index < 100; index++) {
            String bankCode1 = "C10102";
            String bankCode2 = "C10103";
            String bankName1 = "中国工商银行 ";
            String bankName2 = "中国农业银行 ";

            Map<String, Bank> bankMap = bankService.getCachedBanks();
            log.info("bankService.getCachedBanks");
            Assert.assertTrue(MapUtils.isNotEmpty(bankMap));
            Assert.assertEquals(4947L, bankMap.size());
            Bank bank1InCache = bankMap.get(bankCode1);
            Assert.assertEquals(bankCode1, bank1InCache.getBankCode());
            Assert.assertEquals(bankName1, bank1InCache.getBankName());

            Bank bank2InCache = bankMap.get(bankCode2);
            Assert.assertEquals(bankCode2, bank2InCache.getBankCode());
            Assert.assertEquals(bankName2, bank2InCache.getBankName());
        }
    }

}
