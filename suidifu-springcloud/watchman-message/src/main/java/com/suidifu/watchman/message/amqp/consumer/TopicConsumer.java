package com.suidifu.watchman.message.amqp.consumer;

import com.suidifu.watchman.message.amqp.config.RabbitMqConnectionConfig;
import com.suidifu.watchman.message.amqp.config.RabbitMqQueueConfig;
import com.suidifu.watchman.message.amqp.consumer.messagehandler.MessageHandler;
import com.suidifu.watchman.message.amqp.consumer.messagehandler.SpringMessageHandler;
import com.suidifu.watchman.message.amqp.manager.ConsumerMessageManager;

import java.util.List;

/**
 * author: wukai
 * mail: wukai@hzsuidifu.com
 * time: 2018-01-11 11:50
 * description:
 */
public class TopicConsumer extends Consumer {

    private RabbitMqConnectionConfig rabbitMqConnectionConfig;

    private List<RabbitMqQueueConfig> rabbitMqQueueList;

    @Override
    protected MessageHandler messageHandler() {
        return new SpringMessageHandler();
    }

    @Override
    protected RabbitMqConnectionConfig rabbitMqConnectionConfig() {
        return rabbitMqConnectionConfig;
    }

    @Override
    protected void initializeConsumerContainer() {

        for (RabbitMqQueueConfig rabbitMqQueueConfig : rabbitMqQueueList) {

            ConsumerMessageManager consumerMessageManager = getMessageManager(getMessageListenerContainer(rabbitMqQueueConfig));

            consumerMessageManager.start();
        }
    }

    public void setRabbitMqConnectionConfig(RabbitMqConnectionConfig rabbitMqConnectionConfig) {
        this.rabbitMqConnectionConfig = rabbitMqConnectionConfig;
    }

    public void setRabbitMqQueueList(List<RabbitMqQueueConfig> rabbitMqQueueList) {
        this.rabbitMqQueueList = rabbitMqQueueList;
    }
}
