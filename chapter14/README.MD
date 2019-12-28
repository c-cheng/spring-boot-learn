## Spring Boot中使用Flyway来管理数据库版本

### Flyway简介
Flyway是一个简单开源数据库版本控制器（约定大于配置），主要提供migrate、clean、info、validate、baseline、repair等命令。它支持SQL（PL/SQL、T-SQL）方式和Java方式，支持命令行客户端等，还提供一系列的插件支持（Maven、Gradle、SBT、ANT等）。

官方网站：https://flywaydb.org/

这里对于Flyway的自身功能不做过多的介绍

### 在pom.xml中增加flyway依赖、数据库依赖：
```
<dependency>
    <groupId>org.flywaydb</groupId>
    <artifactId>flyway-core</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <scope>runtime</scope>
</dependency>
<dependency>
    <groupId>javax.xml.bind</groupId>
    <artifactId>jaxb-api</artifactId>
</dependency>
```

### 按Flyway的规范创建版本化的SQL脚本。
在工程的src/main/resources目录下创建db目录，在db目录下创建版本化的SQL脚本:V1__初始化.sql
```
DROP TABLE IF EXISTS USER ;
CREATE TABLE `USER` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(20) NOT NULL COMMENT '姓名',
  `age` int(5) DEFAULT NULL COMMENT '年龄',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

### 在application.properties文件中配置Flyway要加载的SQL脚本位置、数据源
```
spring.datasource.driver-class-name=com.mysql.jdbc.Driver
spring.datasource.url=jdbc:mysql://localhost:3306/spring_boot_learn_3?useUnicode=true&characterEncoding=utf8
spring.datasource.username=spring_boot_learn_3
spring.datasource.password=spring_boot_learn_3

spring.flyway.locations=classpath:/db
```
> flyway会校验数据库与版本化SQL，如果不一致就会校验失败无法启动，因为之前的测试已经在spring_boot_learn数据库中创建了其他表，因此这里创建了新的空的数据库spring_boot_learn_3进行测试

### 添加单元测试
```
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
```

### 执行测试，此时我们在日志中可以看到如下信息：
```
2018-09-14 13:09:34.236  INFO 1522 --- [           main] o.f.core.internal.util.VersionPrinter    : Flyway Community Edition 5.0.7 by Boxfuse
2018-09-14 13:09:34.241  INFO 1522 --- [           main] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Starting...
2018-09-14 13:09:34.622  INFO 1522 --- [           main] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Start completed.
2018-09-14 13:09:34.624  INFO 1522 --- [           main] o.f.c.internal.database.DatabaseFactory  : Database: jdbc:mysql://localhost:3306/spring_boot_learn_3 (MySQL 8.0)
2018-09-14 13:09:34.627  WARN 1522 --- [           main] o.f.core.internal.database.Database      : Flyway upgrade recommended: MySQL 8.0 is newer than this version of Flyway and support has not been tested.
2018-09-14 13:09:34.673  INFO 1522 --- [           main] o.f.core.internal.command.DbValidate     : Successfully validated 1 migration (execution time 00:00.014s)
2018-09-14 13:09:34.691  INFO 1522 --- [           main] o.f.c.i.s.JdbcTableSchemaHistory         : Creating Schema History table: `spring_boot_learn_3`.`flyway_schema_history`
2018-09-14 13:09:34.844  INFO 1522 --- [           main] o.f.core.internal.command.DbMigrate      : Current version of schema `spring_boot_learn_3`: << Empty Schema >>
2018-09-14 13:09:34.850  INFO 1522 --- [           main] o.f.core.internal.command.DbMigrate      : Migrating schema `spring_boot_learn_3` to version 1 - 初始化
2018-09-14 13:09:34.873  WARN 1522 --- [           main] o.f.c.i.database.ExecutableSqlScript     : DB: Unknown table 'spring_boot_learn_3.user' (SQL State: 42S02 - Error Code: 1051)
2018-09-14 13:09:34.921  INFO 1522 --- [           main] o.f.core.internal.command.DbMigrate      : Successfully applied 1 migration to schema `spring_boot_learn_3` (execution time 00:00.232s)
```
Flyway监测到需要运行版本脚本来初始化数据库，因此执行了 V1__初始化.sql 脚本，从而创建了user表

### 重新执行测试，此时我们会发现日志输出与之前不同：
```
2018-09-14 13:13:14.720  INFO 1562 --- [           main] o.f.core.internal.util.VersionPrinter    : Flyway Community Edition 5.0.7 by Boxfuse
2018-09-14 13:13:14.724  INFO 1562 --- [           main] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Starting...
2018-09-14 13:13:15.103  INFO 1562 --- [           main] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Start completed.
2018-09-14 13:13:15.106  INFO 1562 --- [           main] o.f.c.internal.database.DatabaseFactory  : Database: jdbc:mysql://localhost:3306/spring_boot_learn_3 (MySQL 8.0)
2018-09-14 13:13:15.109  WARN 1562 --- [           main] o.f.core.internal.database.Database      : Flyway upgrade recommended: MySQL 8.0 is newer than this version of Flyway and support has not been tested.
2018-09-14 13:13:15.162  INFO 1562 --- [           main] o.f.core.internal.command.DbValidate     : Successfully validated 1 migration (execution time 00:00.021s)
2018-09-14 13:13:15.186  INFO 1562 --- [           main] o.f.core.internal.command.DbMigrate      : Current version of schema `spring_boot_learn_3`: 1
2018-09-14 13:13:15.187  INFO 1562 --- [           main] o.f.core.internal.command.DbMigrate      : Schema `spring_boot_learn_3` is up to date. No migration necessary.
```
由于初始化脚本已经执行过，此时数据库中的表结构与 V1__初始化.sql 脚本完全一致，所以这次执行就没有再去执行 V1__初始化.sql 脚本来重建user表。

### 修改并校验
我们可以尝试修改一下 V1__初始化.sql 脚本中的name字段长度，重新执行测试，此时我们可以得到如下错误：
```
Caused by: org.flywaydb.core.api.FlywayException: Validate failed: Migration checksum mismatch for migration version 1
-> Applied to database : -85176594
-> Resolved locally    : -112192011
```

由于初始化脚本的改动，Flyway校验失败，认为当前的 V1__初始化.sql 脚本与上一次执行的内容不同，提示报错并终止程序，以免造成更严重的数据结构破坏。
