package com.zufangbao.earth.task;

import com.zufangbao.earth.handler.impl.trade.FileAnalyzaFactory;
import com.zufangbao.sun.entity.trade.ExternalTradeBatch;
import com.zufangbao.sun.handler.trade.ExternalTradeBatchHandler;
import com.zufangbao.sun.service.trade.ExternalTradeBatchService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 外部交易（回盘文件）审计TASK
 * @author zhanghongbing
 *
 */
@Component("externalTradeAuditTask")
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ExternalTradeAuditTask extends BasicTask{

//	private final static String PARAM_FINANCIAL_CONTRACT_UUID = "financialContractUuid";
	private final static String PARAM_QUERY_PAGE_SIZE = "pageSize";
	
	private final static String FTP_FILE_BACK_STORAGE_FLODER = "filePath";
	
	private static Log logger = LogFactory.getLog(ExternalTradeAuditTask.class);
	
	private final static int DEFAULT_QUERY_PAGE_SIZE = 2;
	
	
	@Autowired
	private FileAnalyzaFactory fileAnalyzaFactory;
	
	@Autowired
	private ExternalTradeBatchService externalTradeBatchService;
	
	@Autowired
	private ExternalTradeBatchHandler externalTradeBatchHandler;
	
	@Override
	public void run() {
//		String financialContractUuid = getWorkParam(PARAM_FINANCIAL_CONTRACT_UUID);
		int queryPageSize = DEFAULT_QUERY_PAGE_SIZE;
		String filePath=getWorkParam(FTP_FILE_BACK_STORAGE_FLODER);
		try {
			queryPageSize = Integer.valueOf(getWorkParam(PARAM_QUERY_PAGE_SIZE));
		} catch (NumberFormatException e1) {
		}
		
		try {
			List<String> externalTradeBatchUuids = externalTradeBatchService.getNeedTradeAuditBatchUuid(queryPageSize);
			logger.info("#taskId［"+getTaskId()+"］，回盘文件交易验真，待验真总计（"+ externalTradeBatchUuids.size() +"）条，externalTradeBatchUuids［"+ externalTradeBatchUuids +"］!");
			loopAuditExternalTradeBatch(externalTradeBatchUuids,filePath);
		} catch (Exception e) {
			logger.error("#回盘文件交易验真发生异常！");
			e.printStackTrace();
		}
	}
	
	private void loopAuditExternalTradeBatch(List<String> externalTradeBatchUuids,String filePath ) {
		for (String externalTradeBatchUuid : externalTradeBatchUuids) {
			try {
				long start = System.currentTimeMillis();
				if(externalTradeBatchHandler.auditSingleExternalTradeBatch(externalTradeBatchUuid)){
					ExternalTradeBatch externalTradeBatch= externalTradeBatchService.getUniqueExternalTradeBatchBy(externalTradeBatchUuid);
					String externalBatchNo =externalTradeBatch.getExternalBatchNo();
					String contractNo = externalTradeBatch.getFinancialProductCode();
					fileAnalyzaFactory.doReturnFtpFile(externalTradeBatchUuid, filePath,contractNo,externalBatchNo);
					logger.info("#returnExternalTradeBatchToFtp success, batchUuid["+externalTradeBatchUuid+"], usedTime["+(System.currentTimeMillis() - start)+"].");
				}
				
				logger.info("#singleAuditExternalTradeBatch success, batchUuid["+externalTradeBatchUuid+"], usedTime["+(System.currentTimeMillis() - start)+"].");
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("#singleAuditExternalTradeBatch occur error, batchUuid["+externalTradeBatchUuid+"].");
			}
		}
	}
	
}
