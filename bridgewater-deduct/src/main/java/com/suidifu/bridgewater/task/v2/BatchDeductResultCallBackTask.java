package com.suidifu.bridgewater.task.v2;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.suidifu.bridgewater.handler.batch.v2.ZhongHangBatchDeductApplicationBusinessHandler;
import com.suidifu.bridgewater.model.v2.BasicTask;

@Component("batchDeductResultCallBackTask")
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class BatchDeductResultCallBackTask extends BasicTask{

	@Autowired
	private ZhongHangBatchDeductApplicationBusinessHandler zhongHangBatchDeductApplicationBusinessHandler;
	
	private static Log logger = LogFactory.getLog(BatchDeductResultCallBackTask.class);
	
	/**
	 * 执行扣款结果回调
	 */

	public void run() {
		
	long startTime = System.currentTimeMillis();
		
		logger.info("#BatchDeductResultCallBackTask# begin to execBatchDeductResultQueryNotify");
		
		zhongHangBatchDeductApplicationBusinessHandler.execBatchNotifyForDeductApplication();
		
		logger.info("#BatchDeductResultCallBackTask# end to execBatchDeductResultQueryNotify,consume time["+(System.currentTimeMillis()-startTime)+"]ms");
		
	}
	
}