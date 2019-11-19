package cn.zc.project.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Auther: zhangchao
 * @Date: 2019-11-15 11:11
 * @classDesc: 功能描述:(类的作用)
 * @Version: 1.0
 */
@Controller
public class TestController {
    //@RequestMapping("/")
    public String view(){
        return "redirect:/swagger-ui.html";
    }
}

