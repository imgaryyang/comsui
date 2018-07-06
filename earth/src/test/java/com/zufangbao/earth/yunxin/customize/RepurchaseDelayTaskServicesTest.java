package com.zufangbao.earth.yunxin.customize;

import com.demo2do.core.entity.Result;
import com.suidifu.matryoshka.delayPosition.handler.DelayProcessingTaskCacheHandler;
import com.suidifu.matryoshka.handler.SandboxDataSetHandler;
import com.suidifu.matryoshka.service.delayTask.DelayProcessingTaskService;
import com.zufangbao.earth.yunxin.customize.scripts.RepurchaseDelayTaskServices;
import com.zufangbao.sun.service.ContractService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
    "classpath:/local/applicationContext-*.xml"})
@Rollback(true)
public class RepurchaseDelayTaskServicesTest extends AbstractTransactionalJUnit4SpringContextTests {
    private final static Log log = LogFactory.getLog(RepurchaseDelayTaskServicesTest.class);
    @Autowired
    private DelayProcessingTaskCacheHandler delayProcessingTaskCacheHandler;

    @Autowired
    @Qualifier("delayProcessingTaskCacheService")
    private DelayProcessingTaskService delayProcessingTaskService;

    @Autowired
    private ContractService contractService;

    @Autowired
    private SandboxDataSetHandler sandboxDataSetHandler;


    @Test
    @Sql("classpath:test/yunxin/customize/repurchaseDelayTaskServicesTest.sql")
    public void test_evaluate() {
        Result processedResult = new Result();
        Map<String, Object> inputMap = new HashMap<String, Object>();
        inputMap.put("uniqueId", "51fd9fbc-1e13-473b-92c5-4adfade246b7");
        inputMap.put("contractNo",null);
        RepurchaseDelayTaskServices repurchaseDelayTaskServices = new RepurchaseDelayTaskServices();
        repurchaseDelayTaskServices
            .evaluate(processedResult, delayProcessingTaskCacheHandler, sandboxDataSetHandler,inputMap, new HashMap<>(), log);
//        delayProcessingTaskService.get
    }

}
