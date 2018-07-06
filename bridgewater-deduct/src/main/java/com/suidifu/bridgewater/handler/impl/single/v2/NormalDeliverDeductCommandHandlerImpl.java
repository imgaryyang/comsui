package com.suidifu.bridgewater.handler.impl.single.v2;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zufangbao.gluon.api.bridgewater.deduct.model.v2.DeductPlanModel;
import com.zufangbao.gluon.api.jpmorgan.model.TradeSchedule;
import com.zufangbao.gluon.exception.ApiException;
import com.zufangbao.gluon.logspec.GloableLogSpec;
import com.zufangbao.gluon.spec.earth.v3.ApiResponseCode;
import com.zufangbao.sun.yunxin.api.deduct.service.DeductApplicationDetailService;
import com.zufangbao.sun.yunxin.api.deduct.service.DeductApplicationService;
import com.zufangbao.sun.yunxin.entity.DeductionStatus;
import com.zufangbao.sun.yunxin.handler.RepaymentPlanHandler;
import com.zufangbao.sun.yunxin.service.RepaymentPlanService;

/**
 * Created by zhenghangbo on 10/05/2017.
 */

@Component("normalDeliverDeductCommandHandlerImpl")
public class NormalDeliverDeductCommandHandlerImpl extends AbstractDeliverCommandProcessor implements DeliverDeductCommandHandler {


    private static final  boolean SEND_TO_OPPOSITE_SUCCESS =true;
    private static final  boolean SEND_TO_OPPOSITE_FAIL =false;

    private static final Log logger = LogFactory.getLog(NormalDeliverDeductCommandHandlerImpl.class);

    @Autowired
    private RepaymentPlanService repaymentPlanService;

    @Autowired
    private RepaymentPlanHandler repaymentPlanHandler;

    @Autowired
    private DeductApplicationDetailService deductApplicationDetailService;

    @Autowired
    private DeductApplicationService deductApplicationService;



    @Override
    public void deliverDeductCommandAndUpdateStatus_NoRollback(List<TradeSchedule> tradeScheduleList, String deductApplicationUuid, String requestNo){


        String localKeyWord="BRIDGEWATER[requestNo="+requestNo+";deductApplicationUuid="+deductApplicationUuid+
                "]";
        String remoteKeyWord="==SENTTO==>>JPMORGAN[";

        //发送到对端
        try {
            deliverDeductCommand_NoRollback(tradeScheduleList,deductApplicationUuid,requestNo,localKeyWord,remoteKeyWord);
        }  catch(ApiException e){
            updateDeductInfoAndAssetStatusAfterSendToRemote(deductApplicationUuid,tradeScheduleList,SEND_TO_OPPOSITE_FAIL,e.getMsg());
            throw e;
        }

        try {
            updateDeductInfoAndAssetStatusAfterSendToRemote(deductApplicationUuid,tradeScheduleList,SEND_TO_OPPOSITE_SUCCESS,"");
        } catch (Exception e) {
            logger.info(GloableLogSpec.AuditLogHeaderSpec() + GloableLogSpec.bridgewater_deduct_function_point.SEND_TRADE_SCHEDULES_TO_JPMORGAN+localKeyWord+ "ERR:[落盘时状态更新为对端处理中失败]");
            e.printStackTrace();
        }
        logger.info(GloableLogSpec.AuditLogHeaderSpec() +GloableLogSpec.bridgewater_deduct_function_point.SEND_TRADE_SCHEDULES_TO_JPMORGAN+localKeyWord+"扣款请求受理成功");

    }

    @Override
    public void deliverDeductCommandAndUpdateStatus(List<TradeSchedule> tradeScheduleList, String deductApplicationUuid, String requestNo, String financialContractUuid, String repaymentCodes) {

    }

    public void updateDeductInfoAndAssetStatusAfterSendToRemote(String deductApplicationUuid, List<TradeSchedule> tradeSchedules,boolean isSuccess,String remark){

        deductApplicationService.updateDeductInfoAfterSendToRemote(deductApplicationUuid,tradeSchedules.get(0).getOutlierTransactionUuid(),remark,isSuccess);

        //更新还款计划扣款状态为对端处理中
        DeductionStatus deductionStatus = isSuccess ==true?DeductionStatus.OPPOSITE_PROCESSING:DeductionStatus.FAIL;
        repaymentPlanHandler.updateAssetSetDeductionStatus(deductApplicationUuid, deductionStatus);
        //解锁还款计划
        if(isSuccess == false){
            if(deductApplicationService.unlockRepaymentPlans(deductApplicationUuid) ==false){
                throw new ApiException(ApiResponseCode.DEDUCT_CONCURRENT_ERROR);
            }
        }
    }


}
