package com.suidifu.barclays.jobs;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.suidifu.barclays.exception.PullAuditBillException;
import com.suidifu.barclays.factory.AuditBillHandlerFactory;
import com.suidifu.barclays.handler.AuditBillHandler;
import com.suidifu.barclays.handler.JobHandler;
import com.zufangbao.sun.yunxin.entity.barclays.ChannelWorkerConfig;
import com.zufangbao.sun.yunxin.entity.barclays.ThirdPartyAuditBill;
import com.zufangbao.sun.yunxin.service.barclays.ChannelWorkerConfigService;
import com.zufangbao.sun.yunxin.service.barclays.ThirdPartyAuditBillService;

@Component("auditBillJob")
public class AuditBillJob implements JobHandler {

	private static Log logger = LogFactory.getLog(AuditBillJob.class);

	@Autowired
	ChannelWorkerConfigService channelWorkerConfigService;
	@Autowired
	ThirdPartyAuditBillService thirdPartyAuditBillService;
	
	@Override
	public void run(Map<String, Object> workingParms) {
		logger.info("start auditBillJob...");
		try {
			
			String workerUuid = workingParms.getOrDefault("workerUuid", StringUtils.EMPTY).toString();

			ChannelWorkerConfig channelWorkerConfig = channelWorkerConfigService.getChannelWorkerConfigBy(workerUuid);
			
			if(null == channelWorkerConfig) {
				logger.warn("initialization failed, cannot get channelWorkerConfig for " + workerUuid);
				return;
			}
			
			String channelIdentity = channelWorkerConfig.getChannelIdentity();
			
			AuditBillHandler auditBillHandler = AuditBillHandlerFactory.newInstance(channelIdentity);
			if(null == auditBillHandler) {
				logger.info("initialization failed, cannot create auditBillHandler for " + workerUuid);
			}
			Map<String, String> localWorkingConfig = channelWorkerConfig.getLocalWorkingConfig();
			String daysBefore = workingParms.getOrDefault("daysBefore", StringUtils.EMPTY).toString();
			if (StringUtils.isNotEmpty(daysBefore)) {
				localWorkingConfig.put("daysBefore", daysBefore);
			}
			
			List<ThirdPartyAuditBill> thirdPartyAuditBillList = auditBillHandler.execPullThirdPartyAuditBill(localWorkingConfig);
			
			thirdPartyAuditBillService.judgeAndSaveThirdPartyAuditBills(thirdPartyAuditBillList);
			
		} catch (PullAuditBillException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("end auditBillJob...");
	}

}
