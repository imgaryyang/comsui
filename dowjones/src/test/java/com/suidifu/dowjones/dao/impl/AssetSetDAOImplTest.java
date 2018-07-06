package com.suidifu.dowjones.dao.impl;

import com.suidifu.dowjones.Dowjones;
import com.suidifu.dowjones.dao.AssetSetDAO;
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
 * @date: 2017/11/6 <br>
 * @time: 下午9:25 <br>
 * @mail: frank_wjs@hotmail.com <br>
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Dowjones.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(profiles = "test")
public class AssetSetDAOImplTest {
    private String idNumber;
    @Resource
    private AssetSetDAO assetSetDAO;

    @Before
    public void setUp() {
        idNumber = "95136038-aebe-4de0-8a1a-2ea46fb2f917";
        //idNumber = "320301198502169142";
    }

    @After
    public void tearDown() {
        idNumber = null;
    }

    @Test
    public void getAllOpenRepaymentPlanList() {
        Long startTime = new Date().getTime();
        Dataset<Row> result = assetSetDAO.getAllOpenRepaymentPlanList(idNumber);
        Long endTime = new Date().getTime();
        log.info("\n\n\n\n\nduration is :{}ms\npartitions length is :{}\nsize is :{}\n\n\n\n\n",
                endTime - startTime, result.rdd().partitions().length, result.count());
    }

    @Test
    public void testGetDailyOverStatusContractNumber() {
        Long startTime = new Date().getTime();
        Dataset<Row> result = assetSetDAO.getDailyOverStatusContractNumber();
        Long endTime = new Date().getTime();
        log.info("\n\n\n\n\nduration is :{}ms\npartitions length is :{}\nsize is :{}\n\n\n\n\n",
                endTime - startTime, result.rdd().partitions().length, result.count());
    }
}