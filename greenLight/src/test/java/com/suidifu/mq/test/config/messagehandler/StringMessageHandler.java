package com.suidifu.mq.test.config.messagehandler;

import org.springframework.stereotype.Component;

import com.suidifu.mq.consumer.messagehandler.AbstractMessageHandler;
import com.suidifu.mq.rpc.request.Request;
import com.suidifu.mq.test.TestMq;

@Component("consumerMessageHandler")
public class StringMessageHandler extends AbstractMessageHandler<String> {

	@Override
	protected String handleSyncMessage(Request request, String queue) {
		String msg = null;
		try {
			// Thread.currentThread();
			// Thread.sleep(5 * 1000);
			msg = queue + "<<<<<同步handlerRequest..10秒内已处理1...." + request.getMethod() + request.getBusinessId();
			// int i=1/0;
			System.out.println(msg);
			return request.getMethod();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			TestMq.countDown();
		}
		return null;
	}

	@Override
	protected void handleAsyncMessage(Request request, String queue) {
		try {
			// Thread.currentThread();
			// Thread.sleep(2 * 1000);
			String msg = queue + "<<<<<异步handlerRequest..10秒内已处理...." + request.getMethod();
			System.out.println(msg);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			TestMq.countDown();
		}
	}

}
