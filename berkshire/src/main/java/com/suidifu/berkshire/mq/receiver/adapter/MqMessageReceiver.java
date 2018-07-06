package com.suidifu.berkshire.mq.receiver.adapter;

import com.demo2do.core.utils.JsonUtils;
import com.suidifu.berkshire.mq.parameter.MessageReceiverParameter;
import com.suidifu.hathaway.exceptions.ErrorRpcException;
import com.suidifu.hathaway.job.Task;
import com.suidifu.hathaway.task.handler.StageTaskHandler;
import com.suidifu.hathaway.util.AopTargetUtils;
import com.suidifu.hathaway.util.ReflectionUtils;
import com.suidifu.hathaway.util.SpringContextUtil;
import com.suidifu.mq.consumer.messagehandler.AbstractMessageHandler;
import com.suidifu.mq.rpc.request.Request;
import com.suidifu.mq.rpc.response.Response;
import com.zufangbao.gluon.exception.GlobalRuntimeException;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wukai
 */
@Component
public class MqMessageReceiver extends AbstractMessageHandler<Response> {

    private static Log logger = LogFactory.getLog(MqMessageReceiver.class);

    @Resource(name = "taskHandlerNoTransaction")
    private StageTaskHandler taskHandler;
    private List<String> jobDrivenBusinessBeanAndMethodList = new ArrayList<>();

    @PostConstruct
    public void init() {
        logger.info("MqServerAdapter initialized");
        jobDrivenBusinessBeanAndMethodList.add("dstJobSourceDocumentReconciliationNewSyncProxy_criticalMarker");
        jobDrivenBusinessBeanAndMethodList.add("dstJobSourceDocumentReconciliationNewSyncProxy_validateSourceDocumentDetailList");
        jobDrivenBusinessBeanAndMethodList.add("dstJobSourceDocumentReconciliationNewSyncProxy_fetchVirtualAccountAndBusinessPaymentVoucherTransfer");
        jobDrivenBusinessBeanAndMethodList.add("dstJobSourceDocumentReconciliationNewSyncProxy_sourceDocumentRecoverDetails");
        jobDrivenBusinessBeanAndMethodList.add("dstJobSourceDocumentReconciliationNewSyncProxy_unfreezeCapital");

        jobDrivenBusinessBeanAndMethodList.add("dstJobAssetValuation_processing_failed_prepayment_application");
        jobDrivenBusinessBeanAndMethodList.add("dstJobAssetValuation_valuate_repayment_plan_and_system_create_order");

        //还款订单
        jobDrivenBusinessBeanAndMethodList.add("dstJobRepaymentOrderPlacing_criticalMarker");
        jobDrivenBusinessBeanAndMethodList.add("dstJobRepaymentOrderPlacing_check_and_save");
        jobDrivenBusinessBeanAndMethodList.add("dstJobRepaymentOrderPlacing_roll_back");

        //还款订单撤销
        jobDrivenBusinessBeanAndMethodList.add("dstJobRepaymentOrderCancel_criticalMarker");
        jobDrivenBusinessBeanAndMethodList.add("dstJobRepaymentOrderCancel_delete_repayment_order_item");

        ///还款订单主动付款
//		jobDrivenBusinessBeanAndMethodList.add("dstJobRepaymentOrderActiveVoucherReconciliation_criticalMarker");
        jobDrivenBusinessBeanAndMethodList.add("repaymentOrderActiveVoucherReconciliationProxy_repaymentOrderRecoverDetails");

        ///还款订单商户
        jobDrivenBusinessBeanAndMethodList.add("dstJobRepaymentOrderReconciliation_criticalMarker");
        jobDrivenBusinessBeanAndMethodList.add("dstJobRepaymentOrderReconciliation_fetch_virtual_account_and_business_payment_voucher_transfer");
        jobDrivenBusinessBeanAndMethodList.add("dstJobRepaymentOrderReconciliation_repayment_order_recover_details");
        jobDrivenBusinessBeanAndMethodList.add("dstJobRepaymentOrderReconciliation_unfreeze_capical");

        jobDrivenBusinessBeanAndMethodList.add("clearDeductPlanHandlerNewProxy_criticalMarker");
        jobDrivenBusinessBeanAndMethodList.add("clearDeductPlanHandlerNewProxy_reconciliationClearingDeductPlan");

        //商户滞留单还款
        jobDrivenBusinessBeanAndMethodList.add("tmpDepositReconciliationSyncProxy_criticalMarker");
        jobDrivenBusinessBeanAndMethodList.add("tmpDepositReconciliationSyncProxy_validateDetailList");
        jobDrivenBusinessBeanAndMethodList.add("tmpDepositReconciliationSyncProxy_virtualAccountTransfer");
        jobDrivenBusinessBeanAndMethodList.add("tmpDepositReconciliationSyncProxy_recoverDetails");
        jobDrivenBusinessBeanAndMethodList.add("tmpDepositReconciliationSyncProxy_unfreezeCapital");

        //专户线下清算至还款户
        jobDrivenBusinessBeanAndMethodList.add("dstJobClearingAssetReconciliation_contract_clearing");
        jobDrivenBusinessBeanAndMethodList.add("dstJobClearingAssetReconciliation_update_financial_contract_account");

        //专户线上清算至还款户
        jobDrivenBusinessBeanAndMethodList.add("dstJobClearingVoucherAssetReconciliation_contract_clearing");
        jobDrivenBusinessBeanAndMethodList.add("dstJobClearingVoucherAssetReconciliation_update_financial_contract_account");

        //还款订单佰仟二期同步
        jobDrivenBusinessBeanAndMethodList.add("dstJobRepaymentOrderDataSyncDelayTask_repaymentOrderDelayTask");
        
        //还款订单变更
        jobDrivenBusinessBeanAndMethodList.add("dstJobRepaymentOrderModifyPlacing_criticalMarker");
        jobDrivenBusinessBeanAndMethodList.add("dstJobRepaymentOrderModifyPlacing_check_and_save");
        jobDrivenBusinessBeanAndMethodList.add("dstJobRepaymentOrderModifyPlacing_relate_payment_order_to_modify_order");
        jobDrivenBusinessBeanAndMethodList.add("dstJobRepaymentOrderModifyPlacing_cancel_original_order");
        jobDrivenBusinessBeanAndMethodList.add("dstJobRepaymentOrderModifyPlacing_roll_back");

        logger.info("need to write to redis job info[" + JsonUtils.toJsonString(jobDrivenBusinessBeanAndMethodList) + "]");
    }

    private boolean isJobDrivenBusinessBeanAndMethod(String beanName, String methodName) {
        return jobDrivenBusinessBeanAndMethodList.contains(beanName + "_" + methodName);
    }

    @Override
    public void handleAsyncMessage(Request mqRequest, String queue) {

        Object exceptionObject = null;

        Object resultObject = null;

        MessageReceiverParameter messageReceiverParameter = null;

        String requestUuid = null;

        Method targetMethod = null;

        Long threadId = Thread.currentThread().getId();

        try {
            long start_ = System.currentTimeMillis();
            requestUuid = mqRequest.getUuid();

            logger.info("threadId[" + threadId + "]#handleAsyncMessage# begin to extract parmeter with requestUuid[" + requestUuid + "] from queue[" + queue + "]");

            messageReceiverParameter = extractMessageReceiverParameter(mqRequest);

            targetMethod = messageReceiverParameter.getTargetMethod();

            String methodName = messageReceiverParameter.getMethodName();
            long end_ = System.currentTimeMillis();
            logger.info("threadId[" + threadId + "]#pre ,consume time [" + (end_ - start_) + "] ms ");

            logger.info("threadId[" + threadId + "]#handleAsyncMessage# begin to invoke method[" + methodName + "] with requestUuid[" + requestUuid + "] from queue[" + queue + "]");

            long start = System.currentTimeMillis();

            resultObject = targetMethod.invoke(messageReceiverParameter.getProxyBean(), messageReceiverParameter.getArgs());

            logger.info("threadId[" + threadId + "]#handleAsyncMessage# end to invoke method[" + methodName + "],consume time [" + (System.currentTimeMillis() - start) + "] ms ");

        } catch (Exception rawException) {

            rawException.printStackTrace();

            logger.error("threadId[" + threadId + "]#handleAsyncMessage# invoke method occur error with rawException with requestUuid[" + requestUuid + "],stack trace [" + ExceptionUtils.getFullStackTrace(rawException) + "]");

            Throwable e = exceptionFilter(rawException instanceof InvocationTargetException ? ((InvocationTargetException) rawException).getTargetException() : rawException, targetMethod);

            logger.error("threadId[" + threadId + "]#handleAsyncMessage# invoke method occur error with filter exception with requestUuid[" + requestUuid + "],stack trace [" + ExceptionUtils.getFullStackTrace(e) + "]");

            exceptionObject = e;

        } finally {

            if (isJobDrivenBusinessBeanAndMethod(messageReceiverParameter.getBeanName(), messageReceiverParameter.getMethodName())) {

                Task taskInRedis = taskHandler.getOneTaskBy(requestUuid);

                if (taskInRedis == null) {

                    logger.error("#MqResponseFactory# write back to redis occur error, taskInRedis is null  with request uuid[" + requestUuid + "] from queue[" + queue + "]");

                }
                taskHandler.updateTask(taskInRedis, exceptionObject, resultObject);
            }
        }
    }

    private MessageReceiverParameter extractMessageReceiverParameter(Request mqRequest) throws Exception {

        String beanName = mqRequest.getBean();

        Object proxyBean = SpringContextUtil.getBean(beanName);

        Object targetBean = AopTargetUtils.getTargetBean(proxyBean);

        String methodName = mqRequest.getMethod();

        Method targetMethod = ReflectionUtils.findOnlyPublicAccessMethod(targetBean.getClass(), methodName);

        Type[] types = targetMethod.getGenericParameterTypes();

        Object[] args = mqRequest.getParams();

        return new MessageReceiverParameter(targetMethod, targetBean, proxyBean, methodName, beanName, types, args);

    }

    private Throwable exceptionFilter(Throwable exception, Method method) {

        try {

            // 是JDK自带的异常，直接抛出
            String className = exception.getClass().getName();

            if (className.startsWith("java.") || className.startsWith("javax.")) {

                return exception;
            }
            
            Throwable exThrowable = new ErrorRpcException(className);

            if (null == method) {
                return exThrowable;
            }

            Class<?>[] exceptionClassses = method.getExceptionTypes();

            for (Class<?> exceptionClass : exceptionClassses) {

                if (exception.getClass().equals(exceptionClass)) {

                    return exception;
                }
            }
            return exThrowable;

        } catch (Exception e) {

            logger.error("#MqServerAdapter# exceptionFilter occur error with filter exception with stack trace [" + ExceptionUtils.getFullStackTrace(e) + "]");

            return new ErrorRpcException(e.getClass().getName());
        }

    }

    @Override
    protected Response handleSyncMessage(Request mqRequest, String queue) {

        Throwable exceptionObject = null;

        Object resultObject = null;

        MessageReceiverParameter messageReceiverParameter = null;

        String requestUuid = null;

        Method targetMethod = null;

        try {

            requestUuid = mqRequest.getUuid();

            logger.info("#handleSyncMessage# begin to extract parmeter with requestUuid[" + requestUuid + "] from queue[" + queue + "]");

            messageReceiverParameter = extractMessageReceiverParameter(mqRequest);

            targetMethod = messageReceiverParameter.getTargetMethod();

            String methodName = messageReceiverParameter.getMethodName();

            logger.info("#handleSyncMessage# begin to invoke method[" + methodName + "] with requestUuid[" + requestUuid + "] from queue[" + queue + "]");

            long start = System.currentTimeMillis();

            resultObject = targetMethod.invoke(messageReceiverParameter.getProxyBean(), messageReceiverParameter.getArgs());

            logger.info("#handleSyncMessage# end to invoke method[" + methodName + "] with requestUuid[" + requestUuid + "] from queue[" + queue + "],consume time [" + (System.currentTimeMillis() - start) + "] ms ");

        } catch (Exception rawException) {

            rawException.printStackTrace();

            logger.error("#handleSyncMessage# invoke method occur error with rawException with requestUuid[" + requestUuid + "],from queue[" + queue + "],stack trace [" + ExceptionUtils.getFullStackTrace(rawException) + "]");

            Throwable e = exceptionFilter(rawException instanceof InvocationTargetException ? ((InvocationTargetException) rawException).getTargetException() : rawException, targetMethod);

            logger.error("#handleSyncMessage# invoke method occur er.sror with filter exception with requestUuid[" + requestUuid + "],from queue[" + queue + "],stack trace [" + ExceptionUtils.getFullStackTrace(e) + "]");

            exceptionObject = e;
        }
        Response response = new Response();

        String exceptionCode = StringUtils.EMPTY;

        String exceptionMsg = StringUtils.EMPTY;

        if (exceptionObject != null && exceptionObject instanceof GlobalRuntimeException) {

            logger.info("this is global runtime exception");

            GlobalRuntimeException gRuntimeException = (GlobalRuntimeException) exceptionObject;

            exceptionCode = "" + gRuntimeException.getCode();

            exceptionMsg = gRuntimeException.getMsg();
        }

        response.setError(exceptionObject, exceptionCode, exceptionMsg);
        response.setResult(resultObject);

        return response;

    }
}