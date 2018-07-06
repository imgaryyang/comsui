package com.suidifu.barclays.jobs;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.suidifu.barclays.factory.TransactionRecordHandlerFactory;
import com.suidifu.barclays.handler.JobHandler;
import com.suidifu.barclays.handler.TransactionRecordHandler;
import com.zufangbao.sun.yunxin.entity.barclays.ChannelWorkerConfig;
import com.zufangbao.sun.yunxin.entity.barclays.ThirdPartyTransactionRecord;
import com.zufangbao.sun.yunxin.service.barclays.ChannelWorkerConfigService;
import com.zufangbao.sun.yunxin.service.barclays.ThirdPartyTransactionRecordService;

@Component("transactionRecordJob")
public class TransactionRecordJob implements JobHandler {

	private static Log logger = LogFactory.getLog(TransactionRecordJob.class);

	@Autowired
	ChannelWorkerConfigService channelWorkerConfigService;
	@Autowired
	ThirdPartyTransactionRecordService thirdPartyTransactionRecordService;

	@Override
	public void run(Map<String, Object> workingParms) {

		logger.info("start transactionRecordJob...");
		
		try {
			
			String workerUuid = workingParms.getOrDefault("workerUuid", StringUtils.EMPTY).toString();

			ChannelWorkerConfig channelWorkerConfig = channelWorkerConfigService.getChannelWorkerConfigBy(workerUuid);
			
			if(null == channelWorkerConfig) {
				logger.warn("initialization failed, cannot get channelWorkerConfig for " + workerUuid);
				return;
			}
			
			String channelIdentity = channelWorkerConfig.getChannelIdentity();
			TransactionRecordHandler transactionRecordHandler = TransactionRecordHandlerFactory.newInstance(channelIdentity);
			
			if(null == transactionRecordHandler) {
				logger.info("initialization failed, cannot create transactionRecordHandler for " + workerUuid);
			}
			Map<String, String> localWorkingConfig = channelWorkerConfig.getLocalWorkingConfig();
			String daysBefore = workingParms.getOrDefault("daysBefore", StringUtils.EMPTY).toString();
			String pageNum = workingParms.getOrDefault("pageNum", StringUtils.EMPTY).toString();
			if (StringUtils.isNotEmpty(daysBefore)) {
				localWorkingConfig.put("daysBefore", daysBefore);
			}
			if (StringUtils.isNotEmpty(pageNum)) {
				localWorkingConfig.put("pageNum", pageNum);
			}
			channelWorkerConfig.setLocalWorkingConfig(JSON.toJSONString(localWorkingConfig));
			List<ThirdPartyTransactionRecord> thirdPartyTransactionRecordList = transactionRecordHandler.execPullThirdPartyTransactionRecord(channelWorkerConfig);
			
			if(CollectionUtils.isEmpty(thirdPartyTransactionRecordList)) {
				return;
			}
			
			thirdPartyTransactionRecordService.judgeAndSaveTransactionRecord(thirdPartyTransactionRecordList);

			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		logger.info("end transactionRecordJob...");
	}

}
