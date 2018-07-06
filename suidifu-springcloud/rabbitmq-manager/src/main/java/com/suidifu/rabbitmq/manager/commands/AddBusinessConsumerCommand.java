package com.suidifu.rabbitmq.manager.commands;

import com.suidifu.rabbitmq.manager.utils.MqUtil;
import com.suidifu.watchman.message.amqp.config.RabbitMqConfig;
import com.suidifu.watchman.message.amqp.config.RabbitMqQueueConfig;
import com.suidifu.watchman.message.amqp.utils.AmqpUtils;

import java.util.Map;

public class AddBusinessConsumerCommand extends AbstarctCommand implements Command {

    public static final String Routing_Key = "routing_key";

    public static final String Exchange_Name = "exchange_name";

    @Override
    public boolean execcommand(Map<String, String> args) {

        String routingKey = args.get(Routing_Key);

        String serviceName = args.get(Service_Name);

        RabbitMqConfig rabbitMqConfig = new RabbitMqConfig();

        rabbitMqConfig.setExchangeName(args.get(Exchange_Name));

        RabbitMqQueueConfig rabbitMqQueueConfig = new RabbitMqQueueConfig();

        rabbitMqQueueConfig.setQueueName(AmqpUtils.buildQueueName(serviceName, -1));

        rabbitMqConfig.addRabbitMqQueueConfig(rabbitMqQueueConfig);
        rabbitMqConfig.addRoutingKey(routingKey);

        MqUtil.topicExchange(rabbitAdmin, rabbitMqConfig, null);

        return true;
    }
}
