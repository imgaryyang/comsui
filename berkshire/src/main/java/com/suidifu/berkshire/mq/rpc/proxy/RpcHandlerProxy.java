/**
 *
 */
package com.suidifu.berkshire.mq.rpc.proxy;

import com.demo2do.core.utils.DateUtils;
import com.suidifu.berkshire.handler.DictionaryHandler;
import com.suidifu.berkshire.mq.rpc.RpcClientUtils;
import com.suidifu.berkshire.mq.rpc.utils.RequestHelper;
import com.suidifu.swift.notifyserver.notifyserver.mq.config.RabbitMqConnectionConfig;
import com.suidifu.swift.notifyserver.notifyserver.mq.util.MqUtil;
import com.suidifu.watchman.message.amqp.request.AmqpRequest;
import com.suidifu.watchman.message.amqp.response.AmqpResponse;
import com.suidifu.watchman.message.core.request.InvokeType;
import com.zufangbao.gluon.spec.morganstanley.CommonSpec;
import com.zufangbao.sun.utils.uuid.UUIDUtil;
import com.zufangbao.sun.yunxin.entity.Dictionary;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeoutException;
import javax.annotation.Resource;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.amqp.AmqpConnectException;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

/**
 * @author wukai 将消息代理后置的微服务
 */
@Aspect
@Log4j2
public class RpcHandlerProxy {

  private RabbitMqConnectionConfig rabbitMqConnectionConfig;

  @Resource(name = "cacheableDictionaryHandler")
  private DictionaryHandler dictionaryHandler;

  private static final Log yali_logger = LogFactory.getLog("yali");


    public RpcHandlerProxy(RabbitMqConnectionConfig rabbitMqConnectionConfig) {
    this.rabbitMqConnectionConfig = rabbitMqConnectionConfig;
  }

  @Around("@annotation(com.suidifu.hathaway.mq.annotations.v2.MicroService)")
  public Object handleRpc(ProceedingJoinPoint joinPoint) throws Throwable {
    long start = 0, end;
    String requestNo = StringUtils.EMPTY;

    if (log.isDebugEnabled()) {
      start = System.currentTimeMillis();
      requestNo = UUIDUtil.random32UUID();
      log.info("MicroService begin to handleRpc, requestNo:{}", requestNo);
    }

    AmqpRequest request = RequestHelper.buildReqeust(joinPoint);

    log.info("begin to handleRpc with request beanName:[{}] methodName:[{}]", request.getBean(),request.getMethod());
    String log_uuid = UUID.randomUUID().toString();
    yali_logger.debug(String.format(
          CommonSpec.log_temp, request.getMethod()+"_start", DateUtils.format(new Date(),"yyyy-MM-dd HH:mm:ss"),System.currentTimeMillis(),log_uuid));

    if (log.isDebugEnabled()) {
      end = System.currentTimeMillis();
      log.debug("handleRpc#buildReqeust, usage:{}, currentTimeMillis:{}, requestNo:{}", (end - start), end, requestNo);
      start = end;
    }

    RabbitTemplate rabbitTemplate = buildRabbitMqTemplate(request);

    if (log.isDebugEnabled()) {
      end = System.currentTimeMillis();
      log.debug("handleRpc#buildRabbitMqTemplate, usage:{}, currentTimeMillis:{}, requestNo:{}", (end - start), end, requestNo);
      start = end;
    }

    String beanName = request.getBean();
    String methodName = request.getMethod();
    boolean directExecute = isDirectExecute(beanName, methodName);

    if (log.isDebugEnabled()) {
      end = System.currentTimeMillis();
      log.debug("handleRpc#isDirectExecute, usage:{}, currentTimeMillis:{}, requestNo:{}", (end - start), end, requestNo);
      start = end;
    }

    if (directExecute) {

      log.info("bean[{}],method[{}] direct execute", beanName, methodName);

      return joinPoint.proceed();
    }

    Object businessIdObj = request.getAppendixValue("businessId");

    if (null != businessIdObj) {

      String routingKeySuffix = getRoutingKeySuffix(businessIdObj.toString());

      if (StringUtils.isNotBlank(routingKeySuffix)) {

        request.setRoutingKey(request.getRoutingKey() + "-" + routingKeySuffix);
      }
    }
    try {

      AmqpResponse result = RpcClientUtils.sendSyncMessage(request, rabbitTemplate);

      log.info("end to handleRpc with request beanName:[{}] methodName:[{}]",request.getBean(),request.getMethod());
        yali_logger.debug(String.format(
            CommonSpec.log_temp, request.getMethod()+"_end", DateUtils.format(new Date(),"yyyy-MM-dd HH:mm:ss"),System.currentTimeMillis(),log_uuid));

      if (log.isDebugEnabled()) {
        end = System.currentTimeMillis();
        log.debug("handleRpc#sendSyncMessage, usage:{}, currentTimeMillis:{}, requestNo:{}", (end - start), end, requestNo);
      }

      if (result !=null && request.getInvokeType() == InvokeType.Sync) {

        return result.getResult();
      }
      return null;

    } catch (TimeoutException e) {

      e.printStackTrace();

      log.error("#RpcHandlerProxy#handleRpc bean[{}],method[{}] with TimeoutException[{}]", beanName, methodName,
          e.getMessage());

      throw new AmqpConnectException(e);

    } catch (Exception e) {

      log.error("#RpcHandlerProxy#handleRpc bean[{}],method[{}] with exception[{}]", beanName, methodName,
          e.getMessage());

      e.printStackTrace();

      throw e;
    }
  }

  private RabbitTemplate buildRabbitMqTemplate(AmqpRequest request) {

    CachingConnectionFactory rabbitMqConnnectionFactory = MqUtil
        .getDefaultCachingConnectionFactory(rabbitMqConnectionConfig, request.getVhost());

    RabbitTemplate rabbitTemplate = MqUtil
        .getSimpleProducerRabbitTemplate(rabbitMqConnnectionFactory, request.getExchange());

    rabbitTemplate.setReplyTimeout(rabbitMqConnectionConfig.getReplyTimeout());
    rabbitTemplate.setReceiveTimeout(rabbitMqConnectionConfig.getReceiveTimeout());

    return rabbitTemplate;
  }

  public RabbitMqConnectionConfig getRabbitMqConnectionConfig() {
    return rabbitMqConnectionConfig;
  }

  public boolean isDirectExecute(String beanName, String methodName) {
    Dictionary dictionary = dictionaryHandler.getValueByKey(beanName + "#" + methodName);
    return null == dictionary ? false : Boolean.valueOf(dictionary.getContent());
  }

  private String getRoutingKeySuffix(String businessId) {
    Dictionary dictionary = dictionaryHandler.getValueByKey(businessId);
    return null == dictionary ? null : dictionary.getContent();
  }
}
