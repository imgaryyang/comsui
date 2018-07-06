package com.suidifu.watchman.common.constant;

/**
 * EhRedisCacheException 常量
 *
 * @author louguanyang at 2018/1/4 11:50
 * @mail louguanyang@hzsuidifu.com
 */
public class EhRedisCacheExceptionSpec {

    public static final int DEFAULT_ERROR_CODE = -1;
    /**
     * Redis 未配置
     */
    public static final String ERROR_REDIS_NOT_CONFIG = "redis not config";
    /**
     * Redis 未配置
     */
    public static final String EH_CACHE_NOT_CONFIG = "ehCache not config";

    private EhRedisCacheExceptionSpec() {
        throw new IllegalStateException("Constant class");
    }
}
