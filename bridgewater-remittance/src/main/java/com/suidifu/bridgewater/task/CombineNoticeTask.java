package com.suidifu.bridgewater.task;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.suidifu.bridgewater.api.model.BasicTask;
import com.suidifu.bridgewater.handler.IRemittanceAsyncNotifyHandler;
import com.suidifu.bridgewater.handler.RemittanceNotifyHandler;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.yunxin.entity.model.SystemTraceLog;
import com.zufangbao.sun.zhonghang.v2.ZhonghangResponseMapSpec.EVENT_NAME;
import com.zufangbao.sun.zhonghang.v2.ZhonghangResponseMapSpec.SYSTEM_NAME;

/**
 * 綜合yunxin和zhonghang的回調外部task
 *
 * @author wsh
 */
@Component("combineNoticeTask")
public class CombineNoticeTask extends BasicTask {

	private static Log logger = LogFactory.getLog(CombineNoticeTask.class);

	@Value("#{config['notifyserver.notifyType']}")
	private String notifyType;

	@Autowired
	private RemittanceNotifyHandler remittanceNotifyHandler;
	
	@Override
	public void run() {
		long start = System.currentTimeMillis();
		String noticeBatchNo = "noticeBatchNo:[" + UUID.randomUUID().toString() + "]";

		logger.info("#execPushApplicationsToOutlier begin." + noticeBatchNo);

		int limit = getIntegetFromWorkParamOrDefault(RemittanceTaskSpec.PARAM_LIMIT_SIZE,
				RemittanceTaskSpec.DEFAULT_LIMIT_SIZE);
		long sleepMillis = getLongFromWorkParamOrDefault(RemittanceTaskSpec.PARAM_SLEEP_MILLIS,
				RemittanceTaskSpec.DEFAULT_SLEEP_MILLIS);
		int day = getIntegetFromWorkParamOrDefault(RemittanceTaskSpec.PARAM_DAY_SIZE,
				RemittanceTaskSpec.DEFAULT_DAY_SIZE);
		int notifyOutlierDelayInteger = getIntegetFromWorkParamOrDefault(RemittanceTaskSpec.PARAM_QUERY_STATUS_DELAY,RemittanceTaskSpec.DEFAULT_QUERY_STATUS_DELAY);

		try {
			Date queryStartDate = DateUtils.addDays(DateUtils.getToday(), -day);
			Date notifyOutlierDelay = org.apache.commons.lang.time.DateUtils.addMinutes(new Date(), -notifyOutlierDelayInteger);

			// 根据配置得到不同的notify的处理类(zhonghang和yunxin)
			IRemittanceAsyncNotifyHandler iRemittanceAsyncNotifyHandler = IRemittanceAsyncNotifyHandler
					.getNotifyHandler(notifyType);
			List<String> remittanceApplicationUuidList = iRemittanceAsyncNotifyHandler
					.getWaitingNoticeRemittanceApplicationBy(limit, queryStartDate, notifyOutlierDelay);

			logger.info("#待回调外部总计（" + remittanceApplicationUuidList.size() + "）条 !" + noticeBatchNo);

			for (String remittanceApplicationUuid : remittanceApplicationUuidList) {

				try {
					String eventKey = "checkRequestNo:[]&applicationUuid:" + remittanceApplicationUuid;

					SystemTraceLog systemTraceLog = new SystemTraceLog(EVENT_NAME.REMITTANCE_SEND_TO_OUTLINR_BY_TASK,
							eventKey, noticeBatchNo, null, SystemTraceLog.INFO, null, SYSTEM_NAME.REMITTANCE_SYSTEM,
							SYSTEM_NAME.OUTLIER_SYSTEM);
					logger.info(systemTraceLog);

					remittanceNotifyHandler.processingRemittanceCallback(remittanceApplicationUuid);
					
					Thread.sleep(sleepMillis);
				} catch (Exception e) {
					logger.error("#放款结果回调，remittanceApplicationUuid:[" + remittanceApplicationUuid + "],noticeBatchNo:["
							+ noticeBatchNo + "].");
				}
			}

		} catch (Exception e) {
			logger.error("#放款结果回调，回调失败." + noticeBatchNo);
			e.printStackTrace();
		}

		long end = System.currentTimeMillis();
		logger.info("#execRemittanceResultQuery end. used [" + (end - start) + "]ms" + noticeBatchNo);
	}

}
