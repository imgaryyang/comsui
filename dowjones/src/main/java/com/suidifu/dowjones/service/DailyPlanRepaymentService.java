package com.suidifu.dowjones.service;

import java.util.Date;

public interface DailyPlanRepaymentService {

    void executeDailyPlanRepaymentJob(String financialContractUuid, Date doingDay);

}
