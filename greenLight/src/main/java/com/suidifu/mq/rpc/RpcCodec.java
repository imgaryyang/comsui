package com.suidifu.mq.rpc;

import com.suidifu.mq.rpc.request.Request;
import com.suidifu.mq.rpc.response.Response;

public interface RpcCodec {
	String encodeRequest(Request request);

	String encodeResponse(Response response);

	Request decodeRequest(String msg);

	Response decodeResponse(String msg);

	String encodeObject(Object object);

	<T> T decodeObject(String msg, Class<T> clazz);

	/**
	 * 强制转换类型，比如JsonCodec中将JSON格式的对象转换为强类型 这个过程在方法本地调用之前组装参数（强类型匹配）的时候使用
	 * 
	 * @param param 弱类型（JSON/XML化的内存对象），简单类型也支持
	 * @param targetType 目标类型
	 * @return
	 * @throws ClassNotFoundException
	 */
	Object convert(Object param, Class<?> targetType) throws ClassNotFoundException;
}