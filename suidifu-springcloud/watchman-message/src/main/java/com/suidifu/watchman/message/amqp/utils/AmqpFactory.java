package com.suidifu.watchman.message.amqp.utils;

import com.suidifu.watchman.message.amqp.config.RabbitMqConnectionConfig;
import com.suidifu.watchman.message.amqp.config.RabbitMqQueueConfig;
import com.suidifu.watchman.message.amqp.consumer.messagehandler.ConsumerErrorHandler;
import com.suidifu.watchman.message.amqp.consumer.messagehandler.MessageHandler;
import com.suidifu.watchman.message.amqp.consumer.messagehandler.MqMessageListenerAdapter;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.AbstractMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;

import java.util.concurrent.ConcurrentHashMap;

/**
 * author: wukai
 * mail: wukai@hzsuidifu.com
 * time: 2018-01-11 12:26
 * description:
 */
public class AmqpFactory {

    private static ConcurrentHashMap<String, CachingConnectionFactory> cachingConnnectFactoryMap = new ConcurrentHashMap<>();

    private static ConcurrentHashMap<String, RabbitTemplate> rabbitTemplateMap = new ConcurrentHashMap<>();

    /**
     * 获取CachingConnectionFactory
     *
     * @return
     */
    public static final CachingConnectionFactory buildDefaultCachingConnectionFactory(RabbitMqConnectionConfig rabbitMqConfig, String vhost) {

        if (cachingConnnectFactoryMap.containsKey(vhost)) {
            return cachingConnnectFactoryMap.get(vhost);
        }
        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory(rabbitMqConfig.getHost(), rabbitMqConfig.getPort());
        cachingConnectionFactory.setUsername(rabbitMqConfig.getUsername());
        cachingConnectionFactory.setPassword(rabbitMqConfig.getPassword());
        cachingConnectionFactory.setRequestedHeartBeat(15);
        cachingConnectionFactory.setVirtualHost(vhost);

        cachingConnnectFactoryMap.put(vhost, cachingConnectionFactory);

        return cachingConnectionFactory;
    }

    /**
     * @param cachingConnectionFactory
     * @return
     */
    public static final RabbitTemplate buildRabbitTemplate(CachingConnectionFactory cachingConnectionFactory, String vhost) {

        if (rabbitTemplateMap.contains(vhost)) {

            return rabbitTemplateMap.get(vhost);
        }

        RabbitTemplate rabbitTemplate = new RabbitTemplate(cachingConnectionFactory);
        ;

        rabbitTemplateMap.put(vhost, rabbitTemplate);

        return rabbitTemplate;
    }

    /**
     * SimpleConsumerMessageListenerContainer
     *
     * @param connectionFactory
     * @param messageHandler
     * @param queueConfig
     * @return
     */
    public static AbstractMessageListenerContainer getSimpleConsumerMessageListenerContainer(ConnectionFactory connectionFactory, MessageHandler messageHandler, RabbitMqQueueConfig queueConfig) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(queueConfig.getQueueName());
        container.setConcurrentConsumers(queueConfig.getConcurrentConsumers());
        container.setMessageListener(new MqMessageListenerAdapter(messageHandler));
        container.setAcknowledgeMode(AcknowledgeMode.AUTO);
        container.setChannelTransacted(true);
        container.setReceiveTimeout(queueConfig.getReceiveTimeout());
        container.setErrorHandler(new ConsumerErrorHandler());
        container.setPrefetchCount(1);
        return container;
    }
}
