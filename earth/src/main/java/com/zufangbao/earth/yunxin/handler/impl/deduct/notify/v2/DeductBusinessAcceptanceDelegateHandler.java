package com.zufangbao.earth.yunxin.handler.impl.deduct.notify.v2;

import com.demo2do.core.utils.JsonUtils;
import com.suidifu.swift.notifyserver.notifyserver.NotifyJob;
import com.suidifu.swift.notifyserver.notifyserver.NotifyResultDelegateHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

/**
 * @author wukai
 *
 */
/**
 * 扣款和签约的监听器
 * @author wukai
 *
 */
@Component("deductBusinessAcceptanceDelegateHandler")
public class DeductBusinessAcceptanceDelegateHandler implements NotifyResultDelegateHandler {

	private static final Log logger = LogFactory.getLog(DeductBusinessAcceptanceDelegateHandler.class);
	
	@Override
	public void onResult(NotifyJob result) {
		
		logger.info("#DeductBusinessAcceptanceDelegateHandler#result["+JsonUtils.toJsonString(result)+"]");
		
		
	}
}
