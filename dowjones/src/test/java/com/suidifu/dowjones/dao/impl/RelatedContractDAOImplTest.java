package com.suidifu.dowjones.dao.impl;

import com.suidifu.dowjones.Dowjones;
import com.suidifu.dowjones.dao.RelatedContractDAO;
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
 * @time: 下午11:13 <br>
 * @mail: frank_wjs@hotmail.com <br>
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Dowjones.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(profiles = "test")
@Slf4j
public class RelatedContractDAOImplTest {
    @Resource
    private RelatedContractDAO relatedContractDAO;
    private String idNumber;

    @Before
    public void setUp() throws Exception {
        idNumber = "95136038-aebe-4de0-8a1a-2ea46fb2f917";
        //idNumber = "320301198502169142";
    }

    @After
    public void tearDown() throws Exception {
        idNumber = null;
    }

    @Test
    public void testGetRelatedContract() throws Exception {
        Long startTime = new Date().getTime();
        Dataset<Row> result = relatedContractDAO.getRelatedContract(idNumber);
        Long endTime = new Date().getTime();
        log.info("\n\n\n\n\nduration is :{}ms\npartitions length is :{}\nsize is :{}\n\n\n\n\n",
                endTime - startTime, result.rdd().partitions().length, result.count());
    }
}