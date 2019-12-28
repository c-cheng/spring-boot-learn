package top.ccheng.springbootlearn;

import org.springframework.core.convert.converter.Converter;
import org.springframework.core.serializer.support.DeserializingConverter;
import org.springframework.core.serializer.support.SerializingConverter;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

/**
 * @author ccheng
 * @date 2018/9/13
 */
public class RedisObjectSerializer implements RedisSerializer<Object> {
    private Converter<Object, byte[]> serializeConverter = new SerializingConverter();
    private Converter<byte[], Object> deserializeConverter = new DeserializingConverter();
    private static final byte[] EMPTY_BYTE_ARRAY = new byte[0];

    private boolean isEmpty(byte[] bytes){
        return null == bytes || bytes.length < 1;
    }

    @Override
    public byte[] serialize(Object o) throws SerializationException {
        if (null == o){
            return EMPTY_BYTE_ARRAY;
        }

        return serializeConverter.convert(o);
    }

    @Override
    public Object deserialize(byte[] bytes) throws SerializationException {
        if (isEmpty(bytes)){
            return null;
        }

        return deserializeConverter.convert(bytes);
    }
}
