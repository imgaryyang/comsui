package com.suidifu.mq.test;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.suidifu.mq.rpc.client.ConsistentHashRPCClient;
import com.suidifu.mq.test.config.application.TestConsistentHashApplication;
import com.suidifu.mq.test.config.producer.consistenthash.TestConsistentHashProducer1;
import com.suidifu.mq.test.config.producer.consistenthash.TestConsistentHashProducer2;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = { TestConsistentHashApplication.class })
@WebIntegrationTest
public class TestConsistentHashMq extends TestMq {
	@Autowired
	TestConsistentHashProducer1 singleProducer1;
	@Autowired
	TestConsistentHashProducer2 singleProducer2;

	protected ConsistentHashRPCClient rpc1() {
		return this.singleProducer1.rpc();
	}

	protected ConsistentHashRPCClient rpc2() {
		return this.singleProducer2.rpc();
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

	@Test
	public void testHash() {
		String expected_queue = rpc1().selectQueue("83bf4526-0ef9-441b-9c1b-d0d7fdfc478f");
		System.out.println("进入队列：" + expected_queue);
		String exchange = rpc2().selectQueue("83bf4526-0ef9-441b-9c1b-d0d7fdfc478f");
		System.out.println("进入队列：" + exchange);
		Assert.assertEquals("没有进入同一个队列", expected_queue, exchange);
	}
}
