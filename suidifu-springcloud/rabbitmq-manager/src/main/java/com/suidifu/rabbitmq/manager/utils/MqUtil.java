package com.suidifu.rabbitmq.manager.utils;


import com.suidifu.rabbitmq.manager.RabbitMqManagerEntry;
import com.suidifu.rabbitmq.manager.configs.RabbitMqConnectionConfig;
import com.suidifu.rabbitmq.manager.exchange.ConsistentHashExchange;
import com.suidifu.watchman.message.amqp.config.RabbitMqConfig;
import com.suidifu.watchman.message.amqp.config.RabbitMqQueueConfig;
import com.suidifu.watchman.message.amqp.utils.AmqpFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.util.StringUtils;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;

public class MqUtil extends AmqpFactory {

    public static RabbitMqConnectionConfig loadRabbitMqConnectionConfig(String configLocation) {

        Properties properties = new Properties();

        InputStream fileInputStream = null;

        try {

            if (StringUtils.isEmpty(configLocation)) {

                fileInputStream = new FileInputStream(RabbitMqManagerEntry.RABBIT_CONFIG_PATH);

            } else {

                fileInputStream = new FileInputStream(configLocation);
            }
            properties.load(fileInputStream);

        } catch (Exception e) {
            throw new IllegalArgumentException("Illegal Config location");
        }

        RabbitMqConnectionConfig rabbitMqConnectionConfig = new RabbitMqConnectionConfig(properties);

        return rabbitMqConnectionConfig;
    }

    public static void topicExchange(RabbitAdmin rabbitAdmin, RabbitMqConfig rabbitMqConfig, RabbitMqConfig parentRabbitConfig) {

        TopicExchange topicExchange = new TopicExchange(rabbitMqConfig.getExchangeName());

        rabbitAdmin.declareExchange(topicExchange);

        for (RabbitMqQueueConfig rabbitMqQueueConfig : rabbitMqConfig.getRabbitMqQueueConfigList()
                ) {

            Queue requestQueue = priorityQueue(rabbitMqQueueConfig.getQueueName());
            rabbitAdmin.declareQueue(requestQueue);

            for (String routingKey : rabbitMqConfig.getRoutingKeySet()) {
                rabbitAdmin.declareBinding(bindTopic(requestQueue, topicExchange, routingKey));
            }

        }

        if (null != parentRabbitConfig) {

            TopicExchange parentExchange = new TopicExchange(parentRabbitConfig.getExchangeName());

            for (String routingKey : rabbitMqConfig.getRoutingKeySet()) {

                rabbitAdmin.declareBinding(BindingBuilder.bind(topicExchange).to(parentExchange).with(routingKey));
            }

        }
    }

    public static void consistentExchange(RabbitAdmin rabbitAdmin, RabbitMqConfig rabbitMqConfig, RabbitMqConfig parentRabbitMqConfig, int start) {

        Map<String, Object> args = new HashMap<>(2);

        args.put("hash-header", "consistence-hash-policy");

        ConsistentHashExchange consistentHashExchange = new ConsistentHashExchange(rabbitMqConfig.getExchangeName(), args);

        rabbitAdmin.declareExchange(consistentHashExchange);

        if (null != parentRabbitMqConfig) {

            TopicExchange parentExchange = new TopicExchange(parentRabbitMqConfig.getExchangeName());

            for (String routingKey : rabbitMqConfig.getRoutingKeySet()
                    ) {
                rabbitAdmin.declareBinding((BindingBuilder.bind(consistentHashExchange).to(parentExchange)).with(routingKey));
            }
        }

        for (RabbitMqQueueConfig queueConfig : rabbitMqConfig.getRabbitMqQueueConfigList()) {
            Queue requestQueue = priorityQueue(queueConfig.getQueueName());
            rabbitAdmin.declareQueue(requestQueue);
            rabbitAdmin.declareBinding(bindConsistentHash(requestQueue, consistentHashExchange, "" + start++));
        }

    }

    private static Binding bindConsistentHash(Queue requestQueue, ConsistentHashExchange consistentHashExchange, String routingKey) {
        return BindingBuilder.bind(requestQueue).to(consistentHashExchange).with(routingKey).and(consistentHashExchange.getArguments());
    }

    public static Binding bindTopic(Queue requestQueue, TopicExchange topicExchange, String routingKey) {
        return BindingBuilder.bind(requestQueue).to(topicExchange).with(routingKey);
    }

    private static Queue priorityQueue(String queueName) {
        Map<String, Object> arguments = new Hashtable<String, Object>();
        arguments.put("x-max-priority", 10);
        return new Queue(queueName, true, false, false, arguments);
    }

}