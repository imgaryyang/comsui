package com.suidifu.watchman.message.amqp.consumer.messagehandler;

import com.rabbitmq.client.Channel;
import com.suidifu.watchman.message.core.request.InvokeType;
import com.suidifu.watchman.util.JacksonUtils;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;

import java.util.Map;

/**
 * 增加MessageProperties处理参数
 */
public class MqMessageListenerAdapter extends MessageListenerAdapter {

    // 区分timeout里的null
    private static final byte[] NULLOBJ = new byte[0];
    private MessageHandler messageHandler;

    public MqMessageListenerAdapter(MessageHandler messageHandler) {
        super(NULLOBJ);
        this.messageHandler = messageHandler;
    }

    @Override
    public void onMessage(Message originalMessage, Channel channel) throws Exception {

        MessageProperties msgprops = originalMessage.getMessageProperties();

        Map<String, Object> msgpropheaders = msgprops.getHeaders();

        Boolean boolSync = Boolean.valueOf(msgpropheaders.get("sync").toString());

        InvokeType sync = InvokeType.getSync(boolSync);

        Integer codecOridinal = Integer.valueOf(msgpropheaders.get("encoding").toString());

        logger.info("MqMessageListenerAdapter#onMessage with msgpropheaders[" + JacksonUtils.toJSONString(msgpropheaders) + ",boolSync[" + boolSync + "],codecOridinal[" + codecOridinal + "]");

        Object result = this.messageHandler.handleMessage(extractMessage(originalMessage).toString(), msgprops.getConsumerQueue(), sync, codecOridinal, msgpropheaders);

        if (sync == InvokeType.Async) {


            return;
        } else if (sync == InvokeType.Sync) {
            handleResult(result, originalMessage, channel);
        }
    }


}
