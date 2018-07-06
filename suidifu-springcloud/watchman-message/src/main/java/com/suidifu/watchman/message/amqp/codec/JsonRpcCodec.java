package com.suidifu.watchman.message.amqp.codec;

import com.alibaba.fastjson.JSON;

/**
 * Json反/序列化
 *
 * @author lisf
 */
public class JsonRpcCodec implements RpcCodec {

    @Override
    public String encodeObject(Object object) throws Exception {
        return JSON.toJSONString(object);
    }

    @Override
    public <T> T decodeObject(String msg, Class<T> clazz) throws Exception {
        return JSON.parseObject(msg, clazz);
    }
}
