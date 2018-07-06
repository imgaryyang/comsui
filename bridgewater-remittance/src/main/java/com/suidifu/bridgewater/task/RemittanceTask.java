package com.suidifu.bridgewater.task;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.demo2do.core.utils.JsonUtils;
import com.suidifu.bridgewater.api.model.BasicTask;
import com.suidifu.bridgewater.handler.IRemittanceApplicationHandler;
import com.suidifu.bridgewater.handler.RemittanceTaskNoSession;
import com.zufangbao.sun.utils.DateUtils;

@Component("remittanceTask")
public class RemittanceTask extends BasicTask{
	
	private static Log logger = LogFactory.getLog(RemittanceTask.class);
	
	@Autowired
	private IRemittanceApplicationHandler iRemittanceApplicationHandler;
	
	@Autowired
	private RemittanceTaskNoSession remittanceTaskNoSession;
	
	@Override
	public void run() {
		long start = System.currentTimeMillis();

		String noticeBatchNo = "noticeBatchNo:["+UUID.randomUUID().toString()+"]";
		logger.info("#execRemittanceResultQuery begin." + noticeBatchNo);
		
		int limit = getIntegetFromWorkParamOrDefault(RemittanceTaskSpec.PARAM_LIMIT_SIZE, RemittanceTaskSpec.DEFAULT_LIMIT_SIZE);
		int queryStatusDelayInteger = getIntegetFromWorkParamOrDefault(RemittanceTaskSpec.PARAM_QUERY_STATUS_DELAY,RemittanceTaskSpec.DEFAULT_QUERY_STATUS_DELAY);
		
		String currentTime = DateUtils.format(new Date(), DateUtils.LONG_DATE_FORMAT);
		List<String> remittanceApplicationUuids=null;
		try {
			Date queryStartDate = DateUtils.addDays(DateUtils.getToday(), -5);
			Date queryStatusDelay = org.apache.commons.lang.time.DateUtils.addMinutes(new Date(), -queryStatusDelayInteger);
			logger.info("noticeBatchNo:["+noticeBatchNo+"],queryStatusDelayInteger:["+queryStatusDelayInteger+"],queryStatusDelay:["+DateUtils.format(queryStatusDelay,"yyyy-MM-dd HH:mm:ss")+"].");
			//查询5日内，状态为处理中的放款申请单
			remittanceApplicationUuids = iRemittanceApplicationHandler.getRemittanceApplicationUuidsInOppositeProcessing(queryStartDate, limit, queryStatusDelay);
			if(CollectionUtils.isEmpty(remittanceApplicationUuids)){
				logger.info(currentTime+"#放款结果回调补漏，处理中的放款计划订单总计（0）条 !"+noticeBatchNo);
				return ;
			}
			logger.info(currentTime+"放款结果回调补漏，处理中的放款计划订单总计（"+ remittanceApplicationUuids.size() +"）条 !"+noticeBatchNo+",remittanceApplicationUuids:["+JsonUtils.toJsonString(remittanceApplicationUuids)+"].");
		
			Map<String, Object> message = remittanceTaskNoSession.callBackQueryStatusForRemittance(remittanceApplicationUuids);
			
			logger.info("noticeBatchNo:["+noticeBatchNo+"],message:["+JsonUtils.toJsonString(message)+"].");
			
		} catch (Exception e) {
			logger.error("#execRemittanceResultQuery occur error."+noticeBatchNo);
			e.printStackTrace();
		}
		long end = System.currentTimeMillis();
		logger.info("#execRemittanceResultQuery end. used ["+(end-start)+"]ms"+noticeBatchNo);
		
	}
}
