package com.zufangbao.earth.config;

import com.esotericsoftware.kryo.Kryo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author dafuchen
 *         2017/12/29
 */
@Configuration
public class ThreadLocalConfig {
    @Bean
    public ThreadLocal<Kryo> kryoThreadLocal() {
        return new ThreadLocal<>();
    }
}
