package com.suidifu.dowjones.service.impl;


import com.suidifu.dowjones.Dowjones;
import com.suidifu.dowjones.service.DailyRepurchaseService;
import com.suidifu.dowjones.utils.GenericJdbcSupport;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Dowjones.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(profiles = "test")
public class DailyRepurchaseServiceImplTest {
    @Autowired
    private DailyRepurchaseService dailyRepurchaseService;
    @Autowired
    private GenericJdbcSupport genericJdbcSupport;

    @Test
    @Sql("classpath:/test/test4ExecuteDailyRepurchaseJob.sql")
    public void test_executeDailyRepurchaseJob() throws ParseException {
        String financialContractUuid = "d2812bc5-5057-4a91-b3fd-9019506f0499";
        Date doingDay = DateUtils.parseDate("2017-12-25", "yyyy-MM-dd");

        dailyRepurchaseService.executeDailyRepurchaseJob(financialContractUuid, doingDay);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("financialContractUuid", financialContractUuid);
        parameters.put("createDate", doingDay);

        String resultQuery = "select repurchase_principal principal from daily_repurchase "
                + "where financial_contract_uuid = :financialContractUuid"
                + " and create_date = :createDate";
        List<BigDecimal> result = genericJdbcSupport.queryForSingleColumnList(resultQuery, parameters, BigDecimal.class);
        Assert.assertEquals(new BigDecimal("2000.00"), result.get(0));

    }
}
