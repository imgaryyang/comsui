/**
 * 
 */
package com.suidifu.berkshire.mq.rpc;

import com.suidifu.mq.rpc.CodecType;
import com.suidifu.watchman.message.amqp.request.AmqpRequest;
import com.suidifu.watchman.message.amqp.response.AmqpResponse;
import com.zufangbao.gluon.exception.GlobalRuntimeException;
import com.zufangbao.gluon.util.Assert;
import java.lang.reflect.Constructor;
import java.util.concurrent.TimeoutException;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;

/**
 * @author wukai
 *
 */
@Log4j2
public class RpcClientUtils {

	public static void sendAsyncMessage(AmqpRequest request, RabbitTemplate rabbitTemplate) throws Exception {

		String exchange = request.getExchange();

		String routingKey = request.getRoutingKey();

		Assert.notBlank(exchange, "exchange name must not be empty");

		Assert.notBlank(routingKey, "routingKey must not be empty");

		rabbitTemplate.convertAndSend(exchange, routingKey,
				buildMessage(rabbitTemplate, request, false, CodecType.JSON));
	}

	private static Message buildMessage(RabbitTemplate rabbitTemplate, AmqpRequest request, boolean sync,
			CodecType codecType) throws Exception {

		String message = codecType.getCodec().encodeObject(request);
		
		Message msg = rabbitTemplate.getMessageConverter().toMessage(message, new MessageProperties());
		msg.getMessageProperties().setHeader("sync", sync);
		msg.getMessageProperties().setHeader("encoding", codecType.getOrdinal());
		msg.getMessageProperties().setPriority(request.getPriority());

		return msg;
	}

	public static AmqpResponse sendSyncMessage(AmqpRequest request, RabbitTemplate rabbitTemplate) throws Exception {

		String exchange = request.getExchange();

		String routingKey = request.getRoutingKey();

		Assert.notBlank(exchange, "exchange name must not be empty");

		Assert.notBlank(routingKey, "routingKey must not be empty");

		CodecType codecType = CodecType.JSON;

		Object respSerializeObj = rabbitTemplate.convertSendAndReceive(exchange, routingKey,
				buildMessage(rabbitTemplate, request, true, codecType));

		rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {

			@Override
			public void confirm(CorrelationData correlationData, boolean ack, String cause) {
				if(!ack) {
					log.error("#sendSyncMessage# not ack with cause["+cause+"]");
				}
			}
		});

		if (respSerializeObj == null)

			throw new TimeoutException("#sendSyncMessage# reply timeout：");

		if (respSerializeObj instanceof byte[]) {

			return null;
		}

        AmqpResponse response = codecType.getCodec().decodeObject(respSerializeObj.toString(),AmqpResponse.class);

        if (response != null && response.isExceptionFired()) {
            Class<?> exThrowableClazz = Class.forName(response.getExceptionType());
            Constructor<?> exConstructor = exThrowableClazz.getConstructor(String.class);
            Exception e = (Exception) exConstructor.newInstance(response.getStackTrace());
            if(e instanceof GlobalRuntimeException){

                GlobalRuntimeException grException = (GlobalRuntimeException)e;

                grException.setCode(Integer.parseInt(response.getCode()));
                grException.setMsg(response.getMessage());

                throw grException;
            }

            throw e;
        }

		return response;
	}

}
