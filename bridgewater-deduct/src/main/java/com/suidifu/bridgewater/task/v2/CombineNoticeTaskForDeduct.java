package com.suidifu.bridgewater.task.v2;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.suidifu.bridgewater.task.DeductTaskSpec;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.suidifu.bridgewater.handler.IDeductAsyncNotifyHandler;
import com.suidifu.bridgewater.model.v2.BasicTask;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.yunxin.entity.model.SystemTraceLog;
import com.zufangbao.sun.zhonghang.v2.ZhonghangResponseMapSpec.EVENT_NAME;
import com.zufangbao.sun.zhonghang.v2.ZhonghangResponseMapSpec.SYSTEM_NAME;

@Component("combineNoticeTaskForDeduct")
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CombineNoticeTaskForDeduct extends BasicTask {
	
	private static Log logger = LogFactory.getLog(CombineNoticeTaskForDeduct.class);
	
	@Value("#{config['deduct.notifyType']}")
	private String notifyType;
	
	
	
	public void run(){
		
		long start = System.currentTimeMillis();
		String noticeBatchNo = "noticeBatchNo:[" + UUID.randomUUID().toString() + "]";

		logger.info("#execPushApplicationsToOutlier begin." + noticeBatchNo);

		int limit = getIntegetFromWorkParamOrDefault(DeductTaskSpec.PARAM_LIMIT_SIZE,
				DeductTaskSpec.DEFAULT_LIMIT_SIZE);
		long sleepMillis = getLongFromWorkParamOrDefault(DeductTaskSpec.PARAM_SLEEP_MILLIS,
				DeductTaskSpec.DEFAULT_SLEEP_MILLIS);
		int queryStartDateInt = getIntegetFromWorkParamOrDefault(DeductTaskSpec.PARAM_QUERY_START_DATE,
				DeductTaskSpec.DEFAULT_QUERY_START_DATE);
		
		try {
			IDeductAsyncNotifyHandler iDeductAsyncNotifyHandler = IDeductAsyncNotifyHandler
					.getNotifyHandler(notifyType);
			List<String> deductApplicationUuidList = iDeductAsyncNotifyHandler
					.getWaitingNoticedeductApplicationBy(limit, getQueryStartDate(queryStartDateInt));

			logger.info("#待回调外部总计（" + deductApplicationUuidList.size() + "）条 !" + noticeBatchNo);

			for (String deductApplicationUuid : deductApplicationUuidList) {

				try {
					String eventKey = "deductApplicationUuid:" + deductApplicationUuid;

					SystemTraceLog systemTraceLog = new SystemTraceLog(EVENT_NAME.DEDUCT_SEND_TO_OUTLINR_BY_TASK,
							eventKey, "#扣款结果回调开始"+noticeBatchNo, null, SystemTraceLog.INFO, null, SYSTEM_NAME.DEDUCT,
							SYSTEM_NAME.OUTLIER_SYSTEM);
					logger.info(systemTraceLog);

					iDeductAsyncNotifyHandler.processingdeductCallback(deductApplicationUuid);

					Thread.sleep(sleepMillis);
				} catch (Exception e) {
					e.printStackTrace();
					logger.error("#扣款结果回调异常，deductApplicationUuid:[" + deductApplicationUuid + "],noticeBatchNo:["
							+ noticeBatchNo + "].");
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#扣款结果回调，回调失败." + noticeBatchNo);
		}

		long end = System.currentTimeMillis();
		logger.info("#execDeductResultQuery end. used [" + (end - start) + "]ms" + noticeBatchNo);
		
		
	}
	
	private Date getQueryStartDate(int queryStartDateInt) {
		Date queryStartDate = DateUtils.addDays(DateUtils.getToday(), -queryStartDateInt);
		return queryStartDate;
	}

}
