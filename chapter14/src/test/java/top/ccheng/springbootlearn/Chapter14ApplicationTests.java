package top.ccheng.springbootlearn;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Chapter14ApplicationTests {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void test() {
        jdbcTemplate.update("insert user(name, age) values(?, ?)", "aaa", 10);
    }

}
