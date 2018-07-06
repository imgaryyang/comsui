package com.suidifu.barclays.jobs;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.suidifu.barclays.handler.JobHandler;
import com.zufangbao.sun.yunxin.entity.barclays.ChannelWorkerConfig;
import com.zufangbao.sun.yunxin.service.barclays.ChannelWorkerConfigService;

@Component("dailyResetJob")
public class DailyResetJob implements JobHandler {

	private static Log logger = LogFactory.getLog(DailyResetJob.class);

    /**是否终止查询**/
    private static final String TERMINAL_SIGNAL = "TERMINAL_SIGNAL";

	@Autowired
	ChannelWorkerConfigService channelWorkerConfigService;
	
	@Override
	public void run(Map<String, Object> workingParms) {
		
		logger.info("start dailyResetJob...");
		
		try {
			
			String workerUuid = workingParms.getOrDefault("workerUuid", StringUtils.EMPTY).toString();
			logger.info("begin to reset worker:" + workerUuid);
			
			ChannelWorkerConfig channelWorkerConfig = channelWorkerConfigService.getChannelWorkerConfigBy(workerUuid);
			Map<String, String> localWorkingConfig = channelWorkerConfig.getLocalWorkingConfig();
			localWorkingConfig.put("nextPageNo", 1 + "");
			localWorkingConfig.put("pageNum", 1 + "");//广银联拉取交易记录分页
			localWorkingConfig.put(TERMINAL_SIGNAL, "false");
			localWorkingConfig.remove("daysBefore");
			channelWorkerConfigService.updateLocalWorkingConfig(channelWorkerConfig.getWorkerUuid(), localWorkingConfig);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		logger.info("end dailyResetJob...");
		
	}

}
