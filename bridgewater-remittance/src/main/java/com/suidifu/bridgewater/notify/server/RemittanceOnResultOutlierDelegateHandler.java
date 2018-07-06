package com.suidifu.bridgewater.notify.server;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.demo2do.core.utils.JsonUtils;
import com.suidifu.bridgewater.handler.IRemittanceApplicationHandler;
import com.suidifu.bridgewater.handler.RemittanceNotifyHandler;
import com.suidifu.swift.notifyserver.notifyserver.NotifyJob;
import com.suidifu.swift.notifyserver.notifyserver.NotifyResultDelegateHandler;
import com.zufangbao.gluon.logspec.GloableLogSpec;
import com.zufangbao.gluon.logspec.GloableLogSpec.bridgewater_remittance_function_point;
import com.zufangbao.sun.api.model.remittance.RemittanceProjectName;
import com.zufangbao.sun.api.model.remittance.RemittanceSqlModel;
import com.zufangbao.sun.yunxin.entity.model.SystemTraceLog;
import com.zufangbao.sun.yunxin.entity.remittance.NotifyStatus;
import com.zufangbao.sun.yunxin.service.remittance.IRemittanceApplicationService;
import com.zufangbao.sun.zhonghang.v2.ZhonghangResponseMapSpec.EVENT_NAME;
import com.zufangbao.sun.zhonghang.v2.ZhonghangResponseMapSpec.SYSTEM_NAME;
/**
 * 接受外部返回结果
 * @author wsh
 *
 */
@Component("remittanceOnResultOutlierDelegateNoSession")
public class RemittanceOnResultOutlierDelegateHandler implements NotifyResultDelegateHandler{
	private static final Log logger = LogFactory.getLog(RemittanceNotifyResultListener.class);
	
	@Autowired
	private IRemittanceApplicationService iRemittanceApplicationService;
	
	@Autowired
	private RemittanceNotifyHandler remittanceNotifyHandler;
	
	@Autowired
	private IRemittanceApplicationHandler iRemittanceApplicationHandler;
	
	@Value("#{config['notifyserver.groupCacheJobQueueMap_group2']}")
	private String groupNameForRetryPushToOutlier;
	
	@Override
	public void onResult(NotifyJob result) {
		String STATUS = "Status";
		String IS_SUCCESS = "IsSuccess";
		String RESPONSE_CODE = "ResponseCode";
		if (remittanceNotifyHandler.getRemittanceProjectName() == RemittanceProjectName.ZHONG_HANG) {
			 STATUS = "status";
			 IS_SUCCESS = "isSuccess";
			 RESPONSE_CODE = "responseCode";
		}
		//TODO log
		String redundanceMap= result.getRedundanceMap();
		HashMap<String,String> workingParams = JSON.parseObject(redundanceMap, new TypeReference<HashMap<String,String> >(){});
		String remittanceApplicationUuid = workingParams.get("remittanceApplicationUuid");
		
		
		String responseJson = result.getResponseJson();
		
		SystemTraceLog systemTraceLog = new SystemTraceLog(EVENT_NAME.REMITTANCE_ON_RESULT_OUTLIER_START,"checkRequestNo:[]"+
				"&applicationUuid:"+remittanceApplicationUuid,"responseJson:"+responseJson,null,SystemTraceLog.INFO,null,
				SYSTEM_NAME.OUTLIER_SYSTEM,SYSTEM_NAME.REMITTANCE_SYSTEM);
		logger.info(systemTraceLog);
		
		if (!NotifyJob.RESPONSE_CODE_200_OK.equals(result.getLastTimeHttpResponseCode())) {
			  systemTraceLog.setEventName(EVENT_NAME.REMITTANCE_ON_RESULT_OUTLIER_EXCEPTION);
			  systemTraceLog.error("httpResponse Code != 200");
			  logger.info(systemTraceLog);
            return;
        }
		
		String responseString = JsonUtils.parse(responseJson,String.class);
		
		Map<String, Object> respMap = JsonUtils.parse(responseString);
		
		if(MapUtils.isEmpty(respMap)) {
			iRemittanceApplicationHandler.updateRemittanceExecuteIfFailed(remittanceApplicationUuid,false);
			return;
		}
		Map<String, Object> statusMap;
		Boolean isSuccess =  false;
		String responseCode = "";
		try{
			//todo :  将接口方的参数转换成本地化. Status,IsSuccess, ResponseCode,0000
			statusMap = (Map<String, Object>) respMap.get(STATUS);
			if(MapUtils.isEmpty(statusMap)){
				logger.info(GloableLogSpec.AuditLogHeaderSpec() + bridgewater_remittance_function_point.SEND_RESULT_TO_OUTLIER_SYSTEM + "处理获取Status时为空");
				iRemittanceApplicationHandler.updateRemittanceExecuteIfFailed(remittanceApplicationUuid, false);
				
				systemTraceLog.setEventName(EVENT_NAME.REMITTANCE_ON_RESULT_OUTLIER_EXCEPTION_STATUS_IS_EMPTY);
				systemTraceLog.setErrorMsg("statusMapIsEmpty");
				logger.info(systemTraceLog);
				return;
			}
			isSuccess = (Boolean) statusMap.get(IS_SUCCESS);
			responseCode = String.valueOf(statusMap.get(RESPONSE_CODE));
			//返回为true并且ResponseCode为0000的时候,表示为成功
			if(isSuccess && StringUtils.equals("0000", responseCode)) {
				iRemittanceApplicationHandler.updateRemittanceExecuteIfSuccess(remittanceApplicationUuid);
				systemTraceLog.setEventName(EVENT_NAME.REMITTANCE_ON_RESULT_OUTLIER_END_BUSINESS_SUCCESS);
				logger.info(systemTraceLog);
			}else{
				
				RemittanceSqlModel remittanceSqlMode = iRemittanceApplicationService.getRemittanceSqlModelBy(remittanceApplicationUuid);
				
				if(remittanceSqlMode.getNotifyStatus() == NotifyStatus.FIRST_PUSH_TO_OUTLIER.ordinal()) {
					iRemittanceApplicationHandler.updateRemittanceExecuteIfFailed(remittanceApplicationUuid, true);
					remittanceNotifyHandler.pushApplicationsToOutlier(remittanceSqlMode,groupNameForRetryPushToOutlier);

					systemTraceLog.setEventName(EVENT_NAME.REMITTANCE_ON_RESULT_OUTLIER_BUSINESS_EXCEPTION);
					systemTraceLog.setErrorMsg("pushNotifyJobInfoRetryGroup");
					logger.info(systemTraceLog);
					
				}else {
					iRemittanceApplicationHandler.updateRemittanceExecuteIfFailed(remittanceApplicationUuid, false);
				}
				
				systemTraceLog.setEventName(EVENT_NAME.REMITTANCE_ON_RESULT_OUTLIER_BUSINESS_EXCEPTION);
				systemTraceLog.setErrorMsg("外部处理remittanceApplication回调失败");
				logger.info(systemTraceLog);
				
			}
			
			
		} catch (Exception e){
			systemTraceLog.setEventName(EVENT_NAME.REMITTANCE_ON_RESULT_OUTLIER_EXCEPTION);
			systemTraceLog.setEventLevel(SystemTraceLog.ERROR);
			systemTraceLog.setErrorMsg(e.getMessage());
			logger.info(systemTraceLog);
			iRemittanceApplicationHandler.updateRemittanceExecuteIfFailed(remittanceApplicationUuid, false);
		}
	}
}
