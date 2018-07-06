package com.suidifu.dowjones.config;

import com.suidifu.dowjones.utils.GenericJdbcSupport;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import javax.annotation.Resource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import org.springframework.context.annotation.Primary;

/**
 * code is far away from bug with the animal protecting
 * Created by veda on 05/01/2018.
 */

@Configuration
public class JdbcConfig {

    @Bean(name = "cfg_datasource")
    @ConfigurationProperties(prefix = "hikaricp")
    public HikariConfig hikariConfig() {
        return new HikariConfig();
    }

    @Resource(name = "cfg_datasource")
    private HikariConfig hikariConfig;

    @Primary
    @Bean(name = "dataSource", destroyMethod = "close")
    public DataSource dataSource() {
        return new HikariDataSource(hikariConfig);
    }

    @Bean
    public GenericJdbcSupport genericJdbcSupport(DataSource datasource) {
        GenericJdbcSupport genericJdbcSupport = new GenericJdbcSupport();
        genericJdbcSupport.setDataSource(datasource);
        return genericJdbcSupport;
    }

}
