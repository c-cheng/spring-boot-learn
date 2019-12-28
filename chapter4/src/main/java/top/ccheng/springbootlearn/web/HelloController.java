package top.ccheng.springbootlearn.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author ccheng
 * @date 2018/9/7
 */
@Controller
public class HelloController {

    @RequestMapping("/")
    public String index(ModelMap map) {
        // 加入一个属性，用来在模板中读取
        map.addAttribute("host", "http://ccheng.top");
        // return模板文件的名称，对应src/main/resources/templates/index.html
        //无需包含src/main/resources/templates路径，SpringBoot会自动读取该目录
        return "index";
    }
}
