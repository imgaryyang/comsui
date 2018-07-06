package com.suidifu.watchman.message.amqp.utils;

/**
 * author: wukai
 * mail: wukai@hzsuidifu.com
 * time: 2018-01-11 12:33
 * description:
 */
public class AmqpUtils {

    public static String buildQueueName(String queueNameSuffix, int index) {
        return index < 0 ? String.format("queue-%s", queueNameSuffix) : String.format("queue-%s-%s", queueNameSuffix, index);
    }

    public static String buildExchangeName(String exchangeNameSuffix) {
        return String.format("exchange-%s", exchangeNameSuffix);
    }

}
