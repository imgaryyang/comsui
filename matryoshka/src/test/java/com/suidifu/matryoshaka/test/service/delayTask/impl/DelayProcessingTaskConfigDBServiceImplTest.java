package com.suidifu.matryoshaka.test.service.delayTask.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.suidifu.matryoshka.delayTask.DelayProcessingTaskConfig;
import com.suidifu.matryoshka.service.delayTask.DelayProcessingTaskConfigDBService;

/**
 *
 * Created by louguanyang on 2017/5/3.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={

        "classpath:/local/applicationContext-*.xml",
})
@Transactional
@TransactionConfiguration
public class DelayProcessingTaskConfigDBServiceImplTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private DelayProcessingTaskConfigDBService delayProcessingTaskConfigService;

    @Test
    @Sql("classpath:test/yunxin/delayTask/test_get_by_uuid.sql")
    public void test_get_by_uuid() {
        DelayProcessingTaskConfig config = delayProcessingTaskConfigService.getValidConfig("");
        assertNull(config);

        DelayProcessingTaskConfig config2 = delayProcessingTaskConfigService.getValidConfig(null);
        assertNull(config2);

        String invalid_uuid = "invalid_uuid";
        DelayProcessingTaskConfig config3 = delayProcessingTaskConfigService.getValidConfig(invalid_uuid);
        assertNull(config3);

        String uuid = "uuid";
        DelayProcessingTaskConfig config4 = delayProcessingTaskConfigService.getValidConfig(uuid);
        assertNotNull(config4);
        assertEquals("md5", config4.getExecuteCodeVersion());
    }
}