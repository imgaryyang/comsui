package com.suidifu.mq.test.config.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.web.EmbeddedServletContainerAutoConfiguration;
import org.springframework.boot.autoconfigure.web.ServerPropertiesAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.suidifu.mq.test.config.PropertiesConfig;
import com.suidifu.mq.test.config.consumer.consistenthash.TestConsistentHashConsumer1;
import com.suidifu.mq.test.config.consumer.consistenthash.TestConsistentHashConsumer2;
import com.suidifu.mq.test.config.messagehandler.StringMessageHandler;
import com.suidifu.mq.test.config.producer.consistenthash.TestConsistentHashProducer1;
import com.suidifu.mq.test.config.producer.consistenthash.TestConsistentHashProducer2;

@Configuration
@Import({
		EmbeddedServletContainerAutoConfiguration.class,
		ServerPropertiesAutoConfiguration.class,
		PropertiesConfig.class,
		TestConsistentHashProducer1.class,
		TestConsistentHashProducer2.class,
		TestConsistentHashConsumer1.class,
		TestConsistentHashConsumer2.class,
		StringMessageHandler.class,
})
public class TestConsistentHashApplication {
	public static void main(String[] args) throws Exception {
		SpringApplication.run(TestConsistentHashApplication.class, args);
	}
}
