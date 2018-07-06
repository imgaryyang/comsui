package com.suidifu.watchman.message.amqp.consumer;

import com.suidifu.watchman.message.amqp.config.RabbitMqConnectionConfig;
import com.suidifu.watchman.message.amqp.config.RabbitMqQueueConfig;
import com.suidifu.watchman.message.amqp.consumer.messagehandler.MessageHandler;
import com.suidifu.watchman.message.amqp.manager.ConsumerMessageManager;
import com.suidifu.watchman.message.amqp.utils.AmqpFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.AbstractMessageListenerContainer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * author: wukai
 * mail: wukai@hzsuidifu.com
 * time: 2018-01-15 11:44
 * description:
 */
public abstract class Consumer implements ApplicationListener<ContextRefreshedEvent> {

    protected ConnectionFactory connectionFactory;

    protected RabbitMqConnectionConfig rabbitMqConnectionConfig;

    protected abstract void initializeConsumerContainer();

    protected abstract MessageHandler messageHandler();

    protected abstract RabbitMqConnectionConfig rabbitMqConnectionConfig();

    /**
     * 初始化 rabbit-mq
     *
     * @throws Exception
     */
    private void initializeRabbitMqConnection() {
        this.rabbitMqConnectionConfig = rabbitMqConnectionConfig();
        this.connectionFactory = AmqpFactory.buildDefaultCachingConnectionFactory(this.rabbitMqConnectionConfig, this.rabbitMqConnectionConfig.getVhost());
    }

    public ConsumerMessageManager getMessageManager(AbstractMessageListenerContainer container) {
        return new ConsumerMessageManager(container);
    }

    public AbstractMessageListenerContainer getMessageListenerContainer(RabbitMqQueueConfig queueConfig) {
        return AmqpFactory.getSimpleConsumerMessageListenerContainer(this.connectionFactory, messageHandler(), queueConfig);
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

        ApplicationContext applicationContext = event.getApplicationContext();

        if (applicationContext.getParent().getParent() != null) {

            initializeRabbitMqConnection();

            initializeConsumerContainer();
        }

    }
}
