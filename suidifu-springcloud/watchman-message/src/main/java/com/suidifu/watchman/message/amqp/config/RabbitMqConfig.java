package com.suidifu.watchman.message.amqp.config;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
public class RabbitMqConfig {

    private String exchangeName;

    private Set<String> routingKeySet = new HashSet<>();

    private List<RabbitMqQueueConfig> rabbitMqQueueConfigList = new ArrayList<>();

    public void addRabbitMqQueueConfig(RabbitMqQueueConfig rabbitMqQueueConfig) {
        this.rabbitMqQueueConfigList.add(rabbitMqQueueConfig);
    }

    public void addRoutingKey(String routingKey) {
        this.routingKeySet.add(routingKey);
    }

}
