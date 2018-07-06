package com.zufangbao.earth.config.redis.serializer.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.util.Objects;

/**
 * @author dafuchen
 *         2017/12/29
 */
@Component
public class KryoRedisSerializer<T> implements RedisSerializer<T> {
    @Autowired
    private ThreadLocal<Kryo> kryoThreadLocal;

    @Override
    public byte[] serialize(T t) throws SerializationException {
        Kryo kryo = kryoThreadLocal.get();
        if (Objects.isNull(kryo)) {
            kryo = new Kryo();
            kryoThreadLocal.set(kryo);
        }
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        Output output = new Output(bytes);
        kryo.writeClassAndObject(output, t);
        output.close();
        return output.getBuffer();
    }

    @Override
    @SuppressWarnings("unchecked")
    public T deserialize(byte[] bytes) throws SerializationException {
        Kryo kryo = kryoThreadLocal.get();
        if (Objects.isNull(kryo)) {
            kryo = new Kryo();
            kryoThreadLocal.set(kryo);
        }
        Input input = new Input(bytes);
        T deserializer = (T) kryo.readClassAndObject(input);
        input.close();
        return deserializer;
    }
}
