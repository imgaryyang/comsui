package com.suidifu.dowjones.service.impl;

import com.suidifu.dowjones.Dowjones;
import com.suidifu.dowjones.service.DailyRemittanceService;
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
import java.util.List;
import java.util.Map;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Dowjones.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(profiles = "test")
public class DailyDailyRemittanceServiceImplTest {

    @Autowired
    private DailyRemittanceService dailyRemittanceService;
    @Autowired
    private GenericJdbcSupport genericJdbcSupport;


    @Test
    @Sql("classpath:/test/test4ExecuteDailyRemittanceJob.sql")
    public void TestRemittanceService() throws ParseException {

        String financialContractUuid = "d2812bc5-5057-4a91-b3fd-9019506f0499";
        Date doDate = DateUtils.parseDate("2017-12-25", "yyyy-MM-dd");

        dailyRemittanceService.doRemittanceData(financialContractUuid, doDate);

        String sql = "select * from daily_remittance";

        List<Map<String, Object>> results = genericJdbcSupport.queryForList(sql);

        Assert.assertEquals(1, results.size());

        Map<String, Object> result = results.get(0);

        Assert.assertEquals("2017-12-25",
                com.suidifu.dowjones.utils.DateUtils.getDateFormatYYMMDD((Date) result.get("create_date")));
        Assert.assertEquals("d2812bc5-5057-4a91-b3fd-9019506f0499", result.get("financial_contract_uuid"));
        Assert.assertEquals(12L, result.get("application_count"));
        Assert.assertEquals(new BigDecimal("918500.00"), result.get("application_amount"));
        Assert.assertEquals(12L, result.get("plan_count"));
        Assert.assertEquals(new BigDecimal("918500.00"), result.get("plan_amount"));
        Assert.assertEquals(12L, result.get("actual_count"));
        Assert.assertEquals(new BigDecimal("918500.00"), result.get("actual_amount"));
        Assert.assertEquals(new BigDecimal("2701000.00"), result.get("asset_amount"));
        Assert.assertEquals(new BigDecimal("901000.00"), result.get("asset_principal"));
        Assert.assertEquals(new BigDecimal("180.00"), result.get("asset_interest"));
        Assert.assertEquals(new BigDecimal("180.00"), result.get("asset_loan_service_fee"));
    }

}
