## Spring Boot中使用MongoDB数据库

### 引入依赖
```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-mongodb</artifactId>
</dependency>

```

### 快速开始使用Spring-data-mongodb
若MongoDB的安装配置采用默认端口，那么在自动配置的情况下，我们不需要做任何参数配置，就能马上连接上本地的MongoDB。下面直接使用spring-data-mongodb来尝试对mongodb的存取操作。（记得启动您的mongodb）

创建要存储的User实体，包含属性：id、username、age
```
public class User {

    @Id
    private Long id;
    private String username;
    private Integer age;

    public User(Long id, String username, Integer age) {
        this.id = id;
        this.username = username;
        this.age = age;
    }

    // 省略getter和setter

}
```


实现User的数据访问对象：UserRepository
```
public interface UserRepository extends MongoRepository<User, Long> {

    User findByUsername(String username);

}
```

### 测试
```
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(Application.class)
public class ApplicationTests {

	@Autowired
	private UserRepository userRepository;

	@Before
	public void setUp() {
		userRepository.deleteAll();
	}

	@Test
	public void test() throws Exception {

		// 创建三个User，并验证User总数
		userRepository.save(new User(1L, "didi", 30));
		userRepository.save(new User(2L, "mama", 40));
		userRepository.save(new User(3L, "kaka", 50));
		Assert.assertEquals(3, userRepository.findAll().size());

		// 删除一个User，再验证User总数
		User u = userRepository.findOne(1L);
		userRepository.delete(u);
		Assert.assertEquals(2, userRepository.findAll().size());

		// 删除一个User，再验证User总数
		u = userRepository.findByUsername("mama");
		userRepository.delete(u);
		Assert.assertEquals(1, userRepository.findAll().size());

	}

}
```


### 参数配置
通过上面的例子，我们可以轻而易举的对MongoDB进行访问，但是实战中，应用服务器与MongoDB通常不会部署于同一台设备之上，这样就无法使用自动化的本地配置来进行使用。这个时候，我们也可以方便的配置来完成支持，只需要在application.properties中加入mongodb服务端的相关配置，具体示例如下：
```
spring.data.mongodb.uri=mongodb://name:pass@localhost:27017/test
#在尝试此配置时，记得在mongo中对test库创建具备读写权限的用户（用户名为name，密码为pass），不同版本的用户创建语句不同，注意查看文档做好准备工作

#若使用mongodb 2.x，也可以通过如下参数配置，该方式不支持mongodb 3.x。
spring.data.mongodb.host=localhost spring.data.mongodb.port=27017
```


