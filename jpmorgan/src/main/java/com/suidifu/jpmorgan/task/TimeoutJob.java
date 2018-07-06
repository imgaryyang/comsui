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

import com.suidifu.coffer.entity.BusinessRequestStatus;
import com.suidifu.coffer.entity.QueryCreditResult;
import com.suidifu.jpmorgan.ConfigInitializer;
import com.suidifu.jpmorgan.entity.PaymentOrder;
import com.suidifu.jpmorgan.entity.PaymentOrderWorkerConfig;
import com.suidifu.jpmorgan.entity.PaymentWorkerContext;
import com.suidifu.jpmorgan.entity.WorkingContext;
import com.suidifu.jpmorgan.exception.PaymentWorkerException;
import com.suidifu.jpmorgan.handler.JobHandler;
import com.suidifu.jpmorgan.handler.PaymentHandler;
import com.suidifu.jpmorgan.handler.PaymentWorkerContextHanlder;
import com.suidifu.jpmorgan.service.PaymentOrderService;
import com.suidifu.jpmorgan.spec.logspec.GloableLogSpec;
import com.suidifu.jpmorgan.spec.logspec.GloableLogSpec.JPMORGAN_FUNCTION_POINT;

@Component("timeoutJob")
public class TimeoutJob implements JobHandler {

	@Autowired
	private PaymentWorkerContextHanlder paymentWorkerContextHanlder;
	@Autowired
	private ConfigInitializer configInitializer;

	private PaymentOrderService paymentOrderService;
	
	public static final long MINUTE_IN_MILLISECOND = 60 * 1000;
	
	private static Log logger = LogFactory.getLog(TimeoutJob.class);
	
	@Override
	public void run(Map<String, Object> workingParms) {
		logger.info("start timeout job...");
		try {
			String workerUuid = workingParms.getOrDefault("workerUuid", StringUtils.EMPTY).toString();	
			Integer workerNo = (Integer) workingParms.getOrDefault("workerNo", 1);
			
			PaymentWorkerContext workercontext=paymentWorkerContextHanlder.buildWorkerContext(workerUuid);
			PaymentHandler paymentHandler=workercontext.getWorkingHanlder();
			String queueTableName = workercontext.getQueueTableName();
			
			PaymentOrderWorkerConfig workerConfigure = workercontext.getWorkerConfig();
			
			List<PaymentOrder> oppositeProcessingTasks = new ArrayList<PaymentOrder>();
			this.paymentOrderService = configInitializer.getPaymentOrderService();
			oppositeProcessingTasks = this.paymentOrderService.getReadyTasksForUpdateBusinessStatus(queueTableName, workerNo);//获取对端处理中的数据，因为目前只要发出去就算对端处理中

			if(CollectionUtils.isEmpty(oppositeProcessingTasks)) {
				return;
			}
			
			long timeoutMinutes = Long.parseLong(workingParms.getOrDefault("timeoutMinutes", 0l).toString());
			if(timeoutMinutes == 0l) {
				return;
			}
			for(PaymentOrder oppositeProcessingTask : oppositeProcessingTasks) {
				try {
					long intervalMinute = compareTwoDatesOnMinute(oppositeProcessingTask.getCommunicationStartTime(), new Date());
					
					if(intervalMinute < timeoutMinutes) {
						continue;
					}
					int nthSlot = oppositeProcessingTask.currentOccupiedSlot();
					WorkingContext workingContext = new WorkingContext(workerUuid, nthSlot);
					
					workingContext.setWorkingParameters(oppositeProcessingTask.getGatewayRouterInfoConfig());
					
					// Read static Config from worker config and append to working parameters;
					workingContext.appendWorkingParameters(workerConfigure.getLocalWorkingConfig());
					
					QueryCreditResult queryCreditResult = paymentHandler.executeQueryPaymentStatus(oppositeProcessingTask, workingContext);
					if(BusinessRequestStatus.NOTRECEIVE.equals(queryCreditResult.getRequestStatus())) {
						
						/*
						1、对第一次通信forcestop,系统自动使用slot2通信，然后businessstatus查询slot2
						2、直接发起打款 businessjob会进行查询
						3、初始化各状态,让paymentjob执行打款
						*/
						String oppositeKeyWord="[trdVoucherUuid=" + oppositeProcessingTask.getUuid() + "]";
						logger.info(GloableLogSpec.AuditLogHeaderSpec() + JPMORGAN_FUNCTION_POINT.TIMEOUT_SEND_PAYMENTORDER_TO_BANK + oppositeKeyWord);
						paymentHandler.executeSinglePay(oppositeProcessingTask, workingContext);
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		} catch (PaymentWorkerException e) {
			e.printStackTrace();
		}catch (Exception e) {
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
