package com.suidifu.mq.rpc;

import java.util.concurrent.TimeoutException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import com.suidifu.mq.rpc.request.Request;

/**
 * 负责生产者发送消息
 * 
 * @author lisf
 *
 */
public abstract class RpcClient {
	protected static Log LOG = LogFactory.getLog(RpcClient.class);
	private RpcCodec codec;
	private CodecType codecTyep;

	protected RpcClient(CodecType codecTyep) {
		this.codecTyep = codecTyep;
		this.codec = this.codecTyep.getCodec();
	}

	public abstract RabbitTemplate rabbitTemplate(String businessId);

	/**
	 * 异步调用
	 * 
	 * @param bean
	 * @param method
	 * @param paramTypes
	 * @param args
	 */
	public void sendAsync(String bean, String method, Class<?>[] paramTypes, Object... args) {
		this.sendAsync(new Request().bean(bean).method(method).paramTypes(paramTypes).params(args));
	}

	public void sendAsync(String bean, String method) {
		this.sendAsync(new Request().bean(bean).method(method));
	}

	public void sendAsync(Request request) {
		this.sendAsync(request, MessageProperties.DEFAULT_PRIORITY);
	}

	public void sendAsync(Request request, int priority) {
		this.sendAsyncMessage(request, priority);
	}

	/**
	 * 同步调用
	 * 
	 * @param bean
	 * @param method
	 * @param paramTypes
	 * @param args
	 */
	public <T> T sendSync(Class<T> clazz, String bean, String method, Class<?>[] paramTypes, Object... args) throws TimeoutException {
		return this.sendSync(clazz, new Request().bean(bean).method(method).paramTypes(paramTypes).params(args));
	}

	public <T> T sendSync(Class<T> clazz, String bean, String method) throws TimeoutException {
		return this.sendSync(clazz, new Request().bean(bean).method(method));
	}

	public <T> T sendSync(Class<T> clazz, Request request) throws TimeoutException {
		return sendSync(clazz, request, MessageProperties.DEFAULT_PRIORITY);
	}

	public <T> T sendSync(Class<T> clazz, Request request, int priority) throws TimeoutException {
		return this.sendSyncMessage(clazz, request, priority);
	}

	/**
	 * 发送同步消息
	 * 
	 * @param clazz
	 * @param request
	 * @param replyTimeout
	 * @param priority
	 * @return
	 */
	private <T> T sendSyncMessage(Class<T> clazz, Request request, int priority) throws TimeoutException {
		return sendSyncMessage(clazz, request.getBusinessId(), this.codec.encodeRequest(request), priority);
	}

	/**
	 * 发送异步消息
	 * 
	 * @param request
	 * @param priority
	 */
	private void sendAsyncMessage(Request request, int priority) {
		sendAsyncMessage(request.getBusinessId(), this.codec.encodeRequest(request), priority);
	}

	private void sendAsyncMessage(String businessId, String message, int priority) {
		RabbitTemplate rabbitTemplate = rabbitTemplate(businessId);
		convertAndSend(rabbitTemplate, buildMessage(rabbitTemplate, message, Boolean.FALSE, priority));
	}

	/**
	 * rpc结果为null时会返回byte[]空字节用于超时区分
	 * 
	 * @param clazz
	 * @param businessId
	 * @param message
	 * @param replyTimeout
	 * @param priority
	 * @return
	 * @throws TimeoutException
	 */
	private <T> T sendSyncMessage(Class<T> clazz, String businessId, Object message, int priority) throws TimeoutException {
		RabbitTemplate rabbitTemplate = rabbitTemplate(businessId);
		Object respSerializeObj = convertSendAndReceive(rabbitTemplate, buildMessage(rabbitTemplate, message, Boolean.TRUE, priority));
		if (respSerializeObj == null)
			throw new TimeoutException("reply timeout：" + businessId);
		if (respSerializeObj instanceof byte[])
			return null;
		return this.codec.decodeObject(respSerializeObj.toString(), clazz);
	}

	/**
	 * 封装非业务Message
	 * 
	 * @param rabbitTemplate
	 * @param message
	 * @param sync
	 * @param priority
	 * @return
	 */
	private Message buildMessage(RabbitTemplate rabbitTemplate, Object message, Boolean sync, int priority) {
		Message msg = rabbitTemplate.getMessageConverter().toMessage(message, new MessageProperties());
		msg.getMessageProperties().setHeader("sync", sync);// 是否同步
		msg.getMessageProperties().setHeader("codec", this.codecTyep.getOrdinal());// codec
		msg.getMessageProperties().setPriority(priority <= 0 ? MessageProperties.DEFAULT_PRIORITY : priority);
		return msg;
	}

	protected void convertAndSend(RabbitTemplate rabbitTemplate, Message buildMessage) {
		rabbitTemplate.convertAndSend(buildMessage);
	}

	protected Object convertSendAndReceive(RabbitTemplate rabbitTemplate, Message buildMessage) {
		return rabbitTemplate.convertSendAndReceive(buildMessage);
	}
}
