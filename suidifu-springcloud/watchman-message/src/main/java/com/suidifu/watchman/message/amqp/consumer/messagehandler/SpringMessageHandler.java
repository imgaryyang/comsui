package com.suidifu.watchman.message.amqp.consumer.messagehandler;


import com.demo2do.core.utils.StringUtils;
import com.suidifu.watchman.message.amqp.request.AmqpRequest;
import com.suidifu.watchman.message.amqp.response.AmqpResponse;
import com.suidifu.watchman.message.core.request.InvokeType;
import com.suidifu.watchman.message.core.request.Request;
import com.suidifu.watchman.message.core.response.Response;
import com.suidifu.watchman.util.SpringContextUtil;
import com.zufangbao.gluon.exception.GlobalRuntimeException;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.MethodInvoker;

/**
 * SpringMessageHandler
 */
public class SpringMessageHandler extends AbstractMessageHandler<Response> {

    private static final Log LOGGER = LogFactory.getLog(SpringMessageHandler.class);

    protected MethodInvoker getMethodInvoker(AmqpRequest request, InvokeType syncType)
        throws ClassNotFoundException, NoSuchMethodException {
        Object targetObject = SpringContextUtil.getBean(request.getBean());
        MethodInvoker invoker = new MethodInvoker();
        invoker.setTargetObject(targetObject);
        invoker.setTargetMethod(request.getMethod());
        invoker.setArguments(request.getParams());
        invoker.prepare();
        return invoker;
    }

    @Override
    protected Response handleSyncMessage(Request request, String queue) {
        long start = System.currentTimeMillis();
        AmqpRequest amqpRequest = null;
        try {
            amqpRequest = (AmqpRequest) request;
            LOGGER.info("micro-service begin consumer message, bean name:[" + amqpRequest.getBean() + "], method name:["
                + amqpRequest.getMethod() + "], request uuid:[" + amqpRequest.getUuid() + "]");

            Object result = getMethodInvoker(amqpRequest, InvokeType.Sync).invoke();

            LOGGER.info("micro-service end consumer message, bean name:[" + amqpRequest.getBean() + "], method name:["
                + amqpRequest.getMethod() + "], request uuid:[" + amqpRequest.getUuid() + "]"
                + ", use:[" + (System.currentTimeMillis() - start) + "]ms");

            return new AmqpResponse(request, result);

        } catch (Exception e) {

            LOGGER.error("micro-service consumer message error, beanName:[" + amqpRequest.getBean() + "], methodName:["
                + amqpRequest.getMethod() + "], requestUuid:[" + amqpRequest.getUuid() + "]"
                + ", use:[" + (System.currentTimeMillis() - start) + "]ms"
                + ", error fullStackTrace:[" + ExceptionUtils.getFullStackTrace(e) + "]");

            String exceptionCode = StringUtils.EMPTY;

            String exceptionMsg = StringUtils.EMPTY;

            if (e instanceof GlobalRuntimeException) {

                LOGGER.info("this is global runtime exception");

                GlobalRuntimeException gRuntimeException = (GlobalRuntimeException) e;

                exceptionCode = "" + gRuntimeException.getCode();

                exceptionMsg = gRuntimeException.getMsg();


            }

            AmqpResponse response = new AmqpResponse(request, null);

            response.setError(e, exceptionCode, exceptionMsg);

            return response;
        }
    }

    @Override
    protected void handleAsyncMessage(Request request, String queue) {

        try {
            getMethodInvoker((AmqpRequest) request, InvokeType.Async).invoke();
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

}
