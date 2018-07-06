package com.suidifu.rabbitmq.manager.commands;

import com.suidifu.rabbitmq.manager.utils.MqUtil;
import com.suidifu.watchman.message.amqp.config.RabbitMqConfig;
import com.suidifu.watchman.message.amqp.config.RabbitMqQueueConfig;
import com.suidifu.watchman.message.amqp.utils.AmqpUtils;

import java.util.Map;

public class AddFinancialContractGroupCommand extends AbstarctCommand implements Command {

    public static final String Routing_Key = "routing_key";

    @Override
    public boolean execcommand(Map<String, String> args) {

        String serviceName = "loan-contract-" + args.get(Service_Name);

        String routingKey = String.format(".fin-%s.*", args.get(Service_Name));

        RabbitMqConfig contractRabbitMqconfig = new RabbitMqConfig();

        contractRabbitMqconfig.setExchangeName(AmqpUtils.buildExchangeName(serviceName));
        contractRabbitMqconfig.addRoutingKey(routingKey);

        int hashQueueStart = Integer.parseInt(args.get(Hash_Queue_Start_Index));
        int hashQueueEnd = Integer.parseInt(args.get(Hash_Queue_End_Index));

        for (int i = hashQueueStart; i < hashQueueEnd; i++
                ) {
            RabbitMqQueueConfig rabbitMqQueueConfig = new RabbitMqQueueConfig();

            rabbitMqQueueConfig.setQueueName(AmqpUtils.buildQueueName(serviceName, i));

            contractRabbitMqconfig.addRabbitMqQueueConfig(rabbitMqQueueConfig);
        }

        RabbitMqConfig financialContractRabbitConfig = new RabbitMqConfig();

        financialContractRabbitConfig.setExchangeName(args.get(Parent_Exchange_Name));

        MqUtil.consistentExchange(rabbitAdmin, contractRabbitMqconfig, financialContractRabbitConfig, hashQueueStart);

        return true;
    }
}
