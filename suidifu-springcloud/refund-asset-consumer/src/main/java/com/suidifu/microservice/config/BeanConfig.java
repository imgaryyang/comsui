package com.suidifu.microservice.config;

import com.suidifu.microservice.ledger.template.CachecableLedgerTemplateParser;
import com.suidifu.watchman.message.amqp.config.RabbitMqConnectionConfig;
import com.suidifu.watchman.message.amqp.config.RabbitMqQueueConfig;
import com.suidifu.watchman.message.amqp.consumer.TopicConsumer;
import com.zufangbao.sun.ledgerbookv2.service.impl.CacheableInsideAccountServiceImpl;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;

/**
 * @author wukai mail: wukai@hzsuidifu.com time: 2018-01-23 10:41 description:
 */
@Configuration
public class BeanConfig {

  @Bean(name = "cfg_rabbit_mq_queue_config")
  @ConfigurationProperties(prefix = "message.rabbitmq.queue")
  public RabbitMqQueueConfig rabbitMqQueueConfig() {
    return new RabbitMqQueueConfig();
  }

  @Bean(name = "cfg_rabbit_mq_connection_config")
  @ConfigurationProperties(prefix = "message.rabbitmq.connection")
  public RabbitMqConnectionConfig rabbitMqConnectionConfig() {
    return new RabbitMqConnectionConfig();
  }

  @Bean
  public TopicConsumer topicConsumer() {
    TopicConsumer topicConsumer = new TopicConsumer();
    topicConsumer.setRabbitMqConnectionConfig(rabbitMqConnectionConfig());
    topicConsumer.setRabbitMqQueueList(new ArrayList<RabbitMqQueueConfig>() {{
      add(rabbitMqQueueConfig());
    }});
    return topicConsumer;
  }

  @Bean(name = "insideAccountService")
  public com.zufangbao.sun.ledgerbookv2.service.InsideAccountService insideAccountService() {
    return new CacheableInsideAccountServiceImpl();
  }

  @Bean(name = "ledgerTemplateParser")
  public com.zufangbao.sun.ledgerbookv2.handler.impl.LedgerTemplateParser ledgerTemplateParser() {
    return new CachecableLedgerTemplateParser();
  }
}
