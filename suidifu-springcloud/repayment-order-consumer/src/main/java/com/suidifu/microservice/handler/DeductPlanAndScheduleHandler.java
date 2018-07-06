/**
 *
 */
package com.suidifu.microservice.handler;

import com.suidifu.microservice.model.DeductDataContext;
import com.suidifu.swift.notifyserver.notifyserver.NotifyApplication;
import com.suidifu.swift.notifyserver.notifyserver.NotifyJobServer;

/**
 * @author wukai
 */
public interface DeductPlanAndScheduleHandler {
    void processDeductPlanModelAndTradeSchedule(NotifyJobServer notifyJobServer,
                                                DeductDataContext deductDataContext,
                                                String groupName,
                                                int deductPlanModelListSize);

    void prepareDeductPlanModelAndTradeSchedule(DeductDataContext deductDataContext);

    NotifyApplication sendDeductPlanModelAndTradeScheduleToDeduct(NotifyJobServer notifyJobServer,
                                                                  DeductDataContext deductDataContext,
                                                                  String failedMsg,
                                                                  int deductPlanModeSize,
                                                                  boolean isSuccess,
                                                                  String groupName);
}