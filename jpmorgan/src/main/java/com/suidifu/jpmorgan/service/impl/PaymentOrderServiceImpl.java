package com.suidifu.jpmorgan.service.impl;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.demo2do.core.service.impl.GenericServiceImpl;
import com.suidifu.coffer.entity.BusinessProcessStatus;
import com.suidifu.coffer.entity.QueryCreditResult;
import com.suidifu.jpmorgan.entity.BusinessStatus;
import com.suidifu.jpmorgan.entity.CommunicationStatus;
import com.suidifu.jpmorgan.entity.OccupyCommunicationStatus;
import com.suidifu.jpmorgan.entity.OccupyStatus;
import com.suidifu.jpmorgan.entity.PaymentOrder;
import com.suidifu.jpmorgan.service.PaymentOrderService;

@Service("paymentOrderService")
public class PaymentOrderServiceImpl extends GenericServiceImpl<PaymentOrder>
		implements PaymentOrderService {

	private static final Log logger = LogFactory.getLog(PaymentOrderServiceImpl.class);

	public static final String WAITING_PAY_QUEUE_CACHE_KEY_SUFFIX = "_WAITING_PAY_";
	
	public static final String WAITING_QUERY_QUEUE_CACHE_KEY_SUFFIX = "_WAITING_QUERY_";
	
	@Override
	public void taskInQueue(PaymentOrder task, String tableName, Integer queueIndex) {
		try {
			Map<String, Object> parms = new HashMap<String, Object>();
			parms.put("businessStatus", BusinessStatus.Processing.ordinal());
			parms.put("communicationStatus", CommunicationStatus.Inqueue.ordinal());
			parms.put("occupyCommunicationStatus",
					OccupyCommunicationStatus.Ready.ordinal());
			parms.put("occupyStatus", OccupyStatus.Free.ordinal());
			
			parms.put("accessVersion", task.getAccessVersion());
			parms.put("accountSide", task.getAccountSide());
			parms.put("currencyCode", task.getCurrencyCode());
			parms.put("destinationAccountAppendix", task.getDestinationAccountAppendix());
			parms.put("destinationAccountName", task.getDestinationAccountName());
			parms.put("destinationAccountNo", task.getDestinationAccountNo());
			parms.put("destinationBankInfo", task.getDestinationBankInfo());
			parms.put("gatewayRouterInfo", task.getGatewayRouterInfo());
			parms.put("gatewayType", task.getGatewayType());
			parms.put("postscript", task.getPostscript());
			parms.put("sourceAccountAppendix", task.getSourceAccountAppendix());
			parms.put("sourceAccountName", task.getSourceAccountName());
			parms.put("sourceAccountNo", task.getSourceAccountNo());
			parms.put("sourceBankInfo", task.getSourceBankInfo());
			parms.put("transactionAmount", task.getTransactionAmount().toPlainString());
			parms.put("outlierTransactionUuid", task.getOutlierTransactionUuid());
			parms.put("uuid", task.getUuid());

			parms.put("sourceMessageTime", task.getSourceMessageTime());
			
			parms.put("longFieldOne", queueIndex);
			parms.put("stringFieldOne", task.getStringFieldOne());
			
			genericDaoSupport
			.executeSQL(
					"insert into "
							+ tableName
							+ " (access_version, account_side, business_status, communication_status, currency_code, destination_account_appendix, destination_account_name, destination_account_no, destination_bank_info, fst_communication_status, fst_occupy_status, gateway_router_info, gateway_type, postscript, snd_communication_status, snd_occupy_status, source_account_appendix, source_account_name, source_account_no, source_bank_info, transaction_amount, outlier_transaction_uuid, trd_communication_status, trd_occupy_status, uuid, source_message_time, long_field_one, string_field_one)"
							+ " values (:accessVersion, :accountSide, :businessStatus, :communicationStatus, :currencyCode, :destinationAccountAppendix, :destinationAccountName, :destinationAccountNo, :destinationBankInfo, :occupyCommunicationStatus, :occupyStatus, :gatewayRouterInfo, :gatewayType, :postscript, :occupyCommunicationStatus, :occupyStatus, :sourceAccountAppendix, :sourceAccountName, :sourceAccountNo, :sourceBankInfo, :transactionAmount, :outlierTransactionUuid, :occupyCommunicationStatus, :occupyStatus, :uuid, :sourceMessageTime, :longFieldOne, :stringFieldOne)",
					parms);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
//	@Override
//	public void taskInQueue(List<PaymentOrder> taskList, String tableName, ConsistentHash<String> consistentHashSource) {
//		if (CollectionUtils.isEmpty(taskList)) {
//			return;
//		}
//		Map<String, Object> parms = new HashMap<String, Object>();
//		parms.put("businessStatus", BusinessStatus.Processing.ordinal());
//		parms.put("communicationStatus", CommunicationStatus.Inqueue.ordinal());
//		parms.put("occupyCommunicationStatus",
//				OccupyCommunicationStatus.Ready.ordinal());
//		parms.put("occupyStatus", OccupyStatus.Free.ordinal());
//
//		for (PaymentOrder paymentOrder : taskList) {
//			try {
//				parms.put("accessVersion", paymentOrder.getAccessVersion());
//				parms.put("accountSide", paymentOrder.getAccountSide());
//				parms.put("currencyCode", paymentOrder.getCurrencyCode());
//				parms.put("destinationAccountAppendix", paymentOrder.getDestinationAccountAppendix());
//				parms.put("destinationAccountName",
//						paymentOrder.getDestinationAccountName());
//				parms.put("destinationAccountNo",
//						paymentOrder.getDestinationAccountNo());
//				parms.put("destinationBankInfo", paymentOrder.getDestinationBankInfo());
//				parms.put("gatewayRouterInfo", paymentOrder.getGatewayRouterInfo());
//				parms.put("gatewayType", paymentOrder.getGatewayType());
//				parms.put("postscript", paymentOrder.getPostscript());
//				parms.put("sourceAccountAppendix", paymentOrder.getSourceAccountAppendix());
//				parms.put("sourceAccountName",
//						paymentOrder.getSourceAccountName());
//				parms.put("sourceAccountNo", paymentOrder.getSourceAccountNo());
//				parms.put("sourceBankInfo", paymentOrder.getSourceBankInfo());
//				parms.put("transactionAmount", paymentOrder
//						.getTransactionAmount().toPlainString());
//				parms.put("outlierTransactionUuid",
//						paymentOrder.getOutlierTransactionUuid());
//				parms.put("uuid", paymentOrder.getUuid());
//
//				parms.put("sourceMessageTime", paymentOrder.getSourceMessageTime());
//				
//				genericDaoSupport
//						.executeSQL(
//								"insert into "
//										+ tableName
//										+ " (access_version, account_side, business_status, communication_status, currency_code, destination_account_appendix, destination_account_name, destination_account_no, destination_bank_info, fst_communication_status, fst_occupy_status, gateway_router_info, gateway_type, postscript, snd_communication_status, snd_occupy_status, source_account_appendix, source_account_name, source_account_no, source_bank_info, transaction_amount, outlier_transaction_uuid, trd_communication_status, trd_occupy_status, uuid, source_message_time)"
//										+ " values (:accessVersion, :accountSide, :businessStatus, :communicationStatus, :currencyCode, :destinationAccountAppendix, :destinationAccountName, :destinationAccountNo, :destinationBankInfo, :occupyCommunicationStatus, :occupyStatus, :gatewayRouterInfo, :gatewayType, :postscript, :occupyCommunicationStatus, :occupyStatus, :sourceAccountAppendix, :sourceAccountName, :sourceAccountNo, :sourceBankInfo, :transactionAmount, :outlierTransactionUuid, :occupyCommunicationStatus, :occupyStatus, :uuid, :sourceMessageTime)",
//								parms);
//
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//	}

	@Override
	public List<PaymentOrder> peekIdleTasks(String tableName, int limit, int workerNo) {
		
		Map<String, Object> parms = new HashMap<String, Object>();
		parms.put("businessStatus", BusinessStatus.Processing.ordinal());
		parms.put("inqueue", CommunicationStatus.Inqueue.ordinal());
		parms.put("process", CommunicationStatus.Process.ordinal());
		parms.put("free", OccupyStatus.Free.ordinal());
		parms.put("occupied", OccupyStatus.Occupied.ordinal());
		parms.put("done", OccupyCommunicationStatus.Done.ordinal());
		parms.put("ready", OccupyCommunicationStatus.Ready.ordinal());
		parms.put("limit", limit);
		
		parms.put("workerNo", workerNo);
		
		return genericDaoSupport
				.queryForList(
						"SELECT * FROM "
								+ tableName
								+ " WHERE long_field_one =:workerNo"
								+ " AND business_status =:businessStatus"
								+ " AND ("
								+ "(communication_status =:inqueue AND fst_occupy_status =:free AND fst_communication_status =:ready)"
								+ " OR (communication_status =:process AND fst_occupy_status =:occupied AND fst_communication_status =:done AND snd_occupy_status =:free AND snd_communication_status =:ready)"
								+ " OR (communication_status =:process AND fst_occupy_status =:occupied AND fst_communication_status =:done AND snd_occupy_status =:occupied AND snd_communication_status =:done AND trd_occupy_status =:free AND trd_communication_status =:ready)"
								+ ")" + " ORDER BY id limit :limit", parms,
						PaymentOrder.class);

	}

	
	@Override
	public boolean atomOccupy(int nthSlot, PaymentOrder dstPaymentOrder,
			int tryTimes, String workerUuid, String tableName) {

		String preAccessVersion = getCurrentAccessVersion(dstPaymentOrder,
				tableName);// TODO
		// 可省
		String accessVersion = UUID.randomUUID().toString();

		Map<String, Object> parms = new HashMap<String, Object>();
		parms.put("id", dstPaymentOrder.getId());
		parms.put("businessStatus", BusinessStatus.Processing.ordinal());
		parms.put("inqueue", CommunicationStatus.Inqueue.ordinal());
		parms.put("process", CommunicationStatus.Process.ordinal());
		parms.put("preOccupyStatus", OccupyStatus.Free.ordinal());
		parms.put("preCommunicationStatus",
				OccupyCommunicationStatus.Ready.ordinal());
		parms.put("preAccessVersion", preAccessVersion);
		parms.put("occupyStatus", OccupyStatus.Occupied.ordinal());
		parms.put("communicationStatus",
				OccupyCommunicationStatus.Processing.ordinal());
		parms.put("accessVersion", accessVersion);
		parms.put("formerCommunicationStatus",
				OccupyCommunicationStatus.Done.ordinal());
		parms.put("formerOccupyStatus", OccupyStatus.Occupied.ordinal());
		parms.put("currentTime", new Date());
		parms.put("workerUuid", workerUuid);

		int updatedRows = 0;
		for (int i = 0; i < tryTimes; i++) {// TODO 此循环无效

			updatedRows = executeOccupy(nthSlot, parms, tableName);

			if (1 <= updatedRows) {
				return true;
			}
		}

		return false;
	}

	
	private int executeOccupy(int nthSlot, Map<String, Object> parms,
			String tableName) {
		try {
			switch (nthSlot) {
			case 1:
				genericDaoSupport
						.executeSQL(
								"UPDATE "
										+ tableName
										+ " SET access_version =:accessVersion, fst_occuppied_worker_uuid =:workerUuid, fst_occupy_status =:occupyStatus, fst_communication_status =:communicationStatus, communication_status =:process, fst_occupied_time =:currentTime, communication_start_time =:currentTime WHERE id =:id AND access_version =:preAccessVersion AND business_status =:businessStatus AND communication_status =:inqueue AND fst_occupy_status =:preOccupyStatus AND fst_communication_status =:preCommunicationStatus",
								parms);

				return genericDaoSupport
						.queryForInt(
								"SELECT id FROM  "
										+ tableName
										+ " WHERE id =:id AND access_version =:accessVersion AND fst_occuppied_worker_uuid =:workerUuid AND fst_occupy_status =:occupyStatus AND fst_communication_status =:communicationStatus AND communication_status =:process",
								parms);

			case 2:
				genericDaoSupport
						.executeSQL(
								"UPDATE  "
										+ tableName
										+ " SET access_version =:accessVersion, snd_occuppied_worker_uuid =:workerUuid, snd_occupy_status =:occupyStatus, snd_communication_status =:communicationStatus, snd_occupied_time =:currentTime WHERE id =:id AND access_version =:preAccessVersion AND business_status =:businessStatus AND communication_status =:process AND snd_occupy_status =:preOccupyStatus AND snd_communication_status =:preCommunicationStatus AND fst_occupy_status =:formerOccupyStatus AND fst_communication_status =:formerCommunicationStatus",
								parms);

				return genericDaoSupport
						.queryForInt(
								"SELECT id FROM  "
										+ tableName
										+ " WHERE id =:id AND access_version =:accessVersion AND snd_occuppied_worker_uuid =:workerUuid AND snd_occupy_status =:occupyStatus AND snd_communication_status =:communicationStatus AND communication_status =:process",
								parms);

			case 3:// TODO
				genericDaoSupport
						.executeSQL(
								"UPDATE  "
										+ tableName
										+ " SET access_version =:accessVersion, trd_occuppied_worker_uuid =:workerUuid, trd_occupy_status =:occupyStatus, trd_communication_status =:communicationStatus, trd_occupied_time =:currentTime WHERE id =:id AND access_version =:preAccessVersion AND business_status =:businessStatus AND communication_status =:process AND trd_occupy_status =:preOccupyStatus AND trd_communication_status =:preCommunicationStatus AND snd_occupy_status =:formerOccupyStatus AND snd_communication_status =:formerCommunicationStatus",
								parms);

				return genericDaoSupport
						.queryForInt(
								"SELECT id FROM  "
										+ tableName
										+ " WHERE id =:id AND access_version =:accessVersion AND trd_occuppied_worker_uuid =:workerUuid AND trd_occupy_status =:occupyStatus AND trd_communication_status =:communicationStatus AND communication_status =:process",
								parms);

			default:
				return 0;
			}
		} catch (Exception e) {
			return 0;
		}
	}

	@Override
	public boolean atomSentOut(int nthSlot, PaymentOrder dstPaymentOrder,
			int tryTimes, String tableName) {
		String preAccessVersion = getCurrentAccessVersion(dstPaymentOrder,
				tableName);

		Map<String, Object> parms = new HashMap<String, Object>();
		parms.put("id", dstPaymentOrder.getId());
		parms.put("preAccessVersion", preAccessVersion);
		parms.put("currentTime", new Date());
		parms.put("occupied", OccupyStatus.Occupied.ordinal());
		parms.put("processing", OccupyCommunicationStatus.Processing.ordinal());
		parms.put("sending", OccupyCommunicationStatus.Sending.ordinal());
		parms.put("accessVersion", UUID.randomUUID().toString());
		parms.put("processing", BusinessStatus.Processing.ordinal());
		parms.put("oppositeProcessing",
				BusinessStatus.OppositeProcessing.ordinal());

		int updatedRows = 0;
		for (int i = 0; i < tryTimes; i++) {

			updatedRows = executeSentOutUpdate(nthSlot, parms, tableName);

			if (1 <= updatedRows) {
				return true;
			}
		}

		return false;
	}

	private int executeSentOutUpdate(int nthSlot, Map<String, Object> parms,
			String tableName) {
		try {
			switch (nthSlot) {
			case 1:
				genericDaoSupport
						.executeSQL(
								"UPDATE  "
										+ tableName
										+ " SET access_version =:accessVersion, business_status=:oppositeProcessing, fst_communication_status =:sending, fst_occupied_sent_time =:currentTime, communication_last_sent_time =:currentTime WHERE id =:id AND access_version =:preAccessVersion AND business_status=:processing AND fst_occupy_status =:occupied AND fst_communication_status =:processing",
								parms);

				return genericDaoSupport
						.queryForInt(
								"SELECT id FROM  "
										+ tableName
										+ " WHERE id =:id AND access_version =:accessVersion AND fst_communication_status =:sending",
								parms);

			case 2:
				genericDaoSupport
						.executeSQL(
								"UPDATE  "
										+ tableName
										+ " SET access_version =:accessVersion, snd_communication_status =:sending, snd_occupied_sent_time =:currentTime, communication_last_sent_time =:currentTime WHERE id =:id AND access_version =:preAccessVersion AND snd_occupy_status =:occupied AND snd_communication_status =:processing",
								parms);

				return genericDaoSupport
						.queryForInt(
								"SELECT id FROM  "
										+ tableName
										+ " WHERE id =:id AND access_version =:accessVersion AND snd_communication_status =:sending",
								parms);

			case 3:
				genericDaoSupport
						.executeSQL(
								"UPDATE  "
										+ tableName
										+ " SET access_version =:accessVersion, trd_communication_status =:sending, trd_occupied_sent_time =:currentTime, communication_last_sent_time =:currentTime WHERE id =:id AND access_version =:preAccessVersion AND trd_occupy_status =:occupied AND trd_communication_status =:processing",
								parms);

				return genericDaoSupport
						.queryForInt(
								"SELECT id FROM  "
										+ tableName
										+ " WHERE id =:id AND access_version =:accessVersion AND trd_communication_status =:sending",
								parms);

			default:
				return 0;
			}
		} catch (Exception e) {
			return 0;
		}
	}

	@Override
	public boolean atomFeedBack(int nthSlot, PaymentOrder dstPaymentOrder,
			int tryTimes, String tableName) {
		String preAccessVersion = getCurrentAccessVersion(dstPaymentOrder,
				tableName);

		Map<String, Object> parms = new HashMap<String, Object>();
		parms.put("id", dstPaymentOrder.getId());
		parms.put("preAccessVersion", preAccessVersion);
		parms.put("communicationStatus", CommunicationStatus.Success.ordinal());
		parms.put("currentTime", new Date());
		parms.put("sending", OccupyCommunicationStatus.Sending.ordinal());
		parms.put("done", OccupyCommunicationStatus.Done.ordinal());
		parms.put("accessVersion", UUID.randomUUID().toString());

		int updatedRows = 0;
		for (int i = 0; i < tryTimes; i++) {

			updatedRows = executeFeedBackUpdate(nthSlot, parms, tableName);

			if (1 <= updatedRows) {
				return true;
			}
		}

		return false;
	}

	private int executeFeedBackUpdate(int nthSlot, Map<String, Object> parms,
			String tableName) {
		try {
			switch (nthSlot) {
			case 1:
				genericDaoSupport
						.executeSQL(
								"UPDATE  "
										+ tableName
										+ " SET access_version =:accessVersion, communication_status =:communicationStatus, fst_communication_status =:done, fst_occupied_feed_back_time =:currentTime, communication_end_time =:currentTime WHERE id =:id AND access_version =:preAccessVersion AND fst_communication_status =:sending",
								parms);

				return genericDaoSupport
						.queryForInt(
								"SELECT id FROM  "
										+ tableName
										+ " WHERE id =:id AND access_version =:accessVersion AND communication_status =:communicationStatus AND fst_communication_status =:done",
								parms);

			case 2:
				genericDaoSupport
						.executeSQL(
								"UPDATE  "
										+ tableName
										+ " SET access_version =:accessVersion, communication_status =:communicationStatus, snd_communication_status =:done, snd_occupied_feed_back_time =:currentTime, communication_end_time =:currentTime WHERE id =:id AND access_version =:preAccessVersion AND snd_communication_status =:sending",
								parms);

				return genericDaoSupport
						.queryForInt(
								"SELECT id FROM  "
										+ tableName
										+ " WHERE id =:id AND access_version =:accessVersion AND communication_status =:communicationStatus AND snd_communication_status =:done",
								parms);

			case 3:
				genericDaoSupport
						.executeSQL(
								"UPDATE  "
										+ tableName
										+ " SET access_version =:accessVersion, communication_status =:communicationStatus, trd_communication_status =:done, trd_occupied_feed_back_time =:currentTime, communication_end_time =:currentTime WHERE id =:id AND access_version =:preAccessVersion AND trd_communication_status =:sending",
								parms);

				return genericDaoSupport
						.queryForInt(
								"SELECT id FROM  "
										+ tableName
										+ " WHERE id =:id AND access_version =:accessVersion AND communication_status =:communicationStatus AND trd_communication_status =:done",
								parms);

			default:
				return 0;
			}
		} catch (Exception e) {
			return 0;
		}
	}

	@Override
	public List<PaymentOrder> peekProcessingTasks(String queueTableName) {

		if (StringUtils.isEmpty(queueTableName)) {
			return Collections.EMPTY_LIST;
		}

		Map<String, Object> parms = new HashMap<String, Object>();
		parms.put("businessStatus", BusinessStatus.Processing);
		parms.put("communicationStatus", CommunicationStatus.Process);
		parms.put("processing", OccupyCommunicationStatus.Processing);
		parms.put("occupied", OccupyStatus.Occupied);
		parms.put("done", OccupyCommunicationStatus.Done);

		List<PaymentOrder> oneTableList = genericDaoSupport
				.queryForList(
						"SELECT * FROM "
								+ queueTableName
								+ " WHERE business_status =:businessStatus AND communication_status =:communicationStatus"
								+ " AND communication_start_time IS NOT NULL AND communication_end_time IS NULL"
								+ " AND ("
								+ "(fst_occupy_status =:occupied AND fst_communication_status =:processing)"
								+ " OR (fst_occupy_status =:occupied AND fst_communication_status =:done AND snd_occupy_status =:occupied AND snd_communication_status =:processing)"
								+ " OR (fst_occupy_status =:occupied AND fst_communication_status =:done AND snd_occupy_status =:occupied AND snd_communication_status =:done AND trd_occupy_status =:occupied AND trd_communication_status =:processing)"
								+ ")", parms, PaymentOrder.class);
		return oneTableList;
	}

	@Override
	public boolean atomForceStop(int nthSlot, PaymentOrder dstPaymentOrder,
			int tryTimes, String tableName) {
		
		String preAccessVersion = getCurrentAccessVersion(dstPaymentOrder,
				tableName);

		Map<String, Object> parms = new HashMap<String, Object>();
		parms.put("id", dstPaymentOrder.getId());
		parms.put("preAccessVersion", preAccessVersion);
		parms.put("accessVersion", UUID.randomUUID().toString());
		parms.put("businessStatus", BusinessStatus.Processing.ordinal());
		parms.put("communicationStatus", CommunicationStatus.Process.ordinal());
		parms.put("processing", OccupyCommunicationStatus.Processing.ordinal());
		parms.put("done", OccupyCommunicationStatus.Done.ordinal());
		parms.put("currentTime", new Date());

		int updatedRows = 0;
		for (int i = 0; i < tryTimes; i++) {

			updatedRows = executeForceStopUpdate(nthSlot, parms, tableName);

			if (1 <= updatedRows) {
				return true;
			}
		}

		return false;
	}

	private int executeForceStopUpdate(int nthSlot, Map<String, Object> parms,
			String tableName) {
		try {
			switch (nthSlot) {
			case 1:
				genericDaoSupport
						.executeSQL(
								"UPDATE "
										+ tableName
										+ " SET access_version =:accessVersion, fst_communication_status =:done, fst_occupied_force_stop_time =:currentTime WHERE id =:id AND access_version =:preAccessVersion AND business_status =:businessStatus AND communication_status =:communicationStatus AND fst_communication_status =:processing",
								parms);

				return genericDaoSupport
						.queryForInt(
								"SELECT id FROM"
										+ tableName
										+ " WHERE id =:id AND access_version =:accessVersion AND fst_communication_status =:done",
								parms);

			case 2:
				genericDaoSupport
						.executeSQL(
								"UPDATE "
										+ tableName
										+ " SET access_version =:accessVersion, snd_communication_status =:done, snd_occupied_force_stop_time =:currentTime WHERE id =:id AND access_version =:preAccessVersion AND business_status =:businessStatus AND communication_status =:communicationStatus AND snd_communication_status =:processing",
								parms);

				return genericDaoSupport
						.queryForInt(
								"SELECT id FROM "
										+ tableName
										+ " WHERE id =:id AND access_version =:accessVersion AND snd_communication_status =:done",
								parms);

			case 3:
				genericDaoSupport
						.executeSQL(
								"UPDATE "
										+ tableName
										+ " SET access_version =:accessVersion, trd_communication_status =:done, trd_occupied_force_stop_time =:currentTime WHERE id =:id AND access_version =:preAccessVersion AND business_status =:businessStatus AND communication_status =:communicationStatus AND trd_communication_status =:processing",
								parms);

				return genericDaoSupport
						.queryForInt(
								"SELECT id FROM "
										+ tableName
										+ " WHERE id =:id AND access_version =:accessVersion AND trd_communication_status =:done",
								parms);

			default:
				return 0;
			}
		} catch (Exception e) {
			return 0;
		}
	}

	@Override
	public List<PaymentOrder> getReadyTasksForUpdateBusinessStatus(String queueTableName, int workerNo) {

		if (StringUtils.isEmpty(queueTableName)) {
			return Collections.EMPTY_LIST;
		}

		Map<String, Object> parms = new HashMap<String, Object>();
		parms.put("businessStatus", BusinessStatus.OppositeProcessing.ordinal());
		parms.put("communicationStatus", CommunicationStatus.Success.ordinal());
		
		parms.put("workerNo", workerNo);
		
		List<PaymentOrder> oneTableList = genericDaoSupport
				.queryForList(
						"SELECT * FROM "
								+ queueTableName
								+ " WHERE business_status =:businessStatus AND communication_status =:communicationStatus AND long_field_one =:workerNo",
						parms, PaymentOrder.class);
		return oneTableList;
	}

	@Override
	public List<PaymentOrder> getSendingTimeOutTasks(int timeoutMinutes,
			String queueTableName) {

		if (StringUtils.isEmpty(queueTableName)) {
			return Collections.EMPTY_LIST;
		}

		Map<String, Object> parms = new HashMap<String, Object>();
		parms.put("sending", OccupyCommunicationStatus.Sending.ordinal());
		parms.put("process", CommunicationStatus.Process.ordinal());
		parms.put("oppositeProcessing",
				BusinessStatus.OppositeProcessing.ordinal());
		parms.put("timeoutMinutes", timeoutMinutes);

		List<PaymentOrder> oneTableList = genericDaoSupport
				.queryForList(
						"SELECT * FROM "
								+ queueTableName
								+ " WHERE business_status =:oppositeProcessing AND communication_status =:process"
								+ " AND (fst_communication_status =:sending OR snd_communication_status =:sending OR trd_communication_status =:sending)"
								+ " AND date_add(communication_last_sent_time, INTERVAL :timeoutMinutes MINUTE) < NOW()",
						parms, PaymentOrder.class);
		return oneTableList;
	}

	@Override
	public boolean updateBusinessStatus(PaymentOrder dstPaymentOrder,
			QueryCreditResult queryCreditResult, int tryTimes, String tableName) {

		String preAccessVersion = getCurrentAccessVersion(dstPaymentOrder,
				tableName);

		Map<String, Object> parms = new HashMap<String, Object>();
		parms.put("id", dstPaymentOrder.getId());
		parms.put("preAccessVersion", preAccessVersion);
		parms.put("accessVersion", UUID.randomUUID().toString());
		parms.put("preBusinessStatus", BusinessStatus.OppositeProcessing.ordinal());
		
		if(BusinessProcessStatus.SUCCESS.equals(queryCreditResult.getProcessStatus())) {
			parms.put("businessStatus", BusinessStatus.Success.ordinal());
		}
		if(BusinessProcessStatus.FAIL.equals(queryCreditResult.getProcessStatus())) {
			parms.put("businessStatus", BusinessStatus.Failed.ordinal());
		}
		
		parms.put("businessSuccessTime", queryCreditResult.getBusinessSuccessTime());//TODO
		parms.put("businessResultMsg", queryCreditResult.getBusinessResultMsg());//TODO
		parms.put("channelSequenceNo", queryCreditResult.getChannelSequenceNo());//TODO
		parms.put("success", CommunicationStatus.Success.ordinal());
		parms.put("process", CommunicationStatus.Process.ordinal());
		parms.put("currentTime", new Date());

		int updatedRows = 0;
		for (int i = 0; i < tryTimes; i++) {

			updatedRows = executeBusinessStatusUpdate(parms, tableName);

			if (1 <= updatedRows) {
				return true;
			}
		}

		return false;
	}

	private int executeBusinessStatusUpdate(Map<String, Object> parms,
			String tableName) {
		try {
			genericDaoSupport
					.executeSQL(
							"UPDATE "
									+ tableName
									+ " SET access_version =:accessVersion, business_status =:businessStatus, business_result_msg =:businessResultMsg, business_success_time =:businessSuccessTime, channel_sequence_no =:channelSequenceNo, business_status_last_update_time =:currentTime WHERE id =:id AND access_version =:preAccessVersion AND business_status =:preBusinessStatus AND (communication_status =:success OR communication_status =:process)",
							parms);

			return genericDaoSupport
					.queryForInt(
							"SELECT id FROM "
									+ tableName
									+ " WHERE id =:id AND access_version =:accessVersion AND business_status =:businessStatus",
							parms);

		} catch (Exception e) {
			return 0;
		}
	}

	private String getCurrentAccessVersion(PaymentOrder paymentOrder,
			String tableName) {

		List<String> resultList = genericDaoSupport.queryForSingleColumnList(
				"SELECT access_version FROM " + tableName + " WHERE id =:id",
				"id", paymentOrder.getId(), String.class);

		if (CollectionUtils.isEmpty(resultList)) {
			return StringUtils.EMPTY;
		}
		return resultList.get(0);
	}

	@Override
	public PaymentOrder getPaymentOrderBy(String paymentOrderUuid, String tableName) {
		if(StringUtils.isEmpty(paymentOrderUuid)) {
			return null;
		}
		List<PaymentOrder> list = genericDaoSupport.queryForList("SELECT * FROM " + tableName + " WHERE uuid =:uuid", "uuid", paymentOrderUuid, PaymentOrder.class);
		if(CollectionUtils.isEmpty(list)) {
			return null;
		}
		return list.get(0);
	}

	@Override
	public void transferToPaymentOrderLog(PaymentOrder paymentOrder,
			String queueTableName, String logTableName) {
		try {
			String insertSql = "insert into "
			+ logTableName 
			+ " (`uuid`,`account_side`,`source_account_name`,`source_account_no`,`source_account_appendix`,`source_bank_info`,`destination_account_name`,`destination_account_no`,`destination_bank_info`,`destination_account_appendix`"
			+ ",`transaction_amount`,`currency_code`,`postscript`,`outlier_transaction_uuid`,`gateway_router_info`,`gateway_type`,`source_message_uuid`,`source_message_ip`,`source_message_time`,`business_status`,`channel_sequence_no`"
			+ ",`business_result_msg`,`business_status_last_update_time`,`business_success_time`,`communication_status`,`communication_start_time`,`communication_end_time`,`communication_last_sent_time`"
			+ ",`fst_occupy_status`,`fst_communication_status`,`fst_occupied_time`,`fst_occupied_sent_time`,`fst_occupied_feed_back_time`,`fst_occupied_force_stop_time`,`fst_occuppied_worker_uuid`,`fst_occupied_worker_ip`,`fst_occupied_message_record_uuid`,`fst_occupied_result_record_uuid`"
			+ ",`snd_occupy_status`,`snd_communication_status`,`snd_occupied_time`,`snd_occupied_sent_time`,`snd_occupied_feed_back_time`,`snd_occupied_force_stop_time`,`snd_occuppied_worker_uuid`,`snd_occupied_worker_ip`,`snd_occupied_message_record_uuid`,`snd_occupied_result_record_uuid`"
			+ ",`trd_occupy_status`,`trd_communication_status`,`trd_occupied_time`,`trd_occupied_sent_time`,`trd_occupied_feed_back_time`,`trd_occupied_force_stop_time`,`trd_occuppied_worker_uuid`,`trd_occupied_worker_ip`,`trd_occupied_message_record_uuid`,`trd_occupied_result_record_uuid`"
			+ ",`access_version`,`long_field_one`,`string_field_one`) select"
			+ " `uuid`,`account_side`,`source_account_name`,`source_account_no`,`source_account_appendix`,`source_bank_info`,`destination_account_name`,`destination_account_no`,`destination_bank_info`,`destination_account_appendix`"
			+ ",`transaction_amount`,`currency_code`,`postscript`,`outlier_transaction_uuid`,`gateway_router_info`,`gateway_type`,`source_message_uuid`,`source_message_ip`,`source_message_time`,`business_status`,`channel_sequence_no`"
			+ ",`business_result_msg`,`business_status_last_update_time`,`business_success_time`,`communication_status`,`communication_start_time`,`communication_end_time`,`communication_last_sent_time`"
			+ ",`fst_occupy_status`,`fst_communication_status`,`fst_occupied_time`,`fst_occupied_sent_time`,`fst_occupied_feed_back_time`,`fst_occupied_force_stop_time`,`fst_occuppied_worker_uuid`,`fst_occupied_worker_ip`,`fst_occupied_message_record_uuid`,`fst_occupied_result_record_uuid`"
			+ ",`snd_occupy_status`,`snd_communication_status`,`snd_occupied_time`,`snd_occupied_sent_time`,`snd_occupied_feed_back_time`,`snd_occupied_force_stop_time`,`snd_occuppied_worker_uuid`,`snd_occupied_worker_ip`,`snd_occupied_message_record_uuid`,`snd_occupied_result_record_uuid`"
			+ ",`trd_occupy_status`,`trd_communication_status`,`trd_occupied_time`,`trd_occupied_sent_time`,`trd_occupied_feed_back_time`,`trd_occupied_force_stop_time`,`trd_occuppied_worker_uuid`,`trd_occupied_worker_ip`,`trd_occupied_message_record_uuid`,`trd_occupied_result_record_uuid`"
			+ ",`access_version`,`long_field_one`,`string_field_one` from "
			+ queueTableName + " where uuid =:uuid";
			
			genericDaoSupport.executeSQL(insertSql, "uuid", paymentOrder.getUuid());
			
			int recordCount = genericDaoSupport.queryForInt("select id from " + logTableName + " where uuid =:uuid", "uuid", paymentOrder.getUuid());
			
			if(recordCount > 0) {
				genericDaoSupport.executeSQL("delete from " + queueTableName + " where uuid =:uuid", "uuid", paymentOrder.getUuid());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
						
	}
	
}
