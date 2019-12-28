package top.ccheng.springbootlearn;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Chapter13ApplicationTests {

    @Autowired
    private UserMapper userMapper;

    @Test
    @Rollback
    public void findByName() throws Exception {
        //清空数据
        userMapper.deleteAll();

        // 使用@Param进行插入
        userMapper.insert("AAA", 10);
        //使用map进行插入
        Map<String, Object> map = new HashMap<>();
        map.put("name", "BBB");
        map.put("age", 20);
        userMapper.insertByMap(map);
        //使用对象进行插入
        User userC = new User("CCC", 30);
        userMapper.insertByUser(userC);

        //根据姓名查询验证
        Assert.assertEquals(10, userMapper.findByName("AAA").getAge().intValue());
        Assert.assertEquals(20, userMapper.findByName("BBB").getAge().intValue());
        Assert.assertEquals(30, userMapper.findByName("CCC").getAge().intValue());

        // update一条数据，并select出来验证
        userMapper.update(new User("AAA", 40));
        User aaa = userMapper.findByName("AAA");
        Assert.assertEquals(40, aaa.getAge().intValue());

        // 删除这条数据，并select验证
        userMapper.delete(aaa.getId());
        aaa = userMapper.findByName("AAA");
        Assert.assertNull(aaa);

        //查询所有用户
        List<User> all = userMapper.findAll();
        Assert.assertEquals(2, all.size());
        for (User user : all) {
            Assert.assertNull(user.getId());
            Assert.assertNotNull(user.getName());
            Assert.assertNotNull(user.getAge());
        }
    }

}
