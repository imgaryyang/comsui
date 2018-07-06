package com.suidifu.dowjones.service.impl;

import com.suidifu.dowjones.Dowjones;
import com.suidifu.dowjones.dao.DailyFirstOverdueRateDAO;
import com.suidifu.dowjones.service.DailyFirstOverdueRateService;
import com.suidifu.dowjones.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by zxj on 2018/3/19.
 * 首逾率
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Dowjones.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(profiles = "test")
@Slf4j
public class DailyFirstOverdueRateServiceImplTest {
    @Autowired
    private DailyFirstOverdueRateService dailyFirstOverdueRateService;

    @Test
    public void doCalculateFirstOverRate() {
        log.info("startTime ======" + new Date().getTime());
        dailyFirstOverdueRateService.doFile("", DateUtils.getDateFrom("2018-04-03", "yyyy-MM-dd"));
        log.info("endTime ======" + new Date().getTime());
    }
}
