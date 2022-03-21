package top.sogrey.csv2json;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication // Spring Boot项目的核心注解，主要目的是开启自动配置
public class App {

	// 在main方法中启动一个应用，即：这个应用的入口
	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
		// or
//		SpringApplication app = new SpringApplication(SpringbootMainApplication.class);
//		app.setBannerMode(Banner.Mode.OFF);// 关闭banner
//		app.run(args);
		
		System.out.println("  \n" +
                "    启动成功.    \n");
	}
}
