package top.ccheng.springbootlearn;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;


/**
 * @author ccheng
 * @date 2018/9/13
 */
@Mapper
@Component
public interface UserMapper {

    @Delete("DELETE FROM USER_6")
    int deleteAll();

    @Insert("INSERT INTO USER_6(NAME, AGE) VALUES(#{name}, #{age})")
    int insert(@Param("name") String name, @Param("age") Integer age);

    @Insert("INSERT INTO USER_6(NAME, AGE) VALUES(#{name,jdbcType=VARCHAR}, #{age,jdbcType=INTEGER})")
    int insertByMap(Map<String, Object> map);

    @Insert("INSERT INTO USER_6(NAME, AGE) VALUES(#{name}, #{age})")
    int insertByUser(User user);

    @Select("SELECT * FROM USER_6 WHERE NAME = #{name}")
    User findByName(@Param("name") String name);

    @Update("UPDATE USER_6 SET age=#{age} WHERE name=#{name}")
    void update(User user);

    @Delete("DELETE FROM USER_6 WHERE id =#{id}")
    void delete(Long id);

    @Results({
            @Result(property = "name", column = "name"),
            @Result(property = "age", column = "age")
    })
    @Select("SELECT name, age FROM USER_6")
    List<User> findAll();
}
