package com.suidifu.mq.consumer.messagehandler;

import org.springframework.util.MethodInvoker;

import com.suidifu.mq.rpc.request.Request;
import com.suidifu.mq.util.SpringContextUtil;

/**
 * SpringMessageHandler
 * 
 * @author lisf
 *
 * @param <T>
 */
public abstract class SpringMessageHandler<T> extends AbstractMessageHandler<T> {

	protected MethodInvoker getMethodInvoker(Request request) throws ClassNotFoundException, NoSuchMethodException {
		Object targetObject = SpringContextUtil.getBean(request.getBean());
		MethodInvoker invoker = new MethodInvoker();
		invoker.setTargetObject(targetObject);
		invoker.setTargetMethod(request.getMethod());
		invoker.setArguments(request.getParams());
		invoker.prepare();
		return invoker;
	}

	@Override
	protected T handleSyncMessage(Request request, String queue) {
		try {
			return handleSyncMessage(request, queue, getMethodInvoker(request).invoke());
		} catch (Exception e) {
			onHandleSyncException(request, queue, e);
		}
		return null;
	}

	@Override
	protected void handleAsyncMessage(Request request, String queue) {
		try {
			handleAsyncMessage(request, queue, getMethodInvoker(request).invoke());
		} catch (Exception e) {
			onHandleAsyncException(request, queue, e);
		}
	}

	protected abstract void handleAsyncMessage(Request request, String queue, Object invokerResult);

	protected abstract void onHandleAsyncException(Request request, String queue, Exception e);

	protected abstract T handleSyncMessage(Request request, String queue, Object invokerResult);

	protected abstract void onHandleSyncException(Request request, String queue, Exception e);
}
