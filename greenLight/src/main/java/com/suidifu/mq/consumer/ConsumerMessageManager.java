package com.suidifu.mq.consumer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.amqp.rabbit.listener.AbstractMessageListenerContainer;

import com.suidifu.mq.MessageManager;

public class ConsumerMessageManager extends MessageManager {
	private static Log LOG = LogFactory.getLog(ConsumerMessageManager.class);

	public ConsumerMessageManager(AbstractMessageListenerContainer container) {
		super(container);
	}

	@Override
	public void beforeStart() {

	}

	@Override
	public void afterStart() {
		for (String qname : this.container.getQueueNames())
			LOG.info("正常启动Consumer：" + qname);
	}

	@Override
	public void beforeStop() {

	}

	@Override
	public void afterStop() {

	}

}
