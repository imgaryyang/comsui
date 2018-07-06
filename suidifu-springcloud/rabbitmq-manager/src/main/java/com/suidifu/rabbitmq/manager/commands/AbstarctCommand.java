package com.suidifu.rabbitmq.manager.commands;

import com.suidifu.rabbitmq.manager.configs.RabbitMqConnectionConfig;
import com.suidifu.rabbitmq.manager.utils.MqUtil;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitManagementTemplate;

import java.util.Map;

public abstract class AbstarctCommand implements Command {


    public static final String Rabbit_Mq_Connection_Config_Path = "rabbit_mq_connection_config";
    public static final String Vhost = "vhost";
    public static final String Service_Name = "service_name";
    public static final String Hash_Queue_Start_Index = "hash_queue_start_index";
    public static final String Hash_Queue_End_Index = "hash_queue_end_index";
    public static final String Concurrent_Consumer_Size = "concurrent_consumer_size";
    public static final String Grant_Privileges = "grant_privileges";
    public static final String Parent_Exchange_Name = "parent_exchange_name";

    protected RabbitMqConnectionConfig rabbitMqConnectionConfig;
    protected String vhost;
    protected RabbitAdmin rabbitAdmin;
    protected RabbitManagementTemplate template;
    protected ConnectionFactory connectionFactory;


    public boolean exec(Map<String, String> args) {

        try {
            initialize(args);
            return execcommand(args);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    protected abstract boolean execcommand(Map<String, String> args);

    protected void initialize(Map<String, String> args) throws Exception {

        this.rabbitMqConnectionConfig = MqUtil.loadRabbitMqConnectionConfig(args.get(Rabbit_Mq_Connection_Config_Path));

        this.vhost = args.get(Vhost);

        this.connectionFactory = MqUtil.buildDefaultCachingConnectionFactory(rabbitMqConnectionConfig, vhost);

        this.rabbitAdmin = new RabbitAdmin(this.connectionFactory);

        template = new RabbitManagementTemplate("http://" + rabbitMqConnectionConfig.getHost() + ":" +
                rabbitMqConnectionConfig.getApiPort() + "/api/",
                rabbitMqConnectionConfig.getUsername(),
                rabbitMqConnectionConfig.getPassword());

    }

}
