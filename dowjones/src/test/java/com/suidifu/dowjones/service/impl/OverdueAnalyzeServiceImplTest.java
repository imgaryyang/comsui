package com.suidifu.dowjones.service.impl;

import com.suidifu.dowjones.Dowjones;
import com.suidifu.dowjones.exception.DowjonesException;
import com.suidifu.dowjones.model.StatisticsReport;
import com.suidifu.dowjones.service.OverdueAnalyzeService;
import com.suidifu.dowjones.vo.request.InputParameter;
import com.suidifu.dowjones.vo.request.StaticOverdueRateInputParameter;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * @author: frankwoo(吴峻申) <br>
 * @date: 2017/11/22 <br>
 * @time: 下午9:41 <br>
 * @mail: frank_wjs@hotmail.com <br>
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Dowjones.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(profiles = "test")
public class OverdueAnalyzeServiceImplTest {
    @Resource
    private OverdueAnalyzeService overdueAnalyzeService;
    private InputParameter inputParameter;
    private StaticOverdueRateInputParameter staticOverdueRateInputParameter;

    @Before
    public void setUp() {
        inputParameter = new InputParameter();
        inputParameter.setComputeTypeFlag(0);//0剩余本金
        inputParameter.setFinancialContractUuid("d2812bc5-5057-4a91-b3fd-9019506f0499");
        inputParameter.setIncludeRepurchase(true);
        inputParameter.setIncludeUnconfirmed(true);
        inputParameter.setOverdueStage("0,1,2,3");
        inputParameter.setPeriodDays(30);

        staticOverdueRateInputParameter = new StaticOverdueRateInputParameter();
        staticOverdueRateInputParameter.setComputeTypeFlag(0);
        staticOverdueRateInputParameter.setFinancialContractUuid("d2812bc5-5057-4a91-b3fd-9019506f0499");
        staticOverdueRateInputParameter.setIncludeRepurchase(true);
        staticOverdueRateInputParameter.setIncludeUnconfirmed(true);
        staticOverdueRateInputParameter.setOverdueStage("0,1,2,3");
        staticOverdueRateInputParameter.setPeriodDays(30);
        staticOverdueRateInputParameter.setYear(2017);
        staticOverdueRateInputParameter.setMonth(5);
    }

    @After
    public void tearDown() {
        inputParameter = null;
        staticOverdueRateInputParameter = null;
    }

    @Test
    public void getDynamicOverdueRate() throws IOException, DowjonesException {
        Long startTime = new Date().getTime();
        List<StatisticsReport> reports = overdueAnalyzeService.getDynamicOverdueRate(inputParameter);
        Long endTime = new Date().getTime();
        log.info("\n\n\n\n\nduration is :{}ms\n\n\n\n\n", endTime - startTime);

        //动态池逾期率
        logInfo(reports);
    }

    private void logInfo(List<StatisticsReport> reports) {
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

    @Test
    public void getStaticOverdueRate() throws IOException, DowjonesException {
        Long startTime = new Date().getTime();
        List<StatisticsReport> reports = overdueAnalyzeService.getStaticOverdueRate(staticOverdueRateInputParameter);
        Long endTime = new Date().getTime();
        log.info("\n\n\n\n\nduration is :{}ms\n\n\n\n\n", endTime - startTime);

        //X月静态池逾期率
        logInfo(reports);
    }
}