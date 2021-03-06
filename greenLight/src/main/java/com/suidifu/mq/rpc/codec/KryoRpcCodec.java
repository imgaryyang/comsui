package com.suidifu.mq.rpc.codec;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;

import org.apache.commons.codec.binary.Base64;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.JavaSerializer;
import com.suidifu.mq.rpc.RpcCodec;
import com.suidifu.mq.rpc.request.Request;
import com.suidifu.mq.rpc.response.Response;

/**
 * Kryo反/序列化
 * 
 * @author lisf
 *
 */
public class KryoRpcCodec implements RpcCodec {

	@Override
	public String encodeObject(Object object) {
		return __serialize(object);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T decodeObject(String msg, Class<T> clazz) {
		return (T) __deserialize(msg, new Type[] { clazz });
	}

	@Override
	public String encodeRequest(Request request) {
		return __serialize(request);
	}

	@Override
	public String encodeResponse(Response response) {
		return __serialize(response);
	}

	@Override
	public Request decodeRequest(String msg) {
		return __deserializeRequest(msg);
	}

	@Override
	public Response decodeResponse(String msg) {
		return __deserializeResponse(msg);
	}

	@Override
	public Object convert(Object param, Class<?> targetType) throws ClassNotFoundException {
		return null;
	}

	/**
	 * 序列化
	 * 
	 * @param object
	 * @return
	 */
	public static String __serialize(Object object) {
		if (null == object)
			return null;
		Kryo kryo = __kryo();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Output output = new Output(baos);
		kryo.writeClassAndObject(output, object);
		output.flush();
		output.close();
		byte[] buffer = baos.toByteArray();
		try {
			baos.flush();
			baos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new String(new Base64().encode(buffer));
	}

	/**
	 * 反序列化
	 * 
	 * @param serializeStr
	 * @param types
	 * @return
	 */
	public static Object __deserialize(String serializeStr, Type[] types) {
		Kryo kryo = __kryo();
		for (Type type : types)
			kryo.register(type.getClass(), new JavaSerializer());
		ByteArrayInputStream bais = new ByteArrayInputStream(new Base64().decode(serializeStr));
		Input input = new Input(bais);
		return kryo.readClassAndObject(input);
	}

	public static Request __deserializeRequest(String serializeStr) {
		return (Request) __deserialize(serializeStr, new Type[] { Request.class });
	}

	public static Response __deserializeResponse(String serializeStr) {
		return (Response) __deserialize(serializeStr, new Type[] { Response.class });
	}

	private static Kryo __kryo() {
		Kryo kryo = new Kryo();
		kryo.setReferences(false);
		return kryo;
	}
}
