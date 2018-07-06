package com.suidifu.citigroup.listeners;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.suidifu.swift.notifyserver.notifyserver.NotifyJob;
import com.suidifu.swift.notifyserver.notifyserver.NotifyResultDelegateHandler;
import com.zufangbao.sun.yunxin.entity.model.SystemTraceLog;
import com.zufangbao.sun.zhonghang.v2.ZhonghangResponseMapSpec.EVENT_NAME;
import com.zufangbao.sun.zhonghang.v2.ZhonghangResponseMapSpec.SYSTEM_NAME;

@Component("remittanceNotifyResultDelegateHandler")
public class RemittanceNotifyResultDelegateHandler implements NotifyResultDelegateHandler {
private static final Log logger = LogFactory.getLog(RemittanceNotifyResultDelegateHandler.class);
	
	@Override
	public void onResult(NotifyJob result) {
		
		SystemTraceLog	systemTraceLog =null;
		
		try {
			if(!NotifyJob.RESPONSE_CODE_200_OK.equals(result.getLastTimeHttpResponseCode())) {
				
				systemTraceLog = new SystemTraceLog(EVENT_NAME.MORGANSTANLEY_NORIFY_REMITTANCE_RESULT,
						result.getBusinessId(), "requestparams:" + result.getRequestParamJson(), null,
						SystemTraceLog.INFO, null, SYSTEM_NAME.REMITTANCE_SYSTEM, SYSTEM_NAME.CITIGROUP);
				
				logger.info(systemTraceLog.toString());
					
			}
		} catch (Exception e) {

			e.printStackTrace();
			
			systemTraceLog = new SystemTraceLog(EVENT_NAME.MORGANSTANLEY_NORIFY_REMITTANCE_RESULT,
					result.getBusinessId(), "requestparams:" + result.getRequestParamJson(), e.getMessage(),
					SystemTraceLog.ERROR, null, SYSTEM_NAME.REMITTANCE_SYSTEM, SYSTEM_NAME.CITIGROUP);
			
			logger.error(systemTraceLog.toString());
			
		}
		
	}

}
