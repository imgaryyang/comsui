package com.suidifu.dowjones.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * @author: frankwoo(吴峻申) <br>
 * @date: 2017/12/13 <br>
 * @time: 14:27 <br>
 * @mail: frank_wjs@hotmail.com <br>
 */
@Configuration
public class CORSConfig {
    private CorsConfiguration buildConfig() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("*"); //allow all cross origin
        corsConfiguration.addAllowedHeader("*"); //allow all header
        corsConfiguration.addAllowedMethod("*"); //allow all http method
        corsConfiguration.setAllowCredentials(true); //允许保留跨域请求凭证
        return corsConfiguration;
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", buildConfig());
        return new CorsFilter(source);
    }
}