package com.suidifu.datasync.config;

import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Controller
@Configuration
public class WebMvcConfigurer extends WebMvcConfigurerAdapter {
	@Value("classpath:/html/index.html")
	private Resource indexHtml;

	@Bean
	@Order(Ordered.HIGHEST_PRECEDENCE)
	public CharacterEncodingFilter characterEncodingFilter() {
		return new CharacterEncodingFilter("UTF-8", true);
	}

	/**
	 * 定义静态资源路径,访问/404.html即可
	 */
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/static/**").addResourceLocations("classpath:/html/");
	}

	/**
	 * 默认首页
	 * 
	 * @return
	 */
	@RequestMapping("/")
	public Object index() {
		return ResponseEntity.ok().body(indexHtml);
	}

}
