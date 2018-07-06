package com.zufangbao.earth.config.redis;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author dafuchen
 *         2017/12/29
 */
@Configuration
public class RedisSerializerConfig {
    @Bean
    public StringRedisSerializer stringRedisSerializer() {
        return new StringRedisSerializer();
    }
}
