package top.sogrey;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication // Spring Boot项目的核心注解，主要目的是开启自动配置
public class HelloApplication {

	// 在main方法中启动一个应用，即：这个应用的入口
	public static void main(String[] args) {
		SpringApplication.run(HelloApplication.class, args);
//		SpringApplication app = new SpringApplication(HelloApplication.class);
//		app.setBannerMode(Banner.Mode.OFF);// 关闭banner
//		app.run(args);
	}

}
