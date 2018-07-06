package com.suidifu.barclays.config.redis;

import com.suidifu.barclays.config.redis.serializer.kryo.KryoRedisSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author dafuchen
 *         2017/12/25
 */
@Configuration
public class RedisOperationConfig {

    @Bean
    public RedisTemplate cashFlowRedisTemplate(JedisConnectionFactory jedisConnectionFactory,
                                               KryoRedisSerializer kryoRedisSerializer,
                                               StringRedisSerializer stringRedisSerializer) {
        RedisTemplate redisTemplate = new RedisTemplate();
        redisTemplate.setConnectionFactory(jedisConnectionFactory);
        redisTemplate.setDefaultSerializer(kryoRedisSerializer);
        redisTemplate.setKeySerializer(stringRedisSerializer);
        return redisTemplate;
    }

    @Bean
    public ZSetOperations cashFlowZSetOperations(RedisTemplate cashFlowRedisTemplate) {
        return cashFlowRedisTemplate.opsForZSet();
    }
}
