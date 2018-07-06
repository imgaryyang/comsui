package com.suidifu.watchman;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @author louguanyang at 2018/1/9 10:56
 * @mail louguanyang@hzsuidifu.com
 */
@Slf4j
@RunWith(SpringRunner.class)
@ActiveProfiles(value = "junit")
@SpringBootTest(classes = WatchManTests.class, webEnvironment = WebEnvironment.RANDOM_PORT)
public class BankRepositoryTest {

//  @LocalServerPort
//  private int port;
//
//  @Test
//  public void testPort() {
//    System.out.println("LocalServerPort:" + port);
//  }

    @Resource
    private BankRepository bankRepository;

    @Before
    public void before() {

    }

    @Test
    public void testGetBankByBankCode() {
        for (int i = 0; i < 2; i++) {
            long start = System.currentTimeMillis();
            Bank bank = bankRepository.findByBankCode("800001");
            Assert.assertNotNull(bank);
            log.info("第{}次查询, Bank[{}], use {} ms.", (i + 1), bank.toString(),
                    (System.currentTimeMillis() - start));
        }
    }
}
