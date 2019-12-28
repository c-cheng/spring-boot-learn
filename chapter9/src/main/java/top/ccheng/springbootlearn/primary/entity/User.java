package top.ccheng.springbootlearn.primary.entity;

import javax.persistence.*;

/**
 * @author ccheng
 * @date 2018/9/9
 */
@Table(name = "user_4")
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", length = 20, nullable = false, unique = true, insertable = true, updatable = true)
    private Long id;

    @Column(name = "name", length = 32, nullable = false, unique = false, insertable = true, updatable = true)
    private String name;

    @Column(name = "age", length = 20, nullable = false, unique = false, insertable = true, updatable = true)
    private Integer age;

    public User() {
    }

    public User(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
}
