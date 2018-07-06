package com.suidifu.watchman.message.amqp.codec;


public interface RpcCodec {

    String encodeObject(Object object) throws Exception;

    <T> T decodeObject(String msg, Class<T> clazz) throws Exception;

}