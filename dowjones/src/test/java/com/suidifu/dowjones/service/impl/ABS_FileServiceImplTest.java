package com.suidifu.dowjones.service.impl;

import com.suidifu.dowjones.Dowjones;
import com.suidifu.dowjones.service.ABS_FileService;
import com.suidifu.dowjones.utils.DateUtils;
import jodd.util.CsvUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by zxj on 2018/2/8.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Dowjones.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(profiles = "test")
@Slf4j
public class ABS_FileServiceImplTest {
    @Autowired
    private ABS_FileService fileService;

    @Test
//    @Sql("classpath:/test/test4ABSActualRepaymentFileData.sql")
    public void doActualRepaymentFile() {
//        Date time = new Date();
        Date time = DateUtils.getDateFrom("2018-07-02", "yyyy-MM-dd");
//        fileService.doActualRepaymentFileData(DateUtils.addDays(time, 1));
//        fileService.doActualRepaymentFileData(DateUtils.substractDays(time, 1));
        fileService.doActualRepaymentFileData(time);
    }

    @Test
//    @Sql("classpath:/test/test4ABSContractChangeFileData.sql")
    public void doContractChangeFileData() {
//        Date time = new Date();
        Date time = DateUtils.getDateFrom("2018-07-02", "yyyy-MM-dd");
//        fileService.doContractChangeFileData(DateUtils.addDays(time, 1));
//        fileService.doContractChangeFileData(DateUtils.substractDays(time, 1));
        fileService.doContractChangeFileData(time);
    }

    @Test
//    @Sql("classpath:/test/test4ABSCurrentContractDetailFileData.sql")
    public void doCurrentContractDetailFileData() {
//        Date time = new Date();
        Date time = DateUtils.getDateFrom("2018-07-02", "yyyy-MM-dd");
//        fileService.doCurrentContractDetailFileData(DateUtils.addDays(time, 1));
//        fileService.doCurrentContractDetailFileData(DateUtils.substractDays(time, 1));
        fileService.doCurrentContractDetailFileData(time);
    }
}
