package com.suidifu.watchman.util

import org.apache.commons.lang3.StringUtils
import org.junit.Assert
import org.junit.Test
import org.slf4j.LoggerFactory

/**
 * @author louguanyang at 2018/1/4 16:58
 * @mail louguanyang@hzsuidifu.com
 */
class Md5UtilsTest {

    companion object {
        private val log = LoggerFactory.getLogger(Md5UtilsTest::class.java)
    }

    /**
     * 空值Md5
     */
    @Test
    fun testMd5Empty() {
        val origin = StringUtils.EMPTY
        val md5Result = Md5Utils.md5(origin)
        log.info("【{}】一次Md5的结果:{}", origin, md5Result)
        Assert.assertEquals(origin, md5Result)
    }

    /**
     * 一次不加盐Md5
     */
    @Test
    fun testMd5() {
        val origin = "123456"
        val expected = "e10adc3949ba59abbe56e057f20f883e"
        val md5Result = Md5Utils.md5(origin)
        log.info("【{}】一次Md5的结果:{}", origin, md5Result)
        Assert.assertEquals(expected, md5Result)
    }

    /**
     * 一次加盐Md5
     */
    @Test
    fun testMd5WithSalt() {
        val origin = "123456"
        val salt = "abc"
        val errorExpected = "e10adc3949ba59abbe56e057f20f883e"
        val expected = "0659c7992e268962384eb17fafe88364"
        val md5Result = Md5Utils.md5(origin, salt)
        log.info("【{}】一次Md5加盐【{}】的结果:{}", origin, salt, md5Result)
        Assert.assertNotEquals(errorExpected, md5Result)
        Assert.assertEquals(expected, md5Result)
    }

    /**
     * 循环两次MD5
     */
    @Test
    fun testIterationsMd5() {
        val origin = "123456"
        val iterations = 2
        val expected = "4280d89a5a03f812751f504cc10ee8a5"
        val md5Result = Md5Utils.md5(origin, iterations)
        log.info("【{}】{}次Md5的结果:{}", origin, iterations, md5Result)
        Assert.assertEquals(expected, md5Result)
    }

    /**
     * 手动多次Md5与循环Md5结果比较
     */
    @Test
    fun encodeMd5() {
        val origin = "123456"
        val md5Result1 = Md5Utils.md5(origin)
        log.info("【{}】一次Md5的结果:{}", origin, md5Result1)
        val md5Result2 = Md5Utils.md5(md5Result1)
        log.info("【{}】一次Md5的结果, 再做一次Md5:{}", origin, md5Result2)
        val iterations = 2
        val md5Two = Md5Utils.md5(origin, iterations)
        val expected = "4280d89a5a03f812751f504cc10ee8a5"
        log.info("【{}】{}次Md5的结果:{}", origin, iterations, md5Two)
        Assert.assertNotEquals(md5Result2, md5Two)
        Assert.assertEquals(expected, md5Two)
    }

    /**
     * 循环128次md5
     */
    @Test
    fun testMd5With128Times() {
        val origin = "123456"
        val iterations = 128
        val expected = "26f75aaf21d1dae0f9074f5502808408"
        val md5Result = Md5Utils.md5(origin, iterations)
        log.info("【{}】{}次Md5的结果:{}", origin, iterations, md5Result)
        Assert.assertEquals(expected, md5Result)
    }

    /**
     * 循环10000次 Md5
     */
    @Test
    fun testMd5With10000Time() {
        val origin = "123456"
        val iterations = 10000
        val expected = "0d529a42cbebd3943ad4709d8dea32a2"
        val md5Result = Md5Utils.md5(origin, iterations)
        log.info("【{}】{}次Md5的结果:{}", origin, iterations, md5Result)
        Assert.assertEquals(expected, md5Result)
    }

    /**
     * 加盐循环128次MD5
     */
    @Test
    fun testMd5WithSalt128Times() {
        val origin = "123456"
        val iterations = 128
        val salt = "abc"
        val errorExpected = "26f75aaf21d1dae0f9074f5502808408"
        val expected = "3d0d972612442ae1f987b109c7645940"
        val md5Result = Md5Utils.md5(origin, salt, iterations)
        log.info("【{}】{}次Md5的结果:{}", origin, iterations, md5Result)
        Assert.assertNotEquals(errorExpected, md5Result)
        Assert.assertEquals(expected, md5Result)
    }

    /**
     * 查看 获取方法名 方法的效率
     */
    @Test
    fun testGetMethodName() {
        val start = System.currentTimeMillis()
        val times = 10000
        for (i in 0 until times) {
            val methodName = Thread.currentThread().stackTrace[1].methodName
            val methodName2 = Thread.currentThread().stackTrace[2].methodName
            log.info("methodName:{}", methodName)
            log.info("methodName2:{}", methodName2)
        }
        log.info("run:{} times, use {} ms", times, System.currentTimeMillis() - start)
    }
}