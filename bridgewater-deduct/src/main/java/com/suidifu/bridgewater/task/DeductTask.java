package com.suidifu.bridgewater.task;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.suidifu.bridgewater.handler.DeductApplicationBusinessHandler;
import com.zufangbao.gluon.api.jpmorgan.model.TradeSchedule;
import com.zufangbao.sun.utils.DateUtils;

/**
 * 扣款处理相关task
 * 
 * @author zhenghangbo
 *
 */
@Component("deductTask")
public class DeductTask {
	@Autowired
	private DeductApplicationBusinessHandler deductApplicationBusinessHandler;

	private static Log logger = LogFactory.getLog(DeductTask.class);

	/**
	 * 执行款结果查询
	 */
	@Deprecated
	public void execDeductResultQuery() {

		logger.info("#execDeductResultQuery begin.");
		long start = System.currentTimeMillis();

		String currentTime = DateUtils.format(new Date(), DateUtils.LONG_DATE_FORMAT);
		try {
			List<String> deductApplicationUuidlist = deductApplicationBusinessHandler.getDeductApplicationUuidInProcessingAndSourceTypeNotRepaymentOrder();
			logger.info(currentTime + "#扣款结果查询，待查询总计（" + deductApplicationUuidlist.size() + "）条 !");
			loopHandlerQueryResult(deductApplicationUuidlist);
		} catch (Exception e) {
			logger.error("#execDeductResultQuery occur error.");
			e.printStackTrace();
		}

		long end = System.currentTimeMillis();
		logger.info("#execDeductResultQuery end. used [" + (end - start) + "]ms");
	}

	private void loopHandlerQueryResult(List<String> deductApplicationUuidlist) {
		
		
		for (String deductApplicationUuid : deductApplicationUuidlist) {
			try {
				
				List<TradeSchedule> tradeSchedules = deductApplicationBusinessHandler.handleSingleQueryResult(deductApplicationUuid);
				//重发
				if(CollectionUtils.isNotEmpty(tradeSchedules)){
				    deductApplicationBusinessHandler.processingAndUpdateDeducInfo_NoRollback(tradeSchedules,deductApplicationUuid, null);
				}
			} catch (Exception e) {
				logger.error("#扣款结果查询，查询失败，扣款申请号［"+deductApplicationUuid+"］");
				e.printStackTrace();
			}
		}
	}

}
