package com.suidifu.mq;

import org.springframework.amqp.rabbit.listener.AbstractMessageListenerContainer;

/**
 * 负责消息容器管理
 * 
 * @author lisf
 *
 */
public abstract class MessageManager {
	protected AbstractMessageListenerContainer container;

	public MessageManager(AbstractMessageListenerContainer container) {
		this.container = container;
	}

	public final void start() {
		beforeStart();
		this.container.start();
		afterStart();
	}

	public final void stop() {
		beforeStop();
		this.container.stop();
		afterStop();
	}

	public AbstractMessageListenerContainer getContainer() {
		return this.container;
	}

	public abstract void beforeStart();

	public abstract void afterStart();

	public abstract void beforeStop();

	public abstract void afterStop();
}
