package top.ccheng.springbootlearn;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import top.ccheng.springbootlearn.vo.RandomValue;
import top.ccheng.springbootlearn.vo.User;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Chapter2ApplicationTests {

    @Autowired
    private User user;
    @Autowired
    private RandomValue randomValue;

    @Test
    public void testUser() {
        System.out.println(user);
        Assert.assertEquals(user.getFirstName(), "cheng");
        Assert.assertEquals(user.getSecondName(), "chen");
        Assert.assertEquals(user.getName(), "chengchen");
        Assert.assertEquals(user.getIncludeValue(), "dev include");
        Assert.assertEquals(user.getAge(), Integer.valueOf(18));

        System.out.println(randomValue);
    }

}
