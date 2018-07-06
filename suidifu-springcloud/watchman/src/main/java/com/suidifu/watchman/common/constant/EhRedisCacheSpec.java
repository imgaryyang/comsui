package com.suidifu.watchman.common.constant;

/**
 * @author louguanyang at 2018/1/4 12:03
 * @mail louguanyang@hzsuidifu.com
 */
public class EhRedisCacheSpec {

    /**
     * 一级缓存名
     */
    public static final String CACHE_LV1_NAME = "Cache_LV1(ehcache)";
    /**
     * 二级缓存名
     */
    public static final String CACHE_LV2_NAME = "Cache_LV2(redis)";

    private EhRedisCacheSpec() {
        throw new IllegalStateException("Constant class");
    }
}
