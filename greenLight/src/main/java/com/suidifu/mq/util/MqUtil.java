package com.suidifu.mq.util;

import java.util.Hashtable;
import java.util.Map;

import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.AbstractMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;

import com.suidifu.mq.MqMessageListenerAdapter;
import com.suidifu.mq.config.RabbitMqQueueConfig;
import com.suidifu.mq.consumer.ConsumerErrorHandler;
import com.suidifu.mq.consumer.messagehandler.MessageHandler;

public class MqUtil {
	/**
	 * Direct Exchange,消息传递时需要一个“RouteKey”,可以简单的理解为要发送到的队列名字
	 * 
	 * @param rabbitAdmin
	 * @param queueConfig
	 */
	public static void directExchange(RabbitAdmin rabbitAdmin, RabbitMqQueueConfig queueConfig) {
		Queue requestQueue = new Queue(queueConfig.getRequestQueueName());
		rabbitAdmin.declareQueue(requestQueue);
	}

	/**
	 * Fanout Exchange,这种模式不需要RouteKey,需要提前将Exchange与Queue进行绑定，
	 * 一个Exchange可以绑定多个Queue， 一个Queue可以同多个Exchange进行绑定
	 * 
	 * @param rabbitAdmin
	 * @param queueConfig
	 */
	public static void fanoutExchange(RabbitAdmin rabbitAdmin, RabbitMqQueueConfig queueConfig) {
		fanoutExchange(rabbitAdmin, queueConfig, new FanoutExchange(queueConfig.getExchange()));
	}

	public static void fanoutExchange(RabbitAdmin rabbitAdmin, RabbitMqQueueConfig queueConfig, FanoutExchange fanoutExchange) {
		Queue requestQueue = priorityQueue(queueConfig.getRequestQueueName());
		rabbitAdmin.declareQueue(requestQueue);
		rabbitAdmin.declareExchange(fanoutExchange);
		rabbitAdmin.declareBinding(bindFanout(requestQueue, fanoutExchange));
	}

	public static Binding bindFanout(Queue requestQueue, FanoutExchange fanoutExchange) {
		return BindingBuilder.bind(requestQueue).to(fanoutExchange);
	}

	/**
	 * Topic Exchange模式需要RouteKey，也需要提前绑定Exchange与Queue;
	 * 对routingKey进行模糊匹配，比如ab*可以传递到所有ab*的queue “#”表示0个或若干个关键字，“*”表示一个关键字
	 * 如“log.*”能与“log.warn”匹配，无法与“log.warn.timeout”匹配；但是“log.#”能与上述两者匹配
	 * 
	 * @param rabbitAdmin
	 * @param queueConfig
	 * @return
	 */
	public static void topicExchange(RabbitAdmin rabbitAdmin, RabbitMqQueueConfig queueConfig) {
		topicExchange(rabbitAdmin, queueConfig, new TopicExchange(queueConfig.getExchange()));
	}

	public static void topicExchange(RabbitAdmin rabbitAdmin, RabbitMqQueueConfig queueConfig, TopicExchange topicExchange) {
		Queue requestQueue = priorityQueue(queueConfig.getRequestQueueName());
		rabbitAdmin.declareQueue(requestQueue);
		rabbitAdmin.declareExchange(topicExchange);
		for (String routingKey : queueConfig.getRoutingKeyList())
			rabbitAdmin.declareBinding(bindTopic(requestQueue, topicExchange, routingKey));
	}

	public static Binding bindTopic(Queue requestQueue, TopicExchange topicExchange, String routingKey) {
		return BindingBuilder.bind(requestQueue).to(topicExchange).with(routingKey);
	}

	private static Queue priorityQueue(String queueName) {
		Map<String, Object> arguments = new Hashtable<String, Object>();
		arguments.put("x-max-priority", 10);
		return new Queue(queueName, true, false, false, arguments);
	}

	/**
	 * SimpleConsumerMessageListenerContainer
	 * 
	 * @param connectionFactory
	 * @param messageHandler
	 * @param queueConfig
	 * @return
	 */
	public static AbstractMessageListenerContainer getSimpleConsumerMessageListenerContainer(ConnectionFactory connectionFactory, MessageHandler messageHandler, RabbitMqQueueConfig queueConfig) {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.setQueueNames(queueConfig.getRequestQueueName());
		container.setConcurrentConsumers(queueConfig.getConcurrentConsumers());
		container.setMessageListener(new MqMessageListenerAdapter(messageHandler));
		container.setAcknowledgeMode(AcknowledgeMode.AUTO);
		container.setChannelTransacted(true);
		container.setReceiveTimeout(queueConfig.getReceiveTimeout());
		container.setErrorHandler(new ConsumerErrorHandler());
		container.setPrefetchCount(1);
		return container;
	}

	/**
	 * SimpleProducerMessageListenerContainer
	 * 
	 * @param connectionFactory
	 * @param rabbitTemplate
	 * @param queueConfig
	 * @return
	 */
	@Deprecated
	public static AbstractMessageListenerContainer getSimpleProducerMessageListenerContainer(ConnectionFactory connectionFactory, RabbitTemplate rabbitTemplate, RabbitMqQueueConfig queueConfig) {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		// container.setQueueNames(queueConfig.getReplyQueueName());
		container.setMessageListener(rabbitTemplate);
		container.setReceiveTimeout(queueConfig.getReceiveTimeout());
		return container;
	}

	/**
	 * SimpleProducerRabbitTemplate
	 * 
	 * @param connectionFactory
	 * @param queueConfig
	 * @return
	 */
	public static RabbitTemplate getSimpleProducerRabbitTemplate(ConnectionFactory connectionFactory, RabbitMqQueueConfig queueConfig) {
		RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
		rabbitTemplate.setExchange(queueConfig.getExchange());
		rabbitTemplate.setQueue(queueConfig.getRequestQueueName());
		rabbitTemplate.setReplyTimeout(queueConfig.getReplyTimeout());
		rabbitTemplate.setReceiveTimeout(queueConfig.getReceiveTimeout());
		return rabbitTemplate;
	}
}
