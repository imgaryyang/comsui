package com.suidifu.mq.test.spring;

import org.springframework.boot.autoconfigure.web.EmbeddedServletContainerAutoConfiguration;
import org.springframework.boot.autoconfigure.web.ServerPropertiesAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.suidifu.mq.util.SpringContextUtil;

@Configuration
@Import({
		EmbeddedServletContainerAutoConfiguration.class,
		ServerPropertiesAutoConfiguration.class,
		
		TestBean1.class,
		TestBean2.class,
		TestBean3.class,
		SpringContextUtil.class,
})
public class TestApplication {
	
}
