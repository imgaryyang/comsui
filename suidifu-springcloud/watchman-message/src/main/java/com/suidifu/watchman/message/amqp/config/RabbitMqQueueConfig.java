package com.suidifu.watchman.message.amqp.config;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * author: wukai
 * mail: wukai@hzsuidifu.com
 * time: 2018-01-16 12:53
 * description:
 */
@Data
@NoArgsConstructor
public class RabbitMqQueueConfig {

    public static final long DEFAULT_RECEIVE_TIMEOUT = 1_000;
    private String queueName;
    private int concurrentConsumers;
    private Long receiveTimeout;

    public Long getReceiveTimeout() {
        return (null == receiveTimeout || 0L == receiveTimeout) ? DEFAULT_RECEIVE_TIMEOUT : receiveTimeout;
    }

}
