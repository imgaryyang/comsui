package com.suidifu.bridgewater.handler.impl.single.v2;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.suidifu.bridgewater.handler.DeductApplicationBusinessHandler;
import com.zufangbao.sun.api.model.deduct.DeductApplicationExecutionStatus;
import com.zufangbao.sun.api.model.deduct.DeductApplicationReceiveStatus;
import com.zufangbao.sun.yunxin.entity.remittance.TransactionRecipient;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.zufangbao.gluon.api.bridgewater.deduct.model.v2.DeductPlanModel;
import com.zufangbao.gluon.api.jpmorgan.model.TradeSchedule;
import com.zufangbao.gluon.exception.ApiException;
import com.zufangbao.gluon.logspec.GloableLogSpec;
import com.zufangbao.sun.yunxin.api.deduct.service.DeductApplicationService;
import com.zufangbao.sun.yunxin.handler.DeductPlanCoreOperationHandler;

/**
 * Created by zhenghangbo on 10/05/2017.
 */

@Component("standardDeliverDeductCommandHandler")
public class StandardDeliverDeductCommandHandlerImpl extends AbstractDeliverCommandProcessor implements DeliverDeductCommandHandler {

    private static final  boolean SEND_TO_OPPOSITE_SUCCESS =true;
    private static final  boolean SEND_TO_OPPOSITE_FAIL =false;

    private static final Log logger = LogFactory.getLog(StandardDeliverDeductCommandHandlerImpl.class);

    @Autowired
    private DeductApplicationService deductApplicationService;
    
    @Autowired 
    private DeductPlanCoreOperationHandler deductPlanCoreOperationHandler;

    @Autowired
    DeductApplicationBusinessHandler deductApplicationBusinessHandler;

    @Override
    public void deliverDeductCommandAndUpdateStatus_NoRollback(List<TradeSchedule> tradeScheduleList, String deductApplicationUuid, String requestNo){


        String localKeyWord="BRIDGEWATER[requestNo="+requestNo+";deductApplicationUuid="+deductApplicationUuid+
                "]";
        String remoteKeyWord="==SENTTO==>>JPMORGAN[";
        //发送到对端
        try {
            deliverDeductCommand_NoRollback(tradeScheduleList,deductApplicationUuid,requestNo,localKeyWord,remoteKeyWord);

        }  catch(ApiException  e){
        	
        	logger.error("#deliverDeductCommandAndUpdateStatus_NoRollback# occur exception with stack trace["+ExceptionUtils.getFullStackTrace(e)+"]");
        	
            deductApplicationService.updateDeductInfoAfterSendToRemote(deductApplicationUuid,tradeScheduleList.get(0).getOutlierTransactionUuid(), e.getCode()+","+e.getMsg(),SEND_TO_OPPOSITE_FAIL);
            throw e;
        }

        try {
            deductApplicationService.updateDeductInfoAfterSendToRemote(deductApplicationUuid,tradeScheduleList.get(0).getOutlierTransactionUuid(), "",SEND_TO_OPPOSITE_SUCCESS);
        } catch (Exception e) {
            logger.info(GloableLogSpec.AuditLogHeaderSpec() + GloableLogSpec.bridgewater_deduct_standard_function_point.SEND_TRADE_SCHEDULES_TO_JPMORGAN+localKeyWord+ "ERR:[落盘时状态更新为对端处理中失败]");
            e.printStackTrace();
        }
        logger.info(GloableLogSpec.AuditLogHeaderSpec() +GloableLogSpec.bridgewater_deduct_standard_function_point.SEND_TRADE_SCHEDULES_TO_JPMORGAN+localKeyWord+"扣款请求受理成功");

    }

	@Override
	public void deliverDeductCommandAndUpdateStatus(List<TradeSchedule> tradeScheduleList, String deductApplicationUuid,
			String requestNo, String financialContractUuid, String repaymentCodes) {
		 String localKeyWord="BRIDGEWATER[requestNo="+requestNo+";deductApplicationUuid="+deductApplicationUuid+
	                "]";
	        String remoteKeyWord="==SENTTO==>>JPMORGAN[";
	        
	        //发送到对端
            deliverDeductCommand_NoRollback(tradeScheduleList,deductApplicationUuid,requestNo,localKeyWord,remoteKeyWord);


	        logger.info(GloableLogSpec.AuditLogHeaderSpec() +GloableLogSpec.bridgewater_deduct_standard_function_point.SEND_TRADE_SCHEDULES_TO_JPMORGAN+localKeyWord+"扣款请求受理成功");
	}
}
