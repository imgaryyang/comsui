package com.suidifu.mq.test.config.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.web.EmbeddedServletContainerAutoConfiguration;
import org.springframework.boot.autoconfigure.web.ServerPropertiesAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.suidifu.mq.test.config.PropertiesConfig;
import com.suidifu.mq.test.config.consumer.common.TestCommonConsumer1;
import com.suidifu.mq.test.config.consumer.common.TestCommonConsumer2;
import com.suidifu.mq.test.config.messagehandler.StringMessageHandler;
import com.suidifu.mq.test.config.producer.common.TestCommonProducer1;
import com.suidifu.mq.test.config.producer.common.TestCommonProducer2;

@Configuration
@Import({
		EmbeddedServletContainerAutoConfiguration.class,
		ServerPropertiesAutoConfiguration.class,
		PropertiesConfig.class,
		TestCommonProducer1.class,
		TestCommonProducer2.class,
		TestCommonConsumer1.class,
		TestCommonConsumer2.class,
		StringMessageHandler.class,
})
public class TestCommonApplication {
	public static void main(String[] args) throws Exception {
		SpringApplication.run(TestCommonApplication.class, args);
	}
}
