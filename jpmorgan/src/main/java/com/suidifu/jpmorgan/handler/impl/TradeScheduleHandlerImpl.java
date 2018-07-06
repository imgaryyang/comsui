package com.suidifu.jpmorgan.handler.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.demo2do.core.entity.Result;
import com.demo2do.core.utils.JsonUtils;
import com.suidifu.coffer.GlobalSpec;
import com.suidifu.coffer.entity.QueryCreditResult;
import com.suidifu.jpmorgan.ConfigInitializer;
import com.suidifu.jpmorgan.config.WFCCBConfigInitializer;
import com.suidifu.jpmorgan.entity.AccountSide;
import com.suidifu.jpmorgan.entity.BusinessErrorReason;
import com.suidifu.jpmorgan.entity.BusinessStatus;
import com.suidifu.jpmorgan.entity.CallbackAsyncJobServer;
import com.suidifu.jpmorgan.entity.DeductErrorCodeMapSpec;
import com.suidifu.jpmorgan.entity.GatewaySlot;
import com.suidifu.jpmorgan.entity.OrderNoRegister;
import com.suidifu.jpmorgan.entity.PaymentOrder;
import com.suidifu.jpmorgan.entity.PaymentOrderWorkerConfig;
import com.suidifu.jpmorgan.entity.PaymentReturnModel;
import com.suidifu.jpmorgan.entity.QueryStatusDetail;
import com.suidifu.jpmorgan.entity.QueryStatusResult;
import com.suidifu.jpmorgan.entity.SlotInfo;
import com.suidifu.jpmorgan.entity.SupplyCallbackResultModel;
import com.suidifu.jpmorgan.entity.TradeInfo;
import com.suidifu.jpmorgan.entity.TradeSchedule;
import com.suidifu.jpmorgan.entity.TradeScheduleLog;
import com.suidifu.jpmorgan.entity.TradeScheduleModel;
import com.suidifu.jpmorgan.entity.WorkingContext;
import com.suidifu.jpmorgan.exception.TradeScheduleParseException;
import com.suidifu.jpmorgan.exception.UpdateSlotException;
import com.suidifu.jpmorgan.factory.PaymentHandlerFactory;
import com.suidifu.jpmorgan.handler.DistributeStatHandler;
import com.suidifu.jpmorgan.handler.PaymentHandler;
import com.suidifu.jpmorgan.handler.TradeScheduleHandler;
import com.suidifu.jpmorgan.hash.ConsistentHash;
import com.suidifu.jpmorgan.service.OrderNoRegisterService;
import com.suidifu.jpmorgan.service.PaymentGatewayConfigService;
import com.suidifu.jpmorgan.service.PaymentOrderWorkerConfigService;
import com.suidifu.jpmorgan.service.TradeScheduleLogService;
import com.suidifu.jpmorgan.service.TradeScheduleService;
import com.suidifu.jpmorgan.spec.StatusWriteBackTaskSpec;
import com.suidifu.jpmorgan.spec.TradeScheduleHandlerSpec;
import com.suidifu.jpmorgan.spec.TradeScheduleHandlerSpec.ErrorMsg;
import com.suidifu.jpmorgan.spec.logspec.GloableLogSpec;
import com.suidifu.jpmorgan.spec.logspec.GloableLogSpec.JPMORGAN_FUNCTION_POINT;
import com.suidifu.jpmorgan.util.JsoupUtils;
import com.suidifu.swift.notifyserver.notifyserver.NotifyApplication;
import com.suidifu.swift.notifyserver.notifyserver.notifyenum.RoutingKeyType;
import com.zufangbao.gluon.spec.morganstanley.CommonSpec;

@Component("tradeScheduleHandler")
public class TradeScheduleHandlerImpl implements TradeScheduleHandler {

	@Autowired
	PaymentGatewayConfigService paymentGatewayConfigService;
	@Autowired
	TradeScheduleService tradeScheduleService;
	@Autowired
	TradeScheduleLogService tradeScheduleLogService;
	@Autowired
	DistributeStatHandler distributeStatHandler;
	@Autowired
	ConfigInitializer configInitializer;
	@Autowired
	PaymentOrderWorkerConfigService paymentOrderWorkerConfigService;
	@Autowired
	OrderNoRegisterService orderNoRegisterService;
	@Autowired
	private CallbackAsyncJobServer callbackAsyncJobServer;
	@Autowired
	WFCCBConfigInitializer wfccbConfigInitializer;
	
	@Value("#{config['notifyserver.groupCacheJobQueueMapForMq']}")
	private String groupCacheJobQueueMapForMqStr;
	
	private static final Log logger = LogFactory
			.getLog(TradeScheduleHandlerImpl.class);

	@Override
	public PaymentReturnModel parseAndSaveSinglePayment(String reqPacket)
			throws TradeScheduleParseException {
		if (StringUtils.isEmpty(reqPacket)) {
			throw new TradeScheduleParseException(
					ErrorMsg.MSG_EMPTY_PACKET_ERROR);
		}
//		TradeSchedule tradeSchedule = JSON.parseObject(reqPacket,
//				TradeSchedule.class);
		TradeScheduleModel tradeScheduleModel = JSON.parseObject(reqPacket, TradeScheduleModel.class);
		if (null == tradeScheduleModel) {
			throw new TradeScheduleParseException(
					ErrorMsg.MSG_FORMAT_PACKET_ERROR);
		}

		TradeSchedule tradeSchedule = new TradeSchedule(tradeScheduleModel);
		
		PaymentReturnModel paymentReturnModel = validateAndSaveSinglePayment(tradeSchedule);

		return paymentReturnModel;
	}

	private PaymentReturnModel validateAndSaveSinglePayment(
			TradeSchedule tradeSchedule) throws TradeScheduleParseException {
		if (!tradeSchedule.hasNecessaryAttr()) {
			throw new TradeScheduleParseException(
					ErrorMsg.MSG_EMPTY_NECESSSARY_ATTR);
		}
		
		String oppositeKeyWord="[sourceMessageUuid=" + tradeSchedule.getSourceMessageUuid() + ";";
		logger.info(GloableLogSpec.AuditLogHeaderSpec() + JPMORGAN_FUNCTION_POINT.RECV_TRADE_REQUEST_FROM_OUTLIER_SYSTEM +oppositeKeyWord);
		
		String paymentChannelUuid = tradeSchedule.getFstPaymentChannelUuid();
		//Map<String, Object> gatewayConfigs = paymentGatewayConfigService.getGatewayConfigByChannelUuid(paymentChannelUuid);
		Map<String, String> gatewayConfigs = configInitializer.getGatewayChannelConfigKeyAndValueMapper().getOrDefault(paymentChannelUuid, null);
		if(null == gatewayConfigs || gatewayConfigs.isEmpty()) {
			throw new TradeScheduleParseException(
					ErrorMsg.MSG_NOT_EXIST_CHANNEL_UUID);
		}

		try {
			orderNoRegisterService.save(new OrderNoRegister(tradeSchedule.getOutlierTransactionUuid(), new Date()));
		} catch (Exception e) {
			throw new TradeScheduleParseException(
					ErrorMsg.MSG_REJECT_SUBMIT_ERROR
							+ tradeSchedule.getOutlierTransactionUuid());
		}
//		List<TradeSchedule> historyScheduleList = queryTradeSchedule(tradeSchedule
//				.getOutlierTransactionUuid());
//		if (!CollectionUtils.isEmpty(historyScheduleList)) {
//			TradeSchedule recentTradeSchedule = historyScheduleList
//					.get(historyScheduleList.size() - 1);// TODO 按时间排序
//			if (!recentTradeSchedule.canResubmit()) {
//				throw new TradeScheduleParseException(
//						ErrorMsg.MSG_REJECT_SUBMIT_ERROR
//								+ tradeSchedule.getOutlierTransactionUuid());
//			}
//		}

		ConsistentHash<Integer> generalConsistentHashSource = configInitializer.getGeneralConsistentHashSource();
		int nextCount = wfccbConfigInitializer.getNextCount();

		tradeSchedule.fillInitData(generalConsistentHashSource, gatewayConfigs.getOrDefault(TradeScheduleHandlerSpec.GatewayNameKey, StringUtils.EMPTY).toString(), nextCount);

		tradeScheduleService.tradeScheduleInQueue(tradeSchedule);

		return tradeSchedule.transferToReturnModel();
	}

	@Override
	public List<PaymentReturnModel> parseAndSaveBatchPayment(String reqPacket)
			throws TradeScheduleParseException {
		if (StringUtils.isEmpty(reqPacket)) {
			throw new TradeScheduleParseException(
					ErrorMsg.MSG_EMPTY_PACKET_ERROR);
		}

//		List<TradeSchedule> tradeScheduleList = JSON.parseArray(reqPacket,
//				TradeSchedule.class);
//
//		if (CollectionUtils.isEmpty(tradeScheduleList)) {
//			throw new TradeScheduleParseException(
//					ErrorMsg.MSG_FORMAT_PACKET_ERROR);
//		}
		
		List<TradeScheduleModel> tradeScheduleModelList = JSON.parseArray(reqPacket, TradeScheduleModel.class);
		if (CollectionUtils.isEmpty(tradeScheduleModelList)) {
			throw new TradeScheduleParseException(
					ErrorMsg.MSG_FORMAT_PACKET_ERROR);
		}
		
		List<PaymentReturnModel> returnModelList = new ArrayList<PaymentReturnModel>();
		for (TradeScheduleModel tradeScheduleModel : tradeScheduleModelList) {
			TradeSchedule tradeSchedule = new TradeSchedule(tradeScheduleModel);

			PaymentReturnModel paymentReturnModel = validateAndSaveSinglePayment(tradeSchedule);
			returnModelList.add(paymentReturnModel);
		}

		return returnModelList;
	}

	@Override
	public Result distributeToWorker(TradeSchedule tradeSchedule, GatewaySlot gatewaySlot, int nthSlot) {
		Result result = new Result();
		String oppositeKeyWord = StringUtils.EMPTY;
		try {
			TradeInfo tradeInfo = tradeSchedule.extractTradeInfo(gatewaySlot);

			//Map<String, Object> gatewayConfig = paymentGatewayConfigService.getGatewayConfigByChannelUuid(gatewaySlot.getPaymentChannelUuid());
			Map<String, String> gatewayConfig = configInitializer.getGatewayChannelConfigKeyAndValueMapper().getOrDefault(gatewaySlot.getPaymentChannelUuid(), new HashMap<String, String>());
			
			String distributeURL = gatewayConfig.getOrDefault(TradeScheduleHandlerSpec.DistributeURLKey, StringUtils.EMPTY);
			String tableName = gatewayConfig.getOrDefault(TradeScheduleHandlerSpec.PaymentOrderQueueTableNameKey, StringUtils.EMPTY);
			if (StringUtils.isEmpty(distributeURL) || StringUtils.isEmpty(tableName)) {
				logger.warn(tradeSchedule.getTradeUuid() + ErrorMsg.MSG_NO_GATEWAY_CONFIG);
				return result.fail();
			}

			ConsistentHash<Integer> consistentHash = configInitializer.getGatewayChannelAndConsistentHashMapper().get(gatewaySlot.getPaymentChannelUuid());
			String tradeUuid = tradeInfo.getUuid();
			Integer queueIndex = consistentHash.getNode(tradeUuid);
			
			Map<String, Object> parms = new HashMap<String, Object>();
			parms.put(TradeScheduleHandlerSpec.DistributeContentKey, JSON.toJSONString(tradeInfo));
			parms.put(TradeScheduleHandlerSpec.PaymentOrderQueueTableNameKey, tableName);
			parms.put(TradeScheduleHandlerSpec.QueueIndexKey, queueIndex);
			
			Map<String, Object> innerCallbackInfo = new HashMap<String, Object>();
			String innerCallbackUrl = gatewayConfig.getOrDefault(TradeScheduleHandlerSpec.InnerCallbackInfoKey, StringUtils.EMPTY);
			innerCallbackInfo.put("innerCallbackUrl", innerCallbackUrl);
			innerCallbackInfo.put("nthSlot", nthSlot);
			innerCallbackInfo.put("tradeUuid", tradeSchedule.getTradeUuid());
			innerCallbackInfo.put("gatewayName", gatewayConfig.getOrDefault(TradeScheduleHandlerSpec.GatewayNameKey, StringUtils.EMPTY));
			
			parms.put(TradeScheduleHandlerSpec.InnerCallbackInfoKey, JsonUtils.toJsonString(innerCallbackInfo));
			
			oppositeKeyWord="[sourceMessageUuid=" + tradeSchedule.getSourceMessageUuid() + ";slotUuid=" + gatewaySlot.getSlotUuid() + "]";
			logger.info(GloableLogSpec.AuditLogHeaderSpec() + JPMORGAN_FUNCTION_POINT.SEND_TRADE_SCHEDULES_TO_PAYMENTORDER +oppositeKeyWord);
						
			//distributeStatHandler.distributeStat(new Date(), gatewaySlot.getPaymentChannelUuid(), tradeInfo.getTransactionAmount());
			result = JsoupUtils.post(distributeURL, parms);
			
			logger.info(GloableLogSpec.AuditLogHeaderSpec() + JPMORGAN_FUNCTION_POINT.SEND_TRADE_SCHEDULES_TO_PAYMENTORDER +oppositeKeyWord+"[SUCC]");
				
		} catch (Exception e) {
			logger.info(GloableLogSpec.AuditLogHeaderSpec() + JPMORGAN_FUNCTION_POINT.SEND_TRADE_SCHEDULES_TO_PAYMENTORDER +oppositeKeyWord+"[ERR:"+e.getMessage()+"]");
			e.printStackTrace();
			return result.fail();
		}

		return result;
	}

	@Override
	public void transferToTradeScheduleLog(TradeSchedule tradeSchedule) {

		TradeScheduleLog tradeScheduleLog = tradeSchedule
				.transferToTradeScheduleLog();
		tradeScheduleLogService.save(tradeScheduleLog);
		tradeScheduleService.delete(tradeSchedule);

		callback(tradeSchedule);

	}

	private void callback(TradeSchedule tradeSchedule) {
		//如果有回调地址
		if(StringUtils.isNotEmpty(tradeSchedule.getStringFieldOne())) {
			NotifyApplication notifyApplication = new NotifyApplication();
			notifyApplication.setBusinessId(UUID.randomUUID().toString());
			notifyApplication.setHttpJobUuid(UUID.randomUUID().toString());
			notifyApplication.setRequestUrl(tradeSchedule.getStringFieldOne());
			QueryStatusResult queryStatusResult = extractQueryInfo(tradeSchedule);
			HashMap<String, String> parms = new HashMap<>();
			parms.put(CommonSpec.TRANSACTION_RESULT, JsonUtils.toJsonString(queryStatusResult));
			notifyApplication.setRequestParameters(parms);
			notifyApplication.setRequestMethod(NotifyApplication.POST_METHOD);
			notifyApplication.setRetryTimes(2);
			//set retry time
			HashMap<Integer, Long> retryIntervals = new HashMap<>();
			retryIntervals.put(1, 300000l);
			retryIntervals.put(2, 600000l);
			notifyApplication.setRetryIntervals(retryIntervals);

			notifyApplication.setMqRoutingKey(RoutingKeyType.CONSISTENT_HASH);
			
			if (StringUtils.isNotEmpty(tradeSchedule.getStringFieldTwo())){
				notifyApplication.setConsistenceHashPolicy(tradeSchedule.getStringFieldTwo());
			}else {
				notifyApplication.setConsistenceHashPolicy(tradeSchedule.getBatchUuid());
			}

			pushJob(notifyApplication,tradeSchedule.getStringFieldThree());
		}
	}

	/*
	 * @Override public void fillGatewayRouterInfo(TradeSchedule tradeSchedule)
	 * { if(null == tradeSchedule.getFstGatewayType()) { return; }
	 * fillGatewayRouterInfo(1, tradeSchedule);
	 * 
	 * if(null == tradeSchedule.getSndGatewayType()) { return; }
	 * fillGatewayRouterInfo(2, tradeSchedule);
	 * 
	 * if(null == tradeSchedule.getTrdGatewayType()) { return; }
	 * fillGatewayRouterInfo(3, tradeSchedule);
	 * 
	 * if(null == tradeSchedule.getFthGatewayType()) { return; }
	 * fillGatewayRouterInfo(4, tradeSchedule);
	 * 
	 * if(null == tradeSchedule.getFvthGatewayType()) { return; }
	 * fillGatewayRouterInfo(5, tradeSchedule); }
	 * 
	 * 
	 * private void fillGatewayRouterInfo(int nthSlot, TradeSchedule
	 * tradeSchedule) { String channelUuid; switch (nthSlot) { case 1:
	 * channelUuid = tradeSchedule.getFstPaymentChannelUuid(); break; case 2:
	 * channelUuid = tradeSchedule.getSndPaymentChannelUuid(); break;
	 * 
	 * case 3: channelUuid = tradeSchedule.getTrdPaymentChannelUuid(); break;
	 * 
	 * case 4: channelUuid = tradeSchedule.getFthPaymentChannelUuid(); break;
	 * 
	 * case 5: channelUuid = tradeSchedule.getFvthPaymentChannelUuid(); break;
	 * 
	 * default: return; }
	 * 
	 * PaymentGatewayConfig paymentGatewayConfig =
	 * paymentGatewayConfigService.getPaymentGatewayConfigBy(channelUuid);
	 * if(null == paymentGatewayConfig) { return; }
	 * 
	 * Map<String, Object> parms = new HashMap<String, Object>();
	 * parms.put(TradeScheduleHandlerSpec.PaymentOrderQueueTableNameKey,
	 * paymentGatewayConfig.getPaymentOrderQueueTableName());
	 * tradeSchedule.appendGatewayRouterInfo(nthSlot, parms); }
	 */

	@Override
	public boolean updateSlot(SlotInfo slotInfo) throws UpdateSlotException {
		if (!slotInfo.hasNecessaryAttr()) {
			throw new UpdateSlotException(ErrorMsg.MSG_NOT_COMPLETE_SLOT_INFO);
		}

		return tradeScheduleService.updateSlotInSchedule(slotInfo);
	}

	@Override
	public List<QueryStatusResult> queryStatus(String transactionUuid, String batchUuid) {
		List<QueryStatusResult> queryStatusResultList = new ArrayList<QueryStatusResult>();
		
		if(StringUtils.isEmpty(transactionUuid) && StringUtils.isEmpty(batchUuid)) {
			return queryStatusResultList;
		}
		
		if(StringUtils.isNotEmpty(transactionUuid)) {
			TradeSchedule tradeSchedule = queryRecentTransaction(transactionUuid);
			if(null == tradeSchedule) {
				return queryStatusResultList;
			}
			QueryStatusResult queryStatusResult = this.extractQueryInfo(tradeSchedule);
			queryStatusResultList.add(queryStatusResult);
			return queryStatusResultList;
		}
		
		if(StringUtils.isNotEmpty(batchUuid)) {
			List<TradeSchedule> tradeScheduleList = this.getTradeScheduleByBatch(batchUuid);
			for(TradeSchedule tradeSchedule : tradeScheduleList) {
				QueryStatusResult queryStatusResult = this.extractQueryInfo(tradeSchedule);
				queryStatusResultList.add(queryStatusResult);
			}
		}

		return queryStatusResultList;
	}

	private TradeSchedule queryRecentTransaction(String transactionUuid) {
		List<TradeSchedule> tradeScheduleList = queryTradeSchedule(transactionUuid);

		if (CollectionUtils.isEmpty(tradeScheduleList)) {
			return null;
		}

		return tradeScheduleList.get(tradeScheduleList.size() - 1);
	}

	private List<TradeSchedule> queryTradeSchedule(String transactionUuid) {
		List<TradeSchedule> tradeScheduleList = new ArrayList<TradeSchedule>();
		tradeScheduleList = tradeScheduleService
				.getTradeScheduleListBy(transactionUuid);
		if (CollectionUtils.isEmpty(tradeScheduleList)) {
			tradeScheduleList = tradeScheduleLogService
					.getTradeScheduleListBy(transactionUuid);
		}
		return tradeScheduleList;
	}


	public boolean meetStartPrecond(String transactionUuid) {

		TradeSchedule tradeSchedule = queryRecentTransaction(transactionUuid);
		
		if(null == tradeSchedule) {
			return false;
		}
		
		BusinessStatus actualBusinessStatus = BusinessStatus
				.fromOrdinal(tradeSchedule.getBusinessStatus());

		if (null == actualBusinessStatus) {
			return false;
		}
		
		return BusinessStatus.Success.equals(actualBusinessStatus);
	}
	
	public boolean meetAbandonPrecond(String transactionUuid) {

		TradeSchedule tradeSchedule = queryRecentTransaction(transactionUuid);
		
		if(null == tradeSchedule) {
			return false;
		}
		
		BusinessStatus actualBusinessStatus = BusinessStatus
				.fromOrdinal(tradeSchedule.getBusinessStatus());

		if (null == actualBusinessStatus) {
			return false;
		}
		
		return (BusinessStatus.Failed.equals(actualBusinessStatus) || BusinessStatus.Abandon.equals(actualBusinessStatus));
	}
	

	@Override
	public boolean meetExecutionPrecond(TradeSchedule tradeSchedule) {

		if (StringUtils.isEmpty(tradeSchedule.getExecutionPrecond())) {
			return true;
		}
		
		try {
			Map<String, List<String>> executionPrecond = JSON
					.parseObject(tradeSchedule.getExecutionPrecond(), Map.class);
			
			List<String> abandonPrecondList = executionPrecond.getOrDefault(
					"abandon", new ArrayList<String>());
			if(!abandonPrecondList.isEmpty()) {
				boolean needAbandon = true;
				for(String abandonPrecond : abandonPrecondList) {
					boolean meetPrecond = meetAbandonPrecond(abandonPrecond);
					if (!meetPrecond) {
						needAbandon = false;
						break;
					}
				}
				if(needAbandon) {
					boolean abandonSuccess = tradeScheduleService.abandonSchedule(tradeSchedule);
					if(abandonSuccess) {
						tradeSchedule = tradeScheduleService.getTradeScheduleBy(tradeSchedule.getId());
						transferToTradeScheduleLog(tradeSchedule);
					}
					return false;
				}
			}

			List<String> startPrecondList = executionPrecond.getOrDefault(
					"start", new ArrayList<String>());
			if (!startPrecondList.isEmpty()) {
				for(String startPrecond : startPrecondList) {
					boolean meetPrecond = meetStartPrecond(startPrecond);
					if (!meetPrecond) {
						return false;
					}
				}
				return true;
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("execution precond 格式错误！");// TODO
		}

		return false;
	}

	@Override
	public QueryStatusResult extractQueryInfo(TradeSchedule tradeSchedule) {
		
		int nthSlot = tradeSchedule.theLastProcessSlot();
		
		QueryStatusResult queryStatusResult = new QueryStatusResult(
				tradeSchedule.getSourceMessageUuid(),
				tradeSchedule.getOutlierTransactionUuid(),
				tradeSchedule.getTradeUuid(),//TODO 2016.11.21改，set tradeuuid此句无效
				tradeSchedule.getDestinationAccountName(),
				tradeSchedule.getDestinationAccountNo(),
				BusinessStatus.fromOrdinal(tradeSchedule.getBusinessStatus()));
		
		GatewaySlot gatewaySlot = tradeSchedule.extractSlotInfo(nthSlot);

		String paymentChannelUuid = gatewaySlot.getPaymentChannelUuid();
		Map<String, Object> gatewayConfig = paymentGatewayConfigService.getGatewayConfigByChannelUuid(paymentChannelUuid);
		String channelAccountNo = gatewayConfig.getOrDefault("channelAccountNo", StringUtils.EMPTY).toString();
		String channelAccountName = gatewayConfig.getOrDefault("channelAccountName", StringUtils.EMPTY).toString();
		
		queryStatusResult.setPaymentChannelUuid(paymentChannelUuid);
		queryStatusResult.setChannelAccountName(channelAccountName);
		queryStatusResult.setChannelAccountNo(channelAccountNo);
		queryStatusResult.setBatchUuid(tradeSchedule.getBatchUuid());
		
		queryStatusResult.setTradeUuid(gatewaySlot.getSlotUuid());//TODO 2016.11.21改，在只用fst_slot的情况下，默认直接将fst_slot_uuid返回
		queryStatusResult.setTransactionAmount(gatewaySlot.getTransactionAmount());
		queryStatusResult.setChannelSequenceNo(gatewaySlot.getChannelSequenceNo());
		
		String businessResultCode = StringUtils.EMPTY;
		String businessResultMsg = gatewaySlot.getBusinessResultMsg();
		
		if(!StringUtils.isEmpty(gatewaySlot.getBusinessResultMsg())) {
			List<String> msgList = new ArrayList<String>();
	        Pattern p = Pattern.compile(GlobalSpec.BUSINESS_RESULT_CODE_REGULAR);  
	        Matcher m = p.matcher(gatewaySlot.getBusinessResultMsg());
	        
	        while(m.find()) {
	            msgList.add(m.group().substring(1, m.group().length()-1));  
	        }
	        if(CollectionUtils.isEmpty(msgList)) {
	        	businessResultMsg = gatewaySlot.getBusinessResultMsg();
	        }
	        if(msgList.size() > 0) {
        		businessResultCode = msgList.get(0);
        	}
	        if (msgList.size() > 1){
        		businessResultMsg = msgList.get(1);
        	}
	        
		}
		
		queryStatusResult.setBusinessResultCode(businessResultCode);
		queryStatusResult.setBusinessResultMsg(businessResultMsg);
		
		queryStatusResult.setCommunicationLastSentTime(gatewaySlot.getCommunicationLastSentTime());
		
		queryStatusResult.setBusinessSuccessTime(tradeSchedule.getBusinessSuccessTime());

		List<QueryStatusDetail> queryStatusDetails = new ArrayList<>();
		for (int i=1;i<6;i++){
			GatewaySlot gs = tradeSchedule.extractSlotInfo(i);
			if (StringUtils.isEmpty(gs.getSlotUuid())){
				continue;
			}
			QueryStatusDetail queryStatusDetail = new QueryStatusDetail();
			queryStatusDetail.setChannelSequenceNo(gs.getChannelSequenceNo());
			queryStatusDetail.setChannelAccountNo(tradeSchedule.getDestinationAccountNo());
			queryStatusDetail.setBusinessSuccessTime(gs.getBusinessSuccessTime());
			if (queryStatusDetail.getBusinessSuccessTime() == null){
				queryStatusDetail.setBusinessSuccessTime(tradeSchedule.getBusinessSuccessTime());
			}

			queryStatusDetail.setBusinessResultMsg(gs.getBusinessResultMsg());
			queryStatusDetail.setBusinessStatus(BusinessStatus.fromOrdinal(gs.getBusinessStatus()));
			queryStatusDetail.setTradeUuid(gs.getSlotUuid());
			queryStatusDetail.setTransactionAmount(gs.getTransactionAmount());
			queryStatusDetails.add(queryStatusDetail);

		}
		queryStatusResult.setQueryStatusDetails(queryStatusDetails);

		return queryStatusResult;
	}
	
	
	@Override
	public QueryStatusResult extractQueryInfo(TradeSchedule tradeSchedule, int nthSlot) {
		
		QueryStatusResult queryStatusResult = new QueryStatusResult(
				tradeSchedule.getSourceMessageUuid(),
				tradeSchedule.getOutlierTransactionUuid(),
				tradeSchedule.getTradeUuid(),//TODO 2016.11.21改，set tradeuuid此句无效
				tradeSchedule.getDestinationAccountName(),
				tradeSchedule.getDestinationAccountNo(),
				null);
		
		GatewaySlot gatewaySlot = tradeSchedule.extractSlotInfo(nthSlot);
		queryStatusResult.setBusinessStatus(BusinessStatus.fromOrdinal(gatewaySlot.getBusinessStatus()));

		String paymentChannelUuid = gatewaySlot.getPaymentChannelUuid();
		Map<String, Object> gatewayConfig = paymentGatewayConfigService.getGatewayConfigByChannelUuid(paymentChannelUuid);
		String channelAccountNo = gatewayConfig.getOrDefault("channelAccountNo", StringUtils.EMPTY).toString();
		String channelAccountName = gatewayConfig.getOrDefault("channelAccountName", StringUtils.EMPTY).toString();
		
		queryStatusResult.setPaymentChannelUuid(paymentChannelUuid);
		queryStatusResult.setChannelAccountName(channelAccountName);
		queryStatusResult.setChannelAccountNo(channelAccountNo);
		
		queryStatusResult.setTradeUuid(gatewaySlot.getSlotUuid());//TODO 2016.11.21改，在只用fst_slot的情况下，默认直接将fst_slot_uuid返回
		queryStatusResult.setTransactionAmount(gatewaySlot.getTransactionAmount());
		queryStatusResult.setChannelSequenceNo(gatewaySlot.getChannelSequenceNo());
		
		String businessResultCode = StringUtils.EMPTY;
		String businessResultMsg = gatewaySlot.getBusinessResultMsg();
		
		if(!StringUtils.isEmpty(gatewaySlot.getBusinessResultMsg())) {
			List<String> msgList = new ArrayList<String>();
	        Pattern p = Pattern.compile(GlobalSpec.BUSINESS_RESULT_CODE_REGULAR);  
	        Matcher m = p.matcher(gatewaySlot.getBusinessResultMsg());
	        
	        while(m.find()) {
	            msgList.add(m.group().substring(1, m.group().length()-1));  
	        }
	        if(CollectionUtils.isEmpty(msgList)) {
	        	businessResultMsg = gatewaySlot.getBusinessResultMsg();
	        }
	        if(msgList.size() > 0) {
        		businessResultCode = msgList.get(0);
        	}
	        if (msgList.size() > 1){
        		businessResultMsg = msgList.get(1);
        	}
	        
		}
		
		queryStatusResult.setBusinessResultCode(businessResultCode);
		queryStatusResult.setBusinessResultMsg(businessResultMsg);
		
		queryStatusResult.setCommunicationLastSentTime(gatewaySlot.getCommunicationLastSentTime());
		
		queryStatusResult.setBusinessSuccessTime(tradeSchedule.getBusinessSuccessTime());
		
		return queryStatusResult;
	}


	@Override
	public List<TradeSchedule> getTradeScheduleByBatch(String batchUuid) {
		//TODO 过滤
		List<TradeSchedule> tradeScheduleList = new ArrayList<TradeSchedule>();
		List<TradeSchedule> tradeScheduleListInMaster = tradeScheduleService.getTradeScheduleListByBatch(batchUuid);
		List<TradeSchedule> tradeScheduleListInLog =  tradeScheduleLogService.getTradeScheduleListByBatch(batchUuid);
		tradeScheduleList.addAll(tradeScheduleListInMaster);
		tradeScheduleList.addAll(tradeScheduleListInLog);
		return tradeScheduleList;
	}

	@Override
	public QueryCreditResult queryOppositeStatus(String paymentChannelUuid, String transactionVoucherNo) {
		QueryCreditResult queryCreditResult =new QueryCreditResult();
		Map<String, Object> gatewayConfig = paymentGatewayConfigService.getGatewayConfigByChannelUuid(paymentChannelUuid);
		String gatewayName = gatewayConfig.getOrDefault(TradeScheduleHandlerSpec.GatewayNameKey, StringUtils.EMPTY).toString();
		String paymentGatewayUuid = gatewayConfig.getOrDefault("paymentGatewayUuid", StringUtils.EMPTY).toString();
		
		PaymentOrderWorkerConfig paymentOrderWorkerConfig = paymentOrderWorkerConfigService.getPaymentOrderWorkerConfigByPaymentGatewayUuid(paymentGatewayUuid);
		if (paymentOrderWorkerConfig == null) {
			logger.info("#queryOppositeStatus# paymentOrderWorkerConfig is null"+"["+"paymentGatewayUuid:"+paymentGatewayUuid+"]");
			return queryCreditResult;
		}
		Map<String, String> localWorkingConfig = paymentOrderWorkerConfig.getLocalWorkingConfig();
		PaymentOrder paymentOrder = new PaymentOrder();
		paymentOrder.setUuid(transactionVoucherNo);
		WorkingContext context = new WorkingContext();
		context.setWorkingParameters(localWorkingConfig);
		
		PaymentHandler hanlder = PaymentHandlerFactory.newInstance(gatewayName);
		if (hanlder == null) {
			logger.info("#queryOppositeStatus# PaymentHandler is null"+"["+"gatewayName:"+gatewayName+"]");
			return queryCreditResult;
		}
		queryCreditResult = hanlder.executeQueryPaymentStatus(paymentOrder, context);
		if (queryCreditResult == null || !transactionVoucherNo.equals(queryCreditResult.getTransactionVoucherNo())) {
			logger.info("#queryOppositeStatus# 查询对端交易状态失败！");
		}
		return queryCreditResult;
	}

	@Override
	public List<SupplyCallbackResultModel> supplyCallback(List<String> transactionUuidList) {
		
		List<SupplyCallbackResultModel> supplyCallbackResultModelList = new ArrayList<SupplyCallbackResultModel>();
		
		if(null == transactionUuidList) {
			return supplyCallbackResultModelList;
		}
		
		for(String transactionUuid : transactionUuidList) {
			
			TradeSchedule tradeSchedule = queryRecentTransaction(transactionUuid);

			if(null == tradeSchedule) {
				continue;
			}
			
			QueryStatusResult queryStatusResult = this.extractQueryInfo(tradeSchedule);
			
			SupplyCallbackResultModel supplyCallbackResultModel = new SupplyCallbackResultModel(transactionUuid, queryStatusResult.getBusinessStatus(), queryStatusResult.getBusinessResultMsg());
			
			supplyCallbackResultModelList.add(supplyCallbackResultModel);
			
			if(tradeSchedule.canBeTransfer()) {
				callback(tradeSchedule);
			}
		}
		
		return supplyCallbackResultModelList;
	}

	private boolean isExclusive(String financialContractUuid) {

		Map<String, Integer> groupCacheJobQueueMapForMq = JSON.parseObject(groupCacheJobQueueMapForMqStr,new TypeReference<Map<String, Integer>>(){});

		if(StringUtils.isEmpty(financialContractUuid) || MapUtils.isEmpty(groupCacheJobQueueMapForMq) || !groupCacheJobQueueMapForMq.containsKey(financialContractUuid)) {
			return false;
		}
		return true;
	}

	private void pushJob(NotifyApplication notifyApplication, String financialContractUuid) {
		if (isExclusive(financialContractUuid)) {
			notifyApplication.setGroupName(financialContractUuid);
		}
		callbackAsyncJobServer.pushJob(notifyApplication);
	}

	@Override
	public void handleInnerCallback(String tradeUuid, int nthSlot,
			String gatewayName, GatewaySlot gatewaySlotUpdate) {
		
		TradeSchedule tradeSchedule = tradeScheduleService.getTradeScheduleByTradeUuid(tradeUuid);
		
		if(null == tradeSchedule) {
			logger.info(GloableLogSpec.AuditLogHeaderSpec() + JPMORGAN_FUNCTION_POINT.INNER_CALLBACK_RECEIVE_CONTENT + "[ERR]:can't find callback tradeSchedule!");
			return;
		}

		tradeScheduleService.updateBusinessStatusAndCommunicationStatusInSlot(tradeSchedule, nthSlot, gatewaySlotUpdate, gatewayName);

		tradeScheduleService.updateTransactionTime(tradeSchedule.getId(), nthSlot, gatewaySlotUpdate.getTransactionBeginTime(), gatewaySlotUpdate.getCommunicationEndTime(), StatusWriteBackTaskSpec.UPDATE_TRY_TIMES);

		tradeSchedule = tradeScheduleService.getTradeScheduleBy(tradeSchedule.getId());
		if(null == tradeSchedule) {
			logger.info(GloableLogSpec.AuditLogHeaderSpec() + JPMORGAN_FUNCTION_POINT.INNER_CALLBACK_RECEIVE_CONTENT + "[ERR]:can't find updated tradeSchedule!");
			return;
		}
		
		if(tradeSchedule.canResubmit()) {
			orderNoRegisterService.logOffRegister(tradeSchedule.getOutlierTransactionUuid());
		}
		
		//如果成功，转移至log
		if(tradeSchedule.canBeTransfer()) {
			this.transferToTradeScheduleLog(tradeSchedule);
		}else {
			callback(tradeSchedule);
		}

		logger.info(GloableLogSpec.AuditLogHeaderSpec() + JPMORGAN_FUNCTION_POINT.INNER_CALLBACK_END + "operateKey:" + gatewaySlotUpdate.getSlotUuid());

	}

	@Override
	public Boolean executionNextSlot(TradeSchedule tradeSchedule) {
		if (tradeSchedule == null){
			return false;
		}
		int slotNub = tradeSchedule.nextReadySlot();
		Integer accountSide = tradeSchedule.getAccountSide();
		GatewaySlot gatewaySlot = tradeSchedule.extractSlotInfo(slotNub);
		if (gatewaySlot.getEffectiveAbsoluteTime() == null){
			return true;
		}else if (1 == slotNub && new Date().before(gatewaySlot.getEffectiveAbsoluteTime())) {
			return false;
		}else if (1 == slotNub && !new Date().before(gatewaySlot.getEffectiveAbsoluteTime())) {
			return true;
		}else if (accountSide == AccountSide.CREDIT.ordinal() && !new Date().before(gatewaySlot.getEffectiveAbsoluteTime())){
			return true;
		}else if (accountSide == AccountSide.CREDIT.ordinal() && new Date().before(gatewaySlot.getEffectiveAbsoluteTime())){
			return false;
		}

		int preSlotBub = slotNub -1;
		GatewaySlot preGatewaySlot = tradeSchedule.extractSlotInfo(preSlotBub);

		Map<String, String> gatewayConfig = configInitializer.getGatewayChannelConfigKeyAndValueMapper().getOrDefault(preGatewaySlot.getPaymentChannelUuid(), new HashMap<String, String>());
		if (gatewayConfig == null){
			return false;
		}
		String gatewayName = gatewayConfig.getOrDefault(TradeScheduleHandlerSpec.GatewayNameKey, StringUtils.EMPTY);
		String businessResultMsg = preGatewaySlot.getBusinessResultMsg();
		String  businessResultCode = StringUtils.EMPTY;
		if(!StringUtils.isEmpty(preGatewaySlot.getBusinessResultMsg())) {
			List<String> msgList = new ArrayList<String>();
			Pattern p = Pattern.compile(GlobalSpec.BUSINESS_RESULT_CODE_REGULAR);
			Matcher m = p.matcher(preGatewaySlot.getBusinessResultMsg());

			while(m.find()) {
				msgList.add(m.group().substring(1, m.group().length()-1));
			}
			if(CollectionUtils.isEmpty(msgList)) {
				businessResultMsg = preGatewaySlot.getBusinessResultMsg();
			}
			if(msgList.size() > 0) {
				businessResultCode = msgList.get(0);
			}
			if (msgList.size() > 1){
				businessResultMsg = msgList.get(1);

			}

		}

		if (AccountSide.DEBIT.ordinal() == accountSide && new Date().before(gatewaySlot.getEffectiveAbsoluteTime())
				&& businessError(gatewayName, businessResultCode)){
			return false;
		}

		return true;
	}

	private boolean businessError(String PaymentGateway, String errorCode){
		if (StringUtils.isEmpty(PaymentGateway)||StringUtils.isEmpty(errorCode)){
			return  false;
		}
		BusinessErrorReason businessErrorReason = DeductErrorCodeMapSpec.getBusinessErrorReason(PaymentGateway, errorCode);
		if (BusinessErrorReason.CARDNOERROR.equals(businessErrorReason) ||
				BusinessErrorReason.INSUFFICIENTBALANCE.equals(businessErrorReason) ||
				BusinessErrorReason.SYSTEMMAINTAINING.equals(businessErrorReason)){
			return  true;
		}
		return false;
	}

}
