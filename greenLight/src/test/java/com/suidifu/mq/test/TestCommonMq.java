package com.suidifu.mq.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.suidifu.mq.rpc.RpcClient;
import com.suidifu.mq.test.config.application.TestCommonApplication;
import com.suidifu.mq.test.config.consumer.common.TestCommonConsumer1;
import com.suidifu.mq.test.config.consumer.common.TestCommonConsumer2;
import com.suidifu.mq.test.config.producer.common.TestCommonProducer1;
import com.suidifu.mq.test.config.producer.common.TestCommonProducer2;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = { TestCommonApplication.class })
@WebIntegrationTest
public class TestCommonMq extends TestMq {
	@Autowired
	TestCommonProducer1 commonProducer1;
	@Autowired
	TestCommonProducer2 commonProducer2;
	@Autowired
	TestCommonConsumer1 commonConsumer1;
	@Autowired
	TestCommonConsumer2 commonConsumer2;

	protected RpcClient rpc1() {
		return this.commonProducer1.rpc();
	}

	protected RpcClient rpc2() {
		return this.commonProducer2.rpc();
	}

	// 正常情况下随机测试
	@Test
	public void test_random() throws Exception {
		super.test_random();
	}

	// 测试优先级,优先级越高的先处理
	@Test
	public void test_priority() throws Exception {
		super.test_priority();
	}

	@Test
	public void test2() {
		super.test2();
	}
}
