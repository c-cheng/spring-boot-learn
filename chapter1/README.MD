# SpringBoot快速入门

## Spring Boot的主要优点：
* 为所有Spring开发者更快的入门
* 开箱即用，提供各种默认配置来简化项目配置
* 内嵌式容器简化Web项目
* 没有冗余代码生成和XML配置的要求

## Spring Boot的基础结构共三个文件:
* src/main/java  程序开发以及主程序入口
* src/main/resources 配置文件
* src/test/java  测试程序

## 引入Web模块

**1、默认的pom.xml内容如下，仅引入了两个模块：**
* spring-boot-starter：核心模块，包括自动配置支持、日志和YAML
* spring-boot-starter-test：测试模块，包括JUnit、Hamcrest、Mockito
```
<dependencies>
        <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter</artifactId>
        </dependency>
        
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
</dependencies>

```

**2、引入Web模块，需添加spring-boot-starter-web模块**
```
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-web</artifactId>
</dependency>

```

## 编写HelloWorld服务

**_创建HelloController类，内容如下_**
```
@RestController
public class HelloController {

    @RequestMapping("/hello")
    public String index() {
        return "Hello World";
    }
}

```
启动主程序（xxxxApplication），打开浏览器访问http://localhost:8080/hello，可以看到页面输出Hello World

## 编写单元测试用例
```
@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringbootlearnApplicationTests {

    private MockMvc mvc;

    @Before
    public void setUp() throws Exception {
        mvc = MockMvcBuilders.standaloneSetup(new HelloController()).build();
    }

    @Test
    public void getHello() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/hello").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(CoreMatchers.equalTo("Hello World")));
    }

}

```

* 注意引入下面内容，让status、content函数可用
```
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

```










