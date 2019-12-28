## Spring Boot中使用Redis数据库

### 引入依赖
Spring Boot提供的数据访问框架Spring Data Redis基于Jedis。可以通过引入spring-boot-starter-redis来配置依赖关系。
```
<dependency>
    <groupId>redis.clients</groupId>
    <artifactId>jedis</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
```

### 参数配置
在application.properties中加入Redis服务端的相关配置，具体说明如下：
```
# REDIS (RedisProperties)
# Redis数据库索引（默认为0）
spring.redis.database=0
# Redis服务器地址
spring.redis.host=localhost
# Redis服务器连接端口
spring.redis.port=6379
# Redis服务器连接密码（默认为空）
spring.redis.password=
# 连接超时时间（毫秒）
spring.redis.timeout=0ms

# 连接池最大连接数（使用负值表示没有限制）
spring.redis.jedis.pool.max-active=8
# 连接池最大阻塞等待时间（使用负值表示没有限制）
spring.redis.jedis.pool.max-wait=-1ms
# 连接池中的最大空闲连接
spring.redis.jedis.pool.max-idle=8
# 连接池中的最小空闲连接
spring.redis.jedis.pool.min-idle=0
```
其中spring.redis.database的配置通常使用0即可，Redis在配置的时候可以设置数据库数量，默认为16，可以理解为数据库的schema


### 测试访问
```
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(Application.class)
public class ApplicationTests {

	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	@Test
	public void test() throws Exception {

		// 保存字符串
		stringRedisTemplate.opsForValue().set("aaa", "111");
		Assert.assertEquals("111", stringRedisTemplate.opsForValue().get("aaa"));

    }

}
```
通过上面这段极为简单的测试案例演示了如何通过自动配置的StringRedisTemplate对象进行Redis的读写操作，该对象从命名中就可注意到支持的是String类型。如果有使用过spring-data-redis的开发者一定熟悉RedisTemplate<K, V>接口，StringRedisTemplate就相当于RedisTemplate<String, String>的实现。

### redis中存储对象（自定义序列化）
除了String类型，实战中我们还经常会在Redis中存储对象，这时候我们就会想是否可以使用类似RedisTemplate<String, User>来初始化并进行操作。


Spring Boot提供了JdkSerializationRedisSerializer对对象进行序列化，我们也可以自己实现RedisSerializer<T>接口来对传入对象进行序列化和反序列化，下面我们通过一个实例来完成对象的读写操作。

创建要存储的对象：User
```
public class User implements Serializable {

    private static final long serialVersionUID = -1L;

    private String username;
    private Integer age;

    public User(String username, Integer age) {
        this.username = username;
        this.age = age;
    }

    // 省略getter和setter

}
```

实现对象的序列化接口
```
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
```

配置针对User的RedisTemplate实例
```
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
```

编写测试用例实验效果
```
    @Autowired
    private RedisTemplate<String, User> redisObjectTemplate;

    @Test
    public void testRedisObjectTemplate(){
        User user = new User("超人", 20);
        redisObjectTemplate.opsForValue().set(user.getUsername(), user);

        user = new User("蝙蝠侠", 30);
        redisObjectTemplate.opsForValue().set(user.getUsername(), user);

        user = new User("蜘蛛侠", 40);
        redisObjectTemplate.opsForValue().set(user.getUsername(), user);

        Assert.assertEquals(20, redisObjectTemplate.opsForValue().get("超人").getAge().longValue());
        Assert.assertEquals(30, redisObjectTemplate.opsForValue().get("蝙蝠侠").getAge().longValue());
        Assert.assertEquals(40, redisObjectTemplate.opsForValue().get("蜘蛛侠").getAge().longValue());
    }
```

当然spring-data-redis中提供的数据操作远不止这些