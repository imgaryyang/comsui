package com.suidifu.morganstanley.configuration;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.suidifu.morganstanley.interceptor.ApiAccessInterceptor;
import com.suidifu.morganstanley.interceptor.WebAccessInterceptor;
import com.zufangbao.sun.utils.FilenameUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.nio.charset.Charset;
import java.util.List;

@EnableScheduling
@EnableAutoConfiguration
@ComponentScan({"com.zufangbao", "com.suidifu"})
@EnableJpaRepositories({"com.zufangbao", "com.suidifu"})
@Configuration
public class CustomMVCConfiguration extends WebMvcConfigurerAdapter {

    public static void main(String[] args) {
        SpringApplication.run(CustomMVCConfiguration.class, args);
    }

    @Bean
    public WebAccessInterceptor webAccessInterceptor() {
        return new WebAccessInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //registry.addInterceptor(apiAccessInterceptor()).addPathPatterns("/api/**").addPathPatterns("/pre/**");
    	registry.addInterceptor(apiAccessInterceptor())
//          .addPathPatterns("/api/**")
          .addPathPatterns("/pre/**").excludePathPatterns("/api/notify/internal/**");
        //.addPathPatterns("/api/notify/**"); api/notify/暂时不验签（requestBody），需要回调配合
        registry.addInterceptor(webAccessInterceptor()).addPathPatterns("/web/**");
    }

    @Bean
    public ApiAccessInterceptor apiAccessInterceptor() {
        return new ApiAccessInterceptor();
    }

    @Bean
    public HttpMessageConverter<String> responseBodyConverter() {
        return new StringHttpMessageConverter(Charset.forName(FilenameUtils.UTF_8));
    }

    @Bean
    public MappingJackson2HttpMessageConverter customJackson2HttpMessageConverter(){
        MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
        jsonConverter.setObjectMapper(objectMapper);
        return jsonConverter;
    }

//    @Override
//    public void configureMessageConverters(
//            List<HttpMessageConverter<?>> converters) {
//        converters.add(customJackson2HttpMessageConverter());
//        super.configureMessageConverters(converters);
//        converters.add(responseBodyConverter());
//    }

    @Override
    public void configureContentNegotiation(
            ContentNegotiationConfigurer configurer) {
        configurer.favorPathExtension(false);
    }


    @Override
    public void configureMessageConverters(
            List<HttpMessageConverter<?>> converters) {
        converters.add(responseBodyConverter());
        converters.add(customJackson2HttpMessageConverter());
        super.configureMessageConverters(converters);
    }
}