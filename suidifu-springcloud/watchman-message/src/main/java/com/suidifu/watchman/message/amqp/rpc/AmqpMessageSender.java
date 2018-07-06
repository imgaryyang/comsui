package com.suidifu.watchman.message.amqp.rpc;

import com.suidifu.watchman.message.amqp.codec.CodecType;
import com.suidifu.watchman.message.amqp.config.RabbitMqConnectionConfig;
import com.suidifu.watchman.message.amqp.request.AmqpRequest;
import com.suidifu.watchman.message.amqp.response.AmqpResponse;
import com.suidifu.watchman.message.amqp.utils.AmqpFactory;
import com.suidifu.watchman.message.core.request.Request;
import com.suidifu.watchman.message.core.response.Response;
import com.suidifu.watchman.message.core.rpc.MessageSender;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.TimeoutException;

/**
 * author: wukai
 * mail: wukai@hzsuidifu.com
 * time: 2018-01-11 12:29
 * description:
 */
public class AmqpMessageSender implements MessageSender {

    public static final String SYNC = "sync";

    public static final String ENCODING = "encoding";

    @Autowired
    private RabbitMqConnectionConfig rabbitMqConnectionConfig;

    @Override
    public void sendAsyncMessage(Request request) throws Exception {

        AmqpRequest amqpRequest = (AmqpRequest) request;

        RabbitTemplate rabbitTemplate = buildRabbitTemplate(amqpRequest);

        rabbitTemplate.convertAndSend(amqpRequest.getExchange(), amqpRequest.getRoutingKey(), buildMessage(rabbitTemplate, amqpRequest));
    }

    private RabbitTemplate buildRabbitTemplate(AmqpRequest amqpRequest) {

        String vhost = amqpRequest.getVhost();

        CachingConnectionFactory cachingConnectionFactory = AmqpFactory.buildDefaultCachingConnectionFactory(rabbitMqConnectionConfig, vhost);

        return AmqpFactory.buildRabbitTemplate(cachingConnectionFactory, vhost);
    }

    private Message buildMessage(RabbitTemplate rabbitTemplate, AmqpRequest amqpRequest) throws Exception {

        CodecType codecType = amqpRequest.getEncodingType();

        String message = codecType.getCodec().encodeObject(amqpRequest);

        Message msg = rabbitTemplate.getMessageConverter().toMessage(message, new MessageProperties());
        msg.getMessageProperties().setHeader(SYNC, amqpRequest.getInvokeType().name());
        msg.getMessageProperties().setHeader(ENCODING, codecType.getOrdinal());
        msg.getMessageProperties().setPriority(amqpRequest.getPriority());

        return msg;
    }

    @Override
    public Response sendSyncMessage(Request request) throws Exception {

        AmqpRequest amqpRequest = (AmqpRequest) request;

        RabbitTemplate rabbitTemplate = buildRabbitTemplate(amqpRequest);

        Object respSerializeObj = rabbitTemplate.convertSendAndReceive(amqpRequest.getExchange(), amqpRequest.getRoutingKey(), buildMessage(rabbitTemplate, amqpRequest));

        //TODO 异常的捕获

        if (respSerializeObj == null)

            throw new TimeoutException("reply timeout：");

        if (respSerializeObj instanceof byte[])

            return null;

        return amqpRequest.getEncodingType().getCodec().decodeObject(respSerializeObj.toString(), AmqpResponse.class);

    }
}
