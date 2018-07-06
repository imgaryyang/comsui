/**
 * 
 */
package com.suidifu.bridgewater.deduct.notify.handler.batch.v2;

import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.demo2do.core.utils.JsonUtils;
import com.suidifu.bridgewater.api.entity.deduct.batch.v2.BatchDeductApplication;
import com.suidifu.bridgewater.api.entity.deduct.batch.v2.DeductPlanApplicationCheckLog;
import com.suidifu.bridgewater.api.entity.deduct.batch.v2.ProcessDeductResultFailType;
import com.suidifu.bridgewater.api.service.batch.v2.BatchDeductApplicationService;
import com.suidifu.bridgewater.api.service.batch.v2.DeductPlanApplicationCheckLogService;
import com.suidifu.bridgewater.api.util.BatchDeductItemHelper;
import com.suidifu.bridgewater.api.util.DeductPlanApplicationCheckLogHelper;
import com.suidifu.bridgewater.model.v2.BatchDeductItem;
import com.suidifu.swift.notifyserver.notifyserver.NotifyJob;
import com.suidifu.swift.notifyserver.notifyserver.NotifyResultDelegateHandler;
import com.zufangbao.gluon.api.earth.v3.model.ApiResult;
import com.zufangbao.gluon.spec.earth.v3.ApiResponseCode;

/**
 * @author wukai
 *
 */
@Component("batchNotifyResultDelegateHandler")
public class BatchNotifyResultDelegateHandler implements NotifyResultDelegateHandler {
	
	private static final Log logger = LogFactory.getLog(DeductNotifyResultListener.class);
	
	@Autowired
	private DeductPlanApplicationCheckLogService deductPlanApplicationCheckLogService;
	
	@Autowired
	private BatchDeductApplicationService batchDeductApplicationService;

	@Override
	public void onResult(NotifyJob result) {
	
		String logUuid = UUID.randomUUID().toString();
		
		logger.info("#NotifyResultListener# begin to parse result parameters,logUuid["+logUuid+"],request param ["+result.getRequestParamJson()+"],serverIdentity["+result.getServerIdentity()+"],"
				+ ",redundanceMap["+result.getRedundanceMap()+"]");
		
		boolean occurError = false;
		
		String deductId = result.getBusinessId();
		
		Map<String,String> requestParameters = JSON.parseObject(result.getRequestParamJson(),Map.class);
		
		BatchDeductItem batchDeductItem = BatchDeductItemHelper.buildBatchDeductItem(requestParameters);
		
		String repsonse = result.getResponseJson();
		
		Map<String, String> workParams = JSON.parseObject(result.getRedundanceMap(),Map.class);
		
		String batchDeductApplicationUuid = workParams.get("batchDeductApplicationUuid");
		
		BatchDeductApplication batchDeductApplication = batchDeductApplicationService.getBatchDeductApplicationBy(batchDeductApplicationUuid);
		
		String failReason = "";
		
		ProcessDeductResultFailType processDeductResultFailType = null;
	
		try{
			
			logger.info("#NotifyResultListener# begin to check http status with deductId["+batchDeductItem.getDeductId()+"],logUuid["+logUuid+"]");
			
			//http非200就是发生了错误
			if(!NotifyJob.RESPONSE_CODE_200_OK.equals(result.getLastTimeHttpResponseCode())){
				
				occurError = true;
				
				failReason = result.getResponseJson()+",logUuid["+logUuid+"]";
				
				processDeductResultFailType = ProcessDeductResultFailType.HTTP_FAIL;
				
			}else{
				
				String apiResultJson = JSON.parseObject(repsonse,String.class);
				
				ApiResult apiResult = JSON.parseObject(apiResultJson,ApiResult.class);
				logger.info("batchNotifyResultDelegateHandlerupdate["+JsonUtils.toJsonString(apiResult)+"]");
		
				if(ApiResponseCode.SUCCESS != apiResult.getCode()){
					
					
					logger.info("batchNotifyResultDelegateHandlerupdate Code["+apiResult.getCode()+"]");
					occurError = true;
					
					failReason = apiResult.getMessage()+",logUuid["+logUuid+"]";
					
					processDeductResultFailType = ProcessDeductResultFailType.VALIDATE_FAIL;
				}
			}
		
			logger.info("#NotifyResultListener# end to onResult,deductId["+batchDeductItem.getDeductId()+"],logUuid["+logUuid+"]");
			
		}catch(Exception e){
			
			occurError=true;
			
			failReason = e.getMessage(); 
			
			processDeductResultFailType = ProcessDeductResultFailType.HTTP_FAIL;
			
			logger.error("#DeductNotifyResultListener# occur exception with stack trace["+ExceptionUtils.getFullStackTrace(e)+"],with result["+JsonUtils.toJsonString(result)+"],logUuid["+logUuid+"]");
		
		}finally{
			
			if(occurError){
				
				DeductPlanApplicationCheckLog deductPlanApplicationCheckLog = DeductPlanApplicationCheckLogHelper.buildErrorDeductPlanApplicationCheckLog(batchDeductApplication, batchDeductItem, failReason, processDeductResultFailType);
				
				deductPlanApplicationCheckLogService.save(deductPlanApplicationCheckLog);
				
				batchDeductApplication.setActualCount(batchDeductApplication.getActualCount()+1);
				batchDeductApplication.setFailCount(batchDeductApplication.getFailCount()+1);
				
				logger.info("batchNotifyResultDelegateHandlerupdate actualCount["+batchDeductApplication.getActualCount()+1+"]"+"failCount["+batchDeductApplication.getFailCount()+1+"]");
				
				batchDeductApplicationService.update(batchDeductApplication);
				
				
			}
		}
	
	}
	
}
