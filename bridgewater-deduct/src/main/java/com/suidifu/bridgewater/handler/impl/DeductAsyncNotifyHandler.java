package com.suidifu.bridgewater.handler.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.demo2do.core.entity.Result;
import com.demo2do.core.persistence.GenericDaoSupport;
import com.demo2do.core.utils.JsonUtils;
import com.suidifu.bridgewater.handler.DeductApplicationDetailHandler;
import com.suidifu.bridgewater.handler.IDeductAsyncNotifyHandler;
import com.zufangbao.gluon.logspec.GloableLogSpec;
import com.zufangbao.gluon.logspec.GloableLogSpec.bridgewater_deduct_function_point;
import com.zufangbao.gluon.opensdk.HttpClientUtils;
import com.zufangbao.sun.api.model.deduct.DeductApplicationExecutionStatus;
import com.zufangbao.sun.yunxin.api.deduct.service.DeductApplicationService;
import com.zufangbao.sun.yunxin.api.deduct.service.DeductRequestLogService;
import com.zufangbao.sun.yunxin.entity.api.deduct.DeductApplication;

public abstract class DeductAsyncNotifyHandler implements IDeductAsyncNotifyHandler {
	
	private static final Log logger = LogFactory.getLog(DeductAsyncNotifyHandler.class);
	
	@Autowired
	DeductApplicationDetailHandler deductAppDetailHandler;

	@Autowired
	DeductRequestLogService deductRequestLogService;

	@Autowired
	private DeductApplicationService deductApplicationService;
	
	@Autowired
	private GenericDaoSupport genericDaoSupport;
	
	public abstract  Map<String, String> buildHeaderParamsForNotifyDeductResult(
			String content,String financialContractUuid );
	
	public abstract String genContextNotifyModel(DeductApplication deductApplication);
	
	@Override
	public void processingdeductCallback(String deductApplicationUuid) {
		String oppositeKeyWord = "[deductApplicationUuid=" + deductApplicationUuid + "]";
		DeductApplication deductApplication = deductApplicationService.getDeductApplicationByDeductApplicationUuid(deductApplicationUuid);

		if (deductApplication == null || StringUtils.isEmpty(deductApplication.getNotifyUrl()) ||
				StringUtils.isNotEmpty(deductApplication.getBatchDeductApplicationUuid()) ||
				!deductApplication.isSuccessOrFailedOrAbandonOrpartSuccess() ||
				deductApplication.getActualNotifyNumber() >= deductApplication.getPlanNotifyNumber()) {
			return;
		}

		String notifyUrl = deductApplication.getNotifyUrl();
		String content = genContextNotifyModel(deductApplication);

		int actualNotifyNumber = deductApplication.getActualNotifyNumber();

		int planNotifyNumber = deductApplication.getPlanNotifyNumber();

		Map<String, String> headerParams = buildHeaderParamsForNotifyDeductResult(content, deductApplication.getFinancialContractUuid());
		logger.info(".#DeductApplicationBusinessHandlerImpl回调参数header：" + headerParams);

		logger.info(
				GloableLogSpec.AuditLogHeaderSpec() + bridgewater_deduct_function_point.SEND_RESULT_TO_OUTLIER_SYSTEM
						+ oppositeKeyWord + "begin to send post");
		Result result = HttpClientUtils.executePostRequest(notifyUrl, content, headerParams);

		String responseStr = String.valueOf(result.get(HttpClientUtils.DATA_RESPONSE_PACKET));
		if (result.isValid()) {
			Map<String, Object> respMap = JsonUtils.parse(responseStr);

			logger.info(GloableLogSpec.AuditLogHeaderSpec() +bridgewater_deduct_function_point.SEND_RESULT_TO_OUTLIER_SYSTEM + oppositeKeyWord+ GloableLogSpec.RawData("当前plan_notify_number:["+ planNotifyNumber +"]"));
			logger.info(GloableLogSpec.AuditLogHeaderSpec() +bridgewater_deduct_function_point.SEND_RESULT_TO_OUTLIER_SYSTEM + oppositeKeyWord+ GloableLogSpec.RawData("自增后actual_notify_number:["+ (actualNotifyNumber + 1) +"]操作"));

			if (MapUtils.isEmpty(respMap)) {
				return;
			}
			Map<String, Object> statusMap;
			Boolean isSuccess = false;
			String responseCode = "";
			try {
				// TODO : 将接口方的参数转换成本地化. Status,IsSuccess, ResponseCode,0000
				statusMap = (Map<String, Object>) respMap.get("status");
				if (MapUtils.isEmpty(statusMap)) {
					logger.info(GloableLogSpec.AuditLogHeaderSpec()
							+ bridgewater_deduct_function_point.SEND_RESULT_TO_OUTLIER_SYSTEM + oppositeKeyWord
							+ "处理获取status时为空");
					return;
				}
				isSuccess = (Boolean) statusMap.get("isSuccess");
				responseCode = String.valueOf(statusMap.get("responseCode"));
				// 返回为true并且ResponseCode为0000的时候,表示为成功
				if (isSuccess && StringUtils.equals("0000", responseCode)) {
					countNotifyNumber(deductApplicationUuid);
					logger.info(GloableLogSpec.AuditLogHeaderSpec()
							+ bridgewater_deduct_function_point.SEND_RESULT_TO_OUTLIER_SYSTEM + oppositeKeyWord
							+ GloableLogSpec.RawData(
							"receive notify result success 内容［" + content + "］，结果［" + responseStr + "］"));
				}else {
					logger.info(GloableLogSpec.AuditLogHeaderSpec()
							+ bridgewater_deduct_function_point.SEND_RESULT_TO_OUTLIER_SYSTEM + oppositeKeyWord
							+ GloableLogSpec.RawData(
							"receive notify result fail 内容［" + content + "］，结果［" + responseStr + "］"));

				}
				return;
			} catch (Exception e) {
				e.printStackTrace();
				logger.error(GloableLogSpec.AuditLogHeaderSpec() + bridgewater_deduct_function_point.SEND_RESULT_TO_OUTLIER_SYSTEM
						+ oppositeKeyWord + ",msg:" + ExceptionUtils.getFullStackTrace(e));

			}

		}
		logger.info(GloableLogSpec.AuditLogHeaderSpec()
				+ bridgewater_deduct_function_point.SEND_RESULT_TO_OUTLIER_SYSTEM + oppositeKeyWord+"#对端响应失败！");
	}
	
	
	@Override
	public List<String> getWaitingNoticedeductApplicationBy(int pageSize, Date queryStartDate) {
		String queryUuidsSql = "SELECT deduct_application_uuid "
				+ "FROM t_deduct_application "
				+ "WHERE execution_status IN(:executionStatusList)"
				+ " AND create_time >=:queryStartDate"
				+ " AND actual_notify_number < plan_notify_number "
				+ " AND notify_url IS NOT NULL AND (batch_deduct_application_uuid is NULL or batch_deduct_application_uuid ='') "
				+ " LIMIT :pageSize";

		Map<String, Object> params = new HashMap<String, Object>();
		List<Integer> finishedExecutionStatusList = new ArrayList<Integer>();
		finishedExecutionStatusList.add(DeductApplicationExecutionStatus.ABANDON.ordinal());
		finishedExecutionStatusList.add(DeductApplicationExecutionStatus.SUCCESS.ordinal());
		finishedExecutionStatusList.add(DeductApplicationExecutionStatus.FAIL.ordinal());
		
		//还款订单模式：PROCESSING+PART_OF_SUCCESS
		//finishedExecutionStatusList.add(DeductApplicationExecutionStatus.PROCESSING.ordinal());
		finishedExecutionStatusList.add(DeductApplicationExecutionStatus.PART_OF_SUCCESS.ordinal());
		params.put("queryStartDate", queryStartDate);
		params.put("executionStatusList", finishedExecutionStatusList);
		params.put("pageSize", pageSize);
		return this.genericDaoSupport.queryForSingleColumnList(queryUuidsSql, params, String.class);
	}

	
	private void countNotifyNumber(String deductApplicationUuid){
		if (StringUtils.isEmpty(deductApplicationUuid)){
			return;
		}
		Map<String, Object> parms = new HashMap<String, Object>();
		try {
			String preAccessVersion = getCurrentAccessVersion(deductApplicationUuid);
			parms.put("preAccessVersion", preAccessVersion);
			parms.put("accessVersion", UUID.randomUUID().toString());
			parms.put("deductApplicationUuid", deductApplicationUuid);

			String executeSql = "update t_deduct_application set version = :accessVersion, actual_notify_number = actual_notify_number+1, last_modified_time = NOW() where deduct_application_uuid =:deductApplicationUuid and version =:preAccessVersion";
			genericDaoSupport.executeSQL(executeSql, parms);

			String validateSql = "select id from t_deduct_application where deduct_application_uuid =:deductApplicationUuid and version =:accessVersion";
			int updatedRows = genericDaoSupport.queryForInt(validateSql, parms);

			if (0 >= updatedRows) {
				logger.info(GloableLogSpec.AuditLogHeaderSpec()  + "countNotifyNumber nothing DeductApplication update and deductApplicationUuid:"+deductApplicationUuid);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
private String getCurrentAccessVersion(String deductApplicationUuid) {
		
		List<String> resultList = genericDaoSupport.queryForSingleColumnList("SELECT version FROM t_deduct_application WHERE deduct_application_uuid =:deductApplicationUuid",
				"deductApplicationUuid", deductApplicationUuid, String.class);

		if (CollectionUtils.isEmpty(resultList)) {
			return StringUtils.EMPTY;
		}
		return resultList.get(0);
		
	}

}
