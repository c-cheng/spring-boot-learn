## Spring Boot中使用MyBatis部分注解配置详解

### 传参方式

* ##### 使用@Param
在之前的整合示例中我们已经使用了这种最简单的传参方式，如下：
```
@Insert("INSERT INTO USER(NAME, AGE) VALUES(#{name}, #{age})")
int insert(@Param("name") String name, @Param("age") Integer age);
```
这种方式很好理解，@Param中定义的name对应了SQL中的#{name}，age对应了SQL中的#{age}。

* ##### 使用Map
如下代码，通过Map对象来作为传递参数的容器：
```
@Insert("INSERT INTO USER(NAME, AGE) VALUES(#{name,jdbcType=VARCHAR}, #{age,jdbcType=INTEGER})")
int insertByMap(Map<String, Object> map);
```
对于Insert语句中需要的参数，我们只需要在map中填入同名的内容即可，具体如下面代码所示：
```
Map<String, Object> map = new HashMap<>();
map.put("name", "CCC");
map.put("age", 40);
userMapper.insertByMap(map);
```

* ##### 使用对象
除了Map对象，我们也可直接使用普通的Java对象来作为查询条件的传参，比如我们可以直接使用User对象:
```
@Insert("INSERT INTO USER(NAME, AGE) VALUES(#{name}, #{age})")
int insertByUser(User user);
```
这样语句中的#{name}、#{age}就分别对应了User对象中的name和age属性。

### 增删改查
MyBatis针对不同的数据库操作分别提供了不同的注解来进行配置，在之前的示例中演示了@Insert，下面针对User表做一组最基本的增删改查作为示例：
```
public interface UserMapper {

    @Select("SELECT * FROM user WHERE name = #{name}")
    User findByName(@Param("name") String name);

    @Insert("INSERT INTO user(name, age) VALUES(#{name}, #{age})")
    int insert(@Param("name") String name, @Param("age") Integer age);

    @Update("UPDATE user SET age=#{age} WHERE name=#{name}")
    void update(User user);

    @Delete("DELETE FROM user WHERE id =#{id}")
    void delete(Long id);
}
```


### 返回结果的绑定
对于增、删、改操作相对变化较小。而对于“查”操作，我们往往需要进行多表关联，汇总计算等操作，那么对于查询的结果往往就不再是简单的实体对象了，往往需要返回一个与数据库实体不同的包装类，那么对于这类情况，就可以通过@Results和@Result注解来进行绑定，具体如下：
```
@Results({
    @Result(property = "name", column = "name"),
    @Result(property = "age", column = "age")
})
@Select("SELECT name, age FROM user")
List<User> findAll();
```

在上面代码中，@Result中的property属性对应User对象中的成员名，column对应SELECT出的字段名。在该配置中故意没有查出id属性，只对User对应中的name和age对象做了映射配置，这样可以通过下面的单元测试来验证查出的id为null，而其他属性不为null：
```
@Test
@Rollback
public void testUserMapper() throws Exception {
	List<User> userList = userMapper.findAll();
	for(User user : userList) {
		Assert.assertEquals(null, user.getId());
		Assert.assertNotEquals(null, user.getName());
	}
}
```

### 单元测试
```
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
        }
    }

}
```
更多其他注解的使用可参见文档：[http://www.mybatis.org/mybatis-3/zh/index.html](http://www.mybatis.org/mybatis-3/zh/index.html)



