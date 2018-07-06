package com.suidifu.mq.producer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.amqp.rabbit.listener.AbstractMessageListenerContainer;

import com.suidifu.mq.MessageManager;

public class ProducerMessageManager extends MessageManager {
	private static Log LOG = LogFactory.getLog(ProducerMessageManager.class);

	public ProducerMessageManager(AbstractMessageListenerContainer container) {
		super(container);
	}

	@Override
	public void beforeStart() {

	}

	@Override
	public void afterStart() {
		for (String qname : this.container.getQueueNames())
			LOG.info("正常启动Producer：" + qname);
	}

	@Override
	public void beforeStop() {

	}

	@Override
	public void afterStop() {

	}

}
