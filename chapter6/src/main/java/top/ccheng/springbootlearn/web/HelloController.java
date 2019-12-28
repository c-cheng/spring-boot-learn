package top.ccheng.springbootlearn.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import top.ccheng.springbootlearn.exception.MyException;

/**
 * @author ccheng
 * @date 2018/9/7
 */
@Controller
public class HelloController {

    @RequestMapping("/")
    public String index(ModelMap map) {
        throw new RuntimeException("运行时异常");
    }

    @RequestMapping("/myException")
    public String myException() throws MyException {
        throw new MyException("自定义异常");
    }
}
