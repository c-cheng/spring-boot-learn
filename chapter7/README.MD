## Spring Boot中使用JdbcTemplate访问数据库

### 在pom中添加jdbc依赖
```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-jdbc</artifactId>
</dependency>

```

### 在pom中添加MySQL依赖
```
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
</dependency>
```


### application.properties中配置数据源
```
spring.datasource.driver-class-name=com.mysql.jdbc.Driver
spring.datasource.url=jdbc:mysql://localhost:3306/spring_boot_learn
spring.datasource.username=spring_boot_learn
spring.datasource.password=spring_boot_learn
```

### 使用JdbcTemplate操作数据库
Spring的JdbcTemplate是自动配置的，你可以直接使用@Autowired来注入到你自己的bean中来使用。

定义包含有插入、删除、查询的抽象接口UserService
```
public interface UserService {

    /**
     * 新增一个用户
     * @param name 用户姓名
     * @param age 年龄
     */
    void create(String name, Integer age);

    /**
     * 根据name删除一个用户高
     * @param name 用户姓名
     */
    void deleteByName(String name);

    /**
     * 获取用户总量
     */
    int countAllUsers();

    /**
     * 删除所有用户
     */
    void deleteAllUsers();

}
```

通过JdbcTemplate实现UserService中定义的数据访问操作
```
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void create(String name, Integer age) {
        jdbcTemplate.update("insert into USER(NAME, AGE) values(?, ?)", name, age);
    }

    @Override
    public void deleteByName(String name) {
        jdbcTemplate.update("delete from USER where NAME = ?", name);
    }

    @Override
    public int countAllUsers() {
        return jdbcTemplate.queryForObject("select count(1) from USER", Integer.class);
    }

    @Override
    public void deleteAllUsers() {
        jdbcTemplate.update("delete from USER");
    }
}
```


创建对UserService的单元测试用例，通过创建、删除和查询来验证数据库操作的正确性。
```
@RunWith(SpringRunner.class)
@SpringBootTest
public class Chapter7ApplicationTests {

    @Autowired
    private UserService userSerivce;

    @Before
    public void setUp() {
        // 准备，清空user表
        userSerivce.deleteAllUsers();
    }

    @Test
    public void test() throws Exception {
        // 插入5个用户
        userSerivce.create("a", 1);
        userSerivce.create("b", 2);
        userSerivce.create("c", 3);
        userSerivce.create("d", 4);
        userSerivce.create("e", 5);

        // 查数据库，应该有5个用户
        Assert.assertEquals(5, userSerivce.countAllUsers());

        // 删除两个用户
        userSerivce.deleteByName("a");
        userSerivce.deleteByName("e");

        // 查数据库，应该有3个用户
        Assert.assertEquals(3, userSerivce.countAllUsers());

    }
}
```

