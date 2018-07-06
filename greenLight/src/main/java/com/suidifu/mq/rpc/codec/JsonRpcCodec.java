package com.suidifu.mq.rpc.codec;

import com.alibaba.fastjson.JSON;
import com.suidifu.mq.rpc.RpcCodec;
import com.suidifu.mq.rpc.request.Request;
import com.suidifu.mq.rpc.response.Response;

/**
 * Json反/序列化
 *
 * @author lisf
 *
 */
public class JsonRpcCodec implements RpcCodec {

	@Override
    public String encodeObject(Object object) {
		return JSON.toJSONString(object);
	}

	@Override
    public <T> T decodeObject(String msg, Class<T> clazz) {
		return JSON.parseObject(msg, clazz);
	}

	@Override
	public String encodeRequest(Request request) {
		return JSON.toJSONString(request);
	}

	@Override
	public String encodeResponse(Response response) {
		return JSON.toJSONString(response);
	}

	@Override
	public Request decodeRequest(String msg) {
		return JSON.parseObject(msg, Request.class);
	}

	@Override
	public Response decodeResponse(String msg) {
		return JSON.parseObject(msg, Response.class);
	}

	@Override
	public Object convert(Object param, Class<?> targetType) throws ClassNotFoundException {
		return null;
	}
}
