package com.suidifu.bridgewater.handler.common.v2;

import com.zufangbao.gluon.api.jpmorgan.model.TradeSchedule;
import java.util.List;

/**
 * Created by zhenghangbo on 23/05/2017.
 */
public interface ISendHttpToJPMorganHandler {

    public void processingAndUpdateRemittanceInfo_NoRollback(List<TradeSchedule> tradeSchedules,
        String remittanceApplicationUuid, String requestNo);
}
