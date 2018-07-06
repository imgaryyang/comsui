package com.suidifu.matryoshaka.test.service.delayTask.impl;

import com.suidifu.matryoshka.delayTask.DelayProcessingTask;
import com.suidifu.matryoshka.delayTask.DelayProcessingTaskLog;
import com.suidifu.matryoshka.service.delayTask.DelayProcessingTaskService;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.utils.StringUtils;
import com.zufangbao.sun.yunxin.exception.SunException;
import org.apache.commons.collections.CollectionUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by louguanyang on 2017/5/3.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:/context/applicationContext-*.xml",
        "classpath:/local/applicationContext-*.xml",
})
@Transactional
@TransactionConfiguration
public class DelayProcessingTaskDBServiceImplTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private DelayProcessingTaskService delayProcessingTaskDBService;

    @Test
    @Sql("classpath:test/yunxin/delayTask/delayProcessingTaskDBService_saveOrUpdate.sql")
    public void test_saveOrUpdate() throws Exception {
        try {
            String uuid = "uuid";
            String configUuid = "configUuid";
            Date executeDate = DateUtils.getToday();
            String repaymentPlanUuid = "repaymentPlanUuid";
            String financialContractUuid = "financialContractUuid";
            String contractUuid = "contractUuid";
            String customerUuid = "customerUuid";
            String workParams = "workParams";

            DelayProcessingTask task = new DelayProcessingTask();
            task.setUuid(uuid);
			task.setConfigUuid(configUuid);
            task.setCreateTime(new Date());
            task.setLastModifyTime(new Date());
            task.setTaskExecuteDate(executeDate);
            task.setRepaymentPlanUuid(repaymentPlanUuid);
            task.setFinancialContractUuid(financialContractUuid);
            task.setContractUuid(contractUuid);
            task.setCustomerUuid(customerUuid);
            task.setWorkParams(workParams);

            delayProcessingTaskDBService.saveOrUpdate(task);

            DelayProcessingTask taskInDB = delayProcessingTaskDBService.getByUuid(uuid);
            Assert.assertNotNull(taskInDB);
            Assert.assertEquals(repaymentPlanUuid, taskInDB.getRepaymentPlanUuid());
            Assert.assertEquals(financialContractUuid, taskInDB.getFinancialContractUuid());
            Assert.assertEquals(contractUuid, taskInDB.getContractUuid());
            Assert.assertEquals(customerUuid, taskInDB.getCustomerUuid());
            Assert.assertEquals(workParams, taskInDB.getWorkParams());
        } catch (SunException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    @Sql("classpath:test/yunxin/delayTask/delayProcessingTaskDBService_delete.sql")
    public void test_delete() throws Exception {
        String delayProcessingTaskUuid = "uuid";
        delayProcessingTaskDBService.delete(delayProcessingTaskUuid);

        DelayProcessingTask taskInDB = delayProcessingTaskDBService.getByUuid(delayProcessingTaskUuid);
        Assert.assertNull(taskInDB);
    }

    @Test
    @Sql("classpath:test/yunxin/delayTask/delayProcessingTaskDBService_getByUuid.sql")
    public void test_getByUuid() throws Exception {

        DelayProcessingTask taskInDB1 = delayProcessingTaskDBService.getByUuid("");
        Assert.assertNull(taskInDB1);

        DelayProcessingTask taskInDB2 = delayProcessingTaskDBService.getByUuid(null);
        Assert.assertNull(taskInDB2);

        String uuid = "uuid";
        String repaymentPlanUuid = "repaymentPlanUuid";
        String financialContractUuid = "financialContractUuid";
        String contractUuid = "contractUuid";
        String customerUuid = "customerUuid";
        String workParams = "workParams";

        DelayProcessingTask taskInDB = delayProcessingTaskDBService.getByUuid(uuid);
        Assert.assertNotNull(taskInDB);
        Assert.assertEquals(repaymentPlanUuid, taskInDB.getRepaymentPlanUuid());
        Assert.assertEquals(financialContractUuid, taskInDB.getFinancialContractUuid());
        Assert.assertEquals(contractUuid, taskInDB.getContractUuid());
        Assert.assertEquals(customerUuid, taskInDB.getCustomerUuid());
        Assert.assertEquals(workParams, taskInDB.getWorkParams());
    }

    @Test
    @Sql("classpath:test/yunxin/delayTask/delayProcessingTaskDBService_get_by_repaymentPlanUuid.sql")
    public void test_get_by_repaymentPlanUuid() {
        String repaymentPlanUuid = "repaymentPlanUuid";
        List<DelayProcessingTask> taskList = delayProcessingTaskDBService.get_by_repaymentPlanUuid
                (repaymentPlanUuid);
        Assert.assertTrue(CollectionUtils.isNotEmpty(taskList));
        Assert.assertEquals(1, taskList.size());
        DelayProcessingTask taskInDB = taskList.get(0);

        Assert.assertNotNull(taskInDB);

        String uuid = "uuid";
        String financialContractUuid = "financialContractUuid";
        String contractUuid = "contractUuid";
        String customerUuid = "customerUuid";
        String workParams = "workParams";

        Assert.assertEquals(uuid, taskInDB.getUuid());
        Assert.assertEquals(repaymentPlanUuid, taskInDB.getRepaymentPlanUuid());
        Assert.assertEquals(financialContractUuid, taskInDB.getFinancialContractUuid());
        Assert.assertEquals(contractUuid, taskInDB.getContractUuid());
        Assert.assertEquals(customerUuid, taskInDB.getCustomerUuid());
        Assert.assertEquals(workParams, taskInDB.getWorkParams());
    }

    @Test
    @Sql("classpath:test/yunxin/delayTask/delayProcessingTaskDBService_get_by_repurchaseDocUuid.sql")
    public void test_get_by_repurchaseDocUuid() {
        String repurchaseDocUuid = "repurchaseDocUuid";
        List<DelayProcessingTask> taskList = delayProcessingTaskDBService.get_by_repurchaseDocUuid(repurchaseDocUuid);

        Assert.assertTrue(CollectionUtils.isNotEmpty(taskList));
        Assert.assertEquals(1, taskList.size());
        DelayProcessingTask taskInDB = taskList.get(0);

        Assert.assertNotNull(taskInDB);

        String uuid = "uuid";
        String financialContractUuid = "financialContractUuid";
        String contractUuid = "contractUuid";
        String customerUuid = "customerUuid";
        String workParams = "workParams";

        Assert.assertEquals(uuid, taskInDB.getUuid());
        Assert.assertTrue(StringUtils.isEmpty(taskInDB.getRepaymentPlanUuid()));

        Assert.assertEquals(financialContractUuid, taskInDB.getFinancialContractUuid());
        Assert.assertEquals(contractUuid, taskInDB.getContractUuid());
        Assert.assertEquals(customerUuid, taskInDB.getCustomerUuid());
        Assert.assertEquals(workParams, taskInDB.getWorkParams());
    }

    @Test
    @Sql("classpath:test/yunxin/delayTask/delayProcessingTaskDBService_get_by_repurchaseDocUuid.sql")
    public void test_get_by_financial_contract() {
        String  fcUuid = "financialContractUuid";
        List<DelayProcessingTask> taskList = delayProcessingTaskDBService.getDelayProcessingTaskByFinancialContract(fcUuid);

        Assert.assertTrue(CollectionUtils.isNotEmpty(taskList));
        Assert.assertEquals(1, taskList.size());
        DelayProcessingTask taskInDB = taskList.get(0);

        Assert.assertNotNull(taskInDB);

        String uuid = "uuid";
        String financialContractUuid = "financialContractUuid";
        String contractUuid = "contractUuid";
        String customerUuid = "customerUuid";
        String workParams = "workParams";

        Assert.assertEquals(uuid, taskInDB.getUuid());
        Assert.assertTrue(StringUtils.isEmpty(taskInDB.getRepaymentPlanUuid()));

        Assert.assertEquals(financialContractUuid, taskInDB.getFinancialContractUuid());
        Assert.assertEquals(contractUuid, taskInDB.getContractUuid());
        Assert.assertEquals(customerUuid, taskInDB.getCustomerUuid());
        Assert.assertEquals(workParams, taskInDB.getWorkParams());
    }

    @Test
    @Sql("classpath:test/yunxin/delayTask/deleteDelayProcessingTask.sql")
    public void test_delDelayProcessingTask() throws Exception {
        String  configUuid = "config_uuid";
        String uuid = "uuid";
        DelayProcessingTaskLog task = new DelayProcessingTaskLog();
        task.setContractUuid(configUuid);
        task.setUuid(uuid);
        List<DelayProcessingTaskLog> list = new ArrayList();
        list.add(task);
        delayProcessingTaskDBService.delDelayProcessingTask(list);
        DelayProcessingTask tasks = delayProcessingTaskDBService.getByUuid("uuid");
        Assert.assertEquals(null, tasks);
    }

    @Test
    @Sql("classpath:test/yunxin/delayTask/delayProcessingTaskDBService_get_by_repurchaseDocUuid.sql")
    public void test_get_by_financial_contract_uuid_And_config_Uuid() {
        String  fcUuid = "financialContractUuid";
        List<DelayProcessingTask> taskList = delayProcessingTaskDBService.getByConfigUuid("config_uuid",fcUuid);

        Assert.assertTrue(CollectionUtils.isNotEmpty(taskList));
        Assert.assertEquals(1, taskList.size());
        DelayProcessingTask taskInDB = taskList.get(0);

        Assert.assertNotNull(taskInDB);

        String uuid = "uuid";
        String financialContractUuid = "financialContractUuid";
        String contractUuid = "contractUuid";
        String customerUuid = "customerUuid";
        String workParams = "workParams";

        Assert.assertEquals(uuid, taskInDB.getUuid());
        Assert.assertTrue(StringUtils.isEmpty(taskInDB.getRepaymentPlanUuid()));

        Assert.assertEquals(financialContractUuid, taskInDB.getFinancialContractUuid());
        Assert.assertEquals(contractUuid, taskInDB.getContractUuid());
        Assert.assertEquals(customerUuid, taskInDB.getCustomerUuid());
        Assert.assertEquals(workParams, taskInDB.getWorkParams());
    }

}