package com.suidifu.dowjones.service;

import java.util.Date;

public interface DailyGuaranteeService {

    void executeDailyGuaranteeJob(String financialContractUuid, Date doingDay);

}
