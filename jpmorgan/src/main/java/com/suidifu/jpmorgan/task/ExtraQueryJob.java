package com.suidifu.jpmorgan.task;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.suidifu.coffer.entity.QueryCreditResult;
import com.suidifu.jpmorgan.entity.PaymentOrder;
import com.suidifu.jpmorgan.entity.PaymentOrderWorkerConfig;
import com.suidifu.jpmorgan.entity.PaymentWorkerContext;
import com.suidifu.jpmorgan.entity.WorkingContext;
import com.suidifu.jpmorgan.exception.PaymentWorkerException;
import com.suidifu.jpmorgan.handler.JobHandler;
import com.suidifu.jpmorgan.handler.PaymentHandler;
import com.suidifu.jpmorgan.handler.PaymentWorkerContextHanlder;
import com.suidifu.jpmorgan.service.PaymentOrderService;
import com.suidifu.jpmorgan.spec.PaymentTaskSpec;
import com.suidifu.jpmorgan.spec.logspec.GloableLogSpec;
import com.suidifu.jpmorgan.spec.logspec.GloableLogSpec.JPMORGAN_FUNCTION_POINT;

@Component("extraQueryJob")
public class ExtraQueryJob implements JobHandler {

	private static Log logger = LogFactory.getLog(ExtraQueryJob.class);
	public static final long MINUTE_IN_MILLISECOND = 60 * 1000;

	@Autowired
	private PaymentWorkerContextHanlder paymentWorkerContextHanlder;
	@Autowired
	private PaymentOrderService paymentOrderService;

	@Override
	public void run(Map<String, Object> workingParms) {
		logger.info("start extraQueryJob job...");
		try {
			String workerUuid = workingParms.getOrDefault("workerUuid", StringUtils.EMPTY).toString();
			Integer workerNo = (Integer) workingParms.getOrDefault("workerNo", 1);
			PaymentWorkerContext workercontext = paymentWorkerContextHanlder.buildWorkerContext(workerUuid);

			String queueTableName = workercontext.getQueueTableName();
			PaymentHandler paymentHandler = workercontext.getWorkingHanlder();
			PaymentOrderWorkerConfig workerConfigure = workercontext.getWorkerConfig();
			
			WorkingContext context = new WorkingContext(workerUuid,-1);
			context.appendWorkingParameters(workerConfigure.getLocalWorkingConfig());
			
			List<PaymentOrder> readyTasks = new ArrayList<PaymentOrder>();
			readyTasks = paymentOrderService.getReadyTasksForUpdateBusinessStatus(queueTableName, workerNo);
			if(CollectionUtils.isEmpty(readyTasks)) {
				return;
			}
			
			long timeoutMinutes = Long.parseLong(workingParms.getOrDefault("extraQueryDelayMinutes", 0l).toString());
			if(timeoutMinutes == 0l) {
				return;
			}
			
			for(PaymentOrder readyTask : readyTasks) {
				String oppositeKeyWord = "";
				try {
					oppositeKeyWord="[trdVoucherUuid=" + readyTask.getUuid() + "]";
					
					long intervalMinute = compareTwoDatesOnMinute(readyTask.getCommunicationStartTime(), new Date());
					if(intervalMinute < timeoutMinutes) {
						continue;
					}
					
					QueryCreditResult queryCreditResult = paymentHandler.executeQueryPaymentStatus(readyTask, context);
					
					logger.info(GloableLogSpec.AuditLogHeaderSpec() + JPMORGAN_FUNCTION_POINT.EXTRAQUERY_RECV_QUERY_PAYMENTORDER_FROM_BANK + oppositeKeyWord + "commStatus=" + queryCreditResult.getCommCode() + "requestStatus=" + queryCreditResult.getRequestStatus() + "businessStatus=" + queryCreditResult.getProcessStatus());

					if(null == queryCreditResult || queryCreditResult.commFailed()) {
						continue;
					}
					
					if(queryCreditResult.isBusinessSuccess() || queryCreditResult.isBusinessFailed()) {
						boolean updateSuccess = paymentOrderService.updateBusinessStatus(readyTask, queryCreditResult, PaymentTaskSpec.UPDATE_TRY_TIMES, queueTableName);
						if(updateSuccess) {
							paymentOrderService.transferToPaymentOrderLog(readyTask, queueTableName, workercontext.getLogTableName());
						}
					}
					
				} catch (Exception e) {
					e.printStackTrace();
					logger.info(GloableLogSpec.AuditLogHeaderSpec() + JPMORGAN_FUNCTION_POINT.EXTRAQUERY_RECV_QUERY_PAYMENTORDER_FROM_BANK + oppositeKeyWord + "ERR:" + e.getMessage());
				}

			}
			
		} catch (PaymentWorkerException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private long compareTwoDatesOnMinute(Date startDate, Date endDate) {
		if(null == endDate || null == startDate) {
			return 0;
		}
		
		if(startDate.getTime() == endDate.getTime()) {
			return 0;
		}
		
		long l1 = startDate.getTime();
		long l2 = endDate.getTime();
		return (long)((l2 - l1) / MINUTE_IN_MILLISECOND);
	}
}
