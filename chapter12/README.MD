## Spring Boot 整合MyBatis

### pom引入依赖
```
<dependency>
    <groupId>org.mybatis.spring.boot</groupId>
    <artifactId>mybatis-spring-boot-starter</artifactId>
</dependency>

<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <scope>runtime</scope>
</dependency>
```

### application.properties配置数据源
```
spring.datasource.driver-class-name=com.mysql.jdbc.Driver
spring.datasource.url=jdbc:mysql://localhost:3306/spring_boot_learn?useUnicode=true&characterEncoding=utf8
spring.datasource.username=spring_boot_learn
spring.datasource.password=spring_boot_learn
```

### 简单使用
在Mysql中创建USER_5表，包含id(BIGINT)、name(INT)、age(VARCHAR)字段。同时，创建映射对象User
```
public class User {

    private Long id;
    private String name;
    private Integer age;

    // 省略getter和setter

}
```

创建User映射的操作UserMapper，为了后续单元测试验证，实现插入和查询操作
```
@Mapper
@Component
public interface UserMapper {

    @Select("SELECT * FROM USER_5 WHERE NAME = #{name}")
    User findByName(@Param("name") String name);

    @Insert("INSERT INTO USER(NAME, AGE) VALUES(#{name}, #{age})")
    int insert(@Param("name") String name, @Param("age") Integer age);

}
```

单元测试
```
@RunWith(SpringRunner.class)
@SpringBootTest
public class Chapter12ApplicationTests {

    @Autowired
    private UserMapper userMapper;

    @Test
    @Rollback
    public void findByName() throws Exception {
        userMapper.insert("AAA", 20);
        User u = userMapper.findByName("AAA");
        Assert.assertEquals(20, u.getAge().intValue());
    }

}
```

