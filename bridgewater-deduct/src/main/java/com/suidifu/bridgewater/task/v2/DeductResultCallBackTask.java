package com.suidifu.bridgewater.task.v2;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.suidifu.bridgewater.deduct.notify.handler.batch.v2.DeductNotifyJobServer;
import com.suidifu.bridgewater.handler.DeductApplicationBusinessHandler;
import com.suidifu.bridgewater.handler.single.v2.ZhongHangDeductApplicationBusinessHandler;
import com.suidifu.bridgewater.model.v2.BasicTask;
import com.zufangbao.gluon.logspec.GloableLogSpec;
import com.zufangbao.gluon.logspec.GloableLogSpec.bridgewater_deduct_function_point;
import com.zufangbao.sun.service.FinancialContractConfigurationService;
import com.zufangbao.sun.utils.DateUtils;


@Component("deductResultCallBackTask")
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class DeductResultCallBackTask extends BasicTask {

	private static Log logger = LogFactory.getLog(DeductResultCallBackTask.class);
	
	private final static String PARAM_FINANCIAL_CONTRACT_UUID = "financialContractUuid";
	private final static String PARAM_NOTICE_PAGE_SIZE = "noticePageSize";
	private final static int DEFAULT_NOTICE_PAGE_SIZE = 30;
	
	
	@Autowired
	private DeductApplicationBusinessHandler deductApplicationBusinessHandler;
	
	@Autowired
	private FinancialContractConfigurationService financialContractConfigurationService;
	
	
	@Autowired
	private ZhongHangDeductApplicationBusinessHandler zhongHangDeductApplicationBusinessHandler;
	
	@Autowired
	private DeductNotifyJobServer deductSingleCallBackNotifyJobServer;
	
	@Value("#{config['notifyserver.groupCacheJobQueueMap_group2']}")
	private String deductSingleCallBackNotifyJobServerGroupName;
	
	/**
	 * 执行扣款结果回调
	 */
	@Deprecated
	public void run(){
		logger.info("# execDeductResultCallBack begin");
		
		long start = System.currentTimeMillis();
		
		String currentTime = DateUtils.format(new Date(), DateUtils.LONG_DATE_FORMAT);
		
		try {
			
			Date queryStartDate = DateUtils.addDays(DateUtils.getToday(), -5);
			
			List<String> deductApplicationUuids = deductApplicationBusinessHandler.getWaitingNoticeDeductApplication(DEFAULT_NOTICE_PAGE_SIZE,queryStartDate);

			//查询5日内，需要回调的扣款申请单
			logger.info(GloableLogSpec.AuditLogHeaderSpec()+ bridgewater_deduct_function_point.SEND_RESULT_TO_OUTLIER_SYSTEM+currentTime+"#扣款结果回调，待回调总计（"+ deductApplicationUuids.size() +"）条 !");
			
			loopNotifydeductResult(deductApplicationUuids);

			long end = System.currentTimeMillis();
			logger.info("# execDeductResultCallBack end use:" + (end-start) + "ms");
		} catch (Exception e) {
			logger.error("# execDeductResultCallBack occur error.");
			e.printStackTrace();
		}
		long end = System.currentTimeMillis();
		logger.info("# execDeductResultCallBack end   used ["+(end-start)+"]ms");
	}

	private void loopNotifydeductResult(List<String> deductApplicationUuids){
		for (String deductApplicationUuid : deductApplicationUuids) {
			
			try {

				deductApplicationBusinessHandler.execSingleNotifyFordeductApplication(deductApplicationUuid);
			} catch (Exception e) {
				
				logger.error("#扣款结果回调，回调失败，扣款申请号［"+deductApplicationUuid+"］！");
				
				e.printStackTrace();
			}
		}
	}
	
}
