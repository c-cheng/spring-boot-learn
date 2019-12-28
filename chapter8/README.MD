## Spring Boot中使用Spring-data-jpa让数据访问更简单、更优雅

### 添加相关依赖
```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
```

### 在application.properties中配置数据源
```
spring.datasource.driver-class-name=com.mysql.jdbc.Driver
spring.datasource.url=jdbc:mysql://localhost:3306/spring_boot_learn?useUnicode=true&characterEncoding=utf8
spring.datasource.username=spring_boot_learn
spring.datasource.password=spring_boot_learn

#自动建表规则：create/create-drop/update/validate/none
spring.jpa.properties.hibernate.hbm2ddl.auto=create-drop
```

**spring.jpa.properties.hibernate.hbm2ddl.auto是hibernate的配置属性，其主要作用是：自动创建、更新、验证数据库表结构。该参数的几种配置如下：**
* create：每次加载hibernate时都会删除上一次的生成的表，然后根据你的model类再重新来生成新表，哪怕两次没有任何改变也要这样执行，这就是导致数据库表数据丢失的一个重要原因。
* create-drop：每次加载hibernate时根据model类生成表，但是sessionFactory一关闭,表就自动删除。
* update：最常用的属性，第一次加载hibernate时根据model类会自动建立起表的结构（前提是先建立好数据库），以后加载hibernate时根据model类自动更新表结构，即使表结构改变了但表中的行仍然存在不会删除以前的行。要注意的是当部署到服务器后，表结构是不会被马上建立起来的，是要等应用第一次运行起来后才会。
* validate：每次加载hibernate时，验证创建数据库表结构，只会和数据库中的表进行比较，不会创建新表，但是会插入新值。
* none：不执行任何操作

### 创建实体
创建一个User实体，包含id（主键）、name（姓名）、age（年龄）属性，通过ORM框架其会被映射到数据库表中，由于配置了hibernate.hbm2ddl.auto，在应用启动的时候框架会自动去数据库中创建对应的表。
```
@Table(name = "user_2")
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", length = 20, nullable = false, unique = true, insertable = true, updatable = true)
    private Long id;

    @Column(name = "name", length = 32, nullable = false, unique = true, insertable = true, updatable = true)
    private String name;

    @Column(name = "age", length = 20, nullable = false, unique = true, insertable = true, updatable = true)
    private Integer age;

    
}
```

### 创建数据访问接口
下面针对User实体创建对应的Repository接口实现对该实体的数据访问，如下代码：
```
public interface UserRepository extends JpaRepository<User, Integer> {
    /**
     * 根据姓名查找用户
     * @param name 姓名
     * @return 用户信息
     */
    User findByName(String name);

    /**
     * 根据姓名与年龄查找用户
     * @param name 姓名
     * @param age 年龄
     * @return 用户信息
     */
    User findByNameAndAge(String name, Integer age);

    /**
     * 自定义SQL查找用户
     * @param name 姓名
     * @return 用户信息
     */
    @Query("from User u where u.name=:name")
    User findUser(@Param("name") String name);
}
```

> 在Spring-data-jpa中，只需要编写类似上面这样的接口就可实现数据访问。不再像我们以往编写了接口时候还需要自己编写接口实现类，直接减少了我们的文件清单。

下面对上面的UserRepository做一些解释，该接口继承自JpaRepository，通过查看JpaRepository接口的API文档，可以看到该接口本身已经实现了创建（save）、更新（save）、删除（delete）、查询（findAll、findOne）等基本操作的函数，因此对于这些基础操作的数据访问就不需要开发者再自己定义。

在上例中，我们可以看到下面两个函数：
* User findByName(String name)
* User findByNameAndAge(String name, Integer age)

它们分别实现了按name查询User实体和按name和age查询User实体，可以看到我们这里没有任何类SQL语句就完成了两个条件查询方法。这就是Spring-data-jpa的一大特性：通过解析方法名创建查询。

除了通过解析方法名来创建查询外，它也提供通过使用@Query 注解来创建查询，您只需要编写JPQL语句，并通过类似“:name”来映射@Param指定的参数，就像例子中的第三个findUser函数一样。


### 单元测试
```
@RunWith(SpringRunner.class)
@SpringBootTest
public class Chapter8ApplicationTests {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testUserRepository() throws Exception {
        // 创建10条记录
        userRepository.save(new User("AAA", 10));
        userRepository.save(new User("BBB", 20));
        userRepository.save(new User("CCC", 30));
        userRepository.save(new User("DDD", 40));
        userRepository.save(new User("EEE", 50));
        userRepository.save(new User("FFF", 60));
        userRepository.save(new User("GGG", 70));
        userRepository.save(new User("HHH", 80));
        userRepository.save(new User("III", 90));
        userRepository.save(new User("JJJ", 100));

        // 测试findAll, 查询所有记录
        Assert.assertEquals(10, userRepository.findAll().size());

        // 测试findByName, 查询姓名为FFF的User
        Assert.assertEquals(60, userRepository.findByName("FFF").getAge().longValue());

        // 测试findUser, 查询姓名为FFF的User
        Assert.assertEquals(60, userRepository.findUser("FFF").getAge().longValue());

        // 测试findByNameAndAge, 查询姓名为FFF并且年龄为60的User
        Assert.assertEquals("FFF", userRepository.findByNameAndAge("FFF", 60).getName());

        // 测试删除姓名为AAA的User
        userRepository.delete(userRepository.findByName("AAA"));

        // 测试findAll, 查询所有记录, 验证上面的删除是否成功
        Assert.assertEquals(9, userRepository.findAll().size());

    }
}
```

### 遇到异常
java.lang.ClassNotFoundException: javax.xml.bind.JAXBException
> JAXB API是java EE 的API，因此在java SE 9.0 中不再包含这个 Jar 包。 
java 9 中引入了模块的概念，默认情况下，Java SE中将不再包含java EE 的Jar包 
而在 java 6/7 / 8 时关于这个API 都是捆绑在一起的

解决方法：在pom中手动引入相应依赖
```
<dependency>
    <groupId>javax.xml.bind</groupId>
    <artifactId>jaxb-api</artifactId>
</dependency>

```


