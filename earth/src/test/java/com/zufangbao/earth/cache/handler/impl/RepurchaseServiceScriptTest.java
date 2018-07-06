package com.zufangbao.earth.cache.handler.impl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.demo2do.core.entity.Result;
import com.suidifu.matryoshka.customize.DelayTaskServices;
import com.suidifu.matryoshka.delayPosition.handler.DelayProcessingTaskCacheHandler;
import com.suidifu.matryoshka.delayPosition.handler.DelayTaskConfigCacheHandler;
import com.suidifu.matryoshka.delayTask.DelayProcessingTask;
import com.suidifu.matryoshka.handler.ProductCategoryCacheHandler;
import com.suidifu.matryoshka.handler.SandboxDataSetHandler;
import com.suidifu.matryoshka.productCategory.ProductCategory;
import com.suidifu.matryoshka.service.delayTask.DelayProcessingTaskService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
    "classpath:/local/applicationContext-*.xml" })
@Transactional
@Rollback(true)
public class RepurchaseServiceScriptTest  extends AbstractTransactionalJUnit4SpringContextTests {
    private final  static Log logger = LogFactory.getLog(RepurchaseServiceScriptTest.class);

    @Autowired
    private ProductCategoryCacheHandler productCategoryCacheHandler;

    @Autowired
    private DelayTaskConfigCacheHandler delayTaskConfigCacheHandler;

    @Autowired
    private SandboxDataSetHandler sandboxDataSetHandler;

    @Autowired
    private DelayProcessingTaskCacheHandler delayProcessingTaskCacheHandler;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    @Qualifier("delayProcessingTaskDBService")
    private DelayProcessingTaskService delayProcessingTaskService;

    @Test
    @Sql("classpath:test/yunxin/productCategory/test_script_RepurchaseServices.sql")
    public void test_RepurchaseServiceScript(){


        stringRedisTemplate.delete("sr:delay_task_config_uuid_01");

        String pre_interface_url = "repurchase/zhongan/repurchase";
        String repurchaseDocUuid ="e2dc60ef-768a-4834-9b95-83734937598c";
        ProductCategory productCategory = productCategoryCacheHandler.get(pre_interface_url, false);
        String delayTaskConfigUuid = productCategory.getDelayTaskConfigUuid();
        DelayTaskServices delayTaskServices = (DelayTaskServices) delayTaskConfigCacheHandler
            .getCompiledObjectDelayTaskConfigUuid(delayTaskConfigUuid);
        Result processedResult = new Result();
        Map<String, Object> inputMap = new HashMap<String, Object>();
        inputMap.put("uniqueId", "51fd9fbc-1e13-473b-92c5-4adfade246b7");
        inputMap.put("contractNo",null);
        inputMap.put("taskConfigUuid",delayTaskConfigUuid);
        delayTaskServices.evaluate(processedResult,delayProcessingTaskCacheHandler,sandboxDataSetHandler,inputMap,new HashMap<>(), logger);

        List<DelayProcessingTask> delayProcessingTasks = delayProcessingTaskService.get_by_repurchaseDocUuid(repurchaseDocUuid);
        Assert.assertEquals(1,delayProcessingTasks.size());
        DelayProcessingTask delayProcessingTask =delayProcessingTasks.get(0);
        Assert.assertEquals("315317d0-438a-4e6d-a4b8-20a260624b6e",delayProcessingTask.getContractUuid());
        Assert.assertEquals(delayTaskConfigUuid,delayProcessingTask.getConfigUuid());
        Assert.assertEquals("57d2f333-de15-4ded-8700-f3fcae0e946c",delayProcessingTask.getCustomerUuid());
        Assert.assertEquals("2d380fe1-7157-490d-9474-12c5a9901e29",delayProcessingTask.getFinancialContractUuid());
        Assert.assertEquals(new Integer(0),delayProcessingTask.getExecuteStatus());
        String workParams = delayProcessingTask.getWorkParams();
        JSONObject workParamMap = JSON.parseObject(workParams);
        Assert.assertEquals(Integer.valueOf(8),Integer.valueOf(workParamMap.get("changeType").toString()));
        Assert.assertEquals("51fd9fbc-1e13-473b-92c5-4adfade246b7",workParamMap.get("loanNo").toString());
        //Assert.assertEquals("340826199407034015",workParamMap.get("thirdUserNo").toString());
        stringRedisTemplate.delete("sr:delay_task_config_uuid_01");
    }


}
