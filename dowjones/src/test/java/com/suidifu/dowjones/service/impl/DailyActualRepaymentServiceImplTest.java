package com.suidifu.dowjones.service.impl;

import com.suidifu.dowjones.Dowjones;
import com.suidifu.dowjones.service.DailyActualRepaymentService;
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
public class DailyActualRepaymentServiceImplTest {
    @Autowired
    private DailyActualRepaymentService dailyActualRepaymentService;
    @Autowired
    private GenericJdbcSupport genericJdbcSupport;

    @Test
    @Sql("classpath:/test/test4ExecuteDailyActualRepaymentJob_onlineRepayment.sql")
    public void test_executeDailyActualRepaymentJob_onlineRepayment() throws ParseException {
        String financialContractUuid = "d2812bc5-5057-4a91-b3fd-9019506f0499";
        Date doingDay = DateUtils.parseDate("2017-05-16", "yyyy-MM-dd");

        dailyActualRepaymentService.executeDailyActualRepaymentJob(financialContractUuid, doingDay);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("financialContractUuid", financialContractUuid);
        parameters.put("createDate", doingDay);

        String resultQuery = "select * from daily_actual_repayment "
                + "where financial_contract_uuid = :financialContractUuid"
                + " and create_date = :createDate and business_type = 0";

        List<Map<String, Object>> results = genericJdbcSupport.queryForList(resultQuery, parameters);

        Assert.assertEquals(2, results.size());

        Map<String, Object> result = results.get(0);

        Assert.assertEquals(2, result.get("count"));
        Assert.assertEquals(new BigDecimal("1056.00"), result.get("loan_asset_principal"));
        Assert.assertEquals(new BigDecimal("0.00"), result.get("loan_asset_interest"));
        Assert.assertEquals(new BigDecimal("0.00"), result.get("loan_service_fee"));
        Assert.assertEquals(new BigDecimal("994.00"), result.get("loan_tech_fee"));
        Assert.assertEquals(new BigDecimal("12.00"), result.get("loan_other_fee"));
        Assert.assertEquals(new BigDecimal("464.00"), result.get("overdue_fee_penalty"));
        Assert.assertEquals(new BigDecimal("621.00"), result.get("overdue_fee_obligation"));
        Assert.assertEquals(new BigDecimal("0.00"), result.get("overdue_fee_service"));
        Assert.assertEquals(new BigDecimal("0.00"), result.get("overdue_fee_other"));

        Map<String, Object> result1 = results.get(1);

        Assert.assertEquals(1, result1.get("count"));
        Assert.assertEquals(new BigDecimal("999.00"), result1.get("loan_asset_principal"));
        Assert.assertEquals(new BigDecimal("1000.00"), result1.get("loan_asset_interest"));
        Assert.assertEquals(new BigDecimal("190.00"), result1.get("loan_service_fee"));
        Assert.assertEquals(new BigDecimal("0.00"), result1.get("loan_tech_fee"));
        Assert.assertEquals(new BigDecimal("0.00"), result1.get("loan_other_fee"));
        Assert.assertEquals(new BigDecimal("0.00"), result1.get("overdue_fee_penalty"));
        Assert.assertEquals(new BigDecimal("0.00"), result1.get("overdue_fee_obligation"));
        Assert.assertEquals(new BigDecimal("0.00"), result1.get("overdue_fee_service"));
        Assert.assertEquals(new BigDecimal("0.00"), result1.get("overdue_fee_other"));

    }

    @Test
    @Sql("classpath:/test/test4ExecuteDailyActualRepaymentJob_offlineRepayment.sql")
    public void test_executeDailyActualRepaymentJob_offlineRepayment() throws ParseException {
        String financialContractUuid = "d2812bc5-5057-4a91-b3fd-9019506f0499";
        Date doingDay = DateUtils.parseDate("2017-05-16", "yyyy-MM-dd");

        dailyActualRepaymentService.executeDailyActualRepaymentJob(financialContractUuid, doingDay);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("financialContractUuid", financialContractUuid);
        parameters.put("createDate", doingDay);

        String resultQuery = "select * from daily_actual_repayment "
                + "where financial_contract_uuid = :financialContractUuid"
                + " and create_date = :createDate and business_type = 1 order by count desc";

        List<Map<String, Object>> results = genericJdbcSupport.queryForList(resultQuery, parameters);

        Assert.assertEquals(2, results.size());

        Map<String, Object> result = results.get(0);

        Assert.assertEquals(2, result.get("count"));
        Assert.assertEquals(new BigDecimal("1056.00"), result.get("loan_asset_principal"));
        Assert.assertEquals(new BigDecimal("0.00"), result.get("loan_asset_interest"));
        Assert.assertEquals(new BigDecimal("0.00"), result.get("loan_service_fee"));
        Assert.assertEquals(new BigDecimal("994.00"), result.get("loan_tech_fee"));
        Assert.assertEquals(new BigDecimal("12.00"), result.get("loan_other_fee"));
        Assert.assertEquals(new BigDecimal("464.00"), result.get("overdue_fee_penalty"));
        Assert.assertEquals(new BigDecimal("621.00"), result.get("overdue_fee_obligation"));
        Assert.assertEquals(new BigDecimal("0.00"), result.get("overdue_fee_service"));
        Assert.assertEquals(new BigDecimal("0.00"), result.get("overdue_fee_other"));

        Map<String, Object> result1 = results.get(1);

        Assert.assertEquals(1, result1.get("count"));
        Assert.assertEquals(new BigDecimal("999.00"), result1.get("loan_asset_principal"));
        Assert.assertEquals(new BigDecimal("1000.00"), result1.get("loan_asset_interest"));
        Assert.assertEquals(new BigDecimal("190.00"), result1.get("loan_service_fee"));
        Assert.assertEquals(new BigDecimal("0.00"), result1.get("loan_tech_fee"));
        Assert.assertEquals(new BigDecimal("0.00"), result1.get("loan_other_fee"));
        Assert.assertEquals(new BigDecimal("0.00"), result1.get("overdue_fee_penalty"));
        Assert.assertEquals(new BigDecimal("0.00"), result1.get("overdue_fee_obligation"));
        Assert.assertEquals(new BigDecimal("0.00"), result1.get("overdue_fee_service"));
        Assert.assertEquals(new BigDecimal("0.00"), result1.get("overdue_fee_other"));

    }

    @Test
    @Sql("classpath:/test/test4ExecuteDailyActualRepaymentJob_offlinePayment.sql")
    public void test_executeDailyActualRepaymentJob_offlinePayment() throws ParseException {
        String financialContractUuid = "d2812bc5-5057-4a91-b3fd-9019506f0499";
        Date doingDay = DateUtils.parseDate("2017-05-16", "yyyy-MM-dd");

        dailyActualRepaymentService.executeDailyActualRepaymentJob(financialContractUuid, doingDay);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("financialContractUuid", financialContractUuid);
        parameters.put("createDate", doingDay);

        String resultQuery = "select * from daily_actual_repayment "
                + "where financial_contract_uuid = :financialContractUuid"
                + " and create_date = :createDate and business_type = 2";

        List<Map<String, Object>> results = genericJdbcSupport.queryForList(resultQuery, parameters);

        Assert.assertEquals(1, results.size());

        Map<String, Object> result = results.get(0);

        Assert.assertEquals(3, result.get("count"));
        Assert.assertEquals(new BigDecimal("2055.00"), result.get("loan_asset_principal"));
        Assert.assertEquals(new BigDecimal("1000.00"), result.get("loan_asset_interest"));
        Assert.assertEquals(new BigDecimal("190.00"), result.get("loan_service_fee"));
        Assert.assertEquals(new BigDecimal("994.00"), result.get("loan_tech_fee"));
        Assert.assertEquals(new BigDecimal("12.00"), result.get("loan_other_fee"));
        Assert.assertEquals(new BigDecimal("464.00"), result.get("overdue_fee_penalty"));
        Assert.assertEquals(new BigDecimal("621.00"), result.get("overdue_fee_obligation"));
        Assert.assertEquals(new BigDecimal("0.00"), result.get("overdue_fee_service"));
        Assert.assertEquals(new BigDecimal("0.00"), result.get("overdue_fee_other"));
    }

    @Test
    @Sql("classpath:/test/test4ExecuteDailyActualRepaymentJob_prePayment.sql")
    public void test_executeDailyActualRepaymentJob_prePayment() throws ParseException {
        String financialContractUuid = "d2812bc5-5057-4a91-b3fd-9019506f0499";
        Date doingDay = DateUtils.parseDate("2017-05-16", "yyyy-MM-dd");

        dailyActualRepaymentService.executeDailyActualRepaymentJob(financialContractUuid, doingDay);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("financialContractUuid", financialContractUuid);
        parameters.put("createDate", doingDay);

        String resultQuery = "select * from daily_pre_repayment "
                + "where financial_contract_uuid = :financialContractUuid"
                + " and create_date = :createDate";

        List<Map<String, Object>> results = genericJdbcSupport.queryForList(resultQuery, parameters);

        Assert.assertEquals(1, results.size());

        Map<String, Object> result = results.get(0);

        Assert.assertEquals(2, result.get("count"));
        Assert.assertEquals(new BigDecimal("1056.00"), result.get("loan_asset_principal"));
        Assert.assertEquals(new BigDecimal("0.00"), result.get("loan_asset_interest"));
        Assert.assertEquals(new BigDecimal("0.00"), result.get("loan_service_fee"));
        Assert.assertEquals(new BigDecimal("994.00"), result.get("loan_tech_fee"));
        Assert.assertEquals(new BigDecimal("12.00"), result.get("loan_other_fee"));
        Assert.assertEquals(new BigDecimal("464.00"), result.get("overdue_fee_penalty"));
        Assert.assertEquals(new BigDecimal("621.00"), result.get("overdue_fee_obligation"));
        Assert.assertEquals(new BigDecimal("0.00"), result.get("overdue_fee_service"));
        Assert.assertEquals(new BigDecimal("0.00"), result.get("overdue_fee_other"));
    }

    @Test
    @Sql("classpath:/test/test4ExecuteDailyActualRepaymentJob_prePayment.sql")
    public void test_executeDailyActualRepaymentJob_partPayment() throws ParseException {
        String financialContractUuid = "d2812bc5-5057-4a91-b3fd-9019506f0499";
        Date doingDay = DateUtils.parseDate("2017-05-16", "yyyy-MM-dd");

        dailyActualRepaymentService.executeDailyActualRepaymentJob(financialContractUuid, doingDay);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("financialContractUuid", financialContractUuid);
        parameters.put("createDate", doingDay);

        String resultQuery = "select * from daily_part_repayment "
                + "where financial_contract_uuid = :financialContractUuid"
                + " and create_date = :createDate";

        List<Map<String, Object>> results = genericJdbcSupport.queryForList(resultQuery, parameters);

        Assert.assertEquals(1, results.size());

        Map<String, Object> result = results.get(0);

        Assert.assertEquals(1, result.get("count"));
        Assert.assertEquals(new BigDecimal("999.00"), result.get("loan_asset_principal"));
        Assert.assertEquals(new BigDecimal("1000.00"), result.get("loan_asset_interest"));
        Assert.assertEquals(new BigDecimal("190.00"), result.get("loan_service_fee"));
        Assert.assertEquals(new BigDecimal("0.00"), result.get("loan_tech_fee"));
        Assert.assertEquals(new BigDecimal("0.00"), result.get("loan_other_fee"));
        Assert.assertEquals(new BigDecimal("0.00"), result.get("overdue_fee_penalty"));
        Assert.assertEquals(new BigDecimal("0.00"), result.get("overdue_fee_obligation"));
        Assert.assertEquals(new BigDecimal("0.00"), result.get("overdue_fee_service"));
        Assert.assertEquals(new BigDecimal("0.00"), result.get("overdue_fee_other"));
    }
}
