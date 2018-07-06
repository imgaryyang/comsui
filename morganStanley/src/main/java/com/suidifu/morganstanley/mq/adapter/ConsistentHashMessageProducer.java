package com.suidifu.morganstanley.mq.adapter;

import com.suidifu.mq.config.RabbitMqConfig;
import com.suidifu.mq.producer.simple.SimpleConsistentHashProducer;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component("messageProducer")
public class ConsistentHashMessageProducer extends SimpleConsistentHashProducer {
    @Resource(name = "cfg_rabbitmq_producer")
    private RabbitMqConfig rabbitMqConfig;

    @Override
    protected RabbitMqConfig getRabbitMqConfig() {
        return rabbitMqConfig;
    }
}
