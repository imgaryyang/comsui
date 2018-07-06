package com.suidifu.mq.test;

import java.util.concurrent.CountDownLatch;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.suidifu.mq.rpc.client.TopicRPCClient;
import com.suidifu.mq.test.config.application.TestTopicApplication;
import com.suidifu.mq.test.config.producer.topic.TestTopicProducer1;
import com.suidifu.mq.test.config.producer.topic.TestTopicProducer2;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = { TestTopicApplication.class })
@WebIntegrationTest
public class TestTopicMq extends TestMq {
	@Autowired
	private TestTopicProducer1 topicProducer1;
	@Autowired
	private TestTopicProducer2 topicProducer2;

	@Override
	protected TopicRPCClient rpc1() {
		return topicProducer1.rpc();
	}

	@Override
	protected TopicRPCClient rpc2() {
		return topicProducer2.rpc();
	}

	@Test
	public void test() throws Exception {
		try {
			countlatch = new CountDownLatch(4);
			rpc1().routingKey("table0.update").sendAsync("testBean0", "testMethod0");
			rpc2().routingKey("table1.update").sendAsync("testBean1", "testMethod1");
			rpc1().routingKey("table4.update").sendAsync("testBean4", "testMethod4");
			rpc2().routingKey("table5.update").sendAsync("testBean5", "testMethod5");
			countlatch.await();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}

	}

}
