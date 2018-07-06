package com.suidifu.rabbitmq.manager.exchange;

import org.springframework.amqp.core.CustomExchange;

import java.util.Map;

/**
 * Created by wukai on 2017/11/3.
 */
public class ConsistentHashExchange extends CustomExchange {

    public ConsistentHashExchange(String name) {
        super(name, "x-consistent-hash");
    }

    public ConsistentHashExchange(String name, Map<String, Object> arguments) {
        super(name, "x-consistent-hash", true, false, arguments);
    }

}

