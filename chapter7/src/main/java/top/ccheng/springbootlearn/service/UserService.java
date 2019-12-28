package top.ccheng.springbootlearn.service;

/**
 * @author ccheng
 * @date 2018/9/9
 */
public interface UserService {

    /**
     * 新增一个用户
     * @param name 用户姓名
     * @param age 年龄
     */
    void create(String name, Integer age);

    /**
     * 根据name删除一个用户高
     * @param name 用户姓名
     */
    void deleteByName(String name);

    /**
     * 获取用户总量
     */
    int countAllUsers();

    /**
     * 删除所有用户
     */
    void deleteAllUsers();

}
