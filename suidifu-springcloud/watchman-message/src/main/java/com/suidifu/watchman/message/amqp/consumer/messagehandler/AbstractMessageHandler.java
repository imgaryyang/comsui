package com.suidifu.watchman.message.amqp.consumer.messagehandler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.suidifu.watchman.message.amqp.codec.CodecType;
import com.suidifu.watchman.message.amqp.codec.RpcCodec;
import com.suidifu.watchman.message.amqp.request.AmqpRequest;
import com.suidifu.watchman.message.core.request.InvokeType;
import com.suidifu.watchman.message.core.request.Request;
import org.jetbrains.annotations.NotNull;

import java.util.Map;


/**
 * 处理消息抽象类
 *
 * @param <T>
 */
public abstract class AbstractMessageHandler<T> implements MessageHandler {

    @Override
    public Object handleMessage(String message, String queue, InvokeType sync, int codecOrdinal, Map<String, Object> additionalHeaders) throws Exception {
        RpcCodec codec = CodecType.getCodec(codecOrdinal);
        AmqpRequest request = extractAmqpRequest(message, codec);

        if (sync == InvokeType.Async) {
            this.handleAsyncMessage(request, queue);
            return null;
        }
        return codec.encodeObject(handleSyncMessage(request, queue));
    }

    @NotNull
    private AmqpRequest extractAmqpRequest(String message, RpcCodec codec) throws Exception {

        AmqpRequest request = codec.decodeObject(message, AmqpRequest.class);

        String[] paramTypes = request.getParamTypes();

        Object[] jsonParams = request.getParams();

        Object[] actualParams = new Object[jsonParams.length];

        for (int i = 0; i < jsonParams.length; i++) {

            Object obj = jsonParams[i];

            if (obj instanceof JSONObject) {
                actualParams[i] = codec.decodeObject(obj.toString(), Class.forName(paramTypes[i]));
            }else if(obj instanceof JSONArray){
                actualParams[i] = JSON.parseArray(obj.toString(), Class.forName(paramTypes[i]));
            }

            else {
                actualParams[i] = obj;
            }

        }

        request.setParams(actualParams);
        return request;
    }


    /**
     * 处理同步消息
     *
     * @param request
     * @return
     */
    protected abstract T handleSyncMessage(Request request, String queue);

    /**
     * 处理异步消息
     *
     * @param request
     */
    protected abstract void handleAsyncMessage(Request request, String queue);

}
