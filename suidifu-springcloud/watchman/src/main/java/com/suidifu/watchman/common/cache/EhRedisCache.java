package com.suidifu.watchman.common.cache;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.ehcache.CacheException;
import net.sf.ehcache.Element;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;
import org.springframework.data.redis.core.RedisTemplate;

import javax.validation.constraints.NotNull;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * Ehcache + Redis 二级缓存
 *
 * @author louguanyang at 2018/1/4 11:01
 * @mail louguanyang@hzsuidifu.com
 */
@Data
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
public class EhRedisCache implements Cache {

    /**
     * Cache 默认存活时间 600 秒
     */
    private static final Long DEFAULT_TIME_TO_LIVE_SECONDS = 600L;
    private static final String CACHE_KEY_IS_EMPTY = "cache key is empty";
    private static final String CACHE_VALUE_IS_NULL = "cache value is null";
    /**
     * Cache Name
     */
    private String name;

    /**
     * Ehcache
     */
    private net.sf.ehcache.Cache ehCache;

    /**
     * Redis
     */
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * Cache 存活时间
     */
    private long timeToLiveSeconds = DEFAULT_TIME_TO_LIVE_SECONDS;

    /**
     * 缓存名字
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * 得到底层使用的缓存
     */
    @Override
    public Object getNativeCache() {
        return this;
    }

    /**
     * 根据key得到一个ValueWrapper，然后调用其get方法获取值
     */
    @Override
    @org.jetbrains.annotations.Contract("null -> fail")
    public ValueWrapper get(Object key) {
        checkCacheArgument(key);
        ValueWrapper ehCacheObject = getValueWrapperFromEhCache(key);
        if (ehCacheObject != null) {
            return ehCacheObject;
        }
        return getValueWrapperFromRedis(key);
    }

    /**
     * 从EhCache 获取缓存数据
     *
     * @param key key
     * @return 缓存对象
     */
    @org.jetbrains.annotations.Nullable
    private ValueWrapper getValueWrapperFromEhCache(@org.jetbrains.annotations.NotNull Object key) {
        Element element = ehCache.get(key);
        if (element == null) {
            return null;
        }
        Object object = element.getObjectValue();
        if (object == null) {
            return null;
        }
        return new SimpleValueWrapper(object);
    }

    /**
     * 从 Redis 获取缓存数据
     *
     * @param key key
     * @return 缓存对象
     */
    @org.jetbrains.annotations.Nullable
    private ValueWrapper getValueWrapperFromRedis(@org.jetbrains.annotations.NotNull Object key) {
        Object object = getFromRedis(key);
        if (object == null) {
            return null;
        }
        putToEhCache(key, object);
        return new SimpleValueWrapper(object);
    }

    @org.jetbrains.annotations.Nullable
    private Object getFromRedis(Object key) {
        try {
            if (key == null) {
                return null;
            }
            String redisKey = String.valueOf(key);
            redisTemplate.boundValueOps(redisKey).expire(timeToLiveSeconds, TimeUnit.SECONDS);
            return redisTemplate.boundValueOps(redisKey).get();
        } catch (Exception e) {
            log.error("getFromRedis occur error", e);
            return null;
        }
    }

    @Override
    public <T> T get(Object key, Class<T> aClass) {
        try {
            ValueWrapper valueWrapper = get(key);
            Object obj = valueWrapper.get();
            return aClass.cast(obj);
        } catch (Exception e) {
            log.error("occur error", e);
            return null;
        }
    }

    @Override
    public <T> T get(Object key, Callable<T> callable) {
        return null;
    }

    @Override
    public void put(Object key, Object value) {
        checkCacheArgument(key);
        checkCacheArgument(value);
        putToEhCache(key, value);
        putToRedis(key, value);
    }

    @org.jetbrains.annotations.Contract("null -> fail")
    private void checkCacheArgument(Object value) {
        if (value == null) {
            throw new IllegalArgumentException("argument is null");
        }
        if (value instanceof String) {
            if (StringUtils.isEmpty((String) value)) {
                throw new IllegalArgumentException("argument is empty");
            }
        }
    }

    @Override
    public ValueWrapper putIfAbsent(Object key, Object value) {
        try {
            put(key, value);
            return new SimpleValueWrapper(value);
        } catch (Exception e) {
            log.error("putIfAbsent error", e);
            return null;
        }
    }

    /**
     * 从缓存中移除key对应的缓存
     */
    @Override
    public void evict(Object key) {
        evictEhCache(key);
        evictRedis(key);
    }

    /**
     * 清空缓存
     */
    @Override
    public void clear() {
        // 清空缓存代码 待续
    }

    private void putToRedis(@NotNull Object key, @NotNull Object value) {
        try {
            String redisKey = String.valueOf(key);
            redisTemplate.boundValueOps(redisKey).set(value, timeToLiveSeconds, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("putToRedis error, stackTrace\n", e);
            throw e;
        }
    }

    /**
     * 缓存到 ehcache
     *
     * @param key   key
     * @param value value
     */
    private void putToEhCache(Object key, Object value) {
        try {
            ehCache.put(new Element(key, value));
        } catch (IllegalArgumentException | IllegalStateException | CacheException e) {
            log.error("putToEhCache error, stackTrace\n", e);
            throw e;
        }
    }

    /**
     * evict Redis by key
     */
    private void evictRedis(Object key) {
        redisTemplate.delete(String.valueOf(key));
    }

    /**
     * evict EhCache by key
     */
    private void evictEhCache(Object key) {
        ehCache.remove(key);
    }

}
