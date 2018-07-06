package com.suidifu.munichre.test.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import com.suidifu.mq.consumer.messagehandler.AbstractMessageHandler;
import com.suidifu.mq.rpc.request.Request;

@Component("mqMunichreMessageHandler")
public class StringMessageHandler extends AbstractMessageHandler<String> {
	private static final Logger LOG = LoggerFactory.getLogger(StringMessageHandler.class);

	@Override
	protected String handleSyncMessage(Request request, String queue) {
		String msg = null;
		try {
			// Thread.currentThread();
			// Thread.sleep(5 * 1000);
			msg = queue + "<<<<<同步handlerRequest..10秒内已处理1...." + request.getMethod() + request.getBusinessId();
			// int i=1/0;
			LOG.info(msg);
			return request.getMethod();
		} catch (Exception e) {
			LOG.error("munichre.error", e);
		}
		return null;
	}

	@Override
	protected void handleAsyncMessage(Request request, String queue) {
		try {
			// Thread.currentThread();
			// Thread.sleep(2 * 1000);
			String msg = queue + "<<<<<异步handlerRequest..10秒内已处理...." + request;
			LOG.info(msg);
		} catch (Exception e) {
			LOG.error("munichre.error", e);
		}
	}

}
