package com.suidifu.rabbitmq.manager.configs;

import lombok.Data;

import java.util.Properties;

@Data
public class RabbitMqConnectionConfig extends com.suidifu.watchman.message.amqp.config.RabbitMqConnectionConfig {

    private int apiPort;

    public RabbitMqConnectionConfig(Properties properties) {

        super(
                properties.getProperty("host", "127.0.0.1"),
                Integer.parseInt(properties.getProperty("port", "5672")),
                properties.getProperty("username", "guest"),
                properties.getProperty("password", "guest"),
                ""
        );
        this.apiPort = Integer.parseInt(properties.getProperty("apiPort", "null"));
    }

}
