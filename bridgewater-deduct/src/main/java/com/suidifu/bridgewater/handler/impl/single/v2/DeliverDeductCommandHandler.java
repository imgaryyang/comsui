package com.suidifu.bridgewater.handler.impl.single.v2;

import com.zufangbao.gluon.api.bridgewater.deduct.model.v2.DeductPlanModel;
import com.zufangbao.gluon.api.jpmorgan.model.TradeSchedule;

import java.util.List;

/**
 * Created by zhenghangbo on 10/05/2017.
 */
public interface DeliverDeductCommandHandler {

    public void deliverDeductCommandAndUpdateStatus_NoRollback(List<TradeSchedule> tradeScheduleList, String deductApplicationUuid, String requestNo);

    public void deliverDeductCommandAndUpdateStatus(List<TradeSchedule> tradeScheduleList, String deductApplicationUuid, String requestNo, String financialContractUuid, String repaymentCodes);
}
