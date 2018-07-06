package com.suidifu.watchman.message.amqp.consumer.messagehandler;

import com.suidifu.watchman.message.core.request.InvokeType;

import java.util.Map;

public interface MessageHandler {

    public Object handleMessage(String message, String queue, InvokeType invokeType, int codecOrdinal, Map<String, Object> additionalHeaders) throws Exception;
}
