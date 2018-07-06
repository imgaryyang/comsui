package com.suidifu.bridgewater.task.v2;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.suidifu.bridgewater.handler.DeductApplicationBusinessHandler;
import com.suidifu.bridgewater.model.v2.BasicTask;
import com.zufangbao.sun.utils.DateUtils;

@Component("deductQueryTask")
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class DeductQueryTask extends BasicTask {

	@Autowired
	private DeductApplicationBusinessHandler deductApplicationBusinessHandler;
	
	private static Log logger = LogFactory.getLog(DeductQueryTask.class);
	
	
	
	/**
	 * 执行扣款结果查询
	 */
	public void run(){
		logger.info("# execDeductResultQuery begin.");
		long start = System.currentTimeMillis();

		String currentTime = DateUtils.format(new Date(), DateUtils.LONG_DATE_FORMAT);
		try {

			List<String> deductApplicationUuidlist = deductApplicationBusinessHandler.getDeductApplicationUuidInProcessing();
			logger.info(currentTime + "#扣款结果查询，待查询总计（" + deductApplicationUuidlist.size() + "）条 !");
			loopHandlerQueryResult(deductApplicationUuidlist);

			long end = System.currentTimeMillis();
			logger.info("# execDeductResultQuery end use:" + (end-start) + "ms");
		} catch (Exception e) {
			logger.error("# execDeductResultQuery occur error.");
			e.printStackTrace();
		}

		long end = System.currentTimeMillis();
		logger.info("# execDeductResultQuery end. used [" + (end - start) + "]ms");
		
	}
	
	private void loopHandlerQueryResult(List<String> deductApplicationUuidlist) {
		
		
		for (String deductApplicationUuid : deductApplicationUuidlist) {
			try {

				deductApplicationBusinessHandler.handleSingleQueryResultForProducts(deductApplicationUuid);

			} catch (Exception e) {
				logger.error("#扣款结果查询，查询失败，扣款申请号［"+deductApplicationUuid+"］");
				e.printStackTrace();
			}
		}
	}
	
}
