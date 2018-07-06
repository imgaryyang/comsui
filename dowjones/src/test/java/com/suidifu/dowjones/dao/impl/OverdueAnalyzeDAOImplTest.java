package com.suidifu.dowjones.dao.impl;

import com.suidifu.dowjones.Dowjones;
import com.suidifu.dowjones.dao.OverdueAnalyzeDAO;
import com.suidifu.dowjones.model.StatisticsReport;
import com.suidifu.dowjones.utils.Constants;
import com.suidifu.dowjones.vo.enumeration.RepurchaseMode;
import com.suidifu.dowjones.vo.request.InputParameter;
import com.suidifu.dowjones.vo.request.StaticOverdueRateInputParameter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

import static org.apache.spark.sql.functions.sum;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.Assert.assertThat;

/**
 * @author: frankwoo(吴峻申) <br>
 * @date: 2017/11/22 <br>
 * @time: 下午7:59 <br>
 * @mail: frank_wjs@hotmail.com <br>
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Dowjones.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(profiles = "test")
@Slf4j
public class OverdueAnalyzeDAOImplTest {
    @Resource
    private OverdueAnalyzeDAO overdueAnalyzeDAO;

    private StatisticsReport statisticsReport;
    private String financialContractUuid;
    private Map<String, String> contractUuidsMap;

    @Before
    public void setUp() {
        statisticsReport = new StatisticsReport();
        statisticsReport.setStatisticsDate(DateFormatUtils.format(new Date(), Constants.DATE_PATTERN));
        statisticsReport.setDenominatorDesc("逾期未偿本金");
        statisticsReport.setNumeratorDesc("剩余贷款本金");
        statisticsReport.setQuotientDesc("动态池逾期率(剩余本金)");
        statisticsReport.setDenominator("10.0");
        statisticsReport.setNumerator("10.0");
        statisticsReport.setQuotient(new BigDecimal("10.0").toPlainString() + "%");
        statisticsReport.setComputeType(0);

        contractUuidsMap = new HashMap<>();
        contractUuidsMap.put("0", "008634e0-9ec1-4272-ba4d-e7458fabe5b7");
        contractUuidsMap.put("1", "0199d0e9-c03e-4b32-a7bd-957915dc84f2");
        contractUuidsMap.put("2", "0070d689-0037-4f4a-8e07-10697627b3f0");
        contractUuidsMap.put("3", "0032bbf7-90ad-44ab-bc71-fda4c75d7a79");
        contractUuidsMap.put("4", "00a566c8-d2e9-41dd-b2fb-f482b777a166");
        contractUuidsMap.put("5", "00d39ba2-5a55-46f6-b047-0fda11097b46");
        contractUuidsMap.put("6", "00b38d38-25c8-41fc-a8c0-d209ec9b2867");
        contractUuidsMap.put("7", "017ce1e5-dee7-47d9-865a-dd0388b5b170");
        contractUuidsMap.put("8", "04e2791b-580a-489c-8a88-4aa6763ce2cd");
        contractUuidsMap.put("9", "01d21596-a159-44f3-91f6-b55e84baa9ee");
        contractUuidsMap.put("10", "0281677a-a466-43cb-9c24-05dac233fa0c");
        contractUuidsMap.put("11", "05139967-10a6-4c45-a975-8f8a59a6e592");
        contractUuidsMap.put("12", "020bd67e-463c-49ab-8534-e5a60f436668");
        contractUuidsMap.put("13", "03d67b4d-06d9-4f35-bcec-a6d299836346");
        contractUuidsMap.put("14", "0502cc08-9fe1-4e62-b6a7-738f0f5da653");
        contractUuidsMap.put("15", "0432c7c0-46d6-42a5-8be6-6c0d6bad65a9");
        contractUuidsMap.put("16", "1319235f-3a5f-481a-986c-a3c8cea75a0c");
        contractUuidsMap.put("17", "495abe68-3b10-4ea0-88c9-4297d3e88ef0");
        contractUuidsMap.put("18", "9b1b181c-59bf-486e-85bf-b279ac83078b");
        contractUuidsMap.put("19", "6e0bc421-f604-4424-83dc-a7bb47d27fca");

        financialContractUuid = "d2812bc5-5057-4a91-b3fd-9019506f0499";
    }

    @After
    public void tearDown() {
        statisticsReport = null;
        contractUuidsMap = null;
        financialContractUuid = null;
    }

    private InputParameter buildInputParameter(
            String financialContractUuid, String overdueStage,
            boolean includeUnconfirmed, boolean includeRepurchase,
            int periodDays) {
        InputParameter inputParameter = new InputParameter();
        inputParameter.setFinancialContractUuid(financialContractUuid);
        inputParameter.setOverdueStage(overdueStage);
        inputParameter.setIncludeUnconfirmed(includeUnconfirmed);
        inputParameter.setIncludeRepurchase(includeRepurchase);
        inputParameter.setPeriodDays(periodDays);
        return inputParameter;
    }

    private StaticOverdueRateInputParameter buildStaticOverdueRateInputParameter(
            String financialContractUuid, String overdueStage,
            boolean includeUnconfirmed, boolean includeRepurchase,
            int periodDays, int year, int month) {
        StaticOverdueRateInputParameter inputParameter = new StaticOverdueRateInputParameter();
        inputParameter.setFinancialContractUuid(financialContractUuid);
        inputParameter.setOverdueStage(overdueStage);
        inputParameter.setIncludeUnconfirmed(includeUnconfirmed);
        inputParameter.setIncludeRepurchase(includeRepurchase);
        inputParameter.setPeriodDays(periodDays);
        inputParameter.setYear(year);
        inputParameter.setMonth(month);
        return inputParameter;
    }

    @Test
    public void saveData() throws IOException {
        Long startTime = new Date().getTime();
        overdueAnalyzeDAO.saveData(statisticsReport);
        Long endTime = new Date().getTime();

        log.info("\n\n\n\n\nduration is :{}ms\n\n\n\n\n", endTime - startTime);
    }

    @Test
    public void loadAllStatisticsReport() {
        Long startTime = new Date().getTime();
        List<StatisticsReport> reports = overdueAnalyzeDAO.loadAllStatisticsReport
                (statisticsReport);
        Long endTime = new Date().getTime();

        log.info("\n\n\n\n\nduration is :{}ms\n\n\n\n\n", endTime - startTime);

        for (StatisticsReport statisticsReport : reports) {
            log.info("分子:{}", statisticsReport.getDenominator());
            log.info("分母:{}", statisticsReport.getNumerator());
            log.info("商数(以%表示):{}", statisticsReport.getQuotient());
            log.info("分子描述:{}", statisticsReport.getDenominatorDesc());
            log.info("分母描述:{}", statisticsReport.getNumeratorDesc());
            log.info("商描述:{}", statisticsReport.getQuotientDesc());
            log.info("日期:{}", statisticsReport.getStatisticsDate());
            log.info("计算类型:{}", statisticsReport.getComputeType());
            log.info("公式id:{}", statisticsReport.getFormulaId());
            log.info("笔数:{}", statisticsReport.getStatisticsNumber());
        }
    }

    //M1 逾期30天以内的还款计划
    //periodDays:30目前按照30天间隔来设定
    //includeUnconfirmed逾期状态:待确认是否计入逾期(true or false)
    //includeRepurchase回购状态:已回购是否计入已还(true or false)
    @Test
    public void getContractUuidsByOverdueAssetSet_M1() {
        InputParameter inputParameter = buildInputParameter(financialContractUuid, "0", true, true, 30);
        String[] uuids = overdueAnalyzeDAO.getContractUuidsByOverdueAssetSet(inputParameter, RepurchaseMode.M1);
        log.info("size is:{}", uuids.length);
        for (String uuid : uuids) {
            log.info("uuid is:{}", uuid);
        }
        assertThat(uuids.length, equalTo(2));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("0")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("4")));

        inputParameter = buildInputParameter(financialContractUuid, "0", true, false, 30);
        uuids = overdueAnalyzeDAO.getContractUuidsByOverdueAssetSet(inputParameter, RepurchaseMode.M1);
        log.info("size is:{}", uuids.length);
        for (String uuid : uuids) {
            log.info("uuid is:{}", uuid);
        }
        assertThat(uuids.length, equalTo(2));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("0")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("4")));

        inputParameter = buildInputParameter(financialContractUuid, "0", false, true, 30);
        uuids = overdueAnalyzeDAO.getContractUuidsByOverdueAssetSet(inputParameter, RepurchaseMode.M1);
        log.info("size is:{}", uuids.length);
        for (String uuid : uuids) {
            log.info("uuid is:{}", uuid);
        }
        assertThat(uuids.length, equalTo(2));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("0")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("4")));
    }

    //M2 逾期30-60天的还款计划
    //periodDays:30目前按照30天间隔来设定
    //includeUnconfirmed逾期状态:待确认是否计入逾期(true or false)
    //includeRepurchase回购状态:已回购是否计入已还(true or false)
    @Test
    public void getContractUuidsByOverdueAssetSet_M2() {
        InputParameter inputParameter = buildInputParameter(financialContractUuid, "1", true, true, 30);
        String[] uuids = overdueAnalyzeDAO.getContractUuidsByOverdueAssetSet(inputParameter, RepurchaseMode.M1);
        log.info("size is:{}", uuids.length);
        for (String uuid : uuids) {
            log.info("uuid is:{}", uuid);
        }
        assertThat(uuids.length, equalTo(2));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("1")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("5")));

        inputParameter = buildInputParameter(financialContractUuid, "1", true, false, 30);
        uuids = overdueAnalyzeDAO.getContractUuidsByOverdueAssetSet(inputParameter, RepurchaseMode.M1);
        log.info("size is:{}", uuids.length);
        for (String uuid : uuids) {
            log.info("uuid is:{}", uuid);
        }
        assertThat(uuids.length, equalTo(2));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("1")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("5")));

        inputParameter = buildInputParameter(financialContractUuid, "1", false, true, 30);
        uuids = overdueAnalyzeDAO.getContractUuidsByOverdueAssetSet(inputParameter, RepurchaseMode.M1);
        log.info("size is:{}", uuids.length);
        for (String uuid : uuids) {
            log.info("uuid is:{}", uuid);
        }
        assertThat(uuids.length, equalTo(2));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("1")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("5")));

        inputParameter = buildInputParameter(financialContractUuid, "1", false, false, 30);
        uuids = overdueAnalyzeDAO.getContractUuidsByOverdueAssetSet(inputParameter, RepurchaseMode.M1);
        log.info("size is:{}", uuids.length);
        for (String uuid : uuids) {
            log.info("uuid is:{}", uuid);
        }
        assertThat(uuids.length, equalTo(2));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("1")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("5")));
    }

    //M3 逾期60-90天的还款计划
    //periodDays:30目前按照30天间隔来设定
    //includeUnconfirmed逾期状态:待确认是否计入逾期(true or false)
    //includeRepurchase回购状态:已回购是否计入已还(true or false)
    @Test
    public void getContractUuidsByOverdueAssetSet_M3() {
        InputParameter inputParameter = buildInputParameter(financialContractUuid, "2", true, true, 30);
        String[] uuids = overdueAnalyzeDAO.getContractUuidsByOverdueAssetSet(inputParameter, RepurchaseMode.M1);
        log.info("size is:{}", uuids.length);
        for (String uuid : uuids) {
            log.info("uuid is:{}", uuid);
        }
        assertThat(uuids.length, equalTo(2));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("2")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("6")));

        inputParameter = buildInputParameter(financialContractUuid, "2", true, false, 30);
        uuids = overdueAnalyzeDAO.getContractUuidsByOverdueAssetSet(inputParameter, RepurchaseMode.M1);
        log.info("size is:{}", uuids.length);
        for (String uuid : uuids) {
            log.info("uuid is:{}", uuid);
        }
        assertThat(uuids.length, equalTo(2));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("2")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("6")));

        inputParameter = buildInputParameter(financialContractUuid, "2", false, true, 30);
        uuids = overdueAnalyzeDAO.getContractUuidsByOverdueAssetSet(inputParameter, RepurchaseMode.M1);
        log.info("size is:{}", uuids.length);
        for (String uuid : uuids) {
            log.info("uuid is:{}", uuid);
        }
        assertThat(uuids.length, equalTo(2));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("2")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("6")));

        inputParameter = buildInputParameter(financialContractUuid, "2", false, false, 30);
        uuids = overdueAnalyzeDAO.getContractUuidsByOverdueAssetSet(inputParameter, RepurchaseMode.M1);
        log.info("size is:{}", uuids.length);
        for (String uuid : uuids) {
            log.info("uuid is:{}", uuid);
        }
        assertThat(uuids.length, equalTo(2));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("2")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("6")));
    }

    //M3+ 逾期60-90天的还款计划
    //periodDays:30目前按照30天间隔来设定
    //includeUnconfirmed逾期状态:待确认是否计入逾期(true or false)
    //includeRepurchase回购状态:已回购是否计入已还(true or false)
    @Test
    public void getContractUuidsByOverdueAssetSet_M3PLUS() {
        InputParameter inputParameter = buildInputParameter(financialContractUuid, "3", true, true, 30);
        String[] uuids = overdueAnalyzeDAO.getContractUuidsByOverdueAssetSet(inputParameter, RepurchaseMode.M1);
        log.info("size is:{}", uuids.length);
        for (String uuid : uuids) {
            log.info("uuid is:{}", uuid);
        }
        assertThat(uuids.length, equalTo(2));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("3")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("7")));

        inputParameter = buildInputParameter(financialContractUuid, "3", true, false, 30);
        uuids = overdueAnalyzeDAO.getContractUuidsByOverdueAssetSet(inputParameter, RepurchaseMode.M1);
        log.info("size is:{}", uuids.length);
        for (String uuid : uuids) {
            log.info("uuid is:{}", uuid);
        }
        assertThat(uuids.length, equalTo(2));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("3")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("7")));

        inputParameter = buildInputParameter(financialContractUuid, "3", false, true, 30);
        uuids = overdueAnalyzeDAO.getContractUuidsByOverdueAssetSet(inputParameter, RepurchaseMode.M1);
        log.info("size is:{}", uuids.length);
        for (String uuid : uuids) {
            log.info("uuid is:{}", uuid);
        }
        assertThat(uuids.length, equalTo(2));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("3")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("7")));

        inputParameter = buildInputParameter(financialContractUuid, "3", false, false, 30);
        uuids = overdueAnalyzeDAO.getContractUuidsByOverdueAssetSet(inputParameter, RepurchaseMode.M1);
        log.info("size is:{}", uuids.length);
        for (String uuid : uuids) {
            log.info("uuid is:{}", uuid);
        }
        assertThat(uuids.length, equalTo(2));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("3")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("7")));
    }

    //逾期阶段可多选，所以是M1,M2,M3,4种逾期阶段进行排列组合
    //M1:"0" M2:"1" M3:"2" M3+:"3"
    //periodDays:30目前按照30天间隔来设定
    //includeUnconfirmed逾期状态:待确认是否计入逾期(true or false)
    //includeRepurchase回购状态:已回购是否计入已还(true or false)
    @Test
    public void getContractUuidsByOverdueAssetSet_OverdueStage_Combination() {
        InputParameter inputParameter = buildInputParameter(financialContractUuid, "0,1", true, true, 30);
        String[] uuids = overdueAnalyzeDAO.getContractUuidsByOverdueAssetSet(inputParameter, RepurchaseMode.M1);
        log.info("size is:{}", uuids.length);
        for (String uuid : uuids) {
            log.info("uuid is:{}", uuid);
        }
        assertThat(uuids.length, equalTo(4));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("0")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("1")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("4")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("5")));

        inputParameter = buildInputParameter(financialContractUuid, "0,1", true, false, 30);
        uuids = overdueAnalyzeDAO.getContractUuidsByOverdueAssetSet(inputParameter, RepurchaseMode.M1);
        log.info("size is:{}", uuids.length);
        for (String uuid : uuids) {
            log.info("uuid is:{}", uuid);
        }
        assertThat(uuids.length, equalTo(4));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("0")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("1")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("4")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("5")));

        inputParameter = buildInputParameter(financialContractUuid, "0,1", false, true, 30);
        uuids = overdueAnalyzeDAO.getContractUuidsByOverdueAssetSet(inputParameter, RepurchaseMode.M1);
        log.info("size is:{}", uuids.length);
        for (String uuid : uuids) {
            log.info("uuid is:{}", uuid);
        }
        assertThat(uuids.length, equalTo(4));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("0")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("1")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("4")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("5")));

        inputParameter = buildInputParameter(financialContractUuid, "0,1", false, false, 30);
        uuids = overdueAnalyzeDAO.getContractUuidsByOverdueAssetSet(inputParameter, RepurchaseMode.M1);
        log.info("size is:{}", uuids.length);
        for (String uuid : uuids) {
            log.info("uuid is:{}", uuid);
        }
        assertThat(uuids.length, equalTo(4));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("0")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("1")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("4")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("5")));

        inputParameter = buildInputParameter(financialContractUuid, "0,2", true, true, 30);
        uuids = overdueAnalyzeDAO.getContractUuidsByOverdueAssetSet(inputParameter, RepurchaseMode.M1);
        log.info("size is:{}", uuids.length);
        for (String uuid : uuids) {
            log.info("uuid is:{}", uuid);
        }
        assertThat(uuids.length, equalTo(4));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("0")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("2")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("4")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("6")));

        inputParameter = buildInputParameter(financialContractUuid, "0,2", true, false, 30);
        uuids = overdueAnalyzeDAO.getContractUuidsByOverdueAssetSet(inputParameter, RepurchaseMode.M1);
        log.info("size is:{}", uuids.length);
        for (String uuid : uuids) {
            log.info("uuid is:{}", uuid);
        }
        assertThat(uuids.length, equalTo(4));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("0")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("2")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("4")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("6")));

        inputParameter = buildInputParameter(financialContractUuid, "0,2", false, true, 30);
        uuids = overdueAnalyzeDAO.getContractUuidsByOverdueAssetSet(inputParameter, RepurchaseMode.M1);
        log.info("size is:{}", uuids.length);
        for (String uuid : uuids) {
            log.info("uuid is:{}", uuid);
        }
        assertThat(uuids.length, equalTo(4));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("0")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("2")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("4")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("6")));

        inputParameter = buildInputParameter(financialContractUuid, "0,2", false, false, 30);
        uuids = overdueAnalyzeDAO.getContractUuidsByOverdueAssetSet(inputParameter, RepurchaseMode.M1);
        log.info("size is:{}", uuids.length);
        for (String uuid : uuids) {
            log.info("uuid is:{}", uuid);
        }
        assertThat(uuids.length, equalTo(4));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("0")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("2")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("4")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("6")));

        inputParameter = buildInputParameter(financialContractUuid, "0,3", true, true, 30);
        uuids = overdueAnalyzeDAO.getContractUuidsByOverdueAssetSet(inputParameter, RepurchaseMode.M1);
        log.info("size is:{}", uuids.length);
        for (String uuid : uuids) {
            log.info("uuid is:{}", uuid);
        }
        assertThat(uuids.length, equalTo(4));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("0")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("3")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("4")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("7")));

        inputParameter = buildInputParameter(financialContractUuid, "0,3", true, false, 30);
        uuids = overdueAnalyzeDAO.getContractUuidsByOverdueAssetSet(inputParameter, RepurchaseMode.M1);
        log.info("size is:{}", uuids.length);
        for (String uuid : uuids) {
            log.info("uuid is:{}", uuid);
        }
        assertThat(uuids.length, equalTo(4));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("0")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("3")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("4")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("7")));

        inputParameter = buildInputParameter(financialContractUuid, "0,3", false, true, 30);
        uuids = overdueAnalyzeDAO.getContractUuidsByOverdueAssetSet(inputParameter, RepurchaseMode.M1);
        log.info("size is:{}", uuids.length);
        for (String uuid : uuids) {
            log.info("uuid is:{}", uuid);
        }
        assertThat(uuids.length, equalTo(4));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("0")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("3")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("4")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("7")));

        inputParameter = buildInputParameter(financialContractUuid, "0,3", false, false, 30);
        uuids = overdueAnalyzeDAO.getContractUuidsByOverdueAssetSet(inputParameter, RepurchaseMode.M1);
        log.info("size is:{}", uuids.length);
        for (String uuid : uuids) {
            log.info("uuid is:{}", uuid);
        }
        assertThat(uuids.length, equalTo(4));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("0")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("3")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("4")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("7")));

        inputParameter = buildInputParameter(financialContractUuid, "1,2", true, true, 30);
        uuids = overdueAnalyzeDAO.getContractUuidsByOverdueAssetSet(inputParameter, RepurchaseMode.M1);
        log.info("size is:{}", uuids.length);
        for (String uuid : uuids) {
            log.info("uuid is:{}", uuid);
        }
        assertThat(uuids.length, equalTo(4));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("1")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("2")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("5")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("6")));

        inputParameter = buildInputParameter(financialContractUuid, "1,2", true, false, 30);
        uuids = overdueAnalyzeDAO.getContractUuidsByOverdueAssetSet(inputParameter, RepurchaseMode.M1);
        log.info("size is:{}", uuids.length);
        for (String uuid : uuids) {
            log.info("uuid is:{}", uuid);
        }
        assertThat(uuids.length, equalTo(4));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("1")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("2")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("5")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("6")));

        inputParameter = buildInputParameter(financialContractUuid, "1,2", false, true, 30);
        uuids = overdueAnalyzeDAO.getContractUuidsByOverdueAssetSet(inputParameter, RepurchaseMode.M1);
        log.info("size is:{}", uuids.length);
        for (String uuid : uuids) {
            log.info("uuid is:{}", uuid);
        }
        assertThat(uuids.length, equalTo(4));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("1")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("2")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("5")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("6")));

        inputParameter = buildInputParameter(financialContractUuid, "1,2", false, false, 30);
        uuids = overdueAnalyzeDAO.getContractUuidsByOverdueAssetSet(inputParameter, RepurchaseMode.M1);
        log.info("size is:{}", uuids.length);
        for (String uuid : uuids) {
            log.info("uuid is:{}", uuid);
        }
        assertThat(uuids.length, equalTo(4));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("1")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("2")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("5")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("6")));

        inputParameter = buildInputParameter(financialContractUuid, "1,3", true, true, 30);
        uuids = overdueAnalyzeDAO.getContractUuidsByOverdueAssetSet(inputParameter, RepurchaseMode.M1);
        log.info("size is:{}", uuids.length);
        for (String uuid : uuids) {
            log.info("uuid is:{}", uuid);
        }
        assertThat(uuids.length, equalTo(4));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("1")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("3")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("5")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("7")));

        inputParameter = buildInputParameter(financialContractUuid, "1,3", true, false, 30);
        uuids = overdueAnalyzeDAO.getContractUuidsByOverdueAssetSet(inputParameter, RepurchaseMode.M1);
        log.info("size is:{}", uuids.length);
        for (String uuid : uuids) {
            log.info("uuid is:{}", uuid);
        }
        assertThat(uuids.length, equalTo(4));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("1")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("3")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("5")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("7")));

        inputParameter = buildInputParameter(financialContractUuid, "1,3", false, true, 30);
        uuids = overdueAnalyzeDAO.getContractUuidsByOverdueAssetSet(inputParameter, RepurchaseMode.M1);
        log.info("size is:{}", uuids.length);
        for (String uuid : uuids) {
            log.info("uuid is:{}", uuid);
        }
        assertThat(uuids.length, equalTo(4));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("1")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("3")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("5")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("7")));

        inputParameter = buildInputParameter(financialContractUuid, "1,3", false, false, 30);
        uuids = overdueAnalyzeDAO.getContractUuidsByOverdueAssetSet(inputParameter, RepurchaseMode.M1);
        log.info("size is:{}", uuids.length);
        for (String uuid : uuids) {
            log.info("uuid is:{}", uuid);
        }
        assertThat(uuids.length, equalTo(4));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("1")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("3")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("5")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("7")));

        inputParameter = buildInputParameter(financialContractUuid, "2,3", true, true, 30);
        uuids = overdueAnalyzeDAO.getContractUuidsByOverdueAssetSet(inputParameter, RepurchaseMode.M1);
        log.info("size is:{}", uuids.length);
        for (String uuid : uuids) {
            log.info("uuid is:{}", uuid);
        }
        assertThat(uuids.length, equalTo(4));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("2")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("3")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("6")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("7")));

        inputParameter = buildInputParameter(financialContractUuid, "2,3", true, false, 30);
        uuids = overdueAnalyzeDAO.getContractUuidsByOverdueAssetSet(inputParameter, RepurchaseMode.M1);
        log.info("size is:{}", uuids.length);
        for (String uuid : uuids) {
            log.info("uuid is:{}", uuid);
        }
        assertThat(uuids.length, equalTo(4));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("2")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("3")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("6")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("7")));

        inputParameter = buildInputParameter(financialContractUuid, "2,3", false, true, 30);
        uuids = overdueAnalyzeDAO.getContractUuidsByOverdueAssetSet(inputParameter, RepurchaseMode.M1);
        log.info("size is:{}", uuids.length);
        for (String uuid : uuids) {
            log.info("uuid is:{}", uuid);
        }
        assertThat(uuids.length, equalTo(4));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("2")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("3")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("6")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("7")));

        inputParameter = buildInputParameter(financialContractUuid, "2,3", false, false, 30);
        uuids = overdueAnalyzeDAO.getContractUuidsByOverdueAssetSet(inputParameter, RepurchaseMode.M1);
        log.info("size is:{}", uuids.length);
        for (String uuid : uuids) {
            log.info("uuid is:{}", uuid);
        }
        assertThat(uuids.length, equalTo(4));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("2")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("3")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("6")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("7")));

        inputParameter = buildInputParameter(financialContractUuid, "0,1,2", true, true, 30);
        uuids = overdueAnalyzeDAO.getContractUuidsByOverdueAssetSet(inputParameter, RepurchaseMode.M1);
        log.info("size is:{}", uuids.length);
        for (String uuid : uuids) {
            log.info("uuid is:{}", uuid);
        }
        assertThat(uuids.length, equalTo(6));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("0")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("1")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("2")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("4")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("5")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("6")));

        inputParameter = buildInputParameter(financialContractUuid, "0,1,2", true, false, 30);
        uuids = overdueAnalyzeDAO.getContractUuidsByOverdueAssetSet(inputParameter, RepurchaseMode.M1);
        log.info("size is:{}", uuids.length);
        for (String uuid : uuids) {
            log.info("uuid is:{}", uuid);
        }
        assertThat(uuids.length, equalTo(6));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("0")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("1")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("2")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("4")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("5")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("6")));

        inputParameter = buildInputParameter(financialContractUuid, "0,1,2", false, true, 30);
        uuids = overdueAnalyzeDAO.getContractUuidsByOverdueAssetSet(inputParameter, RepurchaseMode.M1);
        log.info("size is:{}", uuids.length);
        for (String uuid : uuids) {
            log.info("uuid is:{}", uuid);
        }
        assertThat(uuids.length, equalTo(6));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("0")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("1")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("2")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("4")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("5")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("6")));

        inputParameter = buildInputParameter(financialContractUuid, "0,1,2", false, false, 30);
        uuids = overdueAnalyzeDAO.getContractUuidsByOverdueAssetSet(inputParameter, RepurchaseMode.M1);
        log.info("size is:{}", uuids.length);
        for (String uuid : uuids) {
            log.info("uuid is:{}", uuid);
        }
        assertThat(uuids.length, equalTo(6));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("0")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("1")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("2")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("4")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("5")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("6")));

        inputParameter = buildInputParameter(financialContractUuid, "0,2,3", true, true, 30);
        uuids = overdueAnalyzeDAO.getContractUuidsByOverdueAssetSet(inputParameter, RepurchaseMode.M1);
        log.info("size is:{}", uuids.length);
        for (String uuid : uuids) {
            log.info("uuid is:{}", uuid);
        }
        assertThat(uuids.length, equalTo(6));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("0")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("2")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("3")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("4")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("6")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("7")));

        inputParameter = buildInputParameter(financialContractUuid, "0,2,3", true, false, 30);
        uuids = overdueAnalyzeDAO.getContractUuidsByOverdueAssetSet(inputParameter, RepurchaseMode.M1);
        log.info("size is:{}", uuids.length);
        for (String uuid : uuids) {
            log.info("uuid is:{}", uuid);
        }
        assertThat(uuids.length, equalTo(6));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("0")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("2")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("3")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("4")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("6")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("7")));

        inputParameter = buildInputParameter(financialContractUuid, "0,2,3", false, true, 30);
        uuids = overdueAnalyzeDAO.getContractUuidsByOverdueAssetSet(inputParameter, RepurchaseMode.M1);
        log.info("size is:{}", uuids.length);
        for (String uuid : uuids) {
            log.info("uuid is:{}", uuid);
        }
        assertThat(uuids.length, equalTo(6));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("0")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("2")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("3")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("4")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("6")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("7")));

        inputParameter = buildInputParameter(financialContractUuid, "0,2,3", false, false, 30);
        uuids = overdueAnalyzeDAO.getContractUuidsByOverdueAssetSet(inputParameter, RepurchaseMode.M1);
        log.info("size is:{}", uuids.length);
        for (String uuid : uuids) {
            log.info("uuid is:{}", uuid);
        }
        assertThat(uuids.length, equalTo(6));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("0")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("2")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("3")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("4")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("6")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("7")));

        inputParameter = buildInputParameter(financialContractUuid, "0,1,3", true, true, 30);
        uuids = overdueAnalyzeDAO.getContractUuidsByOverdueAssetSet(inputParameter, RepurchaseMode.M1);
        log.info("size is:{}", uuids.length);
        for (String uuid : uuids) {
            log.info("uuid is:{}", uuid);
        }
        assertThat(uuids.length, equalTo(6));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("0")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("1")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("3")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("4")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("5")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("7")));

        inputParameter = buildInputParameter(financialContractUuid, "0,1,3", true, false, 30);
        uuids = overdueAnalyzeDAO.getContractUuidsByOverdueAssetSet(inputParameter, RepurchaseMode.M1);
        log.info("size is:{}", uuids.length);
        for (String uuid : uuids) {
            log.info("uuid is:{}", uuid);
        }
        assertThat(uuids.length, equalTo(6));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("0")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("1")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("3")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("4")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("5")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("7")));

        inputParameter = buildInputParameter(financialContractUuid, "0,1,3", false, true, 30);
        uuids = overdueAnalyzeDAO.getContractUuidsByOverdueAssetSet(inputParameter, RepurchaseMode.M1);
        log.info("size is:{}", uuids.length);
        for (String uuid : uuids) {
            log.info("uuid is:{}", uuid);
        }
        assertThat(uuids.length, equalTo(6));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("0")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("1")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("3")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("4")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("5")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("7")));

        inputParameter = buildInputParameter(financialContractUuid, "0,1,3", false, false, 30);
        uuids = overdueAnalyzeDAO.getContractUuidsByOverdueAssetSet(inputParameter, RepurchaseMode.M1);
        log.info("size is:{}", uuids.length);
        for (String uuid : uuids) {
            log.info("uuid is:{}", uuid);
        }
        assertThat(uuids.length, equalTo(6));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("0")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("1")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("3")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("4")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("5")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("7")));

        inputParameter = buildInputParameter(financialContractUuid, "1,2,3", true, true, 30);
        uuids = overdueAnalyzeDAO.getContractUuidsByOverdueAssetSet(inputParameter, RepurchaseMode.M1);
        log.info("size is:{}", uuids.length);
        for (String uuid : uuids) {
            log.info("uuid is:{}", uuid);
        }
        assertThat(uuids.length, equalTo(6));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("1")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("2")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("3")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("5")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("6")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("7")));

        inputParameter = buildInputParameter(financialContractUuid, "1,2,3", true, false, 30);
        uuids = overdueAnalyzeDAO.getContractUuidsByOverdueAssetSet(inputParameter, RepurchaseMode.M1);
        log.info("size is:{}", uuids.length);
        for (String uuid : uuids) {
            log.info("uuid is:{}", uuid);
        }
        assertThat(uuids.length, equalTo(6));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("1")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("2")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("3")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("5")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("6")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("7")));

        inputParameter = buildInputParameter(financialContractUuid, "1,2,3", false, true, 30);
        uuids = overdueAnalyzeDAO.getContractUuidsByOverdueAssetSet(inputParameter, RepurchaseMode.M1);
        log.info("size is:{}", uuids.length);
        for (String uuid : uuids) {
            log.info("uuid is:{}", uuid);
        }
        assertThat(uuids.length, equalTo(6));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("1")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("2")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("3")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("5")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("6")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("7")));

        inputParameter = buildInputParameter(financialContractUuid, "1,2,3", false, false, 30);
        uuids = overdueAnalyzeDAO.getContractUuidsByOverdueAssetSet(inputParameter, RepurchaseMode.M1);
        log.info("size is:{}", uuids.length);
        for (String uuid : uuids) {
            log.info("uuid is:{}", uuid);
        }
        assertThat(uuids.length, equalTo(6));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("1")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("2")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("3")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("5")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("6")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("7")));

        inputParameter = buildInputParameter(financialContractUuid, "0,1,2,3", true, true, 30);
        uuids = overdueAnalyzeDAO.getContractUuidsByOverdueAssetSet(inputParameter, RepurchaseMode.M1);
        log.info("size is:{}", uuids.length);
        for (String uuid : uuids) {
            log.info("uuid is:{}", uuid);
        }
        assertThat(uuids.length, equalTo(8));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("0")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("1")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("2")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("3")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("4")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("5")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("6")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("7")));

        inputParameter = buildInputParameter(financialContractUuid, "0,1,2,3", true, false, 30);
        uuids = overdueAnalyzeDAO.getContractUuidsByOverdueAssetSet(inputParameter, RepurchaseMode.M1);
        log.info("size is:{}", uuids.length);
        for (String uuid : uuids) {
            log.info("uuid is:{}", uuid);
        }
        assertThat(uuids.length, equalTo(8));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("0")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("1")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("2")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("3")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("4")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("5")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("6")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("7")));

        inputParameter = buildInputParameter(financialContractUuid, "0,1,2,3", false, true, 30);
        uuids = overdueAnalyzeDAO.getContractUuidsByOverdueAssetSet(inputParameter, RepurchaseMode.M1);
        log.info("size is:{}", uuids.length);
        for (String uuid : uuids) {
            log.info("uuid is:{}", uuid);
        }
        assertThat(uuids.length, equalTo(8));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("0")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("1")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("2")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("3")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("4")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("5")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("6")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("7")));

        inputParameter = buildInputParameter(financialContractUuid, "0,1,2,3", false, false, 30);
        uuids = overdueAnalyzeDAO.getContractUuidsByOverdueAssetSet(inputParameter, RepurchaseMode.M1);
        log.info("size is:{}", uuids.length);
        for (String uuid : uuids) {
            log.info("uuid is:{}", uuid);
        }
        assertThat(uuids.length, equalTo(8));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("0")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("1")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("2")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("3")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("4")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("5")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("6")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("7")));
    }

    @Test
    public void getContractUuidsByUnConfirmedAssetSet1() {
        InputParameter inputParameter = buildInputParameter(financialContractUuid, "0", true, false, 30);
        Long startTime = new Date().getTime();
        Dataset<Row> df = overdueAnalyzeDAO.getContractUuidsByUnConfirmedAssetSet(inputParameter);
        Dataset<Row> df1 = overdueAnalyzeDAO.getUnEarnedPrincipalFromLedgerBookBy(inputParameter);
        df1 = df1.join(df, "contractUuid");
        log.info("=================after join=================");
        df1.show(false);
        df1 = df1.agg(sum("balance"));
        Long endTime = new Date().getTime();
        log.info("duration is :{}ms", endTime - startTime);
        log.info("df1 count is:{}", df1.count());
        df1.show(false);
    }

    //逾期未偿 已逾期_待确认
    @Test
    public void getContractUuidsByUnConfirmedAssetSet() {
        InputParameter inputParameter = buildInputParameter(financialContractUuid, "0", true, false, 30);
        String[] uuids = overdueAnalyzeDAO.getContractUuidsByUnConfirmedAssetSet(inputParameter, RepurchaseMode.M1);
        log.info("size is:{}", uuids.length);
        for (String uuid : uuids) {
            log.info("uuid is:{}", uuid);
        }
        assertThat(uuids.length, equalTo(2));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("8")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("12")));

        inputParameter = buildInputParameter(financialContractUuid, "1", true, true, 30);
        uuids = overdueAnalyzeDAO.getContractUuidsByUnConfirmedAssetSet(inputParameter, RepurchaseMode.M1);
        log.info("size is:{}", uuids.length);
        for (String uuid : uuids) {
            log.info("uuid is:{}", uuid);
        }
        assertThat(uuids.length, equalTo(2));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("9")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("13")));

        inputParameter = buildInputParameter(financialContractUuid, "2", true, true, 30);
        uuids = overdueAnalyzeDAO.getContractUuidsByUnConfirmedAssetSet(inputParameter, RepurchaseMode.M1);
        log.info("size is:{}", uuids.length);
        for (String uuid : uuids) {
            log.info("uuid is:{}", uuid);
        }
        assertThat(uuids.length, equalTo(2));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("10")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("14")));

        inputParameter = buildInputParameter(financialContractUuid, "3", true, true, 30);
        uuids = overdueAnalyzeDAO.getContractUuidsByUnConfirmedAssetSet(inputParameter, RepurchaseMode.M1);
        log.info("size is:{}", uuids.length);
        for (String uuid : uuids) {
            log.info("uuid is:{}", uuid);
        }
        assertThat(uuids.length, equalTo(2));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("11")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("15")));

        inputParameter = buildInputParameter(financialContractUuid, "0,1", true, true, 30);
        uuids = overdueAnalyzeDAO.getContractUuidsByUnConfirmedAssetSet(inputParameter, RepurchaseMode.M1);
        log.info("size is:{}", uuids.length);
        for (String uuid : uuids) {
            log.info("uuid is:{}", uuid);
        }
        assertThat(uuids.length, equalTo(4));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("8")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("9")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("12")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("13")));
        ////
        inputParameter = buildInputParameter(financialContractUuid, "1,2", true, true, 30);
        uuids = overdueAnalyzeDAO.getContractUuidsByUnConfirmedAssetSet(inputParameter, RepurchaseMode.M1);
        log.info("size is:{}", uuids.length);
        for (String uuid : uuids) {
            log.info("uuid is:{}", uuid);
        }
        assertThat(uuids.length, equalTo(4));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("9")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("10")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("13")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("14")));

        inputParameter = buildInputParameter(financialContractUuid, "2,3", true, true, 30);
        uuids = overdueAnalyzeDAO.getContractUuidsByUnConfirmedAssetSet(inputParameter, RepurchaseMode.M1);
        log.info("size is:{}", uuids.length);
        for (String uuid : uuids) {
            log.info("uuid is:{}", uuid);
        }
        assertThat(uuids.length, equalTo(4));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("10")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("11")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("14")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("15")));

        inputParameter = buildInputParameter(financialContractUuid, "0,1,2", true, true, 30);
        uuids = overdueAnalyzeDAO.getContractUuidsByUnConfirmedAssetSet(inputParameter, RepurchaseMode.M1);
        log.info("size is:{}", uuids.length);
        for (String uuid : uuids) {
            log.info("uuid is:{}", uuid);
        }
        assertThat(uuids.length, equalTo(6));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("8")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("9")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("10")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("12")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("13")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("14")));
        ////
        inputParameter = buildInputParameter(financialContractUuid, "1,2,3", true, true, 30);
        uuids = overdueAnalyzeDAO.getContractUuidsByUnConfirmedAssetSet(inputParameter, RepurchaseMode.M1);
        log.info("size is:{}", uuids.length);
        for (String uuid : uuids) {
            log.info("uuid is:{}", uuid);
        }
        assertThat(uuids.length, equalTo(6));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("9")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("10")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("11")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("13")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("14")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("15")));
        ////
        inputParameter = buildInputParameter(financialContractUuid, "0,1,2,3", true, true, 30);
        uuids = overdueAnalyzeDAO.getContractUuidsByUnConfirmedAssetSet(inputParameter, RepurchaseMode.M1);
        assertThat(uuids.length, equalTo(8));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("8")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("9")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("10")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("11")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("12")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("13")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("14")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("15")));
    }

    //剩余贷款
    @Test
    public void getUnclearContractUuidsBy() {
        StaticOverdueRateInputParameter inputParameter = buildStaticOverdueRateInputParameter(
                financialContractUuid, "0", false, true, 30, 2017, 11);
        String[] uuids = overdueAnalyzeDAO.getUnclearContractUuidsBy(inputParameter, RepurchaseMode.M1);
        //log.info("size is:{}", uuids.length);
        //for (String uuid : uuids) {
        //    log.info("uuid is:{}", uuid);
        //}
        assertThat(uuids.length, equalTo(20));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("0")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("1")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("2")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("3")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("4")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("5")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("6")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("7")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("8")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("9")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("10")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("11")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("12")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("13")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("14")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("15")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("16")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("17")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("18")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("19")));
    }

    //X月放款本金
    @Test
    public void getRemittanceTotalAmountByMonth() {
        StaticOverdueRateInputParameter inputParameter = buildStaticOverdueRateInputParameter(
                financialContractUuid, "0", false, true, 30, 2017, 11);
        BigDecimal aBigDecimal = overdueAnalyzeDAO.getRemittanceTotalAmountByMonth(inputParameter);
        assertThat(aBigDecimal, equalTo(BigDecimal.ZERO));

        inputParameter = buildStaticOverdueRateInputParameter(financialContractUuid, "0", false, true, 30, 2017, 5);
        aBigDecimal = overdueAnalyzeDAO.getRemittanceTotalAmountByMonth(inputParameter);
        assertThat(aBigDecimal, equalTo(new BigDecimal("96.00")));
    }

    //X月发放贷款逾期
    @Test
    public void getOverdueLoanPrincipalByMonthAndOverdueAssetSet() {
        StaticOverdueRateInputParameter inputParameter = buildStaticOverdueRateInputParameter(
                financialContractUuid, "0", false, true, 30, 2017, 5);
        String[] uuids = overdueAnalyzeDAO.getOverdueLoanPrincipalByMonthAndOverdueAssetSet(inputParameter, RepurchaseMode.M1);
        log.info("size is:{}", uuids.length);
        for (String uuid : uuids) {
            log.info("uuid is:{}", uuid);
        }
        assertThat(uuids.length, equalTo(2));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("0")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("4")));

        inputParameter = buildStaticOverdueRateInputParameter(financialContractUuid, "1", false, true, 30,
                2017, 5);
        uuids = overdueAnalyzeDAO.getOverdueLoanPrincipalByMonthAndOverdueAssetSet(inputParameter, RepurchaseMode.M1);
        log.info("size is:{}", uuids.length);
        for (String uuid : uuids) {
            log.info("uuid is:{}", uuid);
        }
        assertThat(uuids.length, equalTo(2));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("1")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("5")));

        inputParameter = buildStaticOverdueRateInputParameter(financialContractUuid, "2", false, true, 30,
                2017, 5);
        uuids = overdueAnalyzeDAO.getOverdueLoanPrincipalByMonthAndOverdueAssetSet(inputParameter, RepurchaseMode.M1);
        log.info("size is:{}", uuids.length);
        for (String uuid : uuids) {
            log.info("uuid is:{}", uuid);
        }
        assertThat(uuids.length, equalTo(2));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("2")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("6")));

        inputParameter = buildStaticOverdueRateInputParameter(financialContractUuid, "3", false, true, 30,
                2017, 5);
        uuids = overdueAnalyzeDAO.getOverdueLoanPrincipalByMonthAndOverdueAssetSet(inputParameter, RepurchaseMode.M1);
        log.info("size is:{}", uuids.length);
        for (String uuid : uuids) {
            log.info("uuid is:{}", uuid);
        }
        assertThat(uuids.length, equalTo(2));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("3")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("7")));

        inputParameter = buildStaticOverdueRateInputParameter(financialContractUuid, "0,1", false, true, 30,
                2017, 5);
        uuids = overdueAnalyzeDAO.getOverdueLoanPrincipalByMonthAndOverdueAssetSet(inputParameter, RepurchaseMode.M1);
        log.info("size is:{}", uuids.length);
        for (String uuid : uuids) {
            log.info("uuid is:{}", uuid);
        }
        assertThat(uuids.length, equalTo(4));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("0")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("1")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("4")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("5")));

        inputParameter = buildStaticOverdueRateInputParameter(financialContractUuid, "1,2", false, true, 30,
                2017, 5);
        uuids = overdueAnalyzeDAO.getOverdueLoanPrincipalByMonthAndOverdueAssetSet(inputParameter, RepurchaseMode.M1);
        log.info("size is:{}", uuids.length);
        for (String uuid : uuids) {
            log.info("uuid is:{}", uuid);
        }
        assertThat(uuids.length, equalTo(4));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("1")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("2")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("5")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("6")));

        inputParameter = buildStaticOverdueRateInputParameter(financialContractUuid, "2,3", false, true, 30,
                2017, 5);
        uuids = overdueAnalyzeDAO.getOverdueLoanPrincipalByMonthAndOverdueAssetSet(inputParameter, RepurchaseMode.M1);
        log.info("size is:{}", uuids.length);
        for (String uuid : uuids) {
            log.info("uuid is:{}", uuid);
        }
        assertThat(uuids.length, equalTo(4));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("2")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("3")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("6")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("7")));

        inputParameter = buildStaticOverdueRateInputParameter(financialContractUuid, "0,1,2", false, true,
                30, 2017, 5);
        uuids = overdueAnalyzeDAO.getOverdueLoanPrincipalByMonthAndOverdueAssetSet(inputParameter, RepurchaseMode.M1);
        log.info("size is:{}", uuids.length);
        for (String uuid : uuids) {
            log.info("uuid is:{}", uuid);
        }
        assertThat(uuids.length, equalTo(6));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("0")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("1")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("2")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("4")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("5")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("6")));

        inputParameter = buildStaticOverdueRateInputParameter(financialContractUuid, "1,2,3", false, true,
                30, 2017, 5);
        uuids = overdueAnalyzeDAO.getOverdueLoanPrincipalByMonthAndOverdueAssetSet(inputParameter, RepurchaseMode.M1);
        log.info("size is:{}", uuids.length);
        for (String uuid : uuids) {
            log.info("uuid is:{}", uuid);
        }
        assertThat(uuids.length, equalTo(6));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("1")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("2")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("3")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("5")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("6")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("7")));

        inputParameter = buildStaticOverdueRateInputParameter(financialContractUuid, "0,1,2,3", false, true,
                30, 2017, 5);
        uuids = overdueAnalyzeDAO.getOverdueLoanPrincipalByMonthAndOverdueAssetSet(inputParameter, RepurchaseMode.M1);
        log.info("size is:{}", uuids.length);
        for (String uuid : uuids) {
            log.info("uuid is:{}", uuid);
        }
        assertThat(uuids.length, equalTo(8));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("0")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("1")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("2")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("3")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("4")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("5")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("6")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("7")));
    }

    //X月发放贷款逾期
    @Test
    public void getOverdueLoanPrincipalByMonthAndUnconfirmedAssetSet() {
        StaticOverdueRateInputParameter inputParameter = buildStaticOverdueRateInputParameter(financialContractUuid, "0", true, true, 30, 2017, 5);
        String[] uuids = overdueAnalyzeDAO.getOverdueLoanPrincipalByMonthAndUnconfirmedAssetSet(inputParameter, RepurchaseMode.M1);
        log.info("size is:{}", uuids.length);
        for (String uuid : uuids) {
            log.info("uuid is:{}", uuid);
        }
        assertThat(uuids.length, equalTo(2));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("8")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("12")));

        inputParameter = buildStaticOverdueRateInputParameter(financialContractUuid, "1", true, true, 30, 2017, 5);
        uuids = overdueAnalyzeDAO.getOverdueLoanPrincipalByMonthAndUnconfirmedAssetSet(inputParameter, RepurchaseMode.M1);
        log.info("size is:{}", uuids.length);
        for (String uuid : uuids) {
            log.info("uuid is:{}", uuid);
        }
        assertThat(uuids.length, equalTo(2));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("9")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("13")));

        inputParameter = buildStaticOverdueRateInputParameter(financialContractUuid, "2", true, true, 30, 2017, 5);
        uuids = overdueAnalyzeDAO.getOverdueLoanPrincipalByMonthAndUnconfirmedAssetSet(inputParameter, RepurchaseMode.M1);
        log.info("size is:{}", uuids.length);
        for (String uuid : uuids) {
            log.info("uuid is:{}", uuid);
        }
        assertThat(uuids.length, equalTo(2));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("10")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("14")));

        inputParameter = buildStaticOverdueRateInputParameter(financialContractUuid, "3", true, true, 30, 2017, 5);
        uuids = overdueAnalyzeDAO.getOverdueLoanPrincipalByMonthAndUnconfirmedAssetSet(inputParameter, RepurchaseMode.M1);
        log.info("size is:{}", uuids.length);
        for (String uuid : uuids) {
            log.info("uuid is:{}", uuid);
        }
        assertThat(uuids.length, equalTo(2));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("11")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("15")));

        inputParameter = buildStaticOverdueRateInputParameter(financialContractUuid, "0,1", true, true, 30, 2017, 5);
        uuids = overdueAnalyzeDAO.getOverdueLoanPrincipalByMonthAndUnconfirmedAssetSet(inputParameter, RepurchaseMode.M1);
        log.info("size is:{}", uuids.length);
        for (String uuid : uuids) {
            log.info("uuid is:{}", uuid);
        }
        assertThat(uuids.length, equalTo(4));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("8")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("9")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("12")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("13")));

        inputParameter = buildStaticOverdueRateInputParameter(financialContractUuid, "1,2", true, true, 30, 2017, 5);
        uuids = overdueAnalyzeDAO.getOverdueLoanPrincipalByMonthAndUnconfirmedAssetSet(inputParameter, RepurchaseMode.M1);
        log.info("size is:{}", uuids.length);
        for (String uuid : uuids) {
            log.info("uuid is:{}", uuid);
        }
        assertThat(uuids.length, equalTo(4));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("9")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("10")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("13")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("14")));

        inputParameter = buildStaticOverdueRateInputParameter(financialContractUuid, "2,3", true, true, 30, 2017, 5);
        uuids = overdueAnalyzeDAO.getOverdueLoanPrincipalByMonthAndUnconfirmedAssetSet(inputParameter, RepurchaseMode.M1);
        log.info("size is:{}", uuids.length);
        for (String uuid : uuids) {
            log.info("uuid is:{}", uuid);
        }
        assertThat(uuids.length, equalTo(4));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("10")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("11")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("14")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("15")));

        inputParameter = buildStaticOverdueRateInputParameter(financialContractUuid, "0,1,2", true, true, 30, 2017, 5);
        uuids = overdueAnalyzeDAO.getOverdueLoanPrincipalByMonthAndUnconfirmedAssetSet(inputParameter, RepurchaseMode.M1);
        log.info("size is:{}", uuids.length);
        for (String uuid : uuids) {
            log.info("uuid is:{}", uuid);
        }
        assertThat(uuids.length, equalTo(6));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("8")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("9")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("10")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("12")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("13")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("14")));

        inputParameter = buildStaticOverdueRateInputParameter(financialContractUuid, "1,2,3", true, true, 30, 2017, 5);
        uuids = overdueAnalyzeDAO.getOverdueLoanPrincipalByMonthAndUnconfirmedAssetSet(inputParameter, RepurchaseMode.M1);
        log.info("size is:{}", uuids.length);
        for (String uuid : uuids) {
            log.info("uuid is:{}", uuid);
        }
        assertThat(uuids.length, equalTo(6));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("9")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("10")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("11")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("13")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("14")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("15")));

        inputParameter = buildStaticOverdueRateInputParameter(financialContractUuid, "0,1,2,3", true, true, 30, 2017, 5);
        uuids = overdueAnalyzeDAO.getOverdueLoanPrincipalByMonthAndUnconfirmedAssetSet(inputParameter, RepurchaseMode.M1);
        log.info("size is:{}", uuids.length);
        for (String uuid : uuids) {
            log.info("uuid is:{}", uuid);
        }
        assertThat(uuids.length, equalTo(8));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("8")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("9")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("10")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("11")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("12")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("13")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("14")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("15")));
    }

    //X月发放贷款的剩余贷款
    @Test
    public void getRemittanceUnclearLoansBy() {
        StaticOverdueRateInputParameter inputParameter = buildStaticOverdueRateInputParameter(
                financialContractUuid, "0", false, true, 30, 2017, 5);
        String[] uuids = overdueAnalyzeDAO.getRemittanceUnclearLoansBy(inputParameter,
                RepurchaseMode.M1);
        log.info("size is:{}", uuids.length);
        for (String uuid : uuids) {
            log.info("uuid is:{}", uuid);
        }

        assertThat(uuids.length, equalTo(16));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("0")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("1")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("2")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("3")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("4")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("5")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("6")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("7")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("8")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("9")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("10")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("11")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("12")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("13")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("14")));
        assertThat(Arrays.asList(uuids), hasItem(contractUuidsMap.get("15")));
    }

    //账本流水
    @Test
    public void getUnEarnedPrincipalFromLedgerBookBy() {
        InputParameter inputParameter = buildInputParameter(financialContractUuid, "0", false, true, 30);
        String[] uuids = {contractUuidsMap.get("0"), contractUuidsMap.get("1"),
                contractUuidsMap.get("2"), contractUuidsMap.get("3")};
        BigDecimal amount = overdueAnalyzeDAO.getUnEarnedPrincipalFromLedgerBookBy(inputParameter, uuids);//true已回购计入已还，账本需要计算回购未偿
        log.info("amount is:{}", amount);
        assertThat(amount, equalTo(new BigDecimal("1000.00")));
    }

    //账本流水
    @Test
    public void getUnEarnedPrincipalInterestFromLedgerBookBy() {
        InputParameter inputParameter = buildInputParameter(financialContractUuid, "0", false, true, 30);
        String[] uuids = {contractUuidsMap.get("0"), contractUuidsMap.get("1"),
                contractUuidsMap.get("2"), contractUuidsMap.get("3")};

        BigDecimal amount = overdueAnalyzeDAO.getUnEarnedPrincipalInterestFromLedgerBookBy(inputParameter, uuids);//true已回购计入已还，账本需要计算回购未偿
        log.info("amount is:{}", amount);
        assertThat(amount, equalTo(new BigDecimal("1100.00")));
    }

    //回购单
    @Test
    public void getUnEarnedPrinciFromRepurchaseBy() {
        String[] uuids = {contractUuidsMap.get("16"), contractUuidsMap.get("17"),
                contractUuidsMap.get("18"), contractUuidsMap.get("19")};

        BigDecimal amount = overdueAnalyzeDAO.getUnEarnedPrincipalFromRepurchaseBy(uuids, false);
        log.info("amount is:{}", amount);
        assertThat(amount, equalTo(new BigDecimal("5700.00")));

        amount = overdueAnalyzeDAO.getUnEarnedPrincipalFromRepurchaseBy(uuids,
                true);
        log.info("amount is:{}", amount);
        assertThat(amount, equalTo(new BigDecimal("5850.00")));
    }
}