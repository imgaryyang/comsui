package com.suidifu.dowjones.dao.impl;

import com.suidifu.dowjones.Dowjones;
import com.suidifu.dowjones.dao.CashFingerPrinterDAO;
import com.suidifu.dowjones.utils.DateUtils;
import com.suidifu.dowjones.vo.request.FingerPrinterParameter;
import com.suidifu.dowjones.vo.request.QueryParameter;
import lombok.extern.slf4j.Slf4j;
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
import java.util.Date;

/**
 * @author: frankwoo(吴峻申) <br>
 * @date: 2018/1/8 <br>
 * @time: 15:16 <br>
 * @mail: frank_wjs@hotmail.com <br>
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Dowjones.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(profiles = "test")
public class CashFingerPrinterDAOImplTest {
    private FingerPrinterParameter fingerPrinterParameter;
    private QueryParameter queryParameter;
    @Resource
    private CashFingerPrinterDAO cashFingerPrinterDAO;

    @Before
    public void setUp() {
        fingerPrinterParameter = new FingerPrinterParameter();
        fingerPrinterParameter.setFinancialContractUuid("9495f5f2-d306-461a-8b03-5896923dc1b3");
        fingerPrinterParameter.setTaskId("0");
        fingerPrinterParameter.setDataStreamUuid("0cb66f9e-420f-4e9a-bf49-5eb99b776252");
        fingerPrinterParameter.setPath("/Users/wujunshen/Downloads/csv/");

        queryParameter = new QueryParameter();
        queryParameter.setFinancialContractUuid("9495f5f2-d306-461a-8b03-5896923dc1b3");
        queryParameter.setAccountNO("60000.1220127571120");
        queryParameter.setLedgerBookNO("101036a7-52db-4e15-ac7c-895484a0af4c");
    }

    @After
    public void tearDown() {
        fingerPrinterParameter = null;
    }

    @Test
    public void getCashFingerPrinterOfZeroCashFlow() {
        Long startTime = new Date().getTime();
        Dataset<Row> result = cashFingerPrinterDAO.getCashFingerPrinterOfZeroCashFlow(queryParameter);
        Long endTime = new Date().getTime();
        log.info("\n\n\n\n\nduration is :{}ms\npartitions length is :{}\nsize is :{}\n\n\n\n\n",
                endTime - startTime, result.rdd().partitions().length, result.count());
    }

    @Test
    public void getCashFingerPrinterOfCashFlow() {
        Long startTime = new Date().getTime();
        Dataset<Row> result = cashFingerPrinterDAO.getCashFingerPrinterOfCashFlow(queryParameter, DateUtils.getYesterdayFormatYYMMDD());
        Long endTime = new Date().getTime();
        log.info("\n\n\n\n\nduration is :{}ms\npartitions length is :{}\nsize is :{}\n\n\n\n\n",
                endTime - startTime, result.rdd().partitions().length, result.count());
    }

    @Test
    public void getCashFingerPrinterFromOrderNO() {
        Long startTime = new Date().getTime();
        Dataset<Row> result = cashFingerPrinterDAO.getCashFingerPrinterFromOrderNO(queryParameter);
        Long endTime = new Date().getTime();
        log.info("\n\n\n\n\nduration is :{}ms\npartitions length is :{}\nsize is :{}\n\n\n\n\n",
                endTime - startTime, result.rdd().partitions().length, result.count());
    }

    @Test
    public void getCashFingerPrinterFromOrderNO_EMPTY_DATA() {
        queryParameter.setFinancialContractUuid("3b12ac75-4c58-4375-a733-78c7810efebe");
        Long startTime = new Date().getTime();
        Dataset<Row> result = cashFingerPrinterDAO.getCashFingerPrinterFromOrderNO(queryParameter);
        Long endTime = new Date().getTime();
        log.info("\n\n\n\n\nduration is :{}ms\npartitions length is :{}\nsize is :{}\n\n\n\n\n",
                endTime - startTime, result.rdd().partitions().length, result.count());
    }

    @Test
    public void getOutlierChannelCode() {
        Long startTime = new Date().getTime();
        String[] result = cashFingerPrinterDAO.getOutlierChannelCode(fingerPrinterParameter.getFinancialContractUuid());
        Long endTime = new Date().getTime();
        log.info("\n\n\n\n\nduration is :{}ms\n\n\n\n\n", endTime - startTime);

        for (String element : result) {
            log.info("element is:{}", element);
        }
    }

    @Test
    public void getCashFingerPrinterFromOutlierChannelCode() {
        Long startTime = new Date().getTime();
        Dataset<Row> result = cashFingerPrinterDAO.getCashFingerPrinterFromOutlierChannelCode(queryParameter);
        Long endTime = new Date().getTime();
        log.info("\n\n\n\n\nduration is :{}ms\npartitions length is :{}\nsize is :{}\n\n\n\n\n",
                endTime - startTime, result.rdd().partitions().length, result.count());
    }

    @Test
    public void getQueryParameters() {
        Long startTime = new Date().getTime();
        QueryParameter result = cashFingerPrinterDAO.getQueryParameters(fingerPrinterParameter.getFinancialContractUuid());
        Long endTime = new Date().getTime();
        log.info("\n\n\n\n\nduration is :{}ms\n\n\n\n\n", endTime - startTime);

        log.info("ledgerBookNO is :{}", result.getLedgerBookNO());
        log.info("financialContractUuid is :{}", result.getFinancialContractUuid());
        log.info("accountNO is :{}", result.getAccountNO());
    }

    @Test
    public void save() {
        Long startTime = new Date().getTime();
        cashFingerPrinterDAO.save(fingerPrinterParameter);
        Long endTime = new Date().getTime();
        log.info("\n\n\n\n\nduration is :{}ms\n\n\n\n\n", endTime - startTime);
    }

    @Test
    public void loadScheduleJob1() {
        Long startTime = new Date().getTime();
        FingerPrinterParameter[] fingerPrinterParameters = cashFingerPrinterDAO.loadScheduleJob();
        Long endTime = new Date().getTime();
        log.info("\n\n\n\n\nduration is :{}ms\n\n\n\n\n", endTime - startTime);

        for (FingerPrinterParameter fingerPrinterParameter : fingerPrinterParameters) {
            log.info("taskID is :{}", fingerPrinterParameter.getTaskId());
            log.info("dataStreamUuid is :{}", fingerPrinterParameter.getDataStreamUuid());
            log.info("financialContractUuid is :{}", fingerPrinterParameter.getFinancialContractUuid());
            log.info("path is :{}", fingerPrinterParameter.getPath());
        }
    }

    @Test
    public void getCashFingerPrinterOfOneCashFlow() {
        Long startTime = new Date().getTime();
        Dataset<Row> result = cashFingerPrinterDAO.getCashFingerPrinterOfOneCashFlow(queryParameter);
        Long endTime = new Date().getTime();
        log.info("\n\n\n\n\nduration is :{}ms\npartitions length is :{}\nsize is :{}\n\n\n\n\n",
                endTime - startTime, result.rdd().partitions().length, result.count());
    }

    @Test
    public void loadScheduleJob() {
        Long startTime = new Date().getTime();
        FingerPrinterParameter[] fingerPrinterParameters = cashFingerPrinterDAO.loadScheduleJob("9495f5f2-d306-461a-8b03-5896923dc1b3");
        Long endTime = new Date().getTime();
        log.info("\n\n\n\n\nduration is :{}ms\n\n\n\n\n", endTime - startTime);

        for (FingerPrinterParameter fingerPrinterParameter : fingerPrinterParameters) {
            log.info("taskID is :{}", fingerPrinterParameter.getTaskId());
            log.info("dataStreamUuid is :{}", fingerPrinterParameter.getDataStreamUuid());
            log.info("financialContractUuid is :{}", fingerPrinterParameter.getFinancialContractUuid());
            log.info("path is :{}", fingerPrinterParameter.getPath());
        }
    }
}