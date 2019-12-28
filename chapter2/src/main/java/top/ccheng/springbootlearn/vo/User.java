package top.ccheng.springbootlearn.vo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author ccheng
 * @date 2018/9/8
 */
@Component
public class User {
    @Value("${top.ccheng.user.firstName}")
    private String firstName;
    @Value("${top.ccheng.user.secondName}")
    private String secondName;
    @Value("${top.ccheng.user.name}")
    private String name;
    @Value("${top.ccheng.user.age}")
    private Integer age;

    @Value("${top.ccheng.include.value}")
    private String includeValue;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getIncludeValue() {
        return includeValue;
    }

    public void setIncludeValue(String includeValue) {
        this.includeValue = includeValue;
    }

    @Override
    public String toString() {
        return "User{" +
                "firstName='" + firstName + '\'' +
                ", secondName='" + secondName + '\'' +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", includeValue='" + includeValue + '\'' +
                '}';
    }
}
