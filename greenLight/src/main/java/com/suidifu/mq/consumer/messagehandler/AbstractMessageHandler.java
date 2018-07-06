package com.suidifu.mq.consumer.messagehandler;

import com.suidifu.mq.rpc.CodecType;
import com.suidifu.mq.rpc.RpcCodec;
import com.suidifu.mq.rpc.request.Request;

/**
 * 处理消息抽象类
 * 
 * @author lisf
 *
 * @param <T>
 */
public abstract class AbstractMessageHandler<T> implements MessageHandler {

	@Override
	public String handleMessage(String message, String queue, boolean sync, int codecOrdinal) throws Exception {
		RpcCodec codec = CodecType.getCodec(codecOrdinal);
		Request request = codec.decodeRequest(message);
		if (!sync) {
			this.handleAsyncMessage(request, queue);
			return null;
		}
		return codec.encodeObject(handleSyncMessage(request, queue));
	}

	/**
	 * 处理同步消息
	 * 
	 * @param request
	 * @return
	 */
	protected abstract T handleSyncMessage(Request request, String queue);

	/**
	 * 处理异步消息
	 * 
	 * @param request
	 */
	protected abstract void handleAsyncMessage(Request request, String queue);

}
