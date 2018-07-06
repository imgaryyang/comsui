package com.suidifu.mq.consumer.messagehandler;

public interface MessageHandler {
	public String handleMessage(String message, String queue, boolean sync, int codecOrdinal) throws Exception;
}
