package top.ccheng.springbootlearn;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author ccheng
 * @date 2018/9/8
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket createRestApi(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(createApiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("top.ccheng"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo createApiInfo(){
        return new ApiInfoBuilder()
                .title("Spring Boot中使用Swagger2构建RESTful APIs")
                .description("测试SpringBoot中如何使用Swagger2")
                .termsOfServiceUrl("http://ccheng.top")
                .contact(new Contact("chencheng", "http://ccheng.top", "ccheng.top@qq.com"))
                .version("1.0")
                .build();
    }
}
