package top.ccheng.springbootlearn.controller;

import org.springframework.web.bind.annotation.*;
import top.ccheng.springbootlearn.vo.User;

import java.util.*;

/**
 * @author ccheng
 * @date 2018/9/8
 */
@RestController
@RequestMapping(value="/users")     // 通过这里配置使下面的映射都在/users下
public class UserController {

    /**
     * 线程安全的map
     */
    private static Map<Long, User> users = Collections.synchronizedMap(new HashMap<Long, User>());

    /**
     * 处理"/users/"的GET请求，用来获取用户列表
     * 还可以通过@RequestParam从页面中传递参数来进行查询条件或者翻页信息的传递
     * @return 用户列表
     */
    @RequestMapping(value="/", method= RequestMethod.GET)
    public List<User> getUserList() {
        return new ArrayList<User>(users.values());
    }

    /**
     * 处理"/users/"的POST请求，用来创建User
     * 除了@ModelAttribute绑定参数之外，还可以通过@RequestParam从页面中传递参数
     * @param user 用户信息
     * @return 成功返回success
     */
    @RequestMapping(value="/", method=RequestMethod.POST)
    public String postUser(@ModelAttribute User user) {
        users.put(user.getId(), user);
        return "success";
    }

    /**
     * 处理"/users/{id}"的GET请求，用来获取url中id值的User信息
     * url中的id可通过@PathVariable绑定到函数的参数中
     * @param id 用户ID
     * @return 用户信息
     */
    @RequestMapping(value="/{id}", method=RequestMethod.GET)
    public User getUser(@PathVariable Long id) {
        return users.get(id);
    }

    /**
     * 处理"/users/{id}"的PUT请求，用来更新User信息
     * @param id 用户ID
     * @param user 用户信息
     * @return 成功返回success
     */
    @RequestMapping(value="/{id}", method=RequestMethod.PUT)
    public String putUser(@PathVariable Long id, @ModelAttribute User user) {
        User u = users.get(id);
        u.setName(user.getName());
        u.setAge(user.getAge());
        users.put(id, u);
        return "success";
    }

    /**
     * 处理"/users/{id}"的DELETE请求，用来删除User
     * @param id 用户ID
     * @return 成功返回success
     */
    @RequestMapping(value="/{id}", method=RequestMethod.DELETE)
    public String deleteUser(@PathVariable Long id) {
        users.remove(id);
        return "success";
    }

}
