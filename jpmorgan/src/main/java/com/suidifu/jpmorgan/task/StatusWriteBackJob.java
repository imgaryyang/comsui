package com.suidifu.jpmorgan.task;

import java.util.*;

import com.suidifu.jpmorgan.ConfigInitializer;
import com.suidifu.jpmorgan.entity.*;
import com.suidifu.swift.notifyserver.notifyserver.NotifyApplication;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.demo2do.core.entity.Result;
import com.demo2do.core.utils.JsonUtils;
import com.suidifu.jpmorgan.entity.CommunicationStatus;
import com.suidifu.jpmorgan.entity.GatewaySlot;
import com.suidifu.jpmorgan.entity.QueryStatusResult;
import com.suidifu.jpmorgan.entity.TradeSchedule;
import com.suidifu.jpmorgan.handler.JobHandler;
import com.suidifu.jpmorgan.handler.TradeScheduleHandler;
import com.suidifu.jpmorgan.handler.WritebackStatHandler;
import com.suidifu.jpmorgan.service.OrderNoRegisterService;
import com.suidifu.jpmorgan.service.PaymentGatewayConfigService;
import com.suidifu.jpmorgan.service.TradeScheduleService;
import com.suidifu.jpmorgan.spec.DistributeTaskSpec;
import com.suidifu.jpmorgan.spec.StatusWriteBackTaskSpec;
import com.suidifu.jpmorgan.spec.TradeScheduleHandlerSpec;
import com.suidifu.jpmorgan.spec.TradeScheduleHandlerSpec.ErrorMsg;
import com.suidifu.jpmorgan.spec.logspec.GloableLogSpec;
import com.suidifu.jpmorgan.spec.logspec.GloableLogSpec.JPMORGAN_FUNCTION_POINT;
import com.suidifu.jpmorgan.util.JsoupUtils;

@Component("statusWritebackJob")
public class StatusWriteBackJob implements JobHandler {
	
	@Autowired
	private TradeScheduleService tradeScheduleService;
	@Autowired
	private PaymentGatewayConfigService paymentGatewayConfigService;
	@Autowired
	private TradeScheduleHandler tradeScheduleHandler;
	@Autowired
	private WritebackStatHandler writebackStatHandler;
	@Autowired
	ConfigInitializer configInitializer;
	@Autowired
	OrderNoRegisterService orderNoRegisterService;

	private static Log logger = LogFactory.getLog(StatusWriteBackJob.class);
	
	@Override
	public void run(Map<String, Object> workingParms) {
		//logger.info("statusWriteBackJob start ....");
		//long jobst = System.currentTimeMillis();
		try {
			int workerNo = Integer.parseInt(workingParms.getOrDefault("workerNo", 1).toString());
			List<Integer> modPriority = (List<Integer>) workingParms.getOrDefault("modPriority", new ArrayList<Integer>() {{}});
			
			List<TradeSchedule> idleScheduleList = new ArrayList<TradeSchedule>();
		
			int modIndex = 0;
			while(CollectionUtils.isEmpty(idleScheduleList) && modIndex <= modPriority.size()) {
				if(modIndex == modPriority.size()) {
					modIndex = -1;
				}
				idleScheduleList = tradeScheduleService.peekBusinessProcessingSchedules(DistributeTaskSpec.PEEK_IDLE_LIMIT, modPriority, modIndex, workerNo);
				modIndex ++;
				if(0 == modIndex) {
					break;
				}
			}
			
			
			if(CollectionUtils.isEmpty(idleScheduleList)) {
				return;
			}
		
			//long fort = System.currentTimeMillis();
			for(TradeSchedule idleSchedule : idleScheduleList) {
				String oppositeKeyWord = "";
				try {
					oppositeKeyWord="[sourceMessageUuid=" + idleSchedule.getSourceMessageUuid() + ";slotUuid=" + "]";
					//long startTime = System.currentTimeMillis();
					int nthSlot = idleSchedule.currentWorkSlot();
					if(0 == nthSlot) {
						continue;
					}
					
					GatewaySlot gatewaySlot = idleSchedule.extractSlotInfo(nthSlot);
					
					String slotUuid = gatewaySlot.getSlotUuid();
					
					//Map<String, Object> gatewayConfig = paymentGatewayConfigService.getGatewayConfigByChannelUuid(gatewaySlot.getPaymentChannelUuid());
					Map<String, String> gatewayConfig = configInitializer.getGatewayChannelConfigKeyAndValueMapper().getOrDefault(gatewaySlot.getPaymentChannelUuid(), new HashMap<String, String>());

					String businessStatusUpdateURL = gatewayConfig.getOrDefault(TradeScheduleHandlerSpec.BusinessStatusUpdateURLKey, StringUtils.EMPTY);
					String tableName = gatewayConfig.getOrDefault(TradeScheduleHandlerSpec.PaymentOrderQueueTableNameKey, StringUtils.EMPTY);
					String logTableName = gatewayConfig.getOrDefault(TradeScheduleHandlerSpec.PaymentOrderLogTableNameKey, StringUtils.EMPTY);
					String gatewayName = gatewayConfig.getOrDefault(TradeScheduleHandlerSpec.GatewayNameKey, StringUtils.EMPTY);
					if(StringUtils.isEmpty(businessStatusUpdateURL) || StringUtils.isEmpty(tableName)) {
						throw new Exception(ErrorMsg.MSG_NO_GATEWAY_CONFIG);
					}
					
					Map<String,Object> data = new HashMap<String, Object>();
					data.put("uuid", slotUuid);
					data.put("tableName", tableName);
					data.put("logTableName", logTableName);

					Result responseResult = JsoupUtils.post(businessStatusUpdateURL, data);
					
					if(! responseResult.isValid()) {
						logger.warn(GloableLogSpec.AuditLogHeaderSpec() + JPMORGAN_FUNCTION_POINT.RECV_STATUS_WRITE_BACK +oppositeKeyWord + "ERR:status write communication faile");
						continue;
					}
					
					String jsonResult = responseResult.getData().getOrDefault("gatewaySlot", StringUtils.EMPTY).toString();
					
					if(StringUtils.isEmpty(jsonResult)) {
						logger.warn(GloableLogSpec.AuditLogHeaderSpec() + JPMORGAN_FUNCTION_POINT.RECV_STATUS_WRITE_BACK +oppositeKeyWord + "ERR:result is empty");
						continue;
					}
					GatewaySlot gatewaySlotUpdate = JSON.parseObject(jsonResult, GatewaySlot.class);
					
					logger.info(GloableLogSpec.AuditLogHeaderSpec() + JPMORGAN_FUNCTION_POINT.RECV_STATUS_WRITE_BACK +oppositeKeyWord + "[busienssStatus=" + gatewaySlotUpdate.getBusinessStatus()+"]");
					
					if(! CommunicationStatus.Inqueue.equals(gatewaySlotUpdate.getCommunicationStatus())) {
						tradeScheduleService.updateBusinessStatusAndCommunicationStatusInSlot(idleSchedule, nthSlot, gatewaySlotUpdate, gatewayName);
					}
					
					//writebackStatHandler.writebackStat(gatewaySlot.getPaymentChannelUuid(), new Date(), gatewaySlotUpdate);
					tradeScheduleService.updateTransactionTime(idleSchedule.getId(), nthSlot, gatewaySlot.getTransactionBeginTime(), gatewaySlot.getCommunicationEndTime(), StatusWriteBackTaskSpec.UPDATE_TRY_TIMES);
					TradeSchedule tradeSchedule = tradeScheduleService.getTradeScheduleBy(idleSchedule.getId());
					if(null == tradeSchedule) {
						logger.warn("query tradeSchedule failed...");
						continue;
					}
					
					if(tradeSchedule.canResubmit()) {
						orderNoRegisterService.logOffRegister(tradeSchedule.getOutlierTransactionUuid());
					}
					
					//如果成功，转移至log
					if(tradeSchedule.canBeTransfer()) {
						tradeScheduleHandler.transferToTradeScheduleLog(tradeSchedule);

					}
				} catch (Exception e) {
					e.printStackTrace();
					logger.info(GloableLogSpec.AuditLogHeaderSpec() + JPMORGAN_FUNCTION_POINT.RECV_STATUS_WRITE_BACK +oppositeKeyWord + "ERR:" + e.getMessage());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}

}
