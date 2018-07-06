package com.zufangbao.earth.yunxin.customize;

import com.alibaba.fastjson.JSONObject;
import com.demo2do.core.entity.Result;
import com.suidifu.matryoshka.customize.DelayTaskServices;
import com.suidifu.matryoshka.delayPosition.handler.DelayProcessingTaskCacheHandler;
import com.suidifu.matryoshka.delayPosition.handler.DelayTaskConfigCacheHandler;
import com.suidifu.matryoshka.delayTask.DelayProcessingTask;
import com.suidifu.matryoshka.handler.SandboxDataSetHandler;
import com.suidifu.matryoshka.service.delayTask.DelayProcessingTaskService;
import com.suidifu.matryoshka.snapshot.SandboxDataSetSpec;
import com.zufangbao.earth.yunxin.customize.scripts.ZhongAnRepaymentPlanDateDelayTaskServices;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author louguanyang on 2017/5/11.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:/local/applicationContext-*.xml" })
@Transactional
@TransactionConfiguration(defaultRollback = false)
public class ZhongAnRepaymentPlanDateDelayTaskServicesTest extends AbstractTransactionalJUnit4SpringContextTests {
    private final static Log logger = LogFactory.getLog(ZhongAnRepaymentPlanDateDelayTaskServicesTest.class);
    @Autowired
    private DelayTaskConfigCacheHandler delayTaskConfigCacheHandler;

    @Autowired
    private DelayProcessingTaskCacheHandler delayProcessingTaskCacheHandler;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private SandboxDataSetHandler sandboxDataSetHandler;

    @Autowired
    @Qualifier("delayProcessingTaskDBService")
    private DelayProcessingTaskService delayProcessingTaskService;

    @Before
    public void init_cache() {
        delayTaskConfigCacheHandler.clearAll();
        stringRedisTemplate.delete("sr:delayTaskConfigUuid");
        stringRedisTemplate.delete("sr:374da0c4-3935-11e7-952e-ba77244e1da4");
    }

    @Test
    public void test() {
        Assert.assertTrue(true);
    }
    @Test
    @Sql("classpath:test/yunxin/delayTask/zhongan_sava_plan_date.sql")
    public void test_save_plan_date() {
        try {
            String delayTaskConfigUuid = "delayTaskConfigUuid";

            Map<String, Object> inputMap = new HashMap<>();

            List<String> repaymentPlanNoList = new ArrayList<>();
            repaymentPlanNoList.add("ZC1691381619856510976");
            repaymentPlanNoList.add("ZC1691381620527599616");

            String contractUuid = "be834b15-56a7-4175-b926-64c90869a2f0";
            String financialContractUuid = "b674a323-0c30-4e4b-9eba-b14e05a9d80a";
            inputMap.put(SandboxDataSetSpec.FINANCIAL_CONTRACT_UUID, financialContractUuid);
            inputMap.put(SandboxDataSetSpec.CONTRACT_UUID, contractUuid);
            inputMap.put(SandboxDataSetSpec.REPAYMENT_PLAN_NO_LIST, repaymentPlanNoList);

            Map<String, Object> resultMap = new HashMap<>();

            ZhongAnRepaymentPlanDateDelayTaskServices delayTaskServices = new
                    ZhongAnRepaymentPlanDateDelayTaskServices();

            boolean evaluate = delayTaskServices.evaluate(null, delayProcessingTaskCacheHandler, sandboxDataSetHandler,
                    inputMap, resultMap, logger);
            Assert.assertTrue(evaluate);


            String assetSetUuid1 = "922b2d45-da2c-4b6b-87a2-30a5303a5da0";
            String assetSetUuid2 = "1dcb05fd-8602-4030-b45b-180b4d0b8281";
            List<DelayProcessingTask> taskList1 = delayProcessingTaskService.get_by_repaymentPlanUuid
                    (assetSetUuid1);
            Assert.assertEquals(1, taskList1.size());


            List<DelayProcessingTask> taskList2 = delayProcessingTaskService.get_by_repaymentPlanUuid
                    (assetSetUuid2);
            Assert.assertEquals(1, taskList2.size());
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }

    }

    @Test
    @Sql("classpath:test/yunxin/delayTask/zhongan_sava_plan_date_by_db_source_code.sql")
    public void test_save_plan_date_by_db_source_code() {
        try {
            String delayTaskConfigUuid = "delayTaskConfigUuid";

            Map<String, Object> inputMap = new HashMap<>();

            List<String> repaymentPlanNoList = new ArrayList<>();
            repaymentPlanNoList.add("ZC1691381619856510976");
            repaymentPlanNoList.add("ZC1691381620527599616");

            String contractUuid = "be834b15-56a7-4175-b926-64c90869a2f0";
            String financialContractUuid = "b674a323-0c30-4e4b-9eba-b14e05a9d80a";
            inputMap.put(SandboxDataSetSpec.FINANCIAL_CONTRACT_UUID, financialContractUuid);
            inputMap.put(SandboxDataSetSpec.CONTRACT_UUID, contractUuid);
            inputMap.put(SandboxDataSetSpec.CONFING_UUID, UUID.randomUUID()+"");
            inputMap.put(SandboxDataSetSpec.REPAYMENT_PLAN_NO_LIST, repaymentPlanNoList);

            Map<String, Object> resultMap = new HashMap<>();

            DelayTaskServices delayTaskServices = (DelayTaskServices)delayTaskConfigCacheHandler.getCompiledObjectDelayTaskConfigUuid(delayTaskConfigUuid);
            Assert.assertNotNull(delayTaskServices);
            JSONObject jsonObject1 = new JSONObject();
            JSONObject jsonObject2 = new JSONObject();
            List<JSONObject> planNoList = new ArrayList<>();
            jsonObject1.put("repaymentNumber", repaymentPlanNoList.get(0));
            jsonObject2.put("repaymentNumber",  repaymentPlanNoList.get(1));
            planNoList.add(jsonObject1);
            planNoList.add(jsonObject2);
            inputMap.put("repaymentPlanList", planNoList);
            Result result = new Result();
            result.setCode(0+"");
            result.setData(inputMap);
            boolean evaluate = delayTaskServices.evaluate(result, delayProcessingTaskCacheHandler, sandboxDataSetHandler,
                    inputMap, resultMap, logger);
            Assert.assertTrue(evaluate);


            String assetSetUuid1 = "922b2d45-da2c-4b6b-87a2-30a5303a5da0";
            String assetSetUuid2 = "1dcb05fd-8602-4030-b45b-180b4d0b8281";
            List<DelayProcessingTask> taskList1 = delayProcessingTaskService.get_by_repaymentPlanUuid
                    (assetSetUuid1);
            Assert.assertEquals(1, taskList1.size());


            List<DelayProcessingTask> taskList2 = delayProcessingTaskService.get_by_repaymentPlanUuid
                    (assetSetUuid2);
            Assert.assertEquals(1, taskList2.size());
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }

    }

    @Test
    @Sql("classpath:test/yunxin/delayTask/zhongan_sava_plan_date_by_db_source_code_two.sql")
    public void test_save_plan_date_by_db_source_code_two() {
        try {
            String delayTaskConfigUuid = "delayTaskConfigUuid";

            Map<String, Object> inputMap = new HashMap<>();

            List<String> repaymentPlanNoList = new ArrayList<>();
            repaymentPlanNoList.add("ZC1691381619856510976");
            repaymentPlanNoList.add("ZC1691381620527599616");

            String contractUuid = "be834b15-56a7-4175-b926-64c90869a2f0";
            String financialContractUuid = "b674a323-0c30-4e4b-9eba-b14e05a9d80a";
            inputMap.put(SandboxDataSetSpec.FINANCIAL_CONTRACT_UUID, financialContractUuid);
            inputMap.put(SandboxDataSetSpec.CONTRACT_UUID, contractUuid);
            inputMap.put(SandboxDataSetSpec.CONFING_UUID, UUID.randomUUID()+"");
            inputMap.put(SandboxDataSetSpec.REPAYMENT_PLAN_NO_LIST, repaymentPlanNoList);

            Map<String, Object> resultMap = new HashMap<>();

            DelayTaskServices delayTaskServices = (DelayTaskServices)delayTaskConfigCacheHandler.getCompiledObjectDelayTaskConfigUuid(delayTaskConfigUuid);
            Assert.assertNotNull(delayTaskServices);
            JSONObject jsonObject1 = new JSONObject();
            JSONObject jsonObject2 = new JSONObject();
            List<JSONObject> planNoList = new ArrayList<>();
            jsonObject1.put("repaymentNumber", repaymentPlanNoList.get(0));
            jsonObject2.put("repaymentNumber",  repaymentPlanNoList.get(1));
            planNoList.add(jsonObject1);
            planNoList.add(jsonObject2);
            inputMap.put("repaymentPlanList", planNoList);
            Result result = new Result();
            result.setCode(0+"");
            result.setData(inputMap);
            boolean evaluate = delayTaskServices.evaluate(result, delayProcessingTaskCacheHandler, sandboxDataSetHandler,
                    inputMap, resultMap, logger);
            Assert.assertTrue(evaluate);


            String assetSetUuid1 = "922b2d45-da2c-4b6b-87a2-30a5303a5da0";
            String assetSetUuid2 = "1dcb05fd-8602-4030-b45b-180b4d0b8281";
            List<DelayProcessingTask> taskList1 = delayProcessingTaskService.get_by_repaymentPlanUuid
                    (assetSetUuid1);
            Assert.assertEquals(1, taskList1.size());


            List<DelayProcessingTask> taskList2 = delayProcessingTaskService.get_by_repaymentPlanUuid
                    (assetSetUuid2);
            Assert.assertEquals(1, taskList2.size());
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }

    }

}
