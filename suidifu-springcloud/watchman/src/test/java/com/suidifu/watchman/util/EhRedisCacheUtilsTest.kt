package com.suidifu.watchman.util

import com.suidifu.watchman.WatchManTests
import org.apache.commons.lang3.StringUtils
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.slf4j.LoggerFactory
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.cache.CacheManager
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner
import javax.annotation.Resource

/**
 * @author louguanyang at 2018/1/12 11:51
 * @mail louguanyang@hzsuidifu.com
 */
@RunWith(SpringRunner::class)
@ActiveProfiles(value = "junit")
@SpringBootTest(classes = arrayOf(WatchManTests::class), webEnvironment = WebEnvironment.RANDOM_PORT)
class EhRedisCacheUtilsTest {

    companion object {
        private val LOGGER = LoggerFactory.getLogger(EhRedisCacheUtilsTest::class.java)
        private val CACHE_NAME = "test"
        private val KEY = "put"
        private val VALUE = "123456"
    }

    private var cacheUtils: EhRedisCacheUtils? = null

    @Resource
    private val cacheManager: CacheManager? = null

    @Before
    fun setUp() {
        cacheUtils = EhRedisCacheUtils.getInstance()
        if (cacheUtils != null) {
            cacheUtils!!.put(CACHE_NAME, KEY, VALUE)
        }
    }

    @After
    fun tearDown() {
        if (cacheUtils != null) {
            cacheUtils!!.evict(CACHE_NAME, KEY)
        }
        cacheUtils = null
    }

    @Test
    fun testInit() {

    }

    @Test
    fun testGetInstance() {
        try {
            Assert.assertNotNull(cacheUtils)
            val cacheUtils = EhRedisCacheUtils.getInstance()
            Assert.assertNotNull(cacheUtils)
        } catch (e: Exception) {
            Assert.fail()
        }

    }

    @Test
    fun testGetCache() {
        val cache = cacheUtils!!.getCache(CACHE_NAME)
        Assert.assertNotNull(cache)
    }

    @Test
    fun testGetCacheOfIllegalArgumentException() {
        try {
            cacheUtils!!.getCache(StringUtils.EMPTY)
            Assert.fail()
        } catch (e: IllegalArgumentException) {
            Assert.assertEquals("cache name is empty", e.message)
        } catch (e: Exception) {
            Assert.fail()
        }

        try {
            cacheUtils!!.getCache(null)
            Assert.fail()
        } catch (e: IllegalArgumentException) {
            Assert.assertEquals("cache name is empty", e.message)
        } catch (e: Exception) {
            Assert.fail()
        }

        val cacheName = "error"
        try {
            cacheUtils!!.getCache(cacheName)
            Assert.fail()
        } catch (e: IllegalArgumentException) {
            Assert.assertEquals("can not get cache by name:" + cacheName, e.message)
        } catch (e: Exception) {
            Assert.fail()
        }

    }

    @Test
    fun get() {
        val cache = cacheUtils!!.getCache(CACHE_NAME)
        Assert.assertEquals(CACHE_NAME, cache.name)

        val obj = cacheUtils!!.get(CACHE_NAME, KEY)
        Assert.assertNotNull(obj)
        Assert.assertEquals(VALUE, obj)

        val cacheName2 = "cacheName2"
        try {
            cacheUtils!!.getCache(cacheName2)
            Assert.fail()
        } catch (e: Exception) {
            Assert.assertEquals("can not get cache by name:" + cacheName2, e.message)
        }

        val obj2 = cacheUtils!!.get(cacheName2, KEY)
        Assert.assertNull(obj2)
    }

    @Test
    fun put() {
        val putKey = "putKey"
        val putSuccess = cacheUtils!!.put(CACHE_NAME, putKey, VALUE)
        Assert.assertTrue(putSuccess)

        val evict = cacheUtils!!.evict(CACHE_NAME, putKey)
        Assert.assertTrue(evict)

        val put = cacheUtils!!.put(null, putKey, VALUE)
        Assert.assertFalse(put)
    }

    @Test
    fun evict() {
        val obj = cacheUtils!!.get(CACHE_NAME, KEY)
        Assert.assertNotNull(obj)
        Assert.assertEquals(VALUE, obj)

        val evictSuccess = cacheUtils!!.evict(CACHE_NAME, KEY)
        Assert.assertTrue(evictSuccess)

        val obj2 = cacheUtils!!.get(CACHE_NAME, KEY)
        Assert.assertNull(obj2)
    }

}