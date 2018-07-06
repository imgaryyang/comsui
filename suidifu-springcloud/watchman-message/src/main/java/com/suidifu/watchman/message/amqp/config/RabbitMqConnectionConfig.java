package com.suidifu.watchman.message.amqp.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by wukai on 2017/11/6.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RabbitMqConnectionConfig {

    private String host;
    private int port;
    private String username;
    private String password;
    private String vhost;

}
