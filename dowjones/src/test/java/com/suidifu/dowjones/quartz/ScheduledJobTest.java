package com.suidifu.dowjones.quartz;

import com.suidifu.dowjones.Dowjones;
import com.suidifu.dowjones.service.CashFingerPrinterService;
import com.suidifu.dowjones.utils.AsyncFtp;
import com.suidifu.dowjones.vo.request.FingerPrinterParameter;
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
 * @date: 2017/11/21 <br>
 * @time: 下午4:54 <br>
 * @mail: frank_wjs@hotmail.com <br>
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Dowjones.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(profiles = "test")
@Slf4j
public class ScheduledJobTest {
    @Resource
    private CashFingerPrinterService cashFingerPrinterService;

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void executeInternal() {
        Long startTime = new Date().getTime();

        FingerPrinterParameter[] fingerPrinterParameters = cashFingerPrinterService.loadScheduleJob();
        for (FingerPrinterParameter fingerPrinterParameter : fingerPrinterParameters) {
            new Thread(new AsyncFtp(cashFingerPrinterService, fingerPrinterParameter)).start();
        }

        Long endTime = new Date().getTime();
        log.info("\n\n\n\n\nduration is :{}ms\n\n\n\n\n",
                endTime - startTime);
    }
}