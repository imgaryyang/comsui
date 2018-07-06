package com.suidifu.watchman.util;

import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.CacheManager;

import java.util.Optional;

/**
 * EhRedisCache
 *
 * @author louguanyang at 2018/1/10 23:26
 * @mail louguanyang@hzsuidifu.com
 */
@Slf4j
public class EhRedisCacheUtils {

    private static EhRedisCacheUtils ehRedisCacheUtils = null;
    private static boolean isInit = false;
    private CacheManager cacheManager;

    private EhRedisCacheUtils() {
    }

    /**
     * 初始化缓存工具类
     */
    public static synchronized void init(CacheManager cacheManager) {
        if (ehRedisCacheUtils != null) {
            return;
        }
        if (isInit) {
            return;
        }
        Preconditions.checkState(cacheManager != null, "cacheManager is null");
        ehRedisCacheUtils = new EhRedisCacheUtils();
        ehRedisCacheUtils.setCacheManager(cacheManager);
        isInit = true;
    }

    @org.jetbrains.annotations.Contract(pure = true)
    public static EhRedisCacheUtils getInstance() {
        Optional<EhRedisCacheUtils> optional = Optional.of(ehRedisCacheUtils);
        return optional.orElseGet(() -> {
            throw new IllegalStateException("EhRedisCacheUtils not init");
        });
    }

    private void setCacheManager(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    /**
     * get Object from cache
     *
     * @param cacheName 缓存名称
     * @param key       缓存key
     * @return 缓存对象
     */
    public Object get(String cacheName, Object key) {
        try {
            ValueWrapper valueWrapper = getCache(cacheName).get(key);
            if (valueWrapper == null) {
                return null;
            }
            return valueWrapper.get();
        } catch (Exception e) {
            log.error("getObjectFromCache occur error, cacheName:{}, key:{}",
                    cacheName, String.valueOf(key), e);
            return null;
        }
    }

    /**
     * put Object to cache
     *
     * @param cacheName 缓存名称
     * @param key       缓存key
     * @param value     缓存对象
     * @return 是否成功
     */
    public boolean put(String cacheName, Object key, Object value) {
        try {
            getCache(cacheName).put(key, value);
            return true;
        } catch (Exception e) {
            log.error("putObjectToCache occur error, cacheName:{}, key:{}, value:{}",
                    cacheName, String.valueOf(key), String.valueOf(value), e);
            return false;
        }
    }

    /**
     * evict Cache by key
     *
     * @param cacheName 缓存名称
     * @param key       缓存key
     * @return 是否成功
     */
    public boolean evict(String cacheName, Object key) {
        try {
            getCache(cacheName).evict(key);
            return true;
        } catch (Exception e) {
            log.error("evictCache occur error, cacheName:{}, key:{}", cacheName, String.valueOf(key), e);
            return false;
        }
    }

    public Cache getCache(String cacheName) {
        Preconditions.checkArgument(StringUtils.isNotEmpty(cacheName), "cache name is empty");
        Optional<Cache> optional = Optional.ofNullable(cacheManager.getCache(cacheName));
        return optional.orElseGet(() -> {
            throw new IllegalArgumentException("can not get cache by name:".concat(cacheName));
        });
    }
}
