package com.suidifu.barclays.jobs;

import com.suidifu.barclays.factory.CashflowHandlerFactory;
import com.suidifu.barclays.handler.CashflowHandler;
import com.suidifu.barclays.handler.JobHandler;
import com.zufangbao.sun.entity.directbank.business.CashFlow;
import com.zufangbao.sun.service.CashFlowService;
import com.zufangbao.sun.yunxin.entity.barclays.ChannelWorkerConfig;
import com.zufangbao.sun.yunxin.service.barclays.ChannelWorkerConfigService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component("cashflowJob")
public class CashflowJob implements JobHandler {

	private static Log logger = LogFactory.getLog(CashflowJob.class);

	@Autowired
	private ChannelWorkerConfigService channelWorkerConfigService;
	@Autowired
	private CashFlowService cashFlowService;
	@Autowired
	private ZSetOperations <String, CashFlow> cashFlowZSetOperations;
	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	
	@Override
	public void run(Map<String, Object> workingParms) {
		logger.info("start cashflowJob...");
		
		try {
			String workerUuid = workingParms.getOrDefault("workerUuid", StringUtils.EMPTY).toString();

			ChannelWorkerConfig channelWorkerConfig = channelWorkerConfigService.getChannelWorkerConfigBy(workerUuid);
			
			if(null == channelWorkerConfig) {
				logger.warn("initialization failed, cannot get channelWorkerConfig for " + workerUuid);
				return;
			}
			
			String channelIdentity = channelWorkerConfig.getChannelIdentity();
			CashflowHandler cashflowHandler = CashflowHandlerFactory.newInstance(channelIdentity);
			if(null == cashflowHandler) {
				logger.info("initialization failed, cannot create cashflowHandler for " + workerUuid);
			}

			List<CashFlow> cashflowList = cashflowHandler.execPullCashflow(channelWorkerConfig);
			if(CollectionUtils.isEmpty(cashflowList)) {
				return;
			}

			List<CashFlow> uniqueCashFlowList = cashFlowService.judgeAndSaveCashflows(cashflowList);

			Map<String, String> localWorkingParms = channelWorkerConfig.getLocalWorkingConfig();
			String queryAccountNo = localWorkingParms.getOrDefault("channelAccountNo", StringUtils.EMPTY);
			cashFlowService.saveInRedis(uniqueCashFlowList, cashFlowZSetOperations, stringRedisTemplate,
					queryAccountNo);
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		logger.info("end cashflowJob...");
	}

}
