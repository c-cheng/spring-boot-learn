## Spring Boot多数据源配置与使用

### 多数据源配置

在application.properties中配置两个数据源
```
#主数据源
spring.datasource.primary.driver-class-name=com.mysql.jdbc.Driver
spring.datasource.primary.url=jdbc:mysql://localhost:3306/spring_boot_learn?useUnicode=true&characterEncoding=utf8
spring.datasource.primary.username=spring_boot_learn
spring.datasource.primary.password=spring_boot_learn
#次数据源
spring.datasource.secondary.driver-class-name=com.mysql.jdbc.Driver
spring.datasource.secondary.url=jdbc:mysql://localhost:3306/spring_boot_learn_2?useUnicode=true&characterEncoding=utf8
spring.datasource.secondary.username=spring_boot_learn_2
spring.datasource.secondary.password=spring_boot_learn_2

#自动建表规则：create/create-drop/update/validate/none
spring.jpa.properties.hibernate.hbm2ddl.auto=create-drop
```

创建一个Spring配置类，定义两个DataSource用来读取application.properties中的不同配置。主数据源配置为spring.datasource.primary开头的配置，第二数据源配置为spring.datasource.secondary开头的配置。
```
@Configuration
@Configuration
public class DataSourceConfig {

    /**
     * 使用主数据源配置信息创建主数据源
     * @param dataSourceProperties 主数据源配置信息
     * @return 主数据源
     */
    @Bean(name = "primaryDataSource")
    @Primary
    public DataSource primaryDataSource(@Qualifier("primaryDataSourceProperties") DataSourceProperties dataSourceProperties) {
        return DataSourceBuilder.create()
                .driverClassName(dataSourceProperties.getDriverClassName())
                .url(dataSourceProperties.getUrl())
                .username(dataSourceProperties.getUsername())
                .password(dataSourceProperties.getPassword())
                .build();
    }
    
    /**
     * 使用次数据源配置信息创建次数据源
     * @param dataSourceProperties 次数据源配置信息
     * @return 次数据源
     */
    @Bean(name = "secondaryDataSource")
    public DataSource secondaryDataSource(@Qualifier("secondDataSourceProperties") DataSourceProperties dataSourceProperties) {
        return DataSourceBuilder.create()
                .driverClassName(dataSourceProperties.getDriverClassName())
                .url(dataSourceProperties.getUrl())
                .username(dataSourceProperties.getUsername())
                .password(dataSourceProperties.getPassword())
                .build();
    }

    /**
     * 读取主数据源配置信息
     */
    @Bean(name = "primaryDataSourceProperties")
    @ConfigurationProperties(prefix="spring.datasource.primary")
    public DataSourceProperties primaryDataSourceProperties(){
        return new DataSourceProperties();
    }

    /**
     * 读取次数据源配置信息
     */
    @Bean(name = "secondDataSourceProperties")
    @ConfigurationProperties(prefix="spring.datasource.secondary")
    public DataSourceProperties secondDataSourceProperties(){
        return new DataSourceProperties();
    }

}
```

### JdbcTemplate支持
对JdbcTemplate的支持比较简单，只需要为其注入对应的datasource即可，如下例子，在创建JdbcTemplate的时候分别注入名为primaryDataSource和secondaryDataSource的数据源来区分不同的JdbcTemplate。
```
@Configuration
public class JdbcTemplateConfig {

    @Bean(name = "primaryJdbcTemplate")
    public JdbcTemplate primaryJdbcTemplate(
            @Qualifier("primaryDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean(name = "secondaryJdbcTemplate")
    public JdbcTemplate secondaryJdbcTemplate(
            @Qualifier("secondaryDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

}
```


接下来通过测试用例来演示如何使用这两个针对不同数据源的JdbcTemplate。
```
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
```

### Spring-data-jpa支持
对于数据源的配置沿用上面的DataSourceConfig的实现。

新增对第一数据源的JPA配置，注意两处注释的地方，用于指定数据源对应的Entity实体和Repository定义位置，用@Primary区分主数据源。
```
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef="entityManagerFactoryPrimary",
        transactionManagerRef="transactionManagerPrimary",
        basePackages= { "top.ccheng.springbootlearn.primary" }) //设置Repository所在位置
public class JpaPrimaryConfig {
    @Autowired
    @Qualifier("primaryDataSource")
    private DataSource dataSource;
    @Autowired
    private JpaProperties jpaProperties;

    @Primary
    @Bean("entityManagerPrimary")
    public EntityManager entityManager(EntityManagerFactoryBuilder builder){
        return entityManagerFactoryPrimary(builder).getObject().createEntityManager();
    }

    @Primary
    @Bean(name = "entityManagerFactoryPrimary")
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryPrimary(EntityManagerFactoryBuilder builder){
        return builder.dataSource(dataSource)
                .properties(jpaProperties.getProperties())
                .packages("top.ccheng.springbootlearn.primary")//设置实体类所在位置
                .persistenceUnit("primaryPersistenceUnit")
                .build();
    }

    @Primary
    @Bean(name = "transactionManagerPrimary")
    public PlatformTransactionManager transactionManagerPrimary(EntityManagerFactoryBuilder builder) {
        return new JpaTransactionManager(entityManagerFactoryPrimary(builder).getObject());
    }
}
```


新增对第二数据源的JPA配置，内容与第一数据源类似，具体如下：
```
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef="entityManagerFactorySecond",
        transactionManagerRef="transactionManagerSecond",
        basePackages= { "top.ccheng.springbootlearn.second" }) //设置Repository所在位置
public class JpaSecondConfig {
    @Autowired
    @Qualifier("secondaryDataSource")
    private DataSource dataSource;
    @Autowired
    private JpaProperties jpaProperties;

    @Bean("entityManagerSecond")
    public EntityManager entityManager(EntityManagerFactoryBuilder builder){
        return entityManagerFactorySecond(builder).getObject().createEntityManager();
    }

    @Bean(name = "entityManagerFactorySecond")
    public LocalContainerEntityManagerFactoryBean entityManagerFactorySecond(EntityManagerFactoryBuilder builder){
        return builder.dataSource(dataSource)
                .properties(jpaProperties.getProperties())
                .packages("top.ccheng.springbootlearn.second")//设置实体类所在位置
                .persistenceUnit("secondPersistenceUnit")
                .build();
    }

    @Bean(name = "transactionManagerSecond")
    public PlatformTransactionManager transactionManagerSecond(EntityManagerFactoryBuilder builder) {
        return new JpaTransactionManager(entityManagerFactorySecond(builder).getObject());
    }
}
```

完成了以上配置之后，主数据源的实体和数据访问对象位于：com.didispace.domain.p，次数据源的实体和数据访问接口位于：com.didispace.domain.s。

分别在这两个package下创建各自的实体和数据访问接口

主数据源下，创建User实体和对应的Repository接口
```
@Table(name = "user_4")
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
    ....
}
public interface UserRepository extends JpaRepository<User, Long> {

}
```

从数据源下，创建Message实体和对应的Repository接口
```
@Table(name = "message_4")
@Entity
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", length = 20, nullable = false, unique = true, insertable = true, updatable = true)
    private Long id;

    @Column(name = "name", length = 32, nullable = false, unique = true, insertable = true, updatable = true)
    private String name;

    @Column(name = "content", length = 255, nullable = false, unique = true, insertable = true, updatable = true)
    private String content;
    ...
}
public interface MessageRepository extends JpaRepository<Message, Long> {

}
```


接下来通过测试用例来验证使用这两个针对不同数据源的配置进行数据操作。
```
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
```
