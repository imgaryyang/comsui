package com.suidifu.jpmorgan.task;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.suidifu.jpmorgan.constant.JudgeStatusWhileSendingPayRequestGateWaySet;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.suidifu.coffer.GlobalSpec;
import com.suidifu.coffer.TransactionErrCodeSpec;
import com.suidifu.coffer.entity.BusinessProcessStatus;
import com.suidifu.coffer.entity.CreditResult;
import com.suidifu.coffer.entity.QueryCreditResult;
import com.suidifu.jpmorgan.ConfigInitializer;
import com.suidifu.jpmorgan.entity.PaymentOrder;
import com.suidifu.jpmorgan.entity.PaymentOrderWorkerConfig;
import com.suidifu.jpmorgan.entity.PaymentWorkerContext;
import com.suidifu.jpmorgan.entity.WorkingContext;
import com.suidifu.jpmorgan.exception.PaymentWorkerException;
import com.suidifu.jpmorgan.factory.PaymentHandlerFactory;
import com.suidifu.jpmorgan.handler.JobHandler;
import com.suidifu.jpmorgan.handler.PaymentHandler;
import com.suidifu.jpmorgan.handler.PaymentOrderHandler;
import com.suidifu.jpmorgan.handler.PaymentWorkerContextHanlder;
import com.suidifu.jpmorgan.service.PaymentOrderService;
import com.suidifu.jpmorgan.spec.PaymentTaskSpec;
import com.suidifu.jpmorgan.spec.logspec.GloableLogSpec;
import com.suidifu.jpmorgan.spec.logspec.GloableLogSpec.JPMORGAN_FUNCTION_POINT;

@Component("paymentJob")
public class PaymentJob implements JobHandler {
	
	private static Log logger = LogFactory.getLog(PaymentJob.class);
	
	@Autowired
	private PaymentWorkerContextHanlder paymentWorkerContextHanlder;
	@Autowired
	private ConfigInitializer configInitializer;

	private PaymentOrderService paymentOrderService;
	@Autowired
	private PaymentOrderHandler paymentOrderHandler;


	@Override
	public void run(Map<String, Object> workingParms) {

		try {
			String workerUuid = workingParms.getOrDefault("workerUuid", StringUtils.EMPTY).toString();
			Integer workerNo = Integer.parseInt(workingParms.getOrDefault("workerNo", 1).toString());
			
			PaymentWorkerContext workerContext = paymentWorkerContextHanlder.buildWorkerContext(workerUuid);
			
			String queueTableName = workerContext.getQueueTableName();
			PaymentHandler hanlder = workerContext.getWorkingHanlder();
			PaymentOrderWorkerConfig workerConfigure = workerContext.getWorkerConfig();
		
			List<PaymentOrder> idleTaskList = new ArrayList<PaymentOrder>();
			
			this.paymentOrderService = configInitializer.getPaymentOrderService();
			
			idleTaskList = this.paymentOrderService.peekIdleTasks(queueTableName, PaymentTaskSpec.PEEK_IDLE_LIMIT, workerNo);
			
			if(CollectionUtils.isEmpty(idleTaskList)) {
				return;
			}
		
		
			for(PaymentOrder idleTask : idleTaskList) {
				String oppositeKeyWord="[trdVoucherUuid=" + idleTask.getUuid() + "]";
				try {

					int nthSlot = idleTask.nextReadySlot();
					
					if(0 == nthSlot) {
						continue;
					}
					
					boolean occupySuccess = this.paymentOrderService.atomOccupy(nthSlot, idleTask, PaymentTaskSpec.UPDATE_TRY_TIMES, workerUuid, queueTableName);
					
					if(!occupySuccess) {
						logger.info(workerUuid + PaymentTaskSpec.MSG_OCCUPY_FAIL + idleTask.getUuid());
						continue;
					}
					
					boolean sentOutUpdateSuccess = this.paymentOrderService.atomSentOut(nthSlot, idleTask, PaymentTaskSpec.UPDATE_TRY_TIMES, queueTableName);
					
					if(!sentOutUpdateSuccess) {
						logger.warn(workerUuid + PaymentTaskSpec.MSG_SENTOUT_FAIL + idleTask.getUuid());
						continue;
					}
					
					WorkingContext workingcontext = new WorkingContext(workerUuid, nthSlot);//TODO 检查核对 是否需要放在循里面
					
					workingcontext.setWorkingParameters(idleTask.getGatewayRouterInfoConfig());
					
					// Read static Config from worker config and append to working parameters;
					workingcontext.appendWorkingParameters(workerConfigure.getLocalWorkingConfig());
					
					// log
					logger.info(GloableLogSpec.AuditLogHeaderSpec() + JPMORGAN_FUNCTION_POINT.SEND_PAYMENTORDER_TO_BANK +oppositeKeyWord);
					
					CreditResult creditResult = hanlder.executeSinglePay(idleTask, workingcontext);
					
					logger.info(GloableLogSpec.AuditLogHeaderSpec() + JPMORGAN_FUNCTION_POINT.SEND_PAYMENTORDER_TO_BANK +oppositeKeyWord + "[SUCC]");

					this.paymentOrderService.atomFeedBack(nthSlot, idleTask, PaymentTaskSpec.UPDATE_TRY_TIMES, queueTableName);
					
					if(JudgeStatusWhileSendingPayRequestGateWaySet
							.judgeStatusWhileSendingPayRequestGateWaySet
							.contains(workerContext.getGatewayName())) {
						logger.info(GloableLogSpec.AuditLogHeaderSpec() + JPMORGAN_FUNCTION_POINT.SEND_PAYMENTORDER_TO_BANK +oppositeKeyWord + "[DIRECT RESULT]");
						QueryCreditResult queryCreditResult = new QueryCreditResult();
						
						if(BusinessProcessStatus.SUCCESS.equals(creditResult.getProcessStatus())) {
							queryCreditResult.setProcessStatus(BusinessProcessStatus.SUCCESS);
						}
						
						if(BusinessProcessStatus.FAIL.equals(creditResult.getProcessStatus())) {
							queryCreditResult.setProcessStatus(BusinessProcessStatus.FAIL);
						}
						
						String businessResultMsg = String.format(GlobalSpec.BUSINESS_RESULT_CODE_TEMPLATE, creditResult.getCommCode(), creditResult.getResponseMessage());
						queryCreditResult.setBusinessResultMsg(businessResultMsg);

						if(queryCreditResult.isBusinessSuccess() || queryCreditResult.isBusinessFailed()) {
							
							boolean updateSuccess = this.paymentOrderService.updateBusinessStatus(idleTask, queryCreditResult, PaymentTaskSpec.UPDATE_TRY_TIMES, queueTableName);
						
							if(updateSuccess) {
								this.paymentOrderService.transferToPaymentOrderLog(idleTask, queueTableName, workerContext.getLogTableName());
							}
						}
						
						continue;
					}
					
					logger.info(GloableLogSpec.AuditLogHeaderSpec() + JPMORGAN_FUNCTION_POINT.SEND_PAYMENTORDER_TO_BANK +oppositeKeyWord + "commCode:" + creditResult.getCommCode());

					if(TransactionErrCodeSpec.ERR_CODE_MAP.containsKey(creditResult.getCommCode())) {
						logger.info(GloableLogSpec.AuditLogHeaderSpec() + JPMORGAN_FUNCTION_POINT.SEND_PAYMENTORDER_TO_BANK +oppositeKeyWord + "[DIRECT FAIL]");
						QueryCreditResult queryCreditResult = new QueryCreditResult();
						queryCreditResult.setProcessStatus(BusinessProcessStatus.FAIL);
						
						String businessResultMsg = String.format(GlobalSpec.BUSINESS_RESULT_CODE_TEMPLATE, creditResult.getCommCode(), TransactionErrCodeSpec.ERR_CODE_MAP.getOrDefault(creditResult.getCommCode(), StringUtils.EMPTY));
						queryCreditResult.setBusinessResultMsg(businessResultMsg);
						boolean updateSuccess = this.paymentOrderService.updateBusinessStatus(idleTask, queryCreditResult, PaymentTaskSpec.UPDATE_TRY_TIMES, queueTableName);

						if(updateSuccess) {
							this.paymentOrderService.transferToPaymentOrderLog(idleTask, queueTableName, workerContext.getLogTableName());
						}
						//inner callback
						if(! StringUtils.isEmpty(idleTask.getStringFieldOne())) {
							paymentOrderHandler.handleInnerCallback(idleTask, workerContext.getLogTableName());
						}
					}
					
				} catch (Exception e) {
					logger.info(GloableLogSpec.AuditLogHeaderSpec() + JPMORGAN_FUNCTION_POINT.SEND_PAYMENTORDER_TO_BANK +oppositeKeyWord + "[ERR:"+ e.getMessage() +"]");

					e.printStackTrace();
				}
			}
			
			
		} catch (PaymentWorkerException e) {
			e.printStackTrace();
			return;
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}

}
