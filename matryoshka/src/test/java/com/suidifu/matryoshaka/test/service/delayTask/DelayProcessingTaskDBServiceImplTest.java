package com.suidifu.matryoshaka.test.service.delayTask;

import com.suidifu.matryoshka.customize.DelayTaskServices;
import com.suidifu.matryoshka.delayPosition.handler.DelayTaskConfigCacheHandler;
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
import org.springframework.beans.factory.annotation.Qualifier;
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
    @Autowired
    @Qualifier("delayTaskConfigCacheHandler")
    private DelayTaskConfigCacheHandler delayTaskConfigCacheHandler;
    String uuids[] = new String[]{
            "598ef68c-3927-11e7-ac39-2b4f0a474890",
            "8ffcaf61-3940-11e7-ab82-525400dbb013",
//            "back374da0c4-3935-11e7-952e-ba77244e1da4",
//            "back402fd71a-393b-11e7-bf99-00163e002839",
            "8aec6aa6-46ac-11e7-881a-b208103af10b",
            "18be8bf0-d4d2-11e7-a83d-502b73c136df",
            "4d77e688-d4d2-11e7-a83d-502b73c136df",
            "374da0c4-3935-11e7-952e-ba77244e1da4",
            "402fd71a-393b-11e7-bf99-00163e002839"
    };

    @Test
    public void test() throws Exception {
        for (String uuid : uuids) {
            DelayTaskServices delayTaskServices = (DelayTaskServices) delayTaskConfigCacheHandler.getCompiledObjectDelayTaskConfigUuid(uuid);
            System.out.println(uuid);
            Assert.assertNotNull(delayTaskServices);
            System.out.println(delayTaskServices);
        }
    }

}