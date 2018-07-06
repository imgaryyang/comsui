package com.suidifu.rabbitmq.manager.commands;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.rabbitmq.http.client.domain.UserPermissions;
import com.suidifu.rabbitmq.manager.utils.MqUtil;
import com.suidifu.watchman.message.amqp.config.RabbitMqConfig;
import com.suidifu.watchman.message.amqp.config.RabbitMqQueueConfig;
import com.suidifu.watchman.message.amqp.utils.AmqpUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by wukai on 2017/12/12.
 */
public class InitNewTopologyCommand extends AbstarctCommand {

    public static final String Financial_Contract_Service_Name = "financial_contract_business_service_name";

    public static final String Loan_Contract_Service_Name = "loan_contract_service_name";

    public static final String Loan_Contract_Routing_Key = ".fin_con.*";

    public static final String Business_Service_Name = "business_service_name";

    public void createVhostAndGrantPrivileges(Map<String, String> args) {
        try {
            if ("Y".equalsIgnoreCase(args.get(Grant_Privileges))) {
                template.getClient().createVhost(vhost);
                template.getClient().updatePermissions(vhost, rabbitMqConnectionConfig.getUsername(),
                        new UserPermissions(".*", ".*", ".*"));
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean execcommand(Map<String, String> args) {

        try {

            createVhostAndGrantPrivileges(args);

            List<RabbitMqConfig> rabbitMqQueueConfigs = new ArrayList<>();

            RabbitMqConfig businessRabbitConfig = new RabbitMqConfig();

            businessRabbitConfig.setExchangeName(AmqpUtils.buildExchangeName(args.get(Business_Service_Name)));

            RabbitMqConfig financialContractRabbitConfig = new RabbitMqConfig();

            financialContractRabbitConfig.setExchangeName(AmqpUtils.buildExchangeName(args.get(Financial_Contract_Service_Name)));

            rabbitMqQueueConfigs.add(businessRabbitConfig);
            rabbitMqQueueConfigs.add(financialContractRabbitConfig);

            for (RabbitMqConfig topicConfig : rabbitMqQueueConfigs) {
                MqUtil.topicExchange(rabbitAdmin, topicConfig, null);
            }

            RabbitMqConfig contractRabbitMqconfig = new RabbitMqConfig();

            contractRabbitMqconfig.setExchangeName(AmqpUtils.buildExchangeName(args.get(Loan_Contract_Service_Name)));
            contractRabbitMqconfig.addRoutingKey(args.get(Loan_Contract_Routing_Key));

            int hashQueueStart = Integer.parseInt(args.get(Hash_Queue_Start_Index));
            int hashQueueEnd = Integer.parseInt(args.get(Hash_Queue_End_Index));

            for (int i = hashQueueStart; i < hashQueueEnd; i++
                    ) {
                RabbitMqQueueConfig rabbitMqQueueConfig = new RabbitMqQueueConfig();

                rabbitMqQueueConfig.setQueueName(AmqpUtils.buildQueueName(args.get(Loan_Contract_Service_Name), i));

                contractRabbitMqconfig.addRabbitMqQueueConfig(rabbitMqQueueConfig);
            }
            MqUtil.consistentExchange(rabbitAdmin, contractRabbitMqconfig, financialContractRabbitConfig, hashQueueStart);

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
