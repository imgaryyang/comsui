package com.zufangbao.earth.yunxin.task;

import com.suidifu.hathaway.job.Priority;
import com.zufangbao.sun.service.ThirdPartVoucherCommandLogService;
import com.zufangbao.sun.yunxin.entity.thirdPartVoucher.ThirdPartyVoucherCommandLog;
import com.zufangbao.wellsfargo.silverpool.cashauditing.handler.InstitutinReconciliation.InstitutionReconciliationParameterNameSpace;
import com.zufangbao.wellsfargo.silverpool.cashauditing.handler.InstitutinReconciliation.mq.InstitutionReconciliationTimeOutHandler;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class InstitutionReconciliationTask {

	@Autowired
	private ThirdPartVoucherCommandLogService thirdPartVoucherCommandLogService;
	
	
	@Autowired
	@Qualifier("institutionReconciliationTimeOutHandlerProxy")
	private InstitutionReconciliationTimeOutHandler institutionReconciliationTimeOutHandler;
	
	private static Log logger = LogFactory.getLog(InstitutionReconciliationTask.class); 
	
	
	public  void  handleTimeOutThirdPartPayVoucherCommand(){
		
		List<ThirdPartyVoucherCommandLog> thirdPartVoucherCommands  = thirdPartVoucherCommandLogService.queryTimeOutThirdPartyVoucherCommandLog();
		
		for(ThirdPartyVoucherCommandLog commandLog:thirdPartVoucherCommands){

			if(InstitutionReconciliationParameterNameSpace.NotTimeOutFinancialContractCodeList.contains(commandLog.getFinancialContractNo()) == false){
				sendMessageToReconciliationQueue(commandLog,institutionReconciliationTimeOutHandler);
			}
		}
		
	}
	
	private void sendMessageToReconciliationQueue(ThirdPartyVoucherCommandLog commandLog,InstitutionReconciliationTimeOutHandler reconciliation){
		try{
			String tradeUuid=commandLog.getTradeUuid();
			String voucherUuid = commandLog.getVoucherNo();
			reconciliation.updateThirdPartPayVoucherTimeOut(tradeUuid,  voucherUuid, Priority.Medium.getPriority());
			logger.info("#发送超时数据到机构对账队列处理，voucherUuid["+tradeUuid+"]");
			}catch(Exception e){
			logger.error("#发送超时数据到机构对账队列处理失败，voucherUuid["+commandLog.getVoucherNo()+"],with exception stack trace["+ExceptionUtils.getFullStackTrace(e)+"]");
		}

	}

}
