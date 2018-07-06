package com.suidifu.dowjones.service.impl;

import com.suidifu.dowjones.Dowjones;
import com.suidifu.dowjones.service.DailyPlanRepaymentService;
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
public class DailyPlanRepaymentServiceImplTest {
    @Autowired
    private DailyPlanRepaymentService dailyPlanRepaymentService;
    @Autowired
    private GenericJdbcSupport genericJdbcSupport;

    @Test
    @Sql("classpath:/test/test4ExecuteDailyPlanRepaymentJob.sql")
    public void test_executeDailyPlanRepaymentJob_0() throws ParseException {
        String financialContractUuid = "d2812bc5-5057-4a91-b3fd-9019506f0499";
        Date doingDay = DateUtils.parseDate("2016-10-06", "yyyy-MM-dd");

        dailyPlanRepaymentService.executeDailyPlanRepaymentJob(financialContractUuid, doingDay);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("financialContractUuid", financialContractUuid);
        parameters.put("createDate", doingDay);

        String resultQuery = "select * from daily_plan_repayment "
                + "where financial_contract_uuid = :financialContractUuid"
                + " and create_date = :createDate"
                + " and plan_style = 0";

        List<Map<String, Object>> results = genericJdbcSupport.queryForList(resultQuery, parameters);

        Assert.assertEquals(1, results.size());

        Map<String, Object> result = results.get(0);

        Assert.assertEquals(new BigDecimal("515.20"), result.get("asset_principal_value"));
        Assert.assertEquals(new BigDecimal("64.15"), result.get("asset_interest_value"));
        Assert.assertEquals(new BigDecimal("0.00"), result.get("loan_service_fee"));
        Assert.assertEquals(new BigDecimal("0.00"), result.get("loan_tech_fee"));
        Assert.assertEquals(new BigDecimal("100.00"), result.get("loan_other_fee"));
        Assert.assertEquals(new BigDecimal("1.00"), result.get("overdue_fee_penalty"));
        Assert.assertEquals(new BigDecimal("2.00"), result.get("overdue_fee_obligation"));
        Assert.assertEquals(new BigDecimal("0.00"), result.get("overdue_fee_service"));
        Assert.assertEquals(new BigDecimal("111.00"), result.get("overdue_fee_other"));

    }

    @Test
    @Sql("classpath:/test/test4ExecuteDailyPlanRepaymentJob.sql")
    public void test_executeDailyPlanRepaymentJob_1() throws ParseException {
        String financialContractUuid = "d2812bc5-5057-4a91-b3fd-9019506f0499";
        Date doingDay = DateUtils.parseDate("2016-10-07", "yyyy-MM-dd");

        dailyPlanRepaymentService.executeDailyPlanRepaymentJob(financialContractUuid, doingDay);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("financialContractUuid", financialContractUuid);
        parameters.put("createDate", doingDay);

        String resultQuery = "select * from daily_plan_repayment "
                + "where financial_contract_uuid = :financialContractUuid"
                + " and create_date = :createDate"
                + " and plan_style = 1";

        List<Map<String, Object>> results = genericJdbcSupport.queryForList(resultQuery, parameters);

        Assert.assertEquals(1, results.size());

        Map<String, Object> result = results.get(0);

        Assert.assertEquals(new BigDecimal("515.20"), result.get("asset_principal_value"));
        Assert.assertEquals(new BigDecimal("64.15"), result.get("asset_interest_value"));
        Assert.assertEquals(new BigDecimal("0.00"), result.get("loan_service_fee"));
        Assert.assertEquals(new BigDecimal("0.00"), result.get("loan_tech_fee"));
        Assert.assertEquals(new BigDecimal("100.00"), result.get("loan_other_fee"));
        Assert.assertEquals(new BigDecimal("1.00"), result.get("overdue_fee_penalty"));
        Assert.assertEquals(new BigDecimal("2.00"), result.get("overdue_fee_obligation"));
        Assert.assertEquals(new BigDecimal("0.00"), result.get("overdue_fee_service"));
        Assert.assertEquals(new BigDecimal("111.00"), result.get("overdue_fee_other"));

    }

    @Test
    @Sql("classpath:/test/test4ExecuteDailyPlanRepaymentJob.sql")
    public void test_executeDailyPlanRepaymentJob_2() throws ParseException {
        String financialContractUuid = "d2812bc5-5057-4a91-b3fd-9019506f0499";
        Date doingDay = DateUtils.parseDate("2016-10-06", "yyyy-MM-dd");

        dailyPlanRepaymentService.executeDailyPlanRepaymentJob(financialContractUuid, doingDay);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("financialContractUuid", financialContractUuid);
        parameters.put("createDate", doingDay);

        String resultQuery = "select * from daily_plan_repayment "
                + "where financial_contract_uuid = :financialContractUuid"
                + " and create_date = :createDate"
                + " and plan_style = 2";

        List<Map<String, Object>> results = genericJdbcSupport.queryForList(resultQuery, parameters);

        Assert.assertEquals(1, results.size());

        Map<String, Object> result = results.get(0);

        Assert.assertEquals(new BigDecimal("477.57"), result.get("asset_principal_value"));
        Assert.assertEquals(new BigDecimal("54.99"), result.get("asset_interest_value"));
        Assert.assertEquals(new BigDecimal("0.00"), result.get("loan_service_fee"));
        Assert.assertEquals(new BigDecimal("0.00"), result.get("loan_tech_fee"));
        Assert.assertEquals(new BigDecimal("100.00"), result.get("loan_other_fee"));
        Assert.assertEquals(new BigDecimal("1.00"), result.get("overdue_fee_penalty"));
        Assert.assertEquals(new BigDecimal("2.00"), result.get("overdue_fee_obligation"));
        Assert.assertEquals(new BigDecimal("0.00"), result.get("overdue_fee_service"));
        Assert.assertEquals(new BigDecimal("0.00"), result.get("overdue_fee_other"));

    }

    @Test
    @Sql("classpath:/test/test4ExecuteDailyPlanRepaymentJob.sql")
    public void test_executeDailyPlanRepaymentJob_3() throws ParseException {
        String financialContractUuid = "d2812bc5-5057-4a91-b3fd-9019506f0499";
        Date doingDay = DateUtils.parseDate("2016-10-06", "yyyy-MM-dd");

        dailyPlanRepaymentService.executeDailyPlanRepaymentJob(financialContractUuid, doingDay);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("financialContractUuid", financialContractUuid);
        parameters.put("createDate", doingDay);

        String resultQuery = "select * from daily_plan_repayment "
                + "where financial_contract_uuid = :financialContractUuid"
                + " and create_date = :createDate"
                + " and plan_style = 3";

        List<Map<String, Object>> results = genericJdbcSupport.queryForList(resultQuery, parameters);

        Assert.assertEquals(1, results.size());

        Map<String, Object> result = results.get(0);

        Assert.assertEquals(new BigDecimal("37.63"), result.get("asset_principal_value"));
        Assert.assertEquals(new BigDecimal("9.16"), result.get("asset_interest_value"));
        Assert.assertEquals(new BigDecimal("0.00"), result.get("loan_service_fee"));
        Assert.assertEquals(new BigDecimal("0.00"), result.get("loan_tech_fee"));
        Assert.assertEquals(new BigDecimal("0.00"), result.get("loan_other_fee"));
        Assert.assertEquals(new BigDecimal("0.00"), result.get("overdue_fee_penalty"));
        Assert.assertEquals(new BigDecimal("0.00"), result.get("overdue_fee_obligation"));
        Assert.assertEquals(new BigDecimal("0.00"), result.get("overdue_fee_service"));
        Assert.assertEquals(new BigDecimal("111.00"), result.get("overdue_fee_other"));
    }
}
