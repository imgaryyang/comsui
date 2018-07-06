package com.suidifu.citigroup.configuration;

import java.nio.charset.Charset;
import java.util.List;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.suidifu.citigroup.interceptor.ApiAccessInterceptor;

@EnableScheduling
@EnableAutoConfiguration
@ComponentScan({"com.zufangbao","com.suidifu"})
@EntityScan({"com.zufangbao","com.suidifu"})
@EnableJpaRepositories({"com.zufangbao","com.suidifu"})
@Configuration
public class CustomMVCConfiguration extends WebMvcConfigurerAdapter {
	
	public static void main(String[] args) {  
        SpringApplication.run(CustomMVCConfiguration.class, args);  
    } 
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {  
        registry.addInterceptor(apiAccessInterceptor()).addPathPatterns("/api/v2/**").addPathPatterns("/api/v3/**");
        //.addPathPatterns("/api/notify/**");  
    }  

	@Bean
	public ApiAccessInterceptor apiAccessInterceptor(){
		return new ApiAccessInterceptor();
	}

	@Bean
	public MappingJackson2HttpMessageConverter customJackson2HttpMessageConverter(){

	    MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
        jsonConverter.setObjectMapper(objectMapper);
        return jsonConverter;
    }
	
    @Bean
    public HttpMessageConverter<String> responseBodyConverter() {
        StringHttpMessageConverter converter = new StringHttpMessageConverter(
                Charset.forName("UTF-8"));
        return converter;
    }

    @Override
    public void configureMessageConverters(
            List<HttpMessageConverter<?>> converters) {
        converters.add(responseBodyConverter());
        converters.add(customJackson2HttpMessageConverter());
        super.configureMessageConverters(converters);
    }

    @Override
    public void configureContentNegotiation(
            ContentNegotiationConfigurer configurer) {
        configurer.favorPathExtension(false);
    }
    @Bean
	public FilterRegistrationBean filterRegistrationBean() {
	    FilterRegistrationBean registrationBean = new FilterRegistrationBean();
	    CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
	    characterEncodingFilter.setForceEncoding(true);
	    characterEncodingFilter.setEncoding("UTF-8");
	    registrationBean.setFilter(characterEncodingFilter);
	    return registrationBean;
	}
}
