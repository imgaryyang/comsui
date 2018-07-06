package com.suidifu.watchman.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Sets;
import com.suidifu.watchman.common.cache.EhRedisCache;
import com.suidifu.watchman.util.EhRedisCacheUtils;
import com.suidifu.watchman.util.Md5Utils;
import lombok.extern.slf4j.Slf4j;
import net.sf.ehcache.config.CacheConfiguration;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import javax.annotation.Resource;
import java.util.Set;

/**
 * EhRedisCache 配置
 *
 * @author louguanyang at 2018/1/4 16:18
 * @mail louguanyang@hzsuidifu.com
 */
@Slf4j
@EnableCaching
@Configuration
@EnableAutoConfiguration
public class EhRedisCacheConfig extends CachingConfigurerSupport {

    private static final String EH_REDIS_CACHE_KEY = "ehRedisCache:";

    @Resource
    private JedisConnectionFactory jedisConnectionFactory;

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(
                Object.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);

        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(jedisConnectionFactory);
        //使用StringRedisSerializer来序列化和反序列化redis的key值
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        //使用Jackson2JsonRedisSerializer来序列化和反序列化redis的value值
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.afterPropertiesSet();
        log.info("AutoConfig redisTemplate Success");
        return redisTemplate;
    }

    @Bean
    public EhCacheManagerFactoryBean ehCacheManagerFactoryBean() {
        EhCacheManagerFactoryBean ehCacheManagerFactoryBean = new EhCacheManagerFactoryBean();
        log.info("AutoConfig EhCacheManagerFactoryBean Success");
        return ehCacheManagerFactoryBean;
    }

    @Bean
    public CacheManager cacheManager(RedisTemplate<String, Object> redisTemplate) {
        SimpleCacheManager cacheManager = buildEhRedisCacheManager(redisTemplate);
        initEhRedisCacheUtils(cacheManager);
        return cacheManager;
    }

    @NotNull
    private SimpleCacheManager buildEhRedisCacheManager(RedisTemplate<String, Object> redisTemplate) {
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        Set<Cache> cacheSet = Sets.newHashSet();
        net.sf.ehcache.CacheManager ehcacheManager = ehCacheManagerFactoryBean().getObject();
        for (String ehcacheName : ehcacheManager.getCacheNames()) {
            EhRedisCache ehRedisCache = buildEhRedisCache(redisTemplate, ehcacheManager, ehcacheName);
            cacheSet.add(ehRedisCache);
        }
        cacheManager.setCaches(cacheSet);
        log.info("AutoConfig CacheManager Success");
        return cacheManager;
    }

    private void initEhRedisCacheUtils(SimpleCacheManager cacheManager) {
        EhRedisCacheUtils.init(cacheManager);
        log.info("EhRedisCacheUtils init Success");
    }

    @NotNull
    private EhRedisCache buildEhRedisCache(RedisTemplate<String, Object> redisTemplate,
                                           net.sf.ehcache.CacheManager ehcacheManager, String ehcacheName) {
        EhRedisCache ehRedisCache = new EhRedisCache();
        ehRedisCache.setName(ehcacheName);
        ehRedisCache.setRedisTemplate(redisTemplate);

        net.sf.ehcache.Cache ehCache = ehcacheManager.getCache(ehcacheName);
        ehRedisCache.setEhCache(ehCache);

        CacheConfiguration cacheConfiguration = ehCache.getCacheConfiguration();
        long timeToLiveSeconds = cacheConfiguration.getTimeToLiveSeconds();
        if (timeToLiveSeconds > 0) {
            // timeToLiveSeconds aways 0
            // FIXME get timeToLiveSeconds from CacheConfiguration
            ehRedisCache.setTimeToLiveSeconds(timeToLiveSeconds);
        }
        return ehRedisCache;
    }

    @Override
    public KeyGenerator keyGenerator() {
        return (target, method, objects) -> {
            StringBuilder sb = new StringBuilder();
            String className = target.getClass().getName();
            sb.append(className);
            sb.append(method.getName());
            for (Object object : objects) {
                sb.append(object.toString());
            }
            String originKey = sb.toString();
            String md5Key = Md5Utils.md5(originKey);
            if (log.isDebugEnabled()) {
                log.debug("origin key:{}, md5 key:{}", originKey, md5Key);
            }
            return EH_REDIS_CACHE_KEY.concat(md5Key);
        };
    }
}
