package top.ccheng.springbootlearn;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author ccheng
 * @date 2018/9/13
 */
@Configuration
public class RedisConfig {

    @Bean
    public JedisConnectionFactory jedisConnectionFactory(){
        return new JedisConnectionFactory();
    }

    @Bean
    public RedisTemplate<String, User> redisObjectTemplate(JedisConnectionFactory jedisConnectionFactory){
        RedisTemplate<String, User> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(jedisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new RedisObjectSerializer());//使用自定义的序列化convert
//        redisTemplate.setValueSerializer(new JdkSerializationRedisSerializer());//也可以直接使用SpringBoot提供的对象序列化convert
        return redisTemplate;
    }
}
