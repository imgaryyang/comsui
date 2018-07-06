package com.suidifu.dowjones.dao;

import com.suidifu.dowjones.Dowjones;
import com.suidifu.dowjones.model.StatisticsReport;
import com.suidifu.dowjones.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author: frankwoo(吴峻申) <br>
 * @date: 2017/12/12 <br>
 * @time: 13:35 <br>
 * @mail: frank_wjs@hotmail.com <br>
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Dowjones.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(profiles = "test")
@Slf4j
public class BaseDAOTest extends BaseDAO {
    private String financialContractUuid;
    private StatisticsReport statisticsReport;

    @Before
    public void setUp() {
        financialContractUuid = "d2812bc5-5057-4a91-b3fd-9019506f0499";

        statisticsReport = new StatisticsReport();
        statisticsReport.setStatisticsDate(DateFormatUtils.format(new Date(), Constants.DATE_PATTERN));
        statisticsReport.setDenominatorDesc("逾期未偿本金");
        statisticsReport.setNumeratorDesc("剩余贷款本金");
        statisticsReport.setQuotientDesc("动态池逾期率(剩余本金)");
        statisticsReport.setDenominator("10.0");
        statisticsReport.setNumerator("10.0");
        statisticsReport.setQuotient(new BigDecimal("10.0").toPlainString() + "%");
        statisticsReport.setComputeType(0);
    }

    @After
    public void tearDown() {
        financialContractUuid = null;
        statisticsReport = null;
    }

    @Test
    public void initPredicates() {
    }

    @Test
    public void initPredicates1() {
        String[] predicates = initPredicates(Constants.CONTRACT, financialContractUuid);
        log.info("size is:{}", predicates.length);
        for (String temp : predicates) {
            log.info("result is:{}", temp);
        }
    }

    @Test
    public void loadDataFromTable1() {
    }

    @Test
    public void saveData2Table() {
    }

    @Test
    public void filterRepurchaseMode() {
    }

    @Test
    public void getGraceDay() {
    }
}