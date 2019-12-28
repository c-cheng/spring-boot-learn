package top.ccheng.springbootlearn;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import top.ccheng.springbootlearn.primary.repository.UserRepository;
import top.ccheng.springbootlearn.second.entity.Message;
import top.ccheng.springbootlearn.second.repository.MessageRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Chapter9ApplicationTests {

    @Autowired
    @Qualifier("primaryJdbcTemplate")
    protected JdbcTemplate jdbcTemplate1;

    @Autowired
    @Qualifier("secondaryJdbcTemplate")
    protected JdbcTemplate jdbcTemplate2;


    @Test
    public void testJdbcTemplate() throws Exception {
        //清空数据库
        jdbcTemplate1.update("DELETE  FROM  USER_3 ");
        jdbcTemplate2.update("DELETE  FROM  USER_3 ");

        // 往第一个数据源中插入两条数据
        jdbcTemplate1.update("insert into USER_3(id,name,age) values(?, ?, ?)", 1, "aaa", 20);
        jdbcTemplate1.update("insert into USER_3(id,name,age) values(?, ?, ?)", 2, "bbb", 30);

        // 往第二个数据源中插入一条数据，若插入的是第一个数据源，则会主键冲突报错
        jdbcTemplate2.update("insert into USER_3(id,name,age) values(?, ?, ?)", 1, "aaa", 20);

        // 查一下第一个数据源中是否有两条数据，验证插入是否成功
        Assert.assertEquals("2", jdbcTemplate1.queryForObject("select count(1) from USER_3", String.class));

        // 查一下第一个数据源中是否有两条数据，验证插入是否成功
        Assert.assertEquals("1", jdbcTemplate2.queryForObject("select count(1) from USER_3", String.class));
    }


    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MessageRepository messageRepository;

    @Test
    public void testRepository() throws Exception {

        userRepository.save(new top.ccheng.springbootlearn.primary.entity.User("aaa", 10));
        userRepository.save(new top.ccheng.springbootlearn.primary.entity.User("bbb", 20));
        userRepository.save(new top.ccheng.springbootlearn.primary.entity.User("ccc", 30));
        userRepository.save(new top.ccheng.springbootlearn.primary.entity.User("ddd", 40));
        userRepository.save(new top.ccheng.springbootlearn.primary.entity.User("eee", 50));
        Assert.assertEquals(5, userRepository.findAll().size());

        messageRepository.save(new Message("o1", "aaaaaaaaaa"));
        messageRepository.save(new Message("o2", "bbbbbbbbbb"));
        messageRepository.save(new Message("o3", "cccccccccc"));
        Assert.assertEquals(3, messageRepository.findAll().size());

    }
}
