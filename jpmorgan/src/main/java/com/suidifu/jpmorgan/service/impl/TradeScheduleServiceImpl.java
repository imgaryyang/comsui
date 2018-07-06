package com.suidifu.jpmorgan.service.impl;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.suidifu.coffer.GlobalSpec;
import com.suidifu.jpmorgan.entity.*;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.demo2do.core.service.impl.GenericServiceImpl;
import com.suidifu.jpmorgan.service.TradeScheduleService;

@Service("tradeScheduleService")
public class TradeScheduleServiceImpl extends GenericServiceImpl<TradeSchedule>
		implements TradeScheduleService {
	

	private static HashMap<Integer, String> slotPrefix = new HashMap<Integer, String>() {
		{
			put(1, "fst");
			put(2, "snd");
			put(3, "trd");
			put(4, "fth");
			put(5, "fvth");
		}
	};

	@Override
	public void tradeScheduleInQueue(TradeSchedule tradeSchedule) {
		if(null == tradeSchedule) {
			return;
		}
		
		Map<String, Object> parms = new HashMap<String, Object>();
		parms.put("accessVersion", tradeSchedule.getAccessVersion());
		parms.put("accountSide", tradeSchedule.getAccountSide());
		parms.put("batchUuid", tradeSchedule.getBatchUuid());
		parms.put("businessStatus", tradeSchedule.getBusinessStatus());
		parms.put("currencyCode", tradeSchedule.getCurrencyCode());
		parms.put("destinationAccountAppendix", tradeSchedule.getDestinationAccountAppendix());
		parms.put("destinationAccountName", tradeSchedule.getDestinationAccountName());
		parms.put("destinationAccountNo", tradeSchedule.getDestinationAccountNo());
		parms.put("destinationBankInfo", tradeSchedule.getDestinationBankInfo());
		parms.put("executionPrecond", tradeSchedule.getExecutionPrecond());
		parms.put("fstBusinessResultMsg", tradeSchedule.getFstBusinessResultMsg());
		parms.put("fstBusinessStatus", tradeSchedule.getFstBusinessStatus());
		parms.put("fstCommunicationEndTime", tradeSchedule.getFstCommunicationEndTime());
		parms.put("fstCommunicationLastSentTime", tradeSchedule.getFstCommunicationLastSentTime());
		parms.put("fstCommunicationStartTime", tradeSchedule.getFstCommunicationStartTime());
		parms.put("fstCommunicationStatus", tradeSchedule.getFstCommunicationStatus());
		parms.put("fstEffectiveAbsoluteTime", tradeSchedule.getFstEffectiveAbsoluteTime());
		parms.put("fstGatewayRouterInfo", tradeSchedule.getFstGatewayRouterInfo());
		parms.put("fstGatewayType", null == tradeSchedule.getFstGatewayType() ? null : tradeSchedule.getFstGatewayType());
		parms.put("fstPaymentChannelUuid", tradeSchedule.getFstPaymentChannelUuid());
		parms.put("fstSlotUuid", tradeSchedule.getFstSlotUuid());
		parms.put("fstTransactionAmount", tradeSchedule.getFstTransactionAmount());
		parms.put("fstTransactionBeginTime", tradeSchedule.getFstTransactionBeginTime());
		parms.put("fstTransactionEndTime", tradeSchedule.getFstTransactionEndTime());
		parms.put("fstWorkerUuid", tradeSchedule.getFstWorkerUuid());
		parms.put("fthBusinessResultMsg", tradeSchedule.getFthBusinessResultMsg());
		parms.put("fthBusinessStatus", tradeSchedule.getFthBusinessStatus());
		parms.put("fthCommunicationEndTime", tradeSchedule.getFthCommunicationEndTime());
		parms.put("fthCommunicationLastSentTime", tradeSchedule.getFthCommunicationLastSentTime());
		parms.put("fthCommunicationStartTime", tradeSchedule.getFthCommunicationStartTime());
		parms.put("fthCommunicationStatus", tradeSchedule.getFthCommunicationStatus());
		parms.put("fthEffectiveAbsoluteTime", tradeSchedule.getFthEffectiveAbsoluteTime());
		parms.put("fthGatewayRouterInfo", tradeSchedule.getFthGatewayRouterInfo());
		parms.put("fthGatewayType", null == tradeSchedule.getFthGatewayType() ? null : tradeSchedule.getFthGatewayType());
		parms.put("fthPaymentChannelUuid", tradeSchedule.getFthPaymentChannelUuid());
		parms.put("fthSlotUuid", tradeSchedule.getFthSlotUuid());
		parms.put("fthTransactionAmount", tradeSchedule.getFthTransactionAmount());
		parms.put("fthTransactionBeginTime", tradeSchedule.getFthTransactionBeginTime());
		parms.put("fthTransactionEndTime", tradeSchedule.getFthTransactionEndTime());
		parms.put("fthWorkerUuid", tradeSchedule.getFthWorkerUuid());
		parms.put("fvthBusinessResultMsg", tradeSchedule.getFvthBusinessResultMsg());
		parms.put("fvthBusinessStatus", tradeSchedule.getFvthBusinessStatus());
		parms.put("fvthCommunicationEndTime", tradeSchedule.getFvthCommunicationEndTime());
		parms.put("fvthCommunicationLastSentTime", tradeSchedule.getFvthCommunicationLastSentTime());
		parms.put("fvthCommunicationStartTime", tradeSchedule.getFvthCommunicationStartTime());
		parms.put("fvthCommunicationStatus", tradeSchedule.getFvthCommunicationStatus());
		parms.put("fvthEffectiveAbsoluteTime", tradeSchedule.getFvthEffectiveAbsoluteTime());
		parms.put("fvthGatewayRouterInfo", tradeSchedule.getFvthGatewayRouterInfo());
		parms.put("fvthGatewayType", null == tradeSchedule.getFvthGatewayType() ? null : tradeSchedule.getFvthGatewayType());
		parms.put("fvthPaymentChannelUuid", tradeSchedule.getFvthPaymentChannelUuid());
		parms.put("fvthSlotUuid", tradeSchedule.getFvthSlotUuid());
		parms.put("fvthTransactionAmount", tradeSchedule.getFvthTransactionAmount());
		parms.put("fvthTransactionBeginTime", tradeSchedule.getFvthTransactionBeginTime());
		parms.put("fvthTransactionEndTime", tradeSchedule.getFvthTransactionEndTime());
		parms.put("fvthWorkerUuid", tradeSchedule.getFvthWorkerUuid());
		parms.put("outlierTransactionUuid", tradeSchedule.getOutlierTransactionUuid());
		parms.put("postscript", tradeSchedule.getPostscript());
		parms.put("sndBusinessResultMsg", tradeSchedule.getSndBusinessResultMsg());
		parms.put("sndBusinessStatus", tradeSchedule.getSndBusinessStatus());
		parms.put("sndCommunicationEndTime", tradeSchedule.getSndCommunicationEndTime());
		parms.put("sndCommunicationLastSentTime", tradeSchedule.getSndCommunicationLastSentTime());
		parms.put("sndCommunicationStartTime", tradeSchedule.getSndCommunicationStartTime());
		parms.put("sndCommunicationStatus", tradeSchedule.getSndCommunicationStatus());
		parms.put("sndEffectiveAbsoluteTime", tradeSchedule.getSndEffectiveAbsoluteTime());
		parms.put("sndGatewayRouterInfo", tradeSchedule.getSndGatewayRouterInfo());
		parms.put("sndGatewayType", null == tradeSchedule.getSndGatewayType() ? null : tradeSchedule.getSndGatewayType());
		parms.put("sndPaymentChannelUuid", tradeSchedule.getSndPaymentChannelUuid());
		parms.put("sndSlotUuid", tradeSchedule.getSndSlotUuid());
		parms.put("sndTransactionAmount", tradeSchedule.getSndTransactionAmount());
		parms.put("sndTransactionBeginTime", tradeSchedule.getSndTransactionBeginTime());
		parms.put("sndTransactionEndTime", tradeSchedule.getSndTransactionEndTime());
		parms.put("sndWorkerUuid", tradeSchedule.getSndWorkerUuid());
		parms.put("sourceAccountAppendix", tradeSchedule.getSourceAccountAppendix());
		parms.put("sourceAccountName", tradeSchedule.getSourceAccountName());
		parms.put("sourceAccountNo", tradeSchedule.getSourceAccountNo());
		parms.put("sourceBankInfo", tradeSchedule.getSourceBankInfo());
		parms.put("sourceMessageIp", tradeSchedule.getSourceMessageIp());
		parms.put("sourceMessageTime", tradeSchedule.getSourceMessageTime());
		parms.put("sourceMessageUuid", tradeSchedule.getSourceMessageUuid());
		parms.put("tradeUuid", tradeSchedule.getTradeUuid());
		parms.put("trdBusinessResultMsg", tradeSchedule.getTrdBusinessResultMsg());
		parms.put("trdBusinessStatus", tradeSchedule.getTrdBusinessStatus());
		parms.put("trdCommunicationEndTime", tradeSchedule.getTrdCommunicationEndTime());
		parms.put("trdCommunicationLastSentTime", tradeSchedule.getTrdCommunicationLastSentTime());
		parms.put("trdCommunicationStartTime", tradeSchedule.getTrdCommunicationStartTime());
		parms.put("trdCommunicationStatus", tradeSchedule.getTrdCommunicationStatus());
		parms.put("trdEffectiveAbsoluteTime", tradeSchedule.getTrdEffectiveAbsoluteTime());
		parms.put("trdGatewayRouterInfo", tradeSchedule.getTrdGatewayRouterInfo());
		parms.put("trdGatewayType", null == tradeSchedule.getTrdGatewayType() ? null : tradeSchedule.getTrdGatewayType());
		parms.put("trdPaymentChannelUuid", tradeSchedule.getTrdPaymentChannelUuid());
		parms.put("trdSlotUuid", tradeSchedule.getTrdSlotUuid());
		parms.put("trdTransactionAmount", tradeSchedule.getTrdTransactionAmount());
		parms.put("trdTransactionBeginTime", tradeSchedule.getTrdTransactionBeginTime());
		parms.put("trdTransactionEndTime", tradeSchedule.getTrdTransactionEndTime());
		parms.put("trdWorkerUuid", tradeSchedule.getTrdWorkerUuid());
		
		parms.put("stringFieldOne", tradeSchedule.getStringFieldOne());
		parms.put("stringFieldTwo", tradeSchedule.getStringFieldTwo());
		parms.put("stringFieldThree", tradeSchedule.getStringFieldThree());
		parms.put("longFieldOne", tradeSchedule.getLongFieldOne());
		
		genericDaoSupport.executeSQL("INSERT INTO `trade_schedule` (`access_version`, `account_side`, `batch_uuid`, `business_status`, `currency_code`, `destination_account_appendix`, `destination_account_name`, `destination_account_no`, `destination_bank_info`, `execution_precond`, `fst_business_result_msg`, `fst_business_status`, `fst_communication_end_time`, `fst_communication_last_sent_time`, `fst_communication_start_time`, `fst_communication_status`, `fst_effective_absolute_time`, `fst_gateway_router_info`, `fst_gateway_type`, `fst_payment_channel_uuid`, `fst_slot_uuid`, `fst_transaction_amount`, `fst_transaction_begin_time`, `fst_transaction_end_time`, `fst_worker_uuid`, `fth_business_result_msg`, `fth_business_status`, `fth_communication_end_time`, `fth_communication_last_sent_time`, `fth_communication_start_time`, `fth_communication_status`, `fth_effective_absolute_time`, `fth_gateway_router_info`, `fth_gateway_type`, `fth_payment_channel_uuid`, `fth_slot_uuid`, `fth_transaction_amount`, `fth_transaction_begin_time`, `fth_transaction_end_time`, `fth_worker_uuid`, `fvth_business_result_msg`, `fvth_business_status`, `fvth_communication_end_time`, `fvth_communication_last_sent_time`, `fvth_communication_start_time`, `fvth_communication_status`, `fvth_effective_absolute_time`, `fvth_gateway_router_info`, `fvth_gateway_type`, `fvth_payment_channel_uuid`, `fvth_slot_uuid`, `fvth_transaction_amount`, `fvth_transaction_begin_time`, `fvth_transaction_end_time`, `fvth_worker_uuid`, `outlier_transaction_uuid`, `postscript`, `snd_business_result_msg`, `snd_business_status`, `snd_communication_end_time`, `snd_communication_last_sent_time`, `snd_communication_start_time`, `snd_communication_status`, `snd_effective_absolute_time`, `snd_gateway_router_info`, `snd_gateway_type`, `snd_payment_channel_uuid`, `snd_slot_uuid`, `snd_transaction_amount`, `snd_transaction_begin_time`, `snd_transaction_end_time`, `snd_worker_uuid`, `source_account_appendix`, `source_account_name`, `source_account_no`, `source_bank_info`, `source_message_ip`, `source_message_time`, `source_message_uuid`, `trade_uuid`, `trd_business_result_msg`, `trd_business_status`, `trd_communication_end_time`, `trd_communication_last_sent_time`, `trd_communication_start_time`, `trd_communication_status`, `trd_effective_absolute_time`, `trd_gateway_router_info`, `trd_gateway_type`, `trd_payment_channel_uuid`, `trd_slot_uuid`, `trd_transaction_amount`, `trd_transaction_begin_time`, `trd_transaction_end_time`, `trd_worker_uuid`, `string_field_one`, `string_field_two`, `string_field_three`, `long_field_one`) VALUES"
				+ " (:accessVersion, :accountSide, :batchUuid, :businessStatus, :currencyCode, :destinationAccountAppendix, :destinationAccountName, :destinationAccountNo, :destinationBankInfo, :executionPrecond,"
				+ " :fstBusinessResultMsg, :fstBusinessStatus, :fstCommunicationEndTime, :fstCommunicationLastSentTime, :fstCommunicationStartTime, :fstCommunicationStatus, :fstEffectiveAbsoluteTime, :fstGatewayRouterInfo, :fstGatewayType, :fstPaymentChannelUuid, :fstSlotUuid, :fstTransactionAmount, :fstTransactionBeginTime, :fstTransactionEndTime, :fstWorkerUuid,"
				+ " :fthBusinessResultMsg, :fthBusinessStatus, :fthCommunicationEndTime, :fthCommunicationLastSentTime, :fthCommunicationStartTime, :fthCommunicationStatus, :fthEffectiveAbsoluteTime, :fthGatewayRouterInfo, :fthGatewayType, :fthPaymentChannelUuid, :fthSlotUuid, :fthTransactionAmount, :fthTransactionBeginTime, :fthTransactionEndTime, :fthWorkerUuid,"
				+ " :fvthBusinessResultMsg, :fvthBusinessStatus, :fvthCommunicationEndTime, :fvthCommunicationLastSentTime, :fvthCommunicationStartTime, :fvthCommunicationStatus, :fvthEffectiveAbsoluteTime, :fvthGatewayRouterInfo, :fvthGatewayType, :fvthPaymentChannelUuid, :fvthSlotUuid, :fvthTransactionAmount, :fvthTransactionBeginTime, :fvthTransactionEndTime, :fvthWorkerUuid,"
				+ " :outlierTransactionUuid, :postscript,"
				+ " :sndBusinessResultMsg, :sndBusinessStatus, :sndCommunicationEndTime, :sndCommunicationLastSentTime, :sndCommunicationStartTime, :sndCommunicationStatus, :sndEffectiveAbsoluteTime, :sndGatewayRouterInfo, :sndGatewayType, :sndPaymentChannelUuid, :sndSlotUuid, :sndTransactionAmount, :sndTransactionBeginTime, :sndTransactionEndTime, :sndWorkerUuid,"
				+ " :sourceAccountAppendix, :sourceAccountName, :sourceAccountNo, :sourceBankInfo, :sourceMessageIp, :sourceMessageTime, :sourceMessageUuid, :tradeUuid,"
				+ " :trdBusinessResultMsg, :trdBusinessStatus, :trdCommunicationEndTime, :trdCommunicationLastSentTime, :trdCommunicationStartTime, :trdCommunicationStatus, :trdEffectiveAbsoluteTime, :trdGatewayRouterInfo, :trdGatewayType, :trdPaymentChannelUuid, :trdSlotUuid, :trdTransactionAmount, :trdTransactionBeginTime, :trdTransactionEndTime, :trdWorkerUuid, :stringFieldOne, :stringFieldTwo, :stringFieldThree, :longFieldOne)"
				, parms);
	}

	@Override
	public void tradeScheduleInQueue(List<TradeSchedule> tradeScheduleList) {

		if (CollectionUtils.isEmpty(tradeScheduleList)) {
			return;
		}
		for (TradeSchedule tradeSchedule : tradeScheduleList) {
			try {
				genericDaoSupport.save(tradeSchedule);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public List<TradeSchedule> peekIdleSchedules(int limit, List<Integer> piorityList, int modIndex, int workerNo) {
		Map<String, Object> parms = new HashMap<String, Object>();
		parms.put("inqueue", BusinessStatus.Inqueue.ordinal());
		parms.put("processing", BusinessStatus.Processing.ordinal());
		parms.put("success", BusinessStatus.Success.ordinal());
		parms.put("failed", BusinessStatus.Failed.ordinal());
		parms.put("abandon", BusinessStatus.Abandon.ordinal());
		parms.put("workerNo", workerNo);
		parms.put("limit", limit);

		parms.put("oppositeProcessing",
				BusinessStatus.OppositeProcessing.ordinal());
		
		String ModStr = "";
		if(-1 != modIndex && CollectionUtils.isNotEmpty(piorityList)) {
			ModStr = " MOD(id, " + piorityList.get(modIndex) + ") = 0 AND";
		}

		return genericDaoSupport.queryForList("select * from trade_schedule where"
				+ ModStr
				+ " (business_status =:inqueue or business_status =:processing)"
				+ " and (fst_business_status =:failed or fst_business_status =:inqueue or fst_business_status =:abandon) and"
				+ " (snd_business_status =:failed or snd_business_status =:inqueue or snd_business_status =:abandon) and"
				+ " (trd_business_status =:failed or trd_business_status =:inqueue or trd_business_status =:abandon) and"
				+ " (fth_business_status =:failed or fth_business_status =:inqueue or fth_business_status =:abandon) and"
				+ " (fvth_business_status =:failed or fvth_business_status =:inqueue or fvth_business_status =:abandon)"
				+ " and (fst_business_status =:inqueue or snd_business_status =:inqueue or trd_business_status =:inqueue or fth_business_status =:inqueue or fvth_business_status =:inqueue)"
				+ " and long_field_one =:workerNo"
				+ " order by id limit :limit", parms, TradeSchedule.class);

	}

	private static final String updateRecheckTemplate = "select id from trade_schedule where id =:id and access_version =:accessVersion";

	public boolean ProcessScheduleInSlot(Long tradeScheduleId, int nthSlot) {

		String preAccessVersion = getCurrentAccessVersion(tradeScheduleId);
		Map<String, Object> parms = new HashMap<String, Object>();

		parms.put("id", tradeScheduleId);
		parms.put("preAccessVersion", preAccessVersion);
		parms.put("businessProcessStatus", BusinessStatus.Processing.ordinal());
		parms.put("businessInqueueStatus", BusinessStatus.Inqueue.ordinal());

		parms.put("slotBusinessProcessStatus",
				BusinessStatus.Processing.ordinal());
		parms.put("slotCommunicationInqueueStatus",
				CommunicationStatus.Inqueue.ordinal());

		parms.put("accessVersion", UUID.randomUUID().toString());

		String scheduleInProcessingTemplate = "update trade_schedule set access_version =:accessVersion, "
				+ " business_status=:businessProcessStatus,%s_business_status =:slotBusinessProcessStatus"
				+ " where id =:id and access_version =:preAccessVersion and"
				+ " (business_status =:businessInqueueStatus or business_status =:businessProcessStatus) and"
				+ " %s_business_status =:businessInqueueStatus and %s_communication_status =:slotCommunicationInqueueStatus ";

		String prefix = slotPrefix.get(nthSlot);
		if (StringUtils.isEmpty(prefix) == true)
			return false;
		String scheduleInProcessingSql = scheduleInProcessingTemplate.replace(
				"%s", prefix);

		genericDaoSupport.executeSQL(scheduleInProcessingSql, parms);
		String updateRecheck = updateRecheckTemplate;
		try {
			int nRows = genericDaoSupport.queryForInt(updateRecheck, parms);
			if (1 <= nRows)
				return true;
			else
				return false;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean updateTransactionTime(Long tradeScheduleId, int nthSlot,// TODO
																			// 尝试3次
			Date tranBegin, Date tranEnd, int tryTimes) {
		Map<String, Object> parms = new HashMap<String, Object>();
		String prefix = slotPrefix.get(nthSlot);

		for (int i = 0; i < tryTimes; i++) {
			String preAccessVersion = getCurrentAccessVersion(tradeScheduleId);
			parms.put("id", tradeScheduleId);
			parms.put("preAccessVersion", preAccessVersion);
			parms.put("accessVersion", UUID.randomUUID().toString());
			parms.put("beginTime", tranBegin);
			parms.put("endTime", tranEnd);

			String updateTransactionTemplate = "UPDATE trade_schedule SET access_version =:accessVersion, "
					+ " %s_transaction_begin_time =:beginTime,%s_transaction_end_time=:endTime"
					+ " WHERE id =:id and access_version =:preAccessVersion";

			String updateTransactionSql = updateTransactionTemplate.replace(
					"%s", prefix);

			genericDaoSupport.executeSQL(updateTransactionSql, parms);
			int nRows = genericDaoSupport.queryForInt(updateRecheckTemplate,
					parms);
			if (1 <= nRows) {
				return true;
			}
		}

		return false;
	}

	@Override
	public boolean updateSlotInSchedule(SlotInfo slotInfo) {
		Map<String, Object> parms = new HashMap<String, Object>();

		String preAccessVersion = getCurrentAccessVersion(slotInfo
				.getTradeUuid());
		parms.put("preAccessVersion", preAccessVersion);
		parms.put("accessVersion", UUID.randomUUID().toString());
		parms.put("tradeUuid", slotInfo.getTradeUuid());
		// parms.put("slotUuid", slotInfo.getSlotUuid());
		parms.put("gatewayType", slotInfo.getGatewayType().ordinal());
		parms.put("paymentChannelUuid", slotInfo.getPaymentChannelUuid());
		parms.put("effectiveTime", slotInfo.getEffectiveTime());
		parms.put("transactionAmount", slotInfo.getTransactionAmount());
		parms.put("slotCommunicationInqueueStatus",
				CommunicationStatus.Inqueue.ordinal());
		parms.put("slotBusinessInqueueStatus", BusinessStatus.Inqueue.ordinal());
		// parms.put("gatewayUuid", slotInfo.getGatewayUuid());

		String updateSlotExeTemplate = "UPDATE trade_schedule SET access_version =:accessVersion, %s_gateway_type =:gatewayType,"
				+ "%s_payment_channel_uuid =:paymentChannelUuid,%s_effective_absolute_time =:effectiveTime,"
				+ "%s_transaction_amount =:transactionAmount"
				+ " WHERE access_version =:preAccessVersion AND trade_uuid =:tradeUuid AND"
				+ " %s_business_status=:slotBusinessInqueueStatus AND %s_communication_status=:slotCommunicationInqueueStatus";

		String prefix = slotPrefix.get(slotInfo.getNthSlot());
		if (StringUtils.isEmpty(prefix) == true)
			return false;
		String updateSql = updateSlotExeTemplate.replace("%s", prefix);
		String updateRecheck = updateRecheckTemplate;

		try {
			genericDaoSupport.executeSQL(updateSql, parms);
			int updatedRows = genericDaoSupport.queryForInt(updateRecheck,
					parms);
			if (1 <= updatedRows)
				return true;
			else
				return false;
		} catch (Exception e) {
			return false;
		}

	}

	@Override
	public boolean abandonSchedule(TradeSchedule tradeSchedule) {
		Map<String, Object> parms = new HashMap<String, Object>();

		String preAccessVersion = getCurrentAccessVersion(tradeSchedule.getTradeUuid());
		parms.put("id", tradeSchedule.getId());
		parms.put("preAccessVersion", preAccessVersion);
		parms.put("tradeUuid", tradeSchedule.getTradeUuid());
		parms.put("inqueue", BusinessStatus.Inqueue.ordinal());
		parms.put("processing", BusinessStatus.Processing.ordinal());
		parms.put("success", BusinessStatus.Success.ordinal());
		parms.put("oppositeProcessing",
				BusinessStatus.OppositeProcessing.ordinal());
		parms.put("abandon", BusinessStatus.Abandon.ordinal());
		parms.put("failed", BusinessStatus.Failed.ordinal());
		parms.put("accessVersion", UUID.randomUUID().toString());

		int updatedRows = 0;
		updatedRows = abandonSchedule(parms);
		if (1 <= updatedRows) {
			return true;
		} else
			return false;
	}

	private int abandonSchedule(Map<String, Object> parms) {
		try {
			genericDaoSupport
					.executeSQL(
							"update trade_schedule set access_version =:accessVersion, "
									+ "business_status =:abandon, fst_business_status =:abandon, "
									+ "snd_business_status =:abandon, trd_business_status =:abandon,"
									+ "fth_business_status =:abandon, fvth_business_status =:abandon "
									+ "where access_version =:preAccessVersion and trade_uuid =:tradeUuid and (business_status =:inqueue or business_status =:processing) "
									+ "AND (fst_business_status =:failed or fst_business_status =:inqueue or fst_business_status=:abandon) and "
									+ "(snd_business_status =:failed or snd_business_status =:inqueue or snd_business_status=:abandon) and "
									+ "(trd_business_status =:failed or trd_business_status =:inqueue or trd_business_status=:abandon) and "
									+ "(fth_business_status =:failed or fth_business_status =:inqueue or fth_business_status=:abandon) and "
									+ "(fvth_business_status =:failed or fvth_business_status =:inqueue or fvth_business_status=:abandon) and "
									+ "(fst_business_status=:inqueue or snd_business_status=:inqueue or trd_business_status =:inqueue or fth_business_status =:inqueue or fvth_business_status =:inqueue)",
							parms);

			return genericDaoSupport.queryForInt(updateRecheckTemplate, parms);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	private String getCurrentAccessVersion(Long tradeScheduleId) {

		List<String> resultList = genericDaoSupport.queryForSingleColumnList(
				"SELECT access_version FROM trade_schedule WHERE id =:id",
				"id", tradeScheduleId, String.class);

		if (CollectionUtils.isEmpty(resultList)) {
			return StringUtils.EMPTY;
		}
		return resultList.get(0);
	}

	private String getCurrentAccessVersion(String tradeUuid) {

		List<String> resultList = genericDaoSupport
				.queryForSingleColumnList(
						"SELECT access_version FROM trade_schedule WHERE trade_uuid =:tradeUuid",
						"tradeUuid", tradeUuid, String.class);

		if (CollectionUtils.isEmpty(resultList)) {
			return StringUtils.EMPTY;
		}
		return resultList.get(0);
	}

	private String abandonTemplate = "update trade_schedule set access_version =:accessVersion, "
			+ " %s_business_status =:slotBusinessAbandonStatus,%s_communication_status=:slotCommunicationAbandonStatus"
			+ "where access_version =:preAccessVersion and trade_uuid =:tradeUuid and"
			+ " (business_status =:inqueue or business_status =:processing) and"
			+ "%s_business_status ==:slotBusinessInqueueStatus and %s_communication_status=: slotCommunicationInqueueStatus ";

	@Override
	public boolean abandonSlotInSchedule(String tradeUuid, int nthSlot) {
		try {
			Map<String, Object> parms = new HashMap<String, Object>();

			String preAccessVersion = getCurrentAccessVersion(tradeUuid);
			parms.put("preAccessVersion", preAccessVersion);
			parms.put("tradeUuid", tradeUuid);
			parms.put("slotBusinessInqueueStatus",
					BusinessStatus.Inqueue.ordinal());
			parms.put("slotBusinessAbandonStatus",
					BusinessStatus.Abandon.ordinal());
			parms.put("slotCommunicationInqueueStatus",
					CommunicationStatus.Inqueue.ordinal());
			parms.put("slotCommunicationAbandonStatus",
					CommunicationStatus.Abandon.ordinal());
			parms.put("accessVersion", UUID.randomUUID().toString());
			String prefix = slotPrefix.get(nthSlot);
			String abandonSql = abandonTemplate.replace("%s", prefix);
			genericDaoSupport.executeSQL(abandonSql, parms);

			int Id = genericDaoSupport
					.queryForInt(updateRecheckTemplate, parms);
			if (1 <= Id)
				return true;
			else
				return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public List<TradeSchedule> peekBusinessProcessingSchedules(int limit, List<Integer> piorityList, int modIndex, int workerNo) {
		Map<String, Object> parms = new HashMap<String, Object>();
		parms.put("inqueue", BusinessStatus.Inqueue.ordinal());
		parms.put("processing", BusinessStatus.Processing.ordinal());
		parms.put("success", BusinessStatus.Success.ordinal());
		parms.put("workerNo", workerNo);
		parms.put("limit", limit);

		String ModStr = "";
		if(-1 != modIndex && CollectionUtils.isNotEmpty(piorityList)) {
			ModStr = " MOD(id, " + piorityList.get(modIndex) + ") = 0 AND";
		}
		
		return genericDaoSupport.queryForList("select * from trade_schedule where "
								+ ModStr
								+ " business_status =:processing and long_field_one =:workerNo limit :limit", parms, TradeSchedule.class);
				
	}

	@Override
	public boolean updateBusinessStatusAndCommunicationStatusInSlot(
			TradeSchedule tradeSchedule, int nthSlot,
			GatewaySlot gatewaySlotUpdate, String gatewayName) {

		try {
			Integer commStatus = gatewaySlotUpdate.getCommunicationStatus();
			Integer busiStatus = gatewaySlotUpdate.getBusinessStatus();

			Map<String, Object> parms = new HashMap<String, Object>();

			String preAccessVersion = getCurrentAccessVersion(tradeSchedule
					.getId());
			parms.put("id", tradeSchedule.getId());
			parms.put("preAccessVersion", preAccessVersion);
			parms.put("tradeUuid", tradeSchedule.getTradeUuid());
			parms.put("inqueue", BusinessStatus.Inqueue.ordinal());
			parms.put("oppositeProcess",
					BusinessStatus.OppositeProcessing.ordinal());
			parms.put("succ", BusinessStatus.Success.ordinal());
			parms.put("failed", BusinessStatus.Failed.ordinal());
			parms.put("businessAbandon", BusinessStatus.Abandon.ordinal());
			parms.put("businessProcessing", BusinessStatus.Processing.ordinal());
			parms.put("communicationProcessing",
					CommunicationStatus.Process.ordinal());
			parms.put("communicationInqueue",
					CommunicationStatus.Inqueue.ordinal());
			parms.put("communicationSuccess",
					CommunicationStatus.Success.ordinal());

			parms.put("communicationAbandon",
					CommunicationStatus.Abandon.ordinal());
			parms.put("nthSlotBusiStatus", busiStatus);
			parms.put("nthSlotCommStatus", commStatus);

			parms.put("communicationStartTime",
					gatewaySlotUpdate.getCommunicationStartTime());
			parms.put("communicationEndTime",
					gatewaySlotUpdate.getCommunicationEndTime());
			parms.put("communicationLastSentTime",
					gatewaySlotUpdate.getCommunicationLastSentTime());
			parms.put("businessSuccessTime", gatewaySlotUpdate.getBusinessSuccessTime());
			parms.put("businessResultMsg", gatewaySlotUpdate.getBusinessResultMsg());
			parms.put("channelSequenceNo", gatewaySlotUpdate.getChannelSequenceNo());

			parms.put("accessVersion", UUID.randomUUID().toString());
//			List<TradeSchedule> schedules = genericDaoSupport.searchForList(
//					"FROM TradeSchedule WHERE tradeUuid =:tradeUuid", parms);
//			if (CollectionUtils.isEmpty(schedules) == true)
//				return false;
//			TradeSchedule schedule = schedules.get(0);// TODO get last
			String businessResultCode = StringUtils.EMPTY;
			String businessResultMsg = gatewaySlotUpdate.getBusinessResultMsg();

			if(!StringUtils.isEmpty(gatewaySlotUpdate.getBusinessResultMsg())) {
				List<String> msgList = new ArrayList<String>();
				Pattern p = Pattern.compile(GlobalSpec.BUSINESS_RESULT_CODE_REGULAR);
				Matcher m = p.matcher(gatewaySlotUpdate.getBusinessResultMsg());

				while(m.find()) {
					msgList.add(m.group().substring(1, m.group().length()-1));
				}
				if(CollectionUtils.isEmpty(msgList)) {
					businessResultMsg = gatewaySlotUpdate.getBusinessResultMsg();
				}
				if(msgList.size() > 0) {
					businessResultCode = msgList.get(0);
				}
				if (msgList.size() > 1){
					businessResultMsg = msgList.get(1);
				}

			}
			String prefix = slotPrefix.get(nthSlot);
			String updateBusiStatus = "";
			if (isLastSlot(tradeSchedule, nthSlot) == true
					&& (commStatus.equals(CommunicationStatus.Failed.ordinal()) || busiStatus
							.equals(BusinessStatus.Failed.ordinal())))
				updateBusiStatus = "business_status=:failed,";

			if (busiStatus.equals(BusinessStatus.Success.ordinal())) {
				updateBusiStatus = "business_status=:succ,";
				updateBusiStatus = abandonLaterSlot(tradeSchedule, nthSlot,
						updateBusiStatus);
			}

			String updateBusinessStatus = "update trade_schedule set access_version =:accessVersion, "
					+ updateBusiStatus
					+ " business_success_time =:businessSuccessTime,"
					+ " %s_business_status =:nthSlotBusiStatus,%s_communication_status=:nthSlotCommStatus,"
					+ " %s_communication_start_time =:communicationStartTime, %s_communication_end_time =:communicationEndTime, %s_communication_last_sent_time =:communicationLastSentTime,"
					+ " %s_business_result_msg =:businessResultMsg, %s_channel_sequence_no =:channelSequenceNo"
					+ " where id =:id and access_version =:preAccessVersion and trade_uuid =:tradeUuid and"
					+ " (business_status =:inqueue or business_status =:businessProcessing or business_status=:oppositeProcess ) and"
					+ " (%s_business_status =:businessProcessing or  %s_business_status =:oppositeProcess)"
					+ " and (%s_communication_status =:communicationProcessing or %s_communication_status=:communicationInqueue or %s_communication_status=:communicationSuccess)";

			String updateBusinessStatusSql = updateBusinessStatus.replace("%s",
					prefix);
			genericDaoSupport.executeSQL(updateBusinessStatusSql, parms);
			int Id = genericDaoSupport
					.queryForInt(updateRecheckTemplate, parms);
			if (1 <= Id)
				return true;
			else
				return false;
		} catch (Exception e) {
			e.printStackTrace();//TODO delete
			return false;
		}
	}

	private String abandonLaterSlot(TradeSchedule tradeSchedule, int nthSlot,
			String updateBusiStatus) {
		for (int i = 1; i <= 5; i++) {
			if (i == nthSlot)
				continue;
			GatewaySlot slot = tradeSchedule.extractSlotInfo(i);
			if (StringUtils.isEmpty(slot.getSlotUuid()))
				continue;
			Integer status = slot.getBusinessStatus();
			if (status != null
					&& status.equals(BusinessStatus.Inqueue.ordinal())) {
				String slotprefix = slotPrefix.get(i);
				updateBusiStatus += "%s_business_status=:businessAbandon,".replace("%s", slotprefix)
						+ "%s_communication_status=:communicationAbandon,"
								.replace("%s", slotprefix);

			}
		}
		return updateBusiStatus;
	}

	private boolean isLastSlot(TradeSchedule schedule, int nthSlot) {

		if (nthSlot == 5)
			return true;

		else if (nthSlot == 4
				&& StringUtils.isEmpty(schedule.getFvthPaymentChannelUuid()) == true)
			return true;

		else if (nthSlot == 3
				&& StringUtils.isEmpty(schedule.getFthPaymentChannelUuid()) == true)
			return true;

		else if (nthSlot == 2
				&& StringUtils.isEmpty(schedule.getTrdPaymentChannelUuid()) == true)
			return true;

		else if (nthSlot == 1
				&& StringUtils.isEmpty(schedule.getSndPaymentChannelUuid()) == true)
			return true;

		else
			return false;

	}

	@Override
	public TradeSchedule getTradeScheduleBy(Long tradeScheduleId) {
		if (null == tradeScheduleId) {
			return null;
		}
		List<TradeSchedule> tradeSchedulelist = genericDaoSupport.queryForList(
				"select * from trade_schedule where id =:id", "id",
				tradeScheduleId, TradeSchedule.class);
		if (CollectionUtils.isEmpty(tradeSchedulelist)) {
			return null;
		}
		return tradeSchedulelist.get(0);
	}

	@Override
	public List<TradeSchedule> getTradeScheduleListBy(String transactionUuid) {
		if (StringUtils.isEmpty(transactionUuid)) {
			return Collections.EMPTY_LIST;
		}

		return genericDaoSupport
				.queryForList(
						"select * from trade_schedule where outlier_transaction_uuid =:transactionUuid",
						"transactionUuid", transactionUuid, TradeSchedule.class);

	}

	@Override
	public List<TradeSchedule> getTradeScheduleListByBatch(String batchUuid) {
		if(StringUtils.isEmpty(batchUuid)) {
			return Collections.EMPTY_LIST;
		}
		
		return genericDaoSupport.queryForList("select * from trade_schedule where batch_uuid =:batchUuid", "batchUuid", batchUuid, TradeSchedule.class);
	}

	@Override
	public TradeSchedule getTradeScheduleByTradeUuid(String tradeUuid) {
		if(StringUtils.isEmpty(tradeUuid)) {
			return null;
		}
		
		List<TradeSchedule> tradeScheduleList = genericDaoSupport.queryForList("select * from trade_schedule where trade_uuid =:tradeUuid", "tradeUuid", tradeUuid, TradeSchedule.class);

		if(CollectionUtils.isEmpty(tradeScheduleList)) {
			return null;
		}
		
		return tradeScheduleList.get(0);
	}

}
