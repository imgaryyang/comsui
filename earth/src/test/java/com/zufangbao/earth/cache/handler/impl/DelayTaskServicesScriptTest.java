package com.zufangbao.earth.cache.handler.impl;

import com.suidifu.matryoshka.delayPosition.handler.DelayProcessingTaskCacheHandler;
import com.suidifu.matryoshka.delayPosition.handler.DelayTaskConfigCacheHandler;
import com.suidifu.matryoshka.handler.SandboxDataSetHandler;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author louguanyang on 2017/5/11.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:/local/applicationContext-*.xml" })
@Transactional
public class DelayTaskServicesScriptTest extends AbstractTransactionalJUnit4SpringContextTests {
    @Autowired
    @Qualifier("delayTaskConfigCacheHandler")
    private DelayTaskConfigCacheHandler delayTaskConfigCacheHandler;

    @Autowired
    private DelayProcessingTaskCacheHandler delayProcessingTaskHandler;

    @Autowired
    private SandboxDataSetHandler sandboxDataSetHandler;


}
