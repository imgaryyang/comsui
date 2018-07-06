package com.zufangbao.earth.yunxin.service;

import com.zufangbao.sun.entity.bank.Bank;
import com.zufangbao.sun.utils.EHcacheUtil;
import com.zufangbao.sun.yunxin.service.BankService;
import org.apache.commons.collections.MapUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:/local/applicationContext-*.xml"})
@TransactionConfiguration()
public class BankServiceTest extends AbstractTransactionalJUnit4SpringContextTests {

    private static final String CACHE_NAME = "banks";
    private static final String CACHE_KEY = "bankMap";

    @Autowired
    BankService bankService;

    @Before
    public void before() {
        bankService.evictCachedBanks();
    }

    @Test
    @Sql("classpath:test/yunxin/geography/bank.sql")
    public void testGetCachedBanksFromCache() {
        String bankCode1 = "C10102";
        String bankName1 = "中国工商银行 ";

        Map<String, Bank> map = new HashMap<>(2);
        Bank bank = new Bank();
        bank.setId(10L);
        bank.setBankCode(bankCode1);
        bank.setBankName(bankName1);
        map.put(bankCode1, bank);
        EHcacheUtil.getInstance().put(CACHE_NAME, CACHE_KEY, map);

        Map<String, Bank> bankMap = bankService.getCachedBanks();
        Assert.assertTrue(MapUtils.isNotEmpty(bankMap));
        Assert.assertEquals(1L, bankMap.size());
        Bank bankInCache = bankMap.get(bankCode1);
        logger.info("bankInCache:" + bankInCache.toString());
        Assert.assertEquals(bank.getId(), bankInCache.getId());
        Assert.assertEquals(bank.getBankCode(), bankInCache.getBankCode());
        Assert.assertEquals(bank.getBankName(), bankInCache.getBankName());
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

            logger.info(index + " read from Cache");

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
