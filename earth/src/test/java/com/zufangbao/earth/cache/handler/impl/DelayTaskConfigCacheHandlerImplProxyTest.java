package com.zufangbao.earth.cache.handler.impl;

import com.suidifu.matryoshka.cache.DelayTaskConfigCache;
import com.suidifu.matryoshka.customize.DelayTaskServices;
import com.suidifu.matryoshka.delayPosition.handler.DelayTaskConfigCacheHandler;
import com.suidifu.matryoshka.delayPosition.handler.DelayTaskConfigSourceCodeCompilerHandler;
import com.suidifu.matryoshka.delayTask.DelayProcessingTaskConfig;
import com.suidifu.matryoshka.delayTask.DelayTaskConfigCacheSpec;
import com.suidifu.xcode.handler.SourceCodeCompilerHandler;
import com.suidifu.xcode.util.CompilerContainer;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author louguanyang on 2017/5/11.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:/local/applicationContext-*.xml",
})
@Transactional
@TransactionConfiguration
public class DelayTaskConfigCacheHandlerImplProxyTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private DelayTaskConfigCacheHandler delayTaskConfigCacheHandler;

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Before
    public void init_cache() {
        delayTaskConfigCacheHandler.clearAll();
        stringRedisTemplate.delete("sr:delayTaskConfigUuid");
    }

    @After
    public void after() {
        delayTaskConfigCacheHandler.clearAll();
        stringRedisTemplate.delete("sr:delayTaskConfigUuid");
    }

    @Test
    @Sql("classpath:test/yunxin/delayTask/test_getConfigByUuid.sql")
    public void test_getConfigByUuid() {
        // error params
        DelayProcessingTaskConfig taskConfig1 = delayTaskConfigCacheHandler.getConfigByUuid("");
        Assert.assertNull(taskConfig1);
        DelayProcessingTaskConfig taskConfig2 = delayTaskConfigCacheHandler.getConfigByUuid(null);
        Assert.assertNull(taskConfig2);

        // read from DB
        String delayTaskConfigUuid3 = "delayTaskConfigUuid3";
        DelayProcessingTaskConfig taskConfig3InDB = delayTaskConfigCacheHandler.getConfigByUuid(delayTaskConfigUuid3);
        Assert.assertNotNull(taskConfig3InDB);
        Assert.assertEquals(delayTaskConfigUuid3, taskConfig3InDB.getUuid());


        // read from cache
        String delayTaskConfigUuid4 = "delayTaskConfigUuid4";
        Cache cache = cacheManager.getCache(DelayTaskConfigCacheSpec.CACHE_KEY);
        delayTaskConfigCacheHandler.clearAll();
        DelayProcessingTaskConfig taskConfig4 = new DelayProcessingTaskConfig();
        taskConfig4.setUuid(delayTaskConfigUuid4);
        DelayTaskConfigCache delayTaskConfigCache = new DelayTaskConfigCache(taskConfig4);
        cache.put(delayTaskConfigUuid4, delayTaskConfigCache);

        DelayProcessingTaskConfig taskConfig4InCache = delayTaskConfigCacheHandler.getConfigByUuid(delayTaskConfigUuid4);
        Assert.assertNotNull(taskConfig4InCache);
        Assert.assertEquals(delayTaskConfigUuid4, taskConfig4InCache.getUuid());
    }

    @Test
    public void test_getCompiledObject() {
        DelayProcessingTaskConfig taskConfig1 = null;
        Object compiledObject1 = delayTaskConfigCacheHandler.getCompiledObject(taskConfig1);
        Assert.assertNull(compiledObject1);

    }
    @Test
    @Sql("classpath:test/yunxin/delayTask/test_get_compiledObject_delayTaskConfigUuid.sql")
    public void test_get_compiledObject_delayTaskConfigUuid() {
        try {
            String delayTaskConfigUuid = "delayTaskConfigUuid";
            DelayTaskServices delayTaskServices = (DelayTaskServices)delayTaskConfigCacheHandler.getCompiledObjectDelayTaskConfigUuid(delayTaskConfigUuid);
            Assert.assertNotNull(delayTaskServices);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
    }
    @Test
    public void test_register() {
        String businessType = "businessType";
        SourceCodeCompilerHandler sourceCodeCompilerHandler = new DelayTaskConfigSourceCodeCompilerHandler();
        delayTaskConfigCacheHandler.register(businessType, sourceCodeCompilerHandler);
        boolean containsKey = CompilerContainer.compilerContainer.containsKey(businessType);
        Assert.assertTrue(containsKey);
    }

}