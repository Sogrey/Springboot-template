package top.sogrey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import top.sogrey.interceptors.FangshuaInterceptor;

@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {
	@Autowired
	private FangshuaInterceptor interceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(interceptor);
	}
}