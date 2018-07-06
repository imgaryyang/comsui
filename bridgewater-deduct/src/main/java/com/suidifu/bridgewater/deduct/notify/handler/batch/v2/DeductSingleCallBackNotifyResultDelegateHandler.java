package com.suidifu.bridgewater.deduct.notify.handler.batch.v2;

import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.demo2do.core.persistence.GenericDaoSupport;
import com.demo2do.core.utils.JsonUtils;
import com.suidifu.bridgewater.handler.single.v2.ZhongHangDeductApplicationBusinessHandler;
import com.suidifu.swift.notifyserver.notifyserver.NotifyJob;
import com.suidifu.swift.notifyserver.notifyserver.NotifyResultDelegateHandler;
import com.suidifu.swift.notifyserver.notifyserver.NotifyResultListener;
import com.zufangbao.gluon.logspec.GloableLogSpec;
import com.zufangbao.gluon.logspec.GloableLogSpec.bridgewater_deduct_function_point;
import com.zufangbao.gluon.opensdk.HttpClientUtils;


/**
 * 
 * @author zhanglonfei
 *
 */
@Component("deductSingleCallBackNotifyResultDelegateHandler")
public class DeductSingleCallBackNotifyResultDelegateHandler implements NotifyResultDelegateHandler {

	private static final Log logger = LogFactory.getLog(DeductSingleCallBackNotifyResultDelegateHandler.class);
	
	 @Autowired
	 private ZhongHangDeductApplicationBusinessHandler zhongHangDeductApplicationBusinessHandler;
	 
	 @Autowired
	 private GenericDaoSupport genericDaoSupport;
	 
	
	@Override
	@Deprecated
	public void onResult(NotifyJob result) {
		
		String deductApplicationUuid = result.getBusinessId();
		
		if(!NotifyJob.RESPONSE_CODE_200_OK.equals(result.getLastTimeHttpResponseCode())){
		
			logger.error(".#DeductSingleCallBackNotifyResultListener Post occur error  deductApplicationUuid ["+deductApplicationUuid+"]");
			
			return;
			
		}
		
		String oppositeKeyWord = "[deductApplicationUuid=" + deductApplicationUuid + "]";
		
		String content = result.getRequestParamJson();
		
		Map<String, String> workParams = JSON.parseObject(result.getRedundanceMap(),Map.class);
		
		int actualNotifyNumber = Integer.valueOf(workParams.get("actualNotifyNumber"));
		
		int planNotifyNumber = Integer.valueOf(workParams.get("planNotifyNumber"));
		
		try {
			
		Map<String, Object> returnInfo = JSON.parseObject(result.getResponseJson(), Map.class);
		
		String responseStr = String.valueOf(returnInfo.get(HttpClientUtils.DATA_RESPONSE_PACKET));
		
		Map<String, Object> respMap = JsonUtils.parse(responseStr);
		
		if (MapUtils.isEmpty(respMap)) {
			
				zhongHangDeductApplicationBusinessHandler.updateDeductExecuteIfFailed(actualNotifyNumber, planNotifyNumber, oppositeKeyWord,deductApplicationUuid);
				
				return;
			}
			Map<String, Object> statusMap;
			Boolean isSuccess = false;
			String responseCode = "";
				// todo : 将接口方的参数转换成本地化. Status,IsSuccess, ResponseCode,0000
				statusMap = (Map<String, Object>) respMap.get("status");
				if (MapUtils.isEmpty(statusMap)) {
					logger.info(GloableLogSpec.AuditLogHeaderSpec()
							+ bridgewater_deduct_function_point.SEND_RESULT_TO_OUTLIER_SYSTEM + oppositeKeyWord
							+ "处理获取Status时为空");
					zhongHangDeductApplicationBusinessHandler.updateDeductExecuteIfFailed(actualNotifyNumber, planNotifyNumber, oppositeKeyWord,
							deductApplicationUuid);
					return;
				}
				isSuccess = (Boolean) statusMap.get("isSuccess");
				responseCode = String.valueOf(statusMap.get("responseCode"));
				// 返回为true并且ResponseCode为0000的时候,表示为成功
				if (isSuccess && StringUtils.equals("0000", responseCode)) {
					
					logger.info(GloableLogSpec.AuditLogHeaderSpec()
							+ bridgewater_deduct_function_point.SEND_RESULT_TO_OUTLIER_SYSTEM + oppositeKeyWord
							+ GloableLogSpec.RawData("receive notify result success 内容［" + content + "］，结果［" + responseStr + "］"));
					
					this.genericDaoSupport.executeSQL(
							"UPDATE t_deduct_application " + "SET actual_notify_number = (actual_notify_number + 1) "
									+ "WHERE deduct_application_uuid =:deductApplicationUuid",
							"deductApplicationUuid", deductApplicationUuid);
					
					return;
				}
				zhongHangDeductApplicationBusinessHandler.updateDeductExecuteIfFailed(actualNotifyNumber, planNotifyNumber, oppositeKeyWord,
						deductApplicationUuid);
		} catch (Exception e) {

			zhongHangDeductApplicationBusinessHandler.updateDeductExecuteIfFailed(actualNotifyNumber, planNotifyNumber, oppositeKeyWord,
					deductApplicationUuid);
			e.printStackTrace();
			
			logger.error(".#DeductSingleCallBackNotifyResultListener occur error ErrMsg ["+e.getMessage()+"] deductApplicationUuid ["+deductApplicationUuid+"]");
			
		}
	}

}
