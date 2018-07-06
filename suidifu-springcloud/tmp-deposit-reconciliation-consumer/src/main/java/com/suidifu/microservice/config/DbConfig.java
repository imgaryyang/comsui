package com.suidifu.microservice.config;

import com.demo2do.core.persistence.GenericDaoSupport;
import com.demo2do.core.persistence.GenericJdbcSupport;
import javax.annotation.Resource;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * @author louguanyang at 2018/2/27 17:12
 * @mail louguanyang@hzsuidifu.com
 */
@Configuration
public class DbConfig {

  @Resource
  private DataSource dataSource;

  @Bean(name = "genericJdbcSupport", autowire = Autowire.BY_TYPE)
  public GenericJdbcSupport genericJdbcSupport() {
    GenericJdbcSupport genericJdbcSupport = new GenericJdbcSupport();
    genericJdbcSupport.setDataSource(dataSource);
    return genericJdbcSupport;
  }

  @Primary
  @Bean(name = "genericDaoSupport", autowire = Autowire.BY_TYPE)
  public GenericDaoSupport genericDaoSupport() {
    return new GenericDaoSupport();
  }
}