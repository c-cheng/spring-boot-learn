package top.ccheng.springbootlearn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import top.ccheng.springbootlearn.entity.User;

/**
 * @author ccheng
 * @date 2018/9/9
 */
public interface UserRepository extends JpaRepository<User, Long> {
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
