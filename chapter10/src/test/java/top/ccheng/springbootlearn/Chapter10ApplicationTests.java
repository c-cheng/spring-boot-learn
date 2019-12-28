package top.ccheng.springbootlearn;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Chapter10ApplicationTests {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Test
    public void testStringRedisTemplate() {
        stringRedisTemplate.opsForValue().set("k1", "v1");
        Assert.assertEquals("v1", stringRedisTemplate.opsForValue().get("k1"));
    }


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
}
