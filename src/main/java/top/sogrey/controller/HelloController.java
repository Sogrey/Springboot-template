package top.sogrey.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller // 标明这是一个SpringMVC的Controller控制器
@RequestMapping(value = "/api")
public class HelloController {

	@RequestMapping("/")
    @ResponseBody
    public String hello() {
        return "hello world";
    }
    
    @RequestMapping("/hello")
    @ResponseBody
    public String sayHello(String name) {
        return "hello "+name;
    }

}
