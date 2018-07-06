package com.suidifu.mq.test.config.messagehandler;

import org.springframework.stereotype.Component;

import com.suidifu.mq.consumer.messagehandler.SpringMessageHandler;
import com.suidifu.mq.rpc.request.Request;
import com.suidifu.mq.rpc.response.Response;

@Component
public class RequestMessageHandler extends SpringMessageHandler<Response> {

	@Override
	protected void handleAsyncMessage(Request request, String queue, Object invokerResult) {
		System.out.println(queue + ">>>异步处理handleAsyncMessage..10秒内已处理...." + request.getBusinessId());
	}

	@Override
	protected void onHandleAsyncException(Request request, String queue, Exception e) {

	}

	@Override
	protected Response handleSyncMessage(Request request, String queue, Object invokerResult) {
		try {
			Thread.currentThread();
			Thread.sleep(10 * 1000);
			System.out.println(queue + ">>>>同步处理handleSyncMessage..10秒内已处理...." + request.getBusinessId());
		} catch (Exception e) {
			e.printStackTrace();
		}
		Response response = new Response();
		response.setResult("handlerRequest..10秒内已处理");
		return response;
	}

	@Override
	protected void onHandleSyncException(Request request, String queue, Exception e) {

	}

}
