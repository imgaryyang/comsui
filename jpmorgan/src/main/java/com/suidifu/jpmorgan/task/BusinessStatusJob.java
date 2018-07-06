package com.suidifu.jpmorgan.task;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.suidifu.coffer.entity.QueryCreditResult;
import com.suidifu.jpmorgan.ConfigInitializer;
import com.suidifu.jpmorgan.entity.PaymentOrder;
import com.suidifu.jpmorgan.entity.PaymentOrderWorkerConfig;
import com.suidifu.jpmorgan.entity.PaymentWorkerContext;
import com.suidifu.jpmorgan.entity.WorkingContext;
import com.suidifu.jpmorgan.exception.PaymentWorkerException;
import com.suidifu.jpmorgan.handler.JobHandler;
import com.suidifu.jpmorgan.handler.PaymentHandler;
import com.suidifu.jpmorgan.handler.PaymentOrderHandler;
import com.suidifu.jpmorgan.handler.PaymentWorkerContextHanlder;
import com.suidifu.jpmorgan.service.PaymentOrderService;
import com.suidifu.jpmorgan.spec.PaymentTaskSpec;
import com.suidifu.jpmorgan.spec.logspec.GloableLogSpec;
import com.suidifu.jpmorgan.spec.logspec.GloableLogSpec.JPMORGAN_FUNCTION_POINT;

@Component("businessStatusJob")
public class BusinessStatusJob implements JobHandler {

	@Autowired
	private PaymentWorkerContextHanlder paymentWorkerContextHanlder;
	@Autowired
	private PaymentOrderHandler paymentOrderHandler;
	@Autowired
	private ConfigInitializer configInitializer;

	private PaymentOrderService paymentOrderService;
	
	private static Log logger = LogFactory.getLog(BusinessStatusJob.class);

	@Override
	public void run(Map<String, Object> workingParms) {
		//logger.info("businessStatusJob start ....");
		
		try {
			String workerUuid = workingParms.getOrDefault("workerUuid", StringUtils.EMPTY).toString();
			Integer workerNo = Integer.parseInt(workingParms.getOrDefault("workerNo", 1).toString());
			
			PaymentWorkerContext workercontext = paymentWorkerContextHanlder.buildWorkerContext(workerUuid);
			String queueTableName = workercontext.getQueueTableName();
			
			PaymentHandler paymentHandler = workercontext.getWorkingHanlder();
			
			PaymentOrderWorkerConfig workerConfigure = workercontext.getWorkerConfig();
	
			WorkingContext context = new WorkingContext(workerUuid,-1);
			context.appendWorkingParameters(workerConfigure.getLocalWorkingConfig());
			
			List<PaymentOrder> readyTasks = new ArrayList<PaymentOrder>();
			this.paymentOrderService = configInitializer.getPaymentOrderService();
			readyTasks = this.paymentOrderService.getReadyTasksForUpdateBusinessStatus(queueTableName, workerNo);
			
			//readyTasks.addAll(paymentOrderService.getSendingTimeOutTasks(PaymentTaskSpec.DEFAULT_SENDING_TIMEOUT_MINUTES, queueTableName));
			//System.out.println("business status peek time : " + (System.currentTimeMillis() - startTime));
			for(PaymentOrder readyTask : readyTasks) {
				String oppositeKeyWord = "";
				try {
					//Thread.sleep(200);
					
					oppositeKeyWord="[trdVoucherUuid=" + readyTask.getUuid() + "];";

					QueryCreditResult queryCreditResult = paymentHandler.executeQueryPaymentStatus(readyTask, context);
					
					logger.info(GloableLogSpec.AuditLogHeaderSpec() + JPMORGAN_FUNCTION_POINT.RECV_QUERY_PAYMENTORDER_FROM_BANK +oppositeKeyWord + "commStatus=" + queryCreditResult.getCommCode() + "requestStatus=" + queryCreditResult.getRequestStatus() + "businessStatus=" + queryCreditResult.getProcessStatus());
					
					if(null == queryCreditResult || queryCreditResult.commFailed()) {
						continue;
					}
					
					if(queryCreditResult.isBusinessSuccess() || queryCreditResult.isBusinessFailed()) {//TODO 重构 与paymentjob有部分重复代码
						boolean updateSuccess = this.paymentOrderService.updateBusinessStatus(readyTask, queryCreditResult, PaymentTaskSpec.UPDATE_TRY_TIMES, queueTableName);
						if(updateSuccess) {
							this.paymentOrderService.transferToPaymentOrderLog(readyTask, queueTableName, workercontext.getLogTableName());
						}
						
						//inner callback
						if(! StringUtils.isEmpty(readyTask.getStringFieldOne())) {
							paymentOrderHandler.handleInnerCallback(readyTask, workercontext.getLogTableName());
						}
					}
					
				} catch (Exception e) {
					e.printStackTrace();
					logger.info(GloableLogSpec.AuditLogHeaderSpec() + JPMORGAN_FUNCTION_POINT.RECV_QUERY_PAYMENTORDER_FROM_BANK +oppositeKeyWord + "ERR:" + e.getMessage());
				}
			}
			
		} catch (PaymentWorkerException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}

}
