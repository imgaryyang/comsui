package com.suidifu.dowjones.service.impl;

import com.suidifu.dowjones.Dowjones;
import com.suidifu.dowjones.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author: frankwoo(吴峻申) <br>
 * @date: 2017/11/6 <br>
 * @time: 下午8:18 <br>
 * @mail: frank_wjs@hotmail.com <br>
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Dowjones.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(profiles = "test")
public class CustomerServiceImplTest {
    private String idNumber;
    @Resource
    private CustomerService customerService;

    @Before
    public void setUp() {
        //idNumber = "95136038-aebe-4de0-8a1a-2ea46fb2f917";
        idNumber = "320301198502169142";
    }

    @After
    public void tearDown() {
        idNumber = "";
    }

    @Test
    public void extractContractInfoByIDNumber() throws Exception {
        Long startTime = new Date().getTime();
        String jsonString = customerService.extractContractInfoByIDNumber(idNumber);
        Long endTime = new Date().getTime();
        log.info("duration is :{}ms", endTime - startTime);
        log.info("json is: {}", jsonString);
    }

    @Test
    public void getStatisticByIDNumber() throws Exception {
        Long startTime = new Date().getTime();
        String jsonString = customerService.getStatisticByIDNumber(idNumber);
        Long endTime = new Date().getTime();
        log.info("duration is :{}ms", endTime - startTime);
        log.info("json is: {}", jsonString);
    }
}