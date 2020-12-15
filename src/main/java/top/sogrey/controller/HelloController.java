package top.sogrey.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import top.sogrey.interfaces.AccessLimit;
import top.sogrey.models.ResponseResult;

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
		System.out.println();
		return "hello " + name;
	}

//    @AccessLimit(seconds=5, maxCount=5, needLogin=true)   //接口防刷
	@RequestMapping("/color")
	@ResponseBody
	public String setColor(String colorName) {
		Map<String, String> map = new HashMap<>();
		map.put("黑色", "30");
		map.put("black", "30");
		map.put("红色", "31");
		map.put("red", "31");
		map.put("绿色", "32");
		map.put("green", "32");
		map.put("黄色", "33");
		map.put("yellow", "33");
		map.put("蓝色", "34");
		map.put("blue", "34");
		map.put("紫红色", "35");
		map.put("pred", "35");
		map.put("青蓝色", "36");
		map.put("turquoise", "36");
		map.put("白色", "37");
		map.put("white", "37");
//    	Scanner scanner = new Scanner(System.in);
//    	System.out.println("请输入想要的颜色：黑色，红色，绿色，黄色，蓝色，紫红色，青蓝色，白色");
//    	String importText = scanner.next();
		String colorCode = null;
		for (String key : map.keySet()) {
			if (Objects.equals(key, colorName)) {
				colorCode = (String) map.get(colorName);
			}
		}
		System.out.println("你输入的颜色是\033[1;" + colorCode + "m" + colorName + "\033[0m \n");
		
		return "你输入的颜色： " + colorName + ", 颜色值：" + colorCode;
	}

}
