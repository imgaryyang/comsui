package com.suidifu.mq.test.config.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.web.EmbeddedServletContainerAutoConfiguration;
import org.springframework.boot.autoconfigure.web.ServerPropertiesAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.suidifu.mq.test.config.PropertiesConfig;
import com.suidifu.mq.test.config.consumer.topic.TestTopicConsumer1;
import com.suidifu.mq.test.config.consumer.topic.TestTopicConsumer2;
import com.suidifu.mq.test.config.messagehandler.StringMessageHandler;
import com.suidifu.mq.test.config.producer.topic.TestTopicProducer1;
import com.suidifu.mq.test.config.producer.topic.TestTopicProducer2;

@Configuration
@Import({
		EmbeddedServletContainerAutoConfiguration.class,
		ServerPropertiesAutoConfiguration.class,
		PropertiesConfig.class,
		TestTopicProducer1.class,
		TestTopicProducer2.class,
		TestTopicConsumer1.class,
		TestTopicConsumer2.class,
		StringMessageHandler.class,
})
public class TestTopicApplication {
	public static void main(String[] args) throws Exception {
		SpringApplication.run(TestTopicApplication.class, args);
	}
}
