package com.suidifu.dowjones.service;

import java.util.Date;

public interface DailyActualRepaymentService {

    void executeDailyActualRepaymentJob(String financialContractUuid, Date doingDay);

}
