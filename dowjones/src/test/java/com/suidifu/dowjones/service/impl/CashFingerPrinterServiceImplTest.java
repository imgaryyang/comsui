package com.suidifu.dowjones.service.impl;

import com.suidifu.dowjones.Dowjones;
import com.suidifu.dowjones.service.CashFingerPrinterService;
import com.suidifu.dowjones.vo.request.FingerPrinterParameter;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Date;

/**
 * @author: frankwoo(吴峻申) <br>
 * @date: 2018/1/8 <br>
 * @time: 21:47 <br>
 * @mail: frank_wjs@hotmail.com <br>
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Dowjones.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(profiles = "test")
@Slf4j
public class CashFingerPrinterServiceImplTest {
    private FingerPrinterParameter fingerPrinterParameter;
    @Resource
    private CashFingerPrinterService cashFingerPrinterService;

    @Before
    public void setUp() {
        fingerPrinterParameter = new FingerPrinterParameter();
        fingerPrinterParameter.setFinancialContractUuid("9495f5f2-d306-461a-8b03-5896923dc1b3");
        fingerPrinterParameter.setDataStreamUuid("0cb66f9e-420f-4e9a-bf49-5eb99b776252");
        fingerPrinterParameter.setPath("/Users/wujunshen/Downloads/csv/");
        fingerPrinterParameter.setTaskId("2");
    }

    @After
    public void tearDown() {
        fingerPrinterParameter = null;
    }

    @Test
    public void operateFile() throws IOException {
        Long startTime = new Date().getTime();
        cashFingerPrinterService.operateFile(fingerPrinterParameter);
        Long endTime = new Date().getTime();
        log.info("\n\n\n\n\nduration is :{}ms\n\n\n\n\n", endTime - startTime);
    }

    @Test
    public void operateFile_WITH_DATE() throws IOException {
        Long startTime = new Date().getTime();
        cashFingerPrinterService.operateFile(fingerPrinterParameter, "2018-01-08");
        Long endTime = new Date().getTime();
        log.info("\n\n\n\n\nduration is :{}ms\n\n\n\n\n", endTime - startTime);
    }

    @Ignore
    @Test
    public void save() {
        Long startTime = new Date().getTime();
        cashFingerPrinterService.save(fingerPrinterParameter);
        Long endTime = new Date().getTime();
        log.info("\n\n\n\n\nduration is :{}ms\n\n\n\n\n", endTime - startTime);
    }
}