package com.suidifu.mq.test;

import com.suidifu.mq.rpc.RpcClient;
import com.suidifu.mq.rpc.request.Request;
import org.junit.Assert;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeoutException;

public abstract class TestMq {

	protected static CountDownLatch countlatch = null;

	protected abstract RpcClient rpc1();

	protected abstract RpcClient rpc2();

	// 正常情况下随机测试
	public void test_random() throws Exception {
		try {
			int size = 1;
			System.out.println("<<<<<test_random>>>>>");
			countlatch = new CountDownLatch(size);
			random(size);
			countlatch.await();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// 测试优先级,优先级越高的先处理
	public void test_priority() throws Exception {
		try {
			int size = 10;
			System.out.println("<<<<<test_priority>>>>>");
			countlatch = new CountDownLatch(size);
			priority(Boolean.FALSE, size);
			countlatch.await();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	public static void countDown() {
		countlatch.countDown();
	}

	private void random(int size) throws Exception {
		for (int i = 0; i < size; i++) {
			boolean sync = (i % (new Random().nextInt(5) + 1) == 0);
			Request request = new Request();
			request.setBean("TestBean");
			request.setMethod("test-" + sync + "-" + i);
			request.setBusinessId(sync + "-" + i + "-qd2jmod21nfop121nmd2");
			request.setParamTypes(new String[] { String.class.getTypeName() });
			request.setParams(new Object[] { "hello中文...." });
//			if (sync)
				sendSyncMsg(request, i);
//			else
//				sendAsyncMsg(request, i);
		}
	}

	private void priority(boolean sync, int size) throws Exception {
		for (int i = 0; i < size; i++) {
			Request request = new Request();
			request.setBean("TestBean");
			request.setMethod("test-" + sync + "-" + i);
			request.setBusinessId("qd2jmod21nfop121nmd2");// 在同一个队列里看优先级
			request.setParamTypes(new String[] { String.class.getTypeName() });
			request.setParams(new Object[] { "hello中文...." });
			if (i == 9)// 9号同步发送的会先处理
				sendSyncMsg(request, i);
			else
				sendAsyncMsg(request, i);
		}
	}

	// 异步调用
	private void sendAsyncMsg(Request request, int i) throws Exception {
		System.out.println("发起异步调用.." + request.getBusinessId());
		this.rpc1().sendAsync(request);
	}

	// 同步调用
	private void sendSyncMsg(Request request, int i) throws Exception {
		System.out.println("发起同步调用.." + request.getBusinessId());
		String rs = null;
		if (i % 2 == 0) {
			System.out.println("producer1...");
			rs = this.rpc1().sendSync(String.class, request, i);// 设置优先级
		} else {
			System.out.println("producer2...");
			rs = this.rpc2().sendSync(String.class, request, i);// 设置优先级
		}
		Assert.assertEquals(request.getMethod(), rs);
	}

	public void test2() {
		boolean sync = Boolean.FALSE;
		Request request = new Request();
		request.setBean("TestBean");
		request.setMethod("test-" + sync + "-" + 0);
		request.setBusinessId(sync + "-" + 0 + "-qd2jmod21nfop121nmd2");
		request.setParamTypes(new String[] { String.class.getTypeName() });
		request.setParams(new Object[] { "hello中文...." });
		try {
			countlatch = new CountDownLatch(2);
			// sendAsyncMsg(request, 0);
			System.out.println("发起同步调用.." + request.getBusinessId());
			String rs = this.rpc1().sendSync(String.class, request, 0);
			System.out.println("同步调用结果：" + rs);

			System.out.println("发起同步调用.." + request.getBusinessId());
			rs = this.rpc2().sendSync(String.class, request, 0);
			System.out.println("同步调用结果：" + rs);

			countlatch.await();
		} catch (TimeoutException e) {
			System.out.println("---->>>><<<" + e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("finish...");
	}
}
